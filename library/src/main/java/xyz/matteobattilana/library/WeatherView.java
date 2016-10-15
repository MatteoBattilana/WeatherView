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
 */
public class WeatherView extends View {
    private int rainTime = Constants.rainTime;
    private int snowTime = Constants.snowTime;
    private int fadeOutTime = Constants.fadeOutTime;

    private ParticleSystem ps;
    private Constants.weatherStatus currentWeather = Constants.weatherStatus.SUN;
    Context mContext;
    Activity mActivity;

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        if (!isInEditMode()) {
            mActivity = (Activity) getContext();
            setWeather(Constants.weatherStatus.SUN);
            initOptions(context, attrs);
        }
    }

    private void initOptions(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WeatherView, 0, 0);
        int startingWeather, liveTime;
        try {
            startingWeather = typedArray.getInt(R.styleable.WeatherView_startingWeather, 0);
            liveTime = typedArray.getInt(R.styleable.WeatherView_liveTime, -1);
            fadeOutTime = typedArray.getInt(R.styleable.WeatherView_fadeOutTime, -1);


            setWeather(Constants.weatherStatus.values()[startingWeather], liveTime, Constants.fadeOutTime);
        } finally {
            typedArray.recycle();
        }
    }


    public void setWeather(Constants.weatherStatus status, int liveTime, int fadeOutTime) {
        setFadeOutTime(fadeOutTime);
        currentWeather = status;

        if (ps != null)
            ps.stopEmitting();
        switch (status) {
            case RAIN:
                setRainTime(liveTime);
                ps = new ParticleSystem(mActivity, 100, R.drawable.rain, rainTime);
                ps.setAcceleration(0.00013f, 96);
                ps.setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f);
                ps.setFadeOut(this.fadeOutTime, new AccelerateInterpolator());
                break;
            case SNOW:
                setSnowTime(liveTime);
                ps = new ParticleSystem(mActivity, 100, R.drawable.snow, snowTime);
                ps.setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f);
                ps.setFadeOut(this.fadeOutTime, new AccelerateInterpolator());
                break;
            default:
                break;
        }
        Log.e("INFO",fadeOutTime+" "+snowTime+" "+rainTime);
    }

    public void setWeather(Constants.weatherStatus status) {
        int liveTime = Constants.rainTime;
        switch (status) {
            case SNOW:
                liveTime = Constants.snowTime;
                break;
        }
        setWeather(status, liveTime,Constants.fadeOutTime);

    }

    public void setWeather(Constants.weatherStatus status, int liveTime){
        setWeather(status,liveTime,Constants.fadeOutTime);
    }

    public void restartWithNewConfiguration(){
        setWeather(currentWeather,currentWeather== Constants.weatherStatus.RAIN?rainTime:snowTime,fadeOutTime);
        startAnimation();
    }

    /**
     * Added a Runnable in order to avoid error during the animation. This method wait until the
     * view is loaded and then it shows the animation
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

    public void stopAnimation() {
        if (ps != null) {
            ps.cancel();
        }
    }

    public void setRainTime(int rainTime){
        this.rainTime = rainTime != -1 ? rainTime : Constants.rainTime;
    }

    public void setFadeOutTime(int fadeOutTime) {
        this.fadeOutTime = fadeOutTime != -1 ? fadeOutTime : Constants.fadeOutTime;
    }

    public void setSnowTime(int snowTime){
        this.snowTime=snowTime!=-1?snowTime: Constants.snowTime;
    }

    public void resetConfiguration(){
        this.snowTime=Constants.snowTime;
        this.rainTime=Constants.rainTime;
        this.fadeOutTime=Constants.fadeOutTime;
    }
}
