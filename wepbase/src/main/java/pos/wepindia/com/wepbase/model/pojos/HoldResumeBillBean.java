package pos.wepindia.com.wepbase.model.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MohanN on 1/19/2018.
 */

public class HoldResumeBillBean implements Parcelable{
    private int _id, iSlNo, iItemID, iTaxType, iCategoryCode, iDepartmentCode, iBrandCode,
            iBillStatus;
    private String strGSTIN, strCustName, strCustStateCode, strCustPhone, strInvoiceNo,
            strInvoiceDate, strSupplyType, strBussinessType, strTaxationType,
            strHSNCode, strItemName, strBarcode, strUOM, strIsReverseTaxEnabled, strBillingMode, strSalesManId;
    private double dblQty, dblOriginalRate, dblValue, dblTaxableValue, dblAmount,
            dblIGSTRate, dblIGSTAmount, dblCGSTRate, dblCGSTAmount,
            dblSGSTRate, dblSGSTAmount, dblCessRate, dblCessAmount,
            dblSubTotal, dblBillAmount, dblModifierAmount,
            dblDiscountPercent, dblDiscountAmount, dblTaxAmount, dblTaxPercent, dblMRP, dblRetailPrice, dblWholeSalePrice;

    protected HoldResumeBillBean(Parcel in) {
        _id = in.readInt();
        iSlNo = in.readInt();
        iItemID = in.readInt();
        iTaxType = in.readInt();
        iCategoryCode = in.readInt();
        iDepartmentCode = in.readInt();
        iBrandCode = in.readInt();
        iBillStatus = in.readInt();
        strGSTIN = in.readString();
        strCustName = in.readString();
        strCustStateCode = in.readString();
        strCustPhone = in.readString();
        strInvoiceNo = in.readString();
        strInvoiceDate = in.readString();
        strSupplyType = in.readString();
        strBussinessType = in.readString();
        strTaxationType = in.readString();
        strHSNCode = in.readString();
        strItemName = in.readString();
        strBarcode = in.readString();
        strUOM = in.readString();
        strIsReverseTaxEnabled = in.readString();
        strBillingMode = in.readString();
        strSalesManId = in.readString();
        dblQty = in.readDouble();
        dblOriginalRate = in.readDouble();
        dblValue = in.readDouble();
        dblTaxableValue = in.readDouble();
        dblAmount = in.readDouble();
        dblIGSTRate = in.readDouble();
        dblIGSTAmount = in.readDouble();
        dblCGSTRate = in.readDouble();
        dblCGSTAmount = in.readDouble();
        dblSGSTRate = in.readDouble();
        dblSGSTAmount = in.readDouble();
        dblCessRate = in.readDouble();
        dblCessAmount = in.readDouble();
        dblSubTotal = in.readDouble();
        dblBillAmount = in.readDouble();
        dblModifierAmount = in.readDouble();
        dblDiscountPercent = in.readDouble();
        dblDiscountAmount = in.readDouble();
        dblTaxAmount = in.readDouble();
        dblTaxPercent = in.readDouble();
        dblMRP = in.readDouble();
        dblRetailPrice = in.readDouble();
        dblWholeSalePrice = in.readDouble();
    }

    public static final Creator<HoldResumeBillBean> CREATOR = new Creator<HoldResumeBillBean>() {
        @Override
        public HoldResumeBillBean createFromParcel(Parcel in) {
            return new HoldResumeBillBean(in);
        }

        @Override
        public HoldResumeBillBean[] newArray(int size) {
            return new HoldResumeBillBean[size];
        }
    };

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getiBrandCode() {
        return iBrandCode;
    }

    public void setiBrandCode(int iBrandCode) {
        this.iBrandCode = iBrandCode;
    }

    public int getiSlNo() {
        return iSlNo;
    }

    public void setiSlNo(int iSlNo) {
        this.iSlNo = iSlNo;
    }

    public int getiItemID() {
        return iItemID;
    }

    public void setiItemID(int iItemID) {
        this.iItemID = iItemID;
    }

    public int getiTaxType() {
        return iTaxType;
    }

    public void setiTaxType(int iTaxType) {
        this.iTaxType = iTaxType;
    }

    public int getiCategoryCode() {
        return iCategoryCode;
    }

    public void setiCategoryCode(int iCategoryCode) {
        this.iCategoryCode = iCategoryCode;
    }

    public int getiDepartmentCode() {
        return iDepartmentCode;
    }

    public void setiDepartmentCode(int iDepartmentCode) {
        this.iDepartmentCode = iDepartmentCode;
    }

    public int getiBillStatus() {
        return iBillStatus;
    }

    public void setiBillStatus(int iBillStatus) {
        this.iBillStatus = iBillStatus;
    }

    public String getStrGSTIN() {
        return strGSTIN;
    }

    public void setStrGSTIN(String strGSTIN) {
        this.strGSTIN = strGSTIN;
    }

    public String getStrCustName() {
        return strCustName;
    }

    public void setStrCustName(String strCustName) {
        this.strCustName = strCustName;
    }

    public String getStrCustStateCode() {
        return strCustStateCode;
    }

