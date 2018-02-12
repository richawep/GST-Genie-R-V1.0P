package pos.wepindia.com.wepbase.model.pojos;

/**
 * Created by MohanN on 12/18/2017.
 */

public class AddRoleBean {
    private int _id;
    private int iRoleId;
    private int iRoleAccessId;
    private String strData;
    private boolean status;

    public AddRoleBean(){

    }

    public AddRoleBean(String data, boolean status){
        this.strData = data;
        this.status = status;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getiRoleId() {
        return iRoleId;
    }

    public void setiRoleId(int iRoleId) {
        this.iRoleId = iRoleId;
    }

    public String getStrData() {
        return strData;
    }

    public void setStrData(String strData) {
        this.strData = strData;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getiRoleAccessId() {
        return iRoleAccessId;
    }

    public void setiRoleAccessId(int iRoleAccessId) {
        this.iRoleAccessId = iRoleAccessId;
    }
}
