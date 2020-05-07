package com.donmedapp.netgames.ui.edit

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.donmedapp.netgames.R
import com.donmedapp.netgames.base.Event
import com.donmedapp.netgames.data.pojo.UserGame
import com.donmedapp.netgames.ui.MainViewModel

class EditViewmodel(
    private val application: Application
) : ViewModel() {


    private val _message: MutableLiveData<Event<String>> = MutableLiveData()
    val message: LiveData<Event<String>> get() = _message




    var viewmodelActivity: MainViewModel = MainViewModel()


    private var _avatar: MutableLiveData<Int> = MutableLiveData(-1)
    val avatar: LiveData<Int>
        get() = _avatar

    private var _avatarName: MutableLiveData<String> = MutableLiveData("")
    val avatarName: LiveData<String>
        get() = _avatarName

    fun changeAvatar(avatar: Int) {
        _avatar.value = avatar
    }

    init {
        initAvatar()
    }

    private fun initAvatar() {
        viewmodelActivity.gameNew.get().addOnSuccessListener { documentSnapshot ->
            val userGames = documentSnapshot.toObject(UserGame::class.java)
            _avatar.value = userGames!!.avatar
            _avatarName.value = userGames.name

        }
    }

    fun setName(text: String) {
        _avatarName.value = text
    }

    fun setupAvatar() {

        viewmodelActivity.gameNew.get().addOnSuccessListener { documentSnapshot ->
            val userGames = documentSnapshot.toObject(UserGame::class.java)
            if (avatar.value != -1) {
                userGames!!.avatar = _avatar.value!!
                userGames.name = _avatarName.value!!

                viewmodelActivity.myDB.collection("users")
                    .document(viewmodelActivity.mAuth.currentUser!!.uid)
                    .set(userGames)

                _message.value = Event(application.getString(R.string.avatar_changed))
            }


        }
    }
}