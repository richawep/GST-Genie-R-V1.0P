package pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners;

import java.util.Map;

/**
 * Created by Administrator on 05-02-2018.
 */

public interface AEPSCompleteListener {
    public void onAEPSCompleteListener(int resultCode, Map<String, String> map);
}
