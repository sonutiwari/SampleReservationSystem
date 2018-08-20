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
}