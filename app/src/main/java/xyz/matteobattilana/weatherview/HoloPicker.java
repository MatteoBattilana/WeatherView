package xyz.matteobattilana.weatherview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.NumberPicker;

// For backward-compability
public class HoloPicker extends NumberPicker {


    public HoloPicker(Context context) {
        super(context);
        if (!isInEditMode())
            init();
    }

    public HoloPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init();
        }
    }

    public HoloPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (!isInEditMode()) {
            init();
        }
    }

    private void init() {
        String[] values = getResources().getStringArray(R.array.weatherList);

        this.setMinValue(0); //from array first value
        this.setMaxValue(values.length - 1); //to array last value
        this.setDisplayedValues(values);
        this.setWrapSelectorWheel(true);
    }
}