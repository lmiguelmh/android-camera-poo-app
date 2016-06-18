package mobapptut.com.camera2videoimage.oo.bridge;

import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Handler;
import android.util.Size;

/**
 * @author lmiguelmh
 * @since 17/06/2016.
 */
public class CameraBridge1MP extends CameraBridge {

    public void setup(int deviceOrientation, ImageReader.OnImageAvailableListener mOnImageAvailableListener, Handler mBackgroundHandler, int width, int height) {
        if (cameraManager == null)
            throw new IllegalStateException("setup cameraManager first");
        //CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        //CameraManager cameraManager = cameraManager;
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId);
                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }
                StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                //int deviceOrientation = getWindowManager().getDefaultDisplay().getRotation();
                mTotalRotation = sensorToDeviceRotation(cameraCharacteristics, deviceOrientation);
                boolean swapRotation = mTotalRotation == 90 || mTotalRotation == 270;
                int rotatedWidth = width;
                int rotatedHeight = height;
                if (swapRotation) {
                    rotatedWidth = height;
                    rotatedHeight = width;
                }
                //ERROR: Surface with size (w=3328, h=1872) and format 0x1 is not valid, size not in valid set: [800x600, 864x480, 800x480, 720x480, 640x480, 480x368, 480x320, 352x288, 320x240, 176x144]
                //mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class), rotatedWidth, rotatedHeight);
                //mVideoSize = chooseOptimalSize(map.getOutputSizes(MediaRecorder.class), rotatedWidth, rotatedHeight);
                //mImageSize = chooseOptimalSize(map.getOutputSizes(ImageFormat.JPEG), rotatedWidth, rotatedHeight);
                mVideoSize = new Size(640, 480); //TODO: esto debe ser para cada clase en particular, video, image, o preview
                mImageSize = new Size(640, 480);
                mPreviewSize = new Size(640, 480);
                mImageReader = ImageReader.newInstance(mImageSize.getWidth(), mImageSize.getHeight(), ImageFormat.JPEG, 1);
                mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);
                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void connectCamera(String mCameraId, CameraDevice.StateCallback mCameraDeviceStateCallback, Handler mBackgroundHandler) {
        //CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            /*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraManager.openCamera(mCameraId, mCameraDeviceStateCallback, mBackgroundHandler);
                } else {
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                        throw new RuntimeException("Video app required access to camera");
                        //Toast.makeText(this,
                        //        "Video app required access to camera", Toast.LENGTH_SHORT).show();
                    }
                    requestPermissions(new String[]{
                            android.Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO}, REQUEST_CAMERA_PERMISSION_RESULT);
                }

            } else {
            */
            cameraManager.openCamera(mCameraId, mCameraDeviceStateCallback, mBackgroundHandler);
            //}
        } catch (CameraAccessException | SecurityException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void closeCamera() {
        if (mCameraDevice != null) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
        /*
        if (mMediaRecorder != null) {
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        */
    }
}
