package com.example.bitmapintent

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.chat.*
import java.io.File

class Fragement:Fragment(){
    val takePhoto = 1
    val fromAlbum = 2
    lateinit var imageUri: Uri
    lateinit var outputImage: File
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.chat_fragement,container,false)

    }
    //拍照功能的实现
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val takephot=activity?.findViewById<ImageView>(R.id.take_photo)
        takephot?.setOnClickListener {


            // 启动相机程序
            val chatActivity=activity as ChatActivity

            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            chatActivity.takephoto()


        }
    }
    fun finish(){
        this.finish()

    }

}



