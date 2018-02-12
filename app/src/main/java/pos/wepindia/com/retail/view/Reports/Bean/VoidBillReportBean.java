package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 06-02-2018.
 */

public class VoidBillReportBean implements Parcelable {

    String date;
    String billno;
    int totalItems;
    double discount, IGSTAmount, CGSTAmount, SGSTAmount, cessAmount, BillAmount;


    public VoidBillReportBean(String date, String billno, int totalItems, double discount, double IGSTAmount, double CGSTAmount, double SGSTAmount, double cessAmount, double billAmount) {
        this.date = date;
        this.billno = billno;
        this.totalItems = totalItems;
        this.discount = discount;
        this.IGSTAmount = IGSTAmount;
        this.CGSTAmount = CGSTAmount;
        this.SGSTAmount = SGSTAmount;
        this.cessAmount = cessAmount;
        BillAmount = billAmount;
    }
    public VoidBillReportBean() {
        this.date = "";
        this.billno = "0";
        this.totalItems = 0;
        this.discount = 0;
        this.IGSTAmount = 0;
        this.CGSTAmount = 0;
        this.SGSTAmount = 0;
        this.cessAmount = 0;
        BillAmount = 0;
    }

    protected VoidBillReportBean(Parcel in) {
        date = in.readString();
        billno = in.readString();
        totalItems = in.readInt();
        discount = in.readDouble();
        IGSTAmount = in.readDouble();
        CGSTAmount = in.readDouble();
        SGSTAmount = in.readDouble();
        cessAmount = in.readDouble();
        BillAmount = in.readDouble();
    }

    public static final Creator<VoidBillReportBean> CREATOR = new Creator<VoidBillReportBean>() {
        @Override
        public VoidBillReportBean createFromParcel(Parcel in) {
            return new VoidBillReportBean(in);
        }

        @Override
        public VoidBillReportBean[] newArray(int size) {
            return new VoidBillReportBean[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
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

    public double getCessAmount() {
        return cessAmount;
    }

    public void setCessAmount(double cessAmount) {
        this.cessAmount = cessAmount;
    }

    public double getBillAmount() {
        return BillAmount;
    }

    public void setBillAmount(double billAmount) {
        BillAmount = billAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeString(billno);
        parcel.writeInt(totalItems);
        parcel.writeDouble(discount);
        parcel.writeDouble(IGSTAmount);
        parcel.writeDouble(CGSTAmount);
        parcel.writeDouble(SGSTAmount);
        parcel.writeDouble(cessAmount);
        parcel.writeDouble(BillAmount);
    }
}
