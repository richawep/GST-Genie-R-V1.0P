
package pos.wepindia.com.retail.view.PriceAndStock.AdaptersAndListeners;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.retail.view.ItemMasters.ItemAdaptersAndListeners.CursorRecyclerAdapter;
import pos.wepindia.com.wepbase.model.database.DatabaseHandler;
import pos.wepindia.com.wepbase.model.pojos.ItemMasterBean;


public class SimpleCursorRecyclerAdapter extends CursorRecyclerAdapter<SimpleCursorRecyclerAdapter.SimpleViewHolder> {

    private int mLayout;
    private int[] mFrom;
    private int[] mTo;
    private String[] mOriginalFrom;
    View mView;
    OnItemSelectListener onItemSelectListener;

    public SimpleCursorRecyclerAdapter(int layout, Cursor c, String[] from, int[] to, OnItemSelectListener onItemSelectListener) {
        super(c);
        mLayout = layout;
        mTo = to;
        mOriginalFrom = from;
        findColumns(c, from);
        this.onItemSelectListener = onItemSelectListener;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
        final SimpleViewHolder mViewHolder = new SimpleViewHolder(mView, mTo);

        return  mViewHolder;
    }

    @Override
    public void onBindViewHolder (final SimpleViewHolder holder, final Cursor cursor) {
        final int count = mTo.length;
        final int[] from = mFrom;

        createThreadObject(holder,cursor);
       /* for (int i = 0; i < count; i++) {
            holder.views[i].setText(cursor.getString(from[i]));
        }*/

        holder.tvItemName.setText(holder.itemMasterBean.getStrLongName());
        holder.tvQty.setText(""+holder.itemMasterBean.getDblQty());
        holder.tvMRP.setText(""+holder.itemMasterBean.getDblMRP());
        holder.tvRetailPrice.setText(""+holder.itemMasterBean.getDblRetailPrice());
        holder.tvWholeSalePrice.setText(""+holder.itemMasterBean.getDblWholeSalePrice());

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemSelectListener.onItemClick(holder.itemMasterBean);
            }
        });

    }

    /*@Override
    public void onBindViewHolderCursor(SimpleViewHolder holder, Cursor cursor) {
        final int count = mTo.length;
        final int[] from = mFrom;

        for (int i = 0; i < count; i++) {
            holder.views[i].setText(cursor.getString(from[i]));
        }
    }*/

    /**
     * Create a map from an array of strings to an array of column-id integers in cursor c.
     * If c is null, the array will be discarded.
     *
     * @param c the cursor to find the columns from
     * @param from the Strings naming the columns of interest
     */
    private void findColumns(Cursor c, String[] from) {
        if (c != null) {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count) {
                mFrom = new int[count];
            }
            for (i = 0; i < count; i++) {
                mFrom[i] = c.getColumnIndexOrThrow(from[i]);
            }
        } else {
            mFrom = null;
        }
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        findColumns(c, mOriginalFrom);
        return super.swapCursor(c);
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder
    {
        // public TextView[] views;
        @BindView(R.id.tv_item_master_list_title_name)
        public TextView tvItemName;
        @BindView(R.id.tv_item_master_list_title_qty)
        public TextView tvQty;
        @BindView(R.id.tv_item_master_list_title_uom)
        public TextView tvMRP;
        @BindView(R.id.tv_item_master_list_title_retail)
        public TextView tvRetailPrice;
        @BindView(R.id.tv_item_master_list_title_disc)
        public TextView tvWholeSalePrice;

        @BindView(R.id.ll_item_master_list)
        LinearLayout llItemMasterList;

        public ItemMasterBean itemMasterBean;

        public SimpleViewHolder (View itemView, int[] to)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
           /* views = new TextView[to.length];
            for(int i = 0 ; i < to.length ; i++) {
                views[i] = (TextView) itemView.findViewById(to[i]);
            }*/
            itemMasterBean = new ItemMasterBean();
        }
    }

    private void createThreadObject(SimpleViewHolder simpleViewHolder, Cursor cursor){
        simpleViewHolder.itemMasterBean.setStrLongName(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemShortName)));
//        simpleViewHolder.itemMasterBean.setStrBarcode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_ItemBarcode)));
        simpleViewHolder.itemMasterBean.setStrUOM(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_UOM)));
        simpleViewHolder.itemMasterBean.setStrHSNCode(cursor.getString(cursor.getColumnIndex(DatabaseHandler.KEY_HSNCode)));
        simpleViewHolder.itemMasterBean.set_id(cursor.getInt(cursor.getColumnIndex(DatabaseHandler.KEY_id)));
        simpleViewHolder.itemMasterBean.setDblMRP(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_MRP)));
        simpleViewHolder.itemMasterBean.setDblRetailPrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_RetailPrice)));
        simpleViewHolder.itemMasterBean.setDblWholeSalePrice(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_WholeSalePrice)));
        simpleViewHolder.itemMasterBean.setDblQty(cursor.getDouble(cursor.getColumnIndex(DatabaseHandler.KEY_Quantity)));
    }

}


