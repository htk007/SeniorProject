package com.example.dell.augmentedreality;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import org.opencv.core.Mat;

public class TouchSurfaceView extends GLSurfaceView {

    public TouchSurfaceView(Context context) {
        super(context);
        cr = new CubeRenderer(true);
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.setRenderer(cr);
        this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);


    }

    public void setPose(Mat pose) {
        cr.setPose(pose);
    }

    public boolean onTrackballEvent(MotionEvent e) {
        cr.mAngleX += e.getX() * TRACKBALL_SCALE_FACTOR;
        cr.mAngleY += e.getY() * TRACKBALL_SCALE_FACTOR;
        requestRender();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                cr.mAngleX += dx * TOUCH_SCALE_FACTOR;
                cr.mAngleY += dy * TOUCH_SCALE_FACTOR;
                requestRender();
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;

    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private final float TRACKBALL_SCALE_FACTOR = 36.0f;
    public CubeRenderer cr;
    private float mPreviousX;
    private float mPreviousY;


}