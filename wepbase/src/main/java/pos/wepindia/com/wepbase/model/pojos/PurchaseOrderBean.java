package pos.wepindia.com.wepbase.model.pojos;

/**
 * Created by MohanN on 1/5/2018.
 */

public class PurchaseOrderBean {

    private int _id, iMenuCode,iIsgoodInward;
    private String strPurchaseOrderNo, strInvoiceNo, strInvoiceDate, strSupplierCode, strSupplierName,
            strSupplierPhone, strSupplierAddress, strSupplierGSTIN, strSupplierType, strSupplyType,
            strBarcode, strHSNCode, strItemName, strUOM, strAdditionalCharge, strSupplierPOS;
    private double dblRate, dblMRP, dblRetailPrice, dblWholeSalePrice, dblValue, dblQuantity, dblTaxableValue, dblIGSTRate, dblIGSTAmount, dblCGSTRate,
            dblCGSTAmount, dblSGSTRate, dblSGSTAmount, dblCessRate, dblCessAmount, dblSalesTaxAmount,
            dblServiceTaxAmount, dblAmount, dblAdditionalChargeAmount;
    private boolean bSupplierStateCodeStatus;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getiMenuCode() {
        return iMenuCode;
    }

    public void setiMenuCode(int iMenuCode) {
        this.iMenuCode = iMenuCode;
    }

    public int getiIsgoodInward() {
        return iIsgoodInward;
    }

    public void setiIsgoodInward(int iIsgoodInward) {
        this.iIsgoodInward = iIsgoodInward;
    }

    public String getStrPurchaseOrderNo() {
        return strPurchaseOrderNo;
    }

    public void setStrPurchaseOrderNo(String strPurchaseOrderNo) {
        this.strPurchaseOrderNo = strPurchaseOrderNo;
    }

    public String getStrInvoiceNo() {
        return strInvoiceNo;
    }

    public void setStrInvoiceNo(String strInvoiceNo) {
        this.strInvoiceNo = strInvoiceNo;
    }

    public String getStrInvoiceDate() {
        return strInvoiceDate;
    }

    public void setStrInvoiceDate(String strInvoiceDate) {
        this.strInvoiceDate = strInvoiceDate;
    }

    public String getStrSupplierCode() {
        return strSupplierCode;
    }

    public void setStrSupplierCode(String strSupplierCode) {
        this.strSupplierCode = strSupplierCode;
    }

    public String getStrSupplierName() {
        return strSupplierName;
    }

    public void setStrSupplierName(String strSupplierName) {
        this.strSupplierName = strSupplierName;
    }

    public String getStrSupplierPhone() {
        return strSupplierPhone;
    }

    public void setStrSupplierPhone(String strSupplierPhone) {
        this.strSupplierPhone = strSupplierPhone;
    }

    public String getStrSupplierAddress() {
        return strSupplierAddress;
    }

    public void setStrSupplierAddress(String strSupplierAddress) {
        this.strSupplierAddress = strSupplierAddress;
    }

    public String getStrSupplierGSTIN() {
        return strSupplierGSTIN;
    }

    public void setStrSupplierGSTIN(String strSupplierGSTIN) {
        this.strSupplierGSTIN = strSupplierGSTIN;
    }

    public String getStrSupplierType() {
        return strSupplierType;
    }

    public void setStrSupplierType(String strSupplierType) {
        this.strSupplierType = strSupplierType;
    }

    public String getStrSupplyType() {
        return strSupplyType;
    }

    public void setStrSupplyType(String strSupplyType) {
        this.strSupplyType = strSupplyType;
    }

    public String getStrHSNCode() {
        return strHSNCode;
    }

    public void setStrHSNCode(String strHSNCode) {
        this.strHSNCode = strHSNCode;
    }

    public String getStrItemName() {
        return strItemName;
    }

    public void setStrItemName(String strItemName) {
        this.strItemName = strItemName;
    }

    public String getStrUOM() {
        return strUOM;
    }

    public void setStrUOM(String strUOM) {
        this.strUOM = strUOM;
    }

    public String getStrAdditionalCharge() {
        return strAdditionalCharge;
    }

    public void setStrAdditionalCharge(String strAdditionalCharge) {
        this.strAdditionalCharge = strAdditionalCharge;
    }

    public String getStrSupplierPOS() {
        return strSupplierPOS;
    }

