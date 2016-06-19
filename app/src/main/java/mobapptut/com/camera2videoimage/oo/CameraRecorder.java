/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobapptut.com.camera2videoimage.oo;

import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;

import mobapptut.com.camera2videoimage.Camera2VideoImageActivity;
import mobapptut.com.camera2videoimage.oo.factory.media.MediaFactory;
import mobapptut.com.camera2videoimage.oo.factory.media.PhotoFactory;
import mobapptut.com.camera2videoimage.oo.factory.media.VideoFactory;

/**
 * @author alulab14
 */
public class CameraRecorder {

    private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAIT_LOCK = 1;
    boolean mIsRecording;
    MediaRecorder mMediaRecorder;
    //CameraDevice mCameraDevice;
    //ImageReader mImageReader;
    CameraCaptureSession mRecordCaptureSession;
    //Size mVideoSize;
    //int mTotalRotation;
    CameraCaptureSession mPreviewCaptureSession;
    CaptureRequest.Builder mCaptureRequestBuilder;
    int mCaptureState = STATE_PREVIEW;

    public boolean ismIsRecording() {
        return mIsRecording;
    }

    public void setmIsRecording(boolean mIsRecording) {
        this.mIsRecording = mIsRecording;
    }

    public void setupMediaRecorder(MediaFactory mediaFactory, Size mVideoSize, int mTotalRotation) throws IOException {
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        //mMediaRecorder.setOutputFile(mVideoFileName);
        mMediaRecorder.setOutputFile(mediaFactory.newMedia().create().getPath());
        mMediaRecorder.setVideoEncodingBitRate(1000000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mMediaRecorder.setOrientationHint(mTotalRotation);
        mMediaRecorder.prepare();
    }

    public void startRecord(CameraDevice mCameraDevice, ImageReader mImageReader, Size mVideoSize, Size mPreviewSize, TextureView mTextureView, int mTotalRotation) {
        try {
            if (mIsRecording) {
                setupMediaRecorder(VideoFactory.getInstance(), mVideoSize, mTotalRotation);
                //setupMediaRecorder(null);
            }/* else if (mIsTimelapse) {
                setupTimelapse(VideoFactory.getInstance());
                //setupTimelapse(null);
            }
            */
            SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            Surface previewSurface = new Surface(surfaceTexture);
            Surface recordSurface = mMediaRecorder.getSurface();
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            mCaptureRequestBuilder.addTarget(previewSurface);
            mCaptureRequestBuilder.addTarget(recordSurface);

            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface, recordSurface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(CameraCaptureSession session) {
                            mRecordCaptureSession = session;
                            try {
                                mRecordCaptureSession.setRepeatingRequest(
                                        mCaptureRequestBuilder.build(), null, null
                                );
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession session) {
                            //Toast.makeText(Camera2VideoImageActivity.this, "No se pudo iniciar sesión de captura!", Toast.LENGTH_SHORT).show();
                            //todo tratar posible no inicio, como deteniendo el proceso o cambiando el medio
                        }
                    }, null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkWriteStoragePermission(CameraDevice mCameraDevice, ImageReader mImageReader, Size mVideoSize, Size mPreviewSize, TextureView mTextureView, int mTotalRotation) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                //BAD: no informa al usuario si no se pudo crear archivo! debería informar y terminar captura
//                try {
//                    //createVideoFileName();
//                    VideoFactory.getInstance().newMedia().create();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //BAD: Dont repeat yourself!
//                startRecord(mPreviewSize, mTextureView); //BAD: Oculta el setup()
//                mMediaRecorder.start();
//                /*
//                mChronometer.setBase(SystemClock.elapsedRealtime());
//                mChronometer.setVisibility(View.VISIBLE);
//                mChronometer.start();
//                */
//
//            } else {
//                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                    Toast.makeText(this, "app needs to be able to save videos", Toast.LENGTH_SHORT).show();
//                }
//                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT);
//            }
//        } else {
//            //BAD: no informa al usuario si no se pudo crear archivo! debería informar y terminar captura

        try {
            //createVideoFileName();
            VideoFactory.getInstance().newMedia().create();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //BAD: Dont repeat yourself!
        mMediaRecorder = new MediaRecorder();
        startRecord(mCameraDevice, mImageReader, mVideoSize, mPreviewSize, mTextureView, mTotalRotation);
        mMediaRecorder.start();
//            /*
//            mChronometer.setBase(SystemClock.elapsedRealtime());
//            mChronometer.setVisibility(View.VISIBLE);
//            mChronometer.start();
//            */
//        }
    }

    public void record(CameraDevice mCameraDevice, ImageReader mImageReader, Size mVideoSize, Size mPreviewSize, TextureView mTextureView, int mTotalRotation) {
        try {
            //createVideoFileName();
            VideoFactory.getInstance().newMedia().create();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaRecorder = new MediaRecorder();
        startRecord(mCameraDevice, mImageReader, mVideoSize, mPreviewSize, mTextureView, mTotalRotation);
        mMediaRecorder.start();

        /*
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        mChronometer.setBase(SystemClock.elapsedRealtime());
                        mChronometer.setVisibility(View.VISIBLE);
                        mChronometer.start();
                    }
                }
        );
        */
    }

    public void startPreview(CameraDevice mCameraDevice, ImageReader mImageReader, Size mPreviewSize, TextureView mTextureView, final Handler mBackgroundHandler) {
        SurfaceTexture surfaceTexture = mTextureView.getSurfaceTexture();
        //surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        Surface previewSurface = new Surface(surfaceTexture);

        try {
            mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mCaptureRequestBuilder.addTarget(previewSurface);

            //mCameraDevice.createCaptureSession(Arrays.asList(previewSurface, mImageReader.getSurface()),
            mCameraDevice.createCaptureSession(Arrays.asList(previewSurface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(CameraCaptureSession session) {
                            mPreviewCaptureSession = session;
                            try {
                                mPreviewCaptureSession.setRepeatingRequest(mCaptureRequestBuilder.build(),
                                        null, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession session) {
                            //Toast.makeText(getApplicationContext(), "Unable to setup camera preview", Toast.LENGTH_SHORT).show();
                            //todo observer here for sendind messages
                        }
                    }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void startStillCaptureRequest(CameraDevice mCameraDevice, ImageReader mImageReader, int mTotalRotation) {
        try {
            if (mIsRecording) {
                mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_VIDEO_SNAPSHOT);
            } else {
                mCaptureRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            }
            //mCaptureRequestBuilder.addTarget(mImageReader.getSurface());
            mCaptureRequestBuilder.addTarget(mImageReader.getSurface());
            //mCaptureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, mTotalRotation);
            mCaptureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, mTotalRotation);

            CameraCaptureSession.CaptureCallback stillCaptureCallback = new
                    CameraCaptureSession.CaptureCallback() {
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

    public void lockFocus(final CameraDevice mCameraDevice, final ImageReader mImageReader, final int mTotalRotation, Handler mBackgroundHandler) {
        mCaptureState = STATE_WAIT_LOCK;
        mCaptureRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START);
        try {
            if (mIsRecording) {
                mRecordCaptureSession.capture(mCaptureRequestBuilder.build(), new
                        CameraCaptureSession.CaptureCallback() {

                            private void process(CaptureResult captureResult) {
                                switch (mCaptureState) {
                                    case STATE_PREVIEW:
                                        // Do nothing
                                        break;
                                    case STATE_WAIT_LOCK:
                                        mCaptureState = STATE_PREVIEW;
                                        Integer afState = captureResult.get(CaptureResult.CONTROL_AF_STATE);
                                        if (afState == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED ||
                                                afState == CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED) {
                                            //Toast.makeText(getApplicationContext(), "AF Locked!", Toast.LENGTH_SHORT).show();
                                            //todo observer here
                                            startStillCaptureRequest(mCameraDevice, mImageReader, mTotalRotation);
                                        }
                                        break;
                                }
                            }

                            @Override
                            public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                                super.onCaptureCompleted(session, request, result);

                                process(result);
                            }
                        }, mBackgroundHandler);
            } else {
                mPreviewCaptureSession.capture(mCaptureRequestBuilder.build(), new
                        CameraCaptureSession.CaptureCallback() {

                            private void process(CaptureResult captureResult) {
                                switch (mCaptureState) {
                                    case STATE_PREVIEW:
                                        // Do nothing
                                        break;
                                    case STATE_WAIT_LOCK:
                                        mCaptureState = STATE_PREVIEW;
                                        Integer afState = captureResult.get(CaptureResult.CONTROL_AF_STATE);
                                        if (afState == CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED ||
                                                afState == CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED) {
                                            //Toast.makeText(getApplicationContext(), "AF Locked!", Toast.LENGTH_SHORT).show();
                                            //todo observer here
                                            startStillCaptureRequest(mCameraDevice, mImageReader, mTotalRotation);
                                        }
                                        break;
                                }
                            }

                            @Override
                            public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                                super.onCaptureCompleted(session, request, result);

                                process(result);
                            }
                        }, mBackgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

}
