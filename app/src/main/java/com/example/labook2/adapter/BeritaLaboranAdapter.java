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

import com.example.labook2.CrudLaboratoriumActivity;
import com.example.labook2.DetailBeritaActivity;
import com.example.labook2.Preferences;
import com.example.labook2.R;
import com.example.labook2.TambahBeritaActivity;
import com.example.labook2.TambahBidangActivity;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.BeritaItem;
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

public class BeritaLaboranAdapter extends RecyclerView.Adapter<BeritaLaboranAdapter.MyViewHolder> {

    List<BeritaItem> my_list;
    List<BeritaItem> my_listfull;
    Context context;
    BaseApiService mApiService;
    Preferences sharedPrefManager;
    String id_berita, judul, isi, id_laboratorium;

    public BeritaLaboranAdapter(Context context, List<BeritaItem> my_list) {
        this.my_list = my_list;
        my_listfull = new ArrayList<>(my_list);
        this.context = context;
        mApiService = RetrofitClient.getService(context.getApplicationContext());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_berita_laboran, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final BeritaItem model = my_list.get(position);
        Picasso.get().load(model.getFoto_lab()).into(holder.mImageViewLab);
        holder.mNamaLab.setText(model.getNama_lab());
        holder.mJudul.setText(model.getJudul());
        holder.mLinearBerita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailBeritaActivity.class);
                intent.putExtra("foto_lab", model.getFoto_lab());
                intent.putExtra("nama_lab", model.getNama_lab());
                intent.putExtra("alamat", model.getAlamat());
                intent.putExtra("no_telp", model.getNo_telp());
                intent.putExtra("id_laboratorium", model.getId_laboratorium());
                intent.putExtra("id_berita", model.getId_berita());
                intent.putExtra("judul", model.getJudul());
                intent.putExtra("isi", model.getIsi());
                intent.putExtra("timestamp", model.getTimestamp());

                context.startActivity(intent);
            }
        });

        holder.mRlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TambahBeritaActivity.class);
                intent.putExtra("id_laboratorium", model.getId_laboratorium());
                intent.putExtra("judul", model.getJudul());
                intent.putExtra("isi", model.getIsi());
                intent.putExtra("id_berita", model.getId_berita());
                context.startActivity(intent);
            }
        });

        holder.mRlHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_berita = model.getId_berita();
                showAlertDialog();
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mJudul, mNamaLab;
        public LinearLayout mLinearBerita;
        public ImageView mImageViewLab;
        public RelativeLayout mRlEdit, mRlHapus;

        public MyViewHolder(View itemView) {
            super(itemView);

            mImageViewLab = itemView.findViewById(R.id.foto_lab);
            mJudul = itemView.findViewById(R.id.judul);
            mNamaLab = itemView.findViewById(R.id.nama_lab);
            mLinearBerita = itemView.findViewById(R.id.linear_berita);
            mRlEdit = itemView.findViewById(R.id.rl_edit);
            mRlHapus = itemView.findViewById(R.id.rl_hapus);
        }
    }

    private void showAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(context, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert));


        // set title dialog
        alertDialogBuilder.setTitle("Menghapus Berita");

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
        mApiService.HapusBerita(id_berita)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(context, "BERHASIL HAPUS BERITA", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(context, "GAGAL HAPUS BERITA", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}