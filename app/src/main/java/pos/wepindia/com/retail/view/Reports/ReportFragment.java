package pos.wepindia.com.retail.view.Reports;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.adapters.ReportsAdapter;
import pos.wepindia.com.retail.view.UploadGSTReports.FragmentGSTLink;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;

/**
 * Created by SachinV on 23-01-2018.
 */

public class ReportFragment extends Fragment {

    @BindView(R.id.app_reports_tab)
    TabLayout tabLayout;
    @BindView(R.id.vp_app_reports)
    ViewPager viewPager;
    @BindView(R.id.app_reports_toolbar)
    Toolbar toolbar;
    ReportsAdapter adapter;
    private View view1;
    private MessageDialog msgBox;
    private Context myContext ;

    private static final String TAG = ReportFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view1 = inflater.inflate(R.layout.reports_fragment, container, false);
        ButterKnife.bind(this, view1);
        return view1;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getActivity().getString(R.string.reports));

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
            msgBox = new MessageDialog(getActivity());

            new loadReportsAsyncTask().execute();

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }
                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });

            viewPager.setOffscreenPageLimit(tabLayout.getTabCount());

        }catch (Exception e)
        {
            Logger.e(TAG, e.getMessage());
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ReportsAdapter(getChildFragmentManager());
        adapter.addFragment(new SalesReportFragment(), getString(R.string.sales_report));
        adapter.addFragment(new InventoryReport(), getString(R.string.inventory_report));
        adapter.addFragment(new EmployeeCustomerReport(), getString(R.string.employee_customer_report));
        adapter.addFragment(new GSTReports(), getString(R.string.gst_report));
        adapter.addFragment(new FragmentGSTLink(), getString(R.string.gst_link));
//        adapter.addFragment(new MachineSettingsFragment(), getString(R.string.machine));
        viewPager.setAdapter(adapter);
    }

    ViewPager.OnPageChangeListener myOnPageChangeListener =
            new ViewPager.OnPageChangeListener(){

                @Override
                public void onPageScrolled(final int i, final float v, final int i2) {
                }
                @Override
                public void onPageSelected(final int i) {
                    if(adapter!=null){
                        switch(adapter.getPageTitle(i).toString())
                        {

                            case "Sales Report":  SalesReportFragment fragment = (SalesReportFragment) adapter.instantiateItem(viewPager, i);
                                fragment.onResume();
                                break;
                            case "Inventory Report":  InventoryReport fragment1 = (InventoryReport) adapter.instantiateItem(viewPager, i);
                                fragment1.onResume();
                                break;
                            case "Employee/Customer Report":  EmployeeCustomerReport fragment2 = (EmployeeCustomerReport) adapter.instantiateItem(viewPager, i);
                                fragment2.onResume();
                                break;
                            case "GST Report":  GSTReports fragment3 = (GSTReports) adapter.instantiateItem(viewPager, i);
                                fragment3.onResume();
                                break;
                            case "GST Link":  FragmentGSTLink fragment4 = (FragmentGSTLink) adapter.instantiateItem(viewPager, i);
                                fragment4.onResume();
                                break;
                        }

                    }

                }
                @Override
                public void onPageScrollStateChanged(final int i) {
                }
            };

    private void createTabTitles() {

        TextView salesReports = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        salesReports.setText(getString(R.string.sales_report));
//        salesReports.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_user_name, 0, 0);
        tabLayout.getTabAt(0).setCustomView(salesReports);

        TextView inventoryReports = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        inventoryReports.setText(getString(R.string.inventory_report));
//        inventoryReports.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_header_footer, 0, 0);
        tabLayout.getTabAt(1).setCustomView(inventoryReports);

        TextView employeeCustomerReports = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        employeeCustomerReports.setText(getString(R.string.employee_customer_report));
//        employeeCustomerReports.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_others, 0, 0);
        tabLayout.getTabAt(2).setCustomView(employeeCustomerReports);

        TextView gstReports = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        gstReports.setText(getString(R.string.gst_report));
//        gstReports.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_gst, 0, 0);
        tabLayout.getTabAt(3).setCustomView(gstReports);

        TextView gstLinks = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        gstLinks.setText( getString(R.string.gst_link));
//        gstLinks.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_config_payment_receipt, 0, 0);
        tabLayout.getTabAt(4).setCustomView(gstLinks);

    }

    class loadReportsAsyncTask extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(myContext);
            pd.setTitle(getString(R.string.loading_settings));
            pd.setMessage(getString(R.string.please_wait));
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }



        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            createTabTitles();
            viewPager.addOnPageChangeListener (myOnPageChangeListener);
            pd.dismiss();
        }
    }
}
