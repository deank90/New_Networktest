package cn.konglingwen.new_networktest.util;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/**
 * Created by Administrator on 2016/4/13.
 */
public class Data_analyse {
    public synchronized static void parseXMLWithPull(String xmlData){
        try{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));
            int eventType = xmlPullParser.getEventType();
            String Province = "";
            String City = "";
            String County = "";
            while (eventType != XmlPullParser.END_DOCUMENT){
                String nodeName = xmlPullParser.getName();
                if (eventType == XmlPullParser.START_TAG){
                    if ("province".equals(nodeName)){
                        Province = xmlPullParser.getAttributeValue(null, "name");
                        Log.d("MainActivity", "Province is "+Province);
                    }else if ("city".equals(nodeName)){
                        City = xmlPullParser.getAttributeValue(null, "name");
                        Log.d("MainActivity", "City is " + City);
                    }else if ("county".equals(nodeName)){
                        County = xmlPullParser.getAttributeValue(null, "name");
                        Log.d("MainActivity", "County is "+County);
                    }
                }
            eventType = xmlPullParser.next();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
