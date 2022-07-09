package io.github.glailton.favdish.ui.dish.random

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.github.glailton.favdish.R
import io.github.glailton.favdish.data.entities.FavDish
import io.github.glailton.favdish.data.entities.RandomDish
import io.github.glailton.favdish.databinding.FragmentRandomDishBinding
import io.github.glailton.favdish.ui.dish.favorite.FavoriteDishesViewModel
import io.github.glailton.favdish.ui.extensions.hideDialog
import io.github.glailton.favdish.ui.extensions.loadImage
import io.github.glailton.favdish.ui.extensions.showDialog
import io.github.glailton.favdish.ui.extensions.toast
import io.github.glailton.favdish.ui.utils.Constants
import timber.log.Timber

@AndroidEntryPoint
class RandomDishFragment : Fragment() {

    private val randomDishViewModel: RandomDishViewModel by viewModels()
    private var _binding: FragmentRandomDishBinding? = null

    private var progressDialog: Dialog? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRandomDishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        randomDishViewModel.getRandomDish()

        observers()

        binding.srlRandomDish.setOnRefreshListener {
            randomDishViewModel.getRandomDish()
        }
    }

    private fun observers() {
        randomDishViewModel.randomDishResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                Timber.i("${response.recipes[0]}")

                if (binding.srlRandomDish.isRefreshing) binding.srlRandomDish.isRefreshing = false
                setUi(response.recipes.first())
            }
        }

        randomDishViewModel.randomDishLoadingError.observe(viewLifecycleOwner) { dataError ->
            dataError?.let {
                Timber.e("$dataError")
                if (binding.srlRandomDish.isRefreshing) binding.srlRandomDish.isRefreshing = false
            }
        }

        randomDishViewModel.loadRandomDish.observe(viewLifecycleOwner) { loadRandomDish ->
            loadRandomDish?.let {
                Timber.i("$loadRandomDish")

                if (loadRandomDish && !binding.srlRandomDish.isRefreshing) {
                    showCustomProgressDialog()
                } else {
                    hideProgressDialog()
                }
            }
        }
    }

    private fun setUi(recipe: RandomDish.Recipe) {
        with(binding) {
            ivDishImage.loadImage(recipe.image)
            tvTitle.text = recipe.title

            val dishType =
                if (recipe.dishTypes.isNotEmpty()) recipe.dishTypes.first().toString() else "other"
            tvType.text = dishType
            tvCategory.text = "Other"

            var ingredients = ""
            for (value in recipe.extendedIngredients) {
                ingredients = if (ingredients.isEmpty()) {
                    value.original
                } else {
                    ingredients + ", \n" + value.original
                }
            }
            tvIngredients.text = ingredients
            tvCookingDirection.text =
                Html.fromHtml(recipe.instructions, Html.FROM_HTML_MODE_COMPACT)
            tvCookingTime.text =
                getString(R.string.lbl_estimate_cooking_time, recipe.readyInMinutes.toString())
            ivFavoriteDish.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_favorite_unselected
                )
            )

            var addedToFavorites = false

            ivFavoriteDish.setOnClickListener {
                if (addedToFavorites) getString(R.string.msg_already_added_to_favorites).toast(
                    requireContext(),
                    Toast.LENGTH_SHORT
                ) else {
                    val randomDishDetails = FavDish(
                        recipe.image,
                        Constants.DISH_IMAGE_SOURCE_ONLINE,
                        recipe.title,
                        dishType,
                        "Other",
                        ingredients,
                        recipe.readyInMinutes.toString(),
                        recipe.instructions,
                        true
                    )

                    val favDishesViewModel: FavoriteDishesViewModel by viewModels()
                    favDishesViewModel.insert(randomDishDetails)
                    addedToFavorites = true
                    ivFavoriteDish.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_favorite_selected
                        )
                    )
                    getString(R.string.msg_added_to_favorites).toast(
                        requireContext(),
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }

    }

    private fun showCustomProgressDialog() {
        progressDialog = Dialog(requireContext())

        progressDialog?.let {
            it.setContentView(R.layout.dialog_custom_progress)
            it.show()
        }
    }

    private fun hideProgressDialog() {
        progressDialog?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}