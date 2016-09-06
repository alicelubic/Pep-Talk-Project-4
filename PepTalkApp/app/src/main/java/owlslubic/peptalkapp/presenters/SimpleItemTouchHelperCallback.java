package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.views.CustomDialog;

/**
 * Created by owlslubic on 9/1/16.
 *
 *
 * ADDING CONTEXT AND OBJECT TO CONSTRUCTOR TO ATTEMPT DELETING WITH SWIPE
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    private final ItemTouchHelperAdapter mAdapter;
    private Context mContext;
    private PepTalkObject mPepTalk;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter){//}, PepTalkObject peptalk), Context context, Object o) {
        mAdapter = adapter;
//        mContext = context;
//        mO = o;
//        mPepTalk = peptalk;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        mAdapter.onItemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

//
//        if(direction==ItemTouchHelper.LEFT) {
//
//            CustomDialog.launchDeletePepTalkDialog(viewHolder.getAdapterPosition(), mContext);
//        }
//        if(direction==ItemTouchHelper.RIGHT){
//            CustomDialog.launchEditPeptalkDialog(mContext,viewHolder.getAdapterPosition());
//        }
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition(), mPepTalk);
    }
}
