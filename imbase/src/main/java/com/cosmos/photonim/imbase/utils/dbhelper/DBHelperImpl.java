package com.cosmos.photonim.imbase.utils.dbhelper;

import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.utils.CollectionUtils;
import com.cosmos.photonim.imbase.utils.dbhelper.profile.Profile;
import com.cosmos.photonim.imbase.utils.dbhelper.sessiontest.SessionTest;

import java.util.List;

public class DBHelperImpl implements IDBHelper {
    @Override
    public void saveProfile(String userId, String icon, String name) {
        Profile profile = new Profile();
        profile.setIcon(icon);
        profile.setUserId(userId);
        profile.setName(name);
        MyDB.getInstance(ImBaseBridge.getInstance().getApplication()).profileDao().insert(profile);
    }

    @Override
    public void saveProfiles(List<Profile> profiles) {
        if (CollectionUtils.isEmpty(profiles)) {
            return;
        }
        MyDB.getInstance(ImBaseBridge.getInstance().getApplication()).profileDao().insertList(profiles);
    }


    @Override
    public Profile findProfile(String userId) {
        return MyDB.getInstance(ImBaseBridge.getInstance().getApplication()).profileDao().find(userId);
    }

    @Override
    public void saveSessionTestList(List<SessionTest> sessionTest) {
        MyDB.getInstance(ImBaseBridge.getInstance().getApplication()).sessionTestDao().saveSessionTest(sessionTest);
    }

    @Override
    public List<SessionTest> findAllSessionTest(String userId) {
        return MyDB.getInstance(ImBaseBridge.getInstance().getApplication()).sessionTestDao().findAllSessionTest(userId);
    }

    @Override
    public void deleteSessionTestData(int chatType, String chatWith) {
        SessionTest sessionTest = new SessionTest();
        sessionTest.setChatWith(chatWith);
        sessionTest.setChatType(chatType);
        sessionTest.setUserId(ImBaseBridge.getInstance().getUserId());
        MyDB.getInstance(ImBaseBridge.getInstance().getApplication()).sessionTestDao().deleteSessionTestData(sessionTest);
    }
}
