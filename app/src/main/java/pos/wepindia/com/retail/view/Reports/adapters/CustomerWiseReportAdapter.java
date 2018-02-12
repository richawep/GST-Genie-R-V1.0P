package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.CustomerWiseReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.CustomerWiseReportViewHolder;

/**
 * Created by SachinV on 25-01-2018.
 */

public class CustomerWiseReportAdapter extends RecyclerView.Adapter<CustomerWiseReportViewHolder> {

    private static final String TAG = BillWiseReportAdapter.class.getSimpleName();
    private ArrayList<CustomerWiseReportBean> reportList;
    private Context mContext;

    public CustomerWiseReportAdapter(Context context, ArrayList<CustomerWiseReportBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public CustomerWiseReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer_wise_report, parent,false);
        return new CustomerWiseReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerWiseReportViewHolder holder, int position) {
        CustomerWiseReportBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
