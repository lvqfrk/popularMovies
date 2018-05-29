package comlvqfrk.httpsgithub.popularmovies.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class DbBitmapUtility {
    /**
     * convert bitmap to byte array.
     * @param bitmap the image to convert.
     * @return image as byte array.
     */
    public static byte[] getBytes(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            return stream.toByteArray();
    }
    /**
     * convert byte array to bitmap.
     * @param image the byte array to convert.
     * @return image as a bitmap.
     */
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
