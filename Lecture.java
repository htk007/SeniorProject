package com.example.dell.augmentedreality;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Dell on 8.3.2016.
 */
public class Lecture extends AppCompatActivity {
    String classes;

    String[] konuListesi;
    private SharedPreferences konular;
    private LinearLayout layout;

    private Button geri;
    private String title;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lecture);
        Intent i = getParent().getIntent();
        classes=i.getStringExtra("title");

        Log.e("Lecture", "Lecture clası girdi");
        layout=(LinearLayout) findViewById(R.id.linearLayout);
        geri=(Button) findViewById(R.id.back);
        geri.setOnClickListener(getContentListener);

        refresh();
    }
    public void refresh()
    {
        String [] bol=classes.split("/");
        konular = getSharedPreferences("Konular", 0);
        DerseResimEkle.setSharedPreferencesKonu(konular);
        konuListesi=DerseResimEkle.getKonuListesi(bol[0], bol[bol.length-1]);

        for(int j=0;j<konuListesi.length;j++){
            addTagGUI(konuListesi[j],j);
        }
    }
    private void addTagGUI(String title,int index)
    {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newTagView = layoutInflater.inflate(R.layout.new_tag_view, null);

        Button newTagButton = (Button) newTagView.findViewById(R.id.konu1);
        newTagButton.setText(title);
        newTagButton.setOnClickListener(getContentListener);

        layout.addView(newTagView, index);
    }
    View.OnClickListener getContentListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v.getId()== R.id.back){
                Intent intent = new Intent(Lecture.this, MainActivity.class);
                startActivity(intent);
            }else{
                Button b=(Button)v;
                title=classes+"/"+b.getText().toString();
                Intent inten = new Intent(Lecture.this,LoadCamera.class);
                inten.putExtra("title",title);
                startActivity(inten);
            }
        }
    };
}
