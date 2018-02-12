package pos.wepindia.com.retail.view.UserMasters;


import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.Constants;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.EMOJI_FILTER;
import pos.wepindia.com.retail.utils.Validations;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.retail.view.UserMasters.UserAdapterAndListener.OnUserManagementListener;
import pos.wepindia.com.retail.view.UserMasters.UserAdapterAndListener.UserManagementAdapter;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.User;

/**
 * Created by MohanN on 12/19/2017.
 */

public class UserManagementFragment extends Fragment implements OnUserManagementListener {

    private static final String TAG = UserManagementFragment.class.getName();
    @BindView(R.id.bt_um_submit)     Button btSubmit;
    @BindView(R.id.bt_um_delete)     Button btDelete;
    @BindView(R.id.bt_um_clear)      Button btClear;
    @BindView(R.id.et_um_name)       EditText edtName;
    @BindView(R.id.et_um_mobile)     EditText edtMobile;
    @BindView(R.id.et_um_login)      EditText edtLogin;
    @BindView(R.id.et_um_password)   EditText edtPassword;
    @BindView(R.id.et_um_designation)    EditText edtDesignation;
    @BindView(R.id.et_um_aadhar)      EditText edtAadhar;
    @BindView(R.id.et_um_email)       EditText edtEmail;
    @BindView(R.id.et_um_address)     EditText edtAddress;
    @BindView(R.id.sp_um_role_list)   Spinner spRoleList;
    @BindView(R.id.sc_user_management_dialog_active) android.support.v7.widget.SwitchCompat sc_dialog_active;
    @BindView(R.id.rv_um_list_data)     RecyclerView rvUserManagementDataList;
    @BindView(R.id.et_um_sales_man_id) EditText edtSalesManId;
    @BindView(R.id.tl_sales_man_id)
    TextInputLayout tlSalesManId;

    private List<String> listRoles;
    ArrayAdapter spinnerRoleAdapter;

    boolean mEditing = false;


    private List<User> usersList;
    private UserManagementAdapter usersAdapter;

    MessageDialog msgBox;
    private int userId = -1;
    Context myContext;

    int SALES_MAN_ID = 0;

    Map<String,String> mapRoleId = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.user_management_fragment, container, false);
        try {
            myContext = getActivity();
            msgBox = new MessageDialog(getActivity());
            ButterKnife.bind(this, rootView);
            //App crash error log
            if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }
        } catch (Exception ex) {
            Logger.i(TAG, "User management init onCreateView error." + ex.getMessage());
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("User Management");
        applyValidations();

        spRoleList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!mEditing) {
                    mEditing = true;
                    edtSalesManId.setEnabled(false);
                    edtSalesManId.setText("");
                    if (listRoles.get(position).toUpperCase().trim().equals(Constants.SALES_MAN_ID_KEY.toUpperCase().trim())) {
                        edtSalesManId.setEnabled(true);
                    }
                }
                mEditing = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                // sometimes you need nothing here
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            loadRoleMap();
            initAppSettings();
            listRoles = HomeActivity.dbHandler.getAllRoles();
            if (listRoles.size() > 0) {
                spinnerRoleAdapter = new ArrayAdapter(myContext, android.R.layout.simple_spinner_item, listRoles);
                spinnerRoleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spRoleList.setAdapter(spinnerRoleAdapter);
            }
            if(SALES_MAN_ID == 1){
                tlSalesManId.setVisibility(View.VISIBLE);
                edtSalesManId.setEnabled(false);
            }
            mDisplayUsersAndClearFields();
        } catch (Exception ex) {
            Logger.i(TAG, "unable to populate the user management spinner data from table. " + ex.getMessage());
        }

    }




    @OnClick({R.id.bt_um_submit, R.id.bt_um_delete, R.id.bt_um_clear})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_um_submit: mInsertOrUpdate();
                break;
            case R.id.bt_um_delete: mDeleteUser();
                break;
            case R.id.bt_um_clear: mClearValues();
                break;
            default:
                break;
        }
    }

    private  void mDisplayUsersAndClearFields()
    {
        mDisplayUsers();
        mClearValues();
    }
    private void mDisplayUsers() {
        try {
            usersList = HomeActivity.dbHandler.getAllUsers();
            if (usersList != null && usersList.size() > 0) {
                if(usersAdapter == null) {
                    usersAdapter = new UserManagementAdapter(myContext, this, usersList);
                    rvUserManagementDataList.setLayoutManager(new LinearLayoutManager(getActivity()));
                    DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                    rvUserManagementDataList.addItemDecoration(itemDecor);
                    rvUserManagementDataList.setAdapter(usersAdapter);
                }
                else{
                    usersAdapter.notifyNewDataAdded(usersList);
                }

            }
        } catch (Exception ex) {
            Logger.i(TAG, ex.getMessage());
        }

    }

    private void mInsertOrUpdate() {
        try{
            if (mValidateEditText(edtName) && mValidateEditText(edtMobile)
                    && mValidateEditText(edtLogin) && mValidateEditText(edtPassword)
                    && !spRoleList.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                if (!edtMobile.getText().toString().isEmpty()) {
                    if (edtMobile.getText().toString().trim().length() !=10) {
                        msgBox.Show("Invalid Information", "Please enter 10 digit mobile no.");
                        return;
                    }
                }
                if (!edtEmail.getText().toString().isEmpty()) {
                    if (!Validations.isValidEmailAddress(edtEmail.getText().toString().trim())) {
                        msgBox.Show("Invalid Information", "Please enter valid email address.");
                        return;
                    }
                }
                if (!edtAadhar.getText().toString().trim().isEmpty()) {
                    if (edtAadhar.getText().toString().trim().length() < 12) {
                        msgBox.Show("Invalid Information", "Please enter valid Aadhar number.");
                        return;
                    }
                }

                if(SALES_MAN_ID == 1 && spRoleList.getSelectedItem().toString().toUpperCase().trim().equals(Constants.SALES_MAN_ID_KEY.toUpperCase().trim())){
                    if(edtSalesManId.getText().toString().isEmpty()){
                        msgBox.Show("Information", "Please enter sales man id.");
                        return;
                    }
                } else {
                    edtSalesManId.setText("");
                }

                if (edtName.getText().toString().trim().equalsIgnoreCase("admin")) {
                    msgBox.Show("Error","Name cannot be admin");
                    return;
                }

                if(edtLogin.getText().toString().trim().equalsIgnoreCase("admin") && userId !=1)
                {
                    msgBox.Show("Error","Login cannot be admin");
                    return;
                }
                if(edtLogin.getText().toString().trim().equalsIgnoreCase("d#demo") ||edtPassword.getText().toString().trim().equalsIgnoreCase("d#demo") )
                {
                    msgBox.Show("Error", "Login or password cannot be d#demo");
                    return;
                }
                if(edtName.getText().toString().trim().equalsIgnoreCase("d#demo") ) {
                    msgBox.Show("Error", "Name cannot be d#demo");
                    return;
                }

                final User user = new User();

                user.setUserName(edtName.getText().toString().trim());
                user.setUserMobile(edtMobile.getText().toString().trim());
                user.setUserLogin(edtLogin.getText().toString().trim());
                user.setUserPassword(edtPassword.getText().toString().trim());
                String roleName = spRoleList.getSelectedItem().toString();
                String roleId = mapRoleId.get(roleName);
                user.setUserRole(roleId);
                if(mValidateEditText(edtDesignation)){
                    user.setUserDesignation(edtDesignation.getText().toString().trim());
                }
                if(mValidateEditText(edtAadhar)){
                    user.setUserAdhar(edtAadhar.getText().toString());
                }
                if(mValidateEditText(edtEmail)){
                    user.setUserEmail(edtEmail.getText().toString().trim());
                }
                if(mValidateEditText(edtAddress)){
                    user.setUserAddress(edtAddress.getText().toString().trim());
                }
                if(sc_dialog_active.isChecked()){
                    user.setIsActive(1);
                }
                else
                {
                    user.setIsActive(0);
                }

                if(SALES_MAN_ID == 1){
                    String StrSalesManID = edtSalesManId.getText().toString();
                    if(!StrSalesManID.isEmpty()){
                        user.setStrSalesManId(StrSalesManID);
                    }
                } else {
                    user.setStrSalesManId("");
                }
                if(userId >0)
                {

                    boolean bExistsStatus = false;
                    for(int i = 0; i < usersList.size() && !bExistsStatus; i++)
                    {
                        if(usersList.get(i).getUserLogin().toUpperCase().equals(edtLogin.getText().toString().trim().toUpperCase())
                                && usersList.get(i).getUserPassword().toUpperCase().equals(edtPassword.getText().toString().trim().toUpperCase())
                                && usersList.get(i).getId() !=userId){
                            bExistsStatus = true;
                            MessageDialog msgBox_temp = new MessageDialog(myContext);
                            final int ii =i;
                            msgBox_temp.setTitle("Duplicate")
                                    .setIcon(R.mipmap.ic_company_logo)
                                    .setMessage("User with this Login is already present. Do you want to update already present login")
                                    .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            user.setId(usersList.get(ii).getId());
                                            long lResult = HomeActivity.dbHandler.updateUser(user);
                                            if (lResult > -1) {
                                                msgBox.Show("Warning", "User data updated successfully.");
                                                mDisplayUsersAndClearFields();
                                                userId = -1;
                                            } else {
                                                msgBox.Show("Warning", "User not able to update. Please try later.");
                                            }
                                            return;
                                        } })
                                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            return;
                                        }
                                    })
                                    .show();
                        }
                    }
                    if(!bExistsStatus)
                    {
                        user.setId(userId);
                        long lResult = HomeActivity.dbHandler.updateUser(user);
                        if (lResult > -1) {
                            msgBox.Show("Warning", "User data updated successfully.");
                            mDisplayUsersAndClearFields();
                            userId = -1;
                        } else {
                            msgBox.Show("Warning", "User not able to update. Please try later.");
                        }
                    }
                }else // new user addition
                {
                    boolean bExistsStatus = false;
                    if(usersList != null && usersList.size() > 0){
                        for(int i = 0; i < usersList.size() && !bExistsStatus; i++){
                            if(usersList.get(i).getUserLogin().toUpperCase().equals(edtLogin.getText().toString().trim().toUpperCase())
                                    && usersList.get(i).getUserPassword().toUpperCase().equals(edtPassword.getText().toString().trim().toUpperCase())){
                                bExistsStatus = true;
                                final int ii =i;
                                MessageDialog msgBox_temp = new MessageDialog(myContext);
                                msgBox_temp.setTitle("Duplicate")
                                        .setIcon(R.mipmap.ic_company_logo)
                                        .setMessage("User with this Login is already present. Do you want to update it")
                                        .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                user.setId(usersList.get(ii).getId());
                                                long lResult = HomeActivity.dbHandler.updateUser(user);
                                                if (lResult > -1) {
                                                    msgBox.Show("Warning", "User data updated successfully.");
                                                    mDisplayUsersAndClearFields();
                                                    userId = -1;
                                                } else {
                                                    msgBox.Show("Warning", "User not able to update. Please try later.");
                                                }
                                                return;
                                            } })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                return;
                                            }
                                        })
                                        .show();
                            }
                        }
                    }

                    if(!bExistsStatus){
                        long lRowId;
                        userId = HomeActivity.dbHandler.getMaxUsersID();
                        Logger.d(TAG, "User ID: " + String.valueOf(userId));
                        userId++;
                        user.setId(userId);
                        if(userId > 0) {
                            lRowId = HomeActivity.dbHandler.addNewUser(user);
                            if (lRowId > -1) {
                                msgBox.Show("Warning", "User data added successfully.");
                                mDisplayUsersAndClearFields();
                            } else {
                                msgBox.Show("Warning", "User not able to add. Please try later.");
                            }
                            Logger.d("User Management", "Row Id: " + String.valueOf(lRowId));
                        } else {
                            Logger.i(TAG,"unable to get the user id from the users table.");
                        }
                    }
                }

            } else {
                msgBox.Show("Incomplete Information", "Please fill required details");
            }
        }catch (Exception e)
        {
            Logger.e(TAG,e.getMessage());
            Toast.makeText(myContext, "Some error occured while saving the data", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean mValidateEditText(EditText edt) {
        if (!edt.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }


    public void mClearValues() {
        /*if(edtLogin.getText().toString().equalsIgnoreCase("admin"))
        {
            edtName.setEnabled(true);
            edtLogin.setEnabled(true);
            edtPassword.setEnabled(true);
        }*/
        edtName.setText("");
        edtMobile.setText("");
        edtLogin.setText("");
        edtPassword.setText("");
        edtDesignation.setText("");
        edtAadhar.setText("");
        edtEmail.setText("");
        edtAddress.setText("");
        spRoleList.setSelection(0);
        userId = -1;
        sc_dialog_active.setChecked(true);
        edtSalesManId.setText("");
    }

    public void mDeleteUser() {
        if(edtLogin.getText().toString().trim().equalsIgnoreCase(""))
        {
            Toast.makeText(myContext, "Select user to delete", Toast.LENGTH_SHORT).show();
        }
        else if(edtLogin.getText().toString().trim().equalsIgnoreCase("admin"))
        {
            MessageDialog msgBox_temp = new MessageDialog(myContext);
            msgBox_temp.setTitle("Note")
                    .setIcon(R.mipmap.ic_company_logo)
                    .setMessage("This login cannot be deleted.")
                    .setNegativeButton("Cancel", null)
                    .show();
        }
        else {
            askForDelete();
        }
    }

    public void askForDelete()
    {
        MessageDialog msgBox_temp = new MessageDialog(myContext);
        msgBox_temp.setTitle("Confirmation")
                .setIcon(R.mipmap.ic_company_logo)
                .setMessage("Are you sure want to delete?")
                .setNegativeButton("Cancel",null)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        HomeActivity.dbHandler.deleteUser(edtLogin.getText().toString().trim());
                        mDisplayUsersAndClearFields();
                        dialog.dismiss();
                    } })
                .show();
    }

    @Override
    public void onRowDataSelect(User user) {
        try{
            if(user.getId() ==1)
            {
                msgBox.Show("Note",getString(R.string.um_default_user_click_error_message));
                return;
            }
            edtName.setText(user.getUserName());
            edtMobile.setText(user.getUserMobile());
            edtLogin.setText(user.getUserLogin());
            edtPassword.setText(user.getUserPassword());
            edtDesignation.setText(user.getUserDesignation());
            edtAadhar.setText(user.getUserAdhar());
            edtEmail.setText(user.getUserEmail());
            edtAddress.setText(user.getUserAddress());
            userId = user.getId();
            if(SALES_MAN_ID == 1){
                tlSalesManId.setVisibility(View.VISIBLE);
                edtSalesManId.setEnabled(true);
                edtSalesManId.setText(user.getStrSalesManId());
            } else{
                tlSalesManId.setVisibility(View.INVISIBLE);
                edtSalesManId.setEnabled(false);
                edtSalesManId.setText("");
            }
            mEditing = true;
            String roleName = getRoleNameforId(user.getUserRole());
            spRoleList.setSelection(getIndexRoleList(roleName));
            if(user.getIsActive() ==1)
                sc_dialog_active.setChecked(true);
            else
                sc_dialog_active.setChecked(false);
        }catch (Exception e)
        {
            Logger.e(TAG, e.getMessage());
            Toast.makeText(myContext, "Some error occured while populating data", Toast.LENGTH_SHORT).show();
        }finally {
        }
    /*if(user.getUserLogin().equalsIgnoreCase("admin"))
    {
        edtName.setEnabled(false);
        edtLogin.setEnabled(false);
        edtPassword.setEnabled(false);
    }*/
    }


    void applyValidations()
    {
        edtName.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtLogin.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtPassword.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtDesignation.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtEmail.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtAddress.setFilters(new InputFilter[]{new EMOJI_FILTER()});
    }


    int getIndexRoleList(String roleName)
    {
        int index = 0;
        for (int i = 0; index==0 && i < spRoleList.getCount(); i++){

            if (spRoleList.getItemAtPosition(i).toString().equals(roleName)){
                index = i;
            }

        }

        return index;
    }
    String getRoleNameforId(String roleId)
    {
        String roleName = "";
        for (Map.Entry<String, String> entry : mapRoleId.entrySet()) {
            if (entry.getValue().equals(roleId)) {
                roleName = (entry.getKey());
                break;
            }
        }
        return roleName;
    }
    void loadRoleMap ()
    {
        mapRoleId .clear();
        Cursor cursor = HomeActivity.dbHandler.getAllRolesData();
        try {
            while (cursor != null && cursor.moveToNext()) {
                String roleId = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ROLE_ID));
                String roleName = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ROLE_NAME));
                mapRoleId.put(roleName, roleId);
            }
        }catch (Exception ex){
            Logger.i(TAG,ex.getMessage());
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }

    void initAppSettings ()
    {
        Cursor crsrSettings = null;
        try {
            crsrSettings = HomeActivity.dbHandler.getBillSettings();
            if (crsrSettings != null && crsrSettings.moveToFirst()) {
                SALES_MAN_ID = crsrSettings.getInt(crsrSettings.getColumnIndex(DatabaseHandler.KEY_SALES_MAN_ID));
            }
        } catch (Exception e) {
            Logger.i(TAG,"Settings init() error on user management screen. " +e.getMessage());
        }finally {
            if(crsrSettings != null){
                crsrSettings.close();
            }
        }
    }
    @Override
    public void onRowDataDelete(User user) {

    }
}