package pos.wepindia.com.retail.view.PriceAndStock;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.DecimalDigitsInputFilter;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.retail.view.PriceAndStock.AdaptersAndListeners.OnItemSelectListener;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.pojos.ItemMasterBean;

/**
 * Created by MohanN on 1/11/2018.
 */

public class PriceStockEditDialogFragment extends DialogFragment {
    private static final String TAG = PriceStockEditDialogFragment.class.getName();

    ItemMasterBean itemMasterBean;
    MessageDialog msgBox;

    @BindView(R.id.bt_price_stock_edit_dialog_update)     Button btnUpdate;
    @BindView(R.id.bt_price_stock_edit_dialog_clear)      Button btnClear;
    @BindView(R.id.bt_price_stock_edit_dialog_close)      Button btnClose;
    @BindView(R.id.et_price_stock_edit_dialog_item_name)  TextInputEditText edtItemName;
    @BindView(R.id.et_price_stock_edit_dialog_item_barcode)     TextInputEditText edtBarcode;
    @BindView(R.id.et_price_stock_edit_dialog_existing_stock)   TextInputEditText edtExistingStock;
    @BindView(R.id.et_price_stock_edit_dialog_new_stock)        TextInputEditText edtNewStock;
    @BindView(R.id.et_price_stock_edit_dialog_mrp)              TextInputEditText edtMRP;
    @BindView(R.id.et_price_stock_edit_dialog_retail_price)     TextInputEditText edtRetailPrice;
    @BindView(R.id.et_price_stock_edit_dialog_whole_sale_price) TextInputEditText edtWholeSalePrice;

