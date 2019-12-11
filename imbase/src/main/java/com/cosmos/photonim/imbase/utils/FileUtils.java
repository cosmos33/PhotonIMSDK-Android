package com.cosmos.photonim.imbase.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
    public static final String IM_DEOM_ROOT = "imdemo";
    public static final String VOICE_PATH_SEND = IM_DEOM_ROOT + "/" + "voice_send/";
    public static final String VOICE_PATH_RECEIVE = IM_DEOM_ROOT + "/" + "voice_receive/";

    public static boolean createFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file is null");
        }
        if (file.exists()) {
            return true;
        }
        File parentFile = file.getParentFile();
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            return false;
        }
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean removeFile(String fileName) {
        if (fileName == null) {
            return false;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            return false;
        }
        if (file.isFile()) {
            return file.delete();
        }
        return false;
    }

    public static File createImageFile(Context context) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }
}
