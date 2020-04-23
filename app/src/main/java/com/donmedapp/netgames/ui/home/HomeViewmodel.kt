package com.donmedapp.netgames.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

class HomeViewmodel(
    private val application: Application
) : ViewModel() {


    private val retrofit = Retrofit.Builder().baseUrl("https://api.rawg.io/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val myDB = FirebaseFirestore.getInstance()
    private val gameNew = myDB.collection("users").document(MainViewModel().mAuth.currentUser!!.uid)


    private val rawgService = retrofit.create(RawgApi::class.java)


    private var _gamesAction: MutableLiveData<List<Result>> = MutableLiveData()
    val gamesAction: LiveData<List<Result>>
        get() = _gamesAction

    private var _gamesStrategy: MutableLiveData<List<Result>> = MutableLiveData()
    val gamesStrategy: LiveData<List<Result>>
        get() = _gamesStrategy


    private var _gamesSports: MutableLiveData<List<Result>> = MutableLiveData()
    val gamesSports: LiveData<List<Result>>
        get() = _gamesSports


    private var _gamesAdventure: MutableLiveData<List<Result>> = MutableLiveData()
    val gamesAdventure: LiveData<List<Result>>
        get() = _gamesAdventure

    private var _gamesSimulation: MutableLiveData<List<Result>> = MutableLiveData()
    val gamesSimulation: LiveData<List<Result>>
        get() = _gamesSimulation

    private var _myGamesList: MutableLiveData<List<Game>> = MutableLiveData()
    val myGamesList: LiveData<List<Game>>
        get() = _myGamesList

    init {
        getGamesByGenre(_gamesAction,"action")

        getGamesByGenre(_gamesStrategy,"strategy")

        getGamesByGenre(_gamesSports,"sports")

        getGamesByGenre(_gamesAdventure,"adventure")

        getGamesByGenre(_gamesSimulation,"simulation")

        //setupData()


    }

    fun setupData() {
        gameNew.get().addOnSuccessListener { documentSnapshot ->
            _myGamesList.value = documentSnapshot.toObject(UserGame::class.java)!!.games


        }
    }


     private fun getGamesByGenre(list : MutableLiveData<List<Result>>, filter: String) {

        GlobalScope.launch {
            list.postValue(rawgService.orderByGenres(filter).results.sortedByDescending { it.rating })
            //.sortedByDescending { it.rating })
        }

    }
}