package pos.wepindia.com.wepbase.model.pojos;

/**
 * Created by MohanN on 1/3/2018.
 */

public class AccessPermissionRoleBean {

    private int _id, iRoleId, iRoleAccessId;
    private String strAccessName;

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

    public int getiRoleAccessId() {
        return iRoleAccessId;
    }

    public void setiRoleAccessId(int iRoleAccessId) {
        this.iRoleAccessId = iRoleAccessId;
    }

    public String getStrAccessName() {
        return strAccessName;
    }

    public void setStrAccessName(String strAccessName) {
        this.strAccessName = strAccessName;
    }
}
