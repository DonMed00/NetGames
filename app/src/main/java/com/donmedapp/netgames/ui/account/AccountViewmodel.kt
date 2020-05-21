package com.donmedapp.netgames.ui.account

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.view.View
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import com.donmedapp.netgames.R
import com.donmedapp.netgames.base.Event
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.donmedapp.netgames.ui.MainViewModel
import com.google.firebase.auth.EmailAuthProvider

class AccountViewmodel(var application: Application,var activity: Activity) : ViewModel() {

     val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }


    private val _message: MutableLiveData<Event<String>> = MutableLiveData()
    val message: LiveData<Event<String>> get() = _message

    private val _messageT: MutableLiveData<Event<String>> = MutableLiveData()
    val messageT: LiveData<Event<String>> get() = _messageT

    var viewmodelActivity: MainViewModel = MainViewModel()

    fun updatePassword(view: View, password: String) {
        view.hideSoftKeyboard()
        view.clearFocus()
        if (password.isNotEmpty()) {
            if(settings.getString(
                    "currentPassword",
                    application.getString(R.string.no_password)
                )!=password) {


                val user = viewmodelActivity.mAuth.currentUser
                // Get auth credentials from the user for re-authentication
                // Get auth credentials from the user for re-authentication
                val credential = EmailAuthProvider
                    .getCredential(
                        settings.getString(
                            "currentUser",
                            application.getString(R.string.no_user)
                        )!!,
                        settings.getString(
                            "currentPassword",
                            application.getString(R.string.no_password)
                        )!!
                    ) // Current Login Credentials \\

                // Prompt the user to re-provide their sign-in credentials
                // Prompt the user to re-provide their sign-in credentials
                user!!.reauthenticate(credential)
                    .addOnCompleteListener {
                        val user = viewmodelActivity.mAuth.currentUser
                        user!!.updatePassword(password)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    settings.edit {
                                        putString("currentPassword", password)
                                    }
                                    _message.value =
                                        Event(application.getString(R.string.account_password_changed))
                                } else {
                                    _message.value = Event(task.exception!!.message!!)
                                }
                            }
                    }
            }else{
                _message.value =
                    Event(application.getString(R.string.account_password_different))
            }
        } else {
            _message.value =
                Event(application.getString(R.string.register_rellene_toast))
        }
    }

    fun updateEmail(view: View, email: String) {
        view.hideSoftKeyboard()
        view.clearFocus()
        if (email.isNotEmpty()) {
            if(settings.getString("currentUser", application.getString(R.string.no_user)
                )!=email) {
                val user = viewmodelActivity.mAuth.currentUser
                // Get auth credentials from the user for re-authentication
                // Get auth credentials from the user for re-authentication
                val credential = EmailAuthProvider
                    .getCredential(
                        settings.getString(
                            "currentUser",
                            application.getString(R.string.no_user)
                        )!!,
                        settings.getString(
                            "currentPassword",
                            application.getString(R.string.no_password)
                        )!!
                    ) // Current Login Credentials \\

                // Prompt the user to re-provide their sign-in credentials
                // Prompt the user to re-provide their sign-in credentials
                user!!.reauthenticate(credential)
                    .addOnCompleteListener {
                        //Now change your email address \\
                        //----------------Code for Changing Email Address----------\\
                        val user = viewmodelActivity.mAuth.currentUser
                        user!!.updateEmail(email)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    settings.edit {
                                        putString("currentUser", email)
                                    }
                                    _message.value =
                                        Event(application.getString(R.string.account_email_changed))

                                } else {
                                    _message.value = Event(task.exception!!.message!!)
                                }
                            }
                    }
            }else{
                _message.value =
                    Event(application.getString(R.string.account_email_different))
            }
        } else {
            _message.value =
                Event(application.getString(R.string.register_rellene_toast))
        }
    }

    fun seeLicenses() {
        val firstClicked = settings.getBoolean("firstClicked", true)
        val countTimes = settings.getInt("countTimes", 5)
        if(firstClicked){

            if(countTimes>0){
                _messageT.value=Event(activity.getString(R.string.licenses_toast,countTimes.toString()))
                settings.edit {
                    putInt("countTimes", countTimes-1)
                }
            }else{
                //_messageT.value=Event("Licencias abiertas")
                settings.edit {
                    putBoolean("firstClicked", false)
                }
            }
        }else{
            //_messageT.value=Event("Licencias abiertas jejeje")

        }
    }

}