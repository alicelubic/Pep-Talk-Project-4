package owlslubic.peptalkapp.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import owlslubic.peptalkapp.views.MainActivity;


/**
 * Created by owlslubic on 9/5/16.
 */
public class RecyclerFrag extends Fragment implements View.OnClickListener {
    private static final String TAG = "RecyclerFrag";
    private String mTitle, mBody;
    private PepTalkObject mPepTalk;
    public TextView mTextViewTitle;
    public TextView mTextViewBody;
    public ImageButton mEdit, mBack;
    private ProgressBar mProgressBar;
    public DatabaseReference mPeptalkRef;
    public FirebaseRecyclerAdapter mFirebaseAdapter;
    RecyclerView mFragRecycler;
    private DatabaseReference mRootRef;
    private DatabaseReference mPepTalkRef;
    private DatabaseReference mChecklistRef;
    private FirebaseUser mCurrentUser;
    private String mUID;
    private boolean mIsUserSignedIn;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        assignDBRefs();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_peptalk, container, false);
        mFragRecycler = (RecyclerView) view.findViewById(R.id.recyclerview_fragment);
        mBack = (ImageButton) view.findViewById(R.id.imagebutton_frag_back);
        mBack.setOnClickListener(this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar_recyclerviewfrag);
//        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_peptalk_frag);
//        toolbar.setBackgroundColor(getResources().getColor(R.color.mySecondaryBlue));
//        toolbar.setHapticFeedbackEnabled(true);
//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(toolbar);
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mTextViewTitle = (TextView) view.findViewById(R.id.textview_frag_title);
        mTextViewBody = (TextView) view.findViewById(R.id.textview_frag_body);

        if (mTextViewBody != null) {
            mTextViewBody.setMovementMethod(new ScrollingMovementMethod());
        }

        //setting up the recyclerview
//        mPeptalkRef = getDbRef(true,false);

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, true);
        llm.setStackFromEnd(true);

        mFragRecycler.setLayoutManager(llm);
        mFirebaseAdapter = new PepTalkFirebaseAdapter(PepTalkObject.class, R.layout.frag_card,
                PepTalkViewHolder.class, mPeptalkRef, view.getContext(), null);

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                //once loaded, hide the progress bar
                mProgressBar.setVisibility(View.GONE);

            }
        });
        mFragRecycler.setAdapter(mFirebaseAdapter);
        //so that when you scroll, it doesn't get out of control:
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mFragRecycler);

        /**testing for sunil*/
//        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
//        animator.setAddDuration(1000);
//        mFragRecycler.setItemAnimator(animator);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imagebutton_frag_back:
//                boolean b = false;
//                if ((mTextViewTitle.getText().toString().trim().length() > 0) && (mTextViewBody.getText().toString().trim().length() > 0)) {
//                    b = true;//its true because there is text there, so thusly the text has changed
//                }
                FragmentMethods.detachFragment(getActivity(), FragmentMethods.RECYCLERVIEW_FRAG_TAG, getView());//, b);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }

    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if(item.getItemId()==android.R.id.home){
//            Toast.makeText(getContext(), "tryna go home!", Toast.LENGTH_SHORT).show();
//            getActivity().onBackPressed();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    public void assignDBRefs() {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mIsUserSignedIn = false;

        if (mCurrentUser != null) {
            mUID = mCurrentUser.getUid();
            mIsUserSignedIn = true;
        }
        mRootRef = FirebaseDatabase.getInstance().getReference().child(FirebaseHelper.USERS).child(mUID);

        mPepTalkRef = FirebaseDatabase.getInstance().getReference().child(FirebaseHelper.USERS).child(mUID).child(FirebaseHelper.PEPTALKS);

        mChecklistRef = FirebaseDatabase.getInstance().getReference().child(FirebaseHelper.USERS).child(mUID).child(FirebaseHelper.CHECKLIST);
    }

}