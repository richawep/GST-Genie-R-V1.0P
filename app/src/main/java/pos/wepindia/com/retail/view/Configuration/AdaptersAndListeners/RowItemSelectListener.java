package pos.wepindia.com.retail.view.Configuration.AdaptersAndListeners;

import pos.wepindia.com.wepbase.model.pojos.ConfigBean;

/**
 * Created by MohanN on 12/27/2017.
 */

public interface RowItemSelectListener {
    void onRowDataSelect(ConfigBean configBean);
    void onRowDataDelete(ConfigBean configBean);
}
