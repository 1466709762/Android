package com.example.mychat1.UserDatabase

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User(var uri:String,var name:String,var number:String,var pai:String) {
    @PrimaryKey(autoGenerate = true)
    var id:Long = 0//0qian main bixu you kongge
}