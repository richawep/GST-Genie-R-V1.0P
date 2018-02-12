package pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.PackageUtils;
import pos.wepindia.com.wepbase.model.pojos.ItemMasterBean;

/**
 * Created by MohanN on 1/15/2018.
 */

public class BillingDisplayListViewHolder extends RecyclerView.ViewHolder{

    private static final String TAG = BillingItemsListViewHolder.class.getName();

    private Activity mActivity;

    @BindView(R.id.tv_billing_display_adapter_list)
    TextView tvItemName;
    @BindView(R.id.iv_billing_display_adapter_list)
    ImageView imageView;
    @BindView(R.id.ll_billing_display_adapter_list)
    LinearLayout linearLayout;
    private ItemMasterBean itemMasterBean;

    private OnBillingDisplayAdapterList onBillingDisplayAdapterList;

    public BillingDisplayListViewHolder(Activity activity, OnBillingDisplayAdapterList onBillingDisplayAdapterList, View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mActivity = activity;
        this.onBillingDisplayAdapterList = onBillingDisplayAdapterList;
    }

    public void bind(ItemMasterBean itemMasterBeanTemp){
        itemMasterBean = itemMasterBeanTemp;
        tvItemName.setText(itemMasterBean.getStrShortName());
       /* if(itemMasterBean.getStrImageUri() != null) {
            Picasso.with(imageView.getContext()).load(itemMasterBean.getStrImageUri()).centerCrop().fit().into(imageView);
        }*/
        if(itemMasterBean.getStrImageUri() != null && !itemMasterBean.getStrImageUri().isEmpty()){
            String strFilePathFromDB = itemMasterBean.getStrImageUri();
            //Picasso.with(imageView.getContext()).load(strFilePathFromDB).centerCrop().fit().into(imageView);
            try {
                Uri uri = Uri.fromFile(new File(strFilePathFromDB));
                Picasso.with(mActivity)
                        .load(uri)
                        //.resize(400, 400)
                        .placeholder(R.mipmap.ic_image_blank) //this is optional the image to display while the url image is downloading
                        .error(R.mipmap.ic_image_blank)         //this is also optional if some error has occurred in downloading the image this image would be displayed
                        .into(imageView);
            } catch (Exception e) {
                imageView.setImageResource(R.mipmap.ic_image_blank);
            }
        } else {
            try {
                String icon = PackageUtils.getImagePath(null, itemMasterBean.getStrShortName());
                Uri uri = Uri.fromFile(new File(icon));
                Picasso.with(mActivity)
                        .load(uri)
                        //.resize(400, 400)
                        .placeholder(R.mipmap.ic_image_blank) //this is optional the image to display while the url image is downloading
                        .error(R.mipmap.ic_image_blank)         //this is also optional if some error has occurred in downloading the image this image would be displayed
                        .into(imageView);
            } catch (Exception e) {
                imageView.setImageResource(R.mipmap.ic_image_blank);
            }
        }
    }

    @OnClick({R.id.ll_billing_display_adapter_list})
    public void mOnClickEvent(View view){
        switch (view.getId()){
            case R.id.ll_billing_display_adapter_list:
                if (onBillingDisplayAdapterList != null) {
                    onBillingDisplayAdapterList.onDisplayAdapterSelectItem(itemMasterBean);
                }
                break;
            default:
                break;
        }
    }

}