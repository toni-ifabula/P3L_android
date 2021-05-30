package com.example.p3l_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.p3l_android.API.MenuAPI;
import com.example.p3l_android.API.PesananAPI;
import com.example.p3l_android.Models.DaftarMenu;
import com.google.zxing.Result;
import com.muddzdev.styleabletoast.StyleableToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method.GET;
import static com.android.volley.Request.Method.POST;

public class ScannerActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private CodeScanner mCodeScanner;
    private final Context context = this;
    private MyApplication myApplication;
    public int createNewPesanan = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        myApplication = (MyApplication) getApplicationContext();

        checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);

        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.startPreview();
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String[] arrString = result.getText().split(";", 2);

                        myApplication.setIdReservasi(arrString[0]);

                        checkPesananByReservasi(arrString[0], arrString[1]);

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
            }
        });
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(ScannerActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(context, "Camera permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
        }
        else {
            Toast.makeText(context, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkPesananByReservasi(final String idRerservasi, final String idKaryawan) {
        //deklarasi queue
        RequestQueue queue = Volley.newRequestQueue(context);

        final JsonObjectRequest stringRequest = new JsonObjectRequest(GET, PesananAPI.URL_CHECK + idRerservasi,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //bagian jika response berhasil
                try {
                    if(response.getString("message").equals("Data Pesanan Found")) {
                        JSONObject jsonObject = response.getJSONObject("data");
                        String idPesanan = jsonObject.getString("ID_PESANAN");
                        myApplication.setIdPesanan(idPesanan);

                        new StyleableToast.Builder(context)
                                .text("Data Pesanan Already Exist")
                                .textColor(Color.BLACK)
                                .backgroundColor(Color.GREEN)
                                .show();
                    } else if(response.getString("message").equals("Data Pesanan Not Found")) {
                        createPesanan(idRerservasi, idKaryawan);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //bagian jika response jaringan terdapat gangguan/error
                new StyleableToast.Builder(context)
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

    public void createPesanan(final String idReservasi, final String idKaryawan){
        //Tambahkan tambah buku disini
        //Pendeklarasian queue
        RequestQueue queue = Volley.newRequestQueue(context);

//        final ProgressDialog progressDialog;
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("loading....");
//        progressDialog.setTitle("Creating Pesanan");
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progressDialog.show();

        //Memulai membuat permintaan request menambah data ke jaringan
        StringRequest stringRequest = new StringRequest(POST, PesananAPI.URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Disini bagian jika response jaringan berhasil tidak terdapat ganguan/error
//                progressDialog.dismiss();
                try {
                    //Mengubah response string menjadi object
                    JSONObject obj = new JSONObject(response);
                    JSONObject data = obj.getJSONObject("data");

                    myApplication.setIdPesanan(data.optString("ID_PESANAN"));

                    //obj.getString("message") digunakan untuk mengambil pesan message dari response
                    new StyleableToast.Builder(context)
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
//                progressDialog.dismiss();
                new StyleableToast.Builder(context)
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
                params.put("ID_KARYAWAN", idKaryawan);
                params.put("ID_RESERVASI", idReservasi);
                params.put("STATUS_PESANAN", "Dimasak");
                params.put("SUBTOTAL_PESANAN", "0");
                params.put("SERVICE_PESANAN", "0");
                params.put("TAX_PESANAN", "0");
                params.put("TOTAL_PESANAN", "0");
                params.put("TOTAL_JUMLAH_PESANAN", "0");
                params.put("TOTAL_ITEM_PESANAN", "0");
                params.put("STATUS_LUNAS_PESANAN", "Belum");

                return params;
            }
        };

        //Disini proses penambahan request yang sudah kita buat ke reuest queue yang sudah dideklarasi
        queue.add(stringRequest);
    }

}