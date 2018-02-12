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
import pos.wepindia.com.retail.view.Reports.Bean.GSTR2UnRegisteredBean;
import pos.wepindia.com.retail.view.Reports.adapters.GSTR2UnRegisteredReportAdapter;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR2UnRegisteredReport extends Fragment {

    @BindView(R.id.tv_gstr2_unregistered_total_taxable)
    TextView tv_gstr2_unregistered_total_taxable;
    @BindView(R.id.tv_gstr2_unregistered_total_igst)
    TextView tv_gstr2_unregistered_total_igst;
    @BindView(R.id.tv_gstr2_unregistered_total_cgst)
    TextView tv_gstr2_unregistered_total_cgst;
    @BindView(R.id.tv_gstr2_unregistered_total_sgst)
    TextView tv_gstr2_unregistered_total_sgst;
    @BindView(R.id.tv_gstr2_unregistered_total_cess)
    TextView tv_gstr2_unregistered_total_cess;
    @BindView(R.id.tv_gstr2_unregistered_total_amount)
    TextView tv_gstr2_unregistered_total_amount;
    @BindView(R.id.rv_gstr2_unregistered)
    RecyclerView rv_gstr2_unregistered;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    private ArrayList<GSTR2UnRegisteredBean> gstr2UnRegisteredBeanArrayList;

    double valtot =0, taxvaltot =0,Itot =0, Ctot =0, Stot =0, Amttot =0,cesstot=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gstr2_unregistered_report, container, false);

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
            gstr2UnRegisteredBeanArrayList = bundle.getParcelableArrayList("dataList");
            taxvaltot = bundle.getDouble("taxvaltot", 0);
            Itot = bundle.getDouble("Itot", 0);
            Ctot = bundle.getDouble("Ctot", 0);
            Stot = bundle.getDouble("Stot", 0);
            Amttot = bundle.getDouble("Amttot", 0);
            cesstot = bundle.getDouble("cesstot", 0);

            tv_gstr2_unregistered_total_taxable.setText(String.format("%.2f",taxvaltot));
            tv_gstr2_unregistered_total_igst.setText(String.format("%.2f",Itot));
            tv_gstr2_unregistered_total_cgst.setText(String.format("%.2f",Ctot));
            tv_gstr2_unregistered_total_sgst.setText(String.format("%.2f",Stot));
            tv_gstr2_unregistered_total_amount.setText(String.format("%.2f",Amttot));
            tv_gstr2_unregistered_total_cess.setText(String.format("%.2f",cesstot));

            if (gstr2UnRegisteredBeanArrayList.size() > 0){
                rv_gstr2_unregistered.setLayoutManager(new LinearLayoutManager(myContext));
                rv_gstr2_unregistered.setAdapter(new GSTR2UnRegisteredReportAdapter(myContext, gstr2UnRegisteredBeanArrayList));
            }
        }
    }
}
