package com.donmedapp.netgames.ui.forgotPassword


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.donmedapp.netgames.R
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.donmedapp.netgames.extensions.invisibleUnless
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.forgot_password_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment : Fragment(R.layout.forgot_password_fragment) {


    //Firebase references
    private lateinit var mAuth: FirebaseAuth
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        mAuth = FirebaseAuth.getInstance()
        btnSendPass!!.setOnClickListener { sendPasswordResetEmail() }
        setupAppBar()
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.send_password_title)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

    }

    private fun sendPasswordResetEmail() {
        txtEmailPassword.hideSoftKeyboard()
        val email = txtEmailPassword.text.toString()
        if (email.isNotEmpty()) {
            mAuth
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        txtEmailPassword.invisibleUnless(false)
                        btnSendPass.invisibleUnless(false)
                        lblConfirmation.invisibleUnless(true)
                        Snackbar.make(
                            txtEmailPassword,
                            getString(R.string.forgot_email_enviado),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        Snackbar.make(
                            txtEmailPassword,
                            task.exception!!.message!!,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Snackbar.make(
                txtEmailPassword,
                getString(R.string.forgot_rellenar_campos),
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }
}
