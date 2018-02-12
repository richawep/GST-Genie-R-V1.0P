package pos.wepindia.com.retail.view.Configuration.department;

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
import pos.wepindia.com.wepbase.model.pojos.Department;

/**
 * Created by MohanN on 12/7/2017.
 */

public class DepartmentFragment extends Fragment implements RowItemSelectListener {

    private static final String TAG = DepartmentFragment.class.getName();

    View view;
    MessageDialog msgBox;
    Context myContext;

    @BindView(R.id.bt_config_dept_add)       Button btnAdd;
    @BindView(R.id.bt_config_dept_update)    Button btnUpdate;
    @BindView(R.id.bt_config_dept_clear)     Button btnClear;
    @BindView(R.id.rv_config_department)     RecyclerView rvDeptList;
    @BindView(R.id.et_config_dept_name)      EditText edtDeptName;

    List<ConfigBean> list = new ArrayList<ConfigBean>();

    ConfigBean configBeanSelectedData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.configuration_department_fragment, container, false);
        try{
            ButterKnife.bind(this,view);
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
        mDisplayDepartment();
    }

    @OnClick({R.id.bt_config_dept_add, R.id.bt_config_dept_update, R.id.bt_config_dept_clear})
    protected void OnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.bt_config_dept_add:
                mAddDept();
                break;
            case R.id.bt_config_dept_update:
                mEditDept();
                break;
            case R.id.bt_config_dept_clear:
                mClearWidget();
                break;
            default:
                break;
        }
    }

    private void mDisplayDepartment(){
        Cursor crsrDepartment = null;
        list.clear();
        try {
            crsrDepartment = HomeActivity.dbHandler.getAllDepartments();
            if(crsrDepartment != null && crsrDepartment.getCount() > 0 && crsrDepartment.moveToFirst()){
                do{
                   ConfigBean configBean = new ConfigBean();
                   configBean.setiID(crsrDepartment.getInt(crsrDepartment.getColumnIndex(DatabaseHandler.KEY_id)));
                   configBean.setlDeptCode(crsrDepartment.getInt(crsrDepartment.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)));
                   configBean.setStrDeptName(crsrDepartment.getString(crsrDepartment.getColumnIndex(DatabaseHandler.KEY_DepartmentName)));
                   configBean.setiConfigMode(AppConstants.DEPARTMENT_CONFIG);
                   list.add(configBean);
                }while(crsrDepartment.moveToNext());
            }
        } catch (Exception ex){
            Logger.i(TAG,ex.getMessage());
        } finally {
            if(crsrDepartment != null){
                crsrDepartment.close();
            }
        }

        if (true)//(list.size() > 0)
        {
            rvDeptList.setLayoutManager(new LinearLayoutManager(myContext));
            rvDeptList.setAdapter(new ConfigListAdapter(myContext,this, list));
        }
    }

    private void mAddDept() {
        String strDeptName = edtDeptName.getText().toString().trim();
        int iDeptCode;
        if (strDeptName.equalsIgnoreCase("")) {
            msgBox.Show("Warning", "Please fill department name before adding.");
            return;
        }

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getStrDeptName().toUpperCase().equals(strDeptName.toUpperCase())){
                    msgBox.Show("Warning", "Department already present.");
                    return;
                }
            }
        }

        iDeptCode = HomeActivity.dbHandler.getDeptCode();
        Logger.d("InsertDepartment", "Dept Code: " + String.valueOf(iDeptCode));
        iDeptCode++;
        mInsertDepartment(iDeptCode, strDeptName);
        edtDeptName.setText("");
        mDisplayDepartment();
    }

    private void mInsertDepartment(int iDeptCode,String strDeptName){
        long lRowId;

        Department objDept = new Department(strDeptName,iDeptCode);

        lRowId = HomeActivity.dbHandler.addDepartment(objDept);
        if(lRowId > -1){
            //msgBox.Show("Warning", "Department data added successfully.");
            Toast.makeText(myContext, "Department added successfully", Toast.LENGTH_SHORT).show();
        } else {
            msgBox.Show("Warning", "Department not able to add. Please try later.");
        }
        Logger.d("Department","Row Id: " + String.valueOf(lRowId));
    }

    private void mEditDept(){
        try {
            if(configBeanSelectedData != null && !edtDeptName.getText().toString().isEmpty()) {
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        if(list.get(i).getStrDeptName().toUpperCase().equals(edtDeptName.getText().toString().trim().toUpperCase())){
                            if(list.get(i).getlDeptCode() != configBeanSelectedData.getlDeptCode())
                            {
                                msgBox.Show("Warning", "Department already present.");
                                return;
                            }
                        }
                    }
                }
                int iResult = HomeActivity.dbHandler.updateDepartment(""+configBeanSelectedData.getlDeptCode(), edtDeptName.getText().toString().trim());
                Logger.d("updateDept", "Updated Rows: " + String.valueOf(iResult));
                if (iResult > 0) {
                    Toast.makeText(myContext, "Department updated successfully", Toast.LENGTH_SHORT).show();
                    configBeanSelectedData = null;
                    mDisplayDepartment();
                    mClearWidget();
                } else {
                    msgBox.Show("Warning", "Update failed");
                }
            } else {
                msgBox.Show("Warning", "Please fill department name before adding or please select on the list and try to update.");
            }
        }catch (Exception ex){
            Logger.i(TAG, ex.getMessage());
        }
    }

    private void mClearWidget(){
        edtDeptName.setText("");
        btnAdd.setEnabled(true);
        btnAdd.setBackgroundResource(R.color.widget_color);
        btnUpdate.setEnabled(false);
        btnUpdate.setBackgroundResource(R.color.disable_button);
    }

    @Override
    public void onRowDataSelect(ConfigBean configBean) {
        if(configBean != null){
            configBeanSelectedData = configBean;
            edtDeptName.setText(configBean.getStrDeptName());
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
                .setMessage(getString(R.string.department_delete_msg))
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        int deptCode = configBean.getlDeptCode();
                        long lResult = HomeActivity.dbHandler.DeleteDept(deptCode);
                        lResult = HomeActivity.dbHandler.DeleteCategByDeptCode(deptCode);
                        lResult = HomeActivity.dbHandler.DeleteItemByDeptCode(""+deptCode);
                        //MsgBox.Show("", "Department Deleted Successfully");
                        Toast.makeText(myContext, "Department Deleted Successfully", Toast.LENGTH_SHORT).show();
                        mDisplayDepartment();
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
        edtDeptName.setFilters(new InputFilter[]{new EMOJI_FILTER()});
    }
}