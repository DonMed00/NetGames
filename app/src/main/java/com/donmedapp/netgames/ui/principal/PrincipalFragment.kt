package com.donmedapp.netgames.ui.principal


import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController

import com.donmedapp.netgames.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.principal_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class PrincipalFragment : Fragment(R.layout.principal_fragment) {

    private val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }
    private lateinit var mAuth: FirebaseAuth

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.run {
            setTitle(R.string.principal_title)
        }
        mAuth = FirebaseAuth.getInstance()


        if (settings.getString("currentUser", "NOUSER") != "NOUSER") {
                loginUser()
                lblPrueba.text= mAuth.currentUser!!.email.toString()

               // Toast.makeText(activity, "No hay usuarios", Toast.LENGTH_SHORT).show()

        } else {
            findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun loginUser() {
        //Obtenemos usuario y contraseña
        //Verificamos que los campos no este vacios
        mAuth.signInWithEmailAndPassword(
            settings.getString("currentUser", "NOUSER")!!,
            settings.getString("currentPassword", "NOPASSWORD")!!
        )
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        activity, "Inicio de sesion correcto",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Si se inició correctamente la sesión vamos a la vista Home de la aplicación
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
}
