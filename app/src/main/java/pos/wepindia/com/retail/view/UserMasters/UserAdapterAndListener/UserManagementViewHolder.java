package pos.wepindia.com.retail.view.UserMasters.UserAdapterAndListener;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.wepbase.model.pojos.User;

/**
 * Created by MohanN on 1/2/2018.
 */

public class UserManagementViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_um_serial_no_title)     TextView tv_sno;
    @BindView(R.id.tv_um_name)                TextView tv_userName;
    @BindView(R.id.tv_um_role)                TextView tv_roleName;
    @BindView(R.id.tv_um_state)               TextView tv_State;
    @BindView(R.id.ll_user_row)               LinearLayout ll_user_row;

    public UserManagementViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(User user, int position) {
        if (user != null) {
            tv_sno.setText("" + (position+1));
            tv_userName.setText(user.getUserName());
            tv_roleName.setText(HomeActivity.dbHandler.getRoleNameforRoleId(user.getUserRole()));
            if(user.getIsActive()==1)
                tv_State.setText("Active");
            else
                tv_State.setText("InActive");
        }
        //Picasso.with(imageView.getContext()).load(myObject.getImageUrl()).centerCrop().fit().into(imageView);
    }
}