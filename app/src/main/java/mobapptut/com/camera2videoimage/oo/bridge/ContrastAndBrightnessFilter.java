package mobapptut.com.camera2videoimage.oo.bridge;

import android.graphics.Bitmap;


import mobapptut.com.camera2videoimage.oo.bridge.util.general.AndroidUtils;
import mobapptut.com.camera2videoimage.oo.bridge.util.general.PixelUtils;


public class ContrastAndBrightnessFilter extends CameraFilter{

          public ContrastAndBrightnessFilter() {

    }

    public Bitmap filter(Bitmap image) {


            int[] mColors  = AndroidUtils.bitmapToIntArray(image);
            int width = image.getWidth();
            int height = image.getHeight();

            mColors = filter(mColors, width, height);

            return Bitmap.createBitmap(
                    mColors, 0, width, width, height, Bitmap.Config.ARGB_8888);


    }


    public int[] filter( int[] src ,int w, int h) {
        int width = w;
        int height = h;


        int[] inPixels = new int[width];
        int[] outPixels = new int[width * height];

        for ( int y = 0; y < height; y++ ) {
            int index = 0;
            for(int i=(y*width);i<((y*width) + width);++i){
                inPixels[index] = src[i];
                index++;
            }

            for ( int x = 0; x < width; x++ ){
                inPixels[x] = filterRGB( x, y, inPixels[x] );
            }

            index = 0;
            for(int i=(y*width);i<((y*width) + width);++i){
                outPixels[i] = inPixels[index];
                index++;
            }
        }

        return outPixels;
    }


    public int filterRGB(int x, int y, int rgb) {
        int[] rTable, gTable, bTable;
        rTable = gTable = bTable = makeTable();
        int a = rgb & 0xff000000;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        r = rTable[r];
        g = gTable[g];
        b = bTable[b];
        return a | (r << 16) | (g << 8) | b;
    }


    public int[] makeTable() {
        int[] table = new int[256];
        for (int i = 0; i < 256; i++)
            table[i] = PixelUtils.clamp( (int)( 255 * transferFunction( i / 255.0f ) ) );
        return table;
    }

    /*public float transferFunction( float v ) {
        return 0;
    }*/

    public float transferFunction( float f ) {
        //inicializar valores
        float brightness = 1.2f ;
        float contrast = 0.5f;

        f = f*brightness;
        f = (f-0.5f)*contrast+0.5f;
        return f;
    }



}
