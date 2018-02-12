package  pos.wepindia.com.wepbase;

import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SachinV on 30-11-2017.
 */

public class Logger {

    public static boolean DEBUG = true;
    public static boolean LOG_FOR_FIRST = true;

    private static final String LOG_GENERATE_PATH = Environment.getExternalStorageDirectory().getPath() + "/WeP_Retail_Logs/";

    public static final void v (String tag, String msg) {
        if (DEBUG) Log.v(tag, msg);
    }

    public static final void d (String msg) {
        if (DEBUG) Log.d(getClassNameMethodNameAndLineNumber(), msg);
    }

    public static final void d (String tag, String msg) {
        if (DEBUG) Log.d(tag, msg);
    }

    public static final void i (String tag, String msg) {
        if (DEBUG) Log.i(tag, msg);
    }

    public static final void w (String tag, String msg) {
        if (DEBUG) Log.w(tag, msg);
    }

    public static final void e (String tag, String msg) {
        if (DEBUG) Log.e(tag, msg);
    }

    public static final void e (String tag, String msg, Exception e) {
        if (DEBUG) Log.e(tag, msg, e);
    }

    public static final void wtf (String tag, String msg) { // wtf -> what a terrible failure :P
        if (DEBUG) Log.wtf(tag, msg);
    }

    public static void printLog(){
        File directory = new File(LOG_GENERATE_PATH);
        if (!directory.exists())
            directory.mkdirs();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        Date date = new Date();

        String filename = LOG_GENERATE_PATH  + "GSTGenie_" + dateFormat.format(date) + ".txt";
        final String[] log_cmd = new String[] { "logcat", "-v", "time", "-f", filename, " *:V"};
        final String[] clear_cmd = new String[] { "logcat", "-c"};

        try{
            Runtime.getRuntime().exec(clear_cmd);
            Runtime.getRuntime().exec(log_cmd);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private static String getClassName() {
        String fileName = Thread.currentThread().getStackTrace()[5].getFileName();
        return fileName.substring(0, fileName.length() - 5);
    }

    private static String getMethodName() {
        return Thread.currentThread().getStackTrace()[5].getMethodName();
    }

    private static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[5].getLineNumber();
    }

    private static String getPackageName() {
        return BuildConfig.APPLICATION_ID;
    }

    public static String getClassNameMethodNameAndLineNumber() {
        return "[" + getPackageName() + "::" + getClassName() + "." + getMethodName() + "()-" + getLineNumber() + "]: ";
    }
}
