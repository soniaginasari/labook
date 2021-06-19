package com.example.labook2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.labook2.BidangLaboratoriumActivity;
import com.example.labook2.R;
import com.example.labook2.model.LaboratoriumItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AwalLabAdapter extends RecyclerView.Adapter<AwalLabAdapter.MyViewHolder> {

    List<LaboratoriumItem> my_list;
    List<LaboratoriumItem> my_listfull;
    Context context;

    public AwalLabAdapter(Context context, List<LaboratoriumItem> my_list) {
        this.my_list = my_list;
        my_listfull = new ArrayList<>(my_list);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_awal_lab,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LaboratoriumItem model=my_list.get(position);
        Picasso.get().load(model.getFoto_lab()).into(holder.mImageViewLab);
        holder.namak.setText(model.getNama_lab());
        holder.alamatk.setText(model.getAlamat());
        holder.mLinearLab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, BidangLaboratoriumActivity.class);
                intent.putExtra("foto_lab", model.getFoto_lab());
                intent.putExtra("nama_lab", model.getNama_lab());
                intent.putExtra("alamat",model.getAlamat());
                intent.putExtra("no_telp",model.getNo_telp());
                intent.putExtra("id_laboratorium",model.getId_laboratorium());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView namak, alamatk;
        public LinearLayout mLinearLab;
        public ImageView mImageViewLab;

        public MyViewHolder(View itemView) {
            super(itemView);

            mImageViewLab=itemView.findViewById(R.id.foto_lab);
            namak= itemView.findViewById(R.id.nama_lab);
            alamatk=itemView.findViewById(R.id.alamat);
            mLinearLab=itemView.findViewById(R.id.linearLayout);
        }
    }
}
