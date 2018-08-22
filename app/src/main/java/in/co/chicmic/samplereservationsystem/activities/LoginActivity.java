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

        final EditText securityHintEditText= dialog.findViewById(R.id.security_hint_edit);
        final EditText emailEditText = dialog.findViewById(R.id.email_hint_edit);
        final TextView getPass= dialog.findViewById(R.id.tv_cancel);

        Button getPassword= dialog.findViewById(R.id.get_password_btn);
        Button cancel= dialog.findViewById(R.id.cancel_btn);

        getPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String securityHint=securityHintEditText.getText().toString().trim();
                String emailHint = emailEditText.getText().toString().trim();
                if (!emailHint.isEmpty() && !securityHint.isEmpty())
                {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailHint).matches()){
                        String storedPassword =
                                mDatabaseHelper.getPasswordRemainder(securityHint, emailHint);
                        if(storedPassword==null)
                        {
                            Toast.makeText(getApplicationContext()
                                    , R.string.we_did_not_found_record
                                    , Toast.LENGTH_SHORT)
                                    .show();
                        }else{
                            getPass.setText(storedPassword);
                        }
                    } else {
                        Toast.makeText(getApplicationContext()
                                , R.string.invalid_email_login_hint
                                , Toast.LENGTH_SHORT)
                                .show();
                    }

                } else {
                    Toast.makeText(getApplicationContext()
                            , R.string.email_or_hint_empty
                            , Toast.LENGTH_SHORT)
                            .show();
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
                if (mDatabaseHelper.getUserDetails(mTextInputEditTextEmail.getText().toString().trim()).getIsApproved() == 1){
                    mSession
                            .createLoginSession(mTextInputEditTextEmail.getText().toString(), false);
                    Intent accountsIntent = new Intent(mActivity
                            , UsersMainActivity.class);
                    accountsIntent.putExtra(AppConstants.sEMAIL, mTextInputEditTextEmail.getText().toString().trim());
                    emptyInputEditText();
                    startActivity(accountsIntent);
                } else if (mDatabaseHelper.getUserDetails(mTextInputEditTextEmail.getText().toString().trim()).getIsApproved() == 2){
                    Toast.makeText(this, R.string.blocked_id_message, Toast.LENGTH_SHORT).show();
                } else {
                    // Snack Bar to show success message that record is wrong
                    Toast.makeText(this, R.string.yet_to_approve_string, Toast.LENGTH_SHORT).show();
                }
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