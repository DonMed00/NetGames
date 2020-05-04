package com.donmedapp.netgames.ui.account


import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.donmedapp.netgames.R
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.donmedapp.netgames.ui.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.EmailAuthProvider
import kotlinx.android.synthetic.main.account_fragment.*


/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment(R.layout.account_fragment) {


    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }


    var viewmodel: MainViewModel = MainViewModel()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()

    }


    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        txtEmail.setText(viewmodel.mAuth.currentUser!!.email)
        txtPassword.setText("PASSWORD")
        imgEdit.setOnClickListener { updateEmail() }
        imgEdit2.setOnClickListener { updatePassword() }

    }

    private fun updatePassword() {
        txtEmail.hideSoftKeyboard()
        txtEmail.hideSoftKeyboard()
        val user = viewmodel.mAuth.currentUser
        // Get auth credentials from the user for re-authentication
        // Get auth credentials from the user for re-authentication
        val credential = EmailAuthProvider
            .getCredential(
                settings.getString("currentUser", getString(R.string.no_user))!!,
                settings.getString("currentPassword", getString(R.string.no_password))!!
            ) // Current Login Credentials \\

        // Prompt the user to re-provide their sign-in credentials
        // Prompt the user to re-provide their sign-in credentials
        user!!.reauthenticate(credential)
            .addOnCompleteListener {
                val user = viewmodel.mAuth.currentUser
                user!!.updatePassword(txtPassword.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            settings.edit {
                                putString("currentPassword", txtPassword.text.toString())
                            }
                            Snackbar.make(
                                txtPassword,
                                getString(R.string.account_password_changed),
                                Snackbar.LENGTH_SHORT
                            ).show()
                        } else {
                            Snackbar.make(
                                txtPassword,
                                task.exception!!.message!!,
                                Snackbar.LENGTH_SHORT
                            ).show()

                        }
                    }
            }
    }

    private fun updateEmail() {
        txtEmail.hideSoftKeyboard()
        val user = viewmodel.mAuth.currentUser
        // Get auth credentials from the user for re-authentication
        // Get auth credentials from the user for re-authentication
        val credential = EmailAuthProvider
            .getCredential(
                settings.getString("currentUser", getString(R.string.no_user))!!,
                settings.getString("currentPassword", getString(R.string.no_password))!!
            ) // Current Login Credentials \\

        // Prompt the user to re-provide their sign-in credentials
        // Prompt the user to re-provide their sign-in credentials
        user!!.reauthenticate(credential)
            .addOnCompleteListener {
                //Now change your email address \\
                //----------------Code for Changing Email Address----------\\
                val user = viewmodel.mAuth.currentUser
                user!!.updateEmail(txtEmail.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            settings.edit {
                                putString("currentUser", txtEmail.text.toString())
                            }
                            Snackbar.make(
                                txtPassword,
                                getString(R.string.account_email_changed),
                                Snackbar.LENGTH_SHORT
                            ).show()

                        } else {
                            Snackbar.make(
                                txtPassword,
                                task.exception!!.message!!,
                                Snackbar.LENGTH_SHORT
                            ).show()

                        }
                    }
            }
    }


    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.account_title)
            setDisplayHomeAsUpEnabled(true)
        }

    }
}




