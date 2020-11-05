package com.momo.demo.main.me

import android.os.Bundle
import android.os.PersistableBundle
import com.cosmos.photonim.imbase.base.BaseActivity
import com.cosmos.photonim.imbase.utils.LogUtils
import com.cosmos.photonim.imbase.view.TitleBar
import com.momo.demo.R

class SwitchAccoutActivity:BaseActivity() {


    var titleBar: TitleBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_accout)
        titleBar = findViewById(R.id.titleBar)
        initView()

    }

    fun initView(){
        titleBar?.title = "切换账号"
        titleBar?.setLeftImageEvent(R.drawable.arrow_left){
            this.finish();
        }
    }


}