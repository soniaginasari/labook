package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NonLoginActivity extends AppCompatActivity {

    TextView txt;
    Preferences sharedPrefManager;
    int akses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_login);

//        txt = (TextView) findViewById(R.id.txt_no_akun);
        sharedPrefManager = new Preferences(NonLoginActivity.this.getApplicationContext());

    }

    public void login(View view) {
        Intent intent = new Intent(NonLoginActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    public void daftar(View view) {
        Intent intent = new Intent(NonLoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
}