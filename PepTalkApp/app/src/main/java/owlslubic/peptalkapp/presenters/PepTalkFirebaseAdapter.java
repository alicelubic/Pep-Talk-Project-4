package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.views.CustomDialog;
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
    int mContainerId;
    FragmentTransaction mTransaction;
    FragmentManager mFragmentManager;

    private static final String TAG = "PepTalkFirebaseAdapter";


    public PepTalkFirebaseAdapter(Class<PepTalkObject> modelClass, int modelLayout,
                                  Class<PepTalkViewHolder> viewHolderClass, Query ref,
                                  Context context, FragmentManager fragmentManager) {//OnStartDragListener onStartDragListener, Context context){
        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = ref.getRef();
//        mOnStartDragListener = onStartDragListener;
        mContext = context;
        mFragmentManager = fragmentManager;
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
                    setupEditPeptalkFrag(model);
                }
            });

        } else {//not in main activity, it's in peptalkactivity
            holder.mTitle.setText(model.getTitle());
            holder.mCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setupViewPeptalkFrag(model);
                }
            });


            holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //launches delete peptalk with are you sure? dialog
                    CustomDialog.launchDeletePepTalkDialog(model, mContext);
                    return true;
                }
            });


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

    /** PLEASE FIND A NICE WAY TO DO THIS NEATLY WITHOUT SO MUCH REPETITION */

    public void setupViewPeptalkFrag(PepTalkObject model) {
        mTransaction = mFragmentManager.beginTransaction();
        ViewFrag fragment = new ViewFrag();
        Bundle args = new Bundle();
        args.putString(NewFrag.OBJECT_TYPE, NewFrag.PEPTALKS);
        args.putString(NewFrag.NEW_OR_EDIT, ViewFrag.VIEW);
        args.putString(NewFrag.KEY, model.getKey());
        args.putString(NewFrag.TOP_TEXT, model.getTitle());
        args.putString(NewFrag.BOTTOM_TEXT, model.getBody());
        fragment.setArguments(args);

        if(mContext instanceof MainActivity){//would that be recyclerview frag?
            mContainerId = R.id.framelayout_main_frag_container;
        }else if(mContext instanceof PepTalkListActivity){
            mContainerId = R.id.peptalk_activity_frag_container;
        }
        mTransaction.add(mContainerId, fragment);
        mTransaction.commit();
    }
    public void setupEditPeptalkFrag(PepTalkObject model) {
        mTransaction = mFragmentManager.beginTransaction();
        NewFrag fragment = new NewFrag();
        Bundle args = new Bundle();
        args.putString(NewFrag.OBJECT_TYPE, NewFrag.PEPTALKS);
        args.putString(NewFrag.NEW_OR_EDIT, NewFrag.EDIT);
        args.putString(NewFrag.KEY, model.getKey());
        args.putString(NewFrag.TOP_TEXT, model.getTitle());
        args.putString(NewFrag.BOTTOM_TEXT, model.getBody());
        fragment.setArguments(args);

        if(mContext instanceof MainActivity){//would that be recyclerview frag?
            mContainerId = R.id.framelayout_main_frag_container;
        }else if(mContext instanceof PepTalkListActivity){
            mContainerId = R.id.peptalk_activity_frag_container;
        }
        mTransaction.add(mContainerId, fragment);
        mTransaction.commit();
    }


}

