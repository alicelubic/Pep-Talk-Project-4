package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.ChecklistItemObject;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.views.fragments.NewFrag;
import owlslubic.peptalkapp.views.fragments.ViewFrag;

/**
 * Created by owlslubic on 9/2/16.
 */
public class ChecklistFirebaseAdapter extends FirebaseRecyclerAdapter<ChecklistItemObject,ChecklistViewHolder> implements ItemTouchHelperAdapter {
    private static final String TAG = "ChecklistFirebaseAdapter";
    Context mContext;
    int mContainerId;
    FragmentTransaction mTransaction;
    FragmentManager mFragmentManager;

    public ChecklistFirebaseAdapter(Class<ChecklistItemObject> modelClass, int modelLayout,
                                    Class<ChecklistViewHolder> viewHolderClass, DatabaseReference ref,
                                    Context context, FragmentManager fragmentManager) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = context;
        mFragmentManager = fragmentManager;
    }

    @Override
    protected void populateViewHolder(ChecklistViewHolder holder, final ChecklistItemObject model, int position) {


        holder.mItem.setText(model.getText());
        //TODO expand to show notes

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentMethods.setupViewFrag((FragmentActivity)mContext,NewFrag.CHECKLIST,model.getKey(), model.getText(), model.getNotes());

            }
        });

        holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                DBHelper.launchDeleteChecklistDialog(model, mContext);
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
