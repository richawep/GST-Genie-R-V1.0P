package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.DepartmentWiseReportBean;

/**
 * Created by SachinV on 24-01-2018.
 */

public class DepartmentWiseReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_deptwise_row_code)       TextView tv_deptwise_row_code;
    @BindView(R.id.tv_deptwise_row_name)       TextView tv_deptwise_row_name;
    @BindView(R.id.tv_deptwise_row_total_item) TextView tv_deptwise_row_total_item;
    @BindView(R.id.tv_deptwise_row_discount)   TextView tv_deptwise_row_discount;
    @BindView(R.id.tv_deptwise_row_igst)       TextView tv_deptwise_row_igst;
    @BindView(R.id.tv_deptwise_row_cgst)       TextView tv_deptwise_row_cgst;
    @BindView(R.id.tv_deptwise_row_sgst)       TextView tv_deptwise_row_sgst;
    @BindView(R.id.tv_deptwise_row_cess)       TextView tv_deptwise_row_cess;
    @BindView(R.id.tv_deptwise_row_taxable)    TextView tv_deptwise_row_taxable;

    public DepartmentWiseReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(DepartmentWiseReportBean departmentWiseReportBean, int position) {

        if (departmentWiseReportBean != null) {
            tv_deptwise_row_code.setText(String.valueOf(position+1));
            tv_deptwise_row_name.setText(departmentWiseReportBean.getName());
            tv_deptwise_row_total_item.setText(String.valueOf(departmentWiseReportBean.getTotalItems()));
            tv_deptwise_row_discount.setText(String.format("%.2f",departmentWiseReportBean.getDiscount()));
            tv_deptwise_row_igst.setText(String.format("%.2f",departmentWiseReportBean.getIGSTAmount()));
            tv_deptwise_row_cgst.setText(String.format("%.2f",departmentWiseReportBean.getCGSTAmount()));
            tv_deptwise_row_sgst.setText(String.format("%.2f",departmentWiseReportBean.getSGSTAmount()));
            tv_deptwise_row_cess.setText(String.format("%.2f",departmentWiseReportBean.getCESSAmount()));
            tv_deptwise_row_taxable.setText(String.format("%.2f",departmentWiseReportBean.getTaxableValue()));
        }
    }
    
}
