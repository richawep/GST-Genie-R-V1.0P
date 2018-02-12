/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	BillDetail
 * 
 * Purpose			:	Represents BillDetail table, takes care of intializing
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

public class BillDetailBean implements Parcelable{

	// Private variables
	String Custname, CustStateCode, CustPhone, CustAddress, POS, strDate, strTime, strUserId,BusinessType, GSTIN, strSalesManCode;
	int iID, iBillNumber, iBillStatus, iCustId, iEmployeeId, iReprintCount, iTotalItems, iUserId;
	String BillingMode, strInvoiceNo, strInvoiceDate; // richa_2012
	double dblBillAmount, dblCardPayment, dblCashPayment, dblCouponPayment, dblPettyCashPayment, dblPaidTotalPayment,  dblWalletAmount,
			dblDeliveryCharge, dblTotalDiscountPercentage, dblTotalDiscountAmount, dblIGSTAmount;
	double dblCGSTAmount, dblSGSTAmount, dblcessAmount,  dblTotalTaxAmount, dblTotalServiceTaxAmount;
	double dblRoundOff;
	double dblChangePayment, dblAEPSAmount;
	double dblSubTotal, dblTotalTaxableValue = 0;
	double dblRewardPoints =0;
	String strSalesManId ;
	double dblMSwipeAmount = 0;

	// Default Constructor
	public BillDetailBean(){
		this.GSTIN="";
		this.Custname="";

		this.BillingMode  = ""; // richa_2012
		this.strInvoiceNo ="";
		this.strInvoiceDate = "";
		this.CustStateCode="";
		this.POS = "";
		this.dblCGSTAmount=0;
		this.dblIGSTAmount=0;
		this.dblSGSTAmount=0;
		this.dblSubTotal=0;
		this.BusinessType="";
		this.strDate = "";
		this.strTime = "";
		this.iBillNumber = 0;
		this.iBillStatus = 0;
		this.iCustId = 0;
		this.iEmployeeId = 0;
		this.iReprintCount = 0;
		this.iTotalItems = 0;
		this.strUserId = "";
		this.iUserId = 0;
		this.dblBillAmount = 0;
		this.dblCardPayment = 0;
		this.dblAEPSAmount=0;
		this.dblRoundOff = 0;
		this.dblCashPayment = 0;
		this.dblCouponPayment = 0;
		this.dblDeliveryCharge = 0;
		this.dblTotalDiscountAmount = 0;
		this.dblTotalDiscountPercentage = 0;
		this.dblTotalTaxAmount = 0;
		this.dblTotalServiceTaxAmount = 0;
		this.dblWalletAmount = 0;
		this.dblPettyCashPayment = 0;
		this.dblPaidTotalPayment = 0;
		this.dblChangePayment = 0;
		this.dblcessAmount =0;
		this.dblTotalTaxableValue = 0;
		this.dblRewardPoints =0;
		this.strSalesManId = "";
		this.dblMSwipeAmount = 0;
	}


