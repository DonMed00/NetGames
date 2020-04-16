package com.donmedapp.netgames.ui.login


import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.donmedapp.netgames.R
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.donmedapp.netgames.extensions.invisibleUnless
import com.donmedapp.netgames.ui.MainActivity
import com.donmedapp.netgames.ui.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login_fragment.*

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

        var progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Cargando...")
        progressDialog.show()
        viewmodel.mAuth.signInWithEmailAndPassword(
            settings.getString("currentUser", getString(R.string.no_user))!!,
            settings.getString("currentPassword", getString(R.string.no_password))!!
        )

            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.navigateToHome)
                    progressDialog.dismiss()
                } else {
                    progressDialog.dismiss()
                    Toast.makeText(
                        activity, task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
        viewmodel.mAuth.signInWithEmailAndPassword(
            txtEmail.text.toString(), txtPassword.text.toString()
        )
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        activity, "Inicio de sesion correcto",
                        Toast.LENGTH_SHORT
                    ).show()
                    goPrincipal()
                } else {
                    Toast.makeText(
                        activity, task.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
            Toast.makeText(activity, getString(R.string.register_rellene_toast), Toast.LENGTH_SHORT).show()
        }

    }
    private fun register() {
        txtEmail.hideSoftKeyboard()
        findNavController().navigate(R.id.navigateToRegister)
    }


}
