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
import com.example.labook2.DetailSewaLabActivity;
import com.example.labook2.MainPeminjamActivity;
import com.example.labook2.R;
import com.example.labook2.model.LaboratoriumItem;
import com.example.labook2.model.SewaItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class SewaAdapter extends RecyclerView.Adapter<SewaAdapter.MyViewHolder> {

    List<SewaItem> my_list;
    List<SewaItem> my_listfull;
    Context context;

    public SewaAdapter(Context context, List<SewaItem> my_list) {
        this.my_list = my_list;
        my_listfull = new ArrayList<>(my_list);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_frutorials,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SewaItem model=my_list.get(position);
        Picasso.get().load(model.getFoto_lab()).into(holder.mFoto_lab);
        holder.mnama_layanan.setText(model.getNama_layanan());
        int ket = Integer.parseInt(model.getKeterangan());
        String keterangan = null;
        if(ket == 1) {
            keterangan="Menunggu Konfirmasi";
        }if(ket == 2){
            keterangan="Disetujui";
        }if (ket == 3){
            keterangan="Ditolak";
        }
        final String finalKeterangan = keterangan;
        holder.mketerangan.setText(finalKeterangan);
        int ket_pengerjaan = Integer.parseInt(model.getKet_pengerjaan());
        String keterangan_pengerjaan = null;
        if(ket_pengerjaan == 1) {
            keterangan_pengerjaan="Dalam Proses Pengerjaan";
        }if(ket_pengerjaan == 2){
            keterangan_pengerjaan="Selesai";
        }
        final String finalKeteranganPengerjaan = keterangan_pengerjaan;
        int ket_pembayaran = Integer.parseInt(model.getKet_pembayaran());
        String keterangan_pembayaran = null;
        if(ket_pembayaran == 1) {
            keterangan_pembayaran="Pembayaran belum lunas";
        }if(ket_pembayaran == 2){
            keterangan_pembayaran="Pembayaran lunas";
        }
        final String finalKeteranganPembayaran = keterangan_pembayaran;
        holder.mCardViewPeminjaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DetailSewaLabActivity.class);
                intent.putExtra("id_peminjaman", model.getId_peminjaman());
                intent.putExtra("nama_layanan", model.getNama_layanan());
                intent.putExtra("tgl_order", model.getTgl_order());
                intent.putExtra("tgl_pinjam", model.getTgl_pinjam());
                intent.putExtra("tgl_selesai", model.getTgl_selesai());
                intent.putExtra("jumlah", model.getJumlah());
                intent.putExtra("satuan", model.getSatuan());
                intent.putExtra("keterangan", finalKeterangan);
                intent.putExtra("int_keterangan", model.getKeterangan());
                intent.putExtra("harga", model.getHarga());
                intent.putExtra("total_harga", model.getTotal_harga());
                intent.putExtra("file", model.getFile());
                intent.putExtra("nama_bidang", model.getNama_bidang());
                intent.putExtra("nama_lab", model.getNama_lab());
                intent.putExtra("alamat", model.getAlamat());
                intent.putExtra("no_telp", model.getNo_telp());
                intent.putExtra("id_bidang", model.getId_bidang());
                intent.putExtra("id_laboratorium", model.getId_laboratorium());
                intent.putExtra("alasan", model.getAlasan());
                intent.putExtra("keterangan_pengerjaan", finalKeteranganPengerjaan);
                intent.putExtra("ket_pengerjaan", model.getKet_pengerjaan());
                intent.putExtra("keterangan_pembayaran", finalKeteranganPembayaran);
                intent.putExtra("ket_pembayaran", model.getKet_pembayaran());
                intent.putExtra("metode_pembayaran", model.getMetode_pembayaran());
                intent.putExtra("tgl_bayar", model.getTgl_bayar());
                intent.putExtra("bukti_pembayaran", model.getBukti_pembayaran());
                intent.putExtra("file_selesai", model.getFile_selesai());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mnama_layanan, mketerangan;
        public CardView mCardViewPeminjaman;
        public ImageView mFoto_lab;

        public MyViewHolder(View itemView) {
            super(itemView);

            mnama_layanan = itemView.findViewById(R.id.nama_layanan);
            mketerangan = itemView.findViewById(R.id.keterangan);
            mCardViewPeminjaman=itemView.findViewById(R.id.card_view);
            mFoto_lab = itemView.findViewById(R.id.foto_lab);
        }
    }
}
