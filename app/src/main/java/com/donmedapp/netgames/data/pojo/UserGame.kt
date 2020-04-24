package com.donmedapp.netgames.data.pojo

import com.donmedapp.netgames.R
import com.google.firebase.firestore.Exclude

data class UserGame(
    @Exclude var games: ArrayList<Game>,
    @Exclude var name: String,
    @Exclude var avatar: Int
) {
    constructor() : this(arrayListOf(),"", R.drawable.ic_person_black_24dp)
}