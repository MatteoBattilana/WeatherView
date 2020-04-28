package com.github.matteobattilana.weather

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log


/**
 * Created by Mitchell on 7/7/2017.
 */

class WeatherViewSensorEventListener(val context: Context, val weatherView: WeatherView) : SensorEventListener {
    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

    private var magneticValues: FloatArray? = null
    private var accelerometerValues: FloatArray? = null
    var started = false
        private set

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_MAGNETIC_FIELD -> magneticValues = event.values.copyOf()
            Sensor.TYPE_ACCELEROMETER -> accelerometerValues = event.values.copyOf()
        }

        if (magneticValues != null && accelerometerValues != null) {

            val rotationMatrix = FloatArray(9)
            SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerValues, magneticValues)

            val remappedRotationMatrix = FloatArray(9)
            SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, remappedRotationMatrix)

            val orientationAngles = FloatArray(3)
            SensorManager.getOrientation(remappedRotationMatrix, orientationAngles)

            val pitch = Math.toDegrees(orientationAngles[1].toDouble())
            val roll = Math.toDegrees(orientationAngles[2].toDouble())

            if ((-85.0..85.0).contains(pitch))
                weatherView.angle = roll.toInt()
        }
    }

    private fun registerListener() {
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI)
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI)
    }

    private fun unregisterListener() {
        sensorManager.unregisterListener(this)
    }

    fun start() {
        started = true
        registerListener()
    }

    fun stop() {
        started = false
        unregisterListener()
    }

    fun onResume() {
        if (started)
            registerListener()
    }

    fun onPause() {
        unregisterListener()
    }
}