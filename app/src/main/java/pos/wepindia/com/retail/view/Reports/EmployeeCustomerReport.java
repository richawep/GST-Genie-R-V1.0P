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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import pos.wepindia.com.retail.view.Reports.Bean.CustomerDetailReportBean;
import pos.wepindia.com.retail.view.Reports.Bean.CustomerWiseReportBean;
import pos.wepindia.com.retail.view.Reports.Bean.SalesmanDetailReportBean;
import pos.wepindia.com.retail.view.Reports.Bean.SalesmanWiseReportBean;
import pos.wepindia.com.retail.view.Reports.Bean.SupplierWiseReportBean;
import pos.wepindia.com.retail.view.Reports.Bean.UserDetailReportBean;
import pos.wepindia.com.retail.view.Reports.Bean.UserWiseReportBean;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;

/**
 * Created by SachinV on 23-01-2018.
 */

public class EmployeeCustomerReport extends Fragment {

    private static final String TAG = EmployeeCustomerReport.class.getSimpleName();

    @BindView(R.id.spnrReportNameSelection_employee_customer)
    Spinner spnrReportNameSelection_employee_customer;
    @BindView(R.id.btn_ReportDateFrom_employee_customer)
    Button btn_ReportDateFrom_employee_customer;
    @BindView(R.id.btn_ReportDateTo_employee_customer)
    Button btn_ReportDateTo_employee_customer;
    @BindView(R.id.etReportDateStart_employee_customer)
    EditText etReportDateStart_employee_customer;
    @BindView(R.id.etReportDateEnd_employee_customer)
    EditText etReportDateEnd_employee_customer;
    @BindView(R.id.btn_ReportPrint_employee_customer)
    Button btn_ReportPrint_employee_customer;
    @BindView(R.id.btn_ReportExport_employee_customer)
    Button btn_ReportExport_employee_customer;
    @BindView(R.id.btn_ReportView_employee_customer)
    Button btn_ReportView_employee_customer;
    @BindView(R.id.employee_customer_report_container)
    FrameLayout employee_customer_report_container;
    @BindView(R.id.rowReportPersonId)
    TableRow rowReportPersonId;
    @BindView(R.id.tvReportPersonId)
    TextView tvReportPersonId;
    @BindView(R.id.spnrUsers)
    Spinner spnrUsers;
    @BindView(R.id.spnrSalesman)
    Spinner spnrSalesman;
    @BindView(R.id.spnrCustomers)
    Spinner spnrCustomers;
    @BindView(R.id.etReportPersonId)
    EditText txtPersonId;

    private Context myContext;
    private View view;
    private MessageDialog MsgBox;
    private DateTime objDate;
    private String strDate = "";
    private Date startDate_date, endDate_date;
    private ArrayList<CustomerDetailReportBean> customerDetailReportBeanArrayList;
    private ArrayList<CustomerWiseReportBean> customerWiseReportBeanArrayList;
    private ArrayList<SupplierWiseReportBean> supplierWiseReportBeanArrayList;
    private ArrayList<UserDetailReportBean> userDetailReportBeanArrayList;
    private ArrayList<UserWiseReportBean> userWiseReportBeanArrayList;
    private ArrayList<SalesmanDetailReportBean> salesmanDetailReportBeanArrayList;
    private ArrayList<SalesmanWiseReportBean> salesmanWiseReportBeanArrayList;


    //List used for generating .csv file
    private List<List<String>> listCSVData;

