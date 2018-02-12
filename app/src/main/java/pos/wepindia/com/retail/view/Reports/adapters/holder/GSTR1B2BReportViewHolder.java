package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1B2BBean;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1B2BReportViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_gstr1_b2b_row_sno)            TextView tv_gstr1_b2b_row_sno;
    @BindView(R.id.tv_gstr1_b2b_row_gstin)          TextView tv_gstr1_b2b_row_gstin;
    @BindView(R.id.tv_gstr1_b2b_row_no)             TextView tv_gstr1_b2b_row_no;
    @BindView(R.id.tv_gstr1_b2b_row_date)           TextView tv_gstr1_b2b_row_date;
    @BindView(R.id.tv_gstr1_b2b_row_value)          TextView tv_gstr1_b2b_row_value;
    @BindView(R.id.tv_gstr1_b2b_row_gstrate)        TextView tv_gstr1_b2b_row_gstrate;
    @BindView(R.id.tv_gstr1_b2b_row_taxable_value)  TextView tv_gstr1_b2b_row_taxable_value;
    @BindView(R.id.tv_gstr1_b2b_row_igst)           TextView tv_gstr1_b2b_row_igst;
    @BindView(R.id.tv_gstr1_b2b_row_cgst)           TextView tv_gstr1_b2b_row_cgst;
    @BindView(R.id.tv_gstr1_b2b_row_sgst)           TextView tv_gstr1_b2b_row_sgst;
    @BindView(R.id.tv_gstr1_b2b_row_cess)           TextView tv_gstr1_b2b_row_cess;
    @BindView(R.id.tv_gstr1_b2b_row_state_code)     TextView tv_gstr1_b2b_row_state_code;

    public GSTR1B2BReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(GSTR1B2BBean gstr1B2BBean, int position) {

        if (gstr1B2BBean != null) {

            tv_gstr1_b2b_row_sno.setText(gstr1B2BBean.getSno());
            if (!gstr1B2BBean.isSub()) {
                tv_gstr1_b2b_row_sno.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2b_row_gstin.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2b_row_no.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2b_row_date.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2b_row_value.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2b_row_gstrate.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2b_row_taxable_value.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2b_row_igst.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2b_row_cgst.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2b_row_sgst.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2b_row_cess.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2b_row_state_code.setBackgroundResource(R.color.table_header_gst);

                tv_gstr1_b2b_row_gstin.setText(gstr1B2BBean.getGstin());
                tv_gstr1_b2b_row_no.setText(String.valueOf(gstr1B2BBean.getInvoiceNo()));
                tv_gstr1_b2b_row_date.setText(gstr1B2BBean.getInvoiceDate());
                tv_gstr1_b2b_row_value.setText("");
                tv_gstr1_b2b_row_gstrate.setText("");
                tv_gstr1_b2b_row_state_code.setText(gstr1B2BBean.getCustStateCode());
            } else {

                tv_gstr1_b2b_row_sno.setBackgroundResource(R.color.white);
                tv_gstr1_b2b_row_gstin.setBackgroundResource(R.color.white);
                tv_gstr1_b2b_row_no.setBackgroundResource(R.color.white);
                tv_gstr1_b2b_row_date.setBackgroundResource(R.color.white);
                tv_gstr1_b2b_row_value.setBackgroundResource(R.color.white);
                tv_gstr1_b2b_row_gstrate.setBackgroundResource(R.color.white);
                tv_gstr1_b2b_row_taxable_value.setBackgroundResource(R.color.white);
                tv_gstr1_b2b_row_igst.setBackgroundResource(R.color.white);
                tv_gstr1_b2b_row_cgst.setBackgroundResource(R.color.white);
                tv_gstr1_b2b_row_sgst.setBackgroundResource(R.color.white);
                tv_gstr1_b2b_row_cess.setBackgroundResource(R.color.white);
                tv_gstr1_b2b_row_state_code.setBackgroundResource(R.color.white);

                tv_gstr1_b2b_row_gstin.setText("");
                tv_gstr1_b2b_row_no.setText("");
                tv_gstr1_b2b_row_date.setText("");
                tv_gstr1_b2b_row_value.setText(String.format("%.2f",gstr1B2BBean.getValue()));
                tv_gstr1_b2b_row_gstrate.setText(String.format("%.2f",gstr1B2BBean.getGstRate()));
                tv_gstr1_b2b_row_state_code.setText("");
            }

            tv_gstr1_b2b_row_taxable_value.setText(String.format("%.2f",gstr1B2BBean.getTaxableValue()));
            tv_gstr1_b2b_row_igst.setText(String.format("%.2f",gstr1B2BBean.getIgstAmt()));
            tv_gstr1_b2b_row_cgst.setText(String.format("%.2f",gstr1B2BBean.getCgstAmt()));
            tv_gstr1_b2b_row_sgst.setText(String.format("%.2f",gstr1B2BBean.getSgstAmt()));
            tv_gstr1_b2b_row_cess.setText(String.format("%.2f",gstr1B2BBean.getCessAmt()));

        }
    }
}
