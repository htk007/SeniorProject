package com.example.dell.augmentedreality;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;
import java.io.File;;

/**
 * Created by Dell on 6.3.2016.
 */
public class Kamera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    int size;
    String choice;
    private Mat picture=null;
    private MatOfKeyPoint keypoints;
    private static final String TAG = "OCVSample::Activity";

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("OPENCV", "Open Load Succesfully");
                    mOpenCvCameraView.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };
    private JavaCameraView mOpenCvCameraView;
    private ImageButton ımageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kamera);
        if(!OpenCVLoader.initDebug()){
            Log.e("Hata","Opencv init hatası");
        }
        ımageButton=(ImageButton)findViewById(R.id.picture);
        ımageButton.setOnClickListener(ımageListener);

        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.HelloOpenCvView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

    }


    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {

    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {

        int detectorType = FeatureDetector.ORB;
        FeatureDetector detector = FeatureDetector.create(detectorType);

        Mat img1 = inputFrame.rgba();
        if(img1== null)
        {
            Log.e("İmage","İmage Okunamadı");
        }else {
            Log.e("İmage", "İmage Okundu");
        }

        keypoints = new MatOfKeyPoint();
        detector.detect(img1, keypoints); //noktalar bulur


        String part []=(String.valueOf(keypoints.size()).split("x"));
        size=Integer.parseInt(part[1]);

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                editButton(size);
            }
        });


        Mat outputImage = null;
        if (img1 != null) {
            outputImage = img1.clone();
            picture= inputFrame.rgba();
        }
        Scalar color = new Scalar(255, 0, 255, 1); // BGR
        Imgproc.cvtColor(img1, outputImage, Imgproc.COLOR_RGBA2mRGBA, 4);//kaynak görüntü matrisi ile hedef görüntü matrısi arasında renk dönüşümü yapılır
        Features2d.drawKeypoints(outputImage, keypoints, outputImage, color, 3);// olusturulan hedef görüntü matrisine bulunan bulunan özelliklerin cizimi yapılır.

        Imgproc.cvtColor(img1, picture, Imgproc.COLOR_RGBA2mRGBA, 4);

        return outputImage;
    }

    private void  editButton(int size)  {
        if(size< 150){
            ımageButton.setBackgroundColor(Color.parseColor("#FF0000"));
            ımageButton.invalidate();
            ımageButton.setEnabled(false);
        }
        else if (size>150 && size<350){
            ımageButton.setBackgroundColor(Color.parseColor("#FFFF00"));
            ımageButton.invalidate();
            ımageButton.setEnabled(true);
        }
        else{
            ımageButton.setBackgroundColor(Color.parseColor("#00FF00"));
            ımageButton.invalidate();
            ımageButton.setEnabled(true);
        }
    }
    View.OnClickListener ımageListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            takePicture(picture);
        }
    };

    private void takePicture(Mat image){
        final Bundle getintent=getIntent().getExtras();
        choice=getintent.getString("title");

        Mat mIntermediateMat = new Mat();
        Imgproc.cvtColor(image, mIntermediateMat, Imgproc.COLOR_RGBA2BGR, 3);

        File path =new File(Environment.getExternalStorageDirectory().toString()+File.separator
                +"ImageFile"+File.separator+choice);
        Log.e("sadas", "" + path);
        String filename=((path.list().length)+1)+".png";
        File file = new File(path, filename);
        filename = file.toString();

        Imgcodecs.imwrite(filename, mIntermediateMat);

        Intent inten=new Intent(Kamera.this,SdCard.class);
        inten.putExtra("title",choice);
        inten.putExtra("kamera",1);
        startActivity(inten);
    }
}



