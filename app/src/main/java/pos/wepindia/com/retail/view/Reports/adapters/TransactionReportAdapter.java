package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.TransactionReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.TransactionReportViewHolder;

/**
 * Created by SachinV on 24-01-2018.
 */

public class TransactionReportAdapter extends RecyclerView.Adapter<TransactionReportViewHolder> {

    private static final String TAG = TransactionReportAdapter.class.getSimpleName();
    private ArrayList<TransactionReportBean> reportList;
    private Context mContext;

    public TransactionReportAdapter(Context context, ArrayList<TransactionReportBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public TransactionReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transaction_report, parent,false);
        return new TransactionReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionReportViewHolder holder, int position) {
        TransactionReportBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
