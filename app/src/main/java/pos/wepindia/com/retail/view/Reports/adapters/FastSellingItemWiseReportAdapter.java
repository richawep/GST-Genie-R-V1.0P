package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.FastSellingItemwiseReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.FastSellingItemWiseReportViewHolder;

/**
 * Created by SachinV on 25-01-2018.
 */

public class FastSellingItemWiseReportAdapter extends RecyclerView.Adapter<FastSellingItemWiseReportViewHolder> {

    private ArrayList<FastSellingItemwiseReportBean> reportList;
    private Context mContext;

    public FastSellingItemWiseReportAdapter(Context context, ArrayList<FastSellingItemwiseReportBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public FastSellingItemWiseReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_fast_selling_item_wise_report, parent,false);
        return new FastSellingItemWiseReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FastSellingItemWiseReportViewHolder holder, int position) {
        FastSellingItemwiseReportBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
