package com.example.bitmapintent

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.UserDatabase.User2
import com.example.mychat1.UserDatabase.AppDatabase
import kotlinx.android.synthetic.main.chat.*
import kotlin.concurrent.thread

class Qun_liao : AppCompatActivity(), View.OnClickListener {
    lateinit var a:String
    private val msgList = ArrayList<Msg>()
    lateinit var send: Button
    lateinit var recyclerView: RecyclerView
    lateinit var inputText: EditText
    val list=ArrayList<User2>()
    private lateinit var adapter: MsgAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat)
        setSupportActionBar(toolbar2)
        val size=intent.getIntExtra("listsize",0)
        var m=0
        Toast.makeText(this,size.toString(),Toast.LENGTH_SHORT).show()



        //获取自己设置的头像
        //从数据库获取自己的头像a
        val UserDao=AppDatabase.getDatabase(this).UserDao()
        Log.d("正常运行","TRUE001")
        thread {
            val user=UserDao.loadByID(1)
            a=user.uri
            Log.d("正常运行","TRUE002")
        }.join()
        Log.d("正常运行",a)


        //
        if (size!=0) {
            while (m < size) {
                val name=intent.getStringExtra("name_$m")
                val bitmap=intent.getIntExtra("bitmap_$m",0)
                val user2=User2(name!!)
                Log.d("name",name)
                user2.bitmap=bitmap
                list.add(user2)
                m++
            }
        }

        send=findViewById(R.id.send)
        recyclerView=findViewById(R.id.recyclerView)
        inputText=findViewById(R.id.inputText)
    ///初始化rv
        val msg2 = Msg("Hello. Who is that?", Msg.TYPE_SENT)
        if (a!="") {
            val uri1 = a.toUri()
            val bitmap2 = getBitmapFromUri(uri1)
            msg2.bitmap=bitmap2
        }
        msgList.add(msg2)
        m=0
        while (m<size){
            val msg=Msg("Hi",Msg.TYPE_RECEIVED)
            msg.bitmap2=list[m].bitmap
            msgList.add(msg)
            m++
        }


        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        if (!::adapter.isInitialized) {
            adapter = MsgAdapter(msgList)
        }
        recyclerView.adapter = adapter

    send.setOnClickListener(this)

    }
    private fun getBitmapFromUri(uri: Uri) = contentResolver.openFileDescriptor(uri, "r")?.use {
        BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
    }

    override fun onClick(v: View?) {
        when (v) {
            send -> {
                val content = inputText.text.toString()
                if (content.isNotEmpty()) {
                    ////将头像放入到rv中
                    val msg = Msg(content, Msg.TYPE_SENT)
                    if (a != "") {
                        val uri1 = a.toUri()
                        val bitmap2 = getBitmapFromUri(uri1)
                        msg.bitmap = bitmap2
                    }
                    msgList.add(msg)
                    adapter.notifyItemInserted(msgList.size - 1) // 当有新消息时，刷新RecyclerView中的显示
                    recyclerView.scrollToPosition(msgList.size - 1)  // 将 RecyclerView定位到最后一行
                    //    inputText.setText("") // 清空输入框中的内容
                    inputText.setText("")

                    //  chat_add ->
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.fanhui->{
                Toast.makeText(this,"返回",Toast.LENGTH_SHORT).show()
                val intent= Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }
}