    public void setStrSupplierPOS(String strSupplierPOS) {
        this.strSupplierPOS = strSupplierPOS;
    }

    public double getDblValue() {
        return dblValue;
    }

    public void setDblValue(double dblValue) {
        this.dblValue = dblValue;
    }

    public double getDblQuantity() {
        return dblQuantity;
    }

    public void setDblQuantity(double dblQuantity) {
        this.dblQuantity = dblQuantity;
    }

    public double getDblTaxableValue() {
        return dblTaxableValue;
    }

    public void setDblTaxableValue(double dblTaxableValue) {
        this.dblTaxableValue = dblTaxableValue;
    }

    public double getDblIGSTRate() {
        return dblIGSTRate;
    }

    public void setDblIGSTRate(double dblIGSTRate) {
        this.dblIGSTRate = dblIGSTRate;
    }

    public double getDblIGSTAmount() {
        return dblIGSTAmount;
    }

    public void setDblIGSTAmount(double dblIGSTAmount) {
        this.dblIGSTAmount = dblIGSTAmount;
    }

    public double getDblCGSTRate() {
        return dblCGSTRate;
    }

    public void setDblCGSTRate(double dblCGSTRate) {
        this.dblCGSTRate = dblCGSTRate;
    }

    public double getDblCGSTAmount() {
        return dblCGSTAmount;
    }

    public void setDblCGSTAmount(double dblCGSTAmount) {
        this.dblCGSTAmount = dblCGSTAmount;
    }

    public double getDblSGSTRate() {
        return dblSGSTRate;
    }

    public void setDblSGSTRate(double dblSGSTRate) {
        this.dblSGSTRate = dblSGSTRate;
    }

    public double getDblSGSTAmount() {
        return dblSGSTAmount;
    }

    public void setDblSGSTAmount(double dblSGSTAmount) {
        this.dblSGSTAmount = dblSGSTAmount;
    }

    public double getDblCessRate() {
        return dblCessRate;
    }

    public void setDblCessRate(double dblCessRate) {
        this.dblCessRate = dblCessRate;
    }

    public double getDblCessAmount() {
        return dblCessAmount;
    }

    public void setDblCessAmount(double dblCessAmount) {
        this.dblCessAmount = dblCessAmount;
    }

    public double getDblSalesTaxAmount() {
        return dblSalesTaxAmount;
    }

    public void setDblSalesTaxAmount(double dblSalesTaxAmount) {
        this.dblSalesTaxAmount = dblSalesTaxAmount;
    }

    public double getDblServiceTaxAmount() {
        return dblServiceTaxAmount;
    }

    public void setDblServiceTaxAmount(double dblServiceTaxAmount) {
        this.dblServiceTaxAmount = dblServiceTaxAmount;
    }

    public double getDblAmount() {
        return dblAmount;
    }

    public void setDblAmount(double dblAmount) {
        this.dblAmount = dblAmount;
    }

    public double getDblAdditionalChargeAmount() {
        return dblAdditionalChargeAmount;
    }

    public void setDblAdditionalChargeAmount(double dblAdditionalChargeAmount) {
        this.dblAdditionalChargeAmount = dblAdditionalChargeAmount;
    }

    public String getStrBarcode() {
        return strBarcode;
    }

    public void setStrBarcode(String strBarcode) {
        this.strBarcode = strBarcode;
    }

    public double getDblMRP() {
        return dblMRP;
    }

    public void setDblMRP(double dblMRP) {
        this.dblMRP = dblMRP;
    }

    public double getDblRetailPrice() {
        return dblRetailPrice;
    }

    public void setDblRetailPrice(double dblRetailPrice) {
        this.dblRetailPrice = dblRetailPrice;
    }

    public double getDblWholeSalePrice() {
        return dblWholeSalePrice;
    }

    public void setDblWholeSalePrice(double dblWholeSalePrice) {
        this.dblWholeSalePrice = dblWholeSalePrice;
    }

    public boolean isbSupplierStateCodeStatus() {
        return bSupplierStateCodeStatus;
    }

    public void setbSupplierStateCodeStatus(boolean bSupplierStateCodeStatus) {
        this.bSupplierStateCodeStatus = bSupplierStateCodeStatus;
    }

    public double getDblRate() {
        return dblRate;
    }

    public void setDblRate(double dblRate) {
        this.dblRate = dblRate;
    }
}
