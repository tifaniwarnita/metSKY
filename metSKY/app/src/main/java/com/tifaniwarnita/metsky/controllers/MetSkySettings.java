package com.tifaniwarnita.metsky.controllers;

import android.app.Activity;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Tifani on 3/17/2016.
 */
public class MetSkySettings {
    private static Activity mainActivity;

    private static final String METSKY_SETTINGS = "com.tifaniwarnita.metsky.metSkySettings";
    private static final String EMOTION_SETTING = "emotion";
    private static final String DATE_SETTING = "date";


    public static void initialize(Activity mainActivity) {
        MetSkySettings.mainActivity = mainActivity;
    }

    public static int getEmotion() {
        SharedPreferences settings = mainActivity.getSharedPreferences(METSKY_SETTINGS, 0);
        int currentEmotion = settings.getInt(EMOTION_SETTING, -1);
        if (currentEmotion != -1) {
            int emotionDate = settings.getInt(DATE_SETTING, 0);
            Calendar calendar = new GregorianCalendar();
            int today = calendar.get(Calendar.DATE);
            if (emotionDate != today) { // emotion has expired
                currentEmotion = -1;
            }
        }
        return currentEmotion;
    }

    public static void setEmotion(int emotion) {
        Calendar calendar = new GregorianCalendar();
        int today = calendar.get(Calendar.DATE);
        SharedPreferences settings = mainActivity.getSharedPreferences(METSKY_SETTINGS, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(EMOTION_SETTING, emotion);
        editor.putInt(DATE_SETTING, today);
        editor.commit();
    }
}
