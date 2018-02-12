package pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners;

import pos.wepindia.com.wepbase.model.pojos.BillItemBean;

/**
 * Created by MohanN on 1/20/2018.
 */

public interface OnPriceQuantityChangeDataApplyListener {
    void onPriceQuantityChangeDataApplySucces(BillItemBean billItemBean);
}
