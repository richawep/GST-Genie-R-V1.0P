package pos.wepindia.com.retail.view.Reports;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.DateTime;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.ReportExportToCSVHelper;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.retail.view.Reports.Bean.CategoryWiseReportBean;
import pos.wepindia.com.retail.view.Reports.Bean.DepartmentWiseReportBean;
import pos.wepindia.com.retail.view.Reports.Bean.FastSellingItemwiseReportBean;
import pos.wepindia.com.retail.view.Reports.Bean.ItemWiseReportBean;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;

/**
 * Created by SachinV on 23-01-2018.
 */

public class InventoryReport extends Fragment {

    private static final String TAG = InventoryReport.class.getSimpleName();

    @BindView(R.id.spnrReportNameSelection_inventory)
    Spinner spnrReportNameSelection_inventory;
    @BindView(R.id.btn_ReportDateFrom_inventory)
    Button btn_ReportDateFrom_inventory;
    @BindView(R.id.btn_ReportDateTo_inventory)
    Button btn_ReportDateTo_inventory;
    @BindView(R.id.etReportDateStart_inventory)
    EditText etReportDateStart_inventory;
    @BindView(R.id.etReportDateEnd_inventory)
    EditText etReportDateEnd_inventory;
    @BindView(R.id.btn_ReportPrint_inventory)
    Button btn_ReportPrint_inventory;
    @BindView(R.id.btn_ReportExport_inventory)
    Button btn_ReportExport_inventory;
    @BindView(R.id.btn_ReportView_inventory)
    Button btn_ReportView_inventory;
    @BindView(R.id.sales_report_container_inventory)
    FrameLayout sales_report_container_inventory;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    private DateTime objDate;
    private String strDate = "";
    private Date startDate_date, endDate_date;
    private ArrayList<CategoryWiseReportBean> categoryWiseReportBeanArrayList;
    private ArrayList<DepartmentWiseReportBean> departmentWiseReportBeanArrayList;
    private ArrayList<FastSellingItemwiseReportBean> fastSellingItemwiseReportBeanArrayList;
    private ArrayList<ItemWiseReportBean> itemWiseReportBeanArrayList;
    //List used for generating .csv file
    private List<List<String>> listCSVData;

