package pos.wepindia.com.retail.view.ItemMasters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pos.wepindia.com.retail.Constants;
import pos.wepindia.com.retail.GenericClass.FilePickerDialogFragment;
import pos.wepindia.com.retail.GenericClass.MessageDialog;
import pos.wepindia.com.retail.GenericClass.OnFilePickerClickListener;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.utils.EMOJI_FILTER;
import pos.wepindia.com.retail.view.HomeActivity;
import pos.wepindia.com.retail.view.ItemMasters.ItemAdaptersAndListeners.OnItemAddListetner;
import pos.wepindia.com.retail.view.ItemMasters.ItemAdaptersAndListeners.SimpleCursorRecyclerAdapter;
import pos.wepindia.com.wepbase.Logger;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.ItemModel;


public class ItemMastersFragment extends Fragment  implements OnItemAddListetner,Constants, OnFilePickerClickListener{

    private static final String TAG = ItemMastersFragment.class.getSimpleName();
    private String CSV_GENERATE_PATH = Environment.getExternalStorageDirectory().getPath() + "/WeP_Retail_CSVs/";
    private String FILENAME = "Sample_Items.csv";

    Context myContext;
    MessageDialog MsgBox;
    static SimpleCursorRecyclerAdapter adapter;

    LoadItemsAsync loadItemsAsync = null;
    String[] from = {"ItemShortName", "Quantity", "UOM", "RetailPrice", "IGSTRate"};
    int[] to = new int[]{R.id.tv_item_master_list_title_name, R.id.tv_item_master_list_title_qty,
            R.id.tv_item_master_list_title_uom,
            R.id.tv_item_master_list_title_retail, R.id.tv_item_master_list_title_disc};
    ProgressDialog progressDialog;

    private long lastClickTime = 0;
    private BufferedReader buffer;

    @BindView(R.id.bt_item_master_list_add)
    Button btItemMasterAdd;
    @BindView(R.id.rv_item_master_list_items)
    RecyclerView rv_list;
    @BindView(R.id.bt_item_master_list_count)
    Button btnItemCount;
    @BindView(R.id.autocomplete_item_master_list_search)
    AutoCompleteTextView actv_itemsearch;
    @BindView(R.id.iv_item_master_list_refresh)
    ImageView iv_refresh;

    @BindView(R.id.bt_item_master_list_generate_csv)
    Button btnGenerateCSV;
    @BindView(R.id.bt_item_master_list_upload_file)
    Button btnUploadCSV;

    @BindView(R.id.bt_item_master_list_clear) Button btnClear;

    @BindView(R.id.rb_item_master_list_all)
    RadioButton rb_displayAll;
    @BindView(R.id.rb_item_master_list_brand)
    RadioButton rb_displayBrandwise;
    @BindView(R.id.rb_item_master_list_department)
    RadioButton rb_displayDepartmentwise;
    @BindView(R.id.rb_item_master_list_category)
    RadioButton rb_displayCategorywise;
    @BindView(R.id.rb_item_master_list_active)
    RadioButton rb_displayActiveItems;
    @BindView(R.id.rb_item_master_list_inactive)
    RadioButton rb_displayInactiveItems;
    @BindView(R.id.rg_item_master_list) RadioGroup rg_displayItemCriteria;
    @BindView(R.id.ll_itemMaster)  LinearLayout ll_itemMasters;
    @BindView(R.id.tv_item_master_list_file_path) TextView tv_filePath;

    String strUploadFilepath = "";
    private String mUserCSVInvalidValue = "";
    private boolean mFlag;
    private Map<Integer, ItemModel> mHashMapItemCode = new TreeMap<>();
    private int mCheckCSVValueType;
    private final int CHECK_INTEGER_VALUE = 0;
    private final int CHECK_DOUBLE_VALUE = 1;
    private final int CHECK_STRING_VALUE = 2;
    private ArrayList<Integer> mBrandCodeList = new ArrayList<>();
    private ArrayList<Integer> mDepartmentCodeList = new ArrayList<>();
    private Map<Integer, Integer> mCategoryCodeList = new LinkedHashMap<>();

    private int mShortCode = -1;
    private String mItemShortName = "";
    private String mItemLongName = "";
    private double mRetailPrice = 0.00;
    private double mMRP = 0.00;
    private double mWholeSalePrice = 0.00;
    private double mQuantity = 0.00;
    private String mUOM = "";
    private double mCGSTRate = 0.00;
    private double mSGSTRate = 0.00;
    private double mIGSTRate = 0.00;
    private double mCESSRate = 0.00;
    private double mDiscountPercent = 0.00;
    private double mDiscountAmount = 0.00;
    private int mDeptCode = 0;
    private int mCategCode = 0;
    private int mBrandCode = 0;
    private String mbarCode = "";
    private String mImageUri = "";
    private String mHSN = "";
    private int mFav = -1;
    private int mActive = -1;