    public void setStrCustStateCode(String strCustStateCode) {
        this.strCustStateCode = strCustStateCode;
    }

    public String getStrCustPhone() {
        return strCustPhone;
    }

    public void setStrCustPhone(String strCustPhone) {
        this.strCustPhone = strCustPhone;
    }

    public String getStrInvoiceNo() {
        return strInvoiceNo;
    }

    public void setStrInvoiceNo(String strInvoiceNo) {
        this.strInvoiceNo = strInvoiceNo;
    }

    public String getStrInvoiceDate() {
        return strInvoiceDate;
    }

    public void setStrInvoiceDate(String strInvoiceDate) {
        this.strInvoiceDate = strInvoiceDate;
    }

    public String getStrSupplyType() {
        return strSupplyType;
    }

    public void setStrSupplyType(String strSupplyType) {
        this.strSupplyType = strSupplyType;
    }

    public String getStrBussinessType() {
        return strBussinessType;
    }

    public void setStrBussinessType(String strBussinessType) {
        this.strBussinessType = strBussinessType;
    }

    public String getStrTaxationType() {
        return strTaxationType;
    }

    public void setStrTaxationType(String strTaxationType) {
        this.strTaxationType = strTaxationType;
    }

    public String getStrHSNCode() {
        return strHSNCode;
    }

    public void setStrHSNCode(String strHSNCode) {
        this.strHSNCode = strHSNCode;
    }

    public String getStrItemName() {
        return strItemName;
    }

    public void setStrItemName(String strItemName) {
        this.strItemName = strItemName;
    }

    public String getStrBarcode() {
        return strBarcode;
    }

    public void setStrBarcode(String strBarcode) {
        this.strBarcode = strBarcode;
    }

    public String getStrUOM() {
        return strUOM;
    }

    public void setStrUOM(String strUOM) {
        this.strUOM = strUOM;
    }

    public String getStrIsReverseTaxEnabled() {
        return strIsReverseTaxEnabled;
    }

    public void setStrIsReverseTaxEnabled(String strIsReverseTaxEnabled) {
        this.strIsReverseTaxEnabled = strIsReverseTaxEnabled;
    }

    public String getStrBillingMode() {
        return strBillingMode;
    }

    public void setStrBillingMode(String strBillingMode) {
        this.strBillingMode = strBillingMode;
    }

    public double getDblQty() {
        return dblQty;
    }

    public void setDblQty(double dblQty) {
        this.dblQty = dblQty;
    }

    public double getDblOriginalRate() {
        return dblOriginalRate;
    }

    public void setDblOriginalRate(double dblOriginalRate) {
        this.dblOriginalRate = dblOriginalRate;
    }

    public double getDblValue() {
        return dblValue;
    }

    public void setDblValue(double dblValue) {
        this.dblValue = dblValue;
    }

    public double getDblTaxableValue() {
        return dblTaxableValue;
    }

    public void setDblTaxableValue(double dblTaxableValue) {
        this.dblTaxableValue = dblTaxableValue;
    }

    public double getDblAmount() {
        return dblAmount;
    }

    public void setDblAmount(double dblAmount) {
        this.dblAmount = dblAmount;
    }

    public double getDblIGSTRate() {
        return dblIGSTRate;
    }

    public void setDblIGSTRate(double dblIGSTRate) {
        this.dblIGSTRate = dblIGSTRate;
    }

    public double getDblIGSTAmount() {
        return dblIGSTAmount;
    }

    public void setDblIGSTAmount(double dblIGSTAmount) {
        this.dblIGSTAmount = dblIGSTAmount;
    }

    public double getDblCGSTRate() {
        return dblCGSTRate;
    }

    public void setDblCGSTRate(double dblCGSTRate) {
        this.dblCGSTRate = dblCGSTRate;
    }

    public double getDblCGSTAmount() {
        return dblCGSTAmount;
    }

    public void setDblCGSTAmount(double dblCGSTAmount) {
        this.dblCGSTAmount = dblCGSTAmount;
    }

    public double getDblSGSTRate() {
        return dblSGSTRate;
    }

    public void setDblSGSTRate(double dblSGSTRate) {
        this.dblSGSTRate = dblSGSTRate;
    }

    public double getDblSGSTAmount() {
        return dblSGSTAmount;
    }

    public void setDblSGSTAmount(double dblSGSTAmount) {
        this.dblSGSTAmount = dblSGSTAmount;
    }

    public double getDblCessRate() {
        return dblCessRate;
    }

    public void setDblCessRate(double dblCessRate) {
        this.dblCessRate = dblCessRate;
    }

    public double getDblCessAmount() {
        return dblCessAmount;
    }

    public void setDblCessAmount(double dblCessAmount) {
        this.dblCessAmount = dblCessAmount;
    }

    public double getDblSubTotal() {
        return dblSubTotal;
    }

    public void setDblSubTotal(double dblSubTotal) {
        this.dblSubTotal = dblSubTotal;
    }

    public double getDblBillAmount() {
        return dblBillAmount;
    }

    public void setDblBillAmount(double dblBillAmount) {
        this.dblBillAmount = dblBillAmount;
    }

