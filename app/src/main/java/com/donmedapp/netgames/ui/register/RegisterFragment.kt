package com.donmedapp.netgames.ui.register


import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

import com.donmedapp.netgames.R
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.donmedapp.netgames.extensions.invisibleUnless
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.register_fragment.*
import kotlin.properties.Delegates

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment(R.layout.register_fragment) {
    private lateinit var auth: FirebaseAuth


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        auth = FirebaseAuth.getInstance()
        btnRegister.setOnClickListener { createNewAccount() }
        setupAppBar()
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.register_title)
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true)
        }

    }

    private fun createNewAccount() {
        txtEmail.hideSoftKeyboard()
        if (txtEmail.text.toString().isNotEmpty()
            && txtPassword.text.toString().isNotEmpty()
        ) {
            auth.createUserWithEmailAndPassword(
                txtEmail.text.toString(),
                txtPassword.text.toString()
            )
                .addOnCompleteListener(activity!!) {
                    if (it.isSuccessful) {
                        val user: FirebaseUser = auth.currentUser!!
                        verifyEmail(user)
                    }

                }.addOnFailureListener {
                    Toast.makeText(
                        activity, it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

        } else {
            Toast.makeText(activity, getString(R.string.register_rellene_toast), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun verifyEmail(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener(activity!!) {
                    task ->
                if (task.isSuccessful) {
                    txtEmail.invisibleUnless(false)
                    txtPassword.invisibleUnless(false)
                    btnRegister.invisibleUnless(false)
                    lblConfirmation.invisibleUnless(true)
                } else {
                    Toast.makeText(
                        activity,
                        getString(R.string.register_email_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
