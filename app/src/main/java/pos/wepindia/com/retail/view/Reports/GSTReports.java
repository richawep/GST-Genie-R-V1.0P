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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1B2BBean;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1B2CLBean;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1B2CSBean;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1DocIssuedBean;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR1HSNSummaryBean;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR2RegisteredBean;
import pos.wepindia.com.retail.view.Reports.Bean.GSTR2UnRegisteredBean;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;

/**
 * Created by SachinV on 23-01-2018.
 */

public class GSTReports extends Fragment {

    private static final String TAG = GSTReports.class.getSimpleName();

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
    @BindView(R.id.btn_ReportExport)
    Button btn_ReportExport;
    @BindView(R.id.btn_ReportView)
    Button btn_ReportView;
    @BindView(R.id.gst_report_container)
    FrameLayout gst_report_container;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    private DateTime objDate;
    private String strDate = "";
    private Date startDate_date, endDate_date;
    private ArrayList<GSTR1B2BBean> gstr1B2BBeanArrayList;
    private ArrayList<GSTR1B2CLBean> gstr1B2CLBeanArrayList;
    private ArrayList<GSTR1B2CSBean> gstr1B2CSBeanArrayList;
    private ArrayList<GSTR1DocIssuedBean> gstr1DocIssuedBeanArrayList;
    private ArrayList<GSTR1HSNSummaryBean> gstr1HSNSummaryBeanArrayList;
    private ArrayList<GSTR2RegisteredBean> gstr2RegisteredBeanArrayList;
    private ArrayList<GSTR2UnRegisteredBean> gstr2UnRegisteredBeanArrayList;

