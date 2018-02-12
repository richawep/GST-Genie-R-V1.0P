
package pos.wepindia.com.retail.view.ItemMasters.ItemAdaptersAndListeners;


import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pos.wepindia.com.wepbase.model.database.DatabaseHandler;


public class SimpleCursorRecyclerAdapter extends CursorRecyclerAdapter<SimpleCursorRecyclerAdapter.SimpleViewHolder> {

    private int mLayout;
    private int[] mFrom;
    private int[] mTo;
    private String[] mOriginalFrom;

    private OnItemClickListener listener;

    public SimpleCursorRecyclerAdapter (int layout, Cursor c, String[] from, int[] to, OnItemClickListener listener) {
        super(c);
        mLayout = layout;
        mTo = to;
        mOriginalFrom = from;
        findColumns(c, from);
        this.listener = listener;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(mLayout, parent, false);
        final SimpleViewHolder mViewHolder = new SimpleViewHolder(mView, mTo);
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, mViewHolder.getPosition());
            }
        });
        return  mViewHolder;
    }

    @Override
    public void onBindViewHolder (SimpleViewHolder holder, Cursor cursor) {
        final int count = mTo.length;
        final int[] from = mFrom;

        for (int i = 0; i < count; i++) {
            if(mOriginalFrom[i].equalsIgnoreCase(DatabaseHandler.KEY_Quantity) || mOriginalFrom[i].equalsIgnoreCase(DatabaseHandler.KEY_RetailPrice))
            {
                holder.views[i].setText(String.format("%.2f",cursor.getDouble(from[i])));
            }else{
                holder.views[i].setText(cursor.getString(from[i]));
            }

        }

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
        public TextView[] views;

        public SimpleViewHolder (View itemView, int[] to)
        {
            super(itemView);
            views = new TextView[to.length];
            for(int i = 0 ; i < to.length ; i++) {
                views[i] = (TextView) itemView.findViewById(to[i]);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

}


