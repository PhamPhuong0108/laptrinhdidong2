package com.example.admin.mp3playyer;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        context = this;
    }

    public static Context getContext(){
        return context;
    }
}
