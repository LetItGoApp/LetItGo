package com.example.takeit;

import android.app.Application;

import com.example.takeit.Models.Listing;
import com.parse.Parse;
import com.parse.ParseObject;


public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Register parse models.
        ParseObject.registerSubclass(Listing.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("UE6kPA5Wx9b5oBgQszJO7OooCqI224ZAQjauoxGy")
                .clientKey("7vUPDfOv491jku3lbOJMV3CL88ZFcKhXH6GAVO7r")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
