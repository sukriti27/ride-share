package com.example.rideshare;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

/**
 * Custom EditText which shows error icon without any error message
 *
 * @author Sukriti
 * @version 1.0
 */
public class CustomEditText extends AppCompatAutoCompleteTextView {

    // This variable is true only if there is no error in the custom edit text
    private boolean valid;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        valid = true;
    }

    @Override
    public void setError(CharSequence error, Drawable icon) {
        setCompoundDrawables(null, null, icon, null);
        valid = false;
    }

    @Override
    public void setError(CharSequence error) {
        super.setError(null);
        valid = true;
    }

    public boolean isValid() {
        return valid;
    }
}

