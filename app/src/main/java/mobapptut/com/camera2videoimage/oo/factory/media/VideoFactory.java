package mobapptut.com.camera2videoimage.oo.factory.media;

/**
 * @author lmiguelmh
 * @since 15/06/2016.
 */
public class VideoFactory extends MediaFactory {
    private static VideoFactory instance;

    private VideoFactory() {
    }

    public static VideoFactory getInstance() {
        if( instance  == null ){
            instance = new VideoFactory();
        }
        return instance;
    }

    @Override
    public Media newMedia() {
        return new Video();
    }

}
