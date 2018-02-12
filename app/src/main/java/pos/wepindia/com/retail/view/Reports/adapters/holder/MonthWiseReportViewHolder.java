package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.MonthWiseBean;

/**
 * Created by SachinV on 24-01-2018.
 */

public class MonthWiseReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_monthwise_month)              TextView tv_monthwise_month;
    @BindView(R.id.tv_monthwise_total_bill)         TextView tv_monthwise_total_bill;
    @BindView(R.id.tv_monthwise_discount)           TextView tv_monthwise_discount;
    @BindView(R.id.tv_monthwise_igst_amt)           TextView tv_monthwise_igst_amt;
    @BindView(R.id.tv_monthwise_cgst_amt)           TextView tv_monthwise_cgst_amt;
    @BindView(R.id.tv_monthwise_utgst_sgst_amt)     TextView tv_monthwise_utgst_sgst_amt;
    @BindView(R.id.tv_monthwise_cess_amt)           TextView tv_monthwise_cess_amt;
    @BindView(R.id.tv_monthwise_bill_amt)           TextView tv_monthwise_bill_amt;

    public MonthWiseReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(MonthWiseBean monthWiseBean, int position) {
        String []months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        if (monthWiseBean != null) {
            tv_monthwise_month.setText(months[monthWiseBean.getMonth()]);
            tv_monthwise_total_bill.setText(String.valueOf(monthWiseBean.getTotalBills()));
            tv_monthwise_discount.setText(String.format("%.2f",monthWiseBean.getDiscount()));
            tv_monthwise_igst_amt.setText(String.format("%.2f",monthWiseBean.getIgstAmount()));
            tv_monthwise_cgst_amt.setText(String.format("%.2f",monthWiseBean.getCgstAmount()));
            tv_monthwise_utgst_sgst_amt.setText(String.format("%.2f",monthWiseBean.getSgstAmount()));
            tv_monthwise_cess_amt.setText(String.format("%.2f",monthWiseBean.getCessAmount()));
            tv_monthwise_bill_amt.setText(String.format("%.2f",monthWiseBean.getBillAmount()));
        }
    }
}
