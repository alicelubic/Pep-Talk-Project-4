package owlslubic.peptalkapp.views;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.presenters.OnStartDragListener;
import owlslubic.peptalkapp.presenters.PepTalkFirebaseAdapter;
import owlslubic.peptalkapp.presenters.PepTalkViewHolder;
import owlslubic.peptalkapp.presenters.SimpleItemTouchHelperCallback;

public class PepTalkListActivity extends AppCompatActivity {// implements OnStartDragListener {

    private static final String TAG = "PepTalkListActivity";
    private static final String USERS = "users";
    private static final String PEPTALKS = "peptalks";
    private DatabaseReference mDbRef;
    private PepTalkFirebaseAdapter mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private DatabaseReference mPeptalkRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pep_talk_list);

//        getSupportActionBar().setTitle("Your Pep Talks");
        getSupportActionBar().setDisplayShowTitleEnabled(false);


//TODO inflate menu that has back home button, and maybe some sort of info overflow menu.. do i need it?

        //fab launches dialog
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_peptalk_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog.launchNewPeptalkDialog(PepTalkListActivity.this);
            }
        });


        //recyclerview

        mPeptalkRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(PEPTALKS);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_peptalk_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        mFirebaseAdapter = new PepTalkFirebaseAdapter(PepTalkObject.class,
                R.layout.card_peptalk, PepTalkViewHolder.class, mPeptalkRef,
                this);//, this); took out the onstartdrag listener
        recyclerView.setAdapter(mFirebaseAdapter);



        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);//, this, mFirebaseAdapter.get);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

        //TODO can i use the same adapter for both?
//        FirebaseRecyclerAdapter<PepTalkObject, PepTalkViewHolder> adapter =
//                new FirebaseRecyclerAdapter<PepTalkObject, PepTalkViewHolder>
//                        (PepTalkObject.class, R.layout.card_peptalk, PepTalkViewHolder.class, dbRef.child("PepTalks")) {
//                    @Override
//                    protected void populateViewHolder(PepTalkViewHolder holder, final PepTalkObject model, int position) {
//                        Log.i(TAG, "populateViewHolder: "+ model.getTitle());
//                        holder.mTitle.setText(model.getTitle());
//                        holder.mCard.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                //launches edit pep talk dialog
//                                CustomDialog.launchEditPeptalkDialog(PepTalkListActivity.this, model);
//                            }
//                        });
//                        holder.mCard.setOnLongClickListener(new View.OnLongClickListener() {
//                            @Override
//                            public boolean onLongClick(View view) {
//                                //launches delete peptalk with are you sure? dialog
//                                CustomDialog.launchDeletePepTalkDialog(model, PepTalkListActivity.this);
//                                return true;
//                            }
//                        });
//                    }
//                };



    }

/*    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);

    }
*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }

/*
    //FOR SWIPE TO DISMISS
    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {


            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable background;
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(PepTalkListActivity.this, R.drawable.ic_menu_camera);//to be replaced with the trashcan icon
                xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) PepTalkListActivity.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            //not important ebcause we're not doing drag and drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int position = viewHolder.getAdapterPosition();

            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };
    }
*/

}
