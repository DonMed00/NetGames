package com.donmedapp.netgames.ui.forgotPassword

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ForgotPasswordViewmodelFactory(
    private val application: Application,
    private val activity: Activity
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ForgotPasswordViewmodel( application,activity) as T
}