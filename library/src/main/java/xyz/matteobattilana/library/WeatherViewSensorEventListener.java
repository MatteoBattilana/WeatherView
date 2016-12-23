package xyz.matteobattilana.library;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import xyz.matteobattilana.library.Common.Constants;

/**
 * Created by MatteoB on 21/12/2016.
 */
public class WeatherViewSensorEventListener implements SensorEventListener {
    //Must put in a separate class
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

    //WeatherView
    WeatherView mWeatherView;
    Context mContext;
    SensorManager sManager;


    public WeatherViewSensorEventListener(Context mContext, WeatherView mWeatherView, Constants.orientationStatus start) {
        this.mWeatherView = mWeatherView;
        this.mContext = mContext;
        init(start);
    }

    /**
     * Called on initialization of this class. If it is ENABLE, it starts
     *
     * @param start the orientation mode
     */
    private void init(Constants.orientationStatus start) {
        //test add acc
        sManager = (SensorManager) mContext.getSystemService(mContext.SENSOR_SERVICE);
        if (start == Constants.orientationStatus.ENABLE)
            start();
    }

    /**
     * Interface method for WeatherView to stop the fuction
     */
    void stop() {
        sManager.unregisterListener(this);
    }

    /**
     * Interface method for WeatherView to start the fuction
     */
    void start() {
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);

    }

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

    /**
     * Internal method for update the angle, and set the changes to the WeatherView instance
     *
     * @param angle
     */
    private void updateOrientation(int angle) {

        if (angle > 90 && Math.abs(angle - 90) >= Constants.angleRangeRead)
            angle = 90 + Constants.angleRangeRead;
        else if (angle < 90 && Math.abs(angle - 90) >= 20)
            angle = 90 - Constants.angleRangeRead;
        if (Math.abs(angle - lastAngle) > Constants.angleRangeUpdate) {
            mWeatherView.updateAngle(angle);
            lastAngle = angle;
        }

    }
}
