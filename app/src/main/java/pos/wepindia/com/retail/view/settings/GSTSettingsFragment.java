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

public class GSTSettingsFragment extends Fragment {

    private static final String TAG = GSTSettingsFragment.class.getName();

    private final String POS = "POS";
    private final String HSN_CODE = "HSN CODE";
    private final String PRINT_HSN_IN_BILL = "PRINT HSN IN BILL";
    private final String UTGST = "UTGST";

    private final String ENABLE = "ENABLE";
    private final String DISABLE = "DISABLE";

    //private DatabaseHelper dbHelper;
    View view;
    MessageDialog msgBox;

    @BindView(R.id.rg_gst_pos)
    RadioGroup rgPOS;
    @BindView(R.id.rb_gst_pos_enable)
    RadioButton rbPOSEnable;
    @BindView(R.id.rb_gst_pos_disable)
    RadioButton rbPOSDisable;

    @BindView(R.id.rg_gst_hsn_code)
    RadioGroup rgHSNCode;
    @BindView(R.id.rb_gst_hsn_code_enable)
    RadioButton rbHSNCodeEnable;
    @BindView(R.id.rb_gst_hsn_code_disable)
    RadioButton rbHSNCodeDisbale;

    @BindView(R.id.rg_gst_utgst)
    RadioGroup rgUTGST;
    @BindView(R.id.rb_gst_utgst_enable)
    RadioButton rbUTGSTEnable;
    @BindView(R.id.rb_gst_utgst_disable)
    RadioButton rbUTGSTDisbale;

    @BindView(R.id.rg_gst_hsn_print_bill)
    RadioGroup rgHSNPrintBill;
    @BindView(R.id.rb_gst_hsn_print_bill_enable)
    RadioButton rbHSNPrintBillEnable;
    @BindView(R.id.rb_gst_hsn_print_bill_disable)
    RadioButton rbHSNPrintBillDisable;

    @BindView(R.id.bt_settings_gst_apply)
    Button btnApply;

    BillSetting objBillSettings ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_gst_fragment, container, false);
        ButterKnife.bind(this,view);
        msgBox = new MessageDialog(getActivity());
        objBillSettings =  new BillSetting();
        //dbHelper = DatabaseHelper.getInstance(getActivity());
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
    public void onResume()
    {
        super.onResume();
        mPopulatingData();
    }
    @OnClick({R.id.bt_settings_gst_apply})
    protected void onClickEvent(View view){
        switch (view.getId()){
            case R.id.bt_settings_gst_apply:
                mInsertDataIntoDb();
                break;
            default:
                break;
        }
    }

    private void mInsertDataIntoDb(){
        ContentValues cvOthersData = new ContentValues();

        if(mRBSelected(rgUTGST).getText().toString().toUpperCase().equals(ENABLE)) {
            objBillSettings.setUTGSTEnabled_out(1);
        } else {
            objBillSettings.setUTGSTEnabled_out(0);
        }

        if(mRBSelected(rgHSNCode).getText().toString().toUpperCase().equals(ENABLE)) {
            objBillSettings.setHSNCode(1);
        } else {
            objBillSettings.setHSNCode(0);
        }

        if(mRBSelected(rgPOS).getText().toString().toUpperCase().equals(ENABLE)) {
            objBillSettings.setPOS(1);
        } else {
            objBillSettings.setPOS(0);
        }

        if(mRBSelected(rgHSNPrintBill).getText().toString().toUpperCase().equals(ENABLE)) {
            objBillSettings.setHSNPrintenabled_out(1);
        } else {
            objBillSettings.setHSNPrintenabled_out(0);
        }
        int iResult = 0;
        // Update new settings in database
        iResult = HomeActivity.dbHandler.updateGSTSettings(objBillSettings);

        if (iResult > 0) {
            //msgBox.Show("Information", "Settings saved");
            Toast.makeText(getActivity(), "Data stored successfully", Toast.LENGTH_SHORT).show();
        } else {
            // Toast.makeText(myContext, "Apply Settings failed - Updated row:"
            // + String.valueOf(iResult), Toast.LENGTH_LONG).show();
            msgBox.Show("Warning", "Failed to save. Please try again");
        }
    }

    private void mPopulatingData(){
        Cursor mCursor = null;
        mCursor = HomeActivity.dbHandler.getBillSettings();
        try{
            if(mCursor != null && mCursor.getCount() > 0){
                mCursor.moveToNext();
                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_UTGSTEnabled)) > -1){
                    mModeInit(UTGST,mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_UTGSTEnabled)));
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_HSNPrintEnabled_out)) > -1){
                    mModeInit(PRINT_HSN_IN_BILL,mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_HSNPrintEnabled_out)));
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_POS)) > -1){
                    mModeInit(POS,mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_POS)));
                }

                if(mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_HSNCode)) > -1){
                    mModeInit(HSN_CODE,mCursor.getInt(mCursor.getColumnIndex(DatabaseHandler.KEY_HSNCode)));
                }
            }
        } catch (Exception ex){
            Logger.i(TAG, "Unable to get data from bill settings table for gst fragment " +ex.getMessage());
        }finally {
            if(mCursor != null){
                mCursor.close();
            }
        }
    }


    private RadioButton mRBSelected(RadioGroup radioGroup){
        RadioButton radioButton;
        int selectedId = radioGroup.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioButton = (RadioButton) view.findViewById(selectedId);
        return radioButton;
    }

    private void mModeInit(String strMode, int iValue){
        switch(strMode){
            case POS:
                if(iValue == 1){
                    mSetData(rbPOSEnable);
                } else {
                    mSetData(rbPOSDisable);
                }
                break;
            case HSN_CODE:
                if(iValue == 1){
                    mSetData(rbHSNCodeEnable);
                } else {
                    mSetData(rbHSNCodeDisbale);
                }
                break;
            case UTGST:
                if(iValue == 1){
                    mSetData(rbUTGSTEnable);
                } else {
                    mSetData(rbUTGSTDisbale);
                }
                break;
            case PRINT_HSN_IN_BILL:
                if(iValue == 1){
                    mSetData(rbHSNPrintBillEnable);
                } else {
                    mSetData(rbHSNPrintBillDisable);
                }
                break;
            default:
                break;
        }
    }

    private void mSetData(RadioButton radioButton){
        radioButton.setChecked(true);
    }

   /* @Override
    public void onDestroy() {
        super.onDestroy();
        if(dbHelper != null) {
            dbHelper.close();
        }
    }*/

}