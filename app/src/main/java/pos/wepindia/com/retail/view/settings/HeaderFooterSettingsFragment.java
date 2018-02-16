package pos.wepindia.com.retail.view.settings;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.EMOJI_FILTER;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.wepbase.Auditing;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;

/**
 * Created by MohanN on 12/5/2017.
 */

public class HeaderFooterSettingsFragment extends Fragment{

    private static final String TAG = HeaderFooterSettingsFragment.class.getName();

    //private DatabaseHelper dbHelper;
    View view;
    MessageDialog msgBox;

    @BindView(R.id.et_settings_header_text_one)     EditText edtHeaderOne;
    @BindView(R.id.et_settings_header_text_two)     EditText edtHeaderTwo;
    @BindView(R.id.et_settings_header_text_three)   EditText edtHeaderThree;
    @BindView(R.id.et_settings_header_text_four)    EditText edtHeaderFour;
    @BindView(R.id.et_settings_header_text_five)    EditText edtHeaderFive;
    @BindView(R.id.et_settings_Footer_one)          EditText edtFooterOne;
    @BindView(R.id.et_settings_Footer_two)          EditText edtFooterTwo;
    @BindView(R.id.et_settings_Footer_three)        EditText edtFooterThree;
    @BindView(R.id.et_settings_Footer_four)         EditText edtFooterFour;
    @BindView(R.id.et_settings_Footer_five)         EditText edtFooterFive;
    @BindView(R.id.bt_hf_apply)                     Button btnApply;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings_header_footer_fragment, container, false);
        msgBox = new MessageDialog(getActivity());
        ButterKnife.bind(this, view);
        //dbHelper = DatabaseHelper.getInstance(getActivity());
        //App crash error log

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try{
                if (Logger.LOG_FOR_FIRST) {
                Logger.printLog();
                Logger.LOG_FOR_FIRST = false;
            }
            if (Auditing.AUDIT_FOR_FIRST) {
                Auditing.printAudit();
                Auditing.AUDIT_FOR_FIRST = false;
            }
            applyValidations();
        }catch (Exception e)
        {
            Logger.e(TAG,e.getMessage());

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPopulateInsertedData();
    }

    @OnClick({R.id.bt_hf_apply})
    protected void onClickEvent(View view){
        switch (view.getId()){
            case R.id.bt_hf_apply:
                mInsertHeaderFooter();
                break;
            default:
                break;
        }
    }

    private void mInsertHeaderFooter(){


            if(edtHeaderOne.getText().toString().trim().equalsIgnoreCase("")) {
                msgBox.Show("Information", "Address line1 is mandatory");
            } else {
            String strHeader1 = edtHeaderOne.getText().toString().trim();
            String strHeader2 = edtHeaderTwo.getText().toString().trim();
            String strHeader3 = edtHeaderThree.getText().toString().trim();
            String strHeader4 = edtHeaderFour.getText().toString().trim();
            String strHeader5 = edtHeaderFive.getText().toString().trim();
            String strFooter1 = edtFooterOne.getText().toString().trim();
            String strFooter2 = edtFooterTwo.getText().toString().trim();
            String strFooter3 = edtFooterThree.getText().toString().trim();
            String strFooter4 = edtFooterFour.getText().toString().trim();
            String strFooter5 = edtFooterFive.getText().toString().trim();
            int iResult = 0;
            // Update new settings in database
            iResult = HomeActivity.dbHandler.updateHeaderFooterText(strHeader1, strHeader2, strHeader3, strHeader4, strHeader5,
                    strFooter1, strFooter2, strFooter3, strFooter4, strFooter5);
            if (iResult > 0) {
               // msgBox.Show("Information", "Saved Successfully");
                Toast.makeText(getActivity(), "Data stored successfully", Toast.LENGTH_SHORT).show();
            } else {
                msgBox.Show("Exception", "Failed to save. Please try again");
            }
        }
    }
    private void mPopulateInsertedData(){
        Cursor curHeaderFooter = null;
        curHeaderFooter = HomeActivity.dbHandler.getBillSettings();
        try{
            if(curHeaderFooter != null && curHeaderFooter.getCount() > 0){
                curHeaderFooter.moveToFirst();
                if(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_HeaderText1)) != null){
                    edtHeaderOne.setText(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_HeaderText1)));
                }
                if(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_HeaderText2)) != null){
                    edtHeaderTwo.setText(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_HeaderText2)));
                }
                if(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_HeaderText3)) != null){
                    edtHeaderThree.setText(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_HeaderText3)));
                }
                if(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_HeaderText4)) != null){
                    edtHeaderFour.setText(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_HeaderText4)));
                }
                if(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_HeaderText5)) != null){
                    edtHeaderFive.setText(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_HeaderText5)));
                }

                if(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_FooterText1)) != null){
                    edtFooterOne.setText(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_FooterText1)));
                }
                if(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_FooterText2)) != null){
                    edtFooterTwo.setText(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_FooterText2)));
                }
                if(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_FooterText3)) != null){
                    edtFooterThree.setText(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_FooterText3)));
                }
                if(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_FooterText4)) != null){
                    edtFooterFour.setText(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_FooterText4)));
                }
                if(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_FooterText5)) != null){
                    edtFooterFive.setText(curHeaderFooter.getString(curHeaderFooter.getColumnIndex(DatabaseHandler.KEY_FooterText5)));
                }

            }
        } catch (Exception ex){
            Logger.e(TAG,"Unable able to get data from the table bill settings for header and footer " +ex.getMessage());
        }finally {
            if(curHeaderFooter != null){
                curHeaderFooter.close();
            }
        }
    }

    int counter =0;
    void applyValidations()
    {
        edtFooterOne.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtFooterTwo.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtFooterThree.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtFooterFour.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtFooterFive.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtHeaderOne.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtHeaderTwo.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtHeaderThree.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtHeaderFour.setFilters(new InputFilter[]{new EMOJI_FILTER()});
        edtHeaderFive.setFilters(new InputFilter[]{new EMOJI_FILTER()});

        edtFooterOne.addTextChangedListener(restrictLengthWatcher);
        edtFooterTwo.addTextChangedListener(restrictLengthWatcher);
        edtFooterThree.addTextChangedListener(restrictLengthWatcher);
        edtFooterFour.addTextChangedListener(restrictLengthWatcher);
        edtFooterFive.addTextChangedListener(restrictLengthWatcher);

        edtHeaderOne.addTextChangedListener(restrictLengthWatcher);
        edtHeaderTwo.addTextChangedListener(restrictLengthWatcher);
        edtHeaderThree.addTextChangedListener(restrictLengthWatcher);
        edtHeaderFour.addTextChangedListener(restrictLengthWatcher);
        edtHeaderFive.addTextChangedListener(restrictLengthWatcher);

    }

    TextWatcher restrictLengthWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        @Override
        public void afterTextChanged(Editable editable) {

            try{
                if(counter>0)
                {
                    counter--;
                    return;
                }


                if(editable == edtFooterOne.getEditableText())
                {
                    String content = edtFooterOne.getText().toString();
                    if(content.length()>30) {
                        counter++;
                        edtFooterOne.setText(content.substring(0,30));
                        edtFooterOne.setSelection(edtFooterOne.getText().length());

                    }
                }
                else if(editable == edtFooterTwo.getEditableText())
                {
                    String content = edtFooterTwo.getText().toString();
                    if(content.length()>30) {
                        counter++;
                        edtFooterTwo.setText(content.substring(0,30));
                        edtFooterTwo.setSelection(edtFooterTwo.getText().length());

                    }
                }
                else if(editable == edtFooterThree.getEditableText())
                {
                    String content = edtFooterThree.getText().toString();
                    if(content.length()>30) {
                        counter++;
                        edtFooterThree.setText(content.substring(0,30));
                        edtFooterThree.setSelection(edtFooterThree.getText().length());

                    }
                }
                else if(editable == edtFooterFour.getEditableText())
                {
                    String content = edtFooterFour.getText().toString();
                    if(content.length()>30) {
                        counter++;
                        edtFooterFour.setText(content.substring(0,30));
                        edtFooterFour.setSelection(edtFooterFour.getText().length());

                    }
                }
                else if(editable == edtFooterFive.getEditableText())
                {
                    String content = edtFooterFive.getText().toString();
                    if(content.length()>30) {
                        counter++;
                        edtFooterFive.setText(content.substring(0,30));
                        edtFooterFive.setSelection(edtFooterFive.getText().length());

                    }
                }
                else if(editable == edtHeaderOne.getEditableText())
                {
                    String content = edtHeaderOne.getText().toString();
                    if(content.length()>30) {
                        counter++;
                        edtHeaderOne.setText(content.substring(0,30));
                        edtHeaderOne.setSelection(edtHeaderOne.getText().length());

                    }
                }
                else if(editable == edtHeaderTwo.getEditableText())
                {
                    String content = edtHeaderTwo.getText().toString();
                    if(content.length()>30) {
                        counter++;
                        edtHeaderTwo.setText(content.substring(0,30));
                        edtHeaderTwo.setSelection(edtHeaderTwo.getText().length());

                    }
                }
                else if(editable == edtHeaderThree.getEditableText())
                {
                    String content = edtHeaderThree.getText().toString();
                    if(content.length()>30) {
                        counter++;
                        edtHeaderThree.setText(content.substring(0,30));
                        edtHeaderThree.setSelection(edtHeaderThree.getText().length());

                    }
                }
                else if(editable == edtHeaderFour.getEditableText())
                {
                    String content = edtHeaderFour.getText().toString();
                    if(content.length()>30) {
                        counter++;
                        edtHeaderFour.setText(content.substring(0,30));
                        edtHeaderFour.setSelection(edtHeaderFour.getText().length());

                    }
                }
                else if(editable == edtHeaderFive.getEditableText())
                {
                    String content = edtHeaderFive.getText().toString();
                    if(content.length()>30) {
                        counter++;
                        edtHeaderFive.setText(content.substring(0,30));
                        edtHeaderFive.setSelection(edtHeaderFive.getText().length());

                    }
                }
            }catch (Exception e)
            {
                Logger.e(TAG, "Error occured");
            }

        }
    };
   /* @Override
    public void onDestroy() {
        super.onDestroy();
        if(dbHelper != null) {
            dbHelper.close();
        }
    }*/
}