package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.TransactionReportBean;

/**
 * Created by SachinV on 24-01-2018.
 */

public class TransactionReportViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_transaction_row_date)                 TextView tv_transaction_row_date;
    @BindView(R.id.tv_transaction_row_bill_number)          TextView tv_transaction_row_bill_number;
    @BindView(R.id.tv_transaction_row_Items)                TextView tv_transaction_row_Items;
    @BindView(R.id.tv_transaction_row_bill_amount)          TextView tv_transaction_row_bill_amount;
    @BindView(R.id.tv_transaction_row_cash_payment)         TextView tv_transaction_row_cash_payment;
    @BindView(R.id.tv_transaction_row_card_payment)         TextView tv_transaction_row_card_payment;
    @BindView(R.id.tv_transaction_row_coupon_payment)       TextView tv_transaction_row_coupon_payment;
    @BindView(R.id.tv_transaction_row_petty_cash_payment)   TextView tv_transaction_row_petty_cash_payment;
    @BindView(R.id.tv_transaction_row_wallet_payment)       TextView tv_transaction_row_wallet_payment;
    @BindView(R.id.tv_transaction_row_mswipe_payment)       TextView tv_transaction_row_mswipe_payment;
    @BindView(R.id.tv_transaction_row_aeps_payment)       TextView tv_transaction_row_aeps_payment;

    public TransactionReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(TransactionReportBean transactionReportBean, int position) {

        if (transactionReportBean != null) {
            tv_transaction_row_date.setText(transactionReportBean.getInvoiceDate());
            tv_transaction_row_bill_number.setText(String.valueOf(transactionReportBean.getBillNumber()));
            tv_transaction_row_Items.setText(String.valueOf(transactionReportBean.getItems()));
            tv_transaction_row_bill_amount.setText(String.format("%.2f",transactionReportBean.getBillAmount()));
            tv_transaction_row_cash_payment.setText(String.format("%.2f",transactionReportBean.getCashAmount()));
            tv_transaction_row_card_payment.setText(String.format("%.2f",transactionReportBean.getCardPayment()));
            tv_transaction_row_coupon_payment.setText(String.format("%.2f",transactionReportBean.getCouponPayment()));
            tv_transaction_row_petty_cash_payment.setText(String.format("%.2f",transactionReportBean.getPettyCashPayment()));
            tv_transaction_row_wallet_payment.setText(String.format("%.2f",transactionReportBean.getWalletPayment()));
            tv_transaction_row_mswipe_payment.setText(String.format("%.2f",transactionReportBean.getmSwipePayment()));
            tv_transaction_row_aeps_payment.setText(String.format("%.2f",transactionReportBean.getAepsPayment()));
        }
    }

}
