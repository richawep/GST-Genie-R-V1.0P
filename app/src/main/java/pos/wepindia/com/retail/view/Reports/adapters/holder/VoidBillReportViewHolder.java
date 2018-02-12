package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.TransactionReportBean;
import pos.wepindia.com.retail.view.Reports.Bean.VoidBillReportBean;

/**
 * Created by SachinV on 24-01-2018.
 */

public class VoidBillReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_row_voidBill_date)                 TextView tv_voidBill_date;
    @BindView(R.id.tv_row_voidBill_bill_number)          TextView tv_voidBill_bill_number;
    @BindView(R.id.tv_row_voidBill_totalItems)                TextView tv_row_voidBill_totalItems;
    @BindView(R.id.tv_row_voidBill_IGST_amount)          TextView tv_voidBill_IGST_amount;
    @BindView(R.id.tv_row_voidBill_CGST_amount)         TextView tv_voidBill_CGST_amount;
    @BindView(R.id.tv_row_voidBill_SGST_amount)         TextView tv_voidBill_SGST_amount;
    @BindView(R.id.tv_row_voidBill_cess_amount)       TextView tv_voidBill_cess_amount;
    @BindView(R.id.tv_row_voidBill_bill_amount)   TextView tv_voidBill_bill_amount;


    public VoidBillReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(VoidBillReportBean voidBillReportBean, int position) {

        if (voidBillReportBean != null) {
            tv_voidBill_date.setText(voidBillReportBean.getDate());
            tv_voidBill_bill_number.setText(voidBillReportBean.getBillno());
            tv_row_voidBill_totalItems.setText(String.valueOf(voidBillReportBean.getTotalItems()));
            tv_voidBill_IGST_amount.setText(String.format("%.2f",voidBillReportBean.getIGSTAmount()));
            tv_voidBill_CGST_amount.setText(String.format("%.2f",voidBillReportBean.getCGSTAmount()));
            tv_voidBill_SGST_amount.setText(String.format("%.2f",voidBillReportBean.getSGSTAmount()));
            tv_voidBill_cess_amount.setText(String.format("%.2f",voidBillReportBean.getCessAmount()));
            tv_voidBill_bill_amount.setText(String.format("%.2f",voidBillReportBean.getBillAmount()));
        }
    }

}
