package com.example.dell.augmentedreality;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Add extends AppCompatActivity {

    private SharedPreferences savedDersler;
    private static final int PREFERENCE_MODE_PRIVATE = 0;
    private SharedPreferences savedSiniflar;
    private SharedPreferences savedKonular;

    String[] dersListesi;
    String[] sinifListesi;
    String[] konuListesi;

    private RadioGroup radioGroup;
    private RadioButton radioChoise;
    private Spinner dersSpin;
    private Spinner sınıfSpin;
    private Spinner konuSpin;
    private Button nextButton;

    private ArrayList<Reindeer> populateReindeer()
    {


        final ArrayList<Reindeer> deer = new ArrayList<Reindeer>();

        deer.add(new Reindeer(dersListesi[0]));
        deer.add(new Reindeer(dersListesi[1]));
        deer.add(new Reindeer(dersListesi[2]));
        deer.add(new Reindeer(dersListesi[3]));

        return deer;
    }
    private ArrayList<Reindeer> populateReindeerTwo()
    {
        final ArrayList<Reindeer> deer = new ArrayList<Reindeer>();

        deer.add(new Reindeer(sinifListesi[0]));
        deer.add(new Reindeer(sinifListesi[1]));
        deer.add(new Reindeer(sinifListesi[2]));
        deer.add(new Reindeer(sinifListesi[3]));

        return deer;
    }
    private ArrayList<Reindeer> populateReindeerThree()
    {
        final ArrayList<Reindeer> deer = new ArrayList<Reindeer>();

        deer.add(new Reindeer(konuListesi[0]));
        deer.add(new Reindeer(konuListesi[1]));
        deer.add(new Reindeer(konuListesi[2]));
        deer.add(new Reindeer(konuListesi[3]));

        return deer;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);

        radioGroup = (RadioGroup) findViewById(R.id.radio);
        nextButton =(Button) findViewById(R.id.ileriButton);
        dersSpin=(Spinner) findViewById(R.id.dersSpin);
        sınıfSpin=(Spinner) findViewById(R.id.sınıfSpin);
        konuSpin=(Spinner) findViewById(R.id.konuSpin);

        savedDersler = getSharedPreferences("Dersler", PREFERENCE_MODE_PRIVATE);
        DerseResimEkle.setSharedPreferences(savedDersler);
        dersListesi = DerseResimEkle.getDersListesi();

        CustomAdapter adapter = new CustomAdapter(this, android.R.layout.simple_spinner_item, populateReindeer());
        dersSpin.setAdapter(adapter);

        dersSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sid = dersSpin.getSelectedItem().toString();
                Toast.makeText(getBaseContext(), "Seçiminiz : " + sid, Toast.LENGTH_LONG).show();
                try {
                    savedSiniflar = getSharedPreferences("Siniflar", PREFERENCE_MODE_PRIVATE);
                    DerseResimEkle.setSharedPreferencesSinif(savedSiniflar);
                    sinifListesi = DerseResimEkle.getSiniflistesi(sid);
                    Arrays.sort(sinifListesi);
                    sinifListesi[0]="9.Sınıf";
                    CustomAdapter adapter1 = new CustomAdapter(Add.this, android.R.layout.simple_spinner_item, populateReindeerTwo());
                    sınıfSpin.setAdapter(adapter1);
                    sınıfSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String sid = dersSpin.getSelectedItem().toString();
                            String Sinifid = sınıfSpin.getSelectedItem().toString();
                            Toast.makeText(getBaseContext(), "Seçiminiz : " + Sinifid, Toast.LENGTH_SHORT).show();
                            try {

                                savedKonular = getSharedPreferences("Konular", PREFERENCE_MODE_PRIVATE);
                                DerseResimEkle.setSharedPreferencesKonu(savedKonular);
                                konuListesi = DerseResimEkle.getKonuListesi(sid, Sinifid);
                                Arrays.sort(konuListesi);
                                Log.e("asd",sid+"/"+Sinifid+"/"+konuListesi[0]+"/"+konuListesi[1]+"/"+konuListesi[2]+"/"+konuListesi[3]);
                                CustomAdapter adapter2 = new CustomAdapter(Add.this, android.R.layout.simple_spinner_item, populateReindeerThree());
                                konuSpin.setAdapter(adapter2);


                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nextButton.setOnClickListener(getItemListener);
    }
    View.OnClickListener getItemListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!String.valueOf(dersSpin.getSelectedItem()).equals("Ders") && !String.valueOf(sınıfSpin.getSelectedItem()).equals("Sınıf")
                    && !String.valueOf(konuSpin.getSelectedItem()).equals("Konu")) {
                makeFile(String.valueOf(dersSpin.getSelectedItem()),
                        String.valueOf(sınıfSpin.getSelectedItem()),
                        String.valueOf(konuSpin.getSelectedItem()));

            String title=String.valueOf(dersSpin.getSelectedItem())+ File.separator+
                    String.valueOf(sınıfSpin.getSelectedItem())+File.separator+
                    String.valueOf(konuSpin.getSelectedItem());

            int selectId= radioGroup.getCheckedRadioButtonId();
            radioChoise=(RadioButton) findViewById(selectId);

            if(radioChoise.getText().equals("Kamera")){
                Log.e("Choice", "Kamera Secildi->" + title);
                Intent inten = new Intent(Add.this, Kamera.class);
                inten.putExtra("title",title);
                startActivity(inten);
            }else if(radioChoise.getText().equals("SD Kart")){
                Intent intent = new Intent(Add.this, SdCard.class);
                intent.putExtra("title",title);
                startActivity(intent);
            }
         }
        }
    };
    private void makeFile(String ders, String sınıf, String konu){
        File mFile = new File(Environment.getExternalStorageDirectory().toString()
        +File.separator+"ImageFile");
        if(!mFile.exists())
            mFile.mkdir();

        mFile = new File(mFile.toString(),ders);
        if(!mFile.exists()){
            mFile.mkdir();
            File nFile=new File(mFile.toString(),sınıf);
            if (!nFile.exists()){
                nFile.mkdir();
                mFile=new File(nFile.toString(),konu);
                if(!mFile.exists())
                    mFile.mkdir();
            }
        }else{
            File nFile=new File(mFile.toString(),sınıf);
            if(!nFile.exists()){
                Log.e("asdas","asdasfasd");
                nFile.mkdir();
                mFile=new File(nFile.toString(),konu);
                if(!mFile.exists()){
                    mFile.mkdir();
                }
            }else {
                mFile=new File(nFile.toString(),konu);
                if(!mFile.exists())
                    mFile.mkdir();
            }
        }
    }
}
class Reindeer{
    private String name;

    Reindeer(String name){

        this.name = name;


    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {

        return getName();

    }

}
class CustomAdapter extends ArrayAdapter<Reindeer> {

    private Activity context;
    ArrayList<Reindeer> deer;

    public CustomAdapter(Activity context, int resource, ArrayList<Reindeer> deer) {

        super(context, resource, deer);
        this.context = context;
        this.deer = deer;

    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        View row = convertView;

        if (row == null) {

            LayoutInflater inflater = context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_row, parent, false);
        }
        Reindeer current = deer.get(position);

        TextView name = (TextView) row.findViewById(R.id.spinnerText);
        name.setText(current.getName());

        return row;
    }

}
