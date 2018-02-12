package pos.wepindia.com.retail.view.UploadGSTReports.GSTSupport;

import com.google.gson.Gson;

import pos.wepindia.com.retail.view.UploadGSTReports.GSTPojo.GSTR1Data;
import pos.wepindia.com.retail.view.UploadGSTReports.GSTPojo.GSTRData;

/**
 * Created by PriyabratP on 17-11-2016.
 */

public class GstJsonEncoder {


    public static String getGSTRJsonEncode(GSTRData gstrData){
        String jsonInString = "";
        try {
            Gson gson = new Gson();
            jsonInString = gson.toJson(gstrData);
            int i;
            return jsonInString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonInString;
    }

    public static String getGSTR1JsonEncode(GSTR1Data gstrData){
        String jsonInString = "";
        try {
            Gson gson = new Gson();
            jsonInString = gson.toJson(gstrData);
            int i;
            return jsonInString;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonInString;
    }


    /*public static String getGSTR2JsonEncode(String userName, GSTR2Data gstr2Data){
        GSTRData gstrData = new GSTRData(userName,gstr2Data);
        Gson gson = new Gson();
        String jsonInString = gson.toJson(gstrData);
        return jsonInString;
    }*/
}
