package pos.wepindia.com.retail.view.UserMasters.UserAdapterAndListener;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.Constants;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.wepbase.model.pojos.AddRoleBean;

/**
 * Created by MohanN on 12/18/2017.
 */

public class AddRoleListViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.tv_row_item_one) TextView tvItemOne;
    @BindView(R.id.cb_row_item)     CheckBox cbRowItem;
    private int iMode;
    private AddRoleBean addRoleBean;

    public AddRoleListViewHolder(View itemView, int mode) {
        super(itemView);
        this.iMode = mode;
        ButterKnife.bind(this, itemView);
    }

    public void bind(AddRoleBean addRoleBean) {
        tvItemOne.setText(addRoleBean.getStrData());
        switch (iMode) {
            case Constants.ROLE_LIST:
                cbRowItem.setVisibility(View.GONE);
                break;
            case Constants.ACCESS_PERMISSION_FOR_ROLE:
                cbRowItem.setVisibility(View.VISIBLE);
                if (addRoleBean.isStatus()) {
                    cbRowItem.setChecked(true);
                } else {
                    cbRowItem.setChecked(false);
                }
                break;
            default:
                break;
        }
    }

    @OnClick({R.id.cb_row_item})
    protected void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_row_item:
                switch (iMode) {
                    case Constants.ACCESS_PERMISSION_FOR_ROLE:
                       /* list.get(position).setStatus(cbRowItem.isChecked());
                        cbRowItem.setChecked(list.get(position).isStatus());
                        Toast.makeText(
                                v.getContext(),
                                "Clicked on Checkbox: " + cbRowItem.getText() + " is "
                                        + cbRowItem.isChecked(), Toast.LENGTH_LONG).show();*/
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }
}
