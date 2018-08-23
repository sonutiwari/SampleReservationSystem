package in.co.chicmic.samplereservationsystem.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.dataModels.TrainModel;
import in.co.chicmic.samplereservationsystem.listeners.UpdateDeleteRecyclerClickListener;

public class UpdateDeleteTrainsRecyclerAdapter extends
        RecyclerView.Adapter<UpdateDeleteTrainsRecyclerAdapter.UserViewHolder>{

    private List<TrainModel> mListTrains;
    private UpdateDeleteRecyclerClickListener mListener;
    public UpdateDeleteTrainsRecyclerAdapter(List<TrainModel> pListTrains
            , UpdateDeleteRecyclerClickListener pListener) {
        this.mListTrains = pListTrains;
        mListener = pListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.update_delete_recycler_layout, viewGroup, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int position) {
        userViewHolder.mTrainNoTV.setText(String.valueOf(mListTrains.get(position).getTrainId()));
        userViewHolder.mTrainNameTV.setText(mListTrains.get(position).getTrainName());
        userViewHolder.mNoOfSeatsTV.setText(String.valueOf(mListTrains.get(position).getNoOfSeats()));
        userViewHolder.mNoOfBookedSeatsTV.setText(String.valueOf(mListTrains.get(position).getNoOfBookedSeats()));
        userViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onViewClick(userViewHolder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListTrains.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView mTrainNoTV;
        TextView mTrainNameTV;
        TextView mNoOfSeatsTV;
        TextView mNoOfBookedSeatsTV;
        View mView;
        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mTrainNoTV = itemView.findViewById(R.id.tv_train_no);
            mTrainNameTV = itemView.findViewById(R.id.tv_train_name);
            mNoOfSeatsTV = itemView.findViewById(R.id.tv_train_no_of_seats);
            mNoOfBookedSeatsTV = itemView.findViewById(R.id.tv_train_no_of_booked_seats);
        }
    }
}
