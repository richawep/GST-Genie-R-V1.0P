package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR2RegisteredBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.GSTR2RegisteredReportViewHolder;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR2RegisteredReportAdapter extends RecyclerView.Adapter<GSTR2RegisteredReportViewHolder> {

    private static final String TAG = BillWiseReportAdapter.class.getSimpleName();
    private ArrayList<GSTR2RegisteredBean> reportList;
    private Context mContext;

    public GSTR2RegisteredReportAdapter(Context context, ArrayList<GSTR2RegisteredBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public GSTR2RegisteredReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_gstr2_registered_report, parent,false);
        return new GSTR2RegisteredReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GSTR2RegisteredReportViewHolder holder, int position) {
        GSTR2RegisteredBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
