package pos.wepindia.com.retail.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mswipetech.wisepad.payment.MSwipePaymentActivity;
import com.mswipetech.wisepad.payment.PasswordChangeActivity;
import com.mswipetech.wisepad.payment.fragments.FragmentLogin;
import com.wepindia.printers.WepPrinterBaseActivity;
import com.razorpay.PaymentResultListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.Constants;
import pos.wepindia.com.retail.GenericClass.BillNoReset;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.MyKeyEventListener;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.OnMSwipeResultResponseListener;
import pos.wepindia.com.retail.view.Billing.BillingFragment;
import pos.wepindia.com.retail.view.Billing.PayBillFragment;
import pos.wepindia.com.retail.view.CustomerMasters.CustomerListFragment;
import pos.wepindia.com.retail.view.PriceAndStock.PriceStockFragment;
import pos.wepindia.com.retail.view.Reports.ReportFragment;
import pos.wepindia.com.retail.view.UserMasters.AddRoleFragment;
import pos.wepindia.com.retail.view.UserMasters.UserManagementFragment;
import pos.wepindia.com.retail.view.Configuration.ConfigurationFragment;
import pos.wepindia.com.retail.view.ItemMasters.ItemMastersFragment;
import pos.wepindia.com.retail.view.settings.AppSettingsFragment;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.AccessPermissionRoleBean;

/**
 * Created by MohanN on 12/20/2017.
 */

public class HomeActivity extends WepPrinterBaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, Constants,PaymentResultListener, FragmentLogin.OnLoginCompletedListener {

    private static final String TAG = HomeActivity.class.getName();

    public static DatabaseHandler dbHandler;
    MessageDialog msgBox;
    public boolean isPrinterAvailable = false;
    public static int PRINTER_STATUS = -1;
    Context myContext;

    Fragment currentFragment;

    String userRoleId;
    String userId;
    String userName;
    @BindView(R.id.tv_nav_logged)
    TextView txtUserLoggedIn;
    @BindView(R.id.tv_nav_date)
    TextView txtCurrentDate;

    int DATE_AND_TIME = 1; // 1->Auto, 0->manual
    String BUSINESS_DATE = "";

    private int BILLING_ACCESS_PER = 0;
    private int ITEM_MASTER_ACCESS_PER = 0;
    private int CUSTOMER_MASTER_ACCESS_PER = 0;
    private int USER_MANAGEMENT_ACCESS_PER = 0;
    private int ADD_ROLE_ACCESS_PER = 0;
    private int PRICE_STOCK_ACCESS_PER = 0;
    private int CONFIGURATION_ACCESS_PER = 0;
    private int SUPPLIER_DETAILS_ACCESS_PER = 0;
    private int SUPPLIER_ITEM_LINKAGE_ACCESS_PER = 0;
    private int PURCHASE_ORDER_ACCESS_PER = 0;
    private int GOODS_INWARD_NOTE_ACCESS_PER = 0;
    private int REPORTS_ACCESS_PER = 0;
    private int SETTINGS_ACCESS_PER = 0;
    private int DAY_END_ACCESS_PER = 0;

    List<AccessPermissionRoleBean> accessPermissionRoleBeanList;


    @Override
    public void onConfigurationRequired() {
        askForConfig();
        Logger.i(TAG, "working till here configuration!");
    }

    @Override
    public void onPrinterAvailable(int flag) {
//        String prf = Preferences.getSharedPreferencesForPrint(HomeActivity.this).getString("bill", "--Select--");
        Logger.i(TAG, "working till here printer available!");

        if (flag == 2) {
//            btn_PrintBill.setEnabled(false);
//            btn_Reprint.setEnabled(false);
            SetPrinterAvailable(false);
        } else if (flag == 5) {
//            btn_PrintBill.setEnabled(true);
//            btn_Reprint.setEnabled(true);
//            feedPrinter();
            SetPrinterAvailable(true);
        } else if (flag == 0) {
//            btn_PrintBill.setEnabled(true);
//            btn_Reprint.setEnabled(true);
            SetPrinterAvailable(false);
        }
    }

