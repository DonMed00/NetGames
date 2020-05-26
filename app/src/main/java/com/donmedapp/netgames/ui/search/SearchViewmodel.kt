package com.donmedapp.netgames.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.donmedapp.netgames.RawgApi
import com.donmedapp.netgames.Result
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchViewmodel : ViewModel() {


    private val retrofit = Retrofit.Builder().baseUrl("https://api.rawg.io/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val rawgService = retrofit.create(RawgApi::class.java)


    private var _games: MutableLiveData<List<Result>> = MutableLiveData()
    val games: LiveData<List<Result>>
        get() = _games
    private val listOfWordsOfQuery: List<String> = listOf(
        "fifa",
        "call of duty",
        "counter strike",
        "gta",
        "f1",
        "fortnite",
        "ufc",
        "wwe",
        "battlefield",
        "dragon ball",
        "street",
        "uncharted"
    )

    init {
        setupInitSearch(listOfWordsOfQuery)
    }

    private fun setupInitSearch(listOfWordsOfQuery: List<String>) {
        search(listOfWordsOfQuery.shuffled()[0])
    }


    fun search(text: String) {
        GlobalScope.launch {
            _games.postValue(
                rawgService.getGames(
                    text,
                    1,
                    50
                ).results.sortedByDescending { it.rating })
        }

    }

}