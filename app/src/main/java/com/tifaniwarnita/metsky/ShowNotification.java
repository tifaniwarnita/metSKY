package com.tifaniwarnita.metsky;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.tifaniwarnita.metsky.controllers.MetSkyPreferences;
import com.tifaniwarnita.metsky.views.home.MainActivity;

/**
 * Created by Tifani on 5/15/2016.
 */
public class ShowNotification extends Service {

    private final static String TAG = "ShowNotification";

    @Override
    public void onCreate() {
        super.onCreate();

        Intent mainIntent = new Intent(this, MainActivity.class);

        NotificationManager notificationManager
                = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String text;
        try {
            String namaUser = MetSkyPreferences.getNama(getApplicationContext());
            if (namaUser != null) {
                String[] splited = namaUser.split("\\s+");
                text = "Hi, " + splited[0] + " sudah berbagi foto cuaca hari ini?";
            } else {
                text = "Hi, sudah berbagi foto cuaca hari ini?";
            }
        } catch (Exception e) {
            e.printStackTrace();
            text = "Hi, sudah berbagi foto cuaca hari ini?";
        }

        Notification noti = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle(text)
                .setContentText("Mari berbagi info cuaca hari ini")
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_launcher)
                // .setTicker("ticker message")
                .setWhen(System.currentTimeMillis())
                .build();

        notificationManager.notify(0, noti);

        Log.i(TAG, "Notification created");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
