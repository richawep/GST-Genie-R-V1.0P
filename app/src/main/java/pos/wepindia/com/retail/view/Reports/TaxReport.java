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
import pos.wepindia.com.retail.view.Reports.Bean.TaxReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.TaxReportAdapter;

/**
 * Created by SachinV on 24-01-2018.
 */

public class TaxReport extends Fragment {

    private static final String TAG = TaxReport.class.getSimpleName();

    @BindView(R.id.tv_taxreport_total_tax_amount)
    TextView tv_taxreport_total_tax_amount;
    @BindView(R.id.tv_taxreport_total_taxable_amount)
    TextView tv_taxreport_total_taxable_amount;
    @BindView(R.id.rv_tax_report)
    RecyclerView rv_tax_report;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    private ArrayList<TaxReportBean> taxReportBeanArrayList;

    double totTax = 0, totAmt = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tax_report, container, false);

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
            taxReportBeanArrayList = bundle.getParcelableArrayList("dataList");
            totAmt = bundle.getDouble("totAmt", 0);
            totTax = bundle.getDouble("totTax", 0);

            tv_taxreport_total_tax_amount.setText(String.format("%.2f", totTax));
            tv_taxreport_total_taxable_amount.setText(String.format("%.2f", totAmt));

            if (taxReportBeanArrayList.size() > 0){
                rv_tax_report.setLayoutManager(new LinearLayoutManager(myContext));
                rv_tax_report.setAdapter(new TaxReportAdapter(myContext, taxReportBeanArrayList));
            }
        }
    }
}
