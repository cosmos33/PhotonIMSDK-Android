package com.cosmos.photonim.imbase.utils

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.bumptech.glide.load.engine.Resource
import com.cosmos.photonim.imbase.R
import kotlinx.android.synthetic.main.activity_session.*

object NotifyHelper{
    lateinit var notificationManager:NotificationManager
    var notifyId:Int = 0;
    fun  sendNotify(context: Context,title:String,content:String,autoCancel: Boolean,notifyAutoIncreate:Boolean){
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var notification:Notification.Builder? = null
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            notification = Notification.Builder(context,"notification.msg");
        }else{
            notification = Notification.Builder(context);
        }

        val intent: Intent = Intent(context,NotifyClickReveiver::class.java)
        intent.putExtra("title",title)
        intent.putExtra("content",content)
        var pendingIntent: PendingIntent = PendingIntent.getBroadcast(context,0,intent,0)
        notification.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(autoCancel)
                .setDefaults(Notification.DEFAULT_ALL)

        notificationManager.notify(notifyId,notification.build())
        if(notifyAutoIncreate){
            notifyId++;
        }

    }

}