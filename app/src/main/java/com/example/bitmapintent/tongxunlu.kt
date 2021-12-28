package com.example.bitmapintent

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.ListView
import android.widget.SearchView
import com.example.UserDatabase.User2
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.tongxunlu.*
import kotlin.concurrent.thread
import java.util.HashSet




class tongxunlu : AppCompatActivity() {
    private val UserLIst=ArrayList<User2>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tongxunlu)
        initUser()
        initListView()

        initsp()

        val adapter=tongxunluAdapter(this,R.layout.list_tongxunlu,UserLIst)
        val listView=findViewById<ListView>(R.id.listview2)
        listView.adapter=adapter
        listView.setTextFilterEnabled(true)
        initsearch()
      //
        val BottomNavigationView = findViewById<BottomNavigationView>(R.id.l2)
        BottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val menuId = item.itemId
            when (menuId) {
                R.id.wechat -> {
                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                R.id.mydata->{
                    val intent= Intent(this,MYActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
            false
        }
        add_qunliao.setOnClickListener{
            val intent=Intent(this,Add_Qunliao::class.java)
            startActivity(intent)
        }
    }


    fun initsearch(){
        val query: SearchView =findViewById(R.id.searchview2)
        query.setOnQueryTextListener( object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            //监听输入框的信息变化，过滤listview的信息
            override fun onQueryTextChange(newText: String?): Boolean {
                if (TextUtils.isEmpty(newText)) {
                    // Clear the text filter.
                    listview2.clearTextFilter()
                } else {
                    // Sets the initial value for the text filter.
                    listview2.setFilterText(newText.toString())
                }
                return false
            }

        })
    }

    fun initUser(){



        val User1=User2("Cheng")
        User1.bitmap=R.drawable.tou2
        UserLIst.add(User1)


    }

    fun initListView(){
        // val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,list)
        val listview:ListView=findViewById(R.id.listview2)
        // listview的点击事件

        listview.setOnItemClickListener { parent, view, position, id ->

        //将选择的聊天置顶

            val newUserlist=ArrayList<User2>()
            val listsize=UserLIst.size
            var i=0
            var m=0
            newUserlist.add(UserLIst[id.toInt()])

            while (i<listsize){
                newUserlist.add(UserLIst[i])
                i++
            }
            thread {  val sp = getSharedPreferences("list", MODE_PRIVATE).edit()

                //将List元素注入到Set
                val set: Set<User2> = HashSet<User2>(newUserlist)

                //清空List集合
                newUserlist.clear()

                //set集合的元素添加到List
                newUserlist.addAll(set)

                val listsize = newUserlist.size

                while (m < listsize){
                    sp.putString("name_" + m,newUserlist[m].name2)
                    sp.putInt("bitmap_"+m,newUserlist[m].bitmap)

                    m++
                }
                sp.putInt("listsize",listsize)
                sp.apply()}
            val myname=UserLIst[id.toInt()].name
            val touxiang=UserLIst[id.toInt()].bitmap
            val intent=Intent(this,ChatActivity::class.java)
            intent.putExtra("touxiang",touxiang)
            intent.putExtra("id",id.toInt())
            intent.putExtra("myname",myname)
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
                Log.d("name", name.toString())
                Log.d("bitmap", bitmap.toString())
                m++
                if (name != null) {
                    val user2 = User2(name)
                    if (bitmap != 0) {
                        user2.bitmap = bitmap
                    }
                    UserLIst.add(user2)

                }
            }

        }

    }
}