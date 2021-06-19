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

import com.example.labook2.adapter.SewaHistoryAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.ResponseSewa;
import com.example.labook2.model.SewaItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SetujuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SetujuFragment extends Fragment {

    private RecyclerView recyclerView;
    List<SewaItem> sewaList = new ArrayList<>();
    SewaHistoryAdapter sewaHistoryAdapter;
    BaseApiService mApiService;
    Context mContext;
    String id_peminjam, keterangan;
    Preferences sharedPrefManager;

    public SetujuFragment() {
        // Required empty public constructor
    }

    public static SetujuFragment newInstance() {

        Bundle args = new Bundle();
        SetujuFragment fragment = new SetujuFragment();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_konfirmasi, container, false);
        recyclerView = myView.findViewById(R.id.recyclerView);
        mContext = myView.getContext();
        sharedPrefManager = new Preferences(mContext.getApplicationContext());
        id_peminjam = sharedPrefManager.getSPId();
        keterangan = String.valueOf('2');
        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        sewaHistoryAdapter = new SewaHistoryAdapter(mContext, sewaList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);


        getResult();

        return myView;
    }

    private void getResult() {
        mApiService.getSewa(id_peminjam,keterangan).enqueue(new Callback<ResponseSewa>() {
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