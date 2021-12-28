package com.example.bitmapintent

import android.view.MotionEvent
import android.view.View

open class DoubleTouListener() : View.OnTouchListener {
    private val TAG = this.javaClass.simpleName
    private var count = 0
    private var firClick: Long = 0
    private var secClick: Long = 0
    private val mCallback: DoubleClickCallback?=null
    /**
     * 两次点击时间间隔，单位毫秒
     */
    private val interval = 250
    //双击事件
    interface DoubleClickCallback {
        fun onDoubleClick()
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (MotionEvent.ACTION_DOWN == event.action) {
            count++
            if (1 == count) {
                firClick = System.currentTimeMillis()
            } else if (2 == count) {
                secClick = System.currentTimeMillis()
                if (secClick - firClick < interval) {
                    if (mCallback != null) {
                        mCallback.onDoubleClick()
                    } else {
//                        Log.e(TAG, "请在构造方法中传入一个双击回调");
                    }
                    count = 0
                    firClick = 0
                } else {
                    firClick = secClick
                    count = 1
                }
                secClick = 0
            }
        }
        return true
    }
}