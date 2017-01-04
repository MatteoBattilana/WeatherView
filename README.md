[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-WeatherView-green.svg?style=true)](https://android-arsenal.com/details/1/4737)
[![Android Gems](http://www.android-gems.com/badge/MatteoBattilana/WeatherView.svg?branch=master)](http://www.android-gems.com/lib/MatteoBattilana/WeatherView)

<p>Sample App:</p>

<div>
<a href="https://play.google.com/store/apps/details?id=xyz.matteobattilana.weatherview"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge-border.png" width="300" /></a>
<a href="https://www.youtube.com/watch?v=GDS7Y_aDVcI"><img alt="Watch the demo video" src="https://github.com/MatteoBattilana/WeatherView/blob/master/images/youtube.png" width="300" /></a>
</div>

You can also download <a href="https://github.com/MatteoBattilana/WeatherView/raw/master/app/app-release.apk">WeaterView Library Demo apk</a> to check out what can be done with it.

#WeatherView
> Starting from the 1.1.0 version this library is using a different setter structure. Please look at the above documentation

WeatherView is an Android Library that helps you make a cool weather animation for your app.<br/>
This library is based on a modified version of <a href="https://github.com/plattysoft/Leonids">Leonids</a> library.



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
	compile 'com.github.MatteoBattilana:WeatherView:1.1.1'
}
```

###Basic usage

By default the WeatherView is set to SUN, no animation is showed.
It is possible to change or initialize the weather status with the **setWeather(weatherStatus)** method.<br/>
The animation is stopped by default and must be started with **startAnimation()**. When the animation is playing and the previous method is called the animation is stopped and must be restarted. WeatherView requires minSDK 14.
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
        mWeatherView.setWeather(Constants.weatherStatus.RAIN)
                    .setLifeTime(2000)
                    .setFadeOutTime(1000)
                    .setParticles(43)
                    .setFPS(60)
                    .setAngle(-5)
                    .startAnimation();
    }
}
```

Include WeatherView into activity_main.xml
``` Xml
<xyz.matteobattilana.library.WeatherView
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/weather"
        android:layout_width="match_parent"
        android:layout_height="1dp"
        app:angle="-3"
        app:fadeOutTime="1000"
        app:fps="40"
        app:lifeTime="2200"
        app:numParticles="55"
        app:startingWeather="RAIN"/>
```


It also allows xml customization with the follow attributes:

``` Xml
        app:angle="int"
        app:fadeOutTime="int"
        app:fps="int"
        app:lifeTime="int"
        app:numParticles="int"
        app:startingWeather="{RAIN,SNOW,SUN}"
```
* **angle** is the angle of the single particle, 0 is perpendicular to the ground. This value must be greater than -180 and less than 180.
* **fps** must be greater than 7 and less than 100.
* **lifeTime** is the falling time of a single particle. After this time the particle stop exist. Must be greater than 0.
* **fadeOutTime** during lifeTime the particle starts to fade out. This fade out animation lasts the specified duration. Must be greater than 0.
* **numParticles** number of particle for a second. Must be grather than 0.
* **startingWeather** you can specify the stating weather status but **startAnimation()** MUST BE CALLED.

##Available Methods
List of the methods available on the class WeatherView.
> Since from 1.1.0 there is only one constructor.

###Configuration
Available methods for the configuration are:
* *setWeather(weatherStatus mWeatherStatus)*: RAIN, SUN or SNOW.
* *setLifeTime(int time)* Set the time of the current animation showed
* *getLifeTime()*
* *setFadeOutTime(int fadeOutTime)* Set the fadeOutTime to the all animation
* *getFadeOutTime()*
* *setParticles(int particles)* Set the particles of the current animation showed
* *getParticles()*
* *setAngle(int angle)* Set the angle of every single particle of the current animation showed 
* *getAngle()*
* *setFPS(int fps)* Once you call this method the animation is atomatically stopped by default with the **cancelAnimation()** method.
* *getFPS()*
* *startAnimation()*
* *stopAnimation()* Stops the emission of new particles, but the active ones are updated.
* *cancelAnimation()* Stops the emission of new particles, the active ones are stopped and cancelled.
* *getCurrentWeather()*
* *isPlaying()* 
* *resetConfiguration()* Reset all the values to the default values


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
Added a new feature asked in this [**Improvement**](ttps://github.com/MatteoBattilana/WeatherView/issues/2).
> It will be awesome to add gravity to each particle.
Changing angle is a quick idea but by adding an independant gravity using device sensor on each particle we will respond perfectly and the animation will also be perfect.

You can also download <a href="https://github.com/MatteoBattilana/WeatherView/raw/OrientationSensor/app/app-release.apk">WeaterView Library Demo Beta apk</a> to check out this new feature.



###Android Studio / grandle

Add the following dependency to the **build.gradle** of your project:

``` 
repositories {
    maven { url = 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.MatteoBattilana:WeatherView:1.1.0.3'
}
```

> Please note that this is a beta version which is
still undergoing final testing. It may contains bugs.

###New feature
Now each single particle can rotate in real time with the same phone *roll* angle. In order to avoid useless execution of code, I've added an haldler for *onPause()* and *onResume()* inside the WeatherView View. This methods are called when the visibilty on this View changes. By **default** it is active. You can change this programmatically with *setOrientationMode(orientationStatus mOrientationMode)* or via xml.<br/><br/>It is possible also to set this mode directly from the xml with the *orientationMode* attribute. There are only two options: **ENABLE** and **DISABLE**.
Here a basic example:

``` Java
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeatherView mWeatherView = (WeatherView) findViewById(R.id.weather);
        //Optional
        mWeatherView.setWeather(Constants.weatherStatus.RAIN)
                    .setLifeTime(2000)
                    .setFadeOutTime(1000)
                    .setParticles(43)
                    .setFPS(60)
                    .setAngle(-5)
                	.setOrientationMode(Constants.orientationStatus.ENABLE)
                    .startAnimation();
    }
}
```


``` Xml
<xyz.matteobattilana.library.WeatherView 
		xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/weather"
        android:layout_width="match_parent"
        android:layout_height="1dp"
        app:angle="-3"
        app:fadeOutTime="1000"
        app:fps="40"
        app:lifeTime="2200"
        app:numParticles="55"
        app:orientationMode="ENABLE"
        app:startingWeather="RAIN" />
```

##Available Methods
List of the methods added to the Beta version.

###Configuration
* *setOrientationMode(orientationStatus mOrientationMode)* : ENABLE or DISABLE.
* *getOrientationMode()* Return ENABLE if it is enabled, DISABLE else.
 

##Screenshot

A set of screenshot from the demo application.

<div  align="center" width="100%">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-152953.png" width="250">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-153044.png" width="250">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-153100.png" width="250">
</div>
