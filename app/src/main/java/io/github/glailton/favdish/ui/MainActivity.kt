package io.github.glailton.favdish.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.*
import dagger.hilt.android.AndroidEntryPoint
import io.github.glailton.favdish.R
import io.github.glailton.favdish.data.notification.NotifyWorker
import io.github.glailton.favdish.databinding.ActivityMainBinding
import io.github.glailton.favdish.ui.utils.Constants
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_all_dishes, R.id.navigation_favorite_dishes, R.id.navigation_random_dish
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        if(intent.hasExtra(Constants.NOTIFICATION_ID)) {
            val notificationId = intent.getIntExtra(Constants.NOTIFICATION_ID, 0)
            Timber.i("Notification Id", "$notificationId")

            binding.navView.selectedItemId = R.id.navigation_random_dish
        }
        startWork()
    }

    private fun createConstraints() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .setRequiresCharging(false)
        .setRequiresBatteryNotLow(true)
        .build()

    private fun createWorkRequest() = PeriodicWorkRequestBuilder<NotifyWorker>(15, TimeUnit.MINUTES)
        .setConstraints(createConstraints())
        .build()

    private fun startWork(){
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("FavDish Notify Work",
                ExistingPeriodicWorkPolicy.KEEP, createWorkRequest())
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    fun hideBottomNavigationView(){
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(binding.navView.height.toFloat()).duration = 300
        binding.navView.visibility = View.GONE
    }

    fun showBottomNavigationView(){
        binding.navView.clearAnimation()
        binding.navView.animate().translationY(0f).duration = 300
        binding.navView.visibility = View.VISIBLE
    }

}