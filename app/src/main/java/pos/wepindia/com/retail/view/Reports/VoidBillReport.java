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
import pos.wepindia.com.retail.view.Reports.Bean.VoidBillReportBean;
import pos.wepindia.com.retail.view.Reports.adapters.TransactionReportAdapter;
import pos.wepindia.com.retail.view.Reports.adapters.VoidBillReportAdapter;

/**
 * Created by Administrator on 06-02-2018.
 */

public class VoidBillReport extends Fragment {

    private static final String TAG = VoidBillReport.class.getSimpleName();

    @BindView(R.id.tv_voidBill_total_IGST_amount)     TextView tv_voidBill_total_IGST_amount;
    @BindView(R.id.tv_voidBill_total_CGST_amount)     TextView tv_voidBill_total_CGST_amount;
    @BindView(R.id.tv_voidBill_total_SGST_amount)     TextView tv_voidBill_total_SGST_amount;
    @BindView(R.id.tv_voidBill_total_cess_amount)     TextView tv_voidBill_total_cess_amount;
    @BindView(R.id.tv_voidBill_total_bill_amount)     TextView tv_voidBill_total_bill_amount;
    @BindView(R.id.rv_voidBillReport)                    RecyclerView rv_voidBillReport;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    private ArrayList<VoidBillReportBean> voidBillReportBeanArrayList;


    double totIGSTAmount =0, totCGSTAmount =0, totSGSTAmount =0, totcessAmount =0, totBillAmount =0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.void_bill_report, container, false);

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
            voidBillReportBeanArrayList = bundle.getParcelableArrayList("dataList");
            totIGSTAmount = bundle.getDouble(Constants.TOTALIGSTAMOUNT, 0);
            totCGSTAmount = bundle.getDouble(Constants.TOTALCGSTAMOUNT, 0);
            totSGSTAmount = bundle.getDouble(Constants.TOTALSGSTAMOUNT, 0);
            totcessAmount = bundle.getDouble(Constants.TOTALcessAMOUNT, 0);
            totBillAmount = bundle.getDouble(Constants.TOTALBILLAMOUNT, 0);


            tv_voidBill_total_IGST_amount.setText(String.format("%.2f",totIGSTAmount));
            tv_voidBill_total_CGST_amount.setText(String.format("%.2f",totCGSTAmount));
            tv_voidBill_total_SGST_amount.setText(String.format("%.2f",totSGSTAmount));
            tv_voidBill_total_cess_amount.setText(String.format("%.2f",totcessAmount));
            tv_voidBill_total_bill_amount.setText(String.format("%.2f",totBillAmount));


            if (voidBillReportBeanArrayList.size() > 0){
                rv_voidBillReport.setLayoutManager(new LinearLayoutManager(myContext));
                rv_voidBillReport.setAdapter(new VoidBillReportAdapter(myContext, voidBillReportBeanArrayList));
            }
        }
    }

}
