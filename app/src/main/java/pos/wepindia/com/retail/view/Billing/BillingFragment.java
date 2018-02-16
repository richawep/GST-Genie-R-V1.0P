package pos.wepindia.com.retail.view.Billing;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.Constants;
import pos.wepindia.com.retail.GenericClass.DateTime;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.EMOJI_FILTER;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.MyKeyEventListener;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.OnPriceQuantityChangeDataApplyListener;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.OnPrinterConnectSucess;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.OnProceedToPayCompleteListener;
import pos.wepindia.com.wepbase.model.pojos.AddedItemsToOrderTableClass;
import pos.wepindia.com.retail.utils.DecimalDigitsInputFilter;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.BillingCategoryListAdapter;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.BillingDeptListAdapter;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.BillingDisplayListAdapter;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.BillingListAdapter;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.OnBillHoldResumeListener;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.OnBillingCategoryListAdapterListener;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.OnBillingDeptListAdapter;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.OnBillingDisplayAdapterList;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.OnBillingSelectedProductListsListeners;
import pos.wepindia.com.retail.view.CustomerMasters.CustomerAdaptersAndListeners.OnCustomerAddListener;
import pos.wepindia.com.retail.view.CustomerMasters.CustomerMasterFragment;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.BillDetailBean;
import pos.wepindia.com.wepbase.model.pojos.BillItemBean;
import pos.wepindia.com.wepbase.model.pojos.Category;
import pos.wepindia.com.wepbase.model.pojos.Customer;
import pos.wepindia.com.wepbase.model.pojos.Department;
import pos.wepindia.com.wepbase.model.pojos.HoldResumeBillBean;
import pos.wepindia.com.wepbase.model.pojos.ItemMasterBean;
import pos.wepindia.com.wepbase.model.pojos.PaymentDetails;
import pos.wepindia.com.wepbase.model.pojos.User;
import pos.wepindia.com.wepbase.model.print.BillKotItem;
import pos.wepindia.com.wepbase.model.print.BillServiceTaxItem;
import pos.wepindia.com.wepbase.model.print.BillTaxItem;
import pos.wepindia.com.wepbase.model.print.BillTaxSlab;
import pos.wepindia.com.wepbase.model.print.PrintKotBillItem;
import pos.wepindia.com.wepbase.utils.Preferences;

/**
 * Created by MohanN on 12/8/2017.
 */

public class BillingFragment extends Fragment implements Constants ,OnCustomerAddListener,OnBillingDisplayAdapterList,
        OnBillingDeptListAdapter,OnBillingCategoryListAdapterListener,OnBillingSelectedProductListsListeners,
        OnBillHoldResumeListener , OnProceedToPayCompleteListener, OnPriceQuantityChangeDataApplyListener,
        OnPrinterConnectSucess, MyKeyEventListener {

    private final String TAG = BillingFragment.class.getSimpleName();
    HomeActivity homeActivity;

    String tx = "";
    String linefeed = "";
    @BindView(R.id.tv_billing_discount_percent)                 TextView tv_billing_discount_percent;
    @BindView(R.id.tv_billing_CustId)                           TextView tv_billing_CustId;
    @BindView(R.id.bt_billing_customer_add)                     Button btCustomerAdd;
    @BindView(R.id.bt_billing_hold_resume_bill)                 Button btHoldResumeBill;
    @BindView(R.id.bt_billing_delete)                           Button btDeleteBill;
    @BindView(R.id.bt_billing_reprint)                          Button btnReprintBill;
    @BindView(R.id.bt_billing_proceed_to_pay)                   Button btProceedToPay;
    @BindView(R.id.bt_billing_save_bill)                        Button btnSaveBill;
    @BindView(R.id.bt_billing_print_bill)                       Button btnSavePrintBill;
    //@BindView(R.id.et_billing_screen_hidden_barcode)            EditText etBarcodeHide;
    @BindView(R.id.edt_billing_InvoiceDate)                     EditText edtInvoiceDate;
    // @BindView(R.id.edt_billing_SalesManId)                      EditText edtSalesManId;
    @BindView(R.id.et_billing_roundoff)                         EditText edtRoundOff;
    @BindView(R.id.rv_billing_selected_item_list)               RecyclerView rvBillingProductList;
    @BindView(R.id.rv_billing_fav_list)                         RecyclerView rvFavList;
    @BindView(R.id.autocomplete_billing_customer_search)        AutoCompleteTextView avCustomerSearch;
    @BindView(R.id.autocomplete_billing_items_search)           AutoCompleteTextView avItemSearch;
    @BindView(R.id.av_billing_SalesManId)                       AutoCompleteTextView avSalesManId;
    @BindView(R.id.et_billing_customer_mobile)                  TextInputEditText edtCustomerMobile;
    @BindView(R.id.et_billing_customer_gstin)                   TextInputEditText edtCustomerGSTIN;
    @BindView(R.id.cb_billing_short_code_status)                CheckBox cbShortCodeStatus;
    @BindView(R.id.cb_billing_inter_state)                      CheckBox cbInterStateStatus;
    @BindView(R.id.sp_billing_inter_state)                      Spinner spInterState;
    @BindView(R.id.rg_billing_sp_mrp_wp)                        RadioGroup rgPriceOptions;
    @BindView(R.id.rb_billing_mrp)                              RadioButton rbMRP;
    @BindView(R.id.rb_billing_rp)                               RadioButton rbRetailPrice;
    @BindView(R.id.rb_billing_wp)                               RadioButton rbWholeSalePrice;
    @BindView(R.id.bt_billing_fav)                              Button btnDisplayFav;
    @BindView(R.id.bt_billing_department)                       Button btnDisplayDepartment;
    @BindView(R.id.bt_billing_category)                         Button btnDisplayCategory;
    @BindView(R.id.bt_billing_items)                            Button btnDisplayItems;
    @BindView(R.id.et_billing_total_igst)                       TextInputEditText edtTotalIGST;
    @BindView(R.id.et_billing_total_cgst)                       TextInputEditText edtTotalCGST;
    @BindView(R.id.et_billing_total_sgst)                       TextInputEditText edtTotalSGST;
    @BindView(R.id.et_billing_total_cess)                       TextInputEditText edtTotalCess;
    @BindView(R.id.et_billing_total_discount)                   TextInputEditText edtTotalDiscount;
    @BindView(R.id.et_billing_other_charges)                    TextInputEditText edtOtherCharges;
    @BindView(R.id.et_billing_total_taxable_value)              TextInputEditText edtTotalTaxableValue;
    @BindView(R.id.et_billing_total_bill_amount)                TextInputEditText edtTotalAmount;
    @BindView(R.id.et_billing_bill_number)                      TextInputEditText edtBillNumber;
    @BindView(R.id.et_billing_items_selected_counter)           TextInputEditText edtItemsSelectedCounter;
    @BindView(R.id.iv_billing_sil_title_remove_all)             ImageView ivRemoveBillingList;
    @BindView(R.id.til_sgstAmount)                               TextInputLayout til_sgstAmount;



    MessageDialog msgBox;
    Context myContext;

    Map<String,String> deliveryChargesMap;
    String[] arrayPOS;
    List<BillItemBean> billingItemsList;

    BillingListAdapter billingListAdapter;
    int reprintBillingMode = 0;

    SimpleCursorAdapter mAdapterCustomer, mAdapterItems, mAdapterSalesManId;
    Customer customerBean;
    User userSalesManIdBean;

    ItemMasterBean itemMasterBean;
    List<ItemMasterBean> itemsDisplayLists;

    private String customerId = "0";
    String custPhone = "";
    private String userId, userName;
    float fTotalsubTaxPercent = 0;
    double fRoundOfValue = 0;
    double dblDiscountAmount=0;
    double dFinalBillValue =0;



    PopulateDisplayItemsDataAsync populateDisplayItemsDataAsyncObject = null;
    PopulateDisplayDeptDataAsync populateDisplayDeptDataAsyncObject = null;
    PopulateDisplayCategoryDataAsync populateDisplayCategoryDataAsyncObject = null;

    View rootView;

    int DATE_TIME = 0, PRICE_CHANGE = 0, BILL_WITH_STOCK = 0, PRINT_OWNER_DETAIL = 0,
            BILL_AMOUNT_ROUND_OFF = 0, SALES_MAN_ID = 0, isForwardTaxEnabled = 1, DISCOUNT_TYPE = 0,
            MRP_RETAIL_PRICE_DIFF_PRINT = 0, FAST_BILLING_MODE = 0, isHSNEnabled =1, isDAILY_BILL_NO_RESET =0,
            REWARD_POINTS = 0, BOLD_HEADER = 0, PRINTSERVICE = 0,
            PRINT_MRP_RETAIL_DIFFERENCE =0, PRINT_DISCOUNT =0;

    double RewardPtToAmt =0,  amountToRewardPoints = 0,RewardPoints = 0;
    String OWNERPOS = "";
    String BUSINESS_DATE = "";
    private int UTGSTENABLED = 0, HSNPRINTENABLED = 0;

    double dblCashPayment = 0, dblCardPayment = 0, dblCouponPayment = 0, dblPettyCashPayment = 0, dblPaidTotalPayment = 0, dblWalletPayment = 0,
            dblChangePayment = 0, dblRoundOfValue = 0, dblOtherCharges = 0, dblRewardPointsAmount =0, dblAEPSAmount=0, dblMSwipeAmount = 0;

    private FragmentManager fm;
    BillingDisplayListAdapter billingDisplayListAdapter = null;
    BillingDeptListAdapter billingDeptListAdapter = null;

    private long lastClickTime = 0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        rootView = inflater.inflate(R.layout.billing_fragment, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mPopulateInterStateSpinnerData();
        initAutoCompleteTextDataForCustomer();
        initAutoCompleteTextDataForSalesManId();
        mAutoCompleteTextViewCustomerTextChange();
        initAutoCompleteTextDataForItems();
        //initDataToWidgets();
        populateDisplayItemsDataAsyncObject = new PopulateDisplayItemsDataAsync();
        populateDisplayItemsDataAsyncObject.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Constants.FAVOURITE);
        mBillNumberSet();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {

            if (getArguments() != null) {
                userId = getArguments().getString("UserId");
                userName = getArguments().getString("UserName");
            }

            myContext = getActivity();
            homeActivity = (HomeActivity) getActivity();
            itemsDisplayLists = new ArrayList<ItemMasterBean>();
            billingItemsList = new ArrayList<BillItemBean>();
            msgBox = new MessageDialog(myContext);
            ButterKnife.bind(this, view);
            //App crash error log
            if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }
            spInterState.setEnabled(false);
            initSettingsData();
            mGetRewardDetails();
            clickEvent();
            applyDecimalValidation();
            applyValidations();
            mGetOtherChargesValue();
            mGetOwnerPos();
            if(!homeActivity.isPrinterAvailable) {
                btnReprintBill.setEnabled(false);
                btnReprintBill.setBackgroundResource(R.color.disable_button);
                btnSavePrintBill.setEnabled(false);
                btnSavePrintBill.setBackgroundResource(R.color.disable_button);
            }
            if(OWNERPOS.equals("") || OWNERPOS.equals("0"))
            {
                msgBox.Show(getString(R.string.invalid_attempt),getString(R.string.empty_owner_pos_message) );

            }
        } catch (Exception ex) {
            Log.i(TAG, "Unable init billing fragment. " + ex.getMessage());
        }
    }


    @OnClick({R.id.bt_billing_customer_add, R.id.bt_billing_hold_resume_bill, R.id.bt_billing_proceed_to_pay,
            R.id.bt_billing_clear, R.id.rb_billing_mrp, R.id.rb_billing_rp, R.id.rb_billing_wp, R.id.bt_billing_fav,
            R.id.bt_billing_department,R.id.bt_billing_category,R.id.bt_billing_items, R.id.bt_billing_save_bill,
            R.id.bt_billing_reprint, R.id.bt_billing_print_bill,R.id.iv_billing_sil_title_remove_all,R.id.bt_billing_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_billing_customer_add:
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                CustomerMasterFragment customerMasterFragment = new CustomerMasterFragment();
                customerMasterFragment.mInitListener(this);
                customerMasterFragment.show(fm, "Add New Customer");
                break;
            case R.id.bt_billing_hold_resume_bill:
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                if (billingItemsList.size() > 0) {
                    if (edtTotalAmount.getText().toString().equals("") ) {
                        msgBox.Show("Warning", "Please add item to make bill");
                        return;
                    }
                    if ( edtTotalAmount.getText().toString().equals("0.00")) {
                        msgBox.Show("Warning", "Please make bill of amount greater than 0.00");
                        return;
                    }
                    if (cbInterStateStatus.isChecked() && spInterState.getSelectedItem().equals("Select")) {
                        msgBox.Show("Warning", "Please Select state for Interstate Supply");
                        return;
                    }
                    mHoldBillItems();
                } else {
                    fm = getActivity().getSupportFragmentManager();
                    ResumeBillsListFragment resumeBillsListFragment = new ResumeBillsListFragment();
                    resumeBillsListFragment.setListeners(this);
                    resumeBillsListFragment.show(fm, "Resume Bill List");
                }
                break;
            case R.id.bt_billing_proceed_to_pay:
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                mProceedToPay();
                break;
            case R.id.bt_billing_clear:
                confirmAndClear();
                break;
            case R.id.bt_billing_delete:
                deleteBill();
                break;
//            case R.id.rb_billing_mrp:
//                //Toast.makeText(myContext,"MRP",Toast.LENGTH_LONG).show();
//                mPriceOptionChange(Constants.MRP);
//                break;
//            case R.id.rb_billing_rp:
//                //Toast.makeText(myContext,"SP",Toast.LENGTH_LONG).show();
//                mPriceOptionChange(Constants.RETAIL_PRICE);
//                break;
//            case R.id.rb_billing_wp:
//                mPriceOptionChange(Constants.WHOLE_SALE_PRICE);
//                break;
            case R.id.rb_billing_mrp:
            case R.id.rb_billing_rp:
            case R.id.rb_billing_wp:
                calculateAndDisplayBillingData(-1, false);
                break;
            case R.id.bt_billing_fav:
                if(populateDisplayItemsDataAsyncObject == null){
                    populateDisplayItemsDataAsyncObject = new PopulateDisplayItemsDataAsync();
                    populateDisplayItemsDataAsyncObject.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Constants.FAVOURITE);
                }
                break;
            case R.id.bt_billing_department:
                if(populateDisplayDeptDataAsyncObject == null){
                    populateDisplayDeptDataAsyncObject = new PopulateDisplayDeptDataAsync();
                    populateDisplayDeptDataAsyncObject.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;
            case R.id.bt_billing_category:
                if(populateDisplayCategoryDataAsyncObject == null){
                    populateDisplayCategoryDataAsyncObject = new PopulateDisplayCategoryDataAsync();
                    populateDisplayCategoryDataAsyncObject.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                break;
            case R.id.bt_billing_items:
                if(populateDisplayItemsDataAsyncObject == null){
                    populateDisplayItemsDataAsyncObject = new PopulateDisplayItemsDataAsync();
                    populateDisplayItemsDataAsyncObject.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Constants.ALL_ITEMS);
                }
                break;
            case R.id.iv_billing_sil_title_remove_all:
                mResetBillingListSelectedItems();
                break;
            case R.id.bt_billing_save_bill:
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                mSaveBILL(false,false);
                break;
            case R.id.bt_billing_print_bill:
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                mSaveBILL(true,false);
                break;
            case R.id.bt_billing_reprint:
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                    return;
                }
                lastClickTime = SystemClock.elapsedRealtime();
                mReprintBill();
                break;
            default:
                break;
        }
    }

    void clickEvent()
    {
        avItemSearch.addTextChangedListener(textWatcher);
        avSalesManId.addTextChangedListener(textWatcher);
        avCustomerSearch.addTextChangedListener(textWatcher);
        cbShortCodeStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (!isChecked) {
                    avItemSearch.setHint(getString(R.string.search_items_name_barcode));
                } else {
                    avItemSearch.setHint(getString(R.string.search_items_name_barcode_short_code));
                }
            }
        });


        cbInterStateStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    spInterState.setSelection(0);
                    spInterState.setEnabled(false);
                } else {
                    spInterState.setSelection(0);
                    spInterState.setEnabled(true);
                }

                if (isChecked) // interstate
                {
                    edtTotalIGST.setTextColor(getResources().getColor(R.color.color_black));
                    edtTotalCGST.setTextColor(getResources().getColor(R.color.app_color_background));
                    edtTotalSGST.setTextColor(getResources().getColor(R.color.app_color_background));
                } else {
                    edtTotalIGST.setTextColor(getResources().getColor(R.color.app_color_background));
                    edtTotalCGST.setTextColor(getResources().getColor(R.color.color_black));
                    edtTotalSGST.setTextColor(getResources().getColor(R.color.color_black));
                }
            }
        });
        spInterState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String stateChoosen = ((Spinner)adapterView).getItemAtPosition(position).toString();
                if(!stateChoosen.equalsIgnoreCase("Select") && !stateChoosen.equalsIgnoreCase(""))
                {
                    int length = stateChoosen.length();
                    String posCodeChoosen = stateChoosen.substring(length-2,length);
                    if(OWNERPOS.equalsIgnoreCase(posCodeChoosen))
                    {
                        spInterState.setSelection(0);
                        msgBox.Show(getString(R.string.note), getString(R.string.customer_statecode_same_as_owner_message));
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void deleteBill() {
        AlertDialog.Builder DineInTenderDialog = new AlertDialog.Builder(myContext);
        LayoutInflater UserAuthorization = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vwAuthorization = UserAuthorization.inflate(R.layout.reprint_dialog, null);
        final ImageButton btnCal_reprint = (ImageButton) vwAuthorization.findViewById(R.id.btnCal_reprint);

        final EditText txtDeleteBillNo = (EditText) vwAuthorization.findViewById(R.id.txtDineInReprintBillNumber);
        final TextView tv_inv_date = (TextView) vwAuthorization.findViewById(R.id.tv_inv_date);
        tv_inv_date.setText(BUSINESS_DATE);
        btnCal_reprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelection(tv_inv_date);
            }
        });

        DineInTenderDialog.setIcon(R.mipmap.ic_company_logo)
                .setTitle("Delete Bill")
                /*.setMessage("Enter Bill Number")*/
                .setView(vwAuthorization)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        if (txtDeleteBillNo.getText().toString().equalsIgnoreCase("")) {
                            msgBox.Show("Warning", "Please enter Bill Number");
                            return;
                        } else if (tv_inv_date.getText().toString().equalsIgnoreCase("")) {
                            msgBox.Show("Warning", "Please enter Bill Date");
//                            setInvoiceDate();
                            return;
                        } else {
                            try {
                                int InvoiceNo = Integer.valueOf(txtDeleteBillNo.getText().toString());
                                String date_reprint = tv_inv_date.getText().toString();
//                                tvDate.setText(date_reprint);
                                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(date_reprint);
                                Cursor result = HomeActivity.dbHandler.getBillDetail(InvoiceNo, String.valueOf(date.getTime()));
                                if (result.moveToFirst()) {
                                    if (result.getInt(result.getColumnIndex("BillStatus")) != 0) {
                                        VoidBill(InvoiceNo, String.valueOf(date.getTime()));
                                    } else {
                                        //Toast.makeText(BillingCounterSalesActivity.this, "Bill is already voided", Toast.LENGTH_SHORT).show();

                                        String msg = "Bill Number " + InvoiceNo + " is already voided";
                                        msgBox.Show("Note", msg);
                                        Log.d("VoidBill", msg);
                                    }
                                } else {
                                    //Toast.makeText(BillingCounterSalesActivity.this, "No bill found with bill number " + InvoiceNo, Toast.LENGTH_SHORT).show();
                                    String msg = "No bill found with bill number " + InvoiceNo;
                                    msgBox.Show("Note", msg);
                                    Log.d("VoidBill", msg);
                                }
                                mClear();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).show();
    }

    /*************************************************************************************************************************************
     * Void Bill Button Click event, opens a dialog to enter admin user id and
     * password for voiding bill if user is admin then bill will be voided
     *
     *************************************************************************************************************************************/
    public void VoidBill(final int invoiceno, final String Invoicedate) {

        AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);

        LayoutInflater UserAuthorization = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);

        final EditText txtUserId = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
        final EditText txtPassword = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);

        AuthorizationDialog.setTitle("Authorization")
                .setIcon(R.mipmap.ic_company_logo)
                .setView(vwAuthorization)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Cursor User = HomeActivity.dbHandler.getUser(txtUserId.getText().toString(),txtPassword.getText().toString());
                        if (User.moveToFirst()) {
                            if (User.getInt(User.getColumnIndex("RoleId")) == 1) {
                                //ReprintVoid(Byte.parseByte("2"));
                                int result = HomeActivity.dbHandler.makeBillVoids(invoiceno, Invoicedate);
                                if (result > 0) {
                                    Date dd = new Date(Long.parseLong(Invoicedate));
                                    String dd_str = new SimpleDateFormat("dd-MM-yyyy").format(dd);
                                    String msg = "Bill Number " + invoiceno + " , Dated : " + dd_str + " voided successfully";
                                    // MsgBox.Show("Warning", msg);
                                    Toast.makeText(myContext, msg, Toast.LENGTH_SHORT).show();
                                    Log.d("VoidBill", msg);
                                }
                            } else {
                                msgBox.Show("Warning", "Void Bill failed due to in sufficient access privilage");
                            }
                        } else {
                            msgBox.Show("Warning", "Void Bill failed due to wrong user id or password");
                        }
                    }
                }).show();
    }

    void mReprintBill(){

        AlertDialog.Builder DineInTenderDialog = new AlertDialog.Builder(myContext);

        LayoutInflater UserAuthorization = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.reprint_dialog, null);

        final ImageButton btnCal_reprint = (ImageButton) vwAuthorization.findViewById(R.id.btnCal_reprint);

        final EditText txtReprintBillNo = (EditText) vwAuthorization.findViewById(R.id.txtDineInReprintBillNumber);
        final TextView tv_inv_date = (TextView) vwAuthorization.findViewById(R.id.tv_inv_date);
        tv_inv_date.setText(BUSINESS_DATE);
        btnCal_reprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateSelection(tv_inv_date);
            }
        });

        DineInTenderDialog.setIcon(R.mipmap.ic_company_logo).setTitle("RePrint Bill")
                .setView(vwAuthorization).setNegativeButton("Cancel", null)
                .setPositiveButton("RePrint", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                        if (txtReprintBillNo.getText().toString().equalsIgnoreCase("")) {
                            msgBox.Show("Warning", "Please enter Bill Number");
//                            setInvoiceDate();
                            return;
                        } else if (tv_inv_date.getText().toString().equalsIgnoreCase("")) {
                            msgBox.Show("Warning", "Please enter Bill Date");
//                            setInvoiceDate();
                            return;
                        } else {
                            try {
                                int billStatus = 0;
                                int billNo = Integer.valueOf(txtReprintBillNo.getText().toString());
                                String date_reprint = tv_inv_date.getText().toString();
//                                tvDate.setText(date_reprint);
                                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(date_reprint);
                                Cursor LoadItemForReprint = HomeActivity.dbHandler.getItemsFromBillItem_new(billNo, date.getTime()+"");
                                if (LoadItemForReprint.moveToFirst()) {
                                    Cursor cursor = HomeActivity.dbHandler.getBillDetail(billNo, date.getTime()+"");
                                    if (cursor != null && cursor.moveToFirst()) {
                                        billStatus = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_BillStatus));
                                        if (billStatus == 0) {
                                            msgBox.Show("Warning", "This bill has been deleted");
//                                            setInvoiceDate();
                                            return;
                                        }
                                        String pos = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_POS));
                                        String custStateCode = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_CustStateCode));
                                        if (pos != null && !pos.equals("") && custStateCode != null && !custStateCode.equals("") && !custStateCode.equalsIgnoreCase(pos)) {
                                            cbInterStateStatus.setChecked(true);
                                            int index = getIndex_pos(custStateCode);
                                            spInterState.setSelection(index);
                                            //System.out.println("reprint : InterState");
                                        } else {
                                            cbInterStateStatus.setChecked(false);
                                            spInterState.setSelection(0);
                                            //System.out.println("reprint : IntraState");
                                        }
                                        double dTotalDiscount = cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_TotalDiscountAmount));
                                        float discper = cursor.getFloat(cursor.getColumnIndex(DatabaseHandler.KEY_DiscPercentage));
                                        reprintBillingMode = 1;

