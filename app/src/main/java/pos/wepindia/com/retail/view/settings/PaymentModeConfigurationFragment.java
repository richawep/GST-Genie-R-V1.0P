package pos.wepindia.com.retail.view.settings;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.EMOJI_FILTER;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;

/**
 * Created by MohanN on 1/2/2018.
 */

public class PaymentModeConfigurationFragment extends Fragment {

    private static final String TAG = PaymentModeConfigurationFragment.class.getName();

    View view;
    MessageDialog msgBox;

    @BindView(R.id.et_payment_mode_config_key_id)
    EditText edtKeyId;
    @BindView(R.id.et_payment_mode_config_secret_key)
    EditText edtSecretKey;

    @BindView(R.id.et_payment_mode_config_aeps_appId)     EditText edtAEPSAppId;
    @BindView(R.id.et_payment_mode_config_aeps_merchantId)     EditText edtAEPSMerchantId;
    @BindView(R.id.et_payment_mode_config_aeps_secretkey)     EditText edtAEPSSecretKey;

    @BindView(R.id.cb_payment_mode_config_aeps)       CheckBox cbAEPS;

    @BindView(R.id.bt_payment_mode_config_update)
    Button btnUpdate;

    @BindView(R.id.cb_payment_mode_config_razor_pay)
    CheckBox cbRazorPay;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_payment_mode_configuration_fragment, container, false);
        msgBox = new MessageDialog(getActivity());
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
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyValidations();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSetKeyIdAndSecretKey();
    }

    @OnClick({R.id.bt_payment_mode_config_update, R.id.cb_payment_mode_config_razor_pay, R.id.cb_payment_mode_config_aeps})
    protected void onClickEvent(View view){
        switch (view.getId()){
            case R.id.bt_payment_mode_config_update:
                mInsertPaymentModeConfiguration();
                break;
            case R.id.cb_payment_mode_config_razor_pay:
                if (cbRazorPay.isChecked()) {
                    edtKeyId.setEnabled(true);
                    edtSecretKey.setEnabled(true);
                } else {
                    edtKeyId.setEnabled(false);
                    edtSecretKey.setEnabled(false);
                }
                break;
            case R.id.cb_payment_mode_config_aeps:
                if (cbAEPS.isChecked()) {
                    edtAEPSAppId.setEnabled(true);
                    edtAEPSMerchantId.setEnabled(true);
                    edtAEPSSecretKey.setEnabled(true);
                } else {
                    edtAEPSAppId.setEnabled(false);
                    edtAEPSMerchantId.setEnabled(false);
                    edtAEPSSecretKey.setEnabled(false);
                }
                break;
            default:
                break;
        }
    }

    private void mSetKeyIdAndSecretKey() {
        String strKeyID= "", strSecretKey="", strAEPSAppId= "", strAEPSMerchantId="", strAEPSSecretKey= "";
        Cursor cursorPaymentModeConfig = HomeActivity.dbHandler.getPaymentModeConfiguration();

        try {
            if (cursorPaymentModeConfig != null && cursorPaymentModeConfig.moveToFirst()) {

                if (cursorPaymentModeConfig.getString(cursorPaymentModeConfig.getColumnIndex(DatabaseHandler.KEY_RAZORPAY_KEYID)) != null) {
                    strKeyID = cursorPaymentModeConfig.getString(cursorPaymentModeConfig.getColumnIndex(DatabaseHandler.KEY_RAZORPAY_KEYID));
                } else {
                    strKeyID = "";
                }

                if (cursorPaymentModeConfig.getString(cursorPaymentModeConfig.getColumnIndex(DatabaseHandler.KEY_RAZORPAY_SECRETKEY)) != null) {
                    strSecretKey = cursorPaymentModeConfig.getString(cursorPaymentModeConfig.getColumnIndex(DatabaseHandler.KEY_RAZORPAY_SECRETKEY));
                } else {
                    strSecretKey = "";
                }

                if (cursorPaymentModeConfig.getString(cursorPaymentModeConfig.getColumnIndex(DatabaseHandler.KEY_AEPS_AppId)) != null) {
                    strAEPSAppId = cursorPaymentModeConfig.getString(cursorPaymentModeConfig.getColumnIndex(DatabaseHandler.KEY_AEPS_AppId));
                } else {
                    strAEPSAppId = "";
                }

                if (cursorPaymentModeConfig.getString(cursorPaymentModeConfig.getColumnIndex(DatabaseHandler.KEY_AEPS_MerchantId)) != null) {
                    strAEPSMerchantId = cursorPaymentModeConfig.getString(cursorPaymentModeConfig.getColumnIndex(DatabaseHandler.KEY_AEPS_MerchantId));
                } else {
                    strAEPSMerchantId = "";
                }
                if (cursorPaymentModeConfig.getString(cursorPaymentModeConfig.getColumnIndex(DatabaseHandler.KEY_AEPS_SecretKey)) != null) {
                    strAEPSSecretKey = cursorPaymentModeConfig.getString(cursorPaymentModeConfig.getColumnIndex(DatabaseHandler.KEY_AEPS_SecretKey));
                } else {
                    strAEPSSecretKey = "";
                }

            } else {
                strKeyID = "";
                strSecretKey = "";
                strAEPSAppId = "";
                strAEPSMerchantId = "";
                strAEPSSecretKey = "";
            }
        }catch (Exception ex){
            Logger.e(TAG,"Unable to get data from payment mode configuration table. " + ex.getMessage());

        } finally {
            if(cursorPaymentModeConfig != null){
                cursorPaymentModeConfig.close();
            }
        }
        edtKeyId.setText(strKeyID);
        edtSecretKey.setText(strSecretKey);
        edtAEPSAppId.setText(strAEPSAppId);
        edtAEPSMerchantId.setText(strAEPSMerchantId);
        edtAEPSSecretKey.setText(strAEPSSecretKey);
    }

    private void mInsertPaymentModeConfiguration(){
        if( cbRazorPay.isChecked() && edtKeyId.getText().toString().trim().equalsIgnoreCase("")) {
            msgBox.Show("Information", "Key ID  is mandatory for razorpay");
            return;
        }
        if( cbAEPS.isChecked() && edtAEPSAppId.getText().toString().trim().equalsIgnoreCase("")
                && edtAEPSMerchantId.getText().toString().trim().equalsIgnoreCase("")
                && edtAEPSSecretKey.getText().toString().trim().equalsIgnoreCase("")) {
            msgBox.Show("Information", "All field are mandatory for AEPS");
            return;
        }
        else {

            int iResult = 0;
            // Update new payment mode config data into database
            if(cbRazorPay.isChecked()){
                iResult = HomeActivity.dbHandler.updatePaymentModeDetailsRazorPay(edtKeyId.getText().toString().trim(),
                        edtSecretKey.getText().toString().trim());
                if (iResult > 0) {
                    //msgBox.Show("Information", "Saved Successfully");
                    Toast.makeText(getActivity(), "Data stored successfully", Toast.LENGTH_SHORT).show();
                    edtKeyId.setEnabled(false);
                    edtSecretKey.setEnabled(false);
                    cbRazorPay.setChecked(false);
                } else {
                    msgBox.Show("Exception", "Failed to save razorpay configuration. Please try again");
                }
            }

            if(cbAEPS.isChecked()){
                iResult = HomeActivity.dbHandler.updatePaymentModeDetailsAEPS(edtAEPSAppId.getText().toString().trim(),
                        edtAEPSMerchantId.getText().toString().trim(),edtAEPSSecretKey.getText().toString().trim());
                if (iResult > 0) {
                    //msgBox.Show("Information", "Saved Successfully");
                    Toast.makeText(getActivity(), "Data stored successfully", Toast.LENGTH_SHORT).show();
                    edtAEPSAppId.setEnabled(false);
                    edtAEPSMerchantId.setEnabled(false);
                    edtAEPSSecretKey.setEnabled(false);
                    cbAEPS.setChecked(false);
                } else {
                    msgBox.Show("Exception", "Failed to save AEPS configuration. Please try again");
                }
            }

        }
    }
    void applyValidations()
    {
        edtKeyId.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtSecretKey.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtAEPSAppId.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtAEPSMerchantId.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtAEPSSecretKey.setFilters(new InputFilter[]{new EMOJI_FILTER()});
    }
}