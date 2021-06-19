package com.example.labook2.model;

import java.util.List;

public class ResponseBidang {
    private List<BidangItem> bidang;

    public void setBidang(List<BidangItem> bidang){
        this.bidang = bidang;
    }

    public List<BidangItem> getBidang(){
        return bidang;
    }

    @Override
    public String toString(){
        return
                "ResponseBidang{" +
                        "bidang = '" + bidang + '\'' +
                        "}";
    }
}
