package com.example.dell.augmentedreality;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Created by Dell on 8.3.2016.
 */
public class Lesson extends Activity {
    private Button biyoloji;
    private Button cografya;
    private Button kimya;
    private Button tarih;
    private Button geri;
    private String  title;
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.lesson);

            biyoloji=(Button) findViewById(R.id.biyoloji);
            cografya=(Button) findViewById(R.id.cografya);
            kimya=(Button) findViewById(R.id.kimya);
            tarih=(Button) findViewById(R.id.tarih);
            geri=(Button) findViewById(R.id.back);

            biyoloji.setOnClickListener(getContentListener);
            cografya.setOnClickListener(getContentListener);
            kimya.setOnClickListener(getContentListener);
            tarih.setOnClickListener(getContentListener);
            geri.setOnClickListener(getContentListener);
        }
    View.OnClickListener getContentListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(v.getId()== R.id.back){
                Intent intent = new Intent(Lesson.this, MainActivity.class);
                startActivity(intent);
            }else{
                Button b=(Button)v;
                title=b.getText().toString();
                if(title.equals("")){}
                else {
                    Intent i = getParent().getIntent();
                    i.putExtra("title", title);

                    TabActivity tabs = (TabActivity) getParent();
                    tabs.getTabHost().setCurrentTab(1);
                    Log.e("title", "" + title);
                }
            }
        }
    };
}
