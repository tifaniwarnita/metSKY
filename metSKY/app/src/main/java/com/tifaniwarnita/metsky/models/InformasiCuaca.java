package com.tifaniwarnita.metsky.models;

import android.app.Activity;
import android.location.Location;
import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tifaniwarnita.metsky.HomeFragment;
import com.tifaniwarnita.metsky.controllers.WeatherMeteoXmlParser;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tifani on 3/12/2016.
 */
public class InformasiCuaca {
    private Location lokasi;
    private Cuaca cuaca;
    private Activity activity;
    private HomeFragment homeFragment;

    public InformasiCuaca() {
        // Empty constructor
    }

    public InformasiCuaca(String latitude, String longitude, Activity activity, HomeFragment homeFragment) {
        lokasi = new Location("");
        lokasi.setLatitude(Double.parseDouble(latitude));
        lokasi.setLongitude(Double.parseDouble(longitude));
        this.activity = activity;
        this.homeFragment = homeFragment;
        getInformationFromServer();
    }

    private void getInformationFromServer() {
        if (activity != null) {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(activity);
            String url ="http://weather.meteo.itb.ac.id/script/load.php?q=weather";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            // Parse the XML
                            InputStream stream = new ByteArrayInputStream(response.getBytes());
                            System.out.println(stream.toString() + " this is stream");
                            try {
                                List<Cuaca> daftarCuaca = WeatherMeteoXmlParser.parse(stream);
                                cuaca = findCuacaWithClosestLocation(daftarCuaca, lokasi);
                                System.out.println(cuaca.getKota());
                                System.out.println("Level: " + cuaca.getLevel());
                                System.out.println("Lat: " + cuaca.getLatitude());
                                System.out.println("Lon: " + cuaca.getLongitude());
                                System.out.println("Cuaca: " + cuaca.getCuaca());
                                cuaca.parsingCuaca();
                                cuaca.printCuaca();
                                homeFragment.updateUI(cuaca);
                            } catch (XmlPullParserException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("That didn't work!");
                    getInformationFromServer();
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        }
    }

    Cuaca findCuacaWithClosestLocation(List<Cuaca> daftarCuaca, Location lokasi) {
        if (daftarCuaca.size() > 0) {
            Cuaca cuacaTerdekat = daftarCuaca.get(0);
            float jarakTerdekat = -1;
            for(int i=1; i<daftarCuaca.size(); i++) {
                float jarak = lokasi.distanceTo(
                        daftarCuaca.get(i).getLocation());
                if (jarakTerdekat == -1 || jarak < jarakTerdekat) {
                    cuacaTerdekat = daftarCuaca.get(i);
                    jarakTerdekat = jarak;
                }
            }
            return cuacaTerdekat;
        } else {
            return null;
        }
    }

    public Cuaca getCuaca() {
        return cuaca;
    }

}
