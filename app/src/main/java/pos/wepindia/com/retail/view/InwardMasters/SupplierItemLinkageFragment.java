package pos.wepindia.com.retail.view.InwardMasters;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.AppConstants;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.retail.view.InwardMasters.InwardAdaptersAndListeners.SupplierItemLinkageAdapter;
import pos.wepindia.com.retail.view.InwardMasters.InwardAdaptersAndListeners.SupplierSuggestionAdapter;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.ItemMasterBean;
import pos.wepindia.com.wepbase.model.pojos.SupplierItemLinkageBean;
import pos.wepindia.com.wepbase.model.pojos.Supplier_Model;

/**
 * Created by MohanN on 1/3/2018.
 */

public class SupplierItemLinkageFragment extends Fragment {

    private static final String TAG = SupplierItemLinkageFragment.class.getName();
    private final int RESET = 1;


    @BindView(R.id.bt_supplier_item_linkage_link_item)
    Button btnLink;
    @BindView(R.id.bt_supplier_item_linkage_unlink_item)
    Button btnUlink;
    @BindView(R.id.bt_supplier_item_linkage_clear)
    Button btnClear;

    @BindView(R.id.lv_supplier_item_linkage_list)
    ListView lvSupplierItemLinkage;

    @BindView(R.id.av_supplier_item_linkage_supplier_name)
    AutoCompleteTextView avSupplierName;
    @BindView(R.id.av_supplier_item_linkage_item_name)
    AutoCompleteTextView avItemName;
    @BindView(R.id.et_supplier_item_linkage_gstin)
    EditText edtGSTIN;
    @BindView(R.id.et_supplier_item_linkage_phone)
    EditText edtPhone;
    @BindView(R.id.et_supplier_item_linkage_address)
    EditText edtAddress;

    @BindView(R.id.et_supplier_item_linkage_rate)
    EditText edtRate;
    @BindView(R.id.et_supplier_item_linkage_hsn_code)
    EditText edtHSNCode;
    @BindView(R.id.et_supplier_item_linkage_supply_type)
    EditText edtSupplyType;
    @BindView(R.id.et_supplier_item_linkage_uom)
    EditText edtUOM;
    @BindView(R.id.et_supplier_item_linkage_cgst_rate)
    EditText edtCGSTRate;
    @BindView(R.id.et_supplier_item_linkage_utgst_sgst_rate)
    EditText edtUtgstSgstRate;
    @BindView(R.id.et_supplier_item_linkage_igst_rate)
    EditText edtIGSTRate;
    @BindView(R.id.et_supplier_item_linkage_cess_rate)
    EditText edtCessRate;

    View rootView;
    MessageDialog msgBox;

    ArrayList<HashMap<String, String>> listName;
    SupplierItemLinkageBean supplierItemLinkageBean;
    List<SupplierItemLinkageBean> supplierItemLinkageBeanList;

    SupplierItemLinkageAdapter supplierItemLinkageAdapter = null;

