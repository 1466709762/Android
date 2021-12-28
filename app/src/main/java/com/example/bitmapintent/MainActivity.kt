package com.example.bitmapintent

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.UserDatabase.User2
import com.example.mychat1.UserDatabase.AppDatabase
import com.example.mychat1.UserDatabase.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.list_tongxunlu.view.*
import kotlinx.android.synthetic.main.my.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    var list = ArrayList<User2>()
    val userDao = AppDatabase.getDatabase(this).UserDao()
    private var adapter: tongxunluAdapter? = null
    lateinit var listview: ListView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main2)
        val toolbar2 = findViewById<Toolbar>(R.id.toolbar2)
        setSupportActionBar(toolbar2)

        initsp()

        thread {
            userDao.insertNote(User("", "", "", ""))
        }.join()
        initdatabse()
//        val button_main:ImageButton=findViewById(R.id.button_main)

        initListView()
        //     val my:ImageButton=findViewById(R.id.my)
        //      my.setOnClickListener{
        //          val intent=Intent(this,MYActivity::class.java)
        //           startActivity(intent)

        //       }
        val BottomNavigationView = findViewById<BottomNavigationView>(R.id.l2)
        BottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val menuId = item.itemId
            when (menuId) {
                R.id.wechat -> Log.d("1", "2")
                R.id.mydata -> {
                    val intent = Intent(this, MYActivity::class.java)
                    startActivity(intent)
                    finish()

                }
                R.id.lianxi -> {
                    val intent = Intent(this, tongxunlu::class.java)
                    startActivity(intent)
                    finish()

                }
                R.id.pengyouquan ->{
                    val intent=Intent(Intent.ACTION_VIEW)
                    intent.data=Uri.parse("https://www.weibo.com")
                    startActivity(intent)
                }
            }
            false
        }


        //listview
        //   adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,list)
        listview = findViewById(R.id.listview)
        //    listview.adapter=adapter

        listview.setTextFilterEnabled(true)  ///过滤listview的内容
        val adapter = tongxunluAdapter(this, R.layout.list_tongxunlu, list)
        listview.adapter = adapter
        initsearch()


        //
