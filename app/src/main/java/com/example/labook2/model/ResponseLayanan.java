package com.example.labook2.model;

import java.util.List;

public class ResponseLayanan {
    private List<LayananItem> layanan;

    public void setLayanan(List<LayananItem> layanan){
        this.layanan = layanan;
    }

    public List<LayananItem> getLayanan(){
        return layanan;
    }

    @Override
    public String toString(){
        return
                "ResponseLayanan{" +
                        "layanan = '" + layanan + '\'' +
                        "}";
    }
}
