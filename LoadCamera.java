package com.example.dell.augmentedreality;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class LoadCamera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 , SurfaceHolder.Callback {
    private String loadImge="";
    private String loadVideo="";
    private String loadVoice="";
    private String loadModel="";

    private MediaPlayer mediaPlayer;
    private SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    private TouchSurfaceView mGLSurfaceView;
    private Mat temp = new Mat();
    FeatureDetector Orbdetector = FeatureDetector.create(FeatureDetector.ORB);
    DescriptorExtractor OrbExtractor = DescriptorExtractor.create(DescriptorExtractor.ORB);
    DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_HAMMING);

    MatOfByte keypointsStatus = new MatOfByte();
    MatOfFloat err = new MatOfFloat();
    MatOfPoint2f nextpoints = new MatOfPoint2f();
    MatOfPoint2f prevpoint = new MatOfPoint2f();
    MatOfKeyPoint nextKeyp = new MatOfKeyPoint();

    private ArrayList<String> file = new ArrayList<String>();
    private Mat TemplateImage;
    private Mat InputImage = null;
    private LinkedList<DMatch> good_matches = new LinkedList<DMatch>();

    private MatOfKeyPoint keypoints_scene;
    private MatOfKeyPoint keypoints_object;
    private Mat descriptor_scene;
    private Mat descriptor_object;
    MatOfPoint2f obj = new MatOfPoint2f();
    MatOfPoint2f scene = new MatOfPoint2f();

    Mat prevImage = new Mat();
    MatOfPoint2f prevImageKeyp = new MatOfPoint2f();
    String choice;

    private JavaCameraView loadCvCameraView;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i("OPENCV", "Open Load Succesfully");
                    loadCvCameraView.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load);
        if (!OpenCVLoader.initDebug()) {
            Log.e("Hata", "Opencv init hatası");
        }

        loadCvCameraView = (JavaCameraView) findViewById(R.id.loadCvView);
        loadCvCameraView.setVisibility(SurfaceView.VISIBLE);
        loadCvCameraView.setCvCameraViewListener(this);
        loadCvCameraView.setMaxFrameSize(800,480);
        final Bundle getintent = getIntent().getExtras();
        choice = getintent.getString("title");
        loadImage(choice);
        keypointImage(choice);

        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            mGLSurfaceView = new TouchSurfaceView(this);
            addContentView(mGLSurfaceView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT));
            mGLSurfaceView.setVisibility(View.INVISIBLE);

            mSurfaceView = new SurfaceView(this);
            addContentView(mSurfaceView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.FILL_PARENT));
            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback((SurfaceHolder.Callback) this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT | WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            mGLSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
            mGLSurfaceView.setZOrderOnTop(true);

        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (loadCvCameraView != null) {
            loadCvCameraView.disableView();
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

        if (good_matches.toArray().length <= 150 || good_matches.toArray().length >= 400) {
            Log.e("asd", "DETECTION MODE" + good_matches.toArray().length);
            good_matches.clear();
            InputImage = inputFrame.gray();
            temp = inputFrame.rgba();

            descriptor_scene = new Mat();
            keypoints_scene = new MatOfKeyPoint();

            try {
                Orbdetector.detect(InputImage, keypoints_scene);
                OrbExtractor.compute(InputImage, keypoints_scene, descriptor_scene);
                prevImage = InputImage;

                MatOfDMatch matches = new MatOfDMatch();
                matcher.match(descriptor_object, descriptor_scene, matches);
                List<DMatch> matchesList = matches.toList();

                double max_dist = 0.0;
                double min_dist = 99.0;

                for (int i = 0; i < descriptor_object.rows(); i++) {
                    double dist = matchesList.get(i).distance;
                    if (dist < min_dist) min_dist = dist;
                    if (dist > max_dist) max_dist = dist;
                }

                for (int i = 0; i < descriptor_object.rows(); i++) {
                    if (matchesList.get(i).distance <= 3 * min_dist) {
                        good_matches.addLast(matchesList.get(i));
                    }
                }
                LinkedList<Point> objList = new LinkedList<Point>();
                LinkedList<Point> sceneList = new LinkedList<Point>();

                List<KeyPoint> keypoints_objectList = keypoints_object.toList();
                List<KeyPoint> keypoints_sceneList = keypoints_scene.toList();

                for (int i = 0; i < 150; i++) {
                    objList.addLast(keypoints_objectList.get(good_matches.get(i).queryIdx).pt);
                    sceneList.addLast(keypoints_sceneList.get(good_matches.get(i).trainIdx).pt);
                }

                obj.fromList(objList);
                scene.fromList(sceneList);
                prevImageKeyp = scene;


                InputImage = null;
                InputImage = temp;
            } catch (RuntimeException e) {
                e.printStackTrace();
            }

        } else {
            temp = inputFrame.rgba();
            InputImage = inputFrame.gray();
            InputImage = opticalFlow(InputImage);
        }
        return InputImage;
    }

    private void loadImage(String choice) {
        File path = new File(Environment.getExternalStorageDirectory().toString() + File.separator
                + "ImageFile" + File.separator + choice);
        try {
            File[] list = path.listFiles();
            for (File f : list) {
                if(f.getName().contains(".png"))
                {
                    loadImge=f.getName();
                }
                if(f.getName().contains(".mp3"))
                {
                    loadVoice=f.getName();
                }
                if(f.getName().contains(".obj"))
                {
                    loadModel=f.getName();
                }

            }
        } catch (NullPointerException e) {
        }
    }

    private void keypointImage(String choice) {
        int detectorType = FeatureDetector.ORB;
        FeatureDetector detector = FeatureDetector.create(detectorType);
        DescriptorExtractor briefDescriptor = DescriptorExtractor.create(DescriptorExtractor.ORB);
        descriptor_object = new Mat();
        TemplateImage = new Mat();

        File path = new File(Environment.getExternalStorageDirectory().toString() + File.separator
                + "ImageFile" + File.separator + choice);
            TemplateImage = Imgcodecs.imread(path + "/" + loadImge, Imgcodecs.CV_LOAD_IMAGE_COLOR);


        keypoints_object = new MatOfKeyPoint();
        detector.detect(TemplateImage, keypoints_object);
        briefDescriptor.compute(TemplateImage, keypoints_object, descriptor_object);
    }

    double[] nokta;
    double[] nokta1;
    double fark;


    private Mat cameraPoseFromHomography(Mat h) {
        Log.d("rifat", "cameraPoseFromHomography: homography " + h.toString());

        Mat pose = Mat.eye(3, 4, CvType.CV_32FC1);  // 3x4 matrix, the camera pose
        float norm1 = (float) Core.norm(h.col(0));
        float norm2 = (float) Core.norm(h.col(1));
        float tnorm = (norm1 + norm2) / 2.0f;       // Normalization value

        Mat normalizedTemp = new Mat();
        Core.normalize(h.col(0), normalizedTemp);
        normalizedTemp.convertTo(normalizedTemp, CvType.CV_32FC1);
        normalizedTemp.copyTo(pose.col(0));

        Core.normalize(h.col(1), normalizedTemp);
        normalizedTemp.convertTo(normalizedTemp, CvType.CV_32FC1);
        normalizedTemp.copyTo(pose.col(1));

        Mat p3 = pose.col(0).cross(pose.col(1));
        p3.copyTo(pose.col(2));

        Mat temp = h.col(2);
        float[] buffer = new float[3];
        h.col(2).get(0, 0, buffer);
        pose.put(0, 3, buffer[0] / tnorm);
        pose.put(1, 3, buffer[1] / tnorm);
        pose.put(2, 3, buffer[2] / tnorm);

        return pose;

    }


    private Mat opticalFlow(Mat nextImg) {
        Log.e("asd", "TRACKİNG MODE");
        try {
            if (nextpoints.toArray().length == 0) {
                prevpoint = prevImageKeyp;
                if(!loadVoice.equals("")) {
                    File path = new File(Environment.getExternalStorageDirectory().toString() + File.separator
                            + "ImageFile" + File.separator + choice);

                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(path + "/" + loadVoice);
                    mediaPlayer.prepare();
                }
            }


            Video.calcOpticalFlowPyrLK(prevImage, nextImg, prevpoint, nextpoints, keypointsStatus, err);
            List<Point> nextList = nextpoints.toList();

            MatOfPoint2f scene_point = new MatOfPoint2f();
            scene_point.fromList(nextList);

            Mat H = Calib3d.findHomography(obj, scene_point, Calib3d.RANSAC, 5);
            Log.e("Type", "" + H.type());
            Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);
            Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);

            obj_corners.put(0, 0, new double[]{0, 0});
            obj_corners.put(1, 0, new double[]{TemplateImage.cols(), 0});
            obj_corners.put(2, 0, new double[]{TemplateImage.cols(), TemplateImage.rows()});
            obj_corners.put(3, 0, new double[]{0, TemplateImage.rows()});

            Log.e("Homograpy", "" + Arrays.toString(H.get(0, 0)));
            Core.perspectiveTransform(obj_corners, scene_corners, H);

            Mat intrinsic = Mat.zeros(3, 3, CvType.CV_32FC1);

            intrinsic.put(0, 0, 764.f);
            intrinsic.put(0, 1, 0f);
            intrinsic.put(0, 2, 399.5f);

            intrinsic.put(1, 0, 0f);
            intrinsic.put(1, 1, 764.f);
            intrinsic.put(1, 2, 239.5f);

            intrinsic.put(2, 0, 0f);
            intrinsic.put(2, 1, 0f);
            intrinsic.put(2, 2, 1f);


            Mat intrinsicInverse = new Mat(3, 3, CvType.CV_32FC1);
            Core.invert(intrinsic, intrinsicInverse);
            intrinsicInverse.convertTo(intrinsicInverse, CvType.CV_32FC1);
            H.convertTo(H, CvType.CV_32FC1);
            // compute H respect the intrinsics
            Core.gemm(intrinsicInverse, H, 1, new Mat(), 0, H);


            Mat pose = cameraPoseFromHomography(H);


            Log.d("rifat", "cameraPoseFromHomography: pose " + Arrays.toString(pose.get(0, 0)) + " " + Arrays.toString(pose.get(0, 1)) + " " + Arrays.toString(pose.get(0, 2)) + " " + Arrays.toString(pose.get(0, 3)) + " " + Arrays.toString(pose.get(1, 0)) + " " + Arrays.toString(pose.get(1, 1)) + " " + Arrays.toString(pose.get(1, 2)) + " " + Arrays.toString(pose.get(1, 3)) + " " + Arrays.toString(pose.get(2, 0)) + " " + Arrays.toString(pose.get(2, 1)) + " " + Arrays.toString(pose.get(2, 2)) + " " + Arrays.toString(pose.get(2, 3)));

            if (!H.empty()) {

                Log.d("rifat", "H.empty DEGIL " + H.toString());

                Imgproc.line(temp, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 2);
                Imgproc.line(temp, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 2);
                Imgproc.line(temp, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 2);
                Imgproc.line(temp, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)), new Scalar(0, 255, 0), 2);
                mGLSurfaceView.setVisibility(View.VISIBLE);

                mGLSurfaceView.setPose(pose);
                mGLSurfaceView.requestRender();
                if(!loadVoice.equals("")) {mediaPlayer.start();}
            }
            prevImage = nextImg.clone();
            prevpoint = nextpoints;

            if (keypointsStatus.get(0,0)[0] == 0.0){
                mGLSurfaceView.setVisibility(View.INVISIBLE);
                if(!loadVoice.equals("")) {mediaPlayer.stop();}
                good_matches.clear();
                nextpoints = new MatOfPoint2f();
                prevImageKeyp = new MatOfPoint2f();
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    private MatOfPoint2f convertKeyPointToPoint(MatOfKeyPoint keypoint) {

        KeyPoint[] liste = keypoint.toArray();
        Point point[] = new Point[liste.length];

        for (int i = 0; i < liste.length; i++) {
            point[i] = new Point(liste[i].pt.x, liste[i].pt.y);
        }
        MatOfPoint2f prevpoint = new MatOfPoint2f();
        prevpoint.fromArray(point);

        return prevpoint;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(!loadVoice.equals("")){mediaPlayer.stop();}
    }
}