    Supplier_Model supplier_model = null;
    ItemMasterBean itemMasterBean = null;
    SimpleCursorAdapter mAdapterItemData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.supplier_item_linkage_fragment, container, false);
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

            avSupplierName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    HashMap<String, String> data = listName.get(position);
                    String suppliername_str = data.get("name");
                    String supplierphone_str = data.get("phone");

                    avSupplierName.setText(data.get("name")); // This is required DO NOT IGNORE
                    Cursor cSupplierDetails = null;
                    try {
                        cSupplierDetails = HomeActivity.dbHandler.getSupplierDetailsByPhone(supplierphone_str);
                        if (cSupplierDetails != null && cSupplierDetails.moveToFirst()) {
                            supplier_model = new Supplier_Model();
                            supplier_model.setSupplierName(cSupplierDetails.getString(cSupplierDetails.getColumnIndex(DatabaseHandler.KEY_SUPPLIERNAME)));
                            supplier_model.setSupplierPhone(cSupplierDetails.getString(cSupplierDetails.getColumnIndex(DatabaseHandler.KEY_SupplierPhone)));
                            supplier_model.setSupplierAddress(cSupplierDetails.getString(cSupplierDetails.getColumnIndex(DatabaseHandler.KEY_SupplierAddress)));
                            supplier_model.setSupplierCode(cSupplierDetails.getInt(cSupplierDetails.getColumnIndex(DatabaseHandler.KEY_SupplierCode)));
                                if (cSupplierDetails.getString(cSupplierDetails.getColumnIndex(DatabaseHandler.KEY_GSTIN)) != null) {
                                    supplier_model.setSupplierGSTIN(cSupplierDetails.getString(cSupplierDetails.getColumnIndex(DatabaseHandler.KEY_GSTIN)));
                                }
                            if (cSupplierDetails.getString(cSupplierDetails.getColumnIndex(DatabaseHandler.KEY_SupplierType)) != null) {
                                supplier_model.setStrSupplierType(cSupplierDetails.getString(cSupplierDetails.getColumnIndex(DatabaseHandler.KEY_SupplierType)));
                            }
                            edtPhone.setText(supplier_model.getSupplierPhone());
                            edtAddress.setText(supplier_model.getSupplierAddress());
                            if (supplier_model.getSupplierGSTIN() != null) {
                                edtGSTIN.setText(supplier_model.getSupplierGSTIN());
                            }
                            mPopulateDataToAdapterList(supplier_model.getSupplierPhone());
                        }
                    }catch (Exception ex){
                        Logger.i(TAG,"Unable to fetch data from supplier details table based on supplier phone no. " +ex.getMessage());
                    }finally {
                        if(cSupplierDetails != null){
                            cSupplierDetails.close();
                        }
                    }
                }
            });

            lvSupplierItemLinkage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    supplierItemLinkageBean = supplierItemLinkageBeanList.get(position);
                    if(supplierItemLinkageBean != null){
                        edtPhone.setText(supplierItemLinkageBean.getStrSupplierPhone());
                        avItemName.setText(supplierItemLinkageBean.getStrItemName());
                        edtRate.setText(""+supplierItemLinkageBean.getDblRate());
                        if(!supplierItemLinkageBean.getStrHSNCode().isEmpty()){
                            edtHSNCode.setText(supplierItemLinkageBean.getStrHSNCode());
                        }
                        if(supplierItemLinkageBean.getStrSupplierType() != null && !supplierItemLinkageBean.getStrSupplierType().isEmpty()){
                            edtSupplyType.setText(supplierItemLinkageBean.getStrSupplierType());
                        }
                        if(!supplierItemLinkageBean.getStrUOM().isEmpty()){
                            edtUOM.setText(supplierItemLinkageBean.getStrUOM());
                        }
                        if(supplierItemLinkageBean.getDblCGSTPer() > 0){
                            edtCGSTRate.setText(""+supplierItemLinkageBean.getDblCGSTPer());
                        }
                        if(supplierItemLinkageBean.getDblUTGST_SGSTPer() > 0){
                            edtUtgstSgstRate.setText(""+supplierItemLinkageBean.getDblUTGST_SGSTPer());
                        }
                        if(supplierItemLinkageBean.getDblIGSTPer() > 0){
                            edtIGSTRate.setText(""+supplierItemLinkageBean.getDblIGSTPer());
                        }
                        if(supplierItemLinkageBean.getDblCessPer() > 0){
                            edtCessRate.setText(""+supplierItemLinkageBean.getDblCessPer());
                        }
                    }
                }
            });

        }catch (Exception ex){
            Log.i(TAG, "Unable init supplier details fragment. " + ex.getMessage());
        }

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle(getString(R.string.supplier_item_linkage));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAutoCompleteSupplierName();
        initAutoCompleteTextData();
    }

    @OnClick({R.id.bt_supplier_item_linkage_link_item, R.id.bt_supplier_item_linkage_unlink_item,
            R.id.bt_supplier_item_linkage_clear})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_supplier_item_linkage_link_item:
                LinkItem();
                break;
            case R.id.bt_supplier_item_linkage_unlink_item:
                mUnlinkItem();
                break;
            case R.id.bt_supplier_item_linkage_clear:
                mClear(0);
                break;
            default:
                break;
        }
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
            avSupplierName.setAdapter(dataAdapter);

        } catch (Exception e) {
            e.printStackTrace();
            msgBox.Show("Error",e.getMessage());
        } finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }

    public void  LinkItem()
    {
        if(avSupplierName.getText().toString().isEmpty() && supplier_model != null)
        {
            msgBox.Show("Insufficient Information", "Kindly enter valid supplier ");
            return;
        }
        if(avItemName.getText().toString().isEmpty() && itemMasterBean != null)
        {
            msgBox.Show("Insufficient Information", "Kindly enter valid item");
            return;
        }
        if(edtRate.getText().toString().isEmpty())
        {
            msgBox.Show("Insufficient Information", "Please enter purchase rate.");
            return;
        }
        SupplierItemLinkageBean supplierItemLinkageBean = new SupplierItemLinkageBean();
        supplierItemLinkageBean.setiSupplierCode(supplier_model.getSupplierCode());
        supplierItemLinkageBean.setStrSupplierName(avSupplierName.getText().toString());
        supplierItemLinkageBean.setStrSupplierPhone(edtPhone.getText().toString());
        supplierItemLinkageBean.setStrSupplierAddress(edtAddress.getText().toString());
        if(supplier_model.getStrSupplierType() != null){
            supplierItemLinkageBean.setStrSupplierType(supplier_model.getStrSupplierType());
        }
        if(!edtGSTIN.getText().toString().isEmpty()){
            supplierItemLinkageBean.setStrGSTIN(edtGSTIN.getText().toString());
        }
        supplierItemLinkageBean.setiItemID(itemMasterBean.get_id());
        supplierItemLinkageBean.setStrBarcode(itemMasterBean.getStrBarcode());
        supplierItemLinkageBean.setStrItemName(itemMasterBean.getStrLongName());
        supplierItemLinkageBean.setDblRate(itemMasterBean.getDblMRP());
        if(!edtHSNCode.getText().toString().isEmpty()) {
            supplierItemLinkageBean.setStrHSNCode(edtHSNCode.getText().toString());
        }
        if(!edtSupplyType.getText().toString().isEmpty()) {
            supplierItemLinkageBean.setStrSupplyType(edtSupplyType.getText().toString());
        }
        if(!edtUOM.getText().toString().isEmpty()) {
            supplierItemLinkageBean.setStrUOM(edtUOM.getText().toString());
        }
        if(!edtRate.getText().toString().isEmpty()){
            double dblRate = Double.parseDouble(edtRate.getText().toString());
           supplierItemLinkageBean.setDblRate(dblRate);
        }
        if(itemMasterBean.getDblMRP() > 0){
            supplierItemLinkageBean.setDblMRP(itemMasterBean.getDblMRP());
        }
        if(itemMasterBean.getDblRetailPrice() > 0){
            supplierItemLinkageBean.setDblRetailPrice(itemMasterBean.getDblRetailPrice());
        }
        if(itemMasterBean.getDblWholeSalePrice() > 0){
            supplierItemLinkageBean.setDblWholeSalePrice(itemMasterBean.getDblWholeSalePrice());
        }

        if(!edtCGSTRate.getText().toString().isEmpty()){
            double dblCGST = Double.parseDouble(edtCGSTRate.getText().toString());
            supplierItemLinkageBean.setDblCGSTPer(dblCGST);
        }
        if(!edtUtgstSgstRate.getText().toString().isEmpty()){
            double dblSGST = Double.parseDouble(edtUtgstSgstRate.getText().toString());
            supplierItemLinkageBean.setDblUTGST_SGSTPer(dblSGST);
        }
        if(!edtIGSTRate.getText().toString().isEmpty()){
            double dblIGST = Double.parseDouble(edtIGSTRate.getText().toString());
            supplierItemLinkageBean.setDblIGSTPer(dblIGST);
        }
        if(!edtCessRate.getText().toString().isEmpty()){
            double dblCess = Double.parseDouble(edtCessRate.getText().toString());
            supplierItemLinkageBean.setDblCessPer(dblCess);
        }
        if(itemMasterBean.getStrSupplyType() != null){
            supplierItemLinkageBean.setStrSupplyType(itemMasterBean.getStrSupplyType());
        }
        long lLinkItemStatus = -1;

        if(supplierItemLinkageBeanList != null && supplierItemLinkageBeanList.size() > 0 && supplierItemLinkageBeanList.contains(supplierItemLinkageBean)){
            lLinkItemStatus = HomeActivity.dbHandler.mLinkSupplierWithItem(supplierItemLinkageBean, AppConstants.UPDATE);
        } else {
            lLinkItemStatus = HomeActivity.dbHandler.mLinkSupplierWithItem(supplierItemLinkageBean, AppConstants.INSERT);
        }
        if(lLinkItemStatus>0)
        {
            String itemname = avItemName.getText().toString();
            String suppliername = avSupplierName.getText().toString();
            String msg = itemname+" linked successfully to supplier "+suppliername;
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            Logger.d("ItemSupplierLinkageAct",msg);
            mClear(RESET);
            mPopulateDataToAdapterList(supplier_model.getSupplierPhone());
        }else
        {
            Toast.makeText(getActivity(), "Linkage cannot happen", Toast.LENGTH_SHORT).show();
            Logger.d("ItemSupplierLinkageAct","Linkage cannot happen");
        }
    }

    private void mUnlinkItem(){
        if(avSupplierName.getText().toString().isEmpty())
        {
            msgBox.Show("Insufficient Information", "Kindly enter valid supplier ");
            return;
        }
        if(avItemName.getText().toString().isEmpty())
        {
            msgBox.Show("Insufficient Information", "Kindly enter valid item");
            return;
        }
        if(supplierItemLinkageBean != null){
            if(HomeActivity.dbHandler.mUnLinkSupplierWithItem(""+supplierItemLinkageBean.getiSupplierCode(),""+supplierItemLinkageBean.getiItemID()) > 0){
                mClear(RESET);
                mPopulateDataToAdapterList(supplierItemLinkageBean.getStrSupplierPhone());
            }
        }
    }

    private void mPopulateDataToAdapterList(String strPhone){
        Cursor cSupplierItemLinkage = null;
        try {
            cSupplierItemLinkage = HomeActivity.dbHandler.getSupplierItemlinkageByPhone(strPhone);

            if (cSupplierItemLinkage != null && cSupplierItemLinkage.moveToFirst()) {
                supplierItemLinkageBeanList = new ArrayList<SupplierItemLinkageBean>();
                do {
                    supplierItemLinkageBean = new SupplierItemLinkageBean();
                    supplierItemLinkageBean.setStrSupplierName(cSupplierItemLinkage.getString(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_SUPPLIERNAME)));
                    supplierItemLinkageBean.setStrSupplierPhone(cSupplierItemLinkage.getString(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_SupplierPhone)));
                    supplierItemLinkageBean.setStrSupplierAddress(cSupplierItemLinkage.getString(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_SupplierAddress)));
                    supplierItemLinkageBean.setiSupplierCode(cSupplierItemLinkage.getInt(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_SupplierCode)));
                    if (cSupplierItemLinkage.getString(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_GSTIN)) != null) {
                        supplierItemLinkageBean.setStrGSTIN(cSupplierItemLinkage.getString(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_GSTIN)));
                    }
                    supplierItemLinkageBean.set_id(cSupplierItemLinkage.getInt(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_id)));
                    supplierItemLinkageBean.setiItemID(cSupplierItemLinkage.getInt(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_ITEM_ID)));
                    if(cSupplierItemLinkage.getString(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)) != null) {
                        supplierItemLinkageBean.setStrBarcode(cSupplierItemLinkage.getString(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)));
                    }
                    supplierItemLinkageBean.setStrItemName(cSupplierItemLinkage.getString(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_ItemShortName)));
                    supplierItemLinkageBean.setDblRate(cSupplierItemLinkage.getDouble(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_RetailPrice)));
                    if(cSupplierItemLinkage.getDouble(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_MRP)) > 0) {
                        supplierItemLinkageBean.setDblMRP(cSupplierItemLinkage.getDouble(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_MRP)));
                    }
                    supplierItemLinkageBean.setStrHSNCode(cSupplierItemLinkage.getString(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_HSNCode)));
                    supplierItemLinkageBean.setStrUOM(cSupplierItemLinkage.getString(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.KEY_UOM)));
                    supplierItemLinkageBean.setDblCGSTPer(cSupplierItemLinkage.getDouble(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.CGST_RATE_PER)));
                    supplierItemLinkageBean.setDblUTGST_SGSTPer(cSupplierItemLinkage.getDouble(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.UTGST_SGST_RATE_PER)));
                    supplierItemLinkageBean.setDblIGSTPer(cSupplierItemLinkage.getDouble(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.IGST_RATE_PER)));
                    supplierItemLinkageBean.setDblCessPer(cSupplierItemLinkage.getDouble(cSupplierItemLinkage.getColumnIndex(DatabaseHandler.CESS_RATE_PER)));
                    supplierItemLinkageBeanList.add(supplierItemLinkageBean);
                }while(cSupplierItemLinkage.moveToNext());
            }
        }catch (Exception ex){
            Logger.i(TAG,"Unable to fetch data from supplier item linkage table based on supplier phone no. " +ex.getMessage());
        }finally {
            if(cSupplierItemLinkage != null){
                cSupplierItemLinkage.close();
            }
        }

        if(supplierItemLinkageBeanList != null && supplierItemLinkageBeanList.size() > 0){
            if(supplierItemLinkageAdapter == null)
            {
                supplierItemLinkageAdapter = new SupplierItemLinkageAdapter(getContext(),supplierItemLinkageBeanList);
                lvSupplierItemLinkage.setAdapter(supplierItemLinkageAdapter);
            }else
            {
                supplierItemLinkageAdapter.notifyDataSetChanged(supplierItemLinkageBeanList);
            }
        } else {
            msgBox.Show("Supplier Item Linkage","No Items linked with this supplier");
        }
    }

    private void initAutoCompleteTextData(){
        Cursor c =HomeActivity.dbHandler.getAllItems();
        if(c != null  && c.moveToFirst() ) {
            try {
                mAdapterItemData = new SimpleCursorAdapter(getActivity(), R.layout.auto_complete_textview_two_strings, c,
                        new String[]{DatabaseHandler.KEY_ItemShortName, DatabaseHandler.KEY_ItemBarcode},
                        new int[]{R.id.tv_auto_complete_textview_item_one, R.id.tv_auto_complete_textview_two},
                        0);

                avItemName.setAdapter(mAdapterItemData);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        mAdapterItemData.setFilterQueryProvider(new FilterQueryProvider() {
            public Cursor runQuery(CharSequence str) {
                Logger.i(TAG, "Item name search data." +str);
                if(str != null) {
                    return HomeActivity.dbHandler.mGetItemsForSupplierLinkage(str);
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
                    itemMasterBean = new ItemMasterBean();
                    itemMasterBean.set_id(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_id)));
                    itemMasterBean.setStrBarcode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)));
                    itemMasterBean.setStrLongName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemShortName)));
                    itemMasterBean.setDblMRP(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_MRP)));
                    if(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)) > 0){
                        itemMasterBean.setDblRetailPrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)));
                    }
                    if(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)) > 0){
                        itemMasterBean.setDblWholeSalePrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)));
                    }
                    if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)) != null){
                        itemMasterBean.setStrSupplyType(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_SupplyType)));
                    }
                    if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_HSNCode)) != null){
                        itemMasterBean.setStrHSNCode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_HSNCode)));
                    }
                    if(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_UOM)) != null){
                        itemMasterBean.setStrUOM(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_UOM)));
                    }
                    if(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_CGSTRate)) > 0){
                        itemMasterBean.setDblCGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_CGSTRate)));
                    }
                    if(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_SGSTRate)) > 0){
                        itemMasterBean.setDblSGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_SGSTRate)));
                    }
                    if(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_IGSTRate)) > 0){
                        itemMasterBean.setDblIGSTRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_IGSTRate)));
                    }
                    if(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_cessRate)) > 0){
                        itemMasterBean.setDblCessRate(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_cessRate)));
                    }
                }
                // Update the parent class's TextView
                if(itemMasterBean != null){
                    edtRate.setText("");
                    if(!itemMasterBean.getStrHSNCode().isEmpty()){
                        edtHSNCode.setText(itemMasterBean.getStrHSNCode());
                    }
                    if(!itemMasterBean.getStrUOM().isEmpty()){
                        edtUOM.setText(itemMasterBean.getStrUOM());
                    }
                    if(!itemMasterBean.getStrSupplyType().isEmpty()){
                        edtSupplyType.setText(itemMasterBean.getStrSupplyType());
                    }
                    if(itemMasterBean.getDblCGSTRate() > 0){
                        edtCGSTRate.setText(""+itemMasterBean.getDblCGSTRate());
                    }
                    if(itemMasterBean.getDblSGSTRate() > 0){
                        edtUtgstSgstRate.setText(""+itemMasterBean.getDblSGSTRate());
                    }
                    if(itemMasterBean.getDblIGSTRate() > 0){
                        edtIGSTRate.setText(""+itemMasterBean.getDblIGSTRate());
                    }
                    if(itemMasterBean.getDblCessRate() > 0){
                        edtCessRate.setText(""+itemMasterBean.getDblCessRate());
                    }
                }
            }
        });
    }

    public void mClear(int iReset)
    {
       //Item data reset
        avItemName.setText("");
        itemMasterBean = null;
        avItemName.setText("");
        edtRate.setText("");
        edtHSNCode.setText("");
        edtSupplyType.setText("");
        edtUOM.setText("");
        edtCGSTRate.setText("");
        edtUtgstSgstRate.setText("");
        edtIGSTRate.setText("");
        edtCessRate.setText("");
        if(iReset == RESET){
            return;
        }
        //Supplier details reset
        avSupplierName.setText("");
        edtGSTIN.setText("");
        edtPhone.setText("");
        edtAddress.setText("");
        supplier_model = null;
        if(supplierItemLinkageBeanList != null && supplierItemLinkageBeanList.size() > 0){
            supplierItemLinkageBeanList.clear();
            supplierItemLinkageAdapter.notify(supplierItemLinkageBeanList);
            supplierItemLinkageAdapter = null;
            supplierItemLinkageBeanList = null;
        }
        supplierItemLinkageBean = null;
    }
}