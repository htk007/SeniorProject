package com.example.dell.augmentedreality;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import org.opencv.core.Mat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class CubeRenderer implements GLSurfaceView.Renderer {

    private FloatBuffer glMatrixBuffer;
    private float factor = 0f;
    private Mat pose;
    int i=1;
    public CubeRenderer(boolean useTranslucentBackground) {
        mTranslucentBackground = useTranslucentBackground;
        mCube = new Cube();

        glMatrixBuffer = ByteBuffer.allocateDirect(16*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public void setPose(Mat pose) {
        this.pose = pose;
    }

    public void onDrawFrame(GL10 gl) {
        Log.d("rifat", "onDrawFrame");

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glClearColor(0f, 0f, 0f, 0f);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();

        /** Camera parameters*/
        float f_x = 764f;
        float f_y = 764f;
        float c_y = 239.5f;
        float c_x = 399.5f;

        float screen_width = 800;
        float screen_height = 480;
        float fovY = 1f/(f_x/screen_height * 2);
        float aspectRatio = screen_width/screen_height * f_y/f_x;
        float near = 0.1f;  /**< Near clipping distance*/
        float far = 10000f;  /**< Far clipping distance*/
        float frustum_height = near * fovY;
        float frustum_width = frustum_height * aspectRatio;

        float offset_x = (screen_width/2 - c_x)/screen_width * frustum_width * 2;
        float offset_y = (screen_height/2 - c_y)/screen_height * frustum_height * 2;

        /** Build and apply the projection matrix*/
        float left = -frustum_width - offset_x;
        float right = frustum_width - offset_x;
        float bottom = -frustum_height - offset_y;
        float top = frustum_height - offset_y;

        Log.d("rifat", "left="+left + " right="+right + " bottom="+bottom + " top="+top + " near="+near + " far="+far);
        gl.glFrustumf( left, right, bottom, top, near, far);


        factor = factor + 0.5f;

        glMatrixBuffer.put((float)pose.get(0, 0)[0]); //a
        glMatrixBuffer.put((float)pose.get(1, 0)[0]); //d
        glMatrixBuffer.put(-(float)pose.get(2, 0)[0]); //g
        glMatrixBuffer.put(0f); //0

        glMatrixBuffer.put((float)pose.get(0, 1)[0]); //b
        glMatrixBuffer.put((float)pose.get(1, 1)[0]); //e
        glMatrixBuffer.put(-(float)pose.get(2, 1)[0]); //h
        glMatrixBuffer.put(0f); //0

        glMatrixBuffer.put((float)pose.get(0, 2)[0]);//c
        glMatrixBuffer.put((float)pose.get(1, 2)[0]);//f
        glMatrixBuffer.put(-(float) pose.get(2, 2)[0]);//i
        glMatrixBuffer.put(0f); //0

        Log.d("rifat", "normalized x = " + ((float) pose.get(0, 3)[0] / 60 + 6f));
        Log.d("rifat", "normalized y = " + ((float) pose.get(1, 3)[0] / 60 + 4f));
        Log.d("rifat", "normalized z = " + -((float) pose.get(2, 3)[0] / 1000 + 2f));
        glMatrixBuffer.put(((float)pose.get(0, 3)[0]/60 + 6f)); //t1
        glMatrixBuffer.put(-((float)pose.get(1, 3)[0]/60 + 4f)); //t2
        glMatrixBuffer.put(-20f); //t3
        glMatrixBuffer.put(1f);

        if(factor > 8f)
            factor = -8f;

        glMatrixBuffer.position(0);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadMatrixf(glMatrixBuffer);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        mCube.draw(gl);
    }

    @Override


    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        if (mTranslucentBackground) {
            gl.glClearColor(0,0,0,0);
        } else {
            gl.glClearColor(1,1,1,1);
        }
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);
    }
    public void setAngle(float _angle){

    }

    private boolean mTranslucentBackground;
    private Cube mCube;
    private float mAngle;
    public  float mAngleX;
    public float mAngleY;
}
