package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 24-01-2018.
 */

public class MonthWiseBean implements Parcelable {

    private int month;
    private int totalBills;
    private double discount;
    private double igstAmount;
    private double cgstAmount;
    private double sgstAmount;
    private double cessAmount;
    private double billAmount;

    public MonthWiseBean() {
    }

    protected MonthWiseBean(Parcel in) {
        month = in.readInt();
        totalBills = in.readInt();
        discount = in.readDouble();
        igstAmount = in.readDouble();
        cgstAmount = in.readDouble();
        sgstAmount = in.readDouble();
        cessAmount = in.readDouble();
        billAmount = in.readDouble();
    }

    public static final Creator<MonthWiseBean> CREATOR = new Creator<MonthWiseBean>() {
        @Override
        public MonthWiseBean createFromParcel(Parcel in) {
            return new MonthWiseBean(in);
        }

        @Override
        public MonthWiseBean[] newArray(int size) {
            return new MonthWiseBean[size];
        }
    };

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(int totalBills) {
        this.totalBills = totalBills;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getIgstAmount() {
        return igstAmount;
    }

    public void setIgstAmount(double igstAmount) {
        this.igstAmount = igstAmount;
    }

    public double getCgstAmount() {
        return cgstAmount;
    }

    public void setCgstAmount(double cgstAmount) {
        this.cgstAmount = cgstAmount;
    }

    public double getSgstAmount() {
        return sgstAmount;
    }

    public void setSgstAmount(double sgstAmount) {
        this.sgstAmount = sgstAmount;
    }

    public double getCessAmount() {
        return cessAmount;
    }

    public void setCessAmount(double cessAmount) {
        this.cessAmount = cessAmount;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(month);
        parcel.writeInt(totalBills);
        parcel.writeDouble(discount);
        parcel.writeDouble(igstAmount);
        parcel.writeDouble(cgstAmount);
        parcel.writeDouble(sgstAmount);
        parcel.writeDouble(cessAmount);
        parcel.writeDouble(billAmount);
    }
}
