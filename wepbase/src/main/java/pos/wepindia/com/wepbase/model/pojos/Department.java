/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	Department
 * 
 * Purpose			:	Represents Department table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package pos.wepindia.com.wepbase.model.pojos;

public class Department {

    // Private variables
    int iID;
    String strDeptName;
    int iDeptCode;

    // Default constructor
    public Department() {
        this.strDeptName = "";
        this.iDeptCode = 0;
    }

    // Parameterized construcor
    public Department(String DeptName, int DeptCode) {
        this.strDeptName = DeptName;
        this.iDeptCode = DeptCode;
    }

    // getting DeptName
    public String getDeptName() {
        return this.strDeptName;
    }

    // getting DeptCode
    public int getDeptCode() {
        return this.iDeptCode;
    }

    // setting DeptName
    public void setDeptName(String DeptName) {
        this.strDeptName = DeptName;
    }

    // setting DeptCode
    public void setDeptCode(int DeptCode) {
        this.iDeptCode = DeptCode;
    }

    public int getiID() {
        return iID;
    }

    public void setiID(int iID) {
        this.iID = iID;
    }

    public String toString()
    {
        return this.strDeptName;
    }
}
