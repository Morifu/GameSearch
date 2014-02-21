package com.morfi.gamesearch;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Morfi on 31.01.14.
 */
public class PricePickerPreference extends DialogPreference {

    EditText priceMin;
    EditText priceMax;

    String min, max;

    public PricePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.price_picker);

        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        setInitialValues();
        priceMin = (EditText) v.findViewById(R.id.price_from_edit);
        if (priceMin != null)
            priceMin.setText(min);
        priceMax = (EditText) v.findViewById(R.id.price_to_edit);
        if (priceMax != null)
            priceMax.setText(max);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {

            String min = priceMin.getText().toString();
            if (min.isEmpty())
                min = "0";

            String max = priceMax.getText().toString();
            if (max.isEmpty())
                max = "9999";
            Log.d("DBMANAGER", "min is :" + min + " max is: " + max);
            persistString(min + "-" + max);
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            setInitialValues();
        }
    }

    private void setInitialValues() {
        String value = getPersistedString("");
        Log.d("DBMANAGER", "persisted string value: " + value);
        if (value.isEmpty()) {
            min = "0";
            max = "9999";
        } else {
            String[] minMax = value.split("-");
            min = minMax[0];
            max = minMax[1];
        }
    }

}