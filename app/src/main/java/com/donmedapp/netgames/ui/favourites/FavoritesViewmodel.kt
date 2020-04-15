package com.donmedapp.netgames.ui.favourites

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.donmedapp.netgames.RawgApi
import com.donmedapp.netgames.Result
import com.donmedapp.netgames.data.pojo.UserGame
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FavoritesViewmodel(
    private val application: Application
) : ViewModel() {


    private val retrofit = Retrofit.Builder().baseUrl("https://api.rawg.io/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()




    private val rawgService = retrofit.create(RawgApi::class.java)


    private var _games: MutableLiveData<ArrayList<Result>> = MutableLiveData()
    val games: LiveData<ArrayList<Result>>
        get() = _games


    private var _gamesId: MutableLiveData<ArrayList<Int>> = MutableLiveData()
    val gamesId: LiveData<ArrayList<Int>>
        get() = _gamesId

    private var _game: MutableLiveData<Result> = MutableLiveData()
    val game: LiveData<Result>
        get() = _game



    fun setGameId(listId : ArrayList<Int>){
        _gamesId.value=listId
    }
    fun addGame(game : Result){
        _games.value!!.add(game)
    }



    fun getGame(id : Long) {

        GlobalScope.launch {
            if(_games.value.isNullOrEmpty()){
                _games.postValue(arrayListOf(rawgService.getGame(id)))

            }else{
                _games.postValue(_games.value!!.plus(rawgService.getGame(id)) as ArrayList<Result>?)

            }
        }

    }
}