package mobapptut.com.camera2videoimage.oo.bridge;

import java.io.File;

/**
 * @author lmiguelmh
 * @since 15/06/2016.
 */
public interface MediaGrabber {
    //void setup()?

    void startRecord();
    void startPreview();
    void startStillCaptureRequest();
    void createVideoFolder();
    File createVideoFileName();
}
