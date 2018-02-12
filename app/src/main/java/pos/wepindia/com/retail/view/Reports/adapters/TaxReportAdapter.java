package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.TaxReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.TaxReportViewHolder;

/**
 * Created by SachinV on 24-01-2018.
 */

public class TaxReportAdapter extends RecyclerView.Adapter<TaxReportViewHolder> {

    private static final String TAG = BillWiseReportAdapter.class.getSimpleName();
    private ArrayList<TaxReportBean> reportList;
    private Context mContext;

    public TaxReportAdapter(Context context, ArrayList<TaxReportBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public TaxReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tax_report, parent,false);
        return new TaxReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaxReportViewHolder holder, int position) {
        TaxReportBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
