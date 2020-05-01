package com.donmedapp.netgames.ui.favorites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.donmedapp.netgames.data.pojo.Game
import com.donmedapp.netgames.data.pojo.UserGame
import com.donmedapp.netgames.ui.MainViewModel
import com.google.firebase.firestore.FirebaseFirestore

class FavoritesViewmodel(
    private val application: Application
) : ViewModel() {

    private val myDB = FirebaseFirestore.getInstance()
    private val gameNew = myDB.collection("users").document(MainViewModel().mAuth.currentUser!!.uid)


    private var _games: MutableLiveData<ArrayList<Game>> = MutableLiveData(arrayListOf())
    val games: LiveData<ArrayList<Game>>
        get() = _games



    init {
        //setupData()
    }


     fun setupData() {
        gameNew.get().addOnSuccessListener { documentSnapshot ->
            _games.value = documentSnapshot.toObject(UserGame::class.java)!!.games


         }
    }

}