    private List<List<String>> listCSVData;
    private PopulateSelectedReportData populateSelectedReportData = null;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gst_report_fragment, container, false);

        myContext = getContext();
        MsgBox = new MessageDialog(myContext);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try{
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

            listCSVData = new ArrayList<List<String>>();
            spnNameSelection.setOnItemSelectedListener(spOnItemSelectListener);

        }catch (Exception e)
        {
            Logger.e(TAG, e.getMessage());
        }
    }

    @OnClick({R.id.btn_ReportDateFrom, R.id.btn_ReportDateTo,
            R.id.btn_ReportPrint, R.id.btn_ReportExport, R.id.btn_ReportView})
    protected void mBtnClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_ReportDateFrom : From();
                break;
            case R.id.btn_ReportDateTo : To();
                break;
            case R.id.btn_ReportPrint : //PrintReport();
                break;
            case R.id.btn_ReportExport : //ExportReport();
                mExportReport();
                break;
            case R.id.btn_ReportView :  ViewReport();
                break;
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
                                strDate = "0" + String.valueOf(dateReportDate.getDayOfMonth())+"-";
                            } else {
                                strDate = String.valueOf(dateReportDate.getDayOfMonth())+"-";
                            }
                            if (dateReportDate.getMonth() < 9) {
                                strDate += "0" + String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            } else {
                                strDate += String.valueOf(dateReportDate.getMonth() + 1) + "-";
                            }

                            strDate += String.valueOf(dateReportDate.getYear());

                            if (DateType == 1) {
                                etReportDateStart.setText(strDate);
                                startDate_date =   new Date(dateReportDate.getYear() - 1900, dateReportDate.getMonth(), dateReportDate.getDayOfMonth());
                            } else {
                                etReportDateEnd.setText(strDate);
                                endDate_date =   new Date(dateReportDate.getYear() - 1900, dateReportDate.getMonth(), dateReportDate.getDayOfMonth());

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

    private int getSpinnerListIndex()
    {
        List<String> SalesReport = Arrays.asList(getResources().getStringArray(R.array.gst_report));

        for (String muom : SalesReport)
        {
            if(muom.contains(spnNameSelection.getSelectedItem().toString()))
                return SalesReport.indexOf(muom);
        }
        return 0;
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

    void ViewReport(){

        String txtStartDate = etReportDateStart.getText().toString();
        String txtEndDate = etReportDateEnd.getText().toString();
        if(txtStartDate.equalsIgnoreCase("") || txtEndDate.equalsIgnoreCase(""))
        {
            MsgBox.Show("Warning", "Please select From & To Date");
        }
        else if (startDate_date.getTime() > endDate_date.getTime())
        {
            MsgBox.Show("Warning", "'From Date' cannot be greater than 'To Date' ");
        }else
        {
            populateSelectedReportData = new PopulateSelectedReportData();
            populateSelectedReportData.execute();
        }

    }

    private class PopulateSelectedReportData extends AsyncTask<Void, String, String> {

        double valtot =0, taxvaltot =0,Itot =0, Ctot =0, Stot =0, Amttot =0,cesstot=0;

        Cursor Report = null;
        Bundle bundle;
        FragmentTransaction fragmentTransaction;
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
            try{
                switch (getSpinnerListIndex()) {
                    case 0:    // GSTR1-B2B
                        try
                        {
                            gstr1B2BBeanArrayList = new ArrayList<>();
                            Cursor cursor = HomeActivity.dbHandler.getOutwardB2b(String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
                            GSTR1B2BBean gstr1B2BBean;
                            if (!(cursor != null && cursor.moveToFirst()))
                            {
                                listCSVData.clear();
                                return "No data for entered period B2B";
//                                MsgBox. setMessage("No data for entered period B2B")
//                                        .setPositiveButton("OK", null)
//                                        .show();
                            }
                            else {
                                int count =1;
                                listCSVData.clear();
                                List<String> listStrDataHeader = new ArrayList<String>();
                                listStrDataHeader.add("S.No");
                                listStrDataHeader.add("Recipient GSTIN");
                                listStrDataHeader.add("Invoice No");
                                listStrDataHeader.add("Invoice Date");
                                listStrDataHeader.add("Value");
                                listStrDataHeader.add("GST Rate");
                                listStrDataHeader.add("Taxable Value");
                                listStrDataHeader.add("IGST Amount");
                                listStrDataHeader.add("CGST Amount");
                                listStrDataHeader.add("SGST Amount");
                                listStrDataHeader.add("cess Amount");
                                listStrDataHeader.add("Cust. State Code");
                                listCSVData.add(listStrDataHeader);
                                do {
                                    gstr1B2BBean = new GSTR1B2BBean();

                                    gstr1B2BBean.setSno(String.valueOf(count));
                                    count++;
                                    gstr1B2BBean.setSub(false);
                                    gstr1B2BBean.setInvoiceNo(cursor.getInt(cursor.getColumnIndex("InvoiceNo")));
                                    String dateInMillis = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                    String dateString = formatter.format(Long.parseLong(dateInMillis));
                                    gstr1B2BBean.setInvoiceDate(dateString);
                                    gstr1B2BBean.setTaxableValue(cursor.getDouble(cursor.getColumnIndex("TaxableValue")));
                                    gstr1B2BBean.setCgstAmt(cursor.getDouble(cursor.getColumnIndex("CGSTAmount")));
                                    gstr1B2BBean.setSgstAmt(cursor.getDouble(cursor.getColumnIndex("SGSTAmount")));
                                    gstr1B2BBean.setIgstAmt(cursor.getDouble(cursor.getColumnIndex("IGSTAmount")));
                                    gstr1B2BBean.setCessAmt(cursor.getDouble(cursor.getColumnIndex("cessAmount")));
                                    gstr1B2BBean.setSubTotal(cursor.getDouble(cursor.getColumnIndex("SubTotal")));

                                    gstr1B2BBean.setCustStateCode(cursor.getString(cursor.getColumnIndex("CustStateCode")));
                                    String gstin1 = cursor.getString(cursor.getColumnIndex("GSTIN"));
                                    if (gstin1 == null) {
                                        gstr1B2BBean.setGstin("");
                                    } else {
                                        gstr1B2BBean.setGstin(cursor.getString(cursor.getColumnIndex("GSTIN")));
                                    }
                                    gstr1B2BBeanArrayList.add(gstr1B2BBean);
                                    listStrData = new ArrayList<String>();
                                    listStrData.add(String.valueOf(gstr1B2BBean.getSno()));
                                    listStrData.add(gstr1B2BBean.getGstin());
                                    listStrData.add(String.valueOf(gstr1B2BBean.getInvoiceNo()));
                                    listStrData.add(gstr1B2BBean.getInvoiceDate());
                                    listStrData.add("");
                                    listStrData.add("");
                                    listStrData.add(String.valueOf(gstr1B2BBean.getTaxableValue()));
                                    listStrData.add(String.valueOf(gstr1B2BBean.getIgstAmt()));
                                    listStrData.add(String.valueOf(gstr1B2BBean.getCgstAmt()));
                                    listStrData.add(String.valueOf(gstr1B2BBean.getSgstAmt()));
                                    listStrData.add(String.valueOf(gstr1B2BBean.getCessAmt()));
                                    listStrData.add(gstr1B2BBean.getCustStateCode());
                                    listCSVData.add(listStrData);
                                    addb2b_items(gstr1B2BBean, gstr1B2BBeanArrayList);
                                } while (cursor.moveToNext());
                            }
                        }
                        catch(Exception e)
                        {
                            listCSVData.clear();
//                            MsgBox. setMessage(e.getMessage())
//                                    .setPositiveButton("OK", null)
//                                    .show();
                            e.printStackTrace();
                        }
                        break;

                    case 1:    // GSTR1-B2CL
                        try
                        {
                            gstr1B2CLBeanArrayList = new ArrayList<>();
                            Cursor cursor = HomeActivity.dbHandler.getOutwardB2Cl(String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
                            GSTR1B2CLBean gstr1B2CLBean;
                            if (!(cursor != null && cursor.moveToFirst()))
                            {
                                listCSVData.clear();
                                return "No data for entered period B2C-L";
                            }
                            else {
                                int count =1;
                                listCSVData.clear();
                                List<String> listStrDataHeader = new ArrayList<String>();
                                listStrDataHeader.add("S.No");
                                listStrDataHeader.add("Recipient State Code");
                                listStrDataHeader.add("Recipient Name");
                                listStrDataHeader.add("Invoice No");
                                listStrDataHeader.add("Invoice Date");
                                listStrDataHeader.add("Value");
                                listStrDataHeader.add("IGST Rate");
                                listStrDataHeader.add("Taxable Value");
                                listStrDataHeader.add("IGST Amount");
                                listStrDataHeader.add("cess Amount");
                                listCSVData.add(listStrDataHeader);
                                do
                                {
                                    gstr1B2CLBean = new GSTR1B2CLBean();
                                    String POS_str = cursor.getString(cursor.getColumnIndex("POS"));
                                    String custStateCode_str = cursor.getString(cursor.getColumnIndex("CustStateCode"));
                                    if (POS_str.equals("")== false && !POS_str.equals(custStateCode_str))  { // for interstate only + >2.5l
                                        gstr1B2CLBean.setSno(String.valueOf(count));
                                        count++;
                                        gstr1B2CLBean.setSub(false);
                                        gstr1B2CLBean.setRecipientStateCode(cursor.getString(cursor.getColumnIndex("CustStateCode")));
                                        gstr1B2CLBean.setRecipientName(cursor.getString(cursor.getColumnIndex("CustName")));
                                        gstr1B2CLBean.setInvoiceNo(cursor.getInt(cursor.getColumnIndex("InvoiceNo")));
                                        String dateInMillis = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                                        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                        String dateString = formatter.format(Long.parseLong(dateInMillis));
                                        gstr1B2CLBean.setInvoiceDate(dateString);
                                        gstr1B2CLBean.setTaxableValue(cursor.getDouble(cursor.getColumnIndex("TaxableValue")));
                                        gstr1B2CLBean.setIgstAmt(cursor.getDouble(cursor.getColumnIndex("IGSTAmount")));
                                        gstr1B2CLBean.setCessAmt(cursor.getDouble(cursor.getColumnIndex("cessAmount")));
                                        gstr1B2CLBean.setSubTotal(cursor.getDouble(cursor.getColumnIndex("SubTotal")));
                                        gstr1B2CLBean.setRecipientName(cursor.getString(cursor.getColumnIndex("CustName")));
                                        gstr1B2CLBeanArrayList.add(gstr1B2CLBean);
                                        listStrData = new ArrayList<String>();
                                        listStrData.add(gstr1B2CLBean.getSno());
                                        listStrData.add(gstr1B2CLBean.getRecipientStateCode());
                                        listStrData.add(gstr1B2CLBean.getRecipientName());
                                        listStrData.add(String.valueOf(gstr1B2CLBean.getInvoiceNo()));
                                        listStrData.add(gstr1B2CLBean.getInvoiceDate());
                                        listStrData.add("");
                                        listStrData.add("");
                                        listStrData.add(String.valueOf(gstr1B2CLBean.getTaxableValue()));
                                        listStrData.add(String.valueOf(gstr1B2CLBean.getIgstAmt()));
                                        listStrData.add(String.valueOf(gstr1B2CLBean.getCessAmt()));
                                        listCSVData.add(listStrData);
                                        addb2cl_items(gstr1B2CLBean, gstr1B2CLBeanArrayList);
                                    }
                                } while (cursor.moveToNext());
                            }
                        }
                        catch(Exception e)
                        {
                            listCSVData.clear();
                           /* MsgBox. setMessage(e.getMessage())
                                    .setPositiveButton("OK", null)
                                    .show();*/
                        }
                        break;

                    case 2:    // GSTR1-B2CS
                        gstr1B2CSBeanArrayList = new ArrayList<>();
                        try
                        {
                            Cursor cursor = HomeActivity.dbHandler.getOutwardB2Cs(String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
                            GSTR1B2CSBean gstr1B2CSBean;

                            if (!(cursor != null && cursor.moveToFirst()) )
                            {
                                listCSVData.clear();
                                return "No data for entered period B2C-S";
                            }
                            else {
                                if (cursor.moveToFirst()) {
                                    listCSVData.clear();
                                    List<String> listStrDataHeader = new ArrayList<String>();
                                    listStrDataHeader.add("S.No");
                                    listStrDataHeader.add("GST Rate");
                                    listStrDataHeader.add("Taxable Value");
                                    listStrDataHeader.add("IGST Amount");
                                    listStrDataHeader.add("CGST Amount");
                                    listStrDataHeader.add("SGST Amount");
                                    listStrDataHeader.add("cess Amount");
                                    listCSVData.add(listStrDataHeader);
                                    do {

                                        String POS_str = cursor.getString(cursor.getColumnIndex("POS"));
                                        String custStateCode_str = cursor.getString(cursor.getColumnIndex("CustStateCode"));
                                        double TaxableValue_f = cursor.getDouble(cursor.getColumnIndex("TaxableValue"));
                                        if ((POS_str.equals("")== false  && !POS_str.equals(custStateCode_str)&& TaxableValue_f >250000 )) {

                                        }else{
                                            // for interstate + interstate only + <=2.5l
                                            String InvNo =cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                                            String InvDate1 = cursor.getString(cursor.getColumnIndex("InvoiceDate"));

                                            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                            String InvDate = formatter.format(Long.parseLong(InvDate1));

                                            String custname_str = cursor.getString(cursor.getColumnIndex("CustName"));
                                            String statecode_str = cursor.getString(cursor.getColumnIndex("CustStateCode"));

                                            Cursor rowcursor = HomeActivity.dbHandler.getitems_b2cl(InvNo, InvDate1,custname_str,statecode_str);
                                            if (rowcursor == null)
                                            {
                                                listCSVData.clear();
                                                MsgBox. setMessage("No items for Invoice No : "+InvNo+" & Invoice Date : "+InvDate)
                                                        .setPositiveButton("OK",null)
                                                        .show();
                                            }
                                            else { // bill level
                                                try{
                                                    while(rowcursor.moveToNext()) {

                                                        gstr1B2CSBean = new GSTR1B2CSBean();

                                                        String SupplyType_str;
                                                        double subtotal_f,taxablevalue_f,CGSTRate_f, CGSTAmount_f, SGSTRate_f;
                                                        double SGSTAmount_f,IGSTRate_f, IGSTAmount_f,cessAmt;
                                                        int found  =0;

                                                        // supply type (g/s)
                                                        SupplyType_str = rowcursor.getString(rowcursor.getColumnIndex("SupplyType"));

                                                        String HSN = rowcursor.getString(rowcursor.getColumnIndex("HSNCode"));
                                                        String desc = rowcursor.getString(rowcursor.getColumnIndex("ItemName"));
                                                        if (HSN==null || HSN.equals("")) {
                                                            HSN = "_"+desc;
                                                        }
                                                        taxablevalue_f = Double.parseDouble(rowcursor.getString(rowcursor.getColumnIndex("TaxableValue")));

                                                        String igstrate_str = rowcursor.getString(rowcursor.getColumnIndex("IGSTRate"));
                                                        if (igstrate_str== null) {
                                                            IGSTRate_f =0;
                                                        }
                                                        else {
                                                            IGSTRate_f = Double.parseDouble(igstrate_str);
                                                        }
                                                        String igstamt_str = String.format("%.2f",rowcursor.getDouble(rowcursor.getColumnIndex("IGSTAmount")));
                                                        if (igstamt_str== null ) {
                                                            IGSTAmount_f =0;
                                                        }
                                                        else {
                                                            IGSTAmount_f = Double.parseDouble(igstamt_str);
                                                        }
                                                        String cgstrate_str = rowcursor.getString(rowcursor.getColumnIndex("CGSTRate"));
                                                        if (cgstrate_str== null ) {
                                                            CGSTRate_f =0;
                                                        }
                                                        else {
                                                            CGSTRate_f = Double.parseDouble(cgstrate_str);
                                                        }
                                                        String cgstamt_str = rowcursor.getString(rowcursor.getColumnIndex("CGSTAmount"));
                                                        if (cgstamt_str== null ) {
                                                            CGSTAmount_f =0;
                                                        }
                                                        else {
                                                            CGSTAmount_f = Double.parseDouble(cgstamt_str);
                                                        }
                                                        String sgstrate_str = rowcursor.getString(rowcursor.getColumnIndex("SGSTRate"));
                                                        if (sgstrate_str== null ) {
                                                            SGSTRate_f =0;
                                                        }
                                                        else {
                                                            SGSTRate_f = Double.parseDouble(sgstrate_str);
                                                        }
                                                        String sgstamt_str = rowcursor.getString(rowcursor.getColumnIndex("SGSTAmount"));
                                                        if (sgstamt_str== null ) {
                                                            SGSTAmount_f =0;
                                                        }
                                                        else {
                                                            SGSTAmount_f = Double.parseDouble(sgstamt_str);
                                                        }
                                                        String cessamt_str = rowcursor.getString(rowcursor.getColumnIndex("cessAmount"));
                                                        if (cessamt_str== null ) {
                                                            cessAmt =0;
                                                        }
                                                        else {
                                                            cessAmt = Double.parseDouble(cessamt_str);
                                                        }

                                                        subtotal_f = taxablevalue_f + IGSTAmount_f + CGSTAmount_f + SGSTAmount_f;

                                                        String ProAss = "";
                                                        double gstrate = IGSTRate_f+CGSTRate_f+SGSTRate_f;
                                                        String suply_type = "";
                                                        if(IGSTAmount_f >0)
                                                            suply_type = "INTER";
                                                        else
                                                            suply_type = "INTRA" ;

                                                        gstr1B2CSBean.setSply_ty(suply_type);
                                                        gstr1B2CSBean.setSupplyType(SupplyType_str);
                                                        gstr1B2CSBean.setHSNCode(HSN);
                                                        gstr1B2CSBean.setPlaceOfSupply(POS_str);
                                                        gstr1B2CSBean.setStateCode(custStateCode_str);
                                                        gstr1B2CSBean.setTaxableValue(taxablevalue_f);
                                                        gstr1B2CSBean.setIGSTRate(IGSTRate_f);
                                                        gstr1B2CSBean.setGSTRate(gstrate);
                                                        gstr1B2CSBean.setCessAmt(cessAmt);
                                                        gstr1B2CSBean.setIGSTAmt(IGSTAmount_f);
                                                        gstr1B2CSBean.setCGSTRate(CGSTRate_f);
                                                        gstr1B2CSBean.setCGSTAmt(CGSTAmount_f);
                                                        gstr1B2CSBean.setSGSTRate(SGSTRate_f);
                                                        gstr1B2CSBean.setSGSTAmt(SGSTAmount_f);
                                                        gstr1B2CSBean.setProAss(ProAss);
                                                        gstr1B2CSBean.setSubTotal(subtotal_f);
                                                        if (gstr1B2CSBeanArrayList.size() == 0) // empty list
                                                        {
                                                            gstr1B2CSBeanArrayList.add(gstr1B2CSBean);
                                                        }
                                                        else
                                                        {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  found = 0;
                                                            for(GSTR1B2CSBean data_s: gstr1B2CSBeanArrayList){

                                                                if(data_s.getGSTRate()==gstrate &&
                                                                        data_s.getSply_ty().equalsIgnoreCase(suply_type))
                                                                {
                                                                    // taxval
                                                                    double taxableval = data_s.getTaxableValue();
                                                                    taxableval += taxablevalue_f;
                                                                    data_s.setTaxableValue(taxableval);

                                                                    // IGST Amt
                                                                    double igstamt_temp = data_s.getIGSTAmt();
                                                                    igstamt_temp += IGSTAmount_f;
                                                                    data_s.setIGSTAmt(igstamt_temp);

                                                                    // CGST Amt
                                                                    double cgstamt_temp = data_s.getCGSTAmt();
                                                                    cgstamt_temp += CGSTAmount_f;
                                                                    data_s.setCGSTAmt(cgstamt_temp);

                                                                    // SGST Amt
                                                                    double sgstamt_temp = data_s.getSGSTAmt();
                                                                    sgstamt_temp += SGSTAmount_f;
                                                                    data_s.setSGSTAmt(sgstamt_temp);

                                                                    double cessamt_temp = data_s.getCessAmt();
                                                                    cessamt_temp += cessAmt;
                                                                    data_s.setCessAmt(cessamt_temp);

                                                                    //SubTotal
                                                                    double subtot = data_s.getSubTotal();
                                                                    subtot += subtotal_f;
                                                                    data_s.setSubTotal(Double.parseDouble(String.valueOf(subtot)));

                                                                    found =1;
                                                                    break;

                                                                }
                                                            }
                                                            if (found ==0)
                                                            {
                                                                gstr1B2CSBeanArrayList.add(gstr1B2CSBean);

                                                            }
                                                        }

                                                    }

                                                }
                                                catch (Exception e)
                                                {
//                                                    MsgBox .setTitle("Error while fetching items details")
//                                                            .setIcon(R.mipmap.ic_company_logo)
//                                                            .setMessage(e.getMessage())
//                                                            .setPositiveButton("OK",null)
//                                                            .show();
                                                }
                                            }
                                        }
                                    } while (cursor.moveToNext());
                                }
                            }
                        }
                        catch(Exception e)
                        {
//                            MsgBox. setMessage(e.getMessage())
//                                    .setPositiveButton("OK", null)
//                                    .show();
                        }
                        int count = 1;
                        for (GSTR1B2CSBean bean : gstr1B2CSBeanArrayList) {
                            bean.setSno(count);
                            count++;
                        }
                        for (GSTR1B2CSBean data_s: gstr1B2CSBeanArrayList){
                            listStrData = new ArrayList<String>();
                            listStrData.add(String.valueOf(data_s.getSno()));
                            listStrData.add(String.valueOf(data_s.getGSTRate()));
                            listStrData.add(String.valueOf(data_s.getTaxableValue()));
                            listStrData.add(String.valueOf(data_s.getIGSTAmt()));
                            listStrData.add(String.valueOf(data_s.getCGSTAmt()));
                            listStrData.add(String.valueOf(data_s.getSGSTAmt()));
                            listStrData.add(String.valueOf(data_s.getCessAmt()));
                            listCSVData.add(listStrData);
                        }
                        break;

                    case 3: // GSTR1-HSN Summary
                        gstr1HSNSummaryBeanArrayList = new ArrayList<>();
                        Cursor cursor = HomeActivity.dbHandler.getitems_outward_details(String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
                        GSTR1HSNSummaryBean gstr1HSNSummaryBean;
                        if (cursor == null)
                        {
                            return "No invoice found to display HSN Summay";
                        }
                        else
                        {
                            try{
                                if (cursor.moveToFirst()) {
                                    int count_1 = 0;
                                    listCSVData.clear();
                                    List<String> listStrDataHeader = new ArrayList<String>();
                                    listStrDataHeader.add("S.No");
                                    listStrDataHeader.add("HSN Code");
                                    listStrDataHeader.add("Item Name");
                                    listStrDataHeader.add("UOM");
                                    listStrDataHeader.add("Quantity");
                                    listStrDataHeader.add("Value");
                                    listStrDataHeader.add("GST Rate");
                                    listStrDataHeader.add("Taxable Value");
                                    listStrDataHeader.add("IGST Amount");
                                    listStrDataHeader.add("CGST Amount");
                                    listStrDataHeader.add("SGST Amount");
                                    listStrDataHeader.add("cess Amount");
                                    listCSVData.add(listStrDataHeader);
                                    do {
                                        gstr1HSNSummaryBean = new GSTR1HSNSummaryBean();

                                        gstr1HSNSummaryBean.setSno(++count_1);
                                        gstr1HSNSummaryBean.setSupplyType(cursor.getString(cursor.getColumnIndex("SupplyType")));
                                        gstr1HSNSummaryBean.setHsnCode(cursor.getString(cursor.getColumnIndex("HSNCode")));
                                        gstr1HSNSummaryBean.setItemName(cursor.getString(cursor.getColumnIndex("ItemName")));
                                        gstr1HSNSummaryBean.setUom(cursor.getString(cursor.getColumnIndex("UOM")));
                                        gstr1HSNSummaryBean.setQuantity(cursor.getDouble(cursor.getColumnIndex("Quantity")));
                                        gstr1HSNSummaryBean.setValue(cursor.getDouble(cursor.getColumnIndex("Value")));
                                        gstr1HSNSummaryBean.setTaxableVlue(cursor.getDouble(cursor.getColumnIndex("TaxableValue")));
                                        gstr1HSNSummaryBean.setDiscount(cursor.getDouble(cursor.getColumnIndex("DiscountAmount")));
                                        gstr1HSNSummaryBean.setIgstRate(cursor.getDouble(cursor.getColumnIndex("IGSTRate")));
                                        gstr1HSNSummaryBean.setSgstRate(cursor.getDouble(cursor.getColumnIndex("SGSTRate")));
                                        gstr1HSNSummaryBean.setCgstRate(cursor.getDouble(cursor.getColumnIndex("CGSTRate")));
                                        gstr1HSNSummaryBean.setCessRate(cursor.getDouble(cursor.getColumnIndex("cessRate")));
                                        gstr1HSNSummaryBean.setIgstAmt(cursor.getDouble(cursor.getColumnIndex("IGSTAmount")));
                                        gstr1HSNSummaryBean.setCgstAmt(cursor.getDouble(cursor.getColumnIndex("CGSTAmount")));
                                        gstr1HSNSummaryBean.setSgstAmt(cursor.getDouble(cursor.getColumnIndex("SGSTAmount")));
                                        gstr1HSNSummaryBean.setCessAmt(cursor.getDouble(cursor.getColumnIndex("cessAmount")));

                                        int found = 0;
                                        for (GSTR1HSNSummaryBean data : gstr1HSNSummaryBeanArrayList) {
                                            if (gstr1HSNSummaryBean.getHsnCode() != null && !gstr1HSNSummaryBean.getHsnCode().equals("")) {
                                                if (data.getHsnCode().equals(gstr1HSNSummaryBean.getHsnCode())) {
                                                    double qty_prev = data.getQuantity();
                                                    data.setQuantity(qty_prev + gstr1HSNSummaryBean.getQuantity());
//                                                    double val_prev = data.getValue();
//                                                    data.setValue(val_prev + gstr1HSNSummaryBean.getValue());
                                                    double txval_prev = data.getTaxableVlue();
                                                    data.setTaxableVlue(txval_prev + gstr1HSNSummaryBean.getTaxableVlue());
                                                    double iamt_prev = data.getIgstAmt();
                                                    data.setIgstAmt(iamt_prev + gstr1HSNSummaryBean.getIgstAmt());
                                                    double camt_prev = data.getCgstAmt();
                                                    data.setCgstAmt(camt_prev + gstr1HSNSummaryBean.getCgstAmt());
                                                    double samt_prev = data.getSgstAmt();
                                                    data.setSgstAmt(samt_prev + gstr1HSNSummaryBean.getSgstAmt());
                                                    double csamt_prev = data.getCessAmt();
                                                    data.setCessAmt(csamt_prev + gstr1HSNSummaryBean.getCessAmt());
                                                    found = 1;
                                                    break;
                                                }
                                            }
                                        }
                                        if (found == 0 && gstr1HSNSummaryBean.getHsnCode() != null && !gstr1HSNSummaryBean.getHsnCode().equals("")) {
                                            gstr1HSNSummaryBeanArrayList.add(gstr1HSNSummaryBean);
                                        }
                                    } while (cursor.moveToNext());

                                    for (GSTR1HSNSummaryBean data : gstr1HSNSummaryBeanArrayList) {
                                        listStrData = new ArrayList<String>();
                                        listStrData.add(String.valueOf(data.getSno()));
                                        listStrData.add(String.valueOf(data.getHsnCode()));
                                        listStrData.add(data.getItemName());
                                        listStrData.add(data.getUom());
                                        listStrData.add(String.valueOf(data.getQuantity()));
                                        listStrData.add(String.valueOf(data.getValue()));
                                        if(data.getIgstRate() > 0) {
                                            listStrData.add(String.valueOf(data.getIgstRate()));
                                        } else {
                                            listStrData.add(String.valueOf(data.getCgstRate() + data.getSgstRate()));
                                        }
                                        listStrData.add(String.valueOf(data.getTaxableVlue()));
                                        listStrData.add(String.valueOf(data.getIgstAmt()));
                                        listStrData.add(String.valueOf(data.getCgstAmt()));
                                        listStrData.add(String.valueOf(data.getSgstAmt()));
                                        listStrData.add(String.valueOf(data.getCessAmt()));
                                        listCSVData.add(listStrData);
                                    }
                                }else
                                {
                                    listCSVData.clear();
                                    return "No invoice found for this period";
                                }
                            }catch ( Exception e)
                            {
                                listCSVData.clear();
                                e.printStackTrace();
//                                MsgBox.Show("Error",e.getMessage());
                            }
                        }
                        break;
                    case 4 : // Doc issued
                        gstr1DocIssuedBeanArrayList = new ArrayList<>();
                        GSTR1DocIssuedBean gstr1DocIssuedBean;

                        listCSVData.clear();
                        List<String> listStrDataHeader;

                        listStrDataHeader = new ArrayList<String>();
                        listStrDataHeader.add("S.No");
                        listStrDataHeader.add("Nature of Document");
                        listStrDataHeader.add("From");
                        listStrDataHeader.add("To");
                        listStrDataHeader.add("Total Number");
                        listStrDataHeader.add("Cancelled");
                        listStrDataHeader.add("Net Issued");
                        listCSVData.add(listStrDataHeader);

                        // Invoices for sale
                    {
                        cursor = HomeActivity.dbHandler.getAllInvoices_outward(String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
                        if (cursor!=null && cursor.moveToFirst()) {
                            gstr1DocIssuedBean = new GSTR1DocIssuedBean();

                            String from = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                            String to = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                            int totnum = 0;
                            int cancel = 0;
                            do {
                                to = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                                totnum++;
                                if (cursor.getInt(cursor.getColumnIndex("BillStatus")) == 0)
                                    cancel++;

                            } while (cursor.moveToNext());

                            gstr1DocIssuedBean.setSno(1);
                            gstr1DocIssuedBean.setNatureOfDoc("Invoices for outward supply");
                            gstr1DocIssuedBean.setFrom(String.valueOf(from));
                            gstr1DocIssuedBean.setTo(String.valueOf(to));
                            gstr1DocIssuedBean.setTotalNumber(totnum);
                            gstr1DocIssuedBean.setCancelled(cancel);
                            gstr1DocIssuedBean.setNetIssued(totnum-cancel);

                            gstr1DocIssuedBeanArrayList.add(gstr1DocIssuedBean);
                            listStrDataHeader = new ArrayList<String>();
                            listStrDataHeader.add("1");
                            listStrDataHeader.add("Invoices for outward supply");
                            listStrDataHeader.add(String.valueOf(from));
                            listStrDataHeader.add(String.valueOf(to));
                            listStrDataHeader.add(String.valueOf(totnum));
                            listStrDataHeader.add(String.valueOf(cancel));
                            listStrDataHeader.add(String.valueOf(totnum-cancel));
                            listCSVData.add(listStrDataHeader);
                        }
                    }

                    // Invoices for inward supply from unregistered person
                    {
                        cursor = HomeActivity.dbHandler.getPurchaseOrder_for_unregistered(String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
                        if (cursor!=null && cursor.moveToFirst())
                        {
                            gstr1DocIssuedBean = new GSTR1DocIssuedBean();

                            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
                            if (gstin ==null  || gstin.equals(""))
                            {
                                String from = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                                String to = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                                int totnum =0;
                                int cancel =0;
                                do {
                                    gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
                                    if (gstin ==null  || gstin.equals("")) {
                                        to = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                                        totnum++;
                                /*if (cursor.getInt(cursor.getColumnIndex("BillStatus")) == 0)
                                    cancel++;*/
                                    }

                                }while(cursor.moveToNext());

                                gstr1DocIssuedBean.setSno(2);
                                gstr1DocIssuedBean.setNatureOfDoc("Invoices for inward supply for unregistered supplier");
                                gstr1DocIssuedBean.setFrom(String.valueOf(from));
                                gstr1DocIssuedBean.setTo(String.valueOf(to));
                                gstr1DocIssuedBean.setTotalNumber(totnum);
                                gstr1DocIssuedBean.setCancelled(cancel);
                                gstr1DocIssuedBean.setNetIssued(totnum-cancel);

                                gstr1DocIssuedBeanArrayList.add(gstr1DocIssuedBean);
                                listStrDataHeader = new ArrayList<String>();
                                listStrDataHeader.add("2");
                                listStrDataHeader.add("Invoices for inward supply for unregistered supplier");
                                listStrDataHeader.add(String.valueOf(from));
                                listStrDataHeader.add(String.valueOf(to));
                                listStrDataHeader.add(String.valueOf(totnum));
                                listStrDataHeader.add(String.valueOf(cancel));
                                listStrDataHeader.add(String.valueOf(totnum-cancel));
                                listCSVData.add(listStrDataHeader);
                            }
                        }
                    }
                    // Credit Note
                    {
//                            cursor = HomeActivity.dbHandler.getGSTR1_CDN(String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
//                            if (cursor!=null && cursor.moveToFirst())
//                            {
//                                gstr1DocIssuedBean = new GSTR1DocIssuedBean();
//
//                                String from = cursor.getString(cursor.getColumnIndex("NoteNo"));
//                                String to = cursor.getString(cursor.getColumnIndex("NoteNo"));
//                                int totnum =0;
//                                int cancel =0;
//                                do {
//                                    to = cursor.getString(cursor.getColumnIndex("NoteNo"));
//                                    totnum++;
//                        /*if(cursor.getInt(cursor.getColumnIndex("BillStatus"))==0)
//                            cancel++;*/
//
//                                }while(cursor.moveToNext());
//
//                                gstr1DocIssuedBean.setSno(3);
//                                gstr1DocIssuedBean.setNatureOfDoc("Credit/Debit Notes");
//                                gstr1DocIssuedBean.setFrom(String.valueOf(from));
//                                gstr1DocIssuedBean.setTo(String.valueOf(to));
//                                gstr1DocIssuedBean.setTotalNumber(totnum);
//                                gstr1DocIssuedBean.setCancelled(cancel);
//                                gstr1DocIssuedBean.setNetIssued(totnum-cancel);
//
//                                gstr1DocIssuedBeanArrayList.add(gstr1DocIssuedBean);
//                            }
                    }
                    break;

                }
            }catch (Exception e)
            {
                Logger.e(TAG, "Error in async task do in background method populate data." + e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strResponse) {
            super.onPostExecute(strResponse);
            gst_report_container.removeAllViews();
            if (strResponse != null && !strResponse.isEmpty()) {
                MsgBox.Show("Warning", strResponse);
            }
            try {
                if (strResponse == null) {
                    switch (getSpinnerListIndex()) {
                        case 0:    // GSTR1-B2B
                            GSTR1B2BReport gstr1B2BReport = new GSTR1B2BReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", gstr1B2BBeanArrayList);
                            gstr1B2BReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.gst_report_container, gstr1B2BReport);
                            fragmentTransaction.commit();
                            break;

                        case 1:    // GSTR1-B2CL
                            GSTR1B2CLReport gstr1B2CLReport = new GSTR1B2CLReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", gstr1B2CLBeanArrayList);
                            gstr1B2CLReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.gst_report_container, gstr1B2CLReport);
                            fragmentTransaction.commit();
                            break;

                        case 2:    // GSTR1-B2CS
                            GSTR1B2CSReport gstr1B2CSReport = new GSTR1B2CSReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", gstr1B2CSBeanArrayList);
                            gstr1B2CSReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.gst_report_container, gstr1B2CSReport);
                            fragmentTransaction.commit();
                            break;

                        case 3: // GSTR1-HSN Summary
                            GSTR1HSNSumReport gstr1HSNSumReport = new GSTR1HSNSumReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", gstr1HSNSummaryBeanArrayList);
                            gstr1HSNSumReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.gst_report_container, gstr1HSNSumReport);
                            fragmentTransaction.commit();
                            break;

                        case 4: // Documents Issued
                            GSTR1DocIssuedReport gstr1DocumentIssuedReport = new GSTR1DocIssuedReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", gstr1DocIssuedBeanArrayList);
                            gstr1DocumentIssuedReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.gst_report_container, gstr1DocumentIssuedReport);
                            fragmentTransaction.commit();
                            break;

                        case 9: // GSTR2-Registered
                            GSTR2RegisteredReport gstr2RegisteredReport = new GSTR2RegisteredReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", gstr2RegisteredBeanArrayList);
                            bundle.putDouble("taxvaltot", taxvaltot);
                            bundle.putDouble("Itot", Itot);
                            bundle.putDouble("Ctot", Ctot);
                            bundle.putDouble("Stot", Stot);
                            bundle.putDouble("cesstot", cesstot);
                            bundle.putDouble("Amttot", Amttot);
                            gstr2RegisteredReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.gst_report_container, gstr2RegisteredReport);
                            fragmentTransaction.commit();
                            break;

                        case 10:    // GSTR2-UnRegistered
                            GSTR2UnRegisteredReport gstr2UnRegisteredReport = new GSTR2UnRegisteredReport();
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", gstr2UnRegisteredBeanArrayList);
                            bundle.putDouble("taxvaltot", taxvaltot);
                            bundle.putDouble("Itot", Itot);
                            bundle.putDouble("Ctot", Ctot);
                            bundle.putDouble("Stot", Stot);
                            bundle.putDouble("cesstot", cesstot);
                            bundle.putDouble("Amttot", Amttot);
                            gstr2UnRegisteredReport.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.gst_report_container, gstr2UnRegisteredReport);
                            fragmentTransaction.commit();
                            break;

                        case 11:    // GSTR2-Registered Amend

                            break;

                        case 12:    // GSTR2-UnRegistered Amend

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

    void addb2b_items(GSTR1B2BBean gstr1B2BBeanTemp, ArrayList<GSTR1B2BBean> gstr1B2BBeanArrayList1)
    {
        String count_str[]= {"i","ii","iii","iv","v","vi","vii","viii","ix","x","xi","xii","xiii","xiv","xv","xvi","xvii","xviii","xix","xx"};
        List<String> listStrData = null;
        try{

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = sdf.parse(gstr1B2BBeanTemp.getInvoiceDate());
            long millis = date.getTime();

            GSTR1B2BBean gstr1B2BBeanSub;

            Cursor cursor = HomeActivity.dbHandler.getitems_b2b(String.valueOf(gstr1B2BBeanTemp.getInvoiceNo()), String.valueOf(millis), gstr1B2BBeanTemp.getGstin());
            if (cursor == null)
            {
                MsgBox. setMessage("No items for Invoice No : "+gstr1B2BBeanTemp.getInvoiceNo()+" & Invoice Date : "+gstr1B2BBeanTemp.getInvoiceDate())
                        .setPositiveButton("OK",null)
                        .show();
            }
            else
            {

                if (cursor.moveToFirst()) {

                    int count = 0;

                    do {

                        gstr1B2BBeanSub = new GSTR1B2BBean();

                        gstr1B2BBeanSub.setSno(count_str[count]);
                        count++;
                        gstr1B2BBeanSub.setSub(true);
                        gstr1B2BBeanSub.setSupplyType(cursor.getString(cursor.getColumnIndex("SupplyType")));
                        String HSN = cursor.getString(cursor.getColumnIndex("HSNCode"));
                        if (HSN==null) {
                            gstr1B2BBeanSub.setHsnCode("");
                        } else {
                            gstr1B2BBeanSub.setHsnCode(HSN);
                        }

                        gstr1B2BBeanSub.setValue(cursor.getDouble(cursor.getColumnIndex("Value")));
                        gstr1B2BBeanSub.setQuantity(cursor.getDouble(cursor.getColumnIndex("Quantity")));
                        gstr1B2BBeanSub.setTaxableValue(cursor.getDouble(cursor.getColumnIndex("TaxableValue")));
                        gstr1B2BBeanSub.setCgstAmt(cursor.getDouble(cursor.getColumnIndex("CGSTAmount")));
                        gstr1B2BBeanSub.setSgstAmt(cursor.getDouble(cursor.getColumnIndex("SGSTAmount")));
                        gstr1B2BBeanSub.setIgstAmt(cursor.getDouble(cursor.getColumnIndex("IGSTAmount")));
                        gstr1B2BBeanSub.setCessAmt(cursor.getDouble(cursor.getColumnIndex("cessAmount")));

                        String gstrate = String.format("%.2f",cursor.getDouble(cursor.getColumnIndex("IGSTRate")));
                        double cgstRate = 0.00, sgstRate = 0.00;
                        if (gstrate== null || gstrate.equals("0")|| gstrate.equals("")|| gstrate.equals("0.00"))
                        {
                            cgstRate = cursor.getDouble(cursor.getColumnIndex("CGSTRate"));
                            sgstRate = cursor.getDouble(cursor.getColumnIndex("SGSTRate"));
                            gstrate = String.format("%.2f",cgstRate+sgstRate);
                        }
                        gstr1B2BBeanSub.setGstRate(Double.parseDouble(gstrate));

                        gstr1B2BBeanArrayList1.add(gstr1B2BBeanSub);
                        listStrData = new ArrayList<String>();
                        listStrData.add(String.valueOf(gstr1B2BBeanSub.getSno()));
                        listStrData.add("");
                        listStrData.add("");
                        listStrData.add("");
                        listStrData.add(String.valueOf(gstr1B2BBeanSub.getValue()));
                        listStrData.add(String.valueOf(gstr1B2BBeanSub.getGstRate()));
                        listStrData.add(String.valueOf(gstr1B2BBeanSub.getTaxableValue()));
                        listStrData.add(String.valueOf(gstr1B2BBeanSub.getIgstAmt()));
                        listStrData.add(String.valueOf(gstr1B2BBeanSub.getCgstAmt()));
                        listStrData.add(String.valueOf(gstr1B2BBeanSub.getSgstAmt()));
                        listStrData.add(String.valueOf(gstr1B2BBeanSub.getCessAmt()));
                        listStrData.add("");
                        listCSVData.add(listStrData);

                    } while (cursor.moveToNext());
                }
            } // end else
        }catch ( Exception e)
        {
            e.printStackTrace();
        }
    }

    void addb2cl_items(GSTR1B2CLBean gstr1B2CLBeanTemp, ArrayList<GSTR1B2CLBean> gstr1B2CLBeanArrayList1)
    {
        String count_str[]= {"i","ii","iii","iv","v","vi","vii","viii","ix","x","xi","xii","xiii","xiv","xv","xvi","xvii","xviii","xix","xx"};
        Cursor cursor = null;
        List<String> listStrData = null;
        GSTR1B2CLBean gstr1B2CLBeanSub;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date = null;
        try {
            date = sdf.parse(gstr1B2CLBeanTemp.getInvoiceDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();

        cursor = HomeActivity.dbHandler.getitems_b2cl(String.valueOf(gstr1B2CLBeanTemp.getInvoiceNo()), String.valueOf(millis), gstr1B2CLBeanTemp.getRecipientName(),gstr1B2CLBeanTemp.getRecipientStateCode());
        if (cursor == null)
        {
            //MsgBox = new AlertDialog.Builder(myContext);
            MsgBox. setMessage("No items for Invoice No : "+gstr1B2CLBeanTemp.getInvoiceNo()+" & Invoice Date : "+gstr1B2CLBeanTemp.getInvoiceDate())
                    .setPositiveButton("OK",null)
                    .show();
        }
        else
        {
            try{

                if (cursor.moveToFirst()) {
                    int count =0;
                    do {

                        gstr1B2CLBeanSub = new GSTR1B2CLBean();

                        gstr1B2CLBeanSub.setSno(count_str[count]);
                        count++;
                        gstr1B2CLBeanSub.setSub(true);

                        gstr1B2CLBeanSub.setSupplyType(cursor.getString(cursor.getColumnIndex("SupplyType")));

                        String HSN = cursor.getString(cursor.getColumnIndex("HSNCode"));
                        String desc = cursor.getString(cursor.getColumnIndex("ItemName"));
                        if (HSN==null || HSN.equals(""))
                        {
                            HSN = "_"+desc;
                        }
                        gstr1B2CLBeanSub.setHsnCode(HSN);

                        gstr1B2CLBeanSub.setValue(cursor.getDouble(cursor.getColumnIndex("Value")));
                        gstr1B2CLBeanSub.setQuantity(cursor.getDouble(cursor.getColumnIndex("Quantity")));
                        gstr1B2CLBeanSub.setTaxableValue(cursor.getDouble(cursor.getColumnIndex("TaxableValue")));
                        gstr1B2CLBeanSub.setIgstRate(cursor.getDouble(cursor.getColumnIndex("IGSTRate")));
                        gstr1B2CLBeanSub.setIgstAmt(cursor.getDouble(cursor.getColumnIndex("IGSTAmount")));

                        gstr1B2CLBeanSub.setCessAmt(cursor.getDouble(cursor.getColumnIndex("cessAmount")));
                        gstr1B2CLBeanSub.setSubTotal(gstr1B2CLBeanSub.getTaxableValue() + gstr1B2CLBeanSub.getIgstAmt());

                        gstr1B2CLBeanArrayList1.add(gstr1B2CLBeanSub);
                        listStrData = new ArrayList<String>();
                        listStrData.add(gstr1B2CLBeanSub.getSno());
                        listStrData.add("");
                        listStrData.add("");
                        listStrData.add("");
                        listStrData.add("");
                        listStrData.add(String.valueOf(gstr1B2CLBeanSub.getValue()));
                        listStrData.add(String.valueOf(gstr1B2CLBeanSub.getIgstRate()));
                        listStrData.add(String.valueOf(gstr1B2CLBeanSub.getTaxableValue()));
                        listStrData.add(String.valueOf(gstr1B2CLBeanSub.getIgstAmt()));
                        listStrData.add(String.valueOf(gstr1B2CLBeanSub.getCessAmt()));
                        listCSVData.add(listStrData);

                    } while (cursor.moveToNext());

                }

            }// end try
            catch (Exception e)
            {
                //MsgBox = new AlertDialog.Builder(myContext);
                MsgBox .setTitle("Error while fetching items details")
                        .setIcon(R.mipmap.ic_company_logo)
                        .setMessage(e.getMessage())
                        .setPositiveButton("OK",null)
                        .show();
            }
        }
    }

    void GSTR1_DocIssued()
    {

        gstr1DocIssuedBeanArrayList = new ArrayList<>();
        GSTR1DocIssuedBean gstr1DocIssuedBean;

        // Invoices for sale
        {
            Cursor cursor = HomeActivity.dbHandler.getAllInvoices_outward(String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
            if (cursor!=null && cursor.moveToFirst()) {
                gstr1DocIssuedBean = new GSTR1DocIssuedBean();

                String from = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                String to = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                int totnum = 0;
                int cancel = 0;
                do {
                    to = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                    totnum++;
                    if (cursor.getInt(cursor.getColumnIndex("BillStatus")) == 0)
                        cancel++;

                } while (cursor.moveToNext());

                gstr1DocIssuedBean.setSno(1);
                gstr1DocIssuedBean.setNatureOfDoc("Invoices for outward supply");
                gstr1DocIssuedBean.setFrom(String.valueOf(from));
                gstr1DocIssuedBean.setTo(String.valueOf(to));
                gstr1DocIssuedBean.setTotalNumber(totnum);
                gstr1DocIssuedBean.setCancelled(cancel);
                gstr1DocIssuedBean.setNetIssued(totnum-cancel);

                gstr1DocIssuedBeanArrayList.add(gstr1DocIssuedBean);
            }
        }

        // Invoices for inward supply from unregistered person
        {
            Cursor cursor = HomeActivity.dbHandler.getPurchaseOrder_for_unregistered(String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
            if (cursor!=null && cursor.moveToFirst())
            {
                gstr1DocIssuedBean = new GSTR1DocIssuedBean();

                String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
                if (gstin ==null  || gstin.equals(""))
                {
                    String from = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                    String to = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                    int totnum =0;
                    int cancel =0;
                    do {
                        gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
                        if (gstin ==null  || gstin.equals("")) {
                            to = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                            totnum++;
                            /*if (cursor.getInt(cursor.getColumnIndex("BillStatus")) == 0)
                                cancel++;*/
                        }

                    }while(cursor.moveToNext());

                    gstr1DocIssuedBean.setSno(2);
                    gstr1DocIssuedBean.setNatureOfDoc("Invoices for inward supply for unregistered supplier");
                    gstr1DocIssuedBean.setFrom(String.valueOf(from));
                    gstr1DocIssuedBean.setTo(String.valueOf(to));
                    gstr1DocIssuedBean.setTotalNumber(totnum);
                    gstr1DocIssuedBean.setCancelled(cancel);
                    gstr1DocIssuedBean.setNetIssued(totnum-cancel);

                    gstr1DocIssuedBeanArrayList.add(gstr1DocIssuedBean);
                }
            }
        }
        // Credit Note
        {
//            Cursor cursor = HomeActivity.dbHandler.getGSTR1_CDN(String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
//            if (cursor!=null && cursor.moveToFirst())
//            {
//                gstr1DocIssuedBean = new GSTR1DocIssuedBean();
//
//                String from = cursor.getString(cursor.getColumnIndex("NoteNo"));
//                String to = cursor.getString(cursor.getColumnIndex("NoteNo"));
//                int totnum =0;
//                int cancel =0;
//                do {
//                    to = cursor.getString(cursor.getColumnIndex("NoteNo"));
//                    totnum++;
//                    /*if(cursor.getInt(cursor.getColumnIndex("BillStatus"))==0)
//                        cancel++;*/
//
//                }while(cursor.moveToNext());
//
//                gstr1DocIssuedBean.setSno(3);
//                gstr1DocIssuedBean.setNatureOfDoc("Credit/Debit Notes");
//                gstr1DocIssuedBean.setFrom(String.valueOf(from));
//                gstr1DocIssuedBean.setTo(String.valueOf(to));
//                gstr1DocIssuedBean.setTotalNumber(totnum);
//                gstr1DocIssuedBean.setCancelled(cancel);
//                gstr1DocIssuedBean.setNetIssued(totnum-cancel);
//
//                gstr1DocIssuedBeanArrayList.add(gstr1DocIssuedBean);
//            }
        }

    }

    private void GSTR2_Registered_Report()
    {
        double valtot =0, taxvaltot =0,Itot =0, Ctot =0, Stot =0, Amttot =0,cesstot=0;
        try
        {
            gstr2RegisteredBeanArrayList = new ArrayList<>();

            GSTR2RegisteredBean gstr2RegisteredBean;

            String str_dt = String.valueOf(startDate_date.getTime());
            String end_dt = String.valueOf(endDate_date.getTime());
            ArrayList<String> gstinList = HomeActivity.dbHandler.getGSTR2_b2b_gstinList(str_dt, end_dt);

            if(gstinList.size() == 0)
            {
                MsgBox.Show("Insufficient Information","No transaction has been done");
            } else {
                int i =1;
                for (String gstin : gstinList)
                {
                    Cursor cursor = HomeActivity.dbHandler.getPurchaseOrder_for_gstin(str_dt,end_dt,gstin);
                    ArrayList<String> po_list = new ArrayList<>();

                    while(cursor!=null && cursor.moveToNext())
                    {

                        gstr2RegisteredBean = new GSTR2RegisteredBean();

                        String purchaseorder = cursor.getString(cursor.getColumnIndex("PurchaseOrderNo"));
                        String invoiceNo = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                        String invoiceDate = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                        String pos_supplier = cursor.getString(cursor.getColumnIndex("SupplierPOS"));
                        String supplierCode = cursor.getString(cursor.getColumnIndex("SupplierCode"));
                        if(pos_supplier==null)
                            pos_supplier="";
                        String str = gstin+supplierCode+invoiceNo+invoiceDate+pos_supplier;
                        if(po_list.contains(str))
                            continue;

                        po_list.add(str);

                        gstr2RegisteredBean.setSno(i++);
                        gstr2RegisteredBean.setSupplierGstin(gstin);
                        gstr2RegisteredBean.setInvoiceNo(cursor.getInt(cursor.getColumnIndex("InvoiceNo")));

                        Date dd_milli = new Date(Long.parseLong(invoiceDate));
                        String dd  = new SimpleDateFormat("dd-MM-yyyy").format(dd_milli);
                        gstr2RegisteredBean.setInvoiceDate(dd);

                        gstr2RegisteredBeanArrayList.add(gstr2RegisteredBean);

                        String subNo[] = {"i","ii","iii","iv","v","vi","vii","viii","ix","x","xi","xii","xiii","xiv","xv","xvi","xvii","xviii","xix","xx"};

                        Cursor cursor_item = HomeActivity.dbHandler.getPurchaseOrder_for_gstin(invoiceNo,invoiceDate,gstin,purchaseorder);
                        int ii =0;
                        while (cursor_item!=null && cursor_item.moveToNext())
                        {

//                        gstr2RegisteredBean.setSno(subNo[ii++]);

                            gstr2RegisteredBean.setSupplyType(cursor_item.getString(cursor_item.getColumnIndex("SupplyType")));
                            gstr2RegisteredBean.setHsnCode(cursor_item.getString(cursor_item.getColumnIndex("HSNCode")));
                            gstr2RegisteredBean.setValue(cursor_item.getDouble(cursor_item.getColumnIndex("Value")));
                            gstr2RegisteredBean.setTaxableVlue(cursor_item.getDouble(cursor_item.getColumnIndex("TaxableValue")));
                            taxvaltot +=  cursor_item.getDouble(cursor_item.getColumnIndex("TaxableValue"));
                            gstr2RegisteredBean.setIgstAmt(cursor_item.getDouble(cursor_item.getColumnIndex("IGSTAmount")));
                            Itot += cursor_item.getDouble(cursor_item.getColumnIndex("IGSTAmount"));
                            gstr2RegisteredBean.setCgstAmt(cursor_item.getDouble(cursor_item.getColumnIndex("CGSTAmount")));
                            Ctot += cursor_item.getDouble(cursor_item.getColumnIndex("CGSTAmount"));
                            gstr2RegisteredBean.setSgstAmt(cursor_item.getDouble(cursor_item.getColumnIndex("SGSTAmount")));
                            Stot += cursor_item.getDouble(cursor_item.getColumnIndex("SGSTAmount"));
                            gstr2RegisteredBean.setCessAmt(cursor_item.getDouble(cursor_item.getColumnIndex("cessAmount")));
                            cesstot += cursor_item.getDouble(cursor_item.getColumnIndex("cessAmount"));
                            gstr2RegisteredBean.setAmount(cursor_item.getDouble(cursor_item.getColumnIndex("Amount")));
                            Amttot += cursor_item.getDouble(cursor_item.getColumnIndex("Amount"));

                        }
                    }
                }// end for
            }

        }catch (Exception e)
        {
            e.printStackTrace();

        }



    }

    private void GSTR2_UnRegistered_Report()
    {
        double valtot =0, taxvaltot =0,Itot =0, Ctot =0, Stot =0, Amttot =0,cesstot=0;
        try
        {

            gstr2UnRegisteredBeanArrayList = new ArrayList<>();

            GSTR2UnRegisteredBean gstr2UnRegisteredBean;

            String str_dt = String.valueOf(startDate_date.getTime());
            String end_dt = String.valueOf(endDate_date.getTime());
            Cursor cursor = HomeActivity.dbHandler.getPurchaseOrder_for_unregistered(str_dt,end_dt);
            ArrayList<String>po_list = new ArrayList<>();
            int i=1;
            while(cursor!=null && cursor.moveToNext())
            {

                gstr2UnRegisteredBean = new GSTR2UnRegisteredBean();

                String purchaseorder = cursor.getString(cursor.getColumnIndex("PurchaseOrderNo"));
                String invoiceNo = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                String invoiceDate = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                String pos_supplier = cursor.getString(cursor.getColumnIndex("SupplierPOS"));
                String supplierCode = cursor.getString(cursor.getColumnIndex("SupplierCode"));
                String cname = cursor.getString(cursor.getColumnIndex("SupplierName"));
                if(pos_supplier==null)
                    pos_supplier="";
                String str = supplierCode+invoiceNo+invoiceDate+pos_supplier;
                if(po_list.contains(str))
                    continue;

                po_list.add(str);

                gstr2UnRegisteredBean.setSno(i++);
                gstr2UnRegisteredBean.setInvoiceNo(cursor.getInt(cursor.getColumnIndex("InvoiceNo")));

                Date dd_milli = new Date(Long.parseLong(invoiceDate));
                String dd  = new SimpleDateFormat("dd-MM-yyyy").format(dd_milli);
                gstr2UnRegisteredBean.setInvoiceDate(dd);

                gstr2UnRegisteredBeanArrayList.add(gstr2UnRegisteredBean);

                String subNo[] = {"i","ii","iii","iv","v","vi","vii","viii","ix","x","xi","xii","xiii","xiv","xv","xvi","xvii","xviii","xix","xx"};

                int ii =0;

                Cursor cursor_item = HomeActivity.dbHandler.getPurchaseOrder_for_unregisteredSupplier(invoiceNo,invoiceDate,purchaseorder,supplierCode);

                while (cursor_item!=null && cursor_item.moveToNext())
                {

                    gstr2UnRegisteredBean.setSupplyType(cursor_item.getString(cursor_item.getColumnIndex("SupplyType")));
                    gstr2UnRegisteredBean.setHsnCode(cursor_item.getString(cursor_item.getColumnIndex("HSNCode")));
                    gstr2UnRegisteredBean.setValue(cursor_item.getDouble(cursor_item.getColumnIndex("Value")));
                    gstr2UnRegisteredBean.setTaxableVlue(cursor_item.getDouble(cursor_item.getColumnIndex("TaxableValue")));
                    taxvaltot +=  cursor_item.getDouble(cursor_item.getColumnIndex("TaxableValue"));
                    gstr2UnRegisteredBean.setIgstAmt(cursor_item.getDouble(cursor_item.getColumnIndex("IGSTAmount")));
                    Itot += cursor_item.getDouble(cursor_item.getColumnIndex("IGSTAmount"));
                    gstr2UnRegisteredBean.setCgstAmt(cursor_item.getDouble(cursor_item.getColumnIndex("CGSTAmount")));
                    Ctot += cursor_item.getDouble(cursor_item.getColumnIndex("CGSTAmount"));
                    gstr2UnRegisteredBean.setSgstAmt(cursor_item.getDouble(cursor_item.getColumnIndex("SGSTAmount")));
                    Stot += cursor_item.getDouble(cursor_item.getColumnIndex("SGSTAmount"));
                    gstr2UnRegisteredBean.setCessAmt(cursor_item.getDouble(cursor_item.getColumnIndex("cessAmount")));
                    cesstot += cursor_item.getDouble(cursor_item.getColumnIndex("cessAmount"));
                    gstr2UnRegisteredBean.setAmount(cursor_item.getDouble(cursor_item.getColumnIndex("Amount")));
                    Amttot += cursor_item.getDouble(cursor_item.getColumnIndex("Amount"));

                }
            }


        }catch (Exception e)
        {
            e.printStackTrace();

        }
    }

    private void ResetAll() {
        etReportDateStart.setText("");
        etReportDateEnd.setText("");
        listCSVData.clear();
        DefaultFragmentForEmptyData defaultFragmentForEmptyData = new DefaultFragmentForEmptyData();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.gst_report_container, defaultFragmentForEmptyData);
        fragmentTransaction.commit();
    }

}
