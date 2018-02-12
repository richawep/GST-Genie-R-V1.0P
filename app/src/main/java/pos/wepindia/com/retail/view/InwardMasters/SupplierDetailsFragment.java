package pos.wepindia.com.retail.view.InwardMasters;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.retail.view.InwardMasters.InwardAdaptersAndListeners.SupplierAdapter;
import pos.wepindia.com.retail.view.InwardMasters.InwardAdaptersAndListeners.SupplierSuggestionAdapter;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.pojos.Supplier_Model;

/**
 * Created by MohanN on 1/3/2018.
 */

public class SupplierDetailsFragment extends Fragment {

    private static final String TAG = SupplierDetailsFragment.class.getName();

    @BindView(R.id.bt_supplier_details_add)
    Button btnAdd;
    @BindView(R.id.bt_supplier_details_update)
    Button btnUpdate;
    @BindView(R.id.bt_supplier_details_clear)
    Button btnClear;

    @BindView(R.id.lv_supplier_details_list)
    ListView lvSupplierDetails;

    @BindView(R.id.av_supplier_details_name)
    AutoCompleteTextView avName;
    @BindView(R.id.av_supplier_details_phone)
    AutoCompleteTextView avPhone;
    @BindView(R.id.et_supplier_details_gstin)
    EditText edtGSTIN;
    @BindView(R.id.et_supplier_details_address)
    EditText edtAddress;


    View rootView;
    MessageDialog msgBox;

    ArrayList<String> listPhone;
    ArrayList<HashMap<String, String>> listName;
    ArrayList<Supplier_Model> listSupplier ;

    SupplierAdapter supplierAdapter;

    private final int CHECK_INTEGER_VALUE = 0;
    private final int CHECK_DOUBLE_VALUE = 1;
    private final int CHECK_STRING_VALUE = 2;

