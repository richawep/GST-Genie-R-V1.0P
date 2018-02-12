package pos.wepindia.com.retail.view.Configuration.loyalty;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.DecimalDigitsInputFilter;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;

/**
 * Created by MohanN on 12/7/2017.
 */

public class RewardPointsFragment extends Fragment  {

    private static final String TAG = RewardPointsFragment.class.getName();

    View view;
    MessageDialog msgBox;
    Context myContext;

    @BindView(R.id.et_config_loyalty_conversion)  EditText edtAmtToPt;
    @BindView(R.id.et_config_loyalty_points)  EditText edtLoyaltyPt;
    @BindView(R.id.et_config_loyalty_amt_equivalent)  EditText edtLoyaltyPtToAmt;
    @BindView(R.id.et_config_loyalty_min_to_redeem)  EditText edtLoyaltyPtLimit;
    @BindView(R.id.bt_config_loyalty_update) Button btnUpdate;
    @BindView(R.id.bt_config_loyalty_clear)  Button btnClear;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        view = inflater.inflate(R.layout.configuration_loyalty_fragment, container, false);
        ButterKnife.bind(this,view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myContext = getActivity();
        msgBox = new MessageDialog(myContext);
        try{
            applyDecimalValidation();

        }catch (Exception e)
        {
            Logger.e(TAG, e.getMessage());
        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        mDisplayLoyaltyConfiguration();
    }
    void applyDecimalValidation()
    {
        edtAmtToPt.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(6,2)});
        edtLoyaltyPtToAmt.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(6,2)});
    }

    void mDisplayLoyaltyConfiguration()
    {
        Cursor cursor = HomeActivity.dbHandler.getRewardPointsConfiguration();
        try{

            if(cursor!=null && cursor.moveToNext())
            {
                double amtTopt = cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_AmtToPt));
                int loyaltyPt = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_RewardPoints));
                double ptToAmt = cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RewardPointsToAmt));
                int limit = cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_RewardPointsLimit));

                edtAmtToPt.setText(String.format("%.2f",amtTopt));
                edtLoyaltyPt.setText(""+loyaltyPt);
                edtLoyaltyPtToAmt.setText(String.format("%.2f",ptToAmt));
                edtLoyaltyPtLimit.setText(""+limit);
            }
        }catch(Exception e)
        {
            Logger.e(TAG, e.getMessage());
        }
    }

    @OnClick({R.id.bt_config_loyalty_update, R.id.bt_config_loyalty_clear})
    public void buttonClickEvent(View view)
    {

        switch (view.getId())
        {
            case R.id.bt_config_loyalty_update :mUpdate();
            break;
            case R.id.bt_config_loyalty_clear : mClearWidgets();
            break;
        }
    }

    void mUpdate()
    {
        String amtTopt_str = edtAmtToPt.getText().toString().trim().equals("") ? "0.00": edtAmtToPt.getText().toString().trim();
        String LoyaltyPt_str = edtLoyaltyPt.getText().toString().trim().equals("") ? "0": edtLoyaltyPt.getText().toString().trim();
        String ptToAmt_str = edtLoyaltyPtToAmt.getText().toString().trim().equals("") ? "0.00": edtLoyaltyPtToAmt.getText().toString().trim();
        String limit_str = edtLoyaltyPtLimit.getText().toString().trim().equals("") ? "0": edtLoyaltyPtLimit.getText().toString().trim();

        try{

            double  amtTopt = Double.parseDouble(String.format("%.2f",Double.parseDouble(amtTopt_str)));
            int  loyaltyPt = Integer.parseInt(LoyaltyPt_str);
            double  ptToAmt = Double.parseDouble(String.format("%.2f",Double.parseDouble(ptToAmt_str)));
            int  limit = Integer.parseInt(limit_str);

            edtAmtToPt.setText(""+amtTopt);
            edtLoyaltyPt.setText(""+loyaltyPt);
            edtLoyaltyPtToAmt.setText(""+ptToAmt);
            edtLoyaltyPtLimit.setText(""+limit);



            if(HomeActivity.dbHandler.updateRewardPointsConfiguration(amtTopt,loyaltyPt,ptToAmt,limit) >0) {
               // msgBox.Show("Note", "Updation successful");
                Toast.makeText(myContext, "Updation successful", Toast.LENGTH_SHORT).show();
            }
            else {
               // msgBox.Show("Note", "Updation Failed");
                Toast.makeText(myContext, "Updation Failed", Toast.LENGTH_SHORT).show();
            }

        }
            catch (Exception e)
        {
            Logger.e(TAG, e.getMessage());
        }

    }
    void mClearWidgets()
    {
        edtAmtToPt.setText("");
        edtLoyaltyPt.setText("");
        edtLoyaltyPtToAmt.setText("");
        edtLoyaltyPtLimit.setText("");
    }

}