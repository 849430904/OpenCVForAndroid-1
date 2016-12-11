package com.example.king.opencvforandroid;

import android.media.audiofx.LoudnessEnhancer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{
    public static final String TAG = MainActivity.class.getSimpleName();
    private JavaCameraView mJavaCameraView;
    Mat mRgb,imgGray,imgCanny;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case BaseLoaderCallback.SUCCESS:
                    mJavaCameraView.enableView();
                    break;
                case BaseLoaderCallback.INIT_FAILED:
                    break;
                default:{
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };


    static {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        mJavaCameraView = (JavaCameraView) findViewById(R.id.java_camera_view);
        mJavaCameraView.setVisibility(SurfaceView.VISIBLE);

        mJavaCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(mJavaCameraView!=null){
            mJavaCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mJavaCameraView!=null){
            mJavaCameraView.disableView();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(OpenCVLoader.initDebug()){
            Log.d(TAG,"------OpenCV load successfully");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);

        }else {
            Log.d(TAG,"-----OpenCV not load!");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0,this,mLoaderCallback);
        }
    }


    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgb = new Mat(height,width,CvType.CV_8UC4);
        imgGray = new Mat(height,width,CvType.CV_8UC1);
        imgCanny = new Mat(height,width,CvType.CV_8UC1);

    }

    @Override
    public void onCameraViewStopped() {
        mRgb.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgb = inputFrame.rgba();
        //Gray处理
        Imgproc.cvtColor(mRgb,imgGray,Imgproc.COLOR_RGB2GRAY);
        Imgproc.Canny(imgGray,imgCanny,50,150);
        return imgCanny;
    }
}
