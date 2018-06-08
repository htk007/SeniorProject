package com.example.dell.augmentedreality;

import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class Konu {

    static SharedPreferences preferences;
    private static SharedPreferences.Editor preferenceEditorKonular;

    public static void setSharedPreferences(SharedPreferences pref) {

        preferences = pref;
    }

    public static String[] getKonuListesi(String ders,String sinif) {

        preferenceEditorKonular = preferences.edit();
        Set<String> Konular = new HashSet<String>() {
        };

        if (preferences.getStringSet("Konular", null) == null) {

            Konular.add("Kimya/9.Sınıf/Kimyanın Gelişimi");
            Konular.add("Kimya/9.Sınıf/Bileşikler");
            Konular.add("Kimya/9.Sınıf/Hayatımızda Kimya");
            Konular.add("Kimya/9.Sınıf/Periyodik Tablo");
            Konular.add("Kimya/10.Sınıf/Hidrokarbonlar");
            Konular.add("Kimya/10.Sınıf/Fonksiyonel Gruplar");
            Konular.add("Kimya/10.Sınıf/Çözeltiler");
            Konular.add("Kimya/10.Sınıf/Atom Yapısı");
            Konular.add("Kimya/11.Sınıf/Elektrokimya");
            Konular.add("Kimya/11.Sınıf/Radyoaktivite");
            Konular.add("Kimya/11.Sınıf/Asit-Baz-Tuz");
            Konular.add("Kimya/11.Sınıf/Reaksiyon Hızı");
            Konular.add("Kimya/12.Sınıf/Organik Kimya");
            Konular.add("Kimya/12.Sınıf/Moleküler Geometri");
            Konular.add("Kimya/12.Sınıf/Hibritleşme");
            Konular.add("Kimya/12.Sınıf/Tepkime Çeşitleri");
            Konular.add("Biyoloji/9.Sınıf/Bakteri");
            Konular.add("Biyoloji/9.Sınıf/Bitki");
            Konular.add("Biyoloji/9.Sınıf/Hayvan");
            Konular.add("Biyoloji/9.Sınıf/Hücre");
            Konular.add("Biyoloji/10.Sınıf/Kalıtım");
            Konular.add("Biyoloji/10.Sınıf/Üreme");
            Konular.add("Biyoloji/10.Sınıf/Ekoloji");
            Konular.add("Biyoloji/10.Sınıf/Madde Döngüsü");
            Konular.add("Biyoloji/11.Sınıf/Fotosentez");
            Konular.add("Biyoloji/11.Sınıf/Dokular");
            Konular.add("Biyoloji/11.Sınıf/Duyu Organları");
            Konular.add("Biyoloji/11.Sınıf/Sistemler");
            Konular.add("Biyoloji/12.Sınıf/Evrim");
            Konular.add("Biyoloji/12.Sınıf/Ekoloji");
            Konular.add("Biyoloji/12.Sınıf/Davranış");
            Konular.add("Biyoloji/12.Sınıf/Genetik");
            Konular.add("Cografya/9.Sınıf/Harita Bilgisi");
            Konular.add("Cografya/9.Sınıf/İklim Bilgisi");
            Konular.add("Cografya/9.Sınıf/İç Kuvvetler");
            Konular.add("Cografya/9.Sınıf/Dış Kuvvetler");
            Konular.add("Cografya/10.Sınıf/Akarsular ve Barajlar");
            Konular.add("Cografya/10.Sınıf/Çöl Çeşitleri");
            Konular.add("Cografya/10.Sınıf/Ekonomik Cografya");
            Konular.add("Cografya/10.Sınıf/Enerji Çesitleri");
            Konular.add("Cografya/11.Sınıf/Beseri Sistemler");
            Konular.add("Cografya/11.Sınıf/Doğal Sistemler");
            Konular.add("Cografya/11.Sınıf/Çevre Ve Toplum");
            Konular.add("Cografya/11.Sınıf/Bölgeler ve Ülkeler");
            Konular.add("Cografya/12.Sınıf/Turizm Degerleri");
            Konular.add("Cografya/12.Sınıf/Bolgesel Planlar");
            Konular.add("Cografya/12.Sınıf/UlaSım Sistemleri");
            Konular.add("Cografya/12.Sınıf/Doga etkileşimi");
            Konular.add("Tarih/9.Sınıf/Tarihe Giris");
            Konular.add("Tarih/9.Sınıf/Tarihi Caglar");
            Konular.add("Tarih/9.Sınıf/Uygarlıklar");
            Konular.add("Tarih/9.Sınıf/Türk Devletleri");
            Konular.add("Tarih/10.Sınıf/Osmanlı Devleti Kurulus");
            Konular.add("Tarih/10.Sınıf/İstanbul'un Fethi");
            Konular.add("Tarih/10.Sınıf/Osmanlı Ordusu");
            Konular.add("Tarih/10.Sınıf/Kanuni Dönemi");
            Konular.add("Tarih/11.Sınıf/Kurtulus Savası");
            Konular.add("Tarih/11.Sınıf/Saltanat Kaldırılması");
            Konular.add("Tarih/11.Sınıf/Ataturk Ilkeleri");
            Konular.add("Tarih/11.Sınıf/Dıs Politika");
            Konular.add("Tarih/12.Sınıf/1.Dünya Savası");
            Konular.add("Tarih/12.Sınıf/2.Dünya Savası");
            Konular.add("Tarih/12.Sınıf/Soguk Savaslar");
            Konular.add("Tarih/12.Sınıf/Kuresellesen Dunya");
            preferenceEditorKonular.putStringSet("Konular",  Konular);
            preferenceEditorKonular.commit();
        }
        Set<String> tmp =new HashSet<String>();
        tmp =preferences.getStringSet("Konular", null);

        String temp[] = new String[tmp.size()];
        temp = tmp.toArray(temp);
        int j =0;

        String konu[]= new String[4];
        for(int i=0 ; i<temp.length;i++)
            if(temp[i].contains(ders+"/"+sinif+"/")){
                String[] parts = temp[i].split("/");
                konu[j]=parts[2];j++;}
        return konu;

    }
}
