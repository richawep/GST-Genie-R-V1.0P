package pos.wepindia.com.retail.view.Billing;

import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.OnBillHoldResumeListener;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.OnHoldResumeBillListener;
import pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners.ResumeHoldListAdapter;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.HoldResumeBillBean;

import static android.content.ContentValues.TAG;

/**
 * Created by MohanN on 12/15/2017.
 */

public class ResumeBillsListFragment extends DialogFragment implements OnHoldResumeBillListener {

    @BindView(R.id.bt_hold_resume_dialog_cancel)
    Button btCancel;
    @BindView(R.id.rv_hold_resume_dialog_list)
    RecyclerView rvList;

    List<HoldResumeBillBean> holdResumeBillBeanList;

    ResumeHoldListAdapter resumeHoldListAdapter = null;

    OnBillHoldResumeListener onBillHoldResumeListener = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Custom_Dialog_Gray);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();
        if (d!=null){
            int width = 600;
            int height = 600;
            d.getWindow().setLayout(width, height);
            d.setCanceledOnTouchOutside(false);
        }
    }

    public void setListeners(OnBillHoldResumeListener onBillHoldResumeListener){
        this.onBillHoldResumeListener = onBillHoldResumeListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.billing_resume_list_dialog_fragment, container,
                false);
        //getDialog().setTitle("Add Item Master");
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        //getDialog().setTitle("Add New Customer");
        //App crash error log
        try {
            if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }
            ButterKnife.bind(this, view);
            holdResumeBillBeanList = new ArrayList<HoldResumeBillBean>();
            mFetchHoldResumeDbData();
        } catch (Exception ex) {
            Log.i(TAG, "Unable init hold resume bill fragment. " + ex.getMessage());
        }
    }

    @OnClick({R.id.bt_hold_resume_dialog_cancel})
    protected void onWidgetClick(View view){
        switch(view.getId()){
            case R.id.bt_hold_resume_dialog_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    private void mFetchHoldResumeDbData(){
        Cursor crsrHoldresume = null;
        try {
            crsrHoldresume = HomeActivity.dbHandler.getHoldResumeBillData();
            int iCount = 0;
            if(crsrHoldresume != null && crsrHoldresume.getCount() > 0 && crsrHoldresume.moveToFirst()){
                holdResumeBillBeanList.clear();
                do{
                    HoldResumeBillBean holdResumeBillBean = new HoldResumeBillBean();
                    holdResumeBillBean.set_id(crsrHoldresume.getInt(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_id)));
                    holdResumeBillBean.setStrGSTIN(crsrHoldresume.getString(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_GSTIN)));
                    holdResumeBillBean.setStrCustName(crsrHoldresume.getString(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_CustName)));
                    holdResumeBillBean.setStrCustStateCode(crsrHoldresume.getString(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_CustStateCode)));
                    holdResumeBillBean.setStrCustPhone(crsrHoldresume.getString(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_CustPhoneNo)));
                    holdResumeBillBean.setStrInvoiceNo(crsrHoldresume.getString(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_InvoiceNo)));
                    holdResumeBillBean.setStrInvoiceDate(crsrHoldresume.getString(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_InvoiceDate)));
                    holdResumeBillBean.setStrSupplyType(crsrHoldresume.getString(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_SupplyType)));
                    holdResumeBillBean.setStrBussinessType(crsrHoldresume.getString(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_BusinessType)));
                    holdResumeBillBean.setStrTaxationType(crsrHoldresume.getString(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_TaxationType)));
                    holdResumeBillBean.setStrHSNCode(crsrHoldresume.getString(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_HSNCode)));
                    holdResumeBillBean.setiItemID(crsrHoldresume.getInt(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_ITEM_ID)));
                    holdResumeBillBean.setStrItemName(crsrHoldresume.getString(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_ItemName)));
                    holdResumeBillBean.setDblQty(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_Quantity)));
                    holdResumeBillBean.setStrUOM(crsrHoldresume.getString(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_UOM)));
                    holdResumeBillBean.setDblOriginalRate(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_OriginalRate)));
                    holdResumeBillBean.setDblValue(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_Value)));
                    holdResumeBillBean.setDblTaxableValue(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_TaxableValue)));
                    holdResumeBillBean.setDblAmount(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_Amount)));
                    holdResumeBillBean.setStrIsReverseTaxEnabled(crsrHoldresume.getString(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_IsReverseTaxEnabled)));
                    holdResumeBillBean.setDblIGSTRate(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_IGSTRate)));
                    holdResumeBillBean.setDblIGSTAmount(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_IGSTAmount)));
                    holdResumeBillBean.setDblCGSTRate(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_CGSTRate)));
                    holdResumeBillBean.setDblCGSTAmount(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_CGSTAmount)));
                    holdResumeBillBean.setDblSGSTRate(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_SGSTRate)));
                    holdResumeBillBean.setDblSGSTAmount(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_SGSTAmount)));
                    holdResumeBillBean.setDblCessRate(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_cessRate)));
                    holdResumeBillBean.setDblCessAmount(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_cessAmount)));
                    holdResumeBillBean.setDblSubTotal(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_SubTotal)));
                    holdResumeBillBean.setDblBillAmount(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_BillAmount)));
                    holdResumeBillBean.setStrBillingMode(crsrHoldresume.getString(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_BillingMode)));
//                    holdResumeBillBean.setDblModifierAmount(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_ModifierAmount)));
                    holdResumeBillBean.setiTaxType(crsrHoldresume.getInt(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_TaxType)));
                    holdResumeBillBean.setiCategoryCode(crsrHoldresume.getInt(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_CategoryCode)));
                    holdResumeBillBean.setiDepartmentCode(crsrHoldresume.getInt(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)));
                    holdResumeBillBean.setiBrandCode(crsrHoldresume.getInt(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_BrandCode)));
                    holdResumeBillBean.setDblDiscountPercent(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_DiscountPercent)));
                    holdResumeBillBean.setDblDiscountAmount(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_DiscountAmount)));
                    //holdResumeBillBean.setDblTaxAmount(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_TaxAmount)));
                    holdResumeBillBean.setiBillStatus(crsrHoldresume.getInt(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_BillStatus)));
                    //holdResumeBillBean.setDblTaxPercent(crsrHoldresume.getDouble(crsrHoldresume.getColumnIndex(DatabaseHandler.KEY_TaxPercent)));
                    if(holdResumeBillBeanList.size() > 0){
                        boolean bStatus = true;
                        for(int i = 0; i < holdResumeBillBeanList.size(); i++){
                            if(holdResumeBillBeanList.get(i).getStrInvoiceNo().equals(holdResumeBillBean.getStrInvoiceNo())){
                                bStatus = false;
                                break;
                            }
                        }
                        if(bStatus) {
                            iCount = iCount + 1;
                            holdResumeBillBean.setiSlNo(iCount);
                            holdResumeBillBeanList.add(holdResumeBillBean);
                        }
                    } else {
                        iCount = iCount + 1;
                        holdResumeBillBean.setiSlNo(iCount);
                        holdResumeBillBeanList.add(holdResumeBillBean);
                    }
                }while(crsrHoldresume.moveToNext());
            }
        } catch (Exception e) {
            Logger.i(TAG,"Hold resume bill table fetch error on resume bill list fragment. " +e.getMessage());
        }finally {
            if(crsrHoldresume != null){
                crsrHoldresume.close();
            }
        }
        if(holdResumeBillBeanList.size() > 0){
            rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
            resumeHoldListAdapter = new ResumeHoldListAdapter(this,holdResumeBillBeanList);
            rvList.setAdapter(resumeHoldListAdapter);
        } else {
            Toast.makeText(getActivity(),"No data to display.",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClickBillRemove(String strInvoiceNo) {
        HomeActivity.dbHandler.updateHoldResumeBillTable(strInvoiceNo);
    }

    @Override
    public void onClickBillResume(String strInvoiceNo) {
        if(onBillHoldResumeListener != null) {
            onBillHoldResumeListener.onBillResume(strInvoiceNo);
        }
        HomeActivity.dbHandler.updateHoldResumeBillTable(strInvoiceNo);
        dismiss();
    }
}