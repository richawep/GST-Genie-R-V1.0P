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

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.CustomerDetailReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.CustomerDetailReportAdapter;

/**
 * Created by SachinV on 25-01-2018.
 */

public class CustomerDetailReport extends Fragment {

    @BindView(R.id.rv_customer_detail)
    RecyclerView rv_customer_detail;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    private ArrayList<CustomerDetailReportBean> customerDetailReportBeanArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.customer_detail_report, container, false);

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
            customerDetailReportBeanArrayList = bundle.getParcelableArrayList("dataList");

            if (customerDetailReportBeanArrayList != null){
                rv_customer_detail.setLayoutManager(new LinearLayoutManager(myContext));
                rv_customer_detail.setAdapter(new CustomerDetailReportAdapter(myContext, customerDetailReportBeanArrayList));
            }
        }
    }
}
