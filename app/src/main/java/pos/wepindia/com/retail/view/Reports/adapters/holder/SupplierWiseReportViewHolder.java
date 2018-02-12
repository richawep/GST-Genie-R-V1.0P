package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.SupplierWiseReportBean;

/**
 * Created by SachinV on 25-01-2018.
 */

public class SupplierWiseReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_supplierwise_row_supplier_code)   TextView tv_supplierwise_row_supplier_code;
    @BindView(R.id.tv_supplierwise_row_name)            TextView tv_supplierwise_row_name;
    @BindView(R.id.tv_supplierwise_row_total_items)     TextView tv_supplierwise_row_total_items;
    @BindView(R.id.tv_supplierwise_row_bill_amount)     TextView tv_supplierwise_row_bill_amount;

    public SupplierWiseReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(SupplierWiseReportBean supplierWiseReportBean, int position) {

        if (supplierWiseReportBean != null) {
            tv_supplierwise_row_supplier_code.setText(String.valueOf(supplierWiseReportBean.getSupplierCode()));
            tv_supplierwise_row_name.setText(String.valueOf(supplierWiseReportBean.getName()));
            tv_supplierwise_row_total_items.setText(String.valueOf(supplierWiseReportBean.getTotalBills()));
            tv_supplierwise_row_bill_amount.setText(String.format("%.2f",supplierWiseReportBean.getBillAmount()));
        }
    }
}
