package mobapptut.com.camera2videoimage.oo.factory.media;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author lmiguelmh
 * @since 15/06/2016.
 */
public class Photo implements Media {

    private static final DateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
    File file;

    @Override
    public File create() throws IOException {
        String timestamp = sdf.format(new Date());
        String prepend = "IMAGE_" + timestamp + "_";
        return file = File.createTempFile(prepend, ".jpg", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));
    }

    @Override
    public void update(byte[] data) throws IOException {
        if(file == null) {
            create();
        }

        try(FileOutputStream fs = new FileOutputStream(file)) {
            fs.write(data);
            fs.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
