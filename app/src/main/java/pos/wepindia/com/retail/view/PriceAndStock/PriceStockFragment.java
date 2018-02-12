package pos.wepindia.com.retail.view.PriceAndStock;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.EMOJI_FILTER;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.retail.view.PriceAndStock.AdaptersAndListeners.OnItemSelectListener;
import pos.wepindia.com.retail.view.PriceAndStock.AdaptersAndListeners.SimpleCursorRecyclerAdapter;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.ItemMasterBean;

/**
 * Created by MohanN on 12/29/2017.
 */

public class PriceStockFragment extends Fragment implements OnItemSelectListener {

    private static final String TAG = PriceStockFragment.class.getName();

    View view;
    MessageDialog msgBox;

    SimpleCursorRecyclerAdapter adapter;
    ProgressDialog progressDialog;

    @BindView(R.id.rg_price_stock_list)
    RadioGroup rg_displayItemCriteria;

    @BindView(R.id.rb_price_stock_list_all)
    RadioButton rbAll;
    @BindView(R.id.rb_price_stock_list_brand)
    RadioButton rbBrand;
    @BindView(R.id.rb_price_stock_list_department)
    RadioButton rbDepartment;
    @BindView(R.id.rb_price_stock_list_category)
    RadioButton rbCategory;
    @BindView(R.id.rb_price_stock_list_active)
    RadioButton rbActive;
    @BindView(R.id.rb_price_stock_list_inactive)
    RadioButton rbInactive;

    @BindView(R.id.bt_price_stock_list_count)
    Button btnItemCount;

    @BindView(R.id.rv_price_stock_list_items)
    RecyclerView rv_List;

    @BindView(R.id.autocomplete_price_stock_list_search)     AutoCompleteTextView actv_itemsearch;

    String[] from = {DatabaseHandler.KEY_ItemShortName, DatabaseHandler.KEY_Quantity, DatabaseHandler.KEY_MRP, DatabaseHandler.KEY_RetailPrice, DatabaseHandler.KEY_WholeSalePrice};
    int[] to = new int[]{R.id.tv_item_master_list_title_name, R.id.tv_item_master_list_title_qty,
            R.id.tv_item_master_list_title_uom,
            R.id.tv_item_master_list_title_retail, R.id.tv_item_master_list_title_disc};

