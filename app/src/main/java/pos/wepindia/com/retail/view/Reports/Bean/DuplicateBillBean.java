package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 24-01-2018.
 */

public class DuplicateBillBean implements Parcelable {

    private String invoiceDate;
    private int InvoiceNumber;
    private int items;
    private double discount;
    private double igstAmount;
    private double cgstAmount;
    private double sgstAmount;
    private double cessAmount;
    private double billAmount;
    private int reprintCount;

    public DuplicateBillBean() {
    }

    protected DuplicateBillBean(Parcel in) {
        invoiceDate = in.readString();
        InvoiceNumber = in.readInt();
        items = in.readInt();
        discount = in.readDouble();
        igstAmount = in.readDouble();
        cgstAmount = in.readDouble();
        sgstAmount = in.readDouble();
        cessAmount = in.readDouble();
        billAmount = in.readDouble();
        reprintCount = in.readInt();
    }

    public static final Creator<DuplicateBillBean> CREATOR = new Creator<DuplicateBillBean>() {
        @Override
        public DuplicateBillBean createFromParcel(Parcel in) {
            return new DuplicateBillBean(in);
        }

        @Override
        public DuplicateBillBean[] newArray(int size) {
            return new DuplicateBillBean[size];
        }
    };

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public int getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
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

    public int getReprintCount() {
        return reprintCount;
    }

    public void setReprintCount(int reprintCount) {
        this.reprintCount = reprintCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(invoiceDate);
        parcel.writeInt(InvoiceNumber);
        parcel.writeInt(items);
        parcel.writeDouble(discount);
        parcel.writeDouble(igstAmount);
        parcel.writeDouble(cgstAmount);
        parcel.writeDouble(sgstAmount);
        parcel.writeDouble(cessAmount);
        parcel.writeDouble(billAmount);
        parcel.writeInt(reprintCount);
    }
}
