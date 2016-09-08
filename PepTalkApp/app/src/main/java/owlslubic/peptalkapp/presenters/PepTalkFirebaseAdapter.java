package owlslubic.peptalkapp.presenters;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.views.CustomDialog;
import owlslubic.peptalkapp.views.MyFragment;
import owlslubic.peptalkapp.views.PepTalkListActivity;

/**
 * Created by owlslubic on 9/1/16.
 */

public class PepTalkFirebaseAdapter extends FirebaseRecyclerAdapter<PepTalkObject, PepTalkViewHolder> implements ItemTouchHelperAdapter{
    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;
    PepTalkListActivity mActivity;



    public PepTalkFirebaseAdapter(Class<PepTalkObject> modelClass, int modelLayout, Class<PepTalkViewHolder> viewHolderClass, Query ref, Context context){//OnStartDragListener onStartDragListener, Context context){
        super(modelClass, modelLayout,viewHolderClass,ref);
        mRef = ref.getRef();
//        mOnStartDragListener = onStartDragListener;
        mContext = context;
    }

    @Override
    protected void populateViewHolder(final PepTalkViewHolder holder, final PepTalkObject model, int position) {

        holder.mTitle.setText(model.getTitle());
        holder.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //launches edit pep talk dialog
//                CustomDialog.launchEditPeptalkDialog(mContext, model);

                //display fragment LOL THIS DOESNT WORK

//                mActivity = (PepTalkListActivity)mContext;
//                FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
//                ft.add(R.id.framelayout_peptalk_frag_container, new MyFragment());
//
//                holder.mFragTitle.setText(model.getTitle());
//                holder.mFragBody.setText(model.getBody());
//
//                ft.commit();


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

    public interface OnItemClickListener{
        public void onItemClick(String title, String body);
    }


}

