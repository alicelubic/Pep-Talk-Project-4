package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import owlslubic.peptalkapp.models.ChecklistItemObject;
import owlslubic.peptalkapp.views.fragments.ViewFrag;

import static owlslubic.peptalkapp.presenters.FirebaseHelper.*;

/**
 * Created by owlslubic on 9/2/16.
 */
public class ChecklistFirebaseAdapter extends FirebaseRecyclerAdapter<ChecklistItemObject, ChecklistViewHolder> {
    private static final String TAG = "ChecklistFirebaseAdapt";
    private Context mContext;
    private ViewFrag.FABCoordinatorViewFrag mCallback;

    public ChecklistFirebaseAdapter(Class<ChecklistItemObject> modelClass, int modelLayout,
                                    Class<ChecklistViewHolder> viewHolderClass, DatabaseReference ref,
                                    Context context, ViewFrag.FABCoordinatorViewFrag callback) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = context;
        mCallback = callback;

    }

    @Override
    protected void populateViewHolder(final ChecklistViewHolder holder,
                                      final ChecklistItemObject model, int position) {


        holder.mItem.setText(model.getText());
        //TODO expand to show notes

        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentMethods.setupViewFrag(
                        (FragmentActivity) mContext,
                        FragmentMethods.CHECKLIST_OBJ,
                        model.getKey(),
                        model.getText(),
                        model.getNotes());
                mCallback.hideFabFromViewFrag();

            }
        });

        holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                launchDeleteChecklistDialog(model, mContext);
                return true;
            }
        });

        //TODO check marks still don't really persist...
        holder.mCheckBox.setOnCheckedChangeListener(null);
//        holder.mCheckBox.setChecked(model.isChecked());
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                model.setIsChecked(isChecked);
                updateIsChecked(model.getKey(), isChecked);
                Log.i(TAG, "onCheckedChanged: isChecked " + isChecked);
            }
        });
        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onCHECKClick: model.isChecked() : " + model.isChecked());
            }
        });
    }


}
