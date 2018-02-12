package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.MonthWiseBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.MonthWiseReportViewHolder;

/**
 * Created by SachinV on 24-01-2018.
 */

public class MonthWiseReportAdapter extends RecyclerView.Adapter<MonthWiseReportViewHolder> {

    private static final String TAG = MonthWiseReportAdapter.class.getSimpleName();
    private ArrayList<MonthWiseBean> reportList;
    private Context mContext;

    public MonthWiseReportAdapter(Context context, ArrayList<MonthWiseBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public MonthWiseReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_month_wise_report, parent,false);
        return new MonthWiseReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MonthWiseReportViewHolder holder, int position) {
        MonthWiseBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
