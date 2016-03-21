package com.tifaniwarnita.metsky;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.support.v4.app.Fragment;

import com.tifaniwarnita.metsky.models.Cuaca;
import com.tifaniwarnita.metsky.models.InformasiCuaca;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.functions.Action1;

public class WeatherInformationService extends Service {
    private Cuaca cuaca;
    private static HomeFragment homeFragment;
    private static Activity homeActivity;

    public WeatherInformationService() {

    }

    public static void initialize(final HomeFragment fragment, final Activity activity) {
        homeFragment = fragment;
        homeActivity = activity;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(homeActivity);
        System.out.println("create loc provider");
        final WeatherInformationService weatherService = this;
        locationProvider.getLastKnownLocation()
                .subscribe(new Action1<Location>() {
                    @Override
                    public void call(Location location) {
                        System.out.println("create new informasi cuaca");
                        InformasiCuaca informasiCuaca = new InformasiCuaca(String.valueOf(location.getLatitude()),
                                String.valueOf(location.getLongitude()), homeActivity, homeFragment, weatherService);
                    }
                });
        return 1;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
