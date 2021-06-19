package com.example.labook2.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.labook2.MainPeminjamActivity;
import com.example.labook2.R;
import com.example.labook2.model.LaboranItem;
import com.example.labook2.model.SewaItem;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class LaboranAdapter extends RecyclerView.Adapter<LaboranAdapter.MyViewHolder> {

    List<LaboranItem> my_list;
    List<LaboranItem> my_listfull;
    Context context;

    public LaboranAdapter(Context context, List<LaboranItem> my_list) {
        this.my_list = my_list;
        my_listfull = new ArrayList<>(my_list);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_laboran,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LaboranItem model=my_list.get(position);
        holder.mnama_laboran.setText(model.getNama_laboran());
//        holder.malamat.setText(model.getAlamat());
        holder.mno_hp.setText(model.getNo_hp());
        int hak_akses = Integer.parseInt(model.getHak_akses());
        String ket_hak_akses = null;
        if(hak_akses == 1) {
            ket_hak_akses ="Laboran";
        }if(hak_akses == 2){
            ket_hak_akses="Kepala Lab";
        }if (hak_akses == 3){
            ket_hak_akses="Pimpinan";
        }
        holder.mhak_akses.setText(ket_hak_akses);
        final String finalHak_akses = ket_hak_akses;
        String nomor = model.getNo_hp();
        holder.ivPanggil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent panggil = new Intent(Intent. ACTION_DIAL);
                panggil.setData(Uri. fromParts("tel",nomor,null));
                context.startActivity(panggil);

            }
        });
    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mnama_laboran, malamat, mno_hp, mhak_akses;
        public CardView mCardViewLaboran;
        public ImageView ivPanggil;

        public MyViewHolder(View itemView) {
            super(itemView);

            mnama_laboran = itemView.findViewById(R.id.nama_laboran);
//            malamat = itemView.findViewById(R.id.alamat);
            mno_hp = itemView.findViewById(R.id.no_hp);
            mhak_akses = itemView.findViewById(R.id.hak_akses);
            mCardViewLaboran =itemView.findViewById(R.id.cvLaboran);
            ivPanggil = itemView.findViewById(R.id.panggil);

        }
    }
}
