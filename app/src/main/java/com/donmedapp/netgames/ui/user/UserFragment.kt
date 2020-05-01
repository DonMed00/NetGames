package com.donmedapp.netgames.ui.user


import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager

import com.donmedapp.netgames.R
import com.donmedapp.netgames.ui.MainViewModel
import com.google.android.material.snackbar.Snackbar
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
        viewmodelMain.setupData()
        setupViews()
    }

    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        setupLblOptions()
        observeLiveData()
        imgEdit.setOnClickListener { goToEdit() }
    }

    private fun observeLiveData() {
        //if(!viewmodelMain.mAuth.currentUser!!.isEmailVerified){
        //    Snackbar.make(imgPerfil,"Verifica",Snackbar.LENGTH_SHORT).show()
        //}
        viewmodelMain.avatar.observe(this){
            imgPerfil.setImageResource(it)
        }
        viewmodelMain.avatarName.observe(this){
            if(it==""){
                lblName.text="Nickname"

            }else{
                lblName.text=it

            }
        }
    }

    private fun goToEdit() {
        findNavController().navigate(R.id.navToEdit)
    }

    private fun setupLblOptions() {
        lblList.setOnClickListener { seeFavourites() }
        lblAccount.setOnClickListener { goAccount() }
        lblLogOut.setOnClickListener { logOut() }
    }

    private fun goAccount() {
        findNavController().navigate(R.id.navToAccount)    }

    private fun seeFavourites() {
        findNavController().navigate(R.id.navigateToFavorites)
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
            title = getString(R.string.user_title)
            setDisplayHomeAsUpEnabled(false)
        }

    }
}
