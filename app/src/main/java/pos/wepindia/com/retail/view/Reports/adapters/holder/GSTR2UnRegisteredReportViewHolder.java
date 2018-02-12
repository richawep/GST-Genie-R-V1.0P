package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR2UnRegisteredBean;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR2UnRegisteredReportViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_gstr2_unregistered_row_sno)            TextView tv_gstr2_unregistered_row_sno;
    @BindView(R.id.tv_gstr2_unregistered_row_name)          TextView tv_gstr2_unregistered_row_name;
    @BindView(R.id.tv_gstr2_unregistered_row_bill_number)    TextView tv_gstr2_unregistered_row_bill_number;
    @BindView(R.id.tv_gstr2_unregistered_row_bill_date)      TextView tv_gstr2_unregistered_row_bill_date;
    @BindView(R.id.tv_gstr2_unregistered_row_supply_type)    TextView tv_gstr2_unregistered_row_supply_type;
    @BindView(R.id.tv_gstr2_unregistered_row_hsn_code)       TextView tv_gstr2_unregistered_row_hsn_code;
    @BindView(R.id.tv_gstr2_unregistered_row_value)          TextView tv_gstr2_unregistered_row_value;
    @BindView(R.id.tv_gstr2_unregistered_row_taxable)        TextView tv_gstr2_unregistered_row_taxable;
    @BindView(R.id.tv_gstr2_unregistered_row_igst)           TextView tv_gstr2_unregistered_row_igst;
    @BindView(R.id.tv_gstr2_unregistered_row_petty_cgst)     TextView tv_gstr2_unregistered_row_petty_cgst;
    @BindView(R.id.tv_gstr2_unregistered_row_sgst)           TextView tv_gstr2_unregistered_row_sgst;
    @BindView(R.id.tv_gstr2_unregistered_row_cess)           TextView tv_gstr2_unregistered_row_cess;
    @BindView(R.id.tv_gstr2_unregistered_row_amount)         TextView tv_gstr2_unregistered_row_amount;

    public GSTR2UnRegisteredReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(GSTR2UnRegisteredBean gstr2UnRegisteredBean, int position) {

        if (gstr2UnRegisteredBean != null) {
            tv_gstr2_unregistered_row_sno.setText(String.valueOf(position+1));
            tv_gstr2_unregistered_row_name.setText(gstr2UnRegisteredBean.getSupplierName());
            tv_gstr2_unregistered_row_bill_number.setText(String.valueOf(gstr2UnRegisteredBean.getInvoiceNo()));
            tv_gstr2_unregistered_row_bill_date.setText(gstr2UnRegisteredBean.getInvoiceDate());
            tv_gstr2_unregistered_row_supply_type.setText(gstr2UnRegisteredBean.getSupplyType());
            tv_gstr2_unregistered_row_hsn_code.setText(gstr2UnRegisteredBean.getHsnCode());
            tv_gstr2_unregistered_row_value.setText(String.format("%.2f",gstr2UnRegisteredBean.getValue()));
            tv_gstr2_unregistered_row_taxable.setText(String.format("%.2f",gstr2UnRegisteredBean.getTaxableVlue()));
            tv_gstr2_unregistered_row_igst.setText(String.format("%.2f",gstr2UnRegisteredBean.getIgstAmt()));
            tv_gstr2_unregistered_row_petty_cgst.setText(String.format("%.2f",gstr2UnRegisteredBean.getCgstAmt()));
            tv_gstr2_unregistered_row_sgst.setText(String.format("%.2f",gstr2UnRegisteredBean.getSgstAmt()));
            tv_gstr2_unregistered_row_cess.setText(String.format("%.2f",gstr2UnRegisteredBean.getCessAmt()));
            tv_gstr2_unregistered_row_amount.setText(String.format("%.2f",gstr2UnRegisteredBean.getAmount()));
        }
    }
}
