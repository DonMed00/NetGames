package com.donmedapp.netgames.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.donmedapp.netgames.R
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val navController: NavController by lazy {
        findNavController(R.id.navHostFragment)
    }

    var viewmodel: MainViewModel = MainViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
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
                R.id.gameDestination -> hideViews()
                R.id.assistantFragment -> hideViews()
                R.id.favoritesDestination -> hideViews()
                R.id.registerDestination -> hideViews()
                R.id.forgotPasswordDestination -> hideViews()
                R.id.accountDestination -> hideViews()
                R.id.editDestination -> hideViews()
                R.id.infoDialog -> hideViews()

                else -> showBottomNav()
            }
        }

        bottomNavigationView.setOnNavigationItemReselectedListener { }
    }


    private fun showBottomNav() {
        bottomNavigationView.visibility = View.VISIBLE
        showImgToolbar()
    }

    private fun showImgToolbar() {
        imgToolbar.visibility=View.VISIBLE
    }
    private fun hideImgToolbar() {
        imgToolbar.visibility=View.GONE
    }
    private fun hideViews() {
        hideBottomNav()
        hideImgToolbar()
    }

    private fun hideBottomNav() {
        showImgToolbar()
        bottomNavigationView.visibility = View.GONE

    }

    private fun setupAppBar() {
        setSupportActionBar(toolbar)
    }


    override fun onSupportNavigateUp(): Boolean {
        bottomNavigationView.hideSoftKeyboard()
        onBackPressed()
        return true
    }
}
