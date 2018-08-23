package in.co.chicmic.samplereservationsystem.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.adapters.UpdateDeleteTrainsRecyclerAdapter;
import in.co.chicmic.samplereservationsystem.dataModels.TrainModel;
import in.co.chicmic.samplereservationsystem.database.AdminDBTasks;
import in.co.chicmic.samplereservationsystem.database.DataBaseHelper;
import in.co.chicmic.samplereservationsystem.listeners.UpdateDeleteRecyclerClickListener;
import in.co.chicmic.samplereservationsystem.listeners.UpdateDeleteTrainListener;

public class UpdateDeleteTrainsFragment extends Fragment implements UpdateDeleteRecyclerClickListener {
    private UpdateDeleteTrainListener mListener;
    private List<TrainModel> mTrainList = new ArrayList<>();
    private DataBaseHelper mDataBaseHelper;
    private UpdateDeleteTrainsRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private View mView;
    public UpdateDeleteTrainsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_update_delete_trains, container
                , false);

        initViews();
        setUpRecyclerView();
        return mView;
    }

    private void initViews() {
        mRecyclerView = mView.findViewById(R.id.rv_update_trains);
        mAdapter = new UpdateDeleteTrainsRecyclerAdapter(mTrainList, this);
    }

    private void setUpRecyclerView() {
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mDataBaseHelper = new DataBaseHelper(getActivity());
        getAllTrains();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UpdateDeleteTrainListener) {
            mListener = (UpdateDeleteTrainListener) context;
        } else {
            throw new RuntimeException(context.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllTrains();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void getAllTrains(){
        mTrainList.clear();
        mTrainList.addAll(AdminDBTasks.getAllTrains(mDataBaseHelper.getReadableDatabase()));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewClick(int adapterPosition) {
        mListener.startUpdateTrainDetailsActivity(mTrainList.get(adapterPosition));
    }
}
