package com.example.dell.augmentedreality;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.opencv.android.OpenCVLoader;


public class MainActivity extends AppCompatActivity {
    private Button menuButton;
    private Button ekleButton;
    static {
        if(!OpenCVLoader.initDebug())
        {
            Log.e("Opencv","Opencv failed");
        }else{
            Log.e("Opencv","Opencv success");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuButton=(Button)findViewById(R.id.menuButton);
        ekleButton=(Button)findViewById(R.id.ekleButton);

        menuButton.setOnClickListener(choiceListener);
        ekleButton.setOnClickListener(choiceListener);
    }
    View.OnClickListener choiceListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.menuButton:
                    Intent intent1 = new Intent(MainActivity.this, Menu.class);
                    startActivity(intent1);
                    break;
                case  R.id.ekleButton:
                    Intent intent = new Intent(MainActivity.this, Add.class);
                    startActivity(intent);
                    break;
            }
        }
    };
}
