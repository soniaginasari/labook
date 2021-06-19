package com.example.labook2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.adapter.LaboranAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.LaboranItem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailSewaLabLaboranActivity extends AppCompatActivity {

    String id_laboran, id_peminjaman, nama_layanan,tgl_pinjam, tgl_selesai, jumlah, satuan, harga, total_harga, keterangan, file, nama_bidang, nama_lab, id, pil_ket, nama_peminjam, no_hp_peminjam, id_bidang, keterangan1, alasan, id_laboratorium, strket_pengerjaan, strket_pembayaran, metode, tgl_bayar, bukti_pembayaran, id_peminjam, file_selesai;
    Button btn_terima, btn_tolak, btn_tolakForm, btn_pengerjaan, btn_selesai, btn_file, btn_det_pembayaran, btn_pilihTgl, btn_hasil, btn_validasi;
    int akses, ket, ket_pengerjaan, ket_pembayaran;
    RelativeLayout rl_ket;
    TextView tv_nama_layanan, tv_tgl_pinjam, tv_tgl_selesai, tv_jumlah, tv_satuan, tv_harga, tv_total_harga, tv_keterangan, tv_satuan1, tv_nama_lab, tv_nama_bidang, tv_nama_peminjam, tv_no_hp_peminjam, tv_file, tv_alasan, tv_ket_pengerjaan, tv_det_pembayaran, tv_ket_pembayaran;
    Preferences sharedPrefManager;
    ProgressDialog loading;
    Context mContext;
    BaseApiService mApiService;
    EditText et_alasan;
    String newDateTglSelesai, newDateTglPinjam, ket_bayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sewa_lab_laboran);

        tv_nama_layanan = (TextView) findViewById(R.id.nama_layanan);
        tv_harga = (TextView) findViewById(R.id.harga);
        tv_keterangan = (TextView) findViewById(R.id.keterangan);
        tv_tgl_pinjam = (TextView) findViewById(R.id.tgl_pinjam);
        tv_jumlah = (TextView) findViewById(R.id.jumlah);
        tv_total_harga = (TextView) findViewById(R.id.total_harga);
        tv_nama_bidang = (TextView) findViewById(R.id.nama_bidang);
        tv_nama_lab = (TextView) findViewById(R.id.nama_lab);
        tv_nama_peminjam = (TextView) findViewById(R.id.nama_peminjam);
        tv_no_hp_peminjam = (TextView) findViewById(R.id.no_hp_peminjam);
        tv_alasan = (TextView) findViewById(R.id.tv_alasan);
        tv_ket_pengerjaan = (TextView) findViewById(R.id.tv_ket_pengerjaan);
        tv_ket_pembayaran = (TextView) findViewById(R.id.tv_ket_pembayaran);
        rl_ket = (RelativeLayout) findViewById(R.id.rl_ket);

        btn_terima = (Button) findViewById(R.id.btn_terima);
        btn_tolak = (Button) findViewById(R.id.btn_tolak);
        btn_tolakForm = (Button) findViewById(R.id.btn_tolakForm);
        btn_pengerjaan = (Button) findViewById(R.id.btn_pengerjaan);
        btn_selesai = (Button) findViewById(R.id.btn_selesai);
        btn_det_pembayaran = (Button) findViewById(R.id.btn_det_pembayaran);
        btn_hasil = (Button) findViewById(R.id.btn_hasil);
        btn_file = (Button) findViewById(R.id.btn_file);
        btn_validasi = (Button) findViewById(R.id.btn_validasi);

        id_peminjaman= getIntent().getStringExtra("id_peminjaman");
        nama_layanan = getIntent().getStringExtra("nama_layanan");
        tgl_pinjam=getIntent().getStringExtra("tgl_pinjam");
        tgl_selesai=getIntent().getStringExtra("tgl_selesai");
        jumlah=getIntent().getStringExtra("jumlah");
        harga=getIntent().getStringExtra("harga");
        total_harga=getIntent().getStringExtra("total_harga");
        satuan=getIntent().getStringExtra("satuan");
        file=getIntent().getStringExtra("file");
        file_selesai=getIntent().getStringExtra("file_selesai");
        keterangan=getIntent().getStringExtra("keterangan");
        nama_bidang=getIntent().getStringExtra("nama_bidang");
        nama_lab=getIntent().getStringExtra("nama_lab");
        id_peminjam=getIntent().getStringExtra("id_peminjam");
        nama_peminjam=getIntent().getStringExtra("nama_peminjam");
        no_hp_peminjam=getIntent().getStringExtra("no_hp_peminjam");
        id_bidang=getIntent().getStringExtra("id_bidang");
        id_laboratorium=getIntent().getStringExtra("id_laboratorium");
        ket= Integer.parseInt(getIntent().getStringExtra("int_keterangan"));
        alasan=getIntent().getStringExtra("alasan");
        ket_pengerjaan= Integer.parseInt(getIntent().getStringExtra("ket_pengerjaan"));
        strket_pengerjaan= getIntent().getStringExtra("keterangan_pengerjaan");
        ket_pembayaran= Integer.parseInt(getIntent().getStringExtra("ket_pembayaran"));
        strket_pembayaran= getIntent().getStringExtra("keterangan_pembayaran");
        metode= getIntent().getStringExtra("metode_pembayaran");
        tgl_bayar= getIntent().getStringExtra("tgl_bayar");
        bukti_pembayaran= getIntent().getStringExtra("bukti_pembayaran");

        SimpleDateFormat dfTglPinjam = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTglPinjam = null; //YOUR DATE HERE
        try {
            dateTglPinjam = dfTglPinjam.parse(tgl_pinjam);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dfTglPinjam.applyPattern("dd MMMM yyyy");
        newDateTglPinjam = dfTglPinjam.format(dateTglPinjam);
        if (tgl_selesai != null) {
            SimpleDateFormat dfTglSelesai = new SimpleDateFormat("yyyy-MM-dd");
            Date dateTglSelesai = null; //YOUR DATE HERE
            try {
                dateTglSelesai = dfTglSelesai.parse(tgl_selesai);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dfTglSelesai.applyPattern("dd MMMM yyyy");
            newDateTglSelesai = dfTglSelesai.format(dateTglSelesai);
            tv_tgl_pinjam.setText(newDateTglPinjam+"-"+newDateTglSelesai);
        }else{
            tv_tgl_pinjam.setText(newDateTglPinjam);
        }
        tv_nama_layanan.setText(nama_layanan);
        tv_keterangan.setText(keterangan);
        tv_jumlah.setText("Jumlah = "+jumlah+" "+satuan);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        int total = Integer.parseInt(total_harga);
        int harga_int = Integer.parseInt(harga);
        tv_total_harga.setText("Total Harga ="+formatRupiah.format((double)total));
        tv_harga.setText("Harga = "+ formatRupiah.format((double)harga_int)+"/"+satuan);
//
        tv_nama_bidang.setText("Bidang "+nama_bidang);
        tv_nama_lab.setText(nama_lab);
        tv_nama_peminjam.setText(nama_peminjam);
        tv_no_hp_peminjam.setText(no_hp_peminjam);

        tv_alasan.setVisibility(View.GONE);
        tv_ket_pengerjaan.setVisibility(View.GONE);
        tv_ket_pembayaran.setVisibility(View.GONE);
        btn_det_pembayaran.setVisibility(View.GONE);
        btn_hasil.setVisibility(View.GONE);
        btn_validasi.setVisibility(View.GONE);

        if (ket == 2){
            btn_det_pembayaran.setVisibility(View.VISIBLE);
        }
        if (ket == 3){
            tv_alasan.setVisibility(View.VISIBLE);
            tv_alasan.setText(alasan);
            rl_ket.setBackgroundColor(getResources().getColor(R.color.ket_merah));
        }
        if (bukti_pembayaran != null){
            btn_validasi.setVisibility(View.VISIBLE);
        }

        if (ket == 2 && ket_pembayaran != 0) {
            tv_keterangan.setVisibility(View.GONE);
            tv_ket_pembayaran.setVisibility(View.VISIBLE);
            tv_ket_pembayaran.setText(strket_pembayaran);
            tv_alasan.setVisibility(View.GONE);
            tv_ket_pengerjaan.setVisibility(View.GONE);
            if (ket_pembayaran == 1){
                rl_ket.setBackgroundColor(getResources().getColor(R.color.ket_kuning));
            }
            if (ket_pembayaran == 2){
                rl_ket.setBackgroundColor(getResources().getColor(R.color.ket_hijau));

            }
        }

        if (ket_pengerjaan == 1 || ket_pengerjaan == 2){
            tv_keterangan.setVisibility(View.GONE);
            tv_ket_pembayaran.setVisibility(View.GONE);
            tv_ket_pengerjaan.setVisibility(View.VISIBLE);
            tv_ket_pengerjaan.setText(strket_pengerjaan);
            if (ket_pengerjaan == 1){
                rl_ket.setBackgroundColor(getResources().getColor(R.color.ket_hijau));
            }
            if (ket_pengerjaan == 2){
                rl_ket.setBackgroundColor(getResources().getColor(R.color.ket_merah));
                btn_hasil.setVisibility(View.VISIBLE);
            }
        }

        btn_validasi.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DetailSewaLabLaboranActivity.this, DetailPembayaranLaboranActivity.class);
                intent.putExtra("total_harga", total_harga);
                intent.putExtra("id_peminjaman", id_peminjaman);
                intent.putExtra("nama_layanan", nama_layanan);
                intent.putExtra("nama_lab", nama_lab);
                intent.putExtra("harga", harga);
                intent.putExtra("satuan", satuan);
                intent.putExtra("jumlah", jumlah);
                intent.putExtra("ket_pembayaran", ket_pembayaran);
                intent.putExtra("keterangan_pembayaran", strket_pembayaran);
                intent.putExtra("metode_pembayaran", metode);
                intent.putExtra("tgl_bayar", tgl_bayar);
                intent.putExtra("bukti_pembayaran", bukti_pembayaran);
                intent.putExtra("id_peminjam", id_peminjam);
                startActivity(intent);
                finish();
            }});

        btn_selesai.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DetailSewaLabLaboranActivity.this, UploadPdfSelesaiActivity.class);
                intent.putExtra("total_harga", total_harga);
                intent.putExtra("id_peminjaman", id_peminjaman);
                intent.putExtra("nama_layanan", nama_layanan);
                intent.putExtra("nama_lab", nama_lab);
                intent.putExtra("harga", harga);
                intent.putExtra("satuan", satuan);
                intent.putExtra("jumlah", jumlah);
                intent.putExtra("ket_pembayaran", ket_pembayaran);
                intent.putExtra("keterangan_pembayaran", strket_pembayaran);
                intent.putExtra("metode_pembayaran", metode);
                intent.putExtra("tgl_bayar", tgl_bayar);
                intent.putExtra("bukti_pembayaran", bukti_pembayaran);
                intent.putExtra("id_peminjam", id_peminjam);
                startActivity(intent);
                finish();
            }});

        btn_det_pembayaran.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DetailSewaLabLaboranActivity.this, DetailPembayaranLaboranActivity.class);
                intent.putExtra("total_harga", total_harga);
                intent.putExtra("id_peminjaman", id_peminjaman);
                intent.putExtra("nama_layanan", nama_layanan);
                intent.putExtra("nama_lab", nama_lab);
                intent.putExtra("harga", harga);
                intent.putExtra("satuan", satuan);
                intent.putExtra("jumlah", jumlah);
                intent.putExtra("ket_pembayaran", ket_pembayaran);
                intent.putExtra("keterangan_pembayaran", strket_pembayaran);
                intent.putExtra("metode_pembayaran", metode);
                intent.putExtra("tgl_bayar", tgl_bayar);
                intent.putExtra("bukti_pembayaran", bukti_pembayaran);
                intent.putExtra("id_peminjam", id_peminjam);
                startActivity(intent);
                finish();
            }});

        btn_hasil.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(file_selesai));
                startActivity(intent);
            }});

        btn_file.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(file));
                startActivity(intent);
            }});

        final BottomSheetBehavior sheetBehavior;
        LinearLayout bottom_sheet_tolak;bottom_sheet_tolak = findViewById(R.id.bottom_sheet_tolak);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet_tolak);
        // click event for show-dismiss bottom sheet
        btn_tolak.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {

                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
//                }
            }});
        // callback for do something
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        sharedPrefManager = new Preferences(this);
        id_laboran = sharedPrefManager.getSPId();
        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        btn_terima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keterangan1 = "2";
                alasan = "";
                ket_bayar = "1";
                showAlertDialogKeterangan();

            }
        });
        et_alasan = (EditText) findViewById(R.id.et_alasan);
        btn_tolakForm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                alasan = String.valueOf(et_alasan.getText());
                keterangan1 = "3";
                ket_bayar = "";
                showAlertDialogKeterangan();
