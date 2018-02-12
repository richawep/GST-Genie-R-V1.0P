package pos.wepindia.com.retail.view.Reports.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.UserDetailReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.holder.UserDetailReportViewHolder;

/**
 * Created by SachinV on 25-01-2018.
 */

public class UserDetailReportAdapter extends RecyclerView.Adapter<UserDetailReportViewHolder> {

    private static final String TAG = BillWiseReportAdapter.class.getSimpleName();
    private ArrayList<UserDetailReportBean> reportList;
    private Context mContext;

    public UserDetailReportAdapter(Context context, ArrayList<UserDetailReportBean> reportList) {
        this.mContext = context;
        this.reportList = reportList;
    }


    @Override
    public UserDetailReportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_detail_report, parent,false);
        return new UserDetailReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserDetailReportViewHolder holder, int position) {
        UserDetailReportBean myObject = reportList.get(position);
        holder.bind(myObject,position);
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
