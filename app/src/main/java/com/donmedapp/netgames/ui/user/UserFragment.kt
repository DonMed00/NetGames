package com.donmedapp.netgames.ui.user


import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.preference.PreferenceManager
import com.donmedapp.netgames.R
import com.donmedapp.netgames.base.observeEvent
import com.donmedapp.netgames.ui.MainViewModel
import com.donmedapp.netgames.utils.roundedImg
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.user_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class UserFragment : Fragment(R.layout.user_fragment) {

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
        observeMessage()
        setHasOptionsMenu(true)
        setupLblOptions()
        observeLiveData()
        setupNavigationToEdit()
    }

    private fun setupNavigationToEdit() {
        lblName.setOnClickListener { goToEdit() }
        imgPerfil.setOnClickListener { goToEdit() }
        imgEdit.setOnClickListener { goToEdit() }
    }

    private fun observeMessage() {
        viewmodelMain.message.observeEvent(this) {
            Snackbar.make(lblAccount, it, Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun observeLiveData() {
        //if(!viewmodelMain.mAuth.currentUser!!.isEmailVerified){
        //    Snackbar.make(imgPerfil,"Verifica",Snackbar.LENGTH_SHORT).show()
        //}
        viewmodelMain.avatar.observe(this){
            //imgPerfil.setImageResource(it)
            imgPerfil.setImageDrawable(roundedImg(it,resources))
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController()) ||
                super.onOptionsItemSelected(item)
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
        Snackbar.make(
            lblAccount,
            getString(R.string.logout_make),
            Snackbar.LENGTH_SHORT
        ).show()
        findNavController().navigate(R.id.navegateLogOut)

    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(false)
        }

    }
}
