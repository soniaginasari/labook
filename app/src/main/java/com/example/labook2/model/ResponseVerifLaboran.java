package com.example.labook2.model;

import java.util.List;

public class ResponseVerifLaboran {
    private List<VerifLaboranItem> verif_laboran;

    public void setVerif_laboran(List<VerifLaboranItem> verif_laboran){
        this.verif_laboran = verif_laboran;
    }

    public List<VerifLaboranItem> getVerif_laboran(){
        return verif_laboran;
    }

    @Override
    public String toString(){
        return
                "ResponseVerifLaboran{" +
                        "verif_laboran = '" + verif_laboran + '\'' +
                        "}";
    }
}
