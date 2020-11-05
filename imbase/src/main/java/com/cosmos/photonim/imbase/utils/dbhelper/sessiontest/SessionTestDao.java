package com.cosmos.photonim.imbase.utils.dbhelper.sessiontest;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
