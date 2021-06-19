package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.example.labook2.adapter.LayananAdapter;
import com.example.labook2.adapter.SewaAdapter;
import com.example.labook2.adapter.SewaHistoryAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.LayananItem;
import com.example.labook2.model.ResponseLayanan;
import com.example.labook2.model.ResponseSewa;
import com.example.labook2.model.SewaItem;

import java.util.ArrayList;
import java.util.List;

public class SewaLabActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<SewaItem> sewaList = new ArrayList<>();
    SewaHistoryAdapter sewaHistoryAdapter;
    BaseApiService mApiService;
    Context mContext;
    String id_peminjam, ket_pengerjaan;
    Preferences sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sewa_lab);

//        View myView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = findViewById(R.id.recyclerView);
        sharedPrefManager = new Preferences(SewaLabActivity.this.getApplicationContext());
        id_peminjam = sharedPrefManager.getSPId();
        ket_pengerjaan = String.valueOf('1');
        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        sewaHistoryAdapter = new SewaHistoryAdapter(mContext, sewaList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        getResult();

    }

    private void getResult() {
        mApiService.getSewaHistory(id_peminjam, ket_pengerjaan).enqueue(new Callback<ResponseSewa>() {
            @Override
            public void onResponse(Call<ResponseSewa> call, Response<ResponseSewa> response) {
                if (response.isSuccessful()){

                    final List<SewaItem> sewa = response.body().getSewaHistory();

                    recyclerView.setAdapter(new SewaHistoryAdapter(mContext, sewa));
                    sewaHistoryAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseSewa> call, Throwable t) {
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }
}