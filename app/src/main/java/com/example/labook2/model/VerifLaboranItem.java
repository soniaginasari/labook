package com.example.labook2.model;

import com.google.gson.annotations.SerializedName;

public class VerifLaboranItem {
    @SerializedName("id_verif_lab")
    private String id_verif_lab;

    @SerializedName("id_verif_bidang")
    private String id_verif_bidang;

    @SerializedName("id_verif_layanan")
    private String id_verif_layanan;

    @SerializedName("id_laboratorium")
    private String id_laboratorium;

    @SerializedName("nama_lab")
    private String nama_lab;

    @SerializedName("alamat")
    private String alamat;

    @SerializedName("no_telp")
    private String no_telp;

    @SerializedName("foto_lab")
    private String foto_lab;

    @SerializedName("ket_crud")
    private String ket_crud;

    @SerializedName("id_bidang")
    private String id_bidang;

    @SerializedName("nama_bidang")
    private String nama_bidang;

    @SerializedName("id_layanan")
    private String id_layanan;

    @SerializedName("nama_layanan")
    private String nama_layanan;

    @SerializedName("unit_satuan")
    private String unit_satuan;

    @SerializedName("satuan")
    private String satuan;

    @SerializedName("harga")
    private String harga;

    @SerializedName("keterangan")
    private String keterangan;

    public void setId_verif_lab(String id_verif_lab){
        this.id_verif_lab = id_verif_lab;
    }

    public String getId_verif_lab(){
        return id_verif_lab;
    }

    public void setId_verif_bidang(String id_verif_bidang){
        this.id_verif_bidang = id_verif_bidang;
    }

    public String getId_verif_bidang(){
        return id_verif_bidang;
    }

    public void setId_verif_layanan(String id_verif_layanan){
        this.id_verif_layanan = id_verif_layanan;
    }

    public String getId_verif_layanan(){
        return id_verif_layanan;
    }

    public void setId_laboratorium(String id_laboratorium){
        this.id_laboratorium = id_laboratorium;
    }

    public String getId_laboratorium(){
        return id_laboratorium;
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

    public void setKet_crud(String ket_crud){
        this.ket_crud = ket_crud;
    }

    public String getKet_crud(){
        return ket_crud;
    }

    public void setId_bidang(String id_bidang){
        this.id_bidang = id_bidang;
    }

    public String getId_bidang(){
        return id_bidang;
    }

    public void setNama_bidang(String nama_bidang){
        this.nama_bidang = nama_bidang;
    }

    public String getNama_bidang(){
        return nama_bidang;
    }

    public void setId_layanan(String id_layanan){
        this.id_layanan = id_layanan;
    }

    public String getId_layanan(){
        return id_layanan;
    }

    public void setNama_layanan(String nama_layanan){
        this.nama_layanan = nama_layanan;
    }

    public String getNama_layanan(){
        return nama_layanan;
    }

    public void setUnit_satuan(String unit_satuan){
        this.unit_satuan = unit_satuan;
    }

    public String getUnit_satuan(){
        return unit_satuan;
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

    public void setKeterangan(String keterangan){
        this.keterangan = keterangan;
    }

    public String getKeterangan(){
        return keterangan;
    }


    @Override
    public String toString(){
        return
                "VerifLaboranItem{" +
                        "id_verif_lab = '" + id_verif_lab + '\'' +
                        "id_verif_bidang = '" + id_verif_bidang + '\'' +
                        "id_verif_layanan = '" + id_verif_layanan + '\'' +
                        "id_laboratorium = '" + id_laboratorium + '\'' +
                        "nama_lab = '" + nama_lab + '\'' +
                        "alamat = '" + alamat + '\'' +
                        "no_telp = '" + no_telp + '\'' +
                        "foto_lab = '" + foto_lab + '\'' +
                        "id_bidang = '" + id_bidang + '\'' +
                        "nama_bidang = '" + nama_bidang + '\'' +
                        "id_layanan = '" + id_layanan + '\'' +
                        "nama_layanan = '" + nama_layanan + '\'' +
                        "unit_satuan = '" + unit_satuan + '\'' +
                        "satuan = '" + satuan + '\'' +
                        "harga = '" + harga + '\'' +
                        "keterangan = '" + keterangan + '\'' +
                        "ket_crud = '" + ket_crud + '\'' +
                        "}";
    }
}
