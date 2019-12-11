package com.cosmos.photonim.imbase.utils.dbhelper;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(Profile profile);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertList(List<Profile> profiles);

    @Query("select * from profile where userId = :userId")
    Profile find(String userId);

}
