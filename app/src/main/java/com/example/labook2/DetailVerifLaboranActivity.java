package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.adapter.LaboranAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.LaboranItem;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailVerifLaboranActivity extends AppCompatActivity {

    ImageView ivfoto_lab;
    Button btn_verif;
    String id_user, id_verif_lab, id_verif_bidang, id_verif_layanan, str_ket_crud;
    String id_laboratorium, id_bidang, id_layanan, nama_lab, nama_bidang, nama_layanan, alamat, no_telp, unit_satuan, satuan, harga, foto_lab, keterangan, nama;
    TextView tvnama, tvket_crud, tvnama_layanan, tvalamat, tvno_telp, tvharga, tvsatuan, tvunit_satuan, tvketerangan, tvsatuanf;
    EditText et_tgl_sewa, et_tgl_selesai, et_jumlah, et_file;
    int jumlah, total_harga, hargaInt, ket_crud;
    RelativeLayout rl_lab, rl_layanan;

    BaseApiService mApiService;
    Context mContext;
    Preferences sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_verif_laboran);

        tvnama= (TextView) findViewById(R.id.nama);
        tvalamat = (TextView) findViewById(R.id.alamat);
        tvket_crud= (TextView) findViewById(R.id.ket_crud);
        tvno_telp= (TextView) findViewById(R.id.no_telp);
        tvharga = (TextView) findViewById(R.id.harga);
        tvsatuan = (TextView) findViewById(R.id.satuan);
        tvunit_satuan = (TextView) findViewById(R.id.min_unit);
        tvketerangan = (TextView) findViewById(R.id.deskripsi);
        ivfoto_lab= (ImageView) findViewById(R.id.foto_lab);
        rl_lab = (RelativeLayout) findViewById(R.id.rl_lab);
        rl_layanan = (RelativeLayout) findViewById(R.id.rl_layanan);

        id_verif_lab= getIntent().getStringExtra("id_verif_lab");
        id_verif_bidang= getIntent().getStringExtra("id_verif_bidang");
        id_verif_layanan= getIntent().getStringExtra("id_verif_layanan");
        id_laboratorium= getIntent().getStringExtra("id_laboratorium");
        nama_lab = getIntent().getStringExtra("nama_lab");
        alamat=getIntent().getStringExtra("alamat");
        no_telp=getIntent().getStringExtra("no_telp");
        nama_bidang=getIntent().getStringExtra("nama_bidang");
        id_bidang=getIntent().getStringExtra("id_bidang");
        id_layanan=getIntent().getStringExtra("id_layanan");
        nama_layanan=getIntent().getStringExtra("nama_layanan");
        satuan=getIntent().getStringExtra("satuan");
        harga=getIntent().getStringExtra("harga");
        keterangan=getIntent().getStringExtra("keterangan");
        unit_satuan=getIntent().getStringExtra("unit_satuan");
        foto_lab=getIntent().getStringExtra("foto_lab");
        ket_crud= Integer.parseInt(getIntent().getStringExtra("ket_crud"));
        str_ket_crud=getIntent().getStringExtra("string_ket_crud");

        if (nama_lab != null){
            nama = nama_lab;
            tvalamat.setText("Alamat :"+alamat);
            tvno_telp.setText("No Telepon :"+no_telp);
            Picasso.get().load(foto_lab).into(ivfoto_lab);
            rl_layanan.setVisibility(View.GONE);
        }
        if (nama_bidang != null){
            nama = nama_bidang;
        }
        if (nama_layanan != null){
            nama = nama_layanan;
            tvsatuan.setText("Satuan = "+satuan);
            tvharga.setText("Harga = Rp "+harga+"/"+satuan);
            tvketerangan.setText("Keterangan = \n" +keterangan);
            tvunit_satuan.setText("Minimal sewa :"+unit_satuan);
            Drawable myDrawable = getResources().getDrawable(R.drawable.logo_labook);
            ivfoto_lab.setImageDrawable(myDrawable);
            rl_lab.setVisibility(View.GONE);
        }

        tvnama.setText(nama);

        sharedPrefManager = new Preferences(DetailVerifLaboranActivity.this.getApplicationContext());
        id_user= sharedPrefManager.getSPId();

        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        btn_verif = (Button) findViewById(R.id.btn_verif);
        btn_verif.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

    }

    private void showAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert));


        // set title dialog
        alertDialogBuilder.setTitle("Memverifikasi Data");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Apakah anda yakin ?")
                .setIcon(R.mipmap.logo_labook)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        if (nama_lab != null){
                            if (ket_crud == 2){
                                requestEditLab();
                            }
                        }
                        if (nama_layanan != null){
                            if (ket_crud == 1){
                                requestTambahLayanan();
                            }
                            if (ket_crud == 2){
                                requestEditLayanan();
                            }
                            if (ket_crud == 3){
                                requestHapusLayanan();
                            }
                        }
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void requestEditLab() {
        mApiService.EditLaboratorium(id_laboratorium,nama_lab,no_telp,alamat,foto_lab,id_verif_lab)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "BERHASIL EDIT LABORATORIUM", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(mContext, "GAGAL EDIT LABORATORIUM", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    private void requestTambahLayanan() {
        mApiService.SetLayanan(nama_layanan,unit_satuan,satuan,harga,id_bidang,keterangan,id_verif_layanan)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "LAYANAN BERHASIL DITAMBAHKAN", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(mContext, "LAYANAN GAGAL DITAMBAHKAN", Toast.LENGTH_SHORT).show();
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

    private void requestEditLayanan() {
        mApiService.EditLayanan(id_layanan,nama_layanan,unit_satuan,satuan,harga,id_bidang,keterangan,id_verif_layanan)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "BERHASIL EDIT LAYANAN", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(mContext, "GAGAL EDIT LAYANAN", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    private void requestHapusLayanan() {
        mApiService.HapusLayanan(id_layanan,id_verif_layanan)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "BERHASIL HAPUS LAYANAN", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(mContext, "GAGAL HAPUS LAYANAN", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}