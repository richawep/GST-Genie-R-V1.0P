package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 24-01-2018.
 */

public class TaxReportBean implements Parcelable {

    private int Sno;
    private String description;
    private double taxPercent;
    private double taxAmount;
    private double taxableAmount;

    public TaxReportBean() {
    }

    protected TaxReportBean(Parcel in) {
        Sno = in.readInt();
        description = in.readString();
        taxPercent = in.readDouble();
        taxAmount = in.readDouble();
        taxableAmount = in.readDouble();
    }

    public static final Creator<TaxReportBean> CREATOR = new Creator<TaxReportBean>() {
        @Override
        public TaxReportBean createFromParcel(Parcel in) {
            return new TaxReportBean(in);
        }

        @Override
        public TaxReportBean[] newArray(int size) {
            return new TaxReportBean[size];
        }
    };

    public int getSno() {
        return Sno;
    }

    public void setSno(int sno) {
        Sno = sno;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(double taxPercent) {
        this.taxPercent = taxPercent;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(double taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(Sno);
        parcel.writeString(description);
        parcel.writeDouble(taxPercent);
        parcel.writeDouble(taxAmount);
        parcel.writeDouble(taxableAmount);
    }
}
