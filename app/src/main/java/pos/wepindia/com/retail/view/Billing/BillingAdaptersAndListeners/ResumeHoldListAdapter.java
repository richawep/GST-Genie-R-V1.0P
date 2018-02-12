package pos.wepindia.com.retail.view.Billing.BillingAdaptersAndListeners;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pos.wepindia.com.retail.R;
import pos.wepindia.com.wepbase.model.pojos.HoldResumeBillBean;

/**
 * Created by MohanN on 1/18/2018.
 */

public class ResumeHoldListAdapter extends RecyclerView.Adapter<ResumeHoldListViewHolder> {

    private static final String TAG = ResumeHoldListAdapter.class.getName();
    private List<HoldResumeBillBean> holdResumeBillBeanList;
    private OnHoldResumeBillListener onHoldResumeBillListener;

    public ResumeHoldListAdapter(OnHoldResumeBillListener onHoldResumeBillListener, List<HoldResumeBillBean> list) {
        this.onHoldResumeBillListener = onHoldResumeBillListener;
        this.holdResumeBillBeanList = list;
    }

    @Override
    public ResumeHoldListViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.resume_hold_list_dialog_row,viewGroup,false);
        return new ResumeHoldListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResumeHoldListViewHolder holder, final int position) {
        HoldResumeBillBean myObject = holdResumeBillBeanList.get(position);
        holder.bind(myObject);

        holder.btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHoldResumeBillListener.onClickBillResume(holdResumeBillBeanList.get(position).getStrInvoiceNo());
            }
        });

        holder.ivRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onHoldResumeBillListener.onClickBillRemove(holdResumeBillBeanList.get(position).getStrInvoiceNo());
               holdResumeBillBeanList.remove(position);
               if(holdResumeBillBeanList.size() > 0) {
                   for (int i = 0; i < holdResumeBillBeanList.size();i++){
                       holdResumeBillBeanList.get(i).setiSlNo(i+1);
                   }
               }
               notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return holdResumeBillBeanList.size();
    }

}