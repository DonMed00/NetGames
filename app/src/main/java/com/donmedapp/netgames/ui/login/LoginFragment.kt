package com.donmedapp.netgames.ui.login


import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.donmedapp.netgames.R
import com.donmedapp.netgames.base.observeEvent
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.donmedapp.netgames.ui.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.edit_fragment.*
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment(R.layout.login_fragment) {


    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }
    var viewmodel: MainViewModel = MainViewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkIfUserIsSaved()
        setupViews()

    }


    private fun setupViews() {
        setupAppBar()
        setHasOptionsMenu(true)
        setupBtns()
        observeMessage()
        setupOnEditorAction()
    }

    private fun setupOnEditorAction() {
        txtPassword.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
                return@OnEditorActionListener true
            }
            false
        })

    }

    private fun checkIfUserIsSaved() {
        if (settings.getString(
                "currentUser",
                getString(R.string.no_user)
            ) != getString(R.string.no_user)
        ) {
            txtEmail.setText(settings.getString("currentUser", getString(R.string.no_user)))
            txtPassword.setText(
                settings.getString(
                    "currentPassword",
                    getString(R.string.no_password)
                )
            )
            loginUserAuth()
        }
    }


    private fun loginUserAuth() {
        showProgressbar()
        viewmodel.mAuth.signInWithEmailAndPassword(
            settings.getString("currentUser", getString(R.string.no_user))!!,
            settings.getString("currentPassword", getString(R.string.no_password))!!
        )

            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                   // viewmodel.setMessage(getString(R.string.login_session_init_correct))
                    findNavController().navigate(R.id.navigateToHome)
                } else {
                    viewmodel.setMessage(
                        task.exception!!.message!!
                    )
                }
                hideProgressbar()

            }
    }

    private fun hideProgressbar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressbar() {
        progressBar.visibility = View.VISIBLE
        GlobalScope.launch { Runnable { progressBar.visibility = View.GONE } }

    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(false)
        }

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

    private fun setupBtns() {
        btnLogin.setOnClickListener { login() }
        btnCreate.setOnClickListener {
            register()
        }
        lblForgotPassword.setOnClickListener { goToForgotPassword() }
    }

    private fun goToForgotPassword() {
        txtEmail.hideSoftKeyboard()
        findNavController().navigate(R.id.navigateToForgotPassword)
    }


    private fun loginUser() {
        txtEmail.clearFocus()
        txtPassword.clearFocus()
        layoutTxtEmail.clearFocus()
        layoutTxtPassword.clearFocus()

        showProgressbar()
        viewmodel.mAuth.signInWithEmailAndPassword(
            txtEmail.text.toString(), txtPassword.text.toString()
        )
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    //viewmodel.setMessage(getString(R.string.login_session_init_correct))
                    goPrincipal()
                } else {
                    viewmodel.setMessage(
                        task.exception!!.message!!
                    )
                }
                hideProgressbar()
            }
    }


    private fun goPrincipal() {
        settings.edit {
            putString("currentUser", txtEmail.text.toString())
            putString("currentPassword", txtPassword.text.toString())
        }
        txtEmail.hideSoftKeyboard()
        findNavController().navigate(R.id.navigateToHome)

    }

    private fun login() {
        txtEmail.hideSoftKeyboard()

        if (txtEmail.text.toString().isNotEmpty() && txtPassword.text.toString().isNotEmpty()) {
            loginUser()
        } else {
            viewmodel.setMessage(
                getString(R.string.forgot_rellenar_campos)
            )

        }

    }

    private fun register() {
        txtEmail.hideSoftKeyboard()
        findNavController().navigate(R.id.navigateToRegister)
    }


}
