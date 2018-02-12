package pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.wepbase.model.pojos.BillItemBean;

/**
 * Created by MohanN on 12/15/2017.
 */

public class BillingListAdapter extends RecyclerView.Adapter<BillingItemsListViewHolder> {

    private static final String TAG = BillingListAdapter.class.getName();
    private List<BillItemBean> billingItemsLists;
    private OnBillingSelectedProductListsListeners onBillingSelectedProductListsListeners;
    private int isHsnVisibleEnabled;

    public BillingListAdapter(OnBillingSelectedProductListsListeners onBillingSelectedProductListsListeners,
                              List<BillItemBean> list, int isHsnVisibleEnabled) {
        this.onBillingSelectedProductListsListeners = onBillingSelectedProductListsListeners;
        this.billingItemsLists = list;
        this.isHsnVisibleEnabled = isHsnVisibleEnabled;
    }


    @Override
    public BillingItemsListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_card_billing_items_list,viewGroup,false);
        return new BillingItemsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BillingItemsListViewHolder holder, final int position) {
        BillItemBean myObject = billingItemsLists.get(position);
        holder.bind(myObject);

        holder.llBillingItemsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBillingSelectedProductListsListeners.onBillingListItemSelected(position);
            }
        });

        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBillingSelectedProductListsListeners.onBillingListItemRemove(position);
            }
        });

        if(isHsnVisibleEnabled != 1)
            holder.tvHSN.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return billingItemsLists.size();
    }

    public void notifyData(List<BillItemBean> list){
        this.billingItemsLists = list;
        this.notifyDataSetChanged();
    }
}
