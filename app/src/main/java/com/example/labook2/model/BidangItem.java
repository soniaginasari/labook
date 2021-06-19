package com.example.labook2.model;

import com.google.gson.annotations.SerializedName;

public class BidangItem {
    @SerializedName("id_bidang")
    private String id_bidang;

    @SerializedName("nama_bidang")
    private String nama_bidang;

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

    public void setId_laboratorium(String id_laboratorium){
        this.id_laboratorium = id_laboratorium;
    }

    public String getId_laboratorium(){
        return id_laboratorium;
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

        @Override
    public String toString(){
        return
                "BidangItem{" +
                        "id_bidang = '" + id_bidang + '\'' +
                        "nama_bidang = '" + nama_bidang + '\'' +
                        "id_laboratorium = '" + id_laboratorium + '\'' +
                        "nama_lab = '" + nama_lab + '\'' +
                        "alamat = '" + alamat + '\'' +
                        "no_telp = '" + no_telp + '\'' +
                        "foto_lab = '" + foto_lab + '\'' +
                        "}";
    }
}
