package pos.wepindia.com.wepbase.model.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MohanN on 12/14/2017.
 */

public class BillingItemsList implements Parcelable{

    String strBarcode, strItemName, strUOM, strHSN;
    int iBrand, iDepartment, iCategory, iQty;
    double dblMRP, dblRetailPrice, dblWholeSalePrice, dblPurchasePrice, dblDisc, dblAmount;


    public String getStrBarcode() {
        return strBarcode;
    }

    public void setStrBarcode(String strBarcode) {
        this.strBarcode = strBarcode;
    }

    public String getStrItemName() {
        return strItemName;
    }

    public void setStrItemName(String strItemName) {
        this.strItemName = strItemName;
    }

    public String getStrUOM() {
        return strUOM;
    }

    public void setStrUOM(String strUOM) {
        this.strUOM = strUOM;
    }

    public String getStrHSN() {
        return strHSN;
    }

    public void setStrHSN(String strHSN) {
        this.strHSN = strHSN;
    }

    public int getiBrand() {
        return iBrand;
    }

    public void setiBrand(int iBrand) {
        this.iBrand = iBrand;
    }

    public int getiDepartment() {
        return iDepartment;
    }

    public void setiDepartment(int iDepartment) {
        this.iDepartment = iDepartment;
    }

    public int getiCategory() {
        return iCategory;
    }

    public void setiCategory(int iCategory) {
        this.iCategory = iCategory;
    }

    public double getDblMRP() {
        return dblMRP;
    }

    public void setDblMRP(double dblMRP) {
        this.dblMRP = dblMRP;
    }

    public double getDblRetailPrice() {
        return dblRetailPrice;
    }

    public void setDblRetailPrice(double dblRetailPrice) {
        this.dblRetailPrice = dblRetailPrice;
    }

    public double getDblWholeSalePrice() {
        return dblWholeSalePrice;
    }

    public void setDblWholeSalePrice(double dblWholeSalePrice) {
        this.dblWholeSalePrice = dblWholeSalePrice;
    }

    public double getDblPurchasePrice() {
        return dblPurchasePrice;
    }

    public void setDblPurchasePrice(double dblPurchasePrice) {
        this.dblPurchasePrice = dblPurchasePrice;
    }

    public double getDblDisc() {
        return dblDisc;
    }

    public void setDblDisc(double dblDisc) {
        this.dblDisc = dblDisc;
    }

    public double getDblAmount() {
        return dblAmount;
    }

    public void setDblAmount(double dblAmount) {
        this.dblAmount = dblAmount;
    }

    public int incrementQty(){
        return this.iQty++;
    }

    public int decrementQty(){
        return this.iQty--;
    }

    public int setiQty(int iQty){
        this.iQty = iQty;
        return iQty;
    }

    public int getiQty(){
        return this.iQty;
    }

    public BillingItemsList(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int iFlag) {
        parcel.writeString(strBarcode);
        parcel.writeString(strItemName);
        parcel.writeString(strUOM);
        parcel.writeString(strHSN);
        parcel.writeInt(iBrand);
        parcel.writeInt(iDepartment);
        parcel.writeInt(iCategory);
        parcel.writeInt(iQty);
        parcel.writeDouble(dblMRP);
        parcel.writeDouble(dblRetailPrice);
        parcel.writeDouble(dblWholeSalePrice);
        parcel.writeDouble(dblPurchasePrice);
        parcel.writeDouble(dblDisc);
        parcel.writeDouble(dblAmount);
    }

    /**
     * Retrieving BillingItemsList data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    public BillingItemsList(Parcel in){
        this.strBarcode = in.readString();
        this.strItemName = in.readString();
        this.strUOM = in.readString();
        this.strHSN = in.readString();
        this.iBrand = in.readInt();
        this.iDepartment = in.readInt();
        this.iCategory = in.readInt();
        this.iQty = in.readInt();
        this.dblMRP = in.readDouble();
        this.dblRetailPrice = in.readDouble();
        this.dblWholeSalePrice = in.readDouble();
        this.dblPurchasePrice = in.readDouble();
        this.dblDisc = in.readDouble();
        this.dblAmount = in.readDouble();
    }

    public static final Creator<BillingItemsList> CREATOR = new Creator<BillingItemsList>() {

        @Override
        public BillingItemsList createFromParcel(Parcel source) {
            return new BillingItemsList(source);
        }

        @Override
        public BillingItemsList[] newArray(int size) {
            return new BillingItemsList[size];
        }
    };
}