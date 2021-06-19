package com.example.labook2.model;

import com.google.gson.annotations.SerializedName;

public class LaboranItem {
    @SerializedName("id_laboran")
    private String id_laboran;

    @SerializedName("id_bidang")
    private String id_bidang;

    @SerializedName("hak_akses")
    private String hak_akses;

    @SerializedName("nama_laboran")
    private String nama_laboran;

    @SerializedName("alamat")
    private String alamat;

    @SerializedName("no_hp")
    private String no_hp;

    @SerializedName("foto_user")
    private String foto_user;

    @SerializedName("nama_lab")
    private String nama_lab;

    @SerializedName("alamat_lab")
    private String alamat_lab;

    @SerializedName("no_telp_lab")
    private String no_telp_lab;

    @SerializedName("id_laboratorium")
    private String id_laboratorium;

    @SerializedName("foto_lab")
    private String foto_lab;

    public void setId_laboran(String id_laboran){
        this.id_laboran = id_laboran;
    }

    public String getId_laboran(){
        return id_laboran;
    }

    public void setNama_laboran(String nama_laboran){
        this.nama_laboran = nama_laboran;
    }

    public String getNama_laboran(){
        return nama_laboran;
    }

    public void setFoto_user(String foto_user){
        this.foto_user = foto_user;
    }

    public String getFoto_user(){
        return foto_user;
    }

    public void setId_bidang(String id_bidang){
        this.id_bidang = id_bidang;
    }

    public String getId_bidang(){
        return id_bidang;
    }

    public void setHak_akses(String hak_akses){
        this.hak_akses = hak_akses;
    }

    public String getHak_akses(){
        return hak_akses;
    }

    public void setAlamat(String alamat){
        this.alamat = alamat;
    }

    public String getAlamat(){
        return alamat;
    }

    public void setNo_hp(String no_hp){
        this.no_hp = no_hp;
    }

    public String getNo_hp(){
        return no_hp;
    }

    public void setNama_lab(String nama_lab){
        this.nama_lab = nama_lab;
    }

    public String getNama_lab(){
        return nama_lab;
    }

    public void setAlamat_lab(String alamat_lab){
        this.alamat_lab = alamat_lab;
    }

    public String getAlamat_lab(){
        return alamat_lab;
    }

    public void setNo_telp_lab(String no_telp_lab){
        this.no_telp_lab = no_telp_lab;
    }

    public String getNo_telp_lab(){
        return no_telp_lab;
    }

    public void setId_laboratorium(String id_laboratorium){
        this.id_laboratorium = id_laboratorium;
    }

    public String getId_laboratorium(){
        return id_laboratorium;
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
                "LaboranItem{" +
                        "id_laboran = '" + id_laboran + '\'' +
                        "id_bidang = '" + id_bidang + '\'' +
                        "hak_akses = '" + hak_akses + '\'' +
                        "nama_laboran = '" + nama_laboran + '\'' +
                        "alamat = '" + alamat + '\'' +
                        "no_hp = '" + no_hp + '\'' +
                        "foto_user = '" + foto_user + '\'' +
                        "nama_lab = '" + nama_lab + '\'' +
                        "alamat_lab = '" + alamat_lab + '\'' +
                        "no_telp_lab = '" + no_telp_lab + '\'' +
                        "id_laboratorium = '" + id_laboratorium + '\'' +
                        "foto_lab = '" + foto_lab + '\'' +
                        "}";
    }
}
