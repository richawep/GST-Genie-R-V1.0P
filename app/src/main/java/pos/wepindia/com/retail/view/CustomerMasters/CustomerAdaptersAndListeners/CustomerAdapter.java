package pos.wepindia.com.retail.view.CustomerMasters.CustomerAdaptersAndListeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.wepbase.model.pojos.Customer;

/**
 * Created by MohanN on 1/2/2018.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerViewHolder> {

    private static final String TAG = CustomerAdapter.class.getName();
    private List<Customer> customerList;
    private Context mContext;
    private OnCustomerSelectListener onCustomerSelectListener;

    public CustomerAdapter(Context context, OnCustomerSelectListener onCustomerSelectListener, List<Customer> list) {
        this.mContext = context;
        this.onCustomerSelectListener = onCustomerSelectListener;
        this.customerList = list;
    }


    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_cust_display,viewGroup,false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerViewHolder holder, final int position) {
        Customer myObject = customerList.get(position);
        holder.bind(myObject,position);

        holder.ll_row_cust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCustomerSelectListener.onRowDataSelect(customerList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }
}
