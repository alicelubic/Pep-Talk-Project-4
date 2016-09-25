package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.ChecklistItemObject;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.views.ChecklistActivity;
import owlslubic.peptalkapp.views.CustomDialog;
import owlslubic.peptalkapp.views.MainActivity;
import owlslubic.peptalkapp.views.PepTalkListActivity;
import owlslubic.peptalkapp.views.fragments.EditFrag;
import owlslubic.peptalkapp.views.fragments.NewFrag;

/**
 * Created by owlslubic on 9/2/16.
 */
public class ChecklistFirebaseAdapter extends FirebaseRecyclerAdapter<ChecklistItemObject,ChecklistViewHolder> implements ItemTouchHelperAdapter {
    private static final String TAG = "ChecklistFirebase";
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

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CustomDialog.launchViewChecklist(model, mContext);

                /**this will launch view, not edit, but this is temp*/
                setupEditChecklistFrag(model);
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



    public void setupEditFrag() {

        if (mContext.getClass() == ChecklistActivity.class) {
            mContainerId = R.id.checklist_activity_frag_container;
            ChecklistActivity activity = (ChecklistActivity)mContext;
            mTransaction = activity.getSupportFragmentManager().beginTransaction();
        }
        else if(mContext.getClass() == MainActivity.class){
            Log.d(TAG, "setupEditFrag class: "+mContext.getClass().toString());
            mContainerId = R.id.framelayout_main_frag_container;
            MainActivity activity = (MainActivity)mContext;
            mTransaction = activity.getSupportFragmentManager().beginTransaction();
        }

        EditFrag fragment = new EditFrag();
        mTransaction.add(mContainerId, fragment);
        mTransaction.commit();

    }

    public void setupEditChecklistFrag(ChecklistItemObject model) {

        mTransaction = mFragmentManager.beginTransaction();
        NewFrag fragment = new NewFrag();
        Bundle args = new Bundle();
        args.putString(NewFrag.OBJECT_TYPE, NewFrag.CHECKLIST);
        args.putString(NewFrag.NEW_OR_EDIT, NewFrag.EDIT);
        args.putString(NewFrag.KEY, model.getKey());
        args.putString(NewFrag.TOP_TEXT, model.getText());
        args.putString(NewFrag.BOTTOM_TEXT, model.getNotes());
        fragment.setArguments(args);


        mTransaction.add(R.id.checklist_activity_frag_container, fragment);
        mTransaction.commit();



    }

}
