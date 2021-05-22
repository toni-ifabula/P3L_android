package com.example.p3l_android.Models;

import java.io.Serializable;

public class Cart implements Serializable {

    String idMenu, nama;
    int harga, subtotal, jumlah;

    public Cart(String idMenu, String nama, int harga, int subtotal, int jumlah) {
        this.idMenu = idMenu;
        this.nama = nama;
        this.harga = harga;
        this.subtotal = subtotal;
        this.jumlah = jumlah;
    }

    public String getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(String idMenu) {
        this.idMenu = idMenu;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
}
