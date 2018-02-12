package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 25-01-2018.
 */

public class CustomerDetailReportBean implements Parcelable {

    private String date;
    private int billNumber;
    private int totalItems;
    private double discount;
    private double cashAmount;
    private double cardPayment;
    private double couponPayment;
    private double pettyCashPayment;
    private double walletPayment;
    private double billAmount;

    public CustomerDetailReportBean() {
    }

    protected CustomerDetailReportBean(Parcel in) {
        date = in.readString();
        billNumber = in.readInt();
        totalItems = in.readInt();
        discount = in.readDouble();
        cashAmount = in.readDouble();
        cardPayment = in.readDouble();
        couponPayment = in.readDouble();
        pettyCashPayment = in.readDouble();
        walletPayment = in.readDouble();
        billAmount = in.readDouble();
    }

    public static final Creator<CustomerDetailReportBean> CREATOR = new Creator<CustomerDetailReportBean>() {
        @Override
        public CustomerDetailReportBean createFromParcel(Parcel in) {
            return new CustomerDetailReportBean(in);
        }

        @Override
        public CustomerDetailReportBean[] newArray(int size) {
            return new CustomerDetailReportBean[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(int billNumber) {
        this.billNumber = billNumber;
    }

    public int getTotalBills() {
        return totalItems;
    }

    public void setTotalBills(int totalBills) {
        this.totalItems = totalBills;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
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

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(date);
        parcel.writeInt(billNumber);
        parcel.writeInt(totalItems);
        parcel.writeDouble(discount);
        parcel.writeDouble(cashAmount);
        parcel.writeDouble(cardPayment);
        parcel.writeDouble(couponPayment);
        parcel.writeDouble(pettyCashPayment);
        parcel.writeDouble(walletPayment);
        parcel.writeDouble(billAmount);
    }
}
