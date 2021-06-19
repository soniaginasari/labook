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

import com.example.labook2.adapter.LaboranAdapter;
import com.example.labook2.adapter.LaboranLabAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.LaboranItem;
import com.example.labook2.model.ResponseLaboran;

import java.util.ArrayList;
import java.util.List;

public class LaboranLabActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<LaboranItem> laboranList = new ArrayList<>();
    LaboranLabAdapter laboranLabAdapter;
    BaseApiService mApiService;
    Context mContext;
    Preferences sharedPrefManager;
    String id_user, id_laboratorium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laboran_lab);

        sharedPrefManager = new Preferences(LaboranLabActivity.this.getApplicationContext());
        id_user= sharedPrefManager.getSPId();
        id_laboratorium= getIntent().getStringExtra("id_laboratorium");

        recyclerView = findViewById(R.id.recyclerView);
        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());
        laboranLabAdapter = new LaboranLabAdapter(mContext, laboranList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        getLaboran();
    }

    private void getLaboran() {
        mApiService.getLaboran(id_laboratorium).enqueue(new Callback<ResponseLaboran>() {
            @Override
            public void onResponse(Call<ResponseLaboran> call, Response<ResponseLaboran> response) {
                if (response.isSuccessful()){

                    final List<LaboranItem> laboran = response.body().getLaboran();

                    recyclerView.setAdapter(new LaboranLabAdapter(mContext, laboran));
                    laboranLabAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLaboran> call, Throwable t) {
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_tambah,menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.tambah:
//                Intent intent = new Intent(LaboranLabActivity.this, TambahLaboranActivity.class);
//                intent.putExtra("id_laboratorium", id_laboratorium);
//                startActivity(intent);
//
//                return true;
//
//        }
//        return false;
//    }
}