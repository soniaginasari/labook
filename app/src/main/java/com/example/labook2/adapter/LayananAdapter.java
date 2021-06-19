package com.example.labook2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.labook2.DetailLayananActivity;
import com.example.labook2.LayananLaboratoriumActivity;
import com.example.labook2.MainPeminjamActivity;
import com.example.labook2.R;
import com.example.labook2.model.BidangItem;
import com.example.labook2.model.LayananItem;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class LayananAdapter extends RecyclerView.Adapter<LayananAdapter.MyViewHolder> {

    List<LayananItem> my_list;
    List<LayananItem> my_listfull;
    Context context;

    public LayananAdapter(Context context, List<LayananItem> my_list) {
        this.my_list = my_list;
        my_listfull = new ArrayList<>(my_list);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layanan,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LayananItem model=my_list.get(position);

        holder.namak.setText(model.getNama_layanan());
        holder.mLinearBidang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DetailLayananActivity.class);
                intent.putExtra("nama_layanan", model.getNama_layanan());
                intent.putExtra("id_layanan",model.getId_layanan());
                intent.putExtra("id_bidang",model.getId_bidang());
                intent.putExtra("unit_satuan",model.getUnit_satuan());
                intent.putExtra("satuan",model.getSatuan());
                intent.putExtra("harga",model.getHarga());
                intent.putExtra("keterangan",model.getKeterangan());
                intent.putExtra("id_laboratorium",model.getId_laboratorium());
                intent.putExtra("nama_bidang",model.getNama_bidang());
                intent.putExtra("nama_lab",model.getNama_lab());
                intent.putExtra("alamat",model.getAlamat());
                intent.putExtra("no_telp",model.getNo_telp());
                intent.putExtra("foto_lab",model.getFoto_lab());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView namak ;
        public LinearLayout mLinearBidang;


        public MyViewHolder(View itemView) {
            super(itemView);


            namak= itemView.findViewById(R.id.nama_layanan);
            mLinearBidang=itemView.findViewById(R.id.linearLayout);
        }
    }


    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<LayananItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(my_listfull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (LayananItem item : my_listfull) {
                    if (item.getNama_layanan().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                    if (item.getKeterangan().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            my_list.clear();
            my_list.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
