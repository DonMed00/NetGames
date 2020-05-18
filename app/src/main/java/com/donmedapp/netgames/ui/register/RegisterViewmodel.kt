package com.donmedapp.netgames.ui.register

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
import com.donmedapp.netgames.data.pojo.UserGame
import com.donmedapp.netgames.extensions.hideSoftKeyboard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterViewmodel(var application: Application, var activity: Activity) : ViewModel() {

    val settings: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(activity)
    }


    private val _message: MutableLiveData<Event<String>> = MutableLiveData()
    val message: LiveData<Event<String>> get() = _message

    private val _onBack: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val onBack: LiveData<Event<Boolean>> get() = _onBack


    private var auth: FirebaseAuth = FirebaseAuth.getInstance()


    fun createNewAccount(view: View, email: String, password: String, nickname: String) {
        view.hideSoftKeyboard()
        if (email.isNotEmpty()
            && password.isNotEmpty()
            && nickname.isNotEmpty()
        ) {
            auth.createUserWithEmailAndPassword(
                email,
                password
            )
                .addOnCompleteListener(activity) {
                    if (it.isSuccessful) {
                        setupFirebaseData(nickname)
                        verifyEmail()
                    }

                }.addOnFailureListener {
                    _message.value = Event(it.message!!)

                }
        } else {
            _message.value = Event(application.getString(R.string.register_rellene_toast))
        }
    }

    fun setupFirebaseData(nickname: String) {
        val myDB = FirebaseFirestore.getInstance()
        val gameNew = myDB.collection("users").document(auth.currentUser!!.uid)
        gameNew.get().addOnSuccessListener {
            val userGames = UserGame(
                arrayListOf(),
                nickname,
                R.drawable.no_imagen
            )
            myDB.collection("users").document(auth.currentUser!!.uid)
                .set(userGames)

        }

    }

    fun verifyEmail() {
        auth.currentUser!!.sendEmailVerification()
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    _message.value = Event(application.getString(R.string.register_confirm_account))
                    _onBack.value = Event(true)
                } else {
                    _message.value = Event(
                        task.exception!!.message!!
                    )
                }
            }
    }

}