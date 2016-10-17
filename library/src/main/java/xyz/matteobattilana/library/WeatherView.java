package it.unitn;

/**
 *
 * @author matteo.battilana
 */
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
    private int rainTime = Constants.rainTime;
    private int snowTime = Constants.snowTime;
    private int fadeOutTime = Constants.fadeOutTime;
    
    
    private ParticleSystem ps;
    private Constants.weatherStatus currentWeather = Constants.weatherStatus.SUN;
    Context mContext;
    Activity mActivity;
    
    /**
     * This method initialize the WeatherView to SUN. No animation is showed.
     * If you want to start the animation after set a different weather with the
     * setWeather(weatherStatus mWeatherStatus) method you must call 
     * startAnimation().
     * @param context Context from the application
     * @param attrs Attributes
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
     * @param context Context from the constructor
     * @param attrs Attributest from the constructor
     */
    private void initOptions(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WeatherView, 0, 0);
        int startingWeather, lifeTime,fadeOutTime;
        try {
            //Defatul 0 --> SUN
            startingWeather = typedArray.getInt(R.styleable.WeatherView_startingWeather, 0);
            //If there is not a lifeTime and/or fadeOutTime it reset to 
            //default. If -1 it reset the value
            lifeTime = typedArray.getInt(R.styleable.WeatherView_lifeTime, -1);
            fadeOutTime = typedArray.getInt(R.styleable.WeatherView_fadeOutTime, -1);

            setWeather(Constants.weatherStatus.values()[startingWeather], lifeTime, fadeOutTime);
        } finally {
            typedArray.recycle();
        }
    }

    /**
     * This constructor set the weather specifying the type, the life time and
     * the fade out time
     * @param status set the weatherStatus {RAIN,SUN,SNOW}
     * @param lifeTime must be greater or equals than 0, if set to a negative
     * value it is set to the default value.
     * @param fadeOutTime must be greater or equals than 0, if set to a negative
     * value it is set to the default value.
     */
    public void setWeather(Constants.weatherStatus status, int lifeTime, int fadeOutTime) {
        setFadeOutTime(fadeOutTime);
        currentWeather = status;

        stopAnimation();
        switch (status) {
            case RAIN:
                setRainTime(lifeTime);
                ps = new ParticleSystem(mActivity, 100, R.drawable.rain, rainTime);
                ps.setAcceleration(0.00013f, 96);
                ps.setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f);
                ps.setFadeOut(this.fadeOutTime, new AccelerateInterpolator());
                break;
            case SNOW:
                setSnowTime(lifeTime);
                ps = new ParticleSystem(mActivity, 100, R.drawable.snow, snowTime);
                ps.setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f);
                ps.setFadeOut(this.fadeOutTime, new AccelerateInterpolator());
                break;
            default:
                break;
        }
        Log.e("INFO",fadeOutTime+" "+snowTime+" "+rainTime);
    }
    
    /**
     * This constructor set the weather specifying the type
     * @param status set the weatherStatus {RAIN,SUN,SNOW}
     */
    public void setWeather(Constants.weatherStatus status) {
        int lifeTime = Constants.rainTime;
        switch (status) {
            case SNOW:
                lifeTime = Constants.snowTime;
                break;
        }
        setWeather(status, lifeTime,Constants.fadeOutTime);
    }

    /**
     * This constructor set the weather specifying the type and the life time
     * @param status set the weatherStatus {RAIN,SUN,SNOW}
     * @param lifeTime must be greater or equals than 0, if set to a negative
     * value it is set to the default value.
     */
    public void setWeather(Constants.weatherStatus status, int lifeTime){
        setWeather(status,lifeTime,Constants.fadeOutTime);
    }

    /**
     * If the animation is playing the new configuration is loaded stopping the
     * current animation. Then automatically restart the animation
     */
    public void restartWithNewConfiguration(){
        setWeather(currentWeather,currentWeather== Constants.weatherStatus.RAIN?rainTime:snowTime,fadeOutTime);
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
                    if (width != 0 && height != 0)
                        emitParticles();
                }
            });
        }
    }

    /**
     * Internal method for start the animation
     */
    private void emitParticles() {
        switch (currentWeather) {
            case RAIN:
                ps.emitWithGravity(this, Gravity.BOTTOM, 34);
                break;
            case SNOW:
                ps.emitWithGravity(this, Gravity.BOTTOM, 15);
                break;
            default:
                break;
        }
    }

    /**
     * Stop the animation.
     */
    public void cancelAnimation() {
        if (ps != null) {
            ps.cancel();
        }
    }
    
    /**
     * Pause the animation. If there are some particles playing the animation
     * they would not stopped by this method.
     */
    public void stopAnimation() {
        if (ps != null) {
            ps.stopEmitting();
        }
    }
    
    /**
     * This method set the rain life time of the animation. If the animation is
     * playing, it must stopped with stopAnimation() method or call
     * restartWithNewConfiguration(). After called restartWithNewConfiguration()
     * the animation is automatically restarted.
     * @param rainTime must be greater or equal than 0, if set to a negative
     * value it is set to Constants.rainTime
     */
    public void setRainTime(int rainTime){
        this.rainTime = rainTime >= 0 ? rainTime : Constants.rainTime;
    }

    /**
     * This method set the fade out time of the animation. If the animation is
     * playing, it must stopped with stopAnimation() method or call
     * restartWithNewConfiguration(). After called one of this method the
     * animation must be restarted manually.
     * @param fadeOutTime must be greater or equal than 0, if set to a negative
     * value it is set to Constants.fadeOutTime
     */
    public void setFadeOutTime(int fadeOutTime) {
        this.fadeOutTime = fadeOutTime >= 0 ? fadeOutTime : Constants.fadeOutTime;
    }

      /**
     * This method set the snow life time of the animation. If the animation is
     * playing, it must stopped with stopAnimation() method or call
     * restartWithNewConfiguration(). After called one of this method the
     * animation must be restarted manually.
     * @param snowTime must be greater or equal than 0, if set to a negative
     * value it is set to Constants.snowTime
     */
    public void setSnowTime(int snowTime){
        this.snowTime = snowTime >= 0 ? snowTime : Constants.snowTime;
    }

    /**
     * Restore to the default configuration settings
     */
    public void resetConfiguration(){
        setRainTime(-1);
        setFadeOutTime(-1);
        setSnowTime(-1);
    }
}
