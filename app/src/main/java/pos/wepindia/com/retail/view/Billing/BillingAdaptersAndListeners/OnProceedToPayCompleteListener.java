package pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners;

import pos.wepindia.com.wepbase.model.pojos.PaymentDetails;

/**
 * Created by Administrator on 22-01-2018.
 */

public interface OnProceedToPayCompleteListener {
    void onProceedToPayComplete(PaymentDetails obj);
}
