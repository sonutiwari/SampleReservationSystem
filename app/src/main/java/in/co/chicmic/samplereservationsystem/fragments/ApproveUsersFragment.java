package in.co.chicmic.samplereservationsystem.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.adapters.ApproveUsersRecyclerAdapter;
import in.co.chicmic.samplereservationsystem.dataModels.User;
import in.co.chicmic.samplereservationsystem.database.DataBaseHelper;
import in.co.chicmic.samplereservationsystem.listeners.ApproveUserRecyclerClickListener;
import in.co.chicmic.samplereservationsystem.listeners.ApproveUsersClickListener;
import in.co.chicmic.samplereservationsystem.utilities.AppConstants;

public class ApproveUsersFragment extends Fragment implements ApproveUserRecyclerClickListener{

    private ApproveUsersClickListener mListener;
    private List<User> mUsersList = new ArrayList<>();
    private ApproveUsersRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private DataBaseHelper mDataBaseHelper;
    private TextView mNoUserTextView;

    public ApproveUsersFragment() {
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
        View view = inflater.inflate(R.layout.fragment_approve_users, container, false);
        mNoUserTextView = view.findViewById(R.id.tv_no_user);
        mRecyclerView = view.findViewById(R.id.block_user_recycler);
        setUpRecycler();
        return view;
    }

    private void setUpRecycler() {
        mAdapter = new ApproveUsersRecyclerAdapter(mUsersList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mDataBaseHelper = new DataBaseHelper(getActivity());
        getDataFromSQLite();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ApproveUsersClickListener) {
            mListener = (ApproveUsersClickListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void getDataFromSQLite() {
        mUsersList.clear();
        mUsersList.addAll(mDataBaseHelper.getAllUnApprovedUsers());
        mAdapter.notifyDataSetChanged();
        if (mUsersList.size() == 0){
            mRecyclerView.setVisibility(View.GONE);
            mNoUserTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onApproveButtonClick(int position) {
        User user = mUsersList.get(position);
        mUsersList.remove(position);
        user.setIsApproved(AppConstants.sSTATUS_APPROVED);
        mDataBaseHelper.updateUserStatus(user);
        mAdapter.notifyDataSetChanged();
    }
}
