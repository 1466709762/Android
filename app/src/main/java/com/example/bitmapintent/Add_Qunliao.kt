package com.example.bitmapintent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.ListView
import com.example.UserDatabase.User2
import kotlin.concurrent.thread

class Add_Qunliao : AppCompatActivity() {
    val list = ArrayList<User2>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checkbox)
       initsp()
        val adapter = chebox_list_Adapter(this, R.layout.list_checkbox, list)
        val lv = findViewById<ListView>(R.id.chebox_list)
        lv.adapter = adapter

        lv.setOnItemClickListener { parent, view, position, id ->
            var user2 = list[position]
            val chebox = view.findViewById<CheckBox>(R.id.checkbox)
            chebox.isChecked = !chebox.isChecked
            user2.check = !user2.check
        }
        val checkbutton = findViewById<Button>(R.id.check)
        checkbutton.setOnClickListener {
            var i = list.size
            var m = 0
            val list2=ArrayList<User2>()
            while (m < i) {
                val user = list[m]
                if (user.check!=false)
                {
                    list2.add(user)
                }
                m++
            }
             i=list2.size;m=0
            val intent=Intent(this,Qun_liao::class.java)
            intent.putExtra("listsize",list2.size)
            thread {  while (m<i){
                val user=list2[m]
                intent.putExtra("name_"+m,user.name)
                intent.putExtra("bitmap_"+m,user.bitmap)

                m++
            }
            }.join()
            startActivity(intent)
        }

    }


    fun initsp() {
        val sp = getSharedPreferences("list", MODE_PRIVATE)
        val i = sp.getInt("listsize", 0)
        if (i != 0) {
            var m = 0

            while (m < i) {
                val name = sp.getString("name_" + m, null)
                val bitmap = sp.getInt("bitmap_" + m, 0)
                val time =sp.getString("time_"+m,null)
                val message=sp.getString("message_"+m,null)
                m++
                if (name != null) {
                    val user2 = User2(name)
                    if (bitmap != 0) {
                        user2.bitmap = bitmap
                    }
                    if (time!=null) {
                        user2.time = time

                    }
                    if (message!=null)
                    {
                        user2.message=message
                    }

                    list.add(user2)
                }
            }
          }

    }

}