package com.donmedapp.netgames.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.donmedapp.netgames.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private val navController: NavController by lazy {
        findNavController(R.id.navHostFragment)
    }

    private val appBarConfiguration: AppBarConfiguration =
        AppBarConfiguration.Builder(
            R.id.loginFragment
        )
            .build()


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}
