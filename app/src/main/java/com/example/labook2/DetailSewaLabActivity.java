package com.example.labook2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.adapter.LaboranAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.LaboranItem;
import com.example.labook2.model.ResponseLaboran;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailSewaLabActivity extends AppCompatActivity {

    String id_peminjaman, nama_layanan,tgl_pinjam, tgl_selesai, jumlah, satuan, harga, total_harga, keterangan, file, nama_bidang, nama_lab, id, pil_ket, alamat, no_telp, id_bidang, id_laboratorium, alasan, strket_pengerjaan, strket_pembayaran, metode, tgl_bayar, bukti_pembayaran, file_selesai;
    String jumlahfs, tgl_pinjamf, tgl_selesaif, filef;
    String newDateTglSelesai, newDateTglPinjam;
    Button btn_ubahForm, btn_ubah, btn_hapus, btn_pembayaran, btn_file, btn_det_pembayaran, btn_pilihTgl, btn_hasil;
    int akses, ket, jumlahfi, hargaInt, total_hargaInt, ket_pengerjaan, ket_pembayaran, met_pembayaran;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    EditText et_tgl_sewa, et_tgl_selesai, et_jumlah;
    RelativeLayout rl_ket;
    TextView tv_nama_layanan, tv_tgl_pinjam, tv_tgl_selesai, tv_jumlah, tv_satuan, tv_harga, tv_total_harga, tv_keterangan, tv_satuan1, tv_nama_lab, tv_nama_bidang, tv_alamat, tv_no_telp, tv_alasan, tv_satuanf, tv_ket_pengerjaan, tv_file, tv_ket_pembayaran, tv_det_pembayaran;
    private RecyclerView recyclerView;
    List<LaboranItem> laboranList = new ArrayList<>();
    LaboranAdapter laboranAdapter;
    BaseApiService mApiService;
    Context mContext;

    private Button buttonChoose;
    private Button buttonUpload;
    MultipartBody.Part path_pdf;
    private EditText editText;

    //Pdf request code
    private int PICK_PDF_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    //Uri to store the image uri
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sewa_lab);
        Log.i("Debug PDF", "0");

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        tv_nama_layanan = (TextView) findViewById(R.id.nama_layanan);
        tv_harga = (TextView) findViewById(R.id.harga);
        tv_keterangan = (TextView) findViewById(R.id.keterangan);
        tv_tgl_pinjam = (TextView) findViewById(R.id.tgl_pinjam);
        tv_jumlah = (TextView) findViewById(R.id.jumlah);
        tv_total_harga = (TextView) findViewById(R.id.total_harga);
        tv_nama_bidang = (TextView) findViewById(R.id.nama_bidang);
        tv_nama_lab = (TextView) findViewById(R.id.nama_lab);
        tv_alamat = (TextView) findViewById(R.id.alamat);
        tv_no_telp = (TextView) findViewById(R.id.no_telp_lab);
        tv_alasan = (TextView) findViewById(R.id.tv_alasan);
        tv_ket_pengerjaan = (TextView) findViewById(R.id.tv_ket_pengerjaan);
        tv_ket_pembayaran = (TextView) findViewById(R.id.tv_ket_pembayaran);
        rl_ket = (RelativeLayout) findViewById(R.id.rl_ket);

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
        alamat=getIntent().getStringExtra("alamat");
        no_telp=getIntent().getStringExtra("no_telp");
        id_bidang=getIntent().getStringExtra("id_bidang");
        id_laboratorium=getIntent().getStringExtra("id_laboratorium");
        ket = Integer.parseInt(getIntent().getStringExtra("int_keterangan"));
        alasan=getIntent().getStringExtra("alasan");
        ket_pengerjaan= Integer.parseInt(getIntent().getStringExtra("ket_pengerjaan"));
        strket_pengerjaan= getIntent().getStringExtra("keterangan_pengerjaan");
        ket_pembayaran= Integer.parseInt(getIntent().getStringExtra("ket_pembayaran"));
        strket_pembayaran= getIntent().getStringExtra("keterangan_pembayaran");
        metode= getIntent().getStringExtra("metode_pembayaran");
        if (metode != null){
            met_pembayaran = Integer.parseInt(metode);
        }
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
        Log.i("Debug PDF", "2");
        tv_nama_layanan.setText(nama_layanan);
        tv_keterangan.setText(keterangan);
        tv_jumlah.setText("Jumlah = "+jumlah+" "+satuan);

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        int total = Integer.parseInt(total_harga);
        int harga_int = Integer.parseInt(harga);
        tv_total_harga.setText("Total Harga ="+formatRupiah.format((double)total));
        tv_harga.setText("Harga = "+ formatRupiah.format((double)harga_int)+"/"+satuan);

        tv_nama_bidang.setText("Bidang "+nama_bidang);
        tv_nama_lab.setText(nama_lab);
        tv_alamat.setText(alamat);
        tv_no_telp.setText(no_telp);

        btn_det_pembayaran = (Button) findViewById(R.id.btn_det_pembayaran);
        btn_hasil = (Button) findViewById(R.id.btn_hasil);
        tv_alasan.setVisibility(View.GONE);
        tv_ket_pengerjaan.setVisibility(View.GONE);
        tv_ket_pembayaran.setVisibility(View.GONE);
        btn_det_pembayaran.setVisibility(View.GONE);
        btn_hasil.setVisibility(View.GONE);
        Log.i("Debug PDF", "3");
        if (ket == 2){
            btn_det_pembayaran.setVisibility(View.VISIBLE);
        }
        if (ket == 3){
            tv_alasan.setVisibility(View.VISIBLE);
            tv_alasan.setText(alasan);
            rl_ket.setBackgroundColor(getResources().getColor(R.color.ket_merah));
        }

        if (ket == 2 && ket_pengerjaan == 0) {
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
        Log.i("Debug PDF", "4");
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

        btn_hasil.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(file_selesai));
                startActivity(intent);
            }});

        btn_det_pembayaran.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(DetailSewaLabActivity.this, DetailPembayaranActivity.class);
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
                intent.putExtra("id_laboratorium", id_laboratorium);
                startActivity(intent);
            }});

        Log.i("Debug PDF", "6");
        btn_file = (Button) findViewById(R.id.btn_file);
        btn_file.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(file));
                startActivity(intent);
            }});

        btn_ubah = findViewById(R.id.btn_ubah);
        final BottomSheetBehavior sheetBehavior;
        LinearLayout bottom_sheet_edit;bottom_sheet_edit = findViewById(R.id.bottom_sheet_edit);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet_edit);
        // click event for show-dismiss bottom sheet
        btn_ubah.setOnClickListener(new View.OnClickListener()  {
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
        btn_pilihTgl = (Button) findViewById(R.id.buttonTgl);
        et_tgl_sewa = (EditText) findViewById(R.id.et_tgl_sewa);
        et_jumlah = (EditText) findViewById(R.id.et_jumlah);
         tv_satuanf = (TextView) findViewById(R.id.satuanf);

        et_tgl_sewa.setText(tgl_pinjam);
        et_jumlah.setText(jumlah);
        tv_satuanf.setText(satuan);

        btn_pilihTgl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialogSewa();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());
        laboranAdapter = new LaboranAdapter(mContext, laboranList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        getLaboran();
        Log.i("Debug PDF", "7");
        btn_ubahForm = (Button) findViewById(R.id.btn_sewaForm);
        btn_ubahForm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tgl_pinjamf = String.valueOf(et_tgl_sewa.getText());
                jumlahfs = String.valueOf(et_jumlah.getText());
                jumlahfi = Integer.parseInt(jumlahfs);
                hargaInt = Integer.parseInt(harga);
                total_hargaInt = jumlahfi*hargaInt;
//                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestEdit();

            }
        });
        Log.i("Debug PDF", "8");
        //Requesting storage permission
        requestStoragePermission();

        //Initializing views
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonChoose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showFileChooser();

            }
        });

        editText = (EditText) findViewById(R.id.editTextName);

        btn_hapus = (Button) findViewById(R.id.btn_hapus);
        btn_hapus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showAlertDialog();

            }
        });
        Log.i("Debug PDF", "9");
        btn_pembayaran = (Button) findViewById(R.id.btn_bayar);
        btn_pembayaran.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (met_pembayaran != 0){
                    Intent intent = new Intent(DetailSewaLabActivity.this, AkhirPembayaranActivity.class);
                    intent.putExtra("total_harga", total_harga);
                    intent.putExtra("id_peminjaman", id_peminjaman);
                    intent.putExtra("met_pembayaran", metode);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(DetailSewaLabActivity.this, PembayaranActivity.class);
                    intent.putExtra("total_harga", total_harga);
                    intent.putExtra("id_peminjaman", id_peminjaman);
                    startActivity(intent);
                }
            }
        });

        btn_ubah.setVisibility(View.VISIBLE);
        btn_hapus.setVisibility(View.VISIBLE);
        btn_pembayaran.setVisibility(View.GONE);
        if (ket == 1){
            btn_ubah.setVisibility(View.VISIBLE);
            btn_hapus.setVisibility(View.VISIBLE);
        }
        if (ket == 3 ){
            btn_ubah.setVisibility(View.VISIBLE);
            btn_hapus.setVisibility(View.GONE);
        }
        if (ket_pengerjaan == 1 || ket_pengerjaan == 2){
            btn_ubah.setVisibility(View.GONE);
            btn_hapus.setVisibility(View.GONE);
        }
        if (ket ==2 && ket_pembayaran == 1 ){
            btn_ubah.setVisibility(View.GONE);
            btn_hapus.setVisibility(View.GONE);
            btn_pembayaran.setVisibility(View.VISIBLE);
        } if (met_pembayaran != 0){
            btn_ubah.setVisibility(View.GONE);
            btn_hapus.setVisibility(View.GONE);
            btn_pembayaran.setVisibility(View.GONE);
        }
        if (ket_pembayaran == 2){
            btn_ubah.setVisibility(View.GONE);
            btn_hapus.setVisibility(View.GONE);
            btn_pembayaran.setVisibility(View.GONE);
        }
        Log.i("Debug PDF", "10");
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

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Log.i("Debug PDF", "filepath pilih");
            File file_name = null;
            file_name = new File(filePath.getPath());
            editText.setText(file_name.getName());

        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void requestEdit() {
        File file = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            file = new File(FilePath.getPath(this, filePath));
        Log.i("Debug PDF", "filepath");
        file = new File(filePath.getPath());
//        }
        byte[] in_b = null;
        try {
            ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(filePath, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            FileInputStream fis = new FileInputStream(fileDescriptor);
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024 * 4]; //buff is used to store temporary data read cyclically
            int rc = 0;
            while ((rc = fis.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
            in_b = swapStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(filePath)),
                        in_b
                );
        try {
            Log.i("Debug PDF", "filepath2" + Long.toString(requestFile.contentLength()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("pdf", file.getName(), requestFile);
        Log.i("Debug PDF", "filepath3"+body.headers().toString()+"  "+body.body().toString());
        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);
        Log.i("Debug PDF", "filepath4"+description.toString());

        RequestBody tgl_pinjam1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, tgl_pinjamf);

        String jumlahs = String.valueOf(jumlahfs);
        RequestBody jumlah1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, jumlahs);

        RequestBody id_peminjaman1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, id_peminjaman);

        String total_hargas = String.valueOf(total_hargaInt);
        RequestBody total_harga1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, total_hargas);

        RequestBody id_laboratorium1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, id_laboratorium);

        mApiService.EditSewa(id_peminjaman1,tgl_pinjam1,body,jumlah1,total_harga1,id_laboratorium1)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "BERHASIL EDIT PENYEWAAN", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(mContext, MainPeminjamActivity.class);
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
                            Toast.makeText(mContext, "GAGAL EDIT PENYEWAAN", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    private void requestHapus() {
        mApiService.HapusSewa(id_peminjaman)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "BERHASIL HAPUS PENYEWAAN", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(mContext, MainPeminjamActivity.class);
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
                            Toast.makeText(mContext, "GAGAL HAPUS PENYEWAAN", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    private void showAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Menghapus Penyewaan");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Apakah anda yakin ?")
                .setIcon(R.mipmap.logo_labook)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        requestHapus();
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

    private void showDateDialogSewa(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                et_tgl_sewa.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }


}