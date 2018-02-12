package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 25-01-2018.
 */

public class ItemWiseReportBean implements Parcelable {

    private int itemNo;
    private String itemName;
    private double soldQuantity;
    private double discount;
    private double tax;
    private double taxableValue;
    private double modifierAmount;

    public ItemWiseReportBean() {
    }

    protected ItemWiseReportBean(Parcel in) {
        itemNo = in.readInt();
        itemName = in.readString();
        soldQuantity = in.readDouble();
        discount = in.readDouble();
        tax = in.readDouble();
        taxableValue = in.readDouble();
        modifierAmount = in.readDouble();
    }

    public static final Creator<ItemWiseReportBean> CREATOR = new Creator<ItemWiseReportBean>() {
        @Override
        public ItemWiseReportBean createFromParcel(Parcel in) {
            return new ItemWiseReportBean(in);
        }

        @Override
        public ItemWiseReportBean[] newArray(int size) {
            return new ItemWiseReportBean[size];
        }
    };

    public int getItemNo() {
        return itemNo;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(double soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(double taxableValue) {
        this.taxableValue = taxableValue;
    }

    public double getModifierAmount() {
        return modifierAmount;
    }

    public void setModifierAmount(double modifierAmount) {
        this.modifierAmount = modifierAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(itemNo);
        parcel.writeString(itemName);
        parcel.writeDouble(soldQuantity);
        parcel.writeDouble(discount);
        parcel.writeDouble(tax);
        parcel.writeDouble(taxableValue);
        parcel.writeDouble(modifierAmount);
    }
}
