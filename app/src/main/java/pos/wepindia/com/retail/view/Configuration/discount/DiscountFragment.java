package pos.wepindia.com.retail.view.Configuration.discount;

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
import pos.wepindia.com.retail.utils.DecimalDigitsInputFilter;
import pos.wepindia.com.retail.utils.EMOJI_FILTER;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.retail.view.Configuration.AdaptersAndListeners.ConfigListAdapter;
import pos.wepindia.com.retail.view.Configuration.AdaptersAndListeners.RowItemSelectListener;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.ConfigBean;
import pos.wepindia.com.wepbase.model.pojos.DiscountConfig;

/**
 * Created by MohanN on 12/7/2017.
 */

public class DiscountFragment extends Fragment implements RowItemSelectListener{

    private static final String TAG = DiscountFragment.class.getName();

    View view;
    MessageDialog msgBox;

    @BindView(R.id.bt_config_discount_add)      Button btnAdd;
    @BindView(R.id.bt_config_discount_update)   Button btnUpdate;
    @BindView(R.id.bt_config_discount_clear)    Button btnClear;
    @BindView(R.id.rv_config_discount)          RecyclerView rvDiscountList;
    @BindView(R.id.et_config_discount_desc)     EditText edtDiscDescription;
    @BindView(R.id.et_config_discount_percentage)     EditText edtDiscPercentage;

    List<ConfigBean> list = new ArrayList<ConfigBean>();

