/*
 * **************************************************************************
 * Project Name		:	VAJRA
 * 
 * File Name		:	BillSetting
 * 
 * Purpose			:	Represents BillSetting table, takes care of intializing
 * 						assining and returning values of all the variables.
 * 
 * DateOfCreation	:	15-October-2012
 * 
 * Author			:	Balasubramanya Bharadwaj B S
 * 
 * **************************************************************************
 */

package pos.wepindia.com.wepbase.model.database;

public class BillSetting {

	// Private variables
	String strBusinessDate, strHeaderText, strFooterText;
	int billAmountRounfOff;
	int iLoginWith, iPeripherals;
	int iDateAndTime, iPriceChange, iBillwithStock, iTax, iDiscountType;
	int iRestoreDefault;
	int fastBillingMode, iItemNoReset;
	int CummulativeHeadingEnable ; // richa_2012
	int PrintOwnerDetail, BoldHeader, PrintService;
	int POS, HSNCode;
    int UTGSTEnabled_out , HSNPrintenabled_out;
    int iBillNoDailyReset;
	int iPrintDiscountDifference;

	public int getiPrintDiscountDifference() {
		return iPrintDiscountDifference;
	}

	public void setiPrintDiscountDifference(int iPrintDiscountDifference) {
		this.iPrintDiscountDifference = iPrintDiscountDifference;
	}

	public int getiLoyaltyPoints() {
		return iLoyaltyPoints;
	}

	public void setiLoyaltyPoints(int iLoyaltyPoints) {
		this.iLoyaltyPoints = iLoyaltyPoints;
	}

	int iLoyaltyPoints;
    int iSalesManId;

	public int getiSalesManId() {
		return iSalesManId;
	}

	public void setiSalesManId(int iSalesManId) {
		this.iSalesManId = iSalesManId;
	}

	public int getiBillNoDailyReset() {
		return iBillNoDailyReset;
	}

	public void setiBillNoDailyReset(int iBillNoDailyReset) {
		this.iBillNoDailyReset = iBillNoDailyReset;
	}

	public String getStrBusinessDate() {
		return strBusinessDate;
	}

	public void setStrBusinessDate(String strBusinessDate) {
		this.strBusinessDate = strBusinessDate;
	}

	public String getStrHeaderText() {
		return strHeaderText;
	}

	public void setStrHeaderText(String strHeaderText) {
		this.strHeaderText = strHeaderText;
	}

	public String getStrFooterText() {
		return strFooterText;
	}

	public void setStrFooterText(String strFooterText) {
		this.strFooterText = strFooterText;
	}

	public int getBillAmountRounfOff() {
		return billAmountRounfOff;
	}

	public void setBillAmountRounfOff(int billAmountRounfOff) {
		this.billAmountRounfOff = billAmountRounfOff;
	}

	public int getiLoginWith() {
		return iLoginWith;
	}

	public void setiLoginWith(int iLoginWith) {
		this.iLoginWith = iLoginWith;
	}

	public int getiPeripherals() {
		return iPeripherals;
	}

	public void setiPeripherals(int iPeripherals) {
		this.iPeripherals = iPeripherals;
	}

	public int getiDateAndTime() {
		return iDateAndTime;
	}

	public void setiDateAndTime(int iDateAndTime) {
		this.iDateAndTime = iDateAndTime;
	}

	public int getiPriceChange() {
		return iPriceChange;
	}

	public void setiPriceChange(int iPriceChange) {
		this.iPriceChange = iPriceChange;
	}

	public int getiBillwithStock() {
		return iBillwithStock;
	}

	public void setiBillwithStock(int iBillwithStock) {
		this.iBillwithStock = iBillwithStock;
	}

	public int getiTax() {
		return iTax;
	}

	public void setiTax(int iTax) {
		this.iTax = iTax;
	}

	public int getiDiscountType() {
		return iDiscountType;
	}

	public void setiDiscountType(int iDiscountType) {
		this.iDiscountType = iDiscountType;
	}

	public int getiRestoreDefault() {
		return iRestoreDefault;
	}

	public void setiRestoreDefault(int iRestoreDefault) {
		this.iRestoreDefault = iRestoreDefault;
	}

	public int getFastBillingMode() {
		return fastBillingMode;
	}

	public void setFastBillingMode(int fastBillingMode) {
		this.fastBillingMode = fastBillingMode;
	}

	public int getiItemNoReset() {
		return iItemNoReset;
	}

	public void setiItemNoReset(int iItemNoReset) {
		this.iItemNoReset = iItemNoReset;
	}

	public int getCummulativeHeadingEnable() {
		return CummulativeHeadingEnable;
	}

	public void setCummulativeHeadingEnable(int cummulativeHeadingEnable) {
		CummulativeHeadingEnable = cummulativeHeadingEnable;
	}

	public int getPrintOwnerDetail() {
		return PrintOwnerDetail;
	}

	public void setPrintOwnerDetail(int printOwnerDetail) {
		PrintOwnerDetail = printOwnerDetail;
	}

	public int getBoldHeader() {
		return BoldHeader;
	}

	public void setBoldHeader(int boldHeader) {
		BoldHeader = boldHeader;
	}

	public int getPrintService() {
		return PrintService;
	}

	public void setPrintService(int printService) {
		PrintService = printService;
	}

	public int getPOS() {
		return POS;
	}

	public void setPOS(int POS) {
		this.POS = POS;
	}

	public int getHSNCode() {
		return HSNCode;
	}

	public void setHSNCode(int HSNCode) {
		this.HSNCode = HSNCode;
	}

	public int getUTGSTEnabled_out() {
		return UTGSTEnabled_out;
	}

	public void setUTGSTEnabled_out(int UTGSTEnabled_out) {
		this.UTGSTEnabled_out = UTGSTEnabled_out;
	}

	public int getHSNPrintenabled_out() {
		return HSNPrintenabled_out;
	}

	public void setHSNPrintenabled_out(int HSNPrintenabled_out) {
		this.HSNPrintenabled_out = HSNPrintenabled_out;
	}
}
