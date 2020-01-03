package com.cosmos.photonim.imbase.utils.dbhelper;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.cosmos.photonim.imbase.utils.dbhelper.profile.Profile;
import com.cosmos.photonim.imbase.utils.dbhelper.profile.ProfileDao;
import com.cosmos.photonim.imbase.utils.dbhelper.sessiontest.SessionTest;
import com.cosmos.photonim.imbase.utils.dbhelper.sessiontest.SessionTestDao;

@Database(entities = {Profile.class, SessionTest.class}, version = 3, exportSchema = false)
public abstract class MyDB extends RoomDatabase {
    private static final String DB_NAME = "profile.db";
    private static volatile MyDB instance;

    public static synchronized MyDB getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static MyDB create(final Context context) {
        return Room.databaseBuilder(
                context,
                MyDB.class,
                DB_NAME).addMigrations(migration1To2, migration2To3).build();
    }

    private static Migration migration1To2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE profile "
                    + " ADD COLUMN name TEXT");
        }
    };

    private static Migration migration2To3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL(
                    "CREATE TABLE sesstiontest (chatWith TEXT NOT NULL, chatType INTEGER NOT NULL, userId TEXT NOT NULL, PRIMARY KEY(chatWith,chatType,userId))");
        }
    };

    public abstract ProfileDao profileDao();

    public abstract SessionTestDao sessionTestDao();
}
