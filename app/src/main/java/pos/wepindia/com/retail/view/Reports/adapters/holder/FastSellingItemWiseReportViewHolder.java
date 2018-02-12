package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.FastSellingItemwiseReportBean;

/**
 * Created by SachinV on 25-01-2018.
 */

public class FastSellingItemWiseReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_fastselling_itemwise_row_sno)             TextView tv_fastselling_itemwise_row_sno;
    @BindView(R.id.tv_fastselling_itemwise_row_menucode)        TextView tv_fastselling_itemwise_row_menucode;
    @BindView(R.id.tv_fastselling_itemwise_row_deptname)        TextView tv_fastselling_itemwise_row_deptname;
    @BindView(R.id.tv_fastselling_itemwise_row_categname)       TextView tv_fastselling_itemwise_row_categname;
    @BindView(R.id.tv_fastselling_itemwise_row_itemname)        TextView tv_fastselling_itemwise_row_itemname;
    @BindView(R.id.tv_fastselling_itemwise_row_qty)             TextView tv_fastselling_itemwise_row_qty;
    @BindView(R.id.tv_fastselling_itemwise_row_totalprice)      TextView tv_fastselling_itemwise_row_totalprice;

    public FastSellingItemWiseReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(FastSellingItemwiseReportBean fastSellingItemwiseReportBean, int position) {

        if (fastSellingItemwiseReportBean != null) {
            tv_fastselling_itemwise_row_sno.setText(String.valueOf(position+1));
            tv_fastselling_itemwise_row_menucode.setText(String.valueOf(fastSellingItemwiseReportBean.getMenuCode()));
            tv_fastselling_itemwise_row_deptname.setText(fastSellingItemwiseReportBean.getDeptName());
            tv_fastselling_itemwise_row_categname.setText(fastSellingItemwiseReportBean.getCategName());
            tv_fastselling_itemwise_row_itemname.setText(fastSellingItemwiseReportBean.getItemName());
            tv_fastselling_itemwise_row_qty.setText(String.format("%.2f",fastSellingItemwiseReportBean.getQuantity()));
            tv_fastselling_itemwise_row_totalprice.setText(String.format("%.2f",fastSellingItemwiseReportBean.getTotalPrice()));
        }
    }
}
