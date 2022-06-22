package com.example.captstone;

import android.app.Application;

import com.example.captstone.models.Review;
import com.parse.Parse;
import com.parse.ParseObject;

import android.app.Application;

public class ParseApplication extends Application {

    // Initializes Parse SDK as soon as the application is created
    // test
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Review.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("lRJ1vVp3M8zJ85ZURWCVY5JrAD8l8mqqugYuyQU0")
                .clientKey("SgvZR3gy0wXjXRp2Vsxd8LrMmrvaiw9yARC4nyzq")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}