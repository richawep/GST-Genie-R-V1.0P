package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.SalesmanDetailReportBean;

/**
 * Created by SachinV on 01-02-2018.
 */

public class SalesmanDetailReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_salesmandetail_row_id)            TextView tv_salesmandetail_row_id;
    @BindView(R.id.tv_salesmandetail_row_name)          TextView tv_salesmandetail_row_name;
    @BindView(R.id.tv_salesmandetail_row_invoice_no)    TextView tv_salesmandetail_row_invoice_no;
    @BindView(R.id.tv_salesmandetail_row_invoice_date)  TextView tv_salesmandetail_row_invoice_date;
    @BindView(R.id.tv_salesmandetail_row_taxable_value) TextView tv_salesmandetail_row_taxable_value;
    @BindView(R.id.tv_salesmandetail_row_bill_amount)   TextView tv_salesmandetail_row_bill_amount;

    public SalesmanDetailReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(SalesmanDetailReportBean salesmanDetailReportBean, int position) {

        if (salesmanDetailReportBean != null) {
            tv_salesmandetail_row_id.setText(salesmanDetailReportBean.getSalesManId());
            tv_salesmandetail_row_name.setText(salesmanDetailReportBean.getSalesmanName());
            tv_salesmandetail_row_invoice_no.setText(String.valueOf(salesmanDetailReportBean.getInvoiceNo()));
            tv_salesmandetail_row_invoice_date.setText(salesmanDetailReportBean.getInvoiceDate());
            tv_salesmandetail_row_taxable_value.setText(String.format("%.2f",salesmanDetailReportBean.getTaxableValue()));
            tv_salesmandetail_row_bill_amount.setText(String.format("%.2f",salesmanDetailReportBean.getBillAmount()));
        }
    }

}
