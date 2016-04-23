package cn.konglingwen.new_networktest.util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

import cn.konglingwen.new_networktest.model.City;
import cn.konglingwen.new_networktest.model.County;
import cn.konglingwen.new_networktest.model.NNDB;
import cn.konglingwen.new_networktest.model.Province;

/**
 * Created by Administrator on 2016/4/13.
 */
public class Data_analyse {
    public synchronized static void parseXMLWithPull(String xmlData, NNDB nndb){
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            int province_id = 0;
            int city_id = 0;
            int county_id = 0;
            Province province_tmp = new Province();
            City city_tmp = new City();
            County county_tmp = new County();
            while (eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                if (eventType == XmlPullParser.START_TAG){
                    if ("province".equals(nodeName)){
                        province_id++;
                        province_tmp.setId(province_id);
                        province_tmp.setProvinceName(xmlPullParser.getAttributeValue(null, "name"));
                        province_tmp.setProvinceCode(xmlPullParser.getAttributeValue(null, "id"));
                        nndb.saveProvince(province_tmp);
//                        Log.d("MainActivity", province_tmp.getId()+"Province is "+province_tmp.getProvinceName());
                    }else if ("city".equals(nodeName)){
                        city_id++;
                        city_tmp.setId(city_id);
                        city_tmp.setCityName(xmlPullParser.getAttributeValue(null, "name"));
                        city_tmp.setCityCode(xmlPullParser.getAttributeValue(null, "id"));
                        city_tmp.setProvinceId(province_id);
                        nndb.saveCity(city_tmp);
//                        Log.d("MainActivity", city_tmp.getId()+"Province is "+ province_tmp.getProvinceName()+"City is " + city_tmp.getCityName());
                    }else if ("county".equals(nodeName)){
                        county_id++;
                        county_tmp.setId(county_id);
                        county_tmp.setCountyName(xmlPullParser.getAttributeValue(null, "name"));
                        county_tmp.setCountyCode(xmlPullParser.getAttributeValue(null, "id"));
                        county_tmp.setWeatherCode(xmlPullParser.getAttributeValue(null, "weatherCode"));
                        county_tmp.setCityId(city_id);
                        nndb.saveCounty(county_tmp);
//                        Log.d("MainActivity", county_tmp.getId()+"City is "+ city_tmp.getCityName() +"County is "+county_tmp.getCountyName());
                    }
                }
            eventType = xmlPullParser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void handleWeatherResponse(Context context, String response, String address){
        try {
            String temp1 = null;
            String temp2 = null;
            String weatherDesp = null;
            String currrent_date = null;
            JSONObject jsonRaw = new JSONObject(response);
            String cityName = jsonRaw.getJSONObject("data").getString("city");
//            String aqi = "AQI:" + jsonRaw.getJSONObject("data").getString("aqi");
            String forecast = jsonRaw.getJSONObject("data").getString("forecast");
            JSONArray jsonArray = new JSONArray(forecast);
            int len = jsonArray.length();
            for (int i = 0; i < len; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (i == 1){
                    currrent_date = jsonObject.getString("date");
                    temp1 = jsonObject.getString("high");
                    temp2 = jsonObject.getString("low");
                    weatherDesp = jsonObject.getString("type");
                    break;
                }
            }
            saveWeatherInfo(context, cityName, temp1, temp2, weatherDesp, currrent_date, address);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static void saveWeatherInfo(Context context, String cityName, String temp1, String temp2,
                                       String weatherDesp,  String current_date, String address){
        String[] temp_array = address.split("=");
        String county_code = temp_array[1];
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
//        editor.putString("aqi", aqi);
        editor.putString("current_date", current_date);
        editor.putString("county_code", county_code);
        editor.commit();

    }
}
