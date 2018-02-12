package pos.wepindia.com.retail.view.Billing;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import pos.wepindia.com.retail.Constants;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.DecimalDigitsInputFilter;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.OnPriceQuantityChangeDataApplyListener;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.BillItemBean;

/**
 * Created by MohanN on 1/20/2018.
 */

public class PriceQtyChangeDialog extends DialogFragment {

    private static final String TAG = PriceQtyChangeDialog.class.getName();
    private static final int PLUS = 1;
    private static final int MINUS = 2;

    MessageDialog msgBox;

    @BindView(R.id.bt_price_discount_change_dialog_apply)
    Button btApply;
    @BindView(R.id.iv_price_discount_change_dialog_close)
    ImageView ivClose;
    @BindView(R.id.iv_price_discount_change_dialog_qty_plus)
    ImageView ivQtyPlus;
    @BindView(R.id.iv_price_discount_change_dialog_qty_minus)
    ImageView ivQtyMinus;

    @BindView(R.id.et_price_discount_change_dialog_item_name)
    EditText edtItemName;
    @BindView(R.id.et_price_discount_change_dialog_quantity)
    EditText edtQty;
    @BindView(R.id.et_price_discount_change_dialog_mrp)
    EditText edtMRP;
    @BindView(R.id.et_price_discount_change_dialog_rate)
    EditText edtRate;
    @BindView(R.id.et_price_discount_change_dialog_discount_per)
    EditText edtDiscountPer;
    @BindView(R.id.et_price_discount_change_dialog_discount_amount)
    EditText edtDiscountAmt;

    BillItemBean billItemBean;
    int iDISCOUNT_TYPE = 0, iPRICE_CHANGE = 0, isBILL_WITH_STOCK=0;

    OnPriceQuantityChangeDataApplyListener onPriceQuantityChangeDataApplyListener;

