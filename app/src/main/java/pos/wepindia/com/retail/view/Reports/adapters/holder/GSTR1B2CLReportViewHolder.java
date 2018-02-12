package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1B2CLBean;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1B2CLReportViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_gstr1_b2cl_row_sno)                           TextView tv_gstr1_b2cl_row_sno;
    @BindView(R.id.tv_gstr1_b2cl_row_recipient_state_code)          TextView tv_gstr1_b2cl_row_recipient_state_code;
    @BindView(R.id.tv_gstr1_b2cl_row_name)                          TextView tv_gstr1_b2cl_row_name;
    @BindView(R.id.tv_gstr1_b2cl_row_no)                            TextView tv_gstr1_b2cl_row_no;
    @BindView(R.id.tv_gstr1_b2cl_row_date)                          TextView tv_gstr1_b2cl_row_date;
    @BindView(R.id.tv_gstr1_b2cl_row_value)                         TextView tv_gstr1_b2cl_row_value;
    @BindView(R.id.tv_gstr1_b2cl_row_igstrate)                      TextView tv_gstr1_b2cl_row_igstrate;
    @BindView(R.id.tv_gstr1_b2cl_row_taxable_value)                 TextView tv_gstr1_b2cl_row_taxable_value;
    @BindView(R.id.tv_gstr1_b2cl_row_igst)                          TextView tv_gstr1_b2cl_row_igst;
    @BindView(R.id.tv_gstr1_b2cl_row_cess)                          TextView tv_gstr1_b2cl_row_cess;

    public GSTR1B2CLReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(GSTR1B2CLBean gstr1B2CLBean, int position) {

        if (gstr1B2CLBean != null) {


            tv_gstr1_b2cl_row_sno.setText(gstr1B2CLBean.getSno());
            if (!gstr1B2CLBean.isSub()) {
                tv_gstr1_b2cl_row_sno.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2cl_row_recipient_state_code.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2cl_row_name.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2cl_row_no.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2cl_row_date.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2cl_row_value.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2cl_row_igstrate.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2cl_row_taxable_value.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2cl_row_igst.setBackgroundResource(R.color.table_header_gst);
                tv_gstr1_b2cl_row_cess.setBackgroundResource(R.color.table_header_gst);

                tv_gstr1_b2cl_row_recipient_state_code.setText(gstr1B2CLBean.getRecipientStateCode());
                tv_gstr1_b2cl_row_name.setText(gstr1B2CLBean.getRecipientName());
                tv_gstr1_b2cl_row_no.setText(String.valueOf(gstr1B2CLBean.getInvoiceNo()));
                tv_gstr1_b2cl_row_date.setText(gstr1B2CLBean.getInvoiceDate());
                tv_gstr1_b2cl_row_value.setText("");
                tv_gstr1_b2cl_row_igstrate.setText("");
            } else {

                tv_gstr1_b2cl_row_sno.setBackgroundResource(R.color.white);
                tv_gstr1_b2cl_row_recipient_state_code.setBackgroundResource(R.color.white);
                tv_gstr1_b2cl_row_name.setBackgroundResource(R.color.white);
                tv_gstr1_b2cl_row_no.setBackgroundResource(R.color.white);
                tv_gstr1_b2cl_row_date.setBackgroundResource(R.color.white);
                tv_gstr1_b2cl_row_value.setBackgroundResource(R.color.white);
                tv_gstr1_b2cl_row_igstrate.setBackgroundResource(R.color.white);
                tv_gstr1_b2cl_row_taxable_value.setBackgroundResource(R.color.white);
                tv_gstr1_b2cl_row_igst.setBackgroundResource(R.color.white);
                tv_gstr1_b2cl_row_cess.setBackgroundResource(R.color.white);

                tv_gstr1_b2cl_row_recipient_state_code.setText("");
                tv_gstr1_b2cl_row_name.setText("");
                tv_gstr1_b2cl_row_no.setText("");
                tv_gstr1_b2cl_row_date.setText("");
                tv_gstr1_b2cl_row_value.setText(String.format("%.2f",gstr1B2CLBean.getValue()));
                tv_gstr1_b2cl_row_igstrate.setText(String.format("%.2f",gstr1B2CLBean.getIgstRate()));
            }

            tv_gstr1_b2cl_row_taxable_value.setText(String.format("%.2f",gstr1B2CLBean.getTaxableValue()));
            tv_gstr1_b2cl_row_igst.setText(String.format("%.2f",gstr1B2CLBean.getIgstAmt()));
            tv_gstr1_b2cl_row_cess.setText(String.format("%.2f",gstr1B2CLBean.getCessAmt()));
        }
    }
}
