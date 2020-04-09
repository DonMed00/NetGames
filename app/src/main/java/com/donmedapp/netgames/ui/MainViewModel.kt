package com.donmedapp.netgames.ui

import androidx.lifecycle.ViewModel
import com.donmedapp.netgames.data.pojo.UserGame
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainViewModel : ViewModel() {


    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()


}