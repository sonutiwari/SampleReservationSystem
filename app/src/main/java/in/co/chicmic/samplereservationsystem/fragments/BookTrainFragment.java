package in.co.chicmic.samplereservationsystem.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.adapters.BookTrainRecyclerAdapter;
import in.co.chicmic.samplereservationsystem.dataModels.BookingModel;
import in.co.chicmic.samplereservationsystem.dataModels.TrainModel;
import in.co.chicmic.samplereservationsystem.dataModels.User;
import in.co.chicmic.samplereservationsystem.database.AdminDBTasks;
import in.co.chicmic.samplereservationsystem.database.BookingTasks;
import in.co.chicmic.samplereservationsystem.database.DataBaseHelper;
import in.co.chicmic.samplereservationsystem.listeners.BookTrainFragmentListener;
import in.co.chicmic.samplereservationsystem.listeners.BookTrainRecyclerClickListener;
import in.co.chicmic.samplereservationsystem.sessionManager.SessionManager;
import in.co.chicmic.samplereservationsystem.utilities.AppConstants;

public class BookTrainFragment extends Fragment implements
        View.OnClickListener
        , DatePickerDialog.OnDateSetListener
        , BookTrainRecyclerClickListener
{
    private BookTrainFragmentListener mListener;
    private TextView mDateTextView;
    private TextInputEditText mNoOfSeatsEditText;
    private RecyclerView mRecyclerView;
    private LinearLayout mHeader;
    private View mView;
    private Button mBookButton;
    private BookTrainRecyclerAdapter mAdapter;
    private List<TrainModel> mTrainAvailableList = new ArrayList<>();
    private DataBaseHelper mDataBaseHelper;
    private View mPrevView;
    private int mPosition = -1;
    private Context mContext;
    private String mDate;
    private int mNoOfSeats;
    private SessionManager mSession;
    private List<BookingModel> mBookedSeatsInTrain;
    SQLiteDatabase mReadableDatabase;

    public BookTrainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_book_train, container, false);
        initViews();
        setUpListeners();
        return mView;
    }

    private void initViews() {
      mDateTextView = mView.findViewById(R.id.tv_date_picker);
      mNoOfSeatsEditText = mView.findViewById(R.id.tie_enter_no_of_seats);
      mRecyclerView = mView.findViewById(R.id.show_available_trains_rv);
      mHeader = mView.findViewById(R.id.header_ll);
      mBookButton = mView.findViewById(R.id.btn_book);
      mDataBaseHelper = new DataBaseHelper(getActivity());
      mReadableDatabase = mDataBaseHelper.getReadableDatabase();
      mContext = getContext();
      mSession = new SessionManager(mContext.getApplicationContext());
    }

    private void setUpListeners() {
        mDateTextView.setOnClickListener(this);
        mBookButton.setOnClickListener(this);
    }

    private void setUpRecyclerView() {
        mAdapter = new BookTrainRecyclerAdapter(mTrainAvailableList, this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        getAllTrains();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_date_picker:
                showDatePickerDialog();
                break;
            case R.id.btn_book:
                validateInputAndBook();
                break;
        }
    }

    private void validateInputAndBook() {
        String input = "";
        if (mNoOfSeatsEditText.getText() != null) {
            input = mNoOfSeatsEditText.getText().toString().trim();
        }
        if (input.isEmpty()){
            mNoOfSeatsEditText.setError(getString(R.string.please_enter_number_of_seats));
            return;
        } else {
            mNoOfSeats = Integer.parseInt(input);
            if (mNoOfSeats <= 0){
                mNoOfSeatsEditText.setError(getString(R.string.please_enter_valid_number_of_seats));
            }
        }

        if (mPrevView == null){
            Toast.makeText(getContext(), R.string.please_select_train, Toast.LENGTH_SHORT).show();
            return;
        }
        updateTrainTable(mNoOfSeats);
        clearUI();
    }

    private void clearUI() {
        mDateTextView.setText(R.string.select_date);
        mTrainAvailableList.clear();
        mAdapter.notifyDataSetChanged();
        mNoOfSeatsEditText.setText(null);
        mDate = null;
        mNoOfSeats = 0;
    }

    private void updateTrainTable(int noOfSeats) {
        TrainModel model = mTrainAvailableList.get(mPosition);
        int finallyBookedSeats = model.getNoOfBookedSeats() + noOfSeats;
        if (finallyBookedSeats <= model.getNoOfSeats()){
            model.setNoOfBookedSeats(finallyBookedSeats);
            AdminDBTasks.upDateTrainDetails(model, mDataBaseHelper.getWritableDatabase());
            updateBookingTable();
        } else {
            Toast.makeText(getContext(), R.string.not_enough_seats, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateBookingTable() {
        BookingModel model = new BookingModel();
        model.setBookingDate(mDate);
        model.setUserId(getUserIdFromDB());
        model.setTrainId(mTrainAvailableList.get(mPosition).getTrainId());
        model.setNoOfSeats(mNoOfSeats);
        BookingTasks.addBookingData(model, mDataBaseHelper.getWritableDatabase());
    }

    private void showDatePickerDialog() {
        // Create the DatePickerDialog instance
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePicker = new DatePickerDialog(mContext, this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(false);
        datePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        datePicker.setTitle(getString(R.string.select_journey_date));
        datePicker.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BookTrainFragmentListener) {
            mListener = (BookTrainFragmentListener) context;
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
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        mDate = String.format(Locale.ENGLISH
                , AppConstants.sDATE_FORMAT, dayOfMonth, monthOfYear + 1, year);
        mDateTextView.setText(mDate);
        mHeader.setVisibility(View.VISIBLE);
        setUpRecyclerView();
    }

    @Override
    public void onViewClick(int adapterPosition, View pView) {
        Toast.makeText(getContext(), R.string.clicked_toast, Toast.LENGTH_SHORT).show();
        if (mPrevView != null){
            mPrevView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        pView.setBackgroundColor(mContext.getResources().getColor(R.color.colorAccent));
        mPrevView = pView;
        mPosition = adapterPosition;
    }

    private void getAllTrains(){
        mTrainAvailableList.clear();
        mBookedSeatsInTrain = BookingTasks.getAllTrainsOnThisDate(mDate, mReadableDatabase);
        List<TrainModel> pTrainAvailableList  =
                getAvailableSeatsForSelectedDate(AdminDBTasks.getAllTrains(mReadableDatabase));
        mTrainAvailableList.addAll(pTrainAvailableList);
        mAdapter.notifyDataSetChanged();
    }

    private List<TrainModel> getAvailableSeatsForSelectedDate(List<TrainModel> pTrainAvailableList) {
        for (int index = 0; index < pTrainAvailableList.size(); index++) {
            pTrainAvailableList
                    .get(index)
                    .setNoOfBookedSeats(0);
            for (int index2 = 0; index2 < mBookedSeatsInTrain.size(); index2++) {
                if (mBookedSeatsInTrain.get(index2).getTrainId() == pTrainAvailableList.get(index).getTrainId()){
                    pTrainAvailableList
                            .get(index)
                            .setNoOfBookedSeats(pTrainAvailableList
                                    .get(index).getNoOfBookedSeats()
                                    + mBookedSeatsInTrain.get(index2).getNoOfSeats());
                }
            }
        }
        return pTrainAvailableList;
    }

    public int getUserIdFromDB() {
        HashMap<String, String> userDetails = mSession.getUserDetails();
        String email = userDetails.get(SessionManager.KEY_EMAIL);
        User user = mDataBaseHelper.getUserDetails(email);
        return user.getId();
    }

    @Override
    public void onStop() {
        super.onStop();
        mReadableDatabase.close();
        mDataBaseHelper.close();
    }
}