//TEst



    }

    //创建MENU菜单
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true

    }

    //MENU菜单的点击事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //添加好友
            R.id.tianjia -> {
                val time=getTime()



                val adapter = tongxunluAdapter(this, R.layout.list_tongxunlu, list)
                listview = findViewById(R.id.listview)
                listview.adapter = adapter
                listview.setTextFilterEnabled(true)  ///过滤listview的内容,为searchview的使用铺垫
                val User22 = User2("Yang")
                User22.bitmap = R.drawable.touxiang3
                User22.time = time
                list.add(User22)
                adapter.notifyDataSetChanged()
                thread {
                    val sp = getSharedPreferences("list", MODE_PRIVATE).edit()
                    val listsize = list.size
                    var i = 0
                    while (i < listsize) {
                        sp.putString("name_" + i, list[i].name2)
                        sp.putInt("bitmap_" + i, list[i].bitmap)
                        sp.putString("time_"+ i,list[i].time)
                        sp.putString("message_"+i,list[i].message)
                        i++
                    }
                    sp.putInt("listsize", listsize)
                    sp.apply()
                }

            }
            R.id.shao -> {
                list.clear()
                val adapter = tongxunluAdapter(this, R.layout.list_tongxunlu, list)
                listview.adapter = adapter
                adapter.notifyDataSetChanged()
                thread {
                    val sp = getSharedPreferences("list", MODE_PRIVATE).edit()
                    val listsize = list.size
                    var i = 0
                    while (i < listsize) {
                        sp.putString("name_" + i, list[i].name2)
                        sp.putInt("bitmap_" + i, list[i].bitmap)
                        sp.putString("time_"+i,list[i].time)
                        sp.putString("message_"+i,null)
                        i++
                    }
                    sp.putInt("listsize", listsize)
                    sp.apply()
                }

            }
            R.id.qunliao -> {
                val intent=Intent(this,Add_Qunliao::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    fun initListView() {
        // val adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,list)
        val listview: ListView = findViewById(R.id.listview)
        // listview的点击事件

        listview.setOnItemClickListener { parent, view, position, id ->
            val touxiang = list[id.toInt()].bitmap
            val myname=list[id.toInt()].name
            Toast.makeText(this,myname,Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("touxiang", touxiang)
            intent.putExtra("id",id.toInt())
            intent.putExtra("myname",myname)
            startActivity(intent)
            finish()
        }

        //长按弹出删除对话框
        listview.setOnItemLongClickListener { parent, view, position, id ->
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("Title")
            builder.setMessage("是否要删除该信息")
            //确认
            builder.setPositiveButton(
                "确定"
            ) { dialog, which ->
                val user=list[id.toInt()]
                list.remove(user)
                val intent=Intent(this,MainActivity::class.java)
                thread {
                    val sp = getSharedPreferences("list", MODE_PRIVATE).edit()
                    val listsize = list.size
                    var i = 0
                    while (i < listsize) {
                        sp.putString("name_" + i, list[i].name2)
                        sp.putInt("bitmap_" + i, list[i].bitmap)
                        sp.putString("time_"+i,list[i].time)
                        sp.putString("messageg_"+i,null)
                        i++
                    }
                    sp.putInt("listsize", listsize)
                    sp.apply()
                }
                val adapter = tongxunluAdapter(this, R.layout.list_tongxunlu, list)
                listview.adapter = adapter
                adapter.notifyDataSetChanged()
      //          startActivity(intent)
      //          finish()

            }
            //中立(置顶)
            builder.setNeutralButton("置顶"){
                    dialog, which ->
                val firstuser=list[id.toInt()]
                list.remove(firstuser)
                val b=ArrayList<User2>()
                b.add(firstuser)
                for (user in list){
                    b.add(user)
                }
                list=b
                val intent=Intent(this,MainActivity::class.java)
                thread {
                    val sp = getSharedPreferences("list", MODE_PRIVATE).edit()
                    val listsize = list.size
                    var i = 0
                    while (i < listsize) {
                        sp.putString("name_" + i, list[i].name2)
                        sp.putInt("bitmap_" + i, list[i].bitmap)
                        sp.putString("time_"+i,list[i].time)
                        sp.putString("messageg_"+i,list[i].message)
                        i++
                    }
                    sp.putInt("listsize", listsize)
                    sp.apply()
                }
                val adapter = tongxunluAdapter(this, R.layout.list_tongxunlu, list)
                listview.adapter = adapter
                adapter.notifyDataSetChanged()


                Toast.makeText(this,"已置顶",Toast.LENGTH_SHORT).show()

            }
           //取消
            builder.setNegativeButton(
                "取消"
            ) { dialog, which ->
                Toast.makeText(this, "已取消", Toast.LENGTH_SHORT).show()
            }
            val alertDialog = builder.create()
            alertDialog.show()


            return@setOnItemLongClickListener true
        }

    }

    //初始化数据库
    fun initdatabse() {
        val userDao = AppDatabase.getDatabase(this).UserDao()

        val t0 = thread {///chuangjian  1  Data
            userDao.insertNote(User("1", "LIMING", "123456", "拍了怕肩膀"))
        }.join()
    }

    //创建搜索功能
    fun initsearch() {
        val query=findViewById<SearchView>(R.id.searchview)
        query.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false

            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                val filterString=filter(list,query!!)
                //通过rv的适配器获取搜索数据
                adapter?.setFilter(filterString)
                return false
            }
            ///获得搜索数据
            fun filter(strings: ArrayList<User2>, text: String): ArrayList<User2> {
                val filterString =ArrayList<User2>()
                for (word in strings) {
                    if (word.name.contains(text)){ filterString.add(word)}


            }
                return filterString
            }
        })




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

    fun getTime():String{
        val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
        val date = Date(System.currentTimeMillis())
        val time=simpleDateFormat.format(date)
        return time
    }

}




