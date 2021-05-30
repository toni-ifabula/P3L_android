package com.example.p3l_android;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.p3l_android.API.DetaiPesananAPI;
import com.example.p3l_android.API.MenuAPI;
import com.example.p3l_android.API.PesananAPI;
import com.example.p3l_android.Adapters.MenuAdapter;
import com.example.p3l_android.Adapters.PesananAdapter;
import com.example.p3l_android.Models.DaftarMenu;
import com.example.p3l_android.Models.DetailPesanan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class PesananFragment extends Fragment {

    private View view;
    private TextView tvIdPesanan;
    private MyApplication myApplication;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private PesananAdapter adapter;
    private List<DetailPesanan> detailPesananList;
    private String idPesanan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_pesanan, container, false);

        myApplication = (MyApplication) view.getContext().getApplicationContext();
        idPesanan = myApplication.getIdPesanan();

        tvIdPesanan = view.findViewById(R.id.tvIdPesanan);
        tvIdPesanan.setText(myApplication.getIdPesanan());

        loadDaftarPesanan();

        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadDaftarPesanan();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return view;
    }

    private void loadDaftarPesanan() {
        setAdapter();
        getDaftarPesanan();
    }

    private void setAdapter() {
        getActivity().setTitle("Pesanan");
        detailPesananList = new ArrayList<DetailPesanan>();
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new PesananAdapter(view.getContext(), detailPesananList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    private void getDaftarPesanan() {
        //deklarasi queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data pesanan");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, DetaiPesananAPI.URL_SHOW + idPesanan,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //bagian jika response berhasil
                progressDialog.dismiss();
                try {
                    //mengambil data response json object yang berupa data
                    JSONArray jsonArray = response.getJSONArray("data");

                    if (!detailPesananList.isEmpty())
                        detailPesananList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //mengubah data jsonArray tertentu menjadi object
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        String nama         = jsonObject.optString("NAMA_MENU");
                        int jumlah       = jsonObject.optInt("JUMLAH_ITEM_PESANAN");
                        int subtotal        = jsonObject.optInt("SUBTOTAL_ITEM_PESANAN");
                        //membuat objek
                        DetailPesanan detailPesanan =
                                new DetailPesanan(nama, jumlah, subtotal);

                        //menambahkan obejk tadi ke list
                        detailPesananList.add(detailPesanan);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new StyleableToast.Builder(view.getContext())
                        .text(response.optString("message"))
                        .textColor(Color.BLACK)
                        .backgroundColor(Color.GREEN)
                        .show();
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

}