package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.CategoryWiseReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.CategoryWiseReportViewHolder;

/**
 * Created by SachinV on 24-01-2018.
 */

public class CategoryWiseReportAdapter extends RecyclerView.Adapter<CategoryWiseReportViewHolder> {

    private static final String TAG = CategoryWiseReportAdapter.class.getSimpleName();
    private ArrayList<CategoryWiseReportBean> reportList;
    private Context mContext;

    public CategoryWiseReportAdapter(Context context, ArrayList<CategoryWiseReportBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public CategoryWiseReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category_wise_report, parent,false);
        return new CategoryWiseReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryWiseReportViewHolder holder, int position) {
        CategoryWiseReportBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
