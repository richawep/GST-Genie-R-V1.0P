package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1DocIssuedBean implements Parcelable {

    private int sno;
    private String natureOfDoc;
    private String from;
    private String to;
    private int totalNumber;
    private int cancelled;
    private int netIssued;

    public GSTR1DocIssuedBean() {
    }

    protected GSTR1DocIssuedBean(Parcel in) {
        sno = in.readInt();
        natureOfDoc = in.readString();
        from = in.readString();
        to = in.readString();
        totalNumber = in.readInt();
        cancelled = in.readInt();
        netIssued = in.readInt();
    }

    public static final Creator<GSTR1DocIssuedBean> CREATOR = new Creator<GSTR1DocIssuedBean>() {
        @Override
        public GSTR1DocIssuedBean createFromParcel(Parcel in) {
            return new GSTR1DocIssuedBean(in);
        }

        @Override
        public GSTR1DocIssuedBean[] newArray(int size) {
            return new GSTR1DocIssuedBean[size];
        }
    };

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public String getNatureOfDoc() {
        return natureOfDoc;
    }

    public void setNatureOfDoc(String natureOfDoc) {
        this.natureOfDoc = natureOfDoc;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }

    public int getNetIssued() {
        return netIssued;
    }

    public void setNetIssued(int netIssued) {
        this.netIssued = netIssued;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(sno);
        parcel.writeString(natureOfDoc);
        parcel.writeString(from);
        parcel.writeString(to);
        parcel.writeInt(totalNumber);
        parcel.writeInt(cancelled);
        parcel.writeInt(netIssued);
    }
}
