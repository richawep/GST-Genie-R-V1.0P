package pos.wepindia.com.retail.view;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import pos.wepindia.com.retail.Constants;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.AppConstants;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.WepBaseActivity;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.utils.Preferences;


/**
 * Created by MohanN on 12/20/2017.
 */

public class LoginActivity extends WepBaseActivity implements Constants {


    private static final int HOME_RESULT = 1;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private String DOCUMENT_GENERATE_PATH = Environment.getExternalStorageDirectory().getPath() + "/WeP_Retail_Documents/";
    String FILENAME = "";

    @BindView(R.id.bt_login)  Button btnLogin;
    @BindView(R.id.bt_login_exit)  Button btnExit;
    @BindView(R.id.et_login_user_name)  EditText edtUserName;
    @BindView(R.id.et_login_password)   EditText edtPassword;
    @BindView(R.id.iv_login_company_logo)  ImageView imgLogo;
    @BindView(R.id.iv_login_help)          ImageView imgHelp;
    @BindView(R.id.cb_login_remember_me)   CheckBox cbRememberMe;
    @BindView(R.id.tv_login_error_message) TextView tv_login_error_message;
    @BindView(R.id.tv_pass_error_message)  TextView tv_pass_error_message;

    DatabaseHandler dbLogin;
    Context myContext;
    MessageDialog MsgBox;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Fabric.with(this, new Crashlytics());
        if (Logger.LOG_FOR_FIRST) {
            Logger.printLog();
            Logger.LOG_FOR_FIRST = false;
        }
        if (Auditing.AUDIT_FOR_FIRST) {
            Auditing.printAudit();
            Auditing.AUDIT_FOR_FIRST = false;
        }

        try{
            myContext = LoginActivity.this;
            dbLogin = new DatabaseHandler(LoginActivity.this);
            dbLogin.CreateDatabase();
            dbLogin.OpenDatabase();
            MsgBox = new MessageDialog(myContext);
            ButterKnife.bind(this);
            checkRememberUserName();

        }catch(Exception e)
        {
            Toast.makeText(myContext, "Oops some error occured", Toast.LENGTH_SHORT).show();
            Logger.d(Logger.getClassNameMethodNameAndLineNumber(),e.toString());
        }

        initSinglePrinter();

