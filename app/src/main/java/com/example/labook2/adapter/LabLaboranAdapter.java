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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.BidangLaboratoriumActivity;
import com.example.labook2.CrudLaboratoriumActivity;
import com.example.labook2.DetailLabLaboranActivity;
import com.example.labook2.MainLaboranActivity;
import com.example.labook2.Preferences;
import com.example.labook2.R;
import com.example.labook2.TambahLaboratoriumActivity;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.model.LaboranItem;
import com.example.labook2.model.LaboratoriumItem;
import com.squareup.picasso.Picasso;

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

public class LabLaboranAdapter extends RecyclerView.Adapter<LabLaboranAdapter.MyViewHolder> {

    List<LaboranItem> my_list;
    List<LaboranItem> my_listfull;
    Context context;
    BaseApiService mApiService;
    Preferences sharedPrefManager;
    String nama_lab, alamat, no_telp, id_laboratorium, foto_lab;
    int ket_crud;

    public LabLaboranAdapter(Context context, List<LaboranItem> my_list) {
        this.my_list = my_list;
        my_listfull = new ArrayList<>(my_list);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_lab_laboran, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LaboranItem model = my_list.get(position);
        Picasso.get().load(model.getFoto_lab()).into(holder.mImageViewLab);
        holder.namak.setText(model.getNama_lab());
        holder.alamatk.setText(model.getAlamat_lab());
        holder.no_telpk.setText(model.getNo_telp_lab());
        holder.mLinearLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailLabLaboranActivity.class);
                intent.putExtra("foto_lab", model.getFoto_lab());
                intent.putExtra("nama_lab", model.getNama_lab());
                intent.putExtra("alamat", model.getAlamat_lab());
                intent.putExtra("no_telp", model.getNo_telp_lab());
                intent.putExtra("id_laboratorium", model.getId_laboratorium());
                intent.putExtra("id_laboran", model.getId_laboran());
                context.startActivity(intent);
            }
        });

        holder.mRlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TambahLaboratoriumActivity.class);
                intent.putExtra("foto_lab", model.getFoto_lab());
                intent.putExtra("nama_lab", model.getNama_lab());
                intent.putExtra("alamat", model.getAlamat_lab());
                intent.putExtra("no_telp", model.getNo_telp_lab());
                intent.putExtra("id_laboratorium", model.getId_laboratorium());
                intent.putExtra("id_laboran", model.getId_laboran());
                context.startActivity(intent);
            }
        });

        holder.mRlHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_laboratorium = model.getId_laboratorium();
                nama_lab = model.getNama_lab();
                alamat = model.getAlamat();
                no_telp = model.getNo_telp_lab();
                foto_lab = model.getFoto_lab();
                ket_crud = 3;
                showAlertDialog();
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView namak, alamatk, no_telpk;
        public LinearLayout mLinearLab;
        public ImageView mImageViewLab;
        public RelativeLayout mRlEdit, mRlHapus;

        public MyViewHolder(View itemView) {
            super(itemView);

            mImageViewLab = itemView.findViewById(R.id.foto_lab);
            namak = itemView.findViewById(R.id.nama_lab);
            alamatk = itemView.findViewById(R.id.alamat);
            no_telpk = itemView.findViewById(R.id.no_telp);
            mLinearLab = itemView.findViewById(R.id.linearLayout);
            mRlEdit = itemView.findViewById(R.id.rl_edit);
            mRlHapus = itemView.findViewById(R.id.rl_hapus);
        }
    }

    private void showAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(context, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert));

        // set title dialog
        alertDialogBuilder.setTitle("Menghapus Laboratorium");

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
        mApiService.HapusLaboratorium(id_laboratorium)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(context, "BERHASIL HAPUS LABORATORIUM", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(context, MainLaboranActivity.class);
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
                            Toast.makeText(context, "GAGAL HAPUS LABORATORIUM", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}