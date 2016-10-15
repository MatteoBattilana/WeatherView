package xyz.matteobattilana.weatherview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import xyz.matteobattilana.library.WeatherView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WeatherView mWeatherView = (WeatherView) findViewById(R.id.weater);
        //mWeatherView.setWeather(WeatherView.weatherStatus.RAIN);
        mWeatherView.startAnimation();

    }
}
