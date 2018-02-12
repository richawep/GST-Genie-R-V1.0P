package pos.wepindia.com.retail.view.UserMasters.UserAdapterAndListener;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by MohanN on 12/19/2017.
 */

public class RecyclerItemListener implements RecyclerView.OnItemTouchListener {

    private static final String TAG = RecyclerTouchListener.class.getName();

    private RecyclerTouchListener listener;
    private GestureDetector gd;
    private int iMode;

    public interface RecyclerTouchListener {
        public void onClickItem(View v, int position, int mode) ;
        public void onLongClickItem(View v, int position, int mode);
    }

    public RecyclerItemListener(Context ctx, final RecyclerView rv,
                                final RecyclerTouchListener listener, int mode) {
        this.listener = listener;
        this.iMode = mode;
        gd = new GestureDetector(ctx,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public void onLongPress(MotionEvent e) {
                        View v = rv.findChildViewUnder(e.getX(), e.getY());
                        listener.onLongClickItem(v, rv.getChildAdapterPosition(v),iMode);
                    }

                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        View v = rv.findChildViewUnder(e.getX(), e.getY());
                        // Notify the even
                        listener.onClickItem(v, rv.getChildAdapterPosition(v),iMode);
                        return true;
                    }
                });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        return ( child != null && gd.onTouchEvent(e));
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
