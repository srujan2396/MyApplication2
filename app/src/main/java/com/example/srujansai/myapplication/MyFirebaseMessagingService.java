package com.example.srujansai.myapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by srujan sai on 10-07-2017.
 */

public class MyFirebaseMessagingService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }
}
