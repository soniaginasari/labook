package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.adapter.SewaHistoryLaboranAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.ResponseSewa;
import com.example.labook2.model.SewaItem;

import java.util.ArrayList;
import java.util.List;

public class CalendarSewaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<SewaItem> sewaList = new ArrayList<>();
    SewaHistoryLaboranAdapter sewaHistoryLaboranAdapter;
    BaseApiService mApiService;
    Context mContext;
    String id_laboran, tgl_pinjam;
    Preferences sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_sewa);
        recyclerView = findViewById(R.id.recyclerView);
        sharedPrefManager = new Preferences(CalendarSewaActivity.this.getApplicationContext());
        id_laboran = sharedPrefManager.getSPId();
        tgl_pinjam = getIntent().getStringExtra("tgl_pinjam");
        mContext = this;

        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        sewaHistoryLaboranAdapter = new SewaHistoryLaboranAdapter(mContext, sewaList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        getHistory();
    }

    private void getHistory() {
        mApiService.getCalDetSewa(id_laboran, tgl_pinjam).enqueue(new Callback<ResponseSewa>() {
            @Override
            public void onResponse(Call<ResponseSewa> call, Response<ResponseSewa> response) {
                if (response.isSuccessful()){

                    final List<SewaItem> sewa = response.body().getSewaHistory();

                    recyclerView.setAdapter(new SewaHistoryLaboranAdapter(mContext, sewa));
                    sewaHistoryLaboranAdapter.notifyDataSetChanged();
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