package xyz.matteobattilana.library.Common;

/**
 * Created by MatteoB on 15/10/2016.
 */
@Deprecated
public class Constants {
    public static int fps = 30;
    public static boolean isOrientationActive = false;
    public static int angleRangeUpdate = 3;
    public static int angleRangeRead = 20;
    //RAIN
    public static int rainTime = 2200;
    public static int rainFadeOutTime = 200;
    public static int rainParticles = 34;
    public static int rainAngle = -6;
    //SNOW
    public static int snowFadeOutTime = 200;
    public static int snowTime = 4000;
    public static int snowParticles = 15;
    public static int snowAngle = 0;

    //COMMON
    public enum WeatherStatus {
        RAIN, SNOW, SUN
    }

    public enum OrientationStatus {
        ENABLE, DISABLE
    }

}
