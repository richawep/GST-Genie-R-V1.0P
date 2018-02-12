package pos.wepindia.com.retail.view.settings;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.BillSetting;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;


/**
 * Created by MohanN on 12/6/2017.
 */

public class OthersSettingsFragment extends Fragment {

    private static final String TAG = OthersSettingsFragment.class.getName();


    //private DatabaseHelper dbHelper;
    View view;
    MessageDialog msgBox;

    @BindView(R.id.bt_others_apply)              Button btnApply;
    @BindView(R.id.rg_others_date_time)          RadioGroup rgDateTime;
    @BindView(R.id.rb_others_date_time_auto)     RadioButton rbDateTimeAuto;
    @BindView(R.id.rb_others_date_time_manual)   RadioButton rbDateTimeManual;
    @BindView(R.id.rg_others_price_change)       RadioGroup rgPriceChange;
    @BindView(R.id.rb_others_price_change_enable)     RadioButton rbPriceChangeEnable;
    @BindView(R.id.rb_others_price_change_disable)    RadioButton rbPriceChangeDisable;
    @BindView(R.id.rg_others_bill_with_stock)         RadioGroup rgBillWithStock;
    @BindView(R.id.rb_others_bill_with_stock_enable)  RadioButton rbBillWithStockEnable;
    @BindView(R.id.rb_others_bill_with_stock_disable) RadioButton rbBillWithStockDisable;
    @BindView(R.id.rg_others_short_code_reset)         RadioGroup rgShortCodeReset;
    @BindView(R.id.rb_others_short_code_reset_auto)    RadioButton rbShortCodeResetAuto;
    @BindView(R.id.rb_others_short_code_reset_manual)    RadioButton rbShortCodeResetManual;
    @BindView(R.id.rg_others_bill_no_daily_reset)            RadioGroup rgBillNoDailyReset;
    @BindView(R.id.rb_others_bill_no_daily_reset_enable)     RadioButton rbBillNoDailyResetEnable;
    @BindView(R.id.rb_others_bill_no_daily_reset_disable)    RadioButton rbBillNoDailyResetDisable;
    @BindView(R.id.rg_others_print_owner_detail)             RadioGroup rgPrintOwnerDetail;
    @BindView(R.id.rb_others_print_owner_detail_enable)      RadioButton rbPrintOwnerDetailsEnabled;
    @BindView(R.id.rb_others_print_owner_detail_disable)     RadioButton rbPrintOwnerDetailsDisable;
    @BindView(R.id.rg_others_bill_amount_round_off)          RadioGroup rgBillAmountRoundOff;
    @BindView(R.id.rb_others_bill_amount_round_off_enable)     RadioButton rbBillAmountRoundOffEnable;
    @BindView(R.id.rb_others_bill_amount_round_off_disable)    RadioButton rbBillAmountRoundOffDisable;
    @BindView(R.id.rg_others_sales_man_id)                   RadioGroup rgSalesManId;
    @BindView(R.id.rb_others_sales_man_id_enable)            RadioButton rbSalesManIdEnable;
    @BindView(R.id.rb_others_sales_man_id_disable)           RadioButton rbSalesManIdDisable;
    @BindView(R.id.rg_others_tax)                RadioGroup rgTax;
    @BindView(R.id.rb_others_tax_forward)        RadioButton rbTaxForward;
    @BindView(R.id.rb_others_tax_reverse)        RadioButton rbTaxReverse;
    @BindView(R.id.rg_others_discount_type)           RadioGroup rgDiscountType;
    @BindView(R.id.rb_others_discount_type_item_wise) RadioButton rbDiscountTypeItemWise;
    @BindView(R.id.rb_others_discount_type_bill_wise) RadioButton rbDiscountTypeBillWise;
    @BindView(R.id.rb_others_print_difference_discount_amount_enable) RadioButton rbPrintDiscountDiferenceEnable;
    @BindView(R.id.rb_others_print_difference_discount_amount_disable) RadioButton rbPrintDiscountDiferenceDisable;
    @BindView(R.id.rg_others_fast_billing_mode)       RadioGroup rgFastBillingMode;
    @BindView(R.id.rb_others_fast_billing_mode_items)    RadioButton rbFastBillingModeItem;
    @BindView(R.id.rb_others_fast_billing_mode_dept_items)    RadioButton rbFastBillingModeDeptItems;
    @BindView(R.id.rb_others_fast_billing_mode_dept_categ_items)    RadioButton rbFastBillingModeDeptCategItems;
    @BindView(R.id.rg_others_cummulative_heading)            RadioGroup rgCummulativeHeading;
    @BindView(R.id.rb_others_cummulative_heading_enable)     RadioButton rbCummulativeHeadingEnable;
    @BindView(R.id.rb_others_cummulative_heading_disable)    RadioButton rbCummulativeHeadingDisable;
    @BindView(R.id.rg_others_loyalty_points)                 RadioGroup rgLoyaltyPoints;
    @BindView(R.id.rb_others_loyalty_points_enable)          RadioButton rbLoyaltyPointsEnable;
    @BindView(R.id.rb_others_loyalty_points_disable)         RadioButton rbLoyaltyPointsDisable;
    @BindView(R.id.rg_others_header_bold)                    RadioGroup rgHeaderBold;
    @BindView(R.id.rb_others_header_bold_enable)             RadioButton rbHeaderBoldEnable;
    @BindView(R.id.rb_others_header_bold_disable)            RadioButton rbHeaderBoldDisable;
    @BindView(R.id.rg_others_print_service)                  RadioGroup rgPrintService;
    @BindView(R.id.rb_others_print_service_enable)           RadioButton rbPrintServiceEnable;
    @BindView(R.id.rb_others_print_service_disable)          RadioButton rbPrintServiceDisable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_others_fragment, container, false);

        ButterKnife.bind(this,view);
        //dbHelper = DatabaseHelper.getInstance(getActivity());
        msgBox = new MessageDialog(getActivity());

        if (Logger.LOG_FOR_FIRST) {
            Logger.printLog();
            Logger.LOG_FOR_FIRST = false;
        }
        if (Auditing.AUDIT_FOR_FIRST) {
            Auditing.printAudit();
            Auditing.AUDIT_FOR_FIRST = false;
        }
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPopulatingData();
    }

    @OnClick({R.id.bt_others_apply})
    protected void onClickEvent(View view){
        switch (view.getId()){
            case R.id.bt_others_apply:
                mSaveOtherSettings();
                break;
            default:
                break;
        }
    }

    private void mSaveOtherSettings(){
        BillSetting billSetting = new BillSetting();
        ContentValues cvOthersData = new ContentValues();

        if(rbDateTimeAuto.isChecked()) {
            billSetting.setiDateAndTime(1);
        } else {
            billSetting.setiDateAndTime(0);
        }

        if(rbPriceChangeEnable.isChecked()) {
            billSetting.setiPriceChange(1);
        } else {
            billSetting.setiPriceChange(0);
        }

        if(rbBillWithStockEnable.isChecked()) {
            billSetting.setiBillwithStock(1);
        } else {
            billSetting.setiBillwithStock(0);
        }

        if(rbShortCodeResetAuto.isChecked()) {
            billSetting.setiItemNoReset(1);
        } else {
            billSetting.setiItemNoReset(0);
        }

        if(rbBillNoDailyResetEnable.isChecked()) {
            billSetting.setiBillNoDailyReset(1);
        } else {
            billSetting.setiBillNoDailyReset(0);
        }

        if(rbPrintOwnerDetailsEnabled.isChecked()) {
            billSetting.setPrintOwnerDetail(1);
        } else {
            billSetting.setPrintOwnerDetail(0);
        }

        if(rbBillAmountRoundOffEnable.isChecked()) {
            billSetting.setBillAmountRounfOff(1);
        } else {
            billSetting.setBillAmountRounfOff(0);
        }

        if(rbSalesManIdEnable.isChecked()) {
            billSetting.setiSalesManId(1);
        } else {
            billSetting.setiSalesManId(0);
        }

        if(rbTaxForward.isChecked()) {
            billSetting.setiTax(1);
        } else {
            billSetting.setiTax(0);
        }

        if(rbDiscountTypeItemWise.isChecked()) {
            billSetting.setiDiscountType(1);
        } else {
            billSetting.setiDiscountType(0);
        }

        if(rbPrintDiscountDiferenceEnable.isChecked())
        {
            billSetting.setiPrintDiscountDifference(1);
        }else
        {
            billSetting.setiPrintDiscountDifference(0);
        }
        if(rbFastBillingModeItem.isChecked()) {
            billSetting.setFastBillingMode(0);
        } else if(rbFastBillingModeDeptItems.isChecked()){
            billSetting.setFastBillingMode(1);
        } else {
            billSetting.setFastBillingMode(2);
        }

        if(rbCummulativeHeadingEnable.isChecked()) {
            billSetting.setCummulativeHeadingEnable(1);
        } else {
            billSetting.setCummulativeHeadingEnable(0);
        }

        if(rbLoyaltyPointsEnable.isChecked()) {
            billSetting.setiLoyaltyPoints(1);
        } else {
            billSetting.setiLoyaltyPoints(0);
        }

        if(rbHeaderBoldEnable.isChecked()) {
            billSetting.setBoldHeader(1);
        } else {
            billSetting.setBoldHeader(0);
        }

        if(rbPrintServiceEnable.isChecked()) {
            billSetting.setPrintService(1);
        } else {
            billSetting.setPrintService(0);
        }

        int iResult = 0;
        // Update new settings in database
        iResult = HomeActivity.dbHandler.updateOtherSettings(billSetting);

        // Bill no Reset Update
        String strPeriod = "";
        if(rbBillNoDailyResetEnable.isChecked() == true) {
            strPeriod = "Enable";
            iResult = HomeActivity.dbHandler.UpdateBillNoResetPeriod(strPeriod);
        } else  {
            strPeriod = "Disable";
            iResult = HomeActivity.dbHandler.UpdateBillNoResetPeriod(strPeriod);
        }
        if (iResult > 0) {
            //msgBox.Show("Information", "Settings saved");
            Toast.makeText(getActivity(), "Data stored successfully", Toast.LENGTH_SHORT).show();
        } else {
            msgBox.Show("Warning", "Failed to save. Please try again");
        }
    }

    private void mPopulatingData(){
        Cursor mCursor = null;
        mCursor = HomeActivity.dbHandler.getBillSettings();
        try{
            if(mCursor != null && mCursor.getCount() > 0){
                mCursor.moveToNext();
                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_DateAndTime)) ==1){
                   rbDateTimeAuto.setChecked(true);
                }else
                    rbDateTimeManual.setChecked(true);

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_PriceChange)) ==1){
                    rbPriceChangeEnable.setChecked(true);
                }else
                    rbPriceChangeDisable.setChecked(true);

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_BillwithStock)) ==1){
                    rbBillWithStockEnable.setChecked(true);
                }else
                {
                    rbBillWithStockDisable.setChecked(true);
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_ShortCodeReset))  ==1){
                    rbShortCodeResetAuto.setChecked(true);
                }else{
                    rbShortCodeResetManual.setChecked(true);
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_BILL_NO_RESET)) ==1){
                    rbBillNoDailyResetEnable.setChecked(true);
                }else
                {
                    rbBillNoDailyResetDisable.setChecked(true);
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_PrintOwnerDetail)) == 1){
                    rbPrintOwnerDetailsEnabled.setChecked(true);
                }else{
                    rbPrintOwnerDetailsDisable.setChecked(true);
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_RoundOff)) ==1){
                    rbBillAmountRoundOffEnable.setChecked(true);
                }else
                {
                    rbBillAmountRoundOffDisable.setChecked(true);
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_SALES_MAN_ID)) ==1){
                   rbSalesManIdEnable.setChecked(true);
                }else
                {
                    rbSalesManIdDisable.setChecked(true);
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_Tax)) ==1){
                    rbTaxForward.setChecked(true);
                }else{
                    rbTaxReverse.setChecked(true);
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_DiscountType)) ==1){
                    rbDiscountTypeItemWise.setChecked(true);
                }else
                {
                    rbDiscountTypeBillWise.setChecked(true);
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_PRINT_MRP_RETAIL_DIFFERENCE)) ==1){
                    rbPrintDiscountDiferenceEnable.setChecked(true);
                }else
                {
                    rbPrintDiscountDiferenceDisable.setChecked(true);
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_FastBillingMode)) ==0){
                    rbFastBillingModeItem.setChecked(true);
                }else if (mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_FastBillingMode)) ==1){
                    rbFastBillingModeDeptItems.setChecked(true);
                }else
                {
                    rbFastBillingModeDeptCategItems.setChecked(true);
                }


                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_CummulativeHeadingEnable)) == 1){
                    rbCummulativeHeadingEnable.setChecked(true);
                }else{
                    rbCummulativeHeadingDisable.setChecked(true);
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_RewardPoints))  == 1){
                    rbLoyaltyPointsEnable.setChecked(true);
                }else
                {
                    rbLoyaltyPointsDisable.setChecked(true);
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_HeaderBold))  == 1){
                    rbHeaderBoldEnable.setChecked(true);
                }else
                {
                    rbHeaderBoldDisable.setChecked(true);
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_PrintService)) ==1){
                    rbPrintServiceEnable.setChecked(true);
                }else {
                    rbPrintServiceDisable.setChecked(true);
                }
            }
        } catch (Exception ex){
            Logger.i(TAG, "Unable to get data from billing settings table for others " +ex.getMessage());
        }finally {
            if(mCursor != null){
                mCursor.close();
            }
        }
    }


   /* @Override
    public void onDestroy() {
        super.onDestroy();
        if(dbHelper != null) {
            dbHelper.close();
        }
    }*/
}
