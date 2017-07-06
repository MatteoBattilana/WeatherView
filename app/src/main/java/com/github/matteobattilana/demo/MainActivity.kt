package com.github.matteobattilana.demo

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.*
import com.github.matteobattilana.weather.WeatherCondition
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.browse

/**
 * Created by Mitchell on 7/5/2017.
 */
class MainActivity : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                weather_view.fadeOutPercent = progress.toFloat() / seekBar.max.toFloat()
            }
        })

        angle_seekbar.setOnSeekBarChangeListener(object : ReducedOnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                weather_view.angle = progress - 90
            }
        })

        speed_seekbar.setOnSeekBarChangeListener(object : ReducedOnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                weather_view.speed = progress
            }
        })

        emission_rate_seekbar.setOnSeekBarChangeListener(object : ReducedOnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                weather_view.emissionRate = progress.toFloat()
            }
        })

        weather_condition_spinner.adapter = ArrayAdapter.createFromResource(this, R.array.weather_name_list, android.R.layout.simple_spinner_item)
                .apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
        weather_condition_spinner.onItemSelectedListener = object : ReducedOnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        weather_view.weatherCondition = WeatherCondition.RAIN
                        weather_label.setText(getString(R.string.rain))
                    }
                    1 -> {
                        weather_view.weatherCondition = WeatherCondition.SNOW
                        weather_label.setText(getString(R.string.snow))
                    }
                    2 -> {
                        weather_view.weatherCondition = WeatherCondition.CLEAR
                        weather_label.setText(getString(R.string.sun))
                    }
                    else -> throw IllegalStateException("Invalid spinner position!")
                }
            }
        }

        github_icon.setOnClickListener {
            browse("https://github.com/MatteoBattilana/WeatherView")
        }

        weather_view.fadeOutPercent = 1f
        weather_view.angle = 0
        weather_view.speed = 1000
        weather_view.emissionRate = 50f
        weather_view.weatherCondition = WeatherCondition.RAIN
    }
}

private interface ReducedOnSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}

private interface ReducedOnItemSelectedListener : AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {}
}