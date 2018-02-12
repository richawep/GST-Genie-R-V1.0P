package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.SalesmanWiseReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.SalesmanWiseReportViewHolder;

/**
 * Created by SachinV on 01-02-2018.
 */

public class SalesmanWiseReportAdapter extends RecyclerView.Adapter<SalesmanWiseReportViewHolder> {

    private static final String TAG = BillWiseReportAdapter.class.getSimpleName();
    private ArrayList<SalesmanWiseReportBean> reportList;
    private Context mContext;

    public SalesmanWiseReportAdapter(Context context, ArrayList<SalesmanWiseReportBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public SalesmanWiseReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_salesman_wise_report, parent,false);
        return new SalesmanWiseReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SalesmanWiseReportViewHolder holder, int position) {
        SalesmanWiseReportBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
