package io.github.glailton.favdish.ui.dish.details

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import dagger.hilt.android.AndroidEntryPoint
import io.github.glailton.favdish.R
import io.github.glailton.favdish.data.entities.FavDish
import io.github.glailton.favdish.databinding.FragmentDishDetailsBinding
import io.github.glailton.favdish.ui.extensions.loadImage
import io.github.glailton.favdish.ui.extensions.toast
import io.github.glailton.favdish.ui.utils.Constants
import timber.log.Timber
import java.io.IOException

@AndroidEntryPoint
class DishDetailsFragment : Fragment() {

    private val dishDetailsViewModel: DishDetailsViewModel by viewModels()
    private var _binding: FragmentDishDetailsBinding? = null

    private val binding get() = _binding!!

    private var favDishDetails: FavDish? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDishDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: DishDetailsFragmentArgs by navArgs()
        favDishDetails = args.dishDetails

        args.let {
            try {
                // Load the dish image in the ImageView.
                Glide.with(requireActivity())
                    .load(it.dishDetails.image)
                    .centerCrop()
                        //it is not working
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            @Nullable e: GlideException?,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            // log exception
                            Timber.e("Error loading image")
                            return false // important to return false so the error placeholder can be placed
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any?,
                            target: com.bumptech.glide.request.target.Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {

                            Palette.from(resource.toBitmap())
                                .generate { palette ->
                                    val intColor = palette?.vibrantSwatch?.rgb ?: 0
                                    binding.rlDishDetailMain.setBackgroundColor(intColor)
                                }
                            return false
                        }
                    })
                    .into(binding.ivDishImage)
                binding.ivDishImage.loadImage(it.dishDetails.image)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            binding.tvTitle.text = it.dishDetails.title
            binding.tvType.text =
                it.dishDetails.type.replaceFirstChar(Char::titlecase) // Used to make first letter capital
            binding.tvCategory.text = it.dishDetails.category
            binding.tvIngredients.text = it.dishDetails.ingredients

            // The instruction or you can say the Cooking direction text is in the HTML format so we
            // will you the fromHtml to populate it in the TextView.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.tvCookingDirection.text = Html.fromHtml(
                    it.dishDetails.directionsToCook,
                    Html.FROM_HTML_MODE_COMPACT
                )
            } else {
                @Suppress("DEPRECATION")
                binding.tvCookingDirection.text = Html.fromHtml(it.dishDetails.directionsToCook)
            }

            binding.tvCookingTime.text =
                resources.getString(R.string.lbl_estimate_cooking_time, it.dishDetails.cookingTime)

            if (args.dishDetails.favoriteDish) {
                binding.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )
            } else {
                binding.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_unselected
                    )
                )
            }
        }

        binding.ivFavoriteDish.setOnClickListener {
            args.dishDetails.favoriteDish = !args.dishDetails.favoriteDish

            dishDetailsViewModel.update(args.dishDetails)

            if (args.dishDetails.favoriteDish) {
                binding.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_selected
                    )
                )

                resources.getString(R.string.msg_added_to_favorites).toast(requireActivity())
            } else {
                binding.ivFavoriteDish.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.ic_favorite_unselected
                    )
                )

                resources.getString(R.string.msg_removed_from_favorite).toast(requireActivity())
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_share, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_share_dish ->{
                val type = "text/plain"
                val subject = "Checkout this dish recipe"
                var extraText = ""
                val shareWith = "Share with"

                favDishDetails?.let {
                    var image = ""
                    if (it.imageSource == Constants.DISH_IMAGE_SOURCE_ONLINE) image = it.image

                    var cookingInstructions = ""

                    // The instruction or you can say the Cooking direction text is in the HTML format so we will you the fromHtml to populate it in the TextView.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        cookingInstructions = Html.fromHtml(
                            it.directionsToCook,
                            Html.FROM_HTML_MODE_COMPACT
                        ).toString()
                    } else {
                        @Suppress("DEPRECATION")
                        cookingInstructions = Html.fromHtml(it.directionsToCook).toString()
                    }

                    extraText =
                        "$image \n" +
                                "\n Title:  ${it.title} \n\n Type: ${it.type} \n\n Category: ${it.category}" +
                                "\n\n Ingredients: \n ${it.ingredients} \n\n Instructions To Cook: \n $cookingInstructions" +
                                "\n\n Time required to cook the dish approx ${it.cookingTime} minutes."
                }

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = type
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                intent.putExtra(Intent.EXTRA_TEXT, extraText)
                startActivity(Intent.createChooser(intent, shareWith))

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}