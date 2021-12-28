package com.example.bitmapintent

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.UserDatabase.User2
import com.example.mychat1.UserDatabase.AppDatabase
import com.example.mychat1.UserDatabase.User
import io.reactivex.Scheduler
import kotlinx.android.synthetic.main.chat.*
import kotlinx.android.synthetic.main.list_tongxunlu.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class ChatActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var a:String
    private val msgList = ArrayList<Msg>()
    lateinit var send: Button
    lateinit var recyclerView: RecyclerView
    lateinit var inputText: EditText
    private var UserList=ArrayList<User2>()

    var id:Int=999
    var touxiang:Int=0
    var caidan=false
    var myname:String?=null

    private lateinit var adapter: MsgAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat)
        //从intent获取头像和id
        setSupportActionBar(toolbar2)
         touxiang=intent.getIntExtra("touxiang",0)
        id=intent.getIntExtra("id",999)
        myname=intent.getStringExtra("myname")
       Log.d("myname",myname.toString())


        //从数据库获取自己的头像a
        val UserDao=AppDatabase.getDatabase(this).UserDao()
        Log.d("正常运行","TRUE001")
        thread {
            val user=UserDao.loadByID(1)
            a=user.uri
            Log.d("正常运行","TRUE002")
        }.join()
        Log.d("正常运行",a)

        send=findViewById(R.id.send)
        recyclerView=findViewById(R.id.recyclerView)
        inputText=findViewById(R.id.inputText)

        initMsg()

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        if (!::adapter.isInitialized) {
            adapter = MsgAdapter(msgList)
            adapter.context=this
        }
        recyclerView.adapter = adapter
        send.setOnClickListener(this)
        chat_add.setOnClickListener(this)



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
                    if (a!="") {
                        val uri1 = a.toUri()
                        val bitmap2 = getBitmapFromUri(uri1)
                        msg.bitmap=bitmap2
                    }
                    msgList.add(msg)
                    adapter.notifyItemInserted(msgList.size - 1) // 当有新消息时，刷新RecyclerView中的显示
                    recyclerView.scrollToPosition(msgList.size - 1)  // 将 RecyclerView定位到最后一行
                //    inputText.setText("") // 清空输入框中的内容

                    var iii=1
                    //receive


                    //5秒定时回复
                Toast.makeText(this,"3秒后接受信息",Toast.LENGTH_SHORT).show()
                val t1=Thread{
                    Thread.sleep(5000)
                    val b:String="Hi"
                    val msg2 =Msg(b,Msg.TYPE_RECEIVED)
                    msg2.myname=myname
                    msg2.bitmap2=touxiang
                    msgList.add(msg2)



                    runOnUiThread {
                        Toast.makeText(this,"已接受信息",Toast.LENGTH_SHORT).show()
             //           adapter.notifyDataSetChanged()
                    //       adapter.notifyItemInserted(msgList.size - 1)
                      //  recyclerView.scrollToPosition(msgList.size - 1)
                        val layoutManager = LinearLayoutManager(this)
                        recyclerView.layoutManager = layoutManager
                        if (!::adapter.isInitialized) {
                            adapter = MsgAdapter(msgList)
                        }
                        recyclerView.adapter = adapter
                        adapter.notifyItemInserted(msgList.size - 1)
                          recyclerView.scrollToPosition(msgList.size - 1)
                      }
                }.start()








                    //通过id修改list里的信息
                    val time=getTime()
             //       Toast.makeText(this,time,Toast.LENGTH_SHORT).show()
                    //获取原始的list
                    thread {
                        UserList.clear()
                        val sp=getSharedPreferences("list", MODE_PRIVATE)
                        val i=sp.getInt("listsize",0)
                        iii=i
                        if (i!=0) {
                            var m = 0
                            while (m < i) {
                                val name =sp.getString("name_"+m,null)
                                val bitmap=sp.getInt("bitmap_"+m,0)
                                val time=sp.getString("time_"+m,null)
                                val message=sp.getString("message_"+m,null)
                                if (name!=null){
                                    val user2=User2(name)
                                    if (bitmap!=0){
                                        user2.bitmap=bitmap
                                    }
                                    if (time!=null){
                                        user2.time=time
                                    }
                                    if (message!=null){
                                        user2.message=message
                                    }
                                    UserList.add(user2)

                                }
                                m++
                            }
                            m=0
                            UserList[id].message=content
                            UserList[id].time=time
                            val sp2=getSharedPreferences("list", MODE_PRIVATE).edit()
                            while (m<i){

                                sp2.putString("name_" + m, UserList[m].name2)
                                sp2.putInt("bitmap_" + m, UserList[m].bitmap)
                                sp2.putString("time_"+ m,UserList[m].time)
                                sp2.putString("message_"+m,UserList[m].message)
                                m++
                            }
                            sp2.apply()
                        }
                    }

                    inputText.setText("")
                }
            }
            chat_add -> {


                    val fragement=Fragement() as Fragement
                    val fragmentManager=supportFragmentManager
                    val transaction = fragmentManager.beginTransaction()
                    transaction.replace(R.id.chat_framelayout,fragement)

                    if (caidan==false){
                        transaction.show(fragement)
                    }
                    if (caidan!=false){
                        transaction.hide(fragement)
                    }

                    transaction.commit()
                    caidan=!caidan


            }
          //  chat_add ->
        }
    }



    private fun initMsg() {
        val msg1 = Msg("Hello guy.", Msg.TYPE_RECEIVED)
            msg1.myname=myname
            Log.d("msg1",msg1.myname.toString())
            msg1.bitmap2=touxiang

        msgList.add(msg1)
        val msg2 = Msg("Hello. Who is that?", Msg.TYPE_SENT)
        if (a!="") {
            val uri1 = a.toUri()
            val bitmap2 = getBitmapFromUri(uri1)
            msg2.bitmap=bitmap2
        }

        msgList.add(msg2)
        val msg3 = Msg("This is Tom. Nice talking to you. ", Msg.TYPE_RECEIVED)
        msg3.myname=myname
        Log.d("msg1",msg3.myname.toString())
        msg3.bitmap2=touxiang
        msgList.add(msg3)

    }
    private fun replaceFragment(fragement: Fragement){
        val fragmentManager=supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.chat_framelayout,fragement)
        transaction.commit()

    }

    //获取系统时间
    fun getTime():String{
        val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
        val date = Date(System.currentTimeMillis())
        val time=simpleDateFormat.format(date)
        return time
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.chat, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.fanhui->{
                Toast.makeText(this,"返回",Toast.LENGTH_SHORT).show()
                val intent=Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    fun takephoto(){
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }

   //   class SimpleWorker(context: Context, params: WorkerParameters) : Worker(context, params) {


   //     override fun doWork(): Result {
 //           val shijian=inputData.getBoolean("shijian",true)
 //           Log.d("1",shijian.toString())

//            return Result.success()
 //       }
//
  //  }
    //返回主界面
    override fun onBackPressed() {

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        super.onBackPressed()
    }



}

