package com.example.dell.augmentedreality;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Dell on 8.3.2016.
 */
public class Classes extends AppCompatActivity {

    private Button sınıfDokuz;
    private Button sınıfOn;
    private Button sınıfOnBir;
    private Button sınıfOnIki;
    private Button geri;
    private String  title;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classes);

        sınıfDokuz=(Button) findViewById(R.id.dokuz);
        sınıfOn=(Button) findViewById(R.id.on);
        sınıfOnBir=(Button) findViewById(R.id.onbir);
        sınıfOnIki=(Button) findViewById(R.id.oniki);
        geri=(Button) findViewById(R.id.back);


        sınıfDokuz.setOnClickListener(getContentListener);
        sınıfOn.setOnClickListener(getContentListener);
        sınıfOnBir.setOnClickListener(getContentListener);
        sınıfOnIki.setOnClickListener(getContentListener);
        geri.setOnClickListener(getContentListener);

    }
    View.OnClickListener getContentListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v.getId()== R.id.back){
                Intent intent = new Intent(Classes.this, MainActivity.class);
                startActivity(intent);
            }else{
                Button b=(Button)v;

                Intent i = getParent().getIntent();
                String lesson = i.getStringExtra("title");
                title=lesson+"/"+b.getText().toString();
                i.putExtra("title",title);

                TabActivity tabs = (TabActivity) getParent();
                tabs.getTabHost().setCurrentTab(2);
                Log.e("title", "" + title);
            }
        }
    };
}
