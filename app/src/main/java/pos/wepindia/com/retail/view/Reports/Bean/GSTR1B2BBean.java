package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1B2BBean implements Parcelable {

    private String sno;
    private String gstin;
    private int invoiceNo;
    private String invoiceDate;
    private double value;
    private double gstRate;
    private double taxableValue;
    private double igstAmt;
    private double cgstAmt;
    private double sgstAmt;
    private double cessAmt;
    private String custStateCode;
    private double subTotal;
    private int revcharge;
    private String provisionalAssess;
    private String supplyType;
    private String hsnCode;
    private double quantity;
    private boolean isSub;


    public GSTR1B2BBean() {
    }

    protected GSTR1B2BBean(Parcel in) {
        sno = in.readString();
        gstin = in.readString();
        invoiceNo = in.readInt();
        invoiceDate = in.readString();
        value = in.readDouble();
        gstRate = in.readDouble();
        taxableValue = in.readDouble();
        igstAmt = in.readDouble();
        cgstAmt = in.readDouble();
        sgstAmt = in.readDouble();
        cessAmt = in.readDouble();
        custStateCode = in.readString();
        subTotal = in.readDouble();
        revcharge = in.readInt();
        provisionalAssess = in.readString();
        supplyType = in.readString();
        hsnCode = in.readString();
        quantity = in.readDouble();
        isSub = in.readByte() != 0;
    }

    public static final Creator<GSTR1B2BBean> CREATOR = new Creator<GSTR1B2BBean>() {
        @Override
        public GSTR1B2BBean createFromParcel(Parcel in) {
            return new GSTR1B2BBean(in);
        }

        @Override
        public GSTR1B2BBean[] newArray(int size) {
            return new GSTR1B2BBean[size];
        }
    };

    public boolean isSub() {
        return isSub;
    }

    public void setSub(boolean sub) {
        isSub = sub;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public int getRevcharge() {
        return revcharge;
    }

    public void setRevcharge(int revcharge) {
        this.revcharge = revcharge;
    }

    public String getProvisionalAssess() {
        return provisionalAssess;
    }

    public void setProvisionalAssess(String provisionalAssess) {
        this.provisionalAssess = provisionalAssess;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getGstRate() {
        return gstRate;
    }

    public void setGstRate(double gstRate) {
        this.gstRate = gstRate;
    }

    public double getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(double taxableValue) {
        this.taxableValue = taxableValue;
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

    public String getCustStateCode() {
        return custStateCode;
    }

    public void setCustStateCode(String custStateCode) {
        this.custStateCode = custStateCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(sno);
        parcel.writeString(gstin);
        parcel.writeInt(invoiceNo);
        parcel.writeString(invoiceDate);
        parcel.writeDouble(value);
        parcel.writeDouble(gstRate);
        parcel.writeDouble(taxableValue);
        parcel.writeDouble(igstAmt);
        parcel.writeDouble(cgstAmt);
        parcel.writeDouble(sgstAmt);
        parcel.writeDouble(cessAmt);
        parcel.writeString(custStateCode);
        parcel.writeDouble(subTotal);
        parcel.writeInt(revcharge);
        parcel.writeString(provisionalAssess);
        parcel.writeString(supplyType);
        parcel.writeString(hsnCode);
        parcel.writeDouble(quantity);
        parcel.writeByte((byte) (isSub ? 1 : 0));
    }
}
