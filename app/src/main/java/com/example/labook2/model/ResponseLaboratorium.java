package com.example.labook2.model;

import java.util.List;

public class ResponseLaboratorium {
    private List<LaboratoriumItem> laboratorium;

    public void setLaboratorium(List<LaboratoriumItem> laboratorium){
        this.laboratorium = laboratorium;
    }

    public List<LaboratoriumItem> getLaboratorium(){
        return laboratorium;
    }

    @Override
    public String toString(){
        return
                "ResponseLaboratorium{" +
                        "laboratorium = '" + laboratorium + '\'' +
                        "}";
    }
}
