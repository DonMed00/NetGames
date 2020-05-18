package com.donmedapp.netgames.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.donmedapp.netgames.R
import com.donmedapp.netgames.base.Event
import com.donmedapp.netgames.data.pojo.Game
import com.donmedapp.netgames.data.pojo.UserGame
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel : ViewModel() {

    val myDB = FirebaseFirestore.getInstance()
    lateinit var gameNew: DocumentReference
    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()


    private val _message: MutableLiveData<Event<String>> = MutableLiveData()
    val message: LiveData<Event<String>> get() = _message

    private var _gamesFav: MutableLiveData<ArrayList<Game>> = MutableLiveData(arrayListOf())
    val gamesFav: LiveData<ArrayList<Game>>
        get() = _gamesFav


    private var _avatar: MutableLiveData<Int> = MutableLiveData()
    val avatar: LiveData<Int>
        get() = _avatar

    private var _avatarName: MutableLiveData<String> = MutableLiveData()
    val avatarName: LiveData<String>
        get() = _avatarName


    private val _onBack: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val onBack: LiveData<Event<Boolean>> get() = _onBack

    //var activity = MainActivity()




    fun setMessage(text: String) {
        _message.value = Event(text)
    }

    fun changeAvatar(avatar: Int) {
        _avatar.value = avatar
    }

    fun setName(text: String) {
        _avatarName.value = text
    }

    fun setupData() {
        gameNew = myDB.collection("users").document(mAuth.currentUser!!.uid)
        gameNew.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.toObject(UserGame::class.java) == null) {
                _gamesFav.value = arrayListOf()
                _avatar.value = R.drawable.ic_person_black_24dp
                _avatarName.value = ""
            } else {
                _gamesFav.value = documentSnapshot.toObject(UserGame::class.java)!!.games
                _avatarName.value = documentSnapshot.toObject(UserGame::class.java)!!.name
                _avatar.value = documentSnapshot.toObject(UserGame::class.java)!!.avatar


            }

        }
    }

    fun setupAvatar(texto : String) {
        gameNew = myDB.collection("users").document(mAuth.currentUser!!.uid)
        gameNew.get().addOnSuccessListener { documentSnapshot ->
            val userGames = documentSnapshot.toObject(UserGame::class.java)
            if (avatar.value != -1) {
                userGames!!.avatar = _avatar.value!!
                userGames.name = _avatarName.value!!

                myDB.collection("users")
                    .document(mAuth.currentUser!!.uid)
                    .set(userGames)
                _message.value =Event(texto)
                _onBack.value=Event(true)

            }


        }
    }
}