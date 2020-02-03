package com.cosmos.photonim.imbase.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

public class PermissionUtils {
    public static String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
            , Manifest.permission.RECORD_AUDIO
            , Manifest.permission.ACCESS_FINE_LOCATION
            , Manifest.permission.ACCESS_COARSE_LOCATION
            , Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
            , Manifest.permission.CAMERA};

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean checkPersmission(Context context) {
        for (String permission : permissions) {
            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
