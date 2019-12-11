package com.cosmos.photonim.imbase.utils.dbhelper;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Profile.class}, version = 2, exportSchema = false)
public abstract class ProfileDB extends RoomDatabase {
    private static final String DB_NAME = "profile.db";
    private static volatile ProfileDB instance;

    static synchronized ProfileDB getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static ProfileDB create(final Context context) {
        return Room.databaseBuilder(
                context,
                ProfileDB.class,
                DB_NAME).addMigrations(migration1To2).build();
    }

    private static Migration migration1To2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE profile "
                    + " ADD COLUMN name TEXT");
        }
    };

    public abstract ProfileDao profileDao();
}
