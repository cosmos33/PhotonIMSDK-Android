package com.momo.demo.main.me;

import com.cosmos.photonim.imbase.utils.LocalStorage;
import com.momo.demo.login.LoginInfo;

public class RoamInfo {
    private static final String ROAM_OPEN = "ROAM_OPEN_";
    private static final String ROAM_STARTTIME = "ROAM_STARTTIME_";
    private static final String ROAM_ENDTIME = "ROAM_ENDTIME_";
    private static final String ROAM_COUNT = "ROAM_COUNT_";

    public static boolean openRoam() {
        return LocalStorage.get(ROAM_OPEN + LoginInfo.getInstance().getUserId(), true);
    }

    public static long getStartTime() {
        return LocalStorage.get(ROAM_STARTTIME + LoginInfo.getInstance().getUserId(), 0L);
    }

    public static long getEndTime() {
        return LocalStorage.get(ROAM_ENDTIME + LoginInfo.getInstance().getUserId(), System.currentTimeMillis());
    }

    public static int getCount() {
        return LocalStorage.get(ROAM_COUNT + LoginInfo.getInstance().getUserId(), 20);
    }

    public static void setRoamOpen(boolean open) {
        LocalStorage.save(ROAM_OPEN + LoginInfo.getInstance().getUserId(), open);
    }

    public static void setRoamStartTime(long startTime) {
        LocalStorage.save(ROAM_STARTTIME + LoginInfo.getInstance().getUserId(), startTime);
    }

    public static void setRoamEndTime(long endTime) {
        LocalStorage.save(ROAM_ENDTIME + LoginInfo.getInstance().getUserId(), endTime);
    }

    public static void setRoamCount(int count) {
        LocalStorage.save(ROAM_COUNT + LoginInfo.getInstance().getUserId(), count);
    }
}

