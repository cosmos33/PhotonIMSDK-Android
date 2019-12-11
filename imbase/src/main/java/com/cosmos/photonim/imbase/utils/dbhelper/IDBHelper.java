package com.cosmos.photonim.imbase.utils.dbhelper;

import java.util.List;

public interface IDBHelper {
    void saveProfile(String userId, String icon, String name);

    Profile findProfile(String userId);

    void saveProfiles(List<Profile> profiles);
}
