package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1B2CLBean implements Parcelable {


    private String sno;
    private String recipientStateCode;
    private String recipientName;
    private int invoiceNo;
    private String invoiceDate;
    private double value;
    private double igstRate;
    private double taxableValue;
    private double igstAmt;
    private double cessAmt;
    private double subTotal;
    private int revcharge;
    private String provisionalAssess;
    private String supplyType;
    private String hsnCode;
    private double quantity;
    private boolean isSub;

    public GSTR1B2CLBean() {
    }

    protected GSTR1B2CLBean(Parcel in) {
        sno = in.readString();
        recipientStateCode = in.readString();
        recipientName = in.readString();
        invoiceNo = in.readInt();
        invoiceDate = in.readString();
        value = in.readDouble();
        igstRate = in.readDouble();
        taxableValue = in.readDouble();
        igstAmt = in.readDouble();
        cessAmt = in.readDouble();
        subTotal = in.readDouble();
        revcharge = in.readInt();
        provisionalAssess = in.readString();
        supplyType = in.readString();
        hsnCode = in.readString();
        quantity = in.readDouble();
        isSub = in.readByte() != 0;
    }

    public static final Creator<GSTR1B2CLBean> CREATOR = new Creator<GSTR1B2CLBean>() {
        @Override
        public GSTR1B2CLBean createFromParcel(Parcel in) {
            return new GSTR1B2CLBean(in);
        }

        @Override
        public GSTR1B2CLBean[] newArray(int size) {
            return new GSTR1B2CLBean[size];
        }
    };

    public boolean isSub() {
        return isSub;
    }

    public void setSub(boolean sub) {
        isSub = sub;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getRecipientStateCode() {
        return recipientStateCode;
    }

    public void setRecipientStateCode(String recipientStateCode) {
        this.recipientStateCode = recipientStateCode;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
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

    public double getIgstRate() {
        return igstRate;
    }

    public void setIgstRate(double igstRate) {
        this.igstRate = igstRate;
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

    public double getCessAmt() {
        return cessAmt;
    }

    public void setCessAmt(double cessAmt) {
        this.cessAmt = cessAmt;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sno);
        parcel.writeString(recipientStateCode);
        parcel.writeString(recipientName);
        parcel.writeInt(invoiceNo);
        parcel.writeString(invoiceDate);
        parcel.writeDouble(value);
        parcel.writeDouble(igstRate);
        parcel.writeDouble(taxableValue);
        parcel.writeDouble(igstAmt);
        parcel.writeDouble(cessAmt);
        parcel.writeDouble(subTotal);
        parcel.writeInt(revcharge);
        parcel.writeString(provisionalAssess);
        parcel.writeString(supplyType);
        parcel.writeString(hsnCode);
        parcel.writeDouble(quantity);
        parcel.writeByte((byte) (isSub ? 1 : 0));
    }
}
