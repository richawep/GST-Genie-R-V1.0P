package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1B2CSBean implements Parcelable {

    private int sno;
    private double gstRare;
    private String SupplyType;
    private String HSNCode;
    private String PlaceOfSupply, Description,stateCode;
    private double TaxableValue , SubTotal ;
    private double IGSTRate, IGSTAmt, CGSTRate, CGSTAmt, SGSTRate, SGSTAmt;
    private double GSTRate,cessAmt;
    private String ProAss;
    private double NilRatedValue, ExemptedValue, NonGSTValue, CompoundingValue,unregisteredValue;
    private String cessRate ="0";
    private String Orderno="0";
    private String OrderDate="0";
    private String etin="";
    private String etype="";
    private String sply_ty=""; // Inter/Intra

    public GSTR1B2CSBean() {
    }

    protected GSTR1B2CSBean(Parcel in) {
        sno = in.readInt();
        gstRare = in.readDouble();
        SupplyType = in.readString();
        HSNCode = in.readString();
        PlaceOfSupply = in.readString();
        Description = in.readString();
        stateCode = in.readString();
        TaxableValue = in.readDouble();
        SubTotal = in.readDouble();
        IGSTRate = in.readDouble();
        IGSTAmt = in.readDouble();
        CGSTRate = in.readDouble();
        CGSTAmt = in.readDouble();
        SGSTRate = in.readDouble();
        SGSTAmt = in.readDouble();
        GSTRate = in.readDouble();
        cessAmt = in.readDouble();
        ProAss = in.readString();
        NilRatedValue = in.readDouble();
        ExemptedValue = in.readDouble();
        NonGSTValue = in.readDouble();
        CompoundingValue = in.readDouble();
        unregisteredValue = in.readDouble();
        cessRate = in.readString();
        Orderno = in.readString();
        OrderDate = in.readString();
        etin = in.readString();
        etype = in.readString();
        sply_ty = in.readString();
    }

    public static final Creator<GSTR1B2CSBean> CREATOR = new Creator<GSTR1B2CSBean>() {
        @Override
        public GSTR1B2CSBean createFromParcel(Parcel in) {
            return new GSTR1B2CSBean(in);
        }

        @Override
        public GSTR1B2CSBean[] newArray(int size) {
            return new GSTR1B2CSBean[size];
        }
    };

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public double getGstRare() {
        return gstRare;
    }

    public void setGstRare(double gstRare) {
        this.gstRare = gstRare;
    }

    public String getSupplyType() {
        return SupplyType;
    }

    public void setSupplyType(String supplyType) {
        SupplyType = supplyType;
    }

    public String getHSNCode() {
        return HSNCode;
    }

    public void setHSNCode(String HSNCode) {
        this.HSNCode = HSNCode;
    }

    public String getPlaceOfSupply() {
        return PlaceOfSupply;
    }

    public void setPlaceOfSupply(String placeOfSupply) {
        PlaceOfSupply = placeOfSupply;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public double getTaxableValue() {
        return TaxableValue;
    }

    public void setTaxableValue(double taxableValue) {
        TaxableValue = taxableValue;
    }

    public double getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(double subTotal) {
        SubTotal = subTotal;
    }

    public double getIGSTRate() {
        return IGSTRate;
    }

    public void setIGSTRate(double IGSTRate) {
        this.IGSTRate = IGSTRate;
    }

    public double getIGSTAmt() {
        return IGSTAmt;
    }

    public void setIGSTAmt(double IGSTAmt) {
        this.IGSTAmt = IGSTAmt;
    }

    public double getCGSTRate() {
        return CGSTRate;
    }

    public void setCGSTRate(double CGSTRate) {
        this.CGSTRate = CGSTRate;
    }

    public double getCGSTAmt() {
        return CGSTAmt;
    }

    public void setCGSTAmt(double CGSTAmt) {
        this.CGSTAmt = CGSTAmt;
    }

    public double getSGSTRate() {
        return SGSTRate;
    }

    public void setSGSTRate(double SGSTRate) {
        this.SGSTRate = SGSTRate;
    }

    public double getSGSTAmt() {
        return SGSTAmt;
    }

    public void setSGSTAmt(double SGSTAmt) {
        this.SGSTAmt = SGSTAmt;
    }

    public double getGSTRate() {
        return GSTRate;
    }

    public void setGSTRate(double GSTRate) {
        this.GSTRate = GSTRate;
    }

    public double getCessAmt() {
        return cessAmt;
    }

    public void setCessAmt(double cessAmt) {
        this.cessAmt = cessAmt;
    }

    public String getProAss() {
        return ProAss;
    }

    public void setProAss(String proAss) {
        ProAss = proAss;
    }

    public double getNilRatedValue() {
        return NilRatedValue;
    }

    public void setNilRatedValue(double nilRatedValue) {
        NilRatedValue = nilRatedValue;
    }

    public double getExemptedValue() {
        return ExemptedValue;
    }

    public void setExemptedValue(double exemptedValue) {
        ExemptedValue = exemptedValue;
    }

    public double getNonGSTValue() {
        return NonGSTValue;
    }

    public void setNonGSTValue(double nonGSTValue) {
        NonGSTValue = nonGSTValue;
    }

    public double getCompoundingValue() {
        return CompoundingValue;
    }

    public void setCompoundingValue(double compoundingValue) {
        CompoundingValue = compoundingValue;
    }

    public double getUnregisteredValue() {
        return unregisteredValue;
    }

    public void setUnregisteredValue(double unregisteredValue) {
        this.unregisteredValue = unregisteredValue;
    }

    public String getCessRate() {
        return cessRate;
    }

    public void setCessRate(String cessRate) {
        this.cessRate = cessRate;
    }

    public String getOrderno() {
        return Orderno;
    }

    public void setOrderno(String orderno) {
        Orderno = orderno;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getEtin() {
        return etin;
    }

    public void setEtin(String etin) {
        this.etin = etin;
    }

    public String getEtype() {
        return etype;
    }

    public void setEtype(String etype) {
        this.etype = etype;
    }

    public String getSply_ty() {
        return sply_ty;
    }

    public void setSply_ty(String sply_ty) {
        this.sply_ty = sply_ty;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(sno);
        parcel.writeDouble(gstRare);
        parcel.writeString(SupplyType);
        parcel.writeString(HSNCode);
        parcel.writeString(PlaceOfSupply);
        parcel.writeString(Description);
        parcel.writeString(stateCode);
        parcel.writeDouble(TaxableValue);
        parcel.writeDouble(SubTotal);
        parcel.writeDouble(IGSTRate);
        parcel.writeDouble(IGSTAmt);
        parcel.writeDouble(CGSTRate);
        parcel.writeDouble(CGSTAmt);
        parcel.writeDouble(SGSTRate);
        parcel.writeDouble(SGSTAmt);
        parcel.writeDouble(GSTRate);
        parcel.writeDouble(cessAmt);
        parcel.writeString(ProAss);
        parcel.writeDouble(NilRatedValue);
        parcel.writeDouble(ExemptedValue);
        parcel.writeDouble(NonGSTValue);
        parcel.writeDouble(CompoundingValue);
        parcel.writeDouble(unregisteredValue);
        parcel.writeString(cessRate);
        parcel.writeString(Orderno);
        parcel.writeString(OrderDate);
        parcel.writeString(etin);
        parcel.writeString(etype);
        parcel.writeString(sply_ty);
    }
}
