package pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.wepbase.model.pojos.Department;

/**
 * Created by MohanN on 1/16/2018.
 */

public class BillingDeptListViewHolder extends RecyclerView.ViewHolder{

    private static final String TAG = BillingDeptListViewHolder.class.getName();

    private Activity mActivity;

    @BindView(R.id.tv_billing_display_adapter_list)
    TextView tvItemName;
    @BindView(R.id.iv_billing_display_adapter_list)
    ImageView ivPicture;
    @BindView(R.id.ll_billing_display_adapter_list)
    LinearLayout linearLayout;
    private Department department;

    private OnBillingDeptListAdapter onBillingDeptListAdapter;

    public BillingDeptListViewHolder(Activity activity, OnBillingDeptListAdapter onBillingDeptListAdapter, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mActivity = activity;
        this.onBillingDeptListAdapter = onBillingDeptListAdapter;
    }

    public void bind(Department departmentTemp){
        department = departmentTemp;
        tvItemName.setText(department.getDeptName());
        ivPicture.setVisibility(View.GONE);
        linearLayout.setBackgroundResource(R.drawable.round_shape_draw);
    }

    @OnClick({R.id.ll_billing_display_adapter_list})
    public void mOnClickEvent(View view){
        switch (view.getId()){
            case R.id.ll_billing_display_adapter_list:
                if (onBillingDeptListAdapter != null) {
                    onBillingDeptListAdapter.onDeptDataSelect(department);
                }
                break;
            default:
                break;
        }
    }

}