package pos.wepindia.com.retail.view.InwardMasters.InwardAdaptersAndListeners;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.wepbase.model.pojos.SupplierItemLinkageBean;


/**
 * Created by RichaA on 6/7/2017.
 */

public class SupplierItemLinkageAdapter extends BaseAdapter {

    List<SupplierItemLinkageBean> SupplierItemList;
    Context myContext;

    public SupplierItemLinkageAdapter(Context context, List<SupplierItemLinkageBean> supplierItemList) {
        this.myContext = context;
        SupplierItemList = supplierItemList;
    }

    @Override
    public int getCount() {
        return SupplierItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return SupplierItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void notifyDataSetChanged(List<SupplierItemLinkageBean> list) {
        this.SupplierItemList = list;
        notifyDataSetChanged();
    }

    public void notifyNewDataAdded(List<SupplierItemLinkageBean> list) {
        this.SupplierItemList = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView tv_Sn;
        TextView tv_supplierName;
        TextView tv_itemName;
        TextView tv_averageRate;
        TextView tv_UOM;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SupplierItemLinkageAdapter.ViewHolder viewHolder;
        int count =1;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_supplieritemlinkage, null);
            viewHolder = new SupplierItemLinkageAdapter.ViewHolder();
            viewHolder.tv_Sn = (TextView) convertView.findViewById(R.id.tv_Sn);
            viewHolder.tv_supplierName = (TextView) convertView.findViewById(R.id.tv_supplierName);
            viewHolder.tv_itemName = (TextView) convertView.findViewById(R.id.tv_itemName);
            viewHolder.tv_averageRate = (TextView) convertView.findViewById(R.id.tv_averageRate);
            viewHolder.tv_UOM = (TextView) convertView.findViewById(R.id.tv_UOM);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (SupplierItemLinkageAdapter.ViewHolder) convertView.getTag();
        }
        SupplierItemLinkageBean data = SupplierItemList.get(position);
        viewHolder.tv_Sn.setText(String.valueOf(position+1));
        viewHolder.tv_supplierName.setText(data.getStrSupplierName());
        viewHolder.tv_itemName.setText(data.getStrItemName());
        viewHolder.tv_averageRate.setText(String.format("%.2f",data.getDblRate()));
        viewHolder.tv_UOM.setText(data.getStrUOM());

        return convertView;
    }

    public void notify(List<SupplierItemLinkageBean> supplierItemList){
        this.SupplierItemList = supplierItemList;
        notifyDataSetChanged();
    }
}
