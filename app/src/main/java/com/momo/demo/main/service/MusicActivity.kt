package com.momo.demo.main.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.cosmos.photonim.imbase.base.BaseActivity
import com.cosmos.photonim.imbase.utils.AppContext
import com.momo.demo.R
import kotlinx.android.synthetic.main.activity_music.view.*
import java.lang.Exception


class MusicActivity :BaseActivity() {

    val musicUrl:String = "https://music.163.com/#/song?id=1436709403"
    var conner: Conner? = null
    companion object{
        var musicBinder : MusicService.MusicBinder? = null
    }



    lateinit var startMusic: TextView
    lateinit var pauseMusic: TextView
    lateinit var stopMusic: TextView


    var unbinder : Unbinder?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music)
        conner = Conner()
        startMusic = findViewById(R.id.startMusic)
        startMusic.setOnClickListener{ v: View ->
            onClick(v)
        }

        pauseMusic = findViewById(R.id.pauseMusic)
        pauseMusic.setOnClickListener{ v:View ->
            onClick(v)
        }

        stopMusic = findViewById(R.id.stopMusic)
        stopMusic.setOnClickListener{v:View ->
            onClick(v)
        }

        // 启动去绑定serivice
        Log.e("MUSIC", "onStartMusic ")
        var intent = Intent(AppContext.getAppContext(),MusicService::class.java)
        intent.setAction("service.MusicService");
        intent.putExtra("url",musicUrl)
        try{
            AppContext.getAppContext().startService(intent)
        }catch (e:Exception){}

        AppContext.getAppContext().bindService(intent,conner, Context.BIND_AUTO_CREATE)

    }


    class Conner : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e("MUSIC", "musicService  Connect")
            musicBinder = service as? MusicService.MusicBinder
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e("MUSIC", "musicService  Disconnected")
        }
    }

    fun onClick(view: View?) = when(view){
        startMusic -> musicBinder?.startMusic()
        pauseMusic -> musicBinder?.pauseMusic()
        stopMusic -> musicBinder?.stopMusic()
        else ->
            Log.e("MUSIC","onClick else")
    }

    override fun onDestroy() {
        super.onDestroy()
        AppContext.getAppContext().unbindService(conner)

    }

}




