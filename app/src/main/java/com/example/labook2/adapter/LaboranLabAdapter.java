package com.example.labook2.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.CrudLaboratoriumActivity;
import com.example.labook2.EditLaboranActivity;
import com.example.labook2.Preferences;
import com.example.labook2.R;
import com.example.labook2.TambahBidangActivity;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.LaboranItem;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaboranLabAdapter extends RecyclerView.Adapter<LaboranLabAdapter.MyViewHolder> {

    List<LaboranItem> my_list;
    List<LaboranItem> my_listfull;
    Context context;
    BaseApiService mApiService;
    Preferences sharedPrefManager;
    String id_laboran;

    public LaboranLabAdapter(Context context, List<LaboranItem> my_list) {
        this.my_list = my_list;
        my_listfull = new ArrayList<>(my_list);
        this.context = context;
        mApiService = RetrofitClient.getService(context.getApplicationContext());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_laboran_lab,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LaboranItem model=my_list.get(position);
        holder.mnama_laboran.setText(model.getNama_laboran());
        holder.mno_hp.setText(model.getNo_hp());
        int akses = Integer.parseInt(model.getHak_akses());
        String hak_akses = null;
        if(akses == 1) {
            hak_akses="Teknisi/Laboran";
            holder.mhak_akses.setText(hak_akses);
        }if(akses == 2){
            hak_akses="Kepala Lab";
            holder.mhak_akses.setText(hak_akses);
        }
        String foto = model.getFoto_user();
        if (foto == null){
            holder.ivLaboran.setImageResource(R.drawable.ic_baseline_person_24);
        }if (foto != null) {
            Picasso.get().load(model.getFoto_user()).into(holder.ivLaboran);
        }
        holder.mRlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditLaboranActivity.class);
                intent.putExtra("id_laboran", model.getId_laboran());
                intent.putExtra("nama_laboran", model.getNama_laboran());
                intent.putExtra("hak_akses", model.getHak_akses());
                context.startActivity(intent);
            }
        });

        holder.mRlHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_laboran = model.getId_laboran();
                showAlertDialog();
            }
        });

        holder.mRlEdit.setVisibility(View.GONE);
        holder.mRlHapus.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mnama_laboran, mno_hp, mhak_akses;
        public CardView mCardViewLaboran;
        public ImageView ivLaboran;
        public RelativeLayout mRlEdit, mRlHapus;

        public MyViewHolder(View itemView) {
            super(itemView);

            mnama_laboran = itemView.findViewById(R.id.nama_laboran);
            mno_hp = itemView.findViewById(R.id.no_hp);
            mhak_akses = itemView.findViewById(R.id.hak_akses);
            mCardViewLaboran =itemView.findViewById(R.id.cvLaboran);
            ivLaboran = itemView.findViewById(R.id.ivLab);
            mRlEdit = itemView.findViewById(R.id.rl_edit);
            mRlHapus = itemView.findViewById(R.id.rl_hapus);

        }
    }

    private void showAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(context, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert));


        // set title dialog
        alertDialogBuilder.setTitle("Menghapus Laboran");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Apakah anda yakin ?")
                .setIcon(R.mipmap.logo_labook)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        requestHapus();
//                        DetailSewaLabActivity.this.finish();
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

    private void requestHapus() {
        mApiService.HapusLaboran(id_laboran)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(context, "BERHASIL HAPUS LABORAN", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(context, CrudLaboratoriumActivity.class);
//                                    intent.putExtra("id_bidang", id_bidang);
//                                    intent.putExtra("nama_lab", nama_lab);
//                                    intent.putExtra("alamat", alamat);
//                                    intent.putExtra("no_telp", no_telp);
//                                    intent.putExtra("id_laboratorium", id_laboratorium);
                                    context.startActivity(intent);
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
                            Toast.makeText(context, "GAGAL HAPUS lABORAN", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}
