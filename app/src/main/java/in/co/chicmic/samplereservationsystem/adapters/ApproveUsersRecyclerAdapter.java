package in.co.chicmic.samplereservationsystem.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.dataModels.User;
import in.co.chicmic.samplereservationsystem.listeners.ApproveUserRecyclerClickListener;

public class ApproveUsersRecyclerAdapter extends
        RecyclerView.Adapter<ApproveUsersRecyclerAdapter.UserViewHolder>{
    private List<User> mListUsers;
    private ApproveUserRecyclerClickListener mListener;
    public ApproveUsersRecyclerAdapter(List<User> pListUsers
            , ApproveUserRecyclerClickListener pListener) {
        this.mListUsers = pListUsers;
        mListener = pListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.approve_user_recycler_item, viewGroup, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int position) {
        userViewHolder.mUserNameTV.setText(mListUsers.get(position).getName());
        userViewHolder.mApproveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onApproveButtonClick(userViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListUsers.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView mUserNameTV;
        Button mApproveButton;
        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mUserNameTV = itemView.findViewById(R.id.user_name);
            mApproveButton = itemView.findViewById(R.id.approve_button);
        }
    }
}
