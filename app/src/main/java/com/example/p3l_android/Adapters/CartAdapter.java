package com.example.p3l_android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p3l_android.Models.Cart;
import com.example.p3l_android.Models.DaftarMenu;
import com.example.p3l_android.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.adapterCartViewHolder> {

    private Context context;
    private View view;
    private List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartAdapter.adapterCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.adapter_cart, parent, false);

        return new CartAdapter.adapterCartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.adapterCartViewHolder holder, int position) {
        final Cart cart = cartList.get(position);

        NumberFormat formatter = new DecimalFormat("#,###");
        holder.tvNama.setText(cart.getNama());
        holder.tvHarga.setText(cart.getIdMenu());
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class adapterCartViewHolder extends RecyclerView.ViewHolder{

        private TextView tvNama, tvHarga;

        public adapterCartViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tvNama);
            tvHarga = itemView.findViewById(R.id.tvHarga);
        }
    }
}
