package com.donmedapp.netgames.data.pojo

import com.google.firebase.firestore.Exclude

data class UserGame(
    @Exclude var games: ArrayList<Int>,
    @Exclude val uid: String
) {
    constructor() : this(arrayListOf(), "")
}