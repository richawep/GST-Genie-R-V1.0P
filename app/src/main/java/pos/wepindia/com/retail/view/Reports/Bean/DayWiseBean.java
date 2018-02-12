package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 23-01-2018.
 */

public class DayWiseBean implements Parcelable {

    private String invoiceDate;
    private int totalBills;
    private double discount;
    private double IGSTAmount;
    private double CGSTAmount;
    private double SGSTAmount;
    private double CESSAmount;
    private double billAmount;

    public DayWiseBean() {
    }

    protected DayWiseBean(Parcel in) {
        invoiceDate = in.readString();
        totalBills = in.readInt();
        discount = in.readDouble();
        IGSTAmount = in.readDouble();
        CGSTAmount = in.readDouble();
        SGSTAmount = in.readDouble();
        CESSAmount = in.readDouble();
        billAmount = in.readDouble();
    }

    public static final Creator<DayWiseBean> CREATOR = new Creator<DayWiseBean>() {
        @Override
        public DayWiseBean createFromParcel(Parcel in) {
            return new DayWiseBean(in);
        }

        @Override
        public DayWiseBean[] newArray(int size) {
            return new DayWiseBean[size];
        }
    };

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
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

    public double getIGSTAmount() {
        return IGSTAmount;
    }

    public void setIGSTAmount(double IGSTAmount) {
        this.IGSTAmount = IGSTAmount;
    }

    public double getCGSTAmount() {
        return CGSTAmount;
    }

    public void setCGSTAmount(double CGSTAmount) {
        this.CGSTAmount = CGSTAmount;
    }

    public double getSGSTAmount() {
        return SGSTAmount;
    }

    public void setSGSTAmount(double SGSTAmount) {
        this.SGSTAmount = SGSTAmount;
    }

    public double getCESSAmount() {
        return CESSAmount;
    }

    public void setCESSAmount(double CESSAmount) {
        this.CESSAmount = CESSAmount;
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
        parcel.writeString(invoiceDate);
        parcel.writeInt(totalBills);
        parcel.writeDouble(discount);
        parcel.writeDouble(IGSTAmount);
        parcel.writeDouble(CGSTAmount);
        parcel.writeDouble(SGSTAmount);
        parcel.writeDouble(CESSAmount);
        parcel.writeDouble(billAmount);
    }
}
