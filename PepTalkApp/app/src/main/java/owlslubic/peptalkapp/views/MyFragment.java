package owlslubic.peptalkapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by owlslubic on 9/5/16.
 */
public class MyFragment extends Fragment {
    private static final String USERS = "users";
    private static final String PEPTALKS = "peptalks";
    private static final String TAG = "MyFragment";

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

    //i'm gonna use this to display the peptalk from the list activity too'?


//    public static MyFragment newInstance(PepTalkObject peptalk){//}, String title, String body) {
//        MyFragment frag = new MyFragment();
//        Bundle args = new Bundle();
//        args.putString("title", peptalk.getTitle());
//        args.putString("body", peptalk.getBody());
//        frag.setArguments(args);
//        return frag;
//    }


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
                PepTalkViewHolder.class, mPeptalkRef, view.getContext());
        mFragRecycler.setAdapter(mFirebaseAdapter);
    }


    //does this go in the main activity or?
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }



}

//
//public class MyViewPagerAdapter extends PagerAdapter{
//
//
//    //get db data and add to an array list of strings?
//    //define where all the data is in the beginning, in the constructor
//
//    //viewpager should create arraylist of fragments - add the data and then add it to the list
//
//
//
//    @Override
//    public int getCount() {
//        //get size of fragment array list
//
//        return 0;
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object) {
//        return false;
//    }
//
//
//}
