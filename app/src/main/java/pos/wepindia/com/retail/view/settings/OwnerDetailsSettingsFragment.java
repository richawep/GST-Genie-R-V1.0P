package pos.wepindia.com.retail.view.settings;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.EMOJI_FILTER;
import pos.wepindia.com.retail.utils.Validations;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;


/**
 * Created by MohanN on 12/5/2017.
 */

public class OwnerDetailsSettingsFragment extends Fragment {

    private static final String TAG = OwnerDetailsSettingsFragment.class.getName();

    private final int PHONE = 1;
    private final int EMAIL = 2;
    private final int GSTIN = 3;

    //private static DatabaseHelper dbHelper;
    View view;

    @BindView(R.id.et_od_firm_name)     EditText edtFirmName;
    @BindView(R.id.et_od_phone)         EditText edtPhone;
    @BindView(R.id.et_od_email)         EditText edtEmail;
    @BindView(R.id.et_od_address)       EditText edtAddress;
    @BindView(R.id.et_od_gstin)         EditText edtGSTIN;
    @BindView(R.id.et_od_reference_no)  EditText edtRefNo;
    @BindView(R.id.et_od_bill_no_prefix)    EditText edtBillNoPrefix;
    @BindView(R.id.sp_od_pos)           Spinner spnPOS;
    @BindView(R.id.sp_od_is_main_office) Spinner spnIsMainOffice;
    @BindView(R.id.bt_od_apply)          Button btnApply;

    String[] arrayPOS;
    String[] arrayIsMainOffice = {"Select", "Yes", "No"};

