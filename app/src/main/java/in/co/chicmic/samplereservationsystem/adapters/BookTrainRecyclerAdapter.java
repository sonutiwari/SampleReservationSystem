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
import in.co.chicmic.samplereservationsystem.listeners.BookTrainRecyclerClickListener;

public class BookTrainRecyclerAdapter extends
        RecyclerView.Adapter<BookTrainRecyclerAdapter.UserViewHolder>{

    private List<TrainModel> mListTrains;
    private BookTrainRecyclerClickListener mListener;

    public BookTrainRecyclerAdapter(List<TrainModel> pListTrains
            , BookTrainRecyclerClickListener pListener) {
        this.mListTrains = pListTrains;
        mListener = pListener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_booking_train_recycler, viewGroup, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder userViewHolder, int position) {
        TrainModel model = mListTrains.get(position);
        userViewHolder.mTrainNoTV.setText(String.valueOf(model.getTrainId()));
        userViewHolder.mTrainNameTV.setText(model.getTrainName());
        int noOfAvailableSeats = model.getNoOfSeats() - model.getNoOfBookedSeats();
        userViewHolder.mNoOfSeatsTV.setText(String.valueOf(noOfAvailableSeats));
        userViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onViewClick(userViewHolder.getAdapterPosition(), userViewHolder.mView);
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
        View mView;
        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mTrainNoTV = itemView.findViewById(R.id.tv_train_no);
            mTrainNameTV = itemView.findViewById(R.id.tv_train_name);
            mNoOfSeatsTV = itemView.findViewById(R.id.tv_train_no_of_seats);
        }
    }
}
