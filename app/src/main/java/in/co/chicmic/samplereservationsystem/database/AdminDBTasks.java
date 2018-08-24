package in.co.chicmic.samplereservationsystem.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import in.co.chicmic.samplereservationsystem.dataModels.TrainModel;

public class AdminDBTasks {
    private AdminDBTasks(){}
    private static String[] columns = {
            DBContract.TrainDetails._ID
            , DBContract.TrainDetails.COLUMN_TRAIN_NAME
            , DBContract.TrainDetails.COLUMN_NUMBER_OF_SEATS
            , DBContract.TrainDetails.COLUMN_NUMBER_OF_BOOKED_SEATS
    };

    public static void addTrains(TrainModel pTrainData, SQLiteDatabase pDB){
        ContentValues values = new ContentValues();
        values.put(DBContract.TrainDetails.COLUMN_TRAIN_NAME, pTrainData.getTrainName());
        values.put(DBContract.TrainDetails.COLUMN_NUMBER_OF_SEATS, pTrainData.getNoOfSeats());
        values.put(DBContract.TrainDetails.COLUMN_NUMBER_OF_BOOKED_SEATS, pTrainData.getNoOfBookedSeats());
        pDB.insert(DBContract.TrainDetails.TABLE_NAME, null, values);
    }

    public static void upDateTrainDetails(TrainModel pTrainData, SQLiteDatabase pDB){
        ContentValues values = new ContentValues();
        if (pTrainData.getTrainName() != null){
            values.put(DBContract.TrainDetails.COLUMN_TRAIN_NAME, pTrainData.getTrainName());
        }
        if (pTrainData.getNoOfSeats() != 0){
            values.put(DBContract.TrainDetails.COLUMN_NUMBER_OF_SEATS, pTrainData.getNoOfSeats());
        }
        if (pTrainData.getNoOfBookedSeats() != 0){
            values.put(DBContract.TrainDetails.COLUMN_NUMBER_OF_BOOKED_SEATS
                    , pTrainData.getNoOfBookedSeats());
        }
        String where = DBContract.TrainDetails._ID + "=?";
        String[] selectionArgs = {String.valueOf(pTrainData.getTrainId())};
        pDB.update(DBContract.TrainDetails.TABLE_NAME, values, where, selectionArgs);
    }

    public static void deleteTrainDetails(TrainModel pTrainData, SQLiteDatabase pDB){
        String where = DBContract.TrainDetails._ID + "=?";
        String[] selectionArgs = {String.valueOf(pTrainData.getTrainId())};
        pDB.delete(DBContract.TrainDetails.TABLE_NAME, where, selectionArgs);
    }

    public static List<TrainModel> getAllTrains(SQLiteDatabase pDB){
        List<TrainModel> trainList = new ArrayList<>();
        String sortedOrder = DBContract.TrainDetails.COLUMN_TRAIN_NAME + " ASC";
        Cursor cursor = pDB.query(DBContract.TrainDetails.TABLE_NAME, columns, null, null
                , null, null, sortedOrder);
        while (cursor.moveToNext()){
            TrainModel model = new TrainModel();
            model.setTrainId(cursor.getInt(cursor.getColumnIndex(DBContract.TrainDetails._ID)));
            model.setTrainName(cursor.getString(cursor.getColumnIndex(DBContract.TrainDetails.COLUMN_TRAIN_NAME)));
            model.setNoOfSeats(cursor.getInt(cursor.getColumnIndex(DBContract.TrainDetails.COLUMN_NUMBER_OF_SEATS)));
            model.setNoOfBookedSeats(cursor.getInt(cursor.getColumnIndex(DBContract.TrainDetails.COLUMN_NUMBER_OF_BOOKED_SEATS)));
            trainList.add(model);
        }
        cursor.close();
        return trainList;
    }

}
