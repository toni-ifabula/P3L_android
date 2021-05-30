package com.example.p3l_android;

import android.app.Application;

import com.example.p3l_android.Models.Cart;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    private String idReservasi, namaCustomer, nomorMeja, idPesanan;
    private List<Cart> cartList = new ArrayList<>();

    public String getIdReservasi() {
        return idReservasi;
    }

    public void setIdReservasi(String idReservasi) {
        this.idReservasi = idReservasi;
    }

    public String getNamaCustomer() {
        return namaCustomer;
    }

    public void setNamaCustomer(String namaCustomer) {
        this.namaCustomer = namaCustomer;
    }

    public String getNomorMeja() {
        return nomorMeja;
    }

    public void setNomorMeja(String nomorMeja) {
        this.nomorMeja = nomorMeja;
    }

    public List<Cart> getCartList() {
        return cartList;
    }

    public void setCartList(List<Cart> cartList) {
        this.cartList = cartList;
    }

    public String getIdPesanan() {
        return idPesanan;
    }

    public void setIdPesanan(String idPesanan) {
        this.idPesanan = idPesanan;
    }
}
