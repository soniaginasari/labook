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
import android.widget.SearchView;
import android.widget.Toast;

import com.example.labook2.adapter.LaboratoriumAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.LaboratoriumItem;
import com.example.labook2.model.ResponseLaboratorium;

import java.util.ArrayList;
import java.util.List;

public class CariLaboratoriumActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<LaboratoriumItem> laboratoriumList = new ArrayList<>();
    LaboratoriumAdapter laboratoriumAdapter;
    BaseApiService mApiService;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_laboratorium);

        //        View myView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = findViewById(R.id.recyclerView);

        mContext = this;
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        laboratoriumAdapter = new LaboratoriumAdapter(mContext, laboratoriumList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);


        getResult();

    }

    private void getResult() {
        mApiService.getLaboratorium().enqueue(new Callback<ResponseLaboratorium>() {
            @Override
            public void onResponse(Call<ResponseLaboratorium> call, Response<ResponseLaboratorium> response) {
                if (response.isSuccessful()){

                    final List<LaboratoriumItem> laboratorium = response.body().getLaboratorium();

                    recyclerView.setAdapter(new LaboratoriumAdapter(mContext, laboratorium));
                    laboratoriumAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLaboratorium> call, Throwable t) {
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_cari_lab, menu);
//
//        MenuItem searchItem = menu.findItem(R.id.search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                laboratoriumAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return true;
//    }
}
