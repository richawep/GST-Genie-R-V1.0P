/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	BillItem
 * 
 * Purpose			:	Represents BillItem table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	16-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package pos.wepindia.com.wepbase.model.pojos;

import android.os.Parcel;
import android.os.Parcelable;

public class BillItemBean implements Parcelable{

	public static final String BILL_ITEM_BEAN_PARCELABLE_KEY ="bill_item_bean";

	String strGSTIN, strCustName, strCustStateCode, strCustAddress, strCustPhone, strInvoiceNo, strInvoiceDate, strCustId,
			strSupplyType, strBusinessType, strTaxationType, strHSNCode,
			strItemName, strBarcode, strUOM, strIsReverseTaxEnabled, strBillingMode, strSalesManId;
	int iID, iItemId, iTaxType, iCategCode, iDeptCode, iBrandCode,iBillStatus;
	double dblQty, dblOriginalRate, dblValue, dblTaxbleValue, dblAmount,
			dblIGSTRate, dblIGSTAmount, dblCGSTRate, dblCGSTAmount,
			dblSGSTRate, dblSGSTAmount, dblCessRate, dblCessAmount,
			dblSubTotal, dblOtherCharges,
			dblModifierAmount, dblDiscountPercent, dblDiscountAmount,
			dblTaxAmount, dblTaxPercent;

	double dblMRP, dblRetailPrice, dblWholeSalePrice;


	protected BillItemBean(Parcel in) {
		strGSTIN = in.readString();
		strCustName = in.readString();
		strCustStateCode = in.readString();
		strCustAddress = in.readString();
		strCustPhone = in.readString();
		strInvoiceNo = in.readString();
		strInvoiceDate = in.readString();
		strCustId = in.readString();
		strSupplyType = in.readString();
		strBusinessType = in.readString();
		strTaxationType = in.readString();
		strHSNCode = in.readString();
		strItemName = in.readString();
		strBarcode = in.readString();
		strUOM = in.readString();
		strIsReverseTaxEnabled = in.readString();
		strBillingMode = in.readString();
		strSalesManId = in.readString();
		iID = in.readInt();
		iItemId = in.readInt();
		iTaxType = in.readInt();
		iCategCode = in.readInt();
		iDeptCode = in.readInt();
		iBrandCode = in.readInt();
		iBillStatus = in.readInt();
		dblQty = in.readDouble();
		dblOriginalRate = in.readDouble();
		dblValue = in.readDouble();
		dblTaxbleValue = in.readDouble();
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
		dblOtherCharges = in.readDouble();
		dblModifierAmount = in.readDouble();
		dblDiscountPercent = in.readDouble();
		dblDiscountAmount = in.readDouble();
		dblTaxAmount = in.readDouble();
		dblTaxPercent = in.readDouble();
		dblMRP = in.readDouble();
		dblRetailPrice = in.readDouble();
		dblWholeSalePrice = in.readDouble();
	}

	public static final Creator<BillItemBean> CREATOR = new Creator<BillItemBean>() {
		@Override
		public BillItemBean createFromParcel(Parcel in) {
			return new BillItemBean(in);
		}

		@Override
		public BillItemBean[] newArray(int size) {
			return new BillItemBean[size];
		}
	};

	public int getiBrandCode() {
		return iBrandCode;
	}

