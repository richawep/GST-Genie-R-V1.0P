package pos.wepindia.com.retail.view.UserMasters.UserAdapterAndListener;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.List;

import pos.wepindia.com.retail.Constants;
import pos.wepindia.com.retail.R;
import pos.wepindia.com.wepbase.model.pojos.AddRoleBean;


/**
 * Created by MohanN on 12/18/2017.
 */

public class AddRoleListAdapter extends RecyclerView.Adapter<AddRoleListViewHolder> {
    private static final String TAG = AddRoleListAdapter.class.getName();
    private List<AddRoleBean> list;
    private int iMode;
    private AccessPermissionRoleListener accessPermissionRoleListener;

    public AddRoleListAdapter(AccessPermissionRoleListener accessPermissionRoleListener, List<AddRoleBean> list, int mode) {
        this.accessPermissionRoleListener = accessPermissionRoleListener;
        this.list = list;
        this.iMode = mode;
    }

    @Override
    public AddRoleListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_addrole,viewGroup,false);
        return new AddRoleListViewHolder(view,iMode);
    }

    @Override
    public void onBindViewHolder(AddRoleListViewHolder holder, final int position) {
        AddRoleBean myObject = list.get(position);
        holder.bind(myObject);
        switch (iMode){
            case Constants.ACCESS_PERMISSION_FOR_ROLE:
                holder.cbRowItem.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        list.get(position).setStatus(cb.isChecked());
                        cb.setChecked(list.get(position).isStatus());
                        if(list.get(position).isStatus()){
                            accessPermissionRoleListener.onItemSelected(position);
                        } else {
                            accessPermissionRoleListener.onItemUnSelected(position);
                        }
                        /*Toast.makeText(
                                v.getContext(),
                                "Clicked on Checkbox: " + cb.getText() + " is "
                                        + cb.isChecked(), Toast.LENGTH_LONG).show();*/
                    }
                });
                break;
            default:
                break;
        }
    }

    public void notify(List<AddRoleBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
