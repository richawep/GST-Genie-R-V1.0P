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
import pos.wepindia.com.retail.view.Reports.Bean.CategoryWiseReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.CategoryWiseReportAdapter;

/**
 * Created by SachinV on 24-01-2018.
 */

public class CategoryWiseReport extends Fragment {

    @BindView(R.id.tv_categwise_total_discount)
    TextView tv_categwise_total_discount;
    @BindView(R.id.tv_categwise_total_igst)
    TextView tv_categwise_total_igst;
    @BindView(R.id.tv_categwise_total_cgst)
    TextView tv_categwise_total_cgst;
    @BindView(R.id.tv_categwise_total_sgst)
    TextView tv_categwise_total_sgst;
    @BindView(R.id.tv_categwise_total_cess)
    TextView tv_categwise_total_cess;
    @BindView(R.id.tv_categwise_total_taxable)
    TextView tv_categwise_total_taxable;
    @BindView(R.id.rv_category_wise)
    RecyclerView rv_category_wise;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    private ArrayList<CategoryWiseReportBean> categoryWiseReportBeanArrayList;
    private double totdisc=0, totIGSTTax =0;
    private double totSales=0,totService =0,totAmt =0,totcess=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.category_wise_report, container, false);

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
            categoryWiseReportBeanArrayList = bundle.getParcelableArrayList("dataList");
            totAmt = bundle.getDouble("totAmt", 0);
            totcess = bundle.getDouble("totcess", 0);
            totService = bundle.getDouble("totService", 0);
            totSales = bundle.getDouble("totSales", 0);
            totIGSTTax = bundle.getDouble("totIGSTTax", 0);
            totdisc = bundle.getDouble("totdisc", 0);

            tv_categwise_total_discount.setText(String.format("%.2f",totdisc));
            tv_categwise_total_igst.setText(String.format("%.2f",totIGSTTax));
            tv_categwise_total_cgst.setText(String.format("%.2f",totSales));
            tv_categwise_total_sgst.setText(String.format("%.2f",totService));
            tv_categwise_total_cess.setText(String.format("%.2f",totcess));
            tv_categwise_total_taxable.setText(String.format("%.2f",totAmt));

            if (categoryWiseReportBeanArrayList.size() > 0){
                rv_category_wise.setLayoutManager(new LinearLayoutManager(myContext));
                rv_category_wise.setAdapter(new CategoryWiseReportAdapter(myContext, categoryWiseReportBeanArrayList));
            }
        }
    }
}
