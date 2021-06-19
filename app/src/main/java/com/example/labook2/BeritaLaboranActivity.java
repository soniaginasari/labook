package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.labook2.adapter.BeritaAdapter;
import com.example.labook2.adapter.BeritaLaboranAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.BeritaItem;
import com.example.labook2.model.ResponseBerita;

import java.util.ArrayList;
import java.util.List;

public class BeritaLaboranActivity extends AppCompatActivity {

    private RecyclerView recyclerView_berita;
    List<BeritaItem> beritaList = new ArrayList<>();
    BeritaLaboranAdapter beritaLaboranAdapter;
    BaseApiService mApiService;
    Context mContext;
    Preferences sharedPrefManager;
    String id_user, id_laboratorium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita_laboran);

        sharedPrefManager = new Preferences(BeritaLaboranActivity.this.getApplicationContext());
        id_user= sharedPrefManager.getSPId();
        id_laboratorium= getIntent().getStringExtra("id_laboratorium");
        recyclerView_berita = findViewById(R.id.recyclerView);
        mContext = this;

        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        beritaLaboranAdapter = new BeritaLaboranAdapter(mContext, beritaList);

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        recyclerView_berita.setLayoutManager(mLayoutManager1);
        getResultBerita();
    }

    private void getResultBerita() {
        mApiService.getBeritaLaboran(id_laboratorium).enqueue(new Callback<ResponseBerita>() {
            @Override
            public void onResponse(Call<ResponseBerita> call, Response<ResponseBerita> response) {
                if (response.isSuccessful()) {

                    final List<BeritaItem> berita = response.body().getBerita();

                    recyclerView_berita.setAdapter(new BeritaLaboranAdapter(mContext, berita));
                    beritaLaboranAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBerita> call, Throwable t) {
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_tambah,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.tambah:
                Intent intent = new Intent(BeritaLaboranActivity.this, TambahBeritaActivity.class);
                intent.putExtra("id_laboratorium", id_laboratorium);
                startActivity(intent);

                return true;

        }
        return false;
    }
}