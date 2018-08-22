package in.co.chicmic.samplereservationsystem.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import in.co.chicmic.samplereservationsystem.utilities.AppConstants;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BlockUserFragmentListener) {
            mListener = (BlockUserFragmentListener) context;
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
    public void onBlockButtonClick(int position) {
        User user = mUsersList.get(position);
        if (mDataBaseHelper.checkAdmin(user.getEmail())){
            Toast.makeText(getContext(), R.string.can_not_block_admin
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        if (user.getIsApproved() == AppConstants.sSTATUS_APPROVED){
            user.setIsApproved(AppConstants.sSTATUS_BLOCKED);
            mDataBaseHelper.updateUserStatus(user);
            Toast.makeText(getActivity(), R.string.blocked_string, Toast.LENGTH_SHORT).show();
        } else {
            user.setIsApproved(AppConstants.sSTATUS_APPROVED);
            mDataBaseHelper.updateUserStatus(user);
            Toast.makeText(getActivity(), R.string.unblocked_string, Toast.LENGTH_SHORT).show();
        }
        getDataFromSQLite();
    }

    @Override
    public void onDeleteButtonClick(final int pPosition) {
        if (mDataBaseHelper.checkAdmin(mUsersList.get(pPosition).getEmail())){
            Toast.makeText(getContext(), R.string.can_not_delete_admin
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(R.string.delete_user_dialog_title);
        dialog.setMessage(R.string.delete_dialog_message);
        dialog.setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDataBaseHelper.deleteUser(mUsersList.get(pPosition));
                getDataFromSQLite();
            }
        }).setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        dialog.show();
    }


    private void getDataFromSQLite() {
        mUsersList.clear();
        mUsersList.addAll(mDataBaseHelper.getAllUser());
        mAdapter.notifyDataSetChanged();
    }
}
