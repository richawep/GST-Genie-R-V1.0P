package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.ItemWiseReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.ItemWiseReportViewHolder;

/**
 * Created by SachinV on 25-01-2018.
 */

public class ItemWiseReportAdapter extends RecyclerView.Adapter<ItemWiseReportViewHolder> {

    private static final String TAG = BillWiseReportAdapter.class.getSimpleName();
    private ArrayList<ItemWiseReportBean> reportList;
    private Context mContext;

    public ItemWiseReportAdapter(Context context, ArrayList<ItemWiseReportBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public ItemWiseReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_wise_report, parent,false);
        return new ItemWiseReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemWiseReportViewHolder holder, int position) {
        ItemWiseReportBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
