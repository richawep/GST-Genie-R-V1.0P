package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.SalesmanWiseReportBean;

/**
 * Created by SachinV on 01-02-2018.
 */

public class SalesmanWiseReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_salesmanwise_row_id)              TextView tv_salesmanwise_row_id;
    @BindView(R.id.tv_salesmanwise_row_name)            TextView tv_salesmanwise_row_name;
    @BindView(R.id.tv_salesmanwise_row_total_bills)     TextView tv_salesmanwise_row_total_bills;
    @BindView(R.id.tv_salesmanwise_row_month)           TextView tv_salesmanwise_row_month;
    @BindView(R.id.tv_salesmanwise_row_taxable_value)   TextView tv_salesmanwise_row_taxable_value;
    @BindView(R.id.tv_salesmanwise_row_bill_amount)     TextView tv_salesmanwise_row_bill_amount;

    public SalesmanWiseReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(SalesmanWiseReportBean salesmanWiseReportBean, int position) {
        String []months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        if (salesmanWiseReportBean != null) {
            tv_salesmanwise_row_id.setText(salesmanWiseReportBean.getSalesManId());
            tv_salesmanwise_row_name.setText(String.valueOf(salesmanWiseReportBean.getSalesManName()));
            tv_salesmanwise_row_total_bills.setText(String.valueOf(salesmanWiseReportBean.getTotalBills()));
            tv_salesmanwise_row_month.setText(months[salesmanWiseReportBean.getMonth()]);
            tv_salesmanwise_row_taxable_value.setText(String.format("%.2f",salesmanWiseReportBean.getTaxableValue()));
            tv_salesmanwise_row_bill_amount.setText(String.format("%.2f",salesmanWiseReportBean.getBillAmount()));
        }
    }

}
