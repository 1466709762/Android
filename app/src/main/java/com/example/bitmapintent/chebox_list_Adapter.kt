package com.example.bitmapintent

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.UserDatabase.User2

class chebox_list_Adapter(activity: Activity, val resourceId:Int, data:List<User2>)
    : ArrayAdapter<User2>(activity,resourceId,data){
    lateinit var checkBox: CheckBox
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(context).inflate(resourceId, parent, false)
        val Img=view.findViewById<ImageView>(R.id.chebox_img)
        val text=view.findViewById<TextView>(R.id.chebox_text)
        val checkbox=view.findViewById<CheckBox>(R.id.checkbox)
        //
        val User=getItem(position)
        if (User!=null) {
            text.text=User.name
            if (User.bitmap!=1&&User.bitmap!=null){
                Img.setImageResource(User.bitmap!!)
            }
        }
    return view
    }
   public fun cheboxChange(){
        checkBox.isChecked=!checkBox.isChecked
    }
}