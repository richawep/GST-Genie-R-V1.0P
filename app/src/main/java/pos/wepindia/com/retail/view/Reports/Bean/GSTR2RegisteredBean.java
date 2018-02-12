package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR2RegisteredBean implements Parcelable {

    private int sno;
    private String supplierGstin;
    private int invoiceNo;
    private String invoiceDate;
    private String supplyType;
    private String hsnCode;
    private double taxableVlue;
    private double value;
    private double igstAmt;
    private double cgstAmt;
    private double sgstAmt;
    private double cessAmt;
    private double amount;

    public GSTR2RegisteredBean() {
    }

    protected GSTR2RegisteredBean(Parcel in) {
        sno = in.readInt();
        supplierGstin = in.readString();
        invoiceNo = in.readInt();
        invoiceDate = in.readString();
        supplyType = in.readString();
        hsnCode = in.readString();
        taxableVlue = in.readDouble();
        value = in.readDouble();
        igstAmt = in.readDouble();
        cgstAmt = in.readDouble();
        sgstAmt = in.readDouble();
        cessAmt = in.readDouble();
        amount = in.readDouble();
    }

    public static final Creator<GSTR2RegisteredBean> CREATOR = new Creator<GSTR2RegisteredBean>() {
        @Override
        public GSTR2RegisteredBean createFromParcel(Parcel in) {
            return new GSTR2RegisteredBean(in);
        }

        @Override
        public GSTR2RegisteredBean[] newArray(int size) {
            return new GSTR2RegisteredBean[size];
        }
    };

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getSupplierGstin() {
        return supplierGstin;
    }

    public void setSupplierGstin(String supplierGstin) {
        this.supplierGstin = supplierGstin;
    }

    public int getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(int invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getSupplyType() {
        return supplyType;
    }

    public void setSupplyType(String supplyType) {
        this.supplyType = supplyType;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public double getTaxableVlue() {
        return taxableVlue;
    }

    public void setTaxableVlue(double taxableVlue) {
        this.taxableVlue = taxableVlue;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getIgstAmt() {
        return igstAmt;
    }

    public void setIgstAmt(double igstAmt) {
        this.igstAmt = igstAmt;
    }

    public double getCgstAmt() {
        return cgstAmt;
    }

    public void setCgstAmt(double cgstAmt) {
        this.cgstAmt = cgstAmt;
    }

    public double getSgstAmt() {
        return sgstAmt;
    }

    public void setSgstAmt(double sgstAmt) {
        this.sgstAmt = sgstAmt;
    }

    public double getCessAmt() {
        return cessAmt;
    }

    public void setCessAmt(double cessAmt) {
        this.cessAmt = cessAmt;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(sno);
        parcel.writeString(supplierGstin);
        parcel.writeInt(invoiceNo);
        parcel.writeString(invoiceDate);
        parcel.writeString(supplyType);
        parcel.writeString(hsnCode);
        parcel.writeDouble(taxableVlue);
        parcel.writeDouble(value);
        parcel.writeDouble(igstAmt);
        parcel.writeDouble(cgstAmt);
        parcel.writeDouble(sgstAmt);
        parcel.writeDouble(cessAmt);
        parcel.writeDouble(amount);
    }
}
