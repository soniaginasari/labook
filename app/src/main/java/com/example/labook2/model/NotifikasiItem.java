package com.example.labook2.model;

import com.google.gson.annotations.SerializedName;

public class NotifikasiItem {
    @SerializedName("id_notifikasi")
    private String id_notifikasi;

    @SerializedName("notifikasi")
    private String notifikasi;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("id_peminjaman")
    private String id_peminjaman;

    @SerializedName("nama_layanan")
    private String nama_layanan;

    @SerializedName("jumlah")
    private String jumlah;

    @SerializedName("satuan")
    private String satuan;

    @SerializedName("harga")
    private String harga;

    @SerializedName("total_harga")
    private String total_harga;

    @SerializedName("keterangan")
    private String keterangan;

    @SerializedName("ket_pengerjaan")
    private String ket_pengerjaan;

    @SerializedName("ket_pembayaran")
    private String ket_pembayaran;

    @SerializedName("metode_pembayaran")
    private String metode_pembayaran;

    @SerializedName("bukti_pembayaran")
    private String bukti_pembayaran;

    @SerializedName("tgl_bayar")
    private String tgl_bayar;

    @SerializedName("alasan")
    private String alasan;

    @SerializedName("tgl_order")
    private String tgl_order;

    @SerializedName("tgl_pinjam")
    private String tgl_pinjam;

    @SerializedName("tgl_selesai")
    private String tgl_selesai;

    @SerializedName("file")
    private String file;

    @SerializedName("no_telp")
    private String no_telp;

    @SerializedName("foto_lab")
    private String foto_lab;

    @SerializedName("nama_bidang")
    private String nama_bidang;

    @SerializedName("nama_lab")
    private String nama_lab;

    @SerializedName("alamat")
    private String alamat;

    @SerializedName("id_bidang")
    private String id_bidang;

    @SerializedName("id_laboratorium")
    private String id_laboratorium;

    @SerializedName("id_peminjam")
    private String id_peminjam;

    @SerializedName("nama_peminjam")
    private String nama_peminjam;

    @SerializedName("no_hp_peminjam")
    private String no_hp_peminjam;

    public void setId_notifikasi(String id_notifikasi){
        this.id_notifikasi = id_notifikasi;
    }

    public String getId_notifikasi(){
        return id_notifikasi;
    }

    public void setNotifikasi(String notifikasi){
        this.notifikasi = notifikasi;
    }

    public String getNotifikasi(){
        return notifikasi;
    }

    public void setCreated_at(String created_at){
        this.created_at = created_at;
    }

    public String getCreated_at(){
        return created_at;
    }


    public void setId_peminjaman(String id_peminjaman){
        this.id_peminjaman = id_peminjaman;
    }

    public String getId_peminjaman(){
        return id_peminjaman;
    }

    public void setNama_layanan(String nama_layanan){
        this.nama_layanan = nama_layanan;
    }

    public String getNama_layanan(){
        return nama_layanan;
    }

    public void setJumlah(String jumlah){
        this.jumlah = jumlah;
    }

    public String getJumlah(){
        return jumlah;
    }

    public void setSatuan(String satuan){
        this.satuan = satuan;
    }

    public String getSatuan(){
        return satuan;
    }

    public void setHarga(String harga){
        this.harga = harga;
    }

    public String getHarga(){
        return harga;
    }

    public void setTotal_harga(String total_harga){
        this.total_harga = total_harga;
    }

    public String getTotal_harga(){
        return total_harga;
    }

    public void setKeterangan(String keterangan){
        this.keterangan = keterangan;
    }

    public String getKeterangan(){
        return keterangan;
    }

    public void setAlasan(String alasan){
        this.alasan = alasan;
    }

    public String getAlasan(){
        return alasan;
    }

    public void setKet_pengerjaan(String ket_pengerjaan){
        this.ket_pengerjaan = ket_pengerjaan;
    }

    public String getKet_pengerjaan(){
        return ket_pengerjaan;
    }

    public void setKet_pembayaran(String ket_pembayaran){
        this.ket_pembayaran = ket_pembayaran;
    }

    public String getKet_pembayaran(){
        return ket_pembayaran;
    }

    public void setMetode_pembayaran(String metode_pembayaran){
        this.metode_pembayaran = metode_pembayaran;
    }

    public String getMetode_pembayaran(){
        return metode_pembayaran;
    }

    public void setBukti_pembayaran(String bukti_pembayaran){
        this.bukti_pembayaran = bukti_pembayaran;
    }