    private PopulateSelectedReportData populateSelectedReportData = null;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.employee_customer_report_fragment, container, false);

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

            ClickEvents();
            spnrReportNameSelection_employee_customer.setOnItemSelectedListener(spOnItemSelectListener);
            listCSVData = new ArrayList<List<String>>();
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    AdapterView.OnItemSelectedListener spOnItemSelectListener = new AdapterView.OnItemSelectedListener() {

        public void onItemSelected(AdapterView<?> adapter, View v, int position,
                                   long id) {
            // TODO Auto-generated method stub
            ResetAll();
            try {
                switch (getSpinnerListIndex()) {
                    case 0: // Customer Detailed Report
                        rowReportPersonId.setVisibility(View.VISIBLE);
                        loadSpinnerCustomers();
                        tvReportPersonId.setText("Select Customer : ");
                        spnrCustomers.setVisibility(View.VISIBLE);
                        spnrUsers.setVisibility(View.GONE);
                        spnrSalesman.setVisibility(View.GONE);
                        break;
                    case 2: //User Detailed Report
                        rowReportPersonId.setVisibility(View.VISIBLE);
                        loadSpinnerUsers();
                        tvReportPersonId.setText("Select User : ");
                        spnrUsers.setVisibility(View.VISIBLE);
                        spnrCustomers.setVisibility(View.GONE);
                        spnrSalesman.setVisibility(View.GONE);
                        break;
                    case 4: //Salesman Detailed Report
                        rowReportPersonId.setVisibility(View.VISIBLE);
                        loadSpinnerSalesman();
                        tvReportPersonId.setText("Select Salesman : ");
                        spnrSalesman.setVisibility(View.VISIBLE);
                        spnrUsers.setVisibility(View.GONE);
                        spnrCustomers.setVisibility(View.GONE);
                        break;
                    default:
                        rowReportPersonId.setVisibility(View.INVISIBLE);
                        break;
                }
            } catch (Exception e) {
                Logger.e(TAG, "Error on report type select adapter on select item." + e.getMessage());
                MsgBox.Show("Error", "Some error occurred while selecting report type.");
            }
        }

        public void onNothingSelected(AdapterView<?> adapter) {
            // TODO Auto-generated method stub
            rowReportPersonId.setVisibility(View.INVISIBLE);
        }
    };

    void ClickEvents() {
        spnrCustomers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnrCustomers.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    txtPersonId.setText("");
                } else {
                    int customerid = HomeActivity.dbHandler.getCustomersIdByName(spnrCustomers.getSelectedItem().toString());
                    txtPersonId.setText(String.valueOf(customerid));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnrUsers.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    txtPersonId.setText("");
                } else {
                    int userid = HomeActivity.dbHandler.getUsersIdByName(spnrUsers.getSelectedItem().toString());
                    txtPersonId.setText(String.valueOf(userid));
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrSalesman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spnrSalesman.getSelectedItem().toString().equalsIgnoreCase("Select")) {
                    txtPersonId.setText("");
                } else {
                    String salesmanid = HomeActivity.dbHandler.getSalesmanIdByName(spnrSalesman.getSelectedItem().toString());
                    txtPersonId.setText(salesmanid);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadSpinnerCustomers() {
        List<String> labelCustomers = HomeActivity.dbHandler.getAllCustomersforReport();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, labelCustomers);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnrCustomers.setAdapter(dataAdapter);
    }

    private void loadSpinnerUsers() {
        List<String> labelUsers = HomeActivity.dbHandler.getAllUsersforReport();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, labelUsers);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnrUsers.setAdapter(dataAdapter);
    }

    private void loadSpinnerSalesman() {
        List<String> labelUsers = HomeActivity.dbHandler.getAllSalesmanforReport();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, labelUsers);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spnrSalesman.setAdapter(dataAdapter);
    }

    @OnClick({R.id.btn_ReportDateFrom_employee_customer, R.id.btn_ReportDateTo_employee_customer,
            R.id.btn_ReportPrint_employee_customer, R.id.btn_ReportExport_employee_customer, R.id.btn_ReportView_employee_customer})
    protected void mBtnClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ReportDateFrom_employee_customer:
                From();
                break;
            case R.id.btn_ReportDateTo_employee_customer:
                To();
                break;
            case R.id.btn_ReportPrint_employee_customer: //PrintReport();
                break;
            case R.id.btn_ReportExport_employee_customer: //ExportReport();
                mExportReport();
                break;
            case R.id.btn_ReportView_employee_customer:
                ViewReport();
                break;
        }
    }

    public void From() {
        DateSelection(1);
    }

    public void To() {
        if (!etReportDateStart_employee_customer.getText().toString().equalsIgnoreCase("")) {
            DateSelection(2);

        } else {
            MsgBox.Show("Warning", "Please select report FROM date");
        }
    }

    private int getSpinnerListIndex() {
        List<String> SalesReport = Arrays.asList(getResources().getStringArray(R.array.employee_customer_report));

        for (String muom : SalesReport) {
            if (muom.equals(spnrReportNameSelection_employee_customer.getSelectedItem().toString()))
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
                                etReportDateStart_employee_customer.setText(strDate);
                                startDate_date = new Date(dateReportDate.getYear() - 1900, dateReportDate.getMonth(), dateReportDate.getDayOfMonth());
                            } else {
                                etReportDateEnd_employee_customer.setText(strDate);
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
        String txtStartDate = etReportDateStart_employee_customer.getText().toString();
        String txtEndDate = etReportDateEnd_employee_customer.getText().toString();
        if (txtStartDate.equalsIgnoreCase("") || txtEndDate.equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please select From & To Date");
        } else if (startDate_date.getTime() > endDate_date.getTime()) {
            MsgBox.Show("Warning", "'From Date' cannot be greater than 'To Date' ");
        } else if (getSpinnerListIndex() == 0 && spnrCustomers.getSelectedItem().toString().equals("Select")) {
            MsgBox.Show("Warning", "Please select customer");
        } else if (spnrReportNameSelection_employee_customer.getSelectedItem().toString().equals("User Detailed Report") && spnrUsers.getSelectedItem().toString().equals("Select")) {
            MsgBox.Show("Warning", "Please select user");
        } else {
            populateSelectedReportData = new PopulateSelectedReportData();
            populateSelectedReportData.execute();
           /* try {
                switch (getSpinnerListIndex()) {
                    case 0: // Customer Detail Report
                        break;
                    case 1: // Customer Wise Report
                        break;
                    case 2: // Supplier Wise report
                        SupplierwiseReport();
                        break;
                    case 3: // User Detail Report
                        break;
                    case 4: // User Wise Report
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                MsgBox.Show("Error", "Some error occurred while displaying report");
            }*/
        }
    }


    void SupplierwiseReport() {

        supplierWiseReportBeanArrayList = new ArrayList<>();

        Cursor Report = HomeActivity.dbHandler.getSupplierwiseReport(
                String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));

        SupplierWiseReportBean supplierWiseReportBean;
        boolean isSupplierExists = false;

        if (Report.moveToFirst()) {

            do {
                for (int iPosition = 0; iPosition < supplierWiseReportBeanArrayList.size(); iPosition++) {

                    if (supplierWiseReportBeanArrayList.get(iPosition) != null) {

                        if (supplierWiseReportBeanArrayList.get(iPosition).getSupplierCode() == Report.getInt(Report.getColumnIndex("SupplierCode"))) {

                            supplierWiseReportBeanArrayList.get(iPosition).setTotalBills(supplierWiseReportBeanArrayList.get(iPosition).getTotalBills() + 1);

                            // Amount
                            supplierWiseReportBeanArrayList.get(iPosition).setBillAmount(supplierWiseReportBeanArrayList.get(iPosition).getBillAmount()
                                    + Report.getDouble(Report.getColumnIndex("Amount")));

                            isSupplierExists = true;
                            break;
                        }
                    }
                }

                if (isSupplierExists == false) {

                    supplierWiseReportBean = new SupplierWiseReportBean();

                    supplierWiseReportBean.setSupplierCode(Report.getInt(Report.getColumnIndex("SupplierCode")));
                    supplierWiseReportBean.setName(Report.getString(Report.getColumnIndex("SupplierName")));
                    supplierWiseReportBean.setTotalBills(1);
                    supplierWiseReportBean.setBillAmount(Report.getDouble(Report.getColumnIndex("Amount")));

                    supplierWiseReportBeanArrayList.add(supplierWiseReportBean);
                }

                isSupplierExists = false;

            } while (Report.moveToNext());

        } else {
            MsgBox.Show("Warning", "No transaction has been done");
        }

        employee_customer_report_container.removeAllViews();
        SupplierWiseReport supplierWiseReport = new SupplierWiseReport();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("dataList", supplierWiseReportBeanArrayList);
        supplierWiseReport.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.employee_customer_report_container, supplierWiseReport);
        fragmentTransaction.commit();

    }

    private class PopulateSelectedReportData extends AsyncTask<Void, String, String> {
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
                    case 0: // Customer Detail Report
                        customerDetailReportBeanArrayList = new ArrayList<>();
                        Report = HomeActivity.dbHandler.getCustomerDetailedReport(Integer.parseInt(txtPersonId.getText().toString()), String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
                        CustomerDetailReportBean customerDetailReportBean;
                        if (Report.moveToFirst()) {
                            listCSVData.clear();
                            listStrData = new ArrayList<String>();
                            listStrData.add("Customer Name");
                            listStrData.add(spnrCustomers.getSelectedItem().toString());
                            listStrData.add("");
                            listStrData.add("");
                            listStrData.add("");
                            listStrData.add("");
                            listStrData.add("");
                            listStrData.add("");
                            listStrData.add("");
                            listStrData.add("");
                            listCSVData.add(listStrData);
                            listStrData = new ArrayList<String>();
                            listStrData.add("Date");
                            listStrData.add("Bill Number");
                            listStrData.add("Total Items");
                            listStrData.add("Discount");
                            listStrData.add("Cash Payment");
                            listStrData.add("Card Payment");
                            listStrData.add("Coupon Payment");
                            listStrData.add("Credit Payment");
                            listStrData.add("wallet Payment");
                            listStrData.add("Bill Amount");
                            listCSVData.add(listStrData);
                            do {
                                customerDetailReportBean = new CustomerDetailReportBean();
                                String dateInMillis = Report.getString(Report.getColumnIndex("InvoiceDate"));
                                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                String dateString = formatter.format(Long.parseLong(dateInMillis));
                                customerDetailReportBean.setDate(dateString);
                                customerDetailReportBean.setBillNumber(Report.getInt(Report.getColumnIndex("InvoiceNo")));
                                customerDetailReportBean.setTotalBills(Report.getInt(Report.getColumnIndex("TotalItems")));
                                customerDetailReportBean.setDiscount(Report.getDouble(Report.getColumnIndex("TotalDiscountAmount")));
                                customerDetailReportBean.setCashAmount(Report.getDouble(Report.getColumnIndex("CashPayment")));
                                customerDetailReportBean.setCardPayment(Report.getDouble(Report.getColumnIndex("CardPayment")));
                                customerDetailReportBean.setCouponPayment(Report.getDouble(Report.getColumnIndex("CouponPayment")));
                                customerDetailReportBean.setPettyCashPayment(Report.getDouble(Report.getColumnIndex("PettyCashPayment")));
                                customerDetailReportBean.setWalletPayment(Report.getDouble(Report.getColumnIndex("WalletPayment")));
                                customerDetailReportBean.setBillAmount(Report.getDouble(Report.getColumnIndex("BillAmount")));
                                customerDetailReportBeanArrayList.add(customerDetailReportBean);
                                listStrData = new ArrayList<String>();
                                listStrData.add(customerDetailReportBean.getDate());
                                listStrData.add(String.valueOf(customerDetailReportBean.getBillNumber()));
                                listStrData.add(String.valueOf(customerDetailReportBean.getTotalBills()));
                                listStrData.add(String.valueOf(customerDetailReportBean.getDiscount()));
                                listStrData.add(String.valueOf(customerDetailReportBean.getCashAmount()));
                                listStrData.add(String.valueOf(customerDetailReportBean.getCardPayment()));
                                listStrData.add(String.valueOf(customerDetailReportBean.getCouponPayment()));
                                listStrData.add(String.valueOf(customerDetailReportBean.getPettyCashPayment()));
                                listStrData.add(String.valueOf(customerDetailReportBean.getWalletPayment()));
                                listStrData.add(String.valueOf(customerDetailReportBean.getBillAmount()));
                                listCSVData.add(listStrData);
                            } while (Report.moveToNext());
                        } else {
                            listCSVData.clear();
                            return "No transaction has been done";
                        }
                        break;
                    case 1: // Customer Wise Report
                        customerWiseReportBeanArrayList = new ArrayList<>();
                        Report = HomeActivity.dbHandler.getCustomerwiseReport(String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
                        CustomerWiseReportBean customerWiseReportBean;
                        if (Report.moveToFirst()) {
                            boolean isCustomerExists = false;
                            do {
                                for (int iPosition = 0; iPosition < customerWiseReportBeanArrayList.size(); iPosition++) {
                                    if (customerWiseReportBeanArrayList.get(iPosition) != null) {
                                        if (customerWiseReportBeanArrayList.get(iPosition).getcustId() == Report.getInt(Report.getColumnIndex("CustId"))) {
                                            customerWiseReportBeanArrayList.get(iPosition).setTotalBills(customerWiseReportBeanArrayList.get(iPosition).getTotalBills() + 1);

                                            customerWiseReportBeanArrayList.get(iPosition).setCashAmount(customerWiseReportBeanArrayList.get(iPosition).getCashAmount()
                                                    + Report.getDouble(Report.getColumnIndex("CashPayment")));

                                            customerWiseReportBeanArrayList.get(iPosition).setCardPayment(customerWiseReportBeanArrayList.get(iPosition).getCardPayment()
                                                    + Report.getDouble(Report.getColumnIndex("CardPayment")));

                                            customerWiseReportBeanArrayList.get(iPosition).setCouponPayment(customerWiseReportBeanArrayList.get(iPosition).getCouponPayment()
                                                    + Report.getDouble(Report.getColumnIndex("CouponPayment")));

                                            customerWiseReportBeanArrayList.get(iPosition).setPettyCashPayment(customerWiseReportBeanArrayList.get(iPosition).getPettyCashPayment()
                                                    + Report.getDouble(Report.getColumnIndex("PettyCashPayment")));

                                            customerWiseReportBeanArrayList.get(iPosition).setWalletPayment(customerWiseReportBeanArrayList.get(iPosition).getWalletPayment()
                                                    + Report.getDouble(Report.getColumnIndex("WalletPayment")));

                                            double total = customerWiseReportBeanArrayList.get(iPosition).getCardPayment() +
                                                    customerWiseReportBeanArrayList.get(iPosition).getCashAmount() +
                                                    customerWiseReportBeanArrayList.get(iPosition).getPettyCashPayment() +
                                                    customerWiseReportBeanArrayList.get(iPosition).getCouponPayment() +
                                                    customerWiseReportBeanArrayList.get(iPosition).getWalletPayment();
                                            customerWiseReportBeanArrayList.get(iPosition).setTotalTransaction(total);
                                            isCustomerExists = true;
                                            break;
                                        }
                                    }
                                }

                                if (isCustomerExists == false) {
                                    customerWiseReportBean = new CustomerWiseReportBean();
                                    customerWiseReportBean.setcustId(Report.getInt(Report.getColumnIndex("CustId")));
                                    customerWiseReportBean.setName(Report.getString(Report.getColumnIndex("CustName")));
                                    customerWiseReportBean.setTotalBills(1);
                                    customerWiseReportBean.setLastTransaction(Report.getDouble(Report.getColumnIndex("LastTransaction")));
                                    customerWiseReportBean.setCashAmount(Report.getDouble(Report.getColumnIndex("CashPayment")));
                                    customerWiseReportBean.setCardPayment(Report.getDouble(Report.getColumnIndex("CardPayment")));
                                    customerWiseReportBean.setCouponPayment(Report.getDouble(Report.getColumnIndex("CouponPayment")));
                                    customerWiseReportBean.setPettyCashPayment(Report.getDouble(Report.getColumnIndex("PettyCashPayment")));
                                    customerWiseReportBean.setWalletPayment(Report.getDouble(Report.getColumnIndex("WalletPayment")));
                                    customerWiseReportBean.setTotalTransaction(Report.getDouble(Report.getColumnIndex("TotalTransaction")));
                                    customerWiseReportBeanArrayList.add(customerWiseReportBean);
                                }
                                isCustomerExists = false;
                            } while (Report.moveToNext());
                            listCSVData.clear();
                            listStrData = new ArrayList<String>();
                            listStrData.add("Cust.Id");
                            listStrData.add("Name");
                            listStrData.add("Total Bills");
                            listStrData.add("Last Petty Trans");
                            listStrData.add("Cash Payment");
                            listStrData.add("Card Payment");
                            listStrData.add("Coupon Payment");
                            listStrData.add("Credit Payment");
                            listStrData.add("wallet Payment");
                            listStrData.add("Total Petty Trans");
                            listCSVData.add(listStrData);
                            for (CustomerWiseReportBean customerWiseReportBean1 : customerWiseReportBeanArrayList) {
                                listStrData = new ArrayList<String>();
                                listStrData.add(String.valueOf(customerWiseReportBean1.getcustId()));
                                listStrData.add(customerWiseReportBean1.getName());
                                listStrData.add(String.valueOf(customerWiseReportBean1.getTotalBills()));
                                listStrData.add(String.valueOf(customerWiseReportBean1.getLastTransaction()));
                                listStrData.add(String.valueOf(customerWiseReportBean1.getCashAmount()));
                                listStrData.add(String.valueOf(customerWiseReportBean1.getCardPayment()));
                                listStrData.add(String.valueOf(customerWiseReportBean1.getCouponPayment()));
                                listStrData.add(String.valueOf(customerWiseReportBean1.getPettyCashPayment()));
                                listStrData.add(String.valueOf(customerWiseReportBean1.getWalletPayment()));
                                listStrData.add(String.valueOf(customerWiseReportBean1.getTotalTransaction()));
                                listCSVData.add(listStrData);
                            }
                        } else {
                            listCSVData.clear();
                            return "No transaction has been done";
                        }
                        break;
                  /*  case 2: // Supplier Wise report
                        break;*/
                    case 2: // User Detail Report
                        if (txtPersonId.getText().toString().equalsIgnoreCase("")) {
                            listCSVData.clear();
                            return "Please Select User";
                        } else {
                            userDetailReportBeanArrayList = new ArrayList<>();
                            Report = HomeActivity.dbHandler.getUserDetailedReport(txtPersonId.getText().toString(),
                                    String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));
                            UserDetailReportBean userDetailReportBean;

                            if (Report.moveToFirst()) {
                                listCSVData.clear();
                                listStrData = new ArrayList<String>();
                                listStrData.add("User Name");
                                listStrData.add(spnrUsers.getSelectedItem().toString());
                                listStrData.add("");
                                listStrData.add("");
                                listStrData.add("");
                                listStrData.add("");
                                listCSVData.add(listStrData);
                                listStrData = new ArrayList<String>();
                                listStrData.add("Date");
                                listStrData.add("Bill Number");
                                listStrData.add("Total Items");
                                listStrData.add("Discount");
                                listStrData.add("Tax");
                                listStrData.add("Bill Amount");
                                listCSVData.add(listStrData);
                                do {
                                    userDetailReportBean = new UserDetailReportBean();
                                    String dateInMillis = Report.getString(Report.getColumnIndex("InvoiceDate"));
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                    String dateString = formatter.format(Long.parseLong(dateInMillis));
                                    userDetailReportBean.setDate(dateString);
                                    userDetailReportBean.setBillNumber(Report.getInt(Report.getColumnIndex("InvoiceNo")));
                                    userDetailReportBean.setTotalItems(Report.getInt(Report.getColumnIndex("TotalItems")));
                                    userDetailReportBean.setDiscount(Report.getDouble(Report.getColumnIndex("TotalDiscountAmount")));

                                    double igst_str = Report.getDouble(Report.getColumnIndex("IGSTAmount"));
                                    double cgst_str = Report.getDouble(Report.getColumnIndex("CGSTAmount"));
                                    double sgst_str = Report.getDouble(Report.getColumnIndex("SGSTAmount"));

                                    double tottax = igst_str + cgst_str + sgst_str;
                                    userDetailReportBean.setTax(tottax);

                                    userDetailReportBean.setBillAmount(Report.getDouble(Report.getColumnIndex("BillAmount")));

                                    userDetailReportBeanArrayList.add(userDetailReportBean);
                                    listStrData = new ArrayList<String>();
                                    listStrData.add(userDetailReportBean.getDate());
                                    listStrData.add(String.valueOf(userDetailReportBean.getBillNumber()));
                                    listStrData.add(String.valueOf(userDetailReportBean.getTotalItems()));
                                    listStrData.add(String.valueOf(userDetailReportBean.getDiscount()));
                                    listStrData.add(String.valueOf(userDetailReportBean.getTax()));
                                    listStrData.add(String.valueOf(userDetailReportBean.getBillAmount()));
                                    listCSVData.add(listStrData);
                                } while (Report.moveToNext());

                            } else {
                                listCSVData.clear();
                                return "No transaction has been done";
                            }
                        }
                        break;
                    case 3: // User Wise Report
                        userWiseReportBeanArrayList = new ArrayList<>();

                       Report = HomeActivity.dbHandler.getUserwiseReport(
                                String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));

                        UserWiseReportBean userWiseReportBean;

                        if (Report.moveToFirst()) {
                            boolean isDeptExists = false;

                            do {
                                for (int iPosition = 0; iPosition < userWiseReportBeanArrayList.size(); iPosition++) {

                                    if (userWiseReportBeanArrayList.get(iPosition) != null) {

                                        if (userWiseReportBeanArrayList.get(iPosition).getUserCode() == Report.getInt(Report
                                                .getColumnIndex("UserId"))) {

                                            // Total Bills
                                            userWiseReportBeanArrayList.get(iPosition).setTotalBills(userWiseReportBeanArrayList.get(iPosition).getTotalBills() + 1);

                                            // Amount
                                            userWiseReportBeanArrayList.get(iPosition).setBillAmount(userWiseReportBeanArrayList.get(iPosition).getBillAmount()
                                                    + Report.getDouble(Report.getColumnIndex("BillAmount")));

                                            isDeptExists = true;
                                            break;
                                        }
                                    }
                                }

                                if (isDeptExists == false) {

                                    userWiseReportBean = new UserWiseReportBean();

                                    userWiseReportBean.setUserCode(Report.getInt(Report.getColumnIndex("UserId")));
                                    userWiseReportBean.setName(Report.getString(Report.getColumnIndex("Name")));
                                    userWiseReportBean.setTotalBills(1);
                                    userWiseReportBean.setBillAmount(Report.getDouble(Report.getColumnIndex("BillAmount")));

                                    userWiseReportBeanArrayList.add(userWiseReportBean);
                                }

                                isDeptExists = false;

                            } while (Report.moveToNext());
                            listCSVData.clear();
                            listStrData = new ArrayList<String>();
                            listStrData.add("User Id");
                            listStrData.add("Name");
                            listStrData.add("Total Bills");
                            listStrData.add("Bill Amount");
                            listCSVData.add(listStrData);
                            for(UserWiseReportBean userWiseReportBean1 : userWiseReportBeanArrayList){
                                listStrData = new ArrayList<String>();
                                listStrData.add(String.valueOf(userWiseReportBean1.getUserCode()));
                                listStrData.add(userWiseReportBean1.getName());
                                listStrData.add(String.valueOf(userWiseReportBean1.getTotalBills()));
                                listStrData.add(String.valueOf(userWiseReportBean1.getBillAmount()));
                                listCSVData.add(listStrData);
                            }
                        } else {
                            listCSVData.clear();
                            return "No transaction has been done";
                        }
                        break;
                    case 4: // Salesman Detailed Report
                        if (txtPersonId.getText().toString().equalsIgnoreCase("")) {
                            listCSVData.clear();
                            return "Please Select salesman";
                        } else {

                            salesmanDetailReportBeanArrayList = new ArrayList<>();

                            Cursor Report = HomeActivity.dbHandler.getSalesmanDetailedReport(txtPersonId.getText().toString(),
                                    String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));

                            SalesmanDetailReportBean salesmanDetailReportBean;

                            if (Report.moveToFirst()) {
                                listCSVData.clear();
                                listStrData = new ArrayList<String>();
                                listStrData.add("Sales Man ID");
                                listStrData.add("Name");
                                listStrData.add("Invoice No");
                                listStrData.add("Invoice Date");
                                listStrData.add("Taxable Value");
                                listStrData.add("Bill Amount");
                                listCSVData.add(listStrData);
                                do {
                                    salesmanDetailReportBean = new SalesmanDetailReportBean();

                                    salesmanDetailReportBean.setSalesManId(Report.getString(Report.getColumnIndex(DatabaseHandler.KEY_SALES_MAN_ID)));
                                    salesmanDetailReportBean.setSalesmanName(spnrSalesman.getSelectedItem().toString());

                                    String dateInMillis = Report.getString(Report.getColumnIndex("InvoiceDate"));
                                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                                    String dateString = formatter.format(Long.parseLong(dateInMillis));
                                    salesmanDetailReportBean.setInvoiceDate(dateString);

                                    salesmanDetailReportBean.setInvoiceNo(Report.getInt(Report.getColumnIndex("InvoiceNo")));
                                    salesmanDetailReportBean.setTaxableValue(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_TaxableValue)));
                                    salesmanDetailReportBean.setBillAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_BillAmount)));

                                    salesmanDetailReportBeanArrayList.add(salesmanDetailReportBean);
                                    listStrData = new ArrayList<String>();
                                    listStrData.add(salesmanDetailReportBean.getSalesManId());
                                    listStrData.add(String.valueOf(salesmanDetailReportBean.getSalesmanName()));
                                    listStrData.add(String.valueOf(salesmanDetailReportBean.getInvoiceNo()));
                                    listStrData.add(String.valueOf(salesmanDetailReportBean.getInvoiceDate()));
                                    listStrData.add(String.valueOf(salesmanDetailReportBean.getTaxableValue()));
                                    listStrData.add(String.valueOf(salesmanDetailReportBean.getBillAmount()));
                                    listCSVData.add(listStrData);

                                } while (Report.moveToNext());

                            } else {
                                listCSVData.clear();
                                return "No transaction has been done";
                            }
                        }
                        break;
                    case 5: // Salesman Wise Report
                        salesmanWiseReportBeanArrayList = new ArrayList<>();

                        Cursor Report = HomeActivity.dbHandler.getSalesmanwiseReport(
                                String.valueOf(startDate_date.getTime()), String.valueOf(endDate_date.getTime()));

                        SalesmanWiseReportBean salesmanWiseReportBean;

                        if (Report.moveToFirst()) {
                            boolean isExists = false;
                            listCSVData.clear();
                            listStrData = new ArrayList<String>();
                            listStrData.add("Sales Man ID");
                            listStrData.add("Name");
                            listStrData.add("Total Bills");
                            listStrData.add("Month");
                            listStrData.add("Taxable Value");
                            listStrData.add("Bill Amount");
                            listCSVData.add(listStrData);
                            do {
                                for (int iPosition = 0; iPosition < salesmanWiseReportBeanArrayList.size(); iPosition++) {

                                    if (salesmanWiseReportBeanArrayList.get(iPosition) != null) {

                                        if (salesmanWiseReportBeanArrayList.get(iPosition).getSalesManId().equalsIgnoreCase(Report.getString(Report
                                                .getColumnIndex(DatabaseHandler.KEY_SALES_MAN_ID)))) {

                                            // Total Bills
                                            salesmanWiseReportBeanArrayList.get(iPosition).setTotalBills(salesmanWiseReportBeanArrayList.get(iPosition).getTotalBills() + 1);

                                            // Taxable Value
                                            salesmanWiseReportBeanArrayList.get(iPosition).setTaxableValue(salesmanWiseReportBeanArrayList.get(iPosition).getTaxableValue()
                                                    + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_TaxableValue)));

                                            // Amount
                                            salesmanWiseReportBeanArrayList.get(iPosition).setBillAmount(salesmanWiseReportBeanArrayList.get(iPosition).getBillAmount()
                                                    + Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_BillAmount)));

                                            isExists = true;
                                            break;
                                        }
                                    }
                                }

                                if (isExists == false) {

                                    salesmanWiseReportBean = new SalesmanWiseReportBean();

                                    salesmanWiseReportBean.setSalesManId(Report.getString(Report.getColumnIndex(DatabaseHandler.KEY_SALES_MAN_ID)));
                                    String salesman = HomeActivity.dbHandler.getSalesmanNameById(salesmanWiseReportBean.getSalesManId());
                                    salesmanWiseReportBean.setSalesManName(salesman);
                                    salesmanWiseReportBean.setTotalBills(1);
                                    String dateInMillis = Report.getString(Report.getColumnIndex(DatabaseHandler.KEY_InvoiceDate));
                                    Calendar c1 = Calendar.getInstance();
                                    c1.setTimeInMillis(Long.parseLong(dateInMillis));
                                    int mon = c1.get(Calendar.MONTH);
                                    salesmanWiseReportBean.setMonth(mon);
                                    salesmanWiseReportBean.setTaxableValue(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_TaxableValue)));
                                    salesmanWiseReportBean.setBillAmount(Report.getDouble(Report.getColumnIndex(DatabaseHandler.KEY_BillAmount)));

                                    if (!salesmanWiseReportBean.getSalesManId().isEmpty()) {
                                        salesmanWiseReportBeanArrayList.add(salesmanWiseReportBean);

                                    }
                                }

                                isExists = false;

                            } while (Report.moveToNext());
                            for(SalesmanWiseReportBean salesmanWiseReportBean1 : salesmanWiseReportBeanArrayList){
                                listStrData = new ArrayList<String>();
                                listStrData.add(salesmanWiseReportBean1.getSalesManId());
                                listStrData.add(String.valueOf(salesmanWiseReportBean1.getSalesManName()));
                                listStrData.add(String.valueOf(salesmanWiseReportBean1.getTotalBills()));
                                String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                                listStrData.add(months[salesmanWiseReportBean1.getMonth()]);
                                listStrData.add(String.valueOf(salesmanWiseReportBean1.getTaxableValue()));
                                listStrData.add(String.valueOf(salesmanWiseReportBean1.getBillAmount()));
                                listCSVData.add(listStrData);
                            }
                        } else {
                            listCSVData.clear();
                            return "No transaction has been done";
                        }
                        break;
                    default:
                        listCSVData.clear();
                        break;
                }
            } catch (Exception ex) {
                Logger.e(TAG, "Error in async task do in back ground method populate data." + ex.getMessage());
                listCSVData.clear();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String strResponse) {
            super.onPostExecute(strResponse);
            if (strResponse != null && !strResponse.isEmpty()) {
                DefaultFragmentForEmptyData defaultFragmentForEmptyData = new DefaultFragmentForEmptyData();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.employee_customer_report_container, defaultFragmentForEmptyData);
                fragmentTransaction.commit();
                MsgBox.Show("Warning", strResponse);
            }
            try {
                if (strResponse == null) {
                    switch (getSpinnerListIndex()) {
                        case 0: // Customer Detail Report
                            CustomerDetailReport customerDetailReport = new CustomerDetailReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", customerDetailReportBeanArrayList);
                            customerDetailReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.employee_customer_report_container, customerDetailReport);
                            fragmentTransaction.commit();
                            break;
                        case 1: // Customer Wise Report
                            CustomerWiseReport customerWiseReport = new CustomerWiseReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", customerWiseReportBeanArrayList);
                            customerWiseReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.employee_customer_report_container, customerWiseReport);
                            fragmentTransaction.commit();
                            break;
                       /* case 2: // Supplier Wise report
                            break;*/
                        case 2: // User Detail Report
                            UserDetailReport userDetailReport = new UserDetailReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", userDetailReportBeanArrayList);
                            userDetailReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.employee_customer_report_container, userDetailReport);
                            fragmentTransaction.commit();
                            break;
                        case 3: // User Wise Report
                            UserWiseReport userWiseReport = new UserWiseReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", userWiseReportBeanArrayList);
                            userWiseReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.employee_customer_report_container, userWiseReport);
                            fragmentTransaction.commit();
                            break;
                        case 4: // Salesman Detailed Report
                            SalesmanDetailReport salesmanDetailReport = new SalesmanDetailReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", salesmanDetailReportBeanArrayList);
                            salesmanDetailReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.employee_customer_report_container, salesmanDetailReport);
                            fragmentTransaction.commit();
                            break;
                        case 5:
                            employee_customer_report_container.removeAllViews();
                            SalesmanWiseReport salesmanWiseReport = new SalesmanWiseReport();
                            bundle = new Bundle();
                            bundle.putParcelableArrayList("dataList", salesmanWiseReportBeanArrayList);
                            salesmanWiseReport.setArguments(bundle);
                            fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.add(R.id.employee_customer_report_container, salesmanWiseReport);
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

    private void mExportReport() {
        if (!etReportDateStart_employee_customer.getText().toString().isEmpty() && !etReportDateEnd_employee_customer.getText().toString().isEmpty()) {
            if (listCSVData.size() >= 2) {
                ReportExportToCSVHelper reportExportToCSVHelper = ReportExportToCSVHelper.getInstance();
                reportExportToCSVHelper.mGenerateCSVFile(getActivity(), spnrReportNameSelection_employee_customer.getSelectedItem().toString(),
                        etReportDateStart_employee_customer.getText().toString(),
                        etReportDateEnd_employee_customer.getText().toString(), listCSVData);
            }
        } else {
            MsgBox.Show("Warning", "Please select date and try again.");
        }
    }

    private void ResetAll() {
        etReportDateStart_employee_customer.setText("");
        etReportDateEnd_employee_customer.setText("");
        txtPersonId.setText("");
        listCSVData.clear();
      /*  if(spnrCustomers != null && !spnrCustomers.getSelectedItem().toString().equals("Select")){
            spnrCustomers.setSelection(0);
        }*/
        DefaultFragmentForEmptyData defaultFragmentForEmptyData = new DefaultFragmentForEmptyData();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.employee_customer_report_container, defaultFragmentForEmptyData);
        fragmentTransaction.commit();
    }

}
