package com.github.matteobattilana.weather

/**
 * Created by Mitchell on 7/6/2017.
 */
enum class PrecipType : WeatherData {
    CLEAR {
        override val emissionRate: Float = 0f
        override val speed: Int = 0
    },
    RAIN {
        override val emissionRate: Float = 100f
        override val speed: Int = 2000
    },
    SNOW {
        override val emissionRate: Float = 10f
        override val speed: Int = 400
    };

    @Suppress("LeakingThis") // enum types are actually final, this warning is incorrect. Check if fixed in next plugin update
    override val precipType: PrecipType = this
}