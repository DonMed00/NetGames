package com.donmedapp.netgames.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.donmedapp.netgames.data.pojo.Game
import com.donmedapp.netgames.data.pojo.UserGame
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel : ViewModel() {


    private var gameNew: DocumentReference
    var mAuth: FirebaseAuth


    private val myDB = FirebaseFirestore.getInstance()


    private var _gamesFav: MutableLiveData<ArrayList<Game>> = MutableLiveData(arrayListOf())
    val gamesFav: LiveData<ArrayList<Game>>
        get() = _gamesFav


    init {
        mAuth = FirebaseAuth.getInstance()
        gameNew = myDB.collection("users").document(mAuth.currentUser!!.uid)
        setupData()
    }


    fun setupData() {
        gameNew.get().addOnSuccessListener { documentSnapshot ->
            _gamesFav.value = documentSnapshot.toObject(UserGame::class.java)!!.games


        }
    }

}