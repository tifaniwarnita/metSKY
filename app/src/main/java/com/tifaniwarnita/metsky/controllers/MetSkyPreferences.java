package com.tifaniwarnita.metsky.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Tifani on 3/17/2016.
 */
public class MetSkyPreferences {
    private static Activity activity;

    private static final String METSKY_SETTINGS = "com.tifaniwarnita.metsky.metSkySettings";
    private static final String EMOTION_SETTING = "emotion";
    private static final String DATE_SETTING = "date";
    private static final String ID_SETTING = "id";
    private static final String NAMA_SETTING = "name";
    private static final String LATITUDE_SETTING = "latitude";
    private static final String LONGITUDE_SETTING = "longitude";

    public static void initialize(Activity activity) {
        MetSkyPreferences.activity = activity;
    }

    public static int getEmotion(Context context) {
        if (context != null) {
            SharedPreferences settings = context.getSharedPreferences(METSKY_SETTINGS, 0);
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

    public static void setEmotion(Context context, int emotion) {
        System.out.println("SET EMOTION");
        System.out.println("emotion: " + emotion);
        Calendar calendar = new GregorianCalendar();
        int today = calendar.get(Calendar.DATE);
        System.out.println("today: " + today);
        if (context != null) {
            SharedPreferences settings = context.getSharedPreferences(METSKY_SETTINGS, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(EMOTION_SETTING, emotion);
            editor.putInt(DATE_SETTING, today);
            editor.commit();
        }
    }

    public static String getId(Context context) {
        if (context != null) {
            SharedPreferences settings = context.getSharedPreferences(METSKY_SETTINGS, 0);
            String id = settings.getString(ID_SETTING, null);
            if (id.equals(""))
                return null;
            else
                return id;
        } else {
            return null;
        }
    }

    public static void setId(Context context, String id) {
        if (context != null) {
            SharedPreferences settings = context.getSharedPreferences(METSKY_SETTINGS, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(ID_SETTING, id);
            editor.commit();
        }
    }

    public static String getNama(Context context) {
        if (context != null) {
            SharedPreferences settings = context.getSharedPreferences(METSKY_SETTINGS, 0);
            String nama = settings.getString(NAMA_SETTING, null);
            if (nama.equals(""))
                return null;
            else
                return nama;
        } else {
            return null;
        }
    }

    public static void setNama(Context context, String nama) {
        if (context != null) {
            SharedPreferences settings = context.getSharedPreferences(METSKY_SETTINGS, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(NAMA_SETTING, nama);
            editor.commit();
        }
    }

    public static void setLatitudeLongitude(Context context, Double latitude, Double longitude) {
        if (context != null) {
            SharedPreferences settings = context.getSharedPreferences(METSKY_SETTINGS, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(LATITUDE_SETTING, latitude.toString());
            editor.putString(LONGITUDE_SETTING, longitude.toString());
            editor.commit();
        }
    }

    public static String getLatitude(Context context) {
        if (context != null) {
            SharedPreferences settings = context.getSharedPreferences(METSKY_SETTINGS, 0);
            String latitude = settings.getString(LATITUDE_SETTING, null);
            return latitude;
        } else {
            return null;
        }
    }

    public static String getLongitude(Context context) {
        if (context != null) {
            SharedPreferences settings = context.getSharedPreferences(METSKY_SETTINGS, 0);
            String longitude = settings.getString(LONGITUDE_SETTING, null);
            return longitude;
        } else {
            return null;
        }
    }

}
