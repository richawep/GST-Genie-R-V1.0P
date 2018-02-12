/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	Category
 * 
 * Purpose			:	Represents Category table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package pos.wepindia.com.wepbase.model.pojos;

public class Category {

    int iID;
    // Private variables
    String strCategName;
    int iCategCode;
    int iDeptCode;

    // Default constructor
    public Category() {
        this.strCategName = "";
        this.iCategCode = 0;
        this.iDeptCode = 0;
    }

    // Parameterized construcor
    public Category(String CategName, int CategCode, int DeptCode) {
        this.strCategName = CategName;
        this.iCategCode = CategCode;
        this.iDeptCode = DeptCode;
    }

    public int getiID() {
        return iID;
    }

    public void setiID(int iID) {
        this.iID = iID;
    }

    public String getStrCategName() {
        return strCategName;
    }

    public void setStrCategName(String strCategName) {
        this.strCategName = strCategName;
    }

    public int getiCategCode() {
        return iCategCode;
    }

    public void setiCategCode(int iCategCode) {
        this.iCategCode = iCategCode;
    }

    public int getiDeptCode() {
        return iDeptCode;
    }

    public void setiDeptCode(int iDeptCode) {
        this.iDeptCode = iDeptCode;
    }
}
