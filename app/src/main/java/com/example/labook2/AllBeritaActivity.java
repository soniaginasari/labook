package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.labook2.adapter.BeritaAdapter;
import com.example.labook2.adapter.SewaAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.BeritaItem;
import com.example.labook2.model.ResponseBerita;
import com.example.labook2.model.SewaItem;

import java.util.ArrayList;
import java.util.List;

public class AllBeritaActivity extends AppCompatActivity {

    private RecyclerView recyclerView_berita;
    List<BeritaItem> beritaList = new ArrayList<>();
    BeritaAdapter beritaAdapter;
    BaseApiService mApiService;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_berita);

        recyclerView_berita = findViewById(R.id.recyclerView);
        mContext = this;

        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        beritaAdapter = new BeritaAdapter(mContext, beritaList);

        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this);
        recyclerView_berita.setLayoutManager(mLayoutManager1);
        getResultBerita();
    }

    private void getResultBerita() {
        mApiService.getBerita().enqueue(new Callback<ResponseBerita>() {
            @Override
            public void onResponse(Call<ResponseBerita> call, Response<ResponseBerita> response) {
                if (response.isSuccessful()) {

                    final List<BeritaItem> berita = response.body().getBerita();

                    recyclerView_berita.setAdapter(new BeritaAdapter(mContext, berita));
                    beritaAdapter.notifyDataSetChanged();
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
}