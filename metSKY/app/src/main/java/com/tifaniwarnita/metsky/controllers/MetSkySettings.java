package com.tifaniwarnita.metsky.controllers;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Tifani on 3/17/2016.
 */
public class MetSkySettings {
    private static Activity activity;

    private static final String METSKY_SETTINGS = "com.tifaniwarnita.metsky.metSkySettings";
    private static final String EMOTION_SETTING = "emotion";
    private static final String DATE_SETTING = "date";


    public static void initialize(Activity activity) {
        MetSkySettings.activity = activity;
    }

    public static int getEmotion() {
        if (activity != null) {
            SharedPreferences settings = activity.getSharedPreferences(METSKY_SETTINGS, 0);
            int currentEmotion = settings.getInt(EMOTION_SETTING, -1);
            System.out.println("emotion: " + currentEmotion);
            if (currentEmotion != -1) {
                int emotionDate = settings.getInt(DATE_SETTING, 0);
                Calendar calendar = new GregorianCalendar();
                int today = calendar.get(Calendar.DATE);
                if (emotionDate != today) { // emotion has expired
                    currentEmotion = -1;
                }
                System.out.println("today: " + today);
                System.out.println("emotion date: " + emotionDate);
            }
            System.out.println("current emotion: " + currentEmotion);
            return currentEmotion;
        } else {
            return -1;
        }
    }

    public static void setEmotion(int emotion) {
        System.out.println("SET EMOTION");
        System.out.println("emotion: " + emotion);
        Calendar calendar = new GregorianCalendar();
        int today = calendar.get(Calendar.DATE);
        System.out.println("today: " + today);
        SharedPreferences settings = activity.getSharedPreferences(METSKY_SETTINGS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(EMOTION_SETTING, emotion);
        editor.putInt(DATE_SETTING, today);
        editor.commit();
    }
}
