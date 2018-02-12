package pos.wepindia.com.retail.view.InwardMasters;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.DateTime;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.retail.view.InwardMasters.InwardAdaptersAndListeners.OnPurchaseOrderItemListListener;
import pos.wepindia.com.retail.view.InwardMasters.InwardAdaptersAndListeners.PurchaseOrderAdapter;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.PurchaseOrderBean;
import pos.wepindia.com.wepbase.model.pojos.Supplier_Model;

/**
 * Created by MohanN on 1/10/2018.
 */

public class GoodsInwardNoteFragment extends Fragment implements OnPurchaseOrderItemListListener {

    private static final String TAG = GoodsInwardNoteFragment.class.getName();
    private final int ADD = 0;
    private final int UPDATE = 1;

    @BindView(R.id.bt_goods_inward_note_save)
    Button btnSaveNote;
    @BindView(R.id.bt_goods_inward_note_goods_inward)
    Button btnGoodsInward;
    @BindView(R.id.bt_goods_inward_note_clear)
    Button btnClear;
    @BindView(R.id.bt_goods_inward_note_add_item)
    Button btnAddItem;
    @BindView(R.id.bt_goods_inward_note_supplier_add)
    Button btnSupplierAdd;

    @BindView(R.id.av_goods_inward_note_supplier_name)
    AutoCompleteTextView avSupplierName;
    @BindView(R.id.av_goods_inward_note_po)
    AutoCompleteTextView avPurchaseOrder;
    @BindView(R.id.av_goods_inward_note_item_name)
    AutoCompleteTextView avItemName;

    @BindView(R.id.et_goods_inward_note_supplier_phone)
    EditText edtPhone;
    @BindView(R.id.et_goods_inward_note_supplier_address)
    EditText edtAddress;
    @BindView(R.id.et_goods_inward_note_supplier_gstin)
    EditText edtGSTIN;
    @BindView(R.id.et_goods_inward_note_Qty)
    EditText edtQty;
    @BindView(R.id.et_goods_inward_note_additional_charge_name)
    EditText edtAdditionalChargeName;
    @BindView(R.id.et_goods_inward_note_additional_amount)
    EditText edtAdditionalAmt;
    @BindView(R.id.et_goods_inward_note_sub_total)
    EditText edtSubTotal;
    @BindView(R.id.et_goods_inward_note_grand_total)
    EditText edtGrandTotal;
    @BindView(R.id.et_goods_inward_note_invoice_no)
    EditText edtInvoiceNo;
    @BindView(R.id.et_goods_inward_note_invoice_date)
    EditText edtInvoiceDate;

    @BindView(R.id.sp_goods_inward_note_state_code)
    Spinner spStateCode;

    @BindView(R.id.cb_goods_inward_note_state_code)
    CheckBox cbStateCode;
    @BindView(R.id.cb_goods_inward_note_additional_charge)
    CheckBox cbAdditionalChargeStatus;

    @BindView(R.id.lv_goods_inward_note_list)
    ListView lvPurchaseOrderList;

    @BindView(R.id.iv_goods_inward_note_invoice_date)
    ImageView ivInvoiceDate;

    View rootView;
    MessageDialog msgBox;

    SimpleCursorAdapter mAdapterSupplierName, mAdapterItemData, mAdapterPurchaseOrderNoData;

    Supplier_Model supplier_model;
    String[] arrayPOS;

    PurchaseOrderBean purchaseOrderBean;

    PurchaseOrderAdapter purchaseOrderAdapter = null;
    List<PurchaseOrderBean> purchaseOrderBeanList = null;

    private Boolean machine_changed_edittext = false;

    String strDate= "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.goods_inward_note_fragment, container, false);
        try {
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
            Log.i(TAG, "Unable init purchase order fragment. " + ex.getMessage());
        }

        purchaseOrderBeanList = new ArrayList<PurchaseOrderBean>();
        cbStateCode.setChecked(false);
        spStateCode.setEnabled(false);

        cbAdditionalChargeStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(!isChecked)
                {
                    edtAdditionalChargeName.setEnabled(false);
                    edtAdditionalAmt.setEnabled(false);
                    if(!edtAdditionalAmt.getText().toString().isEmpty()){
                        edtAdditionalAmt.setText("");
                        edtGrandTotal.setText(""+mCalculateGrandTotal(0));
                    }
                    edtAdditionalChargeName.setText("");
                } else {
                    edtAdditionalChargeName.setEnabled(true);
                    edtAdditionalAmt.setEnabled(true);
                }
            }
        });

        cbStateCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                {
                    spStateCode.setSelection(0);
                    spStateCode.setEnabled(false);
                    if(purchaseOrderAdapter== null || purchaseOrderBeanList == null || purchaseOrderBeanList.size()==0)
                        return;
                    for(PurchaseOrderBean po : purchaseOrderBeanList)
                    {
                        double sgstAmt =0, cgstAmt =0;
                        double taxval = po.getDblTaxableValue();
                        sgstAmt= taxval*po.getDblSGSTRate()/100;
                        cgstAmt= taxval*po.getDblCGSTRate()/100;
                        po.setDblIGSTAmount(0);
                        po.setDblCGSTAmount(cgstAmt);
                        po.setDblSGSTAmount(sgstAmt);
                    }
                    purchaseOrderAdapter.notifyDataSetChanged(purchaseOrderBeanList);
                }
                else
                {
                    spStateCode.setSelection(0);
                    spStateCode.setEnabled(true);
                    if(purchaseOrderAdapter== null || purchaseOrderBeanList == null || purchaseOrderBeanList.size()==0)
                        return;
                    for(PurchaseOrderBean po : purchaseOrderBeanList)
                    {
                        double sgst = po.getDblSGSTAmount();
                        double cgst = po.getDblCGSTAmount();
                        po.setDblIGSTAmount(sgst+cgst);
                        po.setDblCGSTAmount(0);
                        po.setDblSGSTAmount(0);
                    }
                    purchaseOrderAdapter.notifyDataSetChanged(purchaseOrderBeanList);
                }

            }
        });


        edtAdditionalAmt.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (! machine_changed_edittext) {
                    machine_changed_edittext = true;
                    if (!s.toString().equals("")) {
                        edtGrandTotal.setText(""+mCalculateGrandTotal(Double.parseDouble(s.toString())));
                    } else {
                        edtGrandTotal.setText(edtSubTotal.getText().toString());
                    }
                    machine_changed_edittext = false;
                }
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getString(R.string.purchase_order));
    }

    @Override
    public void onResume() {
        super.onResume();
        initAutoCompleteTextDataForSupplierName();
        mPopulateStateCodeSpinnerData();
    }

    @OnClick({R.id.bt_goods_inward_note_save, R.id.bt_goods_inward_note_goods_inward, R.id.bt_goods_inward_note_clear,
            R.id.bt_goods_inward_note_add_item, R.id.bt_goods_inward_note_supplier_add, R.id.iv_goods_inward_note_invoice_date})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_goods_inward_note_save:
                savePurchaseOrder(0);
                break;
            case R.id.bt_goods_inward_note_goods_inward:
                mValidateGoodsInwardNote();
                break;
            case R.id.bt_goods_inward_note_clear:
                mClear();
                break;
            case R.id.bt_goods_inward_note_add_item:
                mValidateAndAddingListAdapter();
                break;
            case R.id.bt_goods_inward_note_supplier_add:
                Fragment fragment = new SupplierDetailsFragment();
                if (fragment != null) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
                break;
            case R.id.iv_goods_inward_note_invoice_date:
                mDateSelection_inward();
                break;
            default:
                break;
        }
    }

    private void initAutoCompleteTextDataForSupplierName(){
        mAdapterSupplierName = new SimpleCursorAdapter(getActivity(), R.layout.auto_complete_textview_two_strings, null,
                new String[] {DatabaseHandler.KEY_SUPPLIERNAME, DatabaseHandler.KEY_SupplierPhone},
                new int[] {R.id.tv_auto_complete_textview_item_one, R.id.tv_auto_complete_textview_two},
                0);
        avSupplierName.setThreshold(2);
        avSupplierName.setAdapter(mAdapterSupplierName);

        mAdapterSupplierName.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence str) {
                Logger.i(TAG, "Supplier name search data." +str);
                return HomeActivity.dbHandler.mGetSupplierDetails(str);
            } });

        mAdapterSupplierName.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public CharSequence convertToString(Cursor cur) {
                int index = cur.getColumnIndex(DatabaseHandler.KEY_SUPPLIERNAME);
                return cur.getString(index);
            }});

        avSupplierName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the
                // result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                if(cursor != null){
                    supplier_model = new Supplier_Model();
                    supplier_model.setSupplierPhone(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplierPhone)));
                    supplier_model.setSupplierCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_SupplierCode)));
                    if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN)) != null && !cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN)).isEmpty()){
                        supplier_model.setSupplierGSTIN(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN)));
                    }
                    supplier_model.setSupplierAddress(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplierAddress)));
                    supplier_model.setStrSupplierType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplierType)));
                }
                // Update the parent class's TextView
                if(supplier_model != null){
                    edtPhone.setText(supplier_model.getSupplierPhone());
                    edtAddress.setText(supplier_model.getSupplierAddress());
                    if(!supplier_model.getSupplierGSTIN().isEmpty()){
                        edtGSTIN.setText(supplier_model.getSupplierGSTIN());
                    }
                    initAutoCompleteTextDataForItems(supplier_model.getSupplierCode());
                    initAutoCompleteTextDataForPurchaseOrderNo(supplier_model.getSupplierCode());
                }
            }
        });
    }

    private void mPopulateStateCodeSpinnerData(){
        arrayPOS = getResources().getStringArray(R.array.poscode);
        //Creating the ArrayAdapter instance having the POSCode list
        ArrayAdapter adapterPOS = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, arrayPOS);
        adapterPOS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spStateCode.setAdapter(adapterPOS);
    }

    private void initAutoCompleteTextDataForItems(final int iSupplierCode){
        mAdapterItemData = new SimpleCursorAdapter(getActivity(), R.layout.auto_complete_textview_two_strings, null,
                new String[] {DatabaseHandler.KEY_ItemShortName, DatabaseHandler.KEY_ItemBarcode},
                new int[] {R.id.tv_auto_complete_textview_item_one, R.id.tv_auto_complete_textview_two},
                0);
        avItemName.setThreshold(2);
        avItemName.setAdapter(mAdapterItemData);

        mAdapterItemData.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence str) {
                Logger.i(TAG, "Item name search data in supplier item linkage table." +str);
                if(str != null) {
                    return HomeActivity.dbHandler.mGetPurchaseOrderItems(str,iSupplierCode);
                }
                return null;
            } });

        mAdapterItemData.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public CharSequence convertToString(Cursor cur) {
                int index = cur.getColumnIndex(DatabaseHandler.KEY_ItemShortName);
                return cur.getString(index);
            }});

        avItemName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the
                // result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                // Get the state's capital from this row in the database.
                if(cursor != null){
                    purchaseOrderBean = new PurchaseOrderBean();
                    purchaseOrderBean.setiMenuCode(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_ITEM_ID)));
                    purchaseOrderBean.setStrSupplierCode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplierCode)));
                    purchaseOrderBean.setStrSupplierName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SUPPLIERNAME)));
                    purchaseOrderBean.setStrSupplierPhone(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplierPhone)));
                    purchaseOrderBean.setStrSupplierAddress(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplierAddress)));
                    if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN)) != null){
                        purchaseOrderBean.setStrSupplierGSTIN(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_GSTIN)));
                    }
                    purchaseOrderBean.setStrBarcode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)));
                    purchaseOrderBean.setStrItemName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemShortName)));
                    if(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)) > 0) {
                        purchaseOrderBean.setDblRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)));
                    }
                    if(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_MRP)) > 0) {
                        purchaseOrderBean.setDblMRP(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_MRP)));
                    }
                    if(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)) > 0){
                        purchaseOrderBean.setDblRetailPrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)));
                    }
                    if(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)) > 0){
                        purchaseOrderBean.setDblWholeSalePrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)));
                    }
                    if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)) != null){
                        purchaseOrderBean.setStrSupplyType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)));
                    }
                    if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplierType)) != null){
                        purchaseOrderBean.setStrSupplierType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplierType)));
                    }
                    if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_HSNCode)) != null){
                        purchaseOrderBean.setStrHSNCode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_HSNCode)));
                    }
                    if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_UOM)) != null){
                        purchaseOrderBean.setStrUOM(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_UOM)));
                    }
                    if(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.CGST_RATE_PER)) > 0){
                        purchaseOrderBean.setDblCGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.CGST_RATE_PER)));
                    }
                    if(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.UTGST_SGST_RATE_PER)) > 0){
                        purchaseOrderBean.setDblSGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.UTGST_SGST_RATE_PER)));
                    }
                    if(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.IGST_RATE_PER)) > 0){
                        purchaseOrderBean.setDblIGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.IGST_RATE_PER)));
                    }
                    if(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.CESS_RATE_PER)) > 0){
                        purchaseOrderBean.setDblCessRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.CESS_RATE_PER)));
                    }
                }
            }
        });
    }


    private void initAutoCompleteTextDataForPurchaseOrderNo(final int iSupplierCode){
        mAdapterPurchaseOrderNoData = new SimpleCursorAdapter(getActivity(), R.layout.auto_complete_textview_two_strings, null,
                new String[] {DatabaseHandler.KEY_id},
                new int[] {R.id.tv_auto_complete_textview_item_one},
                0);
        avPurchaseOrder.setThreshold(1);
        avPurchaseOrder.setAdapter(mAdapterPurchaseOrderNoData);

        mAdapterPurchaseOrderNoData.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence str) {
                Logger.i(TAG, "Purchase order no search data in purchase order table." +str);
                if(str != null) {
                    return HomeActivity.dbHandler.mGetPurchaseOrderNo(str,iSupplierCode);
                }
                return null;
            } });

        mAdapterPurchaseOrderNoData.setCursorToStringConverter(new SimpleCursorAdapter.CursorToStringConverter() {
            public CharSequence convertToString(Cursor cur) {
                int index = cur.getColumnIndex(DatabaseHandler.KEY_id);
                return cur.getString(index);
            }});

        avPurchaseOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the
                // result set
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                // Get the state's capital from this row in the database.
                if(cursor != null){
                    mFetchDataOnPurchaseOrderNo(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_id)));
                }
            }
        });
    }
    private void mValidateAndAddingListAdapter(){
        if(avSupplierName.getText().toString().isEmpty()){
            msgBox.Show("Insufficient Information","Please Select/Add Supplier ");
            return;
        }
        if(avItemName.getText().toString().isEmpty()){
            msgBox.Show("Insufficient Information","Please Select Item ");
            return;
        }

        if(edtInvoiceNo.getText().toString().isEmpty()){
            msgBox.Show("Insufficient Information","Please enter invoice number. ");
            return;
        }

        if(edtInvoiceDate.getText().toString().isEmpty()){
            msgBox.Show("Insufficient Information","Please select invoice date. ");
            return;
        }

        if(edtPhone.getText().toString().isEmpty() && edtAddress.getText().toString().isEmpty()){
            msgBox.Show("Insufficient Information","Please select proper supplier or add supplier and select items.");
            return;
        }

        if(purchaseOrderBean == null){
            msgBox.Show("Warning","Kindly goto \"Supplier Item Linkage\" and add the desired item." +
                    "\nPlease save your data , if any , before leaving this screen");
            return;
        }
        if(edtQty.getText().toString().isEmpty()){
            msgBox.Show("Insufficient Information","Please Enter the Quantity ");
            return;
        }

        if (purchaseOrderBeanList != null && purchaseOrderBeanList.size() > 0) {
            boolean bExistsStatus = true;
            for (int i = 0; i < purchaseOrderBeanList.size(); i++) {
                if (purchaseOrderBeanList.get(i).getStrItemName().toUpperCase().trim().equals(purchaseOrderBean.getStrItemName().toUpperCase().trim())
                        && purchaseOrderBeanList.get(i).getDblRate() == purchaseOrderBean.getDblRate()) {
                    mAmountCalculation(purchaseOrderBeanList.get(i),UPDATE);
                    bExistsStatus = false;
                    break;
                }
            }
            if(bExistsStatus){
                mAmountCalculation(purchaseOrderBean,ADD);
            }
        } else {
            mAmountCalculation(purchaseOrderBean,ADD);
        }
    }

    private void mFetchDataOnPurchaseOrderNo(String strPurchaseOrderNo){
        Cursor cursorPO = HomeActivity.dbHandler.mGetPurchaseOrderData(strPurchaseOrderNo);
        try {
            if (cursorPO != null){
                cursorPO.moveToFirst();
                do{
                    purchaseOrderBean = new PurchaseOrderBean();
                    purchaseOrderBean.set_id(cursorPO.getInt(cursorPO.getColumnIndex(DatabaseHandler.KEY_id)));
                    purchaseOrderBean.setiMenuCode(cursorPO.getInt(cursorPO.getColumnIndex(DatabaseHandler.KEY_ShortCode)));
                    purchaseOrderBean.setStrSupplierCode(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_SupplierCode)));
                    purchaseOrderBean.setStrSupplierName(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_SUPPLIERNAME)));
                    purchaseOrderBean.setStrSupplierPhone(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_SupplierPhone)));
                    purchaseOrderBean.setStrSupplierAddress(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_SupplierAddress)));
                    if(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_GSTIN)) != null){
                        purchaseOrderBean.setStrSupplierGSTIN(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_GSTIN)));
                    }
                    purchaseOrderBean.setStrBarcode(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)));
                    purchaseOrderBean.setStrItemName(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_ItemShortName)));
                    if(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_RetailPrice)) > 0) {
                        purchaseOrderBean.setDblRate(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_RetailPrice)));
                    }
                    if(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_MRP)) > 0) {
                        purchaseOrderBean.setDblMRP(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_MRP)));
                    }
                   /* if(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_RetailPrice)) > 0){
                        purchaseOrderBean.setDblRetailPrice(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_RetailPrice)));
                    }
                    if(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)) > 0){
                        purchaseOrderBean.setDblWholeSalePrice(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)));
                    }*/
                    if(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_SupplyType)) != null){
                        purchaseOrderBean.setStrSupplyType(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_SupplyType)));
                    }
                    if(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_SupplierType)) != null){
                        purchaseOrderBean.setStrSupplierType(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_SupplierType)));
                    }
                    if(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_HSNCode)) != null){
                        purchaseOrderBean.setStrHSNCode(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_HSNCode)));
                    }
                    if(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_UOM)) != null){
                        purchaseOrderBean.setStrUOM(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_UOM)));
                    }
                    if(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_CGSTRate)) > 0){
                        purchaseOrderBean.setDblCGSTRate(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_CGSTRate)));
                    }
                    if(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_SGSTRate)) > 0){
                        purchaseOrderBean.setDblSGSTRate(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_SGSTRate)));
                    }
                    if(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_IGSTRate)) > 0){
                        purchaseOrderBean.setDblIGSTRate(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_IGSTRate)));
                    }
                    if(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_cessRate)) > 0){
                        purchaseOrderBean.setDblCessRate(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_cessRate)));
                    }
                    if(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_Quantity)) > 0){
                        purchaseOrderBean.setDblQuantity(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_Quantity)));
                    }
                    if(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_AdditionalChargeAmount)) > 0){
                        purchaseOrderBean.setDblAdditionalChargeAmount(cursorPO.getDouble(cursorPO.getColumnIndex(DatabaseHandler.KEY_AdditionalChargeAmount)));
                    }
                    if(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_AdditionalChargeName)) != null){
                        purchaseOrderBean.setStrAdditionalCharge(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_AdditionalChargeName)));
                    }
                    if(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_InvoiceNo)) != null){
                        purchaseOrderBean.setStrInvoiceNo(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_InvoiceNo)));
                    }
                    if(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_InvoiceDate)) != null){
                        purchaseOrderBean.setStrInvoiceDate(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_InvoiceDate)));
                    }
                    if(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_SupplierPOS)) != null){
                        purchaseOrderBean.setStrSupplierPOS(cursorPO.getString(cursorPO.getColumnIndex(DatabaseHandler.KEY_SupplierPOS)));
                    }
                    mAmountCalculation(purchaseOrderBean,ADD);
                }while(cursorPO.moveToNext());
            }
        }catch (Exception ex){
            Logger.i(TAG,"Unable to fetch data from purchase order table based on the purchase order no.");
        } finally {
            if(cursorPO != null){
                cursorPO.close();
            }
        }
    }

    private void mAmountCalculation(PurchaseOrderBean purchaseOrderBeanTemp, int iMode) {
        double dblQty = 0, dblTaxableValue = 0, dblCGSTAmt = 0, dblSGSTAmt = 0, dblIGSTAmt = 0, dblCessAmt = 0, dblAmount = 0;
        if (purchaseOrderBeanTemp != null && purchaseOrderBeanTemp.getDblQuantity() <= 0) {
            try {
                dblQty = Double.parseDouble(edtQty.getText().toString());
                if (dblQty <= 0) {
                    msgBox.Show("Insufficient Information", "Please enter quantity more than zero.");
                    return;
                }
                purchaseOrderBeanTemp.setDblQuantity(dblQty);
            } catch (Exception ex) {
                Logger.i(TAG, "Unable to process calculation on item selection for purchase order. Barcode : "
                        + purchaseOrderBeanTemp.getStrBarcode() + ex.getMessage());
                return;
            }
        } else {
            dblQty = 0;
            if(!edtQty.getText().toString().isEmpty()) {
                dblQty = Double.parseDouble(edtQty.getText().toString());
            }
            purchaseOrderBeanTemp.setDblQuantity(dblQty + purchaseOrderBeanTemp.getDblQuantity());
        }
        try {
            dblTaxableValue = purchaseOrderBeanTemp.getDblQuantity() * purchaseOrderBeanTemp.getDblRate();
            purchaseOrderBeanTemp.setDblTaxableValue(dblTaxableValue);
            if(cbStateCode.isChecked())
            {
                dblIGSTAmt = (purchaseOrderBeanTemp.getDblIGSTRate() * purchaseOrderBeanTemp.getDblTaxableValue())/100;
                dblCGSTAmt = 0;
                dblSGSTAmt = 0;
            }
            else
            {
                dblCGSTAmt = (purchaseOrderBeanTemp.getDblCGSTRate()*purchaseOrderBeanTemp.getDblTaxableValue())/100;
                dblSGSTAmt = (purchaseOrderBeanTemp.getDblSGSTRate()*purchaseOrderBeanTemp.getDblTaxableValue())/100;
                dblIGSTAmt =0;
            }
            dblCessAmt = (purchaseOrderBeanTemp.getDblCessRate()*purchaseOrderBeanTemp.getDblTaxableValue())/100;
            purchaseOrderBeanTemp.setDblCGSTAmount(dblCGSTAmt);
            purchaseOrderBeanTemp.setDblSGSTAmount(dblSGSTAmt);
            purchaseOrderBeanTemp.setDblIGSTAmount(dblIGSTAmt);
            purchaseOrderBeanTemp.setDblCessAmount(dblCessAmt);
            dblAmount = purchaseOrderBeanTemp.getDblTaxableValue() + purchaseOrderBeanTemp.getDblCGSTAmount()
                    + purchaseOrderBeanTemp.getDblSGSTAmount() + purchaseOrderBeanTemp.getDblIGSTAmount()
                    + purchaseOrderBeanTemp.getDblCessAmount();
            purchaseOrderBeanTemp.setDblAmount(dblAmount);
        } catch (Exception ex) {
            Logger.i(TAG, "Unable to process calculation on item selection for purchase order. Barcode : "
                    + purchaseOrderBeanTemp.getStrBarcode() + ex.getMessage());
            return;
        }
        switch (iMode){
            case ADD:
                purchaseOrderBeanList.add(purchaseOrderBeanTemp);
                break;
            case UPDATE:
                for (int i = 0; i < purchaseOrderBeanList.size(); i++) {
                    if (purchaseOrderBeanList.get(i).getStrItemName().toUpperCase().trim().equals(purchaseOrderBeanTemp.getStrItemName().toUpperCase().trim())
                            && purchaseOrderBeanList.get(i).getDblRate() == purchaseOrderBeanTemp.getDblRate()) {
                        purchaseOrderBeanList.remove(i);
                        purchaseOrderBeanList.add(purchaseOrderBeanTemp);
                        break;
                    }
                }
                break;
            default:
                break;
        }
        mPopulatingDataToAdapter();
    }

    private void mPopulatingDataToAdapter(){
        if(purchaseOrderBeanList.size() > 0){
            if (purchaseOrderAdapter == null) {
                purchaseOrderAdapter = new PurchaseOrderAdapter(getActivity(),this, purchaseOrderBeanList);
                lvPurchaseOrderList.setAdapter(purchaseOrderAdapter);
            } else {
                purchaseOrderAdapter.notifyDataSetChanged(purchaseOrderBeanList);
            }

            edtSubTotal.setText(""+mCalculateSubTotal());
            edtGrandTotal.setText(""+mCalculateGrandTotal(0));
            if(purchaseOrderBeanList.get(0).getDblAdditionalChargeAmount() > 0){
                cbAdditionalChargeStatus.setChecked(true);
                if(!purchaseOrderBeanList.get(0).getStrAdditionalCharge().isEmpty()) {
                    edtAdditionalChargeName.setText(purchaseOrderBeanList.get(0).getStrAdditionalCharge());
                }
                edtAdditionalAmt.setText(""+purchaseOrderBeanList.get(0).getDblAdditionalChargeAmount());
            }
            if(!purchaseOrderBeanList.get(0).getStrInvoiceNo().isEmpty()) {
                edtInvoiceNo.setText(purchaseOrderBeanList.get(0).getStrInvoiceNo());
                String dateformat = "dd-MM-yyyy";
                String invoicedate = getDate(Long.parseLong(purchaseOrderBeanList.get(0).getStrInvoiceDate()), dateformat);
                edtInvoiceDate.setText(invoicedate);
            }
            if(arrayPOS != null && arrayPOS.length > 0) {
                if (!purchaseOrderBeanList.get(0).getStrSupplierPOS().isEmpty()) {
                    cbStateCode.setChecked(true);
                    for (int i = 0; i <arrayPOS.length; i++) {
                        String strStateCode = arrayPOS[i];
                        int l = strStateCode.length();
                        String strState_cd = strStateCode.substring(l-2,l);
                        if(strState_cd.equals(purchaseOrderBeanList.get(0).getStrSupplierPOS())) {
                            spStateCode.setSelection(i);
                            break;
                        }
                    }
                }
            }
        } else {
            edtSubTotal.setText("");
            edtGrandTotal.setText("");
            edtAdditionalAmt.setText("");
            edtAdditionalChargeName.setText("");
        }
        avItemName.setText("");
        edtQty.setText("");
    }

    long savePurchaseOrder(int isGoodInward)
    {
        long result = -1;
        if(purchaseOrderBeanList != null && purchaseOrderBeanList.size() > 0){
            if(purchaseOrderBeanList.get(0).getStrSupplierCode().isEmpty()){
                msgBox.Show(" Insufficient Information ", "Supplier is not in Database. Please add supplier");
                return result;
            }
            if (avSupplierName.getText().toString().isEmpty() || edtAddress.getText().toString().isEmpty()
                    || edtPhone.getText().toString().isEmpty()) {
                msgBox.Show(" Insufficient Information ", "Please fill Supplier Details");
                return result;
            }

            if(avPurchaseOrder.getText().toString().isEmpty()){
                msgBox.Show("Insufficient Information","Please select purchase order number. ");
                return result;
            }

            if (purchaseOrderAdapter == null) {
                msgBox.Show("Insufficient Information ", " Please add item");
                return result;
            }
            if (cbStateCode.isChecked() && spStateCode.getSelectedItem().toString().equals("Select")) {
                msgBox.Show("Insufficient Information ", " Please select state for supplier");
                return result;
            }
            if(edtInvoiceNo.getText().toString().isEmpty()){
                msgBox.Show("Insufficient Information","Please enter invoice number. ");
                return result;
            }

            if(edtInvoiceDate.getText().toString().isEmpty()){
                msgBox.Show("Insufficient Information","Please select invoice date. ");
                return result;
            }
        }
        try
        {
            String purchaseorderno = avPurchaseOrder.getText().toString();
            String supp_name = avSupplierName.getText().toString();
            int suppliercode  = Integer.parseInt(purchaseOrderBeanList.get(0).getStrSupplierCode());
            String sup_phone = edtPhone.getText().toString();
            String sup_address = edtAddress.getText().toString();
            String sup_gstin = edtGSTIN.getText().toString();

            String invoiceno = edtInvoiceNo.getText().toString();
            String invodate = edtInvoiceDate.getText().toString();

            int deletedrows = HomeActivity.dbHandler.deletePurchaseOrder( suppliercode,Integer.parseInt(purchaseorderno));
            Logger.d("InsertPurchaseOrder", deletedrows+" Rows deleted for Purchase Order "+purchaseorderno);

            if (!invodate.equals("")) {
                Date date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(invodate);
                long milliseconds = date.getTime();
                invodate = String.valueOf(milliseconds);
            }

            for (PurchaseOrderBean po :purchaseOrderBeanList) {
                po.setStrPurchaseOrderNo(purchaseorderno);
                po.setStrInvoiceNo(invoiceno);
                po.setStrInvoiceDate(invodate);
                po.setiIsgoodInward(0);
                if(isGoodInward ==1){
                    po.setiIsgoodInward(1);
                }
                if(cbStateCode.isChecked())
                {
                    String supplier_stateCode = spStateCode.getSelectedItem().toString();
                    int l = supplier_stateCode.length();
                    String state_cd = supplier_stateCode.substring(l-2,l);
                    po.setStrSupplierPOS(state_cd);
                }else
                {
                    po.setStrSupplierPOS("");
                }
                if(cbAdditionalChargeStatus.isChecked())
                {
                    po.setStrAdditionalCharge(edtAdditionalChargeName.getText().toString());
                    po.setDblAdditionalChargeAmount(Double.parseDouble(edtAdditionalAmt.getText().toString()));
                }
                else
                {
                    po.setStrAdditionalCharge("");
                    po.setDblAdditionalChargeAmount(0);
                }
                result = HomeActivity.dbHandler.InsertPurchaseOrder(po);
                if(result>-1) {
                    Logger.d("InsertPurchaseOrder", " item inserted at position:" + result);
                }
            }
            if(result>-1) {
                mClear();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            Logger.e("log", e.getMessage(), e);
            result = -1;
        }
        return result;
    }

    public void mValidateGoodsInwardNote() {
        if (avPurchaseOrder.getText().toString().isEmpty()) {
            msgBox.Show("Insufficient Information", "Please Select Purchase Order");
            return;
        }
        String purchaseorderno = avPurchaseOrder.getText().toString();
        if(!purchaseorderno.equalsIgnoreCase("NA") && !isNumeric(purchaseorderno))
        {
            msgBox.Show("Error ", " Please enter Purchase Order in numbers only");
            return;
        }
        if (avSupplierName.getText().toString().isEmpty()) {
            msgBox.Show("Insufficient Information", "Please Select/Add Supplier ");
            return;
        }
        if(edtInvoiceNo.getText().toString().isEmpty()){
            msgBox.Show("Insufficient Information", "Please enter invoice number. ");
            return;
        }
        if(edtInvoiceDate.getText().toString().isEmpty()){
            msgBox.Show("Insufficient Information", "Please select invoice date. ");
            return;
        }
        if (purchaseOrderAdapter == null) {
            msgBox.Show("Insufficient Information", "Please Select Item ");
            return;
        }

        if (edtPhone.getText().toString().isEmpty() && edtAddress.getText().toString().isEmpty()) {
            msgBox.Show("Insufficient Information", "Please select proper supplier or add supplier and select items.");
            return;
        }

        if (cbStateCode.isChecked() && spStateCode.getSelectedItem().toString().equals("Select")) {
            msgBox.Show("Insufficient Information ", " Please select state for supplier");
            return;
        }

        if (purchaseOrderBeanList != null && purchaseOrderBeanList.size() > 0) {
            if (purchaseOrderBeanList.get(0).getStrSupplierCode().isEmpty()) {
                msgBox.Show(" Insufficient Information ", "Supplier is not in Database. Please add supplier");
                return;
            }
        }

        try{

            Date date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(edtInvoiceDate.getText().toString());
            long milliseconds = date.getTime();
            String invodate = String.valueOf(milliseconds);

            Cursor billAlreadyPresent_crsr = HomeActivity.dbHandler.getPurchaseOrder_for_SupplierCode(edtInvoiceNo.getText().toString().trim(),invodate,String.valueOf(purchaseOrderBeanList.get(0).getStrSupplierCode()));
            if(billAlreadyPresent_crsr!=null && billAlreadyPresent_crsr.moveToFirst())
            {
                msgBox.setIcon(R.drawable.ic_launcher_background)
                        .setTitle("Duplicate")
                        .setMessage("Please note, For this supplier, an invoice is already present with same invoice no and date." +
                                "\nDo you want these item(s) to append in the invoice.")
                        .setNegativeButton("Cancel",null)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                goodsinward();
                            }
                        })
                        .show();
            }else
            {
                goodsinward();
            }

        }catch(Exception e)
        {
            e.printStackTrace();
            msgBox.Show("Oops","Some error came while processing ");
            return ;
        }
    }

    void goodsinward() {

        String purchaseorderno = avPurchaseOrder.getText().toString();
        String invoiceno = edtInvoiceNo.getText().toString();
        String invoicedate = edtInvoiceDate.getText().toString();

        long l =-1;
        try {
            for(PurchaseOrderBean po : purchaseOrderBeanList)
            {
                String itemname_str = po.getStrItemName();
                double quantity_d = po.getDblQuantity();
                String uom_str = po.getStrUOM();
                Cursor item_present_crsr = HomeActivity.dbHandler.mGetItems(itemname_str);
                try {
                    if (item_present_crsr != null && item_present_crsr.moveToFirst()) {
                        // already present , needs to update
                        double dblQtyFromDB = item_present_crsr.getDouble(item_present_crsr.getColumnIndex(DatabaseHandler.KEY_Quantity));
                        quantity_d += dblQtyFromDB;
                        ContentValues cvItems = new ContentValues();
                        cvItems.put(DatabaseHandler.KEY_Quantity, quantity_d);
                        l = HomeActivity.dbHandler.mItemUpdateGoodsInwardNote(itemname_str, cvItems);
                        if (l > -1) {
                            Log.d(" GoodsInwardNote ", itemname_str + " updated  successfully at " + l);
                        }
                    }
                }catch (Exception ex){
                    Logger.i(TAG, "error on updating item quantity." +ex.getMessage());
                }finally {
                    if(item_present_crsr != null){
                        item_present_crsr.close();
                    }
                }
            }
            if(l>-1)
            {
                savePurchaseOrder(1);
                Toast.makeText(getActivity(), " Item added Successfully", Toast.LENGTH_SHORT).show();
            }
            mClear();
        } catch (Exception e)
        {
            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("GoodsInwardNote", " "+e.getMessage());
        }
    }


    public void mDateSelection_inward ()
    {
        String currentdate = DateFormat.format("yyyy-MM-dd", (new Date()).getTime()).toString();
        DateTime objDate = new DateTime(currentdate);
        try {
            AlertDialog.Builder dlgReportDate = new AlertDialog.Builder(getActivity());
            final DatePicker dateReportDate = new DatePicker(getActivity());
            // Initialize date picker value to business date
            dateReportDate.updateDate(objDate.getYear(), objDate.getMonth(), objDate.getDay());
            String strMessage = " Select the date";


            dlgReportDate
                    .setIcon(R.drawable.ic_launcher_background)
                    .setTitle("Date Selection")
                    .setMessage(strMessage)
                    .setView(dateReportDate)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            if (dateReportDate.getDayOfMonth() < 10) {
                                strDate = "0" + String.valueOf(dateReportDate.getDayOfMonth())+"-";
                            } else {
                                strDate = String.valueOf(dateReportDate.getDayOfMonth())+"-";
                            }
                            if (dateReportDate.getMonth() < 9) {
                                strDate += "0" + String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            } else {
                                strDate += String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            }

                            strDate += String.valueOf(dateReportDate.getYear());

                            edtInvoiceDate.setText(strDate);

                            Logger.d("ReportDate", "Selected Date:" + strDate);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    })
                    .show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private double mCalculateSubTotal(){
        double dblSubTotal = 0;
        for(int i = 0; i < purchaseOrderBeanList.size(); i++){
            dblSubTotal = dblSubTotal + purchaseOrderBeanList.get(i).getDblAmount();
        }
        return dblSubTotal;
    }

    private double mCalculateGrandTotal(double dblAdditionalCharges){
        double dblGrandTotal = 0;
        for(int i = 0; i < purchaseOrderBeanList.size(); i++){
            dblGrandTotal = dblGrandTotal + purchaseOrderBeanList.get(i).getDblAmount();
        }
        dblGrandTotal = dblGrandTotal + dblAdditionalCharges;
        return dblGrandTotal;
    }

    public boolean isNumeric(String s) {
        return s.matches("[-+]?\\d*\\.?\\d+");
    }

    private void mClear()
    {
        avSupplierName.setText("");
        avItemName.setText("");
        avPurchaseOrder.setText("");
        edtInvoiceNo.setText("");
        edtInvoiceDate.setText("");
        edtPhone.setText("");
        edtAddress.setText("");
        edtGSTIN.setText("");
        edtQty.setText("");
        edtAdditionalAmt.setText("");
        edtAdditionalChargeName.setText("");
        edtSubTotal.setText("");
        edtGrandTotal.setText("");
        cbStateCode.setChecked(false);
        spStateCode.setEnabled(false);
        cbAdditionalChargeStatus.setChecked(false);
        purchaseOrderBeanList.clear();
        purchaseOrderBean = null;
        supplier_model = null;
        if(purchaseOrderAdapter != null){
            purchaseOrderAdapter.notifyDataSetChanged(purchaseOrderBeanList);
        }
    }

    @Override
    public void onPurchaseOrderListItemDeleteSuccess() {
        mPopulatingDataToAdapter();
    }
}
