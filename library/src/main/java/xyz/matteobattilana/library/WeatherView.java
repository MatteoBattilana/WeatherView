package xyz.matteobattilana.library;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
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

    /*
    HugoGresse: It will be awesome to add gravity to each particle.
    Changing angle is a quick idea but by adding an independant gravity using device sensor on each
    particle we will respond perfectly and the animation will also be perfect.
    */

    private boolean isOrientationActive = Constants.isOrientationActive;
    int lastAngle = -1;
    // Gravity rotational data
    private float gravity[];
    // Magnetic rotational data
    private float magnetic[]; //for magnetic rotational data
    private float accels[] = new float[3];
    private float mags[] = new float[3];
    private float[] values = new float[3];

    // azimuth, pitch and roll
    private float azimuth;
    private float pitch;
    private float roll;


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

            initOptions(context, attrs);
        }

        //test add acc
        SensorManager sManager = (SensorManager) mContext.getSystemService(mContext.SENSOR_SERVICE);
        sManager.registerListener(mySensorEventListener, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sManager.registerListener(mySensorEventListener, sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
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
        boolean isOrientationActive;
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
            isOrientationActive = typedArray.getBoolean(R.styleable.WeatherView_orientationActive, false);

            //MUST CALL INSIDE TRY CATCH
            setWeather(Constants.weatherStatus.values()[startingWeather])
                    .setLifeTime(lifeTime)
                    .setFadeOutTime(fadeOutTime)
                    .setParticles(numParticles)
                    .setFPS(fps)
                    .setIsOrientationActive(isOrientationActive)
                    .setAngle(angle);

        } finally {
            typedArray.recycle();
        }
    }


    /**
     * This constructor set the weather specifying the type
     *
     * @param status set the weatherStatus {RAIN,SUN,SNOW}
     * @return the current WeatherView instance
     */
    public WeatherView setWeather(Constants.weatherStatus status) {
        mCurrentWeather = status;
        return this;
    }

    /**
     * Set the current particle angle during animation
     *
     * @param angle
     * @return the current WeatherView instance
     */
    public WeatherView setAngle(int angle) {
        switch (getCurrentWeather()) {
            case RAIN:
                setRainAngle(angle);
                break;
            case SNOW:
                setSnowAngle(angle);
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
    public WeatherView setLifeTime(int lifeTime) {
        switch (getCurrentWeather()) {
            case RAIN:
                setRainTime(lifeTime);
                break;
            case SNOW:
                setSnowTime(lifeTime);
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
    public WeatherView setParticles(int numParticles) {
        switch (getCurrentWeather()) {
            case RAIN:
                setRainParticles(numParticles);
                break;
            case SNOW:
                setSnowParticles(numParticles);
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
                mParticleSystem = new ParticleSystem(mActivity, mRainParticles * mRainTime / 1000, R.drawable.rain, mRainTime)
                        .setAcceleration(0.00013f, 90 - mRainAngle)
                        .setInitialRotation(-mRainAngle)
                        .setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f)
                        .setFadeOut(this.mFadeOutTime, new AccelerateInterpolator());
                break;
            case SNOW:
                mParticleSystem = new ParticleSystem(mActivity, mSnowParticles * mSnowTime / 1000, R.drawable.snow, mSnowTime)
                        .setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f)
                        .setInitialRotation(-mSnowAngle)
                        .setFadeOut(this.mFadeOutTime, new AccelerateInterpolator());
                break;
            default:
                break;
        }

        mParticleSystem.setFPS(getFPS());

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
     */
    private void setRainTime(int rainTime) {
        this.mRainTime = rainTime >= 0 ? rainTime : Constants.rainTime;
    }

    /**
     * This method set the fade out time of the animation in ms.
     *
     * @param fadeOutTime must be greater or equal than 0, if set to a negative
     *                    value it is set to Constants.fadeOutTime
     * @return the current WeatherView instance
     */
    public WeatherView setFadeOutTime(int fadeOutTime) {
        this.mFadeOutTime = fadeOutTime >= 0 ? fadeOutTime : Constants.fadeOutTime;
        return this;
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
        return (getCurrentWeather() == Constants.weatherStatus.RAIN ? mRainTime : mSnowTime);
    }

    /**
     * Return rainParticles or snowParticles
     *
     * @return rainParticles or snowParticles
     */
    public int getParticles() {
        return (getCurrentWeather() == Constants.weatherStatus.RAIN ? mRainParticles : mSnowParticles);
    }


    /**
     * Return angle of current animation
     *
     * @return angle of current animation
     */
    public int getAngle() {
        return (getCurrentWeather() == Constants.weatherStatus.RAIN ? mRainAngle : mSnowAngle);
    }

    /**
     * This method set the snow life time of the animation.
     *
     * @param snowTime must be greater or equal than 0, if set to a negative
     *                 value it is set to Constants.snowTime
     */
    private void setSnowTime(int snowTime) {
        this.mSnowTime = snowTime >= 0 ? snowTime : Constants.snowTime;
    }

    /**
     * This method set the rain particles for second.
     *
     * @param rainParticles must be greater or equal than 0, if set to a negative
     *                      value it is set to Constants.rainParticles
     */
    private void setRainParticles(int rainParticles) {
        this.mRainParticles = rainParticles >= 0 ? rainParticles : Constants.rainParticles;
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
     * Internal method for set the rain particle angle
     *
     * @param angle
     */
    private void setRainAngle(int angle) {
        this.mRainAngle = angle > -181 && angle < 181 ? angle : Constants.rainAngle;
    }

    /**
     * Internal method for set the snow particle angle
     *
     * @param angle
     */
    private void setSnowAngle(int angle) {
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
     *
     * @param snowParticles must be greater or equal than 0, if set to a negative
     *                      value it is set to Constants.rainParticles
     */
    private void setSnowParticles(int snowParticles) {
        this.mSnowParticles = snowParticles >= 0 ? snowParticles : Constants.snowParticles;
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
     * Enable or disable auto orientation with gyro and acc sensor
     *
     * @param isOrientationActive enable or disable auto orientation with gyro and acc sensor
     * @return the current WeatherView instance
     */
    public WeatherView setIsOrientationActive(boolean isOrientationActive) {
        this.isOrientationActive = isOrientationActive;
        return this;
    }

    /**
     * Return true if the auto orientation is enabled
     *
     * @return true if the auto orientation is enabled
     */
    public boolean getIsOrientationActive() {
        return isOrientationActive;
    }

    /**
     * Restore to the default configuration settings
     *
     * @return the current WeatherView instance
     */
    public WeatherView resetConfiguration() {
        setRainTime(-1);
        setFadeOutTime(-1);
        setSnowTime(-1);
        setRainParticles(-1);
        setSnowParticles(-1);
        setFPS(-1);
        setRainAngle(-200);
        setSnowAngle(-200);
        return this;
    }


    //Must put in a separate class

    private SensorEventListener mySensorEventListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_MAGNETIC_FIELD:
                    mags = event.values;
                    break;
                case Sensor.TYPE_ACCELEROMETER:
                    accels = event.values;
                    break;
            }

            if (mags != null && accels != null) {
                gravity = new float[9];
                magnetic = new float[9];
                SensorManager.getRotationMatrix(gravity, magnetic, accels, mags);
                float[] outGravity = new float[9];
                SensorManager.remapCoordinateSystem(gravity, SensorManager.AXIS_X, SensorManager.AXIS_Z, outGravity);
                SensorManager.getOrientation(outGravity, values);

                azimuth = values[0] * 57.2957795f;
                pitch = values[1] * 57.2957795f;
                roll = values[2] * 57.2957795f;
                roll += 90;
                mags = null;
                accels = null;
                updateOrientation((int) roll);

            }
        }
    };


    /**
     * Internal method for update the distance
     *
     * @param angle
     */

    private void updateOrientation(int angle) {

        if (angle > 90 && Math.abs(angle - 90) >= Constants.angleRangeRead)
            angle = 90 + Constants.angleRangeRead;
        else if (angle < 90 && Math.abs(angle - 90) >= 20)
            angle = 90 - Constants.angleRangeRead;
        if (Math.abs(angle - lastAngle) > Constants.angleRangeUpdate) {
            mParticleSystem.setSpeedModuleAndAngleRange(0.05f, 0.1f, 180 - angle, 180 - angle);
            mParticleSystem.updateAngle(90 - angle);
            lastAngle = angle;
        }

    }


}
