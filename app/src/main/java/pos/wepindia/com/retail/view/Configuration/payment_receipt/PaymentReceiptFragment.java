package pos.wepindia.com.retail.view.Configuration.payment_receipt;

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
import pos.wepindia.com.retail.utils.EMOJI_FILTER;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.retail.view.Configuration.AdaptersAndListeners.ConfigListAdapter;
import pos.wepindia.com.retail.view.Configuration.AdaptersAndListeners.RowItemSelectListener;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.ConfigBean;
import pos.wepindia.com.wepbase.model.pojos.Description;

/**
 * Created by MohanN on 12/7/2017.
 */

public class PaymentReceiptFragment extends Fragment implements RowItemSelectListener{
    private static final String TAG = PaymentReceiptFragment.class.getName();

    View view;
    MessageDialog msgBox;

    @BindView(R.id.bt_config_pay_receipt_add)     Button btnAdd;
    @BindView(R.id.bt_config_pay_receipt_update)    Button btnUpdate;
    @BindView(R.id.bt_config_pay_receipt_clear)    Button btnClear;
    @BindView(R.id.rv_config_pay_receipt)     RecyclerView rvPayList;
    @BindView(R.id.et_config_pay_receipt_desc_name)     EditText edtPayDescription;

    List<ConfigBean> list = new ArrayList<ConfigBean>();

    ConfigBean configBeanSelectedData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.configuration_pay_receipt_fragment, container, false);
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
        applyValidations();
    }

    @Override
    public void onResume() {
        super.onResume();
        mDisplayPayRecepit();
    }

    @OnClick({R.id.bt_config_pay_receipt_add, R.id.bt_config_pay_receipt_update, R.id.bt_config_pay_receipt_clear})
    protected void OnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.bt_config_pay_receipt_add:
                mAddDescription();
                break;
            case R.id.bt_config_pay_receipt_update:
                mEditDescription();
                break;
            case R.id.bt_config_pay_receipt_clear:
                mClearWidget();
                break;
            default:
                break;
        }
    }

    private void mDisplayPayRecepit(){
        Cursor crsrPay = null;
        list.clear();
        try {
            crsrPay = HomeActivity.dbHandler.getAllDescription();
            if(crsrPay != null && crsrPay.getCount() > 0 && crsrPay.moveToFirst()){
                do{
                    ConfigBean configBean = new ConfigBean();
                    configBean.setiID(crsrPay.getInt(crsrPay.getColumnIndex(DatabaseHandler.KEY_id)));
                    configBean.setLdescripId(crsrPay.getInt(crsrPay.getColumnIndex(DatabaseHandler.KEY_DescriptionId)));
                    configBean.setStrDescription(crsrPay.getString(crsrPay.getColumnIndex(DatabaseHandler.KEY_DescriptionText)));
                    configBean.setiConfigMode(AppConstants.PAYMENT_RECEIPT_CONFIG);
                    list.add(configBean);
                }while(crsrPay.moveToNext());
            }
        } catch (Exception ex){
            Logger.i(TAG,ex.getMessage());
        } finally {
            if(crsrPay != null){
                crsrPay.close();
            }
        }

        if(true)//(list.size() > 0)
        {
            rvPayList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvPayList.setAdapter(new ConfigListAdapter(getActivity(),this, list));
        }
    }

    private void mAddDescription() {
        String strPayDesc = edtPayDescription.getText().toString().trim();
        int iPayCode;
        if (strPayDesc.equalsIgnoreCase("")) {
            msgBox.Show("Warning", "Please fill description before adding.");
            return;
        }

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getStrDescription().toUpperCase().equals(strPayDesc.toUpperCase())){
                    msgBox.Show("Warning", "Description already present.");
                    return;
                }
            }
        }

        iPayCode = HomeActivity.dbHandler.getDescriptionId();
        Logger.d("InsertDescription", "Description Id Code: " + String.valueOf(iPayCode));
        iPayCode++;
        mInsertDesciption(iPayCode, strPayDesc);
        edtPayDescription.setText("");
        mDisplayPayRecepit();
    }

    private void mInsertDesciption(int iPayCode, String strDescription){
        long lRowId;

        Description objDescript = new Description(strDescription, iPayCode);

        lRowId = HomeActivity.dbHandler.addDescription(objDescript);
        if(lRowId > -1){
           // msgBox.Show("Warning", "Description data added successfully.");
            Toast.makeText(getActivity(), "Payment Receipt added successfully", Toast.LENGTH_SHORT).show();
        } else {
            msgBox.Show("Warning", "Description not able to add. Please try later.");
        }
        Logger.d("Description","Row Id: " + String.valueOf(lRowId));
    }

    private void mEditDescription(){
        try {
            if(configBeanSelectedData != null && !edtPayDescription.getText().toString().isEmpty()) {
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getStrDescription().toUpperCase().equals(edtPayDescription.getText().toString().trim().toUpperCase())){
                            if(list.get(i).getLdescripId() !=configBeanSelectedData.getLdescripId())
                            {
                                msgBox.Show("Warning", "description already present.");
                                return;
                            }
                        }
                    }
                }
                int iResult = HomeActivity.dbHandler.updateDescription(""+configBeanSelectedData.getLdescripId(), edtPayDescription.getText().toString().trim());
                Logger.d("updateDescription", "Updated Rows: " + String.valueOf(iResult));
                if (iResult > 0) {
                    Toast.makeText(getActivity(), "Payment Receipt updated successfully", Toast.LENGTH_SHORT).show();
                    configBeanSelectedData = null;
                    mDisplayPayRecepit();
                    mClearWidget();
                } else {
                    msgBox.Show("Warning", "Update failed");
                }
            } else {
                msgBox.Show("Warning", "Please fill description name before adding or please select on the list and try to update.");
            }
        }catch (Exception ex){
            Logger.i(TAG, ex.getMessage());
        }
    }

    private void mClearWidget(){

        edtPayDescription.setText("");
        btnAdd.setEnabled(true);
        btnAdd.setBackgroundResource(R.color.widget_color);
        btnUpdate.setEnabled(false);
        btnUpdate.setBackgroundResource(R.color.disable_button);
    }



    @Override
    public void onRowDataSelect(ConfigBean configBean) {
        if(configBean != null){
            configBeanSelectedData = configBean;
            edtPayDescription.setText(configBean.getStrDescription());
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
                .setMessage("Are you sure you want to Delete this Payment Description")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        int discipId = configBean.getLdescripId();
                        long lResult = HomeActivity.dbHandler.DeleteDescription(discipId);
                        //MsgBox.Show("", "Payment Description Deleted Successfully");
                        Toast.makeText(getActivity(), "Payment Description Deleted Successfully", Toast.LENGTH_SHORT).show();
                        mClearWidget();
                        mDisplayPayRecepit();

                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    void applyValidations()
    {
        edtPayDescription.setFilters(new InputFilter[]{new EMOJI_FILTER()});
    }
}