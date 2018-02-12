package pos.wepindia.com.retail.view.Configuration.coupon;

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
import pos.wepindia.com.retail.view.Configuration.AdaptersAndListeners.ConfigListAdapter;
import pos.wepindia.com.retail.view.Configuration.AdaptersAndListeners.RowItemSelectListener;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.ConfigBean;
import pos.wepindia.com.wepbase.model.pojos.Coupon;

/**
 * Created by MohanN on 12/7/2017.
 */

public class CouponFragment extends Fragment implements RowItemSelectListener {

    private static final String TAG = CouponFragment.class.getName();

    View view;
    MessageDialog msgBox;

    @BindView(R.id.bt_config_coupon_add)      Button btnAdd;
    @BindView(R.id.bt_config_coupon_update)   Button btnUpdate;
    @BindView(R.id.bt_config_coupon_clear)    Button btnClear;
    @BindView(R.id.rv_config_coupon)          RecyclerView rvCouponList;
    @BindView(R.id.et_config_coupon_desc)     EditText edtDescription;
    @BindView(R.id.et_config_coupon_amount)   EditText edtAmount;

    List<ConfigBean> list = new ArrayList<ConfigBean>();

    ConfigBean configBeanSelectedData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.configuration_coupon_fragment, container, false);
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
            Logger.e(TAG,ex.getMessage());
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
        mDisplayCoupon();
    }

    @OnClick({R.id.bt_config_coupon_add, R.id.bt_config_coupon_update, R.id.bt_config_coupon_clear})
    protected void OnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.bt_config_coupon_add:
                mAddCoupon();
                break;
            case R.id.bt_config_coupon_update:
                mEditCoupon();
                break;
            case R.id.bt_config_coupon_clear:
                mClearWidget();
                break;
            default:
                break;
        }
    }

    private void mDisplayCoupon(){
        Cursor cursorCoupon = null;
        list.clear();
        try {
            cursorCoupon = HomeActivity.dbHandler.getAllCoupon();
            if(cursorCoupon != null && cursorCoupon.getCount() > 0 && cursorCoupon.moveToFirst()){
                do{
                    ConfigBean configBean = new ConfigBean();
                    configBean.setiID(cursorCoupon.getInt(cursorCoupon.getColumnIndex(DatabaseHandler.KEY_id)));
                    configBean.setlDeptCode(cursorCoupon.getInt(cursorCoupon.getColumnIndex(DatabaseHandler.KEY_CouponId)));
                    configBean.setStrDescription(cursorCoupon.getString(cursorCoupon.getColumnIndex(DatabaseHandler.KEY_CouponDescription)));
                    configBean.setDblAmount(Double.parseDouble(String.format("%.2f",cursorCoupon.getDouble(cursorCoupon.getColumnIndex(DatabaseHandler.KEY_CouponAmount)))));
                    configBean.setiConfigMode(AppConstants.COUPON_CONFIG);
                    list.add(configBean);
                }while(cursorCoupon.moveToNext());
            }
        } catch (Exception ex){
            Logger.i(TAG,ex.getMessage());
        } finally {
            if(cursorCoupon != null){
                cursorCoupon.close();
            }
        }

        if(true)//(list.size() > 0)
        {
            rvCouponList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvCouponList.setAdapter(new ConfigListAdapter(getActivity(),this, list));
        }
    }

    public void mAddCoupon(){
        String strDescription = edtDescription.getText().toString().trim();
        String dd = edtAmount.getText().toString().trim().equals("")? "0.00" : edtAmount.getText().toString().trim();
        String strAmount = String.format("%.2f",Float.parseFloat(dd));

        int iCouponId;
        if(strDescription.isEmpty()){
            msgBox.Show("Warning", "Please enter coupon description before adding");
            return;
        }
        if(strAmount.isEmpty()) {
            msgBox.Show("Warning", "Please enter coupon amount before adding");
            return;
        }
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getStrDescription().toUpperCase().equals(strDescription.toUpperCase())
                        && list.get(i).getDblAmount() == Double.parseDouble(strAmount)){
                    msgBox.Show("Warning", "Coupon already present.");
                    return;
                }
            }
        }
        try
        {
            iCouponId = HomeActivity.dbHandler.getCouponId();
            iCouponId++;
            Logger.d("InsertCouponConfig","Coupon Id: " + String.valueOf(iCouponId));
            mInsertCouponConfig(iCouponId, strDescription,null, Float.parseFloat(strAmount));
        } catch (Exception e) {
            msgBox.Show("Error", e.getMessage());
            Logger.d("CouponConfigActivity:" ,"Error : "+e.getMessage());
        }
        edtDescription.setText("");
        edtAmount.setText("");
        mDisplayCoupon();
    }

    private void mInsertCouponConfig(int iCouponId, String strCouponDescription, String strCouponBarcode, float fCouponAmount){
        long lRowId;
        Coupon objCoupon = new Coupon(strCouponBarcode, strCouponDescription, iCouponId, fCouponAmount);
        lRowId = HomeActivity.dbHandler.addCoupon(objCoupon);
        Logger.d("InsertCoupon", "Row Id: " + String.valueOf(lRowId));
        if(lRowId > -1){
           // msgBox.Show("Warning", "Coupon data added successfully.");
            Toast.makeText(getActivity(), "Coupon added successfully", Toast.LENGTH_SHORT).show();
        } else {
            msgBox.Show("Warning", "Coupon not able to add. Please try later.");
        }
        Logger.d("InsertCouponConfig","Row Id: " + String.valueOf(lRowId));
    }

    private void mEditCoupon(){
        String strDescription = edtDescription.getText().toString().trim();
        String dd = edtAmount.getText().toString().trim().equals("")? "0.00" : edtAmount.getText().toString().trim();
        String strAmount = String.format("%.2f",Float.parseFloat(dd));
        try {
            if(configBeanSelectedData != null && !strDescription.isEmpty()
                    && !strAmount.isEmpty()) {
                if(strDescription.isEmpty()){
                    msgBox.Show("Warning", "Please enter coupon description before updating");
                    return;
                }
                if(strAmount.isEmpty()) {
                    msgBox.Show("Warning", "Please enter coupon amount before updating");
                    return;
                }

                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getStrDescription().toUpperCase().equals(strDescription.toUpperCase())
                                && list.get(i).getDblAmount() == Double.parseDouble(strAmount)){
                            if(list.get(i).getlDeptCode() != configBeanSelectedData.getlDeptCode())
                            {
                                msgBox.Show("Warning", "Coupon already present.");
                                return;
                            }
                        }
                    }
                }
                int iResult = HomeActivity.dbHandler.updateCoupon(configBeanSelectedData.getlDeptCode(), strDescription, "", strAmount);
                Logger.d("updateCoupon","Updated Rows: " + String.valueOf(iResult));
                if (iResult > 0) {
                    Toast.makeText(getActivity(), "Coupon updated successfully", Toast.LENGTH_SHORT).show();
                    configBeanSelectedData = null;
                    mDisplayCoupon();
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
        btnAdd.setEnabled(true);
        btnAdd.setBackgroundResource(R.color.widget_color);
        btnUpdate.setEnabled(false);
        btnUpdate.setBackgroundResource(R.color.disable_button);
    }

    @Override
    public void onRowDataSelect(ConfigBean configBean) {
        if(configBean != null){
            configBeanSelectedData = configBean;
            edtDescription.setText(configBean.getStrDescription());
            edtAmount.setText(""+configBean.getDblAmount());
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
                .setMessage("Are you sure you want to Delete this Coupon")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        int couponId =  configBean.getlDeptCode();
                        long lResult = HomeActivity.dbHandler.DeleteCoupon(couponId);
                        //MsgBox.Show("", "Coupon Deleted Successfully");
                        Toast.makeText(getActivity(), "Coupon Deleted Successfully", Toast.LENGTH_SHORT).show();
                        mClearWidget();
                        mDisplayCoupon();

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
        edtDescription.setFilters(new InputFilter[]{new EMOJI_FILTER()});

    }
}