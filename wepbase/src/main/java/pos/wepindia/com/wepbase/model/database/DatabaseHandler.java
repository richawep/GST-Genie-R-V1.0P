/***************************************************************************
 * Project Name		:	VAJRA
 * <p/>
 * File Name		:	DatabaseHandler
 * <p/>
 * DateOfCreation	:	13-October-2012
 * <p/>
 * Author			:	Balasubramanya Bharadwaj B S
 **************************************************************************/

package pos.wepindia.com.wepbase.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.pojos.AccessPermissionRoleBean;
import pos.wepindia.com.wepbase.model.pojos.AddRoleBean;
import pos.wepindia.com.wepbase.model.pojos.BillDetailBean;
import pos.wepindia.com.wepbase.model.pojos.BillItemBean;
import pos.wepindia.com.wepbase.model.pojos.Category;
import pos.wepindia.com.wepbase.model.pojos.ConfigBean;
import pos.wepindia.com.wepbase.model.pojos.Coupon;
import pos.wepindia.com.wepbase.model.pojos.Customer;
import pos.wepindia.com.wepbase.model.pojos.Department;
import pos.wepindia.com.wepbase.model.pojos.Description;
import pos.wepindia.com.wepbase.model.pojos.DiscountConfig;
import pos.wepindia.com.wepbase.model.pojos.HoldResumeBillBean;
import pos.wepindia.com.wepbase.model.pojos.ItemModel;
import pos.wepindia.com.wepbase.model.pojos.PurchaseOrderBean;
import pos.wepindia.com.wepbase.model.pojos.SupplierItemLinkageBean;
import pos.wepindia.com.wepbase.model.pojos.User;
import pos.wepindia.com.wepbase.model.print.Payment;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHandler.class.getSimpleName();

    // DatabaseVersion
    private static final int DB_VERSION = 1;
    String strDate = "";
    Date strDate_date;
    Calendar Time; // Time variable
    // Database path
    // "/sdcard/AppDatabase/"; //"/data/data/com.wepindia.pos/databases/";
    private static String DB_PATH = Environment.getExternalStorageDirectory().getPath() + "/WeP_Retail/";

    // Database backup path
    private static String DB_BACKUP_PATH = Environment.getExternalStorageDirectory().getPath() + "/WeP_Retail_Backup/";

    // Database name
    private static final String DB_NAME = "WeP_Retail_Database.db";

    // Database table names

    private static final String TBL_BILLSETTING = "BillSetting";
    private static final String TBL_BILLNORESETCONFIG = "BillNoConfiguration";
    private static final String TBL_CATEGORY = "Category";
    private static final String TBL_REWARDPOINTSCONGFIGURATION = "RewardPointsConfiguration";
    private static final String TBL_COUPON = "Coupon";
    private static final String TBL_CUSTOMER = "Customer";
    private static final String TBL_DEPARTMENT = "Department";
    private static final String TBL_DESCRIPTION = "Description";
    private static final String TBL_DISCOUNTCONFIG = "DiscountConfig";
    private static final String TBL_VOUCHERCONFIG = "VoucherConfig";
    private static final String TBL_TAXCONFIG = "TaxConfig";
    private static final String TBL_SUBTAXCONFIG = "TaxConfigSub";
    private static final String TBL_USER = "User";
    public static final String TBL_PaymentModeConfiguration = "PaymentModeConfiguration";
    private static final String TBL_PAYMENTRECEIPT = "PaymentReceipt";
    private static final String TBL_Item = "Item";
    private static final String TBL_PURCHASEORDER = "PurchaseOrder";
    public static final String TBL_HOLD_RESUME_BILL_LEDGER = "HoldResumeBill";
    public static final String TBL_OUTWARD_SUPPLY_ITEMS_DETAILS = "OutwardSuppyItemsDetails";
    public static final String TBL_OUTWARD_SUPPLY_LEDGER = "OutwardSupplyLedger";
    public static final String TBL_OUTWARD_SUPPLY_DELIVERY_CHARGES_DETAILS = "OutwardSupplyDeliveryChargesDetails";


    // Column Names for the tables
    public static final String KEY_AmtToPt = "AmtToPt";
    public static final String KEY_RewardPoints = "RewardPoints";
    public static final String KEY_RewardPointsAccumulated = "RewardPointsAccumulated";
    public static final String KEY_RewardPointsToAmt = "RewardPointsToAmt";
    public static final String KEY_RewardPointsLimit = "RewardPointsLimit";


    private static final String KEY_ServiceTaxPercent = "ServiceTaxPercent";
    public static final String KEY_TaxType = "TaxType";
    public static final String KEY_DiscountType = "DiscountType";
    public static final String KEY_FastBillingMode = "FastBillingMode";
    public static final String KEY_CategoryCode = "CategoryCode";
    public static final String KEY_DepartmentCode = "DepartmentCode";
    public static final String KEY_BrandCode = "BrandCode";
    public static final String KEY_CustId = "CustId";
    private static final String KEY_EmployeeId = "EmployeeId";
    private static final String KEY_SubUdfNumber = "SubUdfNumber";
    private static final String KEY_TableNumber = "TableNumber";
    public static final String KEY_DiscId = "DiscId";
    private static final String KEY_KitchenCode = "KitchenCode";
    private static final String KEY_ModifierAmount = "ModifierAmount";
    private static final String KEY_Reason = "Reason";
    public static final String KEY_Amount = "Amount";
    private static final String KEY_ServiceTaxAmount = "ServiceTaxAmount";
    public static final String KEY_DiscountAmount = "DiscountAmount";
    public static final String KEY_DiscountPercent = "DiscountPercent";
    public static final String KEY_ItemName = "ItemName";
    private static final String KEY_SalesTax = "SalesTax";
    private static final String KEY_OtherTax = "OtherTax";
    //public static final String KEY_ItemNumber = "ItemNumber";
    public static final String KEY_Quantity = "Quantity";
    private static final String KEY_Rate = "Rate";
    public static final String KEY_Time = "Time";
    private static final String KEY_TokenNumber = "TokenNumber";
    private static final String KEY_TokenNumber_SubNo = "TokenNumber_SubNo";
    private static final String KEY_Table_Split_No = "TableSplitNo";
    private static final String KEY_DeliveryCharge = "DeliveryCharge";
    public static final String KEY_BillAmount = "BillAmount";
    // private static final String KEY_InvoiceNo = "InvoiceNo";
    public static final String KEY_TotalItems = "TotalItems";
    private static final String KEY_UserId = "UserId";
    private static final String KEY_IngredientCode = "IngredientCode";
    private static final String KEY_IngredientName = "IngredientName";
    private static final String KEY_IngredientQuantity = "IngredientQuantity";
    private static final String KEY_IngredientUOM = "IngredientUOM";
    //private static final String KEY_InvoiceDate = "InvoiceDate";

    // BillDetail
    private static final String KEY_TotalServiceTaxAmount = "TotalServiceTaxAmount";
    public static final String KEY_BillStatus = "BillStatus";
    public static final String KEY_CardPayment = "CardPayment";
    public static final String KEY_CashPayment = "CashPayment";
    public static final String KEY_CouponPayment = "CouponPayment";
    public static final String KEY_WalletPayment = "WalletPayment";
    public static final String KEY_RewardPointsAmount = "RewardPointsAmount";
    public static final String KEY_AEPSAmount = "AEPSAmount";
    public static final String KEY_MSWIPE_Amount = "mSwipeAmount";
    public static final String KEY_ReprintCount = "ReprintCount";
    public static final String KEY_TotalDiscountAmount = "TotalDiscountAmount";
    private static final String KEY_TotalTaxAmount = "TotalTaxAmount";

    public static final String KEY_PettyCashPayment = "PettyCashPayment";
    public  static final String KEY_PaidTotalPayment = "PaidTotalPayment";
    public static final String KEY_ChangePayment = "ChangePayment";

    // BillItem
    public static final String KEY_TaxAmount = "TaxAmount";
    public static final String KEY_TaxPercent = "TaxPercent";

    // BillSetting
    private static final String KEY_DineIn3Caption = "DineIn3Caption";
    private static final String KEY_DineIn2Caption = "DineIn2Caption";
    private static final String KEY_DineIn1Caption = "DineIn1Caption";
    private static final String KEY_BillwithoutStock = "BillwithoutStock";
    private static final String KEY_WeighScale = "WeighScale";
    private static final String KEY_ServiceTaxType = "ServiceTaxType";
    public static final String KEY_BusinessDate = "BusinessDate";
    private static final String KEY_DineIn1From = "DineIn1From";
    private static final String KEY_DineIn1To = "DineIn1To";
    private static final String KEY_DineIn2From = "DineIn2From";
    private static final String KEY_DineIn2To = "DineIn2To";
    private static final String KEY_DineIn3From = "DineIn3From";
    private static final String KEY_DineIn3To = "DineIn3To";
    public static final String KEY_FooterText1 = "FooterText1";
    public static final String KEY_FooterText2 = "FooterText2";
    public static final String KEY_FooterText3 = "FooterText3";
    public static final String KEY_FooterText4 = "FooterText4";
    public static final String KEY_FooterText5 = "FooterText5";
    public static final String KEY_HeaderText1 = "HeaderText1";
    public static final String KEY_HeaderText2 = "HeaderText2";
    public static final String KEY_HeaderText3 = "HeaderText3";
    public static final String KEY_HeaderText4 = "HeaderText4";
    public static final String KEY_HeaderText5 = "HeaderText5";
    private static final String KEY_KOTType = "KOTType";
    private static final String KEY_MaximumTables = "MaximumTables";
    private static final String KEY_MaximumWaiters = "MaximumWaiters";
    private static final String KEY_POSNumber = "POSNumber";
    private static final String KEY_PrintKOT = "PrintKOT";
    private static final String KEY_SubUdfText = "SubUdfText";
    private static final String KEY_TIN = "TIN";
    private static final String KEY_ActiveForBilling = "ActiveForBilling";
    private static final String KEY_LoginWith = "LoginWith";
    public static final String KEY_DateAndTime = "DateAndTime";
    public static final String KEY_PriceChange = "PriceChange";
    public static final String KEY_BillwithStock = "BillwithStock";
    public static final String KEY_Tax = "Tax";
    private static final String KEY_KOT = "KOT";
    private static final String KEY_Token = "Token";
    private static final String KEY_Kitchen = "Kitchen";
    private static final String KEY_OtherChargesItemwise = "OtherChargesItemwise";
    private static final String KEY_OtherChargesBillwise = "OtherChargesBillwise";
    private static final String KEY_Peripherals = "Peripherals";
    private static final String KEY_RestoreDefault = "RestoreDefault";
    private static final String KEY_DineInRate = "DineInRate";
    private static final String KEY_CounterSalesRate = "CounterSalesRate";
    private static final String KEY_PickUpRate = "PickUpRate";
    private static final String KEY_HomeDeliveryRate = "HomeDeliveryRate";
    public static final String KEY_ShortCodeReset = "ShortCodeReset";
    public static final String KEY_BILL_NO_RESET = "bill_no_reset";
    public static final String KEY_CummulativeHeadingEnable = "CummulativeHeadingEnable"; // richa_2012
    private static final String KEY_TableSpliting = "TableSpliting";
    public static final String KEY_DYNAMIC_DISCOUNT = "DynamicDiscount";
    public static final String KEY_PRINT_MRP_RETAIL_DIFFERENCE = "PrintMRPRetailDifference";

    // Bill No Reset
    private static final String KEY_BillNoReset_InvoiceNo = "InvoiceNo";
    private static final String KEY_BillNoReset_Period = "Period";
    private static final String KEY_BillNoReset_PeriodDate = "PeriodDate";

    // Category
    public static final String KEY_CategoryName = "CategoryName";

    // Complimentary Bill Detail
    private static final String KEY_Complimentary_Reason = "ComplimentaryReason";
    private static final String KEY_Paid_Amount = "PaidAmount";

    // Department
    public static final String KEY_DepartmentName = "DepartmentName";
    public static final String KEY_BrandName = "BrandName";
    // Coupon
    public static final String KEY_CouponAmount = "CouponAmount";
    public static final String KEY_CouponBarcode = "CouponBarcode";
    public static final String KEY_CouponDescription = "CouponDescription";
    public static final String KEY_CouponId = "CouponId";

    // Customer
    public static final String KEY_CustAddress = "CustAddress";
    public static final String KEY_CustPhoneNo = "CustPhoneNo";
    //private static final String KEY_CustName = "CustName";
    public static final String KEY_LastTransaction = "LastTransaction";
    public static final String KEY_TotalTransaction = "TotalTransaction";
    public static final String KEY_CreditAmount = "CreditAmount";
    public static final String KEY_CreditLimit = "CreditLimit";
    public static final String KEY_OpeningBalance = "OpeningBalance";

    // Description
    public static final String KEY_DescriptionId = "DescriptionId";
    public static final String KEY_DescriptionText = "DescriptionText";

    // DiscountConfig
    public static final String KEY_DiscDescription = "DiscDescription";
    public static final String KEY_DiscPercentage = "DiscPercentage";
    public static final String KEY_DiscAmount = "DiscAmount";

    // Employee
    private static final String KEY_EmployeeContactNumber = "EmployeeContactNumber";
    private static final String KEY_EmployeeName = "EmployeeName";
    private static final String KEY_EmployeeRole = "EmployeeRole";

    // Item
    public static final String KEY_ImageUri = "ImageUri";
    public static final String KEY_ShortCode = "ShortCode";
    public static final String KEY_ItemShortName = "ItemShortName";
    public static final String KEY_RetailPrice = "RetailPrice";
    public static final String KEY_MRP = "MRP";
    public static final String KEY_WholeSalePrice = "WholeSalePrice";
    private static final String KEY_isFavrouite = "isFavourite";
    public static final String KEY_isActive = "isActive";
    public static final String KEY_ItemBarcode = "ItemBarcode";
    private static final String KEY_ItemLongName = "ItemLongName";
    private static final String KEY_MenuCode = "MenuCode";



    // MachineSetting
    private static final String KEY_BaudRate = "BaudRate";
    private static final String KEY_DataBits = "DataBits";
    private static final String KEY_Parity = "Parity";
    private static final String KEY_PortName = "PortName";
    private static final String KEY_StopBits = "StopBits";

    // PaymentReceipt
    public static final String KEY_id = "_id";
    private static final String KEY_BillType = "BillType";
    private static final String KEY_DescriptionId1 = "DescriptionId1";
    private static final String KEY_DescriptionId2 = "DescriptionId2";
    private static final String KEY_DescriptionId3 = "DescriptionId3";

    // PendingKOT
    private static final String KEY_IsCheckedOut = "IsCheckedOut";
    private static final String KEY_OrderMode = "OrderMode";

    // KITCHEN
    private static final String KEY_KitchenName = "KitchenName";

    // RiderSettlement
    private static final String KEY_PettyCash = "PettyCash";
    private static final String KEY_SettledAmount = "SettledAmount";

    // TableBooking
    private static final String KEY_TBookId = "TBookId";
    private static final String KEY_CustomerName = "CustName";
    private static final String KEY_TimeForBooking = "TimeForBooking";
    private static final String KEY_TableNo = "TableNo";
    private static final String KEY_MobileNo = "MobileNo";

    // TaxConfig
    private static final String KEY_TaxDescription = "TaxDescription";
    private static final String KEY_TaxId = "TaxId";
    private static final String KEY_TaxPercentage = "TaxPercentage";
    private static final String KEY_TotalPercentage = "TotalPercentage";

    // TaxConfigSub
    private static final String KEY_SubTaxId = "SubTaxId";
    private static final String KEY_SubTaxDescription = "SubTaxDescription";
    private static final String KEY_SubTaxPercentage = "SubTaxPercent";

    // User
    private static final String KEY_AccessLevel = "AccessLevel";
    private static final String KEY_Password = "Password";
    private static final String KEY_UserName = "UserName";


    // VoucherConfig
    private static final String KEY_VoucherId = "VoucherId";
    private static final String KEY_VoucherDescription = "VoucherDescription";
    private static final String KEY_VoucherPercentage = "VoucherPercentage";

    // MailConfiguration
    private static final String KEY_FromMailId = "FromMailId";
    private static final String KEY_FromMailPassword = "FromMailPassword";
    private static final String KEY_SmtpServer = "SmtpServer";
    private static final String KEY_PortNo = "PortNo";
    private static final String KEY_FromDate = "FromDate";
    private static final String KEY_ToDate = "ToDate";
    private static final String KEY_SendMail = "SendMail";
    private static final String KEY_AutoMail = "AutoMail";

    // ReportsMaster
    private static final String KEY_ReportsId = "ReportsId";
    private static final String KEY_ReportsName = "ReportsName";
    private static final String KEY_ReportsType = "ReportsType";
    private static final String KEY_Status = "Status";

    // USERS_new
    public static final String TBL_USERS = "Users";
    public static final String TBL_USERSROLE = "UsersRole";
    public static final String TBL_USERROLEACCESS = "UserRoleAccess";
//    private static final String KEY_ID = "_id";

    public static final String KEY_ROLE_ID = "RoleId";
    public static final String KEY_ROLE_NAME = "RoleName";
    private static final String TABLE_NAME_ROLE_ACCESS_MAP = "_tbl_role_map";
    public static final String KEY_ACCESS_NAME = "RoleAccessName";
    public static final String KEY_ACCESS_CODE = "RoleAccessId";
    private static final String TABLE_NAME_USER = "tbl_user";
    public static final String KEY_USER_ID = "UserId";
    public static final String KEY_USER_NAME = "Name";
    private static final String KEY_USER_MOBILE = "Mobile";
    private static final String KEY_USER_DESIGNATION = "Designation";
    public static final String KEY_USER_LOGIN = "LoginId";
    private static final String KEY_USER_ADHAR = "AadhaarNo";
    public static final String KEY_USER_EMAIL = "Email";
    private static final String KEY_USER_ADDRESS = "Address";
    private static final String KEY_USER_FILE_LOCATION = "FileLocation";
    public static final String KEY_USER_PASS = "Password";


    private static final String TBL_TRANSACTION_DETAIL = "TransactionDetails";
    private static final String KEY_TransactionId = "TransactionId";
    private static final String KEY_TransactionName = "TransactionName";
    private static final String KEY_TRANS_ID = "transId";
    private static final String KEY_MERCHANT_NAME = "merchantName";
    private static final String KEY_MERCHANT_ADDRESS = "merchantAdd";
    private static final String KEY_DATE_TIME = "dateTime";
    private static final String KEY_M_ID = "mId";
    private static final String KEY_T_ID = "tId";
    private static final String KEY_BATCH_NO = "batchNo";
    private static final String KEY_VOUCHER_NO = "voucherNo";
    public static final String KEY_REFERENCE_NO = "refNo";
    public static final String KEY_BillNoPrefix = "BillNoPrefix";
    private static final String KEY_SALES_TYPE = "saleType";
    private static final String KEY_CARD_NO = "cardNo";

    private static final String KEY_TRX_TYPE = "trxType";
    private static final String KEY_CARD_TYPE = "cardType";
    private static final String KEY_EXP_DATE = "expDate";
    private static final String KEY_EMVSIGN_EXPDATE = "emvSigExpDate";
    private static final String KEY_CARDHOLDER_NAME = "cardHolderName";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_CASH_AMOUNT = "cashAmount";
    private static final String KEY_BASE_AMOUNT = "baseAmount";
    private static final String KEY_TIP_AMOUNT = "tipAmount";
    private static final String KEY_TOTAL_AMOUNT = "totalAmount";

    private static final String KEY_AUTH_CODE = "authCode";
    private static final String KEY_RRNO = "rrNo";
    private static final String KEY_CERTIFI = "certif";
    private static final String KEY_APP_ID = "appId";
    private static final String KEY_APP_NAME = "appName";
    private static final String KEY_TVR = "tvr";
    private static final String KEY_TSI = "tsi";
    private static final String KEY_APP_VERSION = "appVersion";
    private static final String KEY_IS_PINVERIFIED = "isPinVarifed";
    private static final String KEY_STAN = "stan";

    private static final String KEY_CARD_ISSUER = "cardIssuer";
    private static final String KEY_CARD_EMI_AMOUNT = "emiPerMonthAmount";
    private static final String KEY_TOTAL_PAYAMOUNT = "total_Pay_Amount";
    private static final String KEY_NO_EMI = "noOfEmi";
    private static final String KEY_INTEREST_RATE = "interestRate";
    private static final String KEY_BILL_NO = "billNo";
    private static final String KEY_FIRST_DIGIT_OF_CARD = "firstDigitsOfCard";
    private static final String KEY_IS_INCONV = "isConvenceFeeEnable";
    private static final String KEY_INVOICE = "invoiceNo";
    private static final String KEY_TRX_DATE = "trxDate";

    private static final String KEY_TRX_IMG_DATE = "trxImgDate";
    private static final String KEY_CHEQUE_DATE = "chequeDate";
    private static final String KEY_CHEQUE_NO = "chequeNo";

    private static final String TBL_TRANSACTIONS = "tbl_transactions";

    String QUERY_CREATE_TABLE_Outward_Supply_Ledger = "CREATE TABLE " + TBL_OUTWARD_SUPPLY_LEDGER +
            "( "
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_InvoiceNo + " TEXT, "
            + KEY_InvoiceDate + " TEXT, "
            + KEY_ITEM_ID + " NUMERIC, "
            + KEY_ItemName + " TEXT, "
            + KEY_ItemBarcode + " TEXT, "
            + KEY_HSNCode + " TEXT, "
            + KEY_UOM + " TEXT, "
            + KEY_Quantity + " REAL, "
            + KEY_OriginalRate+" REAL, "
            + KEY_IsReverseTaxEnabled + " TEXT, "
            + KEY_Value + " REAL, "
            + KEY_TaxableValue + " REAL, "
            + KEY_IGSTRate + " REAL,"
            + KEY_IGSTAmount + " REAL,"
            + KEY_CGSTRate + " REAL,"
            + KEY_CGSTAmount + " REAL, "
            + KEY_SGSTRate + " REAL,"
            + KEY_SGSTAmount + " REAL,"
            + KEY_cessRate + " REAL,"
            + KEY_cessAmount + " REAL,"
            + KEY_Amount + " REAL, "
            + KEY_DiscountPercent + " REAL, "
            + KEY_DiscountAmount + " REAL, "
            + KEY_SupplyType + " TEXT, "
            + KEY_TaxType + " NUMERIC, "
            + KEY_TaxationType + " TEXT, "
            + KEY_CategoryCode + " NUMERIC, "
            + KEY_DepartmentCode + " NUMERIC, "
            + KEY_BrandCode +" NUMERIC, "
            + KEY_MRP +" REAL, "
            + KEY_RetailPrice +" REAL, "
            + KEY_WholeSalePrice +" REAL, "
            + KEY_GSTIN + " TEXT, "
            + KEY_CustName + " TEXT, "
            + KEY_CustStateCode + " TEXT, "
            + KEY_CustId + " TEXT, "
            + KEY_BusinessType + " TEXT, "
            + KEY_BillStatus+" INTEGER, "
            + KEY_SALES_MAN_ID + " TEXT )" ;


    String QUERY_CREATE_TABLE_Outward_Supply_Items_Details = " CREATE TABLE " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + "( "
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_InvoiceNo + " INTEGER, "
            + KEY_InvoiceDate + " TEXT, "
            + KEY_Time + " TEXT, "
            + KEY_TotalItems + " NUMERIC,"
            + KEY_TaxableValue + " TEXT, "
            + KEY_IGSTRate + " REAL,"
            + KEY_IGSTAmount + " REAL,"
            + KEY_CGSTRate + " REAL,"
            + KEY_CGSTAmount + " REAL, "
            + KEY_SGSTRate + " REAL,"
            + KEY_SGSTAmount + " REAL,"
            + KEY_cessAmount + " REAL,"
            + KEY_SubTotal + " REAL, "
            + KEY_DeliveryCharge + " REAL, "
            + KEY_GrandTotal + " REAL, "
            + KEY_BillAmount + " REAL, "
            + KEY_PaidTotalPayment + " REAL, "
            + KEY_ChangePayment + " REAL, "
            + KEY_RoundOff + " REAL , "
            + KEY_TotalDiscountAmount + " REAL,"
            + KEY_DiscPercentage + " REAL,"
            + KEY_CardPayment + " REAL, "
            + KEY_CashPayment + " REAL, "
            + KEY_CouponPayment + " REAL, "
            + KEY_PettyCashPayment + " REAL, "
            + KEY_WalletPayment + " REAL, "
            + KEY_RewardPointsAmount +" REAL, "
            + KEY_AEPSAmount+" REAL, "
            + KEY_MSWIPE_Amount + " REAL, "
            + KEY_CustId + " NUMERIC, "
            + KEY_GSTIN + " TEXT, "
            + KEY_CustName + " TEXT, "
            + KEY_CustStateCode + " Text, "
            + KEY_POS + " TEXT, "
            + KEY_BusinessType + " TEXT,"
            + KEY_UserId + " NUMERIC, "
            + KEY_SALES_MAN_ID + " TEXT, "
            + KEY_EmployeeId + " NUMERIC,"
            + KEY_BillStatus + " NUMERIC,"
            + KEY_ReprintCount + " NUMERIC, "
            + KEY_ReverseCharge + " TEXT, "
            + KEY_ProvisionalAssess + " TEXT, "
            + KEY_BillingMode + " TEXT, "
            + KEY_Ecom_GSTIN + " TEXT "
            +")";

    String QUERY_CREATE_TABLE_OUTWARD_SUPPLY_DELIVERY_CHARGES_DETAILS = "CREATE TABLE " + TBL_OUTWARD_SUPPLY_DELIVERY_CHARGES_DETAILS + "("
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_InvoiceNo + " TEXT,"
            + KEY_InvoiceDate + " TEXT, "
            + KEY_Time + " TEXT, "
            + KEY_OTHER_CHARGES_DESCRIPTION + " TEXT,"
            + KEY_OTHER_CHARGES_AMOUNT+" REAL )";


    String QUERY_CREATE_TABLE_USER_ROLE = "CREATE TABLE " + TBL_USERSROLE + "("
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_ROLE_ID + " INTEGER,"
            + KEY_ROLE_NAME + " TEXT, UNIQUE ( "
            + KEY_ROLE_ID + "))";

    String QUERY_CREATE_TABLE_ROLE_ACCESS = "CREATE TABLE " + TBL_USERROLEACCESS + "("
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_ACCESS_CODE + " INTEGER,"
            + KEY_ACCESS_NAME + " TEXT,"
            + KEY_ROLE_ID + " TEXT )";

    String QUERY_CREATE_TABLE_USERS = "CREATE TABLE " + TBL_USERS + "("
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_USER_ID + " INTEGER,"
            + KEY_USER_NAME + " TEXT,"
            + KEY_USER_MOBILE + " TEXT,"
            + KEY_USER_DESIGNATION + " TEXT,"
            + KEY_ROLE_ID + " INTEGER,"
            + KEY_USER_LOGIN + " TEXT,"
            + KEY_USER_PASS + " TEXT,"
            + KEY_USER_ADHAR + " TEXT,"
            + KEY_USER_EMAIL + " TEXT,"
            + KEY_USER_ADDRESS + " TEXT,"
            + KEY_SALES_MAN_ID + " TEXT,"
            + KEY_isActive +" INTEGER, "
            + KEY_USER_FILE_LOCATION + " TEXT, UNIQUE ( "
            + KEY_USER_ID + "))";


    private static final String TBL_BILLDETAIL = "OutwardSuppyItemsDetails";
    private static final String TBL_BILLITEM = "OutwardSupplyLedger";
    private static final String TBL_PREVIEWBILLITEM = "PreviewOutwardSupplyLedger";
    private static final String TBL_StockOutward = "StockOutward";
    private static final String TBL_StockInward = "StockInward";
    public static final String KEY_OpeningStock = "OpeningStock";
    public static final String KEY_ClosingStock = "ClosingStock";
    public static final String KEY_ClosingStockValue = "ClosingStockValue";

    public static final String KEY_GSTIN = "GSTIN";
    public static final String KEY_GSTIN_Ori = "GSTIN_Ori";
    public static final String KEY_CustName = "CustName";
    public static final String KEY_CUST_EMAIL = "email_id";
    public static final String KEY_CUST_DEPOSIT_AMOUNT = "deposit_amount";
    public static final String KEY_CUST_DEPOSIT_AMT_STATUS = "deposit_amount_status";
    public static final String KEY_InvoiceNo = "InvoiceNo";
    public static final String KEY_CustStateCode = "CustStateCode";
    public static final String KEY_UID = "UID";
    public static final String KEY_InvoiceDate = "InvoiceDate";
    public static final String KEY_Value = "Value";
    public static final String KEY_HSNCode = "HSNCode";
    public static final String KEY_MinimumLimit = "MinimumLimit";
    public static final String KEY_SUPPLIERNAME = "SupplierName";
    public static final String KEY_SupplierPhone = "SupplierPhone";
    public static final String KEY_SupplierCount = "SupplierCount";
    public static final String KEY_Count = "Count";
    public static final String KEY_AverageRate = "AverageRate";
    public static final String KEY_SupplierAddress = "SupplierAddress";
    public static final String KEY_SupplierCode = "SupplierCode";
    public static final String KEY_TaxableValue = "TaxableValue";
    public static final String KEY_IGSTAmount = "IGSTAmount";
    public static final String KEY_SGSTAmount = "SGSTAmount";
    public static final String KEY_CGSTAmount = "CGSTAmount";
    public static final String KEY_POS = "POS";
    public static final String KEY_DeviceId = "DeviceId";
    public static final String KEY_DeviceName = "DeviceName";
    public static final String KEY_BillingMode = "BillingMode"; // richa_2012
    public static final String KEY_MONTH = "Month";
    public static final String KEY_SupplyType_REV = "RevisedSupplyType";
    public static final String KEY_NoteType = "NoteType";
    public static final String KEY_NoteNo = "NoteNo";
    public static final String KEY_NoteDate = " NoteDate";
    public static final String KEY_DifferentialValue = "DifferentialValue";
    public static final String KEY_MONTH_ITC_IGSTAMT = "MonthITC_IGSTAmount";
    public static final String KEY_MONTH_ITC_CGSTAMT = "MonthITC_CGSTAmount";
    public static final String KEY_MONTH_ITC_SGSTAMT = "MonthITC_SGSTAmount";
    public static final String KEY_HSNCode_REV = "ReviseHSNCode";
    public static final String KEY_POS_REV = " RevisedPOS";
    public static final String KEY_SupplyType = "SupplyType";
    public static final String KEY_TaxationType = "TaxationType";
    public static final String KEY_Description = "Description";
    public static final String KEY_Price = "Price";
    public static final String KEY_UOM = "UOM";
    public static final String KEY_IsReverseTaxEnabled = "IsReverseTaxEnable";
    public static final String KEY_OriginalRate = "OriginalRate";
    public static final String KEY_DiscountRate = "DiscountRate";
    public static final String KEY_IGSTRate = "IGSTRate";
    public static final String KEY_CGSTRate = "CGSTRate";
    public static final String KEY_SGSTRate = "SGSTRate";
    public static final String KEY_cessRate = "cessRate";
    public static final String KEY_cessAmount = "cessAmount";
    public static final String KEY_ITC_Eligible = "ITC_Eligible";
    public static final String KEY_Total_ITC_IGST = "Total_ITC_IGST";
    public static final String KEY_Total_ITC_CGST = "Total_ITC_CGST";
    public static final String KEY_Total_ITC_SGST = "Total_ITC_SGST";
    public static final String KEY_SupplierType = "SupplierType";
    public static final String KEY_SupplierPOS = "SupplierPOS";
    public static final String KEY_AttractsReverseCharge = "AttractsReverseCharge";
    public static final String KEY_AdditionalChargeName = "AdditionalChargeName";
    public static final String KEY_AdditionalChargeAmount = "AdditionalChargeAmount";
    public static final String KEY_isGoodinward = "isGoodinward";
