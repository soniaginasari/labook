package com.example.labook2.model;

import java.util.List;

public class ResponseNotifikasi {
    private List<NotifikasiItem> notifikasi;

    public void setNotifikasi(List<NotifikasiItem> notifikasi){
        this.notifikasi = notifikasi;
    }

    public List<NotifikasiItem> getNotifikasi(){
        return notifikasi;
    }

//    public List<SewaItem> getSewaHistory(){
//        return sewa;
//    }

    @Override
    public String toString(){
        return
                "ResponseNotifikasi{" +
                        "notifikasi = '" + notifikasi + '\'' +
                        "}";
    }
}
