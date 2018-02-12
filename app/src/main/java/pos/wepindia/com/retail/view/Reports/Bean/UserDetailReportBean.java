package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 25-01-2018.
 */

public class UserDetailReportBean implements Parcelable {

    private String date;
    private int billNumber;
    private int totalItems;
    private double discount;
    private double tax;
    private double billAmount;

    public UserDetailReportBean() {
    }

    protected UserDetailReportBean(Parcel in) {
        date = in.readString();
        billNumber = in.readInt();
        totalItems = in.readInt();
        discount = in.readDouble();
        tax = in.readDouble();
        billAmount = in.readDouble();
    }

    public static final Creator<UserDetailReportBean> CREATOR = new Creator<UserDetailReportBean>() {
        @Override
        public UserDetailReportBean createFromParcel(Parcel in) {
            return new UserDetailReportBean(in);
        }

        @Override
        public UserDetailReportBean[] newArray(int size) {
            return new UserDetailReportBean[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(int billNumber) {
        this.billNumber = billNumber;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
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
        parcel.writeString(date);
        parcel.writeInt(billNumber);
        parcel.writeInt(totalItems);
        parcel.writeDouble(discount);
        parcel.writeDouble(tax);
        parcel.writeDouble(billAmount);
    }
}
