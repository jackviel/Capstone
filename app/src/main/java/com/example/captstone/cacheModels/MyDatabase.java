package com.example.captstone.cacheModels;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ReviewCache.class}, version = 2)
public abstract class MyDatabase extends RoomDatabase {
    public abstract ReviewDao reviewDao();

    public static final String NAME = "ReviewDatabase";
}
