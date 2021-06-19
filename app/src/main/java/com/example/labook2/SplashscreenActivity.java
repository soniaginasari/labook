package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashscreenActivity extends AppCompatActivity {

    Preferences sharedPrefManager;
    int akses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        sharedPrefManager = new Preferences(SplashscreenActivity.this.getApplicationContext());
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sharedPrefManager.getSPSudahLogin()){
                    akses = Integer.parseInt(sharedPrefManager.getSPAkses());
                    if (akses == 1){
                        startActivity(new Intent(SplashscreenActivity.this, MainPeminjamActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }if (akses == 2){
                        startActivity(new Intent(SplashscreenActivity.this, MainLaboranActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                }else {
                    Intent intent = new Intent(SplashscreenActivity.this, MainPeminjamActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000L);
    }


}