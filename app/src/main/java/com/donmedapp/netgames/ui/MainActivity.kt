package com.donmedapp.netgames.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.donmedapp.netgames.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val navController: NavController by lazy {
        findNavController(R.id.navHostFragment)
    }

    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(this)
    }

    var viewmodel: MainViewModel = MainViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setupBottomNav()
        setupAppBar()
    }

    private fun setupBottomNav() {
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginDestination -> hideBottomNav()
                R.id.gameDestination -> hideBottomNav()
                R.id.favoritesDestination -> hideBottomNav()
                R.id.registerDestination -> hideBottomNav()
                R.id.forgotPasswordDestination -> hideBottomNav()
                R.id.accountDestination -> hideBottomNav()

                else -> showBottomNav()
            }
        }
    }

    private fun showBottomNav() {
        bottomNavigationView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        bottomNavigationView.visibility = View.GONE

    }

    private fun setupAppBar() {
        setSupportActionBar(toolbar)

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}
