package in.co.chicmic.samplereservationsystem.utilities;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

public class InputValidation {
    private Context context;

    /**
     * constructor
     *
     * @param context of the activity
     */
    public InputValidation(Context context) {
        this.context = context;
    }

    /**
     * method to check InputEditText filled .
     *
     * @param textInputEditText the text field
     * @param textInputLayout the layout containing text field.
     * @param message the error message.
     * @return true/false
     */
    public boolean isInputEditTextFilled(TextInputEditText textInputEditText
            , TextInputLayout textInputLayout
            , String message)
    {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }

        return true;
    }


    /**
     * method to check InputEditText has valid email .
     *
     * @param textInputEditText the text field
     * @param textInputLayout the layout containing text field.
     * @param message the error message.
     * @return true/false
     */
    public boolean isInputEditTextEmail(TextInputEditText textInputEditText
            , TextInputLayout textInputLayout
            , String message)
    {
        String value = textInputEditText.getText().toString().trim();
        if (value.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    public boolean isInputEditTextMatches(TextInputEditText textInputEditText1
            , TextInputEditText textInputEditText2
            , TextInputLayout textInputLayout
            , String message)
    {
        String value1 = textInputEditText1.getText().toString().trim();
        String value2 = textInputEditText2.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText2);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
        }
        return true;
    }

    /**
     * method to Hide keyboard
     *
     * @param view view
     */
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken()
                , WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public boolean isContactValid(String s)
    {
        return Patterns.PHONE.matcher(s).matches();
    }
}
