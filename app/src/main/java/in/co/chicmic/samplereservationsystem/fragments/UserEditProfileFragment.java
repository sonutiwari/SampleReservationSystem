package in.co.chicmic.samplereservationsystem.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.dataModels.User;
import in.co.chicmic.samplereservationsystem.database.DataBaseHelper;
import in.co.chicmic.samplereservationsystem.listeners.ChooseImageLoadingOptions;
import in.co.chicmic.samplereservationsystem.listeners.UserEditProfileClickListener;
import in.co.chicmic.samplereservationsystem.sessionManager.SessionManager;
import in.co.chicmic.samplereservationsystem.utilities.AppConstants;

import static android.app.Activity.RESULT_OK;

public class UserEditProfileFragment extends Fragment implements View.OnClickListener, ChooseImageLoadingOptions {
    private UserEditProfileClickListener mListener;
    private CircleImageView mProfileImageView;
    private TextInputEditText mTextInputEditTextName;
    private TextInputEditText mTextInputEditTextEmail;
    private TextInputEditText mTextInputEditTextSecurityHint;
    private TextInputEditText mTIEContact;
    private RadioGroup mGenderRG;
    private AppCompatButton mAppCompatButtonRegister;
    private FrameLayout mImageFrame;
    private User mUser;
    private Uri mImageUri;
    private View mView;
    private DialogFragment mDialog;
    private DataBaseHelper mDataBaseHelper;

    public UserEditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.activity_register, container, false);
        initViews();
        initListeners();
        setUpViews();
        return mView;
    }

    private void initViews() {
        mTextInputEditTextName = mView.findViewById(R.id.textInputEditTextName);
        mTextInputEditTextEmail = mView.findViewById(R.id.textInputEditTextEmail);
        mView.findViewById(R.id.textInputEditTextPassword).setVisibility(View.GONE);
        mView.findViewById(R.id.textInputEditTextConfirmPassword).setVisibility(View.GONE);
        mView.findViewById(R.id.textInputLayoutPassword).setVisibility(View.GONE);
        mView.findViewById(R.id.textInputLayoutConfirmPassword).setVisibility(View.GONE);
        mTextInputEditTextSecurityHint = mView.findViewById(R.id.tie_security_hint);
        mTIEContact = mView.findViewById(R.id.textInputEditTextContact);
        mGenderRG = mView.findViewById(R.id.gender_rg);
        mAppCompatButtonRegister = mView.findViewById(R.id.appCompatButtonRegister);
        mAppCompatButtonRegister.setText(R.string.update);
        mView.findViewById(R.id.appCompatTextViewLoginLink).setVisibility(View.GONE);
        mImageFrame = mView.findViewById(R.id.image_frame);
        mProfileImageView = mView.findViewById(R.id.profile_image);
        mDataBaseHelper = new DataBaseHelper(getActivity());
        mUser = mDataBaseHelper.getUserDetails(getUserEmail());
    }

    private String getUserEmail() {
        SessionManager sm = new SessionManager(getContext().getApplicationContext());
        HashMap<String, String> map = sm.getUserDetails();
        return map.get(SessionManager.KEY_EMAIL);
    }

    private void setUpViews() {
        mProfileImageView.setImageURI(Uri.parse(mUser.getProfileImageURI()));
        mTextInputEditTextName.setText(mUser.getName());
        mTextInputEditTextEmail.setText(mUser.getEmail());
        mTextInputEditTextSecurityHint.setText(mUser.getSecurityHint());
        mTIEContact.setText(mUser.getContact());

        if (mUser.getGender().equalsIgnoreCase(AppConstants.sMALE)){
            mGenderRG.check(R.id.gender_female);
        } else {
            mGenderRG.check(R.id.gender_male);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserEditProfileClickListener) {
            mListener = (UserEditProfileClickListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    private void initListeners() {
        mAppCompatButtonRegister.setOnClickListener(this);
        mImageFrame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.appCompatButtonRegister:
                postDataToSQLite();
                break;
            case R.id.image_frame:
                showClickOrChooseImageDialog();
        }
    }

    private void showClickOrChooseImageDialog() {
        mDialog = ChooseOptionsForLoadingImageFragment.newInstance(this);
        mDialog.show(getChildFragmentManager(), AppConstants.sIMAGE_SELECT_OR_CLICK);
    }

    private void emptyInputEditText() {
        mTextInputEditTextName.setText(null);
        mTextInputEditTextEmail.setText(null);
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
            mImageUri = FileProvider.getUriForFile(getContext()
                    , AppConstants.sAUTHORITY
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
        Intent pickPhoto = new Intent(Intent.ACTION_OPEN_DOCUMENT,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, AppConstants.sCHOOSE_IMAGE_FROM_GALLERY);
    }

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat(AppConstants.sDATE_PATTERN,
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir =
                getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        String mImageFilePath = image.getAbsolutePath();
        return image;
    }

    private void postDataToSQLite() {
        String email;
        String name;
        String hint;
        String contact;

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

        Toast.makeText(getActivity(), R.string.registering_please_wait, Toast.LENGTH_SHORT).show();
        emptyInputEditText();
        if (mDataBaseHelper.checkUser(email)) {
            mUser.setName(name);
            mUser.setEmail(email);
            mUser.setSecurityHint(hint);
            mUser.setIsAdmin(false);
            if (mImageUri != null){
                mUser.setProfileImageURI(mImageUri.toString());
            }
            mUser.setContact(contact);
            mUser.setIsApproved(AppConstants.sSTATUS_APPROVED);
            int id = mGenderRG.getCheckedRadioButtonId();
            if (id == R.id.gender_male){
                mUser.setGender(AppConstants.sMALE);
            } else {
                mUser.setGender(AppConstants.sFEMALE);
            }
            mDataBaseHelper.updateUserProfile(mUser);
            mListener.updateProfile();
            Toast.makeText(getActivity(), R.string.success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.failed, Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int pRequestCode
            , int pResultCode, Intent pImageReturnedIntent) {
        super.onActivityResult(pRequestCode, pResultCode, pImageReturnedIntent);
        switch (pRequestCode) {
            case AppConstants.sCLICK_IMAGE:
                if (pResultCode == RESULT_OK) {
                    if (mImageUri != null) {
                        InputStream is;
                        try {
                            is = getActivity().getContentResolver().openInputStream(mImageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            mProfileImageView.setImageBitmap(bitmap);
                            if (is != null) {
                                is.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            case AppConstants.sCHOOSE_IMAGE_FROM_GALLERY:
                if (pResultCode == RESULT_OK) {
                    mImageUri = pImageReturnedIntent.getData();
                    mProfileImageView.setImageURI(mImageUri);
                }
                break;
        }
    }
}
