package com.example.dell.augmentedreality;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;


public class Ders {
   static SharedPreferences preferences;
    private String ders_adi;
    private SharedPreferences savedDersler;
    private static SharedPreferences.Editor  preferenceEditorDersler;
    private static final int PREFERENCE_MODE_PRIVATE = 0;

    public static void setSharedPreferences(SharedPreferences pref)
    {
        preferences = pref;
    }

    public static String[] getDersListesi()
    {
        preferenceEditorDersler = preferences.edit();
        Set<String> Dersler =new HashSet<String>();

        if(preferences.getStringSet("Dersler", null) == null)
        {
            Dersler.add("Kimya");
            Dersler.add("Biyoloji");
            Dersler.add("Tarih");
            Dersler.add("Cografya");
            preferenceEditorDersler.putStringSet("Dersler", Dersler);
            preferenceEditorDersler.commit();

        }

        Set<String> tmp =new HashSet<String>();
        tmp =preferences.getStringSet("Dersler", null);

        String classes[] = new String[tmp.size()];
        classes = tmp.toArray(classes);
        Log.e("Ders","clası2");

        return classes;

    }

}
