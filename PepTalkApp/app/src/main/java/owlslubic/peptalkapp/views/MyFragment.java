//package owlslubic.peptalkapp.views;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.PagerAdapter;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import owlslubic.peptalkapp.R;
//import owlslubic.peptalkapp.models.PepTalkObject;
//
///**
// * Created by owlslubic on 9/5/16.
// */
//public class MyFragment extends Fragment {
//
//    //this is my staging area, i'll move it to main if thats where it should go..?
//    //either the main act default display should be a frag also, or the main is an activity and these just show up atop it?
//
//
//    private String mTitle, mBody;
//    private PepTalkObject mPepTalk;
//    TextView mTextViewTitle, mTextViewBody;
//
//    public static MyFragment newInstance(PepTalkObject peptalk){//}, String title, String body) {
//        MyFragment frag = new MyFragment();
//        Bundle args = new Bundle();
//        args.putString("title", peptalk.getTitle());
//        args.putString("body", peptalk.getBody());
//        frag.setArguments(args);
//        return frag;
//    }
//
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mTitle = getArguments().getString("title");
//        mBody = getArguments().getString("body");
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_peptalk, container, false);
//
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        //this is where i could set an onclick listener?
//
//        mTextViewTitle = (TextView) view.findViewById(R.id.textview_frag_title);
//        mTextViewBody = (TextView) view.findViewById(R.id.textview_frag_body);
//        if (mTextViewTitle != null && mTextViewBody != null){
//            mTextViewTitle.setText(mTitle);
//            mTextViewBody.setText(mBody);
//        }
//    }
//
//
//    //does this go in the main activity or?
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        setRetainInstance(true);
//    }
//
//
//
//
//
//}
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
