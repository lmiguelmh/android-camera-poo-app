package mobapptut.com.camera2videoimage.oo.factory.media;

/**
 * @author lmiguelmh
 * @since 15/06/2016.
 */
public class PhotoFactory extends MediaAbstractFactory {
    private static PhotoFactory instance;

    private PhotoFactory() {
    }

    public static PhotoFactory getInstance() {
        if( instance  == null ){
            instance = new PhotoFactory();
        }
        return instance;
    }

    @Override
    public Media newMedia() {
        return new Photo();
    }
}