    ConfigBean configBeanSelectedData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.configuration_discount_fragment, container, false);

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
        applyDecimalvalidation();
        applyValidations();
    }

    void applyDecimalvalidation()
    {
        edtDiscPercentage.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2,2)});
    }
    @Override
    public void onResume() {
        super.onResume();
        mDisplayDiscount();
    }

    @OnClick({R.id.bt_config_discount_add, R.id.bt_config_discount_update, R.id.bt_config_discount_clear})
    protected void OnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.bt_config_discount_add:
                mAddDiscount();
                break;
            case R.id.bt_config_discount_update:
                mEditDiscount();
                break;
            case R.id.bt_config_discount_clear:
                mClearWidget();
                break;
            default:
                break;
        }
    }

    private void mDisplayDiscount(){
        Cursor crsrDiscount = null;
        list.clear();
        try {
            crsrDiscount = HomeActivity.dbHandler.getAllDiscountConfig();
            if(crsrDiscount != null && crsrDiscount.getCount() > 0 && crsrDiscount.moveToFirst()){
                do{
                    ConfigBean configBean = new ConfigBean();
                    configBean.setiID(crsrDiscount.getInt(crsrDiscount.getColumnIndex(DatabaseHandler.KEY_id)));
                    configBean.setlDeptCode(crsrDiscount.getInt(crsrDiscount.getColumnIndex(DatabaseHandler.KEY_DiscId)));
                    configBean.setStrDescription(crsrDiscount.getString(crsrDiscount.getColumnIndex(DatabaseHandler.KEY_DiscDescription)));
                    configBean.setDblDiscount(Double.parseDouble(String.format("%.2f",crsrDiscount.getDouble(crsrDiscount.getColumnIndex(DatabaseHandler.KEY_DiscPercentage)))));
                    configBean.setiConfigMode(AppConstants.DISCOUNT_CONFIG);
                    list.add(configBean);
                }while(crsrDiscount.moveToNext());
            }
        } catch (Exception ex){
            Logger.i(TAG,ex.getMessage());
        } finally {
            if(crsrDiscount != null){
                crsrDiscount.close();
            }
        }

        if(true)//(list.size() > 0)
        {
            rvDiscountList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvDiscountList.setAdapter(new ConfigListAdapter(getActivity(),this, list));
        }
    }

    public void mAddDiscount(){
        String strDiscDescription = edtDiscDescription.getText().toString().trim();
        String dd = edtDiscPercentage.getText().toString().trim().equals("")? "0.00" : edtDiscPercentage.getText().toString().trim();
        String strDiscPercent = String.format("%.2f",Float.parseFloat(dd));
        int iDiscId;
        if(strDiscDescription.isEmpty()){
            msgBox.Show("Warning", "Please Enter Discount description before adding");
            return;
        }
        if(strDiscPercent.isEmpty()) {
            msgBox.Show("Warning", "Please enter discount percent before adding");
            return;
        }
        if (Float.parseFloat(strDiscPercent) >99.99 || Float.parseFloat(strDiscPercent) < 0){
            msgBox.Show("Warning", "Please enter discount percent between 0 and  99.99");
            return;
        }
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getStrDescription().toUpperCase().equals(strDiscDescription.toUpperCase())
                        && list.get(i).getDblDiscount() == Double.parseDouble(strDiscPercent)){
                    msgBox.Show("Warning", "Discount already present.");
                    return;
                }
            }
        }
        try
        {
            iDiscId = HomeActivity.dbHandler.getDiscountId();
            iDiscId++;
            Logger.d("InsertDiscConfig","Disc Id: " + String.valueOf(iDiscId));
            InsertDiscountConfig(iDiscId,strDiscDescription,Float.parseFloat(strDiscPercent),0);
        } catch (Exception e) {
            msgBox.Show("Error", e.getMessage());
            Logger.d("DiscountConfigActivity:" ,"Error : "+e.getMessage());
        }
        edtDiscDescription.setText("");
        edtDiscPercentage.setText("");
        mDisplayDiscount();
    }

    private void InsertDiscountConfig(int iDiscId,String strDiscDescription,float fDiscPercent, float fDiscAmount){
        long lRowId;

        DiscountConfig objDiscConfig = new DiscountConfig(strDiscDescription,iDiscId,fDiscPercent,fDiscAmount);

        lRowId = HomeActivity.dbHandler.addDiscountConfig(objDiscConfig);
        if(lRowId > -1){
            //msgBox.Show("Warning", "Discount data added successfully.");
            Toast.makeText(getActivity(), "Discount added successfully", Toast.LENGTH_SHORT).show();
        } else {
            msgBox.Show("Warning", "Discount not able to add. Please try later.");
        }

        Logger.d("InsertTaxConfig","Row Id: " + String.valueOf(lRowId));
    }

    private void mEditDiscount(){
        String strDiscDescription = edtDiscDescription.getText().toString().trim();
        String dd = edtDiscPercentage.getText().toString().trim().equals("")? "0.00" : edtDiscPercentage.getText().toString().trim();
        String strDiscPercent = String.format("%.2f",Float.parseFloat(dd));
        try {
            if(configBeanSelectedData != null && !strDiscDescription.isEmpty()
                    && !strDiscPercent.isEmpty()) {
                if(strDiscDescription.isEmpty()){
                    msgBox.Show("Warning", "Please Enter Discount description before updating");
                    return;
                }
                if(strDiscPercent.isEmpty()) {
                    msgBox.Show("Warning", "Please enter discount percent before updating");
                    return;
                }
                if (Float.parseFloat(strDiscPercent) >99.99 || Float.parseFloat(strDiscPercent) < 0){
                    msgBox.Show("Warning", "Please enter discount percent between 0 and  99.99");
                    return;
                }
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getStrDescription().toUpperCase().equals(strDiscDescription.toUpperCase())
                                && list.get(i).getDblDiscount() == Double.parseDouble(strDiscPercent)){
                            if(list.get(i).getlDeptCode() != configBeanSelectedData.getlDeptCode())
                            {
                                msgBox.Show("Warning", "Discount already present.");
                                return;
                            }
                        }
                    }
                }
                int iResult = HomeActivity.dbHandler.updateDiscountConfig(configBeanSelectedData.getlDeptCode(), strDiscDescription, strDiscPercent, "0");
                Logger.d("updateDiscount","Updated Rows: " + String.valueOf(iResult));
                if (iResult > 0) {
                    Toast.makeText(getActivity(), "Discount updated successfully", Toast.LENGTH_SHORT).show();
                    configBeanSelectedData = null;
                    mDisplayDiscount();
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
        edtDiscPercentage.setText("");
        edtDiscDescription.setText("");
        btnAdd.setEnabled(true);
        btnAdd.setBackgroundResource(R.color.widget_color);
        btnUpdate.setEnabled(false);
        btnUpdate.setBackgroundResource(R.color.disable_button);
    }

    @Override
    public void onRowDataSelect(ConfigBean configBean) {
        if(configBean != null){
            configBeanSelectedData = configBean;
            edtDiscDescription.setText(configBean.getStrDescription());
            edtDiscPercentage.setText(""+configBean.getDblDiscount());
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
                .setMessage("Are you sure you want to Delete this Discount")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        int discountId = configBean.getlDeptCode();
                        long lResult = HomeActivity.dbHandler.DeleteDiscount(discountId);
                        //MsgBox.Show("", "Discount Deleted Successfully");
                        Toast.makeText(getActivity(), "Discount Deleted Successfully", Toast.LENGTH_SHORT).show();
                        mClearWidget();
                        mDisplayDiscount();

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
        edtDiscDescription.setFilters(new InputFilter[]{new EMOJI_FILTER()});
    }
}