        Logger.d(TAG,"OnCreate() : Application launched");
    }

    @Override
    public void onHomePressed() {

    }


    @OnClick({R.id.bt_login,R.id.bt_login_exit,R.id.iv_login_company_logo,R.id.iv_login_help})
    protected void mButtonClick(View view) {
        switch (view.getId()) {
            case R.id.iv_login_company_logo : About(view);
                break;
            case R.id.iv_login_help : showOptions(view);
                break;
            case R.id.bt_login_exit : Close(view);
                break;

            case R.id.bt_login : ValidateLogIn(view);
                break;
        }
    }

    @OnFocusChange({R.id.et_login_user_name,R.id.et_login_password})
    protected void mFocusChange(View view,boolean hasFocus )
    {
        switch (view.getId()) {
            case R.id.et_login_user_name:
                if (hasFocus) tv_login_error_message.setText("");
                break;
            case R.id.et_login_password:
                if (hasFocus) tv_pass_error_message.setText("");
                break;
        }
    }

    private void initSinglePrinter() {
        sharedPreferences = Preferences.getSharedPreferencesForPrint(LoginActivity.this); // getSharedPreferences("PrinterConfigurationActivity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("kot","Heyday");
        editor.putString("bill","Heyday");
        editor.putString("report","Heyday");
        editor.putString("receipt","Heyday");
        editor.commit();
    }


    private void ValidateLogIn(View view)
    {
        Cursor cursorUser = null;
        String userNameTxt = edtUserName.getText().toString();
        String userPassTxt = edtPassword.getText().toString();
        if(userNameTxt.equalsIgnoreCase("") )
        {
            tv_login_error_message.setText(R.string.emptyUser);
        }else if ( userPassTxt.equalsIgnoreCase(""))
        {
            tv_pass_error_message.setText((R.string.emptyPassword));
        }
        else
        {
            sharedPreferences = getSharedPreferences(Constants.SHAREDPREFERENCE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if(cbRememberMe.isChecked()){
                editor.putString("userNameTxt",userNameTxt);
                editor.putBoolean("isChecked",true);
                editor.commit();
            }else {
                editor.putString("userNameTxt","");
                editor.putBoolean("isChecked",false);
                editor.commit();
            }

            cursorUser = dbLogin.getUser(userNameTxt,userPassTxt);
            try {
                if (cursorUser == null) {
                    MsgBox.Show("Login", "No user data");
                } else if (cursorUser.moveToFirst()) {
                    String userId = cursorUser.getString(cursorUser.getColumnIndex(DatabaseHandler.KEY_USER_ID));
                    String userName = cursorUser.getString(cursorUser.getColumnIndex(DatabaseHandler.KEY_USER_NAME));
                    String userRoleId = cursorUser.getString(cursorUser.getColumnIndex(DatabaseHandler.KEY_ROLE_ID)) + "";
                    int isActive = cursorUser.getInt(cursorUser.getColumnIndex(DatabaseHandler.KEY_isActive));
                    if(isActive == 0)
                    {
                        MsgBox.Show("Inactive User", "This user is inactive. ");
                        return;
                    }
                    try {
                        editor = sharedPreferences.edit();
                        editor.putInt(AppConstants.USER_ID, Integer.valueOf(userId));
                        editor.putString(AppConstants.USER_NAME, userName);
                        editor.putString(AppConstants.USER_ROLE, userRoleId);
                        editor.commit();
                    } catch (Exception e) {
                        Logger.e(Logger.getClassNameMethodNameAndLineNumber(), e.toString());
                        MsgBox.Show("Login", e.getMessage());
                    }

                    if(cursorUser != null){
                        cursorUser.close();
                    }

                    //finish()
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, HomeActivity.class);
                    intent.putExtra(USERROLEID,userRoleId);
                    intent.putExtra(USERID,userId);
                    intent.putExtra(USERNAME,userName);
                    Logger.d(TAG,"Logging into HomeActivity with credentials : username "+userName);
                    startActivityForResult(intent,HOME_RESULT);

                } else {
                    MsgBox.Show("Login", "Wrong user id or password");
                }
            }catch (Exception ex){
                Logger.e(Logger.getClassNameMethodNameAndLineNumber(), ex.toString());

            }finally {
                if(cursorUser != null){
                    cursorUser.close();
                }
            }
        }
    }
    private void About(View v) {
        String version ="0.0";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version ="0.0";
        }
        String strAboutMsg = "GST Genie Retail\nVersion:"+version+"\n\nAbout WeP Digital Limited." +
                "\n\n\tWeP Digital is the Digital Services arm of WeP Solutions Limited (WeP). WeP is an innovative, reliable and a dynamic company. WeP came into being as a result of entrepreneurial work culture in Wipro. It has a committed and experienced team, helping the company grow leaps and bounds. We have grown and diversified, having spread our roots in an array of different areas like Managed Printing Solutions (MPS), Manufacturing and distribution of IT peripherals, Retail Billing solutions, and Document Management Solutions." +
                "\n\n\tWeP has been a very dynamic and adaptable organization. We keep reinventing ourselves to adapt to the ever-changing technology by introducing new products to the market, based on the need of the hour. We are a self-reliant company for technology for both new products and its manufacturing." +
                "\n\n\tWe are the pioneers of retail printers and billing solutions. We have a large presence in the retail automation space in select segments. We have introduced a lot of innovative products in the retail space which help the small time store owners as well as high-end supermarkets to support their customers. We have innovative business models suiting all kinds of consumers based on their requirements. We are known for our reliability and dependability in the market. We are a pan-India company. So our clients can seek our support across the country and we will be there to serve them.";
        AlertDialog.Builder PickUpTender = new AlertDialog.Builder(this);
        PickUpTender
                .setIcon(R.mipmap.ic_company_logo)
                .setTitle("About")
                .setMessage(strAboutMsg)
                .setNeutralButton("OK", null)
                .show();
    }

    private void Close(View v) {
        finish();
    }

    private void checkRememberUserName() {
        sharedPreferences = getSharedPreferences(Constants.SHAREDPREFERENCE, Context.MODE_PRIVATE);
        cbRememberMe.setChecked(sharedPreferences.getBoolean("isChecked",false));
        edtUserName.setText(sharedPreferences.getString("userNameTxt",""));
    }

    void clear()
    {
        edtUserName.setText("");
        edtPassword.setText("");
        checkRememberUserName();
    }


    @Override
    public void onStart() {
        super.onStart();
        Logger.d(TAG, "Login Activity : OnStart() called");
        Auditing.d("Login Activity : OnStart() called");
        //clear();
    }

    // Download documents


    void showOptions(View v) {
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(this, imgHelp);
        droppyBuilder.addMenuItem(new DroppyMenuItem("Download Quick Start Guide"))
                .addMenuItem(new DroppyMenuItem("Download User Manual"))
                .addSeparator();
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                switch (id) {
                    case 0:
                        FILENAME = "Quick_Start_Guide";
                        new GenerateDocuments().execute();
                        break;
                    case 1:
                        FILENAME = "User_Manual";
                        new GenerateDocuments().execute();
                        break;
                }
            }
        });
        droppyBuilder.build().show();
    }


    class GenerateDocuments extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        int progress = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = 0;
            pd.setMessage("Copying document...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            progress = 1;

            try {
                File directory = new File(DOCUMENT_GENERATE_PATH);
                if (!directory.exists())
                    directory.mkdirs();

                InputStream isAssetDbFile = getApplicationContext().getAssets().open( FILENAME + ".pdf");
                OutputStream osNewDbFile = new FileOutputStream(DOCUMENT_GENERATE_PATH + FILENAME + ".pdf");
                byte[] bFileBuffer = new byte[1024];
                int iBytesRead = 0;

                while ((iBytesRead = isAssetDbFile.read(bFileBuffer)) > 0) {
                    osNewDbFile.write(bFileBuffer, 0, iBytesRead);
                }

                osNewDbFile.flush();
                osNewDbFile.close();
                isAssetDbFile.close();
                pd.dismiss();
                publishProgress();

            } catch (FileNotFoundException e) {
                pd.dismiss();
                progress = 3;
                publishProgress();
                e.printStackTrace();
            } catch (IOException e) {
                pd.dismiss();
                progress = 4;
                publishProgress();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if (progress == 1) {
                Toast.makeText(LoginActivity.this, "Document generated successfully! Path:" + DOCUMENT_GENERATE_PATH + FILENAME, Toast.LENGTH_LONG).show();
            } else if (progress == 3 || progress == 4){
                Toast.makeText(LoginActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(1 == progress )
            {
                if(!isPDFSupported(LoginActivity.this))
                {
                    MsgBox.Show("Error","No pdf reader found to open file");
                    return;
                }
                File file = new File(Environment.getExternalStorageDirectory().getPath() + "/EasyBill_Documents/" + FILENAME + ".pdf");
                Intent target = new Intent(Intent.ACTION_VIEW);
                target.setDataAndType(Uri.fromFile(file),"application/pdf");
                target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                Intent intent = Intent.createChooser(target, "Open File");
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    // Instruct the user to install a PDF reader here, or something
                    Toast.makeText(LoginActivity.this, "Please install a PDF reader to open the document.", Toast.LENGTH_LONG).show();
                }
            }
            progress = 2;
            pd.dismiss();

        }
    }

    public static boolean isPDFSupported( Context context ) {
        Intent i = new Intent( Intent.ACTION_VIEW );
        final File tempFile = new File( context.getExternalFilesDir( Environment.DIRECTORY_DOWNLOADS ), "test.pdf" );
        i.setDataAndType( Uri.fromFile( tempFile ), "application/pdf" );
        return context.getPackageManager().queryIntentActivities( i, PackageManager.MATCH_DEFAULT_ONLY ).size() > 0;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d("Destroy() called");
        Auditing.d("Login Activity : Destroy() called");
        if(dbLogin != null){
            dbLogin.close();
            dbLogin = null;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logger.d(TAG,"onActivityResult : requestCode "+resultCode+" resultCode "+resultCode);
        clear();
        if (resultCode == RESULT_OK) {


            /*if (requestCode == HOME_RESULT) {

            } else {
                edtUserName.setText("");
                edtPassword.setText("");
            }*/
        }
    }



}