package com.example.labook2.model;

import java.util.List;

public class ResponseLaboran {
    private List<LaboranItem> laboran;

    public void setLaboran(List<LaboranItem> laboran){
        this.laboran = laboran;
    }

    public List<LaboranItem> getLaboran(){
        return laboran;
    }

    @Override
    public String toString(){
        return
                "ResponseLaboran{" +
                        "laboran = '" + laboran + '\'' +
                        "}";
    }
}
