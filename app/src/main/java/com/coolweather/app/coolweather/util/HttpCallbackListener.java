package com.coolweather.app.coolweather.util;

/**
 * Created by Think on 2015/9/27.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
