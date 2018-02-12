package pos.wepindia.com.wepbase;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

abstract public class WepBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if(getCurrentFocus()!=null)
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(getCurrentFocus() != null) {
            hideKeyboard();
        }
        return true;
    }

    public abstract void onHomePressed();

}
