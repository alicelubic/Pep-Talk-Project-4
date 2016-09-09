package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.transition.TransitionManager;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import owlslubic.peptalkapp.models.ChecklistItemObject;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.views.ChecklistActivity;
import owlslubic.peptalkapp.views.CustomDialog;

/**
 * Created by owlslubic on 9/2/16.
 */
public class ChecklistFirebaseAdapter extends FirebaseRecyclerAdapter<ChecklistItemObject,ChecklistViewHolder> implements ItemTouchHelperAdapter {
    Context mContext;

    public ChecklistFirebaseAdapter(Class<ChecklistItemObject> modelClass, int modelLayout, Class<ChecklistViewHolder> viewHolderClass, DatabaseReference ref, Context mContext) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.mContext = mContext;
    }

    @Override
    protected void populateViewHolder(ChecklistViewHolder holder, final ChecklistItemObject model, int position) {


        holder.mItem.setText(model.getText());

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.launchViewChecklist(model, mContext);
            }
        });

        holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                CustomDialog.launchDeleteChecklistDialog(model,mContext);
                return true;
            }
        });
    }








    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position, PepTalkObject peptalk) {

    }

}
