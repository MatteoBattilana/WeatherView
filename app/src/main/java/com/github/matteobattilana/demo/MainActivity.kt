package com.github.matteobattilana.demo

import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.matteobattilana.weather.PrecipType
import com.github.matteobattilana.weather.WeatherData
import com.github.matteobattilana.weather.WeatherViewSensorEventListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.browse

/**
 * Created by Mitchell on 7/5/2017.
 */
class MainActivity : AppCompatActivity(), AnkoLogger {
    lateinit var weatherSensor: WeatherViewSensorEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherSensor = WeatherViewSensorEventListener(this, weather_view)

        weather_label.setFactory {
            TextView(this).apply {
                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                gravity = Gravity.CENTER
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 64f)
                typeface = Typeface.create("sans-serif-thin", Typeface.NORMAL)
            }
        }

        fade_out_percent_seekbar.setOnSeekBarChangeListener(object : ReducedOnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    weather_view.fadeOutPercent = progress.toFloat() / seekBar.max.toFloat()
            }
        })

        angle_seekbar.setOnSeekBarChangeListener(object : ReducedOnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    weather_view.angle = progress - 90
            }
        })

        speed_seekbar.setOnSeekBarChangeListener(object : ReducedOnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    weather_view.speed = progress
            }
        })

        emission_rate_seekbar.setOnSeekBarChangeListener(object : ReducedOnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    weather_view.emissionRate = progress.toFloat()
            }
        })

        orientation_switch.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            when (isChecked) {
                true -> weatherSensor.start()
                false -> weatherSensor.stop()
            }
        }

        weather_condition_spinner.adapter = ArrayAdapter.createFromResource(this, R.array.weather_name_list, android.R.layout.simple_spinner_item)
                .apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
        weather_condition_spinner.onItemSelectedListener = object : ReducedOnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> setWeatherData(PrecipType.RAIN) // Runs at start
                    1 -> setWeatherData(PrecipType.SNOW)
                    2 -> setWeatherData(PrecipType.CLEAR)
                    else -> throw IllegalStateException("Invalid spinner position!")
                }
            }
        }

        github_icon.setOnClickListener {
            browse("https://github.com/MatteoBattilana/WeatherView")
        }

        weather_view.fadeOutPercent = 1f
        weather_view.angle = 0
    }

    private fun setWeatherData(weatherData: WeatherData): Unit {
        weather_view.setWeatherData(weatherData)
        speed_seekbar.setProgressCompat(weatherData.speed, true)
        emission_rate_seekbar.setProgressCompat(weatherData.emissionRate.toInt(), true)

        weather_label.setText(getString(
                when (weatherData.precipType) {
                    PrecipType.CLEAR -> R.string.sun
                    PrecipType.RAIN -> R.string.rain
                    PrecipType.SNOW -> R.string.snow
                }
        ))
    }

    override fun onResume() {
        super.onResume()
        weatherSensor.onResume()
    }

    override fun onPause() {
        super.onPause()
        weatherSensor.onPause()
    }
}