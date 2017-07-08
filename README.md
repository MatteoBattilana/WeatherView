[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-WeatherView-green.svg?style=true)](https://android-arsenal.com/details/1/4737)
[![Android Gems](http://www.android-gems.com/badge/MatteoBattilana/WeatherView.svg?branch=master)](http://www.android-gems.com/lib/MatteoBattilana/WeatherView)

Sample App:

<a href="https://play.google.com/store/apps/details?id=xyz.matteobattilana.weatherview"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge-border.png" width="200" /></a>
<a href="https://www.youtube.com/watch?v=GDS7Y_aDVcI"><img alt="Watch the demo video" src="https://github.com/MatteoBattilana/WeatherView/blob/master/images/youtube.png" width="200" /></a>

---

# WeatherView

WeatherView is an Android Library that helps you make a cool weather animation for your app.<br/>
This library is based on the [confetti](https://github.com/jinatonic/confetti) library.

<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/home.gif" width="250">

## Setup
### Android Studio / Gradle
Add the following dependency in your root build.gradle at the end of repositories:
```java
allprojects {
	repositories {
		...
		maven { url = 'https://jitpack.io' }
	}
}
``` 
Add the dependency:
```java
dependencies {
	compile 'com.github.MatteoBattilana:WeatherView:1.2.0'
}
```

### Basic usage

## Migrations
- 1.2.0 to 2.0.0

- 1.1.0 to 1.2.0

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

## License details
```
   Copyright 2016 Matteo Battilana
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

> The library is Free Software, you can use it, extended with no requirement to open source your changes. You can also make paid apps using it.

## Screenshots

A set of screenshots from the demo application.

<div  align="center" width="100%">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-152953.png" width="250">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-153044.png" width="250">
<img src="https://github.com/MatteoBattilana/WeatherView/blob/master/Screenshot/device-2016-10-15-153100.png" width="250">
</div>
