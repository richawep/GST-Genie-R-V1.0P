package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1HSNSummaryBean implements Parcelable {

    private int sno;
    private String itemName;
    private String hsnCode;
    private double taxableVlue;
    private double value;
    private double igstRate;
    private double igstAmt;
    private double cgstRate;
    private double cgstAmt;
    private double sgstRate;
    private double sgstAmt;
    private double cessRate;
    private double cessAmt;
    private double discount;
    private String uom;
    private double quantity;
    private String supplyType;

    public GSTR1HSNSummaryBean() {
    }


    protected GSTR1HSNSummaryBean(Parcel in) {
        sno = in.readInt();
        itemName = in.readString();
        hsnCode = in.readString();
        taxableVlue = in.readDouble();
        value = in.readDouble();
        igstRate = in.readDouble();
        igstAmt = in.readDouble();
        cgstRate = in.readDouble();
        cgstAmt = in.readDouble();
        sgstRate = in.readDouble();
        sgstAmt = in.readDouble();
        cessRate = in.readDouble();
        cessAmt = in.readDouble();
        discount = in.readDouble();
        uom = in.readString();
        quantity = in.readDouble();
        supplyType = in.readString();
    }

    public static final Creator<GSTR1HSNSummaryBean> CREATOR = new Creator<GSTR1HSNSummaryBean>() {
        @Override
        public GSTR1HSNSummaryBean createFromParcel(Parcel in) {
            return new GSTR1HSNSummaryBean(in);
        }

        @Override
        public GSTR1HSNSummaryBean[] newArray(int size) {
            return new GSTR1HSNSummaryBean[size];
        }
    };

    public void setSno(int sno) {
        this.sno = sno;
    }

    public void setItemName(String ty) {
        this.itemName = ty;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public void setTaxableVlue(double taxableVlue) {
        this.taxableVlue = taxableVlue;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setIgstRate(double igstRate) {
        this.igstRate = igstRate;
    }

    public void setIgstAmt(double igstAmt) {
        this.igstAmt = igstAmt;
    }

    public void setCgstRate(double cgstRate) {
        this.cgstRate = cgstRate;
    }

    public void setCgstAmt(double cgstAmt) {
        this.cgstAmt = cgstAmt;
    }

    public void setSgstRate(double sgstRate) {
        this.sgstRate = sgstRate;
    }

    public void setSgstAmt(double sgstAmt) {
        this.sgstAmt = sgstAmt;
    }

    public void setCessRate(double cessRate) {
        this.cessRate = cessRate;
    }

    public void setCessAmt(double cessAmt) {
        this.cessAmt = cessAmt;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setSupplyType(String supplyType) {
        this.supplyType = supplyType;
    }

    public int getSno() {
        return sno;
    }

    public String getItemName() {
        return itemName;
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public double getTaxableVlue() {
        return taxableVlue;
    }

    public double getValue() {
        return value;
    }

    public double getIgstRate() {
        return igstRate;
    }

    public double getIgstAmt() {
        return igstAmt;
    }

    public double getCgstRate() {
        return cgstRate;
    }

    public double getCgstAmt() {
        return cgstAmt;
    }

    public double getSgstRate() {
        return sgstRate;
    }

    public double getSgstAmt() {
        return sgstAmt;
    }

    public double getCessRate() {
        return cessRate;
    }

    public double getCessAmt() {
        return cessAmt;
    }

    public double getDiscount() {
        return discount;
    }

    public String getUom() {
        return uom;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getSupplyType() {
        return supplyType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(sno);
        parcel.writeString(itemName);
        parcel.writeString(hsnCode);
        parcel.writeDouble(taxableVlue);
        parcel.writeDouble(value);
        parcel.writeDouble(igstRate);
        parcel.writeDouble(igstAmt);
        parcel.writeDouble(cgstRate);
        parcel.writeDouble(cgstAmt);
        parcel.writeDouble(sgstRate);
        parcel.writeDouble(sgstAmt);
        parcel.writeDouble(cessRate);
        parcel.writeDouble(cessAmt);
        parcel.writeDouble(discount);
        parcel.writeString(uom);
        parcel.writeDouble(quantity);
        parcel.writeString(supplyType);
    }
}
