package pos.wepindia.com.retail.view.Reports;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.Constants;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.TransactionReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.TransactionReportAdapter;

/**
 * Created by SachinV on 24-01-2018.
 */

public class TransactionReport extends Fragment {

    private static final String TAG = TransactionReport.class.getSimpleName();

    @BindView(R.id.tv_transaction_total_bill_amount)
    TextView tv_transaction_total_bill_amount;
    @BindView(R.id.tv_transaction_total_cash_payment)
    TextView tv_transaction_total_cash_payment;
    @BindView(R.id.tv_transaction_total_card_payment)
    TextView tv_transaction_total_card_payment;
    @BindView(R.id.tv_transaction_total_coupon_payment)
    TextView tv_transaction_total_coupon_payment;
    @BindView(R.id.tv_transaction_total_petty_cash_payment)
    TextView tv_transaction_total_petty_cash_payment;
    @BindView(R.id.tv_transaction_total_wallet_payment)
    TextView tv_transaction_total_wallet_payment;
    @BindView(R.id.tv_transaction_total_mswipe_payment)
    TextView tv_transaction_total_mswipe_payment;
    @BindView(R.id.tv_transaction_total_aeps_payment)
    TextView tv_transaction_total_aeps_payment;
    @BindView(R.id.rv_transaction)
    RecyclerView rv_transaction;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    private ArrayList<TransactionReportBean> transactionReportBeanArrayList;

    double  totCash =0, totCoupon =0,totWallet =0,totCard =0;
    double totAmt =0, totPetty =0, totMSwipe = 0, totAEPS = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.transaction_report, container, false);

        myContext = getContext();
        MsgBox = new MessageDialog(myContext);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            transactionReportBeanArrayList = bundle.getParcelableArrayList("dataList");
            totAmt = bundle.getDouble("totAmt", 0);
            totCash = bundle.getDouble("totCash", 0);
            totCoupon = bundle.getDouble("totCoupon", 0);
            totCard = bundle.getDouble("totCard", 0);
            totPetty = bundle.getDouble("totPetty", 0);
            totWallet = bundle.getDouble("totWallet", 0);
            totMSwipe = bundle.getDouble(Constants.MSWIPE_AMOUNT_TOTAL, 0);
            totAEPS = bundle.getDouble(Constants.AEPS_AMOUNT_TOTAL, 0);

            tv_transaction_total_bill_amount.setText(String.format("%.2f",totAmt));
            tv_transaction_total_cash_payment.setText(String.format("%.2f",totCash));
            tv_transaction_total_coupon_payment.setText(String.format("%.2f",totCoupon));
            tv_transaction_total_card_payment.setText(String.format("%.2f",totCard));
            tv_transaction_total_petty_cash_payment.setText(String.format("%.2f",totPetty));
            tv_transaction_total_wallet_payment.setText(String.format("%.2f",totWallet));
            tv_transaction_total_mswipe_payment.setText(String.format("%.2f",totMSwipe));
            tv_transaction_total_aeps_payment.setText(String.format("%.2f",totAEPS));

            if (transactionReportBeanArrayList.size() > 0){
                rv_transaction.setLayoutManager(new LinearLayoutManager(myContext));
                rv_transaction.setAdapter(new TransactionReportAdapter(myContext, transactionReportBeanArrayList));
            }
        }
    }
}
