package pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners;

import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.DecimalDigitsInputFilter;
import pos.wepindia.com.wepbase.model.pojos.HoldResumeBillBean;

/**
 * Created by MohanN on 1/18/2018.
 */

public class ResumeHoldListViewHolder extends RecyclerView.ViewHolder{
    private static final String TAG = ResumeHoldListViewHolder.class.getName();

    @BindView(R.id.tv_resume_hold_list_dialog_row_slno)
    TextView tvSlNo;
    @BindView(R.id.tv_resume_hold_list_dialog_row_bill_number)
    TextView tvBillNumber;
    @BindView(R.id.tv_resume_hold_list_dialog_row_date)
    TextView tvDate;
    @BindView(R.id.tv_resume_hold_list_dialog_row_amount)
    TextView tvAmount;
    @BindView(R.id.bt_resume_hold_list_dialog_row_resume)
    Button btnResume;
    @BindView(R.id.iv_resume_hold_list_dialog_row_delete)
    ImageView ivRemove;
    @BindView(R.id.ll_resume_hold_list_dialog_row)
    LinearLayout llResumeHoldList;

    public ResumeHoldListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        tvAmount.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(7,2)});
    }

    public void bind(HoldResumeBillBean holdResumeBillBean){
        tvSlNo.setText(""+holdResumeBillBean.getiSlNo());
        tvBillNumber.setText(""+holdResumeBillBean.getStrInvoiceNo());
        tvDate.setText(holdResumeBillBean.getStrInvoiceDate());
        tvAmount.setText(String.format("%.2f",holdResumeBillBean.getDblBillAmount()));
    }
}
