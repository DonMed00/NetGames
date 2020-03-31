package com.donmedapp.netgames.ui.register


import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import com.donmedapp.netgames.R
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
    private lateinit var progressBar: ProgressDialog
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    //global variables
    private var firstName by Delegates.notNull<String>()
    private var lastName by Delegates.notNull<String>()
    private var email by Delegates.notNull<String>()
    private var password by Delegates.notNull<String>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        initialise()
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
/*Creamos un método para inicializar nuestros elementos del diseño y la autenticación y la base de datos de firebase*/

    private fun initialise() {

        //Creamos nuestro progressDialog
        progressBar = ProgressDialog(activity)

/*Creamos una instancia para guardar los datos del usuario en nuestra base  de datos*/
        database = FirebaseDatabase.getInstance()
/*Creamos una instancia para crear nuestra autenticación y guardar el usuario*/
        auth = FirebaseAuth.getInstance()

/*reference nosotros leemos o escribimos en una ubicación específica la base de datos*/
        databaseReference = database.reference.child("Users")
    }

    //Vamos a crear nuestro método para crear una nueva cuenta
    private fun createNewAccount() {

        //Obtenemos los datos de nuestras cajas de texto
        firstName = txtName.text.toString()
        email = txtEmail.text.toString()
        password = txtPassword.text.toString()

//Verificamos que los campos estén llenos
        if (firstName.isNotEmpty() && email.isNotEmpty()
            && password.isNotEmpty()
        ) {

/*Antes de iniciar nuestro registro bloqueamos la pantalla o también podemos usar una barra de proceso por lo que progressbar está obsoleto*/

            progressBar.setMessage("Usuario registrado...")
            progressBar.show()

//vamos a dar de alta el usuario con el correo y la contraseña
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity!!) {

                    //Si está en este método quiere decir que todo salio bien en la autenticación

/*Una vez que se dio de alta la cuenta vamos a dar de alta la información en la base de datos*/

/*Vamos a obtener el id del usuario con que accedio con currentUser*/
                    val user: FirebaseUser = auth.currentUser!!
//enviamos email de verificación a la cuenta del usuario
                    verifyEmail(user);
/*Damos de alta la información del usuario enviamos el la referencia para guardarlo en la base de datos  de preferencia enviamos el id para que no se repita*/
                    val currentUserDb = databaseReference.child(user.uid)
//Agregamos el nombre y el apellido dentro de user/id/
                    currentUserDb.child("userName").setValue(firstName)
//Por último nos vamos a la vista home
                    updateUserInfoAndGoHome()

                }.addOnFailureListener {
                    // si el registro falla se mostrara este mensaje
                    Toast.makeText(
                        activity, "Error en la autenticación.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        } else {
            Toast.makeText(activity, "Llene todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    //llamamos el método de crear cuenta en la accion registrar


    private fun updateUserInfoAndGoHome() {
        //Nos vamos a la actividad home

//ocultamos el progress
        progressBar.hide()

    }

    private fun verifyEmail(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener(activity!!) {
//Verificamos que la tarea se realizó correctamente
                    task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        activity,
                        "Email " + user.email,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        activity,
                        "Error al verificar el correo ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
