package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.DayWiseBean;

/**
 * Created by SachinV on 24-01-2018.
 */

public class DayWiseReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_daywise_date)             TextView tv_daywise_date;
    @BindView(R.id.tv_daywise_total_bill)       TextView tv_daywise_total_bill;
    @BindView(R.id.tv_daywise_discount)         TextView tv_daywise_discount;
    @BindView(R.id.tv_daywise_igst_amt)         TextView tv_daywise_igst_amt;
    @BindView(R.id.tv_daywise_cgst_amt)         TextView tv_daywise_cgst_amt;
    @BindView(R.id.tv_daywise_utgst_sgst_amt)   TextView tv_daywise_utgst_sgst_amt;
    @BindView(R.id.tv_daywise_cess_amt)         TextView tv_daywise_cess_amt;
    @BindView(R.id.tv_daywise_bill_amt)         TextView tv_daywise_bill_amt;

    public DayWiseReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(DayWiseBean dayWiseBean, int position) {

        if (dayWiseBean != null) {
            tv_daywise_date.setText(dayWiseBean.getInvoiceDate());
            tv_daywise_total_bill.setText(String.valueOf(dayWiseBean.getTotalBills()));
            tv_daywise_discount.setText(String.format("%.2f",dayWiseBean.getDiscount()));
            tv_daywise_igst_amt.setText(String.format("%.2f",dayWiseBean.getIGSTAmount()));
            tv_daywise_cgst_amt.setText(String.format("%.2f",dayWiseBean.getCGSTAmount()));
            tv_daywise_utgst_sgst_amt.setText(String.format("%.2f",dayWiseBean.getSGSTAmount()));
            tv_daywise_cess_amt.setText(String.format("%.2f",dayWiseBean.getCESSAmount()));
            tv_daywise_bill_amt.setText(String.format("%.2f",dayWiseBean.getBillAmount()));
        }
    }
}
