package in.co.chicmic.samplereservationsystem.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.co.chicmic.samplereservationsystem.R;
import in.co.chicmic.samplereservationsystem.dataModels.BookingModel;

public class BookingHistoryRecyclerAdapter extends
        RecyclerView.Adapter<BookingHistoryRecyclerAdapter.UserViewHolder>  {
    private List<BookingModel> mBookingList;

    public BookingHistoryRecyclerAdapter(List<BookingModel> pBookingList) {
        this.mBookingList = pBookingList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.booking_history_recycler_item, viewGroup, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        BookingModel model = mBookingList.get(position);
        holder.mPNRTV.setText(String.valueOf(model.getPNR()));
        holder.mDateTV.setText(model.getBookingDate());
        holder.mTrainNameTV.setText(String.valueOf(model.getTrainId()));
        holder.mBookedSeats.setText(String.valueOf(model.getNoOfSeats()));
    }

    @Override
    public int getItemCount() {
        return mBookingList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView mPNRTV;
        TextView mDateTV;
        TextView mTrainNameTV;
        TextView mBookedSeats;
        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mPNRTV = itemView.findViewById(R.id.tv_pnr);
            mDateTV = itemView.findViewById(R.id.tv_date);
            mTrainNameTV = itemView.findViewById(R.id.tv_train_name);
            mBookedSeats = itemView.findViewById(R.id.tv_no_of_seats);
        }
    }
}
