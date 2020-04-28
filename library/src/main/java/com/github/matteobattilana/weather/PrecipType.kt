package com.github.matteobattilana.weather

/**
 * Created by Mitchell on 7/6/2017.
 */
enum class PrecipType : WeatherData {
    CLEAR {
        override val emissionRate: Float = 0f
        override val speed: Int = 0
    },
    SNOW {
        override val emissionRate: Float = 10f
        override val speed: Int = 250
    },
    RAIN {
        override val emissionRate: Float = 100f
        override val speed: Int = (SNOW.speed * EnumConstants.RAIN_SPEED_COEFFICIENT).toInt()
    },
    CUSTOM
    {
        override val emissionRate: Float = 10f
        override val speed: Int = 250
    };

    @Suppress("LeakingThis") // enum types are actually final, this warning is incorrect. Check if fixed in next plugin update
    override val precipType: PrecipType = this
}

private object EnumConstants {
    const val RAIN_SPEED_COEFFICIENT = 5.5 / 1.5 // Rain falls on average 3.6666x faster than snow
}