//                                        tvDiscountPercentage.setText(String.format("%.2f", discper));
                                        edtTotalDiscount.setText(String.format("%.2f", dTotalDiscount));
                                        tv_billing_discount_percent.setText(String.format("%.2f",discper));
                                        edtBillNumber.setText(txtReprintBillNo.getText().toString());

                                        edtTotalIGST.setText(String.format("%.2f", cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_IGSTAmount))));
                                        edtTotalCGST.setText(String.format("%.2f", cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_CGSTAmount))));
                                        edtTotalSGST.setText(String.format("%.2f", cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_SGSTAmount))));
                                        edtTotalTaxableValue.setText(String.format("%.2f", cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_TaxableValue))));
                                        edtTotalAmount.setText(String.format("%.2f", cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_BillAmount))));
                                        edtRoundOff.setText(String.format("%.2f",cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RoundOff))));

                                        LoadItemsForReprintBill(LoadItemForReprint);
                                        customerId = (cursor.getString(cursor.getColumnIndex("CustId")));
                                        if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SALES_MAN_ID)) !=null)
                                            avSalesManId.setText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SALES_MAN_ID)));
                                        else
                                            avSalesManId.setText("");
                                        /*Cursor crsrBillDetail = HomeActivity.dbHandler.getBillDetail(Integer.valueOf(txtReprintBillNo.getText().toString()));
                                        if (crsrBillDetail.moveToFirst()) {
                                            customerId = (crsrBillDetail.getString(crsrBillDetail.getColumnIndex("CustId")));
                                        }*/
                                    }

                                } else {
                                    msgBox.Show("Warning", "No Item is present for the Bill Number " + txtReprintBillNo.getText().toString() + ", Dated :" + tv_inv_date.getText().toString());
//                                    setInvoiceDate();
                                    return;
                                }
                                if (reprintBillingMode == 4 && billStatus == 2) {
                                    strPaymentStatus = "Cash On Delivery";
                                } else
                                    strPaymentStatus = "Paid";
                                isReprint = true;
                                PrintNewBill(date_reprint);
                                // update bill reprint count

                                int Result = HomeActivity.dbHandler.updateBillRepintCounts(Integer.parseInt(txtReprintBillNo.getText().toString()),date.getTime()+"" );
                                mClear();
                                // reverse tax
//                                REVERSETAX = !(crsrSettings.getInt(crsrSettings.getColumnIndex("Tax")) == 1);
                            } catch (Exception e) {
                                Logger.e(TAG , e+"");
                                mClear();
                            }
                        }
                    }
                }).show();
    }

    boolean REVERSETAX = false;

    /*************************************************************************************************************************************
     * Loads KOT order items to billing table
     *
     *************************************************************************************************************************************/
    private void LoadItemsForReprintBill(Cursor crsrBillItems) {

        billingItemsList.clear();

        if (crsrBillItems.moveToFirst()) {

            // reverse tax
            REVERSETAX = crsrBillItems.getString(crsrBillItems.getColumnIndex("IsReverseTaxEnable")).equalsIgnoreCase("YES");

            // Display items in table
            do {

                BillItemBean billItemBean = new BillItemBean();

                billItemBean.setiItemId(crsrBillItems.getInt(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_ITEM_ID)));
                billItemBean.setStrItemName(crsrBillItems.getString(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_ItemName)));
                billItemBean.setStrHSNCode(crsrBillItems.getString(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_HSNCode)));
                billItemBean.setDblQty(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_Quantity)));
                billItemBean.setDblValue(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_Value)));
                billItemBean.setDblAmount(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_Amount)));
                billItemBean.setDblCGSTRate(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_CGSTRate)));
                billItemBean.setDblCGSTAmount(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_CGSTAmount)));
                billItemBean.setDblDiscountPercent(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_DiscountPercent)));
                billItemBean.setDblDiscountAmount(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_DiscountAmount)));
