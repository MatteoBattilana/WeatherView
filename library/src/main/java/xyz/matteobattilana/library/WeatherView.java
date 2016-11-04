package xyz.matteobattilana.library;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import com.plattysoft.leonids.ParticleSystem;
import xyz.matteobattilana.library.Common.Constants;

/**
 * Created by MatteoB on 14/10/2016.
 * This is an extended View.
 */
public class WeatherView extends View {
    private int mRainTime = Constants.rainTime;
    private int mSnowTime = Constants.snowTime;
    private int mFadeOutTime = Constants.fadeOutTime;

    private int mRainParticles = Constants.rainParticles;
    private int mSnowParticles = Constants.snowParticles;

    private int mFps = Constants.fps;
    private int mRainAngle = Constants.rainAngle;
    private int mSnowAngle = Constants.snowAngle;

    private ParticleSystem mParticleSystem;
    private Constants.weatherStatus mCurrentWeather = Constants.weatherStatus.SUN;
    Context mContext;
    Activity mActivity;
    boolean isPlaying = false;


    /**
     * This method initialize the WeatherView to SUN. No animation is showed.
     * If you want to start the animation after set a different weather with the
     * setWeather(weatherStatus mWeatherStatus) method you must call
     * startAnimation().
     *
     * @param context Context from the application
     * @param attrs   Attributes
     */
    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        //Used to avoid issue during the design
        if (!isInEditMode()) {
            mActivity = (Activity) getContext();

            //set the default to SUN
            setWeather(Constants.weatherStatus.SUN);
            initOptions(context, attrs);
        }
    }

    /**
     * This method can set the startingWeather, lifeTime and fadeOutTime from
     * the xml configuration.
     * See https://github.com/MatteoBattilana/WeatherView#basic-usage
     *
     * @param context Context from the constructor
     * @param attrs   Attributest from the constructor
     */
    private void initOptions(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WeatherView, 0, 0);
        int startingWeather, lifeTime, fadeOutTime, numParticles, fps, angle;
        try {
            //Defatul 0 --> SUN
            startingWeather = typedArray.getInt(R.styleable.WeatherView_startingWeather, 0);
            //If there is not a lifeTime and/or fadeOutTime it reset to
            //default. If -1 it reset the value
            lifeTime = typedArray.getInt(R.styleable.WeatherView_lifeTime, -1);
            fadeOutTime = typedArray.getInt(R.styleable.WeatherView_fadeOutTime, -1);
            numParticles = typedArray.getInt(R.styleable.WeatherView_numParticles, -1);
            fps = typedArray.getInt(R.styleable.WeatherView_fps, -1);
            angle = typedArray.getInt(R.styleable.WeatherView_angle, -200);

            //MUST CALL INSIDE TRY CATCH
            setWeather(Constants.weatherStatus.values()[startingWeather], lifeTime, fadeOutTime, numParticles, fps, angle); //angle

        } finally {
            typedArray.recycle();
        }
    }

    /**
     * This constructor set the weather specifying the type, the life time,
     * the fade out time and the numbre of particle per second. The animation is stoppend when this
     * method is called.
     *
     * @param status       set the weatherStatus {RAIN,SUN,SNOW}
     * @param lifeTime     must be greater or equals than 0, if set to a negative
     *                     value it is set to the default value.
     * @param fadeOutTime  must be greater or equals than 0, if set to a negative
     *                     value it is set to the default value.
     * @param numParticles must be greater than 0, if set to a negative
     *                     value it is set to the default value.
     * @param fps          must be less than 100 and higer than 8
     * @param angle        must be higer than 180 and less than -180
     */
    public void setWeather(Constants.weatherStatus status, int lifeTime, int fadeOutTime, int numParticles, int fps, int angle) {

        //Common setters
        setFPS(fps);

        mCurrentWeather = status;
        setFadeOutTime(fadeOutTime);


        switch (status) {
            case RAIN:
                setRainTime(lifeTime);
                setRainParticles(numParticles);
                setRainAngle(angle);
                mParticleSystem = new ParticleSystem(mActivity, mRainParticles * mRainTime / 1000, R.drawable.rain, mRainTime)
                        .setAcceleration(0.00013f, 90 - mRainAngle)
                        .setInitialRotation(-mRainAngle)
                        .setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f)
                        .setFadeOut(this.mFadeOutTime, new AccelerateInterpolator());
                break;
            case SNOW:
                setSnowTime(lifeTime);
               setSnowParticles(numParticles);
                setSnowAngle(angle);
                mParticleSystem = new ParticleSystem(mActivity, mSnowParticles * mSnowTime / 1000, R.drawable.snow, mSnowTime)
                        .setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f)
                        .setInitialRotation(-mSnowAngle)
                        .setFadeOut(this.mFadeOutTime, new AccelerateInterpolator());
                break;
            default:
                break;
        }




    }

    /**
     * This constructor set the weather specifying the type, the life time,
     * the fade out time and the numbre of particle per second. The animation is stoppend when this
     * method is called.
     *
     * @param status       set the weatherStatus {RAIN,SUN,SNOW}
     * @param lifeTime     must be greater or equals than 0, if set to a negative
     *                     value it is set to the default value.
     * @param fadeOutTime  must be greater or equals than 0, if set to a negative
     *                     value it is set to the default value.
     * @param numParticles must be greater than 0, if set to a negative
     *                     value it is set to the default value.
     * @param fps          must be less than 100 and higer than 8
     */
    public void setWeather(Constants.weatherStatus status, int lifeTime, int fadeOutTime, int numParticles, int fps) {
        setWeather(status, lifeTime, fadeOutTime, numParticles, fps, -200);
    }


    /**
     * This constructor set the weather specifying the type, the life time,
     * the fade out time and the numbre of particle per second. The animation is stoppend when this
     * method is called.
     *
     * @param status       set the weatherStatus {RAIN,SUN,SNOW}
     * @param lifeTime     must be greater or equals than 0, if set to a negative
     *                     value it is set to the default value.
     * @param fadeOutTime  must be greater or equals than 0, if set to a negative
     *                     value it is set to the default value.
     * @param numParticles must be greater than 0, if set to a negative
     *                     value it is set to the default value.
     */
    public void setWeather(Constants.weatherStatus status, int lifeTime, int fadeOutTime, int numParticles) {
        setWeather(status, lifeTime, fadeOutTime, numParticles, -1, -200);
    }

    /**
     * This constructor set the weather specifying the type, the life time and
     * the fade out time. The animation is stoppend when this method is called
     *
     * @param status      set the weatherStatus {RAIN,SUN,SNOW}
     * @param lifeTime    must be greater or equals than 0, if set to a negative
     *                    value it is set to the default value.
     * @param fadeOutTime must be greater or equals than 0, if set to a negative
     *                    value it is set to the default value.
     */
    public void setWeather(Constants.weatherStatus status, int lifeTime, int fadeOutTime) {
        setWeather(status, lifeTime, fadeOutTime, -1, -1, -200);
    }

    /**
     * This constructor set the weather specifying the type
     *
     * @param status set the weatherStatus {RAIN,SUN,SNOW}
     */
    public void setWeather(Constants.weatherStatus status) {
        setWeather(status, -1, -1, -1, -1, -200);
    }

    /**
     * This constructor set the weather specifying the type and the life time
     *
     * @param status   set the weatherStatus {RAIN,SUN,SNOW}
     * @param lifeTime must be greater or equals than 0, if set to a negative
     *                 value it is set to the default value.
     */
    public void setWeather(Constants.weatherStatus status, int lifeTime) {
        setWeather(status, lifeTime, -1, -1, -1, -200);
    }

    /**
     * If the animation is playing the new configuration is loaded stopping the
     * current animation. Then automatically restart the animation
     */
    public void restartWithNewConfiguration() {
        reloadNewConfiguration();
        startAnimation();
    }

    /**
     * Reload configuration. If the animation was playing it continue the animation
     * with the new configuration. Used only in setRainParticles() and
     * setSnowParticles()
     */
    private void reloadNewConfiguration() {
        setWeather(mCurrentWeather, mCurrentWeather == Constants.weatherStatus.RAIN ? mRainTime : mSnowTime, mFadeOutTime, mCurrentWeather == Constants.weatherStatus.RAIN ? mRainParticles : mSnowParticles, mFps, mCurrentWeather == Constants.weatherStatus.RAIN ? mRainAngle : mSnowAngle);
    }

    /**
     * Added a Runnable in order to avoid error during the animation. This method
     * wait until the view is loaded and then it plays the animation
     */
    public void startAnimation() {
        if (mParticleSystem != null) {
            this.post(new Runnable() {
                @Override
                public void run() {
                    int width = getWidth(); // will be non-zero
                    int height = getHeight(); // will be non-zero
                    if (width != 0 && height != 0 && !isPlaying)
                        emitParticles();
                }
            });
        }
    }

    /**
     * Internal method for start the animation
     */
    private void emitParticles() {
        //Must check particle
        switch (mCurrentWeather) {
            case RAIN:
                mParticleSystem.emitWithGravity(this, Gravity.BOTTOM, mRainParticles);
                break;
            case SNOW:
                mParticleSystem.emitWithGravity(this, Gravity.BOTTOM, mSnowParticles);
                break;
            default:
                break;
        }
        isPlaying = true;
    }

    /**
     * Stop the animation.
     */
    public void cancelAnimation() {
        if (mParticleSystem != null) {
            mParticleSystem.cancel();
            isPlaying = false;
        }
    }

    /**
     * Pause the animation. If there are some particles playing the animation
     * they would not stopped by this method.
     */
    public void stopAnimation() {
        if (mParticleSystem != null) {
            mParticleSystem.stopEmitting();
            isPlaying = false;
        }
    }

    /**
     * This method set the rain life time of the animation. If the animation is playing, it is stopped.
     * After called one of this method the animation must be restarted manually with 7
     * restartWithNewConfiguration() method.
     *
     * @param rainTime must be greater or equal than 0, if set to a negative
     *                 value it is set to Constants.rainTime
     */
    public void setRainTime(int rainTime) {
        this.mRainTime = rainTime >= 0 ? rainTime : Constants.rainTime;
    }

    /**
     * This method set the fade out time of the animation in ms. If the animation is playing, it is stopped.
     * After called one of this method the animation must be restarted manually with 7
     * restartWithNewConfiguration() method.
     *
     * @param fadeOutTime must be greater or equal than 0, if set to a negative
     *                    value it is set to Constants.fadeOutTime
     */
    public void setFadeOutTime(int fadeOutTime) {
        this.mFadeOutTime = fadeOutTime >= 0 ? fadeOutTime : Constants.fadeOutTime;
    }

    /**
     * Return the fadeOutTime time in ms
     *
     * @return fade out time in ms
     */
    public int getFadeOutTime() {
        return mFadeOutTime;
    }

    /**
     * Return rainTime or snowTime in ms
     *
     * @return rainTime or snowTime in ms
     */
    public int getLifeTime() {
        return (mCurrentWeather == Constants.weatherStatus.RAIN ? mRainTime : mSnowTime);
    }

    /**
     * Return rainParticles or snowParticles
     *
     * @return rainParticles or snowParticles
     */
    public int getParticles() {
        return (mCurrentWeather == Constants.weatherStatus.RAIN ? mRainParticles : mSnowParticles);
    }


    /**
     * Return angle of current animation
     *
     * @return angle of current animation
     */
    public int getAngle() {
        return (mCurrentWeather == Constants.weatherStatus.RAIN ? mRainAngle : mSnowAngle);
    }

    /**
     * This method set the snow life time of the animation. If the animation is playing, it is stopped.
     * After called one of this method the animation must be restarted manually with 7
     * restartWithNewConfiguration() method.
     *
     * @param snowTime must be greater or equal than 0, if set to a negative
     *                 value it is set to Constants.snowTime
     */
    public void setSnowTime(int snowTime) {
        this.mSnowTime = snowTime >= 0 ? snowTime : Constants.snowTime;
    }

    /**
     * This method set the rain particles for second. If the animation is playing, it is stopped.
     * After called one of this method the animation must be restarted manually with
     * restartWithNewConfiguration() method.
     *
     * @param rainParticles must be greater or equal than 0, if set to a negative
     *                      value it is set to Constants.rainParticles
     */
    public void setRainParticles(int rainParticles) {
        int prev = this.mRainParticles;
        this.mRainParticles = rainParticles >= 0 ? rainParticles : Constants.rainParticles;
        //MUST RELOAD --> avoid issue
      //  if (prev != this.mRainParticles)
        //    reloadNewConfiguration();
    }

    /**
     * Set The fps of the animation. Default is 30. Max settable is 99 and min is 8
     * If the animation is playing, it is stopped. After called one of this method the
     * animation must be restarted manually with restartWithNewConfiguration() method.
     *
     * @param fps number of fps between 8 and 99
     */
    public void setFPS(int fps) {
        if (mParticleSystem != null) {
            mParticleSystem.setFPS((fps > 7 && fps < 100) ? fps : Constants.fps);
            this.mFps = (fps > 7 && fps < 100) ? fps : Constants.fps;
            //Must cancel in order to avoid overlapping with particles
            cancelAnimation();
        }

    }

    /**
     * Return the fps set for the animation
     *
     * @return current fps
     */
    public int getFPS() {
        return mFps;
    }

    public void setRainAngle(int angle) {
        this.mRainAngle = angle > -181 && angle < 181 ? angle : Constants.rainAngle;
    }

    public void setSnowAngle(int angle) {
        this.mSnowAngle = angle > -181 && angle < 181 ? angle : Constants.snowAngle;
    }


    /**
     * Return the current type weather
     *
     * @return current type weather
     */
    public Constants.weatherStatus getCurrentWeather() {
        return mCurrentWeather;
    }

    /**
     * This method set the snow particles for second.
     * If the animation is playing, it is stopped. After called one of this method the
     * animation must be restarted manually with restartWithNewConfiguration() method.
     *
     * @param snowParticles must be greater or equal than 0, if set to a negative
     *                      value it is set to Constants.rainParticles
     */
    public void setSnowParticles(int snowParticles) {
        int prev = this.mSnowParticles;
        this.mSnowParticles = snowParticles >= 0 ? snowParticles : Constants.snowParticles;
        //MUST RELOAD --> avoid issue
     //   if (prev != this.mSnowParticles)
        //    reloadNewConfiguration();
    }

    /**
     * This method return true if animation is playing.
     *
     * @return if animation is playing
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Restore to the default configuration settings
     */
    public void resetConfiguration() {
        setRainTime(-1);
        setFadeOutTime(-1);
        setSnowTime(-1);
        setRainParticles(-1);
        setSnowParticles(-1);
        setFPS(-1);
        setRainAngle(-200);
        setSnowAngle(-200);
    }


}
