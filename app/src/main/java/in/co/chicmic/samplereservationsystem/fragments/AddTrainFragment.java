package in.co.chicmic.samplereservationsystem.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.dataModels.TrainModel;
import in.co.chicmic.samplereservationsystem.database.AdminDBTasks;
import in.co.chicmic.samplereservationsystem.database.DataBaseHelper;
import in.co.chicmic.samplereservationsystem.listeners.AddTrainFragmentListener;
import in.co.chicmic.samplereservationsystem.utilities.AppConstants;

public class AddTrainFragment extends Fragment implements View.OnClickListener {
    private AddTrainFragmentListener mListener;
    private TextInputEditText mTrainNameEditText;
    private TextInputEditText mNumberOfSeatsEditText;
    private Button mAddTrainButton;
    private View mView;
    private TrainModel mTrain;
    private DataBaseHelper mDataBaseHelper;
    private String mNoOfSeats;
    private String mTrainName;


    public AddTrainFragment() {
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
        mView = inflater.inflate(R.layout.fragment_add_train, container, false);
        initViews();
        mAddTrainButton.setOnClickListener(this);
        return mView;
    }

    private void initViews() {
        mTrainNameEditText = mView.findViewById(R.id.tie_enter_train_name);
        mNumberOfSeatsEditText = mView.findViewById(R.id.tie_enter_no_of_seats);
        mAddTrainButton = mView.findViewById(R.id.btn_add_train);
        mDataBaseHelper = new DataBaseHelper(getActivity());
        mTrain = new TrainModel();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AddTrainFragmentListener) {
            mListener = (AddTrainFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add_train:
                if (validInput()){
                    addToDB(mTrain);
                }
        }
    }

    private void addToDB(TrainModel mTrain) {
        mTrain.setTrainName(mTrainName);
        mTrain.setNoOfSeats(Integer.parseInt(mNoOfSeats));
        mTrain.setNoOfBookedSeats(AppConstants.sINITIAL_BOOKED_SEATS);
        SQLiteDatabase db = mDataBaseHelper.getWritableDatabase();
        AdminDBTasks.addTrains(mTrain, db);
        db.close();
        cleanUI();
        Toast.makeText(getActivity(), "Train Added", Toast.LENGTH_SHORT).show();
    }

    private void cleanUI() {
        mTrainNameEditText.setText(null);
        mNumberOfSeatsEditText.setText(null);
    }

    private boolean validInput() {
        mNoOfSeats = mNumberOfSeatsEditText.getText().toString().trim();
        mTrainName = mTrainNameEditText.getText().toString().trim();
        if (mNoOfSeats.isEmpty()){
            mNumberOfSeatsEditText.setError("Number of seats can't be empty.");
            return false;
        } else if (mNoOfSeats.length() < 2){
            mNumberOfSeatsEditText.setError("Number of seats can't be less than 4 digits.");
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
