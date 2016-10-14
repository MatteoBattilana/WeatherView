package xyz.matteobattilana.library;


import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.plattysoft.leonids.ParticleSystem;

/**
 * Created by MatteoB on 14/10/2016.
 */
public class WeatherView extends View {
    private ParticleSystem ps;
    public static enum weatherStatus {RAIN, SUN, SNOW};
    private weatherStatus currentWeather = weatherStatus.SUN;
    Context mContext;
    Activity mActivity;

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mActivity = (Activity) getContext();
    }

    public void changeWeather(weatherStatus status) {
        currentWeather = status;
        switch (status) {
            case RAIN:
                ps = new ParticleSystem(mActivity, 100, R.drawable.rain, 2200);
                ps.setAcceleration(0.00013f, 96);
                ps.setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f);
                ps.setFadeOut(200, new AccelerateInterpolator());
                break;
            case SNOW:
                ps = new ParticleSystem(mActivity, 100, R.drawable.snow, 4400);
                ps.setSpeedByComponentsRange(0f, 0f, 0.05f, 0.1f);
                ps.setFadeOut(200, new AccelerateInterpolator());
                break;
            default:
                break;
        }
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
}
