package com.example.labook2;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class ProfileActivity extends AppCompatActivity {

    ImageView foto_profil;
    Preferences sharedPrefManager;
    String nama, alamat, no_hp, hak_akses, email, akses1, foto_user,id ;
    int akses;
    TextView tvprofile_nama, tvprofile_akses, tvnama, tvalamat, tvno_hp, tvemail, tvhak_akses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPrefManager = new Preferences(ProfileActivity.this.getApplicationContext());
        tvprofile_nama = (TextView) findViewById(R.id.profileNama);
        tvprofile_akses = (TextView) findViewById(R.id.profileHakAkses);
        tvnama = (TextView) findViewById(R.id.nama);
        tvalamat = (TextView) findViewById(R.id.alamat);
        tvno_hp = (TextView) findViewById(R.id.no_hp);
        tvhak_akses = (TextView) findViewById(R.id.hak_akses);
        tvemail = (TextView) findViewById(R.id.email);
        foto_profil = (ImageView) findViewById(R.id.profileImageView);

        id = sharedPrefManager.getSPId();
        nama = sharedPrefManager.getSPNama();
        email = sharedPrefManager.getSPEmail();
        alamat = sharedPrefManager.getSPAlamat();
        no_hp = sharedPrefManager.getSPTelepon();
        foto_user = sharedPrefManager.getSPPict();
        hak_akses = sharedPrefManager.getSPAkses();
        akses = Integer.parseInt(hak_akses);
        if (akses == 1){
            akses1 = "Peminjam";
        } if (akses == 2){
            akses1 = "Laboran";
        } if (akses == 3) {
            akses1 = "Admin";
        }

        tvprofile_nama.setText("Hello "+nama+" !");
        tvprofile_akses.setText(akses1);
        tvnama.setText(nama);
        tvalamat.setText(alamat);
        tvemail.setText(email);
        tvno_hp.setText(no_hp);
        tvhak_akses.setText(akses1);
        Picasso.get().load(foto_user).into(foto_profil);
    }

    public void EditProfile(View view) {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
        finish();
    }
}
