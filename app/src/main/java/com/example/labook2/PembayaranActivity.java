package com.example.labook2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class PembayaranActivity extends AppCompatActivity {
    CardView cd_det_cash, cd_det_transfer;
    TextView tv_total;
    String total_harga, metode, id_peminjam, id_peminjaman;
    CheckBox cb_cash, cb_transfer;
    Preferences sharedPrefManager;
    ProgressDialog loading;
    Context mContext;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);
        tv_total = (TextView) findViewById(R.id.total_harga);
        total_harga=getIntent().getStringExtra("total_harga");
        id_peminjaman=getIntent().getStringExtra("id_peminjaman");
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        int total = Integer.parseInt(total_harga);
        tv_total.setText("Total Harga ="+formatRupiah.format((double)total));
        cd_det_cash = (CardView) findViewById(R.id.det_cash);
        cd_det_transfer = (CardView) findViewById(R.id.det_transfer);
        cd_det_cash.setVisibility(View.GONE);
        cd_det_transfer.setVisibility(View.GONE);
        cb_cash = (CheckBox) findViewById(R.id.check_box_cash);
        cb_transfer = (CheckBox) findViewById(R.id.check_box_transfer);

        cb_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_cash.isChecked()){
                    metode = "1";
                }else{
                    metode = "0";
                }
            }
        });

        cb_transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_transfer.isChecked()){
                    metode = "2";
                }else{
                    metode = "0";
                }
            }
        });

        sharedPrefManager = new Preferences(this);
        id_peminjam = sharedPrefManager.getSPId();
        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());
    }

    private void editMetPembayaran() {
        mApiService.EditMetPembayaran(id_peminjaman, metode)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "BERHASIL EDIT METODE PEMBAYARAN", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PembayaranActivity.this, AkhirPembayaranActivity.class);
                                    intent.putExtra("total_harga", total_harga);
                                    intent.putExtra("id_peminjaman", id_peminjaman);
                                    intent.putExtra("met_pembayaran", metode);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "GAGAL EDIT METODE PEMBAYARAN", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }

    public void cash(View view) {
        cd_det_cash.setVisibility(View.VISIBLE);
    }

    public void transfer(View view) {
        cd_det_transfer.setVisibility(View.VISIBLE);
    }

    public void bayar(View view) {
        editMetPembayaran();

    }
}