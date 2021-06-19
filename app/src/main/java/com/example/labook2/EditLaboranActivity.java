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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class EditLaboranActivity extends AppCompatActivity {

    String id_laboran, nama_laboran, hak_akses;
    String hak_aksesf;
    TextView tv_nama_laboran;
    BaseApiService mApiService;
    Context mContext;
    Preferences sharedPrefManager;
    Button submit_btn;
    String akses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_laboran);
        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());
        tv_nama_laboran= (TextView) findViewById(R.id.nama_laboran);

        id_laboran= getIntent().getStringExtra("id_laboran");
        Toast.makeText(getApplicationContext(), "Id "+id_laboran, Toast.LENGTH_SHORT).show();
        nama_laboran = getIntent().getStringExtra("nama_laboran");
        tv_nama_laboran.setText(nama_laboran);

        final Spinner List = findViewById(R.id.listItem);
        submit_btn = (Button) findViewById(R.id.btn_submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hak_aksesf = String.valueOf(List.getSelectedItem());

                if (hak_aksesf.equals("Laboran/Teknisi")) {
                    akses = "1";
                } if (hak_aksesf.equals("Kepala Lab")){
                    akses = "2";
                }if (hak_aksesf.equals("Pimpinan")){
                    akses = "3";
                }
                Toast.makeText(getApplicationContext(), "Hak Akses "+hak_aksesf, Toast.LENGTH_SHORT).show();
                EditLaboran();
            }
        });
    }

    private void EditLaboran() {
        mApiService.editLaboran(id_laboran, akses)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(mContext, "DATA LABORAN BERHASIL DIINPUT ", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(mContext, " DATA LABORAN GAGAL DIINPUT", Toast.LENGTH_SHORT).show();
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