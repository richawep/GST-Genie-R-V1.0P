package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.CustomerWiseReportBean;

/**
 * Created by SachinV on 25-01-2018.
 */

public class CustomerWiseReportViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_customerwise_row_custid)           TextView tv_customerwise_row_custid;
    @BindView(R.id.tv_customerwise_row_name)             TextView tv_customerwise_row_name;
    @BindView(R.id.tv_customerwise_row_total_bills)      TextView tv_customerwise_row_total_bills;
    @BindView(R.id.tv_customerwise_row_lastran)          TextView tv_customerwise_row_lastran;
    @BindView(R.id.tv_customerwise_row_cash)             TextView tv_customerwise_row_cash;
    @BindView(R.id.tv_customerwise_row_card)             TextView tv_customerwise_row_card;
    @BindView(R.id.tv_customerwise_row_coupon)           TextView tv_customerwise_row_coupon;
    @BindView(R.id.tv_customerwise_row_credit)           TextView tv_customerwise_row_credit;
    @BindView(R.id.tv_customerwise_row_wallet)           TextView tv_customerwise_row_wallet;
    @BindView(R.id.tv_customerwise_row_totaltran)        TextView tv_customerwise_row_totaltran;

    public CustomerWiseReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(CustomerWiseReportBean customerWiseReportBean, int position) {

        if (customerWiseReportBean != null) {
            tv_customerwise_row_custid.setText(String.valueOf(customerWiseReportBean.getcustId()));
            tv_customerwise_row_name.setText(customerWiseReportBean.getName());
            tv_customerwise_row_total_bills.setText(String.valueOf(customerWiseReportBean.getTotalBills()));
            tv_customerwise_row_lastran.setText(String.format("%.2f",customerWiseReportBean.getLastTransaction()));
            tv_customerwise_row_cash.setText(String.format("%.2f",customerWiseReportBean.getCashAmount()));
            tv_customerwise_row_card.setText(String.format("%.2f",customerWiseReportBean.getCardPayment()));
            tv_customerwise_row_coupon.setText(String.valueOf(customerWiseReportBean.getCouponPayment()));
            tv_customerwise_row_credit.setText(String.format("%.2f",customerWiseReportBean.getPettyCashPayment()));
            tv_customerwise_row_wallet.setText(String.format("%.2f",customerWiseReportBean.getWalletPayment()));
            tv_customerwise_row_totaltran.setText(String.format("%.2f",customerWiseReportBean.getTotalTransaction()));
        }
    }
}
