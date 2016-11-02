package owlslubic.peptalkapp.views.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.presenters.FirebaseHelper;
import owlslubic.peptalkapp.presenters.FragmentMethods;
import owlslubic.peptalkapp.views.MainActivity;

import static owlslubic.peptalkapp.presenters.FragmentMethods.*;


/**
 * Created by owlslubic on 9/19/16.
 */
public class ViewFrag extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener {
    private static final String TAG = "ViewFrag";
    private static final String PREFS = "prefs";
    TextView mTopTextView, mBottomTextView;
    String mTitleText, mBodyText, mObjectType, mKey;
    ImageButton mEdit, mTrash, mCancel, mDone;
    FABCoordinatorViewFrag mCallback;
    NewFrag.FABCoordinatorNewFrag mCallbackNewFrag;
    Context mContext;
    ListView mListView;
    PepTalkObject mModel;
    FirebaseListAdapter mAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (FABCoordinatorViewFrag) context;
        mCallbackNewFrag = (NewFrag.FABCoordinatorNewFrag) context;
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_view, container, false);
        mTopTextView = (TextView) view.findViewById(R.id.textview_frag_view_top);
        mBottomTextView = (TextView) view.findViewById(R.id.textview_frag_view_bottom);
        mEdit = (ImageButton) view.findViewById(R.id.imagebutton_frag_view_edit);
        mTrash = (ImageButton) view.findViewById(R.id.imagebutton_frag_view_delete);

        //for emergency widget
        mListView = (ListView) view.findViewById(R.id.listview_emergency_peptalk);
        mCancel = (ImageButton) view.findViewById(R.id.imagebutton_viewfrag_cancel);
        mDone = (ImageButton) view.findViewById(R.id.imagebutton_viewfrag_done);

        mObjectType = getArguments().getString(OBJECT_TYPE);
        mKey = getArguments().getString(KEY);
        mTitleText = getArguments().getString(TOP_TEXT);
        mBodyText = getArguments().getString(BOTTOM_TEXT);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mObjectType.equals(EMERGENCY_PEPTALK)) {
            //out with the old
            mBottomTextView.setVisibility(View.GONE);
            mEdit.setVisibility(View.GONE);
            mTrash.setVisibility(View.GONE);
            //in with the new
            mTopTextView.setText(getResources().getString(R.string.widget_prompt_blurb));
            mCancel.setVisibility(View.VISIBLE);
            mCancel.setOnClickListener(this);
            mDone.setVisibility(View.VISIBLE);
            mDone.setOnClickListener(this);
            mListView.setVisibility(View.VISIBLE);
            mListView.setOnItemClickListener(this);
            setListViewAdapter();
        } else
            mTopTextView.setText(mTitleText);
        mBottomTextView.setText(mBodyText);
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupEditFrag(getActivity(), mObjectType, mKey, mTitleText, mBodyText);
                mCallbackNewFrag.hideFabFromNewFrag();
            }
        });
        mTrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mObjectType.equals(PEPTALK_OBJ)) {
                    FirebaseHelper.launchDeletePepTalkDialog(mKey, mTitleText, mContext, VIEW_FRAG_TAG, getView());
                } else if (mObjectType.equals(CHECKLIST_OBJ)) {
                    //get object by key to delete it
                    Toast.makeText(mContext, "get object by key to delete it... forgot to set this up huh?", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
//        mCallback.putFabBackFromViewFrag();
        //TODO when this is here, you can see it pop back up which  i dont like
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        setEmergencyPeptalkText(position);
        Toast.makeText(mContext, "go finish writing the getModelByKey() method", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

    }

    public void setEmergencyPeptalkText(int position){
        //we want to grab the text of the item clicked
        String key = mAdapter.getRef(position).getKey();
        PepTalkObject model = (PepTalkObject) FirebaseHelper.getModelByKey(PEPTALK_OBJ, key);
        String widgetText = model.getBody();
        //pass that text to shared prefs
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("WIDGET_TEXT", widgetText);
        editor.apply();
        //let the user know their emergency peptalk was updates
        Toast.makeText(getContext(), "Text set to widget, go to your home screen to check it out!", Toast.LENGTH_SHORT).show();
        //close the fragment
        FragmentMethods.detachFragment(getActivity(), VIEW_FRAG_TAG, null);

    }

    public interface FABCoordinatorViewFrag {
        void hideFabFromViewFrag();

        void putFabBackFromViewFrag();
    }

    public void setListViewAdapter() {
        DatabaseReference ref = FirebaseHelper.getDbRef(true, false);
        mAdapter = new FirebaseListAdapter<PepTalkObject>(getActivity(),
                PepTalkObject.class, android.R.layout.simple_list_item_1, ref) {
            @Override
            protected void populateView(View v, PepTalkObject model, int position) {
                TextView t = (TextView) v.findViewById(android.R.id.text1);
                t.setText(model.getTitle());
                t.setGravity(View.TEXT_ALIGNMENT_CENTER);
                t.setTextColor(getResources().getColor(R.color.mySecondaryBlue));//, R.style.PepTalkTheme));
                int p = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
//                ViewGroup.LayoutParams params = t.getLayoutParams();
                t.setPadding(p, p, p, p);
            }
        };
        mListView.setAdapter(mAdapter);
    }


}
