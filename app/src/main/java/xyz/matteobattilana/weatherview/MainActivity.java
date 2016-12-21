package xyz.matteobattilana.weatherview;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import xyz.matteobattilana.library.Common.Constants;
import xyz.matteobattilana.library.WeatherView;


public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    WeatherView mWeatherView;
    SeekBar fps, fadeOutTime, lifeTime, particles, angle;
    TextView fpsText, fadeOutTimeText, lifeTimeText, particlesText, angleText;
    Switch orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        init();
    }

    private void init() {
        //WeatherView
        mWeatherView = (WeatherView) findViewById(R.id.weather);

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

        //Switch orientation
        orientation = (Switch)findViewById(R.id.orientationSwitch);

        //Init
        mWeatherView.startAnimation();
        reloadSeek();

        mHoloPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mWeatherView.cancelAnimation()
                        .setWeather(Constants.weatherStatus.values()[newVal])
                        .startAnimation();

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

        orientation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    mWeatherView.enableOrientation();
                else
                    mWeatherView.disableOrientation();
            }
        });

    }


    private void reloadSeek() {
        //Set initial progress
        fps.setProgress(mWeatherView.getFPS() - 8);
        fadeOutTime.setProgress(mWeatherView.getFadeOutTime());
        lifeTime.setProgress(mWeatherView.getLifeTime());
        particles.setProgress(mWeatherView.getParticles());
        angle.setProgress(mWeatherView.getAngle() + 30);
        orientation.setChecked(mWeatherView.getIsOrientationActive());

        //set seekbar text
        fadeOutTimeText.setText("fadeOutTime: " + mWeatherView.getFadeOutTime() + " ms\t");
        fpsText.setText("FPS: " + mWeatherView.getFPS() + "\t");
        lifeTimeText.setText("lifeTime: " + mWeatherView.getLifeTime() + " ms\t");
        particlesText.setText("particles: " + mWeatherView.getParticles() + "\t");
        angleText.setText("angle: " + mWeatherView.getAngle() + " °\t");

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
                mWeatherView.setLifeTime(seekBar.getProgress());
                break;
            case R.id.particles:
                mWeatherView.setParticles(seekBar.getProgress());
                break;
            case R.id.angle:
                mWeatherView.setAngle(seekBar.getProgress() - 30);
                break;
        }
        mWeatherView.startAnimation();

        reloadSeek();

    }
}