    public double getDblModifierAmount() {
        return dblModifierAmount;
    }

    public void setDblModifierAmount(double dblModifierAmount) {
        this.dblModifierAmount = dblModifierAmount;
    }

    public double getDblDiscountPercent() {
        return dblDiscountPercent;
    }

    public void setDblDiscountPercent(double dblDiscountPercent) {
        this.dblDiscountPercent = dblDiscountPercent;
    }

    public double getDblDiscountAmount() {
        return dblDiscountAmount;
    }

    public void setDblDiscountAmount(double dblDiscountAmount) {
        this.dblDiscountAmount = dblDiscountAmount;
    }

    public double getDblTaxAmount() {
        return dblTaxAmount;
    }

    public void setDblTaxAmount(double dblTaxAmount) {
        this.dblTaxAmount = dblTaxAmount;
    }

    public double getDblTaxPercent() {
        return dblTaxPercent;
    }

    public void setDblTaxPercent(double dblTaxPercent) {
        this.dblTaxPercent = dblTaxPercent;
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

    public String getStrSalesManId() {
        return strSalesManId;
    }

    public void setStrSalesManId(String strSalesManId) {
        this.strSalesManId = strSalesManId;
    }

    public HoldResumeBillBean(){
        this.strGSTIN = "";
        this.strCustName = "";
        this.strCustStateCode = "";
        this.strCustPhone = "";
        this.strInvoiceNo = "";
        this.strInvoiceDate = "";
        this.strSupplyType = "";
        this.strBussinessType = "";
        this.strTaxationType = "";
        this.strHSNCode = "";
        this.iItemID = 0;
        this.strItemName = "";
        this.strBarcode = "";
        this.dblQty = 0;
        this.strUOM = "";
        this.dblOriginalRate = 0;
        this.dblValue = 0;
        this.dblTaxableValue = 0;
        this.dblAmount = 0;
        this.strIsReverseTaxEnabled = "";
        this.dblIGSTRate = 0;
        this.dblIGSTAmount = 0;
        this.dblCGSTRate = 0;
        this.dblCGSTAmount = 0;
        this.dblSGSTRate = 0;
        this.dblSGSTAmount = 0;
        this.dblCessRate = 0;
        this.dblCessAmount = 0;
        this.dblSubTotal = 0;
        this.dblBillAmount = 0;
        this.strBillingMode = "";
        this.dblModifierAmount = 0;
        this.iTaxType = 0;
        this.iCategoryCode = 0;
        this.iDepartmentCode = 0;
        this.iBrandCode=0;
        this.dblDiscountPercent = 0;
        this.dblDiscountAmount = 0;
        this.dblTaxAmount = 0;
        this.iBillStatus = 0;
        this.dblTaxPercent = 0;
        this.dblMRP = 0;
        this.dblRetailPrice = 0;
        this.dblWholeSalePrice = 0;
        this.strSalesManId = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(_id);
        parcel.writeInt(iSlNo);
        parcel.writeInt(iItemID);
        parcel.writeInt(iTaxType);
        parcel.writeInt(iCategoryCode);
        parcel.writeInt(iDepartmentCode);
        parcel.writeInt(iBrandCode);
        parcel.writeInt(iBillStatus);
        parcel.writeString(strGSTIN);
        parcel.writeString(strCustName);
        parcel.writeString(strCustStateCode);
        parcel.writeString(strCustPhone);
        parcel.writeString(strInvoiceNo);
        parcel.writeString(strInvoiceDate);
        parcel.writeString(strSupplyType);
        parcel.writeString(strBussinessType);
        parcel.writeString(strTaxationType);
        parcel.writeString(strHSNCode);
        parcel.writeString(strItemName);
        parcel.writeString(strBarcode);
        parcel.writeString(strUOM);
        parcel.writeString(strIsReverseTaxEnabled);
        parcel.writeString(strBillingMode);
        parcel.writeString(strSalesManId);
        parcel.writeDouble(dblQty);
        parcel.writeDouble(dblOriginalRate);
        parcel.writeDouble(dblValue);
        parcel.writeDouble(dblTaxableValue);
        parcel.writeDouble(dblAmount);
        parcel.writeDouble(dblIGSTRate);
        parcel.writeDouble(dblIGSTAmount);
        parcel.writeDouble(dblCGSTRate);
        parcel.writeDouble(dblCGSTAmount);
        parcel.writeDouble(dblSGSTRate);
        parcel.writeDouble(dblSGSTAmount);
        parcel.writeDouble(dblCessRate);
        parcel.writeDouble(dblCessAmount);
        parcel.writeDouble(dblSubTotal);
        parcel.writeDouble(dblBillAmount);
        parcel.writeDouble(dblModifierAmount);
        parcel.writeDouble(dblDiscountPercent);
        parcel.writeDouble(dblDiscountAmount);
        parcel.writeDouble(dblTaxAmount);
        parcel.writeDouble(dblTaxPercent);
        parcel.writeDouble(dblMRP);
        parcel.writeDouble(dblRetailPrice);
        parcel.writeDouble(dblWholeSalePrice);
    }
}
