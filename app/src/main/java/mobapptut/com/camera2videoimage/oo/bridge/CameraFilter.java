package mobapptut.com.camera2videoimage.oo.bridge;

import android.graphics.Bitmap;

/**
 * Created by khas on 17/06/2016.
 */
public abstract class CameraFilter {

    // patron bridge agregado por  AGQY este metodo lo implementara las subclases
    public abstract Bitmap filter(Bitmap image);
}
