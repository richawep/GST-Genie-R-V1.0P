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
import pos.wepindia.com.retail.view.Reports.Bean.BillWiseBean;
import pos.wepindia.com.retail.view.Reports.adapters.BillWiseReportAdapter;

/**
 * Created by SachinV on 23-01-2018.
 */

public class BillWiseReport extends Fragment {

    private static final String TAG = BillWiseReport.class.getSimpleName();

    @BindView(R.id.tv_billwise_total_Items)
    TextView tv_billwise_total_Items;
    @BindView(R.id.tv_billwise_total_discount)
    TextView tv_billwise_total_discount;
    @BindView(R.id.tv_billwise_total_tax)
    TextView tv_billwise_total_tax;
    @BindView(R.id.tv_billwise_total_bill_amount)
    TextView tv_billwise_total_bill_amount;
    @BindView(R.id.rv_bill_wise)
    RecyclerView rv_bill_wise;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    private ArrayList<BillWiseBean> billWiseBeanArrayList;
    private double totbillAmt =0, totTax =0, totDisc =0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bill_wise_report, container, false);

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
            billWiseBeanArrayList = bundle.getParcelableArrayList("dataList");
            totbillAmt = bundle.getDouble("totbillAmt", 0);
            totTax = bundle.getDouble("totTax", 0);
            totDisc = bundle.getDouble("totDisc", 0);

            tv_billwise_total_discount.setText(String.format("%.2f",totDisc));
            tv_billwise_total_tax.setText(String.format("%.2f",totTax));
            tv_billwise_total_bill_amount.setText(String.format("%.2f",totbillAmt));

            if (billWiseBeanArrayList.size() > 0){
                rv_bill_wise.setLayoutManager(new LinearLayoutManager(myContext));
                rv_bill_wise.setAdapter(new BillWiseReportAdapter(myContext, billWiseBeanArrayList));
            }
        }
    }
}
