package pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners;

/**
 * Created by MohanN on 1/19/2018.
 */

public interface OnHoldResumeBillListener {
    void onClickBillRemove(String strInvoiceNo);
    void onClickBillResume(String strInvoiceNo);
}
