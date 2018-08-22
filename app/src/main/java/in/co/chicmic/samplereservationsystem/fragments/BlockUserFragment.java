package in.co.chicmic.samplereservationsystem.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.adapters.BlockUserRecyclerAdapter;
import in.co.chicmic.samplereservationsystem.dataModels.User;
import in.co.chicmic.samplereservationsystem.database.DataBaseHelper;
import in.co.chicmic.samplereservationsystem.listeners.BlockUserFragmentListener;
import in.co.chicmic.samplereservationsystem.listeners.BlockUserRecyclerClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BlockUserFragment} interface
 * to handle interaction events.
 * Use the {@link BlockUserFragment} factory method to
 * create an instance of this fragment.
 */
public class BlockUserFragment extends Fragment implements BlockUserRecyclerClickListener{

    private BlockUserFragmentListener mListener;
    private List<User> mUsersList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private BlockUserRecyclerAdapter mAdapter;
    private DataBaseHelper mDataBaseHelper;

    public BlockUserFragment() {
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
        View view = inflater.inflate(R.layout.fragment_block_user, container, false);
        mRecyclerView = view.findViewById(R.id.block_user_recycler);
        setUpRecycler();
        return view;
    }

    private void setUpRecycler() {
        mAdapter = new BlockUserRecyclerAdapter(mUsersList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        mDataBaseHelper = new DataBaseHelper(getActivity());
        getDataFromSQLite();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BlockUserFragmentListener) {
            mListener = (BlockUserFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onBlockButtonClick(int position) {
        User user = mUsersList.get(position);
        mUsersList.remove(position);
        user.setIsApproved(2);
        mDataBaseHelper.updateUserStatus(user);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getActivity(), "Blocked", Toast.LENGTH_SHORT).show();
    }

    private void getDataFromSQLite() {
        // AsyncTask is used that SQLite operation not blocks the UI Thread.
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                mUsersList.clear();
                mUsersList.addAll(mDataBaseHelper.getAllUser());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mAdapter.notifyDataSetChanged();
            }
        }.execute();
    }
}
