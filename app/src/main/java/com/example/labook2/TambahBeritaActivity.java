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

public class TambahBeritaActivity extends AppCompatActivity {

    String id_laboratorium, id_berita , judul,isi;
    String judulf, isif;
    EditText et_judul, et_isi;
    BaseApiService mApiService;
    Context mContext;
    Preferences sharedPrefManager;
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_berita);

        et_judul = (EditText) findViewById(R.id.editTextJudul);
        et_isi = (EditText) findViewById(R.id.editTextIsi);

        id_laboratorium= getIntent().getStringExtra("id_laboratorium");
        judul = getIntent().getStringExtra("judul");
        isi = getIntent().getStringExtra("isi");
        id_berita = getIntent().getStringExtra("id_berita");

        et_judul.setText(judul);
        et_isi.setText(isi);

        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());
        submit_btn = (Button) findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                judulf = String.valueOf(et_judul.getText());
                isif = String.valueOf(et_isi.getText());
                if (id_berita!=null) {
                    requestEdit();
                }else {
                    requestTambah();
                }
            }
        });
    }

    private void requestTambah() {
        mApiService.SetBerita(judulf,isif,id_laboratorium)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "BERITA BERHASIL DITAMBAHKAN", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(mContext, "BERITA GAGAL DITAMBAHKAN", Toast.LENGTH_SHORT).show();
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

    private void requestEdit() {
        mApiService.EditBerita(id_berita,judulf,isif,id_laboratorium)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "BERHASIL EDIT BERITA", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(mContext, "GAGAL EDIT BERITA", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}