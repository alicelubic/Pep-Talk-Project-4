package owlslubic.peptalkapp.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.ChecklistItemObject;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.presenters.FragmentMethods;

import static owlslubic.peptalkapp.presenters.FragmentMethods.setupEditFrag;
import static owlslubic.peptalkapp.views.fragments.NewFrag.BOTTOM_TEXT;
import static owlslubic.peptalkapp.views.fragments.NewFrag.CHECKLIST;
import static owlslubic.peptalkapp.views.fragments.NewFrag.KEY;
import static owlslubic.peptalkapp.views.fragments.NewFrag.OBJECT_TYPE;
import static owlslubic.peptalkapp.views.fragments.NewFrag.PEPTALKS;
import static owlslubic.peptalkapp.views.fragments.NewFrag.TOP_TEXT;

/**
 * Created by owlslubic on 9/19/16.
 */
public class ViewFrag extends Fragment {
    private static final String TAG = "ViewFrag";
    public static final String VIEW = "view";


    TextView mTopTextView, mBottomTextView;
    String mTitleText, mBodyText, mObjectType, mKey;
    ImageButton mEdit;
    FABCoordinator mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (FABCoordinator) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_view, container, false);
        mTopTextView = (TextView) view.findViewById(R.id.textview_frag_view_top);
        mBottomTextView = (TextView) view.findViewById(R.id.textview_frag_view_bottom);
        mEdit = (ImageButton) view.findViewById(R.id.imagebutton_frag_view_edit);

        mObjectType = getArguments().getString(OBJECT_TYPE);
        mKey = getArguments().getString(KEY);
        mTitleText = getArguments().getString(TOP_TEXT);
        mBodyText = getArguments().getString(BOTTOM_TEXT);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTopTextView.setText(mTitleText);
        mBottomTextView.setText(mBodyText);
        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentMethods.setupEditFrag(getActivity(), mObjectType, mKey, mTitleText, mBodyText);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mCallback.putFabBack();
    }

    public interface FABCoordinator{
        void hideFabWhenFragOpens();
        void putFabBack();
    }




}
