package com.example.labook2.model;

import com.google.gson.annotations.SerializedName;

public class UserItem {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("alamat")
    private String alamat;

    @SerializedName("no_hp")
    private String no_hp;

    @SerializedName("hak_akses")
    private String hak_akses;

    @SerializedName("foto_user")
    private String foto_user;

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setFoto_user(String foto_user){
        this.foto_user = foto_user;
    }

    public String getFoto_user(){
        return foto_user;
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

    @Override
    public String toString(){
        return
                "LaboranItem{" +
                        "id = '" + id + '\'' +
                        "hak_akses = '" + hak_akses + '\'' +
                        "name = '" + name + '\'' +
                        "alamat = '" + alamat + '\'' +
                        "no_hp = '" + no_hp + '\'' +
                        "foto_user = '" + foto_user + '\'' +
                        "}";
    }
}
