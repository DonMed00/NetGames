package com.donmedapp.netgames.ui

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class MainViewModel : ViewModel(){


     var mAuth : FirebaseAuth =FirebaseAuth.getInstance()
}