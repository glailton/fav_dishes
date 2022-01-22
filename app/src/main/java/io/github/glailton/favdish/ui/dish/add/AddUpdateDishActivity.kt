package io.github.glailton.favdish.ui.dish.add

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.glailton.favdish.R
import io.github.glailton.favdish.databinding.ActivityAddUpdateDishBinding
import io.github.glailton.favdish.databinding.DialogCustomImageSelectionBinding
import io.github.glailton.favdish.ui.extensions.toast
import kotlin.math.log

class AddUpdateDishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddUpdateDishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddUpdateDishBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupActionBar()

        binding.ivAddDishImage.setOnClickListener(this)
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddDish)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddDish.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun customImageSelectDialog(){
        val binding: DialogCustomImageSelectionBinding =
            DialogCustomImageSelectionBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(this)
        builder.setView(binding.root)
        val dialog = builder.show()

        binding.tvCamera.setOnClickListener {
            "Camera".toast(this)
            dialog.dismiss()
        }
        binding.tvGallery.setOnClickListener {
            "Gallery".toast(this)
            dialog.dismiss()
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.iv_add_dish_image -> {
                    customImageSelectDialog()
                    return
                }
            }
        }
    }
}