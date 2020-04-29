package xyz.matteobattilana.weatherview;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.matteobattilana.demo.R;

import xyz.matteobattilana.library.Common.Constants;
import xyz.matteobattilana.library.WeatherView;

@Deprecated
public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    WeatherView mWeatherView;
    SeekBar fps, fadeOutTime, lifeTime, particles, angle;
    TextView fpsText, fadeOutTimeText, lifeTimeText, particlesText, angleText;
    Switch orientation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeDeprecated);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        init();
    }

    private void init() {
        //WeatherView
        mWeatherView = findViewById(R.id.weather);

        //SeekBar
        fps = findViewById(R.id.fps);
        fadeOutTime = findViewById(R.id.fadeOutTime);
        lifeTime = findViewById(R.id.lifeTime);
        particles = findViewById(R.id.particles);
        angle = findViewById(R.id.angle);

        //TextView
        final HoloTextView text = findViewById(R.id.weatherText);
        fpsText = findViewById(R.id.fpsText);
        fadeOutTimeText = findViewById(R.id.fadeOutTimeText);
        lifeTimeText = findViewById(R.id.lifeTimeText);
        particlesText = findViewById(R.id.particlesTest);
        angleText = findViewById(R.id.angleText);

        //Button
        Button git = findViewById(R.id.btn_git);
        Typeface fontawesome = Typeface.createFromAsset(getResources().getAssets(), "fontawesome-webfont.ttf");
        git.setTypeface(fontawesome);

        //Picker
        HoloPicker mHoloPicker = findViewById(R.id.picker);

        //Layout
        LinearLayout linear_git = findViewById(R.id.linear_git);

        //Switch orientation
        orientation = findViewById(R.id.orientationSwitch);

        //Init
        mWeatherView.startAnimation();
        reloadSeek();

        mHoloPicker.setOnValueChangedListener((picker, oldVal, newVal) -> {
            mWeatherView.cancelAnimation()
                    .setWeather(Constants.WeatherStatus.values()[newVal])
                    .startAnimation();

            switch (Constants.WeatherStatus.values()[newVal]) {
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
        });


        linear_git.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/MatteoBattilana/WeatherView"));
            startActivity(browserIntent);
        });

        fps.setOnSeekBarChangeListener(this);
        fadeOutTime.setOnSeekBarChangeListener(this);
        lifeTime.setOnSeekBarChangeListener(this);
        particles.setOnSeekBarChangeListener(this);
        angle.setOnSeekBarChangeListener(this);

        orientation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mWeatherView.setOrientationMode(isChecked ? Constants.OrientationStatus.ENABLE : Constants.OrientationStatus.DISABLE);
            reloadSeek();
        });

    }


    private void reloadSeek() {
        //Set initial progress
        fps.setProgress(mWeatherView.getFPS() - 8);
        fadeOutTime.setProgress(mWeatherView.getCurrentFadeOutTime());
        lifeTime.setProgress(mWeatherView.getCurrentLifeTime());
        particles.setProgress(mWeatherView.getCurrentParticles());
        angle.setProgress(mWeatherView.getCurrentAngle() + 30);
        orientation.setChecked(mWeatherView.getOrientationMode() == Constants.OrientationStatus.ENABLE);
        angle.setEnabled(mWeatherView.getOrientationMode() == Constants.OrientationStatus.DISABLE);


        //set seekbar text
        fadeOutTimeText.setText("fadeOutTime: " + mWeatherView.getCurrentFadeOutTime() + " ms\t");
        fpsText.setText("FPS: " + mWeatherView.getFPS() + "\t");
        lifeTimeText.setText("lifeTime: " + mWeatherView.getCurrentLifeTime() + " ms\t");
        particlesText.setText("particles: " + mWeatherView.getCurrentParticles() + "\t");
        angleText.setText("angle: " + mWeatherView.getCurrentAngle() + " °\t");

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
                mWeatherView.setCurrentFadeOutTime(seekBar.getProgress());
                break;

            case R.id.lifeTime:
                mWeatherView.setCurrentLifeTime(seekBar.getProgress());
                break;
            case R.id.particles:
                mWeatherView.setCurrentParticles(seekBar.getProgress());
                break;
            case R.id.angle:
                mWeatherView.setCurrentAngle(seekBar.getProgress() - 30);
                break;
        }
        mWeatherView.startAnimation();
        reloadSeek();

    }
}