    public String getBukti_pembayaran(){
        return bukti_pembayaran;
    }

    public void setTgl_bayar(String tgl_bayar){
        this.tgl_bayar = tgl_bayar;
    }

    public String getTgl_bayar(){
        return tgl_bayar;
    }

    public void setTgl_order(String tgl_order){
        this.tgl_order = tgl_order;
    }

    public String getTgl_order(){
        return tgl_order;
    }

    public void setTgl_pinjam(String tgl_pinjam){
        this.tgl_pinjam = tgl_pinjam;
    }

    public String getTgl_pinjam(){
        return tgl_pinjam;
    }

    public void setTgl_selesai(String tgl_selesai){
        this.tgl_selesai = tgl_selesai;
    }

    public String getTgl_selesai(){
        return tgl_selesai;
    }

    public void setNama_bidang(String nama_bidang){
        this.nama_bidang = nama_bidang;
    }

    public String getNama_bidang(){
        return nama_bidang;
    }

    public void setNama_lab(String nama_lab){
        this.nama_lab = nama_lab;
    }

    public String getNama_lab(){
        return nama_lab;
    }

    public void setAlamat(String alamat){
        this.alamat = alamat;
    }

    public String getAlamat(){
        return alamat;
    }

    public void setNo_telp(String no_telp){
        this.no_telp = no_telp;
    }

    public String getNo_telp(){
        return no_telp;
    }

    public void setFoto_lab(String foto_lab){
        this.foto_lab = foto_lab;
    }

    public String getFoto_lab(){
        return foto_lab;
    }

    public void setFile(String file){
        this.file = file;
    }

    public String getFile(){
        return file;
    }

    public void setId_bidang(String id_bidang){
        this.id_bidang = id_bidang;
    }

    public String getId_bidang(){
        return id_bidang;
    }

    public void setId_laboratorium(String id_laboratorium){
        this.id_laboratorium = id_laboratorium;
    }

    public String getId_laboratorium(){
        return id_laboratorium;
    }

    public void setId_peminjam(String id_peminjam){
        this.id_peminjam = id_peminjam;
    }

    public String getId_peminjam(){
        return id_peminjam;
    }

    public void setNama_peminjam(String nama_peminjam){
        this.nama_peminjam = nama_peminjam;
    }

    public String getNama_peminjam(){
        return nama_peminjam;
    }

    public void setNo_hp_peminjam(String no_hp_peminjam){
        this.no_hp_peminjam = no_hp_peminjam;
    }

    public String getNo_hp_peminjam(){
        return no_hp_peminjam;
    }

    @Override
    public String toString(){
        return
                "NotifikasiItem{" +
                        "id_notifikasi = '" + id_notifikasi + '\'' +
                        "notifikasi = '" + notifikasi + '\'' +
                        "created_at = '" + created_at + '\'' +
                        "id_peminjaman = '" + id_peminjaman + '\'' +
                        "nama_layanan = '" + nama_layanan + '\'' +
                        "jumlah = '" + jumlah + '\'' +
                        "satuan = '" + satuan + '\'' +
                        "harga = '" + harga + '\'' +
                        "total_harga = '" + total_harga + '\'' +
                        "keterangan = '" + keterangan + '\'' +
                        "tgl_order = '" + tgl_order + '\'' +
                        "tgl_pinjam = '" + tgl_pinjam + '\'' +
                        "tgl_selesai = '" + tgl_selesai + '\'' +
                        "nama_bidang = '" + nama_bidang + '\'' +
                        "nama_lab = '" + nama_lab + '\'' +
                        "alamat = '" + alamat + '\'' +
                        "no_telp = '" + no_telp + '\'' +
                        "foto_lab = '" + foto_lab + '\'' +
                        "file = '" + file + '\'' +
                        "id_bidang = '" + id_bidang + '\'' +
                        "id_laboratorium = '" + id_laboratorium + '\'' +
                        "ket_pengerjaan = '" + ket_pengerjaan + '\'' +
                        "ket_pembayaran = '" + ket_pembayaran + '\'' +
                        "metode_pembayaran = '" + metode_pembayaran + '\'' +
                        "bukti_pembayaran = '" + bukti_pembayaran + '\'' +
                        "tgl_bayar = '" + tgl_bayar + '\'' +
                        "alasan = '" + alasan + '\'' +
                        "id_peminjam = '" + id_peminjam + '\'' +
                        "nama_peminjam = '" + nama_peminjam + '\'' +
                        "no_hp_peminjam = '" + no_hp_peminjam + '\'' +
                        "}";
    }
}
