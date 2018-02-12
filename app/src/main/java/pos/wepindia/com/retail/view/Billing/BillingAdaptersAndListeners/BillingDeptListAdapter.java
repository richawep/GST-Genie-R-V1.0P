package pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.wepbase.model.pojos.Department;

/**
 * Created by MohanN on 1/16/2018.
 */

public class BillingDeptListAdapter extends RecyclerView.Adapter<BillingDeptListViewHolder> {

    private static final String TAG = BillingDeptListAdapter.class.getName();
    private List<Department> departmentList;
    private Activity mActivity;
    private OnBillingDeptListAdapter onBillingDeptListAdapter;

    public BillingDeptListAdapter(Activity activity, OnBillingDeptListAdapter onBillingDeptListAdapter, List<Department> list) {
        this.mActivity = activity;
        this.onBillingDeptListAdapter = onBillingDeptListAdapter;
        this.departmentList = list;
    }


    @Override
    public BillingDeptListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.billing_fav_display_adapter_list,viewGroup,false);
        return new BillingDeptListViewHolder(mActivity,onBillingDeptListAdapter,view);
    }

    @Override
    public void onBindViewHolder(BillingDeptListViewHolder holder, int position) {
        Department departmentTemp = departmentList.get(position);
        holder.bind(departmentTemp);
    }

    @Override
    public int getItemCount() {
        return departmentList.size();
    }
}
