package com.github.matteobattilana.weather.confetti

import com.github.jinatonic.confetti.ConfettiSource

/**
 * Created by Mitchell on 7/4/2017.
 */

/**
 * @param x replacement for super.x0
 * @param y replacement for super.y0
 * @param otherX replacement for super.x1
 * @param otherY replacement for super.y1
 */
class MutableRectSource @JvmOverloads constructor(@Volatile var x: Int, @Volatile var y: Int, @Volatile var otherX: Int = x, @Volatile var otherY: Int = y) : ConfettiSource(0, 0) {

    fun getXRange(): Int = otherX - x

    fun getYRange(): Int = otherY - y

    override fun getInitialX(random: Float): Float {
        return random * getXRange() + x
    }

    override fun getInitialY(random: Float): Float {
        return random * getYRange() + y
    }

    fun setBounds(x0: Int, y0: Int, x1: Int = x0, y1: Int = y0) {
        x = x0
        y = y0
        otherX = x1
        otherY = y1
    }
}
