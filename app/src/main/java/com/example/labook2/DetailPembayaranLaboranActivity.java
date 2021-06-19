package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailPembayaranLaboranActivity extends AppCompatActivity {

    String id_peminjaman, nama_layanan, jumlah, satuan, harga, total_harga, nama_lab, id_bidang, id_laboratorium, strket_pembayaran, metode, id_peminjam, tgl_tenggat, tgl_bayar, bukti_pembayaran;
    TextView tv_nama_layanan, tv_jumlah, tv_harga, tv_total_harga,  tv_nama_lab,  tv_ket_pembayaran, tv_metode, tv_tgl_tenggat, tv_tgl_bayar;
    int met_pembayaran, ket_pembayaran;
    Preferences sharedPrefManager;
    ProgressDialog loading;
    Context mContext;
    BaseApiService mApiService;
    Button btn_val;
    ImageView img_bukti;
    CardView cd_tgl_tenggat, cd_bukti, cd_tgl_val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pembayaran_laboran);

        btn_val = (Button) findViewById(R.id.btn_val_pembayaran);
        tv_harga = (TextView) findViewById(R.id.harga);
        tv_jumlah = (TextView) findViewById(R.id.jumlah);
        tv_total_harga = (TextView) findViewById(R.id.total_harga);
        tv_ket_pembayaran = (TextView) findViewById(R.id.tv_ket_pembayaran);
        tv_metode = (TextView) findViewById(R.id.met_pembayaran);
        tv_tgl_tenggat = (TextView) findViewById(R.id.tgl_tenggat);
        tv_tgl_bayar = (TextView) findViewById(R.id.tgl_bayar);
        img_bukti = (ImageView) findViewById(R.id.img_bukti);
        cd_tgl_tenggat = (CardView) findViewById(R.id.cd_tgl_tenggat);
        cd_bukti = (CardView) findViewById(R.id.cd_bukti);
        cd_tgl_val = (CardView) findViewById(R.id.cd_tgl_val);

        id_peminjaman= getIntent().getStringExtra("id_peminjaman");
        id_peminjam= getIntent().getStringExtra("id_peminjam");
        nama_layanan = getIntent().getStringExtra("nama_layanan");
        jumlah=getIntent().getStringExtra("jumlah");
        harga=getIntent().getStringExtra("harga");
        total_harga=getIntent().getStringExtra("total_harga");
        satuan=getIntent().getStringExtra("satuan");
        nama_lab=getIntent().getStringExtra("nama_lab");
        id_bidang=getIntent().getStringExtra("id_bidang");
        id_laboratorium=getIntent().getStringExtra("id_laboratorium");
        strket_pembayaran= getIntent().getStringExtra("keterangan_pembayaran");
//        ket_pembayaran = Integer.parseInt(getIntent().getStringExtra("ket_pembayaran"));
        met_pembayaran= Integer.parseInt(getIntent().getStringExtra("metode_pembayaran"));
        tgl_bayar= getIntent().getStringExtra("tgl_bayar");

        cd_tgl_val.setVisibility(View.GONE);
        if (tgl_bayar != null){
            SimpleDateFormat dfTglBayar = new SimpleDateFormat("yyyy-MM-dd");
            Date dateTglBayar = null; //YOUR DATE HERE
            try {
                dateTglBayar = dfTglBayar.parse(tgl_bayar);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dfTglBayar.applyPattern("dd MMMM yyyy");
            String newDateTglBayar = dfTglBayar.format(dateTglBayar);
            tv_tgl_bayar.setText(newDateTglBayar);
            cd_tgl_val.setVisibility(View.VISIBLE);
        }

        bukti_pembayaran= getIntent().getStringExtra("bukti_pembayaran");
        cd_bukti.setVisibility(View.GONE);
        if (bukti_pembayaran != null){
            cd_bukti.setVisibility(View.VISIBLE);
        }
        Picasso.get().load(bukti_pembayaran).into(img_bukti);

        if (met_pembayaran == 1){
            metode = "Pembayaran Cash";
        } if (met_pembayaran == 2){
            metode = "Pembayaran Transfer Bank";
        } if (met_pembayaran == 0) {
            metode = "Belum ditentukan";
        }

        sharedPrefManager = new Preferences(DetailPembayaranLaboranActivity.this.getApplicationContext());
//        id_peminjam = sharedPrefManager.getSPId();
        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        tv_jumlah.setText("Jumlah = "+jumlah+" "+satuan);
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        int total = Integer.parseInt(total_harga);
        int harga_int = Integer.parseInt(harga);
        tv_total_harga.setText("Total Harga ="+formatRupiah.format((double)total));
        tv_harga.setText("Harga = "+ formatRupiah.format((double)harga_int)+"/"+satuan);
        tv_metode.setText(metode);
        tv_ket_pembayaran.setText(strket_pembayaran);

        getTgl_tenggat();
        btn_val.setVisibility(View.VISIBLE);
        cd_tgl_tenggat.setVisibility(View.VISIBLE);
        if ("Pembayaran lunas".equals(strket_pembayaran)){
            btn_val.setVisibility(View.GONE);
            cd_tgl_tenggat.setVisibility(View.GONE);
        }
        btn_val.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                showAlertDialogValidasi();
            }});
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

    private void showAlertDialogValidasi(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert));

        // set title dialog
        alertDialogBuilder.setTitle("Edit Validasi");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Apakah anda yakin ?")
                .setIcon(R.mipmap.logo_labook)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                        requestKeterangan();
//                        DetailSewaLabActivity.this.finish();
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

    private void requestKeterangan() {
        mApiService.EditValBayar(id_peminjaman,id_peminjam)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "BERHASIL EDIT STATUS", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(mContext, MainLaboranActivity.class);
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
                            Toast.makeText(mContext, "GAGAL EDIT STATUS", Toast.LENGTH_SHORT).show();
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
}