package pos.wepindia.com.retail.view.Configuration.AdaptersAndListeners;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.AppConstants;
import pos.wepindia.com.wepbase.model.pojos.ConfigBean;

/**
 * Created by MohanN on 12/26/2017.
 */

public class ConfigListViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_config_cell_row_one)
    TextView tvRowOne;
    @BindView(R.id.tv_config_cell_row_two)
    TextView tvRowTwo;
    @BindView(R.id.tv_config_cell_row_three)
    TextView tvRowThree;
    @BindView(R.id.tv_config_cell_row_four)
    TextView tvRowFour;
    @BindView(R.id.tv_config_cell_row_five)
    TextView tvRowFive;
    @BindView(R.id.tv_config_cell_row_six)
    TextView tvRowSix;
    @BindView(R.id.iv_config_cell_delete)
    ImageView ivDelete;
    @BindView(R.id.ll_config_cell_row)
    LinearLayout llConfigCellRow;

    public ConfigListViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(ConfigBean configBean, int position){
        position++;
        if(configBean != null){
            switch (configBean.getiConfigMode()){
                case AppConstants.DEPARTMENT_CONFIG:
                    tvRowOne.setVisibility(View.VISIBLE);
                    tvRowTwo.setVisibility(View.VISIBLE);
                    tvRowThree.setVisibility(View.VISIBLE);
                    tvRowOne.setText(""+position);
                    tvRowTwo.setText(""+configBean.getlDeptCode());
                    tvRowThree.setText(configBean.getStrDeptName());
                    break;
                case AppConstants.CATEGORY_CONFIG:
                    tvRowOne.setVisibility(View.VISIBLE);
                    tvRowTwo.setVisibility(View.VISIBLE);
                    tvRowThree.setVisibility(View.VISIBLE);
                    tvRowFour.setVisibility(View.VISIBLE);
                    tvRowOne.setText(""+position);
                    tvRowTwo.setText(""+configBean.getlCategCode());
                    tvRowThree.setText(configBean.getStrCategName());
                    tvRowFour.setText(configBean.getStrDeptName());
                    break;
                case AppConstants.PAYMENT_RECEIPT_CONFIG:
                    tvRowOne.setVisibility(View.VISIBLE);
                    tvRowTwo.setVisibility(View.VISIBLE);
                    tvRowOne.setText(""+position);
                    tvRowTwo.setText(""+configBean.getStrDescription());
                    break;
                case AppConstants.DISCOUNT_CONFIG:
                    tvRowOne.setVisibility(View.VISIBLE);
                    tvRowTwo.setVisibility(View.VISIBLE);
                    tvRowThree.setVisibility(View.VISIBLE);
                    tvRowOne.setText(""+position);
                    tvRowTwo.setText(configBean.getStrDescription());
                    tvRowThree.setText(""+configBean.getDblDiscount());
                    break;
                case AppConstants.COUPON_CONFIG:
                    tvRowOne.setVisibility(View.VISIBLE);
                    tvRowTwo.setVisibility(View.VISIBLE);
                    tvRowThree.setVisibility(View.VISIBLE);
                    tvRowOne.setText(""+position);
                    tvRowTwo.setText(configBean.getStrDescription());
                    tvRowThree.setText(""+configBean.getDblAmount());
                    break;
                case AppConstants.OTHER_CHARGES_CONFIG:
                    tvRowOne.setVisibility(View.VISIBLE);
                    tvRowTwo.setVisibility(View.VISIBLE);
                    tvRowThree.setVisibility(View.VISIBLE);
                    tvRowFour.setVisibility(View.VISIBLE);
                    tvRowOne.setText(""+position);
                    tvRowTwo.setText(configBean.getStrDescription());
                    tvRowThree.setText(""+configBean.getDblAmount());
                    if(configBean.getiChargeable() == 1) {
                        tvRowFour.setText("YES");
                    } else {
                        tvRowFour.setText("NO");
                    }
                    break;
                case AppConstants.BRAND_CONFIG:
                    tvRowOne.setVisibility(View.VISIBLE);
                    tvRowTwo.setVisibility(View.VISIBLE);
                    tvRowThree.setVisibility(View.VISIBLE);
                    tvRowOne.setText(""+position);
                    tvRowTwo.setText(""+configBean.getlBrandCode());
                    tvRowThree.setText(configBean.getStrBrandName());
                    break;
                default:
                    break;
            }
        }
        //Picasso.with(imageView.getContext()).load(myObject.getImageUrl()).centerCrop().fit().into(imageView);
    }
}