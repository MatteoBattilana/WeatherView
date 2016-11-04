## <p>Sample App:</p>
<a href="https://play.google.com/store/apps/details?id=xyz.matteobattilana.weatherview"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge-border.png" width="300" /></a>


You can also download <a href="https://github.com/MatteoBattilana/WeatherView/raw/master/app/app-release.apk">WeaterView Library Demo apk</a> to check out what can be done with it.

#WeatherView
> If you are interested in extra functions please pay attention to the [WeatherView Beta](#beta) version. 

WeatherView is an Android Library that helps you make a cool weather animation for your app.<br/>
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
	compile 'com.github.MatteoBattilana:WeatherView:1.0.10'
}
```

###Basic usage

By default the WeatherView is set to SUN, no animation is showed.
It is possible to change or initialize the weather status with the **setWeather(weatherStatus)** method.<br/>
The animation is stopped by default and must be started with **startAnimation()**. When the animation is playing and the previous method is called the animation is stopped and must be restarted.WeatherView requires minSDK 14.
<br/>
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
        mWeatherView.setWeather(Constants.weatherStatus.RAIN);
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
        app:lifeTime="3000"
        app:startingWeather="RAIN" />
```


It also allows xml customization with the follow attributes:

``` Xml
		app:fadeOutTime="int"
        app:lifeTime="int"
		app:numParticles="int"
        app:startingWeather="{RAIN,SNOW,SUN}"
```

* **lifeTime** is the falling time of a single particle. After this time the particle stop exist.
* **fadeOutTime** during lifeTime the particle starts to fade out. This fade out animation lasts the specified duration.
* **numParticles** number of particle for a second.
* **startingWeather** you can specify the stating weather status but **startAnimation()** MUST BE CALLED.

##Available Methods
List of the methods available on the class WeatherView.
###Configuration
Available methods for the configuration are:
* *setWeather(weatherStatus mWeatherStatus, int lifeTime, int fadeOutTime, int numParticles)*
* *setWeather(weatherStatus mWeatherStatus, int lifeTime, int fadeOutTime)*
* *setWeather(weatherStatus mWeatherStatus, int lifeTime)*
* *setWeather(weatherStatus mWeatherStatus)*: RAIN, SUN or SNOW.
* *startAnimation()*
* *stopAnimation()* Stops the emission of new particles, but the active ones are updated.
* *cancelAnimation()* Stops the emission of new particles, the active ones are stopped and cancelled.
* *setRainTime(int rainTime)*
* *setSnowTime(int snowTime)*
* *setFadeOutTime(int fadeOutTime)*
* *setRainParticles(int rainParticles)*
* *setSnowParticles(int snowParticles)*
* *resetConfiguration()*
* *restartWithNewConfiguration()*
* *isPlaying()*

Only with **setSnowParticles(int snowParticles)** and **setRainParticles(int rainParticles)** the animation is restarted if was playing.
The other setter must be reloaded on the animation with **restartWithNewConfiguration()** method.

##License details
Copyright 2016 Matteo Battilana
   > Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

  > http://www.apache.org/licenses/LICENSE-2.0

  > Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.


> The library is Free Software, you can use it, extended with no requirement to open source your changes. You can also make paid apps using it.

#Beta
###Beta Screenshot
<div  align="center" width="100%">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/localLibrary/Screenshot/beta.png" width="250"/>
</div>

###Android Studio / grandle

Add the following dependency to the **build.gradle** of your project:

``` 
repositories {
    maven { url = 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.MatteoBattilana:WeatherView:1.0.10.11'
}
```

> Please note that this is a beta version which is
still undergoing final testing. It may contains bugs.

###New feature

It seems "TIMMERTASK_INTERVAL" in ParticaleSystem in Leonids library is set with a value to high. In this **beta** it is set to 33, about 30fps.

Now is possible to set the animation fps with the **setFPS(int fps)** method. It is possible also to set it in the xml:

``` Xml
 <xyz.matteobattilana.library.WeatherView
 		xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/weater"
        android:layout_width="match_parent"
        android:layout_height="1dp"
        app:fadeOutTime="1000"
        app:fps="40"
        app:lifeTime="2200"
        app:startingWeather="RAIN"
        app:angle="6"/>
```

**Changed**:
* *reloadNewConfiguration()* stop the animation and does not resume it.
* *resetConfiguration()* now it set the fps to the default value.
* *setFPS(int fps)* once you call this method the animation is atomatically stopped by default with the **cancelAnimation()** method. Like on the other setters in order to restart the animation you must call **restartWithNewConfiguration()**.

**Added**:
* *setWeather(weatherStatus mWeatherStatus, int lifeTime, int fadeOutTime, int numParticles, int fps, int angle)*
* *setWeather(weatherStatus mWeatherStatus, int lifeTime, int fadeOutTime, int numParticles, int fps)*
* *reloadNewConfiguration()*
* *getFadeOutTime()*
* *getLifeTime()*
* *getParticles()*
* *getFPS()*
* *setFPS(int fps)*
* *setAngle(int angle)* 
* *getAngle()*
* *getCurrentWeather()*

##Screenshot

A set of screenshot from the demo application.

<div  align="center" width="100%">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-152953.png" width="250">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-153044.png" width="250">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-153100.png" width="250">
</div>
