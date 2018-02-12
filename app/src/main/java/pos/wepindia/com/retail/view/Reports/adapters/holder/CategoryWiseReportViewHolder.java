package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.CategoryWiseReportBean;

/**
 * Created by SachinV on 24-01-2018.
 */

public class CategoryWiseReportViewHolder extends RecyclerView.ViewHolder  {

    @BindView(R.id.tv_categwise_row_code)       TextView tv_categwise_row_code;
    @BindView(R.id.tv_categwise_row_name)       TextView tv_categwise_row_name;
    @BindView(R.id.tv_categwise_row_total_item) TextView tv_categwise_row_total_item;
    @BindView(R.id.tv_categwise_row_discount)   TextView tv_categwise_row_discount;
    @BindView(R.id.tv_categwise_row_igst)       TextView tv_categwise_row_igst;
    @BindView(R.id.tv_categwise_row_cgst)       TextView tv_categwise_row_cgst;
    @BindView(R.id.tv_categwise_row_sgst)       TextView tv_categwise_row_sgst;
    @BindView(R.id.tv_categwise_row_cess)       TextView tv_categwise_row_cess;
    @BindView(R.id.tv_categwise_row_taxable)    TextView tv_categwise_row_taxable;

    public CategoryWiseReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(CategoryWiseReportBean categoryWiseReportBean, int position) {

        if (categoryWiseReportBean != null) {
            tv_categwise_row_code.setText(String.valueOf(categoryWiseReportBean.getCategCode()));
            tv_categwise_row_name.setText(categoryWiseReportBean.getName());
            tv_categwise_row_total_item.setText(String.valueOf(categoryWiseReportBean.getTotalItems()));
            tv_categwise_row_discount.setText(String.format("%.2f",categoryWiseReportBean.getDiscount()));
            tv_categwise_row_igst.setText(String.format("%.2f",categoryWiseReportBean.getIGSTAmount()));
            tv_categwise_row_cgst.setText(String.format("%.2f",categoryWiseReportBean.getCGSTAmount()));
            tv_categwise_row_sgst.setText(String.format("%.2f",categoryWiseReportBean.getSGSTAmount()));
            tv_categwise_row_cess.setText(String.format("%.2f",categoryWiseReportBean.getCESSAmount()));
            tv_categwise_row_taxable.setText(String.format("%.2f",categoryWiseReportBean.getTaxableValue()));
        }
    }

}
