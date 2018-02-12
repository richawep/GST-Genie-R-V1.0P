package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.DayWiseBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.DayWiseReportViewHolder;

/**
 * Created by SachinV on 24-01-2018.
 */

public class DayWiseReportAdapter extends RecyclerView.Adapter<DayWiseReportViewHolder> {

    private static final String TAG = DayWiseReportAdapter.class.getSimpleName();
    private ArrayList<DayWiseBean> reportList;
    private Context mContext;

    public DayWiseReportAdapter(Context context, ArrayList<DayWiseBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public DayWiseReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_day_wise_report, parent,false);
        return new DayWiseReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DayWiseReportViewHolder holder, int position) {
        DayWiseBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
