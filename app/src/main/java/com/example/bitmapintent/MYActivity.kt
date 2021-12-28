package com.example.bitmapintent

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import com.example.mychat1.UserDatabase.AppDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.my.my_name
import kotlinx.android.synthetic.main.my.my_number
import kotlinx.android.synthetic.main.my.my_touxiang
import kotlin.concurrent.thread

class MYActivity : AppCompatActivity() {
    lateinit var a:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my2)
   //     button_main2.setOnClickListener{
   //         val intent=Intent(this,MainActivity3::class.java)
  //          startActivity(intent)
  //      }
        my_touxiang.setOnClickListener{
            val intent=Intent(this,mydataActivity::class.java)
            startActivity(intent)
            finish()

        }
        val UserDao= AppDatabase.getDatabase(this).UserDao()
        Log.d("正常运行","TRUE001")
        thread {
            val user=UserDao.loadByID(1)
            a=user.uri
            Log.d("正常运行","TRUE002")
        }.join()
        val bitmap=getBitmapFromUri(a.toUri())
        my_touxiang.setImageBitmap(bitmap)
        initData()

        //Bottom
        val bottom2=findViewById<BottomNavigationView>(R.id.bottomNa)
        bottom2.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
        R.id.wechat -> {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
            R.id.lianxi -> {
                val intent=Intent(this,tongxunlu::class.java)
                startActivity(intent)
                finish()
            }
        }
        false
        }
        )




    }
    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri, "r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }
    fun initData(){
        val noteDao=AppDatabase.getDatabase(this).UserDao()
        var name:String="1";var number:String="1"
        thread {
            val User=noteDao.loadByID(1)
            name=User.name
            number=User.number
        }.join()
        my_name.text = name
        my_number.text=number

    }
}