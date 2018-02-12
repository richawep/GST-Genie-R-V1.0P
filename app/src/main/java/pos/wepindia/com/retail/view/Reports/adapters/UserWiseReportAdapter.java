package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.UserWiseReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.UserWiseReportViewHolder;

/**
 * Created by SachinV on 25-01-2018.
 */

public class UserWiseReportAdapter extends RecyclerView.Adapter<UserWiseReportViewHolder> {

    private static final String TAG = BillWiseReportAdapter.class.getSimpleName();
    private ArrayList<UserWiseReportBean> reportList;
    private Context mContext;

    public UserWiseReportAdapter(Context context, ArrayList<UserWiseReportBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }

    @Override
    public UserWiseReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_wise_report, parent,false);
        return new UserWiseReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserWiseReportViewHolder holder, int position) {
        UserWiseReportBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
