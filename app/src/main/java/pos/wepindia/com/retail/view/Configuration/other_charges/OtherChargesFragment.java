package pos.wepindia.com.retail.view.Configuration.other_charges;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.AppConstants;
import pos.wepindia.com.retail.utils.DecimalDigitsInputFilter;
import pos.wepindia.com.retail.utils.EMOJI_FILTER;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.retail.view.Configuration.AdaptersAndListeners.ConfigListAdapter;
import pos.wepindia.com.retail.view.Configuration.AdaptersAndListeners.RowItemSelectListener;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.ConfigBean;

/**
 * Created by MohanN on 12/7/2017.
 */

public class OtherChargesFragment extends Fragment implements RowItemSelectListener{

    private static final String TAG = OtherChargesFragment.class.getName();

    View view;
    MessageDialog msgBox;

    @BindView(R.id.bt_config_other_charges_add)       Button btnAdd;
    @BindView(R.id.bt_config_other_charges_update)    Button btnUpdate;
    @BindView(R.id.bt_config_other_charges_clear)     Button btnClear;
    @BindView(R.id.rv_config_other_charges)           RecyclerView rvOtherChargesList;
    @BindView(R.id.et_config_other_charges_desc)      EditText edtDescription;
    @BindView(R.id.et_config_other_charges_amount)    EditText edtAmount;
    @BindView(R.id.cb_config_other_charges_chargeable)     CheckBox cbChargeableStatus;

    List<ConfigBean> list = new ArrayList<ConfigBean>();

