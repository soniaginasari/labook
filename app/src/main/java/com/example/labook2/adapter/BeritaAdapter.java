package com.example.labook2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.labook2.BidangLaboratoriumActivity;
import com.example.labook2.DetailBeritaActivity;
import com.example.labook2.R;
import com.example.labook2.model.BeritaItem;
import com.example.labook2.model.LaboratoriumItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.MyViewHolder> {

    List<BeritaItem> my_list;
    List<BeritaItem> my_listfull;
    Context context;

    public BeritaAdapter(Context context, List<BeritaItem> my_list) {
        this.my_list = my_list;
        my_listfull = new ArrayList<>(my_list);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_popular_courses, parent, false);
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
    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mJudul, mNamaLab;
        public LinearLayout mLinearBerita;
        public ImageView mImageViewLab;

        public MyViewHolder(View itemView) {
            super(itemView);

            mImageViewLab = itemView.findViewById(R.id.foto_lab);
            mJudul = itemView.findViewById(R.id.judul);
            mNamaLab = itemView.findViewById(R.id.nama_lab);
            mLinearBerita = itemView.findViewById(R.id.linear_berita);
        }
    }
}