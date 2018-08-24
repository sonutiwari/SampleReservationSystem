package in.co.chicmic.samplereservationsystem.dataModels;

public class BookingModel {
    private int mPNR;
    private String mDate;
    private int mTrainId;
    private int mUserId;
    private int mNoOfSeats;

    public long getPNR() {
        return mPNR;
    }

    public void setPNR(int mPNR) {
        this.mPNR = mPNR;
    }

    public String getBookingDate() {
        return mDate;
    }

    public void setBookingDate(String mDate) {
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
