package com.example.bitmapintent

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mychat1.UserDatabase.AppDatabase
import com.example.uibestpractice.LeftViewHolder
import com.example.uibestpractice.MsgViewHolder
import com.example.uibestpractice.PaiViewHolder
import com.example.uibestpractice.RightViewHolder
import org.w3c.dom.Text
import java.lang.Appendable
import kotlin.concurrent.thread
import kotlin.math.log


class MsgAdapter(val msgList: List<Msg>) : RecyclerView.Adapter<MsgViewHolder>() {
    val pai=""
    var my_name:String?=null


    override fun getItemViewType(position: Int): Int {
        val msg = msgList[position]
        my_name=msg.myname
        Log.d("getItemViewType",my_name.toString())
        return msg.type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == Msg.TYPE_RECEIVED) {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.msg_left_item, parent, false)
        LeftViewHolder(view)
    }
    else if(viewType == Msg.TYPE_Pai){
        val view =LayoutInflater.from(parent.context).inflate(R.layout.paiyipai,parent,false)
        PaiViewHolder(view)
    }
    else {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.msg_right_item, parent, false)
        RightViewHolder(view)
    }

    var myholder:LeftViewHolder?=null
    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: MsgViewHolder, position: Int) {
        val msg = msgList[position]
        when (holder) {
            is LeftViewHolder -> {holder.leftMsg.text = msg.content
                myholder=holder
                holder.img1.setOnTouchListener { v, event ->
                    gestureDetector.onTouchEvent(
                        event
                    )
                    //     Toast.makeText(context,"123",Toast.LENGTH_SHORT).show()
                    return@setOnTouchListener true///!!!!!!双击事件的实现必须返回true
                }
                if (msg.bitmap2!=0){
                holder.img1.setImageResource(msg.bitmap2)

            }


            }
            is RightViewHolder -> {holder.rightMsg.text = msg.content
            if (msg.bitmap!=null){
                holder.img2.setImageBitmap(msg.bitmap)
            }
                holder.img2.setOnTouchListener { v: View?, event: MotionEvent? ->
                    gestureDetector.onTouchEvent(
                        event
                    )
                return@setOnTouchListener true
                }


            }
            is PaiViewHolder ->{
                holder.pai.text=msg.content
            }


        }
    }

    override fun getItemCount() = msgList.size
    var context:Context?=null
    //获取context
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        context=recyclerView.context
    }
    var pai_text:String?=null

    val gestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean { //单击事件
         //       Toast.makeText(context,"单机",Toast.LENGTH_SHORT).show()
                return super.onSingleTapConfirmed(e)
            }



            override fun onDoubleTap(e: MotionEvent): Boolean { //双击事件

                if (context!=null) {
                    val userDao = AppDatabase.getDatabase(context!!).UserDao()
                    thread {
                        val user = userDao.loadByID(1)
                        pai_text=user.pai
                    }.join()
                }
                if (my_name!=null){
                    Log.d("myname","!=null")
                }
                if (myholder!=null){
                    if (pai_text!=null){
                       Log.d("paii",my_name.toString())
                        val read="我拍了拍"+my_name+pai_text
                    myholder?.leftpai?.text=read
                }
                }
                Toast.makeText(context,"双击",Toast.LENGTH_SHORT).show()
                Log.d("双击","shuangji")
                return super.onDoubleTap(e)
            }

            /**
             * 双击手势过程中发生的事件，包括按下、移动和抬起事件
             * @param e
             * @return
             */
            /**
             * 双击手势过程中发生的事件，包括按下、移动和抬起事件
             * @param e
             * @return
             */
            override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                return super.onDoubleTapEvent(e)
            }
        })



}