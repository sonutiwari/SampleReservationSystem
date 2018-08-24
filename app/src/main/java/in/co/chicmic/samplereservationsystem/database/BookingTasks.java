package in.co.chicmic.samplereservationsystem.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import in.co.chicmic.samplereservationsystem.dataModels.BookingModel;

public class BookingTasks {
    private BookingTasks(){}
    private static String[] columns = {
            DBContract.BookingDetails._ID
            , DBContract.BookingDetails.COLUMN_DATE
            , DBContract.BookingDetails.COLUMN_TRAIN_ID
            , DBContract.BookingDetails.COLUMN_USER_ID
            , DBContract.BookingDetails.COLUMN_NUMBER_OF_SEATS_BOOKED
    };

    public static void addBookingData(BookingModel bookingModel, SQLiteDatabase pDB){
        ContentValues values = new ContentValues();
        values.put(DBContract.BookingDetails.COLUMN_DATE, bookingModel.getBookingDate());
        values.put(DBContract.BookingDetails.COLUMN_TRAIN_ID, bookingModel.getTrainId());
        values.put(DBContract.BookingDetails.COLUMN_USER_ID, bookingModel.getUserId());
        values.put(DBContract.BookingDetails.COLUMN_NUMBER_OF_SEATS_BOOKED, bookingModel.getNoOfSeats());
        pDB.insert(DBContract.BookingDetails.TABLE_NAME, null, values);
    }

    public static void upDateTrainDetails(BookingModel bookingModel, SQLiteDatabase pDB){
        ContentValues values = new ContentValues();
        if (bookingModel.getBookingDate() != null){
            values.put(DBContract.BookingDetails.COLUMN_DATE, bookingModel.getBookingDate());
        }
        if (bookingModel.getNoOfSeats() != 0){
            values.put(DBContract.BookingDetails.COLUMN_NUMBER_OF_SEATS_BOOKED
                    , bookingModel.getNoOfSeats());
        }
        if (bookingModel.getTrainId() != 0){
            values.put(DBContract.BookingDetails.COLUMN_TRAIN_ID
                    , bookingModel.getTrainId());
        }
        if (bookingModel.getUserId() != 0){
            values.put(DBContract.BookingDetails.COLUMN_USER_ID
                    , bookingModel.getUserId());
        }
        String where = DBContract.BookingDetails._ID + "=?";
        String[] selectionArgs = {String.valueOf(bookingModel.getPNR())};
        pDB.update(DBContract.TrainDetails.TABLE_NAME, values, where, selectionArgs);
    }

    public static void deleteTrainDetails(BookingModel bookingModel, SQLiteDatabase pDB){
        String where = DBContract.BookingDetails._ID + "=?";
        String[] selectionArgs = {String.valueOf(bookingModel.getPNR())};
        pDB.delete(DBContract.TrainDetails.TABLE_NAME, where, selectionArgs);
    }

    public static List<BookingModel> getAllTrains(int id, SQLiteDatabase pDB){
        List<BookingModel> trainList = new ArrayList<>();
        String sortedOrder = DBContract.BookingDetails.COLUMN_DATE + " ASC";

        String selection = DBContract.BookingDetails.COLUMN_USER_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        Cursor cursor = pDB.query(DBContract.BookingDetails.TABLE_NAME, columns
                , selection, selectionArgs
                , null, null, sortedOrder);
        while (cursor.moveToNext()){
            BookingModel model = new BookingModel();
            model.setPNR(cursor.getInt(cursor.getColumnIndex(DBContract.BookingDetails._ID)));
            model.setBookingDate(cursor.getString(cursor.getColumnIndex(DBContract.BookingDetails.COLUMN_DATE)));
            model.setNoOfSeats(cursor.getInt(cursor.getColumnIndex(DBContract.BookingDetails.COLUMN_NUMBER_OF_SEATS_BOOKED)));
            model.setTrainId(cursor.getInt(cursor.getColumnIndex(DBContract.BookingDetails.COLUMN_TRAIN_ID)));
            model.setUserId(cursor.getInt(cursor.getColumnIndex(DBContract.BookingDetails.COLUMN_USER_ID)));
            trainList.add(model);
        }
        cursor.close();
        return trainList;
    }

    public static List<BookingModel> getAllTrainsOnThisDate(String mDate
            , SQLiteDatabase pReadableDatabase) {
        List<BookingModel> trainList = new ArrayList<>();
        String sortedOrder = DBContract.BookingDetails.COLUMN_TRAIN_ID + " ASC";
        String selection = DBContract.BookingDetails.COLUMN_DATE + "=?";
        String[] selectionArgs = {mDate};
        Cursor cursor = pReadableDatabase.query(DBContract.BookingDetails.TABLE_NAME, columns
                , selection, selectionArgs
                , null, null, sortedOrder);
        while (cursor.moveToNext()){
            BookingModel model = new BookingModel();
            model.setPNR(cursor.getInt(cursor.getColumnIndex(DBContract.BookingDetails._ID)));
            model.setBookingDate(cursor.getString(cursor.getColumnIndex(DBContract.BookingDetails.COLUMN_DATE)));
            model.setNoOfSeats(cursor.getInt(cursor.getColumnIndex(DBContract.BookingDetails.COLUMN_NUMBER_OF_SEATS_BOOKED)));
            model.setTrainId(cursor.getInt(cursor.getColumnIndex(DBContract.BookingDetails.COLUMN_TRAIN_ID)));
            model.setUserId(cursor.getInt(cursor.getColumnIndex(DBContract.BookingDetails.COLUMN_USER_ID)));
            trainList.add(model);
        }
        cursor.close();
        return trainList;
    }
}
