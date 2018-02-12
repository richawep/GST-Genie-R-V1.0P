package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.CustomerDetailReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.CustomerDetailReportViewHolder;

/**
 * Created by SachinV on 25-01-2018.
 */

public class CustomerDetailReportAdapter extends RecyclerView.Adapter<CustomerDetailReportViewHolder> {

    private static final String TAG = BillWiseReportAdapter.class.getSimpleName();
    private ArrayList<CustomerDetailReportBean> reportList;
    private Context mContext;

    public CustomerDetailReportAdapter(Context context, ArrayList<CustomerDetailReportBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public CustomerDetailReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_customer_detail_report, parent,false);
        return new CustomerDetailReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomerDetailReportViewHolder holder, int position) {
        CustomerDetailReportBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
