package com.justice.ai_photo_clip;

import android.app.Application;

import com.justice.ai_photo_clip.utils.L;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        L.initialize("young_",true);
    }
}
