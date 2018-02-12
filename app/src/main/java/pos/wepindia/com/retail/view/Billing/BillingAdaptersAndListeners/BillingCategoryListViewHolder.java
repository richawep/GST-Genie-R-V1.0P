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
import pos.wepindia.com.wepbase.model.pojos.Category;

/**
 * Created by MohanN on 1/16/2018.
 */

public class BillingCategoryListViewHolder extends RecyclerView.ViewHolder{

    private static final String TAG = BillingCategoryListViewHolder.class.getName();

    private Activity mActivity;

    @BindView(R.id.tv_billing_display_adapter_list)
    TextView tvItemName;
    @BindView(R.id.iv_billing_display_adapter_list)
    ImageView ivPicture;
    @BindView(R.id.ll_billing_display_adapter_list)
    LinearLayout linearLayout;
    private Category category;

    private OnBillingCategoryListAdapterListener onBillingCategoryListAdapterListener;

    public BillingCategoryListViewHolder(Activity activity, OnBillingCategoryListAdapterListener onBillingCategoryListAdapterListener, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mActivity = activity;
        this.onBillingCategoryListAdapterListener = onBillingCategoryListAdapterListener;
    }

    public void bind(Category categoryTemp){
        category = categoryTemp;
        tvItemName.setText(category.getStrCategName());
        ivPicture.setVisibility(View.GONE);
        linearLayout.setBackgroundResource(R.drawable.round_shape_draw);
    }

    @OnClick({R.id.ll_billing_display_adapter_list})
    public void mOnClickEvent(View view){
        switch (view.getId()){
            case R.id.ll_billing_display_adapter_list:
                if (onBillingCategoryListAdapterListener != null) {
                    onBillingCategoryListAdapterListener.onCategoryItemSelected(category);
                }
                break;
            default:
                break;
        }
    }

}