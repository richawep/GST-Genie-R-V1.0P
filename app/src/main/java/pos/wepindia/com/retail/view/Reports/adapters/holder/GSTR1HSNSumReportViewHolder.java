package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1HSNSummaryBean;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1HSNSumReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_gstr1_hsn_sum_row_sno)                TextView tv_gstr1_hsn_sum_row_sno;
    @BindView(R.id.tv_gstr1_hsn_sum_row_hsn_code)           TextView tv_gstr1_hsn_sum_row_hsn_code;
    @BindView(R.id.tv_gstr1_hsn_sum_row_item_name)          TextView tv_gstr1_hsn_sum_row_item_name;
    @BindView(R.id.tv_gstr1_hsn_sum_row_uom)                TextView tv_gstr1_hsn_sum_row_uom;
    @BindView(R.id.tv_gstr1_hsn_sum_row_qty)                TextView tv_gstr1_hsn_sum_row_qty;
    @BindView(R.id.tv_gstr1_hsn_sum_row_value)              TextView tv_gstr1_hsn_sum_row_value;
    @BindView(R.id.tv_gstr1_hsn_sum_row_gst_rate)           TextView tv_gstr1_hsn_sum_row_gst_rate;
    @BindView(R.id.tv_gstr1_hsn_sum_row_taxable_value)      TextView tv_gstr1_hsn_sum_row_taxable_value;
    @BindView(R.id.tv_gstr1_hsn_sum_row_igst)               TextView tv_gstr1_hsn_sum_row_igst;
    @BindView(R.id.tv_gstr1_hsn_sum_row_cgst)               TextView tv_gstr1_hsn_sum_row_cgst;
    @BindView(R.id.tv_gstr1_hsn_sum_row_sgst)               TextView tv_gstr1_hsn_sum_row_sgst;
    @BindView(R.id.tv_gstr1_hsn_sum_row_cess)               TextView tv_gstr1_hsn_sum_row_cess;

    public GSTR1HSNSumReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(GSTR1HSNSummaryBean gstr1HSNSummaryBean, int position) {

        if (gstr1HSNSummaryBean != null) {
            tv_gstr1_hsn_sum_row_sno.setText(String.valueOf(position+1));
            tv_gstr1_hsn_sum_row_hsn_code.setText(gstr1HSNSummaryBean.getHsnCode());
            tv_gstr1_hsn_sum_row_item_name.setText(gstr1HSNSummaryBean.getItemName());
            tv_gstr1_hsn_sum_row_uom.setText(gstr1HSNSummaryBean.getUom());
            tv_gstr1_hsn_sum_row_qty.setText(String.format("%.2f",gstr1HSNSummaryBean.getQuantity()));
            tv_gstr1_hsn_sum_row_value.setText(String.format("%.2f",gstr1HSNSummaryBean.getValue()));
            if(gstr1HSNSummaryBean.getIgstRate() >0)
            {
                tv_gstr1_hsn_sum_row_gst_rate.setText(String.format("%.2f",gstr1HSNSummaryBean.getIgstRate() ));
            }else
            {
                tv_gstr1_hsn_sum_row_gst_rate.setText(String.format("%.2f", gstr1HSNSummaryBean.getCgstRate() + gstr1HSNSummaryBean.getSgstRate()));
            }

            tv_gstr1_hsn_sum_row_taxable_value.setText(String.format("%.2f",gstr1HSNSummaryBean.getTaxableVlue()));
            tv_gstr1_hsn_sum_row_igst.setText(String.format("%.2f",gstr1HSNSummaryBean.getIgstAmt()));
            tv_gstr1_hsn_sum_row_cgst.setText(String.format("%.2f",gstr1HSNSummaryBean.getCgstAmt()));
            tv_gstr1_hsn_sum_row_sgst.setText(String.format("%.2f",gstr1HSNSummaryBean.getSgstAmt()));
            tv_gstr1_hsn_sum_row_cess.setText(String.valueOf(String.format("%.2f",gstr1HSNSummaryBean.getCessAmt())));
        }
    }

}
