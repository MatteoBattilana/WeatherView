package com.github.matteobattilana.weather.confetti

import android.graphics.*
import com.github.jinatonic.confetti.ConfettoGenerator
import com.github.jinatonic.confetti.confetto.Confetto
import com.github.matteobattilana.weather.PrecipType
import java.util.*

/**
 * Created by Mitchell on 7/4/2017.
 */
class WeatherConfettoGenerator(val confettoInfo: ConfettoInfo) : ConfettoGenerator {
    override fun generateConfetto(random: Random): Confetto = MotionBlurBitmapConfetto(confettoInfo)
}

class MotionBlurBitmapConfetto(val confettoInfo: ConfettoInfo) : Confetto() {
    companion object {
        const val SNOW_RADIUS = 7.5f
        const val RAIN_STRETCH = 20f
    }

    override fun getHeight(): Int = 0

    override fun getWidth(): Int = 0

    override fun configurePaint(paint: Paint) {
        super.configurePaint(paint)
        paint.color = Color.WHITE
        paint.isAntiAlias = true
    }

    override fun drawInternal(canvas: Canvas, matrix: Matrix, paint: Paint, x: Float, y: Float, rotation: Float, percentageAnimated: Float) {

        when (confettoInfo.precipType) {
                PrecipType.CLEAR -> {
                }
                PrecipType.RAIN -> {
                    var rainStretch = RAIN_STRETCH * (confettoInfo.scaleFactor + 1.0f) / 2f;
                    val dX = currentVelocityX
                    val dY = currentVelocityY
                    val x1 = x - dX * rainStretch
                    val y1 = y - dY * rainStretch
                    val x2 = x + dX * rainStretch
                    val y2 = y + dY * rainStretch

                    paint.strokeWidth = confettoInfo.scaleFactor;
                    paint.shader = LinearGradient(x1, y1, x2, y2,
                            intArrayOf(Color.TRANSPARENT, Color.WHITE, Color.WHITE, Color.TRANSPARENT),
                            floatArrayOf(0f, 0.45f, 0.55f, 1f),
                            Shader.TileMode.CLAMP)

                    canvas.drawLine(x1, y1, x2, y2, paint)
                }
                PrecipType.SNOW -> {
                    val sigmoid = (1f / (1f + Math.pow(Math.E, -(confettoInfo.scaleFactor.toDouble() - 1f)))).toFloat();
                    paint.shader = RadialGradient(x, y, SNOW_RADIUS * confettoInfo.scaleFactor,
                            intArrayOf(Color.WHITE, Color.WHITE, Color.TRANSPARENT, Color.TRANSPARENT),
                            floatArrayOf(0f, 0.15f + sigmoid * 0.30f, 0.95f - sigmoid * 0.35f, 1f),
                            Shader.TileMode.CLAMP)

                    canvas.drawCircle(x, y, SNOW_RADIUS * confettoInfo.scaleFactor, paint)
                }
                PrecipType.CUSTOM -> {
                    matrix.preTranslate(x, y)
                    matrix.preRotate(rotation,
                            confettoInfo.customBitmap!!.width / 2f,
                            confettoInfo.customBitmap!!.height / 2f
                    );
                    matrix.preScale(confettoInfo.scaleFactor, confettoInfo.scaleFactor)
                    canvas.drawBitmap(confettoInfo.customBitmap!!, matrix, paint)
                }
        }
    }
}