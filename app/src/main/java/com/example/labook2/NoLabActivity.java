package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class NoLabActivity extends AppCompatActivity {

    BaseApiService mApiService;
    Context mContext;
    String id_laboran, description, fcm;
    Preferences sharedPrefManager;
    String nama, foto_user;
    int hak_akses, akses_laboran;
    Button btn_tambah_lab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_lab);

        btn_tambah_lab = (Button) findViewById(R.id.btn_tambah_lab);
        id_laboran = sharedPrefManager.getSPId();
        mContext = this;

        mApiService = RetrofitClient.getService(mContext.getApplicationContext());
        getAkses_laboran();

        if (akses_laboran == 1) {
            btn_tambah_lab.setVisibility(View.VISIBLE);
        }if (akses_laboran == 2) {
            btn_tambah_lab.setVisibility(View.GONE);
        }
    }

    public void tambah_lab(View view) {
        Intent intent = new Intent(NoLabActivity.this,TambahLaboratoriumActivity.class);
        startActivity(intent);
    }

    public void getAkses_laboran(){
        mApiService.getAkses_laboran(id_laboran)
                .enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    akses_laboran = Integer.parseInt(jsonRESULTS.getString("hak_akses"));
                                    Toast.makeText(mContext, "Akses "+akses_laboran, Toast.LENGTH_SHORT).show();
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
}