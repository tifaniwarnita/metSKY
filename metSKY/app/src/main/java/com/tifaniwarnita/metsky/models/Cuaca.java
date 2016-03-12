package com.tifaniwarnita.metsky.models;

import android.location.Location;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Tifani on 3/12/2016.
 */
public class Cuaca {
    private final String kota;
    private final String level;
    private final String cuaca;
    private Location location;
    private ArrayList<String> waktu = new ArrayList<>();
    private ArrayList<Integer> suhu = new ArrayList<>();
    private ArrayList<Integer> kelembaban = new ArrayList<>();
    private ArrayList<Integer> kecepatanAngin = new ArrayList<>();
    private ArrayList<String> arahAngin = new ArrayList<>();
    private ArrayList<String> awan = new ArrayList<>();


    public Cuaca(String kota, String level, String cuaca, Location location) {
        this.kota = kota;
        this.level = level;
        this.cuaca = cuaca;
        this.location = location;
    }

    public void parsingCuaca() {
        Document doc = Jsoup.parse(cuaca);
        Elements tables = doc.select("table");
        for (Element table : tables) {
            Elements trs = table.select("tr");
            for (int i = 0; i < 4; i++) {
                Elements tds = trs.get(i).select("td");
                for (int j = 1; j < tds.size(); j++) {
                    if (i == 0) waktu.add(tds.get(j).text());
                    else if (i == 1) suhu.add((int) Float.parseFloat(tds.get(j).text()));
                    else if (i == 2) kelembaban.add((int) Float.parseFloat(tds.get(j).text()));
                    else if (i == 3) kecepatanAngin.add((int) Float.parseFloat(tds.get(j).text()));
                }
            }
            for (int i = 4; i < trs.size(); i++) {
                Elements tds = trs.get(i).select("td");
                for (int j = 1; j < tds.size(); j++) {
                    Elements img = tds.get(j).select("img");
                    if (i == 4) arahAngin.add(img.attr("src").replace("images/", "").replace(".gif", ""));
                    else if (i == 5) awan.add(img.attr("src").replace("images/", "").replace(".png", ""));
                }
            }
        }
    }

    public void printCuaca() {
        System.out.println("Waktu");
        for (int i=0; i<waktu.size(); i++) {
            System.out.println(waktu.get(i));
        }
        System.out.println("Suhu");
        for (int i=0; i<suhu.size(); i++) {
            System.out.println(suhu.get(i));
        }
        System.out.println("Kelembaban");
        for (int i=0; i<kelembaban.size(); i++) {
            System.out.println(kelembaban.get(i));
        }
        System.out.println("Kecepatan Angin");
        for (int i=0; i<kecepatanAngin.size(); i++) {
            System.out.println(kecepatanAngin.get(i));
        }
        System.out.println("Arah Angin");
        for (int i=0; i<arahAngin.size(); i++) {
            System.out.println(arahAngin.get(i));
        }
        System.out.println("Awan");
        for (int i=0; i<awan.size(); i++) {
            System.out.println(awan.get(i));
        }
    }

    public String getKota() {
        return kota;
    }

    public String getLevel() {
        return level;
    }

    public String getLatitude() {
        return String.valueOf(location.getLatitude());
    }

    public String getLongitude() {
        return String.valueOf(location.getLongitude());
    }

    public String getCuaca() {
        return cuaca;
    }

    public Location getLocation() {
        return location;
    }

    // TODO: bukan 0
    public int getCurrentSuhu() {
        return suhu.get(0);
    }

    public int getCurrentKelembaban() {
        return kelembaban.get(0);
    }

    public int getCurrentKecepatanAngin() {
        return kecepatanAngin.get(0);
    }

    public String getCurrentArahAngin() {
        return arahAngin.get(0);
    }

    public String getCurrentAwan() {
        return awan.get(0);
    }

}
