package com.example.labook2;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    public static final String SP_BOOK_APP = "spBookApp";

    public static final String SP_ID = "spId";
    public static final String SP_NAMA = "spNama";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_TELEPON = "spTelepon";
    public static final String SP_ALAMAT = "spAlamat";
    public static final String SP_TOKEN = "spToken";
    public static final String SP_PICT = "spPict";
    public static final String SP_AKSES = "spAkses";
    public static final String FCM_TOKEN = "fcmToken";

    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public Preferences(Context context){
        sp = context.getSharedPreferences(SP_BOOK_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSPId(){
        return sp.getString(SP_ID, "");
    }

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }

    public String getSPEmail(){
        return sp.getString(SP_EMAIL, "");
    }

    public String getSPToken(){
        return sp.getString(SP_TOKEN, "");
    }

    public String getSPTelepon() {
        return sp.getString(SP_TELEPON, "");
    }

    public String getSPAlamat() {
        return sp.getString(SP_ALAMAT, "");
    }

    public String getSPPict(){
        return sp.getString(SP_PICT, "");
    }

    public String getSPAkses(){
        return sp.getString(SP_AKSES, "");
    }

    public  String getFCMToken() {
        return sp.getString(FCM_TOKEN,"");
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }
}