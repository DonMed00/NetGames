package com.donmedapp.netgames.ui.user


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity

import com.donmedapp.netgames.R

/**
 * A simple [Fragment] subclass.
 */
class UserFragment : Fragment(R.layout.user_fragment) {



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()

    }

    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.user_title)
            setDisplayHomeAsUpEnabled(false)
        }

    }
}
