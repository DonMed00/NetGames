package com.donmedapp.netgames.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.donmedapp.netgames.data.pojo.UserGame
import com.donmedapp.netgames.ui.MainViewModel

class UserViewmodel : ViewModel() {

    var viewmodelActivity: MainViewModel = MainViewModel()



    private var _avatar: MutableLiveData<Int> = MutableLiveData()


}