package com.donmedapp.netgames.ui.account

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.donmedapp.netgames.R
import com.donmedapp.netgames.base.observeEvent
import com.donmedapp.netgames.ui.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.account_fragment.*


/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : Fragment(R.layout.account_fragment) {


    private val viewmodel: AccountViewmodel by viewModels {
        AccountViewmodelFactory(activity!!.application, activity!!)
    }

    var viewmodelActivity: MainViewModel = MainViewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()

    }


    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        observeMessage()
        setupOnEditorAction(txtEmail)
        setupOnEditorAction(txtPassword)
        txtEmail.setText(viewmodelActivity.mAuth.currentUser!!.email)
        txtPassword.setText(
            viewmodel.settings.getString(
                "currentPassword",
                getString(R.string.no_password)
            )
        )
        imgEdit.setOnClickListener { viewmodel.updateEmail(txtEmail, txtEmail.text.toString()) }
        imgEdit2.setOnClickListener {
            viewmodel.updatePassword(
                txtPassword,
                txtPassword.text.toString()
            )
        }

    }

    private fun setupOnEditorAction(editText: EditText) {
        editText.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (editText == txtEmail) {
                    viewmodel.updateEmail(txtEmail, txtEmail.text.toString())
                } else if (editText == txtPassword) {
                    viewmodel.updatePassword(txtPassword, txtPassword.text.toString())
                }
                return@OnEditorActionListener true
            }
            false
        })

    }

    private fun observeMessage() {
        viewmodel.message.observeEvent(this) {
            Snackbar.make(
                txtEmail,
                it,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    /* private fun updatePassword() {
         txtEmail.hideSoftKeyboard()
         txtEmail.hideSoftKeyboard()
         val user = viewmodelActivity.mAuth.currentUser
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
                 val user = viewmodelActivity.mAuth.currentUser
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
     }*/

    /*private fun updateEmail() {
        txtEmail.hideSoftKeyboard()
        val user = viewmodelActivity.mAuth.currentUser
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
                val user = viewmodelActivity.mAuth.currentUser
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
    }*/


    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.account_title)
            setDisplayHomeAsUpEnabled(true)
        }

    }
}




