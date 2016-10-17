package xyz.matteobattilana.weatherview;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import xyz.matteobattilana.library.Common.Constants;
import xyz.matteobattilana.library.WeatherView;


public class MainActivity extends AppCompatActivity {
    WeatherView mWeatherView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWeatherView = (WeatherView) findViewById(R.id.weater);
        mWeatherView.setWeather(Constants.weatherStatus.RAIN);
        mWeatherView.setRainParticles(Constants.rainParticles);
        mWeatherView.startAnimation();

        final HoloTextView text = (HoloTextView) findViewById(R.id.weatherText);

        HoloPicker mHoloPicker = (HoloPicker) findViewById(R.id.picker);
        mHoloPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mWeatherView.setWeather(Constants.weatherStatus.values()[newVal]);

                mWeatherView.startAnimation();
                switch (Constants.weatherStatus.values()[newVal]) {
                    case RAIN:
                        text.setText(getString(R.string.rain));
                        break;
                    case SNOW:
                        text.setText(getString(R.string.snow));
                        break;
                    default:
                        text.setText(getString(R.string.sun));
                        break;
                }
            }
        });

        Button git = (Button) findViewById(R.id.btn_git);
        Typeface fontawesome = Typeface.createFromAsset(getResources().getAssets(), "fontawesome-webfont.ttf");
        git.setTypeface(fontawesome);

        LinearLayout linear_git = (LinearLayout) findViewById(R.id.linear_git);
        linear_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/MatteoBattilana/WeatherView"));
                startActivity(browserIntent);
            }
        });

    }
}
