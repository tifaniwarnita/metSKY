package com.tifaniwarnita.metsky.models;

/**
 * Created by Tifani on 5/7/2016.
 */
public class Awan {
    private String jenis;
    private String nama;
    private String deskripsiSingkat;
    private String ketinggian;
    private String bentuk;
    private String namaLatin;
    private String prespitasi;
    private String deskripsiLengkap;
    private String sumberGambar;

    public Awan(String jenis, String nama, String deskripsiSingkat, String ketinggian, String bentuk, String namaLatin, String prespitasi, String deskripsiLengkap, String sumberGambar) {
        this.jenis = jenis;
        this.nama = nama;
        this.deskripsiSingkat = deskripsiSingkat;
        this.ketinggian = ketinggian;
        this.bentuk = bentuk;
        this.namaLatin = namaLatin;
        this.prespitasi = prespitasi;
        this.deskripsiLengkap = deskripsiLengkap;
        this.sumberGambar = sumberGambar;
    }

    public String getJenis() {
        return jenis;
    }

    public String getNama() {
        return nama;
    }

    public String getDeskripsiSingkat() {
        return deskripsiSingkat;
    }

    public String getKetinggian() {
        return ketinggian;
    }

    public String getBentuk() {
        return bentuk;
    }

    public String getNamaLatin() {
        return namaLatin;
    }

    public String getPrespitasi() {
        return prespitasi;
    }

    public String getDeskripsiLengkap() {
        return deskripsiLengkap;
    }

    public String getSumberGambar() {
        return sumberGambar;
    }
}