    Supplier_Model supplierModel = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.supplier_details_fragment, container, false);
        try {
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.supplier_details));
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

        }catch (Exception ex){
            Log.i(TAG, "Unable init supplier details fragment. " + ex.getMessage());
        }

        lvSupplierDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                supplierModel = supplierAdapter.getItem(position);
                ListClickEvent(supplierModel);
            }});

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getString(R.string.supplier_details));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAutoCompleteSupplierName();
        Display();
    }

    @OnClick({R.id.bt_supplier_details_add, R.id.bt_supplier_details_update, R.id.bt_supplier_details_clear})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_supplier_details_add:
                AddSupplier();
                break;
            case R.id.bt_supplier_details_update:
                UpdateSupplier();
                break;
            case R.id.bt_supplier_details_clear:
                mClear();
                break;
            default:
                break;
        }
    }

    private  boolean AddSupplier() {
        long l = 0;
        Cursor cursor = HomeActivity.dbHandler.getAllSupplierName_nonGST();
        listPhone = new ArrayList<String>();
        ArrayList<String> labelsSupplierGSTIN = new ArrayList<String>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                listPhone.add(cursor.getString(cursor.getColumnIndex("SupplierPhone")));// adding TODO: changed here
                String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
                if (gstin != null && !gstin.equals(""))
                    labelsSupplierGSTIN.add(gstin);
            } while (cursor.moveToNext());
        }

        msgBox.setTitle("Incomplete Information")
                .setPositiveButton("Ok", null);

        String supplierType_str = "UnRegistered";
        String suppliername_str = avName.getText().toString().toUpperCase();
        String supplierphn_str = avPhone.getText().toString();
        String supplieraddress_str = edtAddress.getText().toString();
        String suppliergstin_str = edtGSTIN.getText().toString().trim().toUpperCase();
        if (suppliergstin_str != null && !suppliergstin_str.equals(""))
            supplierType_str = "Registered";
        else
            suppliergstin_str = "";

        if (labelsSupplierGSTIN.contains(suppliergstin_str)) {
            msgBox.Show("Warning","Supplier with gstin already present in list") ;
            return false;
        }

        boolean mFlag = false;
        try {
            if(suppliergstin_str.trim().length() == 0)
            {mFlag = true;}
            else if (suppliergstin_str.trim().length() > 0 && suppliergstin_str.length() == 15) {
                String[] part = suppliergstin_str.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                if (CHECK_INTEGER_VALUE == checkDataypeValue(part[0], "Int")
                        && CHECK_STRING_VALUE == checkDataypeValue(part[1],"String")
                        && CHECK_INTEGER_VALUE == checkDataypeValue(part[2],"Int")
                        && CHECK_STRING_VALUE == checkDataypeValue(part[3],"String")
                        && CHECK_INTEGER_VALUE == checkDataypeValue(part[4],"Int")
                        && CHECK_STRING_VALUE == checkDataypeValue(part[5],"String")
                        && CHECK_INTEGER_VALUE == checkDataypeValue(part[6],"Int")) {

                               /* int length = gstin.length() -1;
                                if(Integer.parseInt(String.valueOf(gstin.charAt(length))) ==  checksumGSTIN(gstin.substring(0,length)))*/
                    mFlag = true;
                } else {
                    mFlag = false;
                }
            } else {
                mFlag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mFlag = false;
        }
        for (String phone : listPhone) {
            if (supplierphn_str.equalsIgnoreCase(phone)) {
                msgBox.Show("Warning","Supplier with phone already present in list");
                return false;
            }
        }

        if (suppliername_str.equals("") || supplieraddress_str.equals("") || supplierphn_str.equals("")) {
            msgBox.Show("Insufficient Information","Please fill all details of Supplier");
            return false;
        } else if (!mFlag)
        {
            msgBox.Show("Insufficient Information","Please fill valid gstin");
            return false;
        }else if(supplierphn_str.length()!=10){
            msgBox.Show("Invalid Information","Phone no. cannot be less than 10 digits");
            return false;
        }else {

            l = HomeActivity.dbHandler.saveSupplierDetails(supplierType_str, suppliergstin_str, suppliername_str,
                    supplierphn_str, supplieraddress_str);
            if (l > 0) {
                Log.d("Inward_Item_Entry", " Supplier details saved at " + l);
                Toast.makeText(getActivity(), "Supplier details saved at " + l, Toast.LENGTH_SHORT).show();
                mClear();
                Display();
            }
        }
        return true;
    }

    private  boolean UpdateSupplier() {
        long l =0;
        Cursor cursor = null;
        ArrayList<String> labelsSupplierGSTIN = null;
        try {
            cursor = HomeActivity.dbHandler.getAllSupplierName_nonGST();
            listPhone = new ArrayList<String>();
           labelsSupplierGSTIN = new ArrayList<String>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    listPhone.add(cursor.getString(cursor.getColumnIndex("SupplierPhone")));// adding
                    String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
                    if (gstin != null && !gstin.equals(""))
                        labelsSupplierGSTIN.add(gstin);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex){
            Logger.i(TAG, "unable to fetch phone number on supplier details table on update method." +ex.getMessage());

        }finally {
            if(cursor != null){
                cursor.close();
            }
        }

        msgBox.setTitle("Incomplete Information")
                .setPositiveButton("Ok", null);

        String supplierType_str = "UnRegistered";
        String suppliername_str = avName.getText().toString().toUpperCase();
        String supplierphn_str = avPhone.getText().toString();
        String supplieraddress_str = edtAddress.getText().toString();
        String suppliergstin_str = edtGSTIN.getText().toString().trim().toUpperCase();
        if (suppliergstin_str != null && !suppliergstin_str.equals(""))
            supplierType_str = "Registered";
        else
            suppliergstin_str = "";

        if (labelsSupplierGSTIN != null && labelsSupplierGSTIN.size() > 0
                && labelsSupplierGSTIN.contains(suppliergstin_str) ) {
            msgBox.Show("Warning", "Supplier with gstin already present in list");
            return false;
        }

//        for (String supplier : labelsSupplierName) {
//            if (suppliername_str.equalsIgnoreCase(supplier) && !suppliername_str.equalsIgnoreCase(suppliername_clicked)) { // TODO: changed here
//                MsgBox.setTitle("Warning")
//                        .setMessage("Supplier with name already present in list")
//                        .setPositiveButton("OK", null)
//                        .show();
//                return false;
//            }
//        }

        for (String phone : listPhone) {
            if (supplierphn_str.equalsIgnoreCase(phone) && !supplierphn_str.equalsIgnoreCase(supplierModel.getSupplierPhone())) {
                msgBox.Show("Warning", "Supplier with phone already present in list");
                return false;
            }
        }


        boolean mFlag = false;
        try {
            if(suppliergstin_str.trim().length() == 0)
            {mFlag = true;}
            else if (suppliergstin_str.trim().length() > 0 && suppliergstin_str.length() == 15) {
                String[] part = suppliergstin_str.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
                if (CHECK_INTEGER_VALUE == checkDataypeValue(part[0], "Int")
                        && CHECK_STRING_VALUE == checkDataypeValue(part[1],"String")
                        && CHECK_INTEGER_VALUE == checkDataypeValue(part[2],"Int")
                        && CHECK_STRING_VALUE == checkDataypeValue(part[3],"String")
                        && CHECK_INTEGER_VALUE == checkDataypeValue(part[4],"Int")
                        && CHECK_STRING_VALUE == checkDataypeValue(part[5],"String")
                        && CHECK_INTEGER_VALUE == checkDataypeValue(part[6],"Int")) {

                               /* int length = gstin.length() -1;
                                if(Integer.parseInt(String.valueOf(gstin.charAt(length))) ==  checksumGSTIN(gstin.substring(0,length)))*/
                    mFlag = true;
                } else {
                    mFlag = false;
                }
            } else {
                mFlag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mFlag = false;
        }

        if (suppliername_str.equals("") || supplieraddress_str.equals("") || supplierphn_str.equals("")) {
            msgBox.Show("Incomplete Details","Please fill all mandatory details of Supplier");
            return false;

        } else if (!mFlag)
        {
           msgBox.Show("Invalid Information","Please fill valid gstin");
            return false;
        }else if(supplierphn_str.length()!=10){
            msgBox.Show("Invalid Information","Phone no. cannot be less than 10 digits");
            return false;
        } else{
            l = HomeActivity.dbHandler.updateSupplierDetails(supplierType_str, suppliergstin_str, suppliername_str,
                    supplierphn_str, supplieraddress_str, supplierModel.getSupplierCode());
            if (l > 0) {
                Log.d("Inward_Supplier Detail", " Supplier details updated at " + l);
                Toast.makeText(getActivity(), "Supplier details saved at " + l, Toast.LENGTH_SHORT).show();
                mClear();
                Display();
            }
        }
        return true;
    }


    public void loadAutoCompleteSupplierName()
    {
        Cursor cursor = null;
        try {
            cursor = HomeActivity.dbHandler.getAllSupplierName_nonGST();
            listName = new ArrayList<HashMap<String, String>>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("name", cursor.getString(cursor.getColumnIndex("SupplierName")));
                    data.put("phone", cursor.getString(cursor.getColumnIndex("SupplierPhone")));
                    listName.add(data);
                } while (cursor.moveToNext());
            }

            String[] fields = {"name", "phone"};
            int[] res = {R.id.adapterName, R.id.adapterPhone};

            SupplierSuggestionAdapter dataAdapter = new SupplierSuggestionAdapter(getActivity(), R.layout.adapter_supplier_name, listName);
            avName.setAdapter(dataAdapter);

        } catch (Exception e) {
            e.printStackTrace();
            msgBox.Show("Error",e.getMessage());
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }

    public void Display() {
        if (listSupplier == null)
            listSupplier = new ArrayList<>();
        Cursor supplierCursor = null;
        try {
           supplierCursor = HomeActivity.dbHandler.getAllSupplierName_nonGST();
            listSupplier.clear();
            while (supplierCursor != null && supplierCursor.moveToNext()) {
                int suppliercode = supplierCursor.getInt(supplierCursor.getColumnIndex("SupplierCode"));
                String suppliergstin = supplierCursor.getString(supplierCursor.getColumnIndex("GSTIN"));
                String suppliername = supplierCursor.getString(supplierCursor.getColumnIndex("SupplierName"));
                String suppliernphone = supplierCursor.getString(supplierCursor.getColumnIndex("SupplierPhone"));
                String supplieraddress = supplierCursor.getString(supplierCursor.getColumnIndex("SupplierAddress"));
                Supplier_Model supplier_model = new Supplier_Model(suppliercode, suppliergstin, suppliername, suppliernphone, supplieraddress);
                listSupplier.add(supplier_model);
            } // end of while
            if (listSupplier.size() > 0) {
                if (supplierAdapter == null) {
                    supplierAdapter = new SupplierAdapter(getContext(), listSupplier);
                    lvSupplierDetails.setAdapter(supplierAdapter);
                } else {
                    supplierAdapter.notifyNewDataAdded(listSupplier);
                }
            }
        }catch (Exception ex){
            Logger.i(TAG, "Unable to fetch supplier details data from supplier details table." +ex.getMessage());
        }finally {
            if(supplierCursor != null){
                supplierCursor.close();
            }
        }
    }

    private void ListClickEvent(Supplier_Model supplier)
    {
        btnUpdate.setEnabled(true);
        btnAdd.setEnabled(false);

        avName.setFocusable(false);
        avName.setFocusableInTouchMode(false);
        avName.setText(supplier.getSupplierName());
        avName.setFocusable(true);
        avName.setFocusableInTouchMode(true);

        avPhone.setText(supplier.getSupplierPhone());
        if(supplier.getSupplierGSTIN() != null) {
            edtGSTIN.setText(supplier.getSupplierGSTIN());
        }
        edtAddress.setText(supplier.getSupplierAddress());
    }

    public static int checkDataypeValue(String value, String type) {
        int flag =0;
        try {
            switch(type) {
                case "Int":
                    Integer.parseInt(value);
                    flag = 0;
                    break;
                case "Double" : Double.parseDouble(value);
                    flag = 1;
                    break;
                default : flag =2;
            }
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            flag = -1;
        }
        return flag;
    }

    public void mClear()
    {
       edtAddress.setText("");
       edtGSTIN.setText("");
       supplierModel = null;
       avPhone.setText("");
       avName.setText("");
    }


}