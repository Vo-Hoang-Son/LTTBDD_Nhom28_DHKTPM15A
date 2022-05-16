package com.example.planapp_nhom28_mobile;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Plan.class}, version = 1)
public abstract class PlanDB extends RoomDatabase {
    private static final String DATABASE_NAME = "Plan.db";
    private static PlanDB instance;

    public static synchronized PlanDB getInstance(Context context) {
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), PlanDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract PlanDao planDao();
}
