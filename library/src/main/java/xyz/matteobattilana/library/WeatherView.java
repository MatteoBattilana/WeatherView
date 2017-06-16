package xyz.matteobattilana.library;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.plattysoft.leonids.ParticleSystem;

import xyz.matteobattilana.library.Common.Constants;

/**
 * Created by MatteoB on 14/10/2016.
 * This is an extended View.
 */
public class WeatherView extends View {

    //RAIN
    private int mRainTime = Constants.rainTime;
    private int mRainFadeOutTime = Constants.rainFadeOutTime;
    private int mRainParticles = Constants.rainParticles;
    private int mRainAngle = Constants.rainAngle;

    //SNOW
    private int mSnowTime = Constants.snowTime;
    private int mSnowFadeOutTime = Constants.snowFadeOutTime;
    private int mSnowParticles = Constants.snowParticles;
    private int mSnowAngle = Constants.snowAngle;

    //COMMON
    private int mFps = Constants.fps;
    private Constants.WeatherStatus mCurrentWeather = Constants.WeatherStatus.SUN;
    private boolean isPlaying = false;
    private Constants.OrientationStatus mOrientationMode = Constants.OrientationStatus.ENABLE;

    //INSTANCE
    private ParticleSystem mParticleSystem;
    private Context mContext;
    private Activity mActivity;

    /*
    HugoGresse: It will be awesome to add gravity to each particle.
    Changing angle is a quick idea but by adding an independant gravity using device sensor on each
    particle we will respond perfectly and the animation will also be perfect.
    */
    //For reading acc sensor
    WeatherViewSensorEventListener mWeatherViewSensorEventListener;