//                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
//                requestKeterangan();

            }
        });

        btn_terima.setVisibility(View.GONE);
        btn_tolak.setVisibility(View.GONE);
        btn_pengerjaan.setVisibility(View.GONE);
        btn_selesai.setVisibility(View.GONE);

        if (ket == 1){
            btn_terima.setVisibility(View.VISIBLE);
            btn_tolak.setVisibility(View.VISIBLE);
        }

        if (ket == 2 && ket_pembayaran == 2){
            btn_pengerjaan.setVisibility(View.VISIBLE);
            btn_selesai.setVisibility(View.GONE);
            btn_terima.setVisibility(View.GONE);
            btn_tolak.setVisibility(View.GONE);
        }

        if (ket_pengerjaan == 1 ){
            btn_pengerjaan.setVisibility(View.GONE);
            btn_selesai.setVisibility(View.VISIBLE);
            btn_terima.setVisibility(View.GONE);
            btn_tolak.setVisibility(View.GONE);
        }

        if (ket_pengerjaan == 2 || ket == 3){
            btn_pengerjaan.setVisibility(View.GONE);
            btn_selesai.setVisibility(View.GONE);
            btn_terima.setVisibility(View.GONE);
            btn_tolak.setVisibility(View.GONE);
            btn_validasi.setVisibility(View.GONE);
        }


//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date strDate = null;
//        try {
//            strDate = sdf.parse(tgl_selesai);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        if (ket_pengerjaan == 2 || ket == 3) {
            btn_terima.setVisibility(View.GONE);
            btn_tolak.setVisibility(View.GONE);
        }

        btn_pengerjaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ket_pengerjaan = 1;
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestPengerjaan();
            }
        });
    }

    private void requestKeterangan() {
        mApiService.EditKeterangan(id_peminjaman,keterangan1,alasan,ket_bayar,id_peminjam)
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

    private void requestPengerjaan() {
        mApiService.EditPengerjaan(id_peminjaman, String.valueOf(ket_pengerjaan),id_peminjam)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "BERHASIL EDIT PENGERJAAN", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(mContext, "GAGAL EDIT PENGERJAAN", Toast.LENGTH_SHORT).show();
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

    private void showAlertDialogKeterangan(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Edit Permintaan");

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
}