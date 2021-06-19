package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.adapter.BidangAdapter;
import com.example.labook2.adapter.LayananAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.BidangItem;
import com.example.labook2.model.LayananItem;
import com.example.labook2.model.ResponseBidang;
import com.example.labook2.model.ResponseLayanan;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LayananLaboratoriumActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<LayananItem> layananList = new ArrayList<>();
    LayananAdapter layananAdapter;
    BaseApiService mApiService;
    Context mContext;
    String id_bidang, nama_lab, alamat, no_telp, foto_lab, nama_bidang;
    TextView tvnama_lab, tvalamat, tvno_telp, tvnama_bidang;
    ImageView ivfoto_lab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layanan_laboratorium);

//        View myView = inflater.inflate(R.layout.fragment_home, container, false);
        id_bidang = getIntent().getStringExtra("id_bidang");
        nama_bidang = getIntent().getStringExtra("nama_bidang");
        nama_lab = getIntent().getStringExtra("nama_lab");
        alamat = getIntent().getStringExtra("alamat");
        no_telp = getIntent().getStringExtra("no_telp");
        foto_lab = getIntent().getStringExtra("foto_lab");
        recyclerView = findViewById(R.id.recyclerView);

        ivfoto_lab= (ImageView) findViewById(R.id.ivLab);
        tvnama_lab = (TextView) findViewById(R.id.nama_lab);
        tvnama_bidang = (TextView) findViewById(R.id.nama_bidang);

        tvnama_lab.setText(nama_lab);
        tvnama_bidang.setText(nama_bidang);
        Picasso.get().load(foto_lab).into(ivfoto_lab);

        mContext = this;

        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        layananAdapter = new LayananAdapter(mContext, layananList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        getResult();

    }

    private void getResult() {
        mApiService.getLayanan(id_bidang).enqueue(new Callback<ResponseLayanan>() {
            @Override
            public void onResponse(Call<ResponseLayanan> call, Response<ResponseLayanan> response) {
                if (response.isSuccessful()){

                    final List<LayananItem> layanan = response.body().getLayanan();

                    recyclerView.setAdapter(new LayananAdapter(mContext, layanan));
                    layananAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLayanan> call, Throwable t) {
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
