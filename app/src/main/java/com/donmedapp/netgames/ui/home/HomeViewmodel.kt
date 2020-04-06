package com.donmedapp.netgames.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.donmedapp.netgames.RawgApi
import com.donmedapp.netgames.Result
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


    private val rawgService = retrofit.create(RawgApi::class.java)


    private var _games: MutableLiveData<List<Result>> = MutableLiveData()
    val games: LiveData<List<Result>>
        get() = _games

    init {
        getBestGamesOfYear()
    }


    private fun getBestGamesOfYear() {

        GlobalScope.launch {
           _games.postValue(rawgService.orderByGenres("strategy").results.sortedByDescending { it.rating })
            //.sortedByDescending { it.rating })
        }

    }
}