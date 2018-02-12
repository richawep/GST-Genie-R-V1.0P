package pos.wepindia.com.retail.view.settings;

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
import pos.wepindia.com.retail.view.settings.adapters.SettingsAdapter;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;

/**
 * Created by MohanN on 12/5/2017.
 */

public class AppSettingsFragment extends Fragment {

    @BindView(R.id.app_settings_tab)     TabLayout tabLayout;
    @BindView(R.id.vp_app_settings)      ViewPager viewPager;
    @BindView(R.id.app_settings_toolbar)     Toolbar toolbar;
    View view1;
    SettingsAdapter adapter;
    MessageDialog msgBox;
    Context myContext ;

    private static final String TAG = AppSettingsFragment.class.getName();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view1 = inflater.inflate(R.layout.settings_fragment, container, false);
        ButterKnife.bind(this, view1);

        return view1;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getActivity().getString(R.string.settings));
        try{
            if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }
            //dbHelper = DatabaseHelper.getInstance(getActivity());
            myContext = getActivity();
            msgBox = new MessageDialog(getActivity());
            new loadSettingsAsyncTask().execute();

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
        adapter = new SettingsAdapter(getChildFragmentManager());
        adapter.addFragment(new OwnerDetailsSettingsFragment(), getString(R.string.owner_details));
        adapter.addFragment(new HeaderFooterSettingsFragment(), getString(R.string.header_and_footer));
        adapter.addFragment(new OthersSettingsFragment(), getString(R.string.others));
        adapter.addFragment(new GSTSettingsFragment(), getString(R.string.gst));
        adapter.addFragment(new PaymentModeConfigurationFragment(), getString(R.string.payment_mode_configuration));
        adapter.addFragment(new MachineSettingsFragment(), getString(R.string.machine));

        viewPager.setAdapter(adapter);
    }

    private void createTabIcons() {


        TextView tabOwnerDetails = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOwnerDetails.setText("Owner Details");
        tabOwnerDetails.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_user_name, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOwnerDetails);

        TextView tabHeaderFooter = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabHeaderFooter.setText("Header & Footer");
        tabHeaderFooter.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_header_footer, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabHeaderFooter);

        TextView tabOthers = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOthers.setText("Others");
        tabOthers.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_others, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabOthers);

        TextView tabGST = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabGST.setText("GST");
        tabGST.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_gst, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabGST);

        TextView tabPaymentModeConfiguration = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabPaymentModeConfiguration.setText( getString(R.string.payment_mode_configuration));
        tabPaymentModeConfiguration.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_config_payment_receipt, 0, 0);
        tabLayout.getTabAt(4).setCustomView(tabPaymentModeConfiguration);

        TextView tabMachine = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabMachine.setText("Machine");
        tabMachine.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_machine, 0, 0);
        tabLayout.getTabAt(5).setCustomView(tabMachine);


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

                            case "Header & Footer":  HeaderFooterSettingsFragment fragment = (HeaderFooterSettingsFragment) adapter.instantiateItem(viewPager, i);
                                fragment.onResume();
                                break;
                            case "Others":  OthersSettingsFragment fragment1 = (OthersSettingsFragment) adapter.instantiateItem(viewPager, i);
                                fragment1.onResume();
                                break;
                            case "GST":  GSTSettingsFragment fragment2 = (GSTSettingsFragment) adapter.instantiateItem(viewPager, i);
                                fragment2.onResume();
                                break;
                        }

                    }

                }
                @Override
                public void onPageScrollStateChanged(final int i) {
                }
            };

    class loadSettingsAsyncTask extends AsyncTask<Void, Void, Void>
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
            createTabIcons();
            viewPager.addOnPageChangeListener (myOnPageChangeListener);
            pd.dismiss();
        }
    }
}
