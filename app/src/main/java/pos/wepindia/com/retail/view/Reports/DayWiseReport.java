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
import pos.wepindia.com.retail.view.Reports.Bean.DayWiseBean;
import pos.wepindia.com.retail.view.Reports.adapters.DayWiseReportAdapter;

/**
 * Created by SachinV on 23-01-2018.
 */

public class DayWiseReport extends Fragment {

    private static final String TAG = BillWiseReport.class.getSimpleName();

    @BindView(R.id.tv_daywise_total_discount)
    TextView tv_daywise_total_discount;
    @BindView(R.id.tv_daywise_total_igst)
    TextView tv_daywise_total_igst;
    @BindView(R.id.tv_daywise_total_cgst)
    TextView tv_daywise_total_cgst;
    @BindView(R.id.tv_daywise_total_utgst_sgst)
    TextView tv_daywise_total_utgst_sgst;
    @BindView(R.id.tv_daywise_total_cess)
    TextView tv_daywise_total_cess;
    @BindView(R.id.tv_daywise_total_bill_amount)
    TextView tv_daywise_total_bill_amount;
    @BindView(R.id.rv_day_wise)
    RecyclerView rv_day_wise;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    ArrayList<DayWiseBean> dayWiseBeanArrayList;
    double  totDis =0, totIGSTTax=0;
    double totbillAmt =0, totSalesTax =0, totServiceTax=0, totcess =0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.day_wise_report, container, false);

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
            dayWiseBeanArrayList = bundle.getParcelableArrayList("dataList");
            totbillAmt = bundle.getDouble("totbillAmt", 0);
            totSalesTax = bundle.getDouble("totSalesTax", 0);
            totServiceTax = bundle.getDouble("totServiceTax", 0);
            totIGSTTax = bundle.getDouble("totIGSTTax", 0);
            totDis = bundle.getDouble("totDis", 0);
            totcess = bundle.getDouble("totcess", 0);

            tv_daywise_total_discount.setText(String.format("%.2f",totDis));
            tv_daywise_total_igst.setText(String.format("%.2f",totIGSTTax));
            tv_daywise_total_cgst.setText(String.format("%.2f",totSalesTax));
            tv_daywise_total_utgst_sgst.setText(String.format("%.2f",totServiceTax));
            tv_daywise_total_cess.setText(String.format("%.2f",totcess));
            tv_daywise_total_bill_amount.setText(String.format("%.2f",totbillAmt));

            if (dayWiseBeanArrayList.size() > 0){
                rv_day_wise.setLayoutManager(new LinearLayoutManager(myContext));
                rv_day_wise.setAdapter(new DayWiseReportAdapter(myContext, dayWiseBeanArrayList));
            }
        }

    }
}
