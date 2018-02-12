package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1DocIssuedBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.GSTR1DocIssuedReportViewHolder;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1DocIssuedReportAdapter extends RecyclerView.Adapter<GSTR1DocIssuedReportViewHolder> {

    private ArrayList<GSTR1DocIssuedBean> reportList;
    private Context mContext;

    public GSTR1DocIssuedReportAdapter(Context context, ArrayList<GSTR1DocIssuedBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public GSTR1DocIssuedReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_gstr1_doc_issued_report, parent,false);
        return new GSTR1DocIssuedReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GSTR1DocIssuedReportViewHolder holder, int position) {
        GSTR1DocIssuedBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
