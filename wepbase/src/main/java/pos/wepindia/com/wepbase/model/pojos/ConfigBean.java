package pos.wepindia.com.wepbase.model.pojos;

/**
 * Created by MohanN on 12/26/2017.
 */

public class ConfigBean {

    String strDeptName;
    String strCategName;
    String strBrandName;
    String strDescription;
    String strModes;
    double dblAmount, dblDiscount;
    int lDeptCode;
    int lCategCode;
    int lBrandCode, ldescripId;
    int iID, iConfigMode, iChargeable;

    public int getLdescripId() {
        return ldescripId;
    }

    public void setLdescripId(int ldescripId) {
        this.ldescripId = ldescripId;
    }

    public String getStrBrandName() {
        return strBrandName;
    }

    public void setStrBrandName(String strBrandName) {
        this.strBrandName = strBrandName;
    }

    public int getlBrandCode() {
        return lBrandCode;
    }

    public void setlBrandCode(int lBrandCode) {
        this.lBrandCode = lBrandCode;
    }

    public int getiConfigMode() {
        return iConfigMode;
    }

    public void setiConfigMode(int iConfigMode) {
        this.iConfigMode = iConfigMode;
    }

    public String getStrDeptName() {
        return strDeptName;
    }

    public void setStrDeptName(String strDeptName) {
        this.strDeptName = strDeptName;
    }

    public String getStrCategName() {
        return strCategName;
    }

    public void setStrCategName(String strCategName) {
        this.strCategName = strCategName;
    }

    public String getStrDescription() {
        return strDescription;
    }

    public void setStrDescription(String strDescription) {
        this.strDescription = strDescription;
    }

    public String getStrModes() {
        return strModes;
    }

    public void setStrModes(String strModes) {
        this.strModes = strModes;
    }

    public double getDblAmount() {
        return dblAmount;
    }

    public void setDblAmount(double dblAmount) {
        this.dblAmount = dblAmount;
    }

    public double getDblDiscount() {
        return dblDiscount;
    }

    public void setDblDiscount(double dblDiscount) {
        this.dblDiscount = dblDiscount;
    }

    public int getlDeptCode() {
        return lDeptCode;
    }

    public void setlDeptCode(int lDeptCode) {
        this.lDeptCode = lDeptCode;
    }

    public int getlCategCode() {
        return lCategCode;
    }

    public void setlCategCode(int lCategCode) {
        this.lCategCode = lCategCode;
    }

    public int getiID() {
        return iID;
    }

    public void setiID(int iID) {
        this.iID = iID;
    }

    public int getiChargeable() {
        return iChargeable;
    }

    public void setiChargeable(int iChargeable) {
        this.iChargeable = iChargeable;
    }
}
