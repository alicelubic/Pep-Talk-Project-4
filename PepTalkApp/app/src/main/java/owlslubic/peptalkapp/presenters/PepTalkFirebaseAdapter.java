package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.views.CustomDialog;
import owlslubic.peptalkapp.views.MainActivity;
import owlslubic.peptalkapp.views.PepTalkListActivity;
import owlslubic.peptalkapp.views.fragments.EditFrag;

/**
 * Created by owlslubic on 9/1/16.
 */

public class PepTalkFirebaseAdapter extends FirebaseRecyclerAdapter<PepTalkObject, PepTalkViewHolder> implements ItemTouchHelperAdapter {
    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;



    public PepTalkFirebaseAdapter(Class<PepTalkObject> modelClass, int modelLayout, Class<PepTalkViewHolder> viewHolderClass, Query ref, Context context) {//OnStartDragListener onStartDragListener, Context context){
        super(modelClass, modelLayout, viewHolderClass, ref);
        mRef = ref.getRef();
//        mOnStartDragListener = onStartDragListener;
        mContext = context;
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
                    }else if (holder.mEdit.getVisibility() == View.VISIBLE) {
                        holder.mEdit.setVisibility(View.INVISIBLE);
                    }
                }
            });
                holder.mEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        CustomDialog.launchEditPeptalkDialog(mContext, model);
                        //TODO pass model data and launch edit fragment!
                    }
                });



        }

        else {//not in main activity, it's in peptalkactivity
            holder.mTitle.setText(model.getTitle());
            holder.mCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CustomDialog.launchViewPepTalk(model, mContext);
                    //TODO pass model data and launch view fragment!

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




}

