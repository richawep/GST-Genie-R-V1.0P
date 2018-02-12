package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.DuplicateBillBean;

/**
 * Created by SachinV on 24-01-2018.
 */

public class DuplicateReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_duplicateBill_row_date)           TextView tv_duplicateBill_row_date;
    @BindView(R.id.tv_duplicateBill_row_bill_no)        TextView tv_duplicateBill_row_bill_no;
    @BindView(R.id.tv_duplicateBill_row_item)           TextView tv_duplicateBill_row_item;
    @BindView(R.id.tv_duplicateBill_row_discount)       TextView tv_duplicateBill_row_discount;
    @BindView(R.id.tv_duplicateBill_row_igst_amt)       TextView tv_duplicateBill_row_igst_amt;
    @BindView(R.id.tv_duplicateBill_row_cgst_amt)       TextView tv_duplicateBill_row_cgst_amt;
    @BindView(R.id.tv_duplicateBill_row_utgst_sgst_amt) TextView tv_duplicateBill_row_utgst_sgst_amt;
    @BindView(R.id.tv_duplicateBill_row_cess_amt)       TextView tv_duplicateBill_row_cess_amt;
    @BindView(R.id.tv_duplicateBill_row_bill_amt)       TextView tv_duplicateBill_row_bill_amt;
    @BindView(R.id.tv_duplicateBill_row_reprint_count)       TextView tv_duplicateBill_row_reprint_count;

    public DuplicateReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(DuplicateBillBean duplicateBillBean, int position) {

        if (duplicateBillBean != null) {
            tv_duplicateBill_row_date.setText(duplicateBillBean.getInvoiceDate());
            tv_duplicateBill_row_bill_no.setText(String.valueOf(duplicateBillBean.getInvoiceNumber()));
            tv_duplicateBill_row_item.setText(String.valueOf(duplicateBillBean.getItems()));
            tv_duplicateBill_row_discount.setText(String.format("%.2f",duplicateBillBean.getDiscount()));
            tv_duplicateBill_row_igst_amt.setText(String.format("%.2f",duplicateBillBean.getIgstAmount()));
            tv_duplicateBill_row_cgst_amt.setText(String.format("%.2f",duplicateBillBean.getCgstAmount()));
            tv_duplicateBill_row_utgst_sgst_amt.setText(String.format("%.2f",duplicateBillBean.getSgstAmount()));
            tv_duplicateBill_row_cess_amt.setText(String.format("%.2f",duplicateBillBean.getCessAmount()));
            tv_duplicateBill_row_bill_amt.setText(String.format("%.2f",duplicateBillBean.getBillAmount()));
            tv_duplicateBill_row_reprint_count.setText(String.valueOf(duplicateBillBean.getReprintCount()));
        }
    }
}
