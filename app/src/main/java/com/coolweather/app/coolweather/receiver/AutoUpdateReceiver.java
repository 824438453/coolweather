package com.coolweather.app.coolweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.coolweather.app.coolweather.service.AutoUpdateService;

/**
 * Created by Think on 2015/9/29.
 */
public class AutoUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context,Intent intent){
        Intent i = new Intent(context, AutoUpdateService.class);
        context.startService(i);
    }
}
