package pos.wepindia.com.retail.view.Configuration;

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
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Configuration.brand.BrandFragment;
import pos.wepindia.com.retail.view.Configuration.category.CategoryFragment;
import pos.wepindia.com.retail.view.Configuration.coupon.CouponFragment;
import pos.wepindia.com.retail.view.Configuration.department.DepartmentFragment;
import pos.wepindia.com.retail.view.Configuration.discount.DiscountFragment;
import pos.wepindia.com.retail.view.Configuration.loyalty.RewardPointsFragment;
import pos.wepindia.com.retail.view.Configuration.other_charges.OtherChargesFragment;
import pos.wepindia.com.retail.view.settings.adapters.SettingsAdapter;

/**
 * Created by MohanN on 12/6/2017.
 */

public class ConfigurationFragment extends Fragment {

    @BindView(R.id.app_configuration_tab)     TabLayout tabLayout;
    @BindView(R.id.app_configuration_vp)      ViewPager viewPager;
    @BindView(R.id.app_configuration_toolbar)    Toolbar toolbar;
    View view;
    SettingsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.configuration_fragment, container, false);
        ButterKnife.bind(this, view);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        createTabIcons();

        // Attach the page change listener inside the activity
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                if(adapter!=null){
                    switch(position)
                    {
                        case 1:  CategoryFragment fragment = (CategoryFragment) adapter.instantiateItem(viewPager, position);
                            fragment.onResume();
                            break;
                    }
                }
            }
            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }
            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getActivity().getString(R.string.configuration));
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new SettingsAdapter(getChildFragmentManager());
        adapter.addFragment(new DepartmentFragment(), getString(R.string.department));
        adapter.addFragment(new CategoryFragment(), getString(R.string.category));
        adapter.addFragment(new BrandFragment(), getString(R.string.brand));
        //adapter.addFragment(new PaymentReceiptFragment(), getString(R.string.payment_receipt));
        adapter.addFragment(new DiscountFragment(), getString(R.string.discount));
        adapter.addFragment(new CouponFragment(), getString(R.string.coupon));
        adapter.addFragment(new OtherChargesFragment(), getString(R.string.other_charges));
        adapter.addFragment(new RewardPointsFragment(), getString(R.string.reward_points));
        viewPager.setAdapter(adapter);
        viewPager. setOffscreenPageLimit(1);
    }

    private void createTabIcons() {

        int i =0;
        TextView tabDepartment = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabDepartment.setText("Department");
        tabDepartment.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_config_department, 0, 0);
        tabLayout.getTabAt(i++).setCustomView(tabDepartment);


        TextView tabCategory = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabCategory.setText("Category");
        tabCategory.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_config_category, 0, 0);
        tabLayout.getTabAt(i++).setCustomView(tabCategory);

        TextView tabBrand = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabBrand.setText(getString(R.string.brand));
        tabBrand.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_brand, 0, 0);
        tabLayout.getTabAt(i++).setCustomView(tabBrand);

        TextView tabPayReceipt = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabPayReceipt.setText("Payment/Receipt");
        tabPayReceipt.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_config_payment_receipt, 0, 0);
       // tabLayout.getTabAt(i++).setCustomView(tabPayReceipt);

        TextView tabDiscount = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabDiscount.setText("Discount");
        tabDiscount.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_config_discount, 0, 0);
        tabLayout.getTabAt(i++).setCustomView(tabDiscount);

        TextView tabCoupon = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabCoupon.setText("Coupon");
        tabCoupon.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_coupon, 0, 0);
        tabLayout.getTabAt(i++).setCustomView(tabCoupon);

        TextView tabOtherCharges = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOtherCharges.setText("Other Charges");
        tabOtherCharges.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_others, 0, 0);
        tabLayout.getTabAt(i++).setCustomView(tabOtherCharges);

        TextView tabLoyalty = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabLoyalty.setText(getString(R.string.reward_points));
        tabLoyalty.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_config_loyalty, 0, 0);
        tabLayout.getTabAt(i++).setCustomView(tabLoyalty);
    }
}