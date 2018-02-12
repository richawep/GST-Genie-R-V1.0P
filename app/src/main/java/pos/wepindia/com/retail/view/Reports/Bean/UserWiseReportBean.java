package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SachinV on 25-01-2018.
 */

public class UserWiseReportBean implements Parcelable {

    private int userCode;
    private String name;
    private int totalBills;
    private double billAmount;

    public UserWiseReportBean() {
    }

    protected UserWiseReportBean(Parcel in) {
        userCode = in.readInt();
        name = in.readString();
        totalBills = in.readInt();
        billAmount = in.readDouble();
    }

    public static final Creator<UserWiseReportBean> CREATOR = new Creator<UserWiseReportBean>() {
        @Override
        public UserWiseReportBean createFromParcel(Parcel in) {
            return new UserWiseReportBean(in);
        }

        @Override
        public UserWiseReportBean[] newArray(int size) {
            return new UserWiseReportBean[size];
        }
    };

    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
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
        parcel.writeInt(userCode);
        parcel.writeString(name);
        parcel.writeInt(totalBills);
        parcel.writeDouble(billAmount);
    }
}