//    public static final String KEY_ItemNumber = "ItemNumber";
//    public static final String KEY_ItemName = "ItemName";
//    public static final String KEY_Amount = "Amount";
//    public static final String KEY_BillAmount = "BillAmount";
//    public static final String KEY_TaxType = "TaxType";
//    public static final String KEY_DiscountAmount = "DiscountAmount";
//    public static final String KEY_DiscountPercent = "DiscountPercent";
    // BillItemBean
//    public static final String KEY_TaxAmount = "TaxAmount";
//    public static final String KEY_TaxPercent = "TaxPercent";

    public static final String KEY_GrandTotal = "GrandTotal";
    public static final String KEY_SubTotal = "SubTotal";
    public static final String KEY_OriginalInvoiceNo = "OriginalInvoiceNo";
    public static final String KEY_OriginalInvoiceDate = "OriginalInvoiceDate";
    public static final String KEY_BusinessType = "BusinessType";
    public static final String KEY_ReverseCharge = "ReverseCharge";

    public static final String KEY_Environment = "Environment";
    public static final String KEY_UTGSTEnabled = "UTGSTEnabled";
    public static final String KEY_HSNPrintEnabled_out = "HSNPrintEnabled_out";
    public static final String KEY_PrintOwnerDetail = "PrintOwnerDetail";
    public static final String KEY_HeaderBold = "BoldHeader";
    public static final String KEY_PrintService = "PrintService";
    public static final String KEY_RoundOff = "RoundOff";
    public static final String KEY_SALES_MAN_ID = "sales_man_id";

    public static final String KEY_ProvisionalAssess = "ProvisionalAssess";
    public static final String KEY_LineNumber = "LineNumber";
    public static final String KEY_Ecom_GSTIN = "EcommerceGSTIN";
    public static final String KEY_FIRM_NAME = "FirmName";
    public static final String KEY_Address = "Address";
    public static final String KEY_TINCIN = "TINCIN";
    public static final String KEY_PhoneNo = "Phone";
    public static final String KEY_Owner_Name = "OwnerName";
    public static final String KEY_IsMainOffice = "IsMainOffice";
    public static final String KEY_PurchaseOrderNo = "PurchaseOrderNo";

    public static final String KEY_GSTIN_OUT = "GSTIN_Out";
    public static final String KEY_GSTIN_IN = "GSTIN_In";
    public static final String KEY_GSTEnable = "GSTEnable";
    public static final String KEY_POS_OUT = "POS_Out";
    public static final String KEY_HSNCode_OUT = "HSNCode_Out";
    public static final String KEY_ReverseCharge_OUT = "ReverseCharge_Out";
    // payment mode configuration
    public static final String KEY_RAZORPAY_KEYID = "RazorPay_KeyId";
    public static final String KEY_RAZORPAY_SECRETKEY = "RazorPay_SecretKey";
    public static final String KEY_AEPS_MerchantId = "AEPS_MerchantId";
    public static final String KEY_AEPS_AppId = "AEPS_AppId";
    public static final String KEY_AEPS_SecretKey = "AEPS_SecretKey";


    public static final String TBL_BRAND = "Brand";
    public static final String KEY_BRAND_CODE = "BrandCode";
    public static final String KEY_BRAND_NAME = "BrandName";

    //Configuration other charges table constants
    public static final String TBL_OTHER_CHARGES = "other_charges";
    public static final String KEY_OTHER_CHARGES_CODE = "other_charges_code";
    public static final String KEY_OTHER_CHARGES_AMOUNT = "amount";
    public static final String KEY_OTHER_CHARGES_DESCRIPTION = "description";
    public static final String KEY_OTHER_CHARGES_CHARGEABLE = "chargeable";


    //Supplier Details
    public static final String TBL_Supplier = "SupplierDetails";

    public static final String TBL_OWNER_DETAILS = "OwnerDetails";


    public static final String TBL_SUPPLIER_ITEM_LINKAGE = "SupplierItemLinkage";
    public static final String ITEM_NAME = "item_name";
    public static final String BARCODE = "barcode";
    public static final String KEY_ITEM_ID = "item_id";
    public static final String RATE = "rate";
    public static final String MRP = "mrp";
    public static final String RETAIL_PRICE = "retail_price";
    public static final String WHOLE_SALE_PRICE = "whole_sale_price";
    public static final String HSN_CODE = "hsn_code";
    public static final String UOM = "uom";
    public static final String CGST_RATE_PER = "cgst_rate_per";
    public static final String UTGST_SGST_RATE_PER = "utgst_sgst_rate_per";
    public static final String IGST_RATE_PER = "igst_rate_per";
    public static final String CESS_RATE_PER = "cess_rate_per";




    String QUERY_CREATE_TABLE_BILLNO_RESET = "CREATE TABLE " + TBL_BILLNORESETCONFIG + "( " + KEY_BillNoReset_InvoiceNo +
            " NUMERIC, " + KEY_BillNoReset_Period + " TEXT," + KEY_BillNoReset_PeriodDate + " TEXT, KOTNo NUMERIC)";

    String QUERY_CREATE_TABLE_PAYMENT_MODE_CONFIGURATION = "CREATE TABLE " +
            TBL_PaymentModeConfiguration + " ( "
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_RAZORPAY_KEYID + " TEXT, "
            + KEY_RAZORPAY_SECRETKEY + " TEXT , "
            + KEY_AEPS_MerchantId +" TEXT , "
            + KEY_AEPS_AppId +" TEXT , "
            + KEY_AEPS_SecretKey +" TEXT "
            +")";


    String QUERY_CREATE_TABLE_OWNER_DETAILS = "CREATE TABLE " + TBL_OWNER_DETAILS + " ( " +
            KEY_GSTIN + " TEXT, " + KEY_Owner_Name + "  TEXT, " + KEY_FIRM_NAME + " TEXT, " + KEY_PhoneNo + " TEXT, " +
            KEY_POS +" TEXT,"+ KEY_DeviceId+" Text, "+KEY_DeviceName+" Text, "+KEY_USER_EMAIL+" TEXT, "+
            KEY_REFERENCE_NO+" TEXT, "+ KEY_BillNoPrefix +" TEXT, "+
            KEY_Address + " TEXT, " + KEY_TINCIN + " TEXT, " + KEY_IsMainOffice + "  TEXT ) ";

    String QUERY_CREATE_TABLE_BILLSETTING = "CREATE TABLE " + TBL_BILLSETTING + " ( "
            + KEY_WeighScale + " NUMERIC, "
            + KEY_BusinessDate + " TEXT, "
            + KEY_FooterText1 + " TEXT, "
            + KEY_FooterText2 + " TEXT, "
            + KEY_FooterText3 + " TEXT, "
            + KEY_FooterText4 + " TEXT, "
            + KEY_FooterText5 + " TEXT, "
            + KEY_HeaderText1 + " TEXT, "
            + KEY_HeaderText2 + " TEXT, "
            + KEY_HeaderText3 + " TEXT, "
            + KEY_HeaderText4 + " TEXT, "
            + KEY_HeaderText5 + " TEXT, "
            + KEY_DateAndTime + " NUMERIC, "
            + KEY_PriceChange + " NUMERIC, "
            + KEY_BillwithStock + " NUMERIC, "
            + KEY_ShortCodeReset + " NUMERIC , "
            + KEY_BILL_NO_RESET +  " NUMERIC , "
            + KEY_PrintOwnerDetail +" NUMERIC, "
            + KEY_RoundOff +" NUMERIC, "
            + KEY_SALES_MAN_ID +" NUMERIC, "
            + KEY_Tax + " NUMERIC, "
            + KEY_DiscountType + " NUMERIC, "
            + KEY_DYNAMIC_DISCOUNT + " NUMERIC, "
            + KEY_PRINT_MRP_RETAIL_DIFFERENCE +" NUMERIC, "
            + KEY_FastBillingMode + " NUMERIC, "
            + KEY_CummulativeHeadingEnable + " NUMERIC, " // richa_2012
            + KEY_GSTIN_IN + " TEXT,"
            + KEY_GSTIN_OUT + " TEXT, "
            + KEY_POS + " TEXT, "
            + KEY_POS_OUT + " TEXT, "
            + KEY_HSNCode + " TEXT, "
            + KEY_HSNCode_OUT + " TEXT, "
            + KEY_UTGSTEnabled +" NUMERIC, "
            + KEY_HSNPrintEnabled_out +" NUMERIC, "
            + KEY_HeaderBold +" NUMERIC, "
            + KEY_PrintService +" NUMERIC, "
            + KEY_Environment +" NUMERIC, "
            + KEY_RewardPoints + " NUMERIC )";

    String QUERY_CREATE_TABLE_DEPARTMENT = "CREATE TABLE " + TBL_DEPARTMENT + "( "
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_DepartmentCode +
            " INTEGER ," + KEY_DepartmentName + " TEXT, UNIQUE ( "
            + KEY_DepartmentCode + "))";

    String QUERY_CREATE_TABLE_BRAND = "CREATE TABLE " + TBL_BRAND + "( "
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_BRAND_CODE +
            " INTEGER ," + KEY_BRAND_NAME + " TEXT, UNIQUE ( "
            + KEY_BRAND_CODE + "))";

    String QUERY_CREATE_TABLE_CATEGORY = "CREATE TABLE " + TBL_CATEGORY + "( "
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_CategoryCode +
            " INTEGER, " + KEY_CategoryName + " TEXT," + KEY_DepartmentCode + " INTEGER, UNIQUE ( "
            + KEY_CategoryCode + "))";

    String QUERY_CREATE_TABLE_REWARD_POINTS_CONFIGURATION = "CREATE TABLE "+ TBL_REWARDPOINTSCONGFIGURATION +" ( "
            +KEY_AmtToPt +" REAL, "
            + KEY_RewardPoints +" INTEGER, "
            +KEY_RewardPointsToAmt +" REAL, "
            + KEY_RewardPointsLimit +" INTEGER )";

    String QUERY_CREATE_TABLE_DECRIPTION = "CREATE TABLE " + TBL_DESCRIPTION + " ( "
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_DescriptionId +
            " INTEGER, " + KEY_DescriptionText + " TEXT, UNIQUE ( "
            + KEY_DescriptionId + "))";

    String QUERY_CREATE_TABLE_DISCOUNTCONFIG = "CREATE TABLE " + TBL_DISCOUNTCONFIG + "( "
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_DiscDescription + " TEXT, " + KEY_DiscId + " INTEGER, " + KEY_DiscPercentage + " REAL, "
            + KEY_DiscAmount + " REAL, UNIQUE ( "
            + KEY_DiscId + "))";

    String QUERY_CREATE_TABLE_COUPON = "CREATE TABLE " + TBL_COUPON + " ("
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_CouponAmount + " REAL, " +
            KEY_CouponBarcode + " TEXT, " + KEY_CouponDescription + " TEXT,"
            + KEY_CouponId + " INTEGER, UNIQUE ( "
            + KEY_CouponId + "))";

    String QUERY_CREATE_TABLE_OTHER_CHARGES = "CREATE TABLE " + TBL_OTHER_CHARGES + " ("
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_OTHER_CHARGES_CODE + " INTEGER,"
            + KEY_OTHER_CHARGES_DESCRIPTION + " TEXT, "
            + KEY_OTHER_CHARGES_CHARGEABLE + " INTEGER,"
            + KEY_OTHER_CHARGES_AMOUNT + " REAL, UNIQUE ( "
            + KEY_OTHER_CHARGES_CODE + "))";

    String QUERY_CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TBL_CUSTOMER + " ("
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_CustAddress + " TEXT," +
            KEY_CustPhoneNo + " NUMERIC," + KEY_CustId + " INTEGER," + KEY_CustName + " TEXT, "
            + KEY_CUST_EMAIL + " TEXT,"
            + KEY_LastTransaction + " REAL," + KEY_TotalTransaction + " REAL," + KEY_CreditAmount +
            " REAL, "+ KEY_CreditLimit +" REAL,"
            + KEY_RewardPointsAccumulated + " REAL,"
            + KEY_CUST_DEPOSIT_AMOUNT + " REAL,"
            + KEY_OpeningBalance + " REAL,"
            + KEY_isActive +" INTEGER, "
            + KEY_CUST_DEPOSIT_AMT_STATUS + " INTEGER,"
            + KEY_GSTIN + " TEXT, UNIQUE ( "
            + KEY_CustId + "))";

    String QUERY_CREATE_TABLE_SUPPLIER = " CREATE TABLE " + TBL_Supplier + " ( "
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_SupplierCode + " INTEGER, " + KEY_GSTIN + " TEXT," + KEY_SUPPLIERNAME + " TEXT, " +
            KEY_SupplierType + " TEXT, " + KEY_SupplierPhone + "  TEXT, " + KEY_SupplierAddress + " TEXT, UNIQUE ( "
            + KEY_SupplierCode + "))";

    String QUERY_CREATE_TABLE_SUPPLIER_ITEM_LINKAGE = " CREATE TABLE " + TBL_SUPPLIER_ITEM_LINKAGE + " ( "
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_SupplierCode + " INTEGER,"
            + KEY_GSTIN + " TEXT,"
            + KEY_SUPPLIERNAME + " TEXT,"
            + KEY_SupplierType + " TEXT,"
            + KEY_SupplierPhone + "  TEXT,"
            + KEY_SupplierAddress + " TEXT,"
            + KEY_ITEM_ID +  " INTEGER,"
            + ITEM_NAME +  " TEXT,"
            + BARCODE +  " TEXT,"
            + RATE + " REAL,"
            + MRP + " REAL,"
            + RETAIL_PRICE + " REAL,"
            + WHOLE_SALE_PRICE + " REAL,"
            + HSN_CODE + " TEXT,"
            + UOM + " TEXT,"
            + CGST_RATE_PER + " REAL,"
            + UTGST_SGST_RATE_PER + " REAL,"
            + IGST_RATE_PER + " REAL,"
            + CESS_RATE_PER + " REAL);";

    String QUERY_CREATE_TABLE_PAYMENTRECEIPT = "CREATE TABLE " + TBL_PAYMENTRECEIPT + "("
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + KEY_Reason +
            " TEXT, " + KEY_id + " INTEGER, " + KEY_Amount + " REAL, " + KEY_BillType + " NUMERIC, " +
            KEY_DescriptionText + " TEXT, " +
            KEY_InvoiceDate + " TEXT, " + KEY_DescriptionId1 + " NUMERIC, " + KEY_DescriptionId2 + " NUMERIC, " +
            KEY_DescriptionId3 + " NUMERIC, UNIQUE ( "
            + KEY_id + "))";

    String QUERY_CREATE_TABLE_Item = "CREATE TABLE " + TBL_Item + "("
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ShortCode + " INTEGER,"
            + KEY_ItemShortName + " TEXT,"
            + KEY_ItemLongName + " TEXT,"
            + KEY_ItemBarcode + " TEXT,"
            + KEY_SupplyType + " TEXT,"
            + KEY_UOM + " TEXT,"
            + KEY_BrandCode + " INTEGER,"
            + KEY_DepartmentCode + " INTEGER,"
            + KEY_CategoryCode +" INTEGER,"
            + KEY_RetailPrice + " REAL,"
            + KEY_MRP +" REAL,"
            + KEY_WholeSalePrice +" REAL,"
            + KEY_Quantity + " REAL,"
            + KEY_HSNCode  + " TEXT,"
            + KEY_DiscountPercent +" REAL,"
            + KEY_DiscountAmount +" REAL,"
            + KEY_CGSTRate +" REAL,"
            + KEY_SGSTRate +" REAL,"
            + KEY_IGSTRate +" REAL,"
            + KEY_cessRate +" REAL,"
            + KEY_ImageUri +" TEXT,"
            + KEY_MinimumLimit +" INTEGER, "
            + KEY_isFavrouite +" INTEGER,"
            + KEY_isActive + " INTEGER " + ")";

    String QUERY_CREATE_TABLE_Hold_Resume_Bill_Outward_Supply_Ledger = "CREATE TABLE " + TBL_HOLD_RESUME_BILL_LEDGER +
            "( "
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_GSTIN + " TEXT, " + KEY_CustName + " TEXT, " + KEY_CustStateCode + " TEXT, "
            + KEY_CustPhoneNo + " TEXT, "
            + KEY_InvoiceNo + " TEXT, " +
            KEY_InvoiceDate + " TEXT, " + KEY_SupplyType + " TEXT, " +
            KEY_BusinessType + " TEXT, " + KEY_TaxationType + " TEXT, " + KEY_HSNCode + " TEXT, "
            + KEY_ITEM_ID + " NUMERIC, " + KEY_ItemName + " TEXT, "
            + KEY_ItemBarcode + " TEXT, "
            + KEY_Quantity + " REAL, " + KEY_UOM + " TEXT, "
            +KEY_OriginalRate+" REAL, "+
            KEY_Value + " REAL, " +
            KEY_MRP + " REAL, " +
            KEY_RetailPrice + " REAL, " +
            KEY_WholeSalePrice + " REAL, " +
            KEY_TaxableValue + " REAL, " + KEY_Amount + " REAL, " +KEY_IsReverseTaxEnabled + " TEXT, " + KEY_IGSTRate + " REAL," +
            KEY_IGSTAmount + " REAL," + KEY_CGSTRate + " REAL," + KEY_CGSTAmount + " REAL, " + KEY_SGSTRate + " REAL," +
            KEY_SGSTAmount + " REAL," + KEY_cessRate + " REAL," + KEY_cessAmount + " REAL," +
            KEY_SubTotal + " REAl, " + KEY_BillAmount + " REAl, " + KEY_BillingMode + " TEXT, " + KEY_TaxType + " NUMERIC, " +
            KEY_CategoryCode + " NUMERIC, " + KEY_DepartmentCode + " NUMERIC, " + KEY_BrandCode +" NUMERIC, "+
            KEY_DiscountPercent + " REAL, " + KEY_DiscountAmount + " REAL, "  + KEY_BillStatus+" INTEGER, "+
            KEY_SALES_MAN_ID + " TEXT )";

    String QUERY_CREATE_TABLE_TRANSACTION_DETAILS = " CREATE TABLE " + TBL_TRANSACTION_DETAIL + " ( "
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_InvoiceNo +" TEXT, "
            +KEY_InvoiceDate +" TEXT, "
            + KEY_TransactionName +" TEXT, "
            + KEY_TransactionId +" TEXT, "
            + KEY_Amount +" REAL "
            +")";


    String QUERY_CREATE_TABLE_PURCHASE_ORDER = " CREATE TABLE " + TBL_PURCHASEORDER + " ( "
            + KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PurchaseOrderNo + " INTEGER, " + KEY_InvoiceNo + " TEXT, " + KEY_InvoiceDate + " TEXT, " +
            KEY_SupplierCode + " INTEGER, " + KEY_SUPPLIERNAME + " TEXT, " + KEY_SupplierPhone + " TEXT, " +
            KEY_SupplierAddress + " TEXT, " + KEY_GSTIN+" TEXT, "+
            KEY_SupplierPOS+" TEXT, "+ KEY_SupplierType +" TEXT, "+
            KEY_MenuCode + " INTEGER, " + KEY_SupplyType + " TEXT , " + KEY_HSNCode+" TEXT, "+
            KEY_ItemName + " TEXT, " + KEY_Value + " REAL, " + KEY_Quantity + " REAL, " + KEY_UOM + "  TEXT, " + KEY_TaxableValue + " REAL, " +
            KEY_IGSTRate + " REAL," + KEY_IGSTAmount + " REAl, "+KEY_CGSTRate + " REAL," + KEY_CGSTAmount + " REAl, "+
            KEY_SGSTRate + " REAL," + KEY_SGSTAmount + " REAl, "+ KEY_cessRate + " REAL," + KEY_cessAmount + " REAl, "+
            KEY_SalesTax + " REAL," + KEY_ServiceTaxAmount + " REAl, " + KEY_Amount + " REAL," + KEY_AdditionalChargeName + " TEXT, " +
            KEY_AdditionalChargeAmount + " REAL , " + KEY_isGoodinward + " INTEGER )";

    String QUERY_CREATE_TBL_TRANSACTIONS = "CREATE TABLE " + TBL_TRANSACTIONS + "("
            + KEY_TRANS_ID + " INTEGER PRIMARY KEY,"
            + KEY_MERCHANT_NAME + " TEXT,"
            + KEY_MERCHANT_ADDRESS + " TEXT,"
            + KEY_DATE_TIME + " TEXT,"
            + KEY_M_ID + " TEXT,"
            + KEY_T_ID + " TEXT,"
            + KEY_BATCH_NO + " TEXT,"
            + KEY_VOUCHER_NO + " TEXT,"
            + KEY_REFERENCE_NO + " TEXT,"
            + KEY_SALES_TYPE + " TEXT,"
            + KEY_CARD_NO + " TEXT,"
            + KEY_TRX_TYPE + " TEXT,"
            + KEY_CARD_TYPE + " TEXT,"
            + KEY_EXP_DATE + " TEXT,"
            + KEY_EMVSIGN_EXPDATE + " TEXT,"
            + KEY_CARDHOLDER_NAME + " TEXT,"
            + KEY_CURRENCY + " TEXT,"
            + KEY_CASH_AMOUNT + " TEXT,"
            + KEY_BASE_AMOUNT + " TEXT,"
            + KEY_TIP_AMOUNT + " TEXT,"
            + KEY_TOTAL_AMOUNT + " TEXT,"
            + KEY_AUTH_CODE + " TEXT,"
            + KEY_RRNO + " TEXT,"
            + KEY_CERTIFI + " TEXT,"
            + KEY_APP_ID + " TEXT,"
            + KEY_APP_NAME + " TEXT,"
            + KEY_TVR + " TEXT,"
            + KEY_TSI + " TEXT,"
            + KEY_APP_VERSION + " TEXT,"
            + KEY_IS_PINVERIFIED + " TEXT,"
            + KEY_STAN + " TEXT,"
            + KEY_CARD_ISSUER + " TEXT,"
            + KEY_CARD_EMI_AMOUNT + " TEXT,"
            + KEY_TOTAL_PAYAMOUNT + " TEXT,"
            + KEY_NO_EMI + " TEXT,"
            + KEY_INTEREST_RATE + " TEXT,"
            + KEY_BILL_NO + " TEXT,"
            + KEY_FIRST_DIGIT_OF_CARD + " TEXT,"
            + KEY_IS_INCONV + " TEXT,"
            + KEY_INVOICE + " TEXT,"
            + KEY_TRX_DATE + " TEXT,"
            + KEY_TRX_IMG_DATE + " TEXT,"
            + KEY_CHEQUE_DATE + " TEXT,"
            + KEY_CHEQUE_NO + " TEXT"
            + ")";


    // SQLite Database and Context
    private SQLiteDatabase dbRetail;
    Context myContext;
    ContentValues cvDbValues = new ContentValues();

    public DatabaseHandler(Context context) {
        super(context, DB_PATH + DB_NAME, null, DB_VERSION);
        this.myContext = context;
        // Change Database and Database backup path if memory card is not
        // present
        if (Environment.getExternalStorageDirectory().exists() == false) {
            DB_PATH = Environment.getRootDirectory().getPath() + "/WeP_Retail/";
            DB_BACKUP_PATH = Environment.getRootDirectory().getPath() + "/WeP_FnB_Backup/";
        }

        if (Logger.LOG_FOR_FIRST) {
            Logger.printLog();
            Logger.LOG_FOR_FIRST = false;
        }
        if (Auditing.AUDIT_FOR_FIRST) {
            Auditing.printAudit();
            Auditing.AUDIT_FOR_FIRST = false;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(QUERY_CREATE_TABLE_BILLSETTING);
            db.execSQL(QUERY_CREATE_TABLE_BILLNO_RESET);
            db.execSQL(QUERY_CREATE_TABLE_USERS);
            db.execSQL(QUERY_CREATE_TABLE_ROLE_ACCESS);
            db.execSQL(QUERY_CREATE_TABLE_USER_ROLE);
            db.execSQL(QUERY_CREATE_TABLE_OWNER_DETAILS);
            db.execSQL(QUERY_CREATE_TABLE_DEPARTMENT);
            db.execSQL(QUERY_CREATE_TABLE_BRAND);
            db.execSQL(QUERY_CREATE_TABLE_CATEGORY);
            db.execSQL(QUERY_CREATE_TABLE_DECRIPTION);
            db.execSQL(QUERY_CREATE_TABLE_DISCOUNTCONFIG);
            db.execSQL(QUERY_CREATE_TABLE_COUPON);
            db.execSQL(QUERY_CREATE_TABLE_OTHER_CHARGES);
            db.execSQL(QUERY_CREATE_TABLE_PAYMENT_MODE_CONFIGURATION);
            db.execSQL(QUERY_CREATE_TABLE_CUSTOMER);
            //db.execSQL(QUERY_CREATE_TABLE_SUPPLIER);
            //db.execSQL(QUERY_CREATE_TABLE_SUPPLIER_ITEM_LINKAGE);
            db.execSQL(QUERY_CREATE_TABLE_Item);
            db.execSQL(QUERY_CREATE_TABLE_PURCHASE_ORDER);
            db.execSQL(QUERY_CREATE_TABLE_REWARD_POINTS_CONFIGURATION);
            db.execSQL(QUERY_CREATE_TABLE_Outward_Supply_Ledger);
            db.execSQL(QUERY_CREATE_TABLE_Outward_Supply_Items_Details);
            db.execSQL(QUERY_CREATE_TABLE_OUTWARD_SUPPLY_DELIVERY_CHARGES_DETAILS);
            db.execSQL(QUERY_CREATE_TABLE_Hold_Resume_Bill_Outward_Supply_Ledger);
            db.execSQL(QUERY_CREATE_TABLE_TRANSACTION_DETAILS);
            db.execSQL(QUERY_CREATE_TBL_TRANSACTIONS);
            setDefaultTableValues(db);

        } catch (Exception ex) {
            Toast.makeText(myContext, "OnCreate : " + ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public void CreateDatabase() {
        try {
            dbRetail = this.getReadableDatabase();

        } catch (Exception exp) {
            Toast.makeText(myContext, exp.getMessage(), Toast.LENGTH_SHORT).show();
            Logger.e(TAG, "Retail : Error occured",exp);
        }
    }


    public void OpenDatabase() {
        try {
            dbRetail = this.getWritableDatabase();

        } catch (Exception exp) {
            Toast.makeText(myContext, exp.getMessage(), Toast.LENGTH_SHORT).show();
            Logger.d(TAG, exp.toString());
        }
    }

    public int BackUpDatabase() {
        try {
            int iCount = 1;
            long lBackupLastModified = 0;
            String strDbBackupFileName = "CopyOf_WeP_Retail_Database";
            String[] DirectoryFiles;
            File dbBackupDirectory = new File(DB_BACKUP_PATH);
            if (!dbBackupDirectory.exists()) {
                dbBackupDirectory.mkdir();
            }
            DirectoryFiles = dbBackupDirectory.list();
            for (int i = 0; i < DirectoryFiles.length; i++) {

                if (DirectoryFiles[i].contains(strDbBackupFileName)) {
                    iCount++;
                }
            }
            if (iCount > 1) {
                String strTemp = strDbBackupFileName + String.valueOf(iCount - 1) + ".db";
                File DbFile = new File(DB_BACKUP_PATH + strTemp);
                lBackupLastModified = DbFile.lastModified();

            }
            /*if (lDbLastModified == lBackupLastModified) {
                return 0;
            }*/
            strDbBackupFileName += String.valueOf(iCount);

            InputStream istrmDbFile = new FileInputStream(dbRetail.getPath());// returns the path where database is stored internally
            OutputStream ostrmBackUpDbFile = new FileOutputStream(DB_BACKUP_PATH + strDbBackupFileName);
            byte[] bFileBuffer = new byte[1024];
            int iBytesRead = 0;
            while ((iBytesRead = istrmDbFile.read(bFileBuffer)) > 0) {
                ostrmBackUpDbFile.write(bFileBuffer, 0, iBytesRead);
            }

            ostrmBackUpDbFile.flush();
            ostrmBackUpDbFile.close();
            istrmDbFile.close();
            return 1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public void setDefaultTableValues(SQLiteDatabase db) {//user HARDCODING
        ContentValues cnDbValues;
        cvDbValues = new ContentValues();
        cvDbValues.put("AccessLevel", "1");
        cvDbValues.put("Password", "admin");
        cvDbValues.put("UserId", "admin");
        cvDbValues.put("UserName", "administrator");
        db.insert(TBL_USER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_USER_ID, 1);
        cvDbValues.put(KEY_USER_NAME, "admin");
        cvDbValues.put(KEY_USER_MOBILE, "1234567890");
        cvDbValues.put(KEY_USER_DESIGNATION, "Owner");
        cvDbValues.put(KEY_ROLE_ID, 1);
        cvDbValues.put(KEY_USER_LOGIN, "admin");
        cvDbValues.put(KEY_USER_PASS, "admin");
        cvDbValues.put(KEY_USER_ADHAR, "Adhaar1");
        cvDbValues.put(KEY_USER_EMAIL, "wep@india.com");
        cvDbValues.put(KEY_USER_ADDRESS, "lavelle road");
        cvDbValues.put(KEY_USER_FILE_LOCATION, "xx");
        cvDbValues.put(KEY_SALES_MAN_ID,"");
        cvDbValues.put(KEY_isActive, 1);
        long status = 0;
        try {
            status = db.insert(TBL_USERS, null, cvDbValues);

        } catch (Exception e) {
            status = 0;
            Logger.d(TAG, e.toString());
            e.printStackTrace();
        }
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_USER_ID, 2);
        cvDbValues.put(KEY_USER_NAME, "d#demo");
        cvDbValues.put(KEY_USER_MOBILE, "1234567890");
        cvDbValues.put(KEY_USER_DESIGNATION, "d#demo");
        cvDbValues.put(KEY_ROLE_ID, 1);
        cvDbValues.put(KEY_USER_LOGIN, "d#demo");
        cvDbValues.put(KEY_USER_PASS, "d#demo");
        cvDbValues.put(KEY_USER_ADHAR, "Adhaar1");
        cvDbValues.put(KEY_USER_EMAIL, "wep@india.com");
        cvDbValues.put(KEY_USER_ADDRESS, "lavelle road");
        cvDbValues.put(KEY_USER_FILE_LOCATION, "xx");
        cvDbValues.put(KEY_SALES_MAN_ID,"");
        cvDbValues.put(KEY_isActive, 1);
        status = 0;
        try {
            status = db.insert(TBL_USERS, null, cvDbValues);

        } catch (Exception e) {
            status = 0;
            Logger.d(TAG, e.toString());
            e.printStackTrace();
        }

        try {
            long status1 = 0;

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "1");
            cvDbValues.put(KEY_ACCESS_CODE, "1");
            cvDbValues.put(KEY_ACCESS_NAME, "Billing");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);


            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "1");
            cvDbValues.put(KEY_ACCESS_CODE, "2");
            cvDbValues.put(KEY_ACCESS_NAME, "Item Master");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "1");
            cvDbValues.put(KEY_ACCESS_CODE, "3");
            cvDbValues.put(KEY_ACCESS_NAME, "Customer Master");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "1");
            cvDbValues.put(KEY_ACCESS_CODE, "4");
            cvDbValues.put(KEY_ACCESS_NAME, "User Management");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "1");
            cvDbValues.put(KEY_ACCESS_CODE, "5");
            cvDbValues.put(KEY_ACCESS_NAME, "Add Role");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "1");
            cvDbValues.put(KEY_ACCESS_CODE, "6");
            cvDbValues.put(KEY_ACCESS_NAME, "Price/Stock");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "1");
            cvDbValues.put(KEY_ACCESS_CODE, "7");
            cvDbValues.put(KEY_ACCESS_NAME, "Configuration");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "1");
            cvDbValues.put(KEY_ACCESS_CODE, "8");
            cvDbValues.put(KEY_ACCESS_NAME, "Supplier Details");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "1");
            cvDbValues.put(KEY_ACCESS_CODE, "9");
            cvDbValues.put(KEY_ACCESS_NAME, "Supplier Item Linkage");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "1");
            cvDbValues.put(KEY_ACCESS_CODE, "10");
            cvDbValues.put(KEY_ACCESS_NAME, "Purchase Order");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "1");
            cvDbValues.put(KEY_ACCESS_CODE, "11");
            cvDbValues.put(KEY_ACCESS_NAME, "Goods Inward Note");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "1");
            cvDbValues.put(KEY_ACCESS_CODE, "12");
            cvDbValues.put(KEY_ACCESS_NAME, "Reports");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "1");
            cvDbValues.put(KEY_ACCESS_CODE, "13");
            cvDbValues.put(KEY_ACCESS_NAME, "Settings");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "1");
            cvDbValues.put(KEY_ACCESS_CODE, "14");
            cvDbValues.put(KEY_ACCESS_NAME, "Day End");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

        } catch (Exception e) {
            status = 0;
            Logger.d(TAG, e.toString());
        }

        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_ROLE_ID, 1);
        cvDbValues.put(KEY_ROLE_NAME, "Manager");
        db.insert(TBL_USERSROLE, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_ROLE_ID, 2);
        cvDbValues.put(KEY_ROLE_NAME, "Accountant");
        db.insert(TBL_USERSROLE, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_ROLE_ID, 3);
        cvDbValues.put(KEY_ROLE_NAME, "Operator");
        db.insert(TBL_USERSROLE, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_ROLE_ID, 4);
        cvDbValues.put(KEY_ROLE_NAME, "Salesman");
        db.insert(TBL_USERSROLE, null, cvDbValues);


        cvDbValues = new ContentValues();
        cvDbValues.put("WeighScale", 0);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String timeStamp = dateFormat.format(date);
        cvDbValues.put("BusinessDate", timeStamp);
        cvDbValues.put("FooterText1", "Thank You");
        cvDbValues.put("FooterText2", "Visit Again");
        cvDbValues.put("FooterText3", "");
        cvDbValues.put("FooterText4", "");
        cvDbValues.put("FooterText5", "");
        cvDbValues.put("HeaderText1", "Retail Centre");
        cvDbValues.put("HeaderText2", "Bangalore");
        cvDbValues.put("HeaderText3", "");
        cvDbValues.put("HeaderText4", "");
        cvDbValues.put("HeaderText5", "");
        cvDbValues.put("DateAndTime", 1);
        cvDbValues.put("PriceChange", 0);
        cvDbValues.put(KEY_BillwithStock , 0);
        cvDbValues.put(KEY_ShortCodeReset, 1);
        cvDbValues.put(KEY_BILL_NO_RESET, 0);
        cvDbValues.put(KEY_PrintOwnerDetail, 0); // Disabled
        cvDbValues.put(KEY_RoundOff, 0); // Disabled
        cvDbValues.put(KEY_SALES_MAN_ID, 0); // Disabled
        cvDbValues.put(KEY_Tax, 1);
        cvDbValues.put(KEY_DiscountType, 0); // 1 itemwise, 0 billwise
        cvDbValues.put(KEY_DYNAMIC_DISCOUNT, 0); // 1 ENABLE, 0 DISABLE
        cvDbValues.put(KEY_PRINT_MRP_RETAIL_DIFFERENCE, 0); // 1 ENABLE, 0 DISABLE
        cvDbValues.put(KEY_FastBillingMode, 0); // item
        cvDbValues.put(KEY_CummulativeHeadingEnable, 1); // richa_2012

        cvDbValues.put(KEY_POS, 1);
        cvDbValues.put(KEY_HSNCode, 1);

        cvDbValues.put(KEY_UTGSTEnabled, 0); // disabling
        cvDbValues.put(KEY_HSNPrintEnabled_out, 0); // Disabled

        cvDbValues.put(KEY_HeaderBold, 0); // Disabled
        cvDbValues.put(KEY_PrintService, 0); // Disabled
        cvDbValues.put(KEY_Environment , 1) ; // PROduction
        cvDbValues.put(KEY_RewardPoints, 0);        status = 0;
        try {
            status = db.insert(TBL_BILLSETTING, null, cvDbValues);
        } catch (Exception e) {
            status = 0;

            Logger.d(TAG, e.toString());
            Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Bill No Reset Configuration
        cvDbValues = new ContentValues();
        cvDbValues.put("InvoiceNo", 0);
        cvDbValues.put("Period", "Disable");
        cvDbValues.put("PeriodDate", "");
        cvDbValues.put("KOTNo", 0);
        db.insert(TBL_BILLNORESETCONFIG, null, cvDbValues);


        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_AmtToPt, 0.00);
        cvDbValues.put(KEY_RewardPoints, 0);
        cvDbValues.put(KEY_RewardPointsToAmt , 0.00);
        cvDbValues.put(KEY_RewardPointsLimit,0);
        db.insert(TBL_REWARDPOINTSCONGFIGURATION, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_RAZORPAY_KEYID, "xx");
        cvDbValues.put(KEY_RAZORPAY_SECRETKEY, "xx"); // Production

        cvDbValues.put(KEY_AEPS_AppId, "xx");
        cvDbValues.put(KEY_AEPS_MerchantId, "xx"); // Production
        cvDbValues.put(KEY_AEPS_SecretKey, "xx");

        try {
            db.insert(TBL_PaymentModeConfiguration, null, cvDbValues);
        } catch (Exception ex){
            Logger.e(TAG, "Unable to insert default value into payment mode configuration table. " +ex.getMessage());
        }

        //temporary default data for testing item master.
        /*for(int i = 0; i < 10; i++) {
            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ShortCode, i);
            cvDbValues.put(KEY_ItemShortName, "test" + i);
            cvDbValues.put(KEY_ItemLongName, "testing" + i);
            cvDbValues.put(KEY_ItemBarcode, "123456789" + i);
            cvDbValues.put(KEY_SupplyType, "G");
            cvDbValues.put(KEY_UOM, "Packet(Pk)");
            cvDbValues.put(KEY_BrandCode, 1 + i);
            cvDbValues.put(KEY_DepartmentCode, 1 + i);
            cvDbValues.put(KEY_CategoryCode, 1 + i);
            cvDbValues.put(KEY_RetailPrice, 2 + i);
            cvDbValues.put(KEY_MRP, 2 + i);
            cvDbValues.put(KEY_WholeSalePrice, 2 + i);
            cvDbValues.put(KEY_Quantity, 10);
            cvDbValues.put(KEY_HSNCode, "12345" + i);
            cvDbValues.put(KEY_DiscountPercent, 5);
            cvDbValues.put(KEY_DiscountAmount, 10);
            cvDbValues.put(KEY_CGSTRate, i);
            cvDbValues.put(KEY_SGSTRate, 0);
            cvDbValues.put(KEY_IGSTRate, 0);
            cvDbValues.put(KEY_cessRate, 0);
            cvDbValues.put(KEY_ImageUri, "");
            cvDbValues.put(KEY_isFavrouite, 1);
            cvDbValues.put(KEY_isActive, 1);
            status = 0;
            try {
                status = db.insert(TBL_Item, null, cvDbValues);

            } catch (Exception e) {
                status = 0;
                Logger.d(TAG, e.toString());
                e.printStackTrace();
            }
        }*/
    }


    public void RestoreDefault()
    {

        cvDbValues = new ContentValues();
        cvDbValues.put("WeighScale", 0);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String timeStamp = dateFormat.format(date);
        cvDbValues.put("BusinessDate", timeStamp);
        cvDbValues.put("FooterText1", "Thank You");
        cvDbValues.put("FooterText2", "Visit Again");
        cvDbValues.put("FooterText3", "");
        cvDbValues.put("FooterText4", "");
        cvDbValues.put("FooterText5", "");
        cvDbValues.put("HeaderText1", "Retail Centre");
        cvDbValues.put("HeaderText2", "Bangalore");
        cvDbValues.put("HeaderText3", "");
        cvDbValues.put("HeaderText4", "");
        cvDbValues.put("HeaderText5", "");
        cvDbValues.put("DateAndTime", 1);
        cvDbValues.put("PriceChange", 0);
        cvDbValues.put(KEY_BillwithStock , 0);
        cvDbValues.put(KEY_ShortCodeReset, 1);
        cvDbValues.put(KEY_BILL_NO_RESET, 0);
        cvDbValues.put(KEY_PrintOwnerDetail, 0); // Disabled
        cvDbValues.put(KEY_RoundOff, 0); // Disabled
        cvDbValues.put(KEY_SALES_MAN_ID, 0); // disabled
        cvDbValues.put(KEY_Tax, 1);
        cvDbValues.put(KEY_DiscountType, 0); // 1 itemwise, 0 billwise
        cvDbValues.put(KEY_DYNAMIC_DISCOUNT, 0); // 1 ENABLE, 0 DISABLE
        cvDbValues.put(KEY_PRINT_MRP_RETAIL_DIFFERENCE, 0); // 1 ENABLE, 0 DISABLE
        cvDbValues.put(KEY_FastBillingMode, 0);
        cvDbValues.put(KEY_CummulativeHeadingEnable, 1); // richa_2012

        cvDbValues.put(KEY_POS, 1);
        cvDbValues.put(KEY_HSNCode, 1);

        cvDbValues.put(KEY_UTGSTEnabled, 0); // disabling
        cvDbValues.put(KEY_HSNPrintEnabled_out, 0); // Disabled

        cvDbValues.put(KEY_HeaderBold, 0); // Disabled
        cvDbValues.put(KEY_PrintService, 0); // Disabled
        cvDbValues.put(KEY_Environment , 1) ; // PROduction
        cvDbValues.put(KEY_RewardPoints, 0);
        long status = 0;
        try {
            status = dbRetail.update(TBL_BILLSETTING, cvDbValues,null, null);
        } catch (Exception e) {
            status = 0;

            Logger.d(TAG, e.toString());
            Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    // update Business date
    public int updateBusinessDate(String NextDate) {
        cvDbValues = new ContentValues();
        cvDbValues.put("BusinessDate", NextDate);
        return dbRetail.update(TBL_BILLSETTING, cvDbValues, null, null);
    }

    // -----Retrieve Business Date-----
    public Cursor getBusinessDate() {
        return dbRetail.query(TBL_BILLSETTING, new String[]{"BusinessDate"}, null, null, null, null, null);

    }

    // Temp functions - Header-Footer Text update
    public int updateHeaderFooterText(String strHeader1, String strHeader2, String strHeader3, String strHeader4, String strHeader5,
                                      String strFooter1, String strFooter2, String strFooter3, String strFooter4, String strFooter5) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("FooterText1", strFooter1);
        contentValues.put("FooterText2", strFooter2);
        contentValues.put("FooterText3", strFooter3);
        contentValues.put("FooterText4", strFooter4);
        contentValues.put("FooterText5", strFooter5);
        contentValues.put("HeaderText1", strHeader1);
        contentValues.put("HeaderText2", strHeader2);
        contentValues.put("HeaderText3", strHeader3);
        contentValues.put("HeaderText4", strHeader4);
        contentValues.put("HeaderText5", strHeader5);
        return dbRetail.update(TBL_BILLSETTING, contentValues, null, null);
    }

    // -----Retrieve Bill setting-----
    public Cursor getBillSettings() {
        //SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = dbRetail.query(TBL_BILLSETTING, new String[]{"*"}, null, null, null, null, null);
        }catch (Exception e){
            Logger.e(TAG,e.getMessage());

        }finally {
            //db.close();
        }
        return cursor;
    }

    public int updateGSTSettings(BillSetting objBillSetting) {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_POS, objBillSetting.getPOS());
        cvDbValues.put(KEY_HSNCode, objBillSetting.getHSNCode());
        cvDbValues.put(KEY_UTGSTEnabled, objBillSetting.getUTGSTEnabled_out());
        cvDbValues.put(KEY_HSNPrintEnabled_out, objBillSetting.getHSNPrintenabled_out());
        return dbRetail.update(TBL_BILLSETTING, cvDbValues, null, null);
    }

    // Temp functions - Other settings update
    public int updateOtherSettings(BillSetting objBillSetting) {
        cvDbValues = new ContentValues();

        cvDbValues.put("DateAndTime", objBillSetting.getiDateAndTime());
        cvDbValues.put("PriceChange", objBillSetting.getiPriceChange());
        cvDbValues.put(KEY_BillwithStock , objBillSetting.getiBillwithStock());
        cvDbValues.put(KEY_ShortCodeReset, objBillSetting.getiItemNoReset());
        cvDbValues.put(KEY_BILL_NO_RESET,objBillSetting.getiBillNoDailyReset());
        cvDbValues.put(KEY_PrintOwnerDetail, objBillSetting.getPrintOwnerDetail());
        cvDbValues.put(KEY_RoundOff, objBillSetting.getBillAmountRounfOff());
        cvDbValues.put(KEY_SALES_MAN_ID, objBillSetting.getiSalesManId());
        cvDbValues.put(KEY_Tax, objBillSetting.getiTax());
        cvDbValues.put(KEY_DiscountType, objBillSetting.getiDiscountType());
        //cvDbValues.put(KEY_DYNAMIC_DISCOUNT, objBillSetting.getiDynamicDiscount());
        cvDbValues.put(KEY_PRINT_MRP_RETAIL_DIFFERENCE,objBillSetting.getiPrintDiscountDifference());
        cvDbValues.put(KEY_FastBillingMode, objBillSetting.getFastBillingMode());
        cvDbValues.put(KEY_CummulativeHeadingEnable, objBillSetting.getCummulativeHeadingEnable()); // richa_2012
        cvDbValues.put(KEY_RewardPoints, objBillSetting.getiLoyaltyPoints());
        cvDbValues.put(KEY_HeaderBold, objBillSetting.getBoldHeader());
        cvDbValues.put(KEY_PrintService, objBillSetting.getPrintService());


        return dbRetail.update(TBL_BILLSETTING, cvDbValues, null, null);
    }



    // TBL_BillNoConfiguration
    // -----Retrieve new bill Number-----
    public int getNewBillNumber() {
        Cursor result;
        result = dbRetail.rawQuery("SELECT " + KEY_InvoiceNo + " FROM BillNoConfiguration", null);

        if (result.moveToFirst()) {
            return result.getInt(0) + 1;
        } else {
            return 1;
        }
    }

    public int UpdateBillNoResetInvoiceNos(int invno){
        int result =0;
        try{
            cvDbValues = new ContentValues();
            cvDbValues.put("InvoiceNo", invno);
            result= dbRetail.update("BillNoConfiguration", cvDbValues, null, null);
        }catch (Exception e){
            e.printStackTrace();
            result = 0;
        }finally {
            //db.close();
            return result;
        }
    }
    // -----Retrieve Bill setting-----
    public Cursor getBillNoResetSetting() {
        return dbRetail.query("BillNoConfiguration", new String[]{"*"}, null, null, null, null, null);
    }

    public int UpdateBillNoResetInvoiceNo(int invno) {
        cvDbValues = new ContentValues();
        cvDbValues.put("InvoiceNo", invno);

        return dbRetail.update("BillNoConfiguration", cvDbValues, null, null);
    }

    public int UpdateBillNoReset(String period) {
        Date d = new Date();
        CharSequence s = android.text.format.DateFormat.format("dd-MM-yyyy", d.getTime());
        Calendar cal = Calendar.getInstance(); //adding one day to current date cal.add(Calendar.DAY_OF_MONTH, 1); Date tommrrow = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date tommrrow = cal.getTime();
        CharSequence s1 = android.text.format.DateFormat.format("dd-MM-yyyy", tommrrow.getTime());

        cvDbValues = new ContentValues();
        cvDbValues.put("InvoiceNo", 0);
        cvDbValues.put("Period", period);
        cvDbValues.put("PeriodDate", s1.toString());

        return dbRetail.update("BillNoConfiguration", cvDbValues, null, null);
    }
    public int UpdateBillNoResetPeriod(String period) {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_BillNoReset_Period, period);

        return dbRetail.update("BillNoConfiguration", cvDbValues, null, null);
    }
    public int UpdateBillNoResetwithDate(String period, String date , int invoiceNo) {
//        Date d = new Date();
//        CharSequence s = android.text.format.DateFormat.format("dd-MM-yyyy", d.getTime());
//        Calendar cal = Calendar.getInstance(); //adding one day to current date cal.add(Calendar.DAY_OF_MONTH, 1); Date tommrrow = cal.getTime();
//        cal.add(Calendar.DAY_OF_MONTH, 1);
//        Date tommrrow = cal.getTime();
//        CharSequence s1 = android.text.format.DateFormat.format("dd-MM-yyyy", tommrrow.getTime());

        cvDbValues = new ContentValues();
        cvDbValues.put("InvoiceNo", invoiceNo);
        cvDbValues.put("Period", period);
        cvDbValues.put("PeriodDate", date);

        return dbRetail.update("BillNoConfiguration", cvDbValues, null, null);
    }


    // TBL_Owner
    public Cursor getOwnerDetail() {
        String Selectquery = "Select * FROM " + TBL_OWNER_DETAILS ;
        Cursor result = dbRetail.rawQuery(Selectquery, null);

        return result;

    }
    public String getOwnerPOS() {
        String pos = "";
        String selectQuery = "Select POS FROM " + TBL_OWNER_DETAILS;
        Cursor cursor = null;
        try{
            Cursor result = dbRetail.rawQuery(selectQuery, null);
            if (result != null && result.moveToFirst()) {
                pos = result.getString(result.getColumnIndex("POS"));
            }
        }catch (Exception e){
            e.printStackTrace();
            pos = "0";
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return pos;
    }

    public long addOwnerDetails(String name, String gstin, String phone, String email, String address, String pos, String office, String RefernceNo, String billPrefix)
    {
        long status = 0;
        ContentValues cvDbValues = new ContentValues();
        cvDbValues.put(KEY_Owner_Name,name);
        cvDbValues.put(KEY_GSTIN,gstin);
        cvDbValues.put(KEY_PhoneNo,phone);
        cvDbValues.put(KEY_USER_EMAIL,email);
        cvDbValues.put(KEY_USER_ADDRESS,address);
        cvDbValues.put(KEY_POS,pos);
        cvDbValues.put(KEY_IsMainOffice,office);
        cvDbValues.put(KEY_REFERENCE_NO,RefernceNo);
        cvDbValues.put(KEY_BillNoPrefix,billPrefix);
        cvDbValues.put(KEY_FIRM_NAME, name);

        cvDbValues.put(KEY_TINCIN, "1234567890");
        cvDbValues.put(KEY_IsMainOffice, "YES");


        try {
            status = dbRetail.insert(TBL_OWNER_DETAILS, null, cvDbValues);
            //Log.d(TAG,"code "+status);
            if (status > 0) {

            }
            //Log.d(TAG,"Inserted Successfully with code "+status);
        } catch (Exception e) {
            status = 0;
            Logger.e(TAG,e.getMessage());
        }
        return status;

    }

    public  int deleteOwnerDetails()
    {
        return  dbRetail.delete(TBL_OWNER_DETAILS,null,null);
    }
    public int updateOwnerDetails(String BillNoPrefix,String gstin, String referenceNo)
    {
        int result =0;
        try{
            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_BillNoPrefix, BillNoPrefix);
            cvDbValues.put(KEY_GSTIN, gstin);
            cvDbValues.put(KEY_REFERENCE_NO, referenceNo);
            result= dbRetail.update(TBL_OWNER_DETAILS, cvDbValues, null, null);
        }catch (Exception e){
            e.printStackTrace();
            result = 0;
        }finally {
            //db.close();
            return result;
        }
    }



    public String getBillNoPrefix() {
        String billNoPrefix = "";
//        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "Select BillNoPrefix FROM " + TBL_OWNER_DETAILS;
        try {
            Cursor result = dbRetail.rawQuery(selectQuery, null);
            if (result != null && result.moveToFirst()) {
                billNoPrefix = result.getString(result.getColumnIndex(KEY_BillNoPrefix));
                if (billNoPrefix == null)
                    billNoPrefix = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            billNoPrefix = "";
        }
        return billNoPrefix.trim();
    }
    /************************************************************************************************************************************/
    /******************************************************
     * Table - Department
     **********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Department-----
    public long addDepartment(Department objDept) {
        long lResult = -1;
        try {
            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_DepartmentCode, objDept.getDeptCode());
            cvDbValues.put(KEY_DepartmentName, objDept.getDeptName());
            lResult = dbRetail.insert(TBL_DEPARTMENT, null, cvDbValues);
        }catch (Exception ex){
            Logger.i(TAG,ex.getMessage());
        } finally {
            cvDbValues.clear();
        }
        return lResult;
    }

    // -----Retrieve all Departments-----
    public Cursor getAllDepartments() {
        return dbRetail.query(TBL_DEPARTMENT, new String[]{"*"}, null, null, null, null, null);
    }

    public Cursor getDepartments() {
        return dbRetail.rawQuery("Select "+KEY_DepartmentCode+" as _id, "+KEY_DepartmentName+" from "+TBL_DEPARTMENT, null);
    }

    public int getDepartmentIdByName(String name) {
        int id = -1;
        Cursor cursor = null;
        try {
            cursor = dbRetail.query(TBL_DEPARTMENT, null, KEY_DepartmentName + "=?", new String[]{String.valueOf(name)}, null, null, null, null);
            //if (cursor != null)
            if (cursor.moveToFirst()) {
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_DepartmentCode)));
            } else {
                id = 0;
            }
        } catch (Exception e) {
            Logger.d(TAG, e.toString());
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }

        // return contact
        return id;
    }

    // -----Retrieve single Department-----
    public Cursor getDepartment(int DeptCode) {
        try {
            return dbRetail.query(TBL_DEPARTMENT, new String[]{KEY_DepartmentCode, KEY_DepartmentName}, KEY_DepartmentCode+"=" + DeptCode, null, null, null, null);
        } catch (Exception e) {
            Logger.d(TAG, e.toString());
            return null;
        }
    }

    // -----Retrieve single Department-----
    public Cursor getDepartment(String DeptName) {
        return dbRetail.query(TBL_DEPARTMENT, new String[]{KEY_DepartmentCode, KEY_DepartmentName}, KEY_DepartmentName+"=" + DeptName, null, null,
                null, null);
    }

    // -----Retrieve highest DeptCode from table-----
    public int getDeptCode() {
        Cursor result = null;
        int iCount = 0;
        try {
            result = dbRetail.rawQuery("SELECT MAX("+KEY_DepartmentCode+") FROM " + TBL_DEPARTMENT, null);

            if (result.moveToFirst()) {
                iCount =  result.getInt(0);
            }
        } catch (Exception ex){
            Logger.d(TAG,"Unable to get the max dept code count from department table. " +ex.getMessage());
        } finally {
            if(result != null){
                result.close();
            }
        }
        return iCount;
    }

    // -----Update Department table----
    public int updateDepartment(String strDeptCode, String strDeptName) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_DepartmentName, strDeptName);

        return dbRetail.update(TBL_DEPARTMENT, cvDbValues, KEY_DepartmentCode+"=" + strDeptCode, null);
    }
    public Cursor getDepartmentNameByName(String strDeptName) {
        String query = "Select * from "+TBL_DEPARTMENT+" WHERE "+KEY_DepartmentName+"  LIKE '"+strDeptName+"'";
        return dbRetail.rawQuery(query,null);
    }

    // -----Delete Department from Dept Table-----
    public int DeleteDept(int DeptCode) {

        return dbRetail.delete(TBL_DEPARTMENT, KEY_DepartmentCode+"=" + DeptCode, null);
    }

    /************************************************************************************************************************************/
    /*******************************************************
     * Table - Category
     ***********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Category-----
    public long addCategory(Category objCateg) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_CategoryCode, objCateg.getiCategCode());
        cvDbValues.put(KEY_CategoryName, objCateg.getStrCategName());
        cvDbValues.put(KEY_DepartmentCode, objCateg.getiDeptCode());

        return dbRetail.insert(TBL_CATEGORY, null, cvDbValues);
    }

    // -----Retrieve all Category-----
    public Cursor getAllCategory() {
        return dbRetail.query(TBL_CATEGORY, new String[]{"*"}, null, null, null, null,
                null);
    }

    // -----Retrieve all Category-----
    public Cursor getAllCategorywithDeptName() {
        return dbRetail.rawQuery("Select * from " + TBL_CATEGORY + ", " + TBL_DEPARTMENT + " where " + TBL_DEPARTMENT
                + "."+KEY_DepartmentCode+" = " + TBL_CATEGORY + "."+KEY_DepartmentCode, null);
    }

    public Cursor getCategories() {
        return dbRetail.rawQuery("Select "+KEY_CategoryCode+" as _id, "+KEY_CategoryName+", +"+KEY_DepartmentCode+" from Category", null);
    }

    public Cursor getCategoryByDept(int deptcode) {
        return dbRetail.rawQuery("Select "+KEY_CategoryCode+" , "+KEY_CategoryName+", "+KEY_DepartmentCode+" from Category where "+KEY_DepartmentCode+"=" + deptcode, null);
    }

    public int getCategoryIdByName(String name) {
        int id = -1;
        Cursor cursor = null;
        try {
            cursor = dbRetail.query(TBL_CATEGORY, null, KEY_CategoryName+"=?", new String[]{String.valueOf(name)}, null, null, null, null);
            //if (cursor != null)
            if (cursor.moveToFirst()) {
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_CategoryCode)));
            }
        } catch (Exception e) {
            Logger.d(TAG, e.toString());
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        // return contact
        return id;
    }

    public ArrayList<String> getCategoryNameByDeptCode(String name) {
        String categname = "";
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = null;
        try {
            cursor = dbRetail.query(TBL_CATEGORY, null, KEY_DepartmentCode+"=?", new String[]{String.valueOf(name)}, null, null, null, null);
            list.add("Select department first");
            while (cursor != null && cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex(KEY_CategoryName)));
            }
        } catch (Exception e) {
            Logger.d(TAG, e.toString());
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return list;
    }

    public List<String> getAllCategforDept() {
        List<String> list = new ArrayList<String>();
        Cursor cursor = null;
        try {
            cursor = dbRetail.rawQuery("SELECT  "+KEY_CategoryCode+" as _id, "+KEY_CategoryName+", "+KEY_DepartmentCode+" FROM Category", null);// selectQuery,selectedArguments

            list.add("Select");
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(1));// adding 2nd column data
                } while (cursor.moveToNext());
            }
        }catch (Exception ex){
            Logger.d(TAG,ex.getMessage());
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return list;
    }

    // -----Retrieve single Category-----
    public Cursor getCategory(int CategCode) {
        return dbRetail.query(TBL_CATEGORY, new String[]{KEY_CategoryCode, KEY_CategoryName}, KEY_CategoryCode+"=" + CategCode, null,
                null, null, null);
    }

    // -----Retrieve highest CategCode from table-----
    public int getCategCode() {
        Cursor result = null;
        int iCount = 0;
        try {
            result = dbRetail.rawQuery("SELECT MAX("+KEY_CategoryCode+") FROM " + TBL_CATEGORY, null);
            if (result.moveToFirst()) {
                iCount = result.getInt(0);
            }
        }catch (Exception ex){
            Logger.d(TAG,ex.getMessage());
        }finally {
            if(result != null){
                result.close();
            }
        }
        return iCount;
    }

    // -----Update Category table----
    public int updateCategory(String strCategCode, String strCategName, int iDeptCode) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_CategoryName, strCategName);
        cvDbValues.put(KEY_DepartmentCode, iDeptCode);

        return dbRetail.update(TBL_CATEGORY, cvDbValues, KEY_CategoryCode+"=" + strCategCode, null);
    }

    public List<Department> getAllDeptforCateg() {
        List<Department> list = new ArrayList<Department>();
        Cursor cursor = null;
        list.clear();
        try {
            cursor = dbRetail.rawQuery("SELECT  * FROM Department Order By "+KEY_DepartmentName+" ASC", null);// selectQuery,selectedArguments
            Department department = new Department();
            department.setDeptCode(0);
            department.setDeptName("Select");
            list.add(department);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Department dept = new Department();
                    dept.setiID(cursor.getInt(cursor.getColumnIndex(KEY_id)));
                    dept.setDeptCode(cursor.getInt(cursor.getColumnIndex(KEY_DepartmentCode)));
                    dept.setDeptName(cursor.getString(cursor.getColumnIndex(KEY_DepartmentName)));
                    list.add(dept);
                } while (cursor.moveToNext());
            }
        }catch (Exception ex){
            Logger.d(TAG,ex.getMessage());
        }
        return list;
    }

    // -----Delete Category
    public int DeleteCateg(int CategCode) {

        return dbRetail.delete(TBL_CATEGORY, KEY_CategoryCode+"=" + CategCode, null);
    }

    // -----Delete Category by Dept Code
    public int DeleteCategByDeptCode(int DeptCode) {

        return dbRetail.delete(TBL_CATEGORY, KEY_DepartmentCode+"=" + DeptCode, null);
    }



    public void CloseDatabase() {
        // Close database connection
        while (true) {
            if (dbRetail != null && dbRetail.isOpen()) {
                dbRetail.close();
            } else {
                break;
            }
        }
    }


    /************************************************************************************************************************************/
    /******************************************************
     * Table - Brand
     **********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Brand-----
    public long addBrand(ConfigBean configBean) {
        long lResult = -1;
        try {
            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_BRAND_CODE, configBean.getlBrandCode());
            cvDbValues.put(KEY_BRAND_NAME, configBean.getStrBrandName());
            lResult = dbRetail.insert(TBL_BRAND, null, cvDbValues);
        }catch (Exception ex){
            Logger.i(TAG,ex.getMessage());
        } finally {
            cvDbValues.clear();
        }
        return lResult;
    }

    // -----Retrieve all Brand-----
    public Cursor getAllBrands() {
        return dbRetail.query(TBL_BRAND, new String[]{"*"}, null, null, null, null, null);
    }

    public Cursor getBrand() {
        return dbRetail.rawQuery("Select * from " + TBL_BRAND, null);
    }

    public int getBrandIdByName(String name) {
        int id = -1;
        Cursor cursor = null;
        try {
            cursor = dbRetail.query(TBL_BRAND, null, KEY_BRAND_NAME + "=?", new String[]{String.valueOf(name)}, null, null, null, null);
            //if (cursor != null)
            if (cursor.moveToFirst()) {
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_BRAND_CODE)));
            } else {
                id = 0;
            }
        } catch (Exception e) {
            Logger.d(TAG, e.toString());
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return id;
    }

    // -----Retrieve single Brand-----
    public Cursor getBrand(int iBrandCode) {
        try {
            return dbRetail.query(TBL_BRAND, new String[]{KEY_id, KEY_BRAND_CODE, KEY_BRAND_NAME}, KEY_BRAND_CODE + "=" + iBrandCode, null, null, null, null);
        } catch (Exception e) {
            Logger.d(TAG, e.toString());
            return null;
        }
    }

    // -----Retrieve single bRAND-----
    public Cursor getbRAND(String strBrandName) {
        return dbRetail.query(TBL_BRAND, new String[]{KEY_id, KEY_BRAND_CODE, KEY_BRAND_NAME}, KEY_BRAND_NAME +"=" + strBrandName, null, null,
                null, null);
    }

    // -----Retrieve highest DeptCode from table-----
    public int getBrandCode() {
        Cursor result = null;
        int iCount = 0;
        try {
            result = dbRetail.rawQuery("SELECT MAX("+KEY_BRAND_CODE+") FROM " + TBL_BRAND, null);

            if (result.moveToFirst()) {
                iCount =  result.getInt(0);
            }
        } catch (Exception ex){
            Logger.d(TAG,"Unable to get the max brand code count from brand table. " +ex.getMessage());
        } finally {
            if(result != null){
                result.close();
            }
        }
        return iCount;
    }

    // -----Update Brand table----
    public int updateBrand(String strBrandCode, String strBrandName) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_BRAND_NAME, strBrandName);

        return dbRetail.update(TBL_BRAND, cvDbValues, KEY_BRAND_CODE + "=" + strBrandCode, null);
    }
    public Cursor getBrandNameByName(String strBrandName) {
        String query = "Select * from "+TBL_BRAND+" WHERE "+KEY_BRAND_NAME+" LIKE '"+strBrandName+"'";
        return dbRetail.rawQuery(query,null);
    }

    // -----Delete Brand from Brand Table-----
    public int DeleteBrand(int strBrandCode) {

        return dbRetail.delete(TBL_BRAND, KEY_BRAND_CODE +"=" + strBrandCode, null);
    }

    /************************************************************************************************************************************/
    /*****************************************************
     * Table - Description
     **********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Description-----
    public long addDescription(Description objDescription) {
        cvDbValues = new ContentValues();

        cvDbValues.put("DescriptionId", objDescription.getDescriptionId());
        cvDbValues.put("DescriptionText", objDescription.getDescriptionText());

        return dbRetail.insert(TBL_DESCRIPTION, null, cvDbValues);
    }

    // -----Retrieve all Description-----
    public Cursor getAllDescription() {
        return dbRetail.query(TBL_DESCRIPTION, new String[]{"*"}, null, null, null, null,
                null);
    }

    // -----Retrieve single Description-----
    public Cursor getDescription(int DescriptionId) {
        return dbRetail.query(TBL_DESCRIPTION, new String[]{"DescriptionId", "DescriptionText"},
                "DescriptionId=" + DescriptionId, null, null, null, null);
    }

    // -----Retrieve highest DescriptionId from table-----
    public int getDescriptionId() {
        Cursor result;
        result = dbRetail.rawQuery("SELECT MAX(DescriptionId) FROM " + TBL_DESCRIPTION, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update Description table----
    public int updateDescription(String strDescriptionId, String strDescriptionText) {
        cvDbValues = new ContentValues();

        cvDbValues.put("DescriptionText", strDescriptionText);

        return dbRetail.update(TBL_DESCRIPTION, cvDbValues, "DescriptionId=" + strDescriptionId, null);
    }

    public Cursor getPaymentDescription(String strDescriptionText) {
        String query  = " Select * from "+TBL_DESCRIPTION+" WHERE DescriptionText like '"+strDescriptionText+"'";
        return dbRetail.rawQuery(query, null);
    }

    // -----Delete Payment Descriptiom
    public int DeleteDescription(int DescriptionId) {
        return dbRetail.delete(TBL_DESCRIPTION, "DescriptionId=" + DescriptionId, null);
    }

    /************************************************************************************************************************************/
    /****************************************************
     * Table - DiscountConfig
     ********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Discount configuration-----
    public long addDiscountConfig(DiscountConfig objDiscount) {
        cvDbValues = new ContentValues();

        cvDbValues.put("DiscDescription", objDiscount.getDiscDescription());
        cvDbValues.put("DiscId", objDiscount.getDiscId());
        cvDbValues.put("DiscPercentage", objDiscount.getDiscPercentage());
        cvDbValues.put("DiscAmount", objDiscount.getDiscAmount());

        return dbRetail.insert(TBL_DISCOUNTCONFIG, null, cvDbValues);
    }

    // -----Retrieve all DiscountConfig-----
    public Cursor getAllDiscountConfig() {
        return dbRetail.query(TBL_DISCOUNTCONFIG, new String[]{"*"}, null,
                null, null, null, null);
    }

    // -----Retrieve single DiscountConfig-----
    public Cursor getDiscountConfig(int DiscId) {
        return dbRetail.query(TBL_DISCOUNTCONFIG, new String[]{"DiscId", "DiscDescription", "DiscPercentage", "DiscAmount"},
                "DiscId=" + DiscId, null, null, null, null);
    }

    // -----Retrieve single DiscountConfig-----
    public Cursor getDiscountValue(String DiscName) {
        return dbRetail.query(TBL_DISCOUNTCONFIG, new String[]{"DiscId", "DiscDescription", "DiscPercentage", "DiscAmount"},
                "DiscDescription='" + DiscName + "'", null, null, null, null);
    }

    // -----Retrieve highest DiscId from table-----
    public int getDiscountId() {
        Cursor result;
        result = dbRetail.rawQuery("SELECT MAX(DiscId) FROM " + TBL_DISCOUNTCONFIG, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update DiscountConfig table----
    public int updateDiscountConfig(long lDiscID, String strDiscDescription, String strDiscPercent, String strDiscAmt) {
        cvDbValues = new ContentValues();

        cvDbValues.put("DiscDescription", strDiscDescription);
        cvDbValues.put("DiscPercentage", strDiscPercent);
        cvDbValues.put("DiscAmount", strDiscAmt);
        return dbRetail.update(TBL_DISCOUNTCONFIG, cvDbValues, "DiscId=" + lDiscID, null);
    }


    // -----Delete Discount
    public int DeleteDiscount(int DiscId) {
        return dbRetail.delete(TBL_DISCOUNTCONFIG, "DiscId=" + DiscId, null);
    }

    /************************************************************************************************************************************/
    /********************************************************
     * Table - Coupon
     ************************************************************/
    /************************************************************************************************************************************/
    // -----Insert Coupon configuration-----
    public long addCoupon(Coupon objCoupon) {
        cvDbValues = new ContentValues();

        cvDbValues.put("CouponId", objCoupon.getCouponId());
        cvDbValues.put("CouponDescription", objCoupon.getCouponDescription());
        cvDbValues.put("CouponBarcode", objCoupon.getCouponBarcode());
        cvDbValues.put("CouponAmount", objCoupon.getCouponAmount());

        return dbRetail.insert(TBL_COUPON, null, cvDbValues);
    }

    // -----Retrieve all Coupon-----
    public Cursor getAllCoupon() {
        return dbRetail.query(TBL_COUPON,
                new String[]{"*"}, null, null, null,
                null, null);
    }

    // -----Retrieve single Coupon based on CouponId-----
    public Cursor getCoupon(int CouponId) {
        return dbRetail.query(TBL_COUPON,
                new String[]{"CouponId", "CouponDescription", "CouponBarcode", "CouponAmount"},
                "CouponId=" + CouponId, null, null, null, null);
    }

    // -----Retrieve single CouponConfig-----
    public Cursor getCouponValue(String CouponName) {
        return dbRetail.query(TBL_COUPON, new String[]{"CouponId", "CouponDescription", "CouponBarcode", "CouponAmount"},
                "CouponDescription='" + CouponName + "'", null, null, null, null);
    }

    // -----Retrieve single Coupon based on Barcode-----
    public Cursor getCoupon(String CouponBarcode) {
        return dbRetail.query(TBL_COUPON,
                new String[]{"CouponId", "CouponDescription", "CouponBarcode", "CouponAmount"},
                "CouponBarcode=" + CouponBarcode, null, null, null, null);
    }

    // -----Retrieve highest CouponId from table-----
    public int getCouponId() {
        Cursor result;
        result = dbRetail.rawQuery("SELECT MAX(CouponId) FROM " + TBL_COUPON, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update Coupon table----
    public int updateCoupon(long lCouponID, String strCouponDescription, String strCouponBarcode,
                            String strCouponAmount) {
        cvDbValues = new ContentValues();

        cvDbValues.put("CouponDescription", strCouponDescription);
        cvDbValues.put("CouponBarcode", strCouponBarcode);
        cvDbValues.put("CouponAmount", strCouponAmount);

        return dbRetail.update(TBL_COUPON, cvDbValues, "CouponId=" + lCouponID, null);
    }

    // -----Delete Coupon
    public int DeleteCoupon(int CouponId) {
        return dbRetail.delete(TBL_COUPON, "CouponId=" + CouponId, null);
    }

    /************************************************************************************************************************************/
    /********************************************************
     * Table - Other Charges
     ************************************************************/
    /************************************************************************************************************************************/
    // -----Insert Other Charges configuration-----
    public long addOtherCharges(ConfigBean configBean) {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_OTHER_CHARGES_CODE, configBean.getlDeptCode());
        cvDbValues.put(KEY_OTHER_CHARGES_DESCRIPTION, configBean.getStrDescription());
        cvDbValues.put(KEY_OTHER_CHARGES_CHARGEABLE, configBean.getiChargeable());
        cvDbValues.put(KEY_OTHER_CHARGES_AMOUNT, configBean.getDblAmount());
        return dbRetail.insert(TBL_OTHER_CHARGES, null, cvDbValues);
    }

    // -----Retrieve all OtherCharges-----
    public Cursor getAllOtherCharges() {
        return dbRetail.query(TBL_OTHER_CHARGES,
                new String[]{"*"}, null, null, null,
                null, null);
    }

    public Cursor getAllOtherChargesIsEnabled() {
        String strSelection = KEY_OTHER_CHARGES_CHARGEABLE + "=1";
        return dbRetail.query(TBL_OTHER_CHARGES,
                new String[]{"*"}, strSelection, null, null,
                null, null);
    }

    // -----Retrieve single OtherCharges based on OtherChargesId-----
    public Cursor getOtherCharges(int CouponId) {
        return dbRetail.query(TBL_OTHER_CHARGES,
                new String[]{"*"},
                KEY_OTHER_CHARGES_CODE + "=" + CouponId, null, null, null, null);
    }

    // -----Retrieve highest OtherChargesId from table-----
    public int getOtherChargesId() {
        Cursor result;
        result = dbRetail.rawQuery("SELECT MAX("+KEY_OTHER_CHARGES_CODE+") FROM " + TBL_OTHER_CHARGES, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update Other Charges table----
    public int updateOtherCharges(long lOtherChargesID, String strDescription, double dblAmount,
                                  int iStatus) {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_OTHER_CHARGES_DESCRIPTION, strDescription);
        cvDbValues.put(KEY_OTHER_CHARGES_AMOUNT, dblAmount);
        cvDbValues.put(KEY_OTHER_CHARGES_CHARGEABLE, iStatus);

        return dbRetail.update(TBL_OTHER_CHARGES, cvDbValues, KEY_OTHER_CHARGES_CODE + "=" + lOtherChargesID, null);
    }

    // -----Delete Other Charges
    public int DeleteOtherCharges(int OtherChargesId) {
        return dbRetail.delete(TBL_OTHER_CHARGES, KEY_OTHER_CHARGES_CODE + "=" + OtherChargesId, null);
    }

    public int getLastBillNo() {
        Cursor cursor = null;
        int invno = 0;
        try {
            cursor = dbRetail.rawQuery("SELECT MAX(InvoiceNo) as InvoiceNo FROM "+TBL_OUTWARD_SUPPLY_ITEMS_DETAILS, null);
            if (cursor != null && cursor.moveToFirst()) {
                invno = cursor.getInt(cursor.getColumnIndex(KEY_InvoiceNo));
            }
        } catch (Exception ex){
            Logger.i(TAG, "Unable to fetch the last bill no from table " +TBL_OUTWARD_SUPPLY_ITEMS_DETAILS
                    + "." + ex.getMessage());
            invno = -1;
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return invno;
    }

    public ArrayList<String> getGSTR1B2B_gstinList(String startDate, String endDate) {
        String selectQuery = "SELECT DISTINCT GSTIN FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE  " +
                KEY_BusinessType + " = 'B2B' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" + endDate + "' AND "+
                KEY_BillStatus+" = 1";
        Cursor cursor = dbRetail.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {
            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
            list.add(gstin);
        }

        return list;
    }

    public Cursor getGSTR1B2b_for_gstin(String StartDate, String EndDate, String gstin) {
        String b2b = "B2B";

        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + StartDate +
                "' AND '" + EndDate + "' AND " + KEY_BusinessType + " LIKE 'B2B' AND "+KEY_GSTIN+" LIKE '"+gstin+"'";
        Cursor result = dbRetail.rawQuery(selectQuery, null);
        return result;
    }

    public ArrayList<String> getGSTR1B2CL_stateCodeList(String startDate, String endDate) {
        String selectQuery = "SELECT  CustStateCode, POS, TaxableValue FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE  " +
                KEY_BusinessType + " = 'B2C' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" + endDate + "' AND "+
                KEY_BillStatus+" = 1";
        Cursor cursor = dbRetail.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {

            String TaxableValue_str = cursor.getString(cursor.getColumnIndex("TaxableValue"));
            double TaxableValue = Double.parseDouble(TaxableValue_str);
            if(TaxableValue <=250000)
                continue;
            String pos = cursor.getString(cursor.getColumnIndex("POS"));
            String state_cd = cursor.getString(cursor.getColumnIndex("CustStateCode"));
            if(!pos.equalsIgnoreCase(state_cd))
            {
                if(!list.contains(state_cd))
                    list.add(state_cd);
            }
        }

        return list;
    }

    public Cursor getGSTR1B2CL_stateCodeCursor(String startDate, String endDate, String stateCd) {
        String selectQuery = "SELECT  CustStateCode, POS, Invoicedate, InvoiceNo,CustName,TaxableValue," +
                "ProvisionalAssess, EcommerceGSTIN FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE  " +
                KEY_BusinessType + " = 'B2C' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" +
                endDate + "' AND "+KEY_CustStateCode+" LIKE '"+stateCd+"' AND "+
                KEY_BillStatus+" = 1";
        Cursor cursor = dbRetail.rawQuery(selectQuery, null);
        return cursor;
    }


    public Cursor getItemsForcessTaxPrints(int InvoiceNo, String InvoiceDate) {

        Cursor cursor = null;
        try {
            cursor = dbRetail.rawQuery("Select SUM(cessAmount) as cessAmount, cessRate from " + TBL_OUTWARD_SUPPLY_LEDGER +
                    " where InvoiceNo = '" + InvoiceNo + "' AND " + KEY_InvoiceDate + " LIKE '" + InvoiceDate + "' GROUP BY cessRate", null);
        } catch (Exception e) {
            e.printStackTrace();
            cursor = null;
        } finally {
            //db.close();
        }
        return cursor;
    }
    public Cursor getGSTR1B2CL_invoices(String InvoiceNo, String InvoiceDate, String custState, String custName) {
        String selectQuery = "SELECT  * FROM " + TBL_OUTWARD_SUPPLY_LEDGER + " WHERE  " +
                KEY_BusinessType + " = 'B2C' AND " + KEY_InvoiceDate + " LIKE '" + InvoiceDate + "' AND " +
                KEY_InvoiceNo + " LIKE '"+InvoiceNo+"' AND "+KEY_CustStateCode+" LIKE '"+custState+"' AND "+KEY_CustName+
                " LIKE '"+custName+"'";
        Cursor cursor = dbRetail.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getItemsForTaxSlabPrints(int InvoiceNo, String InvoiceDate) {
        //SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = dbRetail.rawQuery("Select IGSTRate, CGSTRate, SGSTRate, IGSTAmount, CGSTAmount, SGSTAmount, TaxableValue from " + TBL_OUTWARD_SUPPLY_LEDGER +
                    " where InvoiceNo = '" + InvoiceNo + "' AND " + KEY_InvoiceDate + " LIKE '" + InvoiceDate + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
            cursor = null;
            //db.close();
        } finally {
            return cursor;

        }

    }

    public  long addDeliveryChargesDetails(ContentValues cv)
    {
        return dbRetail.insert(TBL_OUTWARD_SUPPLY_DELIVERY_CHARGES_DETAILS,null,cv);
    }

    public  Cursor getDeliveryChargesDetails(String invoiceNo, String invoiceDate)
    {
        String query = "Select * FROM "+TBL_OUTWARD_SUPPLY_DELIVERY_CHARGES_DETAILS+" WHERE "+ KEY_InvoiceNo +" LIKE '"+invoiceNo+"' AND "+KEY_InvoiceDate+" LIKE '"+invoiceDate+"'";

        return dbRetail.rawQuery(query,null);
    }


    public Cursor mGetItemSearchData(CharSequence str){
        Cursor cursor = null;
        try{
            String select = "( "+KEY_ItemShortName + " LIKE ? OR "
                    + KEY_ItemBarcode + " LIKE ? OR " + KEY_ShortCode + " LIKE ? ) AND "+KEY_isActive +" = ?";
            String[]  selectArgs = { str + "%", str + "%", str + "%","1"};
            String[] contactsProjection = new String[] {
                    "*"};
            cursor = dbRetail.query(TBL_Item,contactsProjection,select,selectArgs,null,null,null);
        }catch (Exception ex){
            Logger.i(TAG,"Unable to get the data from item master table." +str + " error : " +ex.getMessage());
        }
        return cursor;
    }


    public Cursor getItemssbyBarCode(String Barcode) {

        Cursor cursor = null;
        try{
            String selectQuery = KEY_ItemBarcode+" LIKE '" + Barcode+"' AND "+KEY_isActive+" = 1";
            cursor = dbRetail.query(TBL_Item ,new String[]{"*"}, selectQuery, null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {

            //db.close();
        }
        return cursor;
    }
    public int clearItemdatabase() {
        return dbRetail.delete(TBL_Item, null, null);
    }

    public Cursor checkDuplicateItem(String shortName, String barCode, String uom, double mrp) {
        Cursor cursor = null;
        String query = "SELECT * FROM " + TBL_Item +" WHERE " + KEY_ItemShortName + " LIKE '"
                + shortName + "' AND " + KEY_ItemBarcode + " LIKE '" + barCode + "' AND " + KEY_UOM + " LIKE '" + uom + "' AND " + KEY_MRP + "=" + mrp;
        cursor = dbRetail.rawQuery(query, null);
        // return contact
        return cursor;
    }


    public Cursor mGetItemsForBillingQtyUpdate(int iItemNumber){
        Cursor cursor = null;
        try{
            String select = KEY_id + " = ? ";
            String[]  selectArgs = { ""+iItemNumber };
            String[] contactsProjection = new String[] {
                    "*"};
            cursor = dbRetail.query(TBL_Item,contactsProjection,select,selectArgs,null,null,null);
        }catch (Exception ex){
            Logger.i(TAG,"Unable to get the data from item table. Item ID : " +iItemNumber + " error : " +ex.getMessage());
        }
        return cursor;
    }

    public long mItemUpdateBillingQty(String strItemNumber, ContentValues cv){
        long lStatus = -1;
        try{
            String strWhere = KEY_id + "=?";
            lStatus = dbRetail.update(TBL_Item,cv,strWhere,new String[]{strItemNumber});
        } catch (Exception ex){
            Logger.i(TAG,"error on updating the item quantity on billing item." +ex.getMessage());
        }
        return lStatus;
    }


    //Payment mode configuration module database methods
    public int updatePaymentModeDetailsRazorPay(String keyId, String secretKey) {
        int result = 0;
        try {
            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_RAZORPAY_KEYID, keyId);
            cvDbValues.put(KEY_RAZORPAY_SECRETKEY, secretKey);

            result = dbRetail.update(TBL_PaymentModeConfiguration, cvDbValues, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        } finally {
            //db.close();
            return result;
        }
    }

    public int updatePaymentModeDetailsAEPS(String appID, String merchantId, String secretKey) {
        int result = 0;
        try {
            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_AEPS_AppId, appID);
            cvDbValues.put(KEY_AEPS_MerchantId, merchantId);
            cvDbValues.put(KEY_AEPS_SecretKey, secretKey);

            result = dbRetail.update(TBL_PaymentModeConfiguration, cvDbValues, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        } finally {
            //db.close();
            return result;
        }
    }

    public Cursor getPaymentModeConfiguration(String paymentModeId, String paymentModeSecretKey) {
       // SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = dbRetail.query(TBL_PaymentModeConfiguration, new String[]{"*"}, KEY_RAZORPAY_KEYID+" LIKE'"
                            + paymentModeId + "' AND "+KEY_RAZORPAY_SECRETKEY+" LIKE'" + paymentModeSecretKey + "'",
                    null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            cursor = null;
        } finally {
            //db.close();
            return cursor;
        }
    }


    public Cursor getPaymentModeConfiguration() {
        //SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = dbRetail.rawQuery("SELECT * FROM " + TBL_PaymentModeConfiguration, null);
        } catch (Exception e) {
            e.printStackTrace();
            cursor = null;
        } finally {
            //db.close();
            return cursor;
        }

    }

    //User management module table fetching data
    public ArrayList<String> getAllRoles() {
        ArrayList<String> list = new ArrayList<String>();
        String SELECT_QUERY = "SELECT * FROM " + TBL_USERSROLE;
        Cursor cursor = dbRetail.rawQuery(SELECT_QUERY, null);
        if (cursor != null) {
            //Logger.d(TAG,"fetched "+cursor.getCount()+" Items");
            list.add("Select");
            while (cursor.moveToNext()) {
                String role = cursor.getString(cursor.getColumnIndex("RoleName"));
                list.add(role);
            }
        }
        return list;
    }

    public ArrayList<String> getPermissionsNamesForRole(String roleName) {
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = dbRetail.query(TBL_USERROLEACCESS, null, KEY_ROLE_ID + "=?", new String[]{String.valueOf(roleName)}, null, null, null, null);
        if (cursor != null) {
            Log.d(TAG, "fetched " + cursor.getCount() + " Items");
            while (cursor.moveToNext()) {
                String role = cursor.getString(cursor.getColumnIndex(KEY_ACCESS_NAME));
                list.add(role);
            }
        }
        return list;
    }
    //Users management users module database methods
    public long addNewUser(User user) {
        long status = 0;

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID,user.getId());
        contentValues.put("Name", user.getUserName());
        contentValues.put("Mobile", user.getUserMobile());
        contentValues.put("Designation", user.getUserDesignation());
        contentValues.put("RoleId", user.getUserRole());
        contentValues.put("LoginId", user.getUserLogin());
        contentValues.put("Password", user.getUserPassword());
        contentValues.put("AadhaarNo", user.getUserAdhar());
        contentValues.put("Email", user.getUserEmail());
        contentValues.put("Address", user.getUserAddress());
        contentValues.put("FileLocation", user.getUserFileLoc());
        contentValues.put(KEY_SALES_MAN_ID, user.getStrSalesManId());
        contentValues.put(KEY_isActive, user.getIsActive());
        try {
            status = dbRetail.insert(TBL_USERS, null, contentValues);

        } catch (Exception e) {
            status = 0;
            Logger.d(TAG,e.toString());
        }
        return status;
    }

    public long updateUser(User user) {
        long status = -1;
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID,user.getId());
        contentValues.put("Name", user.getUserName());
        contentValues.put("Mobile", user.getUserMobile());
        contentValues.put("Designation", user.getUserDesignation());
        contentValues.put("RoleId", user.getUserRole());
        contentValues.put("LoginId", user.getUserLogin());
        contentValues.put("Password", user.getUserPassword());
        contentValues.put("AadhaarNo", user.getUserAdhar());
        contentValues.put("Email", user.getUserEmail());
        contentValues.put("Address", user.getUserAddress());
        contentValues.put("FileLocation", user.getUserFileLoc());
        contentValues.put(KEY_SALES_MAN_ID, user.getStrSalesManId());
        contentValues.put(KEY_isActive, user.getIsActive());
        try {
            status = dbRetail.update(TBL_USERS, contentValues, KEY_USER_ID + " = ?", new String[]{String.valueOf(user.getId())});

        } catch (Exception e) {
            status = 0;
            Logger.d(TAG, e.toString());
        }
        return status;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> list = new ArrayList<User>();
        list = new ArrayList<User>();

        String SELECT_QUERY = "SELECT * FROM " + TBL_USERS + " Where LoginId not in ('d#demo')  ORDER BY "+KEY_isActive+" DESC ";
        Cursor cursor = dbRetail.rawQuery(SELECT_QUERY, null);
        try {
            if (cursor != null) {
                //Logger.d(TAG,"fetched "+cursor.getCount()+" Items");
                while (cursor.moveToNext()) {
                    User user = new User();
                    user.setId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
                    user.setUserPassword(cursor.getString(cursor.getColumnIndex(KEY_USER_PASS)));
                    user.setUserName(cursor.getString(cursor.getColumnIndex("Name")));
                    user.setUserMobile(cursor.getString(cursor.getColumnIndex("Mobile")));
                    user.setUserDesignation(cursor.getString(cursor.getColumnIndex("Designation")));
                    user.setUserRole(cursor.getString(cursor.getColumnIndex("RoleId")));
                    user.setUserLogin(cursor.getString(cursor.getColumnIndex("LoginId")));
                    user.setUserAdhar(cursor.getString(cursor.getColumnIndex("AadhaarNo")));
                    user.setUserEmail(cursor.getString(cursor.getColumnIndex("Email")));
                    user.setUserAddress(cursor.getString(cursor.getColumnIndex("Address")));
                    user.setIsActive(cursor.getInt(cursor.getColumnIndex(KEY_isActive)));
                    user.setStrSalesManId(cursor.getString(cursor.getColumnIndex(KEY_SALES_MAN_ID)));
                    list.add(user);
                }
            }
        } catch (Exception ex){
            Logger.i(TAG, "Unable to fetch data from user management table. " + ex.getMessage());
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return list;
    }

    public Cursor getUser(String UserId, String Password) {
        Cursor cursor = null;
        try {
            cursor = dbRetail.query(TBL_USERS, new String[]{"*"}, KEY_USER_LOGIN +" ='" + UserId + "' AND "+KEY_USER_PASS+" ='" + Password + "'",
                    null, null, null, null);

        } catch (Exception e) {
            Logger.d(TAG, e.getMessage() + "");
        }
        return cursor;
    }

    public void deleteUser(String login) {
        dbRetail.delete(TBL_USERS, "LoginId" + " = ?", new String[]{String.valueOf(login)});
    }
    public int deleteUser(int roleId) {
        return dbRetail.delete(TBL_USERS, "RoleId" + " = ?", new String[]{String.valueOf(roleId)});
    }

    public Cursor getUsers(String UserId) {
        return dbRetail.query(TBL_USERS, new String[]{"*"}, "UserId='" + UserId + "'", null, null, null, null);

    }
    public Cursor getUsers_counter(String UserId) {
        Cursor cursor = null;
        try
        {
            //SQLiteDatabase db = getWritableDatabase();
            cursor = dbRetail.query(TBL_USERS, new String[]{"*"}, "UserId='" + UserId + "'", null, null, null, null);
        }catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            cursor = null;
        }
        finally {
            return cursor;
        }

    }

    public int getMaxUsersID() {
        Cursor result = null;
        int iCount = -1;
        try {
            result = dbRetail.rawQuery("SELECT MAX("+KEY_USER_ID+") FROM " + TBL_USERS, null);

            if (result.moveToFirst()) {
                iCount =  result.getInt(0);
            }
        } catch (Exception ex){
            Logger.d(TAG,"Unable to get the max user id count from Users table. " +ex.getMessage());
            iCount = -1;
        } finally {
            if(result != null){
                result.close();
            }
        }
        return iCount;
    }

    public int getRoleIdForUserName(String userName) {
        int roleId =-1;

        try {
            Cursor cursor = dbRetail.query(TBL_USERS, null, "Name=?",
                    new String[]{userName}, null, null, null, null);
            if (cursor != null)
                while (cursor.moveToNext()) {
                    roleId = cursor.getInt(cursor.getColumnIndex("RoleId"));
                    break;
                }
        } catch (Exception e) {
            roleId =-1;
            Logger.d(TAG,e.toString());
        }
        return roleId;
    }


    /************************************************************************************************************************************/
    /*******************************************************
     * Table - Customer
     ***********************************************************/
    /************************************************************************************************************************************/
    // -----Retrieve Customer ID-----
    public int getMaxCustomerId() {
        int iResult = 0;
        Cursor result = null;
        try {
            result = dbRetail.rawQuery("SELECT MAX("+KEY_CustId+") FROM " + TBL_CUSTOMER, null);
            if (result != null && result.getCount() > 0 && result.moveToFirst()) {
                iResult = result.getInt(0);
            }
        }catch (Exception ex){
            Logger.e(TAG," Unable to fetch customer id from customer table. " +ex.getMessage());
        } finally {
            if(result != null){
                result.close();
            }
        }
        return iResult;
    }

    public double getCustomerTotalTransactionFromPhone(String strCustomerPhone) {
        Cursor cursor = null;
        double result = 0;
        try {
            cursor = dbRetail.query(TBL_CUSTOMER, new String[]{"TotalTransaction"}, KEY_CustPhoneNo +"='" + strCustomerPhone +"'", null, null,
                    null, null);

            if (cursor.moveToFirst()) {
                result = cursor.getFloat(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return result;
    }
    public double getCustomerCreditAmountFromPhone(String strCustomerPhone) {
        Cursor cursor = null;
        double result = 0;
        try {
            cursor = dbRetail.query(TBL_CUSTOMER, new String[]{"CreditAmount"}, KEY_CustPhoneNo +"='" + strCustomerPhone +"'", null, null,
                    null, null);

            if (cursor.moveToFirst()) {
                result = cursor.getDouble(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = -1;
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return result;
    }
    public int updateCustomerTransaction(String strCustomerPhone, double fLastTransaction, double fTotalTransaction, double fCreditAmount) {

        int result = 0;
        try {
            cvDbValues = new ContentValues();
            cvDbValues.put("LastTransaction", fLastTransaction);
            cvDbValues.put("TotalTransaction", fTotalTransaction);
            cvDbValues.put("CreditAmount", fCreditAmount);

            result = dbRetail.update(TBL_CUSTOMER, cvDbValues, KEY_CustPhoneNo +"='" + strCustomerPhone +"'", null);
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }

    public Cursor mGetCustomerSearchData(CharSequence str){
        Cursor cursor = null;
        try{
            String select = "( "+KEY_CustName + " LIKE ? OR " + KEY_CustPhoneNo + " LIKE ?  ) AND "+KEY_isActive+" = ?";
            String[]  selectArgs = { str + "%", str + "%", "1"};
            String[] contactsProjection = new String[] {
                    "*"};
            cursor = dbRetail.query(TBL_CUSTOMER,contactsProjection,select,selectArgs,null,null,null);
        }catch (Exception ex){
            Logger.i(TAG,"Unable to get the data from customer master table." +str + " error : " +ex.getMessage());
        }
        return cursor;
    }

    // -----Insert Customer-----
    public long addCustomer(Customer objCustomer) {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_CustId, objCustomer.getiCustId());
        cvDbValues.put(KEY_GSTIN, objCustomer.getStrCustGSTIN());
        cvDbValues.put("CustName", objCustomer.getStrCustName());
        cvDbValues.put(KEY_CustPhoneNo, objCustomer.getStrCustContactNumber());
        cvDbValues.put(KEY_CUST_EMAIL, objCustomer.getStrEmailId());
        cvDbValues.put(KEY_RewardPointsAccumulated, objCustomer.getiRewardPoints());
        cvDbValues.put(KEY_OpeningBalance, objCustomer.getOpeningBalance());
        cvDbValues.put("CustAddress", objCustomer.getStrCustAddress());
        cvDbValues.put("CreditAmount", objCustomer.getdCreditAmount());
        cvDbValues.put("CreditLimit", objCustomer.getdCreditLimit());
        // cvDbValues.put(KEY_CUST_DEPOSIT_AMT_STATUS, objCustomer.getiDepositAmtStatus());
        cvDbValues.put(KEY_CUST_DEPOSIT_AMOUNT, objCustomer.getDblDepositAmt());
        cvDbValues.put(KEY_isActive, objCustomer.getIsActive());

        cvDbValues.put("LastTransaction", objCustomer.getdLastTransaction());
        cvDbValues.put("TotalTransaction", objCustomer.getdTotalTransaction());


        return dbRetail.insert(TBL_CUSTOMER, null, cvDbValues);
    }

    // -----Retrieve All customers-----
    public Cursor getAllCustomer() {
        String OrderByString = KEY_isActive+" DESC";
        return dbRetail.query(TBL_CUSTOMER, new String[]{"*"}, null, null, null, null, OrderByString);
    }

    // -----Retrieve single Customer-----
    public Cursor getCustomer(int iCustId) {
        return dbRetail.query(TBL_CUSTOMER, new String[]{"*"}, "CustId=" + iCustId, null, null, null, null);
    }

    // -----Retrieve single Customer-----
    public Cursor getCustomerbyPhone(String strCustPhone) {
        return dbRetail.query(TBL_CUSTOMER, new String[]{"*"}, KEY_CustPhoneNo+" LIKE '" + strCustPhone + "'", null, null,
                null, null);
    }

    public Cursor getCustomer(String strCustId) {
        return dbRetail.query(TBL_CUSTOMER, new String[]{"*"}, KEY_id+" LIKE '" + strCustId + "'", null, null,
                null, null);
    }

    public Cursor getCustomer(String strCustName, String strCustPhone) {
        String selectionString = KEY_CustPhoneNo+" LIKE '" + strCustPhone + "' AND "+KEY_CustName+" LIKE '"+strCustName+"'";
        return dbRetail.query(TBL_CUSTOMER, new String[]{"*"}, selectionString, null, null,
                null, null);
    }

    /*public Cursor getCustomerList(String Name) {
        return dbRetail.rawQuery("SELECT * FROM " + TBL_CUSTOMER + " WHERE CustName LIKE '" + Name + "%'", null);
    }*/

    public double getCustomerTotalTransaction(int iCustId) {
        //SQLiteDatabase db = getWritableDatabase();
        double result = 0;
        try {
            Cursor cursor = dbRetail.query(TBL_CUSTOMER, new String[]{"TotalTransaction"}, "CustId=" + iCustId, null, null,
                    null, null);

            if (cursor.moveToFirst()) {
                result = cursor.getFloat(0);
            } else {
                result = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }

    public List<String> getAllCustomerName() {
        List<String> list = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  *  FROM " + TBL_CUSTOMER;
        Cursor cursor = dbRetail.rawQuery(selectQuery, null);// selectQuery,selectedArguments
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("CustName"));
                if(!list.contains(name))
                    list.add(name);
            } while (cursor.moveToNext());
        }
        // returning lables
        return list;
    }

    public List<String> getAllCustomerPhone() {
        List<String> list = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  "+KEY_CustPhoneNo+"  FROM " + TBL_CUSTOMER;
        Cursor cursor = dbRetail.rawQuery(selectQuery, null);// selectQuery,selectedArguments
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(KEY_CustPhoneNo));
                if(!list.contains(name))
                    list.add(name);
            } while (cursor.moveToNext());
        }
        // returning lables
        return list;
    }

    public double getCustomerCreditAmount(int iCustId) {
        //SQLiteDatabase db = getWritableDatabase();
        double result = 0;
        try {
            Cursor cursor = dbRetail.query(TBL_CUSTOMER, new String[]{"CreditAmount"}, "CustId=" + iCustId, null, null,
                    null, null);

            if (cursor.moveToFirst()) {
                result = cursor.getDouble(0);
            } else {
                result = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }

    // -----Update Customer table-----
    public int updateCustomer(Customer objCustomer) {
        cvDbValues = new ContentValues();
        cvDbValues.put("CustName", objCustomer.getStrCustName());
        cvDbValues.put("LastTransaction", objCustomer.getdLastTransaction());
        cvDbValues.put("TotalTransaction", objCustomer.getdTotalTransaction());
        cvDbValues.put(KEY_CustPhoneNo, objCustomer.getStrCustContactNumber());
        cvDbValues.put("CustAddress", objCustomer.getStrCustAddress());
        cvDbValues.put("CreditAmount", objCustomer.getdCreditAmount());
        cvDbValues.put("CreditLimit", objCustomer.getdCreditLimit());
        cvDbValues.put(KEY_GSTIN, objCustomer.getStrCustGSTIN());
        cvDbValues.put(KEY_CUST_EMAIL, objCustomer.getStrEmailId());
        cvDbValues.put(KEY_RewardPointsAccumulated, objCustomer.getiRewardPoints());
        cvDbValues.put(KEY_OpeningBalance, objCustomer.getOpeningBalance());
        //cvDbValues.put(KEY_CUST_DEPOSIT_AMT_STATUS, objCustomer.getiDepositAmtStatus());
        cvDbValues.put(KEY_CUST_DEPOSIT_AMOUNT, objCustomer.getDblDepositAmt());
        cvDbValues.put(KEY_isActive, objCustomer.getIsActive());
        return dbRetail.update(TBL_CUSTOMER, cvDbValues, "CustId=" + objCustomer.getiCustId(), null);
    }

    // -----Update Customer table-----
    public int updateCustomerTransaction(int iCustId, double fLastTransaction, double fTotalTransaction, double fCreditAmount, int rewardPointsaccumulated) {
        int result = 0;
        try {
            cvDbValues = new ContentValues();

            cvDbValues.put(KEY_LastTransaction, fLastTransaction);
            cvDbValues.put(KEY_TotalTransaction, fTotalTransaction);
            cvDbValues.put(KEY_CreditAmount, fCreditAmount);
            cvDbValues.put(KEY_RewardPointsAccumulated, rewardPointsaccumulated);
            result = dbRetail.update(TBL_CUSTOMER, cvDbValues, KEY_id+"=" + iCustId, null);
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }

    // ----- Delete Customer --------
    public int DeleteCustomer(int CustId) {

        return dbRetail.delete(TBL_CUSTOMER, "CustId=" + CustId, null);
    }

    public int DeleteAllCustomer() {

        return dbRetail.delete(TBL_CUSTOMER,null, null);
    }

    // New User, Roles, Role Access modules -------------------------------------

    public long addRole(AddRoleBean addRoleBean) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ROLE_NAME, addRoleBean.getStrData());
        contentValues.put(KEY_ROLE_ID,addRoleBean.getiRoleId());
        return dbRetail.insert(TBL_USERSROLE, null, contentValues);
    }

    public long deleteRole(int iRoleID) {
        long lStatus = -1;
        if(dbRetail.delete(TBL_USERSROLE, KEY_ROLE_ID + "=?", new String[]{"" + iRoleID}) > -1){
            lStatus = dbRetail.delete(TBL_USERROLEACCESS, KEY_ROLE_ID + "=?", new String[]{"" + iRoleID});
        }
        return lStatus;
    }

    public long addAccessPermissionRole(AddRoleBean addRoleBean) {
        long lStatus = -1;
        if(addRoleBean.getiRoleId() > 0) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_ROLE_ID, addRoleBean.getiRoleId());
            contentValues.put(KEY_ACCESS_CODE, addRoleBean.getiRoleAccessId());
            contentValues.put(KEY_ACCESS_NAME, addRoleBean.getStrData());
            lStatus = dbRetail.insert(TBL_USERROLEACCESS, null, contentValues);
        }
        return lStatus;
    }

    public long updateAccessPermissionRole(AddRoleBean addRoleBean) {
        long lUpdateresult = -1;
        try {
            lUpdateresult = dbRetail.delete(TBL_USERROLEACCESS, KEY_ACCESS_CODE + "=?", new String[]{"" + addRoleBean.getiRoleAccessId()});
        }catch (Exception ex){
            Logger.i(TAG, " Unable to remove the access permission ");
        }
        return lUpdateresult;
    }

    public ArrayList<AddRoleBean> getAllRolesForAddRole() {
        ArrayList<AddRoleBean> list = new ArrayList<AddRoleBean>();
        String SELECT_QUERY = "SELECT * FROM " + TBL_USERSROLE;
        Cursor cursor = null;
        try {
            cursor = dbRetail.rawQuery(SELECT_QUERY, null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    AddRoleBean addRoleBean = new AddRoleBean();
                    addRoleBean.set_id(cursor.getInt(cursor.getColumnIndex(KEY_id)));
                    addRoleBean.setiRoleId(cursor.getInt(cursor.getColumnIndex(KEY_ROLE_ID)));
                    addRoleBean.setStrData(cursor.getString(cursor.getColumnIndex(KEY_ROLE_NAME)));
                    list.add(addRoleBean);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Logger.e(TAG, "Unable to fetch roles from user roles table." + ex.getMessage());
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return list;
    }

    public int getMaxRoleId() {
        int iResult = 0;
        Cursor result = null;
        try {
            result = dbRetail.rawQuery("SELECT MAX("+KEY_ROLE_ID+") FROM " + TBL_USERSROLE, null);
            if (result != null && result.getCount() > 0 && result.moveToFirst()) {
                iResult = result.getInt(0);
            }
        }catch (Exception ex){
            Logger.e(TAG," Unable to fetch role id from user role table. " +ex.getMessage());
        } finally {
            if(result != null){
                result.close();
            }
        }
        return iResult;
    }

    public int getRoleIdFromUserRoleAccess() {
        int iResult = 0;
        Cursor result = null;
        try {
            result = dbRetail.rawQuery("SELECT MAX("+KEY_ACCESS_CODE+") FROM " + TBL_USERROLEACCESS, null);
            if (result != null && result.getCount() > 0 && result.moveToFirst()) {
                iResult = result.getInt(0);
            }
        }catch (Exception ex){
            Logger.e(TAG," Unable to fetch role id from USER ROLE ACCESS table. " +ex.getMessage());
        } finally {
            if(result != null){
                result.close();
            }
        }
        return iResult;
    }

    public List<AccessPermissionRoleBean> getAccessPermissionRole(int iRoleId) {
        List<AccessPermissionRoleBean> list = new ArrayList<AccessPermissionRoleBean>();
        String SELECT_QUERY = "SELECT * FROM " + TBL_USERROLEACCESS + " where " + KEY_ROLE_ID
                + " = " + iRoleId ;
        Cursor cursor = null;
        try {
            cursor = dbRetail.rawQuery(SELECT_QUERY, null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    AccessPermissionRoleBean roleBean = new AccessPermissionRoleBean();
                    roleBean.set_id(cursor.getInt(cursor.getColumnIndex(KEY_id)));
                    roleBean.setiRoleId(cursor.getInt(cursor.getColumnIndex(KEY_ROLE_ID)));
                    roleBean.setiRoleAccessId(cursor.getInt(cursor.getColumnIndex(KEY_ACCESS_CODE)));
                    roleBean.setStrAccessName(cursor.getString(cursor.getColumnIndex(KEY_ACCESS_NAME)));
                    list.add(roleBean);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Logger.e(TAG, "Unable to fetch data from user roles access table." + ex.getMessage());
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return list;
    }

    public String getRoleNameforRoleId(String roleId) {
        String roleName = "Invalid Role";

        try {
            Cursor cursor = dbRetail.query(TBL_USERSROLE, null, "RoleId=?",
                    new String[]{roleId}, null, null, null, null);
            if (cursor != null && cursor.moveToNext())
                roleName = cursor.getString(cursor.getColumnIndex("RoleName"));
        } catch (Exception e) {
            roleName = "Invalid Role";
            Logger.d(TAG,e.toString());
        }
        return roleName;
    }
    public int getRoleIdforRoleName(String roleName) {
        int roleId = -1;

        try {
            Cursor cursor = dbRetail.query(TBL_USERSROLE, null, "RoleName=?",
                    new String[]{String.valueOf(roleName)}, null, null, null, null);
            if (cursor != null)
                while (cursor.moveToNext()) {
                    roleId = cursor.getInt(cursor.getColumnIndex("RoleId"));
                    break;
                }
        } catch (Exception e) {
            roleId = -1;
            Logger.d(TAG,e.toString());
        }
        return roleId;
    }

    public String getRoleName(String roleId) {
        String str = "";
        try {
            Cursor cursor = dbRetail.query(TBL_USERSROLE, null, "RoleId=?",
                    new String[]{String.valueOf(roleId)}, null, null, null, null);
            if (cursor != null)
                while (cursor.moveToNext()) {
                    str = cursor.getString(cursor.getColumnIndex("RoleName"));
                    break;
                }
        } catch (Exception e) {
            str = "";
            Logger.d(TAG,e.toString());
        }
        return str;
    }

    public void addAccessesForRole(String roleName, ArrayList<String> listsAccess, SparseBooleanArray checkedItems) {

        for (int i = 0; i < checkedItems.size(); i++) {
            long status = 0;
            if(checkedItems.get(checkedItems.keyAt(i)) == false)
            {
                continue;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("RoleId", roleName);
            contentValues.put("RoleAccessId", checkedItems.keyAt(i));
            contentValues.put("RoleAccessName", listsAccess.get(checkedItems.keyAt(i)));
            try {
                status = dbRetail.insert(TBL_USERROLEACCESS, null, contentValues);
                //Logger.d(TAG,"code "+status);
                if (status > 0) {

                }
                //Logger.d(TAG,"Inserted Successfully with code "+status);
            } catch (Exception e) {
                status = 0;
                Logger.d(TAG,e.toString());
            }
        }
    }

    public int  deleteAccessesForRole(String roleName) {
        int status =0;
        try {
            status = dbRetail.delete(TBL_USERROLEACCESS, "RoleId" + " = ?", new String[]{String.valueOf(roleName)});
            //status = dbRetail.insert(TBL_USERROLEACCESS, null, contentValues);
            //Logger.d(TAG,"code "+status);
            if (status > 0) {

            }
            //Logger.d(TAG,"Inserted Successfully with code "+status);
        } catch (Exception e) {
            status = 0;
            Logger.d(TAG,e.toString());
        }
        return status;
    }
    public int deleteRole(String roleName) {
        return dbRetail.delete(TBL_USERSROLE, "RoleName" + " = ?", new String[]{String.valueOf(roleName)});
    }

    public Cursor getAllRolesData()
    {
        String selectQuery = "SELECT * FROM "+TBL_USERSROLE;
        Cursor cursor = dbRetail.rawQuery(selectQuery,null);
        return cursor;
    }

    public Cursor mGetSalesManIdSearchData(CharSequence str){
        Cursor cursor = null;
        try{
            String select = KEY_SALES_MAN_ID + " LIKE ? AND "
                    + KEY_ROLE_ID + "=? AND "+KEY_isActive +" = ?";
            String[]  selectArgs = { str + "%", "4", "1"};
            String[] contactsProjection = new String[] {
                    "*"};
            cursor = dbRetail.query(TBL_USERS,contactsProjection,select,selectArgs,null,null,null);
        }catch (Exception ex){
            Logger.i(TAG,"Unable to get sales man id data from user table." +str + " error : " +ex.getMessage());
        }
        return cursor;
    }

    public long insertItem(ItemModel itemObject)
    {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ShortCode, itemObject.getShortCode());
        cv.put(KEY_ItemShortName, itemObject.getShortName());
        cv.put(KEY_ItemLongName,itemObject.getLongName());
        cv.put(KEY_ItemBarcode,itemObject.getBarCode());
        cv.put(KEY_SupplyType,itemObject.getSupplyType());
        cv.put(KEY_UOM,itemObject.getUOM());
        cv.put(KEY_BrandCode,itemObject.getBrandCode());
        cv.put(KEY_DepartmentCode, itemObject.getDeptCode());
        cv.put(KEY_CategoryCode,itemObject.getCategCode());
        cv.put(KEY_RetailPrice, itemObject.getRetaiPrice());
        cv.put(KEY_MRP,itemObject.getMrp());
        cv.put(KEY_WholeSalePrice,itemObject.getWholeSalePrice());
        cv.put(KEY_Quantity,itemObject.getQuantity());
        cv.put(KEY_HSNCode, itemObject.getHsn());
        cv.put(KEY_CGSTRate,itemObject.getCgstRate());
        cv.put(KEY_SGSTRate, itemObject.getSgstRate());
        cv.put(KEY_IGSTRate, itemObject.getIgstRate());
        cv.put(KEY_cessRate,itemObject.getCessRate());
        cv.put(KEY_ImageUri, itemObject.getImageURL());
        cv.put(KEY_isFavrouite, itemObject.getFav());
        cv.put(KEY_isActive, itemObject.getActive());
        cv.put(KEY_DiscountPercent, itemObject.getDiscountPercent());
        cv.put(KEY_DiscountAmount, itemObject.getDiscountAmount());

        long insertStatus = dbRetail.insert(TBL_Item,null,cv);
        return insertStatus;
    }

    public long updateItem(ItemModel itemObject)
    {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ShortCode, itemObject.getShortCode());
        cv.put(KEY_ItemShortName, itemObject.getShortName());
        cv.put(KEY_ItemLongName,itemObject.getLongName());
        cv.put(KEY_ItemBarcode,itemObject.getBarCode());
        cv.put(KEY_SupplyType,itemObject.getSupplyType());
        cv.put(KEY_UOM,itemObject.getUOM());
        cv.put(KEY_BrandCode,itemObject.getBrandCode());
        cv.put(KEY_DepartmentCode, itemObject.getDeptCode());
        cv.put(KEY_CategoryCode,itemObject.getCategCode());
        cv.put(KEY_RetailPrice, itemObject.getRetaiPrice());
        cv.put(KEY_MRP,itemObject.getMrp());
        cv.put(KEY_WholeSalePrice,itemObject.getWholeSalePrice());
        cv.put(KEY_Quantity,itemObject.getQuantity());
        cv.put(KEY_HSNCode, itemObject.getHsn());
        cv.put(KEY_CGSTRate,itemObject.getCgstRate());
        cv.put(KEY_SGSTRate, itemObject.getSgstRate());
        cv.put(KEY_IGSTRate, itemObject.getIgstRate());
        cv.put(KEY_cessRate,itemObject.getCessRate());
        cv.put(KEY_ImageUri, itemObject.getImageURL());
        cv.put(KEY_isFavrouite, itemObject.getFav());
        cv.put(KEY_isActive, itemObject.getActive());
        cv.put(KEY_DiscountPercent, itemObject.getDiscountPercent());
        cv.put(KEY_DiscountAmount, itemObject.getDiscountAmount());

        String whereClause = KEY_id +" = "+itemObject.get_id();

        long insertStatus = dbRetail.update(TBL_Item,cv,whereClause,null);
        return insertStatus;
    }

    public Cursor getItemByID(int id)
    {
        String query = "SELECT * FROM "+TBL_Item+" WHERE "+KEY_id+" = "+id;
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }

    public Cursor getItemByItemShortName(String itemShortName)
    {
        String query = "SELECT * FROM "+TBL_Item+" WHERE "+KEY_ItemShortName+" Like '"+itemShortName+"'";
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }

    public Cursor getAllItems()
    {
        String query = "SELECT * FROM "+TBL_Item;
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }
    public Cursor getAllItems_Fav()
    {
        String query = "SELECT * FROM "+TBL_Item+" WHERE "+KEY_isFavrouite+" =1 AND "+KEY_isActive+" = 1";
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }
    public Cursor getAllItems_Active_Departmentwise()
    {
        String query = "SELECT * FROM "+TBL_Item+" WHERE "+KEY_DepartmentCode+" > 0 ORDER BY "+KEY_DepartmentCode +" AND "+KEY_isActive+" = 1";
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }

    public Cursor getAllItems_Active_Departmentwise(int deptCode)
    {
        String query = "SELECT * FROM "+TBL_Item+" WHERE "+KEY_DepartmentCode+" ="+deptCode+" ORDER BY "+KEY_DepartmentCode +" AND "+KEY_isActive+" = 1";
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }
    public Cursor getAllItems_Active_Category(int iCategoryCode)
    {
        String query = "SELECT * FROM "+TBL_Item+" WHERE "+KEY_CategoryCode+" = " +iCategoryCode +" AND "+KEY_isActive+" = 1";
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }
    public Cursor getAllItems_Brandwise()
    {
        String query = "SELECT * FROM "+TBL_Item+" WHERE +"+KEY_BRAND_CODE+" > 0 ORDER BY "+KEY_BrandCode;
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }
    public Cursor getAllItems_Departmentwise()
    {
        String query = "SELECT * FROM "+TBL_Item+" WHERE +"+KEY_DepartmentCode+" > 0 ORDER BY "+KEY_DepartmentCode;
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }
    public Cursor getAllItems_Categorywise()
    {
        String query = "SELECT * FROM "+TBL_Item+" WHERE +"+KEY_CategoryCode+" > 0 ORDER BY "+KEY_CategoryCode;
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }
    public Cursor getAllItems_ActiveItems()
    {
        String query = "SELECT * FROM "+TBL_Item+" WHERE "+KEY_isActive+" =1";
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }
    public Cursor getAllItems_InactiveItems()
    {
        String query = "SELECT * FROM "+TBL_Item+" WHERE "+KEY_isActive+" =0";
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }
    public Cursor getAllItems_details(int shortCode, String shortName, String barcode)
    {
        String query = "SELECT * FROM "+TBL_Item+" WHERE "+KEY_ShortCode+" ="+shortCode+" AND "+KEY_ItemShortName+" LIKE '"+shortName+"' AND "+KEY_ItemBarcode+" LIKE '"+barcode+"'";
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }
    public ArrayList<String> getAllItems_for_autocomplete()
    {
        ArrayList<String> list = new ArrayList<>();
        String query = "SELECT _id, ShortCode, ItemShortName , ItemBarcode FROM "+TBL_Item;
        Cursor cursor = dbRetail.rawQuery(query,null);
        while (cursor!=null && cursor.moveToNext())
        {
            String data = cursor.getInt(cursor.getColumnIndex("ShortCode")) +" "+"- "
                    + cursor.getString(cursor.getColumnIndex("ItemShortName")) +" "+"- "
                    + cursor.getString(cursor.getColumnIndex("ItemBarcode"));
            if(!list.contains(data))
                list.add(data);
        }
        return list;
    }

    public int DeleteItemByDeptCode(String DeptCode) {

        return dbRetail.delete(TBL_Item, KEY_DepartmentCode+"=" + DeptCode, null);
    }
    public int DeleteItemByCategCode(int CategCode) {

        return dbRetail.delete(TBL_Item, KEY_CategoryCode+"=" + CategCode, null);
    }

    // -----Update Item stock-----
    public int updateItemStock(int MenuCode, double Stock, double fMRP, double fRetailPrice, double fWholeSalePrice) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_Quantity, Stock);
        cvDbValues.put(KEY_MRP, fMRP);
        cvDbValues.put(KEY_RetailPrice, fRetailPrice);
        cvDbValues.put(KEY_WholeSalePrice, fWholeSalePrice);
        return dbRetail.update(TBL_Item, cvDbValues, KEY_id+ "=" + MenuCode, null);
    }

    //Supplier details
    public long saveSupplierDetails(String supplierType_str, String suppliergstin_str, String suppliername_str,
                                    String supplierphn_str, String supplieraddress_str) {
        long l = 0;
        int iSupplierCode = getSupplierCode();
        if(iSupplierCode > -1) {
            iSupplierCode++;
            ContentValues cvdbValues = new ContentValues();
            cvdbValues.put(KEY_SupplierCode, iSupplierCode);
            cvdbValues.put(KEY_SupplierType, supplierType_str);
            cvdbValues.put(KEY_GSTIN, suppliergstin_str);
            cvdbValues.put(KEY_SUPPLIERNAME, suppliername_str);
            cvdbValues.put(KEY_SupplierPhone, supplierphn_str);
            cvdbValues.put(KEY_SupplierAddress, supplieraddress_str);
            l = dbRetail.insert(TBL_Supplier, null, cvdbValues);
        }
        return l;
    }
    public long updateSupplierDetails(String supplierType_str, String suppliergstin_str, String suppliername_str,
                                      String supplierphn_str, String supplieraddress_str, int suppliercode) {
        long l = 0;
        String whereclause = KEY_SupplierCode+"="+suppliercode;
        ContentValues cvdbValues = new ContentValues();
        cvdbValues.put(KEY_SupplierType, supplierType_str);
        cvdbValues.put(KEY_GSTIN, suppliergstin_str);
        cvdbValues.put(KEY_SUPPLIERNAME, suppliername_str);
        cvdbValues.put(KEY_SupplierPhone, supplierphn_str);
        cvdbValues.put(KEY_SupplierAddress, supplieraddress_str);
        l = dbRetail.update(TBL_Supplier,cvdbValues,KEY_SupplierCode+"="+suppliercode,null );
        return l;
    }

    public int getSuppliercode(String suppliername) {
        int code = -1;
        String selectQuery = "Select " + KEY_SupplierCode + " FROM " + TBL_Supplier + " WHERE " + KEY_SUPPLIERNAME + " LIKE '" + suppliername + "'";
        Cursor cursor = dbRetail.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            code = cursor.getInt(cursor.getColumnIndex(KEY_SupplierCode));
        }
        return code;

    }

    public Cursor getSupplierDetailsByName(String suppliername) {
        String selectquery = "Select * FROM " + TBL_Supplier + " WHERE " + KEY_SUPPLIERNAME + " LIKE '" + suppliername + "'";
        Cursor cursor = dbRetail.rawQuery(selectquery, null);
        return cursor;
    }
    public Cursor getSupplierDetailsByPhone(String supplierPhone) {
        String selectquery = "Select * FROM " + TBL_Supplier + " WHERE " + KEY_SupplierPhone + " LIKE '" + supplierPhone + "'";
        Cursor cursor = dbRetail.rawQuery(selectquery, null);
        return cursor;
    }

    public ArrayList<HashMap<String, String>> getAllSupplierNamePhone() {
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TBL_Supplier+" ORDER BY SupplierName ASC";
        Cursor cursor = dbRetail.rawQuery(selectQuery, null);// selectQuery,selectedArguments
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
//                list.add(cursor.getString(cursor.getColumnIndex(KEY_SUPPLIERNAME)));// adding

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("name", cursor.getString(cursor.getColumnIndex(KEY_SUPPLIERNAME)));
                data.put("phone", cursor.getString(cursor.getColumnIndex(KEY_SupplierPhone)));
                list.add(data);

            } while (cursor.moveToNext());
        }
        // returning lables
        return list;
    }

    public Cursor getAllSupplierName_nonGST() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TBL_Supplier+" ORDER BY SupplierName ASC"/* + " WHERE " + KEY_SupplierType + " LIKE 'UnRegistered'"*/;
        Cursor cursor = dbRetail.rawQuery(selectQuery, null);// selectQuery,selectedArgument
        return cursor;
    }

    public  long deleteSupplier(int supplierCode)
    {
        return dbRetail.delete(TBL_Supplier, KEY_SupplierCode + "=" + supplierCode, null);
    }
   /* public long DeleteSupplierItems_suppliercode(int suppliercode) {

        return dbRetail.delete(TBL_SupplierItemLinkage, "SupplierCode=" + suppliercode, null);
    }*/

    public int getSupplierCode() {
        int iResult = 0;
        Cursor result = null;
        try {
            result = dbRetail.rawQuery("SELECT MAX("+KEY_SupplierCode+") FROM " + TBL_Supplier, null);
            if (result != null && result.getCount() > 0 && result.moveToFirst()) {
                iResult = result.getInt(0);
            }
        }catch (Exception ex){
            Logger.e(TAG," Unable to fetch supplier code from supplier details table. " +ex.getMessage());
        } finally {
            if(result != null){
                result.close();
            }
        }
        return iResult;
    }

    //Supplier item linkage get bean data
    public Cursor getSupplierItemlinkageByPhone(String supplierPhone) {
        String selectquery = "Select * FROM " + TBL_SUPPLIER_ITEM_LINKAGE + " WHERE " + KEY_SupplierPhone + " LIKE '" + supplierPhone + "'";
        Cursor cursor = dbRetail.rawQuery(selectquery, null);
        return cursor;
    }

    public long mLinkSupplierWithItem(SupplierItemLinkageBean supplierItemLinkageBean, int iMode){
        ContentValues cv;
        long lStatus = -1;
        if(supplierItemLinkageBean != null){
            cv = new ContentValues();
            cv.put(KEY_SupplierCode,supplierItemLinkageBean.getiSupplierCode());
            if(supplierItemLinkageBean.getStrGSTIN() != null) {
                cv.put(KEY_GSTIN,supplierItemLinkageBean.getStrGSTIN());
            }
            cv.put(KEY_SUPPLIERNAME,supplierItemLinkageBean.getStrSupplierName());
            if(supplierItemLinkageBean.getStrSupplierType() != null){
                cv.put(KEY_SupplierType,supplierItemLinkageBean.getStrSupplierType());
            }
            cv.put(KEY_SupplierPhone,supplierItemLinkageBean.getStrSupplierPhone());
            cv.put(KEY_SupplierAddress,supplierItemLinkageBean.getStrSupplierAddress());
            cv.put(KEY_ITEM_ID, supplierItemLinkageBean.getiItemID());
            cv.put(ITEM_NAME, supplierItemLinkageBean.getStrItemName());
            if(supplierItemLinkageBean.getStrBarcode() != null) {
                cv.put(BARCODE, supplierItemLinkageBean.getStrBarcode());
            }
            cv.put(RATE,supplierItemLinkageBean.getDblRate());
            if(supplierItemLinkageBean.getStrHSNCode() != null) {
                cv.put(HSN_CODE, supplierItemLinkageBean.getStrHSNCode());
            }
            if(supplierItemLinkageBean.getStrUOM() != null) {
                cv.put(UOM, supplierItemLinkageBean.getStrUOM());
            }
            if(supplierItemLinkageBean.getDblCGSTPer() > 0){
                cv.put(CGST_RATE_PER,supplierItemLinkageBean.getDblCGSTPer());
            }
            if(supplierItemLinkageBean.getDblUTGST_SGSTPer() > 0){
                cv.put(UTGST_SGST_RATE_PER,supplierItemLinkageBean.getDblUTGST_SGSTPer());
            }
            if(supplierItemLinkageBean.getDblIGSTPer() > 0){
                cv.put(IGST_RATE_PER,supplierItemLinkageBean.getDblIGSTPer());
            }
            if(supplierItemLinkageBean.getDblCessPer() > 0){
                cv.put(CESS_RATE_PER,supplierItemLinkageBean.getDblCessPer());
            }
            if(supplierItemLinkageBean.getDblRetailPrice() > 0){
                cv.put(MRP,supplierItemLinkageBean.getDblMRP());
            }
            if(supplierItemLinkageBean.getDblRetailPrice() > 0){
                cv.put(RETAIL_PRICE,supplierItemLinkageBean.getDblRetailPrice());
            }
            if(supplierItemLinkageBean.getDblWholeSalePrice() > 0){
                cv.put(WHOLE_SALE_PRICE,supplierItemLinkageBean.getDblWholeSalePrice());
            }
            switch (iMode){
                case 1:
                    lStatus = dbRetail.insert(TBL_SUPPLIER_ITEM_LINKAGE, null, cv);
                    break;
                case 2:
                    String strWhere = KEY_SupplierCode + "=? and " + KEY_ITEM_ID + "=?";
                    lStatus = dbRetail.update(TBL_SUPPLIER_ITEM_LINKAGE,cv,strWhere,new String[]{""+supplierItemLinkageBean.getiSupplierCode(),""+supplierItemLinkageBean.getiItemID()});
                    break;
                default:
                    break;
            }

        }
        return lStatus;
    }

    public long mUnLinkSupplierWithItem(String strSupplierCode, String strItemID){
        long lstatus = -1;
        try {
            if (!strSupplierCode.isEmpty() && !strItemID.isEmpty()) {
                String strWhere = KEY_SupplierCode + "=? and " + KEY_ITEM_ID + "=?";
                lstatus = dbRetail.delete(TBL_SUPPLIER_ITEM_LINKAGE, strWhere, new String[]{strSupplierCode, strItemID});
            }
        }catch (Exception ex){
            Logger.i(TAG,"Unable to unlink the supplier item linkage." + ex.getMessage());
        }
        return lstatus;
    }





    //Supplier item linkage


    //TBL_CATEGORY functions
    public Cursor getAllCategories()
    {
        String query = "Select * FROM "+TBL_CATEGORY;
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }
    public Cursor getCategoriesForDeptId(int deptCode)
    {
        String query = "Select * FROM "+TBL_CATEGORY+" WHERE "+KEY_DepartmentCode+" = "+deptCode;
        Cursor cursor = dbRetail.rawQuery(query,null);
        return cursor;
    }

    //TBL_RewardPointsConfiguration

    public int updateRewardPointsConfiguration(double amtTopt, int rewardpoints, double ptToAmt, int limit)
    {
        ContentValues cv = new ContentValues();
        cv.put(KEY_AmtToPt, amtTopt);
        cv.put(KEY_RewardPoints,rewardpoints);
        cv.put(KEY_RewardPointsToAmt,ptToAmt);
        cv.put(KEY_RewardPointsLimit,limit);

        return dbRetail.update(TBL_REWARDPOINTSCONGFIGURATION,cv,null,null);
    }

    public Cursor getRewardPointsConfiguration()
    {
        String selectQuery = " SELECT * FROM "+ TBL_REWARDPOINTSCONGFIGURATION;
        return dbRetail.rawQuery(selectQuery,null);
    }


    //Purchase order methods
    public Cursor mGetSupplierDetails(CharSequence str){
        Cursor cursor = null;
        try{
            String select = KEY_SUPPLIERNAME + " LIKE ? OR "
                    + KEY_SupplierPhone + " LIKE ? ";
            String[]  selectArgs = { str + "%", str + "%"};
            String[] contactsProjection = new String[] {
                    KEY_id,
                    KEY_SupplierCode,
                    KEY_SUPPLIERNAME,
                    KEY_SupplierPhone,
                    KEY_GSTIN,
                    KEY_SupplierAddress,
                    KEY_SupplierType};
            cursor = dbRetail.query(TBL_Supplier,contactsProjection,select,selectArgs,null,null,null);
        }catch (Exception ex){
            Logger.i(TAG,"Unable to get the data from supplier details table." +str + " error : " +ex.getMessage());
        }
        return cursor;
    }

    public Cursor mGetPurchaseOrderItems(CharSequence str, int iSupplierCode){
        Cursor cursor = null;
        try{
            String select = "(" + KEY_ItemName + " LIKE ? OR " + KEY_ItemBarcode + " LIKE ? ) AND " + KEY_SupplierCode + "=?";
            String[]  selectArgs = { str + "%", str + "%", ""+iSupplierCode};
            String[] contactsProjection = new String[] {
                    "*"};
            if(select != null) {
                cursor = dbRetail.query(TBL_SUPPLIER_ITEM_LINKAGE, contactsProjection, select, selectArgs, null, null, null);
            }
        }catch (Exception ex){
            Logger.i(TAG,"Unable to get the data from supplier linkage item table." +str + " error : " +ex.getMessage());
        }
        return cursor;
    }

    public Cursor mGetPurchaseOrderNo(CharSequence str, int iSupplierCode){
        Cursor cursor = null;
        try{
            String select = KEY_PurchaseOrderNo + " LIKE ? AND " + KEY_SupplierCode + "=? and " + KEY_isGoodinward +"=0";
            String[]  selectArgs = { str + "%", ""+iSupplierCode};
            String[] contactsProjection = new String[] {
                    "distinct " + KEY_PurchaseOrderNo + " as " + KEY_id};
            if(select != null) {
                cursor = dbRetail.query(TBL_PURCHASEORDER, contactsProjection, select, selectArgs, null, null, null);
            }
        }catch (Exception ex){
            Logger.i(TAG,"Unable to get the data from purchase order no table." +str + " error : " +ex.getMessage());
        }
        return cursor;
    }

    //Supplier item linkage
    public Cursor mGetItemsForSupplierLinkage(CharSequence str){
        Cursor cursor = null;
        try{
            String select = KEY_ItemShortName + " LIKE ? OR " + KEY_ItemBarcode + " LIKE ?";
            String[]  selectArgs = { str + "%",str + "%"};
            String[] contactsProjection = new String[] {
                    "*"};
            cursor = dbRetail.query(TBL_Item,contactsProjection,select,selectArgs,null,null,null);
        }catch (Exception ex){
            Logger.i(TAG,"Unable to get the data from item table." +str + " error : " +ex.getMessage());
        }
        return cursor;
    }

    public Cursor mGetItems(String strItemName){
        Cursor cursor = null;
        try{
            String select = KEY_ItemLongName + " LIKE ? ";
            String[]  selectArgs = { strItemName};
            String[] contactsProjection = new String[] {
                    "*"};
            cursor = dbRetail.query(TBL_Item,contactsProjection,select,selectArgs,null,null,null);
        }catch (Exception ex){
            Logger.i(TAG,"Unable to get the data from item table." +strItemName + " error : " +ex.getMessage());
        }
        return cursor;
    }

    public long mItemUpdateGoodsInwardNote(String strItemName, ContentValues cv){
        long lStatus = -1;
        try{
            String strWhere = KEY_ItemLongName + "=?";
            lStatus = dbRetail.update(TBL_Item,cv,strWhere,new String[]{strItemName});
        } catch (Exception ex){
            Logger.i(TAG,"error on updating the item quantity on goods inward notes" +ex.getMessage());
        }
        return lStatus;
    }

    // -----Insert Purchase Order-----
    public long InsertPurchaseOrder(PurchaseOrderBean po) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_SUPPLIERNAME, po.getStrSupplierName());
        cvDbValues.put(KEY_SupplierCode, po.getStrSupplierCode());
        cvDbValues.put(KEY_SupplierPhone, po.getStrSupplierPhone());
        cvDbValues.put(KEY_SupplierAddress, po.getStrSupplierAddress());
       /* if(po.getStrSupplierType() != null) {
            cvDbValues.put(KEY_SupplierType, po.getStrSupplierType());
        }*/
        cvDbValues.put(KEY_SupplierPOS, po.getStrSupplierPOS());
        if(po.getStrSupplierGSTIN() != null) {
            cvDbValues.put(KEY_GSTIN, po.getStrSupplierGSTIN());
        }
        cvDbValues.put(KEY_PurchaseOrderNo, po.getStrPurchaseOrderNo());
        cvDbValues.put(KEY_InvoiceNo, po.getStrInvoiceNo());
        cvDbValues.put(KEY_InvoiceDate, po.getStrInvoiceDate());

        cvDbValues.put(KEY_MenuCode, po.getiMenuCode());
        cvDbValues.put(KEY_SupplierType, po.getStrSupplierType());
        cvDbValues.put(KEY_SupplyType, po.getStrSupplyType());
        cvDbValues.put(KEY_HSNCode, po.getStrHSNCode());
        cvDbValues.put(KEY_ItemName, po.getStrItemName());
        cvDbValues.put(KEY_ItemBarcode,po.getStrBarcode());
        cvDbValues.put(KEY_Quantity, po.getDblQuantity());
        cvDbValues.put(KEY_UOM, po.getStrUOM());
        cvDbValues.put(KEY_MRP,po.getDblMRP());
        cvDbValues.put(KEY_Rate, po.getDblRate());
        cvDbValues.put(KEY_TaxableValue, po.getDblTaxableValue());
        cvDbValues.put(KEY_IGSTRate,po.getDblIGSTRate());
        cvDbValues.put(KEY_IGSTAmount,po.getDblIGSTAmount());
        cvDbValues.put(KEY_CGSTRate,po.getDblCGSTRate());
        cvDbValues.put(KEY_CGSTAmount,po.getDblCGSTAmount());
        cvDbValues.put(KEY_SGSTRate,po.getDblSGSTRate());
        cvDbValues.put(KEY_SGSTAmount,po.getDblSGSTAmount());
        cvDbValues.put(KEY_cessRate,po.getDblCessRate());
        cvDbValues.put(KEY_cessAmount,po.getDblCessAmount());

        cvDbValues.put(KEY_Amount, po.getDblAmount());
        cvDbValues.put(KEY_AdditionalChargeName, po.getStrAdditionalCharge());
        cvDbValues.put(KEY_AdditionalChargeAmount, po.getDblAdditionalChargeAmount());
        cvDbValues.put(KEY_isGoodinward, po.getiIsgoodInward());

        return dbRetail.insert(TBL_PURCHASEORDER, null, cvDbValues);
    }

    public Cursor getMaxPurchaseOrderNo() {
        Cursor cursor = dbRetail.rawQuery("SELECT MAX("+KEY_PurchaseOrderNo+") FROM " +TBL_PURCHASEORDER, null);
        return cursor;
    }

    public Cursor checkduplicatePO(int suppliercode, int purchaseorder) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_PURCHASEORDER + " WHERE " + KEY_SupplierCode + " = " + suppliercode + " AND " +
                KEY_PurchaseOrderNo + " = " + purchaseorder;
        result = dbRetail.rawQuery(queryString, null);
        return result;
    }

    public int deletePurchaseOrder(int suppliercode, int purchaseorder) {
        return dbRetail.delete(TBL_PURCHASEORDER, KEY_SupplierCode + "=" + suppliercode + " AND " + KEY_PurchaseOrderNo + " = " + purchaseorder, null);
    }

    public Cursor mGetPurchaseOrderData(String strPurchaseOrderNo){
        Cursor cursor = null;
        try{
            String select = KEY_PurchaseOrderNo + "=?";
            String[]  selectArgs = {strPurchaseOrderNo};
            String[] contactsProjection = new String[] {
                    "*"};
            if(select != null) {
                cursor = dbRetail.query(TBL_PURCHASEORDER, contactsProjection, select, selectArgs, null, null, null);
            }
        }catch (Exception ex){
            Logger.i(TAG,"Unable to get the data from purchase order no table." +strPurchaseOrderNo + " error : " +ex.getMessage());
        }
        return cursor;
    }

    public Cursor getPurchaseOrder_for_SupplierCode(String invoiceNo,String invoiceDate,String supplierCode ) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_PURCHASEORDER + " WHERE " + KEY_SupplierCode + " Like '" + supplierCode + "' AND " +
                KEY_InvoiceDate+" LIKE '"+invoiceDate+"' AND "+KEY_InvoiceNo+" LIKE '"+invoiceNo+"' AND "+KEY_isGoodinward+" LIKE '1'";
        result = dbRetail.rawQuery(queryString, null);
        return result;
    }

    // billing functions


    public int getLastBillNoforDate(String date_str) {
        Cursor cursor = dbRetail.rawQuery("SELECT MAX(InvoiceNo) as InvoiceNo FROM "+TBL_OUTWARD_SUPPLY_ITEMS_DETAILS+" WHERE "+
                KEY_InvoiceDate+" LIKE '"+date_str+"'", null);
        int invno =0;
        if(cursor!=null && cursor.moveToFirst()){
            invno = cursor.getInt(cursor.getColumnIndex(KEY_InvoiceNo));
        }
        return invno;
    }

    public Cursor getVoidBillsReport(String StartDate, String EndDate) {
        return dbRetail.query(TBL_OUTWARD_SUPPLY_ITEMS_DETAILS, new String[]{"*"},
                "BillStatus=0 AND InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null, null, null,
                "InvoiceNo");
    }


    // -----Insert Bill-----
    public long addBillDetail(BillDetailBean objBillDetail ,int reverseCharge) {
        long rData = -1;

        try{
            ContentValues cvDbValues = new ContentValues();
            cvDbValues.put(KEY_InvoiceNo, objBillDetail.getStrInvoiceNo());
            cvDbValues.put(KEY_InvoiceDate, objBillDetail.getStrInvoiceDate());
            cvDbValues.put(KEY_Time, objBillDetail.getStrTime());
            cvDbValues.put(KEY_TotalItems, objBillDetail.getiTotalItems());
            cvDbValues.put(KEY_TaxableValue, objBillDetail.getDblTotalTaxableValue());
            cvDbValues.put(KEY_IGSTAmount, objBillDetail.getDblIGSTAmount());
            cvDbValues.put(KEY_CGSTAmount, objBillDetail.getDblCGSTAmount());
            cvDbValues.put(KEY_SGSTAmount, objBillDetail.getDblSGSTAmount());
            cvDbValues.put(KEY_cessAmount, objBillDetail.getDblcessAmount());
            cvDbValues.put(KEY_SubTotal, objBillDetail.getDblSubTotal());
            cvDbValues.put(KEY_DeliveryCharge, objBillDetail.getDblDeliveryCharge());
            cvDbValues.put(KEY_GrandTotal, objBillDetail.getDblBillAmount());
            cvDbValues.put(KEY_BillAmount, objBillDetail.getDblBillAmount());
            cvDbValues.put(KEY_RoundOff, objBillDetail.getDblRoundOff());
            cvDbValues.put(KEY_PaidTotalPayment, objBillDetail.getDblPaidTotalPayment());
            cvDbValues.put(KEY_ChangePayment, objBillDetail.getDblChangePayment());
            cvDbValues.put(KEY_TotalDiscountAmount, objBillDetail.getDblTotalDiscountAmount());
            cvDbValues.put(KEY_DiscPercentage, objBillDetail.getDblTotalDiscountPercentage());

            cvDbValues.put(KEY_CashPayment, objBillDetail.getDblCashPayment());
            cvDbValues.put(KEY_CardPayment, objBillDetail.getDblCardPayment());
            cvDbValues.put(KEY_CouponPayment, objBillDetail.getDblCouponPayment());
            cvDbValues.put(KEY_PettyCashPayment, objBillDetail.getDblPettyCashPayment());
            cvDbValues.put(KEY_WalletPayment, objBillDetail.getDblWalletAmount());
            cvDbValues.put(KEY_RewardPointsAmount, objBillDetail.getDblRewardPoints());
            cvDbValues.put(KEY_AEPSAmount, objBillDetail.getDblAEPSAmount());

            cvDbValues.put(KEY_BillStatus, objBillDetail.getiBillStatus());
            cvDbValues.put(KEY_ReprintCount, objBillDetail.getiReprintCount());

            cvDbValues.put(KEY_EmployeeId, objBillDetail.getiEmployeeId());
            cvDbValues.put(KEY_USER_ID, objBillDetail.getStrUserId());
            cvDbValues.put(KEY_CustId, objBillDetail.getiCustId());
            cvDbValues.put(KEY_GSTIN, objBillDetail.getGSTIN());
            cvDbValues.put(KEY_CustName, objBillDetail.getCustname());
            cvDbValues.put(KEY_CustStateCode, objBillDetail.getCustStateCode());
            cvDbValues.put(KEY_POS, objBillDetail.getPOS());
            cvDbValues.put(KEY_BusinessType, objBillDetail.getBusinessType());
            cvDbValues.put(KEY_SALES_MAN_ID, objBillDetail.getStrSalesManId());
            cvDbValues.put(KEY_ReverseCharge,reverseCharge);
            cvDbValues.put(KEY_BillingMode,objBillDetail.getBillingMode());
            cvDbValues.put(KEY_MSWIPE_Amount,objBillDetail.getDblMSwipeAmount());
            rData = dbRetail.insert(TBL_OUTWARD_SUPPLY_ITEMS_DETAILS, null, cvDbValues);
        }catch (Exception e){
            Log.d(TAG,e.toString());
            rData = -1;
        }finally {
            //db.close();
        }
        return rData;
    }

    public long addBillItems(BillItemBean objBillItem) {
        try{
            ContentValues cvDbValues = new ContentValues();
            cvDbValues.put(KEY_InvoiceNo, objBillItem.getStrInvoiceNo());
            cvDbValues.put(KEY_InvoiceDate, objBillItem.getStrInvoiceDate());
            cvDbValues.put(KEY_ITEM_ID, objBillItem.getiItemId());
            cvDbValues.put(KEY_ItemName, objBillItem.getStrItemName());
            cvDbValues.put(KEY_ItemBarcode,objBillItem.getStrBarcode());
            cvDbValues.put(KEY_Quantity, objBillItem.getDblQty());
            cvDbValues.put(KEY_HSNCode, objBillItem.getStrHSNCode());
            cvDbValues.put(KEY_SupplyType, objBillItem.getStrSupplyType());
            cvDbValues.put(KEY_UOM, objBillItem.getStrUOM());
            cvDbValues.put(KEY_Value, objBillItem.getDblValue());
            cvDbValues.put(KEY_TaxableValue, objBillItem.getDblTaxbleValue());
            cvDbValues.put(KEY_OriginalRate, objBillItem.getDblOriginalRate());
            cvDbValues.put(KEY_IsReverseTaxEnabled, objBillItem.getStrIsReverseTaxEnabled());
            cvDbValues.put(KEY_TaxType, objBillItem.getiTaxType());
            cvDbValues.put(KEY_DiscountAmount, objBillItem.getDblDiscountAmount());
            cvDbValues.put(KEY_DiscountPercent, objBillItem.getDblDiscountPercent());

            cvDbValues.put(KEY_DepartmentCode, objBillItem.getiDeptCode());
            cvDbValues.put(KEY_CategoryCode, objBillItem.getiCategCode());
            cvDbValues.put(KEY_BrandCode, objBillItem.getiBrandCode());
            cvDbValues.put(KEY_MRP, objBillItem.getDblMRP());
            cvDbValues.put(KEY_RetailPrice, objBillItem.getDblRetailPrice());
            cvDbValues.put(KEY_WholeSalePrice, objBillItem.getDblWholeSalePrice());

            cvDbValues.put(KEY_IGSTRate, objBillItem.getDblIGSTRate());
            cvDbValues.put(KEY_IGSTAmount, objBillItem.getDblIGSTAmount());
            cvDbValues.put(KEY_CGSTRate, objBillItem.getDblCGSTRate());
            cvDbValues.put(KEY_CGSTAmount, objBillItem.getDblCGSTAmount());
            cvDbValues.put(KEY_SGSTRate, objBillItem.getDblSGSTRate());
            cvDbValues.put(KEY_SGSTAmount, objBillItem.getDblSGSTAmount());
            cvDbValues.put(KEY_cessRate, objBillItem.getDblCessRate());
            cvDbValues.put(KEY_cessAmount, objBillItem.getDblCessAmount());
            cvDbValues.put(KEY_Amount, objBillItem.getDblAmount());
            cvDbValues.put(KEY_CustName, objBillItem.getStrCustName());
            cvDbValues.put(KEY_GSTIN, objBillItem.getStrGSTIN());
            cvDbValues.put(KEY_CustStateCode, objBillItem.getStrCustStateCode());
            cvDbValues.put(KEY_CustId, objBillItem.getStrCustId());
            cvDbValues.put(KEY_BusinessType, objBillItem.getStrBusinessType());
            cvDbValues.put(KEY_BillStatus, objBillItem.getiBillStatus());
            cvDbValues.put(KEY_SALES_MAN_ID,objBillItem.getStrSalesManId());

            return dbRetail.insert(TBL_OUTWARD_SUPPLY_LEDGER, null, cvDbValues);
        }catch (Exception e){
            Logger.e(TAG,"Unable to store the bill items into OutwardSupplyLedger table." +e.getMessage());
            return -1;
        }
    }

    // -----Void Bill-----
    public int makeBillVoids(int InvoiceNo, String InvoiceDate) {
        //SQLiteDatabase db;
        int result = 0;
        try {
           // db = this.getReadableDatabase();
            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_BillStatus, 0);
            result = dbRetail.update(TBL_OUTWARD_SUPPLY_ITEMS_DETAILS, cvDbValues, KEY_InvoiceNo + "=" + InvoiceNo + " AND " +
                    KEY_InvoiceDate + " LIKE '" + InvoiceDate + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        } finally {
            return result;
        }

    }


    public Cursor getItemsFromBillItem_new(int InvoiceNo, String InvoiceDate) {
        // return dbFNB.rawQuery("Select * from BillItem, BillDetail where BillDetail.BillNumber = '" + BillNumber + "' AND BillItem.BillNumber = BillDetail.BillNumber", null);

        Cursor crsr = null;
        try {
            crsr = dbRetail.rawQuery("Select * from " + TBL_OUTWARD_SUPPLY_LEDGER + " where InvoiceNo = '" + InvoiceNo + "' AND "
                    + KEY_InvoiceDate + " LIKE '" + InvoiceDate + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
            crsr = null;
        } finally {
            return crsr;
        }
    }

    public Cursor getBillDetail(int InvoiceNumber, String InvoiceDate) {

        if (dbRetail != null)
            return dbRetail.query(TBL_OUTWARD_SUPPLY_ITEMS_DETAILS, new String[]{"*"}, KEY_InvoiceNo + "=" + InvoiceNumber +
                    " AND " + KEY_InvoiceDate + " LIKE '" + InvoiceDate + "'", null, null, null, null);
        else
            return null;
    }
    public Cursor getBillDetail(int InvoiceNumber) {

        if (dbRetail != null)
            return dbRetail.query(TBL_OUTWARD_SUPPLY_ITEMS_DETAILS, new String[]{"*"}, KEY_InvoiceNo + "=" + InvoiceNumber, null, null, null, null);
        else
            return null;
    }
    public Cursor getBillDetailByInvoiceDate(String InvoiceDate) {

        if (dbRetail != null)
            return dbRetail.query(TBL_OUTWARD_SUPPLY_ITEMS_DETAILS, new String[]{"*"}, KEY_InvoiceDate + " LIKE '" + InvoiceDate+"'", null, null, null, null);
        else
            return null;
    }

    // -----update Reprint Count for duplicate bill print-----
    public int updateBillRepintCounts(int InvoiceNo, String InvoiceDate) {
        Cursor result = null;
        int status = -1;
        try {
            result = getBillDetail(InvoiceNo, InvoiceDate);
            if (result.moveToFirst()) {
                cvDbValues = new ContentValues();
                int iReprintCount = 0;
                iReprintCount = result.getInt(result.getColumnIndex("ReprintCount"));
                cvDbValues.put(KEY_ReprintCount, iReprintCount + 1);
                String whereClause = KEY_InvoiceNo + "=" + InvoiceNo+" AND "+KEY_InvoiceDate +" LIKE '"+InvoiceDate+"'";
                status = dbRetail.update(TBL_OUTWARD_SUPPLY_ITEMS_DETAILS, cvDbValues, whereClause,null);

            } else {
                Toast.makeText(myContext, "No bill found with bill number " + InvoiceNo, Toast.LENGTH_SHORT).show();
                status = -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = -1;

        } finally {
            //db.close();
            if (result != null){
                result.close();
            }
        }

        return  status;
    }

    public Cursor getHoldResumeBillData(){
        Cursor cursor = null;
        try{
            String select = KEY_BillStatus + " = ? ";
            String[]  selectArgs = { "1" };
            String[] contactsProjection = new String[] {
                    "*"};
            cursor = dbRetail.query(TBL_HOLD_RESUME_BILL_LEDGER,contactsProjection,select,selectArgs,null,null,null);
        }catch (Exception ex){
            Logger.i(TAG,"Unable to get the data from hold resume bill outward supply ledger table." + " error : " +ex.getMessage());
        }
        return cursor;
    }

    public int getHoldResumeLastBillNo() {
        Cursor cursor = null;
        int invno = 0;
        try {
            cursor = dbRetail.rawQuery("SELECT "+ KEY_InvoiceNo +" FROM "+TBL_HOLD_RESUME_BILL_LEDGER + " WHERE "
                    + KEY_id + "= (SELECT MAX(" + KEY_id  + ")"+" FROM " +TBL_HOLD_RESUME_BILL_LEDGER +")", null);

            if (cursor != null && cursor.moveToFirst()) {
                invno = cursor.getInt(cursor.getColumnIndex(KEY_InvoiceNo));
            }
        } catch (Exception ex){
            Logger.i(TAG, "Unable to fetch the last bill no from table " +TBL_HOLD_RESUME_BILL_LEDGER
                    + "." + ex.getMessage());
            invno = -1;
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return invno;
    }

    // -----Insert Hold Resume Bill Data-----
    public long addHoldResumeBill(HoldResumeBillBean objBillItem) {
        try{
            ContentValues cvDbValues = new ContentValues();
            cvDbValues.put(KEY_InvoiceNo, objBillItem.getStrInvoiceNo());
            cvDbValues.put(KEY_BillingMode, objBillItem.getStrBillingMode()); // richa_2012
            cvDbValues.put(KEY_ITEM_ID, objBillItem.getiItemID());
            cvDbValues.put("ItemName", objBillItem.getStrItemName());
            cvDbValues.put(KEY_ItemBarcode,objBillItem.getStrBarcode());
            cvDbValues.put("Quantity", objBillItem.getDblQty());
            cvDbValues.put("Value", objBillItem.getDblValue());
            cvDbValues.put(KEY_Amount, objBillItem.getDblAmount());
            cvDbValues.put(KEY_OriginalRate, objBillItem.getDblOriginalRate());
            cvDbValues.put(KEY_IsReverseTaxEnabled, objBillItem.getStrIsReverseTaxEnabled());
            cvDbValues.put("DiscountAmount", objBillItem.getDblDiscountAmount());
            cvDbValues.put(KEY_DiscountPercent, objBillItem.getDblDiscountPercent());
            cvDbValues.put(KEY_DepartmentCode, objBillItem.getiDepartmentCode());
            cvDbValues.put(KEY_CategoryCode, objBillItem.getiCategoryCode());
            cvDbValues.put(KEY_BrandCode, objBillItem.getiBrandCode());
            cvDbValues.put("TaxType", objBillItem.getiTaxType());
            cvDbValues.put(KEY_InvoiceDate, objBillItem.getStrInvoiceDate());
            cvDbValues.put(KEY_HSNCode, objBillItem.getStrHSNCode());
            cvDbValues.put(KEY_IGSTRate, objBillItem.getDblIGSTRate());
            cvDbValues.put(KEY_IGSTAmount, objBillItem.getDblIGSTAmount());
            cvDbValues.put(KEY_CGSTRate, objBillItem.getDblCGSTRate());
            cvDbValues.put(KEY_CGSTAmount, objBillItem.getDblCGSTAmount());
            cvDbValues.put(KEY_SGSTRate, objBillItem.getDblSGSTRate());
            cvDbValues.put(KEY_SGSTAmount, objBillItem.getDblSGSTAmount());
            cvDbValues.put(KEY_cessRate, objBillItem.getDblCessRate());
            cvDbValues.put(KEY_cessAmount, objBillItem.getDblCessAmount());
            cvDbValues.put(KEY_SupplyType, objBillItem.getStrSupplyType());
            cvDbValues.put(KEY_SubTotal, objBillItem.getDblSubTotal());
            cvDbValues.put(KEY_BillAmount,objBillItem.getDblBillAmount());
            cvDbValues.put(KEY_CustName, objBillItem.getStrCustName());
            cvDbValues.put(KEY_CustPhoneNo,objBillItem.getStrCustPhone());
            cvDbValues.put(KEY_GSTIN, objBillItem.getStrGSTIN());
            cvDbValues.put(KEY_CustStateCode, objBillItem.getStrCustStateCode());
            cvDbValues.put(KEY_UOM, objBillItem.getStrUOM());
            cvDbValues.put(KEY_BusinessType, objBillItem.getStrBussinessType());
            cvDbValues.put(KEY_BillStatus, objBillItem.getiBillStatus());
            cvDbValues.put(KEY_MRP,objBillItem.getDblMRP());
            cvDbValues.put(KEY_RetailPrice,objBillItem.getDblRetailPrice());
            cvDbValues.put(KEY_WholeSalePrice,objBillItem.getDblWholeSalePrice());

            return dbRetail.insert(TBL_HOLD_RESUME_BILL_LEDGER, null, cvDbValues);
        }catch (Exception e){
            Logger.e(TAG,"Unable to store the bill items into "+ TBL_HOLD_RESUME_BILL_LEDGER +" table." +e.getMessage());
            return -1;
        }
    }

    public long updateHoldResumeBillTable(String strInvoiceNo) {
        long lUpdateresult = -1;
        try {
            ContentValues cv = new ContentValues();
            cv.put(KEY_BillStatus, "0");
            lUpdateresult = dbRetail.update(TBL_HOLD_RESUME_BILL_LEDGER, cv,
                    KEY_InvoiceNo
                            + "=?", new String[]{strInvoiceNo});
        }catch (Exception ex){
            Logger.i(TAG, " Unable to update the  invoice no : " + strInvoiceNo
                    + "from table name : " +TBL_HOLD_RESUME_BILL_LEDGER);
        }
        return lUpdateresult;
    }

    public Cursor getHoldResumeBillDataByInvoiceNo(String strInvoiceNo){
        Cursor cursor = null;
        try{
            String select = KEY_InvoiceNo + " = ? ";
            String[]  selectArgs = { strInvoiceNo };
            String[] contactsProjection = new String[] {
                    "*"};
            cursor = dbRetail.query(TBL_HOLD_RESUME_BILL_LEDGER,contactsProjection,select,selectArgs,null,null,null);
        }catch (Exception ex){
            Logger.i(TAG,"Unable to get the data from hold resume bill outward supply ledger table by strInvoiceNo." + " error : " +ex.getMessage());
        }
        return cursor;
    }

/************************************************************************************************************************************/
    /************************************************************ Reports ***************************************************************/
    /************************************************************************************************************************************/

    // -----Bill wise and Transaction Report-----
    public Cursor getBillDetailReport(String StartDate, String EndDate) {
        return dbRetail.query(TBL_BILLDETAIL, new String[]{"*"},
                " BillStatus=1 AND InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null, null, null, KEY_InvoiceDate);
    }

    // -----Day wise and Month wise Report-----
    public Cursor getDaywiseReport(String StartDate, String EndDate) {
        return dbRetail.query(TBL_BILLDETAIL, new String[]{"*"},
                "BillStatus=1 AND InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null, null, null, "InvoiceDate");
    }

    // -----Duplicate bill Report-----
    public Cursor getDuplicateBillsReport(String StartDate, String EndDate) {
        return dbRetail.query(TBL_BILLDETAIL, new String[]{"*"},
                "ReprintCount>0 AND InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null, null, null,
                "InvoiceNo");
    }

    // -----Tax Report Report-----
    public Cursor getTaxDetailforBill(String Invoiceno, String Invoicedate, String taxPercentName, String taxAmountName) {
        String selectQuery = "Select " + taxPercentName + " , " + taxAmountName + " ," + KEY_TaxableValue + "," + KEY_DiscountAmount + " FROM " + TBL_OUTWARD_SUPPLY_LEDGER +
                " WHERE " + KEY_InvoiceNo + " LIKE '" + Invoiceno + "' AND " + KEY_InvoiceDate + " LIKE '" + Invoicedate + "'";
        Cursor cursor = dbRetail.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getitems_outward_details_withCateg(String Startdate, String Enddate) {
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_LEDGER + " , " + TBL_CATEGORY + "  WHERE " + KEY_BillStatus + " = 1 AND " +
                KEY_InvoiceDate + " BETWEEN '" + Startdate + "' AND '" + Enddate + "' AND " + TBL_OUTWARD_SUPPLY_LEDGER + "." + KEY_CategoryCode + " = " + TBL_CATEGORY + "." + KEY_CategoryCode;
        Cursor result = dbRetail.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getitems_outward_details_withDept(String Startdate, String Enddate) {
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_LEDGER + " , " + TBL_DEPARTMENT + "  WHERE " + KEY_BillStatus + " = 1 AND " +
                KEY_InvoiceDate + " BETWEEN '" + Startdate + "' AND '" + Enddate + "' AND " + TBL_OUTWARD_SUPPLY_LEDGER + "." + KEY_DepartmentCode + " = " + TBL_DEPARTMENT + "." + KEY_DepartmentCode;
        Cursor result = dbRetail.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getFastSellingItemwiseReport(String StartDate, String EndDate) {
        return dbRetail.rawQuery("SELECT  * FROM " + TBL_BILLITEM +
                " WHERE BillStatus=1 AND " +
                TBL_OUTWARD_SUPPLY_LEDGER + ".InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "' ", null);
    }

    // -----Item wise Report-----
    public Cursor getItemwiseReport(String StartDate, String EndDate) {
        return dbRetail.rawQuery("SELECT * FROM " + TBL_BILLITEM + " , " + TBL_BILLDETAIL + " WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                + TBL_BILLITEM + ".InvoiceNo= " + TBL_BILLDETAIL + ".InvoiceNo AND " + TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '"
                + EndDate + "' ORDER BY item_id ASC", null);
    }

    // -----Customer wise Report-----
    public Cursor getCustomerwiseReport(String StartDate, String EndDate) {
        return dbRetail.rawQuery("SELECT * FROM " + TBL_BILLDETAIL + ", Customer WHERE OutwardSuppyItemsDetails.BillStatus=1 AND "
                + "OutwardSuppyItemsDetails.CustId>0 AND OutwardSuppyItemsDetails.CustId=Customer.CustId AND " + "OutwardSuppyItemsDetails.InvoiceDate BETWEEN '"
                + StartDate + "' AND '" + EndDate + "'", null);
    }

    public List<String> getAllCustomersforReport() {
        List<String> list = new ArrayList<String>();
        Cursor cursor = null;
        try {
            cursor = dbRetail.rawQuery("SELECT  CustId, CustName FROM Customer", null);

            list.add("Select");
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(1));// adding 2nd column data
                } while (cursor.moveToNext());
            }
        } catch (Exception ex){
            Logger.e(TAG,"Unable to get the customer data for report. getAllCustomersforReport()" +ex.getMessage());
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return list;
    }

    public List<String> getAllSalesmanforReport() {
        List<String> list = new ArrayList<String>();

        if (!dbRetail.isOpen())
            dbRetail = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = dbRetail.rawQuery("SELECT  sales_man_id as _id, Name FROM Users WHERE RoleId=4", null);

            list.add("Select");
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(1));// adding 2nd column data
                } while (cursor.moveToNext());
            }
        } catch (Exception ex){
            Logger.e(TAG,"Unable to get the customer data for report. getAllSalesmanforReport()" +ex.getMessage());
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
        return list;
    }

    public List<String> getAllUsersforReport() {
        List<String> list = new ArrayList<String>();
        Cursor cursor = null;
        try {
            cursor = dbRetail.rawQuery("SELECT  UserId as _id, Name FROM Users Where LoginId not in ('d#demo') ", null);

            list.add("Select");
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(1));// adding 2nd column data
                } while (cursor.moveToNext());
            }
        } catch (Exception ex){
            Logger.e(TAG,"Unable to get the user data for report. getAllUsersforReport()" +ex.getMessage());
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }

        return list;
    }

    public int getCustomersIdByName(String name) {
        //SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;
        try {
            Cursor cursor = dbRetail.query(TBL_CUSTOMER, null, "CustName=?", new String[]{String.valueOf(name)}, null, null, null, null);
            //if (cursor != null)
            if (cursor.moveToFirst()) {
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("CustId")));
            } else {
                id = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //db.close();
        }
        return id;
    }

    public int getUsersIdByName(String name) {
        //SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;
        try {
            Cursor cursor = dbRetail.query(TBL_USERS, null, "Name=?", new String[]{String.valueOf(name)}, null, null, null, null);
            //if (cursor != null)
            if (cursor.moveToFirst()) {
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("UserId")));
            } else {
                id = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //db.close();
        }
        // return contact
        return id;
    }

    public String getSalesmanIdByName(String name) {
       // SQLiteDatabase db = this.getReadableDatabase();
        String id = "";
        try {
            Cursor cursor = dbRetail.query(TBL_USERS, null, "Name=?", new String[]{String.valueOf(name)}, null, null, null, null);
            //if (cursor != null)
            if (cursor.moveToFirst()) {
                id = cursor.getString(cursor.getColumnIndex(KEY_SALES_MAN_ID));
            } else {
                id = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //db.close();
        }
        // return contact
        return id;
    }

    public String getSalesmanNameById(String salesmanId) {
        //SQLiteDatabase db = this.getReadableDatabase();
        String name = "";
        try {
            Cursor cursor = dbRetail.query(TBL_USERS, null, "sales_man_id=?", new String[]{String.valueOf(salesmanId)}, null, null, null, null);
            //if (cursor != null)
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex("Name"));
            } else {
                name = "-";
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           // db.close();
        }
        // return contact
        return name;
    }

    // -----Customer Detailed Report-----
    public Cursor getCustomerDetailedReport(int CustId, String StartDate, String EndDate) {
        Cursor cursor = null;
        try {
            if (!dbRetail.isOpen())
                dbRetail = this.getWritableDatabase();
            cursor = dbRetail.rawQuery("SELECT * FROM OutwardSuppyItemsDetails, Customer WHERE OutwardSuppyItemsDetails.BillStatus=1 AND "
                    + "OutwardSuppyItemsDetails.CustId=" + CustId + " AND Customer.CustId=" + CustId + " AND "
                    + "OutwardSuppyItemsDetails.InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cursor;
    }

    public Cursor getSupplierwiseReport(String StartDate, String EndDate) {
        return dbRetail.rawQuery(
                "SELECT SupplierCode, SupplierName, Amount, AdditionalChargeAmount FROM " + TBL_PURCHASEORDER + " WHERE " +
                        KEY_isGoodinward + "=1 AND " + KEY_InvoiceDate + " BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null);
    }

    // -----User detailed Report-----
    public Cursor getUserDetailedReport(String UserId, String StartDate, String EndDate) {
        Cursor cursor = null;
        try {
            if (!dbRetail.isOpen())
                dbRetail = this.getWritableDatabase();
            cursor = dbRetail.rawQuery("SELECT * FROM " + TBL_BILLDETAIL + ", Users WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                    + TBL_BILLDETAIL + ".UserId='" + UserId + "' AND " + TBL_BILLDETAIL + ".UserId=Users.UserId AND "
                    + TBL_BILLDETAIL + "." + KEY_InvoiceDate + "  BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    // -----User wise Report-----
    public Cursor getUserwiseReport(String StartDate, String EndDate) {
        return dbRetail.rawQuery("SELECT * FROM " + TBL_BILLDETAIL + ", " + TBL_USERS + " WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                        + TBL_BILLDETAIL + ".UserId=Users.UserId AND " + TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'",
                null);
    }

    // -----Sales man detailed Report-----
    public Cursor getSalesmanDetailedReport(String SalesmanId, String StartDate, String EndDate) {
        Cursor cursor = null;
        try {
            if (!dbRetail.isOpen())
                dbRetail = this.getWritableDatabase();
            cursor = dbRetail.rawQuery("SELECT * FROM " + TBL_BILLDETAIL + ", Users WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                    + TBL_BILLDETAIL + ".sales_man_id='" + SalesmanId + "' AND " + TBL_BILLDETAIL + ".sales_man_id=Users.sales_man_id AND "
                    + TBL_BILLDETAIL + "." + KEY_InvoiceDate + "  BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    // -----Sales man wise Report-----
    public Cursor getSalesmanwiseReport(String StartDate, String EndDate) {
        if (!dbRetail.isOpen())
            dbRetail = this.getWritableDatabase();
        return dbRetail.rawQuery("SELECT * FROM " + TBL_BILLDETAIL + ", " + TBL_USERS + " WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                        + TBL_BILLDETAIL + ".sales_man_id=Users.sales_man_id AND " + TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'",
                null);
    }

    public Cursor getOutwardB2b(String StartDate, String EndDate) {

        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + StartDate +
                "' AND '" + EndDate + "' AND " + KEY_BusinessType + " LIKE 'B2B' AND BillStatus =1" +
                " order by GSTIN asc";
        Cursor result = dbRetail.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getitems_b2b(String No, String Date, String cust_GSTIN) {
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_LEDGER + " WHERE " + KEY_InvoiceNo + " Like '" + No + "' AND " +
                KEY_InvoiceDate + " LIKE '" + Date + "' AND "/*+KEY_GSTIN+" LIKE '"+cust_GSTIN+"' AND "+*/ +
                KEY_BusinessType + " LIKE 'B2B'";
        Cursor result = dbRetail.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getOutwardB2Cl(String startDate, String endDate) {
        String b2c = "B2C";
        /*String selectQuery = "SELECT * FROM "+ TBL_OUTWARD_SUPPLY_LEDGER + " WHERE BusinessType LIKE '"+b2c+"'"; // AND GrandTotal > 250000 AND IGSTRate > 0";*/
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + startDate +
                "' AND '" + endDate + "' AND " + KEY_BusinessType + " LIKE 'B2C' AND " + KEY_SubTotal + " > 250000 "
                + " AND BillStatus =1 Order By CustStateCode asc";
        Cursor result = dbRetail.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getitems_b2cl(String No, String Date, String custname_str, String statecode_str) {
        //No = String.valueOf(160005);
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_LEDGER + " WHERE " + KEY_InvoiceNo + " LIKE '" + No + "' AND " +
                KEY_InvoiceDate + " LIKE '" + Date + "' AND " + KEY_CustName + "  LIKE '" + custname_str + "' AND " + KEY_CustStateCode + " LIKE '" +
                statecode_str + "'";
        Cursor result = dbRetail.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getOutwardB2Cs(String startDate, String endDate) {
        String b2c = "B2C";
        /*String selectQuery = "SELECT * FROM "+ TBL_OUTWARD_SUPPLY_LEDGER + " WHERE BusinessType LIKE '"+b2c+"'"; // AND GrandTotal > 250000 AND IGSTRate > 0";*/
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + startDate +
                "' AND '" + endDate + "' AND " + KEY_BusinessType + " LIKE 'B2C' AND BillStatus = 1 ";
        Cursor result = dbRetail.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getAllInvoices_outward(String startDate, String endDate) {
        String selectQuery = "Select * from " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE " + KEY_InvoiceDate +
                " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        return dbRetail.rawQuery(selectQuery, null);
    }

    public Cursor getPurchaseOrder_for_unregistered(String startDate, String endDate) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_PURCHASEORDER + " WHERE " +
                /*KEY_PurchaseOrderNo+" LIKE '"+purchaseorder+"' AND "+*/
                KEY_SupplierType + " LIKE 'UnRegistered' AND " +
                KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" + endDate + "' AND " + KEY_isGoodinward + " LIKE '1'";
        result = dbRetail.rawQuery(queryString, null);
        return result;
    }

    public Cursor getGSTR1_CDN(String startDate, String endDate) {
        /*String selectQuery = "SELECT * FROM CreditDebitOutward WHERE  " +
                KEY_NoteDate + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Cursor result = dbRetail.rawQuery(selectQuery, null);*/
        Cursor result = null;
        return result;
    }

    public Cursor getitems_outward_details(String Startdate, String Enddate) {
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_LEDGER + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + Startdate + "' AND '" + Enddate + "' ";
        Cursor result = dbRetail.rawQuery(selectQuery, null);
        return result;
    }

    public ArrayList<String> getGSTR2_b2b_gstinList(String startDate, String endDate) {
        String selectQuery = "SELECT DISTINCT GSTIN FROM " + TBL_PURCHASEORDER + " WHERE  " + KEY_InvoiceDate +
                " BETWEEN '" + startDate + "' AND '" + endDate + "' AND " + KEY_SupplierType + " LIKE 'Registered' AND " +
                KEY_isGoodinward + " LIKE '1'";
        Cursor cursor = dbRetail.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
            if (gstin == null)
                continue;
            else
                list.add(gstin);
        }
        return list;
    }

    public Cursor getPurchaseOrder_for_gstin(String startDate, String endDate, String gstin) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_PURCHASEORDER + " WHERE " + KEY_GSTIN + " Like '" + gstin + "' AND " +
                KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" + endDate + "' AND " + KEY_isGoodinward + " LIKE '1'";
        result = dbRetail.rawQuery(queryString, null);
        return result;
    }

    public Cursor getPurchaseOrder_for_gstin(String invoiceNo, String invoiceDate, String gstin, String purchaseorder) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_PURCHASEORDER + " WHERE " + KEY_GSTIN + " Like '" + gstin + "' AND " +
                /*KEY_PurchaseOrderNo+" LIKE '"+purchaseorder+"' AND "+*/
                KEY_InvoiceDate + " LIKE '" + invoiceDate + "' AND " + KEY_InvoiceNo + " LIKE '" + invoiceNo + "' AND " + KEY_isGoodinward + " LIKE '1'";
        result = dbRetail.rawQuery(queryString, null);
        return result;
    }

    public Cursor getPurchaseOrder_for_unregisteredSupplier(String invoiceNo, String invoiceDate, String purchaseorder, String supplierCode) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_PURCHASEORDER + " WHERE " + KEY_SupplierCode + " Like '" + supplierCode + "' AND " +
                KEY_SupplierType + " LIKE 'UnRegistered' AND " +
                /*KEY_PurchaseOrderNo+" LIKE '"+purchaseorder+"' AND "+*/
                KEY_InvoiceDate + " LIKE '" + invoiceDate + "' AND " + KEY_InvoiceNo + " LIKE '" + invoiceNo + "' AND " + KEY_isGoodinward + " LIKE '1'";
        result = dbRetail.rawQuery(queryString, null);
        return result;
    }

    // -----Retrieve Bill setting-----
    public Cursor getBillSetting() {
        return dbRetail.query(TBL_BILLSETTING, new String[]{"*"}, null, null, null, null, null);
    }

    public int getEnvironmentSetting() {
        int result =0;
        Cursor cursor = null;
        try {
            cursor = dbRetail.rawQuery("SELECT * FROM " + TBL_BILLSETTING, null);
            if(cursor!=null && cursor.moveToFirst())
            {
                result = cursor.getInt(cursor.getColumnIndex(KEY_Environment));
            }
        } catch (Exception e) {
            e.printStackTrace();
            cursor = null;
            result = 0;

        } finally {
            //db.close();
            cursor.close();
            return result;
        }

    }


    public ArrayList<String> getGSTR1_CDN_gstinlist(String startDate, String endDate) {
        ArrayList<String> list = new ArrayList<>();
        /*String selectQuery = "SELECT DISTINCT GSTIN FROM CreditDebitOutward WHERE  " + KEY_NoteDate +
                " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Cursor cursor = dbRetail.rawQuery(selectQuery, null);
         list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {
            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
            if(gstin==null)
                continue;
            else
                list.add(gstin);
        }*/
        return list;
    }

    public Cursor getGSTR1_CDN_forgstin(String startDate, String endDate, String gstin) {
        Cursor result = null;
        /*String selectQuery = "SELECT * FROM CreditDebitOutward WHERE  " + KEY_GSTIN + " = '" + gstin +
                "' and " + KEY_NoteDate + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
         result = dbRetail.rawQuery(selectQuery, null);*/
        return result;
    }

    public Cursor getNoteDetails(String noteNo, String noteDate, String gstin) {
        Cursor result = null;
        /*String selectQuery = "SELECT * FROM CreditDebitOutward WHERE  " + KEY_NoteNo + " LIKE '" + noteNo +
                "' and " + KEY_NoteDate + " Like '" + noteDate + "' AND " +KEY_GSTIN+" LIKE '" +gstin + "'";
        Cursor result = dbRetail.rawQuery(selectQuery, null);*/
        return result;
    }
    public Cursor getInvoices_outward(String startDate, String endDate)
    {
        String selectQuery = "Select * from "+TBL_OUTWARD_SUPPLY_ITEMS_DETAILS+" WHERE BillStatus =1 AND "+KEY_InvoiceDate+
                " BETWEEN '"+startDate+"' AND '"+endDate+"'";
        return dbRetail.rawQuery(selectQuery, null);
    }


    public ArrayList<String> getGSTR1B2B_A_gstinList(String startDate, String endDate) {
        ArrayList<String> list = new ArrayList<>();
        /*String selectQuery = "SELECT DISTINCT GSTIN FROM " + TBL_GSTR1_AMEND + " WHERE  " +
                KEY_BusinessType + " = 'B2BA' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" +
                endDate + "'  ";
        Cursor cursor = dbRetail.rawQuery(selectQuery, null);

        while(cursor!=null && cursor.moveToNext())
        {
            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
            list.add(gstin);
        }
*/
        return list;
    }

    public ArrayList<String> getGSTR1B2CL_stateCodeList_ammend(String startDate, String endDate) {
        ArrayList<String> list = new ArrayList<>();
        /*String selectQuery = "SELECT  CustStateCode FROM " + TBL_GSTR1_AMEND + " WHERE  " +
                KEY_BusinessType + " = 'B2CLA' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" +
                endDate + "' ";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);

        while(cursor!=null && cursor.moveToNext())
        {
//            String pos = cursor.getString(cursor.getColumnIndex("POS"));
            String state_cd = cursor.getString(cursor.getColumnIndex("CustStateCode"));
            if(!list.contains(state_cd))
                list.add(state_cd);
        }
*/
        return list;
    }

    public Cursor getGSTR1B2CL_stateCodeCursor_ammend(String startDate, String endDate, String stateCd) {
        /*String selectQuery = "SELECT  CustStateCode, POS, Invoicedate, InvoiceNo,OriginalInvoicedate, " +
                "OriginalInvoiceNo,CustName,TaxableValue," +
                "ProvisionalAssess, EcommerceGSTIN FROM " + TBL_GSTR1_AMEND + " WHERE  " +
                KEY_BusinessType + " = 'B2CLA' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" +
                endDate + "' AND "+KEY_CustStateCode+" LIKE '"+stateCd+"'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);*/
        Cursor cursor = null;
        return cursor;
    }

    public Cursor getGSTR1B2CL_invoices_ammend(String InvoiceNo, String InvoiceDate, String custState, String custName,
                                               String pos) {
        Cursor cursor = null;
        /*String selectQuery = "SELECT  * FROM " + TBL_GSTR1_AMEND + " WHERE  " +
                KEY_BusinessType + " = 'B2CLA' AND " + KEY_InvoiceDate + " LIKE '" + InvoiceDate + "' AND " +
                KEY_InvoiceNo + " LIKE '"+InvoiceNo+"' AND "+KEY_CustStateCode+" LIKE '"+custState+"' AND "+KEY_CustName+
                " LIKE '"+custName+*//*"' AND "+KEY_POS+" LIKE '"+pos+*//*"'";
        cursor = dbFNB.rawQuery(selectQuery, null);*/
        return cursor;
    }

    public Cursor getGSTR1B2b_A_for_gstin(String StartDate, String EndDate, String gstin) {
        Cursor result =null;
        /*String b2b = "B2B";

        String selectQuery = "SELECT InvoiceNo, InvoiceDate, GSTIN,OriginalInvoiceNo,InvoiceNo," +
                " InvoiceDate, OriginalInvoiceDate, TaxableValue , CustStateCode, ReverseCharge,ProvisionalAssess,EcommerceGSTIN FROM " + TBL_GSTR1_AMEND + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + StartDate +
                "' AND '" + EndDate + "' AND " + KEY_BusinessType + " LIKE 'B2BA' AND "+KEY_GSTIN+" LIKE '"+gstin+"'";
        result = dbFNB.rawQuery(selectQuery, null);*/
        return result;
    }

    public Cursor getitems_b2ba(String No_ori, String Date_ori,String No, String Date, String cust_GSTIN, String custStateCode) {
        Cursor result = null;
        /*String selectQuery = "SELECT * FROM " + TBL_GSTR1_AMEND + " WHERE " + KEY_InvoiceNo + " Like '" + No + "' AND " +
                KEY_InvoiceDate + " LIKE '" + Date + "' AND "+KEY_GSTIN+" LIKE '"+cust_GSTIN+"' AND "+ KEY_OriginalInvoiceNo+" LIKE '"+No_ori+"' AND "+KEY_OriginalInvoiceDate
                +" LIKE '"+Date_ori+"' AND "+KEY_BusinessType+" LIKE 'B2BA' AND "+
                KEY_CustStateCode+" LIKE '"+custStateCode+"'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);*/
        return result;
    }

    public Cursor getGSTR1B2CSAItems(String startDate, String endDate) {
        Cursor result = null;
        /*String selectQuery = "SELECT * FROM " + TBL_GSTR1_AMEND + " WHERE  " + KEY_BusinessType + " = 'B2CSA' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
         result = dbFNB.rawQuery(selectQuery, null);*/
        return result;
    }

    public ArrayList<String> getGSTR2_b2b_A_gstinList(String startDate, String endDate) {
        ArrayList<String> list = new ArrayList<>();
        /*String selectQuery = "SELECT DISTINCT GSTIN FROM "+TBL_GSTR2_AMEND+" WHERE  " + KEY_InvoiceDate +
                " BETWEEN '" + startDate + "' AND '" + endDate + "' AND "+KEY_SupplierType+" LIKE 'Registered' ";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {
            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
            if(gstin==null)
                continue;
            else
                list.add(gstin);
        }*/
        return list;
    }

    public Cursor getGSTR2_b2bA_invoices_for_gstin_registered(String startDate, String endDate,String gstin ) {
        Cursor result = null;
        /*String queryString = "Select * FROM " + TBL_GSTR2_AMEND + " WHERE " + KEY_GSTIN + " Like '" + gstin + "' AND " +
                KEY_InvoiceDate+" BETWEEN '"+startDate+"' AND '"+endDate+"' AND  "+KEY_SupplierType+" LIKE 'Registered'";
        result = dbFNB.rawQuery(queryString, null);*/
        return result;
    }

    public Cursor getGSTR2_b2bA_ammends_for_gstin_registered(String invoiceNo,String invoiceDate,String gstin,String invoiceNo_ori,
                                                             String invoicedate_ori)
    {
        Cursor result = null;
        /*String queryString = "Select * FROM " + TBL_GSTR2_AMEND + " WHERE " + KEY_GSTIN + " Like '" + gstin + "' AND " +
                KEY_InvoiceDate+" LIKE '"+invoiceDate+"' AND "+KEY_InvoiceNo+" LIKE '"+invoiceNo+"' AND "+KEY_SupplierType+
                " LIKE 'Registered' AND "+KEY_OriginalInvoiceNo+" LIKE '"+invoiceNo_ori+"' AND  "+KEY_OriginalInvoiceDate+" LIKE '"
                +invoicedate_ori+"'";
        result = dbFNB.rawQuery(queryString, null);*/
        return result;
    }
    public Cursor getGSTR2_b2b_A_unregisteredSupplierList(String startDate,String endDate ) {
        Cursor result = null;
        /*String queryString = "Select * FROM " + TBL_GSTR2_AMEND + " WHERE " +
                *//*KEY_PurchaseOrderNo+" LIKE '"+purchaseorder+"' AND "+*//*
                KEY_SupplierType+" LIKE 'UnRegistered' AND "+
                KEY_InvoiceDate+" BETWEEN '"+startDate+"' AND '"+endDate+"'";
        result = dbFNB.rawQuery(queryString, null);*/
        return result;
    }

    public Cursor getGSTR2_A_ammend_for_supplierName(String invoiceNo,String invoiceDate,String invoiceNo_ori,String invoiceDate_ori,
                                                     String supplierName, String pos_supplier ) {
        Cursor result = null;
        /*String queryString = "Select * FROM " + TBL_GSTR2_AMEND + " WHERE " + KEY_OriginalInvoiceNo + " Like '" + invoiceNo_ori + "' AND " +
                KEY_OriginalInvoiceDate+" LIKE '"+invoiceDate_ori+"' AND "+ KEY_SupplierType+" LIKE 'UnRegistered' AND "+
                KEY_InvoiceDate+" LIKE '"+invoiceDate+"' AND "+KEY_InvoiceNo+" LIKE '"+invoiceNo+"' AND "+
                KEY_GSTIN+" LIKE '"+supplierName+"' AND "+KEY_POS+" LIKE '"+pos_supplier+"'";
        result = dbFNB.rawQuery(queryString, null);*/
        return result;
    }
    public Cursor getGSTR2_CDN_forgstin(String startDate, String endDate, String gstin) {
        Cursor result = null;
        /*String selectQuery = "SELECT * FROM CreditDebitInward WHERE  " + KEY_GSTIN + " = '" + gstin +
                "' and " + KEY_NoteDate + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);*/
        return result;
    }

    public ArrayList<String> gethsn_list_for_invoices(Cursor cursor) {
        ArrayList<String > list = new ArrayList<>();
        while (cursor!=null && cursor.moveToNext())
        {
            String invNo = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
            String invDt = cursor.getString(cursor.getColumnIndex("InvoiceDate"));

            String selectQuery = "SELECT  HSNCode FROM "+TBL_OUTWARD_SUPPLY_LEDGER+" WHERE  "
                    + KEY_InvoiceDate + " LIKE '" + invDt + "' AND " + KEY_InvoiceNo +" LIKE '"+invNo+"'";
            Cursor result = dbRetail.rawQuery(selectQuery, null);
            while (result!=null && result.moveToNext())
            {
                String hsn_new = result.getString(result.getColumnIndex("HSNCode"));
                if(!list.contains(hsn_new))
                {
                    list.add(hsn_new);
                }
            }
        }
        return list;
    }

    public Cursor gethsn(String startDate , String endDate, String hsn , String BusinessType)
    {

        String selectQuery = "Select OutwardSuppyItemsDetails.POS, OutwardSupplyLedger.* from "+TBL_OUTWARD_SUPPLY_LEDGER+", " +TBL_OUTWARD_SUPPLY_ITEMS_DETAILS+" Where  OutwardSupplyLedger."+KEY_InvoiceDate+
                " BETWEEN '"+startDate+"' AND '"+endDate+"' AND OutwardSupplyLedger."+KEY_HSNCode+" LIKE '"+hsn+"' AND OutwardSupplyLedger."+
                KEY_BusinessType+" LIKE '" +BusinessType+"' "+
                "AND "+TBL_OUTWARD_SUPPLY_ITEMS_DETAILS+".InvoiceNo = OutwardSupplyLedger.InvoiceNo AND " +
                "OutwardSuppyItemsDetails.InvoiceDate =OutwardSupplyLedger.InvoiceDate  AND OutwardSuppyItemsDetails.BillStatus = 1 ";
        return dbRetail.rawQuery(selectQuery, null);
    }

    public ArrayList<String> getGSTR2_CDN_gstinlist(String startDate, String endDate) {
        ArrayList<String> list = new ArrayList<>();
        /*String selectQuery = "SELECT DISTINCT GSTIN FROM CreditDebitInward WHERE  " + KEY_NoteDate +
                " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {
            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
            if(gstin==null)
                continue;
            else
                list.add(gstin);
        }*/
        return list;
    }

    public long insertTransactionDetails(String invoiceNo, String InvoiceDate, String transactionName, String transactionid, double amount)
    {
        long result =0;


        ContentValues cv = new ContentValues();
        cv.put(KEY_InvoiceNo, invoiceNo);
        cv.put(KEY_InvoiceDate, InvoiceDate);
        cv.put(KEY_TransactionName, transactionName);
        cv.put(KEY_TransactionId, transactionid);
        cv.put(KEY_Amount, amount);

        result = dbRetail.insert(TBL_TRANSACTION_DETAIL, null, cv);

        return result;
    }

    public long saveTransaction(Payment payment) {
        long status = 0;
        ContentValues contentValues = new ContentValues();
        //contentValues.put(KEY_USER_ID,user.getUserId());
        contentValues.put(KEY_MERCHANT_NAME, payment.getMerchantName());
        contentValues.put(KEY_MERCHANT_ADDRESS, payment.getMerchantAdd());
        contentValues.put(KEY_DATE_TIME, payment.getDateTime());
        contentValues.put(KEY_M_ID, payment.getmId());
        contentValues.put(KEY_T_ID, payment.gettId());
        contentValues.put(KEY_BATCH_NO, payment.getBatchNo());
        contentValues.put(KEY_VOUCHER_NO, payment.getVoucherNo());
        contentValues.put(KEY_REFERENCE_NO, payment.getRefNo());
        contentValues.put(KEY_SALES_TYPE, payment.getSaleType());
        contentValues.put(KEY_CARD_NO, payment.getCardNo());
        contentValues.put(KEY_TRX_TYPE, payment.getTrxType());
        contentValues.put(KEY_CARD_TYPE, payment.getCardType());
        contentValues.put(KEY_EXP_DATE, payment.getExpDate());
        contentValues.put(KEY_EMVSIGN_EXPDATE, payment.getEmvSigExpDate());
        contentValues.put(KEY_CARDHOLDER_NAME, payment.getCardHolderName());
        contentValues.put(KEY_CURRENCY, payment.getCurrency());
        contentValues.put(KEY_CASH_AMOUNT, payment.getCashAmount());
        contentValues.put(KEY_BASE_AMOUNT, payment.getBaseAmount());
        contentValues.put(KEY_TIP_AMOUNT, payment.getTipAmount());
        contentValues.put(KEY_TOTAL_AMOUNT, payment.getTotalAmount());
        contentValues.put(KEY_AUTH_CODE, payment.getAuthCode());
        contentValues.put(KEY_RRNO, payment.getRrNo());
        contentValues.put(KEY_CERTIFI, payment.getCertif());
        contentValues.put(KEY_APP_ID, payment.getAppId());
        contentValues.put(KEY_APP_NAME, payment.getAppName());
        contentValues.put(KEY_TVR, payment.getTvr());
        contentValues.put(KEY_TSI, payment.getTsi());
        contentValues.put(KEY_APP_VERSION, payment.getAppVersion());
        contentValues.put(KEY_IS_PINVERIFIED, payment.getIsPinVarifed());
        contentValues.put(KEY_STAN, payment.getStan());
        contentValues.put(KEY_CARD_ISSUER, payment.getCardIssuer());
        contentValues.put(KEY_CARD_EMI_AMOUNT, payment.getEmiPerMonthAmount());
        contentValues.put(KEY_TOTAL_PAYAMOUNT, payment.getTotal_Pay_Amount());
        contentValues.put(KEY_NO_EMI, payment.getNoOfEmi());
        contentValues.put(KEY_INTEREST_RATE, payment.getInterestRate());
        contentValues.put(KEY_BILL_NO, payment.getBillNo());
        contentValues.put(KEY_FIRST_DIGIT_OF_CARD, payment.getFirstDigitsOfCard());
        contentValues.put(KEY_IS_INCONV, payment.getIsConvenceFeeEnable());
        contentValues.put(KEY_INVOICE, payment.getInvoiceNo());
        contentValues.put(KEY_TRX_DATE, payment.getTrxDate());

        contentValues.put(KEY_TRX_IMG_DATE, payment.getTrxImgDate());
        contentValues.put(KEY_CHEQUE_DATE, payment.getChequeDate());
        contentValues.put(KEY_CHEQUE_NO, payment.getChequeNo());

        try {
            status = dbRetail.insert(TBL_TRANSACTIONS, null, contentValues);

        } catch (Exception e) {
            status = 0;
            e.printStackTrace();
            //Log.d(TAG,e.toString());
        }
        return status;
    }


}




