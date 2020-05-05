package com.donmedapp.netgames.ui.game

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameViewmodelFactory(private val activity: Activity,
                           private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        GameViewmodel( activity,application) as T
}
