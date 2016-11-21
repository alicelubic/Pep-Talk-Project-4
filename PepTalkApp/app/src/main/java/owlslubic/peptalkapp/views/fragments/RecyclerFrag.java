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
    public TextView mTextViewTitle;
    private TextView mTextViewBody;
    private ImageButton mBack;
    private ProgressBar mProgressBar;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private RecyclerView mFragRecycler;
    private DatabaseReference mRootRef;
    private FirebaseAuth mAuth;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_peptalk, container, false);
        mFragRecycler = (RecyclerView) view.findViewById(R.id.recyclerview_fragment);
        mBack = (ImageButton) view.findViewById(R.id.imagebutton_frag_back);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar_recyclerviewfrag);
        mTextViewTitle = (TextView) view.findViewById(R.id.textview_frag_title);
        mTextViewBody = (TextView) view.findViewById(R.id.textview_frag_body);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mTextViewBody != null) {
            mTextViewBody.setMovementMethod(new ScrollingMovementMethod());
        }
        mBack.setOnClickListener(this);

        //setting up the recyclerview
        LinearLayoutManager llm = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, true);
        llm.setStackFromEnd(true);

        //get ref to pass to adapter
        DatabaseReference peptalkRef = mRootRef
                .child(FirebaseHelper.USERS)
                .child(mAuth.getCurrentUser().getUid())
                .child(FirebaseHelper.PEPTALKS);

        mFragRecycler.setLayoutManager(llm);
        mFirebaseAdapter = new PepTalkFirebaseAdapter(PepTalkObject.class, R.layout.frag_card,
                PepTalkViewHolder.class, peptalkRef, view.getContext(), null);

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                //once data is loaded, hide the progress bar
                mProgressBar.setVisibility(View.GONE);

            }
        });
        mFragRecycler.setAdapter(mFirebaseAdapter);
        //so that when you scroll, it doesn't get out of control:
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mFragRecycler);


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
                FragmentMethods.detachFragment(getActivity(),
                        FragmentMethods.RECYCLERVIEW_FRAG_TAG, getView());//, b);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }

}