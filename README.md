#WeatherView

WeatherView is an Android Library that helps you make a cool weather animation for your app.
<p>Sample App:</p>
<a href="https://play.google.com/store/apps/details?id=paolorotolo.github.com.appintroexample&utm_source=global_co&utm_medium=prtnr&utm_content=Mar2515&utm_campaign=PartBadge&pcampaignid=MKT-AC-global-none-all-co-pr-py-PartBadges-Oct1515-1"><img alt="Get it on Google Play" src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge-border.png" width="300" /></a>



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
