package pos.wepindia.com.retail.view.CustomerMasters;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.EMOJI_FILTER;
import pos.wepindia.com.retail.view.CustomerMasters.CustomerAdaptersAndListeners.CustomerAdapter;
import pos.wepindia.com.retail.view.CustomerMasters.CustomerAdaptersAndListeners.OnCustomerAddListener;
import pos.wepindia.com.retail.view.CustomerMasters.CustomerAdaptersAndListeners.OnCustomerSelectListener;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.Customer;

/**
 * Created by MohanN on 12/14/2017.
 */

public class CustomerListFragment extends Fragment implements OnCustomerSelectListener, OnCustomerAddListener {

    private static final String TAG = CustomerListFragment.class.getName();


    @BindView(R.id.bt_customer_master_list_add)     Button btnCustomerMasterAdd;
    @BindView(R.id.bt_customer_master_list_count)   Button btnCustomerListCount;
    @BindView(R.id.rv_customer_master_list)         RecyclerView rvCustomerList;
    @BindView(R.id.autocomplete_customer_master_list_search)    AutoCompleteTextView actv_customerSearch;

    private List<Customer> customerList;
    private List<String> customerSearchList;

    View rootView;
    MessageDialog msgBox;
    Context myContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.customer_master_list_fragment, container, false);
       try {

           ButterKnife.bind(this, rootView);

       }catch (Exception ex){
           Logger.i(TAG, "Unable init customer list fragment. " + ex.getMessage());
       }


        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("CustomerMaster");
        //App crash error log
        if (Logger.LOG_FOR_FIRST) {
            Logger.printLog();
            Logger.LOG_FOR_FIRST = false;
        }
        if (Auditing.AUDIT_FOR_FIRST) {
            Auditing.printAudit();
            Auditing.AUDIT_FOR_FIRST = false;
        }
        myContext = getActivity();

        msgBox = new MessageDialog(myContext);
        customerList = new ArrayList<Customer>();
        customerSearchList = new ArrayList<>();
        clickEvent();
        applyValidations();
    }

    @Override
    public void onResume() {
        super.onResume();
        mloadAutoCompleteAndDisplayCustomerData();
    }

    @OnClick({R.id.bt_customer_master_list_add})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.bt_customer_master_list_add:

                CustomerMasterFragment myFragment =
                        (CustomerMasterFragment)getFragmentManager().findFragmentByTag("Add New Customer");
                if (myFragment != null && myFragment.isVisible()) {
                    // prevent multiple opening of same dialogs
                    return;
                }

                FragmentManager fm = getActivity().getSupportFragmentManager();
                CustomerMasterFragment customerMasterFragment = new CustomerMasterFragment();
                customerMasterFragment.mInitListener(this);
                customerMasterFragment.show(fm,"Add New Customer");
                break;
            default:
                break;
        }
    }

    private void mloadAutoCompleteAndDisplayCustomerData(){

        new loadAutoCompleteAndDisplayCustomerData().execute();
    }

    class loadAutoCompleteAndDisplayCustomerData extends AsyncTask<Void , Void, Void>{
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(myContext);
            pd.setTitle("Loading Customer Data");
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor crsrCustomer = null;
            customerList.clear();
            customerSearchList.clear();
            try {
                crsrCustomer = HomeActivity.dbHandler.getAllCustomer();
                while(crsrCustomer != null && crsrCustomer.getCount() > 0 && crsrCustomer.moveToNext())
                {
                    Customer customer = getCustomerFromCursor(crsrCustomer);
                    customerList.add(customer);

                    String custSearchData = crsrCustomer.getString(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_CustName)) + " - " +
                                            crsrCustomer.getString(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_CustPhoneNo));
                    customerSearchList.add(custSearchData);

                }
            } catch (Exception ex){
                Logger.i(TAG,ex.getMessage());
            } finally {
                if(crsrCustomer != null){
                    crsrCustomer.close();
                }
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(customerList.size() > 0) {
                rvCustomerList.setLayoutManager(new LinearLayoutManager(myContext));
                rvCustomerList.setAdapter(new CustomerAdapter(myContext,CustomerListFragment.this, customerList));
                rvCustomerList.addItemDecoration(new DividerItemDecoration(myContext, DividerItemDecoration.VERTICAL));
                btnCustomerListCount.setText(""+customerList.size());

                ArrayAdapter<String> dataAdapter_actv = new ArrayAdapter<String>(myContext, android.R.layout.simple_list_item_1, customerSearchList);
                dataAdapter_actv.setDropDownViewResource(android.R.layout.simple_list_item_1);
                actv_customerSearch.setAdapter(dataAdapter_actv);

            }
            pd.dismiss();
        }
    }

    Customer getCustomerFromCursor(Cursor crsrCustomer)
    {
        Customer customer = new Customer();
        customer.set_id(crsrCustomer.getInt(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_id)));
        customer.setiCustId(crsrCustomer.getInt(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_CustId)));
        customer.setStrCustGSTIN(crsrCustomer.getString(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_GSTIN)));
        customer.setStrCustName(crsrCustomer.getString(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_CustName)));
        customer.setStrCustContactNumber(crsrCustomer.getString(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_CustPhoneNo)));
        customer.setStrCustAddress(crsrCustomer.getString(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_CustAddress)));
        customer.setStrEmailId(crsrCustomer.getString(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_CUST_EMAIL)));
        customer.setdLastTransaction(crsrCustomer.getDouble(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_LastTransaction)));
        customer.setdTotalTransaction(crsrCustomer.getDouble(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_TotalTransaction)));
        customer.setdCreditAmount(crsrCustomer.getDouble(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_CreditAmount)));
        customer.setdCreditLimit(crsrCustomer.getDouble(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_CreditLimit)));
        customer.setiRewardPoints(crsrCustomer.getInt(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_RewardPointsAccumulated)));
        customer.setIsActive(crsrCustomer.getInt(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_isActive)));
        customer.setOpeningBalance(crsrCustomer.getDouble(crsrCustomer.getColumnIndex(DatabaseHandler.KEY_OpeningBalance)));

        return customer;
    }

    void clickEvent()
    {
        actv_customerSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data = actv_customerSearch.getText().toString();
                String[] parts = data.split("-");
                final String custName = parts[0].trim();
                final String custPhoneNo = parts[1].trim();
                Cursor cursor = HomeActivity.dbHandler.getCustomer(custName,custPhoneNo);
                if(cursor!=null && cursor.moveToNext())
                {
                    Customer customer = getCustomerFromCursor(cursor);
                    FragmentManager fm = getActivity().getSupportFragmentManager();
                    CustomerMasterFragment customerMasterFragment = new CustomerMasterFragment();
                    customerMasterFragment.mInitListener(CustomerListFragment.this);
                    Bundle bundle=new Bundle();
                    bundle.putParcelable(Customer.CUSTOMER_PARCELABLE_KEY,customer);
                    customerMasterFragment.setArguments(bundle);
                    customerMasterFragment.show(fm,"Edit Customer");
                }
                actv_customerSearch.setText("");
            }});

    }
    @Override
    public void onRowDataSelect(Customer customer) {

        List<Fragment>list = getFragmentManager().getFragments();
        for(Fragment frag : list)
        {
            if(frag instanceof CustomerMasterFragment)
                return;
        }

        FragmentManager fm = getActivity().getSupportFragmentManager();
        CustomerMasterFragment customerMasterFragment = new CustomerMasterFragment();
        customerMasterFragment.mInitListener(this);
        Bundle bundle=new Bundle();
        bundle.putParcelable(Customer.CUSTOMER_PARCELABLE_KEY,customer);
        customerMasterFragment.setArguments(bundle);
        customerMasterFragment.show(fm,"Edit Customer");
    }

    void applyValidations()
    {
        actv_customerSearch.setFilters(new InputFilter[]{new EMOJI_FILTER()});
    }
    @Override
    public void onCustomerAddSuccess(Customer customer) {
        mloadAutoCompleteAndDisplayCustomerData();
    }
}