package com.example.dell.augmentedreality;

import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class Sinif {
    static SharedPreferences preferences;
    private static SharedPreferences.Editor preferenceEditorSiniflar;

    public static void setSharedPreferences(SharedPreferences pref) {

        preferences = pref;
    }

    public static String[] getSinifListesi(String ders) {

        preferenceEditorSiniflar = preferences.edit();
        //preferenceEditorDersler.putStringSet("Dersler", Dersler);
        //preferenceEditorDersler.commit();
       Set<String> Siniflar = new HashSet<String>() {
       };

        if (preferences.getStringSet("Siniflar", null) == null) {
            //tv2.setText(savedDersler.getStringSet("Dersler",null).toString());
            Siniflar.add("Kimya/09.Sınıf");
            Siniflar.add("Kimya/10.Sınıf");
            Siniflar.add("Kimya/11.Sınıf");
            Siniflar.add("Kimya/12.Sınıf");
            Siniflar.add("Biyoloji/09.Sınıf");
            Siniflar.add("Biyoloji/10.Sınıf");
            Siniflar.add("Biyoloji/11.Sınıf");
            Siniflar.add("Biyoloji/12.Sınıf");
            Siniflar.add("Tarih/09.Sınıf");
            Siniflar.add("Tarih/10.Sınıf");
            Siniflar.add("Tarih/11.Sınıf");
            Siniflar.add("Tarih/12.Sınıf");
            Siniflar.add("Cografya/09.Sınıf");
            Siniflar.add("Cografya/10.Sınıf");
            Siniflar.add("Cografya/11.Sınıf");
            Siniflar.add("Cografya/12.Sınıf");
            preferenceEditorSiniflar.putStringSet("Siniflar",  Siniflar);
            preferenceEditorSiniflar.commit();
        }
        Set<String> tmp =new HashSet<String>();
        tmp =preferences.getStringSet("Siniflar", null);

        String temp[] = new String[tmp.size()];
        temp = tmp.toArray(temp);
        int j =0;
        String sinif[]= new String[4];
        for(int i=0 ; i<temp.length;i++)
            if(temp[i].contains(ders+"/")){
                String[] parts = temp[i].split("/");
                sinif[j]=parts[1];j++;}
        return sinif;

    }
}
