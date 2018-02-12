package pos.wepindia.com.retail.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;

import pos.wepindia.com.wepbase.Logger;

/**
 * Created by MohanN on 12/18/2017.
 */

public class PackageUtils {
    private static String TAG = PackageUtils.class.getName();

    public static void hideKeyboard(Context context){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            View view = ((Activity) context).getCurrentFocus();
            if(view != null){
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }catch (Exception ex){
            Logger.i(TAG, "Hide keyboard error : " + ex.getMessage());
        }
    }

    public static String FILE_PATH_IMAGE = Environment.getExternalStorageDirectory().getPath() + "/WeP_Retail/Images/";

    public static String getImagePath(String icon,String title){
        if(icon == null || icon.equalsIgnoreCase("")){
            String fileName = title;
            //icon = AppUtils.FILE_PATH_IMAGE+fileName.replace(" ","_")+".jpg";
            icon = PackageUtils.FILE_PATH_IMAGE +fileName+".jpg";
        }
        return icon;
    }

    public static final String REPORT_PATH = Environment.getExternalStorageDirectory().getPath() + "/WeP_Retail_Reports/";
    public static  void CheckReportsDirectory(){
        File ReportDirectory = new File(REPORT_PATH);
        Logger.d(TAG, "Does ReportDirectory exists? Ans:" + ReportDirectory.exists());
        if(!ReportDirectory.exists()){
            if(ReportDirectory.mkdir()){
                Logger.d(TAG, "ReportDirectory created");
            } else{
                Logger.d(TAG, "ReportDirectory does not created");
            }
        }
    }
}
