package in.co.chicmic.samplereservationsystem.fragments;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.adapters.BookingHistoryRecyclerAdapter;
import in.co.chicmic.samplereservationsystem.dataModels.BookingModel;
import in.co.chicmic.samplereservationsystem.dataModels.User;
import in.co.chicmic.samplereservationsystem.database.BookingTasks;
import in.co.chicmic.samplereservationsystem.database.DataBaseHelper;
import in.co.chicmic.samplereservationsystem.listeners.UserBookingHistoryClickListener;
import in.co.chicmic.samplereservationsystem.sessionManager.SessionManager;

public class UserBookingHistoryFragment extends Fragment {

    private UserBookingHistoryClickListener mListener;
    private RecyclerView mRecyclerView;
    private TextView mEmptyTextView;
    private DataBaseHelper mDBHelper;
    private BookingHistoryRecyclerAdapter mAdapter;
    private View mView;
    private List<BookingModel> mBookingList = new ArrayList<>();

    public UserBookingHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_booking_history, container, false);
        initObjects();
        setUpRecycler();
        return mView;
    }

    private void initObjects() {
        mRecyclerView = mView.findViewById(R.id.booking_history_recycler);
        mEmptyTextView = mView.findViewById(R.id.tv_empty_layout);
        mDBHelper = new DataBaseHelper(getActivity());
        mAdapter = new BookingHistoryRecyclerAdapter(mBookingList);
    }

    private void setUpRecycler() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        getAllBookingHistory();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UserBookingHistoryClickListener) {
            mListener = (UserBookingHistoryClickListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mDBHelper.close();
    }
    private void getAllBookingHistory() {
        int id = getUserId();
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        mBookingList.addAll(BookingTasks.getAllTrains(id, db));
        mAdapter.notifyDataSetChanged();
    }

    private int getUserId() {
        SessionManager sm = new SessionManager(getContext().getApplicationContext());
        HashMap<String, String> map = sm.getUserDetails();
        String email = map.get(SessionManager.KEY_EMAIL);
        User user = mDBHelper.getUserDetails(email);
        return user.getId();
    }
}
