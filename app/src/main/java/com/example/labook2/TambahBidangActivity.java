package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class TambahBidangActivity extends AppCompatActivity {
    String id_laboratorium, nama_lab , nama_bidang,id_bidang, alamat, no_telp;
    String nama_bidangf;
    TextView tv_nama_lab;
    EditText et_nama_bidang;
    BaseApiService mApiService;
    Context mContext;
    Preferences sharedPrefManager;
    Button submit_btn;
    int ket_crud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_bidang);

        tv_nama_lab= (TextView) findViewById(R.id.nama_lab);
        et_nama_bidang = (EditText) findViewById(R.id.editTextNamaBidang);

        id_laboratorium= getIntent().getStringExtra("id_laboratorium");
        nama_lab = getIntent().getStringExtra("nama_lab");
        id_bidang=null;
        id_bidang = getIntent().getStringExtra("id_bidang");
        nama_bidang = getIntent().getStringExtra("nama_bidang");
        alamat = getIntent().getStringExtra("alamat");
        no_telp = getIntent().getStringExtra("no_telp");

        tv_nama_lab.setText(nama_lab);
        et_nama_bidang.setText(nama_bidang);

        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());
        submit_btn = (Button) findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama_bidangf = String.valueOf(et_nama_bidang.getText());
                if (id_bidang!=null) {
                    ket_crud = 2;
                    requestVerifBidang();
                }else {
                    ket_crud = 1;
                    requestVerifBidang();
                }
            }
        });
    }

    private void requestVerifBidang() {
        mApiService.SetVerifBidang(id_laboratorium,id_bidang,nama_bidangf,String.valueOf(ket_crud))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "DATA BERHASIL DIINPUT \n MENUNGGU VERIFIKASI KEPALA LAB", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(mContext, " DATA BIDANG GAGAL DIINPUT", Toast.LENGTH_SHORT).show();
//                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
//                        loading.dismiss();
                    }
                });
    }

}