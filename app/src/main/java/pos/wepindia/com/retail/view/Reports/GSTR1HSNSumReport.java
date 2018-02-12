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
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1HSNSummaryBean;
import pos.wepindia.com.retail.view.Reports.adapters.GSTR1HSNSumReportAdapter;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1HSNSumReport extends Fragment {

    @BindView(R.id.rv_gstr1_hsn_sum)
    RecyclerView rv_gstr1_hsn_sum;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    private ArrayList<GSTR1HSNSummaryBean> gstr1HSNSumReportArrayList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gstr1_hsn_summary_report, container, false);

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
            gstr1HSNSumReportArrayList = bundle.getParcelableArrayList("dataList");

            if (gstr1HSNSumReportArrayList.size() > 0){
                rv_gstr1_hsn_sum.setLayoutManager(new LinearLayoutManager(myContext));
                rv_gstr1_hsn_sum.setAdapter(new GSTR1HSNSumReportAdapter(myContext, gstr1HSNSumReportArrayList));
            }
        }
    }
}
