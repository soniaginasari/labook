package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TambahLaboratoriumActivity extends AppCompatActivity {

    String id_laboratorium, nama_lab , no_telp,alamat, foto_lab;
    String nama_labf , no_telpf,alamatf, foto_labf;
    EditText et_nama_lab, et_no_telp, et_alamat;
    BaseApiService mApiService;
    Context mContext;
    Preferences sharedPrefManager;
    Button submit_btn;
    int ket_crud;
    ImageView image;
    Button buttonChoose;
    ImageView imageView;
    Bitmap bitmap, decoded;
    int PICK_IMAGE_REQUEST = 1;
    int bitmap_size = 60;
    EditText txt_foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_laboratorium);


        et_nama_lab = (EditText) findViewById(R.id.editTextName);
        et_no_telp = (EditText) findViewById(R.id.editTextMobile);
        et_alamat = (EditText) findViewById(R.id.editTextAlamat);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        imageView = (ImageView) findViewById(R.id.imageView);
        image = findViewById(R.id.profileImageView);

        id_laboratorium = getIntent().getStringExtra("id_laboratorium");
        nama_lab = getIntent().getStringExtra("nama_lab");
        no_telp = getIntent().getStringExtra("no_telp");
        alamat = getIntent().getStringExtra("alamat");
        foto_lab = getIntent().getStringExtra("foto_lab");

        et_nama_lab.setText(nama_lab);
        et_no_telp.setText(no_telp);
        et_alamat.setText(alamat);

        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());
        submit_btn = (Button) findViewById(R.id.SubmitButton);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nama_labf = String.valueOf(et_nama_lab.getText());
                no_telpf = String.valueOf(et_no_telp.getText());
                alamatf = String.valueOf(et_alamat.getText());

                if (id_laboratorium!=null) {
                    ket_crud = 2;
                    requestVerifLab();
                }else {
                    ket_crud = 1;
                    requestTambah();
                }
            }
        });

        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
//                txt_foto.setText(getStringImage(decoded));
            }
        });
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void kosong() {
        imageView.setImageResource(0);

    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageView.setImageBitmap(decoded);
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void requestVerifLab() {
        mApiService.SetVerifLab(id_laboratorium,nama_labf,no_telpf,alamatf,getStringImage(decoded), String.valueOf(ket_crud))
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
                            Toast.makeText(mContext, " DATA LABORATORIUM GAGAL DITAMBAHKAN", Toast.LENGTH_SHORT).show();
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

    private void requestTambah() {
        mApiService.SetLaboratorium(nama_labf,no_telpf,alamatf,getStringImage(decoded))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "LABORATORIUM BERHASIL DITAMBAHKAN", Toast.LENGTH_SHORT).show();

//                                    Intent intent = new Intent(mContext, DetailLabLaboranActivity.class);
//                                    intent.putExtra("id_bidang", id_bidang);
//                                    intent.putExtra("nama_lab", nama_lab);
//                                    intent.putExtra("alamat", alamat);
//                                    intent.putExtra("no_telp", no_telp);
//                                    intent.putExtra("id_laboratorium", id_laboratorium);
//                                    startActivity(intent);
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
                            Toast.makeText(mContext, "LABORATORIUM GAGAL DITAMBAHKAN", Toast.LENGTH_SHORT).show();
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