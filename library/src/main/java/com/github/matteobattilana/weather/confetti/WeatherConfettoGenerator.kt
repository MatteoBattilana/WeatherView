package com.github.matteobattilana.weather.confetti

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import com.github.jinatonic.confetti.ConfettoGenerator
import com.github.jinatonic.confetti.confetto.BitmapConfetto
import com.github.jinatonic.confetti.confetto.Confetto
import java.util.*

/**
 * Created by Mitchell on 7/4/2017.
 */
class WeatherConfettoGenerator(val bitmap: Bitmap) : ConfettoGenerator {
    override fun generateConfetto(random: Random): Confetto = BitmapConfetto(bitmap)
}

class MotionBlurBitmapConfetto(val bitmap: Bitmap) : BitmapConfetto(bitmap) {
    val bitmapCenterX = bitmap.width / 2f
    val bitmapCenterY = bitmap.height / 2f

    override fun drawInternal(canvas: Canvas, matrix: Matrix, paint: Paint?, x: Float, y: Float, rotation: Float, percentageAnimated: Float) {
        matrix.preTranslate(x, y)
        matrix.preRotate(rotation, bitmapCenterX, bitmapCenterY)
        canvas.drawBitmap(bitmap, matrix, paint)
    }
}