	protected BillDetailBean(Parcel in) {
		Custname = in.readString();
		CustStateCode = in.readString();
		CustPhone = in.readString();
		CustAddress = in.readString();
		POS = in.readString();
		strDate = in.readString();
		strTime = in.readString();
		strUserId = in.readString();
		BusinessType = in.readString();
		GSTIN = in.readString();
		strSalesManCode = in.readString();
		iID = in.readInt();
		iBillNumber = in.readInt();
		iBillStatus = in.readInt();
		iCustId = in.readInt();
		iEmployeeId = in.readInt();
		iReprintCount = in.readInt();
		iTotalItems = in.readInt();
		iUserId = in.readInt();
		BillingMode = in.readString();
		strInvoiceNo = in.readString();
		strInvoiceDate = in.readString();
		dblBillAmount = in.readDouble();
		dblCardPayment = in.readDouble();
		dblCashPayment = in.readDouble();
		dblCouponPayment = in.readDouble();
		dblPettyCashPayment = in.readDouble();
		dblPaidTotalPayment = in.readDouble();
		dblWalletAmount = in.readDouble();
		dblDeliveryCharge = in.readDouble();
		dblTotalDiscountPercentage = in.readDouble();
		dblTotalDiscountAmount = in.readDouble();
		dblIGSTAmount = in.readDouble();
		dblCGSTAmount = in.readDouble();
		dblSGSTAmount = in.readDouble();
		dblcessAmount = in.readDouble();
		dblTotalTaxAmount = in.readDouble();
		dblTotalServiceTaxAmount = in.readDouble();
		dblRoundOff = in.readDouble();
		dblChangePayment = in.readDouble();
		dblAEPSAmount = in.readDouble();
		dblSubTotal = in.readDouble();
		dblTotalTaxableValue = in.readDouble();
		dblRewardPoints = in.readDouble();
		strSalesManId = in.readString();
		dblMSwipeAmount = in.readDouble();
	}

	public static final Creator<BillDetailBean> CREATOR = new Creator<BillDetailBean>() {
		@Override
		public BillDetailBean createFromParcel(Parcel in) {
			return new BillDetailBean(in);
		}

		@Override
		public BillDetailBean[] newArray(int size) {
			return new BillDetailBean[size];
		}
	};

	public double getDblAEPSAmount() {
		return dblAEPSAmount;
	}

	public void setDblAEPSAmount(double dblAEPSAmount) {
		this.dblAEPSAmount = dblAEPSAmount;
	}

	public double getDblRewardPoints() {
		return dblRewardPoints;
	}

	public void setDblRewardPoints(double dblRewardPoints) {
		this.dblRewardPoints = dblRewardPoints;
	}

	public String getCustname() {
		return Custname;
	}

	public void setCustname(String custname) {
		Custname = custname;
	}

	public String getCustStateCode() {
		return CustStateCode;
	}

	public void setCustStateCode(String custStateCode) {
		CustStateCode = custStateCode;
	}

	public String getCustPhone() {
		return CustPhone;
	}

	public void setCustPhone(String custPhone) {
		CustPhone = custPhone;
	}

	public String getCustAddress() {
		return CustAddress;
	}

	public void setCustAddress(String custAddress) {
		CustAddress = custAddress;
	}

	public String getPOS() {
		return POS;
	}

	public void setPOS(String POS) {
		this.POS = POS;
	}

