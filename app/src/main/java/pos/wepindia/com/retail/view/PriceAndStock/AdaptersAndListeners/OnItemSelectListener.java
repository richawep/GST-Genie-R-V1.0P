package pos.wepindia.com.retail.view.PriceAndStock.AdaptersAndListeners;

import pos.wepindia.com.wepbase.model.pojos.ItemMasterBean;

/**
 * Created by MohanN on 1/11/2018.
 */

public interface OnItemSelectListener {
    void onItemClick(ItemMasterBean itemMasterBean);
    void onPriceStockUpdateSuccess();
}
