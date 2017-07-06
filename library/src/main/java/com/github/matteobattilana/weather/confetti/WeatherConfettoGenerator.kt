package com.github.matteobattilana.weather.confetti

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import com.github.jinatonic.confetti.ConfettoGenerator
import com.github.jinatonic.confetti.confetto.Confetto
import java.util.*
import kotlin.jvm.internal.Ref

/**
 * Created by Mitchell on 7/4/2017.
 */
class WeatherConfettoGenerator(val bitmapRef: Ref.ObjectRef<Bitmap>) : ConfettoGenerator {
    override fun generateConfetto(random: Random): Confetto = MotionBlurBitmapConfetto(bitmapRef)
}

class MotionBlurBitmapConfetto(val bitmapRef: Ref.ObjectRef<Bitmap>) : Confetto() {

    val bitmapCenterX get() = (bitmapRef.element?.width ?: 0) / 2f
    val bitmapCenterY get() = (bitmapRef.element?.height ?: 0) / 2f

    override fun getHeight(): Int = bitmapRef.element?.height ?: 0

    override fun getWidth(): Int = bitmapRef.element?.width ?: 0

    override fun drawInternal(canvas: Canvas, matrix: Matrix, paint: Paint, x: Float, y: Float, rotation: Float, percentageAnimated: Float) {
        bitmapRef.element?.let {
            matrix.preTranslate(x, y)
            matrix.preRotate(rotation, bitmapCenterX, bitmapCenterY)
            canvas.drawBitmap(it, matrix, paint)
        }
    }
}