    public void initListener(OnPriceQuantityChangeDataApplyListener onPriceQuantityChangeDataApplyListener){
        this.onPriceQuantityChangeDataApplyListener = onPriceQuantityChangeDataApplyListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Custom_Dialog_Gray);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = 600;
            int height = 400;
            d.getWindow().setLayout(width, height);
            d.setCanceledOnTouchOutside(false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.price_discount_change_dialog, container,
                false);
        //getDialog().setTitle("Add Item Master");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        //getDialog().setTitle("Add New Customer");
        //App crash error log
        try {
            if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }
            msgBox = new MessageDialog(getActivity());
            ButterKnife.bind(this, view);
            try {
                edtRate.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(6,2)});
                edtDiscountPer.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(20,2)});
                edtDiscountAmt.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(6,2)});
                edtQty.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(6,2)});
            }catch(Exception e)
            {
                Logger.e(TAG,e.getMessage());
            }
            Bundle args = getArguments();
            if (args != null) {
                billItemBean = args.getParcelable(BillItemBean.BILL_ITEM_BEAN_PARCELABLE_KEY);
                iDISCOUNT_TYPE = args.getInt(Constants.isDISCOUNT_TYPE);
                iPRICE_CHANGE = args.getInt(Constants.isPRICE_CHANGE);
                isBILL_WITH_STOCK = args.getInt(Constants.isBILL_WITH_STOCK);
            } else {
                Logger.w(TAG, "Arguments expected, but missing in price & quantity dialog.");
            }

            if(billItemBean != null){
                edtItemName.setText(billItemBean.getStrItemName());
                edtMRP.setText(""+billItemBean.getDblMRP());
                edtQty.setText(""+billItemBean.getDblQty());
                edtDiscountPer.setEnabled(false);
                edtDiscountAmt.setEnabled(false);
                edtRate.setEnabled(false);
                edtDiscountPer.setText(""+billItemBean.getDblDiscountPercent());
                //edtDiscountAmt.setText(""+billItemBean.getDblDiscountAmount());
                edtRate.setText(""+billItemBean.getDblValue());
                edtDiscountPer.setEnabled(true);
                edtDiscountAmt.setEnabled(true);
                edtRate.setEnabled(true);
            }

            edtRate.setEnabled(false);
            edtDiscountAmt.setEnabled(false);
            edtDiscountPer.setEnabled(false);
            if(iDISCOUNT_TYPE == 1){
                edtDiscountPer.setEnabled(true);
                edtDiscountAmt.setEnabled(true);
            }
            if(iPRICE_CHANGE == 1){
                edtRate.setEnabled(true);
            }
        } catch (Exception ex) {
            Log.i(TAG, "Unable init price & quantity dialog fragment. " + ex.getMessage());
        }
    }



    @OnTextChanged(R.id.et_price_discount_change_dialog_rate)
    protected void onTextChangedRate(CharSequence text) {
        if (!mEditing) {
            mEditing = true;
            try {
                if (edtDiscountPer.isFocused()) {
                    return;
                }
                if (edtDiscountAmt.isFocused()) {
                    return;
                }
                        String Mrp = edtMRP.getText().toString().equals("") ? "0.00" : edtMRP.getText().toString();
                        String retailPrice = edtRate.getText().toString().equals("") ? "0.00" : edtRate.getText().toString();
                        if (Mrp.equals("") || Mrp.equals(".")) {
                            Mrp = ("0");
                        }
                        if (retailPrice.equals("") || retailPrice.equals(".")) {
                            retailPrice = ("0");
                            edtRate.setText(retailPrice);
                        }
                        double mrp_d = Double.parseDouble(Mrp);
                        double retail_d = Double.parseDouble(retailPrice);
                        if (mrp_d >= retail_d) {
                            double discountAmt = mrp_d - retail_d;
                            double discountPercent = (discountAmt / mrp_d) * 100;
                            edtDiscountAmt.setText(String.format("%.2f", discountAmt));
                            edtDiscountPer.setText(String.format("%.2f", discountPercent));
                        } else {
                            msgBox.Show("Warning", "Rate should not be grater than MRP value.");
                            edtDiscountAmt.setText("0.00");
                            edtDiscountPer.setText("0.00");
                            edtRate.setText(String.format("%.2f", mrp_d));
                        }
            } catch (Exception ex) {
                Logger.i(TAG, "Unable to calculate the discount per and amt based on the rate amount on price change dialog.");
            } finally {
            }
            mEditing = false;
        }
    }

    boolean mEditing = false;
    @OnTextChanged(R.id.et_price_discount_change_dialog_discount_amount)
    protected void onTextChangedDiscountAmt(CharSequence text) {
        if(!mEditing) {
            mEditing = true;
            try {
                if (edtDiscountPer.isFocused()) {
                    return;
                }
          /*  if(edtRate.isFocused()){
                return;
            }*/
                if (!edtDiscountAmt.getText().toString().isEmpty()) {
                        String strRetailPrice = edtRate.getText().toString().isEmpty() ? "0.00" : edtRate.getText().toString();
                        if (strRetailPrice.equals("") || strRetailPrice.equals(".")) {
                            strRetailPrice = "0";
                        }
                        String strMrp = edtMRP.getText().toString().isEmpty() ? "0.00" : edtMRP.getText().toString();
                        if (strMrp.isEmpty() && strMrp.equals(".")) {
                            strMrp = strRetailPrice;
                        }
                        double dblRate = Double.parseDouble(strRetailPrice);
                        double dblDiscAmt = Double.parseDouble(edtDiscountAmt.getText().toString());
                        double dblMRP = Double.parseDouble(strMrp);
                        if (edtMRP != null && !edtMRP.getText().toString().isEmpty() && dblDiscAmt > dblMRP) {
                            msgBox.Show("Warning", "Discount Amount should not be grater than MRP value.");
                            edtDiscountAmt.setText("0.00");
                            edtDiscountPer.setText("0.00");
                            edtRate.setText(String.format("%.2f", dblMRP));
                            mEditing = false;
                            return;
                        }
                        dblRate = dblMRP - dblDiscAmt;
                        double discountPercent = (dblDiscAmt / dblMRP) * 100;
                        edtRate.setText(String.format("%.2f", dblRate));
                        edtDiscountPer.setText(String.format("%.2f", discountPercent));
                    if(dblRate <=0 ){
                        edtDiscountPer.setText("99.99");
                    }
                } else {
                    edtRate.setText("0.00");
                    edtDiscountPer.setText("0.00");
                }
            } catch (Exception ex) {
                Logger.i(TAG, "Unable to calculate the discount percentage based on the discount amount on price change dialog.");
            } finally {
            }
            mEditing = false;
        }
    }

    @OnTextChanged(R.id.et_price_discount_change_dialog_discount_per)
    protected void onTextChangedDiscountPer(CharSequence text) {
        if(!mEditing) {
            mEditing = true;
            try {
                if (edtDiscountAmt.isFocused()) {
                    return;
                }
           /* if(edtRate.isFocused()){
                return;
            }*/
                if (!edtDiscountPer.getText().toString().isEmpty()) {
                    double discountPercent = Double.parseDouble(edtDiscountPer.getText().toString());
                    String strRetailPrice = edtRate.getText().toString().isEmpty() ? "0.00" : edtRate.getText().toString();
                    if (strRetailPrice.equals("") || strRetailPrice.equals(".")) {
                        strRetailPrice = "0";
                    }
                    String strMrp = edtMRP.getText().toString().isEmpty() ? "0.00" : edtMRP.getText().toString();
                    if (strMrp.isEmpty() && strMrp.equals(".")) {
                        strMrp = strRetailPrice;
                    }
                    double dblRate = Double.parseDouble(strRetailPrice);
                    double dblMRP = Double.parseDouble(strMrp);
                    if (discountPercent >= 100) {
                        msgBox.Show("Warning", "Discount percentage range from 0 to 99.99%.");
                        edtDiscountPer.setText("0.00");
                        edtDiscountAmt.setText("0.00");
                        edtRate.setText(String.format("%.2f", dblMRP));
                        mEditing = false;
                        return;
                    }
                    double dblDiscAmt = (discountPercent / 100) * dblMRP;
                    dblRate = dblMRP - dblDiscAmt;
                    edtRate.setText(String.format("%.2f", dblRate));
                    edtDiscountAmt.setText(String.format("%.2f", dblDiscAmt));
                } else {
                    edtRate.setText("0.00");
                    edtDiscountAmt.setText("0.00");
                }
            } catch (Exception ex) {
                Logger.i(TAG, "Unable to calculate the discount amount based on the discount percentage on price change dialog.");
            } finally {
            }
            mEditing = false;
        }
    }

    @OnClick({R.id.iv_price_discount_change_dialog_close, R.id.bt_price_discount_change_dialog_apply,
            R.id.iv_price_discount_change_dialog_qty_plus, R.id.iv_price_discount_change_dialog_qty_minus})
    protected void onWidgetClick(View view){
        switch(view.getId()){
            case R.id.iv_price_discount_change_dialog_close:
                dismiss();
                break;
            case R.id.bt_price_discount_change_dialog_apply:
                mApplyData();
                break;
            case R.id.iv_price_discount_change_dialog_qty_plus:
                mQuantityCal(PLUS);
                break;
            case R.id.iv_price_discount_change_dialog_qty_minus:
                mQuantityCal(MINUS);
                break;
            default:
                break;
        }
    }

    private void mQuantityCal(int iMode){
        double dblQty = 1;
        if(!edtQty.getText().toString().isEmpty()){
            dblQty = Double.parseDouble(edtQty.getText().toString());
        }
        switch (iMode){
            case PLUS:
                if(isBILL_WITH_STOCK ==1 )
                {
                    Cursor cursor = HomeActivity.dbHandler.getItemByID(billItemBean.getiItemId());
                    if(cursor!=null && cursor.moveToNext()&& dblQty+1 <= cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_Quantity)))
                    {
                        dblQty = dblQty + 1;
                    }
                }else{
                    dblQty = dblQty + 1;
                }
                break;
            case MINUS:
                dblQty = dblQty - 1;
                if(dblQty <= 0){
                    dblQty = 1;
                }
                break;
            default:
                break;
        }
        edtQty.setText(String.format("%.2f",dblQty));
    }

    private void mApplyData(){
        if(edtRate.getText().toString().isEmpty()){
            msgBox.Show("Warning", "Please enter Rate.");
            return;
        }
        if (isBILL_WITH_STOCK == 1) {
            if (!edtQty.getText().toString().isEmpty()) {
                double dblQtyBulkCheckCurrent = 0;
                try {
                    dblQtyBulkCheckCurrent = Double.parseDouble(String.format("%.2f", Double.parseDouble(edtQty.getText().toString())));
                } catch (Exception ex) {
                    dblQtyBulkCheckCurrent = 1;
                    edtQty.setText("" + dblQtyBulkCheckCurrent);
                    Logger.i(TAG, "Unable to get qty on price quantity change dialog." + ex.getMessage());
                }
                Cursor cursorQtyCheck = null;
                try {
                    cursorQtyCheck = HomeActivity.dbHandler.getItemByID(billItemBean.getiItemId());
                    if (cursorQtyCheck != null && cursorQtyCheck.moveToNext() && dblQtyBulkCheckCurrent > cursorQtyCheck.getDouble(cursorQtyCheck.getColumnIndex(DatabaseHandler.KEY_Quantity))) {
                        msgBox.Show(getString(R.string.note), getString(R.string.less_stock_message) + " " + cursorQtyCheck.getDouble(cursorQtyCheck.getColumnIndex(DatabaseHandler.KEY_Quantity)));
                        return;
                    }
                } catch (Exception ex) {
                    Logger.i(TAG, "Unable to check the bulk stock on mApplyData(). " + ex.getMessage());
                } finally {
                    if (cursorQtyCheck != null) {
                        cursorQtyCheck.close();
                    }
                }
            }
        }

        double dblMRP = 0.00;
        dblMRP = Double.parseDouble(String.format("%.2f",Double.parseDouble(edtMRP.getText().toString())));
        billItemBean.setDblMRP(dblMRP);

        double dblQty = 1;
        try {
            dblQty = Double.parseDouble(String.format("%.2f", Double.parseDouble(edtQty.getText().toString())));
        }catch (Exception ex){
            Logger.i(TAG, "Unable to get qty on price quantity change dialog." +ex.getMessage());
        }
        billItemBean.setDblQty(dblQty);

        double dblRate = 0.00;
        dblRate = Double.parseDouble(String.format("%.2f", Double.parseDouble(edtRate.getText().toString())));
        billItemBean.setDblValue(dblRate);

        if(dblRate > dblMRP){
            msgBox.Show("Warning", "Retail price should not be grater then MRP value.");
            return;
        }

        double dblDiscountPer = 0.00;
        if(!edtDiscountPer.getText().toString().isEmpty()){
            dblDiscountPer = Double.parseDouble(String.format("%.2f",Double.parseDouble(edtDiscountPer.getText().toString())));
        }
        billItemBean.setDblDiscountPercent(dblDiscountPer);

        double dblDiscountAmt = 0.00;
        if(!edtDiscountAmt.getText().toString().isEmpty()) {
            dblDiscountAmt = Double.parseDouble(String.format("%.2f", Double.parseDouble(edtDiscountAmt.getText().toString())));
        }
        billItemBean.setDblDiscountAmount(dblDiscountAmt);

        onPriceQuantityChangeDataApplyListener.onPriceQuantityChangeDataApplySucces(billItemBean);
        dismiss();
    }
}