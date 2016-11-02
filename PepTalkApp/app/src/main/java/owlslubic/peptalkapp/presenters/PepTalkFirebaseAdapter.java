package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
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

public class PepTalkFirebaseAdapter extends FirebaseRecyclerAdapter<PepTalkObject, PepTalkViewHolder> implements View.OnClickListener, ItemTouchHelperAdapter {
    private static final String TAG = "PepTalkFirebaseAdapter";
    private static final String PREFS = "prefs";
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;
    private ViewFrag.FABCoordinatorViewFrag mCallback;
    private PepTalkViewHolder mHolder;
    private PepTalkObject mModel;

    public PepTalkFirebaseAdapter(Class<PepTalkObject> modelClass, int modelLayout,
                                  Class<PepTalkViewHolder> viewHolderClass, Query ref,
                                  Context context, ViewFrag.FABCoordinatorViewFrag callback) {//OnStartDragListener onStartDragListener, Context context){
        super(modelClass, modelLayout, viewHolderClass, ref);
//        mOnStartDragListener = onStartDragListener;
        mContext = context;
        mCallback = callback;


    }

    @Override
    protected void populateViewHolder(final PepTalkViewHolder holder, final PepTalkObject model, int position) {
        mHolder = holder;
        mModel = model;

        if (mContext instanceof MainActivity) {
            //frags
            holder.mFragTitle.setText(model.getTitle());
            holder.mFragTitle.setOnClickListener(this);
            holder.mFragBody.setText(model.getBody());
            holder.mFragBody.setMovementMethod(new ScrollingMovementMethod());
            holder.mFragBody.setOnClickListener(this);
            holder.mFragCard.setOnClickListener(this);/*new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.mEdit.getVisibility() == View.INVISIBLE) {
                        holder.mEdit.setVisibility(View.VISIBLE);
                    } else if (holder.mEdit.getVisibility() == View.VISIBLE) {
                        holder.mEdit.setVisibility(View.INVISIBLE);
                    }

                }
            });*/

            holder.mEdit.setOnClickListener(this);/*new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setupEditFrag((FragmentActivity) mContext, PEPTALK_OBJ,
                            model.getKey(), model.getTitle(), model.getBody());
                    mCallback.hideFabFromViewFrag();

                }
            });
            */
        } else {//not in main activity, it's in peptalkactivity
            holder.mTitle.setText(model.getTitle());
            holder.mCard.setOnClickListener(this);/*new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setupViewFrag((FragmentActivity) mContext, PEPTALK_OBJ, model.getKey(), model.getTitle(), model.getBody());
                    mCallback.hideFabFromViewFrag();
                }
            });
            */

            holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    launchDeletePepTalkDialog(model.getKey(), model.getTitle(), mContext, null, null);
                    return true;
                }
            });

            swipeDismiss(holder.mCard);


            /**swipey shit*/
//        holder.mCard.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(MotionEventCompat.getActionMasked(motionEvent)== MotionEvent.ACTION_DOWN){
//                    mOnStartDragListener.onStartDrag(holder);
//                }
//
//                return false;
//            }
//        });


        }

    }

    private void swipeDismiss(CardView card) {
        SwipeDismissBehavior<CardView> swipeDismissBehavior = new SwipeDismissBehavior<>();
        swipeDismissBehavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_ANY);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) card.getLayoutParams();
        layoutParams.setBehavior(swipeDismissBehavior);
    }


    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position, PepTalkObject peptalk) {
        //        CustomDialog.launchDeletePepTalkDialog(); or equivalent
        //problem is determining which pep talk is getting dismissed
//        CustomDialog.launchDeletePepTalkDialog(peptalk, mContext);


    }

    private void changeIconVisibility(ImageButton edit, ImageButton trash) {
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardview_fragment:
                changeIconVisibility(mHolder.mEdit, mHolder.mTrash);
                break;
            case R.id.textview_frag_body:
                changeIconVisibility(mHolder.mEdit, mHolder.mTrash);
                break;
            case R.id.textview_frag_title:
                changeIconVisibility(mHolder.mEdit, mHolder.mTrash);
                break;
            case R.id.button_frag_edit_peptalk:
                if (mContext instanceof PepTalkListActivity) {
                    mCallback.hideFabFromViewFrag();
                }
                setupEditFrag((FragmentActivity) mContext, PEPTALK_OBJ,
                        mModel.getKey(), mModel.getTitle(), mModel.getBody());
                break;
            case R.id.cardview_peptalk:
                setupViewFrag((FragmentActivity) mContext, PEPTALK_OBJ, mModel.getKey(), mModel.getTitle(), mModel.getBody());
                mCallback.hideFabFromViewFrag();
                break;
        }


//        if ((id == R.id.cardview_fragment) ||
//                (id == R.id.textview_frag_body) ||
//                (id == R.id.textview_frag_title)) {
//            changeIconVisibility(mHolder.mEdit, mHolder.mTrash);
//
//        } else if (id == R.id.button_frag_edit_peptalk) {
//
//            if (mContext instanceof PepTalkListActivity) {
//                mCallback.hideFabFromViewFrag();
//            }
//            setupEditFrag((FragmentActivity) mContext, PEPTALK_OBJ,
//                    mModel.getKey(), mModel.getTitle(), mModel.getBody());
//
//        } else if (id == R.id.cardview_peptalk) {
//            setupViewFrag((FragmentActivity) mContext, PEPTALK_OBJ, mModel.getKey(), mModel.getTitle(), mModel.getBody());
//            mCallback.hideFabFromViewFrag();
//        } else if (id == R.id.imagebutton_frag_view_delete) {
//            String tag = NEW_FRAG_TAG;
//            if (mContext instanceof MainActivity) {
//                tag = RECYCLERVIEW_FRAG_TAG;
//            }
//            launchDeletePepTalkDialog(mModel.getKey(), mModel.getTitle(), mContext, tag, v);
//        }


    }

}


