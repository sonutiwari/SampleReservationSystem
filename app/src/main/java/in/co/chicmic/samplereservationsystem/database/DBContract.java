package in.co.chicmic.samplereservationsystem.database;

import android.provider.BaseColumns;

class DBContract {
    private DBContract(){}

    public static class UserTable implements BaseColumns{
        // User table name
        public static final String TABLE_USER = "user";

        // User Table Columns names
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_USER_EMAIL = "user_email";
        public static final String COLUMN_USER_PASSWORD = "user_password";
        public static final String COLUMN_SECURITY_HINT = "security_hint";
        public static final String COLUMN_IS_ADMIN = "is_admin";
        public static final String COLUMN_PROFILE_IMAGE = "profile_image";
        public static final String COLUMN_USER_CONTACT = "user_contact";
        public static final String COLUMN_USER_GENDER = "user_gender";
        public static final String COLUMN_USER_STATUS = "user_status"; //0 = not approved, 1 = approved, 2 = blocked;

        // create table sql query
        public static String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + _ID + " INTEGER PRIMARY KEY," + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT UNIQUE," + COLUMN_USER_PASSWORD + " TEXT,"
                + COLUMN_SECURITY_HINT + " TEXT," + COLUMN_IS_ADMIN + " INTEGER,"
                + COLUMN_PROFILE_IMAGE + " TEXT," + COLUMN_USER_CONTACT + " TEXT, "
                + COLUMN_USER_GENDER + " TEXT," + COLUMN_USER_STATUS + " INTEGER" + ")";

        // drop table sql query
        public static String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    }

    public static class TrainDetails implements BaseColumns{
        public static final String TABLE_NAME = "train_details";
        public static final String COLUMN_TRAIN_NAME = "train_name";
        public static final String COLUMN_NUMBER_OF_SEATS = "seats_in_train";
        public static final String COLUMN_NUMBER_OF_BOOKED_SEATS = "booked_seats";

        // create table query.
        public static String CREATE_TRAIN_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY, " + COLUMN_TRAIN_NAME + " TEXT UNIQUE, "
                + COLUMN_NUMBER_OF_SEATS + " INTEGER, "
                + COLUMN_NUMBER_OF_BOOKED_SEATS + " INTEGER " + ")";

        // drop table query.
        public static String DROP_TRAIN_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class BookingDetails implements BaseColumns{
        public static final String TABLE_NAME = "booking";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TRAIN_ID = TrainDetails._ID;
        public static final String COLUMN_USER_ID = UserTable._ID;
        public static final String COLUMN_NUMBER_OF_SEATS_BOOKED = "seats";

        // create table query.
        public static String CREATE_BOOKING_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY, " + COLUMN_DATE + " TEXT, "
                + COLUMN_TRAIN_ID + " INTEGER, "
                + COLUMN_USER_ID + " INTEGER, "
                + COLUMN_NUMBER_OF_SEATS_BOOKED + "INTEGER" + ")";

        // drop table query.
        public static String DROP_BOOKING_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
