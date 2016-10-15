## <p>Sample App:</p>
<a href="https://play.google.com/store/apps/developer?id=Matteo+Battilana"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge-border.png" width="300" /></a>


Or you can download <a href="https://github.com/MatteoBattilana/WeatherView/raw/master/app/app-release.apk">WeaterView Library Demo apk</a> to check out what can be done with it.

#WeatherView

WeatherView is an Android Library that helps you make a cool weather animation for your app.
This library is based on this <a href="https://github.com/plattysoft/Leonids">Leonids</a> library.



<div  align="center" width="100%">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/home.gif" width="250"/>
</div>


##Setup
###Android Studio / grandle
Add the following dependency to the **build.gradle** of your project:

``` 
repositories {
	maven { url = 'https://jitpack.io' }
}

dependencies {
	compile 'com.github.MatteoBattilana:WeatherView:1.0.5'
}
```

###Basic usage

By default is set to SUN, no animation is showed.
It is possible to change or initialize the weather status with the **setWeather(weatherStatus)** method.
The animation is stopped by default and must be started with **startAnimation()**. When the animation is playing and the previous method is called the animation is stopped and must be restarted.
You can check the <a href="https://github.com/MatteoBattilana/WeatherView/tree/master/app/">WeatherView Demo Library source code</a>.

Here a basic example:
``` Java
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeatherView mWeatherView = (WeatherView) findViewById(R.id.weather);
        //Optional
        mWeatherView.setWeather(WeatherView.weatherStatus.RAIN);
        mWeatherView.startAnimation();
    }
}
```

Include WeatherView into activity_main.xml
``` Xml
<xyz.matteobattilana.library.WeatherView
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/weater"
        android:layout_width="match_parent"
        android:layout_height="1dp"
        app:fadeOutTime="1000"
        app:liveTime="3000"
        app:startingWeather="RAIN" />
```


It also allows xml customization with the follow attributes:

``` Xml
		app:fadeOutTime="int"
        app:liveTime="int"
        app:startingWeather="{RAIN,SNOW,SUN}"
```

* **liveTime** is the falling time of a single particle. After this time the particle stop exist.
* **fadeOutTime** when liveTime is finished the particle starts to fade out. This fade out animation lasts the specified duration.
* **startingWeather** you can specify the stat	ing weather status but **startAnimation()** MUST BE CALLED.

##Available Methods
List of the methods available on the class WeatherView.
###Configuration
Available methods for the configuration are:
* *setWeather(weatherStatus mWeatherStatus)*: RAIN, SUN or SNOW.
* *setWeather(weatherStatus mWeatherStatus, int liveTime)*
* *setWeather(weatherStatus mWeatherStatus, int liveTime, int fadeOutTime)*
* *startAnimation()*
* *stopAnimation()*

###Screenshot

A set of screenshot from the demo application.

<div  align="center" width="100%">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-152953.png" width="250">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-153044.png" width="250">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-153100.png" width="250">
</div>
