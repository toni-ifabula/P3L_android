package com.example.p3l_android;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.p3l_android.API.MenuAPI;
import com.example.p3l_android.Adapters.CartAdapter;
import com.example.p3l_android.Adapters.MenuAdapter;
import com.example.p3l_android.Models.Cart;
import com.example.p3l_android.Models.DaftarMenu;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<Cart> cartList = new ArrayList<>();
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cart, container, false);

        MyApplication myApplication = (MyApplication) Objects.requireNonNull(getActivity()).getApplicationContext();
        cartList = myApplication.getCartList();

        if (!cartList.isEmpty())
            Toast.makeText(getContext(), cartList.get(0).getNama(), Toast.LENGTH_SHORT).show();

        setAdapter();

        return view;
    }

    public void setAdapter(){
        getActivity().setTitle("Cart");
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new CartAdapter(view.getContext(), cartList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void setCartList(List<Cart> cartList){
        this.cartList = cartList;
    }

}