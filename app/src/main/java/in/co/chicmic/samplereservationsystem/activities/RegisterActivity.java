package in.co.chicmic.samplereservationsystem.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private Uri mImageUri;
    String mImageFilePath;

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
        String email;
        String name;
        String password;
        String hint;
        String contact;
        String confirmPassword;

        if (mTextInputEditTextName.getText() != null) {
            name = mTextInputEditTextName.getText().toString().trim();
            if (name.length() < 3){
                mTextInputEditTextName.setError(getString(R.string.error_invalid_name));
                return;
            }
        } else {
            mTextInputEditTextName.setError(getString(R.string.error_message_name));
            return;
        }

        if (mTextInputEditTextEmail.getText() != null) {
            email = mTextInputEditTextEmail.getText().toString().trim();
            if (email.length() < 7){
                mTextInputEditTextEmail.setError(getString(R.string.error_invalid_email));
                return;
            }
        } else {
            mTextInputEditTextEmail.setError(getString(R.string.error_message_email));
            return;
        }


        if (mTextInputEditTextPassword.getText() != null) {
            password = mTextInputEditTextPassword.getText().toString().trim();
            if (password.length() < 5){
                mTextInputEditTextPassword.setError(getString(R.string.error_invalid_password_5));
                return;
            }
        } else {
            mTextInputEditTextPassword.setError(getString(R.string.error_message_password));
            return;
        }

        if (mTextInputEditTextSecurityHint.getText() != null) {
            hint = mTextInputEditTextSecurityHint.getText().toString().trim();
            if (hint.length() == 0){
                mTextInputEditTextSecurityHint.setError(getString(R.string.error_message_security_hint));
                return;
            }
        } else {
            mTextInputEditTextSecurityHint.setError(getString(R.string.error_message_security_hint));
            return;
        }

        if (mTIEContact.getText() != null) {
            contact = mTIEContact.getText().toString().trim();
            if (contact.length() < 10){
                mTIEContact.setError(getString(R.string.error_message_contact_valid));
                return;
            }
        } else {
            mTIEContact.setError(getString(R.string.error_message_contact));
            return;
        }
        if (mTextInputEditTextConfirmPassword.getText() != null) {
            confirmPassword = mTextInputEditTextConfirmPassword.getText().toString().trim();
            if (!confirmPassword.equals(password)){
                mTextInputEditTextConfirmPassword.setError(getString(R.string.error_password_match));
                return;
            }
        } else {
            mTextInputEditTextConfirmPassword.setError(getString(R.string.error_message_email));
            return;
        }

        if (!mInputValidation.isContactValid(mTIEContact.getText().toString().trim())){
            mTILContact.setError("Please Enter Valid contact number");
            return;
        }
        if (mImageUri == null){
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mGenderRG.getCheckedRadioButtonId() == -1){
            Toast.makeText(this, "Please Select Gender", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(mActivity, "Registering Please wait...", Toast.LENGTH_SHORT).show();
        emptyInputEditText();
        if (!mDatabaseHelper.checkUser(email)) {
            mUser.setName(name);
            mUser.setEmail(email);
            mUser.setPassword(password);
            mUser.setSecurityHint(hint);
            mUser.setIsAdmin(false);
            mUser.setProfileImageURI(mImageUri.toString());
            mUser.setContact(contact);
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
        mTIEContact.setText(null);
        mGenderRG.clearCheck();
        mTextInputEditTextSecurityHint.setText(null);
    }

    @Override
    public void clickImageAndLoad() {
        mDialog.dismiss();
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        } catch (IOException ex) {
            Log.e("Sonu", "clickImageAndLoad: " + ex);
        }
        if(photoFile != null) {
            mImageUri = FileProvider.getUriForFile(this
                    , "in.co.chicmic.samplereservationsystem.cameraImageProvider"
                    , photoFile);
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT,
                    mImageUri);
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            startActivityForResult(takePicture, AppConstants.sCLICK_IMAGE);
        }
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
                    mProfileImageView.setImageURI(mImageUri);
                }
                break;
            case AppConstants.sCHOOSE_IMAGE_FROM_GALLERY:
                if (pResultCode == RESULT_OK) {
                    mImageUri = pImageReturnedIntent.getData();
                    //use the bitmap as you like
                    mProfileImageView.setImageURI(mImageUri);
                    getContentResolver()
                            .takePersistableUriPermission (mImageUri
                                    , Intent.FLAG_GRANT_READ_URI_PERMISSION
                                            |Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                break;
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mImageFilePath = image.getAbsolutePath();
        return image;
    }
}
