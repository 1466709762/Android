package com.example.bitmapintent

import android.graphics.Bitmap

///信息类型
class Msg(val content: String, val type: Int) {
    var bitmap:Bitmap?=null
    var bitmap2:Int=0
    var myname:String?=null
    companion object {
        const val TYPE_RECEIVED = 0
        const val TYPE_SENT = 1
        const val TYPE_Pai = 2
    }
}