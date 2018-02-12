package pos.wepindia.com.wepbase.model.pojos;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MohanN on 12/29/2017.
 */

public class MasterBean implements Parcelable {

    public String strData;
    public int iDrawable;

    public MasterBean(String strData, int iDrawable )
    {
        this.strData=strData;
        this.iDrawable=iDrawable;
    }

    protected MasterBean(Parcel in) {
        strData = in.readString();
        iDrawable = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(strData);
        dest.writeInt(iDrawable);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MasterBean> CREATOR = new Creator<MasterBean>() {
        @Override
        public MasterBean createFromParcel(Parcel in) {
            return new MasterBean(in);
        }

        @Override
        public MasterBean[] newArray(int size) {
            return new MasterBean[size];
        }
    };
}