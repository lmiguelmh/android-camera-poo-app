package mobapptut.com.camera2videoimage.oo.factory.media;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lmiguelmh
 * @since 15/06/2016.
 */
public class Video implements Media {

    private static final DateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");

    @Override
    public File create() throws IOException {
        String timestamp = sdf.format(new Date());
        String prepend = "VIDEO_" + timestamp + "_";
        return File.createTempFile(prepend, ".mp4", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES));
    }

    @Override
    public void update(byte[] data) {
        //
        throw new UnsupportedOperationException("update not supported for video");
    }
}
