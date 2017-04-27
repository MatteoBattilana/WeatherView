[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-WeatherView-green.svg?style=true)](https://android-arsenal.com/details/1/4737)
[![Android Gems](http://www.android-gems.com/badge/MatteoBattilana/WeatherView.svg?branch=master)](http://www.android-gems.com/lib/MatteoBattilana/WeatherView)

<p>Sample App:</p>

<div>
<a href="https://play.google.com/store/apps/details?id=xyz.matteobattilana.weatherview"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge-border.png" width="300" /></a>
<a href="https://www.youtube.com/watch?v=GDS7Y_aDVcI"><img alt="Watch the demo video" src="https://github.com/MatteoBattilana/WeatherView/blob/master/images/youtube.png" width="300" /></a>
</div>

You can also download <a href="https://github.com/MatteoBattilana/WeatherView/raw/master/app/app-release.apk">WeaterView Library Demo apk</a> to check out what can be done with it.

# WeatherView
> **IMPORTANT**<br/>
> Starting from the 1.1.0 version this library is using a different setter structure. Please look at the above documentation.<br/>
> Starting from the 1.2.0 version some methods have been modified.

WeatherView is an Android Library that helps you make a cool weather animation for your app.<br/>
This library is based on a modified version of <a href="https://github.com/plattysoft/Leonids">Leonids</a> library.



<div  align="center" width="100%">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/home.gif" width="250"/>
</div>



## Setup
### Android Studio / grandle
Add the following dependency in your root build.gradle at the end of repositories:
```java
repositories {
	maven { url = 'https://jitpack.io' }
}
``` 
Add the dependency:
```java
dependencies {
	compile 'com.github.MatteoBattilana:WeatherView:1.2.0'
}
```

### Basic usage

By default the WeatherView is set to SUN, no animation is showed.
It is possible to change or initialize the weather status with the `setWeather(weatherStatus)` method.<br>
The animation is initially stopped by default and must be started with `startAnimation()`. When you need to change the weather type, for example from `SUN` to `RAIN`, the animation is automatically stopped and must restart with `startAnimation()`.<br>Each single particle can rotate in real time with the same phone *roll* angle. In order to avoid useless execution of code, I've added an haldler for `onPause()` and `onResume()` inside the WeatherView View. These methods are called when the visibilty on this View changes. By **default** it is disabled. You can change this programmatically with `setOrientationMode(orientationStatus mOrientationMode)` or via xml.<br/><br/>It is possible also to set this mode directly from the xml with the *orientationMode* attribute. There are only two options: **ENABLE** and **DISABLE**.<br/><br/>
WeatherView requires minSDK 14.
<br/>
You can check the <a href="https://github.com/MatteoBattilana/WeatherView/tree/master/app/">WeatherView Demo Library source code</a>.

Here a basic example:
```java
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeatherView mWeatherView = (WeatherView) findViewById(R.id.weather);
        //Optional
        mWeatherView.setWeather(Constants.weatherStatus.RAIN)
			.setCurrentLifeTime(2000)
                	.setCurrentFadeOutTime(1000)
			.setCurrentParticles(43)
                	.setFPS(60)
                	.setCurrentAngle(-5)
                    	.setOrientationMode(Constants.orientationStatus.ENABLE)
                	.startAnimation();
    }
}
```

Include WeatherView into activity_main.xml
```xml
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
        app:startingWeather="RAIN"/>
```


It also allows xml customization with the follow attributes:

```xml
<xyz.matteobattilana.library.WeatherView
	...
        app:angle="int"
        app:fadeOutTime="int"
        app:fps="int"
        app:lifeTime="int"
        app:numParticles="int"
        app:startingWeather="{RAIN,SNOW,SUN}"
	...
	/>
```
* `angle` is the angle of the single particle of the current animation setted, 0 is perpendicular to the ground. This value must be greater than -180 and less than 180.
* `fps` must be greater than 7 and less than 100.
* `lifeTime` is the falling time of a single particle of the current animation setted. After this time the particle stop exist. Must be greater than 0.
* `fadeOutTime` during lifeTime the particle starts to fade out of the current animation setted. This fade out animation lasts the specified duration. Must be greater than 0.
* `numParticles` number of particle for a second of the current animation setted. Must be grather than 0.
* `startingWeather` you can specify the stating weather status but `startAnimation()` MUST BE CALLED.

## Available Methods

List of the methods available on the class WeatherView.
> Since from 1.1.0 there is only one constructor.<br>
Since from 1.2.0 some methods have been modified

Old Name | New Name
------------ | -------------
setLifeTime(int time) | setCurrentLifeTime(int time)
setFadeOutTime(int fadeOutTime) | setCurrentFadeOutTime(int fadeOutTime) 
setParticles(int particles) | setCurrentParticles(int particles) 
setAngle(int angle) | setCurrentAngle(int angle)  
getLifeTime() | getCurrentLifeTime()  
getFadeOutTime() | getCurrentFadeOutTime() 
getParticles() | getCurrentParticles()  
getAngle() | getCurrentAngle()  

Since the modification exposed, now is possible to have more control of each single parameter. In the older version, the developer could set only the paramenter of the playing or setted animation. Please take a look to the wiki to discover more configuration options.




### Base configuration
Setters:
* `setWeather(weatherStatus mWeatherStatus)` RAIN, SUN or SNOW
* `setCurrentLifeTime(int time)` Set the time of the current animation showed
* `setCurrentFadeOutTime(int fadeOutTime)` Set the fadeOutTime to the current animation
* `setCurrentParticles(int particles)` Set the particles of the current animation showed
* `setCurrentAngle(int angle)` Set the angle of every single particle of the current animation showed
* `setOrientationMode(orientationStatus mOrientationMode)` ENABLE or DISABLE
* `setFPS(int fps)` Once you call this method the animation is atomatically stopped by default with the `cancelAnimation()` method and you must restart the animation with `startAnimation()` to resume it.

Getters:
* `getCurrentWeather()` Return the setted animation (RAIN, SUN or SNOW)
* `getCurrentLifeTime()` Return the life time of the current animation
* `getCurrentFadeOutTime()` Return the fade out time of the current animation
* `getCurrentParticles()` Return the number of particles of the current animation
* `getCurrentAngle()` Return the angle of the particles of the current animation
* `getOrientationMode()` Return ENABLE if it is enabled, DISABLE else
* `getFPS()` Return the setted FPS

Base method:
* `startAnimation()` Starts the animation
* `stopAnimation()` Stops the emission of new particles, but the active ones are updated.
* `cancelAnimation()` Stops the emission of new particles, the active ones are stopped and cancelled.
* `isPlaying()` Return `true` if the animation is playing
* `resetConfiguration()` Reset all the values to the default values

There are also some getters and setters more specific for each parameter.

## License details
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

## Screenshot

A set of screenshot from the demo application.

<div  align="center" width="100%">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-152953.png" width="250">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-153044.png" width="250">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-153100.png" width="250">
</div>
