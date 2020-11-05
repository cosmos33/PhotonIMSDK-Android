package com.cosmos.photonim.imbase.utils.dbhelper.profile;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
