package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.CustomerDetailReportBean;

/**
 * Created by SachinV on 25-01-2018.
 */

public class CustomerDetailReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_customerdetail_row_date)           TextView tv_customerdetail_row_date;
    @BindView(R.id.tv_customerdetail_row_billno)         TextView tv_customerdetail_row_billno;
    @BindView(R.id.tv_customerdetail_row_total_item)     TextView tv_customerdetail_row_total_item;
    @BindView(R.id.tv_customerdetail_row_discount)       TextView tv_customerdetail_row_discount;
    @BindView(R.id.tv_customerdetail_row_cash)           TextView tv_customerdetail_row_cash;
    @BindView(R.id.tv_customerdetail_row_card)           TextView tv_customerdetail_row_card;
    @BindView(R.id.tv_customerdetail_row_coupon)         TextView tv_customerdetail_row_coupon;
    @BindView(R.id.tv_customerdetail_row_credit)         TextView tv_customerdetail_row_credit;
    @BindView(R.id.tv_customerdetail_row_wallet)         TextView tv_customerdetail_row_wallet;
    @BindView(R.id.tv_customerdetail_row_billamt)        TextView tv_customerdetail_row_billamt;

    public CustomerDetailReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(CustomerDetailReportBean customerDetailReportBean, int position) {

        if (customerDetailReportBean != null) {
            tv_customerdetail_row_date.setText(customerDetailReportBean.getDate());
            tv_customerdetail_row_billno.setText(String.valueOf(customerDetailReportBean.getBillNumber()));
            tv_customerdetail_row_total_item.setText(String.valueOf(customerDetailReportBean.getTotalBills()));
            tv_customerdetail_row_discount.setText(String.format("%.2f",customerDetailReportBean.getDiscount()));
            tv_customerdetail_row_cash.setText(String.format("%.2f",customerDetailReportBean.getCashAmount()));
            tv_customerdetail_row_card.setText(String.format("%.2f",customerDetailReportBean.getCardPayment()));
            tv_customerdetail_row_coupon.setText(String.format("%.2f",customerDetailReportBean.getCouponPayment()));
            tv_customerdetail_row_credit.setText(String.format("%.2f",customerDetailReportBean.getPettyCashPayment()));
            tv_customerdetail_row_wallet.setText(String.format("%.2f",customerDetailReportBean.getWalletPayment()));
            tv_customerdetail_row_billamt.setText(String.format("%.2f",customerDetailReportBean.getBillAmount()));
        }
    }

}
