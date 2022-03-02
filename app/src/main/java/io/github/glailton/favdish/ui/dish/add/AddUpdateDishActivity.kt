package io.github.glailton.favdish.ui.dish.add

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karumi.dexter.Dexter
import io.github.glailton.favdish.R
import io.github.glailton.favdish.databinding.ActivityAddUpdateDishBinding
import io.github.glailton.favdish.databinding.DialogCustomImageSelectionBinding
import io.github.glailton.favdish.ui.extensions.toast
import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import io.github.glailton.favdish.databinding.DialogCustomListBinding
import io.github.glailton.favdish.ui.adapters.CustomListItemAdapter
import io.github.glailton.favdish.ui.utils.Constants
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateDishBinding
    private var imagePath: String = ""

    private lateinit var customListDialog: MaterialAlertDialogBuilder
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateDishBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupActionBar()

        binding.ivAddDishImage.setOnClickListener(this)
        binding.etType.setOnClickListener(this)
        binding.etCategory.setOnClickListener(this)
        binding.etCookingTime.setOnClickListener(this)
        binding.btnAddDish.setOnClickListener(this)
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddDish)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddDish.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun customImageSelectDialog(){
        val dialogCustomImageSelectionBinding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        customListDialog = MaterialAlertDialogBuilder(this)
        customListDialog.setView(dialogCustomImageSelectionBinding.root)
        val dialog = customListDialog.show()

        dialogCustomImageSelectionBinding.tvCamera.setOnClickListener {
            Dexter.withContext(this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).withListener(object : MultiplePermissionsListener{
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (report.areAllPermissionsGranted()) {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            // TODO check the current way to to that
                            startActivityForResult(intent, CAMERA)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread().check()

            dialog.dismiss()
        }

        dialogCustomImageSelectionBinding.tvGallery.setOnClickListener {
            Dexter.withContext(this@AddUpdateDishActivity).withPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ).withListener(object : PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    //TODO check the current way to to that
//                    val picture = registerForActivityResult(ActivityResultContracts.GetContent()) {
//                        binding.ivDishImage.setImageURI(it)
//                    }
//                    picture.launch("image/*")

                    val galleryIntent = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                    startActivityForResult(galleryIntent, GALLERY)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    "You have denied the storage permission to select image".toast(this@AddUpdateDishActivity)
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread().check()

            dialog.dismiss()
        }
    }

    private fun showRationalDialogForPermissions(){
        MaterialAlertDialogBuilder(this).setMessage("It looks like you have " +
                "turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton("GO TO SETTINGS") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }
            .setNegativeButton(getString(R.string.cancel)){dialog,_ ->
                dialog.dismiss()
            }.show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap) : String {
        val wrapper = ContextWrapper(applicationContext)

        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)

        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        }catch (e: IOException){
            e.printStackTrace()
        }

        return file.absolutePath
    }

    private fun customItemsDialog(title: String, items: List<String>, selection: String){
        customListDialog = MaterialAlertDialogBuilder(this)
        val binding: DialogCustomListBinding = DialogCustomListBinding.inflate(layoutInflater)

        customListDialog.setView(binding.root)

        binding.tvTitle.text = title
        binding.rvList.layoutManager = LinearLayoutManager(this)

        val adapter = CustomListItemAdapter(this, items, selection)
        binding.rvList.adapter = adapter

        dialog = customListDialog.show()
    }

    fun selectedListItem(item: String, selection: String) {
        when(selection){
            Constants.DISH_TYPE ->{
                dialog.dismiss()
                binding.etType.setText(item)
            }
            Constants.DISH_CATEGORY ->{
                dialog.dismiss()
                binding.etCategory.setText(item)
            }
            Constants.DISH_COOKING_TIME ->{
                dialog.dismiss()
                binding.etCookingTime.setText(item)
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA) {

                data?.extras?.let {
                    val thumbnail: Bitmap =
                        data.extras!!.get("data") as Bitmap // Bitmap from camera

                    Glide.with(this)
                        .load(thumbnail)
                        .centerCrop()
                        .into(binding.ivDishImage)

                    imagePath = saveImageToInternalStorage(thumbnail)

                    Log.i(TAG, imagePath)

                    // Replace the add icon with edit icon once the image is selected.
                    binding.ivAddDishImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@AddUpdateDishActivity,
                            R.drawable.ic_edit
                        )
                    )
                }
            }else if (requestCode == GALLERY) {

                data?.let {
                    // Here we will get the select image URI.
                    val selectedPhotoUri = data.data

                    Glide.with(this)
                        .load(selectedPhotoUri)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .listener(object : RequestListener<Drawable>{
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                Log.e(TAG, "Error loading image")
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                resource?.let {
                                    val bitmap: Bitmap = resource.toBitmap()
                                    imagePath = saveImageToInternalStorage(bitmap)
                                }
                                return false
                            }
                        })
                        .into(binding.ivDishImage)

                    // Replace the add icon with edit icon once the image is selected.
                    binding.ivAddDishImage.setImageDrawable(
                        ContextCompat.getDrawable(
                            this@AddUpdateDishActivity,
                            R.drawable.ic_edit
                        )
                    )
                }
            }
            // END
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Cancelled", "Cancelled")
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.iv_add_dish_image -> {
                    customImageSelectDialog()
                    return
                }
                R.id.et_type -> {
                    customItemsDialog(resources.getString(R.string.title_select_dish_type),
                        Constants.dishTypes(), Constants.DISH_TYPE)

                    return
                }
                R.id.et_category -> {
                    customItemsDialog(resources.getString(R.string.title_select_dish_category),
                        Constants.dishCategories(), Constants.DISH_CATEGORY)

                    return
                }
                R.id.et_cooking_time -> {
                    customItemsDialog(resources.getString(R.string.title_select_dish_cooking_time),
                        Constants.dishCookingTime(), Constants.DISH_COOKING_TIME)

                    return
                }
                R.id.btn_add_dish -> {
                    val title = binding.edTitle.text.toString().trim { it <= ' ' }
                    val type = binding.etType.text.toString().trim { it <= ' ' }
                    val category = binding.etCategory.text.toString().trim { it <= ' ' }
                    val ingredients = binding.etIngredients.text.toString().trim { it <= ' ' }
                    val cookingTime = binding.etCookingTime.text.toString().trim { it <= ' ' }
                    val cookingDirection = binding.etDirectionToCook.text.toString().trim { it <= ' ' }

                    when {
                        TextUtils.isEmpty(imagePath) -> {
                            resources.getString(R.string.err_msg_select_dish_image).toast(this)
                        }
                        TextUtils.isEmpty(title) -> {
                            binding.edTitle.error = resources.getString(R.string.err_msg_enter_dish_title)
                        }
                        TextUtils.isEmpty(type) -> {
                            resources.getString(R.string.err_msg_select_dish_type).toast(this)
                        }
                        TextUtils.isEmpty(category) -> {
                            resources.getString(R.string.err_msg_select_dish_category).toast(this)
                        }
                        TextUtils.isEmpty(ingredients) -> {
                            binding.etIngredients.error = resources.getString(R.string.err_msg_enter_dish_ingredients)
                        }
                        TextUtils.isEmpty(cookingTime) -> {
                            resources.getString(R.string.err_msg_select_dish_cooking_time).toast(this)
                        }
                        TextUtils.isEmpty(cookingDirection) -> {
                            binding.etDirectionToCook.error = resources.getString(R.string.err_msg_enter_dish_cooking_directions)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val CAMERA = 1
        private const val GALLERY = 2
        private const val IMAGE_DIRECTORY = "FavDishImages"
        private const val TAG = "AddUpdateDishActivity"
    }
}

