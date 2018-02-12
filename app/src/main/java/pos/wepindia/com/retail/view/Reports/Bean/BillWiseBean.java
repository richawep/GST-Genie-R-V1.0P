package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 23-01-2018.
 */

public class BillWiseBean implements Parcelable {

    private String invoiceDate;
    private int invoiceNumber;
    private int totalItems;
    private double totalDiscountAmount;
    private double totalTax;
    private double billAmount;

    public BillWiseBean() {
    }

    public BillWiseBean(String invoiceDate, int invoiceNumber, int totalItems, double totalDiscountAmount, double totalTax, double billAmount) {
        this.invoiceDate = invoiceDate;
        this.invoiceNumber = invoiceNumber;
        this.totalItems = totalItems;
        this.totalDiscountAmount = totalDiscountAmount;
        this.totalTax = totalTax;
        this.billAmount = billAmount;
    }

    protected BillWiseBean(Parcel in) {
        invoiceDate = in.readString();
        invoiceNumber = in.readInt();
        totalItems = in.readInt();
        totalDiscountAmount = in.readDouble();
        totalTax = in.readDouble();
        billAmount = in.readDouble();
    }

    public static final Creator<BillWiseBean> CREATOR = new Creator<BillWiseBean>() {
        @Override
        public BillWiseBean createFromParcel(Parcel in) {
            return new BillWiseBean(in);
        }

        @Override
        public BillWiseBean[] newArray(int size) {
            return new BillWiseBean[size];
        }
    };

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public double getTotalDiscountAmount() {
        return totalDiscountAmount;
    }

    public void setTotalDiscountAmount(double totalDiscountAmount) {
        this.totalDiscountAmount = totalDiscountAmount;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(double totalTax) {
        this.totalTax = totalTax;
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
        parcel.writeInt(invoiceNumber);
        parcel.writeInt(totalItems);
        parcel.writeDouble(totalDiscountAmount);
        parcel.writeDouble(totalTax);
        parcel.writeDouble(billAmount);
    }
}
