package pos.wepindia.com.retail.view.UserMasters.UserAdapterAndListener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.wepbase.model.pojos.User;

/**
 * Created by MohanN on 1/2/2018.
 */

public class UserManagementAdapter extends RecyclerView.Adapter<UserManagementViewHolder> {

    private static final String TAG = UserManagementAdapter.class.getName();
    private List<User> userList;
    private Context mContext;
    private OnUserManagementListener userManagementListener;

    public UserManagementAdapter(Context context, OnUserManagementListener userManagementListener, List<User> list) {
        this.mContext = context;
        this.userManagementListener = userManagementListener;
        this.userList = list;
    }


    @Override
    public UserManagementViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_user_display,viewGroup,false);
        return new UserManagementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserManagementViewHolder holder, final int position) {
        User myObject = userList.get(position);
        holder.bind(myObject,position);

        holder.ll_user_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userManagementListener.onRowDataSelect(userList.get(position));
            }
        });

    }

    public void notifyNewDataAdded(List<User> list) {
        this.userList = list;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }
}