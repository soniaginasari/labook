package com.example.labook2.model;

import java.util.List;

public class ResponseSewa {
    private List<SewaItem> sewa;

    public void setSewa(List<SewaItem> sewa){
        this.sewa = sewa;
    }

    public List<SewaItem> getSewa(){
        return sewa;
    }

    public List<SewaItem> getSewaHistory(){
        return sewa;
    }

    @Override
    public String toString(){
        return
                "ResponseSewa{" +
                        "sewa = '" + sewa + '\'' +
                        "}";
    }
}
