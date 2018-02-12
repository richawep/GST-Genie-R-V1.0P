package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 25-01-2018.
 */

public class SupplierWiseReportBean implements Parcelable {

    private int supplierCode;
    private String name;
    private int totalBills;
    private double billAmount;

    public SupplierWiseReportBean() {
    }

    protected SupplierWiseReportBean(Parcel in) {
        supplierCode = in.readInt();
        name = in.readString();
        totalBills = in.readInt();
        billAmount = in.readDouble();
    }

    public static final Creator<SupplierWiseReportBean> CREATOR = new Creator<SupplierWiseReportBean>() {
        @Override
        public SupplierWiseReportBean createFromParcel(Parcel in) {
            return new SupplierWiseReportBean(in);
        }

        @Override
        public SupplierWiseReportBean[] newArray(int size) {
            return new SupplierWiseReportBean[size];
        }
    };

    public int getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(int supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(int totalBills) {
        this.totalBills = totalBills;
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
        parcel.writeInt(supplierCode);
        parcel.writeString(name);
        parcel.writeInt(totalBills);
        parcel.writeDouble(billAmount);
    }
}
