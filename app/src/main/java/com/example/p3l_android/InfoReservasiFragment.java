package com.example.p3l_android;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.p3l_android.API.ReservasiAPI;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.android.volley.Request.Method.GET;

public class InfoReservasiFragment extends Fragment {

    private View view;
    private MyApplication myApplication;
    private String idReservasi, namaCustomer, nomorMeja;
    private TextView tvNama, tvNomor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_info_reservasi, container, false);

        myApplication = (MyApplication) Objects.requireNonNull(getActivity()).getApplicationContext();
        idReservasi = myApplication.getIdReservasi();

        getReservasiData(idReservasi);

        tvNama = view.findViewById(R.id.tvNama);
        tvNomor = view.findViewById(R.id.tvNomor);

        return view;
    }

    private void getReservasiData(String id_reservasi) {
        //deklarasi queue
        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        //meminta tanggapan string dari URL yang telah disediakan menggunakan method GET
        //untuk request ini tidak memerlukan parameter
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("loading....");
        progressDialog.setTitle("Menampilkan data reservasi");
        progressDialog.setProgressStyle(android.app.ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, ReservasiAPI.URL_SELECT_INFO + id_reservasi,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //bagian jika response berhasil
                progressDialog.dismiss();
                try {
                    //mengambil data response json object yang berupa data
                    JSONObject jsonObjectCustomer = response.getJSONObject("dataCustomer");
                    JSONObject jsonObjectMeja = response.getJSONObject("dataMeja");

                    myApplication.setNamaCustomer(jsonObjectCustomer.optString("NAMA_CUSTOMER"));
                    myApplication.setNomorMeja(jsonObjectMeja.optString("NOMOR_MEJA"));

                    tvNama.setText(myApplication.getNamaCustomer());
                    tvNomor.setText(myApplication.getNomorMeja());

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