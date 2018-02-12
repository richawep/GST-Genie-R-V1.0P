package pos.wepindia.com.wepbase.model.pojos;

/**
 * Created by MohanN on 1/2/2018.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable{

    public static final String CUSTOMER_PARCELABLE_KEY ="customer";

    // Private Variable
    String strCustName, strCustContactNumber, strCustAddress, strCustGSTIN, strEmailId;
    int iCustId, iDepositAmtStatus, iRewardPoints,isActive,_id;
    double dLastTransaction, dTotalTransaction;
    double dCreditAmount, dCreditLimit,dblDepositAmt;
    double openingBalance=0;


    public Customer() {
        this.strCustName = "";
        this.strCustContactNumber = "";
        this.strCustAddress = "";
        this.strCustGSTIN = "";
        this.strEmailId = "";
        this.iCustId = 0;
        this.iDepositAmtStatus = 0;
        this.iRewardPoints = 0;
        this.isActive = 0;
        this._id = 0;
        this.dLastTransaction = 0;
        this.dTotalTransaction = 0;
        this.dCreditAmount = 0;
        this.dCreditLimit = 0;
        this.dblDepositAmt = 0;
        this.openingBalance = 0;
    }

    protected Customer(Parcel in) {
        strCustName = in.readString();
        strCustContactNumber = in.readString();
        strCustAddress = in.readString();
        strCustGSTIN = in.readString();
        strEmailId = in.readString();
        iCustId = in.readInt();
        iDepositAmtStatus = in.readInt();
        iRewardPoints = in.readInt();
        isActive = in.readInt();
        _id = in.readInt();
        dLastTransaction = in.readDouble();
        dTotalTransaction = in.readDouble();
        dCreditAmount = in.readDouble();
        dCreditLimit = in.readDouble();
        dblDepositAmt = in.readDouble();
        openingBalance = in.readDouble();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public double getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(double openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getStrCustName() {
        return strCustName;
    }

    public void setStrCustName(String strCustName) {
        this.strCustName = strCustName;
    }

    public String getStrCustContactNumber() {
        return strCustContactNumber;
    }

    public void setStrCustContactNumber(String strCustContactNumber) {
        this.strCustContactNumber = strCustContactNumber;
    }

    public String getStrCustAddress() {
        return strCustAddress;
    }

    public void setStrCustAddress(String strCustAddress) {
        this.strCustAddress = strCustAddress;
    }

    public String getStrCustGSTIN() {
        return strCustGSTIN;
    }

    public void setStrCustGSTIN(String strCustGSTIN) {
        this.strCustGSTIN = strCustGSTIN;
    }

    public String getStrEmailId() {
        return strEmailId;
    }

    public void setStrEmailId(String strEmailId) {
        this.strEmailId = strEmailId;
    }

    public int getiCustId() {
        return iCustId;
    }

    public void setiCustId(int iCustId) {
        this.iCustId = iCustId;
    }

    public int getiDepositAmtStatus() {
        return iDepositAmtStatus;
    }

    public void setiDepositAmtStatus(int iDepositAmtStatus) {
        this.iDepositAmtStatus = iDepositAmtStatus;
    }

    public int getiRewardPoints() {
        return iRewardPoints;
    }

    public void setiRewardPoints(int iRewardPoints) {
        this.iRewardPoints = iRewardPoints;
    }

    public double getdLastTransaction() {
        return dLastTransaction;
    }

    public void setdLastTransaction(double dLastTransaction) {
        this.dLastTransaction = dLastTransaction;
    }

    public double getdTotalTransaction() {
        return dTotalTransaction;
    }

    public void setdTotalTransaction(double dTotalTransaction) {
        this.dTotalTransaction = dTotalTransaction;
    }

    public double getdCreditAmount() {
        return dCreditAmount;
    }

    public void setdCreditAmount(double dCreditAmount) {
        this.dCreditAmount = dCreditAmount;
    }

    public double getdCreditLimit() {
        return dCreditLimit;
    }

    public void setdCreditLimit(double dCreditLimit) {
        this.dCreditLimit = dCreditLimit;
    }

    public double getDblDepositAmt() {
        return dblDepositAmt;
    }

    public void setDblDepositAmt(double dblDepositAmt) {
        this.dblDepositAmt = dblDepositAmt;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(strCustName);
        parcel.writeString(strCustContactNumber);
        parcel.writeString(strCustAddress);
        parcel.writeString(strCustGSTIN);
        parcel.writeString(strEmailId);
        parcel.writeInt(iCustId);
        parcel.writeInt(iDepositAmtStatus);
        parcel.writeInt(iRewardPoints);
        parcel.writeInt(isActive);
        parcel.writeInt(_id);
        parcel.writeDouble(dLastTransaction);
        parcel.writeDouble(dTotalTransaction);
        parcel.writeDouble(dCreditAmount);
        parcel.writeDouble(dCreditLimit);
        parcel.writeDouble(dblDepositAmt);
        parcel.writeDouble(openingBalance);
    }
}