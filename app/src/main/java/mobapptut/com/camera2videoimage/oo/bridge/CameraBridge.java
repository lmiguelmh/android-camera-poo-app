package mobapptut.com.camera2videoimage.oo.bridge;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;

import java.util.Observable;

/**
 * @author lmiguelmh
 * @since 15/06/2016.
 */
public abstract class CameraBridge {

    private static SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 0);
        ORIENTATIONS.append(Surface.ROTATION_90, 90);
        ORIENTATIONS.append(Surface.ROTATION_180, 180);
        ORIENTATIONS.append(Surface.ROTATION_270, 270);
    }

    CameraFilter filter;
    Size mVideoSize;
    Size mImageSize;
    Size mPreviewSize;
    ImageReader mImageReader;
    String mCameraId;
    int mTotalRotation;
    //CameraManager cameraManager;
    //CameraDevice mCameraDevice;

    public static int sensorToDeviceRotation(CameraCharacteristics cameraCharacteristics, int deviceOrientation) {
        int sensorOrienatation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        deviceOrientation = ORIENTATIONS.get(deviceOrientation);
        return (sensorOrienatation + deviceOrientation + 360) % 360;
    }

    public void setFilter(CameraFilter filter) {
        this.filter = filter;
    }

    public void setImage() {
    }

    public void process() {
    }

    public Size getmVideoSize() {
        return mVideoSize;
    }

    public Size getmImageSize() {
        return mImageSize;
    }

    public Size getmPreviewSize() {
        return mPreviewSize;
    }

    public ImageReader getmImageReader() {
        return mImageReader;
    }

    public String getmCameraId() {
        return mCameraId;
    }

    public int getmTotalRotation() {
        return mTotalRotation;
    }

//    public CameraDevice getmCameraDevice() {
//        return mCameraDevice;
//    }

    public abstract void setup(CameraManager cameraManager, int deviceOrientation, ImageReader.OnImageAvailableListener mOnImageAvailableListener, Handler mBackgroundHandler, int width, int height);

    public abstract void connect(CameraManager cameraManager, CameraDevice.StateCallback mCameraDeviceStateCallback, Handler mBackgroundHandler);

    public abstract void close(CameraDevice mCameraDevice);

}
