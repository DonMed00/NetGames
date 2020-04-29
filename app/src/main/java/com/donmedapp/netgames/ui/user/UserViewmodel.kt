package com.donmedapp.netgames.ui.user

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.donmedapp.netgames.data.pojo.UserGame
import com.donmedapp.netgames.ui.MainViewModel

class UserViewmodel(
    private val application: Application
) : ViewModel() {

    var viewmodelActivity: MainViewModel = MainViewModel()



    private var _avatar: MutableLiveData<Int> = MutableLiveData()
    val avatar: LiveData<Int>
        get() = _avatar



    init {
        //setupAvatar()
    }


    fun setupAvatar() {
        viewmodelActivity.gameNew.get().addOnSuccessListener { documentSnapshot ->
            _avatar.value = documentSnapshot.toObject(UserGame::class.java)!!.avatar
        }
    }


}