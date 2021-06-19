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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.adapter.BidangAdapter;
import com.example.labook2.adapter.BidangLaboranAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.BidangItem;
import com.example.labook2.model.ResponseBidang;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailLabLaboranActivity extends AppCompatActivity {

    String id_laboratorium, nama_lab ,alamat, no_hp, pimgg;
    TextView tv_nama_lab, tv_alamat, tv_no_hp;
    ImageView timg;
    private RecyclerView recyclerView;
    List<BidangItem> bidangList = new ArrayList<>();
    BidangLaboranAdapter bidangLaboranAdapter;
    BaseApiService mApiService;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lab_laboran);

        tv_nama_lab= (TextView) findViewById(R.id.nama_lab);
        tv_alamat = (TextView) findViewById(R.id.alamat);
        tv_no_hp = (TextView) findViewById(R.id.no_telp);
        timg= (ImageView) findViewById(R.id.ivLab);
        recyclerView = findViewById(R.id.recyclerView);

        id_laboratorium= getIntent().getStringExtra("id_laboratorium");
        nama_lab = getIntent().getStringExtra("nama_lab");
        alamat=getIntent().getStringExtra("alamat");
        no_hp=getIntent().getStringExtra("no_telp");
        pimgg=getIntent().getStringExtra("foto_lab");

        tv_nama_lab.setText(nama_lab);
        tv_alamat.setText(alamat);
        tv_no_hp.setText(no_hp);
        Picasso.get().load(pimgg).into(timg);

        mContext = this;

        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        bidangLaboranAdapter = new BidangLaboranAdapter(mContext, bidangList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        getBidang();
    }

    private void getBidang() {
        mApiService.getBidang(id_laboratorium).enqueue(new Callback<ResponseBidang>() {
            @Override
            public void onResponse(Call<ResponseBidang> call, Response<ResponseBidang> response) {
                if (response.isSuccessful()){

                    final List<BidangItem> bidang = response.body().getBidang();

                    recyclerView.setAdapter(new BidangLaboranAdapter(mContext, bidang));
                    bidangLaboranAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBidang> call, Throwable t) {
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void tambahBidang(View view) {
        Intent intent = new Intent(mContext, TambahBidangActivity.class);
        intent.putExtra("id_laboratorium", id_laboratorium);
        intent.putExtra("nama_lab", nama_lab);
        intent.putExtra("alamat", alamat);
        intent.putExtra("no_telp", no_hp);
        mContext.startActivity(intent);
        finish();
    }
}