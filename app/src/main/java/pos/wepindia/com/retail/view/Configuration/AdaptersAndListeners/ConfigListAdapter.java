package pos.wepindia.com.retail.view.Configuration.AdaptersAndListeners;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.wepbase.model.pojos.ConfigBean;

/**
 * Created by MohanN on 12/26/2017.
 */

public class ConfigListAdapter extends RecyclerView.Adapter<ConfigListViewHolder> {

    private static final String TAG = ConfigListAdapter.class.getName();
    private List<ConfigBean> configBeanList;
    private Context mContext;
    private RowItemSelectListener rowItemSelectListener;

    public ConfigListAdapter(Context context, RowItemSelectListener rowItemSelectListener, List<ConfigBean> list) {
        this.mContext = context;
        this.rowItemSelectListener = rowItemSelectListener;
        this.configBeanList = list;
    }


    @Override
    public ConfigListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.config_cell_row,viewGroup,false);
        return new ConfigListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ConfigListViewHolder holder, final int position) {
        ConfigBean myObject = configBeanList.get(position);
        holder.bind(myObject, position);

        holder.llConfigCellRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               rowItemSelectListener.onRowDataSelect(configBeanList.get(position));
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               rowItemSelectListener.onRowDataDelete(configBeanList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return configBeanList.size();
    }
}
