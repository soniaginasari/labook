package com.example.labook2;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.labook2.adapter.SewaHistoryAdapter;
import com.example.labook2.adapter.VerifLaboranAdapter;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.ResponseSewa;
import com.example.labook2.model.ResponseVerifLaboran;
import com.example.labook2.model.SewaItem;
import com.example.labook2.model.VerifLaboranItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerifLabFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifLabFragment extends Fragment {
    private RecyclerView recyclerView;
    List<VerifLaboranItem> verifList = new ArrayList<>();
    VerifLaboranAdapter verifLaboranAdapter;
    BaseApiService mApiService;
    Context mContext;
    String id_laboran, id_laboratorium;
    Preferences sharedPrefManager;

    public VerifLabFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static VerifLabFragment newInstance() {
        VerifLabFragment fragment = new VerifLabFragment();
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
        View myView = inflater.inflate(R.layout.fragment_verif_lab, container, false);
        recyclerView = myView.findViewById(R.id.recyclerView);
        mContext = myView.getContext();
        sharedPrefManager = new Preferences(mContext.getApplicationContext());
        id_laboran = sharedPrefManager.getSPId();

        mApiService = RetrofitClient.getService(mContext.getApplicationContext());

        verifLaboranAdapter = new VerifLaboranAdapter(mContext, verifList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);

        getId_laboratorium();


        return myView;
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
                                    getResult();
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

    private void getResult() {
        Toast.makeText(mContext, "halo", Toast.LENGTH_SHORT).show();
        mApiService.getVerifLab(id_laboratorium).enqueue(new Callback<ResponseVerifLaboran>() {
            @Override
            public void onResponse(Call<ResponseVerifLaboran> call, Response<ResponseVerifLaboran> response) {
                if (response.isSuccessful()){

                    final List<VerifLaboranItem> verif_laboran = response.body().getVerif_laboran();

                    recyclerView.setAdapter(new VerifLaboranAdapter(mContext, verif_laboran));
                    verifLaboranAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Gagal mengambil data", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseVerifLaboran> call, Throwable t) {
                Toast.makeText(mContext, "Koneksi Internet Bermasalah", Toast.LENGTH_SHORT).show();
            }
        });
    }
}