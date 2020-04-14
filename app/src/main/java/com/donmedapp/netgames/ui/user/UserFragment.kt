package com.donmedapp.netgames.ui.user


import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager

import com.donmedapp.netgames.R
import com.donmedapp.netgames.ui.MainActivity
import com.donmedapp.netgames.ui.MainViewModel
import kotlinx.android.synthetic.main.user_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class UserFragment : Fragment(R.layout.user_fragment) {

    private val viewModel: UserViewmodel by viewModels {
        UserViewmodelFactory(activity!!.application)
    }

    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }


    var viewmodelMain: MainViewModel = MainViewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()

    }

    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        setupLblOptions()
    }

    private fun setupLblOptions() {
        lblLogOut.setOnClickListener { logOut() }
    }

    private fun logOut() {
        settings.edit {
            putString("currentUser", getString(R.string.no_user))
            putString("currentPassword", getString(R.string.no_password))
        }
        viewmodelMain.mAuth.signOut()
        Toast.makeText(activity, "Log Out", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.navegateLogOut)

    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.user_title)
            setDisplayHomeAsUpEnabled(false)
        }

    }
}
