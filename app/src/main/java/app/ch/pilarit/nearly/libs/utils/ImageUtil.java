package app.ch.pilarit.nearly.libs.utils;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by ch_pilarit on 4/10/15 AD.
 */
public class ImageUtil {

    public static Bitmap scaleDownBitmap(Bitmap photo, int newHeight, Context context) {

        final float densityMultiplier = context.getResources().getDisplayMetrics().density;

        int h= (int) (newHeight*densityMultiplier);
        int w= (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo= Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

}
