package com.example.messengerclone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Latest(var message:String,var userId:String,var profileUrl:String,var username:String):Parcelable {
    constructor() : this("","", "", "")
}