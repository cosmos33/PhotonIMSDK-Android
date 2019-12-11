package com.cosmos.photonim.imbase.utils.dbhelper;

import com.cosmos.photonim.imbase.ImBaseBridge;
import com.cosmos.photonim.imbase.utils.CollectionUtils;

import java.util.List;

public class DBHelperImpl implements IDBHelper {
    @Override
    public void saveProfile(String userId, String icon, String name) {
        Profile profile = new Profile();
        profile.setIcon(icon);
        profile.setUserId(userId);
        profile.setName(name);
        ProfileDB.getInstance(ImBaseBridge.getInstance().getApplication()).profileDao().insert(profile);
    }

    @Override
    public void saveProfiles(List<Profile> profiles) {
        if (CollectionUtils.isEmpty(profiles)) {
            return;
        }
        ProfileDB.getInstance(ImBaseBridge.getInstance().getApplication()).profileDao().insertList(profiles);
    }

    @Override
    public Profile findProfile(String userId) {
        return ProfileDB.getInstance(ImBaseBridge.getInstance().getApplication()).profileDao().find(userId);
    }
}