    ConfigBean configBeanSelectedData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.configuration_other_charges_fragment, container, false);
        try{
            ButterKnife.bind(this,view);
            msgBox = new MessageDialog(getActivity());
            if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }

        } catch (Exception ex){

        }
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        applyDecimalValidation();
        applyValidations();
    }

    void applyDecimalValidation()
    {
        edtAmount.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(6,2)});
    }
    @Override
    public void onResume() {
        super.onResume();
        mDisplayOtherCharges();
    }

    @OnClick({R.id.bt_config_other_charges_add, R.id.bt_config_other_charges_update, R.id.bt_config_other_charges_clear})
    protected void OnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.bt_config_other_charges_add:
                mAddOtherCharges();
                break;
            case R.id.bt_config_other_charges_update:
                mEditOtherCharges();
                break;
            case R.id.bt_config_other_charges_clear:
                mClearWidget();
                break;
            default:
                break;
        }
    }

    private void mDisplayOtherCharges(){
        Cursor cursor = null;
        list.clear();
        try {
            cursor = HomeActivity.dbHandler.getAllOtherCharges();
            if(cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()){
                do{
                    ConfigBean configBean = new ConfigBean();
                    configBean.setiID(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_id)));
                    configBean.setlDeptCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_OTHER_CHARGES_CODE)));
                    configBean.setStrDescription(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_OTHER_CHARGES_DESCRIPTION)));
                    configBean.setDblAmount(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_OTHER_CHARGES_AMOUNT)));
                    configBean.setiChargeable(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_OTHER_CHARGES_CHARGEABLE)));
                    configBean.setiConfigMode(AppConstants.OTHER_CHARGES_CONFIG);
                    list.add(configBean);
                }while(cursor.moveToNext());
            }
        } catch (Exception ex){
            Logger.i(TAG,ex.getMessage());
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }

        if(true)//(list.size() > 0)
        {
            rvOtherChargesList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvOtherChargesList.setAdapter(new ConfigListAdapter(getActivity(),this, list));
        }
    }

    public void mAddOtherCharges(){
        String strDescription = edtDescription.getText().toString().trim();
        String dd = edtAmount.getText().toString().trim().equals("")? "0.00" : edtAmount.getText().toString().trim();
        String strAmount = String.format("%.2f",Float.parseFloat(dd));

        int iOtherChargesId;
        if(strDescription.isEmpty()){
            msgBox.Show("Warning", "Please enter other charges description before adding");
            return;
        }
        if(strAmount.isEmpty()) {
            msgBox.Show("Warning", "Please enter other charges amount before adding");
            return;
        }
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getStrDescription().toUpperCase().equals(strDescription.toUpperCase())
                        && list.get(i).getDblAmount() == Double.parseDouble(strAmount)){
                    msgBox.Show("Warning", "Other charges already present.");
                    return;
                }
            }
        }
        try
        {
            iOtherChargesId = HomeActivity.dbHandler.getOtherChargesId();
            iOtherChargesId++;
            Logger.d("InsertOtherChargesConfig","OtherCharges Id: " + String.valueOf(iOtherChargesId));
            int iStatus;
            if(cbChargeableStatus.isChecked()){
                iStatus = 1;
            } else {
                iStatus = 0;
            }
            mInsertOtherChargesConfig(iOtherChargesId, strDescription,Double.parseDouble(strAmount), iStatus);
        } catch (Exception e) {
            msgBox.Show("Error", e.getMessage());
            Logger.d("OtherChargesConfigActivity:" ,"Error : "+e.getMessage());
        }
        edtDescription.setText("");
        edtAmount.setText("");
        cbChargeableStatus.setChecked(true);
        mDisplayOtherCharges();
    }

    private void mInsertOtherChargesConfig(int iCode, String strDescription, double dblAmount, int iStatus) {
        long lRowId;
        ConfigBean configBeanTemp = new ConfigBean();
        configBeanTemp.setlDeptCode(iCode);
        configBeanTemp.setStrDescription(strDescription);
        configBeanTemp.setDblAmount(dblAmount);
        configBeanTemp.setiChargeable(iStatus);
        lRowId = HomeActivity.dbHandler.addOtherCharges(configBeanTemp);
        Logger.d("InsertOtherCharges", "Row Id: " + String.valueOf(lRowId));
        if (lRowId > -1) {
            //msgBox.Show("Warning", "OtherCharges data added successfully.");
            Toast.makeText(getActivity(), "Other Charge added successfully", Toast.LENGTH_SHORT).show();
        } else {
            msgBox.Show("Warning", "OtherCharges not able to add. Please try later.");
        }
        Logger.d("InsertOtherChargesConfig", "Row Id: " + String.valueOf(lRowId));
    }

    private void mEditOtherCharges(){
        String strDescription = edtDescription.getText().toString().trim();
        String dd = edtAmount.getText().toString().trim().equals("")? "0.00" : edtAmount.getText().toString().trim();
        String strAmount = String.format("%.2f",Float.parseFloat(dd));
        try {
            if(configBeanSelectedData != null && !strDescription.isEmpty()
                    && !strAmount.isEmpty()) {
                if(strDescription.isEmpty()){
                    msgBox.Show("Warning", "Please enter other charges description before updating");
                    return;
                }
                if(strAmount.isEmpty()) {
                    msgBox.Show("Warning", "Please enter other charges amount before updating");
                    return;
                }
                int iStatus;
                if(cbChargeableStatus.isChecked()){
                    iStatus = 1;
                } else {
                    iStatus = 0;
                }
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getStrDescription().toUpperCase().equals(strDescription.toUpperCase())
                                && list.get(i).getDblAmount() == Double.parseDouble(strAmount)){
                            if(list.get(i).getlDeptCode() != configBeanSelectedData.getlDeptCode())
                            {
                                msgBox.Show("Warning", "Other charges already present.");
                                return;
                            }
                        }
                    }
                }
                int iResult = HomeActivity.dbHandler.updateOtherCharges(configBeanSelectedData.getlDeptCode(), strDescription,
                        Double.parseDouble(strAmount), iStatus);
                Logger.d("updateOtherCharges","Updated Rows: " + String.valueOf(iResult));
                if (iResult > 0) {
                    Toast.makeText(getActivity(), "Other Charge updated successfully", Toast.LENGTH_SHORT).show();
                    configBeanSelectedData = null;
                    mDisplayOtherCharges();
                    mClearWidget();
                } else {
                    msgBox.Show("Warning", "Update failed");
                }
            } else {
                msgBox.Show("Warning", "Please select on the list and try to update.");
            }
        }catch (Exception ex){
            Logger.i(TAG, ex.getMessage());
        }
    }

    private void mClearWidget(){
        edtDescription.setText("");
        edtAmount.setText("");
        cbChargeableStatus.setChecked(true);
        btnAdd.setEnabled(true);
        btnAdd.setBackgroundResource(R.color.widget_color);
        btnUpdate.setEnabled(false);
        btnUpdate.setBackgroundResource(R.color.disable_button);
    }


    @Override
    public void onRowDataSelect(ConfigBean configBean) {
        if(configBean != null){
            configBeanSelectedData = configBean;
            edtDescription.setText(configBeanSelectedData.getStrDescription());
            edtAmount.setText(""+configBeanSelectedData.getDblAmount());
            if(configBeanSelectedData.getiChargeable() == 1){
                cbChargeableStatus.setChecked(true);
            } else {
                cbChargeableStatus.setChecked(false);
            }
            btnUpdate.setEnabled(true);
            btnUpdate.setBackgroundResource(R.color.widget_color);
            btnAdd.setEnabled(false);
            btnAdd.setBackgroundResource(R.color.disable_button);
        }
    }

    @Override
    public void onRowDataDelete(final ConfigBean configBean) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Delete")
                .setIcon(R.mipmap.ic_company_logo)
                .setMessage("Are you sure you want to Delete this Other Charges")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        int id = configBean.getlDeptCode();
                        long lResult = HomeActivity.dbHandler.DeleteOtherCharges(id);
                        //MsgBox.Show("", "OtherCharges Deleted Successfully");
                        Toast.makeText(getActivity(), "OtherCharges Deleted Successfully", Toast.LENGTH_SHORT).show();

                        mClearWidget();
                        mDisplayOtherCharges();

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

    void applyValidations()
    {
        edtDescription.setFilters(new InputFilter[]{new EMOJI_FILTER()});

    }
}