package in.co.chicmic.samplereservationsystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import in.co.chicmic.samplereservationsystem.dataModels.User;
import in.co.chicmic.samplereservationsystem.utilities.AppConstants;
import in.co.chicmic.samplereservationsystem.utilities.DBBitmapUtilities;

public class DataBaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "RailwayReservationSample.db";

    // User table name
    private static final String TABLE_USER = "user";

    // User Table Columns names
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_SECURITY_HINT = "security_hint";
    private static final String COLUMN_IS_ADMIN = "is_admin";
    private static final String COLUMN_PROFILE_IMAGE = "profile_image";

    // create table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT," + COLUMN_SECURITY_HINT
            + " TEXT," + COLUMN_IS_ADMIN + " INTEGER," + COLUMN_PROFILE_IMAGE + " BLOB" +")";

    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

    /**
     * Constructor
     *
     * @param context of activity
     */
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);

        // Create tables again
        onCreate(db);

    }

    /**
     * This method is to create user record
     *
     * @param user to be added
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName().trim());
        values.put(COLUMN_USER_EMAIL, user.getEmail().trim());
        values.put(COLUMN_USER_PASSWORD, user.getPassword().trim());
        values.put(COLUMN_SECURITY_HINT, user.getSecurityHint().trim());
        values.put(COLUMN_PROFILE_IMAGE, DBBitmapUtilities.getBytes(user.getProfileImage()));
        if (user.getIsAdmin()){
            values.put(COLUMN_IS_ADMIN, AppConstants.sADMIN);
        } else {
            values.put(COLUMN_IS_ADMIN, AppConstants.sNOT_ADMIN);
        }

        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD,
                COLUMN_SECURITY_HINT,
                COLUMN_IS_ADMIN,
                COLUMN_PROFILE_IMAGE
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setName(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                user.setSecurityHint(cursor.getString(cursor.getColumnIndex(COLUMN_SECURITY_HINT)));
                user.setIsAdmin((cursor.getInt(cursor.getColumnIndex(COLUMN_IS_ADMIN))) == 1);
                user.setProfileImage(DBBitmapUtilities.getImage(cursor
                        .getBlob(cursor.getColumnIndex(COLUMN_PROFILE_IMAGE))));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    /**
     * This method to update user record
     *
     * @param user to be updated
     */
    public void updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_SECURITY_HINT, user.getSecurityHint());
        values.put(COLUMN_IS_ADMIN, user.getIsAdmin() ? 1 : 0);
        values.put(COLUMN_PROFILE_IMAGE, DBBitmapUtilities.getBytes(user.getProfileImage()));

        // updating row
        db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user to be deleted.
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_USER, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email of user
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;

    }

    /**
     * This method to check user exist or not
     *
     * @param email of user
     * @param password of user
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user
         * table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email =
         * 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        return cursorCount > 0;

    }

    public User getUserDetails(String pEmail){
        SQLiteDatabase db = this.getReadableDatabase();
        assert db != null;
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USER + " where " + COLUMN_USER_EMAIL
                        + " = '" + pEmail + "'"
                , null);
        User user = new User();
        if (c.moveToNext()) {
            user.setName(c.getString(c.getColumnIndex(COLUMN_USER_NAME)));
            user.setEmail(c.getString(c.getColumnIndex(COLUMN_USER_EMAIL)));
            user.setPassword(c.getString(c.getColumnIndex(COLUMN_USER_PASSWORD)));
            user.setSecurityHint(c.getString(c.getColumnIndex(COLUMN_SECURITY_HINT)));
            user.setIsAdmin(Integer.parseInt(c.getString(c.getColumnIndex(COLUMN_USER_ID))) == 1);
            user.setProfileImage(DBBitmapUtilities.getImage(c.getBlob(c.getColumnIndex(COLUMN_PROFILE_IMAGE))));
        }
        c.close();
        return user;
    }

    public String getAllTags(String a) {
        SQLiteDatabase db = this.getReadableDatabase();
        assert db != null;
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_USER + " where " + COLUMN_SECURITY_HINT
                        + " = '" +a + "'"
                , null);
        String str = null;
        if (c.moveToFirst()) {
            do {
                str = c.getString(c.getColumnIndex(COLUMN_USER_PASSWORD));
            } while (c.moveToNext());
        }
        c.close();
        return str;
    }
}
