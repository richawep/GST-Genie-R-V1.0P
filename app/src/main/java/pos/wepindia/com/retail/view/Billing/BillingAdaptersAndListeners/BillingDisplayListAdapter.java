package pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.wepbase.model.pojos.ItemMasterBean;

/**
 * Created by MohanN on 1/15/2018.
 */

public class BillingDisplayListAdapter extends RecyclerView.Adapter<BillingDisplayListViewHolder> {

    private static final String TAG = BillingListAdapter.class.getName();
    private List<ItemMasterBean> itemMasterBeans;
    private Activity mActivity;
    private OnBillingDisplayAdapterList onBillingDisplayAdapterList;

    public BillingDisplayListAdapter(Activity activity, OnBillingDisplayAdapterList onBillingDisplayAdapterList, List<ItemMasterBean> list) {
        this.mActivity = activity;
        this.onBillingDisplayAdapterList = onBillingDisplayAdapterList;
        this.itemMasterBeans = list;
    }


    @Override
    public BillingDisplayListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.billing_fav_display_adapter_list,viewGroup,false);
        return new BillingDisplayListViewHolder(mActivity,onBillingDisplayAdapterList,view);
    }

    @Override
    public void onBindViewHolder(BillingDisplayListViewHolder holder, int position) {
        ItemMasterBean itemMasterBean = itemMasterBeans.get(position);
        holder.bind(itemMasterBean);
    }

    public void mNotify(List<ItemMasterBean> list){
        this.itemMasterBeans = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemMasterBeans.size();
    }
}