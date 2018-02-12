package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.TaxReportBean;

/**
 * Created by SachinV on 24-01-2018.
 */

public class TaxReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_taxreport_row_sno)            TextView tv_taxreport_row_sno;
    @BindView(R.id.tv_taxreport_row_descrption)     TextView tv_taxreport_row_descrption;
    @BindView(R.id.tv_taxreport_row_tax_percent)    TextView tv_taxreport_row_tax_percent;
    @BindView(R.id.tv_taxreport_row_tax_amount)     TextView tv_taxreport_row_tax_amount;
    @BindView(R.id.tv_taxreport_row_taxable_amount) TextView tv_taxreport_row_taxable_amount;

    public TaxReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(TaxReportBean taxReportBean, int position) {

        if (taxReportBean != null) {
            tv_taxreport_row_sno.setText(String.valueOf(position+1));
            tv_taxreport_row_descrption.setText(String.valueOf(taxReportBean.getDescription()));
            tv_taxreport_row_tax_percent.setText(String.format("%.2f",taxReportBean.getTaxPercent()));
            tv_taxreport_row_tax_amount.setText(String.format("%.2f",taxReportBean.getTaxAmount()));
            tv_taxreport_row_taxable_amount.setText(String.format("%.2f",taxReportBean.getTaxableAmount()));
        }
    }

}
