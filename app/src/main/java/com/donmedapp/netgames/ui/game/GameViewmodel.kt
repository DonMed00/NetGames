package com.donmedapp.netgames.ui.game

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.donmedapp.netgames.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameViewmodel(
    private val application: Application
) : ViewModel() {


    private val retrofit = Retrofit.Builder().baseUrl("https://api.rawg.io/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val rawgService = retrofit.create(RawgApi::class.java)


    private var _game: MutableLiveData<Result> = MutableLiveData()
    val game: LiveData<Result>
        get() = _game


    private var _screenGame: MutableLiveData<Screenshot2> = MutableLiveData()
    val screenGame: LiveData<Screenshot2>
        get() = _screenGame


    private var _gameId: MutableLiveData<Long> = MutableLiveData(0)
    val gameId: LiveData<Long>
        get() = _gameId

    init {
        //getGame(_gameId.value!!)
    }
    fun setGameId(id : Long) {
        _gameId.value=id

    }

     fun getGame(id : Long) {

        GlobalScope.launch {
           _game.postValue(rawgService.getGame(id))
        }

    }

    fun getScreenGame(id : Long) {
        GlobalScope.launch {
             _screenGame.postValue(rawgService.get2Game(id))
        }

    }



}