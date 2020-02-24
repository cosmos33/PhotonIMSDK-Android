package com.momo.demo.main.me;

import com.cosmos.photonim.imbase.utils.LocalStorage;

public class RoamInfo {
    private static final String ROAM_OPEN = "ROAM_OPEN";
    private static final String ROAM_STARTTIME = "ROAM_STARTTIME";
    private static final String ROAM_ENDTIME = "ROAM_ENDTIME";
    private static final String ROAM_COUNT = "ROAM_COUNT";

    public static boolean openRoam() {
        return LocalStorage.get(ROAM_OPEN, true);
    }

    public static long getStartTime() {
        return LocalStorage.get(ROAM_STARTTIME, 0L);
    }

    public static long getEndTime() {
        return LocalStorage.get(ROAM_ENDTIME, System.currentTimeMillis());
    }

    public static int getCount() {
        return LocalStorage.get(ROAM_COUNT, 20);
    }

    public static void setRoamOpen(boolean open) {
        LocalStorage.save(ROAM_OPEN, open);
    }

    public static void setRoamStartTime(long startTime) {
        LocalStorage.save(ROAM_STARTTIME, startTime);
    }

    public static void setRoamEndTime(long endTime) {
        LocalStorage.save(ROAM_ENDTIME, endTime);
    }

    public static void setRoamCount(int count) {
        LocalStorage.save(ROAM_COUNT, count);
    }
}

