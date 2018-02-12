package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 01-02-2018.
 */

public class SalesmanDetailReportBean implements Parcelable {

   private String salesManId;
   private String salesmanName;
   private int invoiceNo;
   private String invoiceDate;
   private double taxableValue;
   private double billAmount;


    protected SalesmanDetailReportBean(Parcel in) {
        salesManId = in.readString();
        salesmanName = in.readString();
        invoiceNo = in.readInt();
        invoiceDate = in.readString();
        taxableValue = in.readDouble();
        billAmount = in.readDouble();
    }

    public static final Creator<SalesmanDetailReportBean> CREATOR = new Creator<SalesmanDetailReportBean>() {
        @Override
        public SalesmanDetailReportBean createFromParcel(Parcel in) {
            return new SalesmanDetailReportBean(in);
        }

        @Override
        public SalesmanDetailReportBean[] newArray(int size) {
            return new SalesmanDetailReportBean[size];
        }
    };

    public String getSalesManId() {
        return salesManId;
    }

    public void setSalesManId(String salesManId) {
        this.salesManId = salesManId;
    }

    public String getSalesmanName() {
        return salesmanName;
    }

    public void setSalesmanName(String salesmanName) {
        this.salesmanName = salesmanName;
    }

    public int getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(int invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public double getTaxableValue() {
        return taxableValue;
    }

    public void setTaxableValue(double taxableValue) {
        this.taxableValue = taxableValue;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }

    public SalesmanDetailReportBean() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(salesManId);
        parcel.writeString(salesmanName);
        parcel.writeInt(invoiceNo);
        parcel.writeString(invoiceDate);
        parcel.writeDouble(taxableValue);
        parcel.writeDouble(billAmount);
    }
}
