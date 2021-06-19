package com.example.labook2;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.labook2.adapter.SewaHistoryLaboranAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.ResponseSewa;
import com.example.labook2.model.SewaItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PengerjaanLaboranFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PengerjaanLaboranFragment extends Fragment {

    private RecyclerView recyclerView;
    List<SewaItem> sewaList = new ArrayList<>();
    SewaHistoryLaboranAdapter sewaHistoryLaboranAdapter;
    BaseApiService mApiService;
    Context mContext;
    String id_peminjam, ket_pengerjaan;
    Preferences sharedPrefManager;

    public PengerjaanLaboranFragment() {
        // Required empty public constructor
    }


    public static PengerjaanLaboranFragment newInstance() {
        PengerjaanLaboranFragment fragment = new PengerjaanLaboranFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.fragment_pengerjaan_laboran, container, false);
        recyclerView = myView.findViewById(R.id.recyclerView);
        mContext = myView.getContext();
        sharedPrefManager = new Preferences(mContext.getApplicationContext());
        id_peminjam = sharedPrefManager.getSPId();
        ket_pengerjaan = String.valueOf('1');
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        sewaHistoryLaboranAdapter = new SewaHistoryLaboranAdapter(mContext, sewaList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);


        getResult();

        return myView;
    }

    private void getResult() {
        mApiService.getSewaLaboranHistory(id_peminjam,ket_pengerjaan).enqueue(new Callback<ResponseSewa>() {
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