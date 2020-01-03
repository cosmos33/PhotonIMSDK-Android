package com.cosmos.photonim.imbase.utils.dbhelper.sessiontest;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SessionTestDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveSessionTest(List<SessionTest> sessionTest);

    @Query("select * from sesstiontest where userId = :userId")
    List<SessionTest> findAllSessionTest(String userId);

    @Delete
    void deleteSessionTestData(SessionTest sessionTest);
}
