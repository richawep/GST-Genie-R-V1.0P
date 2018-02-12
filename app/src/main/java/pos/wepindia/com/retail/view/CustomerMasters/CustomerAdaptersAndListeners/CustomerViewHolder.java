package pos.wepindia.com.retail.view.CustomerMasters.CustomerAdaptersAndListeners;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.wepbase.model.pojos.Customer;

/**
 * Created by MohanN on 1/2/2018.
 */

public class CustomerViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_sno)            TextView tv_sNo;
    @BindView(R.id.tv_custName)       TextView tv_custName;
    @BindView(R.id.tv_custPhoneNo)    TextView tv_custPhoneNo;
    @BindView(R.id.tv_last_trans)     TextView tv_lastTransac;
    @BindView(R.id.tv_total_trans)    TextView tv_totaltransac;
    @BindView(R.id.tv_credit_amt)     TextView tv_creditAmount;
    @BindView(R.id.tv_loyalty_points) TextView tv_loyalPoints;
    @BindView(R.id.tv_State)          TextView tv_State;
    @BindView(R.id.ll_row_cust)   LinearLayout ll_row_cust;

    public CustomerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Customer customer, int position) {
        if (customer != null) {
            tv_sNo.setText(""+(position+1));
            tv_custName.setText(customer.getStrCustName());
            tv_custPhoneNo.setText(customer.getStrCustContactNumber());
            tv_lastTransac.setText(String.format("%.2f",customer.getdLastTransaction()));
            tv_totaltransac.setText(String.format("%.2f",customer.getdTotalTransaction()));
            tv_creditAmount.setText(String.format("%.2f",customer.getdCreditAmount()));
            tv_loyalPoints.setText(""+customer.getiRewardPoints());
            if(1 == customer.getIsActive())
                tv_State.setText("Active");
            else
                tv_State.setText("Inactive");
        }
        //Picasso.with(imageView.getContext()).load(myObject.getImageUrl()).centerCrop().fit().into(imageView);
    }
}