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
import pos.wepindia.com.retail.view.Reports.Bean.CustomerWiseReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.CustomerWiseReportAdapter;

/**
 * Created by SachinV on 25-01-2018.
 */

public class CustomerWiseReport extends Fragment {

    @BindView(R.id.rv_customer_wise)
    RecyclerView rv_customer_wise;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    private ArrayList<CustomerWiseReportBean> customerWiseReportBeanArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.customer_wise_report, container, false);

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
            customerWiseReportBeanArrayList = bundle.getParcelableArrayList("dataList");

            if (customerWiseReportBeanArrayList != null){
                rv_customer_wise.setLayoutManager(new LinearLayoutManager(myContext));
                rv_customer_wise.setAdapter(new CustomerWiseReportAdapter(myContext, customerWiseReportBeanArrayList));
            }
        }
    }
}
