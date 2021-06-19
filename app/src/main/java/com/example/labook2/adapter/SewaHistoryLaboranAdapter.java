package com.example.labook2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.labook2.DetailSewaLabActivity;
import com.example.labook2.DetailSewaLabLaboranActivity;
import com.example.labook2.R;
import com.example.labook2.model.SewaItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class SewaHistoryLaboranAdapter extends RecyclerView.Adapter<SewaHistoryLaboranAdapter.MyViewHolder> {

    List<SewaItem> my_list;
    List<SewaItem> my_listfull;
    Context context;

    public SewaHistoryLaboranAdapter(Context context, List<SewaItem> my_list) {
        this.my_list = my_list;
        my_listfull = new ArrayList<>(my_list);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_sewa,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SewaItem model=my_list.get(position);
        holder.mnama_layanan.setText(model.getNama_layanan());
        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
        Date date = null; //YOUR DATE HERE
        try {
            date = df.parse(model.getTgl_pinjam());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.applyPattern("dd");
        String newDate = df.format(date);  //Output: newDate = "13/09/2014"
        holder.mtgl_pinjam.setText(newDate);
        SimpleDateFormat df_bulan_tahun = new SimpleDateFormat("yyyy-MM-dd");
        Date date_bulan_tahun = null; //YOUR DATE HERE
        try {
            date_bulan_tahun = df_bulan_tahun.parse(model.getTgl_pinjam());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df_bulan_tahun.applyPattern("MMM yyyy");
        String newDate_bulan_tahun = df_bulan_tahun.format(date_bulan_tahun);  //Output: newDate = "13/09/2014"
        holder.mbulan_tahun.setText(newDate_bulan_tahun);
//        String formatTanggal = "dd";
//        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
//        holder.mtgl_pinjam.setText(sdf.format(model.getTgl_pinjam()));
//        String formatBulanTahun = "MM-yyyy";
//        SimpleDateFormat sdfBulanTahun = new SimpleDateFormat(formatBulanTahun);
        holder.mjumlah_satuan.setText(model.getJumlah()+" "+ model.getSatuan());
        holder.mnama_lab.setText(model.getNama_lab());
        int ket = Integer.parseInt(model.getKeterangan());
        String keterangan = null;
        if(ket == 1) {
            keterangan="Menunggu Konfirmasi";
            holder.mketerangan.setTextColor(ContextCompat.getColor(context,R.color.ket_kuning));
        }if(ket == 2){
            keterangan="Disetujui";
            holder.mketerangan.setTextColor(ContextCompat.getColor(context,R.color.ket_hijau));
        }if (ket == 3){
            keterangan="Ditolak";
            holder.mketerangan.setTextColor(ContextCompat.getColor(context,R.color.ket_merah));
        }
        holder.mketerangan.setText(keterangan);
        final String finalKeterangan = keterangan;
        int ket_pembayaran = Integer.parseInt(model.getKet_pembayaran());
        String keterangan_pembayaran = null;
        if(ket_pembayaran == 1) {
            keterangan_pembayaran="Menunggu Proses Pembayaran";
            holder.mketerangan.setTextColor(ContextCompat.getColor(context,R.color.ket_kuning));
            holder.mketerangan.setText(keterangan_pembayaran);
        }if(ket_pembayaran == 2){
            keterangan_pembayaran="Pembayaran lunas";
            holder.mketerangan.setTextColor(ContextCompat.getColor(context,R.color.ket_hijau));
            holder.mketerangan.setText(keterangan_pembayaran);
        }
        final String finalKeteranganPembayaran = keterangan_pembayaran;
        int ket_pengerjaan = Integer.parseInt(model.getKet_pengerjaan());
        String keterangan_pengerjaan = null;
        if(ket_pengerjaan == 1) {
            keterangan_pengerjaan="Dalam Proses Pengerjaan";
            holder.mketerangan.setTextColor(ContextCompat.getColor(context,R.color.ket_hijau));
            holder.mketerangan.setText(keterangan_pengerjaan);
        }if(ket_pengerjaan == 2){
            keterangan_pengerjaan="Selesai";
            holder.mketerangan.setTextColor(ContextCompat.getColor(context,R.color.ket_merah));
            holder.mketerangan.setText(keterangan_pengerjaan);
        }
//        holder.mket_pengerjaan.setText(keterangan_pengerjaan);
        final String finalKeteranganPengerjaan = keterangan_pengerjaan;
        holder.mCardViewPeminjaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DetailSewaLabLaboranActivity.class);
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
                intent.putExtra("id_peminjam", model.getId_peminjam());
                intent.putExtra("nama_peminjam", model.getNama_peminjam());
                intent.putExtra("no_hp_peminjam", model.getNo_hp_peminjam());
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

        public TextView mnama_layanan, mketerangan, msatuan, mjumlah_satuan, mbulan_tahun, mtgl_pinjam, mtgl_selesai, mnama_lab, mket_pengerjaan;
        public CardView mCardViewPeminjaman;

        public MyViewHolder(View itemView) {
            super(itemView);

            mnama_layanan = itemView.findViewById(R.id.nama_layanan);
            mjumlah_satuan = itemView.findViewById(R.id.jumlah_satuan);
            mtgl_pinjam = itemView.findViewById(R.id.tgl_pinjam);
            mbulan_tahun = itemView.findViewById(R.id.bulan_tahun);
            mketerangan = itemView.findViewById(R.id.keterangan);
            mCardViewPeminjaman=itemView.findViewById(R.id.cvPeminjaman);
            mnama_lab = itemView.findViewById(R.id.nama_lab);
            mket_pengerjaan = itemView.findViewById(R.id.ket_pengerjaan);
        }
    }
}
