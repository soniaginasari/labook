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

public class LaboratoriumAdapter extends RecyclerView.Adapter<LaboratoriumAdapter.MyViewHolder> {

    List<LaboratoriumItem> my_list;
    List<LaboratoriumItem> my_listfull;
    Context context;

    public LaboratoriumAdapter(Context context, List<LaboratoriumItem> my_list) {
        this.my_list = my_list;
        my_listfull = new ArrayList<>(my_list);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_laboratorium,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final LaboratoriumItem model=my_list.get(position);
        Picasso.get().load(model.getFoto_lab()).into(holder.mImageViewLab);
        holder.namak.setText(model.getNama_lab());
        holder.alamatk.setText(model.getAlamat());
        holder.no_telpk.setText(model.getNo_telp());
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

        public TextView namak, alamatk, no_telpk;
        public LinearLayout mLinearLab;
        public ImageView mImageViewLab;

        public MyViewHolder(View itemView) {
            super(itemView);

            mImageViewLab=itemView.findViewById(R.id.foto_lab);
            namak= itemView.findViewById(R.id.nama_lab);
            alamatk=itemView.findViewById(R.id.alamat);
            no_telpk=itemView.findViewById(R.id.no_telp);
            mLinearLab=itemView.findViewById(R.id.linearLayout);
        }
    }
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String searchString = charSequence.toString();

                if (searchString.isEmpty()) {

                    my_listfull = my_list;

                } else {

                    ArrayList<LaboratoriumItem> tempFilteredList = new ArrayList<>();

                    for (LaboratoriumItem item : my_list) {

                        // search for user title
                        if (item.getNama_lab().toLowerCase().contains(searchString)) {

                            tempFilteredList.add(item);
                        }
                    }

                    my_listfull = tempFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = my_listfull;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                my_listfull = (ArrayList<LaboratoriumItem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

