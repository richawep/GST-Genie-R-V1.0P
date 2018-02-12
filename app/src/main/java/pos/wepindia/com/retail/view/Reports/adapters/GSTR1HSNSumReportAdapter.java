package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1HSNSummaryBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.GSTR1HSNSumReportViewHolder;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1HSNSumReportAdapter extends RecyclerView.Adapter<GSTR1HSNSumReportViewHolder> {


    private ArrayList<GSTR1HSNSummaryBean> reportList;
    private Context mContext;

    public GSTR1HSNSumReportAdapter(Context context, ArrayList<GSTR1HSNSummaryBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public GSTR1HSNSumReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_gstr1_hsn_sum_report, parent,false);
        return new GSTR1HSNSumReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GSTR1HSNSumReportViewHolder holder, int position) {
        GSTR1HSNSummaryBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
