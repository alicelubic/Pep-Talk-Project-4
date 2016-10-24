package owlslubic.peptalkapp.views;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.presenters.FragmentMethods;
import owlslubic.peptalkapp.presenters.PepTalkFirebaseAdapter;
import owlslubic.peptalkapp.presenters.PepTalkViewHolder;
import owlslubic.peptalkapp.presenters.SimpleItemTouchHelperCallback;
import owlslubic.peptalkapp.views.fragments.NewFrag;
import owlslubic.peptalkapp.views.fragments.ViewFrag;

public class PepTalkListActivity extends AppCompatActivity implements ViewFrag.FABCoordinator {// implements OnStartDragListener {

    private static final String TAG = "PepTalkListActivity";
    private static final String USERS = "users";
    private static final String PEPTALKS = "peptalks";
    private DatabaseReference mDbRef;
    private PepTalkFirebaseAdapter mFirebaseAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private DatabaseReference mPeptalkRef;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFab;
    ViewFrag.FABCoordinator mCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pep_talk_list);

        getSupportActionBar().setTitle("Your Pep Talks");
        mCallback = this;

        //fab launches new pep fragment
        mFab = (FloatingActionButton) findViewById(R.id.fab_peptalk_list);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentMethods.setupNewFrag(NewFrag.PEPTALKS, PepTalkListActivity.this);
            }
        });
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_peptalklist);

        //recyclerview
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mPeptalkRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(PEPTALKS);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_peptalk_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);
        //create adapter, add progress bar, set adapter
        mFirebaseAdapter = new PepTalkFirebaseAdapter(PepTalkObject.class,
                R.layout.card_peptalk, PepTalkViewHolder.class, mPeptalkRef,
                this, getSupportFragmentManager(), mCallback);//, this); took out the onstartdrag listener

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                //once loaded, hide the progress bar
                mProgressBar.setVisibility(View.GONE);
                mFab.setVisibility(View.VISIBLE);
            }
        });
        recyclerView.setAdapter(mFirebaseAdapter);


        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);//, this, mFirebaseAdapter.get);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);


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


    @Override
    public void hideFabWhenFragOpens() {
        mFab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void putFabBack(){
        mFab.setVisibility(View.VISIBLE);
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
