package com.github.matteobattilana.weather

/**
 * Created by Mitchell on 7/6/2017.
 */
interface WeatherData {
    val precipType: PrecipType
    val emissionRate: Float
    val speed: Int
}