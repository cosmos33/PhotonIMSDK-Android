package com.cosmos.photonim.imbase.utils;

import android.content.Context;

public class AppContext {

    private static Context scontext ;
    public static void init(Context context){
        scontext = context.getApplicationContext();
    }

    public static Context getAppContext(){
        return scontext;
    }

}
