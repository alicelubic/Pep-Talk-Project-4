package owlslubic.peptalkapp.views;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.presenters.FirebaseHelper;
import owlslubic.peptalkapp.presenters.FragmentMethods;
import owlslubic.peptalkapp.presenters.PepTalkFirebaseAdapter;
import owlslubic.peptalkapp.presenters.PepTalkViewHolder;
import owlslubic.peptalkapp.views.fragments.NewEditFrag;
import owlslubic.peptalkapp.views.fragments.ViewFrag;

import static owlslubic.peptalkapp.presenters.FragmentMethods.*;


public class PepTalkListActivity extends AppCompatActivity implements ViewFrag.FABCoordinatorViewFrag, NewEditFrag.FABCoordinatorNewFrag {

    private static final String TAG = "PepTalkListActivity";
    private PepTalkFirebaseAdapter mFirebaseAdapter;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFab;
    private RecyclerView mRecyclerView;
    private ViewFrag.FABCoordinatorViewFrag mCallback;
    private DatabaseReference mRootRef;
    private DatabaseReference mPepTalkRef;
    private DatabaseReference mChecklistRef;
    private FirebaseUser mCurrentUser;
    private String mUID;
    private boolean mIsUserSignedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pep_talk_list);
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mCurrentUser != null) {
            mUID = mCurrentUser.getUid();
            Log.d(TAG, "DEBUG DB >>  oncreate mUID = "+ mUID);
            mIsUserSignedIn = true;
        } else {
            mIsUserSignedIn = false;
        }


        initViews();
        setupRecyclerView();

    }

    public void initViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        mCallback = this;

        //fab launches new pep fragment
        mFab = (FloatingActionButton) findViewById(R.id.fab_peptalk_list);
        mFab.setVisibility(View.INVISIBLE);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupNewFrag(PEPTALK_OBJ, PepTalkListActivity.this);
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_peptalklist);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_peptalk_list);
    }

    public void setupRecyclerView() {
        mPepTalkRef = FirebaseDatabase.getInstance().getReference().child(FirebaseHelper.USERS).child(mUID).child(FirebaseHelper.PEPTALKS);
        Log.d(TAG, "DEBUG DB >>  setUpRecyclerView db ref mUID = "+ mUID);

        //set layout
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);

        //create adapter, add progress bar, set adapter
        mFirebaseAdapter = new PepTalkFirebaseAdapter(PepTalkObject.class,
                R.layout.card_peptalk, PepTalkViewHolder.class, mPepTalkRef,
                this, mCallback);//, this); took out the onstartdrag listener

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                //once loaded, hide the progress bar
                mProgressBar.setVisibility(View.GONE);
                mFab.setVisibility(View.VISIBLE);
                //TODO set a transition so this fades in or some shit

            }
        });
        mRecyclerView.setAdapter(mFirebaseAdapter);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }


    /**
     * this is too redundant, but I'll make it lean later
     */
    @Override
    public void hideFabFromViewFrag() {
        mFab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void putFabBackFromViewFrag() {
        mFab.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFabFromNewFrag() {
        mFab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void putFabBackFromNewFrag() {
        mFab.setVisibility(View.VISIBLE);
    }

}
