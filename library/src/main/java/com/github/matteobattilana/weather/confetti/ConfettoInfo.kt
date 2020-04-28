package com.github.matteobattilana.weather.confetti

import android.graphics.Bitmap
import com.github.matteobattilana.weather.PrecipType

/**
 * Created by Mitchell on 7/11/2017.
 *
 * This class stores the current precipitation type and it held by each Confetto.
 */
class ConfettoInfo(var precipType: PrecipType, var scaleFactor: Float, var customBitmap: Bitmap? = null)