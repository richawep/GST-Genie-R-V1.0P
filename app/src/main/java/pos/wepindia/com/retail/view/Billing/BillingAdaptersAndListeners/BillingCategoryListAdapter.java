package pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.wepbase.model.pojos.Category;

/**
 * Created by MohanN on 1/16/2018.
 */

public class BillingCategoryListAdapter extends RecyclerView.Adapter<BillingCategoryListViewHolder> {

    private static final String TAG = BillingCategoryListAdapter.class.getName();
    private List<Category> categoryList;
    private Activity mActivity;
    private OnBillingCategoryListAdapterListener onBillingCategoryListAdapterListener;

    public BillingCategoryListAdapter(Activity activity, OnBillingCategoryListAdapterListener onBillingCategoryListAdapterListener, List<Category> list) {
        this.mActivity = activity;
        this.onBillingCategoryListAdapterListener = onBillingCategoryListAdapterListener;
        this.categoryList = list;
    }


    @Override
    public BillingCategoryListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.billing_fav_display_adapter_list,viewGroup,false);
        return new BillingCategoryListViewHolder(mActivity,onBillingCategoryListAdapterListener,view);
    }

    @Override
    public void onBindViewHolder(BillingCategoryListViewHolder holder, int position) {
        Category categoryTemp = categoryList.get(position);
        holder.bind(categoryTemp);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
