package com.donmedapp.netgames.ui.login


import android.app.ProgressDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.donmedapp.netgames.R
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment(R.layout.login_fragment) {


    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }

    //private lateinit var mProgressBar: ProgressDialog

    //Creamos nuestra variable de autenticación firebase
    private lateinit var mAuth: FirebaseAuth

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        setupAppBar()
        initialise()
        setupBtns()
    }

    private fun setupAppBar() {
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.app_name)
        }
    }

    private fun setupBtns() {
        btnLogin.setOnClickListener { login() }
        btnCreate.setOnClickListener {
            register()
        }
    }

    /*Creamos un método para inicializar nuestros elementos del diseño y la autenticación de firebase*/
    private fun initialise() {
        // mProgressBar = ProgressDialog(activity)
         mAuth = FirebaseAuth.getInstance()
    }

//Ahora vamos a Iniciar sesión con firebase es muy sencillo

    private fun loginUser() {
        //Obtenemos usuario y contraseña
        //Verificamos que los campos no este vacios
        if (txtEmail.text.toString().isNotEmpty() && txtPassword.text.toString()
                .isNotEmpty()
        ) {

            //Mostramos el progressdialog
            //mProgressBar.setMessage("Registering User...")
            // mProgressBar.show()

            //Iniciamos sesión con el método signIn y enviamos usuario y contraseña
            mAuth.signInWithEmailAndPassword(txtEmail.text.toString(), txtPassword.text.toString())
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {

                        // Si se inició correctamente la sesión vamos a la vista Home de la aplicación
                        goPrincipal() // Creamos nuestro método en la parte de abajo
                    } else {
                        // sino le avisamos el usuairo que orcurrio un problema
                        //mProgressBar.hide()
                        Toast.makeText(
                            activity, "Authentication failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Toast.makeText(activity, "Enter all details", Toast.LENGTH_SHORT).show()
        }
        txtEmail.hideSoftKeyboard()
    }


    private fun goPrincipal() {
//Ocultamos el progress
        //mProgressBar.hide()
        settings.edit {
            putLong("currentUser", 1)
        }
        findNavController().navigate(R.id.navigateToPrincipal)

    }

/* Tenemos que crear nuestros métodos con el mismo nombre que le agregamos a nuestros botones en el atributo onclick y forzosamente tenemos que recibir un parámetro view para que sea reconocido como click de nuestro button ya que en view podemos modificar los atributos*/

    /*Primero creamos nuestro evento login dentro de este llamamos nuestro método loginUser al dar click en el botón se iniciara sesión */
    fun login() {
        loginUser()
    }

/*Si se olvido de la contraseña lo enviaremos en la siguiente actividad nos marcara error porque necesitamos crear la actividad*/

    fun forgotPassword(view: View) {

    }

    /*Si quiere registrarse lo enviaremos en la siguiente actividad nos marcara error porque necesitamos crear la actividad*/
    private fun register() {
        findNavController().navigate(R.id.navigateToRegister)
    }


}
