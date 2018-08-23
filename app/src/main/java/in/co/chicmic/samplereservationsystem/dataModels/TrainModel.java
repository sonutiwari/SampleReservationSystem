package in.co.chicmic.samplereservationsystem.dataModels;

public class TrainModel {
    private int mTrainId;
    private String mTrainName;
    private int mNoOfSeats;
    private int mNoOfBookedSeats;

    public int getNoOfSeats() {
        return mNoOfSeats;
    }

    public void setNoOfSeats(int mNoOfSeats) {
        this.mNoOfSeats = mNoOfSeats;
    }

    public String getTrainName() {
        return mTrainName;
    }

    public void setTrainName(String mTrainName) {
        this.mTrainName = mTrainName;
    }

    public int getTrainId() {
        return mTrainId;
    }

    public void setTrainId(int mId) {
        this.mTrainId = mId;
    }

    public int getNoOfBookedSeats() {
        return mNoOfBookedSeats;
    }

    public void setNoOfBookedSeats(int mNoOfBookedSeats) {
        this.mNoOfBookedSeats = mNoOfBookedSeats;
    }
}
