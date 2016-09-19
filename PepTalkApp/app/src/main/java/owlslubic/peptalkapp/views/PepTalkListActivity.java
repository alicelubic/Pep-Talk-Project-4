package owlslubic.peptalkapp.views;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import owlslubic.peptalkapp.presenters.PepTalkFirebaseAdapter;
import owlslubic.peptalkapp.presenters.PepTalkViewHolder;
import owlslubic.peptalkapp.presenters.SimpleItemTouchHelperCallback;
import owlslubic.peptalkapp.views.fragments.EditFrag;
import owlslubic.peptalkapp.views.fragments.NewFrag;
import owlslubic.peptalkapp.views.fragments.RecyclerViewFrag;

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

//        insertContentOnNewAccountCreated();

        getSupportActionBar().setTitle("Your Pep Talks");
//        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //fab launches new pep fragment
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_peptalk_list);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setupNewFrag();
            }
        });


        //recyclerview

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mPeptalkRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(PEPTALKS);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_peptalk_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
//        recyclerView.setLayoutManager(manager);



        mFirebaseAdapter = new PepTalkFirebaseAdapter(PepTalkObject.class,
                R.layout.card_peptalk, PepTalkViewHolder.class, mPeptalkRef,
                this);//, this); took out the onstartdrag listener
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

    /** find out if you can set the view texts like this right in the method...
     * and if so, copy this over to the ChecklistActivity*/

    public void setupNewFrag(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        NewFrag fragment = new NewFrag();
        transaction.add(R.id.framelayout_main_frag_container, fragment);

        fragment.mTitle.setText("Give your new pep talk a title");
        fragment.mBody.setText("Everything is going to be okay because...");
        transaction.commit();
    }

    public void setupEditFrag(PepTalkObject model) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        EditFrag fragment = new EditFrag();
        transaction.add(R.id.framelayout_main_frag_container, fragment);

        fragment.mTitle.setText(model.getTitle());
        fragment.mBody.setText(model.getTitle());
        transaction.commit();
    }









}
