package mobapptut.com.camera2videoimage.oo.factory;

/**
 * @author lmiguelmh
 * @since 15/06/2016.
 */
public interface CameraDevice {
    void setupCamera(int width, int height);
    void connectCamera(); //startthread
    void closeCamera(); //stopthread

    //void setHandler(HandlerThread )?
}
