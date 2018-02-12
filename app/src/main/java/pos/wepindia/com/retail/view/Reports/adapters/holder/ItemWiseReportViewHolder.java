package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.ItemWiseReportBean;

/**
 * Created by SachinV on 25-01-2018.
 */

public class ItemWiseReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_itemwise_row_itemno)          TextView tv_itemwise_row_itemno;
    @BindView(R.id.tv_itemwise_row_itemname)        TextView tv_itemwise_row_itemname;
    @BindView(R.id.tv_itemwise_row_solqty)          TextView tv_itemwise_row_solqty;
    @BindView(R.id.tv_itemwise_row_discount)        TextView tv_itemwise_row_discount;
    @BindView(R.id.tv_itemwise_row_tax)             TextView tv_itemwise_row_tax;
    @BindView(R.id.tv_itemwise_row_taxableamount)   TextView tv_itemwise_row_taxableamount;
    @BindView(R.id.tv_itemwise_row_modifieramt)     TextView tv_itemwise_row_modifieramt;

    public ItemWiseReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(ItemWiseReportBean itemWiseReportBean, int position) {

        if (itemWiseReportBean != null) {
            tv_itemwise_row_itemno.setText(String.valueOf(itemWiseReportBean.getItemNo()));
            tv_itemwise_row_itemname.setText(itemWiseReportBean.getItemName());
            tv_itemwise_row_solqty.setText(String.format("%.2f",itemWiseReportBean.getSoldQuantity()));
            tv_itemwise_row_discount.setText(String.format("%.2f",itemWiseReportBean.getDiscount()));
            tv_itemwise_row_tax.setText(String.format("%.2f",itemWiseReportBean.getTax()));
            tv_itemwise_row_taxableamount.setText(String.format("%.2f",itemWiseReportBean.getTaxableValue()));
            tv_itemwise_row_modifieramt.setText(String.format("%.2f",itemWiseReportBean.getModifierAmount()));
        }
    }
}
