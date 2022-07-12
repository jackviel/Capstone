package com.example.captstone;

import android.app.Application;

import com.example.captstone.cacheModels.MyDatabase;
import com.example.captstone.models.Review;
import com.parse.Parse;
import com.parse.ParseObject;

import androidx.room.Room;

public class ParseApplication extends Application {

    MyDatabase myDatabase;

    // Initializes Parse SDK as soon as the application is created
    @Override
    public void onCreate() {
        super.onCreate();

        myDatabase = Room.databaseBuilder(this, MyDatabase.class, MyDatabase.NAME).fallbackToDestructiveMigration().build();

        ParseObject.registerSubclass(Review.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("lRJ1vVp3M8zJ85ZURWCVY5JrAD8l8mqqugYuyQU0")
                .clientKey("SgvZR3gy0wXjXRp2Vsxd8LrMmrvaiw9yARC4nyzq")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }

    public MyDatabase getMyDatabase() {
        return myDatabase;
    }
}