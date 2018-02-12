package pos.wepindia.com.retail.view.Configuration.category;

import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import pos.wepindia.com.wepbase.model.pojos.Category;
import pos.wepindia.com.wepbase.model.pojos.ConfigBean;
import pos.wepindia.com.wepbase.model.pojos.Department;


/**
 * Created by MohanN on 12/7/2017.
 */

public class CategoryFragment extends Fragment implements RowItemSelectListener {

    private static final String TAG = CategoryFragment.class.getName();

    View view;
    MessageDialog msgBox;
    Context myContext;

    @BindView(R.id.bt_config_category_add)     Button btnAdd;
    @BindView(R.id.bt_config_category_update)  Button btnUpdate;
    @BindView(R.id.bt_config_category_clear)   Button btnClear;
    @BindView(R.id.rv_config_category)         RecyclerView rvCategoryList;
    @BindView(R.id.et_config_category_name)    EditText edtCategoryName;
    @BindView(R.id.sp_config_category_dept_name)     Spinner spCateDept;

    List<ConfigBean> list = new ArrayList<ConfigBean>();
    private List<Department> listDept;

    ConfigBean configBeanSelectedData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.configuration_category_fragment, container, false);
        try {
            ButterKnife.bind(this, view);
            myContext = getActivity();
            msgBox = new MessageDialog(myContext);
            if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }
        } catch (Exception ex) {
            Logger.i(TAG, ex.getMessage());
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
        mLoadSpinnerData();
        mDisplayCategory();
    }

    @OnClick({R.id.bt_config_category_add, R.id.bt_config_category_update, R.id.bt_config_category_clear})
    protected void OnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.bt_config_category_add:
                mAddCateg();
                break;
            case R.id.bt_config_category_update:
                mEditCateg();
                break;
            case R.id.bt_config_category_clear:
                mClearWidget();
                break;
            default:
                break;
        }
    }

    private void mLoadSpinnerData() {
        listDept = HomeActivity.dbHandler.getAllDeptforCateg();
        if (listDept != null && listDept.size() > 0) {
            ArrayAdapter<Department> myAdapter = new ArrayAdapter<Department>(myContext, android.R.layout.simple_spinner_item, listDept);
            myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCateDept.setAdapter(myAdapter);
        }
    }

    private void mDisplayCategory() {
        Cursor crsrCategory = null;
        list.clear();
        try {
            crsrCategory = HomeActivity.dbHandler.getAllCategorywithDeptName();
            if (crsrCategory != null && crsrCategory.getCount() > 0 && crsrCategory.moveToFirst()) {
                do {
                    ConfigBean configBean = new ConfigBean();
                    configBean.setiID(crsrCategory.getInt(crsrCategory.getColumnIndex(DatabaseHandler.KEY_id)));
                    configBean.setlCategCode(crsrCategory.getInt(crsrCategory.getColumnIndex(DatabaseHandler.KEY_CategoryCode)));
                    configBean.setStrCategName(crsrCategory.getString(crsrCategory.getColumnIndex(DatabaseHandler.KEY_CategoryName)));
                    configBean.setlDeptCode(crsrCategory.getInt(crsrCategory.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)));
                    configBean.setStrDeptName(crsrCategory.getString(crsrCategory.getColumnIndex(DatabaseHandler.KEY_DepartmentName)));
                    configBean.setiConfigMode(AppConstants.CATEGORY_CONFIG);
                    list.add(configBean);
                } while (crsrCategory.moveToNext());
            }
        } catch (Exception ex) {
            Logger.i(TAG, ex.getMessage());
        } finally {
            if (crsrCategory != null) {
                crsrCategory.close();
            }
        }

        if (true)//(list.size() > 0)
        {
            rvCategoryList.setLayoutManager(new LinearLayoutManager(myContext));
            rvCategoryList.setAdapter(new ConfigListAdapter(myContext, this, list));
        }
    }

    public void mAddCateg() {
        String strCategName = edtCategoryName.getText().toString().trim();
        int iCategCode, iDeptCode = 0;
        Department department;
        if (strCategName.equalsIgnoreCase("")) {
            msgBox.Show("Warning", "Please Enter Category before adding");
            return;
        }
        if (spCateDept.getSelectedItem().toString().equalsIgnoreCase("Select")) {
            msgBox.Show("Warning", "Please Select Department before adding");
            return;
        }
        if (!(spCateDept.getSelectedItem() == null)) {
            department = (Department) spCateDept.getSelectedItem();
            iDeptCode = department.getDeptCode();
        }

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getStrCategName().toUpperCase().equals(strCategName.toUpperCase())) {
                    msgBox.Show("Warning", "Category already present.");
                    return;
                }
            }
        }
        iCategCode = HomeActivity.dbHandler.getCategCode();
        iCategCode++;
        if (iDeptCode > 0) {
            mInsertCategory(iCategCode, strCategName, iDeptCode);
        }
        mDisplayCategory();
    }

    private void mInsertCategory(int iCategCode, String strCategName, int iDeptCode) {
        long lRowId;

        Category objCateg = new Category(strCategName, iCategCode, iDeptCode);

        lRowId = HomeActivity.dbHandler.addCategory(objCateg);

        Logger.d("Category", "Row Id: " + String.valueOf(lRowId));
        if (lRowId > -1) {
            //msgBox.Show("Warning", "Category data added successfully.");
            Toast.makeText(myContext, "Category added successfully.", Toast.LENGTH_SHORT).show();
            mClearWidget();
        } else {
            msgBox.Show("Warning", "Category not able to add. Please try later.");
        }
    }

    public void mEditCateg(){
        Department department = null;
        try {
            if(configBeanSelectedData == null && edtCategoryName.getText().toString().isEmpty()) {
                msgBox.Show("Warning", "Please fill category name before adding or please select on the list and try to update.");
                return;
            }
            if(edtCategoryName.getText().toString().isEmpty())
            {
                msgBox.Show("Warning", "Please fill category name before adding or please select on the list and try to update.");
                return;
            }
            if (spCateDept.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                msgBox.Show("Warning", "Please Select Department before adding");
                return;
            }
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getStrCategName().toUpperCase().equals(edtCategoryName.getText().toString().trim().toUpperCase())) {
                        if(list.get(i).getlCategCode() != configBeanSelectedData.getlCategCode())
                        {
                            msgBox.Show("Warning", "Category already present.");
                            return;
                        }
                    }
                }
            }
            if (!(spCateDept.getSelectedItem() == null)) {
               department = (Department) spCateDept.getSelectedItem();
            }
            int iResult = HomeActivity.dbHandler.updateCategory(""+configBeanSelectedData.getlCategCode(),
                    edtCategoryName.getText().toString().trim(),department.getDeptCode());
            Logger.d("updateCategory", "Updated Rows: " + String.valueOf(iResult));
            if (iResult > 0) {
                Toast.makeText(myContext, "Category updated successfully", Toast.LENGTH_SHORT).show();
                configBeanSelectedData = null;
                mDisplayCategory();
                mClearWidget();
            } else {
                msgBox.Show("Warning", "Update failed");
            }
        }catch (Exception ex){
            Logger.i(TAG, ex.getMessage());
        }
    }

    private void mClearWidget(){
        edtCategoryName.setText("");
        spCateDept.setSelection(0);
        btnAdd.setEnabled(true);
        btnAdd.setBackgroundResource(R.color.widget_color);
        btnUpdate.setEnabled(false);
        btnUpdate.setBackgroundResource(R.color.disable_button);
    }

    @Override
    public void onRowDataSelect(ConfigBean configBean) {
        if(configBean != null){
            configBeanSelectedData = configBean;
            edtCategoryName.setText(configBeanSelectedData.getStrCategName());
            for(int i = 0; i < listDept.size(); i++){
                if(listDept.get(i).getDeptCode() == configBeanSelectedData.getlDeptCode()){
                    spCateDept.setSelection(i);
                }
            }
            btnUpdate.setEnabled(true);
            btnUpdate.setBackgroundResource(R.color.widget_color);
            btnAdd.setEnabled(false);
            btnAdd.setBackgroundResource(R.color.disable_button);
        }
    }

    @Override
    public void onRowDataDelete(final ConfigBean configBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                .setTitle("Delete")
                .setIcon(R.mipmap.ic_company_logo)
                .setMessage(getString(R.string.category_delete_msg))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        int categCode = configBean.getlCategCode();
                        long lResult = HomeActivity.dbHandler.DeleteCateg(categCode);
                        lResult = HomeActivity.dbHandler.DeleteItemByCategCode(categCode);
                        //MsgBox.Show("", "Category Deleted Successfully");
                        Toast.makeText(myContext, "Category Deleted Successfully", Toast.LENGTH_SHORT).show();
                        //mLoadSpinnerData();
                        mDisplayCategory();
                        mClearWidget();
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
        edtCategoryName.setFilters(new InputFilter[]{new EMOJI_FILTER()});
    }
}