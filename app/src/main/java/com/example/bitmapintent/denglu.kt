package com.example.bitmapintent

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import com.example.mychat1.UserDatabase.AppDatabase
import com.example.mychat1.UserDatabase.User
import com.example.mychat1.UserDatabase.UserDao
import kotlin.concurrent.thread

class denglu : AppCompatActivity() {

        val fromALbum = 1
        val uri: Uri = "".toUri()
        lateinit var img1: ImageView
        lateinit var userDAO: UserDao
        lateinit var user: User
        var UUUri: String = ""
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.denglu)
            //初始化各组件
            val denglu_name=findViewById<EditText>(R.id.denglu_name)
            val denglu_number=findViewById<EditText>(R.id.denglu_number)
            val denglu_pai=findViewById<EditText>(R.id.denglu_pai)

            val list=ArrayList<User>()
            img1 = findViewById<ImageView>(R.id.imgdeng)
            userDAO = AppDatabase.getDatabase(this).UserDao()


            thread {
                userDAO.insertNote(User("", "1", "1", "1"))
             //   val a = userDAO.loadByID(1)
             //   a.uri = ""
             //   userDAO.updateUser(a)

            }.join()
        //

            val button2 = findViewById<Button>(R.id.zhuche)


            button2.setOnClickListener {
                var a = ""
                thread {
                    val b = userDAO.loadByID(1).uri
                    a = b
                }.join()
                if (a != "") {
                   thread {
                       val user=User(a,denglu_name.text.toString(),denglu_number.text.toString(),denglu_pai.text.toString())
                       userDAO.insertNote(user)
                   }
                } else {
                    Toast.makeText(this, "请选择自己的头像再注册", Toast.LENGTH_SHORT).show()
                }
            }





            img1.setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"

                startActivityForResult(intent, fromALbum)
                Log.d("1", uri.toString())

            }

            val denglu=findViewById<Button>(R.id.denglu)
            denglu.setOnClickListener {
                thread {
                    for (a in userDAO.loadAllNotes()){
                        list.add(a)
                        Log.d("zhengchang",a.uri)
                    }
                }.join()
                var i=0
                while (i<list.size){
                   val name1=denglu_name.text.toString()
                   val password1=denglu_number.text.toString()
                    if (name1==list[i].name&&password1==list[i].number) {
                        val intent=Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    i++
                }

            }



        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            when (requestCode) {
                fromALbum -> {
                    if (resultCode == Activity.RESULT_OK && data != null) {

                        data.data?.let { uri ->
                            val bitmap = getBitmapFromUri(uri)
                            img1.setImageBitmap(bitmap)
                            Log.d(uri.toString(), "1")
                            thread {
                                user = userDAO.loadByID(1)
                                user.uri = uri.toString()
                                userDAO.updateUser(user)
                            }

                        }
                    }
                }
            }
        }

        private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri, "r")?.use {
            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
        }
    }