    public static ProgressBar pb;
    private int itemCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_item_masters, container, false);
        ButterKnife.bind(this, fragmentView);
        pb = (ProgressBar) fragmentView.findViewById(R.id.progressBar);
        return fragmentView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("ItemMaster");
        myContext = getActivity();
        MsgBox = new MessageDialog(myContext);
        try {
            displayItemList();
            loadAutocompleteData();
            clickEvent();
            applyValidations();
            setAllDepartmentCode();
            setAllBrandCode();
            mClear();
        } catch (Exception e) {
            Logger.e(TAG, Logger.getClassNameMethodNameAndLineNumber(), e);
        }
        Logger.d(TAG, "onViewCreated()");
    }

    @OnClick({R.id.rb_item_master_list_all, R.id.rb_item_master_list_brand, R.id.rb_item_master_list_department, R.id.rb_item_master_list_category,
            R.id.rb_item_master_list_active, R.id.rb_item_master_list_inactive})
    public void onRadioButtonClicked(RadioButton radioButton) {
        displayItemList();
    }

    @OnClick({R.id.bt_item_master_list_add, R.id.iv_item_master_list_refresh,
            R.id.bt_item_master_list_clear,R.id.bt_item_master_list_generate_csv,
            R.id.bt_item_master_list_browse_file, R.id.bt_item_master_list_upload_file})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_item_master_list_add:
                /*pb.setIndeterminate(true);
                pb.setVisibility(View.VISIBLE);
                ll_itemMasters.setVisibility(View.GONE);*/

                ItemMasterAddItemDialogFragment myFragment =
                        (ItemMasterAddItemDialogFragment)getFragmentManager().findFragmentByTag("Add new Item");
                if (myFragment != null && myFragment.isVisible()) {
                    return;
                }

                FragmentManager fm = getActivity().getSupportFragmentManager();
                ItemMasterAddItemDialogFragment itemMasterAddItemDialogFragment = new ItemMasterAddItemDialogFragment();
                itemMasterAddItemDialogFragment.mInitListener(this);
                itemMasterAddItemDialogFragment.show(fm, "Add new Item");
                break;
            case R.id.bt_item_master_list_generate_csv:
                generateCSV();
                break;

            case R.id.bt_item_master_list_clear : mClear();
                break;
            case R.id.bt_item_master_list_browse_file:
                browseFile();
                break;
            case R.id.bt_item_master_list_upload_file:
                uploadCSV();
                break;
            case R.id.iv_item_master_list_refresh:
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000){
                    return;
                }
                actv_itemsearch.setText("");
                tv_filePath.setText("");
                loadAutocompleteData();
                displayItemList();
                btItemMasterAdd.setEnabled(true);
                btItemMasterAdd.setTextColor(getResources().getColor(R.color.white));
                break;
            default:
                break;
        }
    }

    void browseFile()
    {
        FilePickerDialogFragment myFragment1 =
                (FilePickerDialogFragment)getFragmentManager().findFragmentByTag("File Picker");
        if (myFragment1 != null && myFragment1.isVisible()) {
            return;
        };
        Bundle bundle = new Bundle();
        bundle.putString("contentType","csv");
        bundle.putString(FRAGMENTNAME,TAG);
        FragmentManager fm1 = getActivity().getSupportFragmentManager();
        FilePickerDialogFragment frag = new FilePickerDialogFragment();
        frag.setArguments(bundle);
        frag.mInitListener(ItemMastersFragment.this);
        frag.show(fm1, "File Picker");
    }
    void uploadCSV(){
        try {
            if (strUploadFilepath.equalsIgnoreCase("")) {
                Toast.makeText(myContext, "No File Found", Toast.LENGTH_SHORT).show();
            } else {
                String path = strUploadFilepath;
                       /* FileInputStream inputStream = new FileInputStream(path);
                        buffer = new BufferedReader(new InputStreamReader(inputStream));*/
                buffer = new BufferedReader(
                        new InputStreamReader(new FileInputStream(path), "ISO-8859-1"));
                setCSVFileToDB(itemCount);
            }
        } catch (IOException e) {
            Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            tv_filePath.setText("");
            strUploadFilepath = "";
        }
    }
    private void setCSVFileToDB(int itemCount) {

        if (itemCount > 0) {
            showCSVAlertMessage();
        } else {
            downloadCSVData();
        }
    }
    private void showCSVAlertMessage() {

        AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                .setTitle("Replace Item")
                .setIcon(R.mipmap.ic_company_logo)
                .setMessage(" Are you sure you want to Replace all the existing Items, if any")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        downloadCSVData();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //ClearItemTable();
                        displayItemList();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    private void downloadCSVData() {
        new AsyncTask<Void, Void, Void>() {
            ProgressDialog pd;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(myContext);
                pd.setMessage("Loading...");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                readCSVValue();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                try {
                    if (mFlag) {
                        MsgBox.Show("Note", mUserCSVInvalidValue);
                    } else if (mUserCSVInvalidValue.equals("")) {
                        Toast.makeText(myContext, "Items Imported Successfully", Toast.LENGTH_LONG).show();
                    }
                    displayItemList();
                    loadAutocompleteData();
                    mClear();
                    //ClearItemTable();

                    pd.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
    /**
     * Read response from CSV file and set data to in Data base
     * checking All possible validation
     */

    private void readCSVValue() {

        mUserCSVInvalidValue = "";
        String checkUOMTypye = "BAG, BAL, BDL, BKL, BOU, BOX, BTL, BUN, CAN, CBM, CCM, CMS, CTN, DOZ, DRM, GGR, GMS, GRS, " +
                "GYD, KGS, KLR, KME, MLT, MTR, NOS, PAC, PCS, PRS, QTL, ROL, SET, SQF, SQM, SQY, TBS, TGM, THD, TON, TUB, UGS, UNT, YDS, OTH";
        String[] checkSupplyType = {"G", "S"};
        String csvHeading = "ITEM SHORT NAME,ITEM LONG NAME,SHORT CODE,UOM,RETAIL PRICE,MRP,WHOLE SALE PRICE,CGST RATE,SGST RATE,IGST RATE,CESS RATE,HSN,QUANTITY,BARCODE,BRAND CODE,DEPARMENT CODE,CATEGORY CODE,FAVORITE,ACTIVE,IMAGEURL";
        boolean flag;
        boolean mCSVHashCheckflag = false;
        try {
            String line;
            String chechCSVHeaderLine;
            chechCSVHeaderLine = buffer.readLine();


            mFlag = false;

            flag = csvHeading.equalsIgnoreCase(chechCSVHeaderLine);

            if (!flag) {
                mFlag = true;
                mUserCSVInvalidValue = getResources().getString(R.string.header_value_empty) + "\n" + csvHeading;
                //+ "MENU CODE,ITEM NAME,SUPPLY TYPE,RATE 1,RATE 2,RATE 3,QUANTITY,UOM,CGST RATE,SGST RATE,IGST RATE,cess RATE,DISCOUNT PERCENT";
                return;
            }

            mHashMapItemCode.clear();

            while ((line = buffer.readLine()) != null) {
                if (line.length() == 20)
                    continue;

                final String[] colums = line.split(",", -1);

                if (colums.length == 0)
                    continue;
                else if (colums.length != 20) {
                    mFlag = true;
                    mUserCSVInvalidValue = getResources().getString(R.string.insufficient_information);
                    break;
                }

                // Item short name
                if (colums[0] != null && colums[0].length() > 0 && colums[0].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[0], "String");
                    if (mCheckCSVValueType == CHECK_STRING_VALUE) {
                        if (colums[0].length() > 20)
                            mItemShortName = colums[0].substring(0, 21).toUpperCase();
                        else
                            mItemShortName = colums[0].toUpperCase();
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.item_short_name_invalid) + "" + colums[2];
                        break;
                    }
                } else {
                    mFlag = true;
                    mUserCSVInvalidValue = getResources().getString(R.string.item_short_name_empty) + "" + colums[2];
                    break;
                }

                // Item long name
                if (colums[1] != null && colums[1].length() > 0 && colums[1].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[1], "String");
                    if (mCheckCSVValueType == CHECK_STRING_VALUE) {
                        mItemLongName = colums[1].toUpperCase();
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.item_long_name_invalid) + "" + colums[2];
                        break;
                    }
                }

                // Short code
                if (colums[2] != null && colums[2].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[2], "Int");
                    if (mCheckCSVValueType == CHECK_INTEGER_VALUE) {
                        mShortCode = Integer.parseInt(colums[2]);
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.short_code_invalid) + colums[0];
                        break;
                    }
                }

                // UOM
                if (colums[3] != null && colums[3].length() > 0 && colums[3].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[3], "String");
                    if (mCheckCSVValueType == CHECK_STRING_VALUE) {

                        if (colums[3].trim().length() == 3 && checkUOMTypye.contains(colums[3])) {
                            mUOM = colums[3];
                        } else {
                            mFlag = true;
                            mUserCSVInvalidValue = getResources().getString(R.string.uom_invalid) + colums[0] + "e.g " + checkUOMTypye;
                            break;
                        }
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.uom_invalid) + colums[0];
                        break;
                    }
                } else {
                    mFlag = true;
                    mUserCSVInvalidValue = getResources().getString(R.string.uom_empty) + colums[0];
                    break;
                }

                // Retail Price
                if (colums[4] != null && colums[4].length() > 0 && colums[4].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[4], "Double");
                    int mCheckCSVValueType1 = checkCSVTypeValue(colums[4], "Int");
                    if (mCheckCSVValueType == CHECK_DOUBLE_VALUE || mCheckCSVValueType1 == CHECK_INTEGER_VALUE) {
                        mRetailPrice = Double.parseDouble(String.format("%.2f", Double.parseDouble(colums[4])));
                        if (!(mRetailPrice >= 0 && mRetailPrice < 999999998)) {
                            mFlag = true;
                            mUserCSVInvalidValue = "Please enter Retail Price between 0 and 999999998 for item " + colums[0];
                            break;
                        }
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.retail_price_invalid) + colums[0];
                        break;
                    }
                } else {
                    mFlag = true;
                    mUserCSVInvalidValue = getResources().getString(R.string.retail_price_empty) + colums[0];
                    break;
                }

                // MRP
                if (colums[5] != null && colums[5].length() > 0 && colums[5].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[5], "Double");
                    int mCheckCSVValueType1 = checkCSVTypeValue(colums[5], "Int");
                    if (mCheckCSVValueType == CHECK_DOUBLE_VALUE || mCheckCSVValueType1 == CHECK_INTEGER_VALUE) {
                        mMRP = Double.parseDouble(String.format("%.2f", Double.parseDouble(colums[5])));
                        if (!(mMRP >= 0 && mMRP < 999999998)) {
                            mFlag = true;
                            mUserCSVInvalidValue = "Please enter MRP between 0 and 999999998 for item " + colums[0];
                            break;
                        }
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.mrp_invalid) + colums[0];
                        break;
                    }
                }

                // Whole Sale Price
                if (colums[6] != null && colums[6].length() > 0 && colums[6].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[6], "Double");
                    int mCheckCSVValueType1 = checkCSVTypeValue(colums[6], "Int");
                    if (mCheckCSVValueType == CHECK_DOUBLE_VALUE || mCheckCSVValueType1 == CHECK_INTEGER_VALUE) {
                        mWholeSalePrice = Double.parseDouble(String.format("%.2f", Double.parseDouble(colums[6])));
                        if (!(mWholeSalePrice >= 0 && mWholeSalePrice < 999999998)) {
                            mFlag = true;
                            mUserCSVInvalidValue = "Please enter Whole Sale Price between 0 and 999999998 for item " + colums[0];
                            break;
                        }
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.whole_sale_price_invalid) + colums[0];
                        break;
                    }
                }

                // CGST Rate
                if (colums[7] != null && colums[7].length() > 0 && colums[7].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[7], "Double");
                    int mCheckCSVValueType1 = checkCSVTypeValue(colums[7], "Int");
                    if (mCheckCSVValueType == CHECK_DOUBLE_VALUE || mCheckCSVValueType1 == CHECK_INTEGER_VALUE) {
                        mCGSTRate = Double.parseDouble(String.format("%.2f", Double.parseDouble(colums[7])));
                        if (mCGSTRate > 99.99 || mCGSTRate < 0) {
                            mFlag = true;
                            mUserCSVInvalidValue = "Please enter CGST Rate between 0 and 99.99 for item " + colums[0];
                            return;
                        }
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.cgst_rate_invalid) + colums[0];
                        break;
                    }
                }

                // SGST Rate
                if (colums[8] != null && colums[8].length() > 0 && colums[8].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[8], "Double");
                    int mCheckCSVValueType1 = checkCSVTypeValue(colums[8], "Int");
                    if (mCheckCSVValueType == CHECK_DOUBLE_VALUE || mCheckCSVValueType1 == CHECK_INTEGER_VALUE) {
                        mSGSTRate = Double.parseDouble(String.format("%.2f", Double.parseDouble(colums[8])));
                        if (mSGSTRate > 99.99 || mSGSTRate < 0) {
                            mFlag = true;
                            mUserCSVInvalidValue = "Please enter SGST Rate between 0 and 99.99 for item " + colums[0];
                            return;
                        }
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.sgst_rate_invalid) + colums[0];
                        break;
                    }
                }

                if (mCGSTRate + mSGSTRate < 0 || mCGSTRate + mSGSTRate > 99.99) {
                    mFlag = true;
                    mUserCSVInvalidValue = "Please note sum of SGST Rate and CGST Rate should between 0 and 99.99 for item " + colums[1];
                    return;
                }

                // IGST Rate
                if (colums[9] != null && colums[9].length() > 0 && colums[9].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[9], "Double");
                    int mCheckCSVValueType1 = checkCSVTypeValue(colums[9], "Int");
                    if (mCheckCSVValueType == CHECK_DOUBLE_VALUE || mCheckCSVValueType1 == CHECK_INTEGER_VALUE) {
                        mIGSTRate = Double.parseDouble(String.format("%.2f", Double.parseDouble(colums[9])));
                        if (mIGSTRate > 99.99 || mIGSTRate < 0) {
                            mFlag = true;
                            mUserCSVInvalidValue = "Please enter IGST Rate between 0 and 99.99 for item " + colums[0];
                            return;
                        }
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.igst_rate_invalid) + colums[0];
                        break;
                    }
                }

                /*if (mCGSTRate + mSGSTRate != mIGSTRate) {
                    mFlag = true;
                    mUserCSVInvalidValue = "Please note sum of SGST Rate and CGST Rate should between equal to IGST Rate for item " + colums[1];
                    return;
                }*/

                // CESS Rate
                if (colums[10] != null && colums[10].length() > 0 && colums[10].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[10], "Double");
                    int mCheckCSVValueType1 = checkCSVTypeValue(colums[10], "Int");
                    if (mCheckCSVValueType == CHECK_DOUBLE_VALUE || mCheckCSVValueType1 == CHECK_INTEGER_VALUE) {
                        mCESSRate = Double.parseDouble(String.format("%.2f", Double.parseDouble(colums[10])));
                        if (mCESSRate > 99.99 || mCESSRate < 0) {
                            mFlag = true;
                            mUserCSVInvalidValue = "Please enter cess Rate between 0 and 99.99 for item " + colums[0];
                            return;
                        }
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.cess_rate_invalid) + colums[0];
                        break;
                    }
                }

                // HSN
                if (colums[11] != null && colums[11].trim().length() > 0) {
                    mHSN = colums[11];
                }

                // Quantity
                if (colums[12] != null && colums[12].length() > 0 && colums[12].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[12], "Double");
                    int mCheckCSVValueType1 = checkCSVTypeValue(colums[12], "Int");
                    if (mCheckCSVValueType == CHECK_DOUBLE_VALUE || mCheckCSVValueType1 == CHECK_INTEGER_VALUE) {
                        mQuantity = Double.parseDouble(String.format("%.2f", Double.parseDouble(colums[12])));
                        if (mQuantity > 9999.99 || mQuantity < 0) {
                            mFlag = true;
                            mUserCSVInvalidValue = "Please enter quantity between 0 and 9999.99 for item " + colums[0];
                            return;
                        }
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.quantity_invalid) + colums[0];
                        break;
                    }
                }

                // Barcode
                if (colums[13] != null && colums[13].trim().length() > 0) {
                    mbarCode = colums[13];
                }

                // Brand Code
                if (colums[14] != null && colums[14].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[14], "Int");
                    if (mCheckCSVValueType == CHECK_INTEGER_VALUE) {
                        if (mBrandCodeList != null && mBrandCodeList.size() > 0) {
                            if (mBrandCodeList.contains(Integer.parseInt(colums[14]))) {
                                mBrandCode = Integer.parseInt(colums[14]);
                            } else {
                                mFlag = true;
                                mUserCSVInvalidValue = "This Brand code " + colums[14] + " is not present in the database for Item Name " + colums[0];
                                break;
                            }
                        } else {
                            mFlag = true;
                            mUserCSVInvalidValue = getResources().getString(R.string.database_brand_null);
                            break;
                        }

                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.brand_code_invalid) + " " + colums[0];
                        break;
                    }
                } else {
                    mBrandCode = -1;
                }

                // Department Code
                if (colums[15] != null && colums[15].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[15], "Int");
                    if (mCheckCSVValueType == CHECK_INTEGER_VALUE) {
                        if (mDepartmentCodeList != null && mDepartmentCodeList.size() > 0) {
                            if (mDepartmentCodeList.contains(Integer.parseInt(colums[15]))) {
                                mDeptCode = Integer.parseInt(colums[15]);
                            } else {
                                mFlag = true;
                                mUserCSVInvalidValue = "This department code " + colums[15] + " is not present in the database for Item Name " + colums[0];
                                break;
                            }
                        } else {
                            mFlag = true;
                            mUserCSVInvalidValue = getResources().getString(R.string.database_department_null);
                            break;
                        }

                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.department_code_invalid) + " " + colums[0];
                        break;
                    }
                } else {
                    mDeptCode = -1;
                    mCategCode = -1;
                }

                // Category Code
                boolean categoryFlag = false;
                if (colums[16] != null && colums[16].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[16], "Int");
                    if (mCheckCSVValueType == CHECK_INTEGER_VALUE) {

                        if (mCategoryCodeList != null && mCategoryCodeList.size() > 0) {

                            if (mDeptCode == 0) {
                                mFlag = true;
                                mUserCSVInvalidValue = "Item Short Name " + mItemShortName + " can not have category code " + colums[16] + " without department";
                                break;
                            }

                            if (mCategoryCodeList.containsKey(Integer.parseInt(colums[16]))) {
                                categoryFlag = true;
                            } else {
                                mFlag = true;
                                mUserCSVInvalidValue = "This category code " + colums[16] + " is not present in the database for Item Name " + colums[1];
                                break;
                            }

                            if (categoryFlag) {

                                if (checkDeptandCategoryTogether(mDeptCode, Integer.parseInt(colums[16])) != 0) {
                                    mCategCode = checkDeptandCategoryTogether(mDeptCode, Integer.parseInt(colums[16]));
                                } else {
                                    mFlag = true;
                                    mUserCSVInvalidValue = "Category code " + colums[16] + " is not linked to department code " + mDeptCode + " for Item Name " + mItemShortName
                                            + "  in category tab of configurations feature";
                                    break;
                                }

                            }
                        } else {
                            mFlag = true;
                            mUserCSVInvalidValue = getResources().getString(R.string.database_category_null);
                            break;
                        }

                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.category_code_invalid) + " " + colums[0];
                        break;
                    }

                } else {
                    mCategCode = -1;
                }

                // Favorite
                if (colums[17] != null && colums[17].length() > 0 && colums[17].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[17], "Int");
                    if (mCheckCSVValueType == CHECK_INTEGER_VALUE) {
                        mFav = Integer.valueOf(colums[17]);
                        if (mFav > 1 || mFav < 0) {
                            mFlag = true;
                            mUserCSVInvalidValue = "Please enter favorite as 0 or 1 for item " + colums[1];
                            return;
                        }
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.favorite_invalid) + " " + colums[0];
                        break;
                    }
                }

                // Active/Inactive
                if (colums[18] != null && colums[18].length() > 0 && colums[18].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[18], "Int");
                    if (mCheckCSVValueType == CHECK_INTEGER_VALUE) {
                        mActive = Integer.valueOf(colums[18]);
                        if (mActive > 1 || mActive < 0) {
                            mFlag = true;
                            mUserCSVInvalidValue = "Please enter active/inactive as 0 or 1 for item " + colums[0];
                            return;
                        }
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.active_invalid) + colums[0];
                        break;
                    }
                }

                // Image URI
                if (colums[19] != null && colums[19].length() > 0 && colums[19].trim().length() > 0) {
                    mCheckCSVValueType = checkCSVTypeValue(colums[19], "String");
                    if (mCheckCSVValueType == CHECK_STRING_VALUE) {
                        mImageUri = colums[18];
                    } else {
                        mFlag = true;
                        mUserCSVInvalidValue = getResources().getString(R.string.image_invalid) + colums[0];
                        break;
                    }
                }

                // Discount Amount
                mDiscountAmount = mMRP - mRetailPrice;

                // Discount Percent
                mDiscountPercent = (mDiscountAmount)*100;

                // CHECK : Same short name and barcode can exist with same UOM but different MRP
                for (Map.Entry<Integer, ItemModel> entry : mHashMapItemCode.entrySet()) {
                    ItemModel entryValue = entry.getValue();
                    if (entryValue.getShortName().equalsIgnoreCase(mItemShortName) && entryValue.getBarCode().equalsIgnoreCase(mbarCode)) {
                        if (entryValue.getUOM().equalsIgnoreCase(mUOM)) {
                            if (entryValue.getMrp() == mMRP) {
                                mFlag = true;
                                mUserCSVInvalidValue = getResources().getString(R.string.same_item_same_mrp) + " " + entryValue.getShortName() + " & " + mItemShortName;
                                break;
                            }
                        } else {
                            mFlag = true;
                            mUserCSVInvalidValue = getResources().getString(R.string.same_item_different_uom) + " " + entryValue.getShortName() + " & " + mItemShortName;
                            break;
                        }
                    }
                }

                // if mrp and whole sale price is ZERO
                if (mMRP == 0)
                    mMRP = mRetailPrice;
                if (mWholeSalePrice == 0)
                    mWholeSalePrice = mRetailPrice;

                // Duplicate short code
                if (mHashMapItemCode.containsKey(mShortCode)) {
                    mCSVHashCheckflag = true;
                    break;
                }

                ItemModel item_add = new ItemModel(mShortCode, mItemShortName, mItemLongName, mbarCode, mUOM, mBrandCode,
                        mDeptCode, mCategCode, mRetailPrice, mMRP, mWholeSalePrice, mQuantity,
                        mHSN, mCGSTRate, mSGSTRate, mIGSTRate, mCESSRate, mFav,
                        mActive, mImageUri, mDiscountPercent, mDiscountAmount);

                if (!mCSVHashCheckflag) {
                    mHashMapItemCode.put(mShortCode, item_add);
                } else {
                    mFlag = true;
                    mUserCSVInvalidValue = "ShortCode " + mShortCode + " is already present in CSV file";
                    mHashMapItemCode.clear();
                    return;
                }
            }

            if (!mCSVHashCheckflag && !mFlag) {
                saveCSVinDatabase();

            }

        } catch (Exception exp) {
            mCSVHashCheckflag = true;
            exp.printStackTrace();
        }
    }
    private void saveCSVinDatabase() {

        ArrayList<ItemModel> dataList = new ArrayList<>(mHashMapItemCode.values());

        if (dataList.size() > 0) {
            int deleted = HomeActivity.dbHandler.clearItemdatabase();
            Logger.d(TAG, " Items deleted before uploading excel :" + deleted);
            for (int i = 0; i < dataList.size(); i++) {
                HomeActivity.dbHandler.insertItem(dataList.get(i));
            }
        }

    }
    private void setAllBrandCode(){
        try {
            Cursor crsrBrand = HomeActivity.dbHandler.getAllBrands();
            while (crsrBrand != null && crsrBrand.moveToNext()) {
                mBrandCodeList.add(crsrBrand.getInt(crsrBrand.getColumnIndex(DatabaseHandler.KEY_BRAND_CODE)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void setAllDepartmentCode() {
        try {
            Cursor crsrDept = HomeActivity.dbHandler.getAllDepartments();
            while (crsrDept != null && crsrDept.moveToNext()) {
                mDepartmentCodeList.add(crsrDept.getInt(crsrDept.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)));
            }

            Cursor crsrCategory = HomeActivity.dbHandler.getAllCategory();
            while (crsrCategory != null && crsrCategory.moveToNext()) {
                mCategoryCodeList.put(crsrCategory.getInt(crsrCategory.getColumnIndex(DatabaseHandler.KEY_CategoryCode)),
                        crsrCategory.getInt(crsrCategory.getColumnIndex(DatabaseHandler.KEY_DepartmentCode)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int checkCSVTypeValue(String value, String dataType) {
        int flag = 2;
        try {
            switch (dataType) {
                case "Int":
                    Integer.parseInt(value);
                    flag = 0;
                    break;
                case "Double":
                    Double.parseDouble(value);
                    flag = 1;
                    break;
                default:
                    flag = 2;
            }
        } catch (Exception nfe) {
            // nfe.printStackTrace();
        } finally {
            return flag;
        }
    }
    private int checkDeptandCategoryTogether(int departmentCSVValue, int categoryCSVValue) {
        int categoryCode = 0;

        for (Map.Entry entry : mCategoryCodeList.entrySet()) {

            if (entry.getKey().equals(categoryCSVValue) && entry.getValue().equals(departmentCSVValue)) {
                categoryCode = categoryCSVValue;
                return categoryCode;
            }
        }
        return categoryCode;
    }
    void mClear()
    {
        tv_filePath.setText("");
        strUploadFilepath="";
        actv_itemsearch.setText("");
    }
    public void displayItemList() {
        try {
            if(loadItemsAsync == null)
            {
                loadItemsAsync = new LoadItemsAsync();
                loadItemsAsync.execute();
            }
//            new LoadItemsAsync().execute();
        } catch (Exception e) {
            Logger.e(TAG, e.getMessage());
        }
    }

    class LoadItemsAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(myContext);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Its loading....");
            progressDialog.setTitle("ProgressDialog bar ");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setCancelable(false);
            progressDialog.show();
            itemCount = 0;
            //t1.start();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor cursor = null;
            switch (rg_displayItemCriteria.getCheckedRadioButtonId()) {
                case R.id.rb_item_master_list_all:
                    cursor = HomeActivity.dbHandler.getAllItems();
                    break;
                case R.id.rb_item_master_list_brand:
                    cursor = HomeActivity.dbHandler.getAllItems_Brandwise();
                    break;
                case R.id.rb_item_master_list_department:
                    cursor = HomeActivity.dbHandler.getAllItems_Departmentwise();
                    break;
                case R.id.rb_item_master_list_category:
                    cursor = HomeActivity.dbHandler.getAllItems_Categorywise();
                    break;
                case R.id.rb_item_master_list_active:
                    cursor = HomeActivity.dbHandler.getAllItems_ActiveItems();
                    break;
                case R.id.rb_item_master_list_inactive:
                    cursor = HomeActivity.dbHandler.getAllItems_InactiveItems();
                    break;

                default:
                    cursor = HomeActivity.dbHandler.getAllItems();

            }


            itemCount = cursor.getCount();
            if (adapter != null)
                adapter = null;
            adapter = new SimpleCursorRecyclerAdapter(R.layout.row_item_list, cursor, from, to, itemClickListener);

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            //handle.sendMessage(handle.obtainMessage());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null)
                progressDialog.dismiss();
            //deptCAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            rv_list.setLayoutManager(new LinearLayoutManager(myContext));
            rv_list.setAdapter(adapter);
            btnItemCount.setText(String.valueOf(itemCount));
            if(loadItemsAsync !=null)
                loadItemsAsync = null;
            //progressBar.clearAnimation();
        }
    }

    void clickEvent() {
        actv_itemsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String data = actv_itemsearch.getText().toString();
                //System.out.println("Richa : "+data);
                String[] parts = data.split("-");
                final String shortCode = parts[0].trim();
                final String itemShortName = parts[1].trim();
                final String itemBarcode = parts[2].trim();
                btItemMasterAdd.setEnabled(false);
                btItemMasterAdd.setTextColor(getResources().getColor(R.color.grey));

                new AsyncTask<Void, Void, Void>() {
                    int itemCount = 0;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progressDialog = new ProgressDialog(myContext);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Its loading....");
                        progressDialog.setTitle("ProgressDialog bar ");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        //t1.start();

                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        Cursor cursor = HomeActivity.dbHandler.getAllItems_details(Integer.parseInt(shortCode), itemShortName, itemBarcode);
                        itemCount = cursor.getCount();
                        if (adapter != null)
                            adapter = null;
                        adapter = new SimpleCursorRecyclerAdapter(R.layout.row_item_list, cursor, from, to, itemClickListener);

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        if (progressDialog != null)
                            progressDialog.dismiss();
                        //deptCAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        rv_list.setLayoutManager(new LinearLayoutManager(myContext));
                        rv_list.setAdapter(adapter);
                        btnItemCount.setText(String.valueOf(itemCount));
                        actv_itemsearch.setText("");
                        //progressBar.clearAnimation();
                    }

                }.execute();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Logger.d(TAG, "OnAttach()");
    }

    @Override
    public void onResume() {
        super.onResume();

        Logger.d(TAG, "OnResume()");
    }

    @Override
    public void onStart() {
        super.onStart();
        Logger.d(TAG, "OnStart()");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.d(TAG, "OnActivityCreated()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Logger.d(TAG, "OnDeattach()");
    }

    public void loadAutocompleteData() {
        new AsyncTask<Void, Void, Void>() {
            ArrayList<String> list_actv = new ArrayList<>();
            ProgressDialog pd;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pd = new ProgressDialog(myContext);
                pd.setMessage("Loading AutoComplete Data");
                pd.setCancelable(false);
                pd.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                list_actv = HomeActivity.dbHandler.getAllItems_for_autocomplete();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                pd.dismiss();
                ArrayAdapter<String> dataAdapter_actv = new ArrayAdapter<String>(myContext, android.R.layout.simple_list_item_1, list_actv);
                dataAdapter_actv.setDropDownViewResource(android.R.layout.simple_list_item_1);
                actv_itemsearch.setAdapter(dataAdapter_actv);

            }

        }.execute();
    }

    SimpleCursorRecyclerAdapter.OnItemClickListener itemClickListener = new SimpleCursorRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            ItemMasterEditItemDialogFragment myFragment =
                    (ItemMasterEditItemDialogFragment)getFragmentManager().findFragmentByTag("Update Item");
            if (myFragment != null && myFragment.isVisible()) {
                return;
            }

            Cursor cursor = (((SimpleCursorRecyclerAdapter) rv_list.getAdapter()).getCursor());
            cursor.moveToPosition(position);


            ItemModel obj = new ItemModel();
            obj.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
            obj.setShortCode(cursor.getInt(cursor.getColumnIndex("ShortCode")));
            obj.setLongName(cursor.getString(cursor.getColumnIndex("ItemLongName")));
            obj.setShortName(cursor.getString(cursor.getColumnIndex("ItemShortName")));
            obj.setBarCode(cursor.getString(cursor.getColumnIndex("ItemBarcode")));
            obj.setUOM(cursor.getString(cursor.getColumnIndex("UOM")));
            obj.setBrandCode(cursor.getInt(cursor.getColumnIndex("BrandCode")));
            obj.setDeptCode(cursor.getInt(cursor.getColumnIndex("DepartmentCode")));
            obj.setCategCode(cursor.getInt(cursor.getColumnIndex("CategoryCode")));
            obj.setRetaiPrice(cursor.getDouble(cursor.getColumnIndex("RetailPrice")));
            obj.setMrp(cursor.getDouble(cursor.getColumnIndex("MRP")));
            obj.setWholeSalePrice(cursor.getDouble(cursor.getColumnIndex("WholeSalePrice")));
            obj.setQuantity(cursor.getDouble(cursor.getColumnIndex("Quantity")));
            obj.setHsn(cursor.getString(cursor.getColumnIndex("HSNCode")));
            obj.setCgstRate(cursor.getDouble(cursor.getColumnIndex("CGSTRate")));
            obj.setSgstRate(cursor.getDouble(cursor.getColumnIndex("SGSTRate")));
            obj.setIgstRate(cursor.getDouble(cursor.getColumnIndex("IGSTRate")));
            obj.setCessRate(cursor.getDouble(cursor.getColumnIndex("cessRate")));
            obj.setImageURL(cursor.getString(cursor.getColumnIndex("ImageUri")));
            obj.setFav(cursor.getInt(cursor.getColumnIndex("isFavourite")));
            obj.setActive(cursor.getInt(cursor.getColumnIndex("isActive")));

            /*ItemMasterEditItemDialogFragment ldf = new ItemMasterEditItemDialogFragment();
            ldf.mInitListener(ItemMastersFragment.this);
            Bundle args = new Bundle();
            args.putSerializable("ItemModelObject", obj);
            ldf.setArguments(args);
            getFragmentManager().beginTransaction().add(0, ldf).commit();*/

            FragmentManager fm = getActivity().getSupportFragmentManager();
            ItemMasterEditItemDialogFragment itemMasterEditItemDialogFragment = new ItemMasterEditItemDialogFragment();
            Bundle args = new Bundle();
            args.putSerializable("ItemModelObject", obj);
            itemMasterEditItemDialogFragment.setArguments(args);
            itemMasterEditItemDialogFragment.mInitListener(ItemMastersFragment.this);
            itemMasterEditItemDialogFragment.show(fm, "Update Item");
        }
    };



    public void generateCSV() {
        File temp = new File(CSV_GENERATE_PATH + FILENAME);

        AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                .setIcon(R.mipmap.ic_company_logo)
                .setTitle("Overwrite Alert")
                .setMessage("There already exists a CSV file, regenerating a " +
                        "CSV file will overwrite the existing one. " +
                        "Are you sure you want to overwrite the file?")
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new GenerateCSV().execute();
                    }
                });
        AlertDialog alert = builder.create();

        if (!temp.exists())
            new GenerateCSV().execute();
        else
            alert.show();
    }

    class GenerateCSV extends AsyncTask<Void, Void, Void> {

        ProgressDialog pd = new ProgressDialog(myContext);
        int progress = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = 0;
            pd.setMessage("Generating sample csv...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            progress = 1;

            try {
                File directory = new File(CSV_GENERATE_PATH);
                if (!directory.exists())
                    directory.mkdirs();

                InputStream isAssetDbFile = myContext.getAssets().open(FILENAME);
                OutputStream osNewDbFile = new FileOutputStream(CSV_GENERATE_PATH + FILENAME);
                byte[] bFileBuffer = new byte[1024];
                int iBytesRead = 0;

                while ((iBytesRead = isAssetDbFile.read(bFileBuffer)) > 0) {
                    osNewDbFile.write(bFileBuffer, 0, iBytesRead);
                }

                osNewDbFile.flush();
                osNewDbFile.close();
                isAssetDbFile.close();
                pd.dismiss();
                publishProgress();

            } catch (FileNotFoundException e) {
                pd.dismiss();
                progress = 3;
                publishProgress();
                e.printStackTrace();
            } catch (IOException e) {
                pd.dismiss();
                progress = 4;
                publishProgress();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            if (progress == 1) {
                Toast.makeText(myContext, "CSV generated successfully! Path:" + CSV_GENERATE_PATH + FILENAME, Toast.LENGTH_LONG).show();
            } else if (progress == 3 || progress == 4) {
                Toast.makeText(myContext, "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress = 2;
            pd.dismiss();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        Logger.d(TAG, "OnDestroy()");
    }

    void applyValidations()
    {
        actv_itemsearch.setFilters(new InputFilter[]{new EMOJI_FILTER()});

    }
    @Override
    public void onItemAddSuccess() {
        displayItemList();
        loadAutocompleteData();
    }

    @Override
    public void filePickerSuccessClickListener(String absolutePath) {
        strUploadFilepath = absolutePath;
        tv_filePath.setText(strUploadFilepath.substring(strUploadFilepath.lastIndexOf("/")+1));
    }

}
