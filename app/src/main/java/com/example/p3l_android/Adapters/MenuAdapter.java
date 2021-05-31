package com.example.p3l_android.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.p3l_android.API.DetaiPesananAPI;
import com.example.p3l_android.API.MenuAPI;
import com.example.p3l_android.API.PesananAPI;
import com.example.p3l_android.Models.Cart;
import com.example.p3l_android.Models.DaftarMenu;
import com.example.p3l_android.MyApplication;
import com.example.p3l_android.R;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;
import static com.android.volley.Request.Method.PUT;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.adapterMenuViewHolder> {

    private List<DaftarMenu> daftarMenuList;
    private List<DaftarMenu> daftarMenuListFiltered;
    private Context context;
    private View view;
    private MyApplication myApplication;

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
        String idPesanan = myApplication.getIdPesanan();

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

        holder.btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jumlah = Integer.parseInt(holder.etJumlah.getText().toString());

                jumlah -= 1;

                if(jumlah < 1)
                    jumlah = 1;

                holder.etJumlah.setText(String.valueOf(jumlah));
            }
        });

        holder.btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int jumlah = Integer.parseInt(holder.etJumlah.getText().toString());

                jumlah += 1;

                holder.etJumlah.setText(String.valueOf(jumlah));
            }
        });

        holder.btnPesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idReservasi != null && !idReservasi.isEmpty()) {
                    String idMenu = daftarMenu.getIdMenu();
                    String nama = daftarMenu.getNama();
                    int jml = Integer.parseInt(holder.etJumlah.getText().toString());
                    int harga = daftarMenu.getHarga();
                    int subtotal = jml * harga;

                    checkPesanan(idPesanan, idMenu, jml, subtotal);
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
        private Button btnPesan, btnMinus, btnPlus;
        private EditText etJumlah;

        public adapterMenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvKategori = itemView.findViewById(R.id.tvKategori);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            tvUnit = itemView.findViewById(R.id.tvUnit);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            ivGambar = itemView.findViewById(R.id.ivGambar);
            cardView = itemView.findViewById(R.id.cardMenu);
            btnPesan = itemView.findViewById(R.id.btnPesan);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            etJumlah = itemView.findViewById(R.id.etJumlah);
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

    public void addPesanan(final String idPesanan, final String idMenu, final int jumlah, final int subtotal ) {
        //Tambahkan tambah buku disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menambahkan pesanan");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menambah data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, DetaiPesananAPI.URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    new StyleableToast.Builder(view.getContext())
                            .text(obj.getString("message"))
                            .textColor(Color.BLACK)
                            .backgroundColor(Color.GREEN)
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                new StyleableToast.Builder(view.getContext())
                        .text(error.getMessage())
                        .textColor(Color.BLACK)
                        .backgroundColor(Color.RED)
                        .show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("ID_PESANAN", idPesanan);
                params.put("ID_MENU", idMenu);
                params.put("JUMLAH_ITEM_PESANAN", String.valueOf(jumlah));
                params.put("SUBTOTAL_ITEM_PESANAN", String.valueOf(subtotal));

                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

    private void updateTotalPesanan(final String idPesanan) {
        //deklarasi queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, PesananAPI.URL_UPDATE_TOTAL + idPesanan,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //bagian jika response berhasil
                try {
                    //mengambil data response json object yang berupa data
                    JSONArray jsonArray = response.getJSONArray("data");

                    new StyleableToast.Builder(view.getContext())
                            .text(response.optString("message"))
                            .textColor(Color.BLACK)
                            .backgroundColor(Color.GREEN)
                            .show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //bagian jika response jaringan terdapat gangguan/error
                new StyleableToast.Builder(view.getContext())
                        .text(error.getMessage())
                        .textColor(Color.BLACK)
                        .backgroundColor(Color.RED)
                        .show();
            }
        });

        //proses penambahan request yang sudah kita buat ke request queue
        //yang sudah dideklarasi
        queue.add(stringRequest);
    }

    private void checkPesanan(final String idPesanan, final String idMenu, final int jml, final int subtotal) {
        //deklarasi queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Check Pesanan");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, DetaiPesananAPI.URL_SELECT,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //bagian jika response berhasil
                progressDialog.dismiss();
                int nambah = 0;
                int jumlahSebelum = 0;
                int subtotalSebelum = 0;
                String idDetailPesanan = null;
                String idPesananNambah = null;
                String idMenuNambah = null;
                try {
                    //mengambil data response json object yang berupa data
                    JSONArray jsonArray = response.getJSONArray("data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //mengubah data jsonArray tertentu menjadi object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        idPesananNambah       = jsonObject.optString("ID_PESANAN");
                        idMenuNambah       = jsonObject.optString("ID_MENU");

                        if(idPesananNambah.equals(idPesanan) && idMenuNambah.equals(idMenu)) {
                            nambah = 1;
                            idDetailPesanan = jsonObject.optString("ID_DETAIL_PESANAN");
                            jumlahSebelum = jsonObject.optInt("JUMLAH_ITEM_PESANAN");
                            subtotalSebelum = jsonObject.optInt("SUBTOTAL_ITEM_PESANAN");
                            break;
                        }
                    }

                    if(nambah == 0) {
                        addPesanan(idPesanan, idMenu, jml, subtotal);
                        updateTotalPesanan(idPesanan);
                    } else if (nambah == 1) {
                        updateDetailPesanan(idDetailPesanan, idPesananNambah, idMenuNambah,
                                jml+jumlahSebelum, subtotal+subtotalSebelum);
                        updateTotalPesanan(idPesanan);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //bagian jika response jaringan terdapat gangguan/error
                progressDialog.dismiss();
                new StyleableToast.Builder(view.getContext())
                        .text(error.getMessage())
                        .textColor(Color.BLACK)
                        .backgroundColor(Color.RED)
                        .show();
            }
        });

        //proses penambahan request yang sudah kita buat ke request queue
        //yang sudah dideklarasi
        queue.add(stringRequest);
    }

    private void updateDetailPesanan(final String idDetailPesanan, final String idPesanan,
                                     final String idMenu, final int jumlah, final int subtotal) {

        //Tambahkan tambah buku disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(context);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Update Pesanan");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Memulai membuat permintaan request menambah data ke jaringan
        StringRequest stringRequest = new StringRequest(PUT, DetaiPesananAPI.URL_UPDATE + idDetailPesanan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);

                    new StyleableToast.Builder(view.getContext())
                            .text(obj.getString("message"))
                            .textColor(Color.BLACK)
                            .backgroundColor(Color.GREEN)
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Disini bagian jika response jaringan terdapat ganguan/error
                progressDialog.dismiss();
                new StyleableToast.Builder(view.getContext())
                        .text(error.getMessage())
                        .textColor(Color.BLACK)
                        .backgroundColor(Color.RED)
                        .show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                /*
                    Disini adalah proses memasukan/mengirimkan parameter key dengan data value,
                    dan nama key nya harus sesuai dengan parameter key yang diminta oleh jaringan
                    API.
                */
                Map<String, String>  params = new HashMap<String, String>();
                params.put("ID_PESANAN", idPesanan);
                params.put("ID_MENU", idMenu);
                params.put("JUMLAH_ITEM_PESANAN", String.valueOf(jumlah));
                params.put("SUBTOTAL_ITEM_PESANAN", String.valueOf(subtotal));

                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

}
