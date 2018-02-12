package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.TransactionReportBean;
import pos.wepindia.com.retail.view.Reports.Bean.VoidBillReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.TransactionReportViewHolder;
import pos.wepindia.com.retail.view.Reports.adapters.holder.VoidBillReportViewHolder;

/**
 * Created by SachinV on 24-01-2018.
 */

public class VoidBillReportAdapter extends RecyclerView.Adapter<VoidBillReportViewHolder> {

    private static final String TAG = VoidBillReportAdapter.class.getSimpleName();
    private ArrayList<VoidBillReportBean> reportList;
    private Context mContext;

    public VoidBillReportAdapter(Context context, ArrayList<VoidBillReportBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public VoidBillReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_void_bill_report, parent,false);
        return new VoidBillReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VoidBillReportViewHolder holder, int position) {
        VoidBillReportBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
