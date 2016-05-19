package com.tifaniwarnita.metsky;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;

import com.tifaniwarnita.metsky.controllers.MetSkyPreferences;
import com.tifaniwarnita.metsky.models.Cuaca;
import com.tifaniwarnita.metsky.models.InformasiCuaca;

import java.util.List;

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
        try {
            ReactiveLocationProvider locationProvider = new ReactiveLocationProvider(homeActivity);
            System.out.println("create loc provider");
            final WeatherInformationService weatherService = this;
            if (locationProvider != null) {
                locationProvider.getLastKnownLocation()
                        .subscribe(new Action1<Location>() {
                            @Override
                            public void call(Location location) {
                                System.out.println("create new informasi cuaca");
                                // Save to shared preferences
                                MetSkyPreferences.setLatitudeLongitude(
                                        getApplicationContext(),
                                        location.getLatitude(),
                                        location.getLongitude());
                                new InformasiCuaca(String.valueOf(location.getLatitude()),
                                        String.valueOf(location.getLongitude()), homeActivity, homeFragment, weatherService);
                            }
                        });
            } else {
                String latitude = MetSkyPreferences.getLatitude(getApplicationContext());
                String longitude = MetSkyPreferences.getLongitude(getApplicationContext());
                if (latitude != null && longitude != null) {
                    new InformasiCuaca(latitude, longitude,
                            homeActivity, homeFragment, weatherService);
                } else {
                    LocationManager manager = (LocationManager) homeActivity.getSystemService(Context.LOCATION_SERVICE);
                    Location utilLocation = null;
                    List<String> providers = manager.getProviders(true);
                    for (String provider : providers) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            }
                        }
                        utilLocation = manager.getLastKnownLocation(provider);
                        if(utilLocation != null) {
                            System.out.println("new informasi cuaca");
                            // Save to shared preferences
                            MetSkyPreferences.setLatitudeLongitude(
                                    getApplicationContext(),
                                    utilLocation.getLatitude(),
                                    utilLocation.getLongitude());
                            new InformasiCuaca(String.valueOf(utilLocation.getLatitude()),
                                    String.valueOf(utilLocation.getLongitude()), homeActivity, homeFragment, weatherService);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
