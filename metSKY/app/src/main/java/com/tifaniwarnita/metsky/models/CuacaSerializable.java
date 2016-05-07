package com.tifaniwarnita.metsky.models;

import android.location.Location;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by Tifani on 4/5/2016.
 */
public class CuacaSerializable implements Serializable {
    private final String kota;
    private final String level;
    private final String cuaca;
    private ArrayList<String> suhu;
    private ArrayList<String> waktu;
    private ArrayList<String> awan;
    private Date keluar;
    private Date berlaku;

    public CuacaSerializable(Cuaca cuaca) {
        this.kota = cuaca.getKota();
        this.level = cuaca.getLevel();
        this.cuaca = cuaca.getCuaca();
        ArrayList<Integer> tempSuhu = cuaca.getSuhu();
        this.suhu = new ArrayList<>(tempSuhu.size());
        for(Integer i : tempSuhu) {
            this.suhu.add(new String(String.valueOf(i)));
        }
        ArrayList<String> tempWaktu = cuaca.getWaktu();
        this.waktu = new ArrayList<>(tempWaktu.size());
        for(String s : tempWaktu) {
            this.waktu.add(new String(s));
        }
        ArrayList<String> tempAwan = cuaca.getWaktu();
        this.awan = new ArrayList<>(tempAwan.size());
        for(String a : tempAwan) {
            this.awan.add(new String(a));
        }
        this.keluar = cuaca.getKeluar();
        this.berlaku = cuaca.getDateBerlaku();
    }

    public String getKota() {
        return kota;
    }

    public String getLevel() {
        return level;
    }

    public String getCuaca() {
        return cuaca;
    }

    public String getCurrentAwan() {
        return awan.get(getCurrentWaktu());
    }

    public int getCurrentWaktu() {
        Calendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
        System.out.println("hour " + hour);
        boolean found = false;
        int i=0;
        while(!found && i<waktu.size()) {
            String[] splitHour = waktu.get(i).split("-");
            if((hour >= Integer.parseInt(splitHour[0])*60 && hour <= Integer.parseInt(splitHour[1])*60) ||
                    (hour >= Integer.parseInt(splitHour[0])*60 && hour >= Integer.parseInt(splitHour[1])*60 &&
                            Integer.parseInt(splitHour[0]) > Integer.parseInt(splitHour[1])) ||
                    (hour <= Integer.parseInt(splitHour[0])*60 && hour <= Integer.parseInt(splitHour[1])*60 &&
                            Integer.parseInt(splitHour[0]) > Integer.parseInt(splitHour[1]))) {
                found = true;
                System.out.println(Integer.parseInt(splitHour[0])*60 + "-" + Integer.parseInt(splitHour[1])*60);
                System.out.println("now: " + hour);
            } else {
                i++;
            }
        }
        if (!found) i--;
        System.out.println(i);
        return i;
    }

    public String getDikeluarkan() {
        if(this.keluar != null) {
            DateFormat formatKeluar = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
            return formatKeluar.format(this.keluar);
        } else {
            return "";
        }
    }

    public String getBerlaku() {
        if (this.berlaku != null) {
            DateFormat formatBerlaku = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String waktuBerlaku = formatBerlaku.format(this.berlaku);
            String[] splitHour = waktu.get(getCurrentWaktu()).split("-");
            waktuBerlaku = waktuBerlaku + " " + Integer.parseInt(splitHour[0])
                    + ".00-" + Integer.parseInt(splitHour[1]) + ".00";
            return waktuBerlaku;
        } else {
            return "";
        }
    }

    public ArrayList<ArrayList<String>> getSixAwanWaktu() {
        ArrayList<ArrayList<String>> ret = new ArrayList<>();
        int waktuAwal = getCurrentWaktu();
        int waktuAkhir = waktuAwal + 5;
        if (waktuAkhir >= waktu.size()) {
            waktuAkhir = waktu.size()-2;
            waktuAwal = waktuAkhir - 5;
        }
        try {
            for(int i=waktuAwal; i<=waktuAkhir; i++) {
                ArrayList<String> waktuAwan = new ArrayList<>();
                waktuAwan.add(waktu.get(i));
                waktuAwan.add(awan.get(i));
                ret.add(waktuAwan);
            }
        } catch (Exception e) {
            e.printStackTrace(); // idx out of bound
            return ret;
        }
        return ret;
    }

    public ArrayList<String> getSuhu() {
        return suhu;
    }

    public ArrayList<String> getWaktu() { return waktu; }
}
