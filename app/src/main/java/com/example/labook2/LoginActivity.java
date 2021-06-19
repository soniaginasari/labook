package com.example.labook2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    private EditText email;
    private EditText password;
    private Button login;
    ProgressDialog loading;
    Context mContext;
    BaseApiService mApiService;
    Preferences sharedPrefManager;
    int akses;
    CheckBox checkBox;
    GoogleApiClient googleApiClient;
    Integer captcha;
    String newToken;

    //SiteKey & SecretKey Recaptcha
    String SiteKey = "6Ld9BWUaAAAAAOLbOjLOqHQ2ik3Nt0QMn8h5x5Kn";
    String SecretKey = "6Ld9BWUaAAAAADdHzci0j6jI6D2XSck5GeiAb6iO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
        checkBox = findViewById(R.id.check_box);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(LoginActivity.this)
                .build();
        googleApiClient.connect();
        captcha = 0;

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()){
                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient,SiteKey)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status status = recaptchaTokenResult.getStatus();
                                    if ((status != null) && status.isSuccess()) {
                                        Toast.makeText(getApplicationContext(), "Verifikasi Sukses", Toast.LENGTH_LONG).show();
                                        checkBox.setTextColor(Color.GREEN);
                                        captcha = 1;
                                    }
                                }
                            });
                }else{
                    checkBox.setTextColor(Color.BLACK);
                    captcha = 0;
                }
            }
        });

        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        setTitle("Login");

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        mContext = this;
        mApiService = RetrofitClient.getService(getApplicationContext());
        sharedPrefManager = new Preferences(this);

        if (sharedPrefManager.getSPSudahLogin()){
            akses = Integer.parseInt(sharedPrefManager.getSPAkses());
            if (akses == 1){
                startActivity(new Intent(LoginActivity.this, MainPeminjamActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }if (akses == 2){
                startActivity(new Intent(LoginActivity.this, MainLaboranActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        }

        login = findViewById(R.id.LoginButton);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (captcha == 1){
                    loading = ProgressDialog.show(mContext, null, "Harap Tunggu...", true, false);
                    requestLogin();
                }else {
                    Toast.makeText(mContext, "Maaf Anda harus memverifikasi bahwa Anda bukan robot", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void requestLogin(){
        String token = FirebaseInstanceId.getInstance().getToken();
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this,new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                newToken = instanceIdResult.getToken();
            }
        });
        mApiService.loginRequest(email.getText().toString(), password.getText().toString(),FirebaseInstanceId.getInstance().getToken())
                .enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    String id_user = jsonRESULTS.getString("id");
                                    String nama_user = jsonRESULTS.getString("name");
                                    String email_user = jsonRESULTS.getString("email");
                                    String pict_user = jsonRESULTS.getString("foto_user");
                                    String token_user = jsonRESULTS.getString("token");
                                    String no_hp_user = jsonRESULTS.getString("no_hp");
                                    String alamat_user = jsonRESULTS.getString("alamat");
                                    String hak_akses = jsonRESULTS.getString("hak_akses");
                                    String fcmToken = FirebaseInstanceId.getInstance().getToken();
                                    Log.e("TOKEN", newToken);

                                    sharedPrefManager.saveSPString(sharedPrefManager.SP_TOKEN, token_user);
                                    sharedPrefManager.saveSPString(sharedPrefManager.SP_NAMA, nama_user);
                                    sharedPrefManager.saveSPString(sharedPrefManager.SP_TELEPON, no_hp_user);
                                    sharedPrefManager.saveSPString(sharedPrefManager.SP_ALAMAT, alamat_user);
                                    sharedPrefManager.saveSPString(sharedPrefManager.SP_EMAIL, email_user);
                                    sharedPrefManager.saveSPString(sharedPrefManager.SP_ID,id_user);
                                    sharedPrefManager.saveSPString(sharedPrefManager.SP_PICT,pict_user);
                                    sharedPrefManager.saveSPString(sharedPrefManager.SP_AKSES,hak_akses);
                                    sharedPrefManager.saveSPString(sharedPrefManager.FCM_TOKEN, newToken);
                                    sharedPrefManager.saveSPBoolean(Preferences.SP_SUDAH_LOGIN, true);
                                    akses = Integer.parseInt(sharedPrefManager.getSPAkses());
                                    Toast.makeText(mContext, "login berhasil", Toast.LENGTH_SHORT).show();

                                    loginSuccess(newToken, id_user);
                                    if (akses == 1){
                                        Intent intent = new Intent(mContext, MainPeminjamActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }if (akses == 2){
                                        Intent intent = new Intent(mContext, MainLaboranActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
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
                            Toast.makeText(mContext, "GAGAL LOGIN\n Periksa Email dan Password Anda", Toast.LENGTH_SHORT).show();
                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t){
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                        loading.dismiss();
                    }
                });
    }

    public void loginSuccess(String fcm_token, String user_id){
        mApiService.insertToken(fcm_token,user_id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    Log.e("DEBUG", "Berhasil Insert");
//                    Toast.makeText(mContext, "berhasil "+newToken, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
//                Toast.makeText(mContext, "gagal "+newToken, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onLoginClick(View View) {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}