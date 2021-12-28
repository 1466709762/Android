package com.example.bitmapintent

import android.app.Activity
import android.os.UserManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.UserDatabase.User2
import com.example.mychat1.UserDatabase.User
import org.w3c.dom.Text

class tongxunluAdapter(activity: Activity,val resourceId:Int,var data:List<User2>)
    :ArrayAdapter<User2>(activity,resourceId,data), Filterable{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
       //获取控件实例
        val view=LayoutInflater.from(context).inflate(resourceId,parent,false)
        val UserImage:ImageView=view.findViewById(R.id.touxiang_tong)
        val Username:TextView=view.findViewById(R.id.name_tong)
        val Usertime:TextView=view.findViewById(R.id.time_tong)
        val Usermessage:TextView=view.findViewById(R.id.message_tong)

      //获取每个item
        val User=getItem(position)
        if (User!=null) {
            Username.text=User.name
            if (User.bitmap!=1&&User.bitmap!=null){
                UserImage.setImageResource(User.bitmap!!)
            }
            if (User.time!=null){
                Usertime.text=User.time
            }
            if (User.message!=null){
                Usermessage.text=User.message
            }
        }
        //
        return view
    }
    fun setFilter(filterWords: List<User2>) {
        data= filterWords
        notifyDataSetChanged()
    }




}