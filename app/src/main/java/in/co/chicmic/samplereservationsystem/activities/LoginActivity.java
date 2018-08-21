package in.co.chicmic.samplereservationsystem.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.database.DataBaseHelper;
import in.co.chicmic.samplereservationsystem.sessionManager.SessionManager;
import in.co.chicmic.samplereservationsystem.utilities.AppConstants;
import in.co.chicmic.samplereservationsystem.utilities.InputValidation;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity mActivity = LoginActivity.this;

    private NestedScrollView mNestedScrollView;

    private TextInputLayout mTextInputLayoutEmail;
    private TextInputLayout mTextInputLayoutPassword;

    private TextInputEditText mTextInputEditTextEmail;
    private TextInputEditText mTextInputEditTextPassword;

    private AppCompatButton mAppCompatButtonLogin;

    private AppCompatTextView mTextViewLinkRegister;
    private AppCompatTextView mTextViewForgotPassword;

    private InputValidation mInputValidation;
    private DataBaseHelper mDatabaseHelper;

    private SessionManager mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSession = new SessionManager(getApplicationContext());
        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {

        mNestedScrollView = findViewById(R.id.nestedScrollView);

        mTextInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        mTextInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);

        mTextInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        mTextInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);

        mAppCompatButtonLogin = findViewById(R.id.appCompatButtonLogin);

        mTextViewLinkRegister = findViewById(R.id.textViewLinkRegister);
        mTextViewForgotPassword = findViewById(R.id.tv_link_forgot_password);

    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        mAppCompatButtonLogin.setOnClickListener(this);
        mTextViewLinkRegister.setOnClickListener(this);
        mTextViewForgotPassword.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        mDatabaseHelper = new DataBaseHelper(mActivity);
        mInputValidation = new InputValidation(mActivity);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonLogin:
                verifyFromSQLite();
                break;
            case R.id.textViewLinkRegister:
                // Navigate to RegisterActivity
                Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intentRegister);
                break;
            case R.id.tv_link_forgot_password:
                showDialogAndRetrievePassword();
                break;

        }
    }

    private void showDialogAndRetrievePassword() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.forget_password);
        dialog.show();

        final EditText security= dialog.findViewById(R.id.security_hint_edit);
        final TextView getPass= dialog.findViewById(R.id.tv_cancel);

        Button ok= dialog.findViewById(R.id.get_password_btn);
        Button cancel= dialog.findViewById(R.id.cancel_btn);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=security.getText().toString();
                if(userName.equals(""))
                {
                    Toast.makeText(getApplicationContext()
                            , "Please enter your security hint", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String storedPassword = mDatabaseHelper.getAllTags(userName);
                    if(storedPassword==null)
                    {
                        Toast.makeText(getApplicationContext()
                                , "Please enter correct security hint"
                                , Toast.LENGTH_SHORT)
                                .show();
                    }else{
                        getPass.setText(storedPassword);
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    /**
     * This method is to validate the input text fields and verify login credentials from SQLite
     */
    private void verifyFromSQLite() {
        if (!mInputValidation.isInputEditTextFilled(mTextInputEditTextEmail
                , mTextInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!mInputValidation.isInputEditTextEmail(mTextInputEditTextEmail
                , mTextInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!mInputValidation.isInputEditTextFilled(mTextInputEditTextPassword
                , mTextInputLayoutPassword, getString(R.string.error_message_email))) {
            return;
        }

        if (mDatabaseHelper.checkUser(mTextInputEditTextEmail.getText().toString().trim()
                , mTextInputEditTextPassword.getText().toString().trim())) {
            if (mDatabaseHelper.checkAdmin(mTextInputEditTextEmail.getText().toString().trim())){
                mSession
                        .createLoginSession(mTextInputEditTextEmail.getText().toString(), true);
                Intent accountsIntent = new Intent(mActivity
                        , AdminMainActivity.class);
                accountsIntent.putExtra(AppConstants.sEMAIL, mTextInputEditTextEmail.getText().toString().trim());
                emptyInputEditText();
                startActivity(accountsIntent);
            } else {
                mSession
                        .createLoginSession(mTextInputEditTextEmail.getText().toString(), false);
                Intent accountsIntent = new Intent(mActivity
                        , UsersMainActivity.class);
                accountsIntent.putExtra(AppConstants.sEMAIL, mTextInputEditTextEmail.getText().toString().trim());
                emptyInputEditText();
                startActivity(accountsIntent);
            }
        } else {
            // Snack Bar to show success message that record is wrong
            Snackbar.make(mNestedScrollView, getString(R.string.error_valid_email_password)
                    , Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        mTextInputEditTextEmail.setText(null);
        mTextInputEditTextPassword.setText(null);
    }
}