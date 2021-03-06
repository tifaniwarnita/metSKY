package com.tifaniwarnita.metsky.models;

import android.location.Location;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

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
    private Date keluar;
    private Date berlaku;

    public Cuaca(String kota, String level, String cuaca, Location location) {
        this.kota = kota;
        this.level = level;
        this.cuaca = cuaca;
        this.location = location;
        parseCuaca();
        parseTime();
    }

    public Cuaca(String kota, String level, String cuaca, String latitude, String longitude) {
        this.kota = kota;
        this.level = level;
        this.cuaca = cuaca;
        this.location = new Location("");
        this.location.setLatitude(Double.parseDouble(latitude));
        this.location.setLongitude(Double.parseDouble(longitude));
        parseCuaca();
        parseTime();
    }

    public void parseCuaca() {
        Document doc = Jsoup.parse(cuaca);
        Elements tables = doc.select("table");
        for (Element table : tables) {
            Elements trs = table.select("tr");
            for (int i = 0; i < 4; i++) {
                Elements tds = trs.get(i).select("td");
                for (int j = 1; j < tds.size(); j++) {
                    if (i == 0) getWaktu().add(tds.get(j).text());
                    else if (i == 1) getSuhu().add((int) Float.parseFloat(tds.get(j).text()));
                    else if (i == 2) getKelembaban().add((int) Float.parseFloat(tds.get(j).text()));
                    else if (i == 3) getKecepatanAngin().add((int) Float.parseFloat(tds.get(j).text()));
                }
            }
            for (int i = 4; i < trs.size(); i++) {
                Elements tds = trs.get(i).select("td");
                for (int j = 1; j < tds.size(); j++) {
                    Elements img = tds.get(j).select("img");
                    if (i == 4) getArahAngin().add(img.attr("src").replace("images/", "").replace(".gif", ""));
                    else if (i == 5) getAwan().add(img.attr("src").replace("images/", "").replace(".png", ""));
                }
            }
        }
    }

    private void parseTime() {
        String[] firstSplit = cuaca.split("Dikeluarkan: ");
        String[] secondSplit = firstSplit[1].split("<br />Berlaku mulai: ");
        String[] thirdSplit = secondSplit[1].split("<br />Sumber: ");
        String[] fourthSplit = thirdSplit[0].split(" WI");
        String keluar = secondSplit[0];
        String berlaku = fourthSplit[0].substring(0, fourthSplit[0].length()-2) + ":" +
                fourthSplit[0].substring(fourthSplit[0].length() - 2, fourthSplit[0].length());
        System.out.println("keluar: " + keluar);
        System.out.println("berlaku: " + berlaku);

        DateFormat formatKeluar = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
        DateFormat formatBerlaku = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
        try {
            this.keluar = formatKeluar.parse(keluar);
            System.out.println("beres keluar: " + this.getKeluar());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            this.berlaku = formatBerlaku.parse(berlaku);
            Calendar calendarBerlaku = new GregorianCalendar();
            calendarBerlaku.setTime(this.berlaku);
            String[] splitHour = getWaktu().get(getCurrentWaktu()).split("-");
            if (Integer.parseInt(splitHour[0])<16) {
                calendarBerlaku.add(Calendar.DATE, 1);
                this.berlaku = new Date(calendarBerlaku.getTimeInMillis());
            }
            System.out.println("beres berlaku: " + this.berlaku);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void printCuaca() {
        System.out.println("Waktu");
        for (int i=0; i< getWaktu().size(); i++) {
            System.out.println(getWaktu().get(i));
        }
        System.out.println("Suhu");
        for (int i=0; i< getSuhu().size(); i++) {
            System.out.println(getSuhu().get(i));
        }
        System.out.println("Kelembaban");
        for (int i=0; i< getKelembaban().size(); i++) {
            System.out.println(getKelembaban().get(i));
        }
        System.out.println("Kecepatan Angin");
        for (int i=0; i< getKecepatanAngin().size(); i++) {
            System.out.println(getKecepatanAngin().get(i));
        }
        System.out.println("Arah Angin");
        for (int i=0; i< getArahAngin().size(); i++) {
            System.out.println(getArahAngin().get(i));
        }
        System.out.println("Awan");
        for (int i=0; i< getAwan().size(); i++) {
            System.out.println(getAwan().get(i));
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

    public int getCurrentSuhu() {
        return getSuhu().get(getCurrentWaktu());
    }

    public int getCurrentKelembaban() {
        return getKelembaban().get(getCurrentWaktu());
    }

    public int getCurrentKecepatanAngin() {
        return getKecepatanAngin().get(getCurrentWaktu());
    }

    public String getCurrentArahAngin() {
        return getArahAngin().get(getCurrentWaktu());
    }

    public String getCurrentAwan() {
        return getAwan().get(getCurrentWaktu());
    }

    private int getCurrentWaktu() {
        Calendar calendar = new GregorianCalendar();
        int hour = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE);
        System.out.println("hour " + hour);
        boolean found = false;
        int i=0;
        while(!found && i< getWaktu().size()) {
            String[] splitHour = getWaktu().get(i).split("-");
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
        if(this.getKeluar() != null) {
            DateFormat formatKeluar = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.ENGLISH);
            return formatKeluar.format(this.getKeluar());
        } else {
            return "";
        }
    }

    public String getBerlaku() {
        if (this.berlaku != null) {
            DateFormat formatBerlaku = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            String waktuBerlaku = formatBerlaku.format(this.berlaku);
            String[] splitHour = getWaktu().get(getCurrentWaktu()).split("-");
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
        if (waktuAkhir >= getWaktu().size()) {
            waktuAkhir = getWaktu().size()-2;
            waktuAwal = waktuAkhir - 5;
        }
        try {
            for(int i=waktuAwal; i<=waktuAkhir; i++) {
                ArrayList<String> waktuAwan = new ArrayList<>();
                waktuAwan.add(getWaktu().get(i));
                waktuAwan.add(getAwan().get(i));
                ret.add(waktuAwan);
            }
        } catch (Exception e) {
            e.printStackTrace(); // idx out of bound
            return ret;
        }
        return ret;
    }

    public ArrayList<String> getWaktu() {
        return waktu;
    }

    public ArrayList<Integer> getSuhu() {
        return suhu;
    }

    public ArrayList<Integer> getKelembaban() {
        return kelembaban;
    }

    public ArrayList<Integer> getKecepatanAngin() {
        return kecepatanAngin;
    }

    public ArrayList<String> getArahAngin() {
        return arahAngin;
    }

    public ArrayList<String> getAwan() {
        return awan;
    }

    public Date getKeluar() {
        return keluar;
    }

    public Date getDateBerlaku() {
        return berlaku;
    }
}
