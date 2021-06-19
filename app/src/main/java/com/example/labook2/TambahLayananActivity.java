package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
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

public class TambahLayananActivity extends AppCompatActivity {

    String id_bidang, nama_bidang , nama_layanan,unit_satuan, satuan, harga, id_layanan, keterangan,id_laboratorium;
    String nama_layananf,unit_satuanf, satuanf, hargaf, keteranganf;
    TextView tv_nama_bidang;
    EditText et_nama_layanan, et_unit_satuan, et_satuan, et_harga, et_keterangan;
    BaseApiService mApiService;
    Context mContext;
    Preferences sharedPrefManager;
    Button submit_btn;
    int ket_crud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_layanan);

        tv_nama_bidang= (TextView) findViewById(R.id.nama_bidang);
        et_nama_layanan = (EditText) findViewById(R.id.editTextNama);
        et_unit_satuan = (EditText) findViewById(R.id.editTextMinSewa);
        et_satuan = (EditText) findViewById(R.id.editTextSatuan);
        et_harga = (EditText) findViewById(R.id.editTextHarga);
        et_keterangan = (EditText) findViewById(R.id.editTextDeskripsi);

        id_laboratorium= getIntent().getStringExtra("id_laboratorium");
        id_bidang= getIntent().getStringExtra("id_bidang");
        nama_bidang = getIntent().getStringExtra("nama_bidang");
        id_layanan = getIntent().getStringExtra("id_layanan");
        nama_layanan = getIntent().getStringExtra("nama_layanan");
        unit_satuan = getIntent().getStringExtra("unit_satuan");
        satuan = getIntent().getStringExtra("satuan");
        harga = getIntent().getStringExtra("harga");
        keterangan = getIntent().getStringExtra("keterangan");

        tv_nama_bidang.setText(nama_bidang);
        et_nama_layanan.setText(nama_layanan);
        et_unit_satuan.setText(unit_satuan);
        et_satuan.setText(satuan);
        et_harga.setText(harga);
        et_keterangan.setText(keterangan);

        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());
        submit_btn = (Button) findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama_layananf = String.valueOf(et_nama_layanan.getText());
                unit_satuanf = String.valueOf(et_unit_satuan.getText());
                satuanf = String.valueOf(et_satuan.getText());
                hargaf = String.valueOf(et_harga.getText());
                keteranganf = String.valueOf(et_keterangan.getText());
                if (id_layanan!=null) {
                    ket_crud = 2;
                    requestVerifLayanan();
                }else {
                    ket_crud = 1;
                    requestVerifLayanan();
                }
            }
        });
    }

    private void requestVerifLayanan() {
        mApiService.SetVerifLayanan(id_laboratorium,id_layanan,nama_layananf,unit_satuanf,satuanf,hargaf,id_bidang,keteranganf,String.valueOf(ket_crud))
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
                            Toast.makeText(mContext, " DATA LAYANAN GAGAL DIINPUT", Toast.LENGTH_SHORT).show();
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