	public void setiBrandCode(int iBrandCode) {
		this.iBrandCode = iBrandCode;
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

	public String getStrCustId() {
		return strCustId;
	}

	public void setStrCustId(String strCustId) {
		this.strCustId = strCustId;
	}

	public String getStrCustAddress() {
		return strCustAddress;
	}

	public void setStrCustAddress(String strCustAddress) {
		this.strCustAddress = strCustAddress;
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

	public String getStrBusinessType() {
		return strBusinessType;
	}

	public void setStrBusinessType(String strBusinessType) {
		this.strBusinessType = strBusinessType;
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

	public int getiID() {
		return iID;
	}

	public void setiID(int iID) {
		this.iID = iID;
	}

	public int getiItemId() {
		return iItemId;
	}

	public void setiItemId(int iItemId) {
		this.iItemId = iItemId;
	}

	public int getiTaxType() {
		return iTaxType;
	}

	public void setiTaxType(int iTaxType) {
		this.iTaxType = iTaxType;
	}

	public int getiCategCode() {
		return iCategCode;
	}

	public void setiCategCode(int iCategCode) {
		this.iCategCode = iCategCode;
	}

	public int getiDeptCode() {
		return iDeptCode;
	}

	public void setiDeptCode(int iDeptCode) {
		this.iDeptCode = iDeptCode;
	}

	public int getiBillStatus() {
		return iBillStatus;
	}

	public void setiBillStatus(int iBillStatus) {
		this.iBillStatus = iBillStatus;
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

	public double getDblTaxbleValue() {
		return dblTaxbleValue;
	}

	public void setDblTaxbleValue(double dblTaxbleValue) {
		this.dblTaxbleValue = dblTaxbleValue;
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

	public double getDblOtherCharges() {
		return dblOtherCharges;
	}

	public void setDblOtherCharges(double dblOtherCharges) {
		this.dblOtherCharges = dblOtherCharges;
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


	// Default constructor
	public BillItemBean(){
		this.dblOriginalRate = 0.00;
		this.strCustAddress = "";
		this.strUOM="";
		this.strIsReverseTaxEnabled="";
		this.strCustStateCode="";
		this.strCustName="";
		this.strGSTIN ="";
		this.strCustPhone="";
		this.strTaxationType="";
		this.dblSubTotal = 0;
		this.dblTaxbleValue = 0;
		this.strItemName = "";
		this.strSupplyType = "";
		this.iCategCode = 0;
		this.iDeptCode = 0;
		this.iBrandCode = 0;
		this.iItemId = 0;
		//this.iKitchenCode = 0;
		this.strInvoiceNo = "";
		this.iTaxType = 0;
		this.dblAmount = 0;
		this.dblDiscountAmount = 0;
		this.dblDiscountPercent = 0;
		this.dblQty = 0;
		this.dblValue = 0;
		this.dblTaxAmount = 0;
		this.dblTaxPercent = 0;
		this.dblOtherCharges = 0;
		this.dblModifierAmount = 0;
		this.strBusinessType= "";
		//this.SupplierGSTIN= "";
		this.strInvoiceDate= "";
		this.strHSNCode="";
		this.dblIGSTAmount=0;
		this.dblIGSTRate= 0;
		this.dblCGSTRate=0;
		this.dblCGSTAmount=0;
		this.dblSGSTRate=0;
		this.dblSGSTAmount=0;
		this.dblCessRate=0;
		this.dblCessAmount=0;
		//this.isGoodInwarded=0;
		this.iBillStatus=0;
		this.strSupplyType="";
		this.strSalesManId = "";
		this.strCustId = "-1";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(strGSTIN);
		parcel.writeString(strCustName);
		parcel.writeString(strCustStateCode);
		parcel.writeString(strCustAddress);
		parcel.writeString(strCustPhone);
		parcel.writeString(strInvoiceNo);
		parcel.writeString(strInvoiceDate);
		parcel.writeString(strCustId);
		parcel.writeString(strSupplyType);
		parcel.writeString(strBusinessType);
		parcel.writeString(strTaxationType);
		parcel.writeString(strHSNCode);
		parcel.writeString(strItemName);
		parcel.writeString(strBarcode);
		parcel.writeString(strUOM);
		parcel.writeString(strIsReverseTaxEnabled);
		parcel.writeString(strBillingMode);
		parcel.writeString(strSalesManId);
		parcel.writeInt(iID);
		parcel.writeInt(iItemId);
		parcel.writeInt(iTaxType);
		parcel.writeInt(iCategCode);
		parcel.writeInt(iDeptCode);
		parcel.writeInt(iBrandCode);
		parcel.writeInt(iBillStatus);
		parcel.writeDouble(dblQty);
		parcel.writeDouble(dblOriginalRate);
		parcel.writeDouble(dblValue);
		parcel.writeDouble(dblTaxbleValue);
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
		parcel.writeDouble(dblOtherCharges);
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
