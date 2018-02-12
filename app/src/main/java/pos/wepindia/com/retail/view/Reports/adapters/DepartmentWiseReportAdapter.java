package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.DepartmentWiseReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.DepartmentWiseReportViewHolder;

/**
 * Created by SachinV on 24-01-2018.
 */

public class DepartmentWiseReportAdapter extends RecyclerView.Adapter<DepartmentWiseReportViewHolder> {

    private static final String TAG = CategoryWiseReportAdapter.class.getSimpleName();
    private ArrayList<DepartmentWiseReportBean> reportList;
    private Context mContext;

    public DepartmentWiseReportAdapter(Context context, ArrayList<DepartmentWiseReportBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public DepartmentWiseReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_department_wise_report, parent,false);
        return new DepartmentWiseReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DepartmentWiseReportViewHolder holder, int position) {
        DepartmentWiseReportBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

}
