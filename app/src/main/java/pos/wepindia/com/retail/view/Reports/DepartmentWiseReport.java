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
import pos.wepindia.com.retail.view.Reports.Bean.DepartmentWiseReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.DepartmentWiseReportAdapter;

/**
 * Created by SachinV on 24-01-2018.
 */

public class DepartmentWiseReport extends Fragment {

    @BindView(R.id.tv_deptwise_total_discount)
    TextView tv_deptwise_total_discount;
    @BindView(R.id.tv_deptwise_total_igst)
    TextView tv_deptwise_total_igst;
    @BindView(R.id.tv_deptwise_total_cgst)
    TextView tv_deptwise_total_cgst;
    @BindView(R.id.tv_deptwise_total_sgst)
    TextView tv_deptwise_total_sgst;
    @BindView(R.id.tv_deptwise_total_cess)
    TextView tv_deptwise_total_cess;
    @BindView(R.id.tv_deptwise_total_taxable)
    TextView tv_deptwise_total_taxable;
    @BindView(R.id.rv_department_wise)
    RecyclerView rv_department_wise;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    private ArrayList<DepartmentWiseReportBean> departmentWiseReportBeanArrayList;
    private double totdisc=0, totIGSTTax =0;
    private double totSales=0,totService =0,totAmt =0,totcess=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.department_wise_report, container, false);

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
            departmentWiseReportBeanArrayList = bundle.getParcelableArrayList("dataList");
            totAmt = bundle.getDouble("totAmt", 0);
            totcess = bundle.getDouble("totcess", 0);
            totService = bundle.getDouble("totService", 0);
            totSales = bundle.getDouble("totSales", 0);
            totIGSTTax = bundle.getDouble("totIGSTTax", 0);
            totdisc = bundle.getDouble("totdisc", 0);

            tv_deptwise_total_discount.setText(String.format("%.2f",totdisc));
            tv_deptwise_total_igst.setText(String.format("%.2f",totIGSTTax));
            tv_deptwise_total_cgst.setText(String.format("%.2f",totSales));
            tv_deptwise_total_sgst.setText(String.format("%.2f",totService));
            tv_deptwise_total_cess.setText(String.format("%.2f",totcess));
            tv_deptwise_total_taxable.setText(String.format("%.2f",totAmt));

            if (departmentWiseReportBeanArrayList.size() > 0){
                rv_department_wise.setLayoutManager(new LinearLayoutManager(myContext));
                rv_department_wise.setAdapter(new DepartmentWiseReportAdapter(myContext, departmentWiseReportBeanArrayList));
            }
        }
    }
    
}
