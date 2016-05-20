package com.tifaniwarnita.metsky.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.Toast;

import com.tifaniwarnita.metsky.models.Cuaca;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Tifani on 4/6/2016.
 */
public class FirebaseHandler {

    public static void laporkanFoto(Context context, Cuaca cuaca, Bitmap photo, ProgressDialog progressDialog) {
        String uid = AuthenticationHandler.getUId();
        try {
            if (uid != null) {
                // Add new data in table user
                String nama = MetSkyPreferences.getNama(context);
                Date date = new Date();
                DateFormat formatTanggal = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                DateFormat formatWaktu = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

                String tanggal = formatTanggal.format(date);
                String waktu = formatWaktu.format(date);
                String stringFoto = encodeBitmapToString(photo);
                String latitude = MetSkyPreferences.getLatitude(context);
                String longitude = MetSkyPreferences.getLongitude(context);

                Map<String, String> keadaanCuaca = new HashMap<String, String>();
                keadaanCuaca.put("nama", nama);
                if (cuaca != null) {
                    keadaanCuaca.put("lokasi", cuaca.getKota());
                } else {
                    keadaanCuaca.put("lokasi", "");
                }
                if (latitude != null && longitude != null) {
                    keadaanCuaca.put("latitude", latitude);
                    keadaanCuaca.put("longitude", longitude);
                } else {

                }
                keadaanCuaca.put("tanggal", tanggal);
                keadaanCuaca.put("waktu", waktu);
                keadaanCuaca.put("foto", stringFoto);

                FirebaseConfig.ref.child("foto_cuaca").push().setValue(keadaanCuaca);
                Toast.makeText(context, "Foto berhasil dilaporkan", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else {
                Toast.makeText(context, "Terjadi error", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Terjadi error", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    public static void laporkanKeadaanCuaca(Context context, Cuaca cuaca, String keadaan) {
        String uid = AuthenticationHandler.getUId();
        if (uid != null) {
            // Add new data in table user
            String nama = MetSkyPreferences.getNama(context);
            Date date = new Date();
            DateFormat formatTanggal = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
            DateFormat formatWaktu = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            String tanggal = formatTanggal.format(date);
            String waktu = formatWaktu.format(date);
            String latitude = MetSkyPreferences.getLatitude(context);
            String longitude = MetSkyPreferences.getLongitude(context);

            Map<String, String> keadaanCuaca = new HashMap<String, String>();
            keadaanCuaca.put("nama", nama);
            if (cuaca != null) {
                keadaanCuaca.put("lokasi", cuaca.getKota());
            } else {
                keadaanCuaca.put("lokasi", "");
            }
            if (latitude != null && longitude != null) {
                keadaanCuaca.put("latitude", latitude);
                keadaanCuaca.put("longitude", longitude);
            } else {

            }
            keadaanCuaca.put("tanggal", tanggal);
            keadaanCuaca.put("waktu", waktu);
            keadaanCuaca.put("keadaan", keadaan);

            FirebaseConfig.ref.child("keadaan_cuaca").push().setValue(keadaanCuaca);
            Toast.makeText(context, "Keadaan cuaca berhasil dilaporkan", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Terjadi error", Toast.LENGTH_SHORT).show();
        }
    }

    private static String encodeBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bYtE);
        bitmap.recycle();
        byte[] byteArray = bYtE.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return imageFile;
    }

    public static void sendUmpanBalik(Context context, String nama, String email, String feedback) {
        Map<String, Object> umpanBalik = new HashMap<String, Object>();
        umpanBalik.put("nama", nama);
        umpanBalik.put("email", email);
        umpanBalik.put("feedback", feedback);

        FirebaseConfig.ref.child("umpan_balik").push().setValue(umpanBalik);
        Toast.makeText(context, "Umpan balik berhasil dikirim", Toast.LENGTH_SHORT).show();
    }
}
