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
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.BidangItem;
import com.example.labook2.model.ResponseBidang;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class BidangLaboratoriumActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<BidangItem> bidangList = new ArrayList<>();
    BidangAdapter bidangAdapter;
    BaseApiService mApiService;
    Context mContext;
    String id_laboratorium, nama_lab, alamat, no_telp, foto_lab;
    TextView tvnama_lab, tvalamat, tvno_telp;
    ImageView ivfoto_lab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bidang_laboratorium);

//        View myView = inflater.inflate(R.layout.fragment_home, container, false);
        id_laboratorium = getIntent().getStringExtra("id_laboratorium");
        nama_lab = getIntent().getStringExtra("nama_lab");
        alamat = getIntent().getStringExtra("alamat");
        no_telp = getIntent().getStringExtra("no_telp");
        foto_lab = getIntent().getStringExtra("foto_lab");
        recyclerView = findViewById(R.id.recyclerView);

        ivfoto_lab= (ImageView) findViewById(R.id.ivLab);
        tvnama_lab = (TextView) findViewById(R.id.nama_lab);
        tvalamat = (TextView) findViewById(R.id.alamat);
        tvno_telp = (TextView) findViewById(R.id.no_telp);

        tvnama_lab.setText(nama_lab);
        tvalamat.setText(alamat);
        tvno_telp.setText(no_telp);
        Picasso.get().load(foto_lab).into(ivfoto_lab);
        mContext = this;

        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        bidangAdapter = new BidangAdapter(mContext, bidangList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        getResult();

    }

    private void getResult() {
        mApiService.getBidang(id_laboratorium).enqueue(new Callback<ResponseBidang>() {
            @Override
            public void onResponse(Call<ResponseBidang> call, Response<ResponseBidang> response) {
                if (response.isSuccessful()){

                    final List<BidangItem> bidang = response.body().getBidang();

                    recyclerView.setAdapter(new BidangAdapter(mContext, bidang));
                    bidangAdapter.notifyDataSetChanged();
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

}