//                billItemBean.setiDeptCode(crsrBillItems.getInt(crsrBillItems.getColumnIndex("DeptCode")));
//                billItemBean.setiCategCode(crsrBillItems.getInt(crsrBillItems.getColumnIndex("CategCode")));
                billItemBean.setStrIsReverseTaxEnabled(crsrBillItems.getString(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_IsReverseTaxEnabled)));
                //billItemBean.setDblModifierAmount(crsrBillItems.getDouble(crsrBillItems.getColumnIndex("ModifierAmount")));
                billItemBean.setDblSGSTRate(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_SGSTRate)));
                billItemBean.setDblSGSTAmount(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_SGSTAmount)));
                billItemBean.setStrUOM(crsrBillItems.getString(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_UOM)));
                billItemBean.setStrSupplyType(crsrBillItems.getString(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_SupplyType)));
                billItemBean.setDblIGSTRate(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_IGSTRate)));
                billItemBean.setDblIGSTAmount(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_IGSTAmount)));
                billItemBean.setDblCessRate(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_cessRate)));
                billItemBean.setDblCessAmount(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_cessAmount)));
                billItemBean.setDblOriginalRate(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_OriginalRate)));
                billItemBean.setDblTaxbleValue(crsrBillItems.getDouble(crsrBillItems.getColumnIndex(DatabaseHandler.KEY_TaxableValue)));

                billingItemsList.add(billItemBean);

            } while (crsrBillItems.moveToNext());

            //CalculateTotalAmountforRePrint();

        } else {
            Log.d("LoadKOTItems", "No rows in cursor");
        }
    }

    private int getIndex_pos(String substring) {

        int index = 0;
        for (int i = 0; index == 0 && i < spInterState.getCount(); i++) {

            if (spInterState.getItemAtPosition(i).toString().contains(substring)) {
                index = i;
            }

        }

        return index;

    }

    private void DateSelection(final TextView tv_inv_date) {        // StartDate: DateType = 1 EndDate: DateType = 2
        try {
            AlertDialog.Builder dlgReportDate = new AlertDialog.Builder(myContext);
            final DatePicker dateReportDate = new DatePicker(myContext);
            String date_str = getCurrentDate();
            String dd = date_str.substring(6, 10) + "-" + date_str.substring(3, 5) + "-" + date_str.substring(0, 2);
            DateTime objDate = new DateTime(dd);
            dateReportDate.updateDate(objDate.getYear(), objDate.getMonth(), objDate.getDay());
            String strMessage = "";


            dlgReportDate
                    .setIcon(R.mipmap.ic_company_logo)
                    .setTitle("Date Selection")
                    .setMessage(strMessage)
                    .setView(dateReportDate)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        String strDd = "";

                        public void onClick(DialogInterface dialog, int which) {
                            if (dateReportDate.getDayOfMonth() < 10) {
                                strDd = "0" + String.valueOf(dateReportDate.getDayOfMonth()) + "-";
                            } else {
                                strDd = String.valueOf(dateReportDate.getDayOfMonth()) + "-";
                            }
                            if (dateReportDate.getMonth() < 9) {
                                strDd += "0" + String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            } else {
                                strDd += String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            }

                            strDd += String.valueOf(dateReportDate.getYear());
                            tv_inv_date.setText(strDd);
                            Log.d("ReprintDate", "Selected Date:" + strDd);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mGetOtherChargesValue(){
        Cursor crsrOtherCharges = null;
        deliveryChargesMap = new HashMap<String, String>();
        try {
            crsrOtherCharges = HomeActivity.dbHandler.getAllOtherChargesIsEnabled();
            if(crsrOtherCharges != null && crsrOtherCharges.getCount() > 0 && crsrOtherCharges.moveToFirst()){
                do{
                    String chargename = crsrOtherCharges.getString(crsrOtherCharges.getColumnIndex(DatabaseHandler.KEY_OTHER_CHARGES_DESCRIPTION));
                    String amount = crsrOtherCharges.getString(crsrOtherCharges.getColumnIndex(DatabaseHandler.KEY_OTHER_CHARGES_AMOUNT));
                    deliveryChargesMap.put(chargename,amount);
                    dblOtherCharges = dblOtherCharges + crsrOtherCharges.getDouble(crsrOtherCharges.getColumnIndex(DatabaseHandler.KEY_OTHER_CHARGES_AMOUNT));
                }while(crsrOtherCharges.moveToNext());
                String strOtherCharges = String.format("%.2f", dblOtherCharges);
                double dblOtherChargesTemp = strOtherCharges.equals("")?0.00 : Double.parseDouble(strOtherCharges);
                edtOtherCharges.setText(String.format("%.2f",dblOtherChargesTemp));
            }
        } catch (Exception e) {
            Logger.i(TAG,"Other charges table fetch error on billing screen. " +e.getMessage());
        }finally {
            if(crsrOtherCharges != null){
                crsrOtherCharges.close();
            }
        }
    }

    private void mGetOwnerPos()
    {
        OWNERPOS = HomeActivity.dbHandler.getOwnerPOS();
        System.out.println(OWNERPOS);
    }

    private void mGetRewardDetails()
    {
        Cursor cursor = null;
        try{

            cursor = HomeActivity.dbHandler.getRewardPointsConfiguration();
            if(cursor!=null && cursor.moveToNext())
            {
                RewardPtToAmt = cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RewardPointsToAmt));
                amountToRewardPoints = cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_AmtToPt));
                RewardPoints = cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RewardPoints));
            }
        }catch(Exception e)
        {
            Logger.e(TAG, e+"");
        }finally {
            cursor.close();
        }

    }
    public void initSettingsData() {
        Cursor crsrSettings = null;
        try {
            crsrSettings = HomeActivity.dbHandler.getBillSettings();
            if (crsrSettings != null && crsrSettings.moveToFirst()) {
                DATE_TIME = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_DateAndTime));
                BUSINESS_DATE = crsrSettings.getString(crsrSettings.getColumnIndex(DatabaseHandler.KEY_BusinessDate));
                PRICE_CHANGE = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_PriceChange));
                BILL_WITH_STOCK = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_BillwithStock));
                PRINT_OWNER_DETAIL = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_PrintOwnerDetail));
                BILL_AMOUNT_ROUND_OFF = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_RoundOff));
                SALES_MAN_ID = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_SALES_MAN_ID));
                isForwardTaxEnabled = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_Tax));
                DISCOUNT_TYPE = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_DiscountType));
                MRP_RETAIL_PRICE_DIFF_PRINT = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_PRINT_MRP_RETAIL_DIFFERENCE));
                FAST_BILLING_MODE = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_FastBillingMode));
                REWARD_POINTS = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_RewardPoints));
                BOLD_HEADER = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_HeaderBold));
                isDAILY_BILL_NO_RESET = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_BILL_NO_RESET));
                HSNPRINTENABLED = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_HSNPrintEnabled_out));
                UTGSTENABLED = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_UTGSTEnabled));
                isHSNEnabled = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_HSNCode));
                PRINT_MRP_RETAIL_DIFFERENCE = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_PRINT_MRP_RETAIL_DIFFERENCE));
                if(PRINT_MRP_RETAIL_DIFFERENCE ==1)
                    PRINT_DISCOUNT =1;
                else
                    PRINT_DISCOUNT =0;

                if(UTGSTENABLED ==1)
                    til_sgstAmount.setHint("UTGST Amount");
                //Date business_date = new SimpleDateFormat("dd-MM-yyyy").parse(BUSINESS_DATE);
                String current_date = (getCurrentDate());

                edtInvoiceDate.setText(BUSINESS_DATE);
                if (!BUSINESS_DATE.equals(current_date)) {
                    edtInvoiceDate.setVisibility(View.VISIBLE);
                }// else visibility gone

                if (SALES_MAN_ID == 1){
                    avSalesManId.setVisibility(View.VISIBLE);
                }

                switch (FAST_BILLING_MODE){
                    case Constants.DISPLAY_TAB_ITEM:
                        btnDisplayItems.setVisibility(View.VISIBLE);
                        btnDisplayDepartment.setVisibility(View.GONE);
                        btnDisplayCategory.setVisibility(View.GONE);
                        break;
                    case Constants.DISPLAY_TAB_DEPARTMENT:
                        btnDisplayItems.setVisibility(View.VISIBLE);
                        btnDisplayDepartment.setVisibility(View.VISIBLE);
                        btnDisplayCategory.setVisibility(View.GONE);
                        break;
                    case Constants.DISPLAY_TAB_CATEGORY:
                        btnDisplayItems.setVisibility(View.VISIBLE);
                        btnDisplayDepartment.setVisibility(View.VISIBLE);
                        btnDisplayCategory.setVisibility(View.VISIBLE);
                        break;
                    default:
                        btnDisplayItems.setVisibility(View.GONE);
                        btnDisplayDepartment.setVisibility(View.GONE);
                        btnDisplayCategory.setVisibility(View.GONE);
                        break;
                }
            }
        } catch (Exception e) {
            Logger.i(TAG,"Settings init() error on billing screen. " +e.getMessage());
        }finally {
            if(crsrSettings != null){
                crsrSettings.close();
            }
        }
    }


    private void applyDecimalValidation(){
        edtTotalIGST.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(7,2)});
        edtTotalCGST.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(7,2)});
        edtTotalSGST.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(7,2)});
        edtTotalCess.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(7,2)});
        edtTotalDiscount.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(7,2)});
        edtOtherCharges.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(7,2)});
        edtTotalTaxableValue.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(10,2)});
        edtTotalAmount.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(10,2)});
    }

    public void mBillNumberSet(){
      /*  int iBillNumber = HomeActivity.dbHandler.getLastBillNo();
        if(iBillNumber == -1){
            msgBox.Show("Error","Please contact support team before billing.");
            return;
        }
        iBillNumber = iBillNumber + 1;
        edtBillNumber.setText(""+iBillNumber);*/
        edtBillNumber.setText(String.valueOf(HomeActivity.dbHandler.getNewBillNumber()));
    }

    private void initAutoCompleteTextDataForItems() {
        mAdapterItems = new SimpleCursorAdapter(myContext, R.layout.auto_complete_textview_two_strings, null,
                new String[]{DatabaseHandler.KEY_ItemShortName, DatabaseHandler.KEY_ItemBarcode, DatabaseHandler.KEY_ShortCode},
                new int[]{R.id.tv_auto_complete_textview_item_one, R.id.tv_auto_complete_textview_two,
                        R.id.tv_auto_complete_textview_three},
                0);
        avItemSearch.setThreshold(1);
        avItemSearch.setAdapter(mAdapterItems);

        mAdapterItems.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence str) {
                Logger.i(TAG, "Item search data." + str);
                return HomeActivity.dbHandler.mGetItemSearchData(str);
            }
        });

        mAdapterItems.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public CharSequence convertToString(Cursor cur) {
                int index = cur.getColumnIndex(DatabaseHandler.KEY_ItemShortName);
                return cur.getString(index);
            }
        });

        avItemSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the
                // result set
                avItemSearch.setText("");
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                itemMasterBean = null;
                // Get the state's capital from this row in the database.
                if (cursor != null ) {
                    String itemShortName = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemShortName));
                    Cursor crsr = HomeActivity.dbHandler.getItemByItemShortName(itemShortName);
                    if(crsr!=null && crsr.getCount() >1)
                    {
                        inflateMultipleRateOption(crsr);
                    }
                    else
                    {
                        itemMasterBean = new ItemMasterBean();
                        itemMasterBean.set_id(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_id)));
                        itemMasterBean.setStrShortName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemShortName)));
                        if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_HSNCode)) != null) {
                            itemMasterBean.setStrHSNCode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_HSNCode)));
                        }
                        if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_UOM)) != null) {
                            itemMasterBean.setStrUOM(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_UOM)));
                        }
                        if (cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)) != null && !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)).isEmpty()) {
                            itemMasterBean.setStrBarcode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)));
                        }
                        if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_Quantity)) != null &&
                                !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_Quantity)).equals("")) {
                            itemMasterBean.setDblQty(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_Quantity)));
                        }

                        if(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_BrandCode)) > 0) {
                            itemMasterBean.setiBrandCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_BrandCode)));
                        }
                        if(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)) > 0) {
                            itemMasterBean.setiDeptCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)));
                        }
                        if(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_CategoryCode)) > 0) {
                            itemMasterBean.setiCategoryCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_CategoryCode)));
                        }

                        if (cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)) != null && !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)).isEmpty()) {
                            itemMasterBean.setStrSupplyType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)));
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)) > 0) {
                            itemMasterBean.setDblRetailPrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)));
                        } else{
                            itemMasterBean.setDblRetailPrice(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_MRP)) > 0) {
                            itemMasterBean.setDblMRP(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_MRP)));
                        } else{
                            itemMasterBean.setDblMRP(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)) > 0) {
                            itemMasterBean.setDblWholeSalePrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)));
                        } else{
                            itemMasterBean.setDblWholeSalePrice(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_CGSTRate)) > 0) {
                            itemMasterBean.setDblCGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_CGSTRate)));
                        } else {
                            itemMasterBean.setDblCGSTRate(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_SGSTRate)) > 0) {
                            itemMasterBean.setDblSGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_SGSTRate)));
                        } else {
                            itemMasterBean.setDblSGSTRate(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_IGSTRate)) > 0) {
                            itemMasterBean.setDblIGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_IGSTRate)));
                        } else {
                            itemMasterBean.setDblIGSTRate(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_cessRate)) > 0) {
                            itemMasterBean.setDblCessRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_cessRate)));
                        } else {
                            itemMasterBean.setDblCessRate(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountAmount)) > 0) {
                            itemMasterBean.setDblDiscountAmt(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountAmount)));
                        } else {
                            itemMasterBean.setDblDiscountAmt(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountPercent)) > 0) {
                            itemMasterBean.setDbDiscountPer(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountPercent)));
                        } else {
                            itemMasterBean.setDbDiscountPer(0);
                        }
                    }

                }
                // Update the parent class's TextView
                if (itemMasterBean != null) {
                    //mPopulateItemsListAdapterData(billingItemsList);
                    mAddItemToOrderTableList(itemMasterBean,false);
                }
            }
        });
    }

    private void initAutoCompleteTextDataForCustomer() {
        mAdapterCustomer = new SimpleCursorAdapter(myContext, R.layout.auto_complete_textview_two_strings, null,
                new String[]{DatabaseHandler.KEY_CustName, DatabaseHandler.KEY_CustPhoneNo},
                new int[]{R.id.tv_auto_complete_textview_item_one, R.id.tv_auto_complete_textview_two},
                0);
        avCustomerSearch.setThreshold(1);
        avCustomerSearch.setAdapter(mAdapterCustomer);

        mAdapterCustomer.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence str) {
                Logger.i(TAG, "Customer name search data." + str);
                return HomeActivity.dbHandler.mGetCustomerSearchData(str);
            }
        });

        mAdapterCustomer.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public CharSequence convertToString(Cursor cur) {
                int index = cur.getColumnIndex(DatabaseHandler.KEY_CustName);
                return cur.getString(index);
            }
        });

        avCustomerSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the
                // result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                customerBean = null;
                if (cursor != null) {
                    customerBean = new Customer();
                    customerBean.setStrCustName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_CustName)));
                    customerBean.setStrCustContactNumber(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_CustPhoneNo)));
                    customerBean.setiCustId(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_id)));
                    customerBean.set_id(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_id)));
                    if (cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN)) != null && !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN)).isEmpty()) {
                        customerBean.setStrCustGSTIN(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN)));
                    }
                    if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_CreditAmount)) > 0) {
                        customerBean.setdCreditAmount(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_CreditAmount)));
                    }
                }
                // Update the parent class's TextView
                if (customerBean != null) {
                    edtCustomerMobile.setText(customerBean.getStrCustContactNumber());
                    tv_billing_CustId.setText(""+customerBean.get_id());
                    if (customerBean.getStrCustGSTIN() != null && !customerBean.getStrCustGSTIN().isEmpty()) {
                        edtCustomerGSTIN.setText(customerBean.getStrCustGSTIN());
                        String custGSTIN = customerBean.getStrCustGSTIN();
                        String posCode = custGSTIN.substring(0,2);
                        checkForInterstateTransaction(posCode);

                    }
                }
            }
        });
    }

    void checkForInterstateTransaction(String posCode)
    {
        if(!posCode.equalsIgnoreCase(OWNERPOS))
        {
            cbInterStateStatus.setChecked(true);
            spInterState.setSelection(getIndex_pos(posCode));
            Toast.makeText(myContext,getString(R.string.autosetting_cust_State_message),Toast.LENGTH_SHORT).show();

        }else
        {
            cbInterStateStatus.setChecked(false);
        }
    }

    private void initAutoCompleteTextDataForSalesManId() {
        if(SALES_MAN_ID == 0){
            return;
        }
        mAdapterSalesManId = new SimpleCursorAdapter(myContext, R.layout.auto_complete_textview_two_strings, null,
                new String[]{DatabaseHandler.KEY_SALES_MAN_ID},
                new int[]{R.id.tv_auto_complete_textview_item_one},
                0);
        avSalesManId.setThreshold(1);
        avSalesManId.setAdapter(mAdapterSalesManId);

        mAdapterSalesManId.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence str) {
                Logger.i(TAG, "SalesManId search data." + str);
                return HomeActivity.dbHandler.mGetSalesManIdSearchData(str);
            }
        });

        mAdapterSalesManId.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public CharSequence convertToString(Cursor cur) {
                int index = cur.getColumnIndex(DatabaseHandler.KEY_SALES_MAN_ID);
                return cur.getString(index);
            }
        });

        avSalesManId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the
                // result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                userSalesManIdBean = null;
                if (cursor != null) {
                    userSalesManIdBean = new User();
                    userSalesManIdBean.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_id)));
                    userSalesManIdBean.setUserRole(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ROLE_ID)));
                    userSalesManIdBean.setStrSalesManId(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SALES_MAN_ID)));
                }
            }
        });
    }

    private void mPopulateInterStateSpinnerData(){
        arrayPOS = getResources().getStringArray(R.array.poscode);
        //Creating the ArrayAdapter instance having the POSCode list
        ArrayAdapter adapterPOS = new ArrayAdapter(myContext, android.R.layout.simple_spinner_item, arrayPOS);
        adapterPOS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spInterState.setAdapter(adapterPOS);
    }

    private void mAutoCompleteTextViewCustomerTextChange(){
        avCustomerSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strCustomerData = editable.toString();
                if (strCustomerData.isEmpty()) {
                    edtCustomerMobile.setText("");
                    edtCustomerGSTIN.setText("");
                    tv_billing_CustId.setText("-1");
                    cbInterStateStatus.setChecked(false);
                    customerBean = null;
                }
            }
        });
    }

    private void mPopulateItemsListAdapterData(){

        if(billingItemsList != null) {
            edtItemsSelectedCounter.setText(""+ billingItemsList.size());
            rvBillingProductList.setLayoutManager(new LinearLayoutManager(myContext));
            if(billingListAdapter!=null)
                billingListAdapter.notifyData(billingItemsList);
            else
                billingListAdapter = new BillingListAdapter(this, billingItemsList,isHSNEnabled);
            rvBillingProductList.setAdapter(billingListAdapter);
            rvBillingProductList.smoothScrollToPosition(billingItemsList.size());
        }
    }


    /*private void initDataToWidgets() {
        etBarcodeHide.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String strBarcodeData = editable.toString();
                if (strBarcodeData.length() > 0 && (strBarcodeData.charAt(strBarcodeData.length() - 1) == '\n')) {
                    //search method call
                    etBarcodeHide.setText("");
                    editable.clear();
                    PackageUtils.hideKeyboard(myContext);
                }
            }
        });
    }
*/
    private void mAddItemToOrderTableList(ItemMasterBean recieved_itemMasterBean, boolean bFromResumeBill){
        try {
            boolean bExistsStatus = false;
            boolean alreadyAddedItem = false;
            int position =-1;
            for (int i = 0; i < billingItemsList.size() && !bExistsStatus; i++) {
                if (billingItemsList.get(i).getiItemId() == recieved_itemMasterBean.get_id())
                {
                    bExistsStatus = true;
                    position =i;
                    if(BILL_WITH_STOCK ==1)
                    {
                        double availableQty = recieved_itemMasterBean.getDblQty();
                        if (availableQty < (billingItemsList.get(i).getDblQty()+1))
                        {
                            msgBox.Show(getString(R.string.note),getString(R.string.less_stock_message)+" "+availableQty );
                            return;
                        }
                    }
                    double dblQtyTemp = billingItemsList.get(i).getDblQty();
                    billingItemsList.get(i).setDblQty((1 + dblQtyTemp));
                    alreadyAddedItem = true;
                }
            }
            if (!bExistsStatus)
            {
                BillItemBean billItemBeanTemp = new BillItemBean();
                if (true) //jWeighScale == 0)
                {
                    billItemBeanTemp.setDblQty(1.00);
                    if(bFromResumeBill){
                        billItemBeanTemp.setDblQty(recieved_itemMasterBean.getDblQty());
                    }
                } else {
                    //etQty.setText(String.format("%.2f", getQuantityFromWeighScale()));
                }
                if(BILL_WITH_STOCK ==1)
                {
                    double availableQty = recieved_itemMasterBean.getDblQty();
                    if (availableQty < billItemBeanTemp.getDblQty())
                    {
                        msgBox.Show(getString(R.string.note),getString(R.string.less_stock_message)+" "+availableQty );
                        return;
                    }
                }

                billItemBeanTemp.setiItemId(recieved_itemMasterBean.get_id());
                billItemBeanTemp.setStrItemName(recieved_itemMasterBean.getStrShortName());
                billItemBeanTemp.setStrHSNCode(recieved_itemMasterBean.getStrHSNCode());
                billItemBeanTemp.setStrBarcode(recieved_itemMasterBean.getStrBarcode());
                billItemBeanTemp.setStrUOM(recieved_itemMasterBean.getStrUOM());
                billItemBeanTemp.setiDeptCode(recieved_itemMasterBean.getiDeptCode());
                billItemBeanTemp.setiCategCode(recieved_itemMasterBean.getiCategoryCode());
                billItemBeanTemp.setiBrandCode(recieved_itemMasterBean.getiBrandCode());
                billItemBeanTemp.setStrSupplyType(recieved_itemMasterBean.getStrSupplyType());
                billItemBeanTemp.setDblCGSTRate(recieved_itemMasterBean.getDblCGSTRate());
                billItemBeanTemp.setDblSGSTRate(recieved_itemMasterBean.getDblSGSTRate());
                billItemBeanTemp.setDblIGSTRate(recieved_itemMasterBean.getDblIGSTRate());
                billItemBeanTemp.setDblCessRate(recieved_itemMasterBean.getDblCessRate());
                billItemBeanTemp.setDblMRP(recieved_itemMasterBean.getDblMRP());
                billItemBeanTemp.setDblRetailPrice(recieved_itemMasterBean.getDblRetailPrice());
                billItemBeanTemp.setDblWholeSalePrice(recieved_itemMasterBean.getDblWholeSalePrice());

                billingItemsList.add(billItemBeanTemp);
                position = billingItemsList.size() -1;
            }
            if(position>-1)
            {
                calculateAndDisplayBillingData(position, alreadyAddedItem);
            }

        } catch (Exception ex){
            Logger.e(TAG,"Unable to add the selected item into billingItemsList. " +
                    "Function Name : mAddItemToOrderTableList(ItemMasterBean itemMasterBean). "
                    + ex.getMessage());
        }
    }

    private void calculateAndDisplayBillingData(int position, boolean isPriceChange)
    {
        mAddedItemDiscountAndTaxes(position, isPriceChange);
        mCalculateTotalAmount();
        mPopulateItemsListAdapterData();
    }
    private void mAddedItemDiscountAndTaxes(int position, boolean isPriceChange){
        try {
            boolean calculateForPosition = false;
            double dblTaxableValue = 0, dblCGSTAmt = 0, dblSGSTAmt = 0, dblIGSTAmt = 0, dblCessAmt = 0, dblAmount = 0, dblDiscountAmount = 0, dblDiscountPercent = 0;
            double qty =0, baseRate=0;
            double IGSTRate = 0, CGSTRate = 0, SGSTRate = 0, cessRate =0;
            for(int i =0;i< billingItemsList.size() && !calculateForPosition;i++) //BillItemBean billItemBean : billingItemsList)
            {
                BillItemBean billItemBean = new BillItemBean();
                if(position >-1)
                {
                    billItemBean = billingItemsList.get(position);
                    calculateForPosition = true;
                }
                else
                {
                    billItemBean = billingItemsList.get(i);
                }
                int selectedId = rgPriceOptions.getCheckedRadioButtonId();
                RadioButton rbPriceOptions = (RadioButton) rootView.findViewById(selectedId);
                switch (rbPriceOptions.getText().toString()) {
                    case Constants.MRP:
                        billItemBean.setDblOriginalRate(billItemBean.getDblMRP());
                        break;
                    case Constants.RETAIL_PRICE:
                        billItemBean.setDblOriginalRate(billItemBean.getDblRetailPrice());
                        break;
                    case Constants.WHOLE_SALE_PRICE:
                        billItemBean.setDblOriginalRate(billItemBean.getDblWholeSalePrice());
                        break;
                    default:
                        break;
                }
                if(isForwardTaxEnabled ==1) // forward Tax
                {
                    if(!isPriceChange)
                        billItemBean.setDblValue(billItemBean.getDblOriginalRate());

                    String strQty = ""+billItemBean.getDblQty();
                    String strBaseRate = ""+billItemBean.getDblValue();
                    qty = strQty.equals("")?0.00 : Double.parseDouble(strQty);
                    baseRate = strBaseRate.equals("")?0.00 : Double.parseDouble(strBaseRate);
                    IGSTRate = billItemBean.getDblIGSTRate();
                    CGSTRate = billItemBean.getDblCGSTRate();
                    SGSTRate = billItemBean.getDblSGSTRate();
                    cessRate = billItemBean.getDblCessRate();

                    dblDiscountAmount = billItemBean.getDblMRP() - billItemBean.getDblValue();
                    dblDiscountPercent = (dblDiscountAmount/billItemBean.getDblMRP())*100;


                }else
                {
                    // reverse Tax
                    billItemBean.setDblCessRate(0.00);
                    String strQty = ""+billItemBean.getDblQty();
                    String strOriginalRate = ""+billItemBean.getDblOriginalRate();
                    qty = strQty.equals("")?0.00 : Double.parseDouble(strQty);
                    double originalRate = strOriginalRate.equals("")?0.00 : Double.parseDouble(strOriginalRate);

                    IGSTRate = billItemBean.getDblIGSTRate();
                    CGSTRate = billItemBean.getDblCGSTRate();
                    SGSTRate = billItemBean.getDblSGSTRate();
                    cessRate = billItemBean.getDblCessRate();

                    baseRate = (originalRate )/ (1 + (CGSTRate / 100)+(SGSTRate / 100)+ (cessRate/100));
                    //billItemBean.setDblValue(baseRate);
                    //Changed on 30/01/2018
                    billItemBean.setDblValue(Double.parseDouble(String.format("%.2f",baseRate)));

                    dblDiscountAmount = billItemBean.getDblMRP() - billItemBean.getDblValue();
                    dblDiscountPercent = (dblDiscountAmount/billItemBean.getDblMRP())*100;

                }

                double totalDiscountAmount = dblDiscountAmount*qty;
                dblTaxableValue  = baseRate *qty;
                dblIGSTAmt =  dblTaxableValue *IGSTRate/100;
                dblCGSTAmt = dblTaxableValue* CGSTRate/100;
                dblSGSTAmt = dblTaxableValue * SGSTRate/100;
                dblCessAmt = dblTaxableValue * cessRate/100;
                dblAmount = dblTaxableValue + dblIGSTAmt + dblCessAmt;

              /*  billItemBean.setDblDiscountPercent(dblDiscountPercent);
                billItemBean.setDblDiscountAmount(totalDiscountAmount);
                billItemBean.setDblTaxbleValue(dblTaxableValue);
                billItemBean.setDblIGSTAmount(dblIGSTAmt);
                billItemBean.setDblCGSTAmount(dblCGSTAmt);
                billItemBean.setDblSGSTAmount(dblSGSTAmt);
                billItemBean.setDblCessAmount(dblCessAmt);
                billItemBean.setDblAmount(dblAmount);*/
                //Changed on 30/01/2018
                billItemBean.setDblDiscountPercent(Double.parseDouble(String.format("%.2f",dblDiscountPercent)));
                billItemBean.setDblDiscountAmount(Double.parseDouble(String.format("%.2f",totalDiscountAmount)));
                billItemBean.setDblTaxbleValue(Double.parseDouble(String.format("%.2f",dblTaxableValue)));
                billItemBean.setDblIGSTAmount(Double.parseDouble(String.format("%.2f",dblIGSTAmt)));
                billItemBean.setDblCGSTAmount(Double.parseDouble(String.format("%.2f",dblCGSTAmt)));
                billItemBean.setDblSGSTAmount(Double.parseDouble(String.format("%.2f",dblSGSTAmt)));
                billItemBean.setDblCessAmount(Double.parseDouble(String.format("%.2f",dblCessAmt)));
                billItemBean.setDblAmount(Double.parseDouble(String.format("%.2f",dblAmount)));
            }

        }catch (Exception ex){
            Logger.e(TAG,"Unable able to calculate listed Products on billing list. " +
                    "Function Name : mAddedItemDiscountAndTaxes(). "
                    + ex.getMessage());
        }
    }

    /*************************************************************************************************************************************
     * Calculates bill sub total, sales tax amount, service tax amount and Bill
     * total amount.
     ************************************************************************************************************************************/
    private void mCalculateTotalAmount()
    {
        double dblTaxableValue = 0,dblCGSTAmt = 0,  dblSGSTAmt = 0, dblOtherCharges = 0, dTaxAmt = 0, dSerTaxAmt = 0, dTotalBillAmount = 0;
        float dTaxPercent = 0, dSerTaxPercent = 0;
        double dTotalBillAmount_for_reverseTax =0, dblTotalDiscountAmount = 0;
        double dIGSTAmt =0, dcessAmt =0;
        try {
            if(billingItemsList.size() > 0) {
                try {
                    if(!edtOtherCharges.getText().toString().isEmpty() && edtOtherCharges.getText().toString().length() > 0){
                        dblOtherCharges = Double.parseDouble(edtOtherCharges.getText().toString());
                    }
                }catch (Exception e){
                    Logger.e(TAG,"Unable to get other charges data." +e.getMessage());
                }
                // Item wise tax calculation ----------------------------
                for (BillItemBean billItemBean : billingItemsList)
                {
                    dblCGSTAmt += billItemBean.getDblCGSTAmount();
                    dblSGSTAmt += billItemBean.getDblSGSTAmount();
                    dIGSTAmt += billItemBean.getDblIGSTAmount();
                    dcessAmt += billItemBean.getDblCessAmount();
                    dblTotalDiscountAmount += billItemBean.getDblDiscountAmount();
//                    if (isForwardTaxEnabled == 1)  // forward tax
//                    {
//                        dblSubTotal += billItemBean.getDblTaxbleValue();
//                        dTotalBillAmount += billItemBean.getDblAmount();
//                    }
//                    else // reverse tax
//                    {
//                        String strQty = ""+billItemBean.getDblQty();
//                        String strValue = ""+billItemBean.getDblValue();
//                        double qty = strQty.equals("")?0.00 : Double.parseDouble(strQty);
//                        double baseRate = strValue.equals("")?0.00 : Double.parseDouble(strValue);
//                        dblSubTotal += (qty*baseRate);
//                        dTotalBillAmount_for_reverseTax += billItemBean.getDblAmount();
//                    }
                    dblTaxableValue += billItemBean.getDblTaxbleValue();
                    dTotalBillAmount += billItemBean.getDblAmount();
                }

                // ------------------------------------------
                // Bill wise tax Calculation -------------------------------
          /*  Cursor crsrtax = db.getTaxConfigs(1);
            if (crsrtax.moveToFirst()) {
                dTaxPercent = crsrtax.getFloat(crsrtax.getColumnIndex("TotalPercentage"));
                dTaxAmt += dSubTotal * (dTaxPercent / 100);
            }
            Cursor crsrtax1 = db.getTaxConfigs(2);
            if (crsrtax1.moveToFirst()) {
                dSerTaxPercent = crsrtax1.getFloat(crsrtax1.getColumnIndex("TotalPercentage"));
                dSerTaxAmt += dSubTotal * (dSerTaxPercent / 100);
            }*/
                // -------------------------------------------------
                //String strTax = crsrSettings.getString(crsrSettings.getColumnIndex("Tax"));
//                switch (isForwardTaxEnabled){
//                    case 0: // reverse tax
//                        if (isDISCOUNT_TYPE == 1) // item wise
//                        {
////                            edtTotalIGST.setText(String.format("%.2f", dIGSTAmt));
////                            edtTotalCGST.setText(String.format("%.2f", dblCGSTAmt));
////                            edtTotalSGST.setText(String.format("%.2f", dblSGSTAmt));
////                            edtTotalCess.setText(String.format("%.2f",dcessAmt));
////
////                            if (cbInterStateStatus.isChecked()) // interstate
////                            {
////                                edtTotalIGST.setTextColor(getResources().getColor(R.color.color_black));
////                                edtTotalCGST.setTextColor(getResources().getColor(R.color.app_color_background));
////                                edtTotalSGST.setTextColor(getResources().getColor(R.color.app_color_background));
////                            } else {
////                                edtTotalIGST.setTextColor(getResources().getColor(R.color.app_color_background));
////                                edtTotalCGST.setTextColor(getResources().getColor(R.color.color_black));
////                                edtTotalSGST.setTextColor(getResources().getColor(R.color.color_black));
////                            }
////                            edtTotalTaxableValue.setText(String.format("%.2f", dblSubTotal));
////                            edtTotalAmount.setText(String.format("%.2f", dTotalBillAmount_for_reverseTax + dblOtherCharges));
//
//                        }
//                        else
//                        {
//                            edtTotalTaxableValue.setText(String.format("%.2f", dblTaxableValue));
//                            edtTotalIGST.setText(String.format("%.2f", dIGSTAmt));
//                            edtTotalCGST.setText(String.format("%.2f", dblCGSTAmt));
//                            edtTotalSGST.setText(String.format("%.2f", dblSGSTAmt));
//                            edtTotalCess.setText(String.format("%.2f",dcessAmt));
//
//                            if (cbInterStateStatus.isChecked()) // interstate
//                            {
//                                edtTotalIGST.setTextColor(getResources().getColor(R.color.color_black));
//                                edtTotalCGST.setTextColor(getResources().getColor(R.color.app_color_background));
//                                edtTotalSGST.setTextColor(getResources().getColor(R.color.app_color_background));
//                            } else {
//                                edtTotalIGST.setTextColor(getResources().getColor(R.color.app_color_background));
//                                edtTotalCGST.setTextColor(getResources().getColor(R.color.color_black));
//                                edtTotalSGST.setTextColor(getResources().getColor(R.color.color_black));
//                            }
//                            edtTotalAmount.setText(String.format("%.2f", dTotalBillAmount + dblOtherCharges));
//                        }
//                        break;
//                    case 1: // forward tax
//                        if (isDISCOUNT_TYPE == 1) {
//                            edtTotalIGST.setText(String.format("%.2f", dIGSTAmt));
//                            edtTotalCGST.setText(String.format("%.2f", dblCGSTAmt));
//                            edtTotalSGST.setText(String.format("%.2f", dblSGSTAmt));
//                            edtTotalCess.setText(String.format("%.2f",dcessAmt));
//
//                            if (cbInterStateStatus.isChecked()) // interstate
//                            {
//                                edtTotalIGST.setTextColor(getResources().getColor(R.color.color_black));
//                                edtTotalCGST.setTextColor(getResources().getColor(R.color.app_color_background));
//                                edtTotalSGST.setTextColor(getResources().getColor(R.color.app_color_background));
//                            } else {
//                                edtTotalIGST.setTextColor(getResources().getColor(R.color.app_color_background));
//                                edtTotalCGST.setTextColor(getResources().getColor(R.color.color_black));
//                                edtTotalSGST.setTextColor(getResources().getColor(R.color.color_black));
//                            }
//
//                            edtTotalTaxableValue.setText(String.format("%.2f", dblTaxableValue));
//                            double totalBillAmount = dTotalBillAmount + dblOtherCharges;
//                            double effectiveBillAmount = totalBillAmount;
//                            double roundOffAmount = 0.00;
//                            if(BILL_AMOUNT_ROUND_OFF ==1)
//                            {
//                                String strTotal = ""+totalBillAmount;
//                                effectiveBillAmount = +(Math.round(totalBillAmount)); // Round off done
//                                roundOffAmount = Double.parseDouble(strTotal.substring(strTotal.indexOf(".")));
//                            }
//                            edtRoundOff.setText(String.format("%.2f",roundOffAmount));
//                            edtTotalAmount.setText(String.format("%.2f", effectiveBillAmount));
//                        }
//                        else
//                        {
//                            edtTotalIGST.setText(String.format("%.2f", dIGSTAmt));
//                            edtTotalCGST.setText(String.format("%.2f", dblCGSTAmt));
//                            edtTotalSGST.setText(String.format("%.2f", dblSGSTAmt));
//                            edtTotalCess.setText(String.format("%.2f",dcessAmt));
//
//                            if (cbInterStateStatus.isChecked()) // interstate
//                            {
//                                edtTotalIGST.setTextColor(getResources().getColor(R.color.color_black));
//                                edtTotalCGST.setTextColor(getResources().getColor(R.color.app_color_background));
//                                edtTotalSGST.setTextColor(getResources().getColor(R.color.app_color_background));
//                            } else {
//                                edtTotalIGST.setTextColor(getResources().getColor(R.color.app_color_background));
//                                edtTotalCGST.setTextColor(getResources().getColor(R.color.color_black));
//                                edtTotalSGST.setTextColor(getResources().getColor(R.color.color_black));
//                            }
//                            edtTotalTaxableValue.setText(String.format("%.2f", dblTaxableValue));
//                            //edtTotalAmount.setText(String.format("%.2f", dTotalBillAmount + dOtherCharges));
//                            double totalBillAmount = dTotalBillAmount + dblOtherCharges;
//                            double effectiveBillAmount = totalBillAmount;
//                            double roundOffAmount = 0.00;
//                            if(BILL_AMOUNT_ROUND_OFF ==1)
//                            {
//                                String strTotal = ""+totalBillAmount;
//                                effectiveBillAmount = +(Math.round(totalBillAmount)); // Round off done
//                                roundOffAmount = Double.parseDouble(strTotal.substring(strTotal.indexOf(".")));
//                            }
//                            edtRoundOff.setText(String.format("%.2f",roundOffAmount));
//                            edtTotalAmount.setText(String.format("%.2f", effectiveBillAmount));
//                        }
//                        break;
//                    default:
//                        break;
//                }

            }else {
                Logger.e(TAG,"No data to calculate over all bill." +
                        "Function Name : mOverAllTotalBillCalculation().");
            }

            edtTotalDiscount.setText(String.format("%.2f", dblTotalDiscountAmount));
            edtTotalIGST.setText(String.format("%.2f", dIGSTAmt));
            edtTotalCGST.setText(String.format("%.2f", dblCGSTAmt));
            edtTotalSGST.setText(String.format("%.2f", dblSGSTAmt));
            edtTotalCess.setText(String.format("%.2f",dcessAmt));

            if (cbInterStateStatus.isChecked()) // interstate
            {
                edtTotalIGST.setTextColor(getResources().getColor(R.color.color_black));
                edtTotalCGST.setTextColor(getResources().getColor(R.color.app_color_background));
                edtTotalSGST.setTextColor(getResources().getColor(R.color.app_color_background));
            } else {
                edtTotalIGST.setTextColor(getResources().getColor(R.color.app_color_background));
                edtTotalCGST.setTextColor(getResources().getColor(R.color.color_black));
                edtTotalSGST.setTextColor(getResources().getColor(R.color.color_black));
            }
            edtTotalTaxableValue.setText(String.format("%.2f", dblTaxableValue));
            //edtTotalAmount.setText(String.format("%.2f", dTotalBillAmount + dOtherCharges));
            double totalBillAmount = dTotalBillAmount + dblOtherCharges;
            double effectiveBillAmount = totalBillAmount;
            double roundOffAmount = 0.00;
            if(BILL_AMOUNT_ROUND_OFF ==1)
            {
                String strTotal = ""+totalBillAmount;
                effectiveBillAmount = +(Math.round(totalBillAmount)); // Round off done
                roundOffAmount = Double.parseDouble(strTotal.substring(strTotal.indexOf(".")));
            }
            edtRoundOff.setText(String.format("%.2f",roundOffAmount));
            edtTotalAmount.setText(String.format("%.2f", effectiveBillAmount));

        }catch (Exception ex){
            Logger.e(TAG,"Unable able to calculate over all bill. " +
                    "Function Name : mOverAllTotalBillCalculation(). "
                    + ex.getMessage());
        }
    }


    //Not included reverse and forward tax calculation
    private void mOverAllTotalBillCalculation(){
        try {
            if (billingItemsList.size() > 0) {
                double dblTotalTaxableValue = 0, dblSubTotalValue = 0, dblTotalCGSTAmt = 0, dblTotalSGSTAmt = 0,
                        dblTotalIGSTAmt = 0, dblTotalCessAmt = 0, dblTotalAmount = 0, dblTotalDiscount = 0, dblOtherCharges = 0, dTotalBillAmount_for_reverseTax = 0;
                try {
                    if(!edtOtherCharges.getText().toString().isEmpty() && edtOtherCharges.getText().toString().length() > 0){
                        dblOtherCharges = Double.parseDouble(edtOtherCharges.getText().toString());
                    }
                }catch (Exception e){
                    Logger.e(TAG,"Unable to get other charges data." +e.getMessage());
                }
                for(BillItemBean billItemBean : billingItemsList) {
                    dblTotalTaxableValue = dblTotalTaxableValue + billItemBean.getDblTaxbleValue();
                    dblSubTotalValue = dblSubTotalValue + billItemBean.getDblAmount();
                    //Implemented call method multiple time is required for calculation
                    if (cbInterStateStatus.isChecked()) {
                        dblTotalIGSTAmt = dblTotalIGSTAmt + billItemBean.getDblIGSTAmount();
                        dblTotalCGSTAmt = 0;
                        dblTotalSGSTAmt = 0;
                    } else {
                        dblTotalCGSTAmt = dblTotalCGSTAmt + billItemBean.getDblCGSTAmount();
                        dblTotalSGSTAmt = dblTotalSGSTAmt + billItemBean.getDblSGSTAmount();
                        dblTotalIGSTAmt = 0;
                    }

                    if (cbInterStateStatus.isChecked()) {
                        edtTotalCGST.setTextColor(getResources().getColor(R.color.app_color_background));
                        edtTotalSGST.setTextColor(getResources().getColor(R.color.app_color_background));
                        edtTotalIGST.setTextColor(getResources().getColor(R.color.color_black));
                    } else {
                        edtTotalCGST.setTextColor(getResources().getColor(R.color.color_black));
                        edtTotalSGST.setTextColor(getResources().getColor(R.color.color_black));
                        edtTotalIGST.setTextColor(getResources().getColor(R.color.app_color_background));
                    }

                    dblTotalIGSTAmt = dblTotalIGSTAmt + billItemBean.getDblIGSTAmount();
                    dblTotalCGSTAmt = dblTotalCGSTAmt + billItemBean.getDblCGSTAmount();
                    dblTotalSGSTAmt = dblTotalSGSTAmt + billItemBean.getDblSGSTAmount();

                    dblTotalCessAmt = dblTotalCessAmt + billItemBean.getDblCessAmount();

                    dblTotalAmount = dblTotalTaxableValue + dblTotalCGSTAmt
                            + dblTotalSGSTAmt + dblTotalIGSTAmt
                            + dblTotalCessAmt + dblOtherCharges;
                    dblTotalDiscount = dblTotalDiscount + billItemBean.getDblDiscountAmount();
                }
                edtTotalIGST.setText(String.format("%.2f",dblTotalIGSTAmt));
                edtTotalCGST.setText(String.format("%.2f",dblTotalCGSTAmt));
                edtTotalSGST.setText(String.format("%.2f",dblTotalSGSTAmt));
                edtTotalCess.setText(String.format("%.2f",dblTotalCessAmt));
                edtTotalDiscount.setText(String.format("%.2f",dblTotalDiscount));
                edtOtherCharges.setText(String.format("%.2f",dblOtherCharges));
                edtTotalTaxableValue.setText(String.format("%.2f",dblTotalTaxableValue));
                edtTotalAmount.setText(String.format("%.2f",dblTotalAmount));
            } else {
                Logger.e(TAG,"No data to calculate over all bill." +
                        "Function Name : mOverAllTotalBillCalculation().");
            }
            mPopulateItemsListAdapterData();
        }catch (Exception ex){
            Logger.e(TAG,"Unable able to calculate over all bill. " +
                    "Function Name : mOverAllTotalBillCalculation(). "
                    + ex.getMessage());
        }
    }




    private class PopulateDisplayItemsDataAsync extends AsyncTask<Integer,Void,List<ItemMasterBean>>{
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(myContext);
            pd.setTitle("Loading Data.Please wait.");
            pd.show();
        }

        @Override
        protected List<ItemMasterBean> doInBackground(Integer... integers) {
            int iMode = 1, iDept_CategCode = 0;
            Cursor cursor = null;
            if(integers != null){
                iMode = integers[0];
                if(integers.length > 1) {
                    if (integers[1] != null && integers[1] > 0) {
                        iDept_CategCode = integers[1];
                    }
                }
            }
            switch (iMode){
                case Constants.FAVOURITE:
                    cursor = HomeActivity.dbHandler.getAllItems_Fav();
                    break;
                case Constants.DEPARTMENT:
                    cursor = HomeActivity.dbHandler.getAllItems_Active_Departmentwise(iDept_CategCode);
                    break;
                case Constants.CATEGORY:
                    if(iDept_CategCode > 0) {
                        cursor = HomeActivity.dbHandler.getAllItems_Active_Category(iDept_CategCode);
                    } /*else {
                        cursor = HomeActivity.dbHandler.getAllItems_Categorywise();
                    }*/
                    break;
                default:
                    cursor = HomeActivity.dbHandler.getAllItems_ActiveItems();
                    break;
            }
            try{
                itemsDisplayLists.clear();
                if(cursor != null && cursor.getCount() > 0){
                    cursor.moveToFirst();
                    do{
                        itemMasterBean = new ItemMasterBean();
                        itemMasterBean.setStrShortName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemShortName)));
                        itemMasterBean.setStrHSNCode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_HSNCode)));
                        if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_UOM)) != null){
                            itemMasterBean.setStrUOM(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_UOM)));
                        }
                        if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ImageUri)) != null){
                            itemMasterBean.setStrImageUri(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ImageUri)));
                        }
                        itemMasterBean.set_id(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_id)));
                        if(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_BrandCode)) > 0) {
                            itemMasterBean.setiBrandCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_BrandCode)));
                        }
                        if(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)) > 0) {
                            itemMasterBean.setiDeptCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)));
                        }
                        if(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_CategoryCode)) > 0) {
                            itemMasterBean.setiCategoryCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_CategoryCode)));
                        }
                        if(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_BrandCode)) > 0) {
                            itemMasterBean.setiCategoryCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_BrandCode)));
                        }
                        if (cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)) != null && !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)).isEmpty()) {
                            itemMasterBean.setStrBarcode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)));
                        }
                        if (cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)) != null && !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)).isEmpty()) {
                            itemMasterBean.setStrSupplyType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)));
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)) > 0) {
                            itemMasterBean.setDblRetailPrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)));
                        } else {
                            itemMasterBean.setDblRetailPrice(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_MRP)) > 0) {
                            itemMasterBean.setDblMRP(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_MRP)));
                        } else {
                            itemMasterBean.setDblMRP(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)) > 0) {
                            itemMasterBean.setDblWholeSalePrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)));
                        } else {
                            itemMasterBean.setDblWholeSalePrice(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_CGSTRate)) > 0) {
                            itemMasterBean.setDblCGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_CGSTRate)));
                        } else {
                            itemMasterBean.setDblCGSTRate(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_SGSTRate)) > 0) {
                            itemMasterBean.setDblSGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_SGSTRate)));
                        } else {
                            itemMasterBean.setDblSGSTRate(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_IGSTRate)) > 0) {
                            itemMasterBean.setDblIGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_IGSTRate)));
                        } else {
                            itemMasterBean.setDblIGSTRate(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_cessRate)) > 0) {
                            itemMasterBean.setDblCessRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_cessRate)));
                        } else {
                            itemMasterBean.setDblCessRate(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountAmount)) > 0) {
                            itemMasterBean.setDblDiscountAmt(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountAmount)));
                        } else {
                            itemMasterBean.setDblDiscountAmt(0);
                        }
                        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountPercent)) > 0) {
                            itemMasterBean.setDbDiscountPer(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountPercent)));
                        } else {
                            itemMasterBean.setDbDiscountPer(0);
                        }
                        if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ImageUri)) != null){
                            itemMasterBean.setStrImageUri(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ImageUri)));
                        } else {
                            itemMasterBean.setStrImageUri("");
                        }
                        double qty = Double.parseDouble(String.format("%.2f",cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_Quantity))));
                        itemMasterBean.setDblQty(qty);
                        //itemMasterBean.setiMode(iMode);
                        //for(int i = 0 ; i < 100; i++) {
                        itemsDisplayLists.add(itemMasterBean);
                        //}
                    }while (cursor.moveToNext());
                }
            }catch (Exception ex){
                Logger.i(TAG,"Unable to populate display adapter data on billing screen. " +ex.getMessage());
            } finally {
                if(cursor != null){
                    cursor.close();
                }
            }
            return itemsDisplayLists;
        }

        @Override
        protected void onPostExecute(List<ItemMasterBean> itemMasterBeansList) {
            super.onPostExecute(itemMasterBeansList);
            if(itemsDisplayLists.size() == 0){
                Toast.makeText(myContext,"No data to display.",Toast.LENGTH_LONG).show();
            }
            GridLayoutManager manager = new GridLayoutManager(myContext, 4, GridLayoutManager.VERTICAL, false);
            rvFavList.setLayoutManager(manager);
            billingDisplayListAdapter = new BillingDisplayListAdapter(getActivity(),BillingFragment.this,itemsDisplayLists);
            rvFavList.setAdapter(billingDisplayListAdapter);
            if(populateDisplayItemsDataAsyncObject != null){
                populateDisplayItemsDataAsyncObject = null;
            }
            if(pd != null){
                pd.dismiss();
                pd = null;
            }
        }
    }

    private class PopulateDisplayDeptDataAsync extends AsyncTask<Integer,Void,List<Department>>{
        List<Department> departmentList = new ArrayList<Department>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Department> doInBackground(Integer... integers) {
            Cursor cursor = null;
            try{
                cursor = HomeActivity.dbHandler.getAllDepartments();
                if(cursor != null && cursor.getCount() > 0){
                    cursor.moveToFirst();
                    departmentList.clear();
                    do{
                        Department department = new Department();
                        department.setDeptCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)));
                        department.setDeptName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_DepartmentName)));
                        departmentList.add(department);
                    }while (cursor.moveToNext());
                }
            }catch (Exception ex){
                Logger.i(TAG,"Unable to populate display dept adapter data on billing screen. " +ex.getMessage());
            } finally {
                if(cursor != null){
                    cursor.close();
                }
            }
            return departmentList;
        }

        @Override
        protected void onPostExecute(List<Department> departmentList1) {
            super.onPostExecute(departmentList1);
            if (departmentList1.size() == 0) {
                Toast.makeText(myContext, "No data to display.", Toast.LENGTH_LONG).show();
            }
            GridLayoutManager manager = new GridLayoutManager(myContext, 4, GridLayoutManager.VERTICAL, false);
            rvFavList.setLayoutManager(manager);
            rvFavList.setAdapter(new BillingDeptListAdapter(getActivity(), BillingFragment.this, departmentList1));
            if (populateDisplayDeptDataAsyncObject != null) {
                populateDisplayDeptDataAsyncObject = null;
            }
        }
    }

    private class PopulateDisplayCategoryDataAsync extends AsyncTask<Integer,Void,List<Category>>{
        List<Category> categoryList = new ArrayList<Category>();
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<Category> doInBackground(Integer... integers) {
            Cursor cursor = null;
            try {
                int iDeptCode = -1;
                categoryList.clear();
                if (integers != null && integers.length >= 1) {
                    iDeptCode = integers[0];
                }
                if (iDeptCode > -1) {
                    cursor = HomeActivity.dbHandler.getCategoryByDept(iDeptCode);
                } else {
                    cursor = HomeActivity.dbHandler.getAllCategory();
                }
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        Category category = new Category();
                        category.setiCategCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_CategoryCode)));
                        category.setiDeptCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)));
                        category.setStrCategName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_CategoryName)));
                        categoryList.add(category);
                    } while (cursor.moveToNext());
                }

            } catch (Exception ex) {
                Logger.i(TAG, "Unable to populate display category adapter data on billing screen. " + ex.getMessage());
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return categoryList;
        }

        @Override
        protected void onPostExecute(List<Category> categoryListTemp) {
            super.onPostExecute(categoryListTemp);
            if(categoryListTemp.size() == 0) {
                Toast.makeText(myContext, "No data to display.", Toast.LENGTH_LONG).show();
            }
            GridLayoutManager manager = new GridLayoutManager(myContext, 4, GridLayoutManager.VERTICAL, false);
            rvFavList.setLayoutManager(manager);
            rvFavList.setAdapter(new BillingCategoryListAdapter(getActivity(),BillingFragment.this,categoryListTemp));
            if(populateDisplayCategoryDataAsyncObject != null){
                populateDisplayCategoryDataAsyncObject = null;
            }
        }
    }

    private void mPriceOptionChange(String strMode){
        try {
            if(billingItemsList.size() > 0) {
                for(BillItemBean billItemBean: billingItemsList) {
                    switch (strMode) {
                        case Constants.MRP:
                            billItemBean.setDblValue(billItemBean.getDblMRP());
                            break;
                        case Constants.RETAIL_PRICE:
                            billItemBean.setDblValue(billItemBean.getDblRetailPrice());
                            break;
                        case Constants.WHOLE_SALE_PRICE:
                            billItemBean.setDblValue(billItemBean.getDblWholeSalePrice());
                            break;
                        default:
                            break;
                    }
                }
                mAddedItemDiscountAndTaxes(-1, false);
            }else {
                Toast.makeText(myContext,"No item on the list to update. Please add item into list and try for update.",Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){
            Logger.i(TAG,"Unable to update the price option data mode : " + strMode + " Error : " +ex.getMessage());
        }
    }

    // -----Print Bill Code Started-----

    public void mSaveBILL(boolean isPrint, boolean isPayBill)
    {
        int proceed =1;
        if (billingItemsList.size() <= 0 )
        {
            msgBox.Show("Warning", "Add Item before Printing Bill");
            proceed =0;
        }else if (edtTotalAmount.getText().toString().equals("") ) {
            msgBox.Show("Warning", "Please add item to make bill");
            proceed =0;
        } else if ( edtTotalAmount.getText().toString().equals("0.00")) {
            msgBox.Show("Warning", "Please make bill of amount greater than 0.00");
            proceed =0;
        }else if (cbInterStateStatus.isChecked() && spInterState.getSelectedItem().equals("Select")) {
            msgBox.Show("Warning", "Please Select state for Interstate Supply");
            proceed =0;
        }else if((OWNERPOS.equals("") || OWNERPOS.equals("0")))
        {
            msgBox.Show(getString(R.string.invalid_attempt),getString(R.string.empty_owner_pos_message) );
            proceed =0;
        }
        else
        {
            String salesmanId = avSalesManId.getText().toString().trim();
            if(salesmanId != null && !salesmanId.equals(""))
            {
                Cursor cursor = HomeActivity.dbHandler.mGetSalesManIdSearchData(salesmanId);
                if(!(cursor !=null && cursor.moveToFirst()))
                {
                    msgBox.Show(getString(R.string.invalid_attempt),getString(R.string.invalid_salesmanId_message) );
                    proceed =0;
                }
            }
        }

        if(proceed == 0)
            return;
        if(!isPayBill)
        {
            dblCashPayment = Double.parseDouble(String.format("%.2f",Double.parseDouble(edtTotalAmount.getText().toString())));
        }
        if(!isPrint)
        {
            mSaveBillData(1);
            mClear();
        }
        else if (homeActivity.isPrinterAvailable) {
            strPaymentStatus = "Paid";
            PrintBillPayment = 1;
            // Print Bill with Save Bill
            if (billingItemsList.size() < 0) {
                msgBox.Show("Warning", "Insert item before Print Bill");
                return;
            } else {

//                if (BILL_AMOUNT_ROUND_OFF == 1) {
//                    String temp = edtTotalAmount.getText().toString().trim();
//                    double finalAmount = Double.parseDouble(edtTotalAmount.getText().toString().trim());
//                    double roundOffFinalAmount = 0;
//
//                    if (!temp.contains(".00")) {
//                        roundOffFinalAmount = Math.round(finalAmount);
//                        edtTotalAmount.setText(String.valueOf(roundOffFinalAmount));
//                        dblRoundOfValue = Float.parseFloat("0" + temp.substring(temp.indexOf(".")));
//                    }
//                }

                mSaveBillData(1);
                if (isPrint)
                    mPrintBill();
                //updateOutwardStock();
                //PrintNewBill();
                Toast.makeText(myContext, "Bill Saved Successfully", Toast.LENGTH_SHORT).show();
                mClear();
                /*if (jBillingMode == 2)
                {
                    ClearAll();
                    btn_PrintBill.setEnabled(true);
                }*/
            }
        }
        else
        {
            Toast.makeText(myContext, "Printer is not ready", Toast.LENGTH_SHORT).show();
            homeActivity.askForConfig();
        }
    }

    private String strPaymentStatus;
    private int PrintBillPayment = 0;

    void mPrintBill(){
        int proceed = 1;
        if (billingItemsList.size() < 1) {
            msgBox.Show("Warning", "Add Item before Printing Bill");
            proceed = 0;
        } else if (edtTotalAmount.getText().toString().equals("")) {
            msgBox.Show("Warning", "Please add item to make bill");
            proceed = 0;
        } else if (edtTotalAmount.getText().toString().equals("0.00")) {
            msgBox.Show("Warning", "Please make bill of amount greater than 0.00");
            proceed = 0;
        } else if (cbInterStateStatus.isChecked() && spInterState.getSelectedItem().equals("Select")) {
            msgBox.Show("Warning", "Please Select state for Interstate Supply");
            proceed = 0;
        }

        if (proceed == 0)
            return;
        if (true) {
            strPaymentStatus = "Paid";
            PrintBillPayment = 1;
            // Print Bill with Save Bill
            if (billingItemsList.size() < 1) {
                msgBox.Show("Warning", "Insert item before Print Bill");
                return;
            } else {

//                if (BILL_AMOUNT_ROUND_OFF == 1) {
//                    String temp = edtTotalAmount.getText().toString().trim();
//                    double finalAmount = Double.parseDouble(edtTotalAmount.getText().toString().trim());
//                    double roundOffFinalAmount = 0;
//
//                    if (!temp.contains(".00")){
//                        roundOffFinalAmount = Math.round(finalAmount);
//                        edtTotalAmount.setText(String.valueOf(roundOffFinalAmount));
//                        dblRoundOfValue = Float.parseFloat("0" + temp.substring(temp.indexOf(".")));
//                    }
//                }
                isReprint = false;
                if(tv_billing_CustId.getText().toString().equals(""))
                    customerId = "0";
                else
                    customerId = tv_billing_CustId.getText().toString();

                PrintNewBill(BUSINESS_DATE);
                Toast.makeText(myContext, "Bill Saved Successfully", Toast.LENGTH_SHORT).show();
//                if (jBillingMode == 2) {
                mClear();
                btnSavePrintBill.setEnabled(true);
//                }
            }
        } else {
            Toast.makeText(myContext, "Printer is not ready", Toast.LENGTH_SHORT).show();
//            askForConfig();
        }
    }

    private byte jBillingMode = 2;
    Calendar Time;
    boolean isReprint = false;

    public ArrayList<BillTaxItem> otherChargesPrint(String date_to_print) {
        ArrayList<BillTaxItem> billOtherChargesItems = new ArrayList<BillTaxItem>();
        if (isReprint) {
            try{
                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(date_to_print);
                Cursor crsrTax = HomeActivity.dbHandler.getDeliveryChargesDetails((edtBillNumber.getText().toString()), ""+date.getTime());
                while (crsrTax!=null && crsrTax.moveToNext()) {
                    String taxname = crsrTax.getString(crsrTax.getColumnIndex(DatabaseHandler.KEY_OTHER_CHARGES_DESCRIPTION));
                    double taxpercent = 0;
                    Double taxvalue = crsrTax.getDouble(crsrTax.getColumnIndex(DatabaseHandler.KEY_OTHER_CHARGES_AMOUNT));

                    if (taxvalue > 0) {
                        BillTaxItem taxItem = new BillTaxItem(taxname, (taxpercent), Double.parseDouble(String.format("%.2f", taxvalue)));
                        billOtherChargesItems.add(taxItem);
                    /*double totalamt = Double.parseDouble(tvBillAmount.getText().toString().trim());
                    totalamt+= taxvalue;*/
                        //tvBillAmount.setText(String.format("%.2f", totalamt));
                    }
                }

            }catch (Exception e)
            {
                Logger.e(TAG, ""+e);
            }
        } else {
            for(Map.Entry<String,String> entry : deliveryChargesMap.entrySet())
            {
                String chargeName = entry.getKey();
                double taxpercent = 0;
                Double chargeValue = Double.parseDouble(entry.getValue());

                if (chargeValue > 0) {
                    BillTaxItem taxItem = new BillTaxItem(chargeName, (taxpercent),
                            Double.parseDouble(String.format("%.2f", chargeValue)));
                    billOtherChargesItems.add(taxItem);
                }
            }
        }
        return billOtherChargesItems;
    }



    protected void PrintNewBill(String BUSINESS_DATE) {
        if (homeActivity.isPrinterAvailable) {
            if (billingItemsList.size() < 1) {
                msgBox.Show("Warning", "Insert item before Print Bill");
                return;
            } else {
                int orderId = 0;
                if ((!edtBillNumber.getText().toString().trim().equalsIgnoreCase(""))) {
                    orderId = Integer.parseInt(edtBillNumber.getText().toString().trim());
                    ArrayList<BillTaxItem> billTaxItems;
                    ArrayList<BillServiceTaxItem> billServiceTaxItems = new ArrayList<BillServiceTaxItem>();
                    ArrayList<BillTaxItem> billOtherChargesItems = otherChargesPrint(BUSINESS_DATE);
                    ArrayList<BillServiceTaxItem> billcessTaxItems = new ArrayList<BillServiceTaxItem>();
                    ArrayList<BillTaxSlab> billTaxSlabs = new ArrayList<BillTaxSlab>();
                    ArrayList<BillKotItem> billKotItems = new ArrayList<>();

                    PrintKotBillItem item = new PrintKotBillItem();

                    Cursor crsrCustomer = null;
                    try {
                        crsrCustomer = HomeActivity.dbHandler.getCustomer(Integer.parseInt(customerId));
                        if (crsrCustomer.moveToFirst()) {
                            item.setCustomerName(crsrCustomer.getString(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_CustName)));
                        } else {
                            item.setCustomerName(" - - - ");
                        }
                    }catch (Exception ex){
                        Logger.e(TAG, "Unable to fetch data from the customer table on PrintNewBill()." +ex.getMessage());
                    }finally {
                        if(crsrCustomer != null){
                            crsrCustomer.close();
                        }
                    }

                    if (cbInterStateStatus.isChecked()) {
                        item.setIsInterState("y");
                        billTaxSlabs = TaxSlabPrint_InterState();
                    } else {
                        item.setIsInterState("n");
                        billTaxSlabs = TaxSlabPrint_IntraState();
                    }

                    item.setPrintService(PRINTSERVICE);
                    item.setBoldHeader(BOLD_HEADER);
                    item.setOwnerDetail(PRINT_OWNER_DETAIL);
                    if (isReprint)
                        item.setIsDuplicate("(Duplicate Bill)");
                    else
                        item.setIsDuplicate("");

                    billcessTaxItems = cessTaxPrint();
                    billKotItems = billPrint(billTaxSlabs);
                    item.setBillKotItems(billKotItems);
                    item.setAmountInNextLine(0);
                    item.setBillOtherChargesItems(billOtherChargesItems);
                    item.setBillTaxSlabs(billTaxSlabs);
                    item.setBillcessTaxItems(billcessTaxItems);
                    item.setSubTotal(Double.parseDouble(edtTotalTaxableValue.getText().toString().trim()));
                    item.setNetTotal(Double.parseDouble(edtTotalAmount.getText().toString().trim()));
//                    item.setWaiterNo(waiterId);
                    if (SALES_MAN_ID == 1 && (!avSalesManId.getText().toString().isEmpty()) )
                    {
                        item.setSalesManId(avSalesManId.getText().toString().trim());
                    }
                    else {
                        item.setSalesManId("");
                    }
                    item.setUTGSTEnabled(UTGSTENABLED);
                    item.setHSNPrintEnabled_out(HSNPRINTENABLED);
                    String billNoPrefix = HomeActivity.dbHandler.getBillNoPrefix();
                    item.setBillNo(billNoPrefix + String.valueOf(orderId));
                    item.setOrderBy(userName);
                    item.setBillingMode(String.valueOf(jBillingMode));
                    if (strPaymentStatus.equalsIgnoreCase("")) {
                        item.setPaymentStatus("");
                    } else {
                        item.setPaymentStatus(strPaymentStatus);
                    }
                    item.setdiscountPercentage(Float.parseFloat(tv_billing_discount_percent.getText().toString()));
                    //item.setdiscountPercentage(0);
                    //if(ItemwiseDiscountEnabled ==1)
                    calculateDiscountAmount();
                    if(PRINT_DISCOUNT ==1)
                        item.setFdiscount(dTotalDiscount);
                    else
                        item.setFdiscount(0.00);
                    Log.d("Discount :", String.valueOf(dTotalDiscount));
                    item.setTotalsubTaxPercent(fTotalsubTaxPercent);
                    item.setTotalSalesTaxAmount(String.valueOf(Double.parseDouble(edtTotalCGST.getText().toString().trim()))+ Double.parseDouble(edtTotalCGST.getText().toString().trim()));
                    item.setTotalServiceTaxAmount(String.valueOf(Double.parseDouble(edtTotalIGST.getText().toString().trim())));
                    item.setRoundOff(Double.parseDouble(edtRoundOff.getText().toString()));
                    Time = Calendar.getInstance();
                    String strTime = new SimpleDateFormat("kk:mm:ss").format(Time.getTime());
                    item.setTime(strTime);

                    item.setStrBillingModeName("Billing");
                    item.setDate(BUSINESS_DATE);


                    String date_today = edtInvoiceDate.getText().toString();
                    //Log.d("Date ", date_today);
                    Cursor paymentModeinBillcrsr = null;
                    try {
                        Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date_today);
                        paymentModeinBillcrsr = HomeActivity.dbHandler.getBillDetail((orderId), String.valueOf(date1.getTime()));
                        if (paymentModeinBillcrsr != null && paymentModeinBillcrsr.moveToFirst()) {
                            double cardValue = 0.00, eWalletValue = 0.00, couponValue = 0.00, pettyCashValue = 0.00, cashValue = 0.00, changeValue = 0.00,
                                    rewardValue = 0.00, aepsAmount=0, dblMSwipeValue = 0.00;

                            if (paymentModeinBillcrsr.getString(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_CardPayment)) != null)
                                cardValue = paymentModeinBillcrsr.getDouble(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_CardPayment));
                            if (paymentModeinBillcrsr.getString(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_WalletPayment)) != null)
                                eWalletValue = paymentModeinBillcrsr.getDouble(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_WalletPayment));
                            if (paymentModeinBillcrsr.getString(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_CouponPayment)) != null)
                                couponValue = paymentModeinBillcrsr.getDouble(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_CouponPayment));
                            if (paymentModeinBillcrsr.getString(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_PettyCashPayment)) != null)
                                pettyCashValue = paymentModeinBillcrsr.getDouble(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_PettyCashPayment));
                            if (paymentModeinBillcrsr.getString(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_CashPayment)) != null)
                                cashValue = paymentModeinBillcrsr.getDouble(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_CashPayment));
                            if (paymentModeinBillcrsr.getString(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_ChangePayment)) != null)
                                changeValue = paymentModeinBillcrsr.getDouble(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_ChangePayment));
                            if (paymentModeinBillcrsr.getString(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_RewardPointsAmount)) != null)
                                rewardValue = paymentModeinBillcrsr.getDouble(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_RewardPointsAmount));
                            if (paymentModeinBillcrsr.getString(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_AEPSAmount)) != null)
                                aepsAmount = paymentModeinBillcrsr.getDouble(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_AEPSAmount));
                            if (paymentModeinBillcrsr.getString(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_MSWIPE_Amount)) != null)
                                dblMSwipeValue = paymentModeinBillcrsr.getDouble(paymentModeinBillcrsr.getColumnIndex(DatabaseHandler.KEY_MSWIPE_Amount));


                            cardValue = Double.parseDouble(String.format("%.2f", cardValue));
                            eWalletValue = Double.parseDouble(String.format("%.2f", eWalletValue));
                            couponValue = Double.parseDouble(String.format("%.2f", couponValue));
                            pettyCashValue = Double.parseDouble(String.format("%.2f", pettyCashValue));
                            cashValue = Double.parseDouble(String.format("%.2f", cashValue));
                            changeValue = Double.parseDouble(String.format("%.2f", changeValue));
                            rewardValue = Double.parseDouble(String.format("%.2f", rewardValue));
                            aepsAmount = Double.parseDouble(String.format("%.2f", aepsAmount));
                            dblMSwipeValue = Double.parseDouble(String.format("%.2f", dblMSwipeValue));

                            item.setCardPaymentValue(cardValue);
                            item.seteWalletPaymentValue(eWalletValue);
                            item.setCouponPaymentValue(couponValue);
                            item.setPettyCashPaymentValue(pettyCashValue);
                            item.setCashPaymentValue(cashValue);
                            item.setChangePaymentValue(changeValue);
                            item.setRewardPoints(rewardValue);
                            item.setAepsPaymentValue(aepsAmount);
                            item.setDblMSwipeVale(dblMSwipeValue);
                        }
                    }catch (Exception e)
                    {
                        Logger.e(TAG, "error on fetching different mode of payment values on method PrintNewBill()." +e.getMessage());
                    } finally {
                        if(paymentModeinBillcrsr != null){
                            paymentModeinBillcrsr.close();
                        }
                    }

                    if (reprintBillingMode == 0) {


                    } else {
                        Cursor c = null;
                        try {
//                            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(getBusinessDate());
                            c = HomeActivity.dbHandler.getBillDetail(orderId, BUSINESS_DATE);
                            if (c != null && c.moveToNext()) {

                                String time = c.getString(c.getColumnIndex("Time"));
                                item.setTime(time);
                                item.setDate(BUSINESS_DATE); // using current date for now
                                float round = c.getString(c.getColumnIndex("RoundOff")) == (null) ? 0 : c.getFloat(c.getColumnIndex("RoundOff"));
                                item.setRoundOff(Float.parseFloat(String.format("%.2f", round)));

                                if (reprintBillingMode == 1) {
                                    String tableNo = c.getString(c.getColumnIndex("TableNo"));
                                    String splitno = c.getString(c.getColumnIndex("TableSplitNo"));
                                    if (splitno != null && !splitno.equals(""))
                                        item.setTableNo(tableNo + " - " + splitno);
                                    else
                                        item.setTableNo(tableNo);
                                }
                                String userId = c.getString(c.getColumnIndex("UserId"));
                                if (reprintBillingMode != 0 && userId != null) {
                                    Cursor user_cursor = null;
                                    try {
                                        user_cursor = HomeActivity.dbHandler.getUsers_counter(userId);
                                        if (user_cursor != null && user_cursor.moveToFirst()) {
                                            item.setOrderBy(user_cursor.getString(user_cursor.getColumnIndex("Name")));
                                        }
                                    }catch (Exception ex){
                                        Logger.i(TAG,"Unable to fetch data from getUserCounter() method." +ex.getMessage());
                                    } finally {
                                        if(user_cursor != null){
                                            user_cursor.close();
                                        }
                                    }
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if(c != null){
                                c.close();
                            }
                        }
                    }
//
                    String prf = Preferences.getSharedPreferencesForPrint(getActivity()).getString("bill", "--Select--");
                /*Intent intent = new Intent(getApplicationContext(), PrinterSohamsaActivity.class);*/
                    Intent intent = null;
                    if (prf.equalsIgnoreCase("Sohamsa")) {
                        String[] tokens = new String[3];
                        tokens[0] = "";
                        tokens[1] = "";
                        tokens[2] = "";

                        //printSohamsaBILL(item, "BILL");
                    } else if (prf.equalsIgnoreCase("Heyday")) {
                        String[] tokens = new String[3];
                        tokens[0] = "";
                        tokens[1] = "";
                        tokens[2] = "";

                        Cursor crsrOwnerDetails = null;
                        try {
                            crsrOwnerDetails = HomeActivity.dbHandler.getOwnerDetail();

                            if (crsrOwnerDetails.moveToFirst()) {
                                try {
                                    tokens[0] = crsrOwnerDetails.getString(crsrOwnerDetails.getColumnIndex(DatabaseHandler.KEY_GSTIN));
                                    tokens[1] = crsrOwnerDetails.getString(crsrOwnerDetails.getColumnIndex(DatabaseHandler.KEY_FIRM_NAME));
                                    tokens[2] = crsrOwnerDetails.getString(crsrOwnerDetails.getColumnIndex(DatabaseHandler.KEY_Address));
                                } catch (Exception e) {
                                    tokens[0] = "";
                                    tokens[1] = "";
                                    tokens[2] = "";
                                }
                                if (!tokens[0].equalsIgnoreCase(""))
                                    item.setAddressLine1(tokens[0]);
                                if (!tokens[1].equalsIgnoreCase(""))
                                    item.setAddressLine2(tokens[1]);

                                if (cbInterStateStatus.isChecked()) {
                                    item.setCustomerName(item.getCustomerName() + "  (" + (spInterState.getSelectedItem().toString()) + ") ");
                                    tokens[2] = tokens[2] + "\n (" + getState_pos(HomeActivity.dbHandler.getOwnerPOS()) + ") ";
                                }
                                item.setAddressLine3(tokens[2]);
                            } else {
                                Log.d(TAG, "Display Owner Details No data in BillSettings table");
                            }
                        } catch (Exception ex){
                            Logger.e(TAG,"Unable to fetch data from owner details data from table." +ex.getMessage());
                        } finally {
                            if(crsrOwnerDetails != null){
                                crsrOwnerDetails.close();
                            }
                        }
                        Cursor crsrHeaderFooterSetting = null;
                        try {
                            crsrHeaderFooterSetting = HomeActivity.dbHandler.getBillSettings();

                            if (crsrHeaderFooterSetting.moveToFirst()) {
                                item.setHeaderLine1(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("HeaderText1")));
                                item.setHeaderLine2(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("HeaderText2")));
                                item.setHeaderLine3(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("HeaderText3")));
                                item.setHeaderLine4(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("HeaderText4")));
                                item.setHeaderLine5(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("HeaderText5")));
                                item.setFooterLine1(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("FooterText1")));
                                item.setFooterLine2(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("FooterText2")));
                                item.setFooterLine3(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("FooterText3")));
                                item.setFooterLine4(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("FooterText4")));
                                item.setFooterLine5(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("FooterText5")));
                            } else {
                                Log.d(TAG, "DisplayHeaderFooterSettings No data in BillSettings table");
                            }
                        }catch (Exception ex){
                            Logger.e(TAG,"Unable to fetch header details from billSettings table. From method PrintNewBill()." +ex.getMessage());
                        } finally {
                            if(crsrHeaderFooterSetting != null){
                                crsrHeaderFooterSetting.close();
                            }
                        }
                        //startActivity(intent);
                        if (true) {
                            homeActivity.printHeydeyBILL(item, "BILL");
                        } else {
//                            askForConfig();
                        }
                    } else {
                        Toast.makeText(myContext, "Printer not configured. Kindly goto settings and configure printer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(myContext, "Please Enter Bill, Waiter, Table Number", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(myContext, "Printer is not ready", Toast.LENGTH_SHORT).show();
//            askForConfig();
        }
    }

    private String getState_pos(String substring) {

        String index = "";
        for (int i = 0; i < spInterState.getCount(); i++) {

            if (spInterState.getItemAtPosition(i).toString().contains(substring)) {
                index = spInterState.getItemAtPosition(i).toString();
                break;
            }

        }

        return index;

    }

    int AMOUNTPRINTINNEXTLINE = 0;

    public ArrayList<BillKotItem> billPrint(ArrayList<BillTaxSlab> billTaxSlabs) {
        ArrayList<BillKotItem> billKotItems = new ArrayList<BillKotItem>();
        int count = 1;
        AMOUNTPRINTINNEXTLINE = 0;
        for (int iRow = 0; iRow < billingItemsList.size(); iRow++) {

            BillItemBean billItemBean = billingItemsList.get(iRow);

            int id = billItemBean.getiID();
            int sno = count;
            String name = billItemBean.getStrItemName();
            String hsncode = billItemBean.getStrHSNCode();
            String UOM = billItemBean.getStrUOM();
            Double qty = billItemBean.getDblQty();
            double rate = billItemBean.getDblValue();
//            double originalRate = Double.parseDouble(OriginalRate_tv.getText().toString().trim().equals("") ? "0" : OriginalRate_tv.getText().toString().trim());
            double originalRate = billItemBean.getDblOriginalRate();
            double amount = 0;
            if(isForwardTaxEnabled ==0)
                amount = originalRate *qty;
            else
                amount = Double.parseDouble(String.format("%.2f",billItemBean.getDblValue() * qty));

            //amount = originalRate * qty;
            if (String.format("%.2f", amount).length() > 8)
                AMOUNTPRINTINNEXTLINE = 1;
            String taxIndex = " ";
            double TaxRate = 0;
            if (cbInterStateStatus.isChecked())
                TaxRate = billItemBean.getDblIGSTRate();
            else
                TaxRate = billItemBean.getDblCGSTRate() + billItemBean.getDblSGSTRate();


            for (BillTaxSlab taxEntry : billTaxSlabs) {
                if (String.format("%.2f", TaxRate).equals(String.format("%.2f", taxEntry.getTaxRate()))) {
                    taxIndex = taxEntry.getTaxIndex();
                    break;
                }
            }
            BillKotItem billKotItem = new BillKotItem(sno, name, qty, rate, amount, hsncode, UOM, taxIndex);
            billKotItems.add(billKotItem);
            count++;

        }
        return billKotItems;
    }

    double dTotalDiscount = 0;

    void calculateDiscountAmount() {
        dTotalDiscount = 0;
        for (int i = 0; i < billingItemsList.size(); i++) {
            BillItemBean billItemBean = billingItemsList.get(i);
//            TextView discountAmt = (TextView) row.getChildAt(9);
            dTotalDiscount += billItemBean.getDblDiscountAmount();
//            if (edtTotalDiscount.getText().toString() != null && !edtTotalDiscount.getText().toString().equals(""))
//                dTotalDiscount += Double.parseDouble(edtTotalDiscount.getText().toString());
        }
    }

    public ArrayList<BillServiceTaxItem> cessTaxPrint() {
        ArrayList<BillServiceTaxItem> billcessTaxItems = new ArrayList<BillServiceTaxItem>();
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(BUSINESS_DATE);
            Cursor crsrTax = HomeActivity.dbHandler.getItemsForcessTaxPrints(Integer.valueOf(edtBillNumber.getText().toString()), ""+date.getTime());
            if (crsrTax.moveToFirst()) {
                do {
                    String taxname = "cess "; //crsrTax.getString(crsrTax.getColumnIndex("TaxDescription"));
                    String taxpercent = String.format("%.2f", crsrTax.getDouble(crsrTax.getColumnIndex("cessRate")));
                    Double taxvalue = Double.parseDouble(String.format("%.2f", crsrTax.getDouble(crsrTax.getColumnIndex("cessAmount"))));

                    BillServiceTaxItem taxItem = new BillServiceTaxItem(taxname, Double.parseDouble(taxpercent), Double.parseDouble(String.format("%.2f", taxvalue)));
                    billcessTaxItems.add(taxItem);
                } while (crsrTax.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            billcessTaxItems = new ArrayList<BillServiceTaxItem>();
        } finally {
            return billcessTaxItems;
        }

    }

    public ArrayList<BillTaxSlab> TaxSlabPrint_InterState() {
        ArrayList<BillTaxSlab> billTaxSlabs = new ArrayList<BillTaxSlab>();

        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(BUSINESS_DATE);
            Cursor crsrTax = HomeActivity.dbHandler.getItemsForTaxSlabPrints(Integer.valueOf(edtBillNumber.getText().toString()), ""+date.getTime());
            int count = 0;
            //System.out.println(crsrTax.getCount());
            if (crsrTax.moveToFirst()) {
                do {
                    Double taxpercent = Double.parseDouble(String.format("%.2f", crsrTax.getDouble(crsrTax.getColumnIndex("IGSTRate"))));
                    Double igstamt = Double.parseDouble(String.format("%.2f", crsrTax.getDouble(crsrTax.getColumnIndex("IGSTAmount"))));
                    Double taxableValue = Double.parseDouble(String.format("%.2f", crsrTax.getDouble(crsrTax.getColumnIndex("TaxableValue"))));

                    if (taxpercent == 0)
                        continue;

                    BillTaxSlab taxItem = new BillTaxSlab("", taxpercent, igstamt, 0.00, 0.00, taxableValue, igstamt);
                    int found = 0;
                    for (BillTaxSlab taxSlabItem : billTaxSlabs) {
                        if (taxSlabItem.getTaxRate() == taxpercent) {
                            taxSlabItem.setIGSTAmount(taxSlabItem.getIGSTAmount() + igstamt);
                            taxSlabItem.setTaxableValue(taxSlabItem.getTaxableValue() + taxableValue);
                            taxSlabItem.setTotalTaxAmount(taxSlabItem.getTotalTaxAmount() + igstamt);
                            found = 1;
                            break;
                        }
                    }
                    if (found == 0) {
                        taxItem.setTaxIndex(Character.toString((char) ('A' + count)));
                        count++;
                        billTaxSlabs.add(taxItem);
                    }

                } while (crsrTax.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            billTaxSlabs = new ArrayList<BillTaxSlab>();
        } finally {
            return billTaxSlabs;
        }

    }

    public ArrayList<BillTaxSlab> TaxSlabPrint_IntraState() {
        ArrayList<BillTaxSlab> billTaxSlabs = new ArrayList<BillTaxSlab>();

        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(BUSINESS_DATE);
            Cursor crsrTax = HomeActivity.dbHandler.getItemsForTaxSlabPrints(Integer.valueOf(edtBillNumber.getText().toString()), ""+date.getTime());
            int count = 0;
            //System.out.println(crsrTax.getCount());
            if (crsrTax.moveToFirst()) {
                do {
                    Double taxpercent = Double.parseDouble(String.format("%.2f", (crsrTax.getDouble(crsrTax.getColumnIndex("CGSTRate")) +
                            crsrTax.getDouble(crsrTax.getColumnIndex("SGSTRate")))));

                    Double cgstamt = Double.parseDouble(String.format("%.2f",
                            crsrTax.getDouble(crsrTax.getColumnIndex("CGSTAmount"))));
                    Double sgstamt = Double.parseDouble(String.format("%.2f",
                            crsrTax.getDouble(crsrTax.getColumnIndex("SGSTAmount"))));
                    Double taxableValue = Double.parseDouble(String.format("%.2f",
                            crsrTax.getDouble(crsrTax.getColumnIndex("TaxableValue"))));
                    //String cc = crsrTax.getString(crsrTax.getColumnIndex("TaxableValue"));

                    if (taxpercent == 0)
                        continue;
                    BillTaxSlab taxItem = new BillTaxSlab("", taxpercent, 0.00, cgstamt, sgstamt, taxableValue, cgstamt + sgstamt);

                    int found = 0;
                    for (BillTaxSlab taxSlabItem : billTaxSlabs) {
                        if (taxSlabItem.getTaxRate() == taxpercent) {
                            taxSlabItem.setCGSTAmount(taxSlabItem.getCGSTAmount() + cgstamt);
                            taxSlabItem.setSGSTAmount(taxSlabItem.getSGSTAmount() + sgstamt);
                            taxSlabItem.setTaxableValue(taxSlabItem.getTaxableValue() + taxableValue);
                            taxSlabItem.setTotalTaxAmount(taxSlabItem.getTotalTaxAmount() + cgstamt + sgstamt);
                            found = 1;
                            break;
                        }
                    }
                    if (found == 0) {
                        taxItem.setTaxIndex(Character.toString((char) ('A' + count)));
                        count++;
                        billTaxSlabs.add(taxItem);
                    }

                } while (crsrTax.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            billTaxSlabs = new ArrayList<BillTaxSlab>();
        } finally {
            return billTaxSlabs;
        }

    }


    /*void saveOtherChargesForBill()
    {
        String Invoiceno = edtBillNumber.getText().toString();
        Date date1 = new Date();
        try{
            date1 = new SimpleDateFormat("dd-MM-yyyy").parse(BUSINESS_DATE);
        }catch (Exception e)
        {
            Logger.e(TAG,""+e);
            Log.e(TAG, ""+e);

        }
        String InvoiceDate = ""+date1.getTime();
        for(Map.Entry<String,String>entry : deliveryChargesMap.entrySet())
        {
            String chargeName = entry.getKey();
            String chargeValue = entry.getValue();
            Logger.d("saveOtherChargesForBill : ", "InvoiceNo :"+Invoiceno);
            Logger.d("saveOtherChargesForBill : ", "InvoiceDate :"+ InvoiceDate);
            Logger.d("saveOtherChargesForBill : ", "ChargeName :"+chargeName);
            Logger.d("saveOtherChargesForBill : ", "ChargeAmount :"+chargeValue);
            long lResult = HomeActivity.dbHandler.insertOtherChargesForBill(Invoiceno, InvoiceDate,chargeName,chargeValue);
        }

    }
*/
    //start bill save
    private void mSaveBillData(int TenderType) { // TenderType:
        long result1 =0;
        long result = InsertBillItems();
        if(result >0)
        {
            result1 = InsertBillDetail();
//            if(result1 > 0)
//            {
//                saveOtherChargesForBill();
//            }

        }

    }

    /*************************************************************************************************************************************
     * Insert each bill item to bill items database table
     *************************************************************************************************************************************/
    private long InsertBillItems() {

        // Inserted Row Id in database table
        long lResult = 0;
        int result = 1;
        for (BillItemBean billItemBean : billingItemsList) {

            billItemBean.setStrInvoiceNo(edtBillNumber.getText().toString());
            //Log.d("InsertBillItem : ", "Invoice No :"+billItemBean.getStrInvoiceNo());
            Logger.d("InsertBillItem : ", "Invoice No :"+billItemBean.getStrInvoiceNo());

            Date date1 = new Date();
            try{
                date1 = new SimpleDateFormat("dd-MM-yyyy").parse(BUSINESS_DATE);
            }catch (Exception e)
            {
                Logger.e(TAG,""+e);
                Log.e(TAG, ""+e);

            }
            billItemBean.setStrInvoiceDate(""+date1.getTime());
            //Log.d("InsertBillItem : ", "Invoice Date "+BUSINESS_DATE+" - "+billItemBean.getStrInvoiceDate());
            Logger.d("InsertBillItem : ", "Invoice Date "+billItemBean.getStrInvoiceDate());


            //Log.d("InsertBillItem : ", "ItemId :"+billItemBean.getiItemId());
            Logger.d("InsertBillItem : ", "ItemId :"+billItemBean.getiItemId());

            //Log.d("InsertBillItem : ", "ItemName :"+billItemBean.getStrItemName());
            Logger.d("InsertBillItem : ", "ItemName :"+billItemBean.getStrItemName());

            //Log.d("InsertBillItem : ", "ItemBarcode :"+billItemBean.getStrBarcode());
            Logger.d("InsertBillItem : ", "ItemBarcode :"+billItemBean.getStrBarcode());

            double mrp  = billItemBean.getDblMRP();
            billItemBean.setDblMRP(Double.parseDouble(String.format("%.2f",mrp)));
            //Log.d("InsertBillItem : ", "MRP :"+billItemBean.getDblMRP());
            Logger.d("InsertBillItem : ", "MRP :"+billItemBean.getDblMRP());

            double retail = billItemBean.getDblRetailPrice();
            billItemBean.setDblRetailPrice(Double.parseDouble(String.format("%.2f",retail)));
            //Log.d("InsertBillItem : ", "RetailPrice :"+billItemBean.getDblRetailPrice());
            Logger.d("InsertBillItem : ", "RetailPrice :"+billItemBean.getDblRetailPrice());

            double wholesal= billItemBean.getDblWholeSalePrice();
            billItemBean.setDblWholeSalePrice(Double.parseDouble(String.format("%.2f",wholesal)));
            //Log.d("InsertBillItem : ", "WholeSalePrice :"+billItemBean.getDblWholeSalePrice());
            Logger.d("InsertBillItem : ", "WholeSalePrice :"+billItemBean.getDblWholeSalePrice());

            // Quantity
            double qty_d = 0.00;
            qty_d = billItemBean.getDblQty();
            billItemBean.setDblQty(Double.parseDouble(String.format("%.2f",qty_d)));
            //Log.d("InsertBillItem : ", "Quantity :"+billItemBean.getDblQty());
            Logger.d("InsertBillItem : ", "Quantity :"+billItemBean.getDblQty());

            // Rate
            double rate_d = 0.00;
            rate_d = billItemBean.getDblValue();
            billItemBean.setDblValue(Double.parseDouble(String.format("%.2f",rate_d)));
            //Log.d("InsertBillItem : ", "Rate :"+billItemBean.getDblValue());
            Logger.d("InsertBillItem : ", "Rate :"+billItemBean.getDblValue());


            double originalRate = 0.00;
            originalRate = billItemBean.getDblOriginalRate();
            billItemBean.setDblOriginalRate(Double.parseDouble(String.format("%.2f",originalRate)));
            //Log.d("InsertBillItem : ", "Original Rate :"+billItemBean.getDblOriginalRate());
            Logger.d("InsertBillItem : ", "Original Rate :"+billItemBean.getDblOriginalRate());

            if (isForwardTaxEnabled == 0) { // reverse tax
                billItemBean.setStrIsReverseTaxEnabled("YES");
            }else
            {
                billItemBean.setStrIsReverseTaxEnabled("NO");
            }
            billItemBean.setiTaxType(isForwardTaxEnabled);
            //Log.d("InsertBillItem : ", "TaxType :"+billItemBean.getiTaxType());
            Logger.d("InsertBillItem : ", "TaxType :"+billItemBean.getiTaxType());
            //Log.d("InsertBillItem : ", "isReverseTaxEnabled :"+billItemBean.getStrIsReverseTaxEnabled());
            Logger.d("InsertBillItem : ", "isReverseTaxEnabled :"+billItemBean.getStrIsReverseTaxEnabled());


            double taxVal =  billItemBean.getDblTaxbleValue();
            billItemBean.setDblTaxbleValue(Double.parseDouble(String.format("%.2f",taxVal)));
            //Log.d("InsertBillItem : ", "TaxableValue :"+billItemBean.getDblTaxbleValue());
            Logger.d("InsertBillItem : ", "TaxableValue :"+billItemBean.getDblTaxbleValue());


            // Discount %
            double discPer = 0.00;
            discPer = billItemBean.getDblDiscountPercent();
            billItemBean.setDblDiscountPercent(Double.parseDouble(String.format("%.2f",discPer)));
            //Log.d("InsertBillItem : ", "Discount% :"+billItemBean.getDblDiscountPercent());
            Logger.d("InsertBillItem : ", "Discount% :"+billItemBean.getDblDiscountPercent());

            // Discount Amount
            double discAmt = 0.00;
            discAmt = billItemBean.getDblDiscountAmount();
            billItemBean.setDblDiscountAmount(Double.parseDouble(String.format("%.2f",discAmt)));
            //Log.d("InsertBillItem : ", "DiscountAmount :"+billItemBean.getDblDiscountAmount());
            Logger.d("InsertBillItem : ", "DiscountAmount :"+billItemBean.getDblDiscountAmount());

            // Service Tax Percent
            double sgstRate = billItemBean.getDblSGSTRate();
            double cgstRate = billItemBean.getDblCGSTRate();
            double igstRate = billItemBean.getDblIGSTRate();

            double cgstAmount = billItemBean.getDblCGSTAmount();
            double sgstAmount = billItemBean.getDblSGSTAmount();
            double igstAmount = billItemBean.getDblIGSTAmount();

            if (cbInterStateStatus.isChecked()) {
                billItemBean.setDblCGSTRate(0.00);
                billItemBean.setDblSGSTRate(0.00);
                billItemBean.setDblCGSTAmount(0.00);
                billItemBean.setDblSGSTAmount(0.00);
                billItemBean.setDblIGSTRate(Double.parseDouble(String.format("%.2f",igstRate)));
                billItemBean.setDblIGSTAmount(Double.parseDouble(String.format("%.2f",igstAmount)));
            }
            else
            {
                billItemBean.setDblCGSTRate(Double.parseDouble(String.format("%.2f",cgstRate)));
                billItemBean.setDblSGSTRate(Double.parseDouble(String.format("%.2f",sgstRate)));
                billItemBean.setDblCGSTAmount(Double.parseDouble(String.format("%.2f",cgstAmount)));
                billItemBean.setDblSGSTAmount(Double.parseDouble(String.format("%.2f",sgstAmount)));
                billItemBean.setDblIGSTRate(0.00);
                billItemBean.setDblIGSTAmount(0.00);
            }

            //Log.d("InsertBillItem : ", "CGSTRate :"+billItemBean.getDblCGSTRate());
            Logger.d("InsertBillItem : ", "CGSTRate :"+billItemBean.getDblCGSTRate());
            //Log.d("InsertBillItem : ", "CGSTAmount :"+billItemBean.getDblCGSTAmount());
            Logger.d("InsertBillItem : ", "CGSTAmount :"+billItemBean.getDblCGSTAmount());

            //Log.d("InsertBillItem : ", "SGSTRate :"+billItemBean.getDblSGSTRate());
            Logger.d("InsertBillItem : ", "SGSTRate :"+billItemBean.getDblSGSTRate());
            //Log.d("InsertBillItem : ", "SGSTAmount :"+billItemBean.getDblSGSTAmount());
            Logger.d("InsertBillItem : ", "SGSTAmount :"+billItemBean.getDblSGSTAmount());

            //Log.d("InsertBillItem : ", "IGSTRate :"+billItemBean.getDblIGSTRate());
            Logger.d("InsertBillItem : ", "IGSTRate :"+billItemBean.getDblIGSTRate());
            //Log.d("InsertBillItem : ", "IGSTAmount :"+billItemBean.getDblIGSTAmount());
            Logger.d("InsertBillItem : ", "IGSTAmount :"+billItemBean.getDblIGSTAmount());




            // cess Tax %
            double cessRate = billItemBean.getDblCessRate();
            billItemBean.setDblCessRate(Double.parseDouble(String.format("%.2f",cessRate)));
            //Log.d("InsertBillItem : ", "cessRate :"+billItemBean.getDblCessRate());
            Logger.d("InsertBillItem : ", "cessRate :"+billItemBean.getDblCessRate());

            // cessTax Amount
            double cessAmount =  billItemBean.getDblCessAmount();
            billItemBean.setDblCessAmount(Double.parseDouble(String.format("%.2f",cessAmount)));
            //Log.d("InsertBillItem : ", "cessAmount :"+billItemBean.getDblCessAmount());
            Logger.d("InsertBillItem : ", "cessAmount :"+billItemBean.getDblCessAmount());


            // Amount
            double amount = 0.00;
            amount = billItemBean.getDblAmount();
            billItemBean.setDblAmount(Double.parseDouble(String.format("%.2f",amount)));
            //Log.d("InsertBillItem : ", "Amount :"+billItemBean.getDblAmount());
            Logger.d("InsertBillItem : ", "Amount  :"+billItemBean.getDblAmount());


            if(!avCustomerSearch.getText().toString().isEmpty()){
                // cust info
                billItemBean.setStrCustName(avCustomerSearch.getText().toString());
                billItemBean.setStrCustId(tv_billing_CustId.getText().toString());
                billItemBean.setStrGSTIN(edtCustomerGSTIN.getText().toString());
            }

            //Log.d("InsertBillItem : ", "CustomerName :"+billItemBean.getStrCustName());
            Logger.d("InsertBillItem : ", "CustomerName  :"+billItemBean.getStrCustName());
            //Log.d("InsertBillItem : ", "CustomerId :"+billItemBean.getStrCustId());
            Logger.d("InsertBillItem : ", "CustomerId  :"+billItemBean.getStrCustId());
            //Log.d("InsertBillItem : ", "CustomerGSTIN :"+billItemBean.getStrGSTIN());
            Logger.d("InsertBillItem : ", "CustomerGSTIN  :"+billItemBean.getStrGSTIN());

            // cust StateCode
            if (cbInterStateStatus.isChecked()) {
                String str = spInterState.getSelectedItem().toString();
                int length = str.length();
                String sub = "";
                if (length > 0) {
                    sub = str.substring(length - 2, length);
                }
                billItemBean.setStrCustStateCode(sub);
                //Log.d("InsertBillItems", "CustStateCode :" + sub+" - "+str);
                Logger.d("InsertBillItems", "CustStateCode :" + sub+" - "+str);
            } else {
                billItemBean.setStrCustStateCode(OWNERPOS);
                Logger.d("InsertBillItems", "CustStateCode :" +billItemBean.getStrCustStateCode());
                Logger.d("InsertBillItems", "CustStateCode :"+billItemBean.getStrCustStateCode());
            }

            // BusinessType
            if (edtCustomerGSTIN.getText().toString().isEmpty() && edtCustomerGSTIN.getText().toString().equals("")) {
                billItemBean.setStrBusinessType("B2C");
            } else // gstin present means b2b bussiness
            {
                billItemBean.setStrBusinessType("B2B");
            }
            //Log.d("InsertBillItems", "BusinessType : " + billItemBean.getStrBusinessType());
            Logger.d("InsertBillItems", "BusinessType : " + billItemBean.getStrBusinessType());
            billItemBean.setiBillStatus(1);
            //Log.d("InsertBillItems", "Bill Status: 1");
            Logger.d("InsertBillItems", "Bill Status: 1");

            if(!avSalesManId.getText().toString().isEmpty()){
                billItemBean.setStrSalesManId(avSalesManId.getText().toString());
            }
            //Log.d("InsertBillItem : ", "SalesManId :"+billItemBean.getStrSalesManId());
            Logger.d("InsertBillItem : ", "SalesManId  :"+billItemBean.getStrSalesManId());


            lResult = HomeActivity.dbHandler.addBillItems(billItemBean);
            if((lResult > 0) && BILL_WITH_STOCK ==1){

                Cursor item_qty_crsr = HomeActivity.dbHandler.mGetItemsForBillingQtyUpdate(billItemBean.getiItemId());
                try {
                    if (item_qty_crsr != null && item_qty_crsr.moveToFirst()) {
                        // already present , needs to update
                        double dblQtyFromDB = item_qty_crsr.getDouble(item_qty_crsr.getColumnIndex(DatabaseHandler.KEY_Quantity));
                        double quantity_d = 0.00;
                        if(dblQtyFromDB - billItemBean.getDblQty() > 0)
                            quantity_d = dblQtyFromDB - billItemBean.getDblQty();

                        ContentValues cvItems = new ContentValues();
                        cvItems.put(DatabaseHandler.KEY_Quantity, quantity_d);
                        long l = HomeActivity.dbHandler.mItemUpdateBillingQty(""+billItemBean.getiItemId(), cvItems);
                        if (l > -1) {
                            //Log.d(TAG, billItemBean.getiItemId() + " updated  successfully at " + l);
                            Logger.d(TAG, billItemBean.getiItemId() + " updated  successfully at " + l);
                            int ii = -1;
                            for(ItemMasterBean displayitem : itemsDisplayLists)
                            {
                                ii++;
                                if(displayitem.get_id() == billItemBean.getiItemId())
                                {
                                    displayitem.setDblQty(displayitem.getDblQty()-billItemBean.getDblQty());
                                    billingDisplayListAdapter.notifyItemChanged(ii);
                                    break;
                                }
                            }
                        }
                    }
                }catch (Exception ex){
                    Log.i(TAG, "error on updating item quantity." +ex.getMessage());
                    Logger.i(TAG, "error on updating item quantity." +ex.getMessage());
                }finally {
                    if(item_qty_crsr != null){
                        item_qty_crsr.close();
                    }
                }
                //Log.d("InsertBillItem", "Bill item inserted at position:" + lResult);
                Logger.d("InsertBillItem", "Bill item inserted at position:" + lResult);
            }/*else
            {
                lResult =-1;
                break;
            }*/

        }
        return lResult;
    }

    //Testing implemented
    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;
    }

    /*************************************************************************************************************************************
     * Inserts bill details to bill detail database table
     *
     * TenderType : Type of tender, 1 - Pay cash, 2 - Tender Screen payment
     *************************************************************************************************************************************/
    private long InsertBillDetail() {

        // Inserted Row Id in database table
        long lResult = -1;

        // BillDetail object
        BillDetailBean objBillDetail;

        objBillDetail = new BillDetailBean();

        objBillDetail.setStrInvoiceNo(edtBillNumber.getText().toString());
        //Log.d("InsertBillDetail", "InvoiceNo :" + objBillDetail.getStrInvoiceNo());
        Logger.d("InsertBillDetail", "InvoiceNo :" + objBillDetail.getStrInvoiceNo());

        Date date1 = new Date();
        try{
            date1 = new SimpleDateFormat("dd-MM-yyyy").parse(BUSINESS_DATE);
        }catch (Exception e)
        {
            Logger.e(TAG,""+e);
        }
        objBillDetail.setStrInvoiceDate(""+date1.getTime());
        //Log.d("InsertBillDetail", "InvoiceDate :" + objBillDetail.getStrInvoiceDate() +" "+BUSINESS_DATE);
        Logger.d("InsertBillDetail", "InvoiceDate :" + objBillDetail.getStrInvoiceDate());


        // Time
        //objBillDetail.setTime(String.format("%tR", Time));
        Date Time = new Date();
        String strTime = new SimpleDateFormat("kk:mm:ss").format(Time.getTime());
        objBillDetail.setStrTime(strTime);
        //Log.d("InsertBillDetail", "Time :" + strTime);
        Logger.d("InsertBillDetail", "Time :" + strTime);



        // custStateCode
        if (cbInterStateStatus.isChecked()) {
            String str = spInterState.getSelectedItem().toString();
            int length = str.length();
            String sub = "";
            if (length > 0) {
                sub = str.substring(length - 2, length);
            }
            objBillDetail.setCustStateCode(sub);
            //Log.d("InsertBillDetail", "CustStateCode :" + sub+" - "+str);
            Logger.d("InsertBillDetail", "CustStateCode :" + sub+" - "+str);
        } else {
            objBillDetail.setCustStateCode(OWNERPOS);
            //Log.d("InsertBillDetail", "CustStateCode : "+objBillDetail.getCustStateCode());
            Logger.d("InsertBillDetail", "CustStateCode : "+objBillDetail.getCustStateCode());
        }


        objBillDetail.setPOS(OWNERPOS);
        //Log.d("InsertBillDetail", "POS : "+objBillDetail.getPOS());
        Logger.d("InsertBillDetail", "POS : "+objBillDetail.getPOS());

        // Total Items
        objBillDetail.setiTotalItems(Integer.parseInt(edtItemsSelectedCounter.getText().toString()));
        //Log.d("InsertBillDetail", "Total Items :" + objBillDetail.getiTotalItems());
        Logger.d("InsertBillDetail", "Total Items :" + objBillDetail.getiTotalItems());

        double taxval_f = Double.parseDouble(edtTotalTaxableValue.getText().toString());
        objBillDetail.setDblTotalTaxableValue(Double.parseDouble(String.format("%.2f", taxval_f)));
        //Log.d("InsertBillDetail", "Taxable Value : " + objBillDetail.getDblTotalTaxableValue());
        Logger.d("InsertBillDetail", "Taxable Value : " + objBillDetail.getDblTotalTaxableValue());


        String igst = edtTotalIGST.getText().toString();
        String cgst = edtTotalCGST.getText().toString();
        String sgst = edtTotalSGST.getText().toString();
        if (cbInterStateStatus.isChecked()) {
            objBillDetail.setDblIGSTAmount(Double.parseDouble(String.format("%.2f",Double.parseDouble(igst))));
            objBillDetail.setDblCGSTAmount(0.00f);
            objBillDetail.setDblSGSTAmount(0.00f);
        } else {
            objBillDetail.setDblIGSTAmount(0.00f);
            objBillDetail.setDblCGSTAmount(Double.parseDouble(String.format("%.2f",Double.parseDouble(cgst))));
            objBillDetail.setDblSGSTAmount(Double.parseDouble(String.format("%.2f",Double.parseDouble(sgst))));
        }
        Logger.d("InsertBillDetail", "IGSTAmount : " + objBillDetail.getDblIGSTAmount());
        Logger.d("InsertBillDetail", "CGSTAmount : " + objBillDetail.getDblCGSTAmount());
        Logger.d("InsertBillDetail", "SGSTAmount : " + objBillDetail.getDblSGSTAmount());

        //Log.d("InsertBillDetail", "IGSTAmount : " + objBillDetail.getDblIGSTAmount());
        //Log.d("InsertBillDetail", "CGSTAmount : " + objBillDetail.getDblCGSTAmount());
        //Log.d("InsertBillDetail", "SGSTAmount : " + objBillDetail.getDblIGSTAmount());

        objBillDetail.setDblcessAmount(Double.parseDouble(String.format("%.2f",Double.parseDouble(edtTotalCess.getText().toString()))));
        //Log.d("InsertBillDetail", "cessAmount : " + objBillDetail.getDblcessAmount());
        Logger.d("InsertBillDetail", "cessAmount : " + objBillDetail.getDblcessAmount());

        double subtot_f = objBillDetail.getDblTotalTaxableValue() + objBillDetail.getDblIGSTAmount()
                + objBillDetail.getDblCGSTAmount()+ objBillDetail.getDblSGSTAmount()
                + objBillDetail.getDblcessAmount();

        objBillDetail.setDblSubTotal(Double.parseDouble(String.format("%.2f",subtot_f)));
        //Log.d("InsertBillItems", "SubTotal :" + objBillDetail.getDblSubTotal());
        Logger.d("InsertBillDetail", "SubTotal :" + objBillDetail.getDblSubTotal());


        objBillDetail.setDblDeliveryCharge(Double.parseDouble(String.format("%.2f",Double.parseDouble(edtOtherCharges.getText().toString()))));
        //Log.d("InsertBillDetail", "DeliveryCharges : "+objBillDetail.getDblDeliveryCharge());
        Logger.d("InsertBillDetail", "DeliveryCharges : "+objBillDetail.getDblDeliveryCharge());


        String billamt_temp = String.format("%.2f",Double.parseDouble(edtTotalAmount.getText().toString()));
        objBillDetail.setDblBillAmount(Double.parseDouble(billamt_temp));
        //Log.d("InsertBillDetaiFl", "Bill Amount :" + objBillDetail.getDblBillAmount());
        Logger.d("InsertBillDetail", "Bill Amount :" + objBillDetail.getDblBillAmount());


        String roundOff = String.format("%.2f",Double.parseDouble(edtRoundOff.getText().toString()));
        objBillDetail.setDblRoundOff(Double.parseDouble(roundOff));
        //Log.d("InsertBillDetaiFl", "roundOff :" + objBillDetail.getDblRoundOff());
        Logger.d("InsertBillDetail", "roundOff :" + objBillDetail.getDblRoundOff());

        // Discount Percentage
        double dblTotalDiscountPer = Double.parseDouble(String.format("%.2f", Double.parseDouble(tv_billing_discount_percent.getText().toString())));
        objBillDetail.setDblTotalDiscountPercentage(dblTotalDiscountPer);
        //Log.d("InsertBillDetail", "Discount Percentage:" + objBillDetail.getDblTotalDiscountPercentage());
        Logger.d("InsertBillDetail", "Discount Percentage:" + objBillDetail.getDblTotalDiscountPercentage());


        String strTotalDiscount = String.format("%.2f", Double.parseDouble(edtTotalDiscount.getText().toString()));
        objBillDetail.setDblTotalDiscountAmount(Double.parseDouble(strTotalDiscount));
        //Log.d("InsertBillDetail", "Total Discount:" + objBillDetail.getDblTotalDiscountAmount());
        Logger.d("InsertBillDetail", "Total Discount:" + objBillDetail.getDblTotalDiscountAmount());



        if(!avCustomerSearch.getText().toString().isEmpty()) {
            // cust name
            String custname = avCustomerSearch.getText().toString().trim();
            objBillDetail.setCustname(custname);
            String custGSTIN = edtCustomerGSTIN.getText().toString().trim().toUpperCase();
            objBillDetail.setGSTIN(custGSTIN);
            // objBillDetail.setCustPhone(edtCustomerMobile.getText().toString());
            objBillDetail.setiCustId(Integer.parseInt(tv_billing_CustId.getText().toString()));
            //objBillDetail.setCustAddress(customerBean.getStrCustAddress());
        }
        //Log.d("InsertBillDetail", "CustName :" + objBillDetail.getCustname());
        Logger.d("InsertBillDetail", "CustName :" + objBillDetail.getCustname());
        Logger.d("InsertBillDetail", "CustId :" + objBillDetail.getiCustId());
        //Log.d("InsertBillDetail", "custGSTIN :" + objBillDetail.getGSTIN());
        Logger.d("InsertBillDetail", "custGSTIN :" + objBillDetail.getGSTIN());

        if(SALES_MAN_ID == 1){
            if(!avSalesManId.getText().toString().isEmpty()){
                objBillDetail.setStrSalesManId(avSalesManId.getText().toString());
            }
        }
        //Log.d("InsertBillDetail", "SalesManID :" + objBillDetail.getStrSalesManId());
        Logger.d("InsertBillDetail", "SalesManID :" + objBillDetail.getStrSalesManId());


        // BusinessType
        if (edtCustomerGSTIN.getText().toString().equals("")) {
            objBillDetail.setBusinessType("B2C");
        } else // gstin present means b2b bussiness
        {
            objBillDetail.setBusinessType("B2B");
        }
        //Log.d("InsertBillDetail", "BusinessType : " + objBillDetail.getBusinessType());
        Logger.d("InsertBillDetail", "BusinessType : " + objBillDetail.getBusinessType());

        // Payment types

        objBillDetail.setDblCashPayment(dblCashPayment);
        //Log.d("InsertBillDetail", "Cash :" + objBillDetail.getDblCashPayment());
        Logger.d("InsertBillDetail", "Cash :" + objBillDetail.getDblCashPayment());

        objBillDetail.setDblCardPayment(dblCardPayment);
        //Log.d("InsertBillDetail", "Card :" + dblCardPayment);
        Logger.d("InsertBillDetail", "Card :" + dblCardPayment);

        objBillDetail.setDblCouponPayment(dblCouponPayment);
        //Log.d("InsertBillDetail", "Coupon :" + dblCouponPayment);
        Logger.d("InsertBillDetail", "Coupon :" + dblCouponPayment);

        objBillDetail.setDblPettyCashPayment(dblPettyCashPayment);
        //Log.d("InsertBillDetail", "PettyCash :" + dblPettyCashPayment);
        Logger.d("InsertBillDetail", "PettyCash :" + dblPettyCashPayment);

        objBillDetail.setDblWalletAmount(dblWalletPayment);
        //Log.d("InsertBillDetail", "Wallet :" + dblWalletPayment);
        Logger.d("InsertBillDetail", "Wallet :" + dblWalletPayment);

        objBillDetail.setDblRewardPoints(dblRewardPointsAmount);
        //Log.d("InsertBillDetail", "RewardPoints :" + dblRewardPointsAmount);
        Logger.d("InsertBillDetail", "RewardPoints :" + dblRewardPointsAmount);

        objBillDetail.setDblMSwipeAmount(dblMSwipeAmount);
        //Log.d("InsertBillDetail", "MSwipe Amount :" + dblMSwipeAmount);
        Logger.d("InsertBillDetail", "MSwipe Amount :" + dblMSwipeAmount);

        objBillDetail.setDblAEPSAmount(dblAEPSAmount);
        //Log.d("InsertBillDetail", "AEPSAmount :" + dblAEPSAmount);
        Logger.d("InsertBillDetail", "AEPSAmount :" + dblAEPSAmount);

        objBillDetail.setDblPaidTotalPayment(dblPaidTotalPayment);
        //Log.d("InsertBillDetail", "TotalPaidAmount :" + dblPaidTotalPayment);
        Logger.d("InsertBillDetail", "TotalPaidAmount :" + dblPaidTotalPayment);

        objBillDetail.setDblChangePayment(dblChangePayment);
        //Log.d("InsertBillDetail", "ReturnAmount :" + dblChangePayment);
        Logger.d("InsertBillDetail", "ReturnAmount :" + dblChangePayment);

        objBillDetail.setiReprintCount(0);
        //Log.d("InsertBillDetail", "Reprint Count :0");
        Logger.d("InsertBillDetail", "Reprint Count :0");

        objBillDetail.setiBillStatus(1);
        //Log.d("InsertBillDetail", "Bill Status :1");
        Logger.d("InsertBillDetail", "Bill Status :1");


        // Employee Id (Waiter / Rider)
        objBillDetail.setiEmployeeId(0);
        //Log.d("InsertBillDetail", "EmployeeId:0");
        Logger.d("InsertBillDetail", "EmployeeId:0");



        // User Id
        objBillDetail.setStrUserId(userId);
        //Log.d("InsertBillDetail", "UserID:" + userId);
        Logger.d("InsertBillDetail", "UserID:" + userId);


        int reverseCharge = 0;
        if(isForwardTaxEnabled ==0)
            reverseCharge =1;
        objBillDetail.setBillingMode("1"); // normal billing
        lResult = HomeActivity.dbHandler.addBillDetail(objBillDetail, reverseCharge);
        //Log.d("InsertBill", "Bill inserted at position:" + lResult);

        if(lResult >0)
        {
            for(Map.Entry<String,String> entry : deliveryChargesMap.entrySet())
            {
                ContentValues cv = new ContentValues();
                cv.put(DatabaseHandler.KEY_InvoiceNo, objBillDetail.getStrInvoiceNo());
                cv.put(DatabaseHandler.KEY_InvoiceDate, objBillDetail.getStrInvoiceDate());
                cv.put(DatabaseHandler.KEY_Time, objBillDetail.getStrTime());
                cv.put(DatabaseHandler.KEY_OTHER_CHARGES_DESCRIPTION, entry.getKey());
                cv.put(DatabaseHandler.KEY_OTHER_CHARGES_AMOUNT, entry.getValue());
                long res = HomeActivity.dbHandler.addDeliveryChargesDetails(cv);
            }
            if (String.valueOf(avCustomerSearch).equalsIgnoreCase("") || String.valueOf(avCustomerSearch).isEmpty())
            {
                // No customer Details, do nothing
            }
            else
            {
                String custid = tv_billing_CustId.getText().toString();
                Cursor cursor = HomeActivity.dbHandler.getCustomer(custid);
                double totalBillAmount = Double.parseDouble(edtTotalAmount.getText().toString());
                if(cursor!=null && cursor.moveToFirst())
                {
                    double fTotalTransaction = cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_TotalTransaction));
                    double fCreditAmount = cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_CreditAmount));
                    double dblLatTransaction  =0;
                    if(dblPettyCashPayment >0)
                        dblLatTransaction = dblPettyCashPayment;
                    else
                        dblLatTransaction = cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_LastTransaction));
                    fCreditAmount = fCreditAmount - dblPettyCashPayment;
                    fTotalTransaction += dblPettyCashPayment;
                    int rewardPointsAccumulated = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_RewardPointsAccumulated));
                    if(REWARD_POINTS ==1  )
                    {
                        if(dblRewardPointsAmount <= 0)
                        {
                            rewardPointsAccumulated += Math.abs((RewardPoints/amountToRewardPoints)*totalBillAmount);
                        }else
                        {
                            rewardPointsAccumulated -= dblRewardPointsAmount/RewardPtToAmt;
                        }
                    }
                    long lResult1 = HomeActivity.dbHandler.updateCustomerTransaction(Integer.parseInt(custid),dblLatTransaction, fTotalTransaction,
                            fCreditAmount,rewardPointsAccumulated);
                }

            }
            long Result2 = HomeActivity.dbHandler.UpdateBillNoResetInvoiceNos(Integer.parseInt(edtBillNumber.getText().toString()));
        }
        return lResult;
    }

    private void mHoldBillItems() {

        // Inserted Row Id in database table
        long lResult = 0;
        int iHoldResume = mHoldResumeBillNumberSet();
        if(iHoldResume == -1){
            msgBox.Show("Error", "Unable to get last hold resume bill number please try later.");
            return;
        }
        for (BillItemBean billItemBean : billingItemsList) {
            HoldResumeBillBean holdResumeBillBean = new HoldResumeBillBean();
            // Bill Number
            holdResumeBillBean.setStrInvoiceNo(""+iHoldResume);
            holdResumeBillBean.setStrSupplyType(billItemBean.getStrSupplyType());
            holdResumeBillBean.setStrHSNCode(billItemBean.getStrHSNCode());
            holdResumeBillBean.setiItemID(billItemBean.getiItemId());
            holdResumeBillBean.setStrItemName(billItemBean.getStrItemName());
            holdResumeBillBean.setStrBarcode(billItemBean.getStrBarcode());
            holdResumeBillBean.setStrUOM(billItemBean.getStrUOM());
            holdResumeBillBean.setiDepartmentCode(billItemBean.getiDeptCode());
            holdResumeBillBean.setiCategoryCode(billItemBean.getiCategCode());
            holdResumeBillBean.setiBrandCode(billItemBean.getiBrandCode());

            double dblBillAmount = Double.parseDouble(edtTotalAmount.getText().toString());
            holdResumeBillBean.setDblBillAmount(Double.parseDouble(String.format("%.2f",dblBillAmount)));
            // richa_2012
            //BillingMode
            holdResumeBillBean.setStrBillingMode("1");

            // Quantity
            double qty_d = 0.00;
            qty_d = billItemBean.getDblQty();
            holdResumeBillBean.setDblQty(Double.parseDouble(String.format("%.2f",qty_d)));

            // Rate
            double rate_d = 0.00;
            rate_d = billItemBean.getDblValue();
            holdResumeBillBean.setDblValue(Double.parseDouble(String.format("%.2f",rate_d)));

            // oRIGINAL rate in case of reverse tax
            int selectedId = rgPriceOptions.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            RadioButton rbPriceOptions = (RadioButton) rootView.findViewById(selectedId);
            switch (rbPriceOptions.getText().toString()) {
                case Constants.MRP:
                    holdResumeBillBean.setDblOriginalRate(billItemBean.getDblMRP());
                    break;
                case Constants.RETAIL_PRICE:
                    holdResumeBillBean.setDblOriginalRate(billItemBean.getDblRetailPrice());
                    break;
                case Constants.WHOLE_SALE_PRICE:
                    holdResumeBillBean.setDblOriginalRate(billItemBean.getDblWholeSalePrice());
                    break;
                default:
                    break;
            }
            double originalRate = 0.00;
            originalRate = billItemBean.getDblOriginalRate();
            holdResumeBillBean.setDblOriginalRate(Double.parseDouble(String.format("%.2f",originalRate)));

            double mrp = 0.00;
            mrp = billItemBean.getDblMRP();
            holdResumeBillBean.setDblMRP(Double.parseDouble(String.format("%.2f",mrp)));

            double rp = 0.00;
            rp = billItemBean.getDblRetailPrice();
            holdResumeBillBean.setDblRetailPrice(Double.parseDouble(String.format("%.2f",rp)));

            double wp = 0.00;
            wp = billItemBean.getDblWholeSalePrice();
            holdResumeBillBean.setDblWholeSalePrice(Double.parseDouble(String.format("%.2f",wp)));

            // Amount
            double amount = 0.00;
            amount = billItemBean.getDblAmount();
            holdResumeBillBean.setDblAmount(Double.parseDouble(String.format("%.2f",amount)));
            if (isForwardTaxEnabled == 1) { // forward tax
                holdResumeBillBean.setStrIsReverseTaxEnabled("No");
            }else
            {
                holdResumeBillBean.setStrIsReverseTaxEnabled("YES");
            }

            // Discount %
            double discPer = 0.00;
            discPer = billItemBean.getDblDiscountPercent();
            holdResumeBillBean.setDblDiscountPercent(Double.parseDouble(String.format("%.2f",discPer)));

            // Discount Amount
            double discAmt = 0.00;
            discAmt = billItemBean.getDblDiscountAmount();
            holdResumeBillBean.setDblDiscountAmount(Double.parseDouble(String.format("%.2f",discAmt)));

            // Service Tax Percent
            double sgstRate = 0.00;
            sgstRate = billItemBean.getDblSGSTRate();
            holdResumeBillBean.setDblSGSTRate(Double.parseDouble(String.format("%.2f",sgstRate)));

            // Service Tax Amount
            double sgstAmt = 0;
            sgstAmt = billItemBean.getDblSGSTAmount();
            holdResumeBillBean.setDblSGSTAmount(Double.parseDouble(String.format("%.2f",sgstAmt)));


            // Sales Tax %
            double cgstRate = 0.00;
            cgstRate = billItemBean.getDblCGSTRate();
            holdResumeBillBean.setDblCGSTRate(Double.parseDouble(String.format("%.2f",cgstRate)));


            // Sales Tax Amount
            double cgstAmt = 0.00;
            cgstAmt = billItemBean.getDblCGSTAmount();
            holdResumeBillBean.setDblCGSTAmount(Double.parseDouble(String.format("%.2f",cgstAmt)));


            // IGST Tax %
            double igstRate = 0.00;
            igstRate = billItemBean.getDblIGSTRate();
            holdResumeBillBean.setDblIGSTRate(Double.parseDouble(String.format("%.2f",igstRate)));


            // IGST Tax Amount
            double igstAmt = 0.00;
            igstAmt = billItemBean.getDblIGSTAmount();
            holdResumeBillBean.setDblIGSTAmount(Double.parseDouble(String.format("%.2f",igstAmt)));


            // cess Tax %
            double cessRate = 0.00;
            cessRate = billItemBean.getDblCessRate();
            holdResumeBillBean.setDblCessRate(Double.parseDouble(String.format("%.2f",cessRate)));

            // cessTax Amount
            double cessAmount = 0.00;
            cessAmount = billItemBean.getDblCessAmount();
            holdResumeBillBean.setDblCessAmount(Double.parseDouble(String.format("%.2f",cessAmount)));


            // subtotal
            double subTotal = 0.00;
            subTotal = billItemBean.getDblTaxbleValue();
            holdResumeBillBean.setDblTaxableValue(Double.parseDouble(String.format("%.2f",subTotal)));
            holdResumeBillBean.setDblSubTotal(Double.parseDouble(String.format("%.2f",subTotal)));
            // Date
          /*  String date_today = tvDate.getText().toString();
            ////Log.d("Date ", date_today);
            try {
                Date date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date_today);
                objBillItem.setInvoiceDate(String.valueOf(date1.getTime()));
            }catch (Exception e)
            {
                e.printStackTrace();
            }*/

            holdResumeBillBean.setStrInvoiceDate(BUSINESS_DATE);

            if(!avCustomerSearch.getText().toString().isEmpty()){
                // cust info
                holdResumeBillBean.setStrCustName(avCustomerSearch.getText().toString());
                holdResumeBillBean.setStrCustPhone(edtCustomerMobile.getText().toString());
                holdResumeBillBean.setStrGSTIN(edtCustomerGSTIN.getText().toString());
            }

            // cust StateCode
            if (cbInterStateStatus.isChecked()) {
                String str = spInterState.getSelectedItem().toString();
                int length = str.length();
                String sub = "";
                if (length > 0) {
                    sub = str.substring(length - 2, length);
                }
                holdResumeBillBean.setStrCustStateCode(sub);
                Logger.d("InsertBillItems", "CustStateCode :" + sub+" - "+str);
            } else {
                holdResumeBillBean.setStrCustStateCode(HomeActivity.dbHandler.getOwnerPOS());// to be retrieved from database later -- richa to do
                Logger.d("InsertBillItems", "CustStateCode :"+billItemBean.getStrCustStateCode());
            }

            // BusinessType
            if (edtCustomerGSTIN.getText().toString().isEmpty() || edtCustomerGSTIN.getText().toString().equals("")) {
                holdResumeBillBean.setStrBussinessType("B2C");
            } else // gstin present means b2b bussiness
            {
                holdResumeBillBean.setStrBussinessType("B2B");
            }
            Logger.d("InsertBillItems", "BusinessType : " + billItemBean.getStrBusinessType());
            holdResumeBillBean.setiBillStatus(1);
            Logger.d("InsertBillItems", "Bill Status:1");
            // richa to do - hardcoded b2b bussinies type
            //objBillItem.setBusinessType("B2B");
            lResult = HomeActivity.dbHandler.addHoldResumeBill(holdResumeBillBean);
            //Log.d("InsertBillItem", "Bill item inserted at position:" + lResult);

        }
        if(lResult > -1){
            Toast.makeText(myContext, "Bill hold success.",Toast.LENGTH_LONG).show();
        }
        mClear();
    }

    private int mHoldResumeBillNumberSet(){
        int iHoldResumeBillNumber = HomeActivity.dbHandler.getHoldResumeLastBillNo();
        if(iHoldResumeBillNumber == -1){
            msgBox.Show("Error","Please contact support team before billing.");
            return -1;
        }
        iHoldResumeBillNumber = iHoldResumeBillNumber + 1;
        return iHoldResumeBillNumber;
    }

    private  void confirmAndClear()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                .setTitle(getString(R.string.clear))
                .setIcon(R.mipmap.ic_company_logo)
                .setMessage(getString(R.string.clear_screen_message) )
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mClear();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
    private void mClear() {
        tx = "";
        Time = Calendar.getInstance();
        avSalesManId.setText("");
        tv_billing_CustId.setText("-1");
        tv_billing_discount_percent.setText("0");
        avCustomerSearch.setText("");
        avItemSearch.setText("");
        edtCustomerGSTIN.setText("");
        edtCustomerMobile.setText("");
        edtInvoiceDate.setText(BUSINESS_DATE);
        cbShortCodeStatus.setChecked(false);
        cbInterStateStatus.setChecked(false);
        spInterState.setEnabled(false);
        billingItemsList.clear();
        reprintBillingMode = 0;
        if(billingListAdapter != null){
            billingListAdapter.notifyData(billingItemsList);
        }
        edtTotalIGST.setText("");
        edtTotalCGST.setText("");
        edtTotalSGST.setText("");
        edtTotalCess.setText("");
        edtTotalDiscount.setText("");
        edtTotalTaxableValue.setText("");
        edtTotalAmount.setText("");
        edtRoundOff.setText("0.00");
        itemMasterBean = null;
        mBillNumberSet();
        edtItemsSelectedCounter.setText("");
        dTotalDiscount = 0;
        fRoundOfValue = 0;
        AMOUNTPRINTINNEXTLINE = 0;
        dblCashPayment = 0;
        dblCardPayment = 0;
        dblCouponPayment = 0;
        dblPaidTotalPayment = 0;
        dblPettyCashPayment = 0;
        dblChangePayment = 0;
        dblWalletPayment = 0;
        dblRewardPointsAmount =0;
        dblAEPSAmount = 0;
        dblMSwipeAmount = 0;
        dFinalBillValue = 0;
        if(PRINT_MRP_RETAIL_DIFFERENCE ==1)
            PRINT_DISCOUNT =1;
        else
            PRINT_DISCOUNT =0;
    }

    private void mResetBillingListSelectedItems(){
//        edtTotalIGST.setText("");
//        edtTotalCGST.setText("");
//        edtTotalSGST.setText("");
//        edtTotalCess.setText("");
//        edtTotalDiscount.setText("");
//        edtOtherCharges.setText("");
//        edtTotalTaxableValue.setText("");
//        edtTotalAmount.setText("");
//        itemMasterBean = null;
//        billingItemsList.clear();
//        if(billingListAdapter != null){
//            billingListAdapter.notifyData(billingItemsList);
//        }
//        edtItemsSelectedCounter.setText("");

        if(billingItemsList.size()>0)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                    .setTitle("Delete")
                    .setIcon(R.mipmap.ic_company_logo)
                    .setMessage(getString(R.string.billing_list_all_item_delete_msg) )
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            billingItemsList.clear();
                            calculateAndDisplayBillingData(-1, false);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
        else
        {
            msgBox.Show(getString(R.string.delete), getString(R.string.no_item_to_delete_message));
        }

    }

    @Override
    public void onCustomerAddSuccess(Customer customer) {
        customerBean = customer;
        // Update the parent class's TextView
        if (customerBean != null) {
            avCustomerSearch.setThreshold(100);
            avCustomerSearch.setText(customerBean.getStrCustName());
            avCustomerSearch.setThreshold(1);
            edtCustomerMobile.setText(customerBean.getStrCustContactNumber());
            if (customerBean.getStrCustGSTIN() != null && !customerBean.getStrCustGSTIN().isEmpty()) {
                edtCustomerGSTIN.setText(customerBean.getStrCustGSTIN());
                checkForInterstateTransaction(customerBean.getStrCustGSTIN().substring(0,2));
            }
            tv_billing_CustId.setText(""+customer.get_id());
        }
    }

    @Override
    public void onDisplayAdapterSelectItem(ItemMasterBean itemMasterBeanTemp) {
        //Toast.makeText(myContext,itemMasterBeanTemp.getStrShortName(),Toast.LENGTH_LONG).show();
        mAddItemToOrderTableList(itemMasterBeanTemp, false);
    }

    @Override
    public void onDeptDataSelect(Department department) {
        //Toast.makeText(myContext,department.getDeptName(),Toast.LENGTH_LONG).show();
        if(FAST_BILLING_MODE ==2)
        {
            if(populateDisplayCategoryDataAsyncObject == null){
                populateDisplayCategoryDataAsyncObject = new PopulateDisplayCategoryDataAsync();
                populateDisplayCategoryDataAsyncObject.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,department.getDeptCode());
            }
        } else if (FAST_BILLING_MODE ==1)
        {
            if(populateDisplayItemsDataAsyncObject == null){
                populateDisplayItemsDataAsyncObject = new PopulateDisplayItemsDataAsync();
                populateDisplayItemsDataAsyncObject.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Constants.DEPARTMENT, department.getDeptCode());
            }
        }

    }

    @Override
    public void onCategoryItemSelected(Category category) {
        //Toast.makeText(myContext,category.getCategName(),Toast.LENGTH_LONG).show();
        if(populateDisplayItemsDataAsyncObject == null){
            populateDisplayItemsDataAsyncObject = new PopulateDisplayItemsDataAsync();
            populateDisplayItemsDataAsyncObject.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Constants.CATEGORY,category.getiCategCode());
        }
    }

    @Override
    public void onBillingListItemSelected(int iPosition) {
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
            return;
        }
        lastClickTime = SystemClock.elapsedRealtime();

        fm = getActivity().getSupportFragmentManager();
        PriceQtyChangeDialog priceQtyChangeDialog = new PriceQtyChangeDialog();
        priceQtyChangeDialog.initListener(this);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BillItemBean.BILL_ITEM_BEAN_PARCELABLE_KEY, billingItemsList.get(iPosition));
        bundle.putInt(Constants.isDISCOUNT_TYPE,DISCOUNT_TYPE);
        bundle.putInt(Constants.isPRICE_CHANGE,PRICE_CHANGE);
        bundle.putInt(Constants.isBILL_WITH_STOCK,BILL_WITH_STOCK);
        priceQtyChangeDialog.setArguments(bundle);
        priceQtyChangeDialog.show(fm, "Price & Quantity Change");
    }

    @Override
    public void onBillingListItemRemove(final int iPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                .setTitle("Delete")
                .setIcon(R.mipmap.ic_company_logo)
                .setMessage(getString(R.string.billing_list_selected_item_delete_msg) + "\nItemName : " + billingItemsList.get(iPosition).getStrItemName())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        if(billingItemsList.size() > 0){
                            billingItemsList.remove(iPosition);
                            /*if(billingItemsList.size()==0){
                                calculateAndDisplayBillingData(-1, false);
                                return;
                            }*/
                            calculateAndDisplayBillingData(-1,false);
                        }
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    public void onBillResume(String strInvoiceNo) {
        Cursor cursor = null;
        List<ItemMasterBean> itemMasterBeanHoldResumeList = new ArrayList<ItemMasterBean>();
        String strCustName = null, strCustPhone  = null, strCustGSTIN  = null;
        try {
            cursor = HomeActivity.dbHandler.getHoldResumeBillDataByInvoiceNo(strInvoiceNo);
            // Get the state's capital from this row in the database.
            itemMasterBean = null;
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                itemMasterBeanHoldResumeList.clear();
                do {
                    itemMasterBean = new ItemMasterBean();
                    itemMasterBean.setStrShortName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemName)));
                    if (cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_HSNCode)) != null) {
                        itemMasterBean.setStrHSNCode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_HSNCode)));
                    }
                    if (cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_UOM)) != null) {
                        itemMasterBean.setStrUOM(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_UOM)));
                    }
                    itemMasterBean.set_id(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_ITEM_ID
                    )));
                    if(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_BrandCode)) > 0) {
                        itemMasterBean.setiBrandCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_BrandCode)));
                    }
                    if (cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)) > 0) {
                        itemMasterBean.setiDeptCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)));
                    }
                    if (cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_CategoryCode)) > 0) {
                        itemMasterBean.setiCategoryCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_CategoryCode)));
                    }
                    if (cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)) != null
                            && !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)).isEmpty()) {
                        itemMasterBean.setStrBarcode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)));
                    }
                    if (cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)) != null
                            && !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)).isEmpty()) {
                        itemMasterBean.setStrSupplyType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)));
                    }
                    if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)) > 0) {
                        itemMasterBean.setDblRetailPrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)));
                    } else {
                        itemMasterBean.setDblRetailPrice(0);
                    }
                    if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_MRP)) > 0) {
                        itemMasterBean.setDblMRP(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_MRP)));
                    } else {
                        itemMasterBean.setDblMRP(0);
                    }
                    if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)) > 0) {
                        itemMasterBean.setDblWholeSalePrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)));
                    } else {
                        itemMasterBean.setDblWholeSalePrice(0);
                    }

                    int selectedId = rgPriceOptions.getCheckedRadioButtonId();
                    // find the radiobutton by returned id
                    RadioButton rbPriceOptions = (RadioButton) rootView.findViewById(selectedId);
                    switch (rbPriceOptions.getText().toString()) {
                        case Constants.MRP:
                            itemMasterBean.setDblMRP(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_Value)));
                            break;
                        case Constants.RETAIL_PRICE:
                            itemMasterBean.setDblRetailPrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_Value)));
                            break;
                        case Constants.WHOLE_SALE_PRICE:
                            itemMasterBean.setDblWholeSalePrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_Value)));
                            break;
                        default:
                            break;
                    }

                    if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_CGSTRate)) > 0) {
                        itemMasterBean.setDblCGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_CGSTRate)));
                    } else {
                        itemMasterBean.setDblCGSTRate(0);
                    }
                    if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_SGSTRate)) > 0) {
                        itemMasterBean.setDblSGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_SGSTRate)));
                    } else {
                        itemMasterBean.setDblSGSTRate(0);
                    }
                    if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_IGSTRate)) > 0) {
                        itemMasterBean.setDblIGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_IGSTRate)));
                    } else {
                        itemMasterBean.setDblIGSTRate(0);
                    }
                    if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_cessRate)) > 0) {
                        itemMasterBean.setDblCessRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_cessRate)));
                    } else {
                        itemMasterBean.setDblCessRate(0);
                    }
                    if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountAmount)) > 0) {
                        itemMasterBean.setDblDiscountAmt(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountAmount)));
                    } else {
                        itemMasterBean.setDblDiscountAmt(0);
                    }
                    if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountPercent)) > 0) {
                        itemMasterBean.setDbDiscountPer(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountPercent)));
                    } else {
                        itemMasterBean.setDbDiscountPer(0);
                    }
                    itemMasterBean.setDblQty(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_Quantity)));
                    // Update the parent class's TextView
                    if (itemMasterBean != null) {
                        itemMasterBeanHoldResumeList.add(itemMasterBean);
                        //mAddItemToOrderTableList(itemMasterBean);
                        //mPopulateItemsListAdapterData(billingItemsList);
                    }
                    if (cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_CustName)) != null
                            && !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_CustName)).isEmpty()) {
                        strCustName = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_CustName));
                        strCustPhone = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_CustPhoneNo));
                        if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN)) != null
                                && !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN)).isEmpty())
                            strCustGSTIN = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN));

                    }
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            Logger.i(TAG, "Unable to populate data from the resume bill. Hold Resume Bill Number : "
                    + strInvoiceNo + " error : " + ex.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        if(itemMasterBeanHoldResumeList.size() > 0){
            for(ItemMasterBean itemMasterBean1 : itemMasterBeanHoldResumeList){
                mAddItemToOrderTableList(itemMasterBean1,true);
            }
            if(strCustName != null && !strCustName.isEmpty()){
                avCustomerSearch.setThreshold(100);
                avCustomerSearch.setText(strCustName);
                avCustomerSearch.setThreshold(1);
                edtCustomerMobile.setText(strCustPhone);
                if (strCustGSTIN != null && !strCustGSTIN.isEmpty()) {
                    edtCustomerGSTIN.setText(strCustGSTIN);
                    checkForInterstateTransaction(strCustGSTIN.substring(0, 2));
                }
            }
        }
    }

    void mProceedToPay()
    {
        if (edtTotalAmount.getText().toString().equals("") ) {
            msgBox.Show("Warning", "Please add item to make bill");
            return;
        } else if ( edtTotalTaxableValue.getText().toString().equals("0.00")) {
            msgBox.Show("Warning", "Please add item of rate greater than 0.00");
            return;
        }else if (cbInterStateStatus.isChecked() && spInterState.getSelectedItem().equals("Select")) {
            msgBox.Show("Warning", "Please Select state for Interstate Supply");
            return;
        } else if((OWNERPOS.equals("") || OWNERPOS.equals("0")))
        {
            msgBox.Show(getString(R.string.invalid_attempt),getString(R.string.empty_owner_pos_message) );
            return;
        }else
        {
            String salesmanId = avSalesManId.getText().toString().trim();
            if(salesmanId != null && !salesmanId.equals(""))
            {
                Cursor cursor = HomeActivity.dbHandler.mGetSalesManIdSearchData(salesmanId);
                if(!(cursor !=null && cursor.moveToFirst()))
                {
                    msgBox.Show(getString(R.string.invalid_attempt),getString(R.string.invalid_salesmanId_message) );
                    return;
                }
            }
        }

        {
            ArrayList<AddedItemsToOrderTableClass> orderItemList = new ArrayList<>();
            int taxType =0;

            for(BillItemBean itemObj : billingItemsList) {

                int itemId = 0;
                String itemName = "";
                double quantity = 0.00;
                double rate = 0.00;
                double igstRate = 0.00;
                double igstAmt = 0.00;
                double cgstRate = 0.00;
                double cgstAmt = 0.00;
                double sgstRate = 0.00;
                double sgstAmt = 0.00;
                double cessRate = 0.00;
                double cessAmt = 0.00;
                double amount = 0.00;
                double billamount = 0.00;
                double discountamount = 0.00;


                itemId = itemObj.getiItemId();
                itemName = itemObj.getStrItemName();
                quantity = itemObj.getDblQty();
                rate = itemObj.getDblValue();
                if (cbInterStateStatus.isChecked()) {
                    cgstRate = 0;
                    cgstAmt = 0;
                    sgstRate = 0;
                    sgstAmt = 0;
                    igstRate = itemObj.getDblIGSTRate();
                    igstAmt = itemObj.getDblIGSTAmount();
                } else {
                    igstRate = 0;
                    igstAmt = 0;
                    cgstRate = itemObj.getDblCGSTRate();
                    cgstAmt = itemObj.getDblCGSTAmount();
                    sgstRate = itemObj.getDblSGSTRate();
                    sgstAmt = itemObj.getDblSGSTAmount();

                }
                cessRate = itemObj.getDblCessRate();
                cessAmt = itemObj.getDblCessAmount();
                taxType = itemObj.getiTaxType();
                amount = itemObj.getDblAmount();


                AddedItemsToOrderTableClass orderItem = new AddedItemsToOrderTableClass(itemId, itemName, quantity, rate,
                        igstRate, igstAmt, cgstRate, cgstAmt, sgstRate, sgstAmt, rate * quantity, amount, billamount, cessRate, cessAmt, discountamount);
                orderItemList.add(orderItem);
            }

            /*AddedItemsToOrderTableClass orderItem = new AddedItemsToOrderTableClass ( 1,"Aloo Mirch",2.3,50.1,3,1.50, 1.5,0.75
                    ,1.5,0.75,115.23,11.23,0,3,1.05,10);
            orderItemList.add(orderItem);*/

            Bundle bundle = new Bundle();
            bundle.putDouble(TOTALBILLAMOUNT , Double.parseDouble(edtTotalAmount.getText().toString()));
            bundle.putDouble(TAXABLEVALUE, Double.parseDouble(edtTotalTaxableValue.getText().toString()));

            bundle.putDouble(ROUNDOFFAMOUNT,Double.parseDouble(edtRoundOff.getText().toString()) );
            if(edtOtherCharges.getText().toString().isEmpty()){
                bundle.putDouble(OTHERCHARGES, Double.parseDouble(edtOtherCharges.getText().toString()));
            } else {
                bundle.putDouble(OTHERCHARGES, Double.parseDouble(edtOtherCharges.getText().toString()));
            }
            bundle.putDouble(DISCOUNTAMOUNT , Double.parseDouble(edtTotalDiscount.getText().toString()));
            bundle.putInt(TAXTYPE, isForwardTaxEnabled);
            //bundle.putInt(CUSTID, Integer.parseInt(tvCustId.getText().toString()));
            bundle.putString(PHONENO, edtCustomerMobile.getText().toString());
            bundle.putParcelableArrayList(ORDERLIST,orderItemList);
            if(cbInterStateStatus.isChecked())
            {
                double igstAmount = Double.parseDouble(edtTotalIGST.getText().toString());
                double cessAmount  = Double.parseDouble(edtTotalCess.getText().toString());
                bundle.putDouble(TAXAMOUNT , igstAmount+ cessAmount);
            }else
            {
                double sgstAmount = Double.parseDouble(edtTotalSGST.getText().toString());
                double cgstAmount = Double.parseDouble(edtTotalCGST.getText().toString());
                double cessAmount  = Double.parseDouble(edtTotalCess.getText().toString());
                bundle.putDouble(TAXAMOUNT , cgstAmount+sgstAmount+ cessAmount);
            }

            fm = getActivity().getSupportFragmentManager();
            PayBillFragment proceedToPayBillingFragment = new PayBillFragment();
            proceedToPayBillingFragment.initProceedToPayListener(this);
            proceedToPayBillingFragment.setArguments(bundle);
            proceedToPayBillingFragment.show(fm, "Proceed To Pay");

        }
    }


    @Override
    public void onProceedToPayComplete(PaymentDetails obj) {

        boolean  isDiscounted, isPrintBill = false;
        double dDiscPercent;

        custPhone = obj.getCustPhoneNo();
        Cursor cursor  = HomeActivity.dbHandler.getCustomerbyPhone(custPhone);
        if(cursor!=null && cursor.moveToFirst()) {
            customerId = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_CustId));
            tv_billing_CustId.setText(customerId);
            avCustomerSearch.setThreshold(100);
            avCustomerSearch.setText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_CustName)));
            avCustomerSearch.setThreshold(1);
            if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN)) !=null && !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN)).equals("")){
                edtCustomerGSTIN.setText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN)));
                checkForInterstateTransaction(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN)).substring(0,2));
            }
        }
        else
        {
            customerId ="0";
        }

        /*if(!tv_billing_CustId.getText().toString().equals(""))
            customerId = tv_billing_CustId.getText().toString();
        else
            customerId ="0";*/

        isPrintBill = obj.getToPrintBill();
        dDiscPercent = obj.getTotalDiscountPercent();

        dblCashPayment = obj.getCashAmount();
        dblAEPSAmount = obj.getAepsAmount();
        dblCardPayment = obj.getCardAmount();
        dblCouponPayment = obj.getCouponAmount();
        dblPettyCashPayment = obj.getPettyCash();
        dblPaidTotalPayment = obj.getTotalPaidAmount();
        dblWalletPayment = obj.getWalletAmount();
        dblRewardPointsAmount = obj.getRewardPoints();
        dblMSwipeAmount = obj.getmSwipeAmount();
        dblChangePayment = obj.getTotalreturnAmount();
        fRoundOfValue = obj.getTotalRoundOff();
        dFinalBillValue = obj.getTotalFinalBillAmount();
        isDiscounted = obj.getisDiscounted();
        dblDiscountAmount = obj.getTotalDiscountAmount();

        if (isDiscounted == true) {
            PRINT_DISCOUNT = 1;
            Log.v("Tender Result", "Discounted:" + isDiscounted);
            Log.v("Tender Result", "Discount Amount:" + dblDiscountAmount);
            edtTotalDiscount.setText(String.valueOf(dblDiscountAmount));
            //edtDiscountPercent.setText(String.valueOf(dDiscPercent));

            double igst = obj.getTotalIGSTAmount();
            double cgst = obj.getTotalCGSTAmount();
            double sgst = obj.getTotalSGSTAmount();
            double cess = obj.getTotalcessAmount();
            double billtot = obj.getTotalBillAmount();
            if(billtot >0)
            {
                edtTotalAmount.setText(String.format("%.2f",billtot));
                edtTotalCess.setText(String.format("%.2f",cess));
                if(cbInterStateStatus.isChecked())
                {
                    edtTotalIGST.setText(String.format("%.2f", igst));
                    edtTotalCGST.setText(String.format("%.2f", "0.00"));
                    edtTotalSGST.setText("0.00");
                }else {
                    edtTotalIGST.setText(String.format("0.00"));
                    edtTotalCGST.setText(String.format("%.2f", cgst));
                    edtTotalSGST.setText(String.format("%.2f", sgst));
                }

            }

            ArrayList<AddedItemsToOrderTableClass> orderList_recieved = obj.getOrderList();
            double taxableValue = 0.00;

            for(BillItemBean addedItem : billingItemsList) {
                int itemId = addedItem.getiItemId();
                for (AddedItemsToOrderTableClass changedItemObj : orderList_recieved) {
                    if (changedItemObj.getMenuCode() == itemId) {
                        addedItem.setDblDiscountAmount(changedItemObj.getDiscountamount());
                        addedItem.setDblSGSTAmount(changedItemObj.getSgstAmt());
                        addedItem.setDblCGSTAmount(changedItemObj.getCgstAmt());
                        addedItem.setDblIGSTAmount(changedItemObj.getIgstAmt());
                        addedItem.setDblCessAmount(changedItemObj.getCessAmt());
                        addedItem.setDblQty(changedItemObj.getQuantity());
                        addedItem.setDblTaxbleValue(changedItemObj.getTaxableValue() * changedItemObj.getQuantity());
                        addedItem.setDblAmount(changedItemObj.getTaxableValue() + changedItemObj.getIgstAmt());
                        taxableValue += addedItem.getDblTaxbleValue();
                        break;
                    }
                }
            }

            edtTotalTaxableValue.setText(String.format("%.2f", taxableValue));
        }
        edtTotalAmount.setText(String.format("%.2f",dFinalBillValue));

        PrintBillPayment =0;
        mSaveBillData( 1);
        //updateOutwardStock();
        Toast.makeText(myContext, "Bill saved Successfully", Toast.LENGTH_SHORT).show();
        if (isPrintBill == true) {
            strPaymentStatus = "Paid";
            for (int i=0; i<obj.getNoOfPrint(); i++)
                PrintNewBill(BUSINESS_DATE);
        }
        mClear();
        btnSavePrintBill.setEnabled(true);
    }

    @Override
    public void onPriceQuantityChangeDataApplySucces(BillItemBean billItemBean) {
        if(billingItemsList.size() > 0){
            for(int i = 0; i < billingItemsList.size(); i++){
                if(billingItemsList.get(i).getiItemId() == billItemBean.getiItemId()){
                    billingItemsList.remove(i);
                    billingItemsList.add(billItemBean);
                    if(billingItemsList.get(i).getDblDiscountAmount() > 0){
                        double dblDiscAmtTemp = billingItemsList.get(i).getDblDiscountAmount();
                        double dblQtyTemp = billingItemsList.get(i).getDblQty();
                        billingItemsList.get(i).setDblDiscountAmount((dblDiscAmtTemp *
                                dblQtyTemp));
                    }
                    //billingListAdapter.notifyData(billingItemsList);
                    calculateAndDisplayBillingData(billingItemsList.size()-1, true);
                    break;
                }
            }
        }
    }

    void applyValidations()
    {
        avCustomerSearch.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        avItemSearch.setFilters(new InputFilter[]{new EMOJI_FILTER()});
    }

    @Override
    public void onPrinterConnectsEnableWidgets() {
        btnReprintBill.setEnabled(true);
        btnReprintBill.setBackgroundResource(R.color.widget_color);
        btnSavePrintBill.setEnabled(true);
        btnSavePrintBill.setBackgroundResource(R.color.widget_color);
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String str = charSequence.toString();
            //View view = myContext.getCurrentFocus();
            View view = getView();
            if (view == null)
                return;
            try {
                switch (view.getId()) {
                    case R.id.av_billing_SalesManId:
                        tx = avSalesManId.getText().toString();
                        break;
                    case R.id.autocomplete_billing_customer_search:
                        tx = avCustomerSearch.getText().toString();
                        break;

                    case R.id.autocomplete_billing_items_search:
                        tx = avItemSearch.getText().toString();
                        break;

                }
            } catch (Exception e){
                e.printStackTrace();
                Toast.makeText(myContext, "An error occured", Toast.LENGTH_SHORT).show();
                //messageDialog.Show("Error","An error occured");
            }


        }


        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable s) {
            try {
                View view = getView();
                /*if(view== null  || CUSTOMER_FOUND==1)
                    return;
                if(view.getId() == R.id.edtCustPhoneNo){
                    if (editTextMobile.getText().toString().length() == 10 ) {
                        Cursor crsrCust = db.getFnbCustomer(editTextMobile.getText().toString());
                        if (crsrCust.moveToFirst()) {
                            CUSTOMER_FOUND = 1;
                            customerId = crsrCust.getString(crsrCust.getColumnIndex("CustId"));
                            editTextName.setText(crsrCust.getString(crsrCust.getColumnIndex("CustName")));
                            editTextAddress.setText(crsrCust.getString(crsrCust.getColumnIndex("CustAddress")));
                            String gstin = crsrCust.getString(crsrCust.getColumnIndex("GSTIN"));
                            if (gstin == null)
                                etCustGSTIN.setText("");
                            else
                                etCustGSTIN.setText(gstin);
                            checkForInterStateTax();
                            ControlsSetEnabled();
                            btn_DineInAddCustomer.setEnabled(false);
                            btn_PrintBill.setEnabled(true);
                            btn_PayBill.setEnabled(true);
                            CUSTOMER_FOUND = 0;
                        } else {
                            messageDialog.Show("Note", "Customer is not Found, Please Add Customer before Order");
                            btn_DineInAddCustomer.setVisibility(View.VISIBLE);
                            btn_DineInAddCustomer.setEnabled(true);
                        }
                    }else if (editTextMobile.getText().toString().trim().equals("")){
                        CUSTOMER_FOUND=1;
                        editTextName.setText("");
                        customerId = "0";
                        editTextAddress.setText("");
                        etCustGSTIN.setText("");
                        CUSTOMER_FOUND=0;
                        chk_interstate.setChecked(false);
                        chk_interstate.setEnabled(true);
                        spnr_pos.setEnabled(false);
                        //Toast.makeText(this, "Please select customer for billing , if required", Toast.LENGTH_SHORT).show();

                    } else {
                        btn_DineInAddCustomer.setEnabled(true);
                    }
                }*/
            }catch (Exception ex) {
                //messageDialog.Show("Error " , ex.getMessage());
                Toast.makeText(myContext, "An error occurred.", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
        }
    };

    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }*/

    void addBarCodeItemToOrderTable()
    {
        String barcode = avItemSearch.getText().toString().trim();
        System.out.println("Barcode = "+barcode);
        if(barcode == null || barcode.equals("") )
            return;
        Cursor crsr = HomeActivity.dbHandler.getItemssbyBarCode(barcode);
        if(crsr!=null )
        {
            if(crsr.getCount()>1)
            {
                inflateMultipleRateOption(crsr);
            }else if(crsr.moveToFirst())
            {
                fill(crsr);
                if (itemMasterBean != null) {
                    //mPopulateItemsListAdapterData(billingItemsList);
                    mAddItemToOrderTableList(itemMasterBean, false);
                }
            }
        }
        else {
            msgBox.Show("Oops ","Item not found");
            Toast.makeText(myContext, "Item not found", Toast.LENGTH_SHORT).show();
        }
        avItemSearch.setText("");
        linefeed="";
    }

    int checkCount =0;
    void inflateMultipleRateOption(Cursor cursor)
    {
        checkCount =0;
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_multiple_item_with_same_name, null, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        ImageView iv_close = (ImageView)view.findViewById(R.id.iv_close);
        final TableLayout tbl_rate = (TableLayout)view.findViewById(R.id.tbl_rates);
        Button btnOk = (Button) view.findViewById(R.id.btnok) ;

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        View view1 = getActivity().getCurrentFocus();
        if (view1 != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view1.getWindowToken(), 0);
        }
        /*btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkCount <=0) {
                    msgBox.Show(getString(R.string.invalid_attempt),getString(R.string.no_rate_choosen_message));
                    //dialog.dismiss();
                    return;
                }else if(checkCount >1)
                {
                    msgBox.Show(getString(R.string.invalid_attempt),getString(R.string.choose_one_rate_message));
                    return;
                }

                for(int i=1;i<tbl_rate.getChildCount();i++)
                {
                    TableRow row = (TableRow) tbl_rate.getChildAt(i);
                    CheckBox checkBox = (CheckBox) row.getChildAt(0);
                    if(checkBox.isChecked())
                    {
                        int id = Integer.parseInt(checkBox.getText().toString());
                        Cursor cursor = HomeActivity.dbHandler.getItemByID(id);
                        if (cursor!=null && cursor.moveToNext())
                        {
                            fill(cursor);
                            mAddItemToOrderTableList(itemMasterBean, false);
                        }
                        break;
                    }
                }
                dialog.dismiss();
            }
        });*/
        int count =1;

        while(cursor!=null && cursor.moveToNext())
        {

            TableRow row = new TableRow(myContext);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.drawable.row_item_background);

            CheckBox checkBox = new CheckBox(myContext);
            String item_id = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_id));
            checkBox.setText(item_id);
            checkBox.setHeight(40);
            checkBox.setTextSize(1);
            checkBox.setVisibility(View.GONE);
            //checkBox.setTextColor(getResources().getColor(R.drawable.row_item_background));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked)
                        checkCount++;
                    else
                        checkCount--;
                }
            });
            row.addView(checkBox);

            TextView tvSno = new TextView(myContext);
            tvSno.setText(""+count);
            tvSno.setHeight(50);
            count++;
            tvSno.setTextSize(20);
            tvSno.setPadding(5,0,0,0);
            row.addView(tvSno);

            TextView tvName = new TextView(myContext);
            tvName.setHeight(50);
            tvName.setTextSize(20);
            tvName.setTextColor(Color.parseColor("#000000"));
            tvName.setText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemShortName)));
            row.addView(tvName);

            TextView tvMrp  = new TextView(myContext);
            tvMrp.setHeight(50);
            tvMrp.setTextSize(20);
            tvMrp.setTextColor(Color.parseColor("#000000"));
            String mrp = String.format("%.2f", cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_MRP)));
            tvMrp.setText(mrp);
            row.addView(tvMrp);

            row.setTag("TAG");
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (String.valueOf(view.getTag()) == "TAG") {
                        TableRow Row = (TableRow) view;
                        CheckBox checkBox1= (CheckBox) Row.getChildAt(0);
                        int id = Integer.parseInt(checkBox1.getText().toString());
                        Cursor cursor = HomeActivity.dbHandler.getItemByID(id);
                        if (cursor!=null && cursor.moveToNext())
                        {
                            fill(cursor);
                            mAddItemToOrderTableList(itemMasterBean, false);
                        }else {
                            Toast.makeText(myContext, "Some error occured", Toast.LENGTH_SHORT).show();
                            Logger.d(TAG, "Item not found");
                        }
                        dialog.dismiss();
                    }
                }
            });
            tbl_rate.addView(row);
        }
    }
    /*void inflateMultipleRateOptionwithOkButton(Cursor cursor)
    {
        checkCount =0;
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_multiple_item_with_same_name, null, false);
        final AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        ImageView iv_close = (ImageView)view.findViewById(R.id.iv_close);
        final TableLayout tbl_rate = (TableLayout)view.findViewById(R.id.tbl_rates);
        Button btnOk = (Button) view.findViewById(R.id.btnok) ;

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkCount <=0) {
                    msgBox.Show(getString(R.string.invalid_attempt),getString(R.string.no_rate_choosen_message));
                    //dialog.dismiss();
                    return;
                }else if(checkCount >1)
                {
                    msgBox.Show(getString(R.string.invalid_attempt),getString(R.string.choose_one_rate_message));
                    return;
                }

                for(int i=1;i<tbl_rate.getChildCount();i++)
                {
                    TableRow row = (TableRow) tbl_rate.getChildAt(i);
                    CheckBox checkBox = (CheckBox) row.getChildAt(0);
                    if(checkBox.isChecked())
                    {
                        int id = Integer.parseInt(checkBox.getText().toString());
                        Cursor cursor = HomeActivity.dbHandler.getItemByID(id);
                        if (cursor!=null && cursor.moveToNext())
                        {
                            fill(cursor);
                            mAddItemToOrderTableList(itemMasterBean, false);
                        }
                        break;
                    }
                }
                dialog.dismiss();
            }
        });
        while(cursor!=null && cursor.moveToNext())
        {

            TableRow row = new TableRow(myContext);
            row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            row.setBackgroundResource(R.drawable.row_item_background);

            CheckBox checkBox = new CheckBox(myContext);
            String item_id = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_id));
            checkBox.setText(item_id);
            checkBox.setHeight(40);
            checkBox.setTextSize(1);
            //checkBox.setTextColor(getResources().getColor(R.drawable.row_item_background));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(isChecked)
                        checkCount++;
                    else
                        checkCount--;
                }
            });
            row.addView(checkBox);

            TextView tvName = new TextView(myContext);
            tvName.setHeight(40);
            tvName.setTextSize(20);
            tvName.setTextColor(Color.parseColor("#000000"));
            tvName.setText(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemShortName)));
            row.addView(tvName);

            TextView tvMrp  = new TextView(myContext);
            tvMrp.setHeight(40);
            tvMrp.setTextSize(20);
            tvMrp.setTextColor(Color.parseColor("#000000"));
            String mrp = String.format("%.2f", cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_MRP)));
            tvMrp.setText(mrp);
            row.addView(tvMrp);


            tbl_rate.addView(row);
        }
    }*/
    void fill(Cursor cursor)
    {
        itemMasterBean = new ItemMasterBean();
        itemMasterBean.set_id(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_id)));
        itemMasterBean.setStrShortName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemShortName)));
        if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_HSNCode)) != null) {
            itemMasterBean.setStrHSNCode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_HSNCode)));
        }
        if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_UOM)) != null) {
            itemMasterBean.setStrUOM(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_UOM)));
        }
        if (cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)) != null && !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)).isEmpty()) {
            itemMasterBean.setStrBarcode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)));
        }
        if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_Quantity)) != null &&
                !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_Quantity)).equals("")) {
            itemMasterBean.setDblQty(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_Quantity)));
        }

        if(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_BrandCode)) > 0) {
            itemMasterBean.setiBrandCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_BrandCode)));
        }
        if(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)) > 0) {
            itemMasterBean.setiDeptCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)));
        }
        if(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_CategoryCode)) > 0) {
            itemMasterBean.setiCategoryCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_CategoryCode)));
        }

        if (cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)) != null && !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)).isEmpty()) {
            itemMasterBean.setStrSupplyType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)));
        }
        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)) > 0) {
            itemMasterBean.setDblRetailPrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)));
        } else{
            itemMasterBean.setDblRetailPrice(0);
        }
        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_MRP)) > 0) {
            itemMasterBean.setDblMRP(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_MRP)));
        } else{
            itemMasterBean.setDblMRP(0);
        }
        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)) > 0) {
            itemMasterBean.setDblWholeSalePrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)));
        } else{
            itemMasterBean.setDblWholeSalePrice(0);
        }
        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_CGSTRate)) > 0) {
            itemMasterBean.setDblCGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_CGSTRate)));
        } else {
            itemMasterBean.setDblCGSTRate(0);
        }
        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_SGSTRate)) > 0) {
            itemMasterBean.setDblSGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_SGSTRate)));
        } else {
            itemMasterBean.setDblSGSTRate(0);
        }
        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_IGSTRate)) > 0) {
            itemMasterBean.setDblIGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_IGSTRate)));
        } else {
            itemMasterBean.setDblIGSTRate(0);
        }
        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_cessRate)) > 0) {
            itemMasterBean.setDblCessRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_cessRate)));
        } else {
            itemMasterBean.setDblCessRate(0);
        }
        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountAmount)) > 0) {
            itemMasterBean.setDblDiscountAmt(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountAmount)));
        } else {
            itemMasterBean.setDblDiscountAmt(0);
        }
        if (cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountPercent)) > 0) {
            itemMasterBean.setDbDiscountPer(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_DiscountPercent)));
        } else {
            itemMasterBean.setDbDiscountPer(0);
        }

    }




    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        long dd = event.getEventTime()-event.getDownTime();

        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            System.out.println("Richa : Enter encountered for barcode");
            addBarCodeItemToOrderTable();
        }else if (event.getKeyCode() == KeyEvent.KEYCODE_J ||event.getKeyCode() == KeyEvent.KEYCODE_CTRL_LEFT   )
        //}else if (event.getKeyCode() == KeyEvent.KEYCODE_J ||event.getKeyCode() == KeyEvent.KEYCODE_CTRL_LEFT ||event.getKeyCode() == KeyEvent.KEYCODE_SHIFT_LEFT  )
        {
            linefeed +=String.valueOf(event.getKeyCode());
            if(linefeed.equalsIgnoreCase("38113")|| linefeed.equalsIgnoreCase("11338")) // line feed value
                addBarCodeItemToOrderTable();
        }else {
            linefeed = "";
            if (dd < 15 && dd > 0 ) {
                View v = getView();
                if (v.getId() != R.id.autocomplete_billing_items_search) {
                    switch (v.getId()) {
                        case R.id.autocomplete_billing_customer_search:
                            avItemSearch.setText(tx);
                            break;
                        case R.id.av_billing_SalesManId:
                            avItemSearch.setText(tx);
                            break;
                    }
                    String bar_str = avItemSearch.getText().toString();
                    bar_str += (char) event.getUnicodeChar();
                    avItemSearch.setText(bar_str.trim());
                    avItemSearch.showDropDown();

                } else if (v.getId() == R.id.autocomplete_billing_items_search) {

                    tx += (char) event.getUnicodeChar();
                    avItemSearch.setText(tx.trim());
                    avItemSearch.showDropDown();

                }
            }
        }
                /*Toast.makeText(myContext, "keyUp:"+keyCode+" : "+dd, Toast.LENGTH_SHORT).show();*/


        return true;
    }
}