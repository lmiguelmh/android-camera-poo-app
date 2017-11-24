/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobapptut.com.camera2videoimage.oo.test;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.widget.Toast;

import java.io.IOException;

import mobapptut.com.camera2videoimage.oo.factory.media.PhotoFactory;

/**
 *
 * @author alulab14
 */
public class StillCaptureRequest {
 
    boolean mIsRecording;
    CameraDevice mCameraDevice;
    ImageReader mImageReader;
    CaptureRequest.Builder mCaptureRequestBuilder;
    //int mTotalRotation = sensorToDeviceRotation(cameraCharacteristics, deviceOrientation);
    CameraCaptureSession mRecordCaptureSession; //si camera
    CameraCaptureSession mPreviewCaptureSession;

    /*
    @Override
    public void onImageAvailable(ImageReader reader) {
        mBackgroundHandler.post(new ImageSaver(reader.acquireLatestImage()));
        Toast.makeText(getApplicationContext(), "Foto guardada!", Toast.LENGTH_SHORT).show();
    }
    */

    int mTotalRotation = Integer.MAX_VALUE;

    private void startStillCaptureRequest() {
        if(mTotalRotation == Integer.MAX_VALUE) {
            throw new IllegalStateException("setup mTotalRotation first!");
        }
        try {
            if (mIsRecording) {
                mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_VIDEO_SNAPSHOT);
            } else {
                mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            }
            
            mCaptureRequestBuilder.addTarget(mImageReader.getSurface());
            mCaptureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, mTotalRotation);

            CameraCaptureSession.CaptureCallback stillCaptureCallback = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureStarted(CameraCaptureSession session, CaptureRequest request, long timestamp, long frameNumber) {
                    super.onCaptureStarted(session, request, timestamp, frameNumber);

                    try {
                        PhotoFactory.getInstance().newMedia().create();
                        //createImageFileName();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };

            if (mIsRecording) {
                mRecordCaptureSession.capture(mCaptureRequestBuilder.build(), stillCaptureCallback, null);
            } else {
                mPreviewCaptureSession.capture(mCaptureRequestBuilder.build(), stillCaptureCallback, null);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}
