package pos.wepindia.com.retail.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;

/**
 * Created by MohanN on 1/31/2018.
 */

public class ReportExportToCSVHelper {

    private static final String TAG = ReportExportToCSVHelper.class.getName();
    private Context mContext = null;
    private MessageDialog msgBox;

    private static ReportExportToCSVHelper reportExportToCSVHelper = null;

    public static ReportExportToCSVHelper getInstance(){
        if(reportExportToCSVHelper == null){
            reportExportToCSVHelper = new ReportExportToCSVHelper();
        }
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
        }catch (Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
        return reportExportToCSVHelper;
    }

    public synchronized void mGenerateCSVFile(Context context,String ReportName, String StartDate, String EndDate,
                                  List<List<String>> listData){
        FileOutputStream fileOSReport;
        int iRow = 0, iColumn = 0;
        String strData, strReportFileName;
        byte[] bDataBuffer;
        byte[] bReportName = ReportName.getBytes();
        byte[] bColumnSeperator = (",").getBytes();
        byte[] bLineFeed = ("\n").getBytes();

        try{
            this.mContext = context;
            msgBox = new MessageDialog(mContext);
        }catch (Exception ex){
            Logger.e(TAG,"Unable to init the message box." + ex.getMessage());
        }

        try{
            PackageUtils.CheckReportsDirectory();
            // Set default file name.
            strReportFileName = ReportName +"{"+ StartDate + " To " + EndDate +"}" +".csv";
            fileOSReport = new FileOutputStream(PackageUtils.REPORT_PATH + strReportFileName);

            // Set Report name in file.
            // Center alignment for report name
           /* TableRow rowHeading = (TableRow)ReportTable.getChildAt(0);
            for(iColumn=1; iColumn<((rowHeading.getChildCount() / 2) + 1); iColumn++){*/
            fileOSReport.write(bColumnSeperator,0,bColumnSeperator.length);
            //}
            fileOSReport.write(bReportName,0,bReportName.length);

            // Line space between column headers and report date range
            fileOSReport.write(bLineFeed,0,bLineFeed.length);

            for(List<String> listStrData : listData){
                for(int j = 0; j < listStrData.size(); j++){
                    bDataBuffer = listStrData.get(j).getBytes();
                    fileOSReport.write(bDataBuffer,0,bDataBuffer.length);
                    if(j != (listStrData.size() - 1)){
                        fileOSReport.write(bColumnSeperator,0,bColumnSeperator.length);
                    }
                    else{
                        fileOSReport.write(bLineFeed,0,bLineFeed.length);
                    }
                }
            }
            // Flush the buffer and close the file
            fileOSReport.flush();
            fileOSReport.close();
            Toast.makeText(mContext,mContext.getString(R.string.report_exported_successfully),Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException ex) {
            // TODO Auto-generated catch block
            msgBox.Show("Exception", "Error(FNF): ExportReportToCSV - " + ex.getMessage());
            Logger.i(TAG,"Error on generating .csv file for reports." + ex.getMessage());
        } catch (IOException ex) {
            // TODO Auto-generated catch block
            msgBox.Show("Exception", "Error(IOE): ExportReportToCSV - " + ex.getMessage());
            Logger.i(TAG,"Error on generating .csv file for reports." + ex.getMessage());
        } catch (Exception ex){
            Logger.i(TAG,"Error on generating .csv file for reports." + ex.getMessage());
        }

    }
}
