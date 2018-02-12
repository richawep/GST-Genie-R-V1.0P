package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.UserDetailReportBean;

/**
 * Created by SachinV on 25-01-2018.
 */

public class UserDetailReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_supplierdetail_row_date)          TextView tv_supplierdetail_row_date;
    @BindView(R.id.tv_supplierdetail_row_bill_number)   TextView tv_supplierdetail_row_bill_number;
    @BindView(R.id.tv_supplierdetail_row_Items_items)   TextView tv_supplierdetail_row_Items_items;
    @BindView(R.id.tv_supplierdetail_row_discount)      TextView tv_supplierdetail_row_discount;
    @BindView(R.id.tv_supplierdetail_row_tax)           TextView tv_supplierdetail_row_tax;
    @BindView(R.id.tv_supplierdetail_row_bill_amount)   TextView tv_supplierdetail_row_bill_amount;

    public UserDetailReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(UserDetailReportBean userDetailReportBean, int position) {

        if (userDetailReportBean != null) {
            tv_supplierdetail_row_date.setText(userDetailReportBean.getDate());
            tv_supplierdetail_row_bill_number.setText(String.valueOf(userDetailReportBean.getBillNumber()));
            tv_supplierdetail_row_Items_items.setText(String.valueOf(userDetailReportBean.getTotalItems()));
            tv_supplierdetail_row_discount.setText(String.format("%.2f",userDetailReportBean.getDiscount()));
            tv_supplierdetail_row_tax.setText(String.format("%.2f",userDetailReportBean.getTax()));
            tv_supplierdetail_row_bill_amount.setText(String.format("%.2f",userDetailReportBean.getBillAmount()));
        }
    }

}
