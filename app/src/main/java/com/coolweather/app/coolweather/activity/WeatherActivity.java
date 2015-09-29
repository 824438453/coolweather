package com.coolweather.app.coolweather.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.coolweather.app.coolweather.R;
import com.coolweather.app.coolweather.util.HttpCallbackListener;
import com.coolweather.app.coolweather.util.HttpUtil;
import com.coolweather.app.coolweather.util.Utility;

/**
 * Created by Think on 2015/9/29.
 */
public class WeatherActivity extends AppCompatActivity {
    private RelativeLayout weatherInfoLayout;
    /*
    用于显示城市名
     */
    private TextView cityNameText;
    /*
    用于显示发布时间
     */
    private TextView publishText;
    /*
   用于显示天气描述信息
    */
    private TextView weatherDespText;
     /*
    用于显示最低气温
     */
    private TextView temp1Text;
     /*
    用于显示最高气温
     */
    private TextView temp2Text;
     /*
    用于显示当前日期
     */
    private TextView currentDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);

        weatherInfoLayout = (RelativeLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        publishText = (TextView) findViewById(R.id.publish_text);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        currentDateText = (TextView) findViewById(R.id.current_date);
        String countyCode = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)){
            publishText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);
        }else{
            showWeather();
        }
    }
    /*
    查询县级代号对应的天气代号
     */
    private void queryWeatherCode(String countyCode){
        String  address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
//        Log.d("chaxun","查询县级天气代号");
//        Log.d("chaxun",address);
        queryFromServer(address, "countyCode");
    }
    /*
    查询天气代号对应的天气
     */
    private void queryWeatherInfo(String weatherCode){
        String  address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
//        Log.d("chaxun","查询县级天气代号对应的天气");
//        Log.d("chaxun",address);
        queryFromServer(address,"weatherCode");
    }
    /*
    根据传入的地址和类型去向服务器查询天气代号或者天气信息
     */
    private void queryFromServer(final String address,final String type){
//        Log.d("chaxun","从服务器查询县级天气代号");
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                if ("countyCode".equals(type)){
                    if(!TextUtils.isEmpty(response)){
                        String[] array = response.split("\\|");
                        if(array !=null && array.length == 2){
                            String  weatherCode = array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                }else if ("weatherCode".equals(type)){
//                    Log.d("chaxun","调用解析天气数据方法");
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publishText.setText("同步失败");
                    }
                });
            }
        });
    }

    /*
    从SharePreferences文件中读取存储的天气信息，并显示到界面上
     */
    private void showWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name",""));
        temp1Text.setText(prefs.getString("temp1",""));
        temp2Text.setText(prefs.getString("temp2",""));
        weatherDespText.setText(prefs.getString("weather_desp",""));
        publishText.setText("今天"+prefs.getString("publish_time","")+"发布");
        currentDateText.setText(prefs.getString("current_date",""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);

    }
}
