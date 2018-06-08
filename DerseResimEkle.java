package com.example.dell.augmentedreality;

import android.content.SharedPreferences;
import android.util.Log;


public class DerseResimEkle {
    static SharedPreferences derspreferences;
    static SharedPreferences sinifpreferences;
    static SharedPreferences konupreferences;
    public static void setSharedPreferences(SharedPreferences pref)
    {
        derspreferences = pref;
    }

    public static String[] getDersListesi()
    {
        Ders.setSharedPreferences(derspreferences);
        Log.e("Ders", "clası");
        return Ders.getDersListesi();

    }
    public static void setSharedPreferencesSinif(SharedPreferences pref)
    {
        sinifpreferences = pref;
    }

    public  static  String [] getSiniflistesi(String ders){
        Sinif.setSharedPreferences(sinifpreferences);
        return Sinif.getSinifListesi(ders);
    }

    public static void setSharedPreferencesKonu(SharedPreferences pref)
    {
        konupreferences= pref;
    }

    public  static  String [] getKonuListesi(String ders,String sinif){
        Konu.setSharedPreferences(konupreferences);
        return Konu.getKonuListesi(ders, sinif);
    }
}
