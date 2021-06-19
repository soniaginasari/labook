package com.example.labook2.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labook2.CrudLaboratoriumActivity;
import com.example.labook2.DetailSewaLabLaboranActivity;
import com.example.labook2.DetailVerifLaboranActivity;
import com.example.labook2.ListVerifLaboranActivity;
import com.example.labook2.Preferences;
import com.example.labook2.R;
import com.example.labook2.apihelper.BaseApiService;
import com.example.labook2.apihelper.RetrofitClient;
import com.example.labook2.model.SewaItem;
import com.example.labook2.model.VerifLaboranItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifLaboranAdapter extends RecyclerView.Adapter<VerifLaboranAdapter.MyViewHolder> {

    List<VerifLaboranItem> my_list;
    List<VerifLaboranItem> my_listfull;
    Context context;
    BaseApiService mApiService;
    Preferences sharedPrefManager;
    String nama_lab, alamat, no_telp, id_laboratorium, foto_lab, nama_bidang, id_bidang, id_verif_bidang, str_ket_crud;
    int ket_crud;

    public VerifLaboranAdapter(Context context, List<VerifLaboranItem> my_list) {
        this.my_list = my_list;
        my_listfull = new ArrayList<>(my_list);
        this.context = context;
        mApiService = RetrofitClient.getService(context.getApplicationContext());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_verif_laboran, parent, false);
            return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final VerifLaboranItem model=my_list.get(position);
        String nama_lab = model.getNama_lab();
        nama_bidang = model.getNama_bidang();
        String nama_layanan = model.getNama_layanan();
        id_bidang = model.getId_bidang();
        id_verif_bidang = model.getId_verif_bidang();
        id_laboratorium = model.getId_laboratorium();
        ket_crud = Integer.parseInt(model.getKet_crud());
        int ket = Integer.parseInt(model.getKet_crud());
        String ket_crud = null;
        if(ket == 1) {
            ket_crud="Insert Data";
        }if(ket == 2){
            ket_crud="Update Data";
        }if (ket == 3){
            ket_crud="Delete Data";
        }
        holder.mket_crud.setText(ket_crud);
        final String finalKet_crud = ket_crud;
        if (nama_lab != null){
            holder.mnama.setText(nama_lab);
            holder.mbtnVerif.setVisibility(View.GONE);
            holder.mCardViewVerif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, DetailVerifLaboranActivity.class);
                    intent.putExtra("id_verif_lab", model.getId_verif_lab());
                    intent.putExtra("id_verif_bidang", model.getId_verif_bidang());
                    intent.putExtra("id_verif_layanan", model.getId_verif_layanan());
                    intent.putExtra("id_laboratorium", model.getId_laboratorium());
                    intent.putExtra("nama_lab", model.getNama_lab());
                    intent.putExtra("alamat", model.getAlamat());
                    intent.putExtra("no_telp", model.getNo_telp());
                    intent.putExtra("foto_lab", model.getFoto_lab());
                    intent.putExtra("id_bidang", model.getId_bidang());
                    intent.putExtra("nama_bidang", model.getNama_bidang());
                    intent.putExtra("id_layanan", model.getId_layanan());
                    intent.putExtra("nama_layanan", model.getNama_layanan());
                    intent.putExtra("unit_satuan", model.getUnit_satuan());
                    intent.putExtra("satuan", model.getSatuan());
                    intent.putExtra("harga", model.getHarga());
                    intent.putExtra("keterangan", model.getKeterangan());
                    intent.putExtra("ket_crud", model.getKet_crud());
                    intent.putExtra("string_ket_crud", finalKet_crud);
                    context.startActivity(intent);
                }
            });
        }
        if (nama_bidang != null){
            holder.mnama.setText(nama_bidang);
            holder.mbtnVerif.setVisibility(View.VISIBLE);
            holder.mIvPanah.setVisibility(View.GONE);
        }
        if (nama_layanan != null){
            holder.mnama.setText(nama_layanan);
            holder.mbtnVerif.setVisibility(View.GONE);
            holder.mCardViewVerif.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context, DetailVerifLaboranActivity.class);
                    intent.putExtra("id_verif_lab", model.getId_verif_lab());
                    intent.putExtra("id_verif_bidang", model.getId_verif_bidang());
                    intent.putExtra("id_verif_layanan", model.getId_verif_layanan());
                    intent.putExtra("id_laboratorium", model.getId_laboratorium());
                    intent.putExtra("nama_lab", model.getNama_lab());
                    intent.putExtra("alamat", model.getAlamat());
                    intent.putExtra("no_telp", model.getNo_telp());
                    intent.putExtra("foto_lab", model.getFoto_lab());
                    intent.putExtra("id_bidang", model.getId_bidang());
                    intent.putExtra("nama_bidang", model.getNama_bidang());
                    intent.putExtra("id_layanan", model.getId_layanan());
                    intent.putExtra("nama_layanan", model.getNama_layanan());
                    intent.putExtra("unit_satuan", model.getUnit_satuan());
                    intent.putExtra("satuan", model.getSatuan());
                    intent.putExtra("harga", model.getHarga());
                    intent.putExtra("keterangan", model.getKeterangan());
                    intent.putExtra("ket_crud", model.getKet_crud());
                    intent.putExtra("string_ket_crud", finalKet_crud);
                    context.startActivity(intent);
                }
            });
        }




        holder.mbtnVerif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mket_crud, mnama;
        public CardView mCardViewVerif;
        public Button mbtnVerif;
        public ImageView mIvPanah;

        public MyViewHolder(View itemView) {
            super(itemView);

            mket_crud = itemView.findViewById(R.id.ket_crud);
            mnama = itemView.findViewById(R.id.nama);
            mCardViewVerif=itemView.findViewById(R.id.cvVerif);
            mbtnVerif = itemView.findViewById(R.id.btn_verif);
            mIvPanah = itemView.findViewById(R.id.panah);
        }
    }

    private void showAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title dialog
        alertDialogBuilder.setTitle("Memverifikasi Data");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Apakah anda yakin ?")
                .setIcon(R.mipmap.logo_labook)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // jika tombol diklik, maka akan menutup activity ini
                        if (ket_crud == 1){
                            requestTambahBidang();
                        }
                        if (ket_crud == 2){
                            requestEditBidang();
                        }
                        if (ket_crud == 3){
                            requestHapusBidang();
                        }
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

    private void requestTambahBidang() {
        mApiService.SetBidang(nama_bidang,id_laboratorium,id_verif_bidang)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
//                            loading.dismiss();
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(context, "BIDANG BERHASIL DITAMBAHKAN", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(context, ListVerifLaboranActivity.class);
                                    intent.putExtra("id_laboratorium", id_laboratorium);
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
                            Toast.makeText(context, "BIDANG GAGAL DITAMBAHKAN", Toast.LENGTH_SHORT).show();
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

    private void requestEditBidang() {
        mApiService.EditBidang(id_bidang,nama_bidang,id_verif_bidang)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(context, "BERHASIL EDIT BIDANG", Toast.LENGTH_SHORT).show();

                                   Intent intent = new Intent(context, ListVerifLaboranActivity.class);
                                    intent.putExtra("id_laboratorium", id_laboratorium);
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
                            Toast.makeText(context, "GAGAL EDIT BIDANG", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }

    private void requestHapusBidang() {
        mApiService.HapusBidang(id_bidang,id_verif_bidang)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            try {
                                JSONObject jsonRESULTS = new JSONObject(response.body().string());
                                if (jsonRESULTS.getString("status").equals("true")) {

                                    Toast.makeText(context, "BERHASIL HAPUS BIDANG", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(context, ListVerifLaboranActivity.class);
                                    intent.putExtra("id_laboratorium", id_laboratorium);
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
                            Toast.makeText(context, "GAGAL HAPUS BIDANG", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("debug", "onFailure: ERROR > " + t.toString());
                    }
                });
    }
}
