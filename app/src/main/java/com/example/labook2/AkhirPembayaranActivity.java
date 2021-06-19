package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AkhirPembayaranActivity extends AppCompatActivity {
    CardView cd_met_cash, cd_met_transfer;
    TextView tv_total, tv_tgl_tenggat;
    String total_harga, id_peminjam, id_peminjaman, tgl_tenggat;
    int metode;
    Preferences sharedPrefManager;
    ProgressDialog loading;
    Context mContext;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akhir_pembayaran);
        tv_total = (TextView) findViewById(R.id.total_harga);
        tv_tgl_tenggat = (TextView) findViewById(R.id.tgl_tenggat);
        total_harga=getIntent().getStringExtra("total_harga");
        id_peminjaman=getIntent().getStringExtra("id_peminjaman");
        metode= Integer.parseInt(getIntent().getStringExtra("met_pembayaran"));

        sharedPrefManager = new Preferences(AkhirPembayaranActivity.this.getApplicationContext());
        id_peminjam = sharedPrefManager.getSPId();
        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        int total = Integer.parseInt(total_harga);
        tv_total.setText("Total Harga ="+formatRupiah.format((double)total));

        cd_met_cash = (CardView) findViewById(R.id.met_cash);
        cd_met_transfer = (CardView) findViewById(R.id.met_transfer);
        cd_met_cash.setVisibility(View.GONE);
        cd_met_transfer.setVisibility(View.GONE);

        if (metode == 1){
            cd_met_cash.setVisibility(View.VISIBLE);
        } if (metode == 2){
            cd_met_transfer.setVisibility(View.VISIBLE);
        }

        getTgl_tenggat();

    }

    public void bayar(View view) {
        Intent intent = new Intent(AkhirPembayaranActivity.this, ListSewaActivity.class);
//        intent.putExtra("total_harga", total_harga);
        startActivity(intent);
    }

    public void getTgl_tenggat(){
        mApiService.getTgl_tenggat(id_peminjaman)
                .enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    tgl_tenggat = jsonRESULTS.getString("tgl_tenggat");
                                    SimpleDateFormat dfTglTenggat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date dateTglTenggat = null; //YOUR DATE HERE
                                    try {
                                        dateTglTenggat = dfTglTenggat.parse(tgl_tenggat);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    dfTglTenggat.applyPattern("dd MMMM yyyy");
                                    String newDateTglTenggat = dfTglTenggat.format(dateTglTenggat);
                                    tv_tgl_tenggat.setText(newDateTglTenggat);

                                    Toast.makeText(mContext, "Data tenggat didapatkan ", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(mContext, "Data tidak didapatkan", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t){
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}