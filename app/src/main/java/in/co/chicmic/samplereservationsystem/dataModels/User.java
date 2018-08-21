package in.co.chicmic.samplereservationsystem.dataModels;

import android.graphics.Bitmap;

public class User {

    private int mId;
    private String mName;
    private String mEmail;
    private String mPassword;
    private String mSecurityHint;
    private boolean mIsAdmin;
    private Bitmap mProfileImage;
    private String mContact;
    private String mGender;
    private int mIsApproved; // 0 = not approved, 1 = approved, 2 = blocked;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getSecurityHint() {
        return mSecurityHint;
    }

    public void setSecurityHint(String mSecurityHint) {
        this.mSecurityHint = mSecurityHint;
    }

    public boolean getIsAdmin() {
        return mIsAdmin;
    }

    public void setIsAdmin(boolean pIsAdmin) {
        this.mIsAdmin = pIsAdmin;
    }

    public Bitmap getProfileImage() {
        return mProfileImage;
    }

    public void setProfileImage(Bitmap mProfileImage) {
        this.mProfileImage = mProfileImage;
    }

    public String getContact() {
        return mContact;
    }

    public void setContact(String mContact) {
        this.mContact = mContact;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String mGender) {
        this.mGender = mGender;
    }

    public int getIsApproved() {
        return mIsApproved;
    }

    public void setIsApproved(int mIsApproved) {
        this.mIsApproved = mIsApproved;
    }
}