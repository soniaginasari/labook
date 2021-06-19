package com.example.labook2.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseUser {
    @SerializedName("semuauser")
    private List<UserItem> semuauser;

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public void setSemuauser(List<UserItem> semuauser){
        this.semuauser = semuauser;
    }

    public List<UserItem> getSemuaUser(){
        return semuauser;
    }

    public void setError(boolean error){
        this.error = error;
    }

    public boolean isError(){
        return error;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    @Override
    public String toString(){
        return
                "ResponseUser{" +
                        "semuauser = '" + semuauser + '\'' +
                        ",error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}
