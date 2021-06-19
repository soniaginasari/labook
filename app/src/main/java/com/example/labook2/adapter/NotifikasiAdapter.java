package com.example.labook2.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.labook2.DetailSewaLabActivity;
import com.example.labook2.R;
import com.example.labook2.model.NotifikasiItem;
import com.example.labook2.model.SewaItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.MyViewHolder> {

    List<NotifikasiItem> my_list;
    List<NotifikasiItem> my_listfull;
    Context context;

    public NotifikasiAdapter(Context context, List<NotifikasiItem> my_list) {
        this.my_list = my_list;
        my_listfull = new ArrayList<>(my_list);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_notifikasi,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final NotifikasiItem model=my_list.get(position);
        holder.mtv_layanan.setText(model.getNama_layanan());
        holder.mtv_notif.setText(model.getNotifikasi());
        holder.mtv_date.setText(model.getCreated_at());
        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
        Date date = null; //YOUR DATE HERE
        try {
            date = df.parse(model.getTgl_pinjam());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df.applyPattern("dd");
        String newDate = df.format(date);  //Output: newDate = "13/09/2014"
        SimpleDateFormat df_bulan_tahun = new SimpleDateFormat("yyyy-MM-dd");
        Date date_bulan_tahun = null; //YOUR DATE HERE
        try {
            date_bulan_tahun = df_bulan_tahun.parse(model.getTgl_pinjam());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        df_bulan_tahun.applyPattern("MMM yyyy");
        String newDate_bulan_tahun = df_bulan_tahun.format(date_bulan_tahun);  //Output: newDate = "13/09/2014"
//        String formatTanggal = "dd";
//        SimpleDateFormat sdf = new SimpleDateFormat(formatTanggal);
//        holder.mtgl_pinjam.setText(sdf.format(model.getTgl_pinjam()));
//        String formatBulanTahun = "MM-yyyy";
//        SimpleDateFormat sdfBulanTahun = new SimpleDateFormat(formatBulanTahun);
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

        int ket_pembayaran = Integer.parseInt(model.getKet_pembayaran());
        String keterangan_pembayaran = null;
        if(ket_pembayaran == 1) {
            keterangan_pembayaran="Pembayaran belum lunas";

        }if(ket_pembayaran == 2){
            keterangan_pembayaran="Pembayaran lunas";

        }
        final String finalKeteranganPembayaran = keterangan_pembayaran;
        int ket_pengerjaan = Integer.parseInt(model.getKet_pengerjaan());
        String keterangan_pengerjaan = null;
        if(ket_pengerjaan == 1) {
            keterangan_pengerjaan="Dalam Proses Pengerjaan";

        }if(ket_pengerjaan == 2){
            keterangan_pengerjaan="Selesai";

        }
//        holder.mket_pengerjaan.setText(keterangan_pengerjaan);
        final String finalKeteranganPengerjaan = keterangan_pengerjaan;
        holder.mCardViewNotif.setOnClickListener(new View.OnClickListener() {
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
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return my_list.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView mtv_layanan, mtv_notif, mtv_date;
        public CardView mCardViewNotif;

        public MyViewHolder(View itemView) {
            super(itemView);

            mtv_layanan= itemView.findViewById(R.id.tv_layanan);
            mtv_notif = itemView.findViewById(R.id.tv_notif);
            mtv_date = itemView.findViewById(R.id.tv_date);
            mCardViewNotif=itemView.findViewById(R.id.cd_notif);
        }
    }
}
