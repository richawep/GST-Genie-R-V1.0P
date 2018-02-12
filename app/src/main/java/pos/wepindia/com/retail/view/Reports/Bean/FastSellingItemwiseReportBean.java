package pos.wepindia.com.retail.view.Reports.Bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Comparator;

/**
 * Created by SachinV on 25-01-2018.
 */

public class FastSellingItemwiseReportBean implements Parcelable  {

    private int sno;
    private int menuCode;
    private String deptName;
    private String categName;
    private String itemName;
    private double quantity;
    private double totalPrice;

    public FastSellingItemwiseReportBean() {
    }

    protected FastSellingItemwiseReportBean(Parcel in) {
        sno = in.readInt();
        menuCode = in.readInt();
        deptName = in.readString();
        categName = in.readString();
        itemName = in.readString();
        quantity = in.readDouble();
        totalPrice = in.readDouble();
    }

    public static final Creator<FastSellingItemwiseReportBean> CREATOR = new Creator<FastSellingItemwiseReportBean>() {
        @Override
        public FastSellingItemwiseReportBean createFromParcel(Parcel in) {
            return new FastSellingItemwiseReportBean(in);
        }

        @Override
        public FastSellingItemwiseReportBean[] newArray(int size) {
            return new FastSellingItemwiseReportBean[size];
        }
    };

    public int getSno() {
        return sno;
    }

    public void setSno(int sno) {
        this.sno = sno;
    }

    public int getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(int menuCode) {
        this.menuCode = menuCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getCategName() {
        return categName;
    }

    public void setCategName(String categName) {
        this.categName = categName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public static Creator<FastSellingItemwiseReportBean> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(sno);
        parcel.writeInt(menuCode);
        parcel.writeString(deptName);
        parcel.writeString(categName);
        parcel.writeString(itemName);
        parcel.writeDouble(quantity);
        parcel.writeDouble(totalPrice);
    }


    /*@Override
    public int compareTo(@NonNull Object recievedObj) {
        int result ;
        double receivedQty = ((FastSellingItemwiseReportBean)recievedObj).getQuantity();
        if((this.quantity - receivedQty) >0)
            result =1;
        else
            result =0;
        return result;
    }
*/
    public static Comparator<FastSellingItemwiseReportBean> QuantityComparator = new Comparator<FastSellingItemwiseReportBean>() {
        @Override
        public int compare(FastSellingItemwiseReportBean o1, FastSellingItemwiseReportBean o2) {

            int result =0;
            double q1 = o1.getQuantity();
            double q2 = o2.getQuantity();
            //ascending order
            if((q1-q2) >0)
                result =-1;
            else if((q1-q2) ==0)
                result =0;
            else
                result =1;

            return result;
        }
    };
}
