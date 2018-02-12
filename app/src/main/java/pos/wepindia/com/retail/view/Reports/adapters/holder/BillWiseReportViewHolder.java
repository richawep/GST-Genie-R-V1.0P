package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.BillWiseBean;

/**
 * Created by SachinV on 23-01-2018.
 */

public class BillWiseReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_bill_wise_date)           TextView tv_bill_wise_date;
    @BindView(R.id.tv_billwise_bill_number)     TextView tv_billwise_bill_number;
    @BindView(R.id.tv_billwise_Items)           TextView tv_billwise_Items;
    @BindView(R.id.tv_billwise_discount)        TextView tv_billwise_discount;
    @BindView(R.id.tv_billwise_tax)             TextView tv_billwise_tax;
    @BindView(R.id.tv_billwise_bill_amount)     TextView tv_billwise_bill_amount;

    public BillWiseReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(BillWiseBean billWiseBean, int position) {

        if (billWiseBean != null) {
            tv_bill_wise_date.setText(billWiseBean.getInvoiceDate());
            tv_billwise_bill_number.setText(String.valueOf(billWiseBean.getInvoiceNumber()));
            tv_billwise_Items.setText(String.valueOf(billWiseBean.getTotalItems()));
            tv_billwise_discount.setText(String.format("%.2f",billWiseBean.getTotalDiscountAmount()));
            tv_billwise_tax.setText(String.format("%.2f",billWiseBean.getTotalTax()));
            tv_billwise_bill_amount.setText(String.format("%.2f",billWiseBean.getBillAmount()));
        }
    }
}
