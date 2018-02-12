package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.UserWiseReportBean;

/**
 * Created by SachinV on 25-01-2018.
 */

public class UserWiseReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_userwise_row_user_code)   TextView tv_userwise_row_user_code;
    @BindView(R.id.tv_userwise_row_name)            TextView tv_userwise_row_name;
    @BindView(R.id.tv_userwise_row_total_items)     TextView tv_userwise_row_total_items;
    @BindView(R.id.tv_userwise_row_bill_amount)     TextView tv_userwise_row_bill_amount;

    public UserWiseReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(UserWiseReportBean userWiseReportBean, int position) {

        if (userWiseReportBean != null) {
            tv_userwise_row_user_code.setText(String.valueOf(userWiseReportBean.getUserCode()));
            tv_userwise_row_name.setText(String.valueOf(userWiseReportBean.getName()));
            tv_userwise_row_total_items.setText(String.valueOf(userWiseReportBean.getTotalBills()));
            tv_userwise_row_bill_amount.setText(String.format("%.2f",userWiseReportBean.getBillAmount()));
        }
    }
}