    /**
     * Use to get onPause and onResume interrupt to avid polling on acc sensor
     *
     * @param changedView
     * @param visibility
     */
    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        //Used to avoid issue during the design
        if (!isInEditMode()) {
            if (visibility == View.VISIBLE) resumeOrientation();
            else pauseOrientation();
        }
    }


    /**
     * This method initialize the WeatherView to SUN. No animation is showed.
     * If you want to start the animation after set a different weather with the
     * setWeather(WeatherStatus mWeatherStatus) method you must call
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

            //intialize SensorEventListener
            mWeatherViewSensorEventListener = new WeatherViewSensorEventListener(mContext, this, mOrientationMode);

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
        int startingWeather, lifeTime, fadeOutTime, numParticles, fps, angle, startingOrientation;
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

            startingOrientation = typedArray.getInt(R.styleable.WeatherView_orientationMode, Constants.isOrientationActive?0:1);

            //MUST CALL INSIDE TRY CATCH
            setWeather(Constants.WeatherStatus.values()[startingWeather])
                    .setCurrentLifeTime(lifeTime)
                    .setCurrentFadeOutTime(fadeOutTime)
                    .setCurrentParticles(numParticles)
                    .setFPS(fps)
                    .setOrientationMode(Constants.OrientationStatus.values()[startingOrientation])
                    .setCurrentAngle(angle);

        } finally {
            typedArray.recycle();
        }
    }


    /**
     * This constructor set the weather specifying the type
     *
     * @param status set the WeatherStatus {RAIN,SUN,SNOW}
     * @return the current WeatherView instance
     */
    public WeatherView setWeather(Constants.WeatherStatus status) {
        mCurrentWeather = status;
        return this;
    }

    /**
     * Set the current particle angle during animation
     *
     * @param angle
     * @return the current WeatherView instance
     */
    public WeatherView setCurrentAngle(int angle) {
        switch (getCurrentWeather()) {
            case RAIN:
                setRainAngle(angle);
                break;
            case SNOW:
                setSnowAngle(angle);
                break;
        }
        return this;
    }

    /**
     * Set the current life time of a single particle during the animation
     *
     * @param lifeTime must be greater or equal than 0, if set to a negative
     *                 value it is set to Constants.rainTime / Constants.snowTime
     * @return the current WeatherView instance
     */
    public WeatherView setCurrentLifeTime(int lifeTime) {
        switch (getCurrentWeather()) {
            case RAIN:
                setRainTime(lifeTime);
                break;
            case SNOW:
                setSnowTime(lifeTime);
                break;
        }
        return this;
    }

    /**
     * Set the number of particles during the animation
     *
     * @param numParticles must be greater or equal than 0, if set to a negative
     *                     value it is set to Constants.rainParticles / Constants.snowParticles
     * @return the current WeatherView instance
     */
    public WeatherView setCurrentParticles(int numParticles) {
        switch (getCurrentWeather()) {
            case RAIN:
                setRainParticles(numParticles);
                break;
            case SNOW:
                setSnowParticles(numParticles);
                break;
        }
        return this;
    }


    /**
     * Added a Runnable in order to avoid error during the animation. This method
     * wait until the view is loaded and then it plays the animation
     */
    public void startAnimation() {
        stopAnimation();
        switch (getCurrentWeather()) {
            case RAIN:
                mParticleSystem = new ParticleSystem((ViewGroup) this.getParent(), mRainParticles * mRainTime / 1000, ContextCompat.getDrawable(mActivity, R.drawable.rain), mRainTime)
                        //.setAcceleration(0.00013f, 90 - mRainAngle)
                        //.setInitialRotation(-mRainAngle)
                        //.setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f)
                        .setFadeOut(mRainFadeOutTime, new AccelerateInterpolator());
                        updateAngle(90-mRainAngle);
                break;
            case SNOW:
                mParticleSystem = new ParticleSystem((ViewGroup) this.getParent(), mSnowParticles * mSnowTime / 1000, ContextCompat.getDrawable(mActivity, R.drawable.snow), mSnowTime)
                        //.setAcceleration(0.000001f, 90 - mSnowAngle)
                        //.setInitialRotation(-mSnowAngle)
                        //.setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f)
                        .setFadeOut(mSnowFadeOutTime, new AccelerateInterpolator());
                        updateAngle(90-mSnowAngle);
                break;
            default:
                break;
        }


        if (mParticleSystem != null) {

            ParticleSystem.setFPS(getFPS());

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
        }
        isPlaying = true;
    }

    /**
     * Stop the animation. If there are some particles playing the animation
     * they wouldn't stopped by this method.
     *
     * @return the current WeatherView instance
     */
    public WeatherView cancelAnimation() {
        if (mParticleSystem != null) {
            mParticleSystem.cancel();
            isPlaying = false;
        }
        return this;
    }

    /**
     * Stop the animation. If there are some particles playing the animation
     * they would not stopped by this method.
     */
    public void stopAnimation() {
        if (mParticleSystem != null) {
            mParticleSystem.stopEmitting();
            isPlaying = false;
        }
    }

    /**
     * This method set the rain life time of the animation.
     *
     * @param rainTime must be greater or equal than 0, if set to a negative
     *                 value it is set to Constants.rainTime
     * @return the current WeatherView instance
     */
    public WeatherView setRainTime(int rainTime) {
        this.mRainTime = rainTime >= 0 ? rainTime : Constants.rainTime;
        return this;
    }

    /**
     * This method set the fade out time of the animation in ms.
     *
     * @param fadeOutTime must be greater or equal than 0, if set to a negative
     *                    value it is set to Constants.fadeOutTime
     * @return the current WeatherView instance
     */
    public WeatherView setCurrentFadeOutTime(int fadeOutTime) {
        switch (getCurrentWeather()) {
            case RAIN:
                setRainFadeOutTime(fadeOutTime);
                break;
            case SNOW:
                setSnowFadeOutTime(fadeOutTime);
                break;
        }
        return this;
    }

    /**
     * This method set the fade out time of the rain animation in ms.
     *
     * @param fadeOutTime must be greater or equal than 0, if set to a negative
     *                    value it is set to Constants.rainFadeOutTime
     * @return the current WeatherView instance
     */
    public WeatherView setRainFadeOutTime(int fadeOutTime) {
        this.mRainFadeOutTime = fadeOutTime >= 0 ? fadeOutTime : Constants.rainFadeOutTime;
        return this;
    }

    /**
     * This method set the fade out time of the snow animation in ms.
     *
     * @param fadeOutTime must be greater or equal than 0, if set to a negative
     *                    value it is set to Constants.snowFadeOutTime
     * @return the current WeatherView instance
     */
    public WeatherView setSnowFadeOutTime(int fadeOutTime) {
        this.mSnowFadeOutTime = fadeOutTime >= 0 ? fadeOutTime : Constants.snowFadeOutTime;
        return this;
    }

    /**
     * Return the current fadeOutTime time in ms
     *
     * @return fade out time in ms
     */
    public int getCurrentFadeOutTime() {
        return (getCurrentWeather() == Constants.WeatherStatus.RAIN ? mRainFadeOutTime : mSnowFadeOutTime);
    }

    /**
     * Return the rain fadeOutTime time in ms
     *
     * @return fade out time in ms
     */
    public int getRainFadeOutTime() {
        return mRainFadeOutTime;
    }

    /**
     * Return the snow fadeOutTime time in ms
     *
     * @return fade out time in ms
     */
    public int getSnowFadeOutTime() {
       return mSnowFadeOutTime;
    }

    /**
     * Return rainTime or snowTime in ms
     *
     * @return rainTime or snowTime in ms
     */
    public int getCurrentLifeTime() {
        return (getCurrentWeather() == Constants.WeatherStatus.RAIN ? mRainTime : mSnowTime);
    }

    /**
     * Return the snow particles life time in ms
     *
     * @return the snow particles life time in ms
     */
    public int getSnowLifeTime() {
        return mSnowTime;
    }

    /**
     * Return the rain particles life time in ms
     *
     * @return the rain particles life time in ms
     */
    public int getRainLifeTime() {
        return mRainTime;
    }

    /**
     * Return current number of particles
     *
     * @return current number of particles
     */
    public int getCurrentParticles() {
        return (getCurrentWeather() == Constants.WeatherStatus.RAIN ? mRainParticles : mSnowParticles);
    }

    /**
     * Return the snow number of particles
     *
     * @return the snow number of particles
     */
    public int getSnowParticles() {
        return mSnowParticles;
    }

    /**
     * Return the rain number of particles
     *
     * @return the rain number of particles
     */
    public int getRainParticles() {
        return mRainParticles;
    }


    /**
     * Return angle of current animation
     *
     * @return angle of current animation
     */
    public int getCurrentAngle() {
        return (getCurrentWeather() == Constants.WeatherStatus.RAIN ? mRainAngle : mSnowAngle);
    }

    /**
     * Return angle of the snow animation
     *
     * @return angle of the snow animation
     */
    public int getSnowAngle() {
        return mSnowAngle;
    }

    /**
     * Return angle of the rain animation
     *
     * @return angle of the rain animation
     */
    public int getRainAngle() {
        return mRainAngle;
    }

    /**
     * This method set the snow life time of the animation.
     *
     * @param snowTime must be greater or equal than 0, if set to a negative
     *                 value it is set to Constants.snowTime
     * @return the current WeatherView instance
     */
    public WeatherView setSnowTime(int snowTime) {
        this.mSnowTime = snowTime >= 0 ? snowTime : Constants.snowTime;
        return this;
    }

    /**
     * This method set the rain particles for second.
     *
     * @param rainParticles must be greater or equal than 0, if set to a negative
     *                      value it is set to Constants.rainParticles
     * @return the current WeatherView instance
     */
    public WeatherView setRainParticles(int rainParticles) {
        this.mRainParticles = rainParticles >= 0 ? rainParticles : Constants.rainParticles;
        return this;
    }

    /**
     * Set The fps of the animation. Default is 30. Max settable is 99 and min is 8
     * If the animation is playing, it is stopped.
     *
     * @param fps number of fps between 8 and 99
     * @return the current WeatherView instance
     */
    public WeatherView setFPS(int fps) {
        this.mFps = (fps > 7 && fps < 100) ? fps : Constants.fps;

        //Must cancel in order to avoid overlapping with particles
        if (mParticleSystem != null) {
            cancelAnimation();
        }
        return this;
    }

    /**
     * Return the fps set for the animation
     *
     * @return current fps
     */
    public int getFPS() {
        return mFps;
    }

    /**
     * Method for set the rain particle angle
     *
     * @param angle
     * @return the current WeatherView instance
     */
    public WeatherView setRainAngle(int angle) {
        this.mRainAngle = angle > -181 && angle < 181 ? angle : Constants.rainAngle;
        return this;
    }

    /**
     * Method for set the snow particle angle
     *
     * @param angle
     * @return the current WeatherView instance
     */
    public WeatherView setSnowAngle(int angle) {
        this.mSnowAngle = angle > -181 && angle < 181 ? angle : Constants.snowAngle;
        return this;
    }


    /**
     * Return the current type weather
     *
     * @return current type weather
     */
    public Constants.WeatherStatus getCurrentWeather() {
        return mCurrentWeather;
    }

    /**
     * This method set the snow particles for second.
     *
     * @param snowParticles must be greater or equal than 0, if set to a negative
     *                      value it is set to Constants.rainParticles
     * @return the current WeatherView instance
     */
    public WeatherView setSnowParticles(int snowParticles) {
        this.mSnowParticles = snowParticles >= 0 ? snowParticles : Constants.snowParticles;
        return this;
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
     * Return the current orientation mode
     *
     * @return the current orientation mode
     */
    public Constants.OrientationStatus getOrientationMode() {
        return mOrientationMode;
    }

    /**
     * Restore to the default configuration settings
     *
     * @return the current WeatherView instance
     */
    public WeatherView resetConfiguration() {
        setRainTime(-1);
        setSnowFadeOutTime(-1);
        setRainFadeOutTime(-1);
        setSnowTime(-1);
        setRainParticles(-1);
        setSnowParticles(-1);
        setFPS(-1);
        setRainAngle(-200);
        setSnowAngle(-200);
        return this;
    }

    /**
     * Internal method to change che particle angle
     *
     * @param angle in degrees
     */
    void updateAngle(int angle) {
        if(mParticleSystem!=null) {
            mParticleSystem.setSpeedModuleAndAngleRange(0.05f, 0.1f, 180 - angle, 180 - angle);
            if (getCurrentWeather() == Constants.WeatherStatus.RAIN) {
                mParticleSystem.setAcceleration(0.00013f, 180 - angle);
            }
        }
    }

    /**
     * This method allow to set if the particle change their direction based on the phone orientation
     *
     * @param orientationMode can be ENABLE or DISABLE
     * @return current instance of WeatherView
     */
    public WeatherView setOrientationMode(Constants.OrientationStatus orientationMode) {
        mOrientationMode = orientationMode;

        switch (orientationMode) {
            case ENABLE:
                mWeatherViewSensorEventListener.start();
                break;
            case DISABLE:
                mWeatherViewSensorEventListener.stop();
                break;
        }
        return this;
    }

    /**
     * Use to resume the animation when the onResume method is called.
     * I made this to avoid polling on accelerometer
     */
    private void resumeOrientation() {
        if (mOrientationMode != null && mOrientationMode == Constants.OrientationStatus.ENABLE)
            mWeatherViewSensorEventListener.start();

    }

    /**
     * Use to pause the animation when the onPause method is called.
     * I made this to avoid polling on accelerometer
     */
    private void pauseOrientation() {
        if (mWeatherViewSensorEventListener != null)
            mWeatherViewSensorEventListener.stop();
    }

}
