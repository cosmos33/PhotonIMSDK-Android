package com.momo.demo.main.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.util.Log
import androidx.annotation.RequiresApi
import com.cosmos.photonim.imbase.utils.AppContext
import com.cosmos.photonim.imbase.utils.NotifyHelper
import java.lang.Exception

class MusicService :Service(){
    var musicUlr:String? = null
    var mediaPlayer: MediaPlayer = MediaPlayer()
    var handlerThread: HandlerThread? = HandlerThread("music_service_update")
    var handlerCallback:Handler.Callback = Handler.Callback {
        msg: Message? ->
        when(msg?.what){
            1 ->
                if(msg?.arg1 != 0){
                    NotifyHelper.sendNotify(AppContext.getAppContext(),"音乐播放器","播放进度："+msg?.arg1+"%",false,true)
                }
        }
        var tmp:Message = Message.obtain()
        tmp.what = 1
        try {
            tmp.arg1 = mediaPlayer.currentPosition/mediaPlayer.duration *100
        }catch (e:Exception){}
        handler.sendMessageDelayed(tmp,5*1000)
        true
    }


    lateinit var handler:Handler

    override fun onCreate() {
        super.onCreate()
        handlerThread?.start()
        handler = Handler(handlerThread?.looper,handlerCallback)
        Log.e("MUSIC","MusicService create")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("MUSIC","MusicService onStartCommand")
        super.onStartCommand(intent, flags, startId)
        exeCommand(1,intent)
        if(!mediaPlayer.isPlaying){
            mediaPlayer.start()
        }
        return START_STICKY
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBind(intent: Intent?): IBinder? {
        Log.e("MUSIC","MusicService onBind")

        exeCommand(2,intent)
        return MusicBinder()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun exeCommand(type:Int, intent: Intent?):Int{
        Log.e("MUSIC", "service exeCommand type:$type")
        musicUlr = intent?.getStringExtra("url")
        if(musicUlr?.isEmpty()!!){
            Log.e("MUSIC","musicUrl is null")
            return -1
        }
        // 处理播放器逻辑
        try {
            with(mediaPlayer){
                reset()
                setDataSource("http://music.163.com/song/media/outer/url?id=1448025691.mp3")
                isLooping = true
                prepareAsync()
                setOnPreparedListener { mp ->
                    var duration = mp.duration
                    Log.e("MUSIC","mediaPlayer play duration:$duration")
                    handler.sendEmptyMessage(1)
                }
                setOnCompletionListener { mp ->
                    Log.e("MUSIC","mediaPlayer playCompletion !!!")
                }
            }
        }catch (e:Exception){
            Log.e("MUSIC",e.message?:"e is null")
        }finally {

        }

        return 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDestroy() {
        super.onDestroy()
        if(mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
        mediaPlayer.release()
    }

    inner class MusicBinder: Binder(),MusicOperation{
        override fun startMusic() {
            mediaPlayer.start()
            NotifyHelper.sendNotify(AppContext.getAppContext(),"音乐播放器","开始播放",false,false)
        }

        override fun pauseMusic() {
            mediaPlayer.pause()
            NotifyHelper.sendNotify(AppContext.getAppContext(),"音乐播放器","暂停播放",false,false)

        }

        override fun stopMusic() {
            mediaPlayer.stop()
            NotifyHelper.sendNotify(AppContext.getAppContext(),"音乐播放器","停止播放",false,false)
            handler.removeMessages(1)
        }

    }
}