package pos.wepindia.com.retail;

/**
 * Created by Administrator on 09-01-2018.
 */

public interface Constants {

    // Database Handler

    public final String SHAREDPREFERENCE = "SharedPreference";
    public  final String KEY_id = "_id";
    public  final String KEY_CustId = "CustId";


    public static final int ROLE_LIST = 1;
    public static final int ACCESS_PERMISSION_FOR_ROLE = 2;
    public static String USERNAME = "UserName";
    public static String USERID = "UserId";
    public  String USERROLEID = "UserRoleId";

    //Billing Screens
    public String CUSTID = "CustId";
    public String TOTALBILLAMOUNT = "TotalBillAmount";
    public String PHONENO = "PhoneNo";
    public String TAXABLEVALUE = "TaxableValue";
    public String TAXAMOUNT = "TaxAmount";
    public String OTHERCHARGES = "OtherCharges";
    public String DISCOUNTAMOUNT = "DiscountAmount";
    public String TAXTYPE = "TaxType";
    public String ROUNDOFFAMOUNT = "RoundOffAmount";

    //MSwipe
    public int REQUEST_CODE_CARD_PAYMENT = 12;

    //PayBill
    public String EWALLET = "E-Wallet";
    public String COUPON = "Coupon";
    public String CREDITCUSTOMER = "Credit Customer";
    public String CASH = "Cash";
    public String OTHERCARDS = "Other Cards";
    public String REWARDSPOINTS = "Reward Points";
    public String AEPS_UPI = "AEPS/UPI";
    public String MSWIPE = "MSwipe";
    public String PETTYCASH = "PettyCash";
    String DISCOUNT = "Discount";
    String ORDERDELIVERED = "Order Delivered";
    String ORDERLIST = "Order List";

    //FilePicker
    public String FRAGMENTNAME = "FragmentName";

    public int ALL_ITEMS = 0;
    public int FAVOURITE = 1;
    public int DEPARTMENT = 2;
    public int CATEGORY = 3;
    public int DISPLAY_TAB_ITEM = 0;
    public int DISPLAY_TAB_DEPARTMENT = 1;
    public int DISPLAY_TAB_CATEGORY = 2;
    public static final String MRP = "MRP";
    public static final String RETAIL_PRICE = "Retail Price";
    public static final String WHOLE_SALE_PRICE = "Whole Sale Price";

    public static final String DYNAMIC_DISCOUNT = "dynamic_discount";
    public static final String DISCOUNT_TYPE = "discount_type";
    public static final String PRICE_CHANGE = "price_change";

    public static final String isDISCOUNT_TYPE = "isdiscount_type";
    public static final String isPRICE_CHANGE = "isprice_change";
    public static final String isBILL_WITH_STOCK = "isbill_with_stock";

    public String BILLING = "Billing";
    public String ITEM_MASTER = "Item Master";
    public String CUSTOMER_MASTER = "Customer Master";
    public String USER_MANAGEMENT = "User Management";
    public String ADD_ROLE = "Add Role";
    public String PRICE_STOCK = "Price/Stock";
    public String CONFIGURATION = "Configuration";
    public String SUPPLIER_DETAILS = "Supplier Details";
    public String SUPPLIER_ITEM_LINKAGE = "Supplier Item Linkage";
    public String PURCHASE_ORDER = "Purchase Order";
    public String GOODS_INWARD_NOTE = "Goods Inward Note";
    public String REPORTS = "Reports";
    public String SETTINGS = "Settings";
    public String DAYEND = "Day End";
    public String SALES_MAN_ID_KEY = "Salesman";

    // Reports
    public  String TOTALIGSTAMOUNT = "TotalIGSTAmount";
    public  String TOTALCGSTAMOUNT = "TotalCGSTAmount";
    public  String TOTALSGSTAMOUNT = "TotalSGSTAmount";
    public  String TOTALcessAMOUNT = "TotalcessAmount";


    //Transaction Reports
    public static final String MSWIPE_AMOUNT_TOTAL = "mSwipeAmountTotal";
    public static final String AEPS_AMOUNT_TOTAL = "AEPSAmountTotal";

}
