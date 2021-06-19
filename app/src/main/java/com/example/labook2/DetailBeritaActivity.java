package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailBeritaActivity extends AppCompatActivity {

    String id_laboratorium, id_berita, nama_lab, alamat, no_telp, foto_lab, judul, isi, timestamp;
    TextView tvnama_lab, tvalamat, tvno_telp, tvjudul, tvisi, tvtimestamp;
    ImageView ivfoto_lab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_berita);

        tvnama_lab= (TextView) findViewById(R.id.nama_lab);
        tvalamat = (TextView) findViewById(R.id.alamat);
        tvno_telp= (TextView) findViewById(R.id.no_telp);
        tvjudul = (TextView) findViewById(R.id.judul);
        tvisi = (TextView) findViewById(R.id.isi);
        tvtimestamp = (TextView) findViewById(R.id.timestamp);
        ivfoto_lab= (ImageView) findViewById(R.id.foto_lab);

        id_laboratorium= getIntent().getStringExtra("id_laboratorium");
        nama_lab = getIntent().getStringExtra("nama_lab");
        alamat=getIntent().getStringExtra("alamat");
        no_telp=getIntent().getStringExtra("no_telp");
        id_berita=getIntent().getStringExtra("id_berita");
        judul=getIntent().getStringExtra("judul");
        isi=getIntent().getStringExtra("isi");
        timestamp=getIntent().getStringExtra("timestamp");
        foto_lab=getIntent().getStringExtra("foto_lab");

        tvnama_lab.setText(nama_lab);
        tvalamat.setText("Alamat :"+alamat);
        tvno_telp.setText("No Telepon :"+no_telp);
        tvjudul.setText(judul);
        tvisi.setText(isi);
        tvtimestamp.setText(timestamp);
        Picasso.get().load(foto_lab).into(ivfoto_lab);

    }

    public void nama_lab(View view) {
        Intent intent = new Intent(DetailBeritaActivity.this,BidangLaboratoriumActivity.class);
        intent.putExtra("id_laboratorium",id_laboratorium);
        intent.putExtra("nama_lab",nama_lab);
        intent.putExtra("alamat",alamat);
        intent.putExtra("no_telp",no_telp);
        startActivity(intent);
    }
}