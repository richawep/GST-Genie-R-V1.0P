package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 01-02-2018.
 */

public class SalesmanWiseReportBean implements Parcelable {

    private String salesManId;
    private String salesManName;
    private int totalBills;
    private int month;
    private double taxableValue;
    private double billAmount;

    public SalesmanWiseReportBean() {
    }

    protected SalesmanWiseReportBean(Parcel in) {
        salesManId = in.readString();
        salesManName = in.readString();
        totalBills = in.readInt();
        month = in.readInt();
        taxableValue = in.readDouble();
        billAmount = in.readDouble();
    }

    public static final Creator<SalesmanWiseReportBean> CREATOR = new Creator<SalesmanWiseReportBean>() {
        @Override
        public SalesmanWiseReportBean createFromParcel(Parcel in) {
            return new SalesmanWiseReportBean(in);
        }

        @Override
        public SalesmanWiseReportBean[] newArray(int size) {
            return new SalesmanWiseReportBean[size];
        }
    };

    public String getSalesManId() {
        return salesManId;
    }

    public void setSalesManId(String salesManId) {
        this.salesManId = salesManId;
    }

    public String getSalesManName() {
        return salesManName;
    }

    public void setSalesManName(String salesManName) {
        this.salesManName = salesManName;
    }

    public int getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(int totalBills) {
        this.totalBills = totalBills;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(double taxableValue) {
        this.taxableValue = taxableValue;
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
        parcel.writeString(salesManId);
        parcel.writeString(salesManName);
        parcel.writeInt(totalBills);
        parcel.writeInt(month);
        parcel.writeDouble(taxableValue);
        parcel.writeDouble(billAmount);
    }
}
