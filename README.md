[![](https://jitpack.io/v/MatteoBattilana/WeatherView.svg)](https://jitpack.io/#MatteoBattilana/WeatherView)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-WeatherView-green.svg?style=true)](https://android-arsenal.com/details/1/4737)
[![Android Gems](http://www.android-gems.com/badge/MatteoBattilana/WeatherView.svg?branch=master)](http://www.android-gems.com/lib/MatteoBattilana/WeatherView)

<a href="https://play.google.com/store/apps/details?id=xyz.matteobattilana.weatherview"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge-border.png" width="200" /></a> <a href="https://www.youtube.com/watch?v=Lw65v6QPz_M"><img alt="Watch the demo video" src="images/youtube.png" width="200" /></a>

---

# WeatherView

WeatherView is an Android Library that helps you make a cool weather animation for your app.<br/>
This library is based on the [confetti](https://github.com/jinatonic/confetti) library.
<p align="center">
	<img src="images/sample_video_rain_1.gif" width="320"> <img src="images/sample_video_snow_1.gif" width="320">
<p>
	
## Setup
### Android Studio / Gradle
Add the following dependency in your root build.gradle at the end of repositories:
```Gradle
allprojects {
    repositories {
        //...
        maven { url = 'https://jitpack.io' }
    }
}
```
Add the dependency:
```Gradle
dependencies {
    implementation 'com.github.MatteoBattilana:WeatherView:3.0.0'
}
```

## Simple usage
Simple use cases will look something like this:

### Kotlin

```Kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
	
	weather_view.setWeatherData(PrecipType.RAIN)
     }
 }
```

### Java
```Java
public class Main2Activity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeatherView weatherView = findViewById(R.id.weather_view);
        weatherView.setWeatherData(PrecipType.RAIN);
    }
}
```

```Xml
<com.github.matteobattilana.weather.WeatherView
    android:id="@+id/weather_view"
    android:layout_width="0dp"
    android:layout_height="0dp"
    app:layout_constraintBottom_toBottomOf="parent"
    app:layout_constraintLeft_toLeftOf="parent"
    app:layout_constraintRight_toRightOf="parent"
    app:layout_constraintTop_toTopOf="parent" />
```

For examples of usage, see the demo app.
	
## Screenshots
<img src="images/sample_clear_1.png" width="250"> <img src="images/sample_rain_1.png" width="250"> <img src="images/sample_snow_1.png" width="250">

## License details

```
Copyright 2019 Matteo Battilana

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

The library is Free Software, you can use it, extended with no requirement to open source your changes. You can also make paid apps using it.
