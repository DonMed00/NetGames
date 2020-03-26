package com.donmedapp.netgames.ui.principal


import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

import com.donmedapp.netgames.R

/**
 * A simple [Fragment] subclass.
 */
class PrincipalFragment : Fragment(R.layout.principal_fragment) {

    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.principal_title)
        }

        if (settings.getLong("currentUser", -1) != 1L) {
            findNavController().navigate(R.id.loginFragment)
        }
    }


}
