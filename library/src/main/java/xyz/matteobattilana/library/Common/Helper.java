package xyz.matteobattilana.library.Common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.DrawableRes;

/**
 * Created by MatteoB on 20/10/2016.
 */
public class Helper {
    public static Bitmap rotateDrawable(Context context, @DrawableRes int resId, int angle) {
        Bitmap bmpOriginal = BitmapFactory.decodeResource(context.getResources(), resId);
        Bitmap bmResult = Bitmap.createBitmap(bmpOriginal.getWidth(), bmpOriginal.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas tempCanvas = new Canvas(bmResult);
        tempCanvas.rotate(angle-90, bmpOriginal.getWidth()/2, bmpOriginal.getHeight()/2);
        tempCanvas.drawBitmap(bmpOriginal, 0, 0, null);
        return bmResult;
    }
}
