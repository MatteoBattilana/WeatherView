package com.github.matteobattilana.demo

import android.widget.AdapterView
import android.widget.SeekBar

/**
 * Created by Mitchell on 7/6/2017.
 */

interface ReducedOnSeekBarChangeListener : SeekBar.OnSeekBarChangeListener {
    override fun onStartTrackingTouch(seekBar: SeekBar?) {}
    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}

interface ReducedOnItemSelectedListener : AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {}
}