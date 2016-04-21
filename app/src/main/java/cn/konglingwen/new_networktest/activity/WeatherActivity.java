package cn.konglingwen.new_networktest.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.konglingwen.new_networktest.R;
import cn.konglingwen.new_networktest.util.Data_analyse;
import cn.konglingwen.new_networktest.util.HttpUtil;

/**
 * Created by Administrator on 2016/4/21.
 */
public class WeatherActivity extends Activity {
    private LinearLayout weatherInfoLayout;
    private TextView aqiText;
    private TextView cityNameText;
    private TextView dateText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private Button switchCity;
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);
        weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText = (TextView) findViewById(R.id.city_name);
        aqiText = (TextView) findViewById(R.id.aqi);
        weatherDespText = (TextView) findViewById(R.id.weather_desp);
        temp1Text = (TextView) findViewById(R.id.temp1);
        temp2Text = (TextView) findViewById(R.id.temp2);
        dateText = (TextView) findViewById(R.id.current_date);
//        switchCity = (Button) findViewById(R.id.)
        dateText = (TextView) findViewById(R.id.current_date);
        String countyCode = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)){
            aqiText.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);

        }else {
            showWeather();
        }
    }

    private void queryWeatherCode(String countyCode){
        String address = "http://wthrcdn.etouch.cn/weather_mini?citykey="+countyCode;
        queryFromServer(address);
    }

    private void queryFromServer(final String address){
        HttpUtil.sendHttpRequest(address, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Data_analyse.handleWeatherResponse(WeatherActivity.this, response);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        aqiText.setText("同步失败！");
                    }
                });
            }
        });
    }

    private void showWeather(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        cityNameText.setText(prefs.getString("city_name",""));
        temp1Text.setText(prefs.getString("temp1",""));
        temp2Text.setText(prefs.getString("temp2",""));
        weatherDespText.setText(prefs.getString("weather_desp", ""));
        aqiText.setText(prefs.getString("aqi", ""));
        dateText.setText(prefs.getString("current_date", ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
    }
}
