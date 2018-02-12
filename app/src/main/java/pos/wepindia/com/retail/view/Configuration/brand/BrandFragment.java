package pos.wepindia.com.retail.view.Configuration.brand;

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

/**
 * Created by MohanN on 12/27/2017.
 */

public class BrandFragment extends Fragment implements RowItemSelectListener {

    private static final String TAG = BrandFragment.class.getName();

    View view;
    MessageDialog msgBox;

    @BindView(R.id.bt_config_brand_add)     Button btnAdd;
    @BindView(R.id.bt_config_brand_update)  Button btnUpdate;
    @BindView(R.id.bt_config_brand_clear)   Button btnClear;
    @BindView(R.id.rv_config_brand)         RecyclerView rvBrandList;
    @BindView(R.id.et_config_brand_name)    EditText edtBrandName;

    List<ConfigBean> list = new ArrayList<ConfigBean>();

    ConfigBean configBeanSelectedData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.configuration_brand_fragment, container, false);
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
            Logger.e(TAG, ex.getMessage());
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
        mDisplayBrand();
    }

    @OnClick({R.id.bt_config_brand_add, R.id.bt_config_brand_update, R.id.bt_config_brand_clear})
    protected void OnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.bt_config_brand_add:
                mAddBrand();
                break;
            case R.id.bt_config_brand_update:
                mEditBrand();
                break;
            case R.id.bt_config_brand_clear:
                mClearWidget();
                break;
            default:
                break;
        }
    }

    private void mDisplayBrand(){
        Cursor crsrBrand = null;
        list.clear();
        try {
            crsrBrand = HomeActivity.dbHandler.getAllBrands();
            if(crsrBrand != null && crsrBrand.getCount() > 0 && crsrBrand.moveToFirst()){
                do{
                    ConfigBean configBean = new ConfigBean();
                    configBean.setiID(crsrBrand.getInt(crsrBrand.getColumnIndex(DatabaseHandler.KEY_id)));
                    configBean.setlBrandCode(crsrBrand.getInt(crsrBrand.getColumnIndex(DatabaseHandler.KEY_BRAND_CODE)));
                    configBean.setStrBrandName(crsrBrand.getString(crsrBrand.getColumnIndex(DatabaseHandler.KEY_BRAND_NAME)));
                    configBean.setiConfigMode(AppConstants.BRAND_CONFIG);
                    list.add(configBean);
                }while(crsrBrand.moveToNext());
            }
        } catch (Exception ex){
            Logger.i(TAG,ex.getMessage());
        } finally {
            if(crsrBrand != null){
                crsrBrand.close();
            }
        }

        if(true)//(list.size() > 0)
        {
            rvBrandList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvBrandList.setAdapter(new ConfigListAdapter(getActivity(),this, list));
        }
    }

    private void mAddBrand() {
        String strBrandName = edtBrandName.getText().toString().trim();
        int iBrandCode;
        if (strBrandName.equalsIgnoreCase("")) {
            msgBox.Show("Warning", "Please fill brand name before adding.");
            return;
        }

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getStrBrandName().toUpperCase().equals(strBrandName.toUpperCase())){
                    msgBox.Show("Warning", "Brand already present.");
                    return;
                }
            }
        }

        iBrandCode = HomeActivity.dbHandler.getBrandCode();
        Logger.d("InsertBrand", "Brand Code: " + String.valueOf(iBrandCode));
        iBrandCode++;
        mInsertBrand(iBrandCode, strBrandName);
        edtBrandName.setText("");
        mDisplayBrand();
    }

    private void mInsertBrand(int iBrandCode,String strBrandName){
        long lRowId;

        ConfigBean objDept = new ConfigBean();
        objDept.setlBrandCode(iBrandCode);
        objDept.setStrBrandName(strBrandName);

        lRowId = HomeActivity.dbHandler.addBrand(objDept);
        if(lRowId > -1){
            //msgBox.Show("Warning", "Brand data added successfully.");
            Toast.makeText(getActivity(), "Brand data added successfully", Toast.LENGTH_SHORT).show();
        } else {
            msgBox.Show("Warning", "Brand not able to add. Please try later.");
        }
        Logger.d("Brand","Row Id: " + String.valueOf(lRowId));
    }

    private void mEditBrand(){
        try {
            if(configBeanSelectedData != null && !edtBrandName.getText().toString().isEmpty()) {
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getStrBrandName().toUpperCase().equals(edtBrandName.getText().toString().trim().toUpperCase())){
                            if(configBeanSelectedData.getlBrandCode() != list.get(i).getlBrandCode())
                            {
                                msgBox.Show("Warning", "Brand already present.");
                                return;
                            }

                        }
                    }
                }
                int iResult = HomeActivity.dbHandler.updateBrand(""+configBeanSelectedData.getlBrandCode(), edtBrandName.getText().toString().trim());
                Logger.d("updateBrand", "Updated Rows: " + String.valueOf(iResult));
                if (iResult > 0) {
                    Toast.makeText(getActivity(), "Brand  updated successfully", Toast.LENGTH_SHORT).show();
                    configBeanSelectedData = null;
                    mDisplayBrand();
                    mClearWidget();
                } else {
                    msgBox.Show("Warning", "Update failed");
                }
            } else {
                msgBox.Show("Warning", "Please fill brand name before adding or please select on the list and try to update.");
            }
        }catch (Exception ex){
            Logger.i(TAG, ex.getMessage());
        }
    }

    private void mClearWidget(){
        edtBrandName.setText("");
        btnAdd.setEnabled(true);
        btnAdd.setBackgroundResource(R.color.widget_color);
        btnUpdate.setEnabled(false);
        btnUpdate.setBackgroundResource(R.color.disable_button);
    }

    @Override
    public void onRowDataSelect(ConfigBean configBean) {
        if(configBean != null){
            configBeanSelectedData = configBean;
            edtBrandName.setText(configBean.getStrBrandName());
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
                .setMessage(getString(R.string.brand_delete_msg))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        int brandCode = configBean.getlBrandCode();
                        long lResult = HomeActivity.dbHandler.DeleteBrand(brandCode);
                        Toast.makeText(getActivity(), "Brand Deleted Successfully", Toast.LENGTH_SHORT).show();
                        mDisplayBrand();
                        mClearWidget();
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
        edtBrandName.setFilters(new InputFilter[]{new EMOJI_FILTER()});
    }
}