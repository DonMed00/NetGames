package com.donmedapp.netgames.ui.forgotPassword

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.donmedapp.netgames.R
import com.donmedapp.netgames.base.Event
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewmodel(var application: Application, var activity: Activity) : ViewModel() {

    val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }


    private val _message: MutableLiveData<Event<String>> = MutableLiveData()
    val message: LiveData<Event<String>> get() = _message

    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun sendPasswordResetEmail(view: View, txtPasswordReset: String): Boolean {
        view.hideSoftKeyboard()
        view.clearFocus()
        var flag = false
        if (txtPasswordReset.isNotEmpty()) {
            mAuth
                .sendPasswordResetEmail(txtPasswordReset)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        flag = true
                        _message.value = Event(application.getString(R.string.forgot_email_enviado))

                    } else {
                        _message.value = Event(task.exception!!.message!!)
                    }
                }
        } else {
            _message.value = Event(application.getString(R.string.forgot_rellenar_campos))

        }

        return flag
    }
}