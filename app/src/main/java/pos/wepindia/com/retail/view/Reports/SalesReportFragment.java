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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.Constants;
import pos.wepindia.com.retail.GenericClass.DateTime;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.ReportExportToCSVHelper;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.retail.view.Reports.Bean.BillWiseBean;
import pos.wepindia.com.retail.view.Reports.Bean.DayWiseBean;
import pos.wepindia.com.retail.view.Reports.Bean.DuplicateBillBean;
import pos.wepindia.com.retail.view.Reports.Bean.MonthWiseBean;
import pos.wepindia.com.retail.view.Reports.Bean.TaxReportBean;
import pos.wepindia.com.retail.view.Reports.Bean.TransactionReportBean;
import pos.wepindia.com.retail.view.Reports.Bean.VoidBillReportBean;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;

/**
 * Created by SachinV on 23-01-2018.
 */

public class SalesReportFragment extends Fragment {

    private static final String TAG = SalesReportFragment.class.getSimpleName();

    private final String BILL_WISE_REPORT = "Bill Wise Report";
    private final String DAY_WISE_REPORT = "Day Wise Report";
    private final String DUPLICATE_BILL_REPORT = "Duplicate Bill Report";
    private final String MONTH_WISE_REPORT = "Month Wise Report";
    private final String TAX_REPORT = "Tax Report";
    private final String TRANSACTION_REPORT = "Transaction Report";
    private final String VOID_BILL_REPORT = "Void Bill Report";

    @BindView(R.id.spnrReportNameSelection)
    Spinner spnNameSelection;
    @BindView(R.id.btn_ReportDateFrom)
    Button btn_ReportDateFrom;
    @BindView(R.id.btn_ReportDateTo)
    Button btn_ReportDateTo;
    @BindView(R.id.etReportDateStart)
    EditText etReportDateStart;
    @BindView(R.id.etReportDateEnd)
    EditText etReportDateEnd;
    @BindView(R.id.btn_ReportPrint)
    Button btn_ReportPrint;
    @BindView(R.id.btn_sales_report_ReportExport)
    Button btn_ReportExport;
    @BindView(R.id.btn_ReportView)
    Button btn_ReportView;
    @BindView(R.id.sales_report_container)
    FrameLayout sales_report_container;


    private Context myContext;
    private HomeActivity homeActivity;
    private View view;
    private MessageDialog MsgBox;
    private DateTime objDate;
    private String strDate = "";
    private Date startDate_date, endDate_date;
    private ArrayList<BillWiseBean> billWiseBeanArrayList;
    private ArrayList<DayWiseBean> dayWiseBeanArrayList;
    private ArrayList<DuplicateBillBean> duplicateBillBeanArrayList;
    private ArrayList<MonthWiseBean> monthWiseBeanArrayList;
    private ArrayList<TaxReportBean> taxReportBeanArrayList;
    private ArrayList<TransactionReportBean> transactionReportBeanArrayList;
    private ArrayList<VoidBillReportBean> voidBillReportBeanArrayList;
    //List used for generating .csv file
    private List<List<String>> listCSVData;

