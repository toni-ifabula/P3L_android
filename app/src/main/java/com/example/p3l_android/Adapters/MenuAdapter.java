package com.example.p3l_android.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.p3l_android.CartFragment;
import com.example.p3l_android.Models.Cart;
import com.example.p3l_android.Models.DaftarMenu;
import com.example.p3l_android.MyApplication;
import com.example.p3l_android.R;
import com.muddzdev.styleabletoast.StyleableToast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.adapterMenuViewHolder> {

    private List<DaftarMenu> daftarMenuList;
    private List<DaftarMenu> daftarMenuListFiltered;
    private Context context;
    private View view;
    private MyApplication myApplication;
    private List<Cart> cartList = new ArrayList<>();

    public MenuAdapter(Context context, List<DaftarMenu> daftarMenuList) {
        this.context = context;
        this.daftarMenuList = daftarMenuList;
        this.daftarMenuListFiltered = daftarMenuList;
    }

    @NonNull
    @Override
    public MenuAdapter.adapterMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.adapter_menu, parent, false);

        return new MenuAdapter.adapterMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.adapterMenuViewHolder holder, int position) {
        final DaftarMenu daftarMenu = daftarMenuListFiltered.get(position);
        myApplication = (MyApplication) context.getApplicationContext();
        String idReservasi = myApplication.getIdReservasi();

        NumberFormat formatter = new DecimalFormat("#,###");
        holder.tvNama.setText(daftarMenu.getNama());
        holder.tvKategori.setText(daftarMenu.getKategori());
        holder.tvDeskripsi.setText(daftarMenu.getDeskripsi());
        holder.tvUnit.setText("Sajian : " + daftarMenu.getUnit());
        holder.tvHarga.setText("Rp " + formatter.format(daftarMenu.getHarga()));
        Glide.with(context)
                .load(daftarMenu.getImage())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.ivGambar);

        holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idReservasi != null && !idReservasi.isEmpty()) {
                    String idMenu = daftarMenu.getIdMenu();
                    String nama = daftarMenu.getNama();
                    int harga = daftarMenu.getHarga();

                    Cart cart = new Cart(idMenu, nama, harga, 0, 0);
                    cartList.add(cart);
                    MyApplication myApplication = (MyApplication) context.getApplicationContext();
                    myApplication.setCartList(cartList);
                } else {
                    showErrorToast("Anda Belum Scan QR");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (daftarMenuListFiltered != null) ? daftarMenuListFiltered.size() : 0;
    }

    public class adapterMenuViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNama, tvKategori, tvDeskripsi, tvUnit, tvHarga;
        private ImageView ivGambar;
        private CardView cardView;
        private Button btnAddToCart;

        public adapterMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvKategori = itemView.findViewById(R.id.tvKategori);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvUnit = itemView.findViewById(R.id.tvUnit);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            ivGambar = itemView.findViewById(R.id.ivGambar);
            cardView = itemView.findViewById(R.id.cardMenu);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String userInput = charSequence.toString();
                if (userInput.isEmpty()) {
                    daftarMenuListFiltered = daftarMenuList;
                }
                else {
                    List<DaftarMenu> filteredList = new ArrayList<>();
                    for(DaftarMenu daftarMenu : daftarMenuList) {
                        if(String.valueOf(daftarMenu.getNama()).toLowerCase().contains(userInput) ||
                                String.valueOf(daftarMenu.getKategori()).toLowerCase().contains(userInput)) {
                            filteredList.add(daftarMenu);
                        }
                    }
                    daftarMenuListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = daftarMenuListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                daftarMenuListFiltered = (ArrayList<DaftarMenu>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void showErrorToast(String message){
        new StyleableToast.Builder(context)
                .text(message)
                .textColor(Color.BLACK)
                .backgroundColor(Color.RED)
                .show();
    }

}
