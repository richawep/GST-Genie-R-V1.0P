package pos.wepindia.com.retail.view.CustomerMasters;

/**
 * Created by MohanN on 12/1/2017.
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.SwitchCompat;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.GenericClass.NegativeDecimalValidation;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.DecimalDigitsInputFilter;
import pos.wepindia.com.retail.utils.EMOJI_FILTER;
import pos.wepindia.com.retail.utils.Validations;
import pos.wepindia.com.retail.view.CustomerMasters.CustomerAdaptersAndListeners.OnCustomerAddListener;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.pojos.Customer;

public class CustomerMasterFragment extends DialogFragment {

    private static final String TAG = CustomerMasterFragment.class.getName();
    private final int ADD = 1;
    private final int UPDATE = 2;
    private final int PHONE = 1;
    private final int EMAIL = 2;
    private final int GSTIN = 3;


    @BindView(R.id.til_depositAmount)                             TextInputLayout til_depositAmount;
    @BindView(R.id.til_opening_balance)                           TextInputLayout til_opening_balance;
    @BindView(R.id.et_customer_master_add_dialog_gstin)           TextInputEditText edtGSTIN;
    @BindView(R.id.et_customer_master_add_dialog_name)            TextInputEditText edtName;
    @BindView(R.id.et_customer_master_add_dialog_phone_no)        TextInputEditText edtPhoneNo;
    @BindView(R.id.et_customer_master_add_dialog_email_id)        TextInputEditText edtEmailID;
    @BindView(R.id.et_customer_master_add_dialog_loyalty_point)   TextInputEditText edtLoyaltyPoints;
    @BindView(R.id.et_customer_master_add_dialog_address)         TextInputEditText edtAddress;
    @BindView(R.id.et_customer_master_add_dialog_credit_amt)      TextInputEditText edtCreditAmt;
    @BindView(R.id.et_customer_master_add_dialog_credit_limit)    TextInputEditText edtCreditLimit;
    @BindView(R.id.et_customer_master_add_dialog_deposit_amt)     TextInputEditText edtDepositAmt;
    @BindView(R.id.et_customer_master_add_dialog_opening_balance)     TextInputEditText edtOpeningBalance;
   // @BindView(R.id.cb_customer_master_add_dialog_deposit_amt)     CheckBox cbDepositAmt;
    @BindView(R.id.tv_cust_master_title)                          TextView tv_title;
    @BindView(R.id.bt_customer_master_add_dialog_close)           Button btClose;
    @BindView(R.id.bt_customer_master_add_dialog_save)            Button btnSaveUpdate;
    @BindView(R.id.sc_customer_master_dialog_active)              SwitchCompat sc_dialog_active;


   private OnCustomerAddListener onCustomerAddListener;
   Customer customer = null;
   Context myContext ;
   MessageDialog msgBox;

   int mClickedCustId =-1;
   List<String> mCustPhoneList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootView = inflater.inflate(R.layout.customer_master_layout, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        try {

            ButterKnife.bind(this, rootView);

        }catch (Exception ex){
            Logger.i(TAG, "Unable to init the customer master fragment or add new customer screen." + ex.getMessage());
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try{
            myContext = getActivity();
            msgBox = new MessageDialog(myContext);
            //App crash error log
            if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }
            applyDecimalValidation();
            applyValidations();
            fillCustPhoneList();

            Bundle args = getArguments();
            if (args != null) {
                customer = args.getParcelable(Customer.CUSTOMER_PARCELABLE_KEY);
            } else {
                Logger.w("GetIncidencia", "Arguments expected, but missing");
            }

            if(customer != null){
                edtName.setText(customer.getStrCustName());
                edtPhoneNo.setText(customer.getStrCustContactNumber());
                edtAddress.setText(customer.getStrCustAddress());
                edtEmailID.setText(customer.getStrEmailId());
                if(customer.getStrCustGSTIN() != null && !customer.getStrCustGSTIN().isEmpty()) {
                    edtGSTIN.setText(customer.getStrCustGSTIN());
                }
                if(customer.getiRewardPoints() >= 0){
                    edtLoyaltyPoints.setText(""+customer.getiRewardPoints());
                }
                edtCreditAmt.setText(""+customer.getdCreditAmount());
                if(customer.getdCreditLimit() > 0){
                    edtCreditLimit.setText(""+customer.getdCreditLimit());
                }

                if(1 == customer.getIsActive()) {
                    sc_dialog_active.setChecked(true);
                } else {
                    sc_dialog_active.setChecked(false);
                }
                mClickedCustId = customer.getiCustId();
                btnSaveUpdate.setText(getString(R.string.update));
                tv_title.setText(getString(R.string.edit_customer));
                edtOpeningBalance.setText(String.format("%.2f",customer.getOpeningBalance()));
                edtOpeningBalance.setEnabled(false);
                edtOpeningBalance.setTextColor(getResources().getColor(R.color.gray));
            }else
            {
                til_depositAmount.setVisibility(View.GONE);
            }

        }catch (Exception e) {
            Logger.e(TAG, e.toString());
        }
    }

    @OnClick({R.id.bt_customer_master_add_dialog_save, R.id.bt_customer_master_add_dialog_clear, R.id.bt_customer_master_add_dialog_close})
    protected void onWidgetClick(View view){
        switch(view.getId()){
            case R.id.bt_customer_master_add_dialog_save:
                                                            if(customer != null) {
                                                                mAddUpdateCustomer(UPDATE);
                                                            } else {
                                                                mAddUpdateCustomer(ADD);
                                                            }
                                                            break;
            case R.id.bt_customer_master_add_dialog_clear:
                                                            mClearCustomer();
                                                            break;
            case R.id.bt_customer_master_add_dialog_close:
                                                            dismiss();
                                                            break;
            default:
                break;
        }
    }

    private void mAddUpdateCustomer(int mode){
        if(mValidateEditText(edtName) && mValidateEditText(edtPhoneNo) && mValidateEditText(edtAddress)){
            if (mValidateEditText(edtGSTIN)) {
                if (!mDataValidation(GSTIN)) {
                    //msgBox.Show("Invalid Information", "Please Enter Valid GSTIN.");
                    return;
                }
            }
            if(mValidateEditText(edtEmailID)){
                if (!mDataValidation(EMAIL)) {
                    //msgBox.Show("Invalid Information", "Please Enter Valid Email id.");
                    return;
                }
            }
            if(mValidateEditText(edtPhoneNo)){
                if (!mDataValidation(PHONE)) {
                    //msgBox.Show("Invalid Information", "Please Enter Valid Phone Number.");
                    return;
                }
            }

            String phoneNo = edtPhoneNo.getText().toString().trim();
            if(mCustPhoneList.contains(phoneNo))
            {
                if(customer == null || (mClickedCustId >0 && !customer.getStrCustContactNumber().equals(phoneNo) ))
                {
                    msgBox.Show(getString(R.string.duplicate), getString(R.string.duplicate_phone_error_msg));
                    return;
                }
            }
            int iCustID = -1;
            try{
                iCustID = HomeActivity.dbHandler.getMaxCustomerId();
            }catch (Exception ex){
                Logger.i(TAG,"Unable to fetch the customer id from customer table." +ex.getMessage());
            }
            if(iCustID > -1){
                iCustID++;
                if(customer == null) {
                    customer = new Customer();
                    customer.setiCustId(iCustID);
                }
                customer.setStrCustName(edtName.getText().toString().trim());
                customer.setStrCustContactNumber(edtPhoneNo.getText().toString().trim());
                customer.setStrCustAddress(edtAddress.getText().toString());
                if(mValidateEditText(edtGSTIN)){
                    customer.setStrCustGSTIN(edtGSTIN.getText().toString().trim().toUpperCase());
                }
                if(mValidateEditText(edtEmailID)){
                    customer.setStrEmailId(edtEmailID.getText().toString().trim());
                }
                if(mValidateEditText(edtLoyaltyPoints) && Integer.parseInt(edtLoyaltyPoints.getText().toString()) > 0){
                    customer.setiRewardPoints(Integer.parseInt(edtLoyaltyPoints.getText().toString()));
                }else
                {
                    customer.setiRewardPoints(0);
                }
                if(mValidateEditText(edtCreditAmt) /*&& Double.parseDouble(edtCreditAmt.getText().toString()) > 0*/){
                    customer.setdCreditAmount(Double.parseDouble(String.format("%.2f",Double.parseDouble(edtCreditAmt.getText().toString()))));
                }
                else
                {
                    customer.setdCreditAmount(0.00);
                }
                if(mValidateEditText(edtCreditLimit) && Double.parseDouble(edtCreditLimit.getText().toString()) > 0){
                    customer.setdCreditLimit(Double.parseDouble(String.format("%.2f",Double.parseDouble(edtCreditLimit.getText().toString()))));
                }
                if(mValidateEditText(edtDepositAmt) && mode == UPDATE /*&& Double.parseDouble(edtDepositAmt.getText().toString()) > 0*/)
                {
                    customer.setdCreditAmount(Double.parseDouble(String.format("%.2f",(Double.parseDouble(edtDepositAmt.getText().toString()) + customer.getdCreditAmount()))));
                    customer.setDblDepositAmt(0.00);
                }
                if(mValidateEditText(edtOpeningBalance) )
                {
                    customer.setOpeningBalance(Double.parseDouble(String.format("%.2f",(Double.parseDouble(edtOpeningBalance.getText().toString())))));
                    if(mode ==ADD)
                        customer.setdCreditAmount(Double.parseDouble(String.format("%.2f",(Double.parseDouble(edtOpeningBalance.getText().toString()) + customer.getdCreditAmount()))));
                }
                if(sc_dialog_active.isChecked())
                    customer.setIsActive(1);
                else
                    customer.setIsActive(0);

                switch (mode) {
                    case ADD:
                        long id = HomeActivity.dbHandler.addCustomer(customer);
                        if (id > -1) {
                            //msgBox.Show("Message", "Customer data added successfully.");
                            Toast.makeText(myContext, "Customer added successfully", Toast.LENGTH_SHORT).show();
                            customer.set_id((int)id);
                            onCustomerAddListener.onCustomerAddSuccess(customer);
                            dismiss();
                        } else {
                            msgBox.Show("Warning", "Customer not able to add. Please try later.");
                        }
                        break;
                    case UPDATE:
                        long id1 =HomeActivity.dbHandler.updateCustomer(customer);
                        if (id1 > -1) {
                            //msgBox.Show("Warning", "Customer data update successfully.");
                            Toast.makeText(myContext, "Customer updated successfully", Toast.LENGTH_SHORT).show();
                            customer.set_id((int)id1);
                            onCustomerAddListener.onCustomerAddSuccess(customer);
                            edtCreditAmt.setText(String.format("%.2f",customer.getdCreditAmount()));
                            edtDepositAmt.setText("");
                           // dismiss();
                        } else {
                            msgBox.Show("Warning", "Customer not able to update. Please try later.");
                        }
                        break;
                    default:
                        break;
                }

            } else {
                msgBox.Show(TAG, "Unable able to get customer id. for adding new customer");
            }
        } else {
            msgBox.Show("Incomplete Information", "Please fill required details");
        }
    }

    private boolean mValidateEditText(EditText edt) {
        if (!edt.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean mDataValidation(int mode) {
        boolean bResult = false;
        switch (mode) {
            case EMAIL:
                bResult = Validations.isValidEmailAddress(edtEmailID.getText().toString());
                if (!bResult) {
                    msgBox.Show("Invalid Information", "Please Enter Valid Email id");
                }
                break;
            case PHONE:
                if (edtPhoneNo.getText().toString().trim().length() == 10) {
                    bResult = true;
                } else {
                    msgBox.Show("Invalid Information", "Phone no cannot be less than 10 digits.");
                }
                break;
            case GSTIN:
                String gstin = edtGSTIN.getText().toString();
                if( Validations.checkGSTINValidation(gstin))
                {
                    bResult = Validations.checkValidStateCode(gstin,getContext());
                    if(!bResult)
                    {
                        msgBox.Show("Invalid Information", "Please Enter Valid StateCode in GSTIN");
                    }
                }else {
                    msgBox.Show("Invalid Information", "Please Enter Valid GSTIN");
                }
                break;
            default:
                break;
        }
        return bResult;
    }

    public void mInitListener(OnCustomerAddListener onCustomerAddListener){
        this.onCustomerAddListener = onCustomerAddListener;
    }

    private void mClearCustomer() {

        mClickedCustId = -1;
        edtGSTIN.setText("");
        edtName.setText("");
        edtPhoneNo.setText("");
        edtEmailID.setText("");
        edtLoyaltyPoints.setText("");
        edtAddress.setText("");
        edtCreditAmt.setText("0.00");
        edtCreditLimit.setText("0.00");
        edtDepositAmt.setText("");
        btnSaveUpdate.setText(getString(R.string.save));
        tv_title.setText(getString(R.string.add_new_customer));
        customer = null;
        sc_dialog_active.setChecked(true);
    }

    void applyDecimalValidation()
    {
        try {
            edtCreditLimit.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(6,2)});
            edtDepositAmt.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(6,2)});
            edtOpeningBalance.setFilters(new InputFilter[] {new NegativeDecimalValidation(6,2)});

        }catch(Exception e)
        {
            Logger.e(TAG,e.getMessage());
        }
    }

    void fillCustPhoneList()
    {
        if(mCustPhoneList!=null)
            mCustPhoneList.clear();
        mCustPhoneList = HomeActivity.dbHandler.getAllCustomerPhone();
    }

    void applyValidations()
    {
        edtAddress.setFilters(new InputFilter[]{new EMOJI_FILTER()});

    }

}