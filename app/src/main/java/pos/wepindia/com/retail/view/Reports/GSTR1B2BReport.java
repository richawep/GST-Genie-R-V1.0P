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
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1B2BBean;
import pos.wepindia.com.retail.view.Reports.adapters.GSTR1B2BReportAdapter;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1B2BReport extends Fragment {

    @BindView(R.id.rv_gstr1_b2b)
    RecyclerView rv_gstr1_b2b;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    private ArrayList<GSTR1B2BBean> gstr1B2BBeanArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gstr1_b2b_report, container, false);

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
            gstr1B2BBeanArrayList = bundle.getParcelableArrayList("dataList");

            if (gstr1B2BBeanArrayList.size() > 0){
                rv_gstr1_b2b.setLayoutManager(new LinearLayoutManager(myContext));
                rv_gstr1_b2b.setAdapter(new GSTR1B2BReportAdapter(myContext, gstr1B2BBeanArrayList));
            }
        }
    }
}
