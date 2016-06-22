package mobapptut.com.camera2videoimage.oo.bridge;

import android.graphics.Bitmap;
import android.graphics.Color;

import mobapptut.com.camera2videoimage.oo.bridge.util.general.AndroidUtils;


public class SaturationAndToneFilter extends CameraFilter {

    public SaturationAndToneFilter() {

    }


    public Bitmap filter(Bitmap image) {

        int[] mColors = AndroidUtils.bitmapToIntArray(image);
        int width = image.getWidth();
        int height = image.getHeight();

        mColors = filter(mColors, width, height);

        return Bitmap.createBitmap(
                mColors, 0, width, width, height, Bitmap.Config.ARGB_8888);
    }


    public int[] filter(int[] src, int w, int h) {
        int width = w;
        int height = h;

        //setDimensions( width, height);

        int[] inPixels = new int[width];
        int[] outPixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            int index = 0;
            for (int i = (y * width); i < ((y * width) + width); ++i) {
                inPixels[index] = src[i];
                index++;
            }

            for (int x = 0; x < width; x++) {
                inPixels[x] = filterRGB(x, y, inPixels[x]);
            }

            index = 0;
            for (int i = (y * width); i < ((y * width) + width); ++i) {
                outPixels[i] = inPixels[index];
                index++;
            }
        }

        return outPixels;
    }


    public int filterRGB(int x, int y, int rgb) {
        int a = rgb & 0xff000000;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;

        //inicializar
        float[] hsb = new float[3];
//        float hFactor = 0.00f;
//        float sFactor = -0.03f;
//        float bFactor = 0.01f;
        float hFactor = 0.2f;
        float sFactor = -0.2f;
        float bFactor = 0.2f;

        Color.RGBToHSV(r, g, b, hsb);
        hsb[0] += hFactor;
        while (hsb[0] < 0)
            hsb[0] += Math.PI * 2;
        hsb[1] += sFactor;
        if (hsb[1] < 0)
            hsb[1] = 0;
        else if (hsb[1] > 1.0)
            hsb[1] = 1.0f;
        hsb[2] += bFactor;
        if (hsb[2] < 0)
            hsb[2] = 0;
        else if (hsb[2] > 1.0)
            hsb[2] = 1.0f;
        rgb = Color.HSVToColor(hsb);
        return a | (rgb & 0xffffff);
    }


}
