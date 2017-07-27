package com.github.matteobattilana.demo

import android.os.Build
import android.widget.SeekBar

/**
 * Created by Mitchell on 7/6/2017.
 */

fun SeekBar.setProgressCompat(progress: Int, animate: Boolean = false): Unit {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        setProgress(progress, animate)
    else
        setProgress(progress)
}