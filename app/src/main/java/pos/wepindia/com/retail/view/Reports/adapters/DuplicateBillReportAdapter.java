package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.DuplicateBillBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.DuplicateReportViewHolder;

/**
 * Created by SachinV on 24-01-2018.
 */

public class DuplicateBillReportAdapter extends RecyclerView.Adapter<DuplicateReportViewHolder> {

    private static final String TAG = DayWiseReportAdapter.class.getSimpleName();
    private ArrayList<DuplicateBillBean> reportList;
    private Context mContext;

    public DuplicateBillReportAdapter(Context context, ArrayList<DuplicateBillBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public DuplicateReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_duplicate_bill_report, parent,false);
        return new DuplicateReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DuplicateReportViewHolder holder, int position) {
        DuplicateBillBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
