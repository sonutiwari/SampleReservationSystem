package in.co.chicmic.samplereservationsystem.activities;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.dataModels.TrainModel;
import in.co.chicmic.samplereservationsystem.database.AdminDBTasks;
import in.co.chicmic.samplereservationsystem.database.DataBaseHelper;
import in.co.chicmic.samplereservationsystem.utilities.AppConstants;

public class UpdateTrainDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    private DataBaseHelper mDBHelper;
    private Button mUpdateButton;
    private Button mDeleteButton;
    private TextInputEditText mTrainNameEditText;
    private TextInputEditText mNoOfSeatsEditText;
    private TextView mTrainNoTV;
    private String mNoOfSeats;
    private String mTrainName;
    private TrainModel mTrain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_train_details);
        initViews();
        setData();
        setListeners();
    }

    private void initViews() {
        mTrain = new TrainModel();
        mDBHelper = new DataBaseHelper(this);
        mUpdateButton = findViewById(R.id.btn_update_train);
        mDeleteButton = findViewById(R.id.btn_delete_train);
        mTrainNameEditText = findViewById(R.id.tie_enter_train_name);
        mNoOfSeatsEditText = findViewById(R.id.tie_enter_no_of_seats);
        mTrainNoTV = findViewById(R.id.tv_train_no);
    }

    private void setData(){
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            mTrainNoTV.setText(String.valueOf(bundle.getInt(AppConstants.sTRAIN_ID)));
            mTrainNameEditText.setText(bundle.getString(AppConstants.sTRAIN_NAME));
            mNoOfSeatsEditText.setText(String.valueOf(bundle.getInt(AppConstants.sTRAIN_NO_OF_SEATS)));
        }
    }
    private void setListeners(){
        mUpdateButton.setOnClickListener(this);
        mDeleteButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_update_train:
                if (validInput()){
                    updateDB();
                }
                break;
            case R.id.btn_delete_train:
                if (validInput()){
                    showDialogAndTakeAction();
                }
        }
    }

    private void showDialogAndTakeAction() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.ask_delete_this_train)
                .setMessage(R.string.delete_train_message)
                .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTrainFromDB();
                    }
                }).setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    private void deleteTrainFromDB() {
        setTrainDetails();
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        AdminDBTasks.deleteTrainDetails(mTrain, db);
        db.close();
        cleanUI();
        Toast.makeText(this, "Train Deleted", Toast.LENGTH_SHORT).show();
    }

    private void updateDB() {
        setTrainDetails();
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        AdminDBTasks.upDateTrainDetails(mTrain, db);
        db.close();
        cleanUI();
        Toast.makeText(this, "Train Added", Toast.LENGTH_SHORT).show();
    }

    private void setTrainDetails(){
        mTrain.setTrainName(mTrainName);
        mTrain.setNoOfSeats(Integer.parseInt(mNoOfSeats));
        mTrain.setTrainId(Integer.parseInt(mTrainNoTV.getText().toString()));
    }

    private void cleanUI() {
        mTrainNameEditText.setText(null);
        mNoOfSeatsEditText.setText(null);
        mTrainNoTV.setText(null);
        finish();
    }

    private boolean validInput() {
        mNoOfSeats= mNoOfSeatsEditText.getText().toString().trim();
        mTrainName = mTrainNameEditText.getText().toString().trim();
        if (mNoOfSeats.isEmpty()){
            mNoOfSeatsEditText.setError("Number of seats can't be empty.");
            return false;
        } else if (mNoOfSeats.length() < 2){
            mNoOfSeatsEditText.setError("Number of seats can't be less than 4 digits.");
            return false;
        }

        if (mTrainName.isEmpty()){
            mTrainNameEditText.setError("Train Name can't be empty.");
            return false;
        } else if (mTrainName.length() < 4){
            mTrainNameEditText.setError("Train Name can't be less than 4 letters.");
            return false;
        }
        return true;
    }
}
