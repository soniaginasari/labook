package com.example.labook2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.adapter.BidangAdapter;
import com.example.labook2.adapter.LabLaboranAdapter;
import com.example.labook2.adapter.LaboratoriumAdapter;
import com.example.labook2.adapter.VerifLaboranAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.BidangItem;
import com.example.labook2.model.LaboranItem;
import com.example.labook2.model.LaboratoriumItem;
import com.example.labook2.model.ResponseBidang;
import com.example.labook2.model.ResponseLaboran;
import com.example.labook2.model.ResponseLaboratorium;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CrudLaboratoriumActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<LaboranItem> labLaboranList = new ArrayList<>();
    LabLaboranAdapter labLaboranAdapter;
    BaseApiService mApiService;
    Context mContext;
    Preferences sharedPrefManager;
    String id_laboran, id_laboratorium;
    TextView tv_txt;
    RelativeLayout rl_verifikasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud_laboratorium);
        sharedPrefManager = new Preferences(CrudLaboratoriumActivity.this.getApplicationContext());
        id_laboran = sharedPrefManager.getSPId();
        //        View myView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = findViewById(R.id.recyclerView);
        rl_verifikasi = (RelativeLayout) findViewById(R.id.rl_verifikasi);

        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        labLaboranAdapter = new LabLaboranAdapter(mContext, labLaboranList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        getLabLaboran();
        getId_laboratorium();
        tv_txt = (TextView) findViewById(R.id.txt);
        tv_txt.setText(id_laboratorium);
        rl_verifikasi.setVisibility(View.GONE);
    }

    private void getLabLaboran() {
        mApiService.getLabLaboran(id_laboran).enqueue(new Callback<ResponseLaboran>() {
            @Override
            public void onResponse(Call<ResponseLaboran> call, Response<ResponseLaboran> response) {
                if (response.isSuccessful()){

                    final List<LaboranItem> labLaboran = response.body().getLaboran();

                    recyclerView.setAdapter(new LabLaboranAdapter(mContext, labLaboran));
                    labLaboranAdapter.notifyDataSetChanged();

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

    public void getId_laboratorium(){
        mApiService.getId_laboratorium(id_laboran)
                .enqueue(new Callback<ResponseBody>(){
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    id_laboratorium = jsonRESULTS.getString("id_laboratorium");
                                    Toast.makeText(mContext, "Data didapatkan "+id_laboratorium+" sukses", Toast.LENGTH_SHORT).show();
                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(mContext, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mContext, "Data tidak didapatkan", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t){
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_tambah_lab,menu);
//        return true;
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item){
//        switch (item.getItemId()){
//            case R.id.tambah_lab:
//                Intent intent = new Intent(CrudLaboratoriumActivity.this, TambahLaboratoriumActivity.class);
//                startActivity(intent);
//
//                return true;
//
//        }
//        return false;
//    }

    public void laboran(View view) {
        Intent intent = new Intent(CrudLaboratoriumActivity.this, LaboranLabActivity.class);
        intent.putExtra("id_laboratorium", id_laboratorium);
        startActivity(intent);
    }

    public void berita(View view) {
        Intent intent = new Intent(CrudLaboratoriumActivity.this, BeritaLaboranActivity.class);
        intent.putExtra("id_laboratorium", id_laboratorium);
        startActivity(intent);
    }

    public void verif(View view) {
        Intent intent = new Intent(CrudLaboratoriumActivity.this, ListVerifLaboranActivity.class);
        intent.putExtra("id_laboratorium", id_laboratorium);
        startActivity(intent);
    }
}