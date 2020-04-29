package com.donmedapp.netgames.ui.register


import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity

import com.donmedapp.netgames.R
import com.donmedapp.netgames.data.pojo.UserGame
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.donmedapp.netgames.extensions.invisibleUnless
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.register_fragment.*

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
            && txtNickRegister.text.toString().isNotEmpty()
        ) {
            auth.createUserWithEmailAndPassword(
                txtEmail.text.toString(),
                txtPassword.text.toString()
            )
                .addOnCompleteListener(activity!!) {
                    if (it.isSuccessful) {
                        val user: FirebaseUser = auth.currentUser!!
                        setupFirebaseData(user)
                        verifyEmail(user)
                    }

                }.addOnFailureListener {
                    Snackbar.make(
                        txtEmail,
                        it.message!!,
                        Snackbar.LENGTH_SHORT
                    ).show()

                }

        } else {
            Snackbar.make(
                txtEmail,
                getString(R.string.register_rellene_toast),
                Snackbar.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun setupFirebaseData(user: FirebaseUser) {
        val myDB = FirebaseFirestore.getInstance()
        val gameNew = myDB.collection("users").document(user.uid)
        gameNew.get().addOnSuccessListener {
            val userGames = UserGame(
                arrayListOf(),
                txtNickRegister.text.toString(),
                R.drawable.ic_person_black_24dp
            )
            myDB.collection("users").document(user.uid)
                .set(userGames)

        }

    }

    private fun verifyEmail(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    txtEmail.invisibleUnless(false)
                    txtPassword.invisibleUnless(false)
                    txtNickRegister.invisibleUnless(false)
                    btnRegister.invisibleUnless(false)
                    lblConfirmation.invisibleUnless(true)
                } else {
                    Snackbar.make(
                        txtEmail,
                        getString(R.string.register_email_error),
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
    }
}
