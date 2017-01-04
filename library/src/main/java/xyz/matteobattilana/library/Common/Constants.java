package xyz.matteobattilana.library.Common;

/**
 * Created by MatteoB on 15/10/2016.
 */
public class Constants {
    public static enum weatherStatus {RAIN, SNOW, SUN};
    public static enum orientationStatus {ENABLE, DISABLE};

    public static int rainTime = 2200;
    public static int snowTime = 4000;
    public static int fadeOutTime = 200;

    public static int rainParticles = 34;
    public static int snowParticles = 15;

    public static int fps = 30;

    public static int rainAngle = -6;
    public static int snowAngle = 0;

    public static boolean isOrientationActive = false;
    public static int angleRangeUpdate = 3;
    public static int angleRangeRead = 20;
}
