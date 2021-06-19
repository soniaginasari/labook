package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.adapter.LaboranAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.LaboranItem;
import com.example.labook2.model.ResponseLaboran;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailLayananLaboranActivity extends AppCompatActivity {

    ImageView ivfoto_lab;
    Button btn_sewa, btn_sewaForm, btn_hapus, btn_edit;
    String id_user, jumlahs, tgl_pinjam, tgl_selesai, file, status, tgl_order;
    String id_laboratorium, id_bidang, id_layanan, nama_lab, nama_bidang, nama_layanan, alamat, no_telp, unit_satuan, satuan, harga, foto_lab, keterangan;
    TextView tvnama_lab, tvnama_bidang, tvnama_layanan, tvalamat, tvno_telp, tvharga, tvsatuan, tvunit_satuan, tvketerangan, tvsatuanf;
    EditText et_tgl_sewa, et_tgl_selesai, et_jumlah, et_file;
    int jumlah, total_harga, hargaInt, ket_crud;

    private RecyclerView recyclerView;
    List<LaboranItem> laboranList = new ArrayList<>();
    LaboranAdapter laboranAdapter;
    BaseApiService mApiService;
    Context mContext;
    Preferences sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_layanan_laboran);

        tvnama_lab= (TextView) findViewById(R.id.nama_lab);
        tvalamat = (TextView) findViewById(R.id.alamat);
//        tvnama_bidang= (TextView) findViewById(R.id.nama_bidang);
        tvnama_layanan = (TextView) findViewById(R.id.nama_layanan);
        tvno_telp= (TextView) findViewById(R.id.no_telp);
        tvharga = (TextView) findViewById(R.id.harga);
//        tvsatuan = (TextView) findViewById(R.id.satuan);
        tvunit_satuan = (TextView) findViewById(R.id.min_unit);
        tvketerangan = (TextView) findViewById(R.id.deskripsi);
        ivfoto_lab= (ImageView) findViewById(R.id.foto_lab);

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

        tvnama_lab.setText(nama_lab);
        tvalamat.setText("Alamat :"+alamat);
        tvno_telp.setText("No Telepon :"+no_telp);
//        tvnama_bidang.setText(nama_bidang);
        tvnama_layanan.setText(nama_layanan);
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        int harga_int = Integer.parseInt(harga);
        tvharga.setText("Harga = "+formatRupiah.format((double)harga_int)+"/"+satuan);
        tvketerangan.setText(keterangan);
//        tvsatuan.setText(satuan);
        tvunit_satuan.setText("Minimal sewa :"+unit_satuan+" "+satuan);
        Picasso.get().load(foto_lab).into(ivfoto_lab);

        sharedPrefManager = new Preferences(DetailLayananLaboranActivity.this.getApplicationContext());
        id_user= sharedPrefManager.getSPId();

        recyclerView = findViewById(R.id.recyclerView);
        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());
        laboranAdapter = new LaboranAdapter(mContext, laboranList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        getLaboran();
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TambahLayananActivity.class);
                intent.putExtra("id_laboratorium", id_laboratorium);
                intent.putExtra("id_bidang", id_bidang);
                intent.putExtra("id_layanan", id_layanan);
                intent.putExtra("nama_bidang", nama_bidang);
                intent.putExtra("nama_layanan", nama_layanan);
                intent.putExtra("unit_satuan", unit_satuan);
                intent.putExtra("satuan", satuan);
                intent.putExtra("harga", harga);
                intent.putExtra("keterangan", keterangan);
                mContext.startActivity(intent);
            }
        });


        btn_hapus = (Button) findViewById(R.id.btn_delete);
        btn_hapus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showAlertDialog();

            }
        });
    }

    private void getLaboran() {
        mApiService.getLaboran(id_laboratorium).enqueue(new Callback<ResponseLaboran>() {
            @Override
            public void onResponse(Call<ResponseLaboran> call, Response<ResponseLaboran> response) {
                if (response.isSuccessful()){

                    final List<LaboranItem> laboran = response.body().getLaboran();

                    recyclerView.setAdapter(new LaboranAdapter(mContext, laboran));
                    laboranAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLaboran> call, Throwable t) {
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert));


        // set title dialog
        alertDialogBuilder.setTitle("Menghapus Layanan");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Apakah anda yakin ?")
                .setIcon(R.mipmap.logo_labook)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        ket_crud = 3;
                        requestVerifLayanan();
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

    private void requestVerifLayanan() {
        mApiService.SetVerifLayanan(id_laboratorium,id_layanan,nama_layanan,unit_satuan,satuan,harga,id_bidang,keterangan,String.valueOf(ket_crud))
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