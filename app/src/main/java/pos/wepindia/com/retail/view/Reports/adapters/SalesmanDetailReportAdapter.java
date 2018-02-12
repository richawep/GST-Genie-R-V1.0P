package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.SalesmanDetailReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.SalesmanDetailReportViewHolder;

/**
 * Created by SachinV on 01-02-2018.
 */

public class SalesmanDetailReportAdapter extends RecyclerView.Adapter<SalesmanDetailReportViewHolder> {

    private static final String TAG = BillWiseReportAdapter.class.getSimpleName();
    private ArrayList<SalesmanDetailReportBean> reportList;
    private Context mContext;

    public SalesmanDetailReportAdapter(Context context, ArrayList<SalesmanDetailReportBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public SalesmanDetailReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_salesman_detail_report, parent,false);
        return new SalesmanDetailReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SalesmanDetailReportViewHolder holder, int position) {
        SalesmanDetailReportBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
