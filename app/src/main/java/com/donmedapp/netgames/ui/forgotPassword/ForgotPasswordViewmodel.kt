package com.donmedapp.netgames.ui.forgotPassword

import android.app.Activity
import android.app.Application
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.donmedapp.netgames.R
import com.donmedapp.netgames.base.Event
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewmodel(var application: Application, var activity: Activity) : ViewModel() {


    private val _message: MutableLiveData<Event<String>> = MutableLiveData()
    val message: LiveData<Event<String>> get() = _message

    private val _onBack: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val onBack: LiveData<Event<Boolean>> get() = _onBack



    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun sendPasswordResetEmail(view: View, txtPasswordReset: String){
        view.hideSoftKeyboard()
        view.clearFocus()
        if (txtPasswordReset.isNotEmpty()) {
            mAuth
                .sendPasswordResetEmail(txtPasswordReset)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _message.value = Event(application.getString(R.string.forgot_email_enviado))
                        _onBack.value=Event(true)

                    } else {
                        _message.value = Event(task.exception!!.message!!)
                    }
                }
        } else {
            _message.value = Event(application.getString(R.string.forgot_rellenar_campos))
        }
    }
}