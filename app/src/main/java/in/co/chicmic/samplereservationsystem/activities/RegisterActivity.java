package in.co.chicmic.samplereservationsystem.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.dataModels.User;
import in.co.chicmic.samplereservationsystem.database.DataBaseHelper;
import in.co.chicmic.samplereservationsystem.fragments.ChooseOptionsForLoadingImageFragment;
import in.co.chicmic.samplereservationsystem.listeners.ChooseImageLoadingOptions;
import in.co.chicmic.samplereservationsystem.utilities.AppConstants;
import in.co.chicmic.samplereservationsystem.utilities.InputValidation;

public class RegisterActivity extends AppCompatActivity
        implements View.OnClickListener, ChooseImageLoadingOptions {

    private DialogFragment mDialog;
    private final AppCompatActivity mActivity = RegisterActivity.this;

    private NestedScrollView mNestedScrollView;

    private TextInputLayout mTextInputLayoutName;
    private TextInputLayout mTextInputLayoutEmail;
    private TextInputLayout mTextInputLayoutPassword;
    private TextInputLayout mTextInputLayoutConfirmPassword;
    private TextInputLayout mTextInputLayoutSecurityHint;
    private TextInputLayout mTILContact;

    private TextInputEditText mTextInputEditTextName;
    private TextInputEditText mTextInputEditTextEmail;
    private TextInputEditText mTextInputEditTextPassword;
    private TextInputEditText mTextInputEditTextConfirmPassword;
    private TextInputEditText mTextInputEditTextSecurityHint;
    private TextInputEditText mTIEContact;

    private RadioGroup mGenderRG;
    private AppCompatButton mAppCompatButtonRegister;
    private AppCompatTextView mAppCompatTextViewLoginLink;

    private FrameLayout mImageFrame;
    private CircleImageView mProfileImageView;

    private InputValidation mInputValidation;
    private DataBaseHelper mDatabaseHelper;
    private User mUser;
    private Bitmap mBitMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        mNestedScrollView = findViewById(R.id.nestedScrollView);

        mTextInputLayoutName = findViewById(R.id.textInputLayoutName);
        mTextInputLayoutEmail = findViewById(R.id.textInputLayoutEmail);
        mTextInputLayoutPassword = findViewById(R.id.textInputLayoutPassword);
        mTextInputLayoutConfirmPassword = findViewById(R.id.textInputLayoutConfirmPassword);
        mTextInputLayoutSecurityHint = findViewById(R.id.til_security_hint);
        mTILContact = findViewById(R.id.textInputLayoutContact);

        mTextInputEditTextName = findViewById(R.id.textInputEditTextName);
        mTextInputEditTextEmail = findViewById(R.id.textInputEditTextEmail);
        mTextInputEditTextPassword = findViewById(R.id.textInputEditTextPassword);
        mTextInputEditTextConfirmPassword = findViewById(R.id.textInputEditTextConfirmPassword);
        mTextInputEditTextSecurityHint = findViewById(R.id.tie_security_hint);
        mTIEContact = findViewById(R.id.textInputEditTextContact);

        mGenderRG = findViewById(R.id.gender_rg);
        mAppCompatButtonRegister = findViewById(R.id.appCompatButtonRegister);

        mAppCompatTextViewLoginLink = findViewById(R.id.appCompatTextViewLoginLink);
        mImageFrame = findViewById(R.id.image_frame);
        mProfileImageView = findViewById(R.id.profile_image);
    }

    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        mAppCompatButtonRegister.setOnClickListener(this);
        mAppCompatTextViewLoginLink.setOnClickListener(this);
        mImageFrame.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        mInputValidation = new InputValidation(mActivity);
        mDatabaseHelper = new DataBaseHelper(mActivity);
        mUser = new User();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;
            case R.id.appCompatTextViewLoginLink:
                finish();
                break;
            case R.id.image_frame:
                showClickOrChooseImageDialog();
        }
    }

    private void showClickOrChooseImageDialog() {
        mDialog = ChooseOptionsForLoadingImageFragment.newInstance(this);
        mDialog.show(getSupportFragmentManager(), AppConstants.sIMAGE_SELECT_OR_CLICK);
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        if (!mInputValidation.isInputEditTextFilled(mTextInputEditTextName
                , mTextInputLayoutName, getString(R.string.error_message_name))) {
            return;
        }
        if (!mInputValidation.isInputEditTextFilled(mTextInputEditTextEmail
                , mTextInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!mInputValidation.isInputEditTextEmail(mTextInputEditTextEmail
                , mTextInputLayoutEmail, getString(R.string.error_message_email))) {
            return;
        }
        if (!mInputValidation.isInputEditTextFilled(mTextInputEditTextPassword
                , mTextInputLayoutPassword, getString(R.string.error_message_password))) {
            return;
        }
        if (!mInputValidation.isInputEditTextMatches(mTextInputEditTextPassword
                , mTextInputEditTextConfirmPassword,
                mTextInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return;
        }
        if (!mInputValidation.isInputEditTextFilled(mTextInputEditTextSecurityHint
                , mTextInputLayoutSecurityHint, getString(R.string.error_message_security_hint))) {
            return;
        }

        if (!mInputValidation.isInputEditTextFilled(mTIEContact
                , mTILContact, getString(R.string.error_message_contact))) {
            return;
        }
        if (!mInputValidation.isContactValid(mTIEContact.getText().toString().trim())){
            mTILContact.setError("Please Enter Valid contact number");
            return;
        }
        if (mBitMap == null){
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mGenderRG.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mDatabaseHelper.checkUser(mTextInputEditTextEmail.getText().toString().trim())) {
            mUser.setName(mTextInputEditTextName.getText().toString().trim());
            mUser.setEmail(mTextInputEditTextEmail.getText().toString().trim());
            mUser.setPassword(mTextInputEditTextPassword.getText().toString().trim());
            mUser.setSecurityHint(mTextInputEditTextSecurityHint.getText().toString());
            mUser.setIsAdmin(false);
            mUser.setProfileImage(mBitMap);
            mUser.setContact(mTIEContact.getText().toString().trim());
            mUser.setIsApproved(0);
            int id = mGenderRG.getCheckedRadioButtonId();
            if (id == R.id.gender_male){
                mUser.setGender("Male");
            } else {
                mUser.setGender("Female");
            }

            mDatabaseHelper.addUser(mUser);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(mNestedScrollView, getString(R.string.success_message)
                    , Snackbar.LENGTH_LONG).show();
            emptyInputEditText();

            startActivity(new Intent(this, LoginActivity.class));

        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(mNestedScrollView, getString(R.string.error_email_exists)
                    , Snackbar.LENGTH_LONG).show();
        }


    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        mTextInputEditTextName.setText(null);
        mTextInputEditTextEmail.setText(null);
        mTextInputEditTextPassword.setText(null);
        mTextInputEditTextConfirmPassword.setText(null);
    }

    @Override
    public void clickImageAndLoad() {
        mDialog.dismiss();
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, AppConstants.sCLICK_IMAGE);
    }

    @Override
    public void loadImageFromGallery() {
        mDialog.dismiss();
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, AppConstants.sCHOOSE_IMAGE_FROM_GALLERY);
    }

    protected void onActivityResult(int pRequestCode
            , int pResultCode, Intent pImageReturnedIntent) {
        super.onActivityResult(pRequestCode, pResultCode, pImageReturnedIntent);
        switch (pRequestCode) {
            case AppConstants.sCLICK_IMAGE:
                if (pResultCode == RESULT_OK) {
                    Bundle extras = pImageReturnedIntent.getExtras();
                    if (extras != null){
                        mBitMap = (Bitmap) extras.get("data");
                        mProfileImageView.setImageBitmap(mBitMap);
                    }

                }
                break;
            case AppConstants.sCHOOSE_IMAGE_FROM_GALLERY:
                if (pResultCode == RESULT_OK) {
                    Uri pickedImage = pImageReturnedIntent.getData();
                    try {
                        mBitMap = MediaStore.Images.Media.getBitmap(this.getContentResolver()
                                , pickedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //use the bitmap as you like
                    mProfileImageView.setImageBitmap(mBitMap);
                }
                break;
        }
    }
}
