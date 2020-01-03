package com.cosmos.photonim.imbase.utils.dbhelper;

import com.cosmos.photonim.imbase.utils.dbhelper.profile.Profile;
import com.cosmos.photonim.imbase.utils.dbhelper.sessiontest.SessionTest;

import java.util.List;

public interface IDBHelper {
    void saveProfile(String userId, String icon, String name);

    Profile findProfile(String userId);

    void saveProfiles(List<Profile> profiles);

    void saveSessionTestList(List<SessionTest> sessionTest);

    List<SessionTest> findAllSessionTest(String userId);

    void deleteSessionTestData(int chatType, String chatWith);
}
