package xyz.matteobattilana.weatherview;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import xyz.matteobattilana.library.Common.Constants;
import xyz.matteobattilana.library.WeatherView;


public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    WeatherView mWeatherView;
    SeekBar fps, fadeOutTime, lifeTime, particles, angle;
    TextView fpsText, fadeOutTimeText, lifeTimeText, particlesText, angleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        //WeatherView
        mWeatherView = (WeatherView) findViewById(R.id.weater);

        //SeekBar
        fps = (SeekBar) findViewById(R.id.fps);
        fadeOutTime = (SeekBar) findViewById(R.id.fadeOutTime);
        lifeTime = (SeekBar) findViewById(R.id.lifeTime);
        particles = (SeekBar) findViewById(R.id.particles);
        angle = (SeekBar) findViewById(R.id.angle);

        //TextView
        final HoloTextView text = (HoloTextView) findViewById(R.id.weatherText);
        fpsText = (TextView) findViewById(R.id.fpsText);
        fadeOutTimeText = (TextView) findViewById(R.id.fadeOutTimeText);
        lifeTimeText = (TextView) findViewById(R.id.lifeTimeText);
        particlesText = (TextView) findViewById(R.id.particlesTest);
        angleText = (TextView) findViewById(R.id.angleText);

        //Button
        Button git = (Button) findViewById(R.id.btn_git);
        Typeface fontawesome = Typeface.createFromAsset(getResources().getAssets(), "fontawesome-webfont.ttf");
        git.setTypeface(fontawesome);

        //Picker
        HoloPicker mHoloPicker = (HoloPicker) findViewById(R.id.picker);

        //Layout
        LinearLayout linear_git = (LinearLayout) findViewById(R.id.linear_git);


        //Init
        mWeatherView.setWeather(Constants.weatherStatus.RAIN);
        mWeatherView.startAnimation();
        reloadSeek();


        mHoloPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.e("ASD", Constants.weatherStatus.values()[newVal] + " r");

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
                reloadSeek();
            }
        });


        linear_git.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/MatteoBattilana/WeatherView"));
                startActivity(browserIntent);
            }
        });

        fps.setOnSeekBarChangeListener(this);
        fadeOutTime.setOnSeekBarChangeListener(this);
        lifeTime.setOnSeekBarChangeListener(this);
        particles.setOnSeekBarChangeListener(this);
        angle.setOnSeekBarChangeListener(this);

    }

    private void reloadSeek() {
        //Set initial progress
        fps.setProgress(mWeatherView.getFPS() - 8);
        fadeOutTime.setProgress(mWeatherView.getFadeOutTime());
        lifeTime.setProgress(mWeatherView.getLifeTime());
        particles.setProgress(mWeatherView.getParticles());
        angle.setProgress(mWeatherView.getAngle() + 30);

        //set seekbar text
        fadeOutTimeText.setText("fadeOutTime: " + mWeatherView.getFadeOutTime() + " ms\t");
        fpsText.setText("FPS: " + mWeatherView.getFPS() + "\t");
        lifeTimeText.setText("lifeTime: " + mWeatherView.getLifeTime() + " ms\t");
        particlesText.setText("particles: " + mWeatherView.getParticles() + "\t");
        angleText.setText("Angle: " + mWeatherView.getAngle() + " °\t");

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.fps:
                mWeatherView.setFPS(seekBar.getProgress() + 8);
                break;

            case R.id.fadeOutTime:
                mWeatherView.setFadeOutTime(seekBar.getProgress());
                break;

            case R.id.lifeTime:
                mWeatherView.setRainTime(seekBar.getProgress());
                mWeatherView.setSnowTime(seekBar.getProgress());
                break;
            case R.id.particles:
                mWeatherView.setRainParticles(seekBar.getProgress());
                mWeatherView.setSnowParticles(seekBar.getProgress());
                break;
            case R.id.angle:
                mWeatherView.setRainAngle(seekBar.getProgress() - 30);
                mWeatherView.setSnowAngle(seekBar.getProgress() - 30);
                break;
        }
        mWeatherView.restartWithNewConfiguration();

        reloadSeek();

    }
}
