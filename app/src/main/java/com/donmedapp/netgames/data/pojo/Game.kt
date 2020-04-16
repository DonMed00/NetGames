package com.donmedapp.netgames.data.pojo

import com.google.firebase.firestore.Exclude

data class Game(@Exclude val gameId: String,
                @Exclude val name: String,
                @Exclude val backgroundImg: String,
                @Exclude val released: String
) {
    constructor() : this("","","","")

}