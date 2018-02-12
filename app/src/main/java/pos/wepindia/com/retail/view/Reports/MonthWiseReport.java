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
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.MonthWiseBean;
import pos.wepindia.com.retail.view.Reports.adapters.MonthWiseReportAdapter;

/**
 * Created by SachinV on 24-01-2018.
 */

public class MonthWiseReport extends Fragment {

    private static final String TAG = MonthWiseReport.class.getSimpleName();

    @BindView(R.id.tv_monthwise_total_discount)
    TextView tv_monthwise_total_discount;
    @BindView(R.id.tv_monthwise_total_igst)
    TextView tv_monthwise_total_igst;
    @BindView(R.id.tv_monthwise_total_cgst)
    TextView tv_monthwise_total_cgst;
    @BindView(R.id.tv_monthwise_total_utgst_sgst)
    TextView tv_monthwise_total_utgst_sgst;
    @BindView(R.id.tv_monthwise_total_cess)
    TextView tv_monthwise_total_cess;
    @BindView(R.id.tv_monthwise_total_bill_amount)
    TextView tv_monthwise_total_bill_amount;
    @BindView(R.id.rv_month_wise)
    RecyclerView rv_month_wise;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    ArrayList<MonthWiseBean> monthWiseBeanArrayList;
    double  totDis =0, totIGSTTax=0;
    double totbillAmt =0, totSalesTax =0, totServiceTax=0, totcess =0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.month_wise_report, container, false);

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
            monthWiseBeanArrayList = bundle.getParcelableArrayList("dataList");
            totbillAmt = bundle.getDouble("totbillAmt", 0);
            totSalesTax = bundle.getDouble("totSalesTax", 0);
            totServiceTax = bundle.getDouble("totServiceTax", 0);
            totIGSTTax = bundle.getDouble("totIGSTTax", 0);
            totDis = bundle.getDouble("totDis", 0);
            totcess = bundle.getDouble("totcess", 0);

            tv_monthwise_total_discount.setText(String.format("%.2f",totDis));
            tv_monthwise_total_igst.setText(String.format("%.2f",totIGSTTax));
            tv_monthwise_total_cgst.setText(String.format("%.2f",totSalesTax));
            tv_monthwise_total_utgst_sgst.setText(String.format("%.2f",totServiceTax));
            tv_monthwise_total_cess.setText(String.format("%.2f",totcess));
            tv_monthwise_total_bill_amount.setText(String.format("%.2f",totbillAmt));

            if (monthWiseBeanArrayList.size() > 0){
                rv_month_wise.setLayoutManager(new LinearLayoutManager(myContext));
                rv_month_wise.setAdapter(new MonthWiseReportAdapter(myContext, monthWiseBeanArrayList));
            }
        }
    }
}
