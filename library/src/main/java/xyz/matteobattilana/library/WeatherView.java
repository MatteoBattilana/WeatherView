package xyz.matteobattilana.library;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
public class WeatherView extends View implements SensorEventListener {
    private int rainTime = Constants.rainTime;
    private int snowTime = Constants.snowTime;
    private int fadeOutTime = Constants.fadeOutTime;

    private int rainParticles = Constants.rainParticles;
    private int snowParticles = Constants.snowParticles;

    private ParticleSystem ps;
    private Constants.weatherStatus currentWeather = Constants.weatherStatus.SUN;
    Context mContext;
    Activity mActivity;
    boolean isPlaying = false;

    //the Sensor Manager
    private SensorManager mSensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;

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
            mSensorManager = (SensorManager) mActivity.getSystemService(mActivity.SENSOR_SERVICE);
            accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            initListeners();
            //set the default to SUN
            setWeather(Constants.weatherStatus.SUN);
            initOptions(context, attrs);
        }
    }

    private void initListeners()
    {
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_FASTEST);
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
        int startingWeather, lifeTime, fadeOutTime, numParticles;
        try {
            //Defatul 0 --> SUN
            startingWeather = typedArray.getInt(R.styleable.WeatherView_startingWeather, 0);
            //If there is not a lifeTime and/or fadeOutTime it reset to
            //default. If -1 it reset the value
            lifeTime = typedArray.getInt(R.styleable.WeatherView_lifeTime, -1);
            fadeOutTime = typedArray.getInt(R.styleable.WeatherView_fadeOutTime, -1);
            numParticles = typedArray.getInt(R.styleable.WeatherView_numParticles, -1);

            setWeather(Constants.weatherStatus.values()[startingWeather], lifeTime, fadeOutTime, numParticles);
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
     */
    public void setWeather(Constants.weatherStatus status, int lifeTime, int fadeOutTime, int numParticles) {
        currentWeather = status;

        setFadeOutTime(fadeOutTime);
        stopAnimation();

        switch (status) {
            case RAIN:
                setRainTime(lifeTime);
                setRainParticles(numParticles);
                ps = new ParticleSystem(mActivity, rainParticles * rainTime / 1000, R.drawable.rain, rainTime);
                ps.setAcceleration(0.00013f, 96);
                ps.setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f);
                ps.setFadeOut(this.fadeOutTime, new AccelerateInterpolator());
                break;
            case SNOW:
                setSnowTime(lifeTime);
                setSnowParticles(numParticles);
                ps = new ParticleSystem(mActivity, snowParticles * snowTime / 1000, R.drawable.snow, snowTime);
                ps.setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f);
                ps.setFadeOut(this.fadeOutTime, new AccelerateInterpolator());
                break;
            default:
                break;
        }
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
        setWeather(status, lifeTime, fadeOutTime, -1);
    }

    /**
     * This constructor set the weather specifying the type
     *
     * @param status set the weatherStatus {RAIN,SUN,SNOW}
     */
    public void setWeather(Constants.weatherStatus status) {
        setWeather(status, -1, -1, -1);
    }

    /**
     * This constructor set the weather specifying the type and the life time
     *
     * @param status   set the weatherStatus {RAIN,SUN,SNOW}
     * @param lifeTime must be greater or equals than 0, if set to a negative
     *                 value it is set to the default value.
     */
    public void setWeather(Constants.weatherStatus status, int lifeTime) {
        setWeather(status, lifeTime, -1, -1);
    }

    /**
     * If the animation is playing the new configuration is loaded stopping the
     * current animation. Then automatically restart the animation
     */
    public void restartWithNewConfiguration() {
        setWeather(currentWeather, currentWeather == Constants.weatherStatus.RAIN ? rainTime : snowTime, fadeOutTime, currentWeather == Constants.weatherStatus.RAIN ? rainParticles : snowParticles);
        startAnimation();
    }

    /**
     * Reload configuration. If the animation was playing it continue the animation
     * with the new configuration. Used only in setRainParticles() and
     * setSnowParticles()
     */
    private void reloadNewConfiguration() {
        boolean wasPlaying = isPlaying();
        setWeather(currentWeather, currentWeather == Constants.weatherStatus.RAIN ? rainTime : snowTime, fadeOutTime, currentWeather == Constants.weatherStatus.RAIN ? rainParticles : snowParticles);
        if (wasPlaying)
            startAnimation();
    }

    /**
     * Added a Runnable in order to avoid error during the animation. This method
     * wait until the view is loaded and then it plays the animation
     */
    public void startAnimation() {
        if (ps != null) {
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
        switch (currentWeather) {
            case RAIN:
                ps.emitWithGravity(this, Gravity.BOTTOM, rainParticles);
                break;
            case SNOW:
                ps.emitWithGravity(this, Gravity.BOTTOM, snowParticles);
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
        if (ps != null) {
            ps.cancel();
            isPlaying = false;
        }
    }

    /**
     * Pause the animation. If there are some particles playing the animation
     * they would not stopped by this method.
     */
    public void stopAnimation() {
        if (ps != null) {
            ps.stopEmitting();
            isPlaying = false;
        }
    }

    /**
     * This method set the rain life time of the animation. If the animation is
     * playing, it must stopped with stopAnimation() method or call
     * restartWithNewConfiguration(). After called restartWithNewConfiguration()
     * the animation is automatically restarted.
     *
     * @param rainTime must be greater or equal than 0, if set to a negative
     *                 value it is set to Constants.rainTime
     */
    public void setRainTime(int rainTime) {
        this.rainTime = rainTime >= 0 ? rainTime : Constants.rainTime;
    }

    /**
     * This method set the fade out time of the animation. If the animation is
     * playing, it must stopped with stopAnimation() method or call
     * restartWithNewConfiguration(). After called one of this method the
     * animation must be restarted manually.
     *
     * @param fadeOutTime must be greater or equal than 0, if set to a negative
     *                    value it is set to Constants.fadeOutTime
     */
    public void setFadeOutTime(int fadeOutTime) {
        this.fadeOutTime = fadeOutTime >= 0 ? fadeOutTime : Constants.fadeOutTime;
    }

    /**
     * This method set the snow life time of the animation. If the animation is
     * playing, it must stopped with stopAnimation() method or call
     * restartWithNewConfiguration(). After called one of this method the
     * animation must be restarted manually.
     *
     * @param snowTime must be greater or equal than 0, if set to a negative
     *                 value it is set to Constants.snowTime
     */
    public void setSnowTime(int snowTime) {
        this.snowTime = snowTime >= 0 ? snowTime : Constants.snowTime;

    }

    /**
     * This method set the rain particles for second. If the animation is
     * playing, it must stopped with stopAnimation() method or call
     * restartWithNewConfiguration(). After called one of this method the
     * animation must be restarted manually.
     *
     * @param rainParticles must be greater or equal than 0, if set to a negative
     *                      value it is set to Constants.rainParticles
     */
    public void setRainParticles(int rainParticles) {
        int prev = this.rainParticles;
        this.rainParticles = rainParticles >= 0 ? rainParticles : Constants.rainParticles;
        //MUST RELOAD --> avoid issue
        if (prev != this.rainParticles)
            reloadNewConfiguration();

    }

    /**
     * This method set the snow particles for second. If the animation is
     * playing, it must stopped with stopAnimation() method or call
     * restartWithNewConfiguration(). After called one of this method the
     * animation must be restarted manually.
     *
     * @param snowParticles must be greater or equal than 0, if set to a negative
     *                      value it is set to Constants.rainParticles
     */
    public void setSnowParticles(int snowParticles) {
        int prev = this.snowParticles;
        this.snowParticles = snowParticles >= 0 ? snowParticles : Constants.snowParticles;
        //MUST RELOAD --> avoid issue
        if (prev != this.snowParticles)
            reloadNewConfiguration();
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
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor != accelerometer)
            return;

        double aX= event.values[0];
        double aY= event.values[1];
        //aZ= event.values[2];
        double angle = Math.atan2(aX, aY)/(Math.PI/180);
        angle=angle>0?angle:angle*-1;
        angle+=90;
        ps.setSpeedModuleAndAngleRange(0f, 0.3f, (int)angle, (int)angle);
        Log.e("sadsa",angle+ " asd");
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do nothing.

    }


}
