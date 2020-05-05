package com.donmedapp.netgames.ui.forgotPassword


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels

import com.donmedapp.netgames.R
import com.donmedapp.netgames.base.observeEvent
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.donmedapp.netgames.extensions.invisibleUnless
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.forgot_password_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment : Fragment(R.layout.forgot_password_fragment) {


    private val viewmodel: ForgotPasswordViewmodel by viewModels {
        ForgotPasswordViewmodelFactory(activity!!.application, activity!!)
    }
    //Firebase references

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupAppBar()
        observeMessage()
        btnSendPass!!.setOnClickListener {
            sendResetPasswordToEmail()
        }
    }

    private fun observeMessage() {
        viewmodel.message.observeEvent(this) {
            Snackbar.make(
                txtEmailPassword,
                it,
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun sendResetPasswordToEmail() {
        if (viewmodel.sendPasswordResetEmail(txtEmailPassword, txtEmailPassword.text.toString())) {
            txtEmailPassword.invisibleUnless(false)
            btnSendPass.invisibleUnless(false)
            lblConfirmation.invisibleUnless(true)
        }
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.send_password_title)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

    }


}
