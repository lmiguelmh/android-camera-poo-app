package mobapptut.com.camera2videoimage.oo.factory.media;

import java.io.File;
import java.io.IOException;

/**
 * @author lmiguelmh
 * @since 15/06/2016.
 * <p/>
 * representa un archivo multimedia
 */
public interface Media {
    File create() throws IOException;

    void update(byte[] data) throws IOException;
}
