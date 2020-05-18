package com.donmedapp.netgames.ui.game

import android.app.Activity
import android.app.Application
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.donmedapp.netgames.R
import com.donmedapp.netgames.RawgApi
import com.donmedapp.netgames.Result
import com.donmedapp.netgames.Screenshot2
import com.donmedapp.netgames.base.Event
import com.donmedapp.netgames.data.pojo.Game
import com.donmedapp.netgames.data.pojo.UserGame
import com.donmedapp.netgames.ui.MainViewModel
import com.donmedapp.netgames.utils.isNetDisponible
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameViewmodel(
    private val activity: Activity,
    private val application: Application
) : ViewModel() {


    private val retrofit = Retrofit.Builder().baseUrl("https://api.rawg.io/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val rawgService = retrofit.create(RawgApi::class.java)

    var viewmodelActivity: MainViewModel = MainViewModel()
    val myDB = FirebaseFirestore.getInstance()
    val gameNew = myDB.collection("users").document(viewmodelActivity.mAuth.currentUser!!.uid)


    private var _game: MutableLiveData<Result> = MutableLiveData()
    val game: LiveData<Result>
        get() = _game


    private var _screenGame: MutableLiveData<Screenshot2> = MutableLiveData()
    val screenGame: LiveData<Screenshot2>
        get() = _screenGame


    private var _gameId: MutableLiveData<Long> = MutableLiveData(0)


    private val _message: MutableLiveData<Event<String>> = MutableLiveData()
    val message: LiveData<Event<String>> get() = _message


    fun getGame(id: Long) {

        GlobalScope.launch {
            _game.postValue(rawgService.getGame(id))
        }

    }

    fun getScreenGame(id: Long) {
        GlobalScope.launch {
            _screenGame.postValue(rawgService.getScreenshotsOfGame(id))
        }

    }

    fun addGame() {
        if (isNetDisponible(activity.applicationContext)) {
            gameNew.get().addOnSuccessListener { documentSnapshot ->
                val userGames = documentSnapshot.toObject(UserGame::class.java)
                game.value.run {
                    userGames!!.games.add(
                        Game(
                            this!!.id.toString(),
                            name,
                            backgroundImage ?: "",
                            released
                        )
                    )
                }
                //val userG = UserGame(userGames!!.games,viewmodelActivity.mAuth.currentUser!!.uid)
                myDB.collection("users").document(viewmodelActivity.mAuth.currentUser!!.uid)
                    .set(userGames!!)
            }
            _message.value = Event(application.getString(R.string.game_gameadded))

        } else {
            _message.value = Event(application.getString(R.string.no_conection_detected))
        }

    }

    fun removeGame() {
        if (isNetDisponible(activity.applicationContext)) {
            gameNew.get().addOnSuccessListener { documentSnapshot ->
                val userGames = documentSnapshot.toObject(UserGame::class.java)
                game.value.run {
                    userGames!!.games.remove(
                        Game(
                            this!!.id.toString(),
                            name,
                            backgroundImage ?: "",
                            released
                        )
                    )
                }
                //val userG = UserGame(userGames!!.games,viewmodelActivity.mAuth.currentUser!!.uid)
                myDB.collection("users").document(viewmodelActivity.mAuth.currentUser!!.uid)
                    .set(userGames!!)
            }
            _message.value = Event(application.getString(R.string.game_gameremoved))

        } else {
            _message.value = Event(application.getString(R.string.no_conection_detected))
        }
    }

    fun setMessage(text: String) {
        _message.value = Event(text)
    }


    fun navigateToStore(spinner: Spinner): String {
        return if (spinner.selectedItem.toString() != application.getString(R.string.no_stores)) {
            val store = game.value!!.stores!![spinner.selectedItemPosition]
            var url = store.url!!
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://$url"
            url
        } else {
            _message.value = Event(application.getString(R.string.game_noshops))
            ""
        }
    }

}