package pos.wepindia.com.retail.view.UserMasters.UserAdapterAndListener;

import pos.wepindia.com.wepbase.model.pojos.User;

/**
 * Created by MohanN on 1/2/2018.
 */

public interface OnUserManagementListener {
    void onRowDataSelect(User user);
    void onRowDataDelete(User user);
}