    MessageDialog MsgBox;
    Context myContext ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_owner_details_fragment, container, false);

        myContext = getContext();
        MsgBox = new MessageDialog(myContext);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try{
            //App crash error log
            if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }
            loadSpinnerData();
            TextChangeListener();
            applyValidations();

        }catch (Exception e)
        {
            Logger.e(TAG, e.getMessage());
        }

    }


    @OnClick({R.id.bt_od_apply})
    protected void OnClickEvent(View view) {
        switch (view.getId()) {
            case R.id.bt_od_apply:
                   mInsertOrUpdate();
                break;
            default:
                break;
        }
    }

    private void mPopulateDataOnWidget() {
        Cursor cursorOwnerDetails = null;
        try{
            cursorOwnerDetails = HomeActivity.dbHandler.getOwnerDetail();
            if(cursorOwnerDetails != null && cursorOwnerDetails.getCount() > 0){
               cursorOwnerDetails.moveToFirst();
               if(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_FIRM_NAME)) != null){
                   edtFirmName.setText(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_FIRM_NAME)));
               }
                if(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_PhoneNo)) != null){
                    edtPhone.setText(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_PhoneNo)));
                }
                if(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_USER_EMAIL)) != null){
                    edtEmail.setText(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_USER_EMAIL)));
                }
                if(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_Address)) != null){
                    edtAddress.setText(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_Address)));
                }
                if(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_GSTIN)) != null){
                    edtGSTIN.setText(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_GSTIN)));
                }
                if(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_REFERENCE_NO)) != null){
                    edtRefNo.setText(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_REFERENCE_NO)));
                }
                if(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_BillNoPrefix)) != null){
                    edtBillNoPrefix.setText(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_BillNoPrefix)));
                }
                if(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_POS)) != null){
                    spnPOS.setSelection(getIndex_pos(cursorOwnerDetails.getString(cursorOwnerDetails.getColumnIndex(DatabaseHandler.KEY_POS))));
                }

            }
        } catch (Exception ex){
            Log.e(TAG,"Unable able to get data from the table owners table " +ex.getMessage());
        }finally {
            if(cursorOwnerDetails != null){
                cursorOwnerDetails.close();
            }
        }
    }

    private void mInsertOrUpdate() {

        if (mValidateEditText(edtFirmName) && mValidateEditText(edtPhone) && mValidateEditText(edtGSTIN)
                && mValidateEditText(edtEmail) && mValidateEditText(edtAddress)
                && spnPOS.getSelectedItemPosition() !=0) {

            if (!mDataValidation(GSTIN)) {
                MsgBox.Show("Invalid Information", "Please Enter Valid GSTIN");
                return;
            }
            if (mDataValidation(EMAIL) && mDataValidation(PHONE)) {

                String gstin = edtGSTIN.getText().toString().toUpperCase().trim();
                String name = edtFirmName.getText().toString();
                String phone =edtPhone.getText().toString();
                String pos= spnPOS.getSelectedItem().toString();
                String email =edtEmail.getText().toString();
                String RefernceNo = edtRefNo.getText().toString();
                String billPrefix = edtBillNoPrefix.getText().toString();
                String address = edtAddress.getText().toString();
                String  isMainOffice = "Yes";

                int length = pos.length();
                String posCode = "";
                if (length > 0) {
                    posCode = pos.substring(length - 2, length);
                }else
                {
                    if (!gstin.equals(""))
                        posCode =pos.substring(length - 2, length);
                }
                int delDetails = HomeActivity.dbHandler.deleteOwnerDetails();
                long lDataStored = HomeActivity.dbHandler.addOwnerDetails( name,  gstin, phone, email,  address, posCode, isMainOffice,
                                              RefernceNo, billPrefix);
                if(lDataStored > -1)
                {
                    //mClearWidgets();
                    Toast.makeText(getActivity(),"Data stored successfully.",Toast.LENGTH_SHORT).show();
                    edtGSTIN.setText(gstin);
                } else {
                    Toast.makeText(getActivity(),"Failed to save. Please try again.",Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            MsgBox.Show("Incomplete Information", "Please fill required details");
        }
    }

    private boolean mValidateEditText(EditText edt) {
        if (!edt.getText().toString().isEmpty()) {
            return true;
        }
        return false;
    }

    private boolean mDataValidation(int mode) {
        boolean bResult = false;
        switch (mode) {
            case EMAIL:
                bResult = Validations.isValidEmailAddress(edtEmail.getText().toString());
                if (!bResult) {
                    MsgBox.Show("Invalid Information", "Please Enter Valid Email id");
                }
                break;
            case PHONE:
                if (edtPhone.getText().toString().trim().length() == 10) {
                    bResult = true;
                } else {
                    MsgBox.Show("Invalid Information", "Phone no cannot be less than 10 digits.");
                }
                break;
            case GSTIN: String gstin = edtGSTIN.getText().toString().trim();
                if(Validations.checkGSTINValidation(gstin)) {
                    bResult = Validations.checkValidStateCode(gstin, myContext);
                }
                break;
            default:
                break;
        }
        return bResult;
    }

   /* public boolean isDataAvailableInDB(){
        boolean bResult = false;
        Cursor cursorDataCheck = null;
        String strTableName = OwnerDetailTable.OWNER_DETAILS.OWNER_DETAILS_TABLE_NAME.getFieldName();
        String[] strColumns = {DatabaseConstants._ID};
        String strSelection = DatabaseConstants._ID +"=?";
        String[] strArg = {"1"};
        try{
            cursorDataCheck = AppSettingsFragment.dbHelper.mGeneralQuery(strTableName, strColumns, strSelection, strArg,null,null, null, null);
            if(cursorDataCheck != null && cursorDataCheck.getCount() > 0){
                cursorDataCheck.moveToFirst();
                bResult = true;
            }
        }catch (Exception ex){
            Log.i(TAG, "Unable to check data exists or not in table " +strTableName + " " +ex.getMessage());
        } finally {
            if(cursorDataCheck != null && !cursorDataCheck.isClosed()){
                cursorDataCheck.close();
            }
        }
        return bResult;
    }*/

    private void mClearWidgets(){
        edtFirmName.setText("");
        edtPhone.setText("");
        edtEmail.setText("");
        edtAddress.setText("");
        spnPOS.setSelection(0);
        spnIsMainOffice.setSelection(0);
        edtGSTIN.setText("");
        edtRefNo.setText("");
        edtBillNoPrefix.setText("");
    }

    @Override
    public void onResume() {
        super.onResume();
       // dbHelper = DatabaseHelper.getInstance(getActivity());
        mPopulateDataOnWidget();
    }

    void loadSpinnerData()
    {
        arrayPOS = getResources().getStringArray(R.array.poscode);
        //Creating the ArrayAdapter instance having the POSCode list
        ArrayAdapter adapterPOS = new ArrayAdapter(myContext, android.R.layout.simple_spinner_item, arrayPOS);
        adapterPOS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spnPOS.setAdapter(adapterPOS);

        ArrayAdapter adapterIsMainOffice = new ArrayAdapter(myContext, android.R.layout.simple_spinner_item, arrayIsMainOffice);
        adapterIsMainOffice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnIsMainOffice.setAdapter(adapterIsMainOffice);
    }

    private void TextChangeListener() {

        try {
            edtGSTIN.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {   }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                @Override
                public void afterTextChanged(Editable s) {
                    String gstin = edtGSTIN.getText().toString();
                    if(gstin.equals("")) {
                        spnPOS.setEnabled(true);
                        spnPOS.setSelection(0);
                    }else if(spnPOS.isEnabled()) // gstin entered and spnPos is enabled
                    {
                        spnPOS.setEnabled(false);
                    }
                    if(gstin.length() == 2)
                    {
                        if(Validations.checkValidStateCode(gstin, myContext)){
                            String stateCode = gstin.substring(0,2);
                            spnPOS.setSelection(getIndex_pos(stateCode));
                        }else
                        {
                            MsgBox.Show("Invalid Information","Please Enter Valid StateCode for GSTIN");
                            edtGSTIN.setText("");
                        }
                    }
                }
            });
        } catch (Exception e)
        {
            Logger.e(TAG, e.getMessage());
            e.printStackTrace();
        }

    }

    private int getIndex_pos(String substring) {

        int index = 0;
        for (int i = 0; index == 0 && i < spnPOS.getCount(); i++) {

            if (spnPOS.getItemAtPosition(i).toString().contains(substring)) {
                index = i;
            }

        }

        return index;

    }

    void applyValidations()
    {
        edtAddress.setFilters(new InputFilter[]{new EMOJI_FILTER()});
    }

    /*private void loadOwnerDetail() {

        Cursor cursor = HomeActivity.dbHandler.getOwnerDetail();
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_FIRM_NAME));
            String gstin = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN));
            String refernceNo = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_REFERENCE_NO));
            String phone = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_PhoneNo));
            String email = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_USER_EMAIL));
            String address = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_Address));
            String pos = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_POS));
            String mainOffice = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_IsMainOffice));
            String bill_prefix = cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_BillNoPrefix));;

            edtFirmName.setText(name);
            edtGSTIN.setText(gstin);
            edtRefNo.setText(refernceNo);
            edtPhone.setText(phone);
            edtEmail.setText(email);
            edtAddress.setText(address);
            edtBillNoPrefix.setText(bill_prefix);
            spnPOS.setSelection(getIndex_pos(pos));
            if (mainOffice.equalsIgnoreCase("yes"))
                spnIsMainOffice.setSelection(1);
            else
                spnIsMainOffice.setSelection(2);
        }
        // dbHelper.close();
    }*/
    /* @Override
    public void onDestroy() {
        super.onDestroy();
        if(dbHelper != null) {
            dbHelper.close();
        }
    }*/
}