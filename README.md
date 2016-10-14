#WeatherView

WeatherView is an Android Library that helps you make a cool weather animation for your app.


##Usage
###Basic usage
Add this to your **build.gradle**:

``` 
repositories {
	maven { url = 'https://jitpack.io' }
}

dependencies {
	compile 'com.github.MatteoBattilana:WeatherView:1.0.2'
}
```

Open an Activity:
``` Java
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WeatherView mWeatherView = (WeatherView) findViewById(R.id.weather);
        mWeatherView.setWeather(WeatherView.weatherStatus.RAIN);
        mWeatherView.startAnimation();
    }
}
```

Include into activity_main.xml
``` Xml
    <xyz.matteobattilana.library.WeatherView
        android:id="@+id/weather"
        android:layout_width="match_parent"
        android:layout_height="1dp" />
```
