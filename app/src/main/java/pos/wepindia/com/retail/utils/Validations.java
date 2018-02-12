package pos.wepindia.com.retail.utils;

import android.content.Context;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pos.wepindia.com.retail.R;

/**
 * Created by RichaA on 10/23/2017.
 */

public class Validations {


    public static boolean checkGSTINValidation(String str) {
        boolean mFlag = false;
        try {
            if (str.trim().length() == 0) {
                mFlag = true;
            } else if (str.trim().length() > 0 && str.length() == 15) {
                Pattern p = Pattern.compile("^[0-9]{2}[a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ][0-9a-zA-Z]{1}$");
                Matcher m = p.matcher(str);
                if (m.find()) {
                    mFlag = true;
                } else {
                    mFlag = false;
                }
            } else {
                mFlag = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            mFlag = false;
        } finally {
            return mFlag;
        }
    }


    public static boolean checkValidStateCode(String gstin, Context activityContext) {
        if (gstin == null || gstin.equals(""))
            return true;
        boolean statecodePresent = false;
        //List<Integer> StateCodeList = Arrays.asList(R.array.poscode_list);
        List<String> StateCodeList = Arrays.asList(activityContext.getResources().getStringArray(R.array.poscode_list));
        String statecode = (gstin.substring(0, 2));
        if (StateCodeList.contains(statecode))
            statecodePresent = true;
        return statecodePresent;
    }




    public static boolean isValidEmailAddress(String email) {

        if(email.equalsIgnoreCase(""))
        {
            return true;
        }
        else {
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
            java.util.regex.Matcher m = p.matcher(email);
            return m.matches();
        }
    }

}
