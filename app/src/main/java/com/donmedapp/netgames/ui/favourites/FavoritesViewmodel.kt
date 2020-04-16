package com.donmedapp.netgames.ui.favourites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import com.donmedapp.netgames.RawgApi
import com.donmedapp.netgames.Result
import com.donmedapp.netgames.data.pojo.Game
import com.donmedapp.netgames.data.pojo.UserGame
import com.donmedapp.netgames.ui.MainViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FavoritesViewmodel(
    private val application: Application
) : ViewModel() {

    private val myDB = FirebaseFirestore.getInstance()
    private val gameNew = myDB.collection("users").document(MainViewModel().mAuth.currentUser!!.uid)


    private var _games: MutableLiveData<ArrayList<Game>> = MutableLiveData(arrayListOf())
    val games: LiveData<ArrayList<Game>>
        get() = _games



    init {
        setupData()
    }


     fun setupData() {
        gameNew.get().addOnSuccessListener { documentSnapshot ->
            _games.value = documentSnapshot.toObject(UserGame::class.java)!!.games


         }
    }

}