package pos.wepindia.com.retail.view.UserMasters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
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
import pos.wepindia.com.retail.Constants;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.EMOJI_FILTER;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.retail.view.UserMasters.UserAdapterAndListener.AccessPermissionRoleListener;
import pos.wepindia.com.retail.view.UserMasters.UserAdapterAndListener.AddRoleListAdapter;
import pos.wepindia.com.retail.view.UserMasters.UserAdapterAndListener.RecyclerItemListener;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.pojos.AccessPermissionRoleBean;
import pos.wepindia.com.wepbase.model.pojos.AddRoleBean;


/**
 * Created by MohanN on 12/18/2017.
 */

public class AddRoleFragment extends Fragment implements Constants,RecyclerItemListener.RecyclerTouchListener,
        AccessPermissionRoleListener {

    private static final String TAG = AddRoleFragment.class.getName();
    private final int INSERT = 1;
    private final int UPDATE = 2;

    @BindView(R.id.bt_ar_add_role)              Button btnAddRole;
    @BindView(R.id.bt_ar_update_permission)     Button btnUpdate;
    @BindView(R.id.bt_ar_delete_role)           Button btnDelete;
    @BindView(R.id.bt_ar_clear)                 Button btnClear;
    @BindView(R.id.et_add_role)                 TextInputEditText edtAddRole;
    @BindView(R.id.rv_ar_role_list)             RecyclerView rvRoleList;
    @BindView(R.id.rv_ar_access_permission_for_role)     RecyclerView rvAccessPermissionForRole;

    List<AddRoleBean> listRole = new ArrayList<AddRoleBean>();
    List<AddRoleBean> listAccessPermission = new ArrayList<AddRoleBean>();
    List<AccessPermissionRoleBean> accessPermissionRoleBeanList = new ArrayList<AccessPermissionRoleBean>();

    int iRoleID = -1;

    View rootView;
    MessageDialog msgBox;
    Context myContext;

    String loggedInUserRoleId;
    String loggedInUserName;

    private AddRoleListAdapter addRoleListAdapterAccessPermission;
    private AddRoleListAdapter addRoleListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_role_fragment, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.add_role));
        try {
            myContext = getActivity();
            msgBox = new MessageDialog(myContext);
            //App crash error log
            if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }

            if (getArguments() != null) {
                loggedInUserRoleId = getArguments().getString(USERROLEID);
                loggedInUserName = getArguments().getString(USERNAME);
            }
            applyValidations();
        } catch (Exception ex) {
            Logger.i(TAG, "Unable init add role fragment. " + ex.getMessage());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public void onResume() {
        super.onResume();
        displayRoleList();
        displayRoleAccessPermission();
        mClear();
    }

    @OnClick({R.id.bt_ar_add_role, R.id.bt_ar_update_permission, R.id.bt_ar_delete_role, R.id.bt_ar_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_ar_add_role:
                mInsertOrUpdateData(INSERT);
                break;
            case R.id.bt_ar_update_permission:
                if(iRoleID == 1){
                    msgBox.Show("Warning", "Admin role cannot be update.");
                    return;
                }
                mInsertOrUpdateData(UPDATE);
                break;
            case R.id.bt_ar_delete_role:
                mDeleteRole();
                break;
            case R.id.bt_ar_clear:
                mClear();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClickItem(View v, int position, int mode) {
        switch (mode) {
            case Constants.ROLE_LIST:
                AddRoleBean addRoleBean = listRole.get(position);
                //Toast.makeText(getContext(), addRoleBean.getStrData(), Toast.LENGTH_LONG).show();
                /*if(addRoleBean.getiRoleId()>0 && addRoleBean.getiRoleId()<4)
                    edtAddRole.setEnabled(false);
                else
                    edtAddRole.setEnabled(true);*/

                edtAddRole.setEnabled(false);
                edtAddRole.setText(addRoleBean.getStrData());
                accessPermissionRoleBeanList.clear();
                accessPermissionRoleBeanList = HomeActivity.dbHandler.getAccessPermissionRole(addRoleBean.getiRoleId());
                getDefaultAccessPermissionData(addRoleBean.getiRoleId());
                displayRoleAccessPermission();

                btnAddRole.setEnabled(false);
                btnAddRole.setBackgroundResource(R.color.disable_button);
                btnUpdate.setEnabled(true);
                btnUpdate.setBackgroundResource(R.color.widget_color);
                break;
            case Constants.ACCESS_PERMISSION_FOR_ROLE:
                AddRoleBean addRoleBeanAccessPer = listAccessPermission.get(position);
                addRoleBeanAccessPer.setStatus(addRoleBeanAccessPer.isStatus());
                //Toast.makeText(getContext(), addRoleBeanAccessPer.getStrData(), Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLongClickItem(View v, int position, int mode) {
        switch (mode) {
            case Constants.ROLE_LIST:
                break;
            case Constants.ACCESS_PERMISSION_FOR_ROLE:
                break;
            default:
                break;
        }
    }

    public void mInsertOrUpdateData(int iInsertUpdateMode) {
        if (!mValidateEditText(edtAddRole)) {
            msgBox.Show("Error", "Please enter a role");
            return;
        }
        if (listAccessPermission != null && listAccessPermission.size() > 0) {
            boolean bCheckBoxValidation = false;
            for (int i = 0; i < listAccessPermission.size(); i++) {
                if (listAccessPermission.get(i).isStatus()) {
                    bCheckBoxValidation = true;
                    break;
                }
            }
            if (!bCheckBoxValidation) {
                msgBox.Show("Error", "Select Permissions.");
                return;
            }
        } else {
            msgBox.Show("Error", "Access permission for role list is not present.");
            return;
        }

        switch (iInsertUpdateMode) {
            case INSERT:
                for (int k = 0; k < listRole.size(); k++) {
                    if (listRole.get(k).getStrData().toUpperCase().trim()
                            .equals(edtAddRole.getText().toString().toUpperCase().trim())) {
                        msgBox.Show("Error", "Role already exists.");
                        return;
                    }
                }
                int iRoleIDTemp = 0;
                try {
                    iRoleIDTemp = HomeActivity.dbHandler.getMaxRoleId();
                    iRoleIDTemp++;
                } catch (Exception ex) {
                    Logger.i(TAG, "Error on insert user role. " + ex.getMessage());
                }
                AddRoleBean addRoleBean = new AddRoleBean();
                addRoleBean.setiRoleId(iRoleIDTemp);
                addRoleBean.setStrData(edtAddRole.getText().toString());
                long lStatus = HomeActivity.dbHandler.addRole(addRoleBean);
                if (lStatus > -1) {
                    //msgBox.Show("Message", "User role data added successfully.");
                    long lResultStatus = -1;
                    for (int l = 0; l < listAccessPermission.size(); l++) {
                        if (listAccessPermission.get(l).isStatus()) {
                            int iAccessRoleID = HomeActivity.dbHandler.getRoleIdFromUserRoleAccess();
                            iAccessRoleID++;
                            addRoleBean = new AddRoleBean();
                            addRoleBean.setiRoleAccessId(iAccessRoleID);
                            addRoleBean.setiRoleId(iRoleIDTemp);
                            addRoleBean.setStrData(listAccessPermission.get(l).getStrData());
                            lResultStatus = HomeActivity.dbHandler.addAccessPermissionRole(addRoleBean);
                        }
                    }
                    String strDBStatus = "Unable able to add new role. Please try again later.";
                    if(lResultStatus > -1){
                        strDBStatus = "New role added successfully.";
                    }
                    Toast.makeText(getActivity(),strDBStatus,Toast.LENGTH_LONG).show();
                    displayRoleList();
                    mClear();
                } else {
                    msgBox.Show("Error", "User role not able to add. Please try later.");
                }
                break;
            case UPDATE:
                long lResultStatus = -1;
                boolean bStatus = false;
                for (int l = 0; l < listAccessPermission.size(); l++) {
                    if (listAccessPermission.get(l).isStatus()) {
                        if (listAccessPermission.get(l).getiRoleAccessId() == 0) {
                            int iAccessRoleID = HomeActivity.dbHandler.getRoleIdFromUserRoleAccess();
                            iAccessRoleID++;
                            addRoleBean = new AddRoleBean();
                            addRoleBean.setiRoleAccessId(iAccessRoleID);
                            addRoleBean.setiRoleId(iRoleID);
                            //addRoleBean.setStrData(edtAddRole.getText().toString());
                            addRoleBean.setStrData(listAccessPermission.get(l).getStrData());
                            lResultStatus = HomeActivity.dbHandler.addAccessPermissionRole(addRoleBean);
                            bStatus = true;
                        }
                    } else {
                        if (listAccessPermission.get(l).getiRoleAccessId() > 0) {
                            lResultStatus = HomeActivity.dbHandler.updateAccessPermissionRole(listAccessPermission.get(l));
                            bStatus = true;
                        }
                    }
                }
                String strDBStatus = "Unable able to update. Please try again later.";
                if(lResultStatus > -1){
                    strDBStatus = "Updated successfully.";
                }
                if(bStatus) {
                    Toast.makeText(getActivity(), strDBStatus, Toast.LENGTH_LONG).show();
                    displayRoleList();
                    mClear();
                } else {
                    Toast.makeText(getActivity(), "Please remove or add access permission and try to update.", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

    }

    public void displayRoleList() {
        listRole.clear();
        listRole = HomeActivity.dbHandler.getAllRolesForAddRole();
        if (listRole != null && listRole.size() > 0) {
            if (addRoleListAdapter == null) {
                rvRoleList.setLayoutManager(new LinearLayoutManager(getActivity()));
                DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                rvRoleList.addItemDecoration(itemDecor);
                addRoleListAdapter = new AddRoleListAdapter(this, listRole, Constants.ROLE_LIST);
                rvRoleList.setAdapter(addRoleListAdapter);

                RecyclerItemListener recyclerItemListener = new RecyclerItemListener(getActivity(), rvRoleList, this, Constants.ROLE_LIST);
                rvRoleList.addOnItemTouchListener(recyclerItemListener);
            } else {
                addRoleListAdapter.notify(listRole);
            }
        }
    }

    public void getDefaultAccessPermissionData(int iRoleIDTemp) {
        if (iRoleIDTemp > 0) {
            iRoleID = iRoleIDTemp;
        }
        listAccessPermission.clear();
        listAccessPermission.add(new AddRoleBean(Constants.BILLING, false));
        listAccessPermission.add(new AddRoleBean(Constants.ITEM_MASTER, false));
        listAccessPermission.add(new AddRoleBean(Constants.CUSTOMER_MASTER, false));
        listAccessPermission.add(new AddRoleBean(Constants.USER_MANAGEMENT, false));
        listAccessPermission.add(new AddRoleBean(Constants.ADD_ROLE, false));
        listAccessPermission.add(new AddRoleBean(Constants.PRICE_STOCK, false));
        listAccessPermission.add(new AddRoleBean(Constants.CONFIGURATION, false));
        /*listAccessPermission.add(new AddRoleBean(Constants.SUPPLIER_DETAILS, false));
        listAccessPermission.add(new AddRoleBean(Constants.SUPPLIER_ITEM_LINKAGE, false));
        listAccessPermission.add(new AddRoleBean(Constants.PURCHASE_ORDER, false));
        listAccessPermission.add(new AddRoleBean(Constants.GOODS_INWARD_NOTE, false));*/
        listAccessPermission.add(new AddRoleBean(Constants.REPORTS, false));
        listAccessPermission.add(new AddRoleBean(Constants.SETTINGS, false));
        listAccessPermission.add(new AddRoleBean(Constants.DAYEND, false));

        if (accessPermissionRoleBeanList.size() > 0) {
            for (int i = 0; i < listAccessPermission.size(); i++) {
                for (int j = 0; j < accessPermissionRoleBeanList.size(); j++) {
                    if (listAccessPermission.get(i).getStrData().toUpperCase().trim()
                            .equals(accessPermissionRoleBeanList.get(j).getStrAccessName().toUpperCase().trim())) {
                        listAccessPermission.get(i).setStatus(true);
                        listAccessPermission.get(i).setiRoleId(accessPermissionRoleBeanList.get(j).getiRoleId());
                        listAccessPermission.get(i).setiRoleAccessId(accessPermissionRoleBeanList.get(j).getiRoleAccessId());
                    }
                }
            }
            addRoleListAdapterAccessPermission.notify(listAccessPermission);
        }
    }

    public void displayRoleAccessPermission() {
        getDefaultAccessPermissionData(0);
        if (listAccessPermission != null && listAccessPermission.size() > 0) {
            rvAccessPermissionForRole.setLayoutManager(new LinearLayoutManager(getActivity()));
            addRoleListAdapterAccessPermission = new AddRoleListAdapter(this, listAccessPermission, Constants.ACCESS_PERMISSION_FOR_ROLE);
            rvAccessPermissionForRole.setAdapter(addRoleListAdapterAccessPermission);
        }
    }

    private void mDeleteRole() {
        if(iRoleID<1)
        {
            msgBox.Show("Warning", "Please select role and try again.");
        }
        else if (iRoleID >0 && iRoleID<=4) {
            msgBox.Show("Warning", "Default roles cannot be deleted.");
        }
        else if(Integer.parseInt(loggedInUserRoleId) == iRoleID)
        {
            String roleName = edtAddRole.getText().toString().trim();
            String msg = "You are logged in with user name \""+loggedInUserName+"\" which is assigned role \""+roleName+"\".\n" +
                    "If you want to delete this role, then kindly login with different user name, who is not assigned this role.";
            msgBox.Show("Warning",msg);
        }
        else
        {
            String msg = "Are you sure want to delete? \nPlease note all the logins for this role(if any) will also be deleted.";
            AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
            builder.setTitle("Confirmation")
                    .setIcon(R.mipmap.ic_company_logo)
                    .setMessage(msg)
                    .setNegativeButton("Cancel",null)
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            if (HomeActivity.dbHandler.deleteRole(iRoleID) > -1) {
                                int deleteuserName = HomeActivity.dbHandler.deleteUser(iRoleID);
                                if(deleteuserName > -1){
                                    Toast.makeText(getActivity(), "Deleted successfully.", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(), "Delete unsuccessful. Please try again later.", Toast.LENGTH_LONG).show();
                                }
                                displayRoleList();
                                mClear();
                                dialog.dismiss();
                            }
                        }  });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private boolean mValidateEditText(EditText edt) {
        if (!edt.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    private void mClear() {
        iRoleID = -1;
        edtAddRole.setText("");
        edtAddRole.setEnabled(true);
        accessPermissionRoleBeanList.clear();
        for(int i=0; i< listAccessPermission.size();i++)
        {
            listAccessPermission.get(i).setStatus(false);
        }
        addRoleListAdapterAccessPermission.notify(listAccessPermission);
        btnAddRole.setEnabled(true);
        btnAddRole.setBackgroundResource(R.color.widget_color);
        btnUpdate.setEnabled(false);
        btnUpdate.setBackgroundResource(R.color.disable_button);
    }


    void applyValidations()
    {
        edtAddRole.setFilters(new InputFilter[]{new EMOJI_FILTER()});

    }

    @Override
    public void onItemSelected(int iPosition) {
        listAccessPermission.get(iPosition).setStatus(true);
    }

    @Override
    public void onItemUnSelected(int iPosition) {
        listAccessPermission.get(iPosition).setStatus(false);
    }
}