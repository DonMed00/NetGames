package com.donmedapp.netgames.ui.account

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AccountViewmodelFactory(
    private val application: Application,
    private val activity: Activity
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        AccountViewmodel( application,activity) as T
}