package com.example.labook2;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.labook2.adapter.LaboranAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.LaboranItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UploadPdfSelesaiActivity extends AppCompatActivity {

    String id_peminjaman, id_peminjam;
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
        setContentView(R.layout.activity_upload_pdf_selesai);

        id_peminjaman = getIntent().getStringExtra("id_peminjaman");
        id_peminjam = getIntent().getStringExtra("id_peminjam");

        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

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
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        buttonUpload.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                requestTambah();
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
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE
            );
        }
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
//        File file = new File(FilePath.getPath(this, filePath));
        // create RequestBody instance from file
        File file = new File(filePath.getPath());

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

        RequestBody id_peminjam1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, id_peminjam);

        RequestBody id_peminjaman1 =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, id_peminjaman);


        Log.i("Debug PDF", "filepath44");

        mApiService.SetPdfSelesai(id_peminjaman1,body,id_peminjam1)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.i("Debug PDF", "filepath5");
                        if (response.isSuccessful()) {
//                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "STATUS PEMINJAMAN SELESAI", Toast.LENGTH_SHORT).show();
                                    Log.i("Debug PDF", "filepath6");
                                    Intent intent = new Intent(mContext, MainLaboranActivity.class);
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
                            Toast.makeText(mContext, "STATUS GAGAL DIEDIT", Toast.LENGTH_SHORT).show();
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