package com.donmedapp.netgames.ui.login


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

    //private lateinit var mProgressBar: ProgressDialog
    var viewmodel : MainViewModel= MainViewModel()

    //Creamos nuestra variable de autenticación firebase
   // private lateinit var mAuth: FirebaseAuth

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //findNavController().graph.remove()
        if (settings.getString("currentUser", getString(R.string.no_user)) != getString(R.string.no_user)) {
            loginUserAuth()
            //lblPrueba.text = mAuth.currentUser!!.email.toString()

            // Toast.makeText(activity, "No hay usuarios", Toast.LENGTH_SHORT).show()


        }
        setupViews()

    }

    private fun loginUserAuth() {
        //Obtenemos usuario y contraseña
        //Verificamos que los campos no este vacios
        viewmodel.mAuth.signInWithEmailAndPassword(
            settings.getString("currentUser", getString(R.string.no_user))!!,
            settings.getString("currentPassword", getString(R.string.no_password))!!
        )
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    //Toast.makeText(
                    //  activity, "Inicio de sesion correcto",
                    //  Toast.LENGTH_SHORT
                    // ).show()
                    // Si se inició correctamente la sesión vamos a la vista Home de la aplicación
                    findNavController().navigate(R.id.navigateToHome)

                } else {
                    // sino le avisamos el usuairo que orcurrio un problema
                    //mProgressBar.hide()
                    Toast.makeText(
                        activity, "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun setupViews() {
        setupAppBar()
        initialise()
        setupBtns()
        setHasOptionsMenu(true)
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
        findNavController().navigate(R.id.navigateToForgotPassword)
    }

    /*Creamos un método para inicializar nuestros elementos del diseño y la autenticación de firebase*/
    private fun initialise() {
        // mProgressBar = ProgressDialog(activity)
    }

    //Ahora vamos a Iniciar sesión con firebase es muy sencillo
    private fun loginUser() {
        //Obtenemos usuario y contraseña
        //Verificamos que los campos no este vacios
        viewmodel.mAuth.signInWithEmailAndPassword(
            txtEmail.text.toString(), txtPassword.text.toString()
        )
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        activity, "Inicio de sesion correcto",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Si se inició correctamente la sesión vamos a la vista Home de la aplicación
                    goPrincipal()
                } else {
                    // sino le avisamos el usuairo que orcurrio un problema
                    //mProgressBar.hide()
                    Toast.makeText(
                        activity, "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    private fun goPrincipal() {
//Ocultamos el progress
        //mProgressBar.hide()
        settings.edit {
            putString("currentUser", txtEmail.text.toString())
            putString("currentPassword", txtPassword.text.toString())
        }
        txtEmail.hideSoftKeyboard()
        findNavController().navigate(R.id.navigateToHome)

    }

/* Tenemos que crear nuestros métodos con el mismo nombre que le agregamos a nuestros botones en el atributo onclick y forzosamente tenemos que recibir un parámetro view para que sea reconocido como click de nuestro button ya que en view podemos modificar los atributos*/

    /*Primero creamos nuestro evento login dentro de este llamamos nuestro método loginUser al dar click en el botón se iniciara sesión */
    fun login() {
        if (txtEmail.text.toString().isNotEmpty() && txtPassword.text.toString().isNotEmpty()) {
            loginUser()
        } else {
            Toast.makeText(activity, "esta vacio", Toast.LENGTH_SHORT).show()
        }

    }

/*Si se olvido de la contraseña lo enviaremos en la siguiente actividad nos marcara error porque necesitamos crear la actividad*/

    fun forgotPassword(view: View) {

    }

    /*Si quiere registrarse lo enviaremos en la siguiente actividad nos marcara error porque necesitamos crear la actividad*/
    private fun register() {
        findNavController().navigate(R.id.navigateToRegister)
    }


}
