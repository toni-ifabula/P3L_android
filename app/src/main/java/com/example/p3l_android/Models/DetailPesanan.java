package com.example.p3l_android.Models;

import java.io.Serializable;

public class DetailPesanan implements Serializable {

    private String nama;
    private int jumlah, subtotal;

    public DetailPesanan(String nama, int jumlah, int subtotal) {
        this.nama = nama;
        this.jumlah = jumlah;
        this.subtotal = subtotal;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }
}
