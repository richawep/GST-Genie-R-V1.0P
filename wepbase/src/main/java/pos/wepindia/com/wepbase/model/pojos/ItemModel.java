package pos.wepindia.com.wepbase.model.pojos;

import java.io.Serializable;

/**
 * Created by Administrator on 02-01-2018.
 */

public class ItemModel implements Serializable{

    private int _id;
    private int shortCode;
    private String shortName;
    private String longName;
    private String barCode;
    private String UOM;
    private int brandCode;
    private int deptCode;
    private int categCode;
    private double retaiPrice;
    private double mrp;
    private double wholeSalePrice;
    private double quantity;
    private String hsn;
    private double cgstRate, sgstRate,igstRate,cessRate;
    private int fav;
    private int active;
    private String imageURL;
    private double discountPercent;
    private double discountAmount;
    private String supplyType;


    public ItemModel() {
        this._id = -1;
        this.shortCode = 0;
        this.shortName = "";
        this.longName = "";
        this.barCode = "";
        this.UOM = "";
        this.brandCode = -1;
        this.deptCode = -1;
        this.categCode = -1;
        this.retaiPrice = 0.00;
        this.mrp = 0.00;
        this.wholeSalePrice = 0.00;
        this.quantity = 0.00;
        this.hsn = "";
        this.cgstRate = 0.00;
        this.sgstRate = 0.00;
        this.igstRate = 0.00;
        this.cessRate = 0.00;
        this.fav = 0;
        this.active = 0;
        this.imageURL ="";
        this.discountPercent=0.00;
        this.discountAmount =0.00;
        this.supplyType="G";

    }

    public ItemModel(int shortCode, String shortName, String longName, String barCode, String UOM, int brandCode,
                     int deptCode, int categCode, double retaiPrice, double mrp, double getWholeSalePrice, double quantity,
                     String hsn, double cgstRate, double sgstRate, double igstRate, double cessRate, int fav, int active,String imageURL,double discountPercent,double discountAmount) {
        this.shortCode = shortCode;
        this.shortName = shortName;
        this.longName = longName;
        this.barCode = barCode;
        this.UOM = UOM;
        this.brandCode = brandCode;
        this.deptCode = deptCode;
        this.categCode = categCode;
        this.retaiPrice = retaiPrice;
        this.mrp = mrp;
        this.wholeSalePrice = getWholeSalePrice;
        this.quantity = quantity;
        this.hsn = hsn;
        this.cgstRate = cgstRate;
        this.sgstRate = sgstRate;
        this.igstRate = igstRate;
        this.cessRate = cessRate;
        this.fav = fav;
        this.active = active;
        this.imageURL=imageURL;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.supplyType="G";
    }

    public String getSupplyType() {
        return supplyType;
    }

    public void setSupplyType(String supplyType) {
        this.supplyType = supplyType;
    }

    public int getShortCode() {
        return shortCode;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getUOM() {
        return UOM;
    }

    public int getBrandCode() {
        return brandCode;
    }

    public int getDeptCode() {
        return deptCode;
    }

    public int getCategCode() {
        return categCode;
    }

    public double getRetaiPrice() {
        return retaiPrice;
    }

    public double getMrp() {
        return mrp;
    }

    public double getWholeSalePrice() {
        return wholeSalePrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getHsn() {
        return hsn;
    }

    public double getCgstRate() {
        return cgstRate;
    }

    public double getSgstRate() {
        return sgstRate;
    }

    public double getIgstRate() {
        return igstRate;
    }

    public double getCessRate() {
        return cessRate;
    }

    public int getFav() {
        return fav;
    }

    public int getActive() {
        return active;
    }

    public String getImageURL() {
        return imageURL;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setShortCode(int shortCode) {
        this.shortCode = shortCode;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public void setBrandCode(int brandCode) {
        this.brandCode = brandCode;
    }

    public void setDeptCode(int deptCode) {
        this.deptCode = deptCode;
    }

    public void setCategCode(int categCode) {
        this.categCode = categCode;
    }

    public void setRetaiPrice(double retaiPrice) {
        this.retaiPrice = retaiPrice;
    }

    public void setMrp(double mrp) {
        this.mrp = mrp;
    }

    public void setWholeSalePrice(double wholeSalePrice) {
        this.wholeSalePrice = wholeSalePrice;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setHsn(String hsn) {
        this.hsn = hsn;
    }

    public void setCgstRate(double cgstRate) {
        this.cgstRate = cgstRate;
    }

    public void setSgstRate(double sgstRate) {
        this.sgstRate = sgstRate;
    }

    public void setIgstRate(double igstRate) {
        this.igstRate = igstRate;
    }

    public void setCessRate(double cessRate) {
        this.cessRate = cessRate;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
