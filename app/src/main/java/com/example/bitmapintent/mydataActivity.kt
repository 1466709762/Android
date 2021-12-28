package com.example.bitmapintent

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import com.example.mychat1.UserDatabase.AppDatabase
import com.example.mychat1.UserDatabase.User
import com.example.mychat1.UserDatabase.UserDao
import kotlinx.android.synthetic.main.mydata.*
import kotlin.concurrent.thread

class mydataActivity : AppCompatActivity() {
    val fromALbum=1
    val uri:Uri="".toUri()
    lateinit var img1:ImageView
    lateinit var userDAO:UserDao
    lateinit var user:User
    lateinit var myuri:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val toolbar2=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar2)
        img1=findViewById(R.id.img1)
        userDAO=AppDatabase.getDatabase(this).UserDao()
        thread{
            userDAO.insertNote(User("","1","1","1"))
            val a= userDAO.loadByID(1)
            a.uri=""
            userDAO.updateUser(a)

        }.join()


        val button2=findViewById<Button>(R.id.save)


        button2.setOnClickListener {

            var a=""
            thread {
                val b=userDAO.loadByID(1).uri
                a=b
            }.join()
            if (a!=""){
                val pai=set_pai.text.toString()
                val name=set_name.text.toString();val number=set_number.text.toString()
                thread {
                    user=userDAO.loadByID(1)
                    user.uri=myuri.toString()
                    user.name=name
                    user.number=number
                    user.pai=pai
                    userDAO.updateUser(user)
                }.join()
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)}
            else{
                Toast.makeText(this,"请选择自己的头像",Toast.LENGTH_SHORT).show()
            }
        }




        val img2=findViewById<ImageView>(R.id.img2)


        img1.setOnClickListener {
            val intent= Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type="image/*"

            startActivityForResult(intent, fromALbum)
                Log.d("1", uri.toString())

        }








    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            fromALbum->{
                if(resultCode== Activity.RESULT_OK&&data!=null){

                    data.data?.let{uri->
                        val bitmap=getBitmapFromUri(uri)
                        img1.setImageBitmap(bitmap)
                        Log.d(uri.toString(),"1")
                        thread {
                            user=userDAO.loadByID(1)
                            user.uri=uri.toString()
                            userDAO.updateUser(user)
                        }.join()
                       myuri=uri
                    }
                }
            }
        }
    }
    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri, "r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }
}