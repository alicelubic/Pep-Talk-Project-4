package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.presenters.interfaces_behaviors.ItemTouchHelperAdapter;
import owlslubic.peptalkapp.presenters.interfaces_behaviors.OnStartDragListener;
import owlslubic.peptalkapp.views.MainActivity;
import owlslubic.peptalkapp.views.PepTalkListActivity;
import owlslubic.peptalkapp.views.fragments.ViewFrag;

import static owlslubic.peptalkapp.presenters.FirebaseHelper.*;
import static owlslubic.peptalkapp.presenters.FragmentMethods.*;

/**
 * Created by owlslubic on 9/1/16.
 */

public class PepTalkFirebaseAdapter extends FirebaseRecyclerAdapter<PepTalkObject, PepTalkViewHolder> implements View.OnClickListener {
    private static final String TAG = "PepFireAdapter";
    private static final String PREFS = "prefs";
    private Context mContext;
    private ViewFrag.FABCoordinatorViewFrag mCallback;
    private PepTalkViewHolder mHolder;

    public PepTalkFirebaseAdapter(Class<PepTalkObject> modelClass, int modelLayout,
                                  Class<PepTalkViewHolder> viewHolderClass, Query ref,
                                  Context context, ViewFrag.FABCoordinatorViewFrag callback) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        mContext = context;
        mCallback = callback;
    }

    @Override
    protected void populateViewHolder(final PepTalkViewHolder holder, final PepTalkObject model,
                                      int position) {
        mHolder = holder;
        if (mContext instanceof MainActivity) {
            //frags
            holder.mFragTitle.setText(model.getTitle());
            holder.mFragTitle.setOnClickListener(this);
            holder.mFragBody.setText(model.getBody());
            holder.mFragBody.setMovementMethod(new ScrollingMovementMethod());
            holder.mFragBody.setOnClickListener(this);
            holder.mFragCard.setOnClickListener(this);
            holder.mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setupEditFrag((FragmentActivity) mContext, PEPTALK_OBJ,
                            model.getKey(), model.getTitle(), model.getBody());
                }
            });

        } else {//not in main activity, it's in peptalkactivity
            holder.mTitle.setText(model.getTitle());
            holder.mCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setupViewFrag((FragmentActivity) mContext, PEPTALK_OBJ,
                            model.getKey(), model.getTitle(), model.getBody());

                    mCallback.hideFabFromViewFrag();
                }
            });

            holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    launchDeletePepTalkDialog(model.getKey(), model.getTitle(), mContext, null, null);
                    return true;
                }
            });

            swipeDismiss(holder.mCard);
        }

    }

    private void changeIconVisibility(ImageButton edit) {
        if (edit != null) {
            if (edit.getVisibility() == View.INVISIBLE) {
                edit.setVisibility(View.VISIBLE);
            } else {
                edit.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if ((id == R.id.cardview_fragment) ||
                (id == R.id.textview_frag_body) ||
                (id == R.id.textview_frag_title)) {
            changeIconVisibility(mHolder.mEdit);
        }

    }

    private void swipeDismiss(CardView card) {
        SwipeDismissBehavior<CardView> swipeDismissBehavior = new SwipeDismissBehavior<>();
        swipeDismissBehavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) card.getLayoutParams();
        layoutParams.setBehavior(swipeDismissBehavior);

    }

    //this is not where trash is but i wanna remember how i was gonnna do it so just commenting out not deleting
/*  private void changeIconVisibility(ImageButton edit, ImageButton trash) {
        if (edit != null) {
            if (edit.getVisibility() == View.INVISIBLE) {
                edit.setVisibility(View.VISIBLE);
                if (trash != null) {
                    trash.setVisibility(View.VISIBLE);
                }
            } else {
                edit.setVisibility(View.INVISIBLE);
                if (trash != null) {
                    trash.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

*/


}


