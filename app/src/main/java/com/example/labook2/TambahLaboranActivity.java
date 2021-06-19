package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.adapter.LaboratoriumAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.LaboratoriumItem;
import com.example.labook2.model.ResponseLaboratorium;
import com.example.labook2.model.ResponseUser;
import com.example.labook2.model.UserItem;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TambahLaboranActivity extends AppCompatActivity {

    private EditText nama, alamat, no_hp, email, password, c_password;
    private Button registerBtn;
    String hak_akses, id_laboratorium;
    ProgressDialog loading;

    Context mContext;
    BaseApiService mApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_laboran);

        nama = findViewById(R.id.editTextName);
        alamat = findViewById(R.id.editTextAlamat);
        no_hp = findViewById(R.id.editTextMobile);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        c_password = findViewById(R.id.editTextKonfirmPass);
        registerBtn = findViewById(R.id.RegisterButton);

        id_laboratorium= getIntent().getStringExtra("id_laboratorium");

        hak_akses="2";
        mContext = this;
        mApiService = RetrofitClient.getService(getApplicationContext());

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                requestRegister();
            }
        });
    }


    private void requestRegister(){
        mApiService.registerLaboran(nama.getText().toString(),
                alamat.getText().toString(),
                no_hp.getText().toString(),
                email.getText().toString(),
                hak_akses,
                password.getText().toString(),
                c_password.getText().toString(),
                id_laboratorium)
                .enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response){
                        if (response.isSuccessful()){
                            loading.dismiss();
                            try {
//                                Toast.makeText(mContext, "BERHASIL DIDAFTARKAN", Toast.LENGTH_SHORT).show();
//                                Intent intent1 = new Intent(mContext, LoginActivity.class);
//                                startActivity(intent1);
//                                finish();
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")){
                                    Toast.makeText(mContext, "BERHASIL DIDAFTARKAN", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e){
                                e.printStackTrace();
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "GAGAL DIDAFTARKAN", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t){
                        Log.e("debug", "onFailure: ERROR > " + t.getMessage());
                        Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}