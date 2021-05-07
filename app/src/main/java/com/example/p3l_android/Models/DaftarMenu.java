package com.example.p3l_android.Models;

import java.io.Serializable;

public class DaftarMenu implements Serializable {

    private String nama, kategori, deskripsi, unit, image;
    private Integer harga;

    public DaftarMenu(String nama, String kategori, String deskripsi, String unit, String image, Integer harga) {
        this.nama = nama;
        this.kategori = kategori;
        this.deskripsi = deskripsi;
        this.unit = unit;
        this.harga = harga;
        this.image = image;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
