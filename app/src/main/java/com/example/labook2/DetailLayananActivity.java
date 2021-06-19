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
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.adapter.LaboranAdapter;
import com.example.labook2.adapter.LayananAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.LaboranItem;
import com.example.labook2.model.LayananItem;
import com.example.labook2.model.ResponseLaboran;
import com.example.labook2.model.ResponseLayanan;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class DetailLayananActivity extends AppCompatActivity {

    ImageView ivfoto_lab;
    Button btn_sewa, btn_sewaForm;
    String id_user, jumlahs, tgl_pinjam, tgl_selesai, file, status, tgl_order;
    String id_laboratorium, id_bidang, id_layanan, nama_lab, nama_bidang, nama_layanan, alamat, no_telp, unit_satuan, satuan, harga, foto_lab, keterangan;
    TextView tvnama_lab, tvnama_bidang, tvnama_layanan, tvalamat, tvno_telp, tvharga, tvsatuan, tvunit_satuan, tvketerangan, tvsatuanf;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormatter;
    EditText et_tgl_sewa,  et_jumlah, et_file;
    int jumlah, total_harga, hargaInt;
    String name;

    private RecyclerView recyclerView;
    List<LaboranItem> laboranList = new ArrayList<>();
    LaboranAdapter laboranAdapter;
    BaseApiService mApiService;
    Context mContext;
    Preferences sharedPrefManager;

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
        setContentView(R.layout.activity_detail_layanan);

        tvnama_lab= (TextView) findViewById(R.id.nama_lab);
        tvalamat = (TextView) findViewById(R.id.alamat);
        tvnama_layanan = (TextView) findViewById(R.id.nama_layanan);
        tvno_telp= (TextView) findViewById(R.id.no_telp);
        tvharga = (TextView) findViewById(R.id.harga);
        tvunit_satuan = (TextView) findViewById(R.id.min_unit);
        tvketerangan = (TextView) findViewById(R.id.deskripsi);
        ivfoto_lab= (ImageView) findViewById(R.id.foto_lab);
        tvsatuanf = (TextView) findViewById(R.id.satuanf);

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
        tvnama_layanan.setText(nama_layanan);
        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        int harga_int = Integer.parseInt(harga);
        tvharga.setText("Harga = "+formatRupiah.format((double)harga_int)+"/"+satuan);
        tvketerangan.setText(keterangan);
        Picasso.get().load(foto_lab).into(ivfoto_lab);
        tvsatuanf.setText(satuan);

        tvunit_satuan.setVisibility(View.GONE);

        if (unit_satuan != null){
            tvunit_satuan.setVisibility(View.VISIBLE);
            tvunit_satuan.setText("Minimal sewa :"+unit_satuan+" "+satuan);
        }

        btn_sewa = findViewById(R.id.btn_sewa);
        final BottomSheetBehavior sheetBehavior;
        LinearLayout bottom_sheet;bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        // click event for show-dismiss bottom sheet
        btn_sewa.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View view) {
                if (sharedPrefManager.getSPSudahLogin()){
                    if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    } else {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }
                }else{
                    Intent intent = new Intent(DetailLayananActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

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

        sharedPrefManager = new Preferences(DetailLayananActivity.this.getApplicationContext());
        id_user= sharedPrefManager.getSPId();

        recyclerView = findViewById(R.id.recyclerView);
        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());
        laboranAdapter = new LaboranAdapter(mContext, laboranList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        getLaboran();

        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        et_tgl_sewa = (EditText) findViewById(R.id.et_tgl_sewa);
        et_jumlah = (EditText) findViewById(R.id.et_jumlah);
//        et_file = (EditText) findViewById(R.id.et_file);
        et_tgl_sewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialogSewa();
            }
        });

//        tgl_order = java.text.DateFormat.getDateTimeInstance().format(new Date());
        tvketerangan.setText(keterangan);
        btn_sewaForm = (Button) findViewById(R.id.btn_sewaForm);
        btn_sewaForm.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "coba tambah", Toast.LENGTH_SHORT).show();
                tgl_pinjam = String.valueOf(et_tgl_sewa.getText());
                jumlahs = String.valueOf(et_jumlah.getText());
                jumlah = Integer.parseInt(jumlahs);
                hargaInt = Integer.parseInt(harga);
                total_harga = jumlah*hargaInt;
                status ="1";
//                file = String.valueOf(et_file.getText());
//                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                Log.i("Debug PDF", "filepath awal");
                requestTambah();
                Log.i("Debug PDF", "filepath tambah");

            }
        });

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadMultipart(Uri fileUri) {
        //getting name for the image
        // create upload service client
//        FileUploadService service =
//                ServiceGenerator.createService(FileUploadService.class);
//
//        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
//        // use the FileUtils to get the actual file by uri
//        File file = FileUtils.getFile(this, fileUri);
        File file = new File(FilePath.getPath(this, filePath));
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(fileUri)),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("pdf", file.getName(), requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, descriptionString);
    }


    private void requestTambah() {

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
                        okhttp3.MultipartBody.FORM, tgl_pinjam);

        String jumlahs = String.valueOf(jumlah);
        RequestBody jumlah1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, jumlahs);

        RequestBody satuan1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, satuan);

        String hargas = String.valueOf(hargaInt);
        RequestBody harga1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, hargas);

        String total_hargas = String.valueOf(total_harga);
        RequestBody total_harga1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, total_hargas);

        RequestBody status1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, status);

        RequestBody id_user1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, id_user);

        RequestBody id_laboratorium1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, id_laboratorium);

        RequestBody id_layanan1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, id_layanan);
        Log.i("Debug PDF", "filepath44");

        mApiService.SetSewa(tgl_pinjam1,jumlah1,satuan1,harga1,total_harga1,body,description,status1,id_user1,id_laboratorium1,id_layanan1)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.i("Debug PDF", "filepath5");
                        if (response.isSuccessful()) {
//                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "SEWA BERHASIL DITAMBAHKAN", Toast.LENGTH_SHORT).show();
                                    Log.i("Debug PDF", "filepath6");
                                    Intent intent = new Intent(mContext, ListSewaActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i("Debug PDF", e.getMessage());
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.i("Debug PDF", e.getMessage());
                            }
                            Log.i("Debug PDF", "bebas");
                        } else {
                            Toast.makeText(mContext, "SEWA GAGAL DITAMBAHKAN", Toast.LENGTH_SHORT).show();
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