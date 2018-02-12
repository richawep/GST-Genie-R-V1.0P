package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 25-01-2018.
 */

public class CustomerWiseReportBean implements Parcelable {
    
    private int custId;
    private String name;
    private int totalBills;
    private double lastTransaction;
    private double cashAmount;
    private double cardPayment;
    private double couponPayment;
    private double pettyCashPayment;
    private double walletPayment;
    private double totalTransaction;

    public CustomerWiseReportBean() {
    }

    protected CustomerWiseReportBean(Parcel in) {
        custId = in.readInt();
        name = in.readString();
        totalBills = in.readInt();
        lastTransaction = in.readDouble();
        cashAmount = in.readDouble();
        cardPayment = in.readDouble();
        couponPayment = in.readDouble();
        pettyCashPayment = in.readDouble();
        walletPayment = in.readDouble();
        totalTransaction = in.readDouble();
    }

    public static final Creator<CustomerWiseReportBean> CREATOR = new Creator<CustomerWiseReportBean>() {
        @Override
        public CustomerWiseReportBean createFromParcel(Parcel in) {
            return new CustomerWiseReportBean(in);
        }

        @Override
        public CustomerWiseReportBean[] newArray(int size) {
            return new CustomerWiseReportBean[size];
        }
    };

    public int getcustId() {
        return custId;
    }

    public void setcustId(int custId) {
        this.custId = custId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalBills() {
        return totalBills;
    }

    public void setTotalBills(int totalBills) {
        this.totalBills = totalBills;
    }

    public double getLastTransaction() {
        return lastTransaction;
    }

    public void setLastTransaction(double lastTransaction) {
        this.lastTransaction = lastTransaction;
    }

    public double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(double cashAmount) {
        this.cashAmount = cashAmount;
    }

    public double getCardPayment() {
        return cardPayment;
    }

    public void setCardPayment(double cardPayment) {
        this.cardPayment = cardPayment;
    }

    public double getCouponPayment() {
        return couponPayment;
    }

    public void setCouponPayment(double couponPayment) {
        this.couponPayment = couponPayment;
    }

    public double getPettyCashPayment() {
        return pettyCashPayment;
    }

    public void setPettyCashPayment(double pettyCashPayment) {
        this.pettyCashPayment = pettyCashPayment;
    }

    public double getWalletPayment() {
        return walletPayment;
    }

    public void setWalletPayment(double walletPayment) {
        this.walletPayment = walletPayment;
    }

    public double getTotalTransaction() {
        return totalTransaction;
    }

    public void setTotalTransaction(double totalTransaction) {
        this.totalTransaction = totalTransaction;
    }

    public static Creator<CustomerWiseReportBean> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(custId);
        parcel.writeString(name);
        parcel.writeInt(totalBills);
        parcel.writeDouble(lastTransaction);
        parcel.writeDouble(cashAmount);
        parcel.writeDouble(cardPayment);
        parcel.writeDouble(couponPayment);
        parcel.writeDouble(pettyCashPayment);
        parcel.writeDouble(walletPayment);
        parcel.writeDouble(totalTransaction);
    }
}
