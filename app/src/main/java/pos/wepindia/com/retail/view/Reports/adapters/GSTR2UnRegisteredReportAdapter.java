package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR2UnRegisteredBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.GSTR2UnRegisteredReportViewHolder;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR2UnRegisteredReportAdapter extends RecyclerView.Adapter<GSTR2UnRegisteredReportViewHolder> {

    private static final String TAG = BillWiseReportAdapter.class.getSimpleName();
    private ArrayList<GSTR2UnRegisteredBean> reportList;
    private Context mContext;

    public GSTR2UnRegisteredReportAdapter(Context context, ArrayList<GSTR2UnRegisteredBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public GSTR2UnRegisteredReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_gstr2_unregistered_report, parent,false);
        return new GSTR2UnRegisteredReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GSTR2UnRegisteredReportViewHolder holder, int position) {
        GSTR2UnRegisteredBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
