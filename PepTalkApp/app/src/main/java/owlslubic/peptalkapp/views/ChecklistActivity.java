package owlslubic.peptalkapp.views;

import android.net.Uri;
import android.os.RecoverySystem;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.ChecklistItemObject;
import owlslubic.peptalkapp.presenters.ChecklistFirebaseAdapter;
import owlslubic.peptalkapp.presenters.ChecklistViewHolder;
import owlslubic.peptalkapp.presenters.FirebaseHelper;
import owlslubic.peptalkapp.presenters.FragmentMethods;
import owlslubic.peptalkapp.views.fragments.NewFrag;
import owlslubic.peptalkapp.views.fragments.ViewFrag;

import static owlslubic.peptalkapp.presenters.FirebaseHelper.*;
import static owlslubic.peptalkapp.presenters.FragmentMethods.*;


public class ChecklistActivity extends AppCompatActivity implements ViewFrag.FABCoordinatorViewFrag, NewFrag.FABCoordinatorNewFrag{

    private static final String TAG = "ChecklistActivity";
    private ChecklistFirebaseAdapter mFirebaseAdapter;
    private DatabaseReference mChecklistRef;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private ViewFrag.FABCoordinatorViewFrag mCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        initViews();
        setupRecyclerView();

//        getSupportActionBar().setTitle("Your Feel-Better Checklist");
//        if(getSupportActionBar()!=null){
//            getSupportActionBar().setDisplayShowTitleEnabled(false);
//        }
//
//        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_checklistactivity);
//
//        //fab for add new checklist item
//        mFab = (FloatingActionButton) findViewById(R.id.fab_checklist);
//
//        mFab.setVisibility(View.INVISIBLE);
//        mFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setupNewFrag(CHECKLIST_OBJ, ChecklistActivity.this);
//            }
//        });
//
//        //recyclerview setup
//        mChecklistRef = FirebaseDatabase.getInstance().getReference().child(USERS)
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(CHECKLIST);
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_checklist);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager llm = new LinearLayoutManager(this);
//        llm.setReverseLayout(true);
//        llm.setStackFromEnd(true);
//        recyclerView.setLayoutManager(llm);
//
//        //create adapter, add progress bar, set adapter
//        mFirebaseAdapter = new ChecklistFirebaseAdapter(ChecklistItemObject.class, R.layout.card_checklist,
//                ChecklistViewHolder.class, mChecklistRef, this, getSupportFragmentManager());
//        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                //once loaded, hide the progress bar
//                mProgressBar.setVisibility(View.GONE);
//                mFab.setVisibility(View.VISIBLE);
//            }
//        });
//        recyclerView.setAdapter(mFirebaseAdapter);

    }

    public void initViews(){
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mCallback = this;

        mFab = (FloatingActionButton) findViewById(R.id.fab_checklist);
        mFab.setVisibility(View.INVISIBLE);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupNewFrag(CHECKLIST_OBJ, ChecklistActivity.this);
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_checklistactivity);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_checklist);
    }

    public void setupRecyclerView(){
        //set layout
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(llm);

        //get database ref to pass to adapter
        mChecklistRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(CHECKLIST);

        //create adapter, add progress bar, set adapter
        mFirebaseAdapter = new ChecklistFirebaseAdapter(ChecklistItemObject.class, R.layout.card_checklist,
                ChecklistViewHolder.class, mChecklistRef, this, mCallback);
        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                //once loaded, hide the progress bar
                mProgressBar.setVisibility(View.GONE);
                mFab.setVisibility(View.VISIBLE);
            }
        });

        mRecyclerView.setAdapter(mFirebaseAdapter);

    }

    @Override
    public void hideFabFromNewFrag() {
        mFab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void putFabBackFromNewFrag() {
        mFab.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideFabFromViewFrag() {
        mFab.setVisibility(View.INVISIBLE);

    }

    @Override
    public void putFabBackFromViewFrag() {
        mFab.setVisibility(View.VISIBLE);

    }
}