    private PopulateSelectedReportData populateSelectedReportData = null;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.sales_report_fragment, container, false);
        myContext = getContext();
        MsgBox = new MessageDialog(myContext);
        homeActivity = new HomeActivity();
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
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        listCSVData = new ArrayList<List<String>>();
        spnNameSelection.setOnItemSelectedListener(spOnItemSelectListener);
    }

    @OnClick({R.id.btn_ReportDateFrom, R.id.btn_ReportDateTo,
            R.id.btn_ReportPrint, R.id.btn_sales_report_ReportExport, R.id.btn_ReportView})
    protected void mBtnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ReportDateFrom:
                From();
                break;
            case R.id.btn_ReportDateTo:
                To();
                break;
            case R.id.btn_ReportPrint: //PrintReport();
                break;
            case R.id.btn_sales_report_ReportExport:
                mExportReport();
                break;
            case R.id.btn_ReportView:
                ViewReport();
                break;
        }
    }

    public void From() {
        DateSelection(1);
    }

    public void To() {
        if (!etReportDateStart.getText().toString().equalsIgnoreCase("")) {
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

//    public void PrintReport() {
//        String prf = Preferences.getSharedPreferencesForPrint(getActivity()).getString("report", "--Select--");
//        if (prf.equalsIgnoreCase("Heyday"))
//        {
//            ArrayList<ArrayList<String>> list = printReport();
//            /*Intent intent = new Intent(getApplicationContext(), PrinterFragment.class);
//            intent.putExtra("printType", "REPORT");
//            intent.putExtra("reportName", strReportName);
//            intent.putExtra("printData", list);
//            startActivity(intent);*/
//            if(true)
//            {
//                homeActivity.printHeydeyReport(list, spnNameSelection.getSelectedItem().toString(), "REPORT");
//            }
//            else
//            {
//                homeActivity.askForConfig();
//            }
//        }
//        else
//        {
//            Toast.makeText(myContext, "Printer not configured", Toast.LENGTH_SHORT).show();
//        }
//    }

//    public ArrayList<ArrayList<String>> printReport() {
//        //This will iterate through your table layout and get the total amount of cells.
//        ArrayList<ArrayList<String>> arrayListRows = new ArrayList<ArrayList<String>>();
//        for (int i = 0; i < tblReport.getChildCount(); i++) {
//            //Remember that .getChildAt() method returns a View, so you would have to cast a specific control.
//            TableRow row = (TableRow) tblReport.getChildAt(i);
//            //This will iterate through the table row.
//            ArrayList<String> arrayListColumns = new ArrayList<String>();
//            for (int j = 0; j < row.getChildCount(); j++) {
//                TextView btn = (TextView) row.getChildAt(j);
//                String name = btn.getText().toString();
//                //String name1 = btn.getText().toString();
//                arrayListColumns.add(name);
//            }
//            if(arrayListColumns.size()>0)
//                arrayListRows.add(arrayListColumns);
//        }
//
//        return arrayListRows;
//    }

//    public void ExportReport() {
//        boolean isSuccess = false;
//        // ReportHelper object
//        ReportHelper objReportExport = new ReportHelper(myContext);
//        isSuccess = objReportExport.ExportReportToCSV(myContext, tblReport, spnNameSelection.getSelectedItem().toString(),
//                etReportDateStart.getText().toString(), etReportDateEnd.getText().toString());
//        if (isSuccess) {
//            MsgBox.Show("Information", "Report exported successfully");
//        }
//
//    }

    private int getSpinnerListIndex() {
        List<String> SalesReport = Arrays.asList(getResources().getStringArray(R.array.sales_report));

        for (String muom : SalesReport) {
            if (muom.contains(spnNameSelection.getSelectedItem().toString()))
                return SalesReport.indexOf(muom);
        }
        return 0;
    }

    void ViewReport() {

        String txtStartDate = etReportDateStart.getText().toString();
        String txtEndDate = etReportDateEnd.getText().toString();
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
                MsgBox.Show("Error", "Some error occurred while displaying report");
            }
        }

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
                                etReportDateStart.setText(strDate);
                                startDate_date = new Date(dateReportDate.getYear() - 1900, dateReportDate.getMonth(), dateReportDate.getDayOfMonth());
                            } else {
                                etReportDateEnd.setText(strDate);
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


    private ArrayList<TaxReportBean> getTaxReport(String TaxPercentName, String TaxAmountName, int i) {

        ArrayList<TaxReportBean> taxReportBeanArrayListTemp = new ArrayList<>();

        int count = 0;
        Cursor Report_detail = null;
        try {
            Report_detail = HomeActivity.dbHandler.getBillDetailReport(
                    String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));

            double totbillAmt = 0;

            TaxReportBean taxReportBean;

            while (Report_detail.moveToNext()) {
                boolean isTaxExists = false;

                String inv_no = Report_detail.getString(Report_detail.getColumnIndex(DatabaseHandler.KEY_InvoiceNo));
                String inv_date = Report_detail.getString(Report_detail.getColumnIndex(DatabaseHandler.KEY_InvoiceDate));
                Cursor Report = null;
                try {
                    Report = HomeActivity.dbHandler.getTaxDetailforBill(inv_no, inv_date, TaxPercentName, TaxAmountName);

                    while (Report.moveToNext()) {

                        for (int iPosition = count; iPosition < taxReportBeanArrayListTemp.size(); iPosition++) {

                            if (taxReportBeanArrayListTemp.get(iPosition) != null) {

                                double presenttax = taxReportBeanArrayListTemp.get(iPosition).getTaxPercent();
                                double reporttax = Report.getDouble(Report.getColumnIndex(TaxPercentName));

                                String presenttax_str = String.format("%.2f", presenttax);
                                String reporttax_str = String.format("%.2f", reporttax);
                                if (presenttax_str.equals(reporttax_str)) {

                                    // Sales Tax
                                    taxReportBeanArrayListTemp.get(iPosition).setTaxAmount(taxReportBeanArrayListTemp.get(iPosition).getTaxAmount() + Report.getDouble(Report.getColumnIndex(TaxAmountName)));
                                    // Amount
                                    taxReportBeanArrayListTemp.get(iPosition).setTaxableAmount(taxReportBeanArrayListTemp.get(iPosition).getTaxableAmount() + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_TaxableValue)));

                                    isTaxExists = true;
                                    break;

                                }
                            }
                        }

                        if (isTaxExists == false) {

                            taxReportBean = new TaxReportBean();

                            taxReportBean.setTaxPercent(Report.getDouble(Report.getColumnIndex(TaxPercentName)));
                            taxReportBean.setSno(i++);


                            if (TaxPercentName.equals("SGSTRate"))
                                taxReportBean.setDescription("UTGST/SGSTRate");
                            else
                                taxReportBean.setDescription(TaxPercentName);

                            taxReportBean.setTaxAmount(Report.getDouble(Report.getColumnIndex(TaxAmountName)));

                            taxReportBean.setTaxableAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_TaxableValue)));
                            totbillAmt = totbillAmt + taxReportBean.getTaxableAmount();

                            taxReportBeanArrayListTemp.add(taxReportBean);

                        }
                        isTaxExists = false;
                    }
                }catch (Exception ex){
                    Logger.e(TAG, " Unable to fetch data from table OuterWardSupplyLedger." +ex.getMessage());
                }finally {
                    if(Report != null){
                        Report.close();
                    }
                }
            }
        } catch (Exception ex){
            Logger.i(TAG,"Error on fetching the data for tax report." +ex.getMessage());
        } finally {
            if(Report_detail != null){
                Report_detail.close();
            }
        }
        return taxReportBeanArrayListTemp;
    }

    private void mExportReport() {
        if(!etReportDateStart.getText().toString().isEmpty() && !etReportDateEnd.getText().toString().isEmpty()) {
            if (listCSVData.size() >= 2) {
                ReportExportToCSVHelper reportExportToCSVHelper = ReportExportToCSVHelper.getInstance();
                reportExportToCSVHelper.mGenerateCSVFile(getActivity(), spnNameSelection.getSelectedItem().toString(), etReportDateStart.getText().toString(),
                        etReportDateEnd.getText().toString(), listCSVData);
            }
        } else {
            MsgBox.Show("Warning","Please select date and try again.");
        }
    }

    private class PopulateSelectedReportData extends AsyncTask<Void, String, String> {
        //BillWiseReport
        double totBillWiseReportBillAmt = 0, totBillWiseReportTax = 0, totBillWiseReportDisc = 0;
        //DayWiseReport
        double totDayWiseReportDis = 0, totDayWiseReportIGSTTax = 0, totDayWiseReportbillAmt = 0, totDayWiseReportSalesTax = 0,
                totDayWiseReportServiceTax = 0, totDayWiseReportcess = 0;
        //DuplicateBillReport
        double totDuplicateBillReportDis = 0, totDuplicateBillReportIGSTTax = 0, totDuplicateBillReportBillAmt = 0,
                totDuplicateBillReportSalesTax = 0, totDuplicateBillReportCess = 0, totDuplicateBillReportServiceTax = 0;
        ;
        //MonthWiseReport
        double totMonthWiseReportDis = 0, totMonthWiseReportIGSTTax = 0, totMonthWiseReportBillAmt = 0,
                totMonthWiseReportCess = 0, totMonthWiseReportSalesTax = 0, totMonthWiseReportServiceTax = 0;
        //TaxReport
        double totTaxReportTax = 0, totTaxReportAmt = 0;
        //Transaction
        double totTransactionCash = 0, totTransactionCoupon = 0, totTransactionWallet = 0, totTransactionCard = 0,
                totTransactionAmt = 0, totTransactionPetty = 0, totTransactionMSwipe = 0, totTransactionAEPS = 0;
        // voidBillReport
        double totVoidBillAmount=0, totVoidBillIGSTAmount =0, totVoidBillCGSTAmount =0, totVoidBillSGSTAmount =0,
                totVoidBillcessAmount =0;
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
                    case 0:    // Bill wise Report
                        BillWiseBean billWiseBean;
                        billWiseBeanArrayList = new ArrayList<>();
                        Report = HomeActivity.dbHandler.getBillDetailReport(String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
                        if (Report.moveToFirst()) {
                            listCSVData.clear();
                            List<String> listStrDataHeader = new ArrayList<String>();
                            listStrDataHeader.add("Date");
                            listStrDataHeader.add("Bill No");
                            listStrDataHeader.add("Item");
                            listStrDataHeader.add("Discount");
                            listStrDataHeader.add("Tax");
                            listStrDataHeader.add("Bill Amount");
                            listCSVData.add(listStrDataHeader);
                            do {
                                billWiseBean = new BillWiseBean();
                                String dateInMillis = Report.getString(Report.getColumnIndex(DatabaseHandler.KEY_InvoiceDate));
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String dateString = formatter.format(Long.parseLong(dateInMillis));
                                billWiseBean.setInvoiceDate(dateString);
                                billWiseBean.setInvoiceNumber(Report.getInt(Report.getColumnIndex(DatabaseHandler.KEY_InvoiceNo)));
                                billWiseBean.setTotalItems(Report.getInt(Report.getColumnIndex("TotalItems")));

                                double amt_s = (Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_BillAmount)));
                                billWiseBean.setBillAmount(amt_s);

                                String amt_str = (String.format("%.2f", amt_s));
                                totBillWiseReportBillAmt += Double.parseDouble(amt_str);
                                double tax = 0;
                                tax = Double.parseDouble(String.format("%.2f", Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_CGSTAmount))));
                                tax += Double.parseDouble(String.format("%.2f", Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_SGSTAmount))));
                                tax += Double.parseDouble(String.format("%.2f", Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_IGSTAmount))));
                                tax += Double.parseDouble(String.format("%.2f", Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_cessAmount))));
                                billWiseBean.setTotalTax(Double.parseDouble(String.format("%.2f", tax)));
                                totBillWiseReportTax += tax;
                                billWiseBean.setTotalDiscountAmount(Double.parseDouble(String.format("%.2f", Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_TotalDiscountAmount)))));
                                totBillWiseReportDisc += billWiseBean.getTotalDiscountAmount();
                                billWiseBeanArrayList.add(billWiseBean);
                                listStrData = new ArrayList<String>();
                                listStrData.add(billWiseBean.getInvoiceDate());
                                listStrData.add(String.valueOf(billWiseBean.getInvoiceNumber()));
                                listStrData.add(String.valueOf(billWiseBean.getTotalItems()));
                                listStrData.add(String.valueOf(billWiseBean.getTotalDiscountAmount()));
                                listStrData.add(String.valueOf(billWiseBean.getTotalTax()));
                                listStrData.add(String.valueOf(billWiseBean.getBillAmount()));
                                listCSVData.add(listStrData);
                            } while (Report.moveToNext());
                            List<String> listStrDataTotal = new ArrayList<String>();
                            listStrDataTotal.add("Total");
                            listStrDataTotal.add("");
                            listStrDataTotal.add("");
                            listStrDataTotal.add(String.valueOf(totBillWiseReportDisc));
                            listStrDataTotal.add(String.valueOf(totBillWiseReportTax));
                            listStrDataTotal.add(String.valueOf(totBillWiseReportBillAmt));
                            listCSVData.add(listStrDataTotal);
                        } else {
                            listCSVData.clear();
                            return "No transaction has been done";
                        }
                        break;
                    case 1: // Day wise report
                        dayWiseBeanArrayList = new ArrayList<>();
                        Report = HomeActivity.dbHandler.getDaywiseReport(
                                String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
                        int count = 0;
                        if (Report.moveToFirst()) {
                            boolean isItemExists = false;
                            DayWiseBean dayWiseBean;
                            listCSVData.clear();
                            List<String> listStrDataHeader = new ArrayList<String>();
                            listStrDataHeader.add("Date");
                            listStrDataHeader.add("Total Bills");
                            listStrDataHeader.add("Discount");
                            listStrDataHeader.add("IGST Amount");
                            listStrDataHeader.add("CGST Amount");
                            listStrDataHeader.add("UTGST/SGST Amount");
                            listStrDataHeader.add("cess Amount");
                            listStrDataHeader.add("Bill Amount");
                            listCSVData.add(listStrDataHeader);
                            do {
                                for (int iPosition = count; iPosition < dayWiseBeanArrayList.size(); iPosition++) {
                                    if (dayWiseBeanArrayList.get(iPosition) != null) {
                                        String dateInMillis = Report.getString(Report.getColumnIndex(DatabaseHandler.KEY_InvoiceDate));
                                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                        String dateString = formatter.format(Long.parseLong(dateInMillis));
                                        if (dayWiseBeanArrayList.get(iPosition).getInvoiceDate().equalsIgnoreCase(dateString)) {
                                            dayWiseBeanArrayList.get(iPosition).setTotalBills(dayWiseBeanArrayList.get(iPosition).getTotalBills() + 1);
                                            dayWiseBeanArrayList.get(iPosition).setBillAmount(dayWiseBeanArrayList.get(iPosition).getBillAmount() + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_BillAmount)));
                                            dayWiseBeanArrayList.get(iPosition).setIGSTAmount(dayWiseBeanArrayList.get(iPosition).getIGSTAmount() + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_IGSTAmount)));
                                            dayWiseBeanArrayList.get(iPosition).setCGSTAmount(dayWiseBeanArrayList.get(iPosition).getCGSTAmount() + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_CGSTAmount)));
                                            dayWiseBeanArrayList.get(iPosition).setSGSTAmount(dayWiseBeanArrayList.get(iPosition).getSGSTAmount() + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_SGSTAmount)));
                                            dayWiseBeanArrayList.get(iPosition).setCESSAmount(dayWiseBeanArrayList.get(iPosition).getCESSAmount() + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_cessAmount)));
                                            dayWiseBeanArrayList.get(iPosition).setDiscount(dayWiseBeanArrayList.get(iPosition).getDiscount() + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_TotalDiscountAmount)));
                                            isItemExists = true;
                                            break;
                                        }
                                    }
                                }
                                if (isItemExists == false) {
                                    dayWiseBean = new DayWiseBean();
                                    String dateInMillis = Report.getString(Report.getColumnIndex(DatabaseHandler.KEY_InvoiceDate));
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                    String dateString = formatter.format(Long.parseLong(dateInMillis));
                                    dayWiseBean.setInvoiceDate(dateString);
                                    dayWiseBean.setTotalBills(1);
                                    dayWiseBean.setBillAmount(Double.parseDouble(String.format("%.2f", Report.getDouble(Report
                                            .getColumnIndex(DatabaseHandler.KEY_BillAmount)))));
                                    dayWiseBean.setIGSTAmount(Double.parseDouble(String.format("%.2f", Report.getDouble(Report
                                            .getColumnIndex(DatabaseHandler.KEY_IGSTAmount)))));
                                    dayWiseBean.setCGSTAmount(Double.parseDouble(String.format("%.2f", Report.getDouble(Report
                                            .getColumnIndex(DatabaseHandler.KEY_CGSTAmount)))));
                                    dayWiseBean.setSGSTAmount(Double.parseDouble(String.format("%.2f", Report.getDouble(Report
                                            .getColumnIndex(DatabaseHandler.KEY_SGSTAmount)))));
                                    dayWiseBean.setCESSAmount(Double.parseDouble(String.format("%.2f", Report.getDouble(Report
                                            .getColumnIndex(DatabaseHandler.KEY_cessAmount)))));
                                    dayWiseBean.setDiscount(Double.parseDouble(String.format("%.2f", Report.getDouble(Report
                                            .getColumnIndex(DatabaseHandler.KEY_TotalDiscountAmount)))));
                                    dayWiseBeanArrayList.add(dayWiseBean);
                                }
                                isItemExists = false;
                            } while (Report.moveToNext());
                            for (DayWiseBean dayWiseBeanCSV : dayWiseBeanArrayList) {
                                listStrData = new ArrayList<String>();
                                listStrData.add(dayWiseBeanCSV.getInvoiceDate());
                                listStrData.add(String.valueOf(dayWiseBeanCSV.getTotalBills()));
                                listStrData.add(String.valueOf(dayWiseBeanCSV.getDiscount()));
                                listStrData.add(String.valueOf(dayWiseBeanCSV.getIGSTAmount()));
                                listStrData.add(String.valueOf(dayWiseBeanCSV.getCGSTAmount()));
                                listStrData.add(String.valueOf(dayWiseBeanCSV.getSGSTAmount()));
                                listStrData.add(String.valueOf(dayWiseBeanCSV.getCESSAmount()));
                                listStrData.add(String.valueOf(dayWiseBeanCSV.getBillAmount()));
                                listCSVData.add(listStrData);
                            }
                            int count1 = dayWiseBeanArrayList.size();
                            for (int i = count; i < count1; i++) {
                                totDayWiseReportDis += dayWiseBeanArrayList.get(i).getDiscount();
                                totDayWiseReportIGSTTax += dayWiseBeanArrayList.get(i).getIGSTAmount();
                                totDayWiseReportSalesTax += dayWiseBeanArrayList.get(i).getCGSTAmount();
                                totDayWiseReportServiceTax += dayWiseBeanArrayList.get(i).getSGSTAmount();
                                totDayWiseReportcess += dayWiseBeanArrayList.get(i).getCESSAmount();
                                totDayWiseReportbillAmt += dayWiseBeanArrayList.get(i).getBillAmount();
                            }
                            List<String> listStrDataTotal = new ArrayList<String>();
                            listStrDataTotal.add("Total");
                            listStrDataTotal.add("");
                            listStrDataTotal.add(String.valueOf(totDayWiseReportDis));
                            listStrDataTotal.add(String.valueOf(totDayWiseReportIGSTTax));
                            listStrDataTotal.add(String.valueOf(totDayWiseReportSalesTax));
                            listStrDataTotal.add(String.valueOf(totDayWiseReportServiceTax));
                            listStrDataTotal.add(String.valueOf(totDayWiseReportcess));
                            listStrDataTotal.add(String.valueOf(totDayWiseReportbillAmt));
                            listCSVData.add(listStrDataTotal);
                        } else {
                            listCSVData.clear();
                            return "No transaction has been done";
                        }
                        break;
                    case 2:    // Duplicate Bill Report
                        duplicateBillBeanArrayList = new ArrayList<>();
                        Report = HomeActivity.dbHandler.getDuplicateBillsReport(
                                String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
                        DuplicateBillBean duplicateBillBean;
                        listCSVData.clear();
                        listStrData = new ArrayList<String>();
                        listStrData.add("Date");
                        listStrData.add("Bill No");
                        listStrData.add("Item");
                        listStrData.add("Discount");
                        listStrData.add("IGST Amount");
                        listStrData.add("CGST Amount");
                        listStrData.add("UTGST/SGST Amount");
                        listStrData.add("cess Amount");
                        listStrData.add("Bill Amount");
                        listStrData.add("Reprint Count");
                        listCSVData.add(listStrData);
                        if (Report.moveToFirst()) {
                            do {
                                duplicateBillBean = new DuplicateBillBean();

                                String dateInMillis = Report.getString(Report.getColumnIndex(DatabaseHandler.KEY_InvoiceDate));
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String dateString = formatter.format(Long.parseLong(dateInMillis));
                                duplicateBillBean.setInvoiceDate(dateString);
                                duplicateBillBean.setInvoiceNumber(Report.getInt(Report.getColumnIndex(DatabaseHandler.KEY_InvoiceNo)));
                                duplicateBillBean.setItems(Report.getInt(Report.getColumnIndex("TotalItems")));
                                duplicateBillBean.setBillAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_BillAmount)));
                                totDuplicateBillReportBillAmt += duplicateBillBean.getBillAmount();
                                duplicateBillBean.setIgstAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_IGSTAmount)));
                                totDuplicateBillReportIGSTTax += duplicateBillBean.getIgstAmount();
                                duplicateBillBean.setCgstAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_CGSTAmount)));
                                totDuplicateBillReportSalesTax += duplicateBillBean.getCgstAmount();
                                duplicateBillBean.setSgstAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_SGSTAmount)));
                                totDuplicateBillReportServiceTax += duplicateBillBean.getSgstAmount();
                                duplicateBillBean.setCessAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_cessAmount)));
                                totDuplicateBillReportCess += duplicateBillBean.getCessAmount();
                                duplicateBillBean.setDiscount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_TotalDiscountAmount)));
                                totDuplicateBillReportDis += duplicateBillBean.getDiscount();
                                duplicateBillBean.setReprintCount(Report.getInt(Report.getColumnIndex(DatabaseHandler.KEY_ReprintCount)));
                                duplicateBillBeanArrayList.add(duplicateBillBean);

                                //Report to export as csv file
                                listStrData = new ArrayList<String>();
                                listStrData.add(duplicateBillBean.getInvoiceDate());
                                listStrData.add(String.valueOf(duplicateBillBean.getInvoiceNumber()));
                                listStrData.add(String.valueOf(duplicateBillBean.getItems()));
                                listStrData.add(String.valueOf(duplicateBillBean.getDiscount()));
                                listStrData.add(String.valueOf(duplicateBillBean.getIgstAmount()));
                                listStrData.add(String.valueOf(duplicateBillBean.getCgstAmount()));
                                listStrData.add(String.valueOf(duplicateBillBean.getSgstAmount()));
                                listStrData.add(String.valueOf(duplicateBillBean.getCessAmount()));
                                listStrData.add(String.valueOf(duplicateBillBean.getBillAmount()));
                                listStrData.add(String.valueOf(duplicateBillBean.getReprintCount()));
                                listCSVData.add(listStrData);
                            } while (Report.moveToNext());
                            listStrData = new ArrayList<String>();
                            listStrData.add("Total");
                            listStrData.add("");
                            listStrData.add("");
                            listStrData.add(String.valueOf(totDuplicateBillReportDis));
                            listStrData.add(String.valueOf(totDuplicateBillReportIGSTTax));
                            listStrData.add(String.valueOf(totDuplicateBillReportSalesTax));
                            listStrData.add(String.valueOf(totDuplicateBillReportServiceTax));
                            listStrData.add(String.valueOf(totDuplicateBillReportCess));
                            listStrData.add(String.valueOf(totDuplicateBillReportBillAmt));
                            listStrData.add("");
                            listCSVData.add(listStrData);
                        } else {
                            listCSVData.clear();
                            return "No transaction has been done";
                        }
                        break;
                    case 3: // Month wise Report
                        monthWiseBeanArrayList = new ArrayList<>();
                        Report = HomeActivity.dbHandler.getDaywiseReport(
                                String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));

                        MonthWiseBean monthWiseBean;
                        listCSVData.clear();
                        listStrData = new ArrayList<String>();
                        listStrData.add("Month");
                        listStrData.add("TotalBills");
                        listStrData.add("Discount");
                        listStrData.add("IGST Amount");
                        listStrData.add("CGST Amount");
                        listStrData.add("UTGST/SGST Amount");
                        listStrData.add("cess Amount");
                        listStrData.add("Bill Amount");
                        listCSVData.add(listStrData);
                        if (Report.moveToFirst()) {
                            boolean isItemExists = false;
                            do {
                                for (int iPosition = 0; iPosition < monthWiseBeanArrayList.size(); iPosition++) {
                                    if (monthWiseBeanArrayList.get(iPosition) != null) {
                                        String dateInMillis = Report.getString(Report.getColumnIndex(DatabaseHandler.KEY_InvoiceDate));
                                        Calendar c2 = Calendar.getInstance();
                                        c2.setTimeInMillis(Long.parseLong(dateInMillis));
                                        int pre1 = monthWiseBeanArrayList.get(iPosition).getMonth();
                                        int next1 = c2.get(Calendar.MONTH);
                                        if (pre1 == next1) {
                                            monthWiseBeanArrayList.get(iPosition).setTotalBills(monthWiseBeanArrayList.get(iPosition).getTotalBills() + 1);
                                            monthWiseBeanArrayList.get(iPosition).setBillAmount(monthWiseBeanArrayList.get(iPosition).getBillAmount() + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_BillAmount)));
                                            monthWiseBeanArrayList.get(iPosition).setIgstAmount(monthWiseBeanArrayList.get(iPosition).getIgstAmount() + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_IGSTAmount)));
                                            monthWiseBeanArrayList.get(iPosition).setCgstAmount(monthWiseBeanArrayList.get(iPosition).getCgstAmount() + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_CGSTAmount)));
                                            monthWiseBeanArrayList.get(iPosition).setSgstAmount(monthWiseBeanArrayList.get(iPosition).getSgstAmount() + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_SGSTAmount)));
                                            monthWiseBeanArrayList.get(iPosition).setCessAmount(monthWiseBeanArrayList.get(iPosition).getCessAmount() + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_cessAmount)));
                                            monthWiseBeanArrayList.get(iPosition).setDiscount(monthWiseBeanArrayList.get(iPosition).getDiscount() + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_TotalDiscountAmount)));
                                            isItemExists = true;
                                            break;
                                        }
                                    }
                                }

                                if (isItemExists == false) {
                                    monthWiseBean = new MonthWiseBean();
                                    String dateInMillis = Report.getString(Report.getColumnIndex(DatabaseHandler.KEY_InvoiceDate));
                                    Calendar c1 = Calendar.getInstance();
                                    c1.setTimeInMillis(Long.parseLong(dateInMillis));
                                    int mon = c1.get(Calendar.MONTH);
                                    monthWiseBean.setMonth(mon);
                                    monthWiseBean.setTotalBills(1);
                                    monthWiseBean.setBillAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_BillAmount)));
                                    monthWiseBean.setIgstAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_IGSTAmount)));
                                    monthWiseBean.setCgstAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_CGSTAmount)));
                                    monthWiseBean.setSgstAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_SGSTAmount)));
                                    monthWiseBean.setCessAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_cessAmount)));
                                    monthWiseBean.setDiscount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_TotalDiscountAmount)));
                                    monthWiseBeanArrayList.add(monthWiseBean);
                                }
                                isItemExists = false;
                            } while (Report.moveToNext());
                            if (monthWiseBeanArrayList.size() > 0) {
                                for (MonthWiseBean monthWiseBean1 : monthWiseBeanArrayList) {
                                    listStrData = new ArrayList<String>();
                                    String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                                    listStrData.add(months[monthWiseBean1.getMonth()]);
                                    listStrData.add(String.valueOf(monthWiseBean1.getTotalBills()));
                                    listStrData.add(String.valueOf(monthWiseBean1.getDiscount()));
                                    listStrData.add(String.valueOf(monthWiseBean1.getIgstAmount()));
                                    listStrData.add(String.valueOf(monthWiseBean1.getCgstAmount()));
                                    listStrData.add(String.valueOf(monthWiseBean1.getSgstAmount()));
                                    listStrData.add(String.valueOf(monthWiseBean1.getCessAmount()));
                                    listStrData.add(String.valueOf(monthWiseBean1.getBillAmount()));
                                    listCSVData.add(listStrData);
                                }
                            }
                            for (int i = 0; i < monthWiseBeanArrayList.size(); i++) {
                                totMonthWiseReportBillAmt += monthWiseBeanArrayList.get(i).getBillAmount();
                                totMonthWiseReportIGSTTax += monthWiseBeanArrayList.get(i).getIgstAmount();
                                totMonthWiseReportSalesTax += monthWiseBeanArrayList.get(i).getCgstAmount();
                                totMonthWiseReportServiceTax += monthWiseBeanArrayList.get(i).getSgstAmount();
                                totMonthWiseReportDis += monthWiseBeanArrayList.get(i).getDiscount();
                                totMonthWiseReportCess += monthWiseBeanArrayList.get(i).getCessAmount();
                            }
                            listStrData = new ArrayList<String>();
                            listStrData.add("Total");
                            listStrData.add("");
                            listStrData.add(String.valueOf(totMonthWiseReportDis));
                            listStrData.add(String.valueOf(totMonthWiseReportIGSTTax));
                            listStrData.add(String.valueOf(totMonthWiseReportSalesTax));
                            listStrData.add(String.valueOf(totMonthWiseReportServiceTax));
                            listStrData.add(String.valueOf(totMonthWiseReportCess));
                            listStrData.add(String.valueOf(totMonthWiseReportBillAmt));
                            listCSVData.add(listStrData);
                        } else {
                            listCSVData.clear();
                            return "No transaction has been done";
                        }
                        break;
                    case 4: // Tax report
                        int i = 1;
                        ArrayList<TaxReportBean> IGSTArrayList = getTaxReport("IGSTRate", "IGSTAmount", i);
                        ArrayList<TaxReportBean> CGSTArrayList = getTaxReport("CGSTRate", "CGSTAmount", i + IGSTArrayList.size());
                        ArrayList<TaxReportBean> SGSTArrayList = getTaxReport("SGSTRate", "SGSTAmount", i + IGSTArrayList.size() + CGSTArrayList.size());
                        ArrayList<TaxReportBean> CESSArrayList = getTaxReport("cessRate", "cessAmount", i + IGSTArrayList.size() + CGSTArrayList.size() + SGSTArrayList.size());

                        taxReportBeanArrayList = new ArrayList<>();

                        taxReportBeanArrayList.addAll(IGSTArrayList);
                        taxReportBeanArrayList.addAll(CGSTArrayList);
                        taxReportBeanArrayList.addAll(SGSTArrayList);
                        taxReportBeanArrayList.addAll(CESSArrayList);
                        if (taxReportBeanArrayList.size() > 0) {
                            listCSVData.clear();
                            listStrData = new ArrayList<String>();
                            listStrData.add("S.No");
                            listStrData.add("Description");
                            listStrData.add("Tax Percent");
                            listStrData.add("Tax Amount");
                            listStrData.add("Taxable Amount");
                            listCSVData.add(listStrData);
                            for (int ii = 0; ii < taxReportBeanArrayList.size(); ii++) {
                                totTaxReportTax += taxReportBeanArrayList.get(ii).getTaxAmount();
                                totTaxReportAmt += taxReportBeanArrayList.get(ii).getTaxableAmount();
                                listStrData = new ArrayList<String>();
                                listStrData.add(String.valueOf(ii));
                                listStrData.add(taxReportBeanArrayList.get(ii).getDescription());
                                listStrData.add(String.valueOf(taxReportBeanArrayList.get(ii).getTaxPercent()));
                                listStrData.add(String.valueOf(taxReportBeanArrayList.get(ii).getTaxAmount()));
                                listStrData.add(String.valueOf(taxReportBeanArrayList.get(ii).getTaxableAmount()));
                                listCSVData.add(listStrData);
                            }
                            listStrData = new ArrayList<String>();
                            listStrData.add("Total");
                            listStrData.add("");
                            listStrData.add("");
                            listStrData.add(String.valueOf(totTaxReportTax));
                            listStrData.add(String.valueOf(totTaxReportAmt));
                            listCSVData.add(listStrData);
                        } else  {
                            listCSVData.clear();
                            return "No transaction has been done";
                        }
                        break;
                    case 5: // Transaction Report
                        transactionReportBeanArrayList = new ArrayList<>();

                        Report = HomeActivity.dbHandler.getBillDetailReport(
                                String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));

                        TransactionReportBean transactionReportBean;
                        listCSVData.clear();
                        listStrData = new ArrayList<String>();
                        listStrData.add("Date");
                        listStrData.add("Bill Number");
                        listStrData.add("Items");
                        listStrData.add("Bill Amount");
                        listStrData.add("Cash Payment");
                        listStrData.add("Card Payment");
                        listStrData.add("Coupon Payment");
                        listStrData.add("Petty Cash Payment");
                        listStrData.add("Wallet Payment");
                        listStrData.add("MSwipe Payment");
                        listStrData.add("AEPS Payment");
                        listCSVData.add(listStrData);

                        if (Report.moveToFirst()) {
                            do {
                                transactionReportBean = new TransactionReportBean();
                                String dateInMillis = Report.getString(Report.getColumnIndex(DatabaseHandler.KEY_InvoiceDate));
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String dateString = formatter.format(Long.parseLong(dateInMillis));
                                transactionReportBean.setInvoiceDate(dateString);
                                transactionReportBean.setBillNumber(Report.getInt(Report.getColumnIndex(DatabaseHandler.KEY_InvoiceNo)));
                                transactionReportBean.setItems(Report.getInt(Report.getColumnIndex("TotalItems")));
                                transactionReportBean.setBillAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_BillAmount)));
                                totTransactionAmt += transactionReportBean.getBillAmount();
                                transactionReportBean.setCashAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_CashPayment)));
                                totTransactionCash += transactionReportBean.getCashAmount();
                                transactionReportBean.setCardPayment(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_CardPayment)));
                                totTransactionCard += transactionReportBean.getCardPayment();
                                transactionReportBean.setCouponPayment(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_CouponPayment)));
                                totTransactionCoupon += transactionReportBean.getCouponPayment();
                                transactionReportBean.setPettyCashPayment(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_PettyCashPayment)));
                                totTransactionPetty += transactionReportBean.getPettyCashPayment();
                                transactionReportBean.setWalletPayment(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_WalletPayment)));
                                totTransactionWallet += transactionReportBean.getWalletPayment();
                                transactionReportBean.setmSwipePayment(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_MSWIPE_Amount)));
                                totTransactionMSwipe += transactionReportBean.getmSwipePayment();
                                transactionReportBean.setAepsPayment(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_AEPSAmount)));
                                totTransactionAEPS += transactionReportBean.getAepsPayment();
                                transactionReportBeanArrayList.add(transactionReportBean);
                                listStrData = new ArrayList<String>();
                                listStrData.add(transactionReportBean.getInvoiceDate());
                                listStrData.add(String.valueOf(transactionReportBean.getBillNumber()));
                                listStrData.add(String.valueOf(transactionReportBean.getItems()));
                                listStrData.add(String.valueOf(transactionReportBean.getBillAmount()));
                                listStrData.add(String.valueOf(transactionReportBean.getCashAmount()));
                                listStrData.add(String.valueOf(transactionReportBean.getCardPayment()));
                                listStrData.add(String.valueOf(transactionReportBean.getCouponPayment()));
                                listStrData.add(String.valueOf(transactionReportBean.getPettyCashPayment()));
                                listStrData.add(String.valueOf(transactionReportBean.getWalletPayment()));
                                listStrData.add(String.valueOf(transactionReportBean.getmSwipePayment()));
                                listStrData.add(String.valueOf(transactionReportBean.getAepsPayment()));
                                listCSVData.add(listStrData);
                            } while (Report.moveToNext());
                            listStrData = new ArrayList<String>();
                            listStrData.add("Total");
                            listStrData.add("");
                            listStrData.add("");
                            listStrData.add(String.valueOf(totTransactionAmt));
                            listStrData.add(String.valueOf(totTransactionCash));
                            listStrData.add(String.valueOf(totTransactionCard));
                            listStrData.add(String.valueOf(totTransactionCoupon));
                            listStrData.add(String.valueOf(totTransactionPetty));
                            listStrData.add(String.valueOf(totTransactionWallet));
                            listStrData.add(String.valueOf(totTransactionMSwipe));
                            listStrData.add(String.valueOf(totTransactionAEPS));
                            listCSVData.add(listStrData);
                        } else {
                            listCSVData.clear();
                            return "No transaction has been done";
                        }
                        break;

                    case 6:
                        //Cursor Report = null;
                        voidBillReportBeanArrayList = new ArrayList<>();
                        try{

                            Report = HomeActivity.dbHandler.getVoidBillsReport(
                                    String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));

                            if (Report.moveToFirst()) {

                                float totDisc = 0, totIGSTTax = 0;
                                double totbillAmt = 0, totSalesTax = 0, totServiceTax = 0, totcess = 0;

                                listCSVData.clear();
                                listStrData = new ArrayList<String>();
                                listStrData.add("Date");
                                listStrData.add("Bill Number");
                                listStrData.add("Total Items");
                                listStrData.add("IGST Amount");
                                listStrData.add("CGST Amount");
                                listStrData.add("SGST Amount");
                                listStrData.add("cess Amount");
                                listStrData.add("Bill Amount");
                                listCSVData.add(listStrData);

                                do {

                                    VoidBillReportBean voidBean = new VoidBillReportBean();

                                    String dateInMillis = Report.getString(Report.getColumnIndex("InvoiceDate"));
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                    String dateString = formatter.format(Long.parseLong(dateInMillis));
                                    voidBean.setDate(dateString);
                                    voidBean.setBillno(Report.getString(Report.getColumnIndex(DatabaseHandler.KEY_InvoiceNo)));
                                    voidBean.setTotalItems(Report.getInt(Report.getColumnIndex(DatabaseHandler.KEY_TotalItems)));
                                    voidBean.setBillAmount(Double.parseDouble(String.format("%.2f",
                                            Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_BillAmount)))));

                                    voidBean.setIGSTAmount(Double.parseDouble(String.format("%.2f",
                                            Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_IGSTAmount)))));
                                    voidBean.setCGSTAmount(Double.parseDouble(String.format("%.2f",
                                            Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_CGSTAmount)))));
                                    voidBean.setSGSTAmount(Double.parseDouble(String.format("%.2f",
                                            Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_SGSTAmount)))));
                                    voidBean.setCessAmount(Double.parseDouble(String.format("%.2f",
                                            Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_cessAmount)))));
                                    voidBillReportBeanArrayList.add(voidBean);
                                    listStrData = new ArrayList<String>();
                                    listStrData.add(voidBean.getDate());
                                    listStrData.add(voidBean.getBillno());
                                    listStrData.add(String.valueOf(voidBean.getTotalItems()));
                                    listStrData.add(String.valueOf(voidBean.getIGSTAmount()));
                                    listStrData.add(String.valueOf(voidBean.getCGSTAmount()));
                                    listStrData.add(String.valueOf(voidBean.getSGSTAmount()));
                                    listStrData.add(String.valueOf(voidBean.getCessAmount()));
                                    listStrData.add(String.valueOf(voidBean.getBillAmount()));
                                    listCSVData.add(listStrData);

                                    totVoidBillIGSTAmount += voidBean.getIGSTAmount();
                                    totVoidBillCGSTAmount += voidBean.getCGSTAmount();
                                    totVoidBillSGSTAmount  += voidBean.getSGSTAmount();
                                    totVoidBillcessAmount += voidBean.getCessAmount();
                                    totVoidBillAmount += voidBean.getBillAmount();
                                } while (Report.moveToNext());

                                listStrData = new ArrayList<String>();
                                listStrData.add("Total");
                                listStrData.add("");
                                listStrData.add("");
                                listStrData.add(String.format("%.2f",totVoidBillIGSTAmount));
                                listStrData.add(String.format("%.2f",totVoidBillCGSTAmount));
                                listStrData.add(String.format("%.2f",totVoidBillSGSTAmount));
                                listStrData.add(String.format("%.2f",totVoidBillcessAmount));
                                listStrData.add(String.format("%.2f",totVoidBillAmount));
                                listCSVData.add(listStrData);

                            }else
                            {
                                listCSVData.clear();
                                return "No transaction has been done";
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        finally {
                            if(Report !=null )
                                Report.close();
                        }
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
                        case 0:    // Bill wise Report
                            BillWiseReport billWiseReport = new BillWiseReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", billWiseBeanArrayList);
                            bundle.putDouble("totbillAmt", totBillWiseReportBillAmt);
                            bundle.putDouble("totTax", totBillWiseReportTax);
                            bundle.putDouble("totDisc", totBillWiseReportDisc);
                            billWiseReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.sales_report_container, billWiseReport, BILL_WISE_REPORT);
                            fragmentTransaction.commit();
                            break;
                        case 1: // Day wise report
                            DayWiseReport dayWiseReport = new DayWiseReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", dayWiseBeanArrayList);
                            bundle.putDouble("totbillAmt", totDayWiseReportbillAmt);
                            bundle.putDouble("totSalesTax", totDayWiseReportSalesTax);
                            bundle.putDouble("totServiceTax", totDayWiseReportServiceTax);
                            bundle.putDouble("totcess", totDayWiseReportcess);
                            bundle.putDouble("totIGSTTax", totDayWiseReportIGSTTax);
                            bundle.putDouble("totDis", totDayWiseReportDis);
                            dayWiseReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.sales_report_container, dayWiseReport);
                            fragmentTransaction.commit();
                            break;
                        case 2:    // Duplicate Bill Report
                            DuplicateBillReport duplicateBillReport = new DuplicateBillReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", duplicateBillBeanArrayList);
                            bundle.putDouble("totbillAmt", totDuplicateBillReportBillAmt);
                            bundle.putDouble("totSalesTax", totDuplicateBillReportSalesTax);
                            bundle.putDouble("totServiceTax", totDuplicateBillReportServiceTax);
                            bundle.putDouble("totcess", totDuplicateBillReportCess);
                            bundle.putDouble("totIGSTTax", totDuplicateBillReportIGSTTax);
                            bundle.putDouble("totDis", totDuplicateBillReportDis);
                            duplicateBillReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.sales_report_container, duplicateBillReport);
                            fragmentTransaction.commit();
                            break;
                        case 3: // Month wise Report
                            MonthWiseReport monthWiseReport = new MonthWiseReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", monthWiseBeanArrayList);
                            bundle.putDouble("totbillAmt", totMonthWiseReportBillAmt);
                            bundle.putDouble("totSalesTax", totMonthWiseReportSalesTax);
                            bundle.putDouble("totServiceTax", totMonthWiseReportServiceTax);
                            bundle.putDouble("totcess", totMonthWiseReportCess);
                            bundle.putDouble("totIGSTTax", totMonthWiseReportIGSTTax);
                            bundle.putDouble("totDis", totMonthWiseReportDis);
                            monthWiseReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.sales_report_container, monthWiseReport);
                            fragmentTransaction.commit();
                            break;
                        case 4: // Tax report
                            TaxReport taxReport = new TaxReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", taxReportBeanArrayList);
                            bundle.putDouble("totAmt", totTaxReportAmt);
                            bundle.putDouble("totTax", totTaxReportTax);
                            taxReport.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.sales_report_container, taxReport);
                            fragmentTransaction.commit();
                            break;
                        case 5: // Transaction Report
                            TransactionReport transactionReport = new TransactionReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", transactionReportBeanArrayList);
                            bundle.putDouble("totAmt", totTransactionAmt);
                            bundle.putDouble("totCash", totTransactionCash);
                            bundle.putDouble("totCoupon", totTransactionCoupon);
                            bundle.putDouble("totCard", totTransactionCard);
                            bundle.putDouble("totPetty", totTransactionPetty);
                            bundle.putDouble("totWallet", totTransactionWallet);
                            bundle.putDouble(Constants.MSWIPE_AMOUNT_TOTAL, totTransactionMSwipe);
                            bundle.putDouble(Constants.AEPS_AMOUNT_TOTAL, totTransactionAEPS);
                            transactionReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.sales_report_container, transactionReport);
                            fragmentTransaction.commit();
                            break;

                        case 6: // VoidBill Report
                            VoidBillReport voidBillReport = new VoidBillReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", voidBillReportBeanArrayList);
                            bundle.putDouble(Constants.TOTALIGSTAMOUNT, totVoidBillIGSTAmount);
                            bundle.putDouble(Constants.TOTALCGSTAMOUNT, totVoidBillCGSTAmount);
                            bundle.putDouble(Constants.TOTALSGSTAMOUNT, totVoidBillSGSTAmount);
                            bundle.putDouble(Constants.TOTALcessAMOUNT, totVoidBillcessAmount);
                            bundle.putDouble(Constants.TOTALBILLAMOUNT, totVoidBillAmount);
                            voidBillReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.sales_report_container, voidBillReport);
                            fragmentTransaction.commit();
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
        etReportDateStart.setText("");
        etReportDateEnd.setText("");
        listCSVData.clear();
        DefaultFragmentForEmptyData defaultFragmentForEmptyData = new DefaultFragmentForEmptyData();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.sales_report_container, defaultFragmentForEmptyData);
        fragmentTransaction.commit();
    }
}
