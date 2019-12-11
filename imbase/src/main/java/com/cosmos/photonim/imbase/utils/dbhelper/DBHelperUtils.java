package com.cosmos.photonim.imbase.utils.dbhelper;

import java.util.List;

public class DBHelperUtils {
    private static final DBHelperUtils ourInstance = new DBHelperUtils();
    private IDBHelper idbHelper;

    public static DBHelperUtils getInstance() {
        return ourInstance;
    }

    private DBHelperUtils() {
        idbHelper = new DBHelperImpl();
    }

    public void saveProfile(String userId, String icon, String name) {
        idbHelper.saveProfile(userId, icon, name);
    }

    public void saveProfiles(List<Profile> map) {
        idbHelper.saveProfiles(map);
    }

    public Profile findProfile(String userId) {
        return idbHelper.findProfile(userId);
    }

}
