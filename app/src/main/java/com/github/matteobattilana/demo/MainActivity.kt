package com.github.matteobattilana.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger

/**
 * Created by Mitchell on 7/5/2017.
 */
class MainActivity : AppCompatActivity(), AnkoLogger {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

        weather_view.fadeOutPercent = 1f
        weather_view.angle = 0
        weather_view.speed = 1000
        weather_view.emissionRate = 50f
    }
}

private interface ReducedOnSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}