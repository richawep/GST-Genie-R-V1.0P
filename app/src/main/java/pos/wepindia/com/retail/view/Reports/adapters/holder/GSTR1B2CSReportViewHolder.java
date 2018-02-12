package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1B2CSBean;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1B2CSReportViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_gstr1_b2cs_row_sno)              TextView tv_gstr1_b2cs_row_sno;
    @BindView(R.id.tv_gstr1_b2cs_row_gstrate)          TextView tv_gstr1_b2cs_row_gstrate;
    @BindView(R.id.tv_gstr1_b2cs_row_taxable_value)    TextView tv_gstr1_b2cs_row_taxable_value;
    @BindView(R.id.tv_gstr1_b2cs_row_igst)             TextView tv_gstr1_b2cs_row_igst;
    @BindView(R.id.tv_gstr1_b2cs_row_cgst)             TextView tv_gstr1_b2cs_row_cgst;
    @BindView(R.id.tv_gstr1_b2cs_row_sgst)             TextView tv_gstr1_b2cs_row_sgst;
    @BindView(R.id.tv_gstr1_b2cs_row_cess)             TextView tv_gstr1_b2cs_row_cess;

    public GSTR1B2CSReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(GSTR1B2CSBean gstr1B2CSBean, int position) {

        if (gstr1B2CSBean != null) {
            tv_gstr1_b2cs_row_sno.setText(String.valueOf(position+1));
            tv_gstr1_b2cs_row_gstrate.setText(String.format("%.2f",gstr1B2CSBean.getGSTRate()));
            tv_gstr1_b2cs_row_taxable_value.setText(String.format("%.2f",gstr1B2CSBean.getTaxableValue()));
            tv_gstr1_b2cs_row_igst.setText(String.format("%.2f",gstr1B2CSBean.getIGSTAmt()));
            tv_gstr1_b2cs_row_cgst.setText(String.format("%.2f",gstr1B2CSBean.getCGSTAmt()));
            tv_gstr1_b2cs_row_sgst.setText(String.format("%.2f",gstr1B2CSBean.getSGSTAmt()));
            tv_gstr1_b2cs_row_cess.setText(String.format("%.2f",gstr1B2CSBean.getCessAmt()));
        }
    }
}
