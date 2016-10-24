package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.views.MainActivity;
import owlslubic.peptalkapp.views.PepTalkListActivity;
import owlslubic.peptalkapp.views.fragments.NewFrag;
import owlslubic.peptalkapp.views.fragments.ViewFrag;

/**
 * Created by owlslubic on 9/1/16.
 */

public class PepTalkFirebaseAdapter extends FirebaseRecyclerAdapter<PepTalkObject, PepTalkViewHolder> implements ItemTouchHelperAdapter {
    private static final String PREFS = "prefs";
    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;
    FragmentManager mFragmentManager;
    ViewFrag.FABCoordinator mCallback;

    private static final String TAG = "PepTalkFirebaseAdapter";


    public PepTalkFirebaseAdapter(Class<PepTalkObject> modelClass, int modelLayout,
                                  Class<PepTalkViewHolder> viewHolderClass, Query ref,
                                  Context context, FragmentManager fragmentManager, ViewFrag.FABCoordinator callback) {//OnStartDragListener onStartDragListener, Context context){
        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = ref.getRef();
//        mOnStartDragListener = onStartDragListener;
        mContext = context;
        mFragmentManager = fragmentManager;
        mCallback = callback;


    }

    @Override
    protected void populateViewHolder(final PepTalkViewHolder holder, final PepTalkObject model, int position) {

        if (mContext instanceof MainActivity) {
            //frags
            holder.mFragTitle.setText(model.getTitle());
            holder.mFragBody.setText(model.getBody());
            holder.mFragBody.setMovementMethod(new ScrollingMovementMethod());
            holder.mFragCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.mEdit.getVisibility() == View.INVISIBLE) {
                        holder.mEdit.setVisibility(View.VISIBLE);
                    } else if (holder.mEdit.getVisibility() == View.VISIBLE) {
                        holder.mEdit.setVisibility(View.INVISIBLE);
                    }
                }
            });
            holder.mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentMethods.setupEditFrag((FragmentActivity) mContext, NewFrag.PEPTALKS,
                            model.getKey(), model.getTitle(), model.getBody());
                    mCallback.hideFabWhenFragOpens();

                }
            });

        } else {//not in main activity, it's in peptalkactivity
            holder.mTitle.setText(model.getTitle());
            holder.mCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentMethods.setupViewFrag((FragmentActivity)mContext,NewFrag.PEPTALKS,model.getKey(), model.getTitle(), model.getBody());
                    mCallback.hideFabWhenFragOpens();
                }
            });


            holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    DBHelper.launchDeletePepTalkDialog(model, mContext);
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

    private void swipeDismiss(CardView card){
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


}

