package com.cosmos.photonim.imbase.utils.dbhelper;

import com.cosmos.photonim.imbase.utils.dbhelper.profile.Profile;
import com.cosmos.photonim.imbase.utils.dbhelper.sessiontest.SessionTest;

import java.util.List;

public class DBHelperUtils implements IDBHelper {
    private static final DBHelperUtils ourInstance = new DBHelperUtils();
    private IDBHelper idbHelper;

    public static DBHelperUtils getInstance() {
        return ourInstance;
    }

    private DBHelperUtils() {
        idbHelper = new DBHelperImpl();
    }

    @Override
    public void saveProfile(String userId, String icon, String name) {
        idbHelper.saveProfile(userId, icon, name);
    }

    @Override
    public void saveProfiles(List<Profile> map) {
        idbHelper.saveProfiles(map);
    }


    @Override
    public Profile findProfile(String userId) {
        return idbHelper.findProfile(userId);
    }

    @Override
    public void saveSessionTestList(List<SessionTest> sessionTest) {
        idbHelper.saveSessionTestList(sessionTest);
    }

    @Override
    public List<SessionTest> findAllSessionTest(String userId) {
        return idbHelper.findAllSessionTest(userId);
    }

    @Override
    public void deleteSessionTestData(int chatType, String chatWith) {
        idbHelper.deleteSessionTestData(chatType, chatWith);
    }
}
