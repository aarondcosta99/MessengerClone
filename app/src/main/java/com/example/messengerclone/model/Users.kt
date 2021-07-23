package com.example.messengerclone.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Users(var username:String, var profileUrl:String,var userId:String ):Parcelable{
    constructor():this("","","")
}

