package com.example.labook2.model;

import java.util.List;

public class ResponseBerita {
    private List<BeritaItem> berita;

    public void setBerita(List<BeritaItem> berita){
        this.berita = berita;
    }

    public List<BeritaItem> getBerita(){
        return berita;
    }

    @Override
    public String toString(){
        return
                "ResponseBerita{" +
                        "berita = '" + berita + '\'' +
                        "}";
    }
}
