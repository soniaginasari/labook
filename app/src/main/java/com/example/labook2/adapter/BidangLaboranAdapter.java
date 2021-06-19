package com.example.labook2.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.CrudLaboratoriumActivity;
import com.example.labook2.DetailBidangLaboranActivity;
import com.example.labook2.DetailLabLaboranActivity;
import com.example.labook2.LayananLaboratoriumActivity;
import com.example.labook2.MainLaboranActivity;
import com.example.labook2.Preferences;
import com.example.labook2.R;
import com.example.labook2.TambahBidangActivity;
import com.example.labook2.TambahLaboratoriumActivity;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.BidangItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BidangLaboranAdapter extends RecyclerView.Adapter<BidangLaboranAdapter.MyViewHolder> {

    List<BidangItem> my_list;
    List<BidangItem> my_listfull;
    Context context;
    BaseApiService mApiService;
    Preferences sharedPrefManager;
    String id_bidang, nama_bidang, id_laboratorium;
    int ket_crud;

    public BidangLaboranAdapter(Context context, List<BidangItem> my_list) {
        this.my_list = my_list;
        my_listfull = new ArrayList<>(my_list);
        this.context = context;
        mApiService = RetrofitClient.getService(context.getApplicationContext());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_bidang_laboran, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final BidangItem model = my_list.get(position);

        holder.namak.setText(model.getNama_bidang());
        holder.mLinearBidang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailBidangLaboranActivity.class);
                intent.putExtra("nama_bidang", model.getNama_bidang());
                intent.putExtra("id_bidang", model.getId_bidang());
                intent.putExtra("id_laboratorium", model.getId_laboratorium());
                intent.putExtra("foto_lab", model.getFoto_lab());
                intent.putExtra("nama_lab", model.getNama_lab());
                intent.putExtra("alamat", model.getAlamat());
                intent.putExtra("no_telp", model.getNo_telp());
                context.startActivity(intent);
            }
        });

        holder.mRlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TambahBidangActivity.class);
                intent.putExtra("id_laboratorium", model.getId_laboratorium());
                intent.putExtra("nama_lab", model.getNama_lab());
                intent.putExtra("id_bidang", model.getId_bidang());
                intent.putExtra("nama_bidang", model.getNama_bidang());
                context.startActivity(intent);
            }
        });

        holder.mRlHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_bidang = model.getId_bidang();
                id_laboratorium = model.getId_laboratorium();
                nama_bidang = model.getNama_bidang();

                showAlertDialog();
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView namak;
        public LinearLayout mLinearBidang;
        public RelativeLayout mRlEdit, mRlHapus;


        public MyViewHolder(View itemView) {
            super(itemView);


            namak = itemView.findViewById(R.id.nama_bidang);
            mLinearBidang = itemView.findViewById(R.id.linearLayout);
            mRlEdit = itemView.findViewById(R.id.rl_edit);
            mRlHapus = itemView.findViewById(R.id.rl_hapus);
        }
    }

    private void showAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(context, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert));


        // set title dialog
        alertDialogBuilder.setTitle("Menghapus Bidang");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Apakah anda yakin ?")
                .setIcon(R.mipmap.logo_labook)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini

                        ket_crud = 3;
                        requestVerifBidang();
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

    private void requestVerifBidang() {
        mApiService.SetVerifBidang(id_laboratorium,id_bidang,nama_bidang,String.valueOf(ket_crud))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(context, "DATA BERHASIL DIINPUT \n MENUNGGU VERIFIKASI KEPALA LAB", Toast.LENGTH_SHORT).show();

                                } else {
                                    String error_message = jsonRESULTS.getString("error_msg");
                                    Toast.makeText(context, error_message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(context, " DATA BIDANG GAGAL DIINPUT", Toast.LENGTH_SHORT).show();
//                            loading.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
//                        loading.dismiss();
                    }
                });
    }
}
