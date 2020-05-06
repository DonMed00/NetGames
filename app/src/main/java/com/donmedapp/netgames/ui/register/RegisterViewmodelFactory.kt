package com.donmedapp.netgames.ui.register

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RegisterViewmodelFactory(
    private val application: Application,
    private val activity: Activity
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        RegisterViewmodel( application,activity) as T
}