package com.example.p3l_android.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.p3l_android.Models.Cart;
import com.example.p3l_android.Models.DetailPesanan;
import com.example.p3l_android.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class PesananAdapter extends RecyclerView.Adapter<PesananAdapter.adapterPesananViewHolder> {

    private Context context;
    private View view;
    private List<DetailPesanan> detailPesananList;

    public PesananAdapter(Context context, List<DetailPesanan> detailPesananList) {
        this.context = context;
        this.detailPesananList = detailPesananList;
    }

    @NonNull
    @Override
    public PesananAdapter.adapterPesananViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        view = layoutInflater.inflate(R.layout.adapter_pesanan, parent, false);

        return new PesananAdapter.adapterPesananViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PesananAdapter.adapterPesananViewHolder holder, int position) {
        final DetailPesanan detailPesanan = detailPesananList.get(position);

        NumberFormat formatter = new DecimalFormat("#,###");
        holder.tvJumlah.setText(detailPesanan.getJumlah() + "x");
        holder.tvNama.setText(detailPesanan.getNama());
        holder.tvSubtotal.setText("Rp " + formatter.format(detailPesanan.getSubtotal()));
    }

    @Override
    public int getItemCount() {
        return detailPesananList.size();
    }

    public class adapterPesananViewHolder extends RecyclerView.ViewHolder{

        private TextView tvJumlah, tvNama, tvSubtotal;

        public adapterPesananViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJumlah = itemView.findViewById(R.id.tvJumlah);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvSubtotal = itemView.findViewById(R.id.tvSubtotal);
        }
    }
}
