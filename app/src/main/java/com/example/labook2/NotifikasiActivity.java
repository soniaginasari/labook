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

import com.example.labook2.adapter.NotifikasiAdapter;
import com.example.labook2.adapter.SewaHistoryAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.NotifikasiItem;
import com.example.labook2.model.ResponseNotifikasi;
import com.example.labook2.model.ResponseSewa;
import com.example.labook2.model.SewaItem;

import java.util.ArrayList;
import java.util.List;

public class NotifikasiActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<NotifikasiItem> notifList = new ArrayList<>();
    NotifikasiAdapter notifikasiAdapter;
    BaseApiService mApiService;
    Context mContext;
    String id_user, ket_pengerjaan;
    Preferences sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

//        View myView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = findViewById(R.id.recyclerView);
        sharedPrefManager = new Preferences(NotifikasiActivity.this.getApplicationContext());
        id_user = sharedPrefManager.getSPId();
        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        notifikasiAdapter = new NotifikasiAdapter(mContext, notifList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        getResult();

    }

    private void getResult() {
        mApiService.getNotifikasi(id_user).enqueue(new Callback<ResponseNotifikasi>() {
            @Override
            public void onResponse(Call<ResponseNotifikasi> call, Response<ResponseNotifikasi> response) {
                if (response.isSuccessful()){

                    final List<NotifikasiItem> notifikasi = response.body().getNotifikasi();

                    recyclerView.setAdapter(new NotifikasiAdapter(mContext, notifikasi));
                    notifikasiAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseNotifikasi> call, Throwable t) {
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

}