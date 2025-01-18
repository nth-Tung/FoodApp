package com.nttung.oufood;

import android.app.Application;

import com.cloudinary.android.MediaManager;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Map config = new HashMap();
        config.put("cloud_name", "dqe19i7og");
        config.put("api_key", "544847883651533");
        config.put("api_secret", "6NlAmRMqu9DrrJ4-78cMYonsw-c");
        MediaManager.init(this, config);
    }
}
