package mobapptut.com.camera2videoimage;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.TextureView;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Observable;
import java.util.Observer;

import mobapptut.com.camera2videoimage.oo.observer.CameraRecorder;
import mobapptut.com.camera2videoimage.oo.bridge.CameraBridge;
import mobapptut.com.camera2videoimage.oo.bridge.Camera1MP;
import mobapptut.com.camera2videoimage.oo.factory.media.Media;
import mobapptut.com.camera2videoimage.oo.factory.media.MediaAbstractFactory;
import mobapptut.com.camera2videoimage.oo.factory.media.PhotoFactory;

public class Camera2VideoImageActivity
        extends AppCompatActivity
        implements Observer {

    private static final int REQUEST_CAMERA_PERMISSION_RESULT = 0;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT = 1;

    CameraBridge cameraBridge = new Camera1MP();
    CameraRecorder cameraRecorder;
    private TextureView mTextureView;
    private CameraDevice mCameraDevice;
    private HandlerThread mBackgroundHandlerThread;
    private Handler mBackgroundHandler;
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener = new
            ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
                    mBackgroundHandler.post(new ImageSaver(reader.acquireLatestImage()));
                    Toast.makeText(getApplicationContext(), "Foto guardada!", Toast.LENGTH_SHORT).show();
                }
            };
    private Chronometer mChronometer;
    private ImageButton mRecordImageButton;
    private ImageButton mStillImageButton;
    //private boolean mIsTimelapse = false;
    private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mCameraDevice = camera;
            cameraRecorder = new CameraRecorder();
            cameraRecorder.addObserver(Camera2VideoImageActivity.this);
            if (cameraRecorder.ismIsRecording()) {
                cameraRecorder.record(mCameraDevice, cameraBridge.getmImageReader(), cameraBridge.getmVideoSize(), cameraBridge.getmPreviewSize(), mTextureView, cameraBridge.getmTotalRotation());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChronometer.setBase(SystemClock.elapsedRealtime());
                        mChronometer.setVisibility(View.VISIBLE);
                        mChronometer.start();
                    }
                });
            } else {
                cameraRecorder.startPreview(mCameraDevice, cameraBridge.getmImageReader(), cameraBridge.getmPreviewSize(), mTextureView, mBackgroundHandler);
            }
            Toast.makeText(getApplicationContext(), "Camera connection made!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            mCameraDevice = null;
        }
    };
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            try {
                connectCamera(width, height);
            } catch (Exception e) {
                Toast.makeText(Camera2VideoImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    public void connectCamera(int width, int height) {
        checkCameraPermission();
        cameraBridge.setup((CameraManager) getSystemService(Context.CAMERA_SERVICE), getWindowManager().getDefaultDisplay().getRotation(), mOnImageAvailableListener, mBackgroundHandler, width, height);
        cameraBridge.connect((CameraManager) getSystemService(Context.CAMERA_SERVICE), mCameraDeviceStateCallback, mBackgroundHandler);
    }

    private void checkCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Camera2VideoImageActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    throw new RuntimeException("Video app required access to camera");
                }
                requestPermissions(new String[]{
                        android.Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO}, REQUEST_CAMERA_PERMISSION_RESULT);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2_video_image);

//        createVideoFolder();
//        createImageFolder();

        mChronometer = (Chronometer) findViewById(R.id.chronometer);
        mTextureView = (TextureView) findViewById(R.id.textureView);
        mStillImageButton = (ImageButton) findViewById(R.id.cameraImageButton2);
        mStillImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraRecorder.lockFocus(mCameraDevice, cameraBridge.getmImageReader(), cameraBridge.getmTotalRotation(), mBackgroundHandler);
            }
        });
        mRecordImageButton = (ImageButton) findViewById(R.id.videoOnlineImageButton);
        mRecordImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cameraRecorder.ismIsRecording()) {
                    //BAD: el mismo botón cumple dos funciones!
                    //aquí finaliza grabación
                    Toast.makeText(Camera2VideoImageActivity.this, "Finalizando grabación", Toast.LENGTH_SHORT).show();
                    mChronometer.stop();
                    mChronometer.setVisibility(View.INVISIBLE);
                    cameraRecorder.setmIsRecording(false);
                    mRecordImageButton.setImageResource(R.mipmap.btn_video_online);
                    //mMediaRecorder.stop(); //ESTO PROVOCA QUE NO SE PUEDA INICIAR EL SERVICIO NUEVAMENTE!
                    //mMediaRecorder.reset();
                    cameraRecorder.startPreview(mCameraDevice, cameraBridge.getmImageReader(), cameraBridge.getmPreviewSize(), mTextureView, mBackgroundHandler);

                } else {
                    //aquí inicia grabación
                    //BAD: se usa un flag como una forma de enviar un mensaje! debería ser un observer/observable de los eventos
                    Toast.makeText(Camera2VideoImageActivity.this, "Iniciando grabación", Toast.LENGTH_SHORT).show();
                    cameraRecorder.setmIsRecording(true);
                    mRecordImageButton.setImageResource(R.mipmap.btn_video_busy);
                    //BAD: método "checkWriteStoragePermission" inicia captura! el nombre debería ser explícito
                    //checkWriteStoragePermission();
                    cameraRecorder.record(mCameraDevice, cameraBridge.getmImageReader(), cameraBridge.getmVideoSize(), cameraBridge.getmPreviewSize(), mTextureView, cameraBridge.getmTotalRotation());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mChronometer.setBase(SystemClock.elapsedRealtime());
                            mChronometer.setVisibility(View.VISIBLE);
                            mChronometer.start();
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // el thread se usa como handler para connect, capture, listeners...
        // "the handler on which the callback should be invoked"
        startBackgroundThread();

        if (mTextureView.isAvailable()) {
            try {
                connectCamera(mTextureView.getWidth(), mTextureView.getHeight());
            } catch (Exception e) {
                Toast.makeText(Camera2VideoImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION_RESULT) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        "Application will not run without camera services", Toast.LENGTH_SHORT).show();
            }
            if (grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(),
                        "Application will not have audio on record", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION_RESULT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                cameraRecorder.setmIsRecording(true);
                mRecordImageButton.setImageResource(R.mipmap.btn_video_busy);
                Toast.makeText(this,
                        "Permission successfully granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "App needs to save video to run", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        //closeCamera();
        cameraBridge.close(mCameraDevice);

        stopBackgroundThread();

        super.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocas) {
        super.onWindowFocusChanged(hasFocas);
        View decorView = getWindow().getDecorView();
        if (hasFocas) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    private void startBackgroundThread() {
        mBackgroundHandlerThread = new HandlerThread("Camera2VideoImage");
        mBackgroundHandlerThread.start();
        mBackgroundHandler = new Handler(mBackgroundHandlerThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundHandlerThread.quitSafely();
        try {
            mBackgroundHandlerThread.join();
            mBackgroundHandlerThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void write(MediaAbstractFactory factory, byte[] data) throws IOException {
        Media media = factory.newMedia();
        if (data != null) {
            media.update(data);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        if (data instanceof String) {
            Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private class ImageSaver implements Runnable {

        private final Image mImage;

        public ImageSaver(Image image) {
            mImage = image;
        }

        @Override
        public void run() {
            ByteBuffer byteBuffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            mImage.close();
            try {
                write(PhotoFactory.getInstance(), bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
