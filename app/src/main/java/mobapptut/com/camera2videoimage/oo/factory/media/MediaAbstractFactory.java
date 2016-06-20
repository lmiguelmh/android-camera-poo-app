package mobapptut.com.camera2videoimage.oo.factory.media;

import java.io.File;

/**
 * @author lmiguelmh
 * @since 15/06/2016.
 */
public abstract class MediaAbstractFactory {

    protected File createDirectory(File root, String file) {
        File dir = new File(root, file);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("can't create directory: " + (root.getPath() + file));
            }
        }
        return dir;
    }

    public abstract Media newMedia();
}