	public String getStrDate() {
		return strDate;
	}

	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}

	public String getStrTime() {
		return strTime;
	}

	public void setStrTime(String strTime) {
		this.strTime = strTime;
	}

	public String getStrUserId() {
		return strUserId;
	}

	public void setStrUserId(String strUserId) {
		this.strUserId = strUserId;
	}

	public String getBusinessType() {
		return BusinessType;
	}

	public void setBusinessType(String businessType) {
		BusinessType = businessType;
	}

	public String getGSTIN() {
		return GSTIN;
	}

	public void setGSTIN(String GSTIN) {
		this.GSTIN = GSTIN;
	}

	public String getStrSalesManCode() {
		return strSalesManCode;
	}

	public void setStrSalesManCode(String strSalesManCode) {
		this.strSalesManCode = strSalesManCode;
	}

	public int getiID() {
		return iID;
	}

	public void setiID(int iID) {
		this.iID = iID;
	}

	public int getiBillNumber() {
		return iBillNumber;
	}

	public void setiBillNumber(int iBillNumber) {
		this.iBillNumber = iBillNumber;
	}

	public int getiBillStatus() {
		return iBillStatus;
	}

	public void setiBillStatus(int iBillStatus) {
		this.iBillStatus = iBillStatus;
	}

	public int getiCustId() {
		return iCustId;
	}

	public void setiCustId(int iCustId) {
		this.iCustId = iCustId;
	}

	public int getiEmployeeId() {
		return iEmployeeId;
	}

	public void setiEmployeeId(int iEmployeeId) {
		this.iEmployeeId = iEmployeeId;
	}

	public int getiReprintCount() {
		return iReprintCount;
	}

	public void setiReprintCount(int iReprintCount) {
		this.iReprintCount = iReprintCount;
	}

	public int getiTotalItems() {
		return iTotalItems;
	}

	public void setiTotalItems(int iTotalItems) {
		this.iTotalItems = iTotalItems;
	}

	public int getiUserId() {
		return iUserId;
	}

	public void setiUserId(int iUserId) {
		this.iUserId = iUserId;
	}

	public String getBillingMode() {
		return BillingMode;
	}

	public void setBillingMode(String billingMode) {
		BillingMode = billingMode;
	}

	public double getDblBillAmount() {
		return dblBillAmount;
	}

	public void setDblBillAmount(double dblBillAmount) {
		this.dblBillAmount = dblBillAmount;
	}

	public double getDblCardPayment() {
		return dblCardPayment;
	}

	public void setDblCardPayment(double dblCardPayment) {
		this.dblCardPayment = dblCardPayment;
	}

	public double getDblCashPayment() {
		return dblCashPayment;
	}

	public void setDblCashPayment(double dblCashPayment) {
		this.dblCashPayment = dblCashPayment;
	}

	public double getDblCouponPayment() {
		return dblCouponPayment;
	}

	public void setDblCouponPayment(double dblCouponPayment) {
		this.dblCouponPayment = dblCouponPayment;
	}

	public double getDblPettyCashPayment() {
		return dblPettyCashPayment;
	}

	public void setDblPettyCashPayment(double dblPettyCashPayment) {
		this.dblPettyCashPayment = dblPettyCashPayment;
	}

	public double getDblPaidTotalPayment() {
		return dblPaidTotalPayment;
	}

	public void setDblPaidTotalPayment(double dblPaidTotalPayment) {
		this.dblPaidTotalPayment = dblPaidTotalPayment;
	}

	public double getDblWalletAmount() {
		return dblWalletAmount;
	}

	public void setDblWalletAmount(double dblWalletAmount) {
		this.dblWalletAmount = dblWalletAmount;
	}

	public double getDblDeliveryCharge() {
		return dblDeliveryCharge;
	}

	public void setDblDeliveryCharge(double dblDeliveryCharge) {
		this.dblDeliveryCharge = dblDeliveryCharge;
	}

	public double getDblTotalDiscountPercentage() {
		return dblTotalDiscountPercentage;
	}

	public void setDblTotalDiscountPercentage(double dblTotalDiscountPercentage) {
		this.dblTotalDiscountPercentage = dblTotalDiscountPercentage;
	}

	public double getDblTotalDiscountAmount() {
		return dblTotalDiscountAmount;
	}

	public void setDblTotalDiscountAmount(double dblTotalDiscountAmount) {
		this.dblTotalDiscountAmount = dblTotalDiscountAmount;
	}

	public double getDblIGSTAmount() {
		return dblIGSTAmount;
	}

	public void setDblIGSTAmount(double dblIGSTAmount) {
		this.dblIGSTAmount = dblIGSTAmount;
	}

	public double getDblCGSTAmount() {
		return dblCGSTAmount;
	}

	public void setDblCGSTAmount(double dblCGSTAmount) {
		this.dblCGSTAmount = dblCGSTAmount;
	}

	public double getDblSGSTAmount() {
		return dblSGSTAmount;
	}

	public void setDblSGSTAmount(double dblSGSTAmount) {
		this.dblSGSTAmount = dblSGSTAmount;
	}

	public double getDblcessAmount() {
		return dblcessAmount;
	}

	public void setDblcessAmount(double dblcessAmount) {
		this.dblcessAmount = dblcessAmount;
	}

	public double getDblTotalTaxAmount() {
		return dblTotalTaxAmount;
	}

	public void setDblTotalTaxAmount(double dblTotalTaxAmount) {
		this.dblTotalTaxAmount = dblTotalTaxAmount;
	}

	public double getDblTotalServiceTaxAmount() {
		return dblTotalServiceTaxAmount;
	}

	public void setDblTotalServiceTaxAmount(double dblTotalServiceTaxAmount) {
		this.dblTotalServiceTaxAmount = dblTotalServiceTaxAmount;
	}

	public double getDblRoundOff() {
		return dblRoundOff;
	}

	public void setDblRoundOff(double dblRoundOff) {
		this.dblRoundOff = dblRoundOff;
	}

	public double getDblChangePayment() {
		return dblChangePayment;
	}

	public void setDblChangePayment(double dblChangePayment) {
		this.dblChangePayment = dblChangePayment;
	}

	public double getDblSubTotal() {
		return dblSubTotal;
	}

	public void setDblSubTotal(double dblSubTotal) {
		this.dblSubTotal = dblSubTotal;
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

	public double getDblTotalTaxableValue() {
		return dblTotalTaxableValue;
	}

	public void setDblTotalTaxableValue(double dblTotalTaxableValue) {
		this.dblTotalTaxableValue = dblTotalTaxableValue;
	}

	public String getStrSalesManId() {
		return strSalesManId;
	}

	public void setStrSalesManId(String strSalesManId) {
		this.strSalesManId = strSalesManId;
	}

	public double getDblMSwipeAmount() {
		return dblMSwipeAmount;
	}

	public void setDblMSwipeAmount(double dblMSwipeAmount) {
		this.dblMSwipeAmount = dblMSwipeAmount;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeString(Custname);
		parcel.writeString(CustStateCode);
		parcel.writeString(CustPhone);
		parcel.writeString(CustAddress);
		parcel.writeString(POS);
		parcel.writeString(strDate);
		parcel.writeString(strTime);
		parcel.writeString(strUserId);
		parcel.writeString(BusinessType);
		parcel.writeString(GSTIN);
		parcel.writeString(strSalesManCode);
		parcel.writeInt(iID);
		parcel.writeInt(iBillNumber);
		parcel.writeInt(iBillStatus);
		parcel.writeInt(iCustId);
		parcel.writeInt(iEmployeeId);
		parcel.writeInt(iReprintCount);
		parcel.writeInt(iTotalItems);
		parcel.writeInt(iUserId);
		parcel.writeString(BillingMode);
		parcel.writeString(strInvoiceNo);
		parcel.writeString(strInvoiceDate);
		parcel.writeDouble(dblBillAmount);
		parcel.writeDouble(dblCardPayment);
		parcel.writeDouble(dblCashPayment);
		parcel.writeDouble(dblCouponPayment);
		parcel.writeDouble(dblPettyCashPayment);
		parcel.writeDouble(dblPaidTotalPayment);
		parcel.writeDouble(dblWalletAmount);
		parcel.writeDouble(dblDeliveryCharge);
		parcel.writeDouble(dblTotalDiscountPercentage);
		parcel.writeDouble(dblTotalDiscountAmount);
		parcel.writeDouble(dblIGSTAmount);
		parcel.writeDouble(dblCGSTAmount);
		parcel.writeDouble(dblSGSTAmount);
		parcel.writeDouble(dblcessAmount);
		parcel.writeDouble(dblTotalTaxAmount);
		parcel.writeDouble(dblTotalServiceTaxAmount);
		parcel.writeDouble(dblRoundOff);
		parcel.writeDouble(dblChangePayment);
		parcel.writeDouble(dblAEPSAmount);
		parcel.writeDouble(dblSubTotal);
		parcel.writeDouble(dblTotalTaxableValue);
		parcel.writeDouble(dblRewardPoints);
		parcel.writeString(strSalesManId);
		parcel.writeDouble(dblMSwipeAmount);
	}
}
