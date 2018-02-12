package pos.wepindia.com.retail.view.Reports.adapters.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1DocIssuedBean;

/**
 * Created by SachinV on 29-01-2018.
 */

public class GSTR1DocIssuedReportViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_gstr1_dosc_issued_row_sno)                TextView tv_gstr1_dosc_issued_row_sno;
    @BindView(R.id.tv_gstr1_dosc_issued_row_nature_of_doc)      TextView tv_gstr1_dosc_issued_row_nature_of_doc;
    @BindView(R.id.tv_gstr1_dosc_issued_row_from)               TextView tv_gstr1_dosc_issued_row_from;
    @BindView(R.id.tv_gstr1_dosc_issued_row_to)                 TextView tv_gstr1_dosc_issued_row_to;
    @BindView(R.id.tv_gstr1_dosc_issued_row_total_number)       TextView tv_gstr1_dosc_issued_row_total_number;
    @BindView(R.id.tv_gstr1_dosc_issued_row_cancelled)          TextView tv_gstr1_dosc_issued_row_cancelled;
    @BindView(R.id.tv_gstr1_dosc_issued_row_net_issued)         TextView tv_gstr1_dosc_issued_row_net_issued;

    public GSTR1DocIssuedReportViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(GSTR1DocIssuedBean gstr1DocIssuedBean, int position) {

        if (gstr1DocIssuedBean != null) {
            tv_gstr1_dosc_issued_row_sno.setText(String.valueOf(position+1));
            tv_gstr1_dosc_issued_row_nature_of_doc.setText(gstr1DocIssuedBean.getNatureOfDoc());
            tv_gstr1_dosc_issued_row_from.setText(gstr1DocIssuedBean.getFrom());
            tv_gstr1_dosc_issued_row_to.setText(gstr1DocIssuedBean.getTo());
            tv_gstr1_dosc_issued_row_total_number.setText(String.valueOf(gstr1DocIssuedBean.getTotalNumber()));
            tv_gstr1_dosc_issued_row_cancelled.setText(String.valueOf(gstr1DocIssuedBean.getCancelled()));
            tv_gstr1_dosc_issued_row_net_issued.setText(String.valueOf(gstr1DocIssuedBean.getNetIssued()));
        }
    }
}