    private OnItemSelectListener onItemSelectListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG_PRICE_STOCK);
    }

    public void intiListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d != null) {
            int width = 600;
            int height = 650;
            d.getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        View rootView = inflater.inflate(R.layout.price_stock_edit_dialog_layout, container,
                false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        //getDialog().setTitle("Add New Customer");
        try {
            msgBox = new MessageDialog(getActivity());
            ButterKnife.bind(this, view);
            //App crash error log
            if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }


            Bundle args = getArguments();
            if (args != null) {
                itemMasterBean = args.getParcelable(ItemMasterBean.ITEM_MASTER_PARCELABLE_KEY);
            } else {
                Logger.w("GetIncidencia", "Arguments expected, but missing");
            }

            applyDecimalValidation();

            // tvDialogTitle.setText(getString(R.string.add_new_customer));
            if (itemMasterBean != null) {
                //tvDialogTitle.setText(getString(R.string.update_customer));
                edtItemName.setText(itemMasterBean.getStrLongName());
                edtBarcode.setText(itemMasterBean.getStrBarcode());
                edtExistingStock.setText("" + itemMasterBean.getDblQty());
                edtMRP.setText("" + itemMasterBean.getDblMRP());
                edtRetailPrice.setText("" + itemMasterBean.getDblRetailPrice());
                edtWholeSalePrice.setText("" + itemMasterBean.getDblWholeSalePrice());

               /* if (itemMasterBean.getDblQty() > 0) {
                    edtExistingStock.setText("" + itemMasterBean.getDblQty());
                }
                if (itemMasterBean.getDblMRP() > 0) {
                    edtMRP.setText("" + itemMasterBean.getDblMRP());
                }
                if (itemMasterBean.getDblRetailPrice() > 0) {
                    edtRetailPrice.setText("" + itemMasterBean.getDblRetailPrice());
                }
                if (itemMasterBean.getDblWholeSalePrice() > 0) {
                    edtWholeSalePrice.setText("" + itemMasterBean.getDblWholeSalePrice());
                }*/
            }

        } catch (Exception ex) {
            Logger.i(TAG, "Unable to init the customer master fragment or add new customer screen." + ex.getMessage());
        }
    }

    @OnClick({R.id.bt_price_stock_edit_dialog_update, R.id.bt_price_stock_edit_dialog_clear, R.id.bt_price_stock_edit_dialog_close})
    protected void onWidgetClick(View view) {
        switch (view.getId()) {
            case R.id.bt_price_stock_edit_dialog_update:
                mUpdateStock();
                break;
            case R.id.bt_price_stock_edit_dialog_clear:
                mClear();
                break;
            case R.id.bt_price_stock_edit_dialog_close:
                dismiss();
                break;
            default:
                break;
        }
    }

    public void mUpdateStock() {
        if (itemMasterBean == null) {
            msgBox.Show("Warning", "Please fill all necessary fields and try again later.");
            return;
        }
        if(ValidateCompulsoryField())
        {
            String strExistingStock = edtExistingStock.getText().toString();
            String strNewStock = edtNewStock.getText().toString().equals("")?"0.00":edtNewStock.getText().toString().trim();
            String strMRP = edtMRP.getText().toString();
            String strRetailPrice = edtRetailPrice.getText().toString();
            String strWholeSale = edtWholeSalePrice.getText().toString();

            double newStock = Double.parseDouble(String.format("%.2f", Double.parseDouble(strNewStock)));

            double retailprice = Double.parseDouble(String.format("%.2f", Double.parseDouble(strRetailPrice)));
            double mrp = strMRP.equals("") ? retailprice : Double.parseDouble(String.format("%.2f", Double.parseDouble(strMRP)));
            double wholeSalePrice = strWholeSale.equals("") ? retailprice : Double.parseDouble(String.format("%.2f", Double.parseDouble(strWholeSale)));

            UpdateItemStock(itemMasterBean.get_id(),
                    (Double.parseDouble(strExistingStock) + newStock),
                    mrp,
                    retailprice,
                    wholeSalePrice);

            edtNewStock.setText("0");
            edtExistingStock.setText(String.format("%.2f", (Double.parseDouble(strExistingStock) + newStock)));
            edtMRP.setText(""+mrp);
            edtRetailPrice.setText(""+retailprice);
            edtWholeSalePrice.setText(""+wholeSalePrice);

        }

    }

    private void UpdateItemStock(int MenuCode, double NewStock, double mrp, double retailPrice, double wholeSalePrice) {
        long lRowId = -1;
        lRowId = HomeActivity.dbHandler.updateItemStock(MenuCode, NewStock, mrp, retailPrice, wholeSalePrice);
        if (lRowId > -1) {
            Toast.makeText(getActivity(), "Updated Successfully", Toast.LENGTH_LONG).show();
            Logger.d("StockUpdate", "Row Id:" + String.valueOf(lRowId));
            onItemSelectListener.onPriceStockUpdateSuccess();
        } else {
            Logger.d("StockUpdate", "Unable to update the stock value. ItemCode : " + MenuCode);
        }
    }

    private boolean mValidateEdittextFields(TextInputEditText edtText) {
        boolean bResult = false;
        if (edtText != null && !edtText.getText().toString().isEmpty()) {
            bResult = true;
        }
        return bResult;
    }

    private void mClear() {
        edtItemName.setText("");
        edtBarcode.setText("");
        edtExistingStock.setText("");
        edtNewStock.setText("");
        edtWholeSalePrice.setText("");
        edtMRP.setText("");
        edtRetailPrice.setText("");
    }

    private boolean ValidateCompulsoryField()
    {
        boolean flag  = false;


        if(edtRetailPrice.getText().toString().equals(""))
        {
            msgBox.Show("Mandory Information", getString(R.string.itemRetailPriceMessage));
            return flag;
        }
        double retailPrice = Double.parseDouble(String.format("%.2f",Double.parseDouble(edtRetailPrice.getText().toString())));
        if(!edtMRP.getText().toString().equals(""))
        {
            double mrp = Double.parseDouble(edtMRP.getText().toString());
            if(retailPrice > mrp)
            {
                msgBox.Show("Inconsistent Data", getString(R.string.retailPriceGreaterThanMRPError));
                return flag;
            }
        }
        if(!edtWholeSalePrice.getText().toString().equals(""))
        {
            double wholesalePrice = Double.parseDouble(edtWholeSalePrice.getText().toString());
            if(wholesalePrice > retailPrice)
            {
                msgBox.Show("Inconsistent Data", getString(R.string.wholePriceGeaterThanRetailError));
                return flag;
            }
        }
        flag = true;
        return flag;
    }

    void applyDecimalValidation()
    {
        edtMRP.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(6, 2)});
        edtRetailPrice.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(6, 2)});
        edtWholeSalePrice.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(6, 2)});
        edtNewStock.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(6, 2)});
        //edtExistingStock.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(7, 2)});

    }
}