    public void SetPrinterAvailable(boolean flag) {

        if (flag) {
            try {
                android.support.v4.app.FragmentManager fragmentManager = HomeActivity.this.getSupportFragmentManager();
                List<Fragment> fragments = fragmentManager.getFragments();
                if (fragments != null) {
                    for (Fragment fragment : fragments) {
                        if (fragment != null && fragment.isVisible()) {
                            BillingFragment fragment1 = (BillingFragment) fragment;
                            fragment1.onPrinterConnectsEnableWidgets();
                        }
                    }
                }
            }catch (Exception ex){
                Logger.i(TAG,"Unable to connect printer and change printer widget's color");
            }
            Toast.makeText(this, "Bill Printer Status : " + "Connected", Toast.LENGTH_SHORT).show();
        }
        isPrinterAvailable = flag;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        try{
            myContext = this;
            dbHandler = new DatabaseHandler(myContext);
            dbHandler.CreateDatabase();
            dbHandler.OpenDatabase();
            msgBox = new MessageDialog(myContext);

            userId = getIntent().getStringExtra(USERID);
            userName = getIntent().getStringExtra(USERNAME);
            userRoleId = getIntent().getStringExtra(USERROLEID);
            accessPermissionRoleBeanList = new ArrayList<AccessPermissionRoleBean>();
            mGetRoleAccessPermissionData();
            getBillSettings();
            checkForAutoDayEnd();
        }catch(Exception e)
        {
            Toast.makeText(myContext, "Oops some error occurred", Toast.LENGTH_SHORT).show();
            Logger.d(e.toString());
        }

        //App crash error log
        if (Logger.LOG_FOR_FIRST) {
            Logger.printLog();
            Logger.LOG_FOR_FIRST = false;
        }
        if (Auditing.AUDIT_FOR_FIRST) {
            Auditing.printAudit();
            Auditing.AUDIT_FOR_FIRST = false;
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View vNavigation = navigationView.getHeaderView(0);

        ButterKnife.bind(this, vNavigation);
        txtUserLoggedIn.setText(userName);
        //getting current date and time using Date class
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        Date dateobj = new Date();
        txtCurrentDate.setText(df.format(dateobj));


        toolbar.setTitleTextColor(getResources().getColor(R.color.color_black));

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.color_black));

        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            setTextColorForMenuItem(navigationView.getMenu().getItem(i), R.color.widget_color);
            for (int j = 0; j < navigationView.getMenu().getItem(i).getSubMenu().size(); j++) {
                setTextColorForMenuItem(navigationView.getMenu().getItem(i).getSubMenu().getItem(j), R.color.grey);
            }
        }

        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            for(int j = 0; j < navigationView.getMenu().getItem(i).getSubMenu().size(); j++){
                switch (navigationView.getMenu().getItem(i).getSubMenu().getItem(j).getTitle().toString()) {
                    case Constants.BILLING:
                        if (BILLING_ACCESS_PER == 1) {
                            setTextColorForMenuItem(navigationView.getMenu().getItem(i).getSubMenu().getItem(j), R.color.color_black);
                        }
                        break;
                    case Constants.ITEM_MASTER:
                        if (ITEM_MASTER_ACCESS_PER == 1) {
                            setTextColorForMenuItem(navigationView.getMenu().getItem(i).getSubMenu().getItem(j), R.color.color_black);
                        }
                        break;
                    case Constants.CUSTOMER_MASTER:
                        if (CUSTOMER_MASTER_ACCESS_PER == 1) {
                            setTextColorForMenuItem(navigationView.getMenu().getItem(i).getSubMenu().getItem(j), R.color.color_black);
                        }
                        break;
                    case Constants.USER_MANAGEMENT:
                        if (USER_MANAGEMENT_ACCESS_PER == 1) {
                            setTextColorForMenuItem(navigationView.getMenu().getItem(i).getSubMenu().getItem(j), R.color.color_black);
                        }
                        break;
                    case Constants.ADD_ROLE:
                        if (ADD_ROLE_ACCESS_PER == 1) {
                            setTextColorForMenuItem(navigationView.getMenu().getItem(i).getSubMenu().getItem(j), R.color.color_black);
                        }
                        break;
                    case Constants.PRICE_STOCK:
                        if (PRICE_STOCK_ACCESS_PER == 1) {
                            setTextColorForMenuItem(navigationView.getMenu().getItem(i).getSubMenu().getItem(j), R.color.color_black);
                        }
                        break;
                    case Constants.CONFIGURATION:
                        if (CONFIGURATION_ACCESS_PER == 1) {
                            setTextColorForMenuItem(navigationView.getMenu().getItem(i).getSubMenu().getItem(j), R.color.color_black);
                        }
                        break;
                    case Constants.SUPPLIER_DETAILS:
                        if (SUPPLIER_DETAILS_ACCESS_PER == 1) {
                            setTextColorForMenuItem(navigationView.getMenu().getItem(i).getSubMenu().getItem(j), R.color.color_black);
                        }
                        break;
                    case Constants.SUPPLIER_ITEM_LINKAGE:
                        if (SUPPLIER_ITEM_LINKAGE_ACCESS_PER == 1) {
                            setTextColorForMenuItem(navigationView.getMenu().getItem(i).getSubMenu().getItem(j), R.color.color_black);
                        }
                        break;
                    case Constants.PURCHASE_ORDER:
                        if (PURCHASE_ORDER_ACCESS_PER == 1) {
                            setTextColorForMenuItem(navigationView.getMenu().getItem(i).getSubMenu().getItem(j), R.color.color_black);
                        }
                        break;
                    case Constants.GOODS_INWARD_NOTE:
                        if (GOODS_INWARD_NOTE_ACCESS_PER == 1) {
                            setTextColorForMenuItem(navigationView.getMenu().getItem(i).getSubMenu().getItem(j), R.color.color_black);
                        }
                        break;
                    case Constants.REPORTS:
                        if (REPORTS_ACCESS_PER == 1) {
                            setTextColorForMenuItem(navigationView.getMenu().getItem(i).getSubMenu().getItem(j), R.color.color_black);
                        }
                        break;
                    case Constants.DAYEND:
                        if (DAY_END_ACCESS_PER == 1) {
                            setTextColorForMenuItem(navigationView.getMenu().getItem(i).getSubMenu().getItem(j), R.color.color_black);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        if(BILLING_ACCESS_PER == 1) {
            displaySelectedScreen(R.id.nav_billing);
        }
    }

    @Override
    public void onHomePressed() {

    }

    private void mGetRoleAccessPermissionData(){
        if(userRoleId != null && !userRoleId.isEmpty()) {
            accessPermissionRoleBeanList.clear();
            accessPermissionRoleBeanList = HomeActivity.dbHandler.getAccessPermissionRole(Integer.parseInt(userRoleId));
            if(accessPermissionRoleBeanList.size() > 0) {
                for (int i = 0; i < accessPermissionRoleBeanList.size(); i++){
                    switch (accessPermissionRoleBeanList.get(i).getStrAccessName()){
                        case Constants.BILLING:
                            BILLING_ACCESS_PER = 1;
                            break;
                        case Constants.ITEM_MASTER:
                            ITEM_MASTER_ACCESS_PER = 1;
                            break;
                        case Constants.CUSTOMER_MASTER:
                            CUSTOMER_MASTER_ACCESS_PER = 1;
                            break;
                        case Constants.USER_MANAGEMENT:
                            USER_MANAGEMENT_ACCESS_PER = 1;
                            break;
                        case Constants.ADD_ROLE:
                            ADD_ROLE_ACCESS_PER = 1;
                            break;
                        case Constants.PRICE_STOCK:
                            PRICE_STOCK_ACCESS_PER = 1;
                            break;
                        case Constants.CONFIGURATION:
                            CONFIGURATION_ACCESS_PER = 1;
                            break;
                        case Constants.SUPPLIER_DETAILS:
                            SUPPLIER_DETAILS_ACCESS_PER = 1;
                            break;
                        case Constants.SUPPLIER_ITEM_LINKAGE:
                            SUPPLIER_ITEM_LINKAGE_ACCESS_PER = 1;
                            break;
                        case Constants.PURCHASE_ORDER:
                            PURCHASE_ORDER_ACCESS_PER = 1;
                            break;
                        case Constants.GOODS_INWARD_NOTE:
                            GOODS_INWARD_NOTE_ACCESS_PER = 1;
                            break;
                        case Constants.REPORTS:
                            REPORTS_ACCESS_PER = 1;
                            break;
                        case Constants.SETTINGS:
                            SETTINGS_ACCESS_PER = 1;
                            break;
                        case Constants.DAYEND:
                            DAY_END_ACCESS_PER = 1;
                            break;
                        default:
                            break;
                    }
                }
            } else {
                Toast.makeText(this,"Please contact admin.",Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            Toast.makeText(this,"Please contact admin.",Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setTextColorForMenuItem(MenuItem menuItem, @ColorRes int color) {
        SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
        spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, spanString.length(), 0);
        menuItem.setTitle(spanString);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                if(SETTINGS_ACCESS_PER == 1) {
                    getSupportActionBar().setTitle("Settings");
                    displaySelectedScreen(id);
                } else {
                    Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.action_logout:
                finish();
                System.exit(0);
                break;
            /*case R.id.add:
                getSupportActionBar().setTitle("Add");
                return true;
            case R.id.reset:
                getSupportActionBar().setTitle("Reset");
                return true;*/
            default:
                break;
        }
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }

    void getBillSettings()
    {
        Cursor cursor = dbHandler.getBillSettings();
        if(cursor!=null && cursor.moveToFirst())
        {
            DATE_AND_TIME = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_DateAndTime));
            BUSINESS_DATE = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_BusinessDate));
        }
        //DATE_AND_TIME = 1;
    }

    private void manualDayend()
    {
        Cursor crsrSettings = dbHandler.getBillSettings();
        if(crsrSettings!=null && crsrSettings.moveToNext())
            DATE_AND_TIME = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_DateAndTime));
        if(DATE_AND_TIME ==1)
        {
            msgBox.Show(getString(R.string.invalid_attempt), getString(R.string.auto_dayend_message));
            return;
        }
        try{

            AlertDialog.Builder DayEndDialog = new AlertDialog.Builder(myContext);
            final DatePicker dateNextDate = new DatePicker(myContext);

            Cursor BusinessDate = dbHandler.getBusinessDate();
            String date_str = BUSINESS_DATE;


            if (BusinessDate.moveToFirst()) {
                date_str = BusinessDate.getString(BusinessDate.getColumnIndex("BusinessDate"));
            }
            final String date_str1 = date_str;
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date d = (Date) formatter.parse(date_str);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int day = c.get(Calendar.DAY_OF_MONTH);
            int month = c.get(Calendar.MONTH);
            int year = c.get(Calendar.YEAR);
            dateNextDate.updateDate(year, month, day + 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                Calendar prevDate = Calendar.getInstance();
                prevDate.add(Calendar.MONTH,-1);
                int qday = prevDate.get(Calendar.DAY_OF_MONTH);
                prevDate.add(Calendar.DAY_OF_MONTH, -(qday));
                dateNextDate.setMinDate(prevDate.getTimeInMillis());
                Calendar nextDate = Calendar.getInstance();
                nextDate.add(Calendar.DAY_OF_MONTH,5);
                dateNextDate.setMaxDate(nextDate.getTimeInMillis());
            }


            DayEndDialog
                    .setIcon(R.mipmap.ic_company_logo)
                    .setTitle("Day End")
                    .setMessage("Current transaction date is " + BUSINESS_DATE + "\n" + "select next transaction date")
                    .setView(dateNextDate)
                    .setPositiveButton("Set", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            String strNextDate = "";

                            if (dateNextDate.getDayOfMonth() < 10) {
                                strNextDate = "0" + String.valueOf(dateNextDate.getDayOfMonth());
                            } else {
                                strNextDate = String.valueOf(dateNextDate.getDayOfMonth());
                            }
                            if (dateNextDate.getMonth() < 9) {
                                strNextDate += "-" + "0" + String.valueOf(dateNextDate.getMonth() + 1) + "-";
                            } else {
                                strNextDate += "-" + String.valueOf(dateNextDate.getMonth() + 1) + "-";
                            }
                            strNextDate += String.valueOf(dateNextDate.getYear());

                            try{
                                if(!date_str1.equals("") && !date_str1.equals(strNextDate)){
                                    Date dd = new SimpleDateFormat("dd-MM-yyyy").parse(strNextDate);
                                    Cursor cc = dbHandler.getBillDetailByInvoiceDate(""+dd.getTime());
                                    if(cc!= null && cc.getCount()>0)
                                    {
                                        msgBox.Show("Error","Since bill for date "+strNextDate+" is already present in database, therefore this date cannot be selected");
                                        return;
                                    }
                                }else if(date_str1.equals(strNextDate))
                                {
                                    return;
                                }
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }

                            long iResult = dbHandler.updateBusinessDate(String.valueOf(strNextDate));
                            Logger.d("ManualDayEnd", "Bussiness Date updation status :" + iResult);
                            //UpdateStock();
                            /*
                            StockOutwardMaintain stock_outward = new StockOutwardMaintain(myContext, getDb());
                            stock_outward.saveOpeningStock_Outward(strNextDate);

                            StockInwardMaintain stock_inward = new StockInwardMaintain(myContext, getDb());
                            stock_inward.saveOpeningStock_Inward(strNextDate);*/
                            //setBillNo();
                            BillNoReset bs = new BillNoReset();
                            bs.setBillNo(dbHandler);

                            List<Fragment> list = getSupportFragmentManager().getFragments();
                            for(Fragment frag : list)
                            {
                                if(frag instanceof BillingFragment)
                                {
                                    ((BillingFragment) frag).initSettingsData();
                                    ((BillingFragment) frag).mBillNumberSet();
                                    break;
                                }
                            }
                            if (iResult > 0) {
                                msgBox.Show("Information", "Transaction Date changed to " + strNextDate);
                            } else {
                                msgBox.Show("Warning", "Error: DayEnd is not done");
                            }
                        }

                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            msgBox.Show("Warning", "DayEnd operation has been cancelled, Transaction date remains same");
                        }
                    })
                    .show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Logger.e(TAG,e+"");
        }

    }
    private void checkForAutoDayEnd()
    {
        if(DATE_AND_TIME== 1) //DateChange -> 1 - auto, 0 = manual
        {
            Date d = new Date();
            String currentdate  = String.valueOf(android.text.format.DateFormat.format("dd-MM-yyyy", d.getTime()));

            if(!BUSINESS_DATE.equals(currentdate))
            {
                // needs to change to current date
                int iResult = dbHandler.updateBusinessDate(currentdate);
                Logger.d("AutoDayEnd", "BusinessDate set to "+currentdate+". Status of updation : " + iResult);
                /*StockOutwardMaintain stock_outward = new StockOutwardMaintain(myContext, getDb());
                stock_outward.saveOpeningStock_Outward(currentdate);

                StockInwardMaintain stock_inward = new StockInwardMaintain(myContext, getDb());
                stock_inward.saveOpeningStock_Inward(currentdate);*/
                // delete all pending KOTS

                BillNoReset bs = new BillNoReset();
                bs.setBillNo(dbHandler);
            }

        }
        else {
            Logger.d("HomeActivity", getString(R.string.manual_dayend_message));
        }
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;
        Bundle bundle;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_billing:
                if (BILLING_ACCESS_PER == 1) {
                    fragment = new BillingFragment();
                    bundle = new Bundle();
                    bundle.putString("UserName", userName);
                    bundle.putString("UserId", userId);
                    bundle.putString("UserRoleId", userRoleId);
                    bundle.putString("TAG", "BillingScreen");
                    fragment.setArguments(bundle);
                    getSupportActionBar().setTitle(getString(R.string.billing));
                } else {
                    Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_item_master:
                if(ITEM_MASTER_ACCESS_PER == 1) {
                    fragment = new ItemMastersFragment();
                    getSupportActionBar().setTitle(getString(R.string.item_master));
                } else {
                    Toast.makeText(this,"Access Denied.",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_customer_master:
                if (CUSTOMER_MASTER_ACCESS_PER == 1) {
                    fragment = new CustomerListFragment();
                    getSupportActionBar().setTitle(getString(R.string.customer_master));
                } else {
                    Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();
                }
                break;
           /* case R.id.nav_master:
                fragment = new MastersFragment();
                getSupportActionBar().setTitle(getString(R.string.user_management));
                break;*/
            case R.id.nav_config:
                if(CONFIGURATION_ACCESS_PER == 1) {
                    fragment = new ConfigurationFragment();
                    getSupportActionBar().setTitle(getString(R.string.configuration));
                } else {
                    Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_user_management:
                if(USER_MANAGEMENT_ACCESS_PER == 1) {
                    fragment = new UserManagementFragment();
                    getSupportActionBar().setTitle(getString(R.string.user_management));
                }  else {
                    Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_add_role:
                if(ADD_ROLE_ACCESS_PER == 1) {
                    fragment = new AddRoleFragment();
                    bundle = new Bundle();
                    bundle.putString(USERNAME, userName);
                    bundle.putString(USERID, userId);
                    bundle.putString(USERROLEID, userRoleId);
                    fragment.setArguments(bundle);
                    getSupportActionBar().setTitle(getString(R.string.add_role));
                } else {
                    Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_reports:
                if (REPORTS_ACCESS_PER == 1) {
                    fragment = new ReportFragment();
                    getSupportActionBar().setTitle(getString(R.string.reports));

                } else {
                    Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.nav_price_stock:
                if(PRICE_STOCK_ACCESS_PER == 1) {
                    fragment = new PriceStockFragment();
                    getSupportActionBar().setTitle(getString(R.string.price_stock));
                } else {
                    Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_supplier_details:
                if(SUPPLIER_DETAILS_ACCESS_PER == 1) {
                    //fragment = new SupplierDetailsFragment();
                    //getSupportActionBar().setTitle(getString(R.string.supplier_details));
                } else {
                    Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_supplier_item_linkage:
                if (SUPPLIER_ITEM_LINKAGE_ACCESS_PER == 1) {
                    //fragment = new SupplierItemLinkageFragment();
                    //getSupportActionBar().setTitle(getString(R.string.supplier_details));
                } else {
                    Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_purchase_order:
                if (PURCHASE_ORDER_ACCESS_PER == 1) {
                    // fragment = new PurchaseOrderFragment();
                    //getSupportActionBar().setTitle(getString(R.string.purchase_order));
                } else {
                    Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_goods_inward_note:
                if (GOODS_INWARD_NOTE_ACCESS_PER == 1) {
                    //fragment = new GoodsInwardNoteFragment();
                    //getSupportActionBar().setTitle(getString(R.string.purchase_order));
                } else {
                    Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.action_settings:
                if (SETTINGS_ACCESS_PER == 1) {
                    boolean fragAlreadyInflated = false;
                    List<Fragment>list = getSupportFragmentManager().getFragments();
                    for(Fragment frag : list )
                    {
                        if(frag instanceof AppSettingsFragment){
                            fragAlreadyInflated = true;
                            break;
                        }
                    }
                    if(!fragAlreadyInflated)
                    {
                        fragment = new AppSettingsFragment();
                        getSupportActionBar().setTitle(getString(R.string.settings));
                    }
                } else {
                    Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.nav_dayend:
                if(DAY_END_ACCESS_PER == 1) {
                    manualDayend();
                } else {
                    Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            currentFragment = fragment;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private static long back_pressed;
    @Override
    public void onBackPressed(){

        if (back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
            if(dbHandler != null) {
                dbHandler.close();
            }
            finish();
            System.exit(0);
        }
        else{
            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }

    private Menu defaultMenu;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        defaultMenu = menu;
       /* setHomeDeliveryCount(this, "1");
        setPromotionCount(this,"10");*/
        return true;
    }

    public boolean dispatchTouchEvent(MotionEvent event) {

        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText || view instanceof TextInputEditText) {
            View w = getCurrentFocus();
            int location[] = new int[2];
            w.getLocationOnScreen(location);
            float x = event.getRawX() + w.getLeft() - location[0];
            float y = event.getRawY() + w.getTop() - location[1];
            if (event.getAction() == MotionEvent.ACTION_DOWN
                    && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }

   /* public void setHomeDeliveryCount(Context context, String count) {
        MenuItem menuItem = defaultMenu.findItem(R.id.action_home_delivery);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        ActionBarCountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_layer_list_home_delivery);
        if (reuse != null && reuse instanceof ActionBarCountDrawable) {
            badge = (ActionBarCountDrawable) reuse;
        } else {
            badge = new ActionBarCountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_layer_list_home_delivery, badge);
    }

    public void setPromotionCount(Context context, String count) {
        MenuItem menuItem = defaultMenu.findItem(R.id.action_promo);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        ActionBarCountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_layer_list_promo);
        if (reuse != null && reuse instanceof ActionBarCountDrawable) {
            badge = (ActionBarCountDrawable) reuse;
        } else {
            badge = new ActionBarCountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_layer_list_promo, badge);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            disconnect();
        }catch (Exception ex){
            Logger.e(TAG, ex.getMessage());
        }
        if(dbHandler != null) {
            dbHandler.close();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(currentFragment != null && currentFragment instanceof MyKeyEventListener) {
            ((MyKeyEventListener) currentFragment).onKeyUp(keyCode, event);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onPaymentSuccess(String s) {
        // razorpay callback func
        PayBillFragment frag = (PayBillFragment)getSupportFragmentManager().findFragmentByTag("Proceed To Pay");
        OnWalletPaymentResponseListener walletListner =  frag;
        walletListner.onWalletPaymentSuccessListener(s);

    }

    @Override
    public void onPaymentError(int i, String s) {
        // razorpay callback func
        PayBillFragment frag = (PayBillFragment)getSupportFragmentManager().findFragmentByTag("Proceed To Pay");
        OnWalletPaymentResponseListener walletListner =  frag;
        walletListner.onWalletPaymentErrorListener(i,s);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Logger.i(TAG, "Request code : " + requestCode);
       /* if(requestCode == 65548 || requestCode == 262156)
        {*/
            if (data != null) {
                final String amount = data.getStringExtra("amount");
                PayBillFragment frag = (PayBillFragment) getSupportFragmentManager().findFragmentByTag("Proceed To Pay");
                if (frag != null) {
                    OnMSwipeResultResponseListener onMSwipeResultResponseListener = frag;
                    onMSwipeResultResponseListener.onMSwipeResultResponseSuccess(amount);
                    //Toast.makeText(myContext, "Hello" + amount, Toast.LENGTH_SHORT).show();
                }
            }
       /* }*/
        } catch (Exception ex) {
            Logger.i(TAG, "Unable to pass mSwipe payment value to PayBillFragment screen on error : onActivityResult()." + ex.getMessage());
        }
    }

    @Override
    public void onLoginCompleted(boolean success) {
        try {
            if (success) {
                PayBillFragment frag = (PayBillFragment) getSupportFragmentManager().findFragmentByTag("Proceed To Pay");
                if (frag != null) {
                    Intent intent = new Intent(myContext, MSwipePaymentActivity.class);
                    intent.putExtra("amount", frag.tvDifferenceAmount.getText().toString());
                    intent.putExtra("phone", frag.phoneReceived);
                    startActivityForResult(intent, REQUEST_CODE_CARD_PAYMENT);
                }
            } else {
                Toast.makeText(myContext, "Login failed. Please try again later.", Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Logger.i(TAG,"Unable to navigate to mSwipe payment screen on error : onLoginCompleted()." +ex.getMessage());
        }
    }

    @Override
    public void onChangePassword(boolean success) {
        try{
            PayBillFragment frag = (PayBillFragment)getSupportFragmentManager().findFragmentByTag("Proceed To Pay");
            Intent intent = new Intent(myContext, PasswordChangeActivity.class);
            intent.putExtra("amount", frag.txt);
            startActivity(intent);
        }catch (Exception ex){
            Logger.i(TAG,"Unable to navigate to PasswordChangeActivity screen on error : onChangePassword()." +ex.getMessage());
        }
    }
}
