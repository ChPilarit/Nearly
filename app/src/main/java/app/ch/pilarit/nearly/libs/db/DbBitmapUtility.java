package app.ch.pilarit.nearly.libs.db;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by ch_pilarit on 4/10/15 AD.
 */
public class DbBitmapUtility {

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getBitmapBase64(String base64Str) {
        byte[] imageAsBytes = Base64.decode(base64Str.getBytes(), Base64.NO_WRAP);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
    }

    public static String getStringBase64(Bitmap bitmap) {
        return Base64.encodeToString(getBytes(bitmap), Base64.NO_WRAP);
    }

}
