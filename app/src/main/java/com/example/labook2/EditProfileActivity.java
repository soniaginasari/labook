package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    private EditText et_nama, et_alamat, et_no_hp, et_email;
    private Button editBtn;
    String nama,alamat,no_hp,email,id;
    ProgressDialog loading;
    Preferences sharedPrefManager;
    Context mContext;
    BaseApiService mApiService;
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
        setContentView(R.layout.activity_edit_profile);

        et_nama = findViewById(R.id.editTextName);
        et_alamat = findViewById(R.id.editTextAlamat);
        et_no_hp = findViewById(R.id.editTextMobile);
        et_email = findViewById(R.id.editTextEmail);
        editBtn = findViewById(R.id.editButton);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        imageView = (ImageView) findViewById(R.id.imageView);
        image = findViewById(R.id.profileImageView);

        mContext = this;
        mApiService = RetrofitClient.getService(getApplicationContext());
        sharedPrefManager = new Preferences(this);

        et_nama.setText(sharedPrefManager.getSPNama());
        et_no_hp.setText(sharedPrefManager.getSPTelepon());
        et_alamat.setText(sharedPrefManager.getSPAlamat());
        et_email.setText(sharedPrefManager.getSPEmail());
        id = sharedPrefManager.getSPId();
        Picasso.get().load(sharedPrefManager.getSPPict()).into(image);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestEdit();
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

    public void requestEdit() {
        mApiService.editRequest(id, et_nama.getText().toString(), et_no_hp.getText().toString(), et_email.getText().toString(), et_alamat.getText().toString(),getStringImage(decoded))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    String nama_edit = jsonRESULTS.getString("name");
                                    String no_hp_edit = jsonRESULTS.getString("no_hp");
                                    String alamat_edit = jsonRESULTS.getString("alamat");
                                    String email_edit = jsonRESULTS.getString("email");
                                    String foto_edit = jsonRESULTS.getString("foto_user");

                                    sharedPrefManager.saveSPString(sharedPrefManager.SP_NAMA, nama_edit);
                                    sharedPrefManager.saveSPString(sharedPrefManager.SP_TELEPON, no_hp_edit);
                                    sharedPrefManager.saveSPString(sharedPrefManager.SP_ALAMAT, alamat_edit);
                                    sharedPrefManager.saveSPString(sharedPrefManager.SP_EMAIL, email_edit);
                                    sharedPrefManager.saveSPString(sharedPrefManager.SP_PICT, foto_edit);

                                    Toast.makeText(mContext, "BERHASIL EDIT", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(mContext, ProfileActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    String error_message = jsonRESULTS.getString("status");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "GAGAL EDIT", Toast.LENGTH_SHORT).show();
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