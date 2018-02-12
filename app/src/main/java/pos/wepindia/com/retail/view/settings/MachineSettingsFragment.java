package pos.wepindia.com.retail.view.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.retail.view.LoginActivity;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;

/**
 * Created by MohanN on 12/6/2017.
 */

public class MachineSettingsFragment extends Fragment {

    private static final String TAG = MachineSettingsFragment.class.getName();

    Context myContext;
    MessageDialog MsgBox;
    private static final String DB_PATH = Environment.getExternalStorageDirectory().getPath() + "/WeP_Retail/";
    private static final String DB_NAME = "WeP_Retail_Database.db";

    @BindView(R.id.btn_RestoreDefault)  Button btn_RestoreDefault;
    @BindView(R.id.btn_DbBackup) Button btn_DbBackup;
    @BindView(R.id.btn_FactoryReset) Button btn_FactoryReset;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        View view =  inflater.inflate(R.layout.settings_machine_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try{
            if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }

            myContext = getActivity();
            MsgBox = new MessageDialog(myContext);
        }catch(Exception e)
        {
            Logger.e(TAG,e.getMessage());
        }
    }


    @OnClick({R.id.btn_RestoreDefault,R.id.btn_DbBackup,R.id.btn_FactoryReset})
    void OnClickButton(View view)
    {
        switch(view.getId())
        {
            case R.id.btn_RestoreDefault : RestoreDefault();
            break;
            case R.id.btn_DbBackup : CreateBackup();
            break;
            case  R.id.btn_FactoryReset : FactoryReset();
            break;
        }
    }
    private void CreateBackup() {
        int iResult = -1;
        long lDbLastModified = 0;

        File DbFile = new File(DB_PATH + DB_NAME);
        lDbLastModified = DbFile.lastModified();

        iResult = HomeActivity.dbHandler.BackUpDatabase();
        try {
            if (iResult == 1) {
                MsgBox.Show("Information", "Database backup has been created successfully");
            } else if (iResult == 0) {
                MsgBox.Show("Warning", "No backup created since Database is not updated after creating a backup");
            } else {
                MsgBox.Show("Warning", "Failed to create database backup");
            }
        } catch (Exception e) {
            Toast.makeText(myContext, "DATABACKUP : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void BackUp() {

        AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);

        LayoutInflater UserAuthorization = (LayoutInflater) myContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);

        AuthorizationDialog.setView(vwAuthorization)
                .show();

        final EditText txtUserId = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
        final EditText txtPassword = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);
        AuthorizationDialog
                .setTitle("Authorization")
                .setIcon(R.mipmap.ic_company_logo)
                .setView(vwAuthorization)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Cursor User = HomeActivity.dbHandler.getUser(txtUserId.getText().toString(), txtPassword.getText().toString());
                        if ((User != null) && (User.moveToFirst())) {
                            String roleId = User.getString(User.getColumnIndex(DatabaseHandler.KEY_ROLE_ID));
                            //ArrayList<String> list = HomeActivity.dbHandler.getPermissionsNamesForRole(roleName);
                            String str = "1";
                            Boolean status = false;
                            /*if (list == null)
                                Log.d("BackUp()", "No access to " + txtUserId.getText().toString());

                            for (String s : list) {
                                if (str.equalsIgnoreCase(s)) {
                                    Log.d("BackUp()", txtUserId.getText().toString() + " has access for backup");
                                    status = true;
                                    break;
                                }
                            }*/
                            if (str.equalsIgnoreCase(roleId)) {
                                Log.d("BackUp()", " creating backup");
                                int iResult = HomeActivity.dbHandler.BackUpDatabase();
                                try {
                                    /*if (iResult == 1) {
                                        MsgBox.Show("Information", "Database backup has been created successfully");
                                    } else if (iResult == 0) {
                                        MsgBox.Show("Warning", "No backup created since Database is not updated after creating a backup");
                                    } else {
                                        MsgBox.Show("Warning", "Failed to create database backup");
                                    }*/
                                }catch (Exception e)
                                {
                                    Logger.e(TAG,e.getMessage());
                                }
                                //dbBackup.DeleteAllTransaction();
                            } else {
                                MsgBox.Show("Warning", "Could not proceed due to in suffiecient access privilage");
                            }
                        } else {
                            MsgBox.Show("Warning", "Could not proceed due to wrong user id or password");
                        }
                    }
                })
                .show();
    }


    public void RestoreDefault() {
        AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);

        LayoutInflater UserAuthorization = (LayoutInflater) myContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);

        final EditText txtUserId = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
        final EditText txtPassword = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);

        AuthorizationDialog
                .setTitle("Authorization")
                .setIcon(R.mipmap.ic_company_logo)
                .setView(vwAuthorization)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Cursor User = HomeActivity.dbHandler.getUser(txtUserId.getText().toString(), txtPassword.getText().toString());
                        if ((User != null) && (User.moveToFirst())) {
                            String roleName = User.getString(User.getColumnIndex("RoleId"));
                            ArrayList<String> list = HomeActivity.dbHandler.getPermissionsNamesForRole(roleName);
                            String str = "1";
                            /*Boolean status = false;
                            if (list == null)
                                Log.d("BackUp()", "No access to " + txtUserId.getText().toString());

                            for (String s : list) {
                                if (str.equalsIgnoreCase(s)) {
                                    Log.d("BackUp()", txtUserId.getText().toString() + " has access for backup");
                                    status = true;
                                    break;
                                }
                            }*/
                            if (str.equalsIgnoreCase(roleName)) {
                                Log.d("RestoreDefault()", "Settings Restored");
                                HomeActivity.dbHandler.RestoreDefault();
                                Toast.makeText(myContext, "Settings Restored Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                MsgBox.Show("Warning", "Could not proceed due to in sufficient access privilage");
                            }
                        } else {
                            MsgBox.Show("Warning", "Could not proceed due to wrong user id or password");
                        }
                    }
                })
                .show();
    }

    public void FactoryReset() {
        AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);

        LayoutInflater UserAuthorization = (LayoutInflater) myContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);

        final EditText txtUserId = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
        final EditText txtPassword = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);


        AuthorizationDialog
                .setTitle("Authorization")
                .setIcon(R.mipmap.ic_company_logo)
                .setView(vwAuthorization)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Cursor User = HomeActivity.dbHandler.getUser(txtUserId.getText().toString(), txtPassword.getText().toString());
                        if ((User != null) && (User.moveToFirst())) {
                            String roleName = User.getString(User.getColumnIndex("RoleId"));
                            //ArrayList<String> list = HomeActivity.dbHandler.getPermissionsNamesForRole(roleName);
                            String str = "1";

                            if (str.equalsIgnoreCase(roleName)) {
                                Log.d("RestoreDefault()", "Factory Resetted");
                                //HomeActivity.dbHandler.FactoryReset();
                                String DB_PATH = Environment.getExternalStorageDirectory().getPath() + "/WeP_Retail/";
                                myContext.deleteDatabase(DB_PATH + "WeP_Retail_Database.db");
                                //Toast.makeText(myContext, "Factory Reset Successfully", Toast.LENGTH_LONG).show();
                                MsgBox.setIcon(R.mipmap.ic_company_logo)
                                        .setTitle("Note")
                                        .setMessage("Factory Reset Successfully")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                HomeActivity.dbHandler.CloseDatabase();
                                                Intent intent = new Intent(myContext, LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .show();
                                //System.exit(0);
                            } else {
                                MsgBox.Show("Warning", "Could not proceed due to in sufficient access privilage");
                            }
                        } else {
                            MsgBox.Show("Warning", "Could not proceed due to wrong user id or password");
                        }
                    }
                })
                .show();
    }


}