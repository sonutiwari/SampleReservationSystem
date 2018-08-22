package in.co.chicmic.samplereservationsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import in.co.chicmic.samplereservationsystem.dataModels.User;
import in.co.chicmic.samplereservationsystem.database.DBContract.UserTable;
import in.co.chicmic.samplereservationsystem.utilities.AppConstants;

public class DataBaseHelper extends SQLiteOpenHelper {
    private User mUser;
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // array of columns to fetch
    private String[] mColumns = {
            UserTable._ID,
            UserTable.COLUMN_USER_EMAIL,
            UserTable.COLUMN_USER_NAME,
            UserTable.COLUMN_USER_PASSWORD,
            UserTable.COLUMN_SECURITY_HINT,
            UserTable.COLUMN_IS_ADMIN,
            UserTable.COLUMN_PROFILE_IMAGE,
            UserTable.COLUMN_USER_CONTACT,
            UserTable.COLUMN_USER_GENDER,
            UserTable.COLUMN_USER_STATUS
    };

    // Database Name
    private static final String DATABASE_NAME = "RailwayReservationSample.db";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTable.CREATE_USER_TABLE);
        addAdmin(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserTable.DROP_USER_TABLE);
        onCreate(db);
    }

    private void addAdmin(SQLiteDatabase db) {
        ContentValues values = setUserDetailsToSendInDB(
                "Sonu", "sonu@android.com", "sonutjtj"
                , "sonutj", AppConstants.sADMIN,null
                , "1234567890", "Male"
                , AppConstants.sSTATUS_APPROVED
        );
        db.insert(UserTable.TABLE_USER, null, values);
    }

    private ContentValues setUserDetailsToSendInDB(String pName, String pEmail, String pPassword
            , String pSecurityHint, int pIsAdmin, String pImageUri
            , String pContact, String pGender, int pStatus) {
        ContentValues values = new ContentValues();
        values.put(UserTable.COLUMN_USER_NAME, pName);
        values.put(UserTable.COLUMN_USER_EMAIL, pEmail);
        values.put(UserTable.COLUMN_USER_PASSWORD, pPassword);
        values.put(UserTable.COLUMN_SECURITY_HINT, pSecurityHint);
        values.put(UserTable.COLUMN_IS_ADMIN, pIsAdmin);
        values.put(UserTable.COLUMN_PROFILE_IMAGE, pImageUri);
        values.put(UserTable.COLUMN_USER_CONTACT, pContact);
        values.put(UserTable.COLUMN_USER_GENDER, pGender);
        values.put(UserTable.COLUMN_USER_STATUS, pStatus);
        return values;
    }


    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = setUserDetailsToSendInDB(
                user.getName().trim(), user.getEmail().trim(), user.getPassword().trim()
                , user.getSecurityHint().trim(), AppConstants.sNOT_ADMIN,user.getProfileImageURI()
                , user.getContact().trim(), user.getGender(), user.getIsApproved()
        );
        db.insert(UserTable.TABLE_USER, null, values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        String sortOrder =
                UserTable.COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(UserTable.TABLE_USER, //Table to query
                mColumns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder);
        if (cursor.moveToFirst()) {
            do {
               mUser = new User();
                setUserDetails(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(UserTable._ID)))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_NAME))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_EMAIL))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_PASSWORD))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_SECURITY_HINT))
                        , (cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_IS_ADMIN))) == 1
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_PROFILE_IMAGE))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_CONTACT))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_GENDER))
                        , cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_USER_STATUS))
                );
                userList.add(mUser);
            } while (cursor.moveToNext());
        }
        mUser = null;
        cursor.close();
        db.close();
        return userList;
    }

    public void updateUserStatus(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserTable.COLUMN_USER_STATUS, user.getIsApproved());
        db.update(UserTable.TABLE_USER, values
                , UserTable._ID + " = ?"
                , new String[]{String.valueOf(user.getId())});
        db.close();
    }

    private void deleteUser(User user) {
        // delete user record by id
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UserTable.TABLE_USER, UserTable._ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    public boolean checkUser(String email) {
        String[] columns = {
                UserTable._ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = UserTable.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(UserTable.TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;
    }

    public boolean checkUser(String email, String password) {
        String[] columns = {
                UserTable._ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = UserTable.COLUMN_USER_EMAIL
                + " = ?" + " AND " + UserTable.COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(UserTable.TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();
        return cursorCount > 0;

    }

    public User getUserDetails(String pEmail) {
        String selection = UserTable.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {pEmail};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(UserTable.TABLE_USER
                ,mColumns
                , selection
                , selectionArgs
                , null
                , null
                , null
        );
        mUser = new User();
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                setUserDetails(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(UserTable._ID)))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_NAME))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_EMAIL))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_PASSWORD))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_SECURITY_HINT))
                        , (cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_IS_ADMIN))) == 1
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_PROFILE_IMAGE))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_CONTACT))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_GENDER))
                        , cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_USER_STATUS))
                );
            }
        }
        cursor.close();
        return mUser;
    }

    public String getPasswordRemainder(String pHint, String pEmail) {
        String selection = UserTable.COLUMN_USER_EMAIL + "=? AND "
                + UserTable.COLUMN_SECURITY_HINT + "=?";
        String[] selectionArgs = {pEmail, pHint};
        SQLiteDatabase db = this.getReadableDatabase();
        assert db != null;
        Cursor cursor = db.query(UserTable.TABLE_USER
                , mColumns
                ,selection
                ,selectionArgs
                ,null
                , null
                , null
                );
        String str = null;
        if (cursor.moveToFirst()) {
            do {
                str = cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_PASSWORD));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return str;
    }

    public boolean checkAdmin(String pEmail) {
        String[] columns = {
                UserTable.COLUMN_IS_ADMIN
        };
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = UserTable.COLUMN_USER_EMAIL + " = ?";
        String[] selectionArgs = {pEmail};
        Cursor cursor = db.query(UserTable.TABLE_USER,
                columns,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor != null && cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                return Integer.parseInt(cursor
                        .getString(cursor.getColumnIndex(UserTable.COLUMN_IS_ADMIN))) == 1;
            }
            cursor.close();
        }
        db.close();
        return false;
    }

    public List<User> getAllUnApprovedUsers() {
        String sortOrder =
                UserTable.COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<>();
        String selection = UserTable.COLUMN_USER_STATUS + "=?";
        String[] selectionArgs = {"0"};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(UserTable.TABLE_USER,
                mColumns,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );

        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            do {
                mUser = new User();
                setUserDetails(
                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(UserTable._ID)))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_NAME))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_EMAIL))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_PASSWORD))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_SECURITY_HINT))
                        , (cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_IS_ADMIN))) == 1
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_PROFILE_IMAGE))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_CONTACT))
                        , cursor.getString(cursor.getColumnIndex(UserTable.COLUMN_USER_GENDER))
                        , cursor.getInt(cursor.getColumnIndex(UserTable.COLUMN_USER_STATUS))
                );
                userList.add(mUser);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        mUser = null;
        return userList;
    }

    private void setUserDetails(int pId, String pName, String pEmail
            , String pPassword, String pSecurityHint, boolean pIsAdmin
            , String pImageUri, String pContact,  String pGender, int pIsApproved){
        mUser.setId(pId);
        mUser.setName(pName);
        mUser.setEmail(pEmail);
        mUser.setPassword(pPassword);
        mUser.setSecurityHint(pSecurityHint);
        mUser.setIsAdmin(pIsAdmin);
        mUser.setProfileImageURI(pImageUri);
        mUser.setContact(pContact);
        mUser.setGender(pGender);
        mUser.setIsApproved(pIsApproved);
    }
}