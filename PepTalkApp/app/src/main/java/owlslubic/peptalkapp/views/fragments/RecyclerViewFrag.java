package owlslubic.peptalkapp.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.presenters.PepTalkFirebaseAdapter;
import owlslubic.peptalkapp.presenters.PepTalkViewHolder;
import owlslubic.peptalkapp.views.MainActivity;

/**
 * Created by owlslubic on 9/5/16.
 */
public class RecyclerViewFrag extends Fragment {
    private static final String USERS = "users";
    private static final String PEPTALKS = "peptalks";
    private static final String TAG = "RecyclerViewFrag";

    //this is my staging area, i'll move it to main if thats where it should go..?
    //either the main act default display should be a frag also, or the main is an activity and these just show up atop it?

    private String mTitle, mBody;
    private PepTalkObject mPepTalk;
    public TextView mTextViewTitle;
    public TextView mTextViewBody;
    public ImageButton mEdit, mBack;

    public DatabaseReference mPeptalkRef;
    public FirebaseRecyclerAdapter mFirebaseAdapter;
    RecyclerView mFragRecycler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_peptalk, container, false);
        mFragRecycler = (RecyclerView) view.findViewById(R.id.recyclerview_fragment);
        mBack = (ImageButton) view.findViewById(R.id.imagebutton_frag_back);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mTextViewTitle = (TextView) view.findViewById(R.id.textview_pepview_title);
        mTextViewBody = (TextView) view.findViewById(R.id.textview_pepview_body);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });
        if (mTextViewBody != null) {
            mTextViewBody.setMovementMethod(new ScrollingMovementMethod());
        }

        //setting up the recyclerview
        mPeptalkRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(PEPTALKS);

        mFragRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false));
        mFirebaseAdapter = new PepTalkFirebaseAdapter(PepTalkObject.class, R.layout.frag_card,
                PepTalkViewHolder.class, mPeptalkRef, view.getContext(), getFragmentManager());
        mFragRecycler.setAdapter(mFirebaseAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mFragRecycler);

        /**testing for sunil*/
//        RecyclerView.ItemAnimator animator = new DefaultItemAnimator();
//        animator.setAddDuration(1000);
//        mFragRecycler.setItemAnimator(animator);9 `


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }



}