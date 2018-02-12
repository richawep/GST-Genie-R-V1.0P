package  pos.wepindia.com.wepbase;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by SachinV on 05-12-2017.
 */

public class Auditing {

    public static boolean DEBUG = true;
    public static boolean AUDIT_FOR_FIRST = true;
    private static final String AUDIT_GENERATE_PATH = Environment.getExternalStorageDirectory().getPath() + "/WeP_Retail_Audits/";

    public static final void d (String msg) {
        if (DEBUG) Log.d("Audit", msg);
    }

    public static void printAudit(){

        File directory = new File(AUDIT_GENERATE_PATH);

        if (!directory.exists())
            directory.mkdirs();

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH:mm:ss");
        Date date = new Date();

        String filename = AUDIT_GENERATE_PATH  + "Audit_" + dateFormat.format(date) + ".txt";

        final String[] audit_cmd = new String[] { "logcat", "-v", "time", "-f", filename, "Audit:D"," *:S"};

        try{
            Runtime.getRuntime().exec(audit_cmd);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
