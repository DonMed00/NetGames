package com.donmedapp.netgames.ui.login


import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.donmedapp.netgames.R
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.donmedapp.netgames.ui.MainViewModel
import com.google.android.material.snackbar.Snackbar
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
    var viewmodel : MainViewModel= MainViewModel()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (settings.getString("currentUser", getString(R.string.no_user)) != getString(R.string.no_user)) {
            txtEmail.setText(settings.getString("currentUser", getString(R.string.no_user)))
            txtPassword.setText(settings.getString("currentPassword", getString(R.string.no_password)))
            loginUserAuth()
        }
        setupViews()

    }


    private fun setupViews() {
        setupAppBar()
        setupBtns()
        setHasOptionsMenu(true)
    }


    private fun loginUserAuth() {

        showProgressbar()
        viewmodel.mAuth.signInWithEmailAndPassword(
            settings.getString("currentUser", getString(R.string.no_user))!!,
            settings.getString("currentPassword", getString(R.string.no_password))!!
        )

            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    Snackbar.make(
                        txtEmail,
                        getString(R.string.login_session_init_correct),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.navigateToHome)
                } else {
                    Snackbar.make(
                        txtEmail,
                        task.exception!!.message!!,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                hideProgressbar()

            }
    }

    private fun hideProgressbar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressbar() {
        progressBar.visibility=View.VISIBLE
        val r = Runnable {
            progressBar.visibility=View.GONE
        }
        GlobalScope.launch { r }

    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.app_name)
            setDisplayHomeAsUpEnabled(false)
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
        showProgressbar()
        viewmodel.mAuth.signInWithEmailAndPassword(
            txtEmail.text.toString(), txtPassword.text.toString()
        )
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    Snackbar.make(
                        txtEmail,
                        getString(R.string.login_session_init_correct),
                        Snackbar.LENGTH_SHORT
                    ).show()

                    goPrincipal()
                } else {
                    Snackbar.make(
                        txtEmail,
                        task.exception!!.message!!,
                        Snackbar.LENGTH_SHORT
                    ).show()

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
            Snackbar.make(
                txtEmail,
                getString(R.string.forgot_rellenar_campos),
                Snackbar.LENGTH_SHORT
            ).show()
        }

    }
    private fun register() {
        txtEmail.hideSoftKeyboard()
        findNavController().navigate(R.id.navigateToRegister)
    }


}
