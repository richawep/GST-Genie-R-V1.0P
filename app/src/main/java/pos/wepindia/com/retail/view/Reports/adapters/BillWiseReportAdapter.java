package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.BillWiseBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.BillWiseReportViewHolder;

/**
 * Created by SachinV on 23-01-2018.
 */

public class BillWiseReportAdapter extends RecyclerView.Adapter<BillWiseReportViewHolder> {

    private static final String TAG = BillWiseReportAdapter.class.getSimpleName();
    private ArrayList<BillWiseBean> reportList;
    private Context mContext;

    public BillWiseReportAdapter(Context context, ArrayList<BillWiseBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public BillWiseReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_bill_wise_report, parent,false);
        return new BillWiseReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BillWiseReportViewHolder holder, int position) {
        BillWiseBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