    Context myContext;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.price_stock_fragment, container, false);

        try {
            ButterKnife.bind(this, view);

            if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }
        } catch (Exception ex) {
            Log.i(TAG, "Initialization error.");
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myContext = getActivity();
        msgBox = new MessageDialog(getActivity());
        applyValidations();
        clickEvent();
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            displayAndLoadAutoCompletedata();
        }catch (Exception e)
        {
            Logger.e(TAG,e.getMessage());
        }
    }

    void displayAndLoadAutoCompletedata()
    {
        displayItemList();
        loadAutocompleteData();
    }
    public  void displayItemList()
    {
        try{
            new LoadItems().execute();
        }catch (Exception e)
        {
            Logger.e(TAG,e.getMessage());
        }
    }

    @Override
    public void onItemClick(ItemMasterBean itemMasterBean) {
        if(itemMasterBean != null){
            FragmentManager fm = getActivity().getSupportFragmentManager();
            PriceStockEditDialogFragment priceStockEditDialogFragment = new PriceStockEditDialogFragment();
            priceStockEditDialogFragment.intiListener(this);
            Bundle bundle=new Bundle();
            bundle.putParcelable(ItemMasterBean.ITEM_MASTER_PARCELABLE_KEY,itemMasterBean);
            priceStockEditDialogFragment.setArguments(bundle);
            priceStockEditDialogFragment.show(fm,"Update Price and Stock");
        } else {
            String StrMsg = "Unable to populate the price stock data for update.";
            Logger.i(TAG, StrMsg );
            msgBox.Show("Error",StrMsg);
        }
    }


    @OnClick({R.id.rb_price_stock_list_all, R.id.rb_price_stock_list_brand, R.id.rb_price_stock_list_department,
            R.id.rb_price_stock_list_category,
            R.id.rb_price_stock_list_active, R.id.rb_price_stock_list_inactive})
    public void onRadioButtonClicked(RadioButton radioButton) {

        displayItemList();
    }

    class LoadItems extends AsyncTask<Void, Void, Void> {
        int itemCount = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Its loading....");
            progressDialog.setTitle("ProgressDialog bar ");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
            //t1.start();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor cursor = null;
            switch (rg_displayItemCriteria.getCheckedRadioButtonId()) {
                case R.id.rb_price_stock_list_all:
                    cursor = HomeActivity.dbHandler.getAllItems();
                    break;
                case R.id.rb_price_stock_list_brand:
                    cursor = HomeActivity.dbHandler.getAllItems_Brandwise();
                    break;
                case R.id.rb_price_stock_list_department:
                    cursor = HomeActivity.dbHandler.getAllItems_Departmentwise();
                    break;
                case R.id.rb_price_stock_list_category:
                    cursor = HomeActivity.dbHandler.getAllItems_Categorywise();
                    break;
                case R.id.rb_price_stock_list_active:
                    cursor = HomeActivity.dbHandler.getAllItems_ActiveItems();
                    break;
                case R.id.rb_price_stock_list_inactive:
                    cursor = HomeActivity.dbHandler.getAllItems_InactiveItems();
                    break;
                default:
                    cursor = HomeActivity.dbHandler.getAllItems_ActiveItems();
                    break;
            }
            itemCount = cursor.getCount();
            if (adapter != null)
                adapter = null;
            adapter = new SimpleCursorRecyclerAdapter(R.layout.row_item_list, cursor, from, to,PriceStockFragment.this);
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            //handle.sendMessage(handle.obtainMessage());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            rv_List.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv_List.setAdapter(adapter);
            btnItemCount.setText(String.valueOf(itemCount));
            if (progressDialog != null)
                progressDialog.dismiss();
        }
    }

    public void loadAutocompleteData() {
        new AsyncTask<Void, Void, Void>() {
            ArrayList<String> list_actv = new ArrayList<>();
            ProgressDialog pd;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(myContext);
                pd.setMessage("Loading AutoComplete Data");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                list_actv = HomeActivity.dbHandler.getAllItems_for_autocomplete();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                pd.dismiss();
                ArrayAdapter<String> dataAdapter_actv = new ArrayAdapter<String>(myContext, android.R.layout.simple_list_item_1, list_actv);
                dataAdapter_actv.setDropDownViewResource(android.R.layout.simple_list_item_1);
                actv_itemsearch.setAdapter(dataAdapter_actv);

            }

        }.execute();
    }


    void clickEvent() {
        actv_itemsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data = actv_itemsearch.getText().toString();
                //System.out.println("Richa : "+data);
                String[] parts = data.split("-");
                final String shortCode = parts[0].trim();
                final String itemShortName = parts[1].trim();
                final String itemBarcode = parts[2].trim();

                new AsyncTask<Void, Void, Void>() {
                    int itemCount = 0;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = new ProgressDialog(myContext);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Its loading....");
                        progressDialog.setTitle("ProgressDialog bar ");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        //t1.start();

                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        Cursor cursor = HomeActivity.dbHandler.getAllItems_details(Integer.parseInt(shortCode), itemShortName, itemBarcode);
                        itemCount = cursor.getCount();
                        if (adapter != null)
                            adapter = null;
                        adapter = new SimpleCursorRecyclerAdapter(R.layout.row_item_list, cursor, from, to,PriceStockFragment.this);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        //deptCAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        rv_List.setLayoutManager(new LinearLayoutManager(myContext));
                        rv_List.setAdapter(adapter);
                        btnItemCount.setText(String.valueOf(itemCount));
                        actv_itemsearch.setText("");
                        //progressBar.clearAnimation();
                    }

                }.execute();
            }
        });
    }

    void applyValidations()
    {
        actv_itemsearch.setFilters(new InputFilter[]{new EMOJI_FILTER()});
    }

    @Override
    public void onPriceStockUpdateSuccess() {
        displayItemList();
    }

}