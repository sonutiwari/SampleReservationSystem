package in.co.chicmic.samplereservationsystem.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.dataModels.User;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.UserViewHolder> {

    private List<User> mListUsers;

    public UsersRecyclerAdapter(List<User> pListUsers) {
        this.mListUsers = pListUsers;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_recycler, parent, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.mTextViewName.setText(mListUsers.get(position).getName());
        holder.mTextViewEmail.setText(mListUsers.get(position).getEmail());
        holder.mTextViewPassword.setText(mListUsers.get(position).getPassword());
        holder.mTextViewSecurityHint.setText(mListUsers.get(position).getSecurityHint());
        holder.mTextViewAdmin.setText(mListUsers.get(position).getIsAdmin() ? "Yes" : "No");
        holder.mProfileImageView.setImageURI(Uri.parse(mListUsers.get(position).getProfileImageURI()));
    }

    @Override
    public int getItemCount() {
        Log.v(UsersRecyclerAdapter.class.getSimpleName(),""+ mListUsers.size());
        return mListUsers.size();
    }


    /**
     * ViewHolder class
     */
    class UserViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView mTextViewName;
        AppCompatTextView mTextViewEmail;
        AppCompatTextView mTextViewPassword;
        AppCompatTextView mTextViewSecurityHint;
        AppCompatTextView mTextViewAdmin;
        CircleImageView mProfileImageView;

        UserViewHolder(View view) {
            super(view);
            mTextViewName = view.findViewById(R.id.textViewName);
            mTextViewEmail = view.findViewById(R.id.textViewEmail);
            mTextViewPassword = view.findViewById(R.id.textViewPassword);
            mTextViewSecurityHint = view.findViewById(R.id.tv_security_hint);
            mTextViewAdmin = view.findViewById(R.id.tv_admin_rights);
            mProfileImageView = view.findViewById(R.id.profile_image);
        }
    }


}