    private PopulateSelectedReportData populateSelectedReportData = null;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.inventory_report_fragment, container, false);

        myContext = getContext();
        MsgBox = new MessageDialog(myContext);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            //App crash error log
            if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }

            Date d = new Date();
            CharSequence currentdate = DateFormat.format("yyyy-MM-dd", d.getTime());
            objDate = new DateTime(currentdate.toString());
            spnrReportNameSelection_inventory.setOnItemSelectedListener(spOnItemSelectListener);
            listCSVData = new ArrayList<List<String>>();
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    @OnClick({R.id.btn_ReportDateFrom_inventory, R.id.btn_ReportDateTo_inventory,
            R.id.btn_ReportPrint_inventory, R.id.btn_ReportExport_inventory, R.id.btn_ReportView_inventory})
    protected void mBtnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ReportDateFrom_inventory:
                From();
                break;
            case R.id.btn_ReportDateTo_inventory:
                To();
                break;
            case R.id.btn_ReportPrint_inventory: //PrintReport();
                break;
            case R.id.btn_ReportExport_inventory:
                mExportReport();
                break;
            case R.id.btn_ReportView_inventory:
                ViewReport();
                break;
        }
    }

    public void From() {
        DateSelection(1);
    }

    public void To() {
        if (!etReportDateStart_inventory.getText().toString().equalsIgnoreCase("")) {
            DateSelection(2);

        } else {
            MsgBox.Show("Warning", "Please select report FROM date");
        }
    }

    AdapterView.OnItemSelectedListener spOnItemSelectListener = new AdapterView.OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> adapter, View v, int position,
                                   long id) {
            // TODO Auto-generated method stub
            ResetAll();
        }

        public void onNothingSelected(AdapterView<?> adapter) {
            // TODO Auto-generated method stub
        }
    };

    private int getSpinnerListIndex() {
        List<String> SalesReport = Arrays.asList(getResources().getStringArray(R.array.inventory_report));

        for (String muom : SalesReport) {
            if (muom.equals(spnrReportNameSelection_inventory.getSelectedItem().toString()))
                return SalesReport.indexOf(muom);
        }
        return 0;
    }

    private void DateSelection(final int DateType) {        // StartDate: DateType = 1 EndDate: DateType = 2
        try {
            AlertDialog.Builder dlgReportDate = new AlertDialog.Builder(myContext);
            final DatePicker dateReportDate = new DatePicker(myContext);

            // Initialize date picker value to business date
            dateReportDate.updateDate(objDate.getYear(), objDate.getMonth(), objDate.getDay());
            String strMessage = "";
            if (DateType == 1) {
                strMessage = "Select report Start date";
            } else {
                strMessage = "Select report End date";
            }

            dlgReportDate
                    .setIcon(R.mipmap.ic_company_logo)
                    .setTitle("Date Selection")
                    .setMessage(strMessage)
                    .setView(dateReportDate)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            // richa date format change

                            //strDate = String.valueOf(dateReportDate.getYear()) + "-";
                            if (dateReportDate.getDayOfMonth() < 10) {
                                strDate = "0" + String.valueOf(dateReportDate.getDayOfMonth()) + "-";
                            } else {
                                strDate = String.valueOf(dateReportDate.getDayOfMonth()) + "-";
                            }
                            if (dateReportDate.getMonth() < 9) {
                                strDate += "0" + String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            } else {
                                strDate += String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            }

                            strDate += String.valueOf(dateReportDate.getYear());

                            if (DateType == 1) {
                                etReportDateStart_inventory.setText(strDate);
                                startDate_date = new Date(dateReportDate.getYear() - 1900, dateReportDate.getMonth(), dateReportDate.getDayOfMonth());
                            } else {
                                etReportDateEnd_inventory.setText(strDate);
                                endDate_date = new Date(dateReportDate.getYear() - 1900, dateReportDate.getMonth(), dateReportDate.getDayOfMonth());

                            }
                            Log.d("ReportDate", "Selected Date:" + strDate);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void ViewReport() {
        String txtStartDate = etReportDateStart_inventory.getText().toString();
        String txtEndDate = etReportDateEnd_inventory.getText().toString();
        if (txtStartDate.equalsIgnoreCase("") || txtEndDate.equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please select From & To Date");
        } else if (startDate_date.getTime() > endDate_date.getTime()) {
            MsgBox.Show("Warning", "'From Date' cannot be greater than 'To Date' ");
        } else {
            try {
                populateSelectedReportData = new PopulateSelectedReportData();
                populateSelectedReportData.execute();
            } catch (Exception e) {
                e.printStackTrace();
                MsgBox.Show("Error", "Some error occured while displaying report");
            }
        }
    }

    private void mExportReport() {
        if (!etReportDateStart_inventory.getText().toString().isEmpty() && !etReportDateEnd_inventory.getText().toString().isEmpty()) {
            if (listCSVData.size() >= 2) {
                ReportExportToCSVHelper reportExportToCSVHelper = ReportExportToCSVHelper.getInstance();
                reportExportToCSVHelper.mGenerateCSVFile(getActivity(), spnrReportNameSelection_inventory.getSelectedItem().toString(), etReportDateStart_inventory.getText().toString(),
                        etReportDateEnd_inventory.getText().toString(), listCSVData);
            }
        } else {
            MsgBox.Show("Warning", "Please select date and try again.");
        }
    }

    private class PopulateSelectedReportData extends AsyncTask<Void, String, String> {
        //CategoryWiseReport
        double totCategoryWiseReportDisc = 0, totCategoryWiseReportIGSTTax = 0, totCategoryWiseReportSales = 0,
                totCategoryWiseReportService = 0, totCategoryWiseReportAmt = 0, totCategoryWiseReportCess = 0;
        //DepartmentWiseReport
        double totDepartmentWiseReportDisc = 0, totDepartmentWiseReportIGSTTax = 0, totDepartmentWiseReportSales = 0,
                totDepartmentWiseReportService = 0, totDepartmentWiseReportAmt = 0, totDepartmentWiseReportCess = 0;

        Bundle bundle;
        FragmentTransaction fragmentTransaction;
        Cursor Report = null;
        List<String> listStrData = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Report = null;
            progressDialog = new ProgressDialog(myContext);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Its loading....");
            progressDialog.setTitle(" Reports ");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                switch (getSpinnerListIndex()) {
                    case 0: // Category Wise Report
                        categoryWiseReportBeanArrayList = new ArrayList<>();

                        Report = HomeActivity.dbHandler.getitems_outward_details_withCateg(
                                String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
                        CategoryWiseReportBean categoryWiseReportBean;

                        if (Report.moveToFirst()) {
                            boolean isCategExists = false;

                            do {
                                for (int iPosition = 0; iPosition < categoryWiseReportBeanArrayList.size(); iPosition++) {

                                    if (categoryWiseReportBeanArrayList.get(iPosition) != null) {

                                        if (categoryWiseReportBeanArrayList.get(iPosition).getCategCode() == Report.getInt(Report
                                                .getColumnIndex(DatabaseHandler.KEY_CategoryCode))) {

                                            // Total Items
                                            categoryWiseReportBeanArrayList.get(iPosition).setTotalItems(categoryWiseReportBeanArrayList.get(iPosition).getTotalItems()
                                                    + Report.getInt(Report.getColumnIndex(DatabaseHandler.KEY_Quantity)));

                                            // Discount
                                            categoryWiseReportBeanArrayList.get(iPosition).setDiscount(categoryWiseReportBeanArrayList.get(iPosition).getDiscount()
                                                    + Report.getInt(Report.getColumnIndex(DatabaseHandler.KEY_DiscountAmount)));

                                            // IGST Tax
                                            categoryWiseReportBeanArrayList.get(iPosition).setIGSTAmount(categoryWiseReportBeanArrayList.get(iPosition).getIGSTAmount()
                                                    + Report.getInt(Report.getColumnIndex(DatabaseHandler.KEY_IGSTAmount)));

                                            // Sales Tax
                                            categoryWiseReportBeanArrayList.get(iPosition).setCGSTAmount(categoryWiseReportBeanArrayList.get(iPosition).getCGSTAmount()
                                                    + Report.getInt(Report.getColumnIndex(DatabaseHandler.KEY_CGSTAmount)));

                                            // Service Tax
                                            categoryWiseReportBeanArrayList.get(iPosition).setSGSTAmount(categoryWiseReportBeanArrayList.get(iPosition).getSGSTAmount()
                                                    + Report.getInt(Report.getColumnIndex(DatabaseHandler.KEY_SGSTAmount)));

                                            // CESS Tax
                                            categoryWiseReportBeanArrayList.get(iPosition).setCESSAmount(categoryWiseReportBeanArrayList.get(iPosition).getCESSAmount()
                                                    + Report.getInt(Report.getColumnIndex(DatabaseHandler.KEY_cessAmount)));

                                            // Amount
                                            double taxVal = Double.parseDouble(Report.getString(Report.getColumnIndex(DatabaseHandler.KEY_TaxableValue))); // discount already applied
                                            double amt = categoryWiseReportBeanArrayList.get(iPosition).getTaxableValue();
                                            amt += taxVal;

                                            isCategExists = true;
                                            break;
                                        }
                                    }
                                }

                                if (isCategExists == false) {

                                    categoryWiseReportBean = new CategoryWiseReportBean();

                                    categoryWiseReportBean.setCategCode(Report.getInt(Report.getColumnIndex(DatabaseHandler.KEY_CategoryCode)));

                                    Cursor categ = HomeActivity.dbHandler.getCategory(categoryWiseReportBean.getCategCode());
                                    if (categ.moveToFirst())
                                        categoryWiseReportBean.setName(categ.getString(categ.getColumnIndex(DatabaseHandler.KEY_CategoryName)));
                                    else
                                        categoryWiseReportBean.setName("");
                                    categoryWiseReportBean.setTotalItems(Report.getInt(Report.getColumnIndex(DatabaseHandler.KEY_Quantity)));
                                    categoryWiseReportBean.setDiscount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_DiscountAmount)));
                                    categoryWiseReportBean.setIGSTAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_IGSTAmount)));
                                    categoryWiseReportBean.setCGSTAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_CGSTAmount)));
                                    categoryWiseReportBean.setSGSTAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_SGSTAmount)));
                                    categoryWiseReportBean.setCESSAmount(Report.getDouble(Report.getColumnIndex("cessAmount")));
                                    categoryWiseReportBean.setTaxableValue(Report.getDouble(Report.getColumnIndex("TaxableValue")));

                                    categoryWiseReportBeanArrayList.add(categoryWiseReportBean);
                                }

                                isCategExists = false;

                            } while (Report.moveToNext());

                            listCSVData.clear();
                            listStrData = new ArrayList<String>();
                            listStrData.add("Categ.Code");
                            listStrData.add("Categ Name");
                            listStrData.add("Total Items");
                            listStrData.add("Discount");
                            listStrData.add("IGST Amount");
                            listStrData.add("CGST Amount");
                            listStrData.add("UTGST/SGST Amount");
                            listStrData.add("cess Amount");
                            listStrData.add("Taxable Value");
                            listCSVData.add(listStrData);

                            for (int i = 0; i < categoryWiseReportBeanArrayList.size(); i++) {
                                totCategoryWiseReportDisc += categoryWiseReportBeanArrayList.get(i).getDiscount();
                                totCategoryWiseReportIGSTTax += categoryWiseReportBeanArrayList.get(i).getIGSTAmount();
                                totCategoryWiseReportSales += categoryWiseReportBeanArrayList.get(i).getCGSTAmount();
                                totCategoryWiseReportService += categoryWiseReportBeanArrayList.get(i).getSGSTAmount();
                                totCategoryWiseReportAmt += categoryWiseReportBeanArrayList.get(i).getTaxableValue();
                                totCategoryWiseReportCess += categoryWiseReportBeanArrayList.get(i).getCESSAmount();
                                listStrData = new ArrayList<String>();
                                listStrData.add(String.valueOf(categoryWiseReportBeanArrayList.get(i).getCategCode()));
                                listStrData.add(categoryWiseReportBeanArrayList.get(i).getName());
                                listStrData.add(String.valueOf(categoryWiseReportBeanArrayList.get(i).getTotalItems()));
                                listStrData.add(String.valueOf(categoryWiseReportBeanArrayList.get(i).getDiscount()));
                                listStrData.add(String.valueOf(categoryWiseReportBeanArrayList.get(i).getIGSTAmount()));
                                listStrData.add(String.valueOf(categoryWiseReportBeanArrayList.get(i).getCGSTAmount()));
                                listStrData.add(String.valueOf(categoryWiseReportBeanArrayList.get(i).getSGSTAmount()));
                                listStrData.add(String.valueOf(categoryWiseReportBeanArrayList.get(i).getCESSAmount()));
                                listStrData.add(String.valueOf(categoryWiseReportBeanArrayList.get(i).getTaxableValue()));
                                listCSVData.add(listStrData);
                            }
                            listStrData = new ArrayList<String>();
                            listStrData.add("Total");
                            listStrData.add("");
                            listStrData.add("");
                            listStrData.add(String.valueOf(totCategoryWiseReportDisc));
                            listStrData.add(String.valueOf(totCategoryWiseReportIGSTTax));
                            listStrData.add(String.valueOf(totCategoryWiseReportSales));
                            listStrData.add(String.valueOf(totCategoryWiseReportService));
                            listStrData.add(String.valueOf(totCategoryWiseReportCess));
                            listStrData.add(String.valueOf(totCategoryWiseReportAmt));
                            listCSVData.add(listStrData);
                        } else {
                            listCSVData.clear();
                            return "No transaction has been done";
                        }
                        break;
                    case 1: // Department Wise report
                        departmentWiseReportBeanArrayList = new ArrayList<>();

                        Report = HomeActivity.dbHandler.getitems_outward_details_withDept(
                                String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));

                        DepartmentWiseReportBean departmentWiseReportBean;
                        if (Report.moveToFirst()) {
                            boolean isDeptExists = false;
                            do {
                                for (int iPosition = 0; iPosition < departmentWiseReportBeanArrayList.size(); iPosition++) {

                                    if (departmentWiseReportBeanArrayList.get(iPosition) != null) {

                                        if (departmentWiseReportBeanArrayList.get(iPosition).getDeptCode() == Report.getInt(Report.getColumnIndex("DepartmentCode"))) {

                                            // Total Items
                                            departmentWiseReportBeanArrayList.get(iPosition).setTotalItems(departmentWiseReportBeanArrayList.get(iPosition).getTotalItems()
                                                    + Report.getInt(Report.getColumnIndex("Quantity")));

                                            // Discount
                                            departmentWiseReportBeanArrayList.get(iPosition).setDiscount(departmentWiseReportBeanArrayList.get(iPosition).getDiscount()
                                                    + Report.getInt(Report.getColumnIndex("DiscountAmount")));

                                            // IGST Tax
                                            departmentWiseReportBeanArrayList.get(iPosition).setIGSTAmount(departmentWiseReportBeanArrayList.get(iPosition).getIGSTAmount()
                                                    + Report.getInt(Report.getColumnIndex("IGSTAmount")));

                                            // Sales Tax
                                            departmentWiseReportBeanArrayList.get(iPosition).setCGSTAmount(departmentWiseReportBeanArrayList.get(iPosition).getCGSTAmount()
                                                    + Report.getInt(Report.getColumnIndex("CGSTAmount")));

                                            // Service Tax
                                            departmentWiseReportBeanArrayList.get(iPosition).setSGSTAmount(departmentWiseReportBeanArrayList.get(iPosition).getSGSTAmount()
                                                    + Report.getInt(Report.getColumnIndex("SGSTAmount")));

                                            // CESS Tax
                                            departmentWiseReportBeanArrayList.get(iPosition).setCESSAmount(departmentWiseReportBeanArrayList.get(iPosition).getCESSAmount()
                                                    + Report.getInt(Report.getColumnIndex("cessAmount")));

                                            // Amount
                                            departmentWiseReportBeanArrayList.get(iPosition).setTaxableValue(departmentWiseReportBeanArrayList.get(iPosition).getTaxableValue()
                                                    + Report.getInt(Report.getColumnIndex("TaxableValue")));

                                            isDeptExists = true;
                                            break;
                                        }
                                    }
                                }

                                if (isDeptExists == false) {
                                    departmentWiseReportBean = new DepartmentWiseReportBean();
                                    departmentWiseReportBean.setDeptCode(Report.getInt(Report.getColumnIndex("DepartmentCode")));
                                    Cursor categ = HomeActivity.dbHandler.getDepartment(departmentWiseReportBean.getDeptCode());
                                    if (categ.moveToFirst())
                                        departmentWiseReportBean.setName(categ.getString(categ.getColumnIndex("DepartmentName")));
                                    else
                                        departmentWiseReportBean.setName("");
                                    departmentWiseReportBean.setTotalItems(Report.getInt(Report.getColumnIndex("Quantity")));
                                    departmentWiseReportBean.setDiscount(Report.getDouble(Report.getColumnIndex("DiscountAmount")));
                                    departmentWiseReportBean.setIGSTAmount(Report.getDouble(Report.getColumnIndex("IGSTAmount")));
                                    departmentWiseReportBean.setCGSTAmount(Report.getDouble(Report.getColumnIndex("CGSTAmount")));
                                    departmentWiseReportBean.setSGSTAmount(Report.getDouble(Report.getColumnIndex("SGSTAmount")));
                                    departmentWiseReportBean.setCESSAmount(Report.getDouble(Report.getColumnIndex("cessAmount")));
                                    departmentWiseReportBean.setTaxableValue(Report.getDouble(Report.getColumnIndex("TaxableValue")));
                                    departmentWiseReportBeanArrayList.add(departmentWiseReportBean);
                                }
                                isDeptExists = false;
                            } while (Report.moveToNext());

                            listCSVData.clear();
                            listStrData = new ArrayList<String>();
                            listStrData.add("Dept.Code");
                            listStrData.add("Dept Name");
                            listStrData.add("Total Items");
                            listStrData.add("Discount");
                            listStrData.add("IGST Amount");
                            listStrData.add("CGST Amount");
                            listStrData.add("UTGST/SGST Amount");
                            listStrData.add("cess Amount");
                            listStrData.add("Taxable Value");
                            listCSVData.add(listStrData);
                            for (int i = 0; i < departmentWiseReportBeanArrayList.size(); i++) {
                                totDepartmentWiseReportDisc += departmentWiseReportBeanArrayList.get(i).getDiscount();
                                totDepartmentWiseReportIGSTTax += departmentWiseReportBeanArrayList.get(i).getIGSTAmount();
                                totDepartmentWiseReportSales += departmentWiseReportBeanArrayList.get(i).getCGSTAmount();
                                totDepartmentWiseReportService += departmentWiseReportBeanArrayList.get(i).getSGSTAmount();
                                totDepartmentWiseReportAmt += departmentWiseReportBeanArrayList.get(i).getTaxableValue();
                                totDepartmentWiseReportCess += departmentWiseReportBeanArrayList.get(i).getCESSAmount();
                                listStrData = new ArrayList<String>();
                                listStrData.add(String.valueOf(departmentWiseReportBeanArrayList.get(i).getDeptCode()));
                                listStrData.add(departmentWiseReportBeanArrayList.get(i).getName());
                                listStrData.add(String.valueOf(departmentWiseReportBeanArrayList.get(i).getTotalItems()));
                                listStrData.add(String.valueOf(departmentWiseReportBeanArrayList.get(i).getDiscount()));
                                listStrData.add(String.valueOf(departmentWiseReportBeanArrayList.get(i).getIGSTAmount()));
                                listStrData.add(String.valueOf(departmentWiseReportBeanArrayList.get(i).getCGSTAmount()));
                                listStrData.add(String.valueOf(departmentWiseReportBeanArrayList.get(i).getSGSTAmount()));
                                listStrData.add(String.valueOf(departmentWiseReportBeanArrayList.get(i).getCESSAmount()));
                                listStrData.add(String.valueOf(departmentWiseReportBeanArrayList.get(i).getTaxableValue()));
                                listCSVData.add(listStrData);
                            }
                            listStrData = new ArrayList<String>();
                            listStrData.add("Total");
                            listStrData.add("");
                            listStrData.add("");
                            listStrData.add(String.valueOf(totDepartmentWiseReportDisc));
                            listStrData.add(String.valueOf(totDepartmentWiseReportIGSTTax));
                            listStrData.add(String.valueOf(totDepartmentWiseReportSales));
                            listStrData.add(String.valueOf(totDepartmentWiseReportService));
                            listStrData.add(String.valueOf(totDepartmentWiseReportCess));
                            listStrData.add(String.valueOf(totDepartmentWiseReportAmt));
                            listCSVData.add(listStrData);
                        } else {
                            listCSVData.clear();
                            return "No transaction has been done";
                        }
                        break;
                    case 2: // Fast Selling Item Wise Report
                        int i = 1;
                        fastSellingItemwiseReportBeanArrayList = new ArrayList<>();

                        Report = HomeActivity.dbHandler.getFastSellingItemwiseReport(
                                String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
                        FastSellingItemwiseReportBean fastSellingItemwiseReportBean;
                        if (Report.moveToFirst()) {
                            do {
                                boolean exist = false;
                                for (int iPosition = 0; iPosition < fastSellingItemwiseReportBeanArrayList.size(); iPosition++) {
                                    if (fastSellingItemwiseReportBeanArrayList.get(iPosition) != null) {
                                        int MenuCodeInTable = fastSellingItemwiseReportBeanArrayList.get(iPosition).getMenuCode();
                                        int menucode = Report.getInt(Report.getColumnIndex("item_id"));
                                        if (MenuCodeInTable == menucode) {
                                            fastSellingItemwiseReportBeanArrayList.get(iPosition).setQuantity(fastSellingItemwiseReportBeanArrayList.get(iPosition).getQuantity()
                                                    + Report.getDouble(Report.getColumnIndex("Quantity")));
                                            fastSellingItemwiseReportBeanArrayList.get(iPosition).setTotalPrice(fastSellingItemwiseReportBeanArrayList.get(iPosition).getTotalPrice()
                                                    + Report.getDouble(Report.getColumnIndex("TaxableValue")));
                                            exist = true;
                                            break;
                                        }
                                    }
                                }
                                if (!exist) {
                                    fastSellingItemwiseReportBean = new FastSellingItemwiseReportBean();
                                    fastSellingItemwiseReportBean.setSno(i);
                                    int dept = Report.getInt(Report.getColumnIndex("DepartmentCode"));
                                    if (dept == 0)
                                        fastSellingItemwiseReportBean.setDeptName("-");
                                    else {
                                        Cursor dd = HomeActivity.dbHandler.getDepartment(dept);
                                        if (dd != null && dd.moveToFirst())
                                            fastSellingItemwiseReportBean.setDeptName(dd.getString(dd.getColumnIndex("DepartmentName")));
                                        else
                                            fastSellingItemwiseReportBean.setDeptName("-");
                                    }
                                    int categ = Report.getInt(Report.getColumnIndex("CategoryCode"));
                                    if (categ == 0)
                                        fastSellingItemwiseReportBean.setCategName("-");
                                    else {
                                        Cursor cc = HomeActivity.dbHandler.getCategory(categ);
                                        if (cc != null && cc.moveToFirst())
                                            fastSellingItemwiseReportBean.setCategName(cc.getString(cc.getColumnIndex("CategoryName")));
                                        else
                                            fastSellingItemwiseReportBean.setCategName("-");
                                    }
                                    fastSellingItemwiseReportBean.setMenuCode(Report.getInt(Report.getColumnIndex("item_id")));
                                    fastSellingItemwiseReportBean.setItemName(Report.getString(Report.getColumnIndex("ItemName")));
                                    fastSellingItemwiseReportBean.setQuantity(Report.getDouble(Report.getColumnIndex("Quantity")));
                                    fastSellingItemwiseReportBean.setTotalPrice(Report.getDouble(Report.getColumnIndex("TaxableValue")));
                                    fastSellingItemwiseReportBeanArrayList.add(fastSellingItemwiseReportBean);
                                    i++;
                                }
                            } while (Report.moveToNext());
                            Collections.sort(fastSellingItemwiseReportBeanArrayList,
                                    FastSellingItemwiseReportBean.QuantityComparator);

                            listCSVData.clear();
                            listStrData = new ArrayList<String>();
                            listStrData.add("S.No");
                            listStrData.add("Short Code");
                            listStrData.add("Dept Name");
                            listStrData.add("Categ Name");
                            listStrData.add("Item Name");
                            listStrData.add("Qty");
                            listStrData.add("Total Price");
                            listCSVData.add(listStrData);
                            for(FastSellingItemwiseReportBean fastSellingItemwiseReportBean1 : fastSellingItemwiseReportBeanArrayList){
                                listStrData = new ArrayList<String>();
                                listStrData.add(String.valueOf(fastSellingItemwiseReportBean1.getSno()));
                                listStrData.add(String.valueOf(fastSellingItemwiseReportBean1.getMenuCode()));
                                listStrData.add(fastSellingItemwiseReportBean1.getDeptName());
                                listStrData.add(fastSellingItemwiseReportBean1.getCategName());
                                listStrData.add(fastSellingItemwiseReportBean1.getItemName());
                                listStrData.add(String.valueOf(fastSellingItemwiseReportBean1.getQuantity()));
                                listStrData.add(String.valueOf(fastSellingItemwiseReportBean1.getTotalPrice()));
                                listCSVData.add(listStrData);
                            }
                        } else {
                            listCSVData.clear();
                            return "No transaction has been done";
                        }
                        break;
                    case 3: // Item Wise Report
                        itemWiseReportBeanArrayList = new ArrayList<>();

                        Report = HomeActivity.dbHandler.getItemwiseReport(
                                String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));

                        ItemWiseReportBean itemWiseReportBean;
                        if (Report.moveToFirst()) {
                            boolean isItemExists = false;

                            do {
                                for (int iPosition = 0; iPosition < itemWiseReportBeanArrayList.size(); iPosition++) {

                                    if (itemWiseReportBeanArrayList.get(iPosition) != null) {

                                        if (itemWiseReportBeanArrayList.get(iPosition).getItemNo() == Report.getInt(Report.getColumnIndex("item_id"))) {

                                            // Quantity
                                            itemWiseReportBeanArrayList.get(iPosition).setSoldQuantity(itemWiseReportBeanArrayList.get(iPosition).getSoldQuantity()
                                                    + Report.getDouble(Report.getColumnIndex("Quantity")));
                                            // Discount
                                            itemWiseReportBeanArrayList.get(iPosition).setDiscount(itemWiseReportBeanArrayList.get(iPosition).getDiscount()
                                                    + Report.getDouble(Report.getColumnIndex("DiscountAmount")));

                                            // Sales Tax
                                            double tax = 0;
                                            tax = Report.getDouble(Report.getColumnIndex("CGSTAmount"));
                                            tax += Report.getDouble(Report.getColumnIndex("SGSTAmount"));
                                            tax += Report.getDouble(Report.getColumnIndex("IGSTAmount"));
                                            tax += Report.getDouble(Report.getColumnIndex("cessAmount"));

                                            itemWiseReportBeanArrayList.get(iPosition).setTax(itemWiseReportBeanArrayList.get(iPosition).getTax()
                                                    + tax);

                                            // Amount
                                            itemWiseReportBeanArrayList.get(iPosition).setTaxableValue(itemWiseReportBeanArrayList.get(iPosition).getTaxableValue()
                                                    + Report.getDouble(Report.getColumnIndex("TaxableValue")));

                                            // Modifier Amount
//                            itemWiseReportBeanArrayList.get(iPosition).setModifierAmount(itemWiseReportBeanArrayList.get(iPosition).getModifierAmount()
//                                    + Report.getDouble(Report.getColumnIndex("ModifierAmount")));

                                            isItemExists = true;
                                            break;
                                        }
                                    }
                                }

                                if (isItemExists == false) {

                                    itemWiseReportBean = new ItemWiseReportBean();

                                    itemWiseReportBean.setItemNo(Report.getInt(Report.getColumnIndex("item_id")));
                                    itemWiseReportBean.setItemName(Report.getString(Report.getColumnIndex("ItemName")));
                                    itemWiseReportBean.setSoldQuantity(Report.getDouble(Report.getColumnIndex("Quantity")));
                                    itemWiseReportBean.setTaxableValue(Report.getDouble(Report.getColumnIndex("TaxableValue")));

                                    double tax = Report.getDouble(Report.getColumnIndex("CGSTAmount"));
                                    tax += Report.getDouble(Report.getColumnIndex("SGSTAmount"));
                                    tax += Report.getDouble(Report.getColumnIndex("IGSTAmount"));
                                    tax += Report.getDouble(Report.getColumnIndex("cessAmount"));
                                    itemWiseReportBean.setTax(tax);

                                    itemWiseReportBean.setDiscount(Report.getDouble(Report.getColumnIndex("DiscountAmount")));
                //                    itemWiseReportBean.setModifierAmount(Report.getDouble(Report.getColumnIndex("ModifierAmount")));
                                    itemWiseReportBeanArrayList.add(itemWiseReportBean);
                                }
                                isItemExists = false;
                            } while (Report.moveToNext());
                            listCSVData.clear();
                            listStrData = new ArrayList<String>();
                            listStrData.add("Item No");
                            listStrData.add("Item Name");
                            listStrData.add("Qty");
                            listStrData.add("Discount");
                            listStrData.add("Tax");
                            listStrData.add("Taxable Value");
                            listCSVData.add(listStrData);
                            for(ItemWiseReportBean itemWiseReportBean1 : itemWiseReportBeanArrayList){
                                listStrData = new ArrayList<String>();
                                listStrData.add(String.valueOf(itemWiseReportBean1.getItemNo()));
                                listStrData.add(itemWiseReportBean1.getItemName());
                                listStrData.add(String.valueOf(itemWiseReportBean1.getSoldQuantity()));
                                listStrData.add(String.valueOf(itemWiseReportBean1.getDiscount()));
                                listStrData.add(String.valueOf(itemWiseReportBean1.getTax()));
                                listStrData.add(String.valueOf(itemWiseReportBean1.getTaxableValue()));
                                listCSVData.add(listStrData);
                            }
                        } else {
                            listCSVData.clear();
                            return "No transaction has been done";
                        }
                        break;
                    default:
                        break;
                }

            } catch (Exception ex) {
                Logger.e(TAG, "Error in async task do in background method populate data." + ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strResponse) {
            super.onPostExecute(strResponse);
            if (strResponse != null && !strResponse.isEmpty()) {
                MsgBox.Show("Warning", strResponse);
            }
            try {
                if (strResponse == null) {
                    switch (getSpinnerListIndex()) {
                        case 0: // Category Wise Report
                            CategoryWiseReport categoryWiseReport = new CategoryWiseReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", categoryWiseReportBeanArrayList);
                            bundle.putDouble("totAmt", totCategoryWiseReportAmt);
                            bundle.putDouble("totcess", totCategoryWiseReportCess);
                            bundle.putDouble("totService", totCategoryWiseReportService);
                            bundle.putDouble("totSales", totCategoryWiseReportSales);
                            bundle.putDouble("totIGSTTax", totCategoryWiseReportIGSTTax);
                            bundle.putDouble("totdisc", totCategoryWiseReportDisc);
                            categoryWiseReport.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.sales_report_container_inventory, categoryWiseReport);
                            fragmentTransaction.commit();
                            break;
                        case 1: // Department Wise report
                            DepartmentWiseReport departmentWiseReport = new DepartmentWiseReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", departmentWiseReportBeanArrayList);
                            bundle.putDouble("totAmt", totDepartmentWiseReportAmt);
                            bundle.putDouble("totcess", totDepartmentWiseReportCess);
                            bundle.putDouble("totService", totDepartmentWiseReportService);
                            bundle.putDouble("totSales", totDepartmentWiseReportSales);
                            bundle.putDouble("totIGSTTax", totDepartmentWiseReportIGSTTax);
                            bundle.putDouble("totdisc", totDepartmentWiseReportDisc);
                            departmentWiseReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.sales_report_container_inventory, departmentWiseReport);
                            fragmentTransaction.commit();
                            break;
                        case 2: // Fast Selling Item Wise Report
                            FastSellingItemWiseReport fastSellingItemWiseReport = new FastSellingItemWiseReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", fastSellingItemwiseReportBeanArrayList);
                            fastSellingItemWiseReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.sales_report_container_inventory, fastSellingItemWiseReport);
                            fragmentTransaction.commit();
                            break;
                        case 3: // Item Wise Report
                            sales_report_container_inventory.removeAllViews();
                            ItemWiseReport itemWiseReport = new ItemWiseReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", itemWiseReportBeanArrayList);
                            itemWiseReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.sales_report_container_inventory, itemWiseReport);
                            fragmentTransaction.commit();
                            break;
                        default:
                            break;
                    }
                } else {
                    Logger.e(TAG, strResponse);
                }
            } catch (Exception ex) {
                Logger.e(TAG, "Error in async task post execute method populate data." + ex.getMessage());
            } finally {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (populateSelectedReportData != null) {
                    populateSelectedReportData = null;
                }
            }
        }
    }

    private void ResetAll() {
        etReportDateStart_inventory.setText("");
        etReportDateEnd_inventory.setText("");
        listCSVData.clear();
        DefaultFragmentForEmptyData defaultFragmentForEmptyData = new DefaultFragmentForEmptyData();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.sales_report_container_inventory, defaultFragmentForEmptyData);
        fragmentTransaction.commit();
    }
}
