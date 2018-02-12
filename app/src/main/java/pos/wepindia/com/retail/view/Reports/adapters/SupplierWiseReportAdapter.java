package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.SupplierWiseReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.SupplierWiseReportViewHolder;

/**
 * Created by SachinV on 25-01-2018.
 */

public class SupplierWiseReportAdapter extends RecyclerView.Adapter<SupplierWiseReportViewHolder> {

    private static final String TAG = BillWiseReportAdapter.class.getSimpleName();
    private ArrayList<SupplierWiseReportBean> reportList;
    private Context mContext;

    public SupplierWiseReportAdapter(Context context, ArrayList<SupplierWiseReportBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public SupplierWiseReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_supplier_wise_report, parent,false);
        return new SupplierWiseReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SupplierWiseReportViewHolder holder, int position) {
        SupplierWiseReportBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
