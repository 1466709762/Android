package com.example.uibestpractice

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bitmapintent.R


sealed class MsgViewHolder(view: View) : RecyclerView.ViewHolder(view)

class LeftViewHolder(view: View) : MsgViewHolder(view) {
    val leftMsg: TextView = view.findViewById(R.id.leftMsg)
    val img1:ImageView=view.findViewById(R.id.imgleft2)
    val leftpai:TextView=view.findViewById(R.id.pai)
}

class RightViewHolder(view: View) : MsgViewHolder(view) {
    val rightMsg: TextView = view.findViewById(R.id.rightMsg)
    val img2:ImageView=view.findViewById(R.id.img2)////绑定头像
}
class PaiViewHolder(view: View):MsgViewHolder(view){
    val pai:TextView=view.findViewById(R.id.paiyipai_v)

}