package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1B2CSBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.GSTR1B2CSReportViewHolder;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1B2CSReportAdapter extends RecyclerView.Adapter<GSTR1B2CSReportViewHolder> {


    private ArrayList<GSTR1B2CSBean> reportList;
    private Context mContext;

    public GSTR1B2CSReportAdapter(Context context, ArrayList<GSTR1B2CSBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public GSTR1B2CSReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_gstr1_b2cs_report, parent,false);
        return new GSTR1B2CSReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GSTR1B2CSReportViewHolder holder, int position) {
        GSTR1B2CSBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
