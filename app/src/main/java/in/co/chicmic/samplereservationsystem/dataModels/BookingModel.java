package in.co.chicmic.samplereservationsystem.dataModels;

public class BookingModel {
    long mPNR;
    long mDate;
    int mTrainId;
    int mUserId;
    int mNoOfSeats;

    public long getPNR() {
        return mPNR;
    }

    public void setPNR(long mPNR) {
        this.mPNR = mPNR;
    }

    public long getBookingDate() {
        return mDate;
    }

    public void setBookingDate(long mDate) {
        this.mDate = mDate;
    }

    public int getTrainId() {
        return mTrainId;
    }

    public void setTrainId(int mTrainId) {
        this.mTrainId = mTrainId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int mUserId) {
        this.mUserId = mUserId;
    }

    public int getNoOfSeats() {
        return mNoOfSeats;
    }

    public void setNoOfSeats(int mNoOfSeats) {
        this.mNoOfSeats = mNoOfSeats;
    }
}
