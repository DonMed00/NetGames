package com.donmedapp.netgames.ui.principal


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
class PrincipalFragment : Fragment(R.layout.principal_fragment) {
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.principal_title)
        }
    }


}
