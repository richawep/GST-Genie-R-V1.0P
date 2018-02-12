package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 24-01-2018.
 */

public class TransactionReportBean implements Parcelable {

    private String invoiceDate;
    private int billNumber;
    private int items;
    private double billAmount;
    private double cashAmount;
    private double cardPayment;
    private double couponPayment;
    private double pettyCashPayment;
    private double walletPayment;
    private double mSwipePayment;
    private double aepsPayment;

    public TransactionReportBean() {
    }

    protected TransactionReportBean(Parcel in) {
        invoiceDate = in.readString();
        billNumber = in.readInt();
        items = in.readInt();
        billAmount = in.readDouble();
        cashAmount = in.readDouble();
        cardPayment = in.readDouble();
        couponPayment = in.readDouble();
        pettyCashPayment = in.readDouble();
        walletPayment = in.readDouble();
        mSwipePayment = in.readDouble();
        aepsPayment = in.readDouble();
    }

    public static final Creator<TransactionReportBean> CREATOR = new Creator<TransactionReportBean>() {
        @Override
        public TransactionReportBean createFromParcel(Parcel in) {
            return new TransactionReportBean(in);
        }

        @Override
        public TransactionReportBean[] newArray(int size) {
            return new TransactionReportBean[size];
        }
    };

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public int getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(int billNumber) {
        this.billNumber = billNumber;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
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

    public double getmSwipePayment() {
        return mSwipePayment;
    }

    public void setmSwipePayment(double mSwipePayment) {
        this.mSwipePayment = mSwipePayment;
    }

    public double getAepsPayment() {
        return aepsPayment;
    }

    public void setAepsPayment(double aepsPayment) {
        this.aepsPayment = aepsPayment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(invoiceDate);
        parcel.writeInt(billNumber);
        parcel.writeInt(items);
        parcel.writeDouble(billAmount);
        parcel.writeDouble(cashAmount);
        parcel.writeDouble(cardPayment);
        parcel.writeDouble(couponPayment);
        parcel.writeDouble(pettyCashPayment);
        parcel.writeDouble(walletPayment);
        parcel.writeDouble(mSwipePayment);
        parcel.writeDouble(aepsPayment);
    }
}
