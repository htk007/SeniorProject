package com.example.dell.augmentedreality;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SdCard extends AppCompatActivity {
    private ArrayList<String> array=new ArrayList<String>();
    private String secim;
    private int choice;
    int kamera;
    private Button resimSec;
    private Button videoSec;
    private Button sesSec;
    private Button ucboyutluSec;
    private Button yukleButton;
    private TextView resimtextView;
    private TextView videotextView;
    private TextView sestextView;
    private TextView ucdmodeltextView;

    private ArrayList<File> fileList = new ArrayList<File>();

    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sd_card);

        resimSec=(Button) findViewById(R.id.resimsec);
        videoSec=(Button) findViewById(R.id.videosec);
        sesSec=(Button) findViewById(R.id.sessec);
        ucboyutluSec=(Button) findViewById(R.id.ucdmodelsec);
        resimtextView=(TextView) findViewById(R.id.resimtextView);
        videotextView=(TextView) findViewById(R.id.videotextView);
        sestextView=(TextView) findViewById(R.id.sestextView);
        ucdmodeltextView=(TextView) findViewById(R.id.ucdmodeltextView);
        yukleButton=(Button) findViewById(R.id.yukleButton);


        resimSec.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {

                // in onCreate or any event where your want the user to
                // select a file
                Intent intent = new Intent();
                intent.setType("image/png");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
                choice = 0;
                choice += 1;
            }
        });

        videoSec.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {

                        // in onCreate or any event where your want the user to
                        // select a file
                        Intent intent = new Intent();
                        intent.setType("video/mp4");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,
                                "Select Video"), SELECT_PICTURE);
                        choice=0;
                        choice+=2;
                    }
                });


        sesSec.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View arg0) {

                        // in onCreate or any event where your want the user to
                        // select a file
                        Intent intent = new Intent();
                        intent.setType("audio/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,
                                "Select Audio"), SELECT_PICTURE);
                        choice=0;
                        choice+=3;
                    }
                });

                ucboyutluSec.setOnClickListener(new View.OnClickListener() {

                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                    public void onClick(View arg0) {

                        fileList.clear();
                       File root = new File(Environment.getExternalStorageDirectory()
                                .getAbsolutePath());

                        getfile(root);

                        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View textFile = layoutInflater.inflate(R.layout.textfile, null, false);
                        LinearLayout user = (LinearLayout) textFile.findViewById(R.id.view);
                        for (int i = 0; i < fileList.size(); i++) {
                            if (!fileList.get(i).isDirectory()) {
                                final Button textView = new Button(SdCard.this);
                                textView.setText(fileList.get(i).getName());
                                textView.setPadding(5, 5, 5, 5);
                                user.addView(textView);
                                final int finalI = i;
                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Button b=(Button)v;
                                        write(String.valueOf(fileList.get(finalI)));

                                    }
                                });
                            }

                        }
                        new AlertDialog.Builder(SdCard.this).setView(textFile).show();
                        choice=0;
                        choice+=4;

                    }
                });

        final Bundle getintent=getIntent().getExtras();
        if(getintent.size() == 2){
            File path =new File(Environment.getExternalStorageDirectory().toString()+File.separator
                    +"ImageFile"+File.separator+getintent.get("title"));
            secim=path.toString();
            kamera=getintent.getInt("kamera");
            resimSec.setEnabled(false);
        }
        if(getintent.size()== 1)
        {
            File path =new File(Environment.getExternalStorageDirectory().toString()+File.separator
                    +"ImageFile"+File.separator+getintent.get("title"));
            secim=path.toString();
        }

        yukleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<array.size();i++){
                    String part[]= array.get(i).split("/");
                    copyFile(array.get(i), part[part.length - 1], secim);
                    Toast.makeText(SdCard.this, "İşleminiz Başarıyla Kaydedildi.", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SdCard.this,MainActivity.class);
                    startActivity(intent);
                }

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                write(selectedImagePath);
            }
        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }
    private void write(String path)
    {
        switch (choice){
            case 1:array.add(path);
                String part[]=path.split("/");
                resimtextView.setText(part[part.length - 1]);
                break;
            case 2:array.add(path);
                part = path.split("/");
                videotextView.setText(part[part.length - 1]);
                break;
            case 3:array.add(path);
                part = path.split("/");
                sestextView.setText(part[part.length - 1]);
                break;
            case 4:array.add(path);
                part = path.split("/");
                ucdmodeltextView.setText(part[part.length - 1]);
                break;
        }
    }

    private void copyFile(String inputPath, String inputFile, String outputPath)
    {
        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File (outputPath);
            if (!dir.exists())
            {
                dir.mkdirs();
            }

            in = new FileInputStream(inputPath);
            out = new FileOutputStream(outputPath+"/"+ inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        }  catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }
    public ArrayList<File> getfile(File dir) {

        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    //fileList.add(listFile[i]);
                    getfile(listFile[i]);

                } else {
                    if (listFile[i].getName().endsWith(".obj"))

                    {
                        String files=dir.listFiles().toString();
                        String [] part=files.split("/");
                        files="";
                        for (int j=1;j<part.length;j++){
                            files+=part[i];
                        }
                        File mFile=new File(files, listFile[i].toString());
                        fileList.add(mFile);
                    }
                }

            }
        }
        return fileList;
    }
}