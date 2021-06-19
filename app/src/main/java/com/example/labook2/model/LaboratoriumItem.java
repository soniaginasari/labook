package com.example.labook2.model;

import com.google.gson.annotations.SerializedName;

public class LaboratoriumItem {
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


    @Override
    public String toString(){
        return
                "LaboratoriumItem{" +
                        "id_laboratorium = '" + id_laboratorium + '\'' +
                        "nama_lab = '" + nama_lab + '\'' +
                        "alamat = '" + alamat + '\'' +
                        "no_telp = '" + no_telp + '\'' +
                        "foto_lab = '" + foto_lab + '\'' +
                        "}";
    }
}
