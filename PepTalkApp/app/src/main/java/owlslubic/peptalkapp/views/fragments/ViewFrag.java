package owlslubic.peptalkapp.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import owlslubic.peptalkapp.R;

import static owlslubic.peptalkapp.presenters.FragmentMethods.*;


/**
 * Created by owlslubic on 9/19/16.
 */
public class ViewFrag extends Fragment {
    private static final String TAG = "ViewFrag";
    TextView mTopTextView, mBottomTextView;
    String mTitleText, mBodyText, mObjectType, mKey;
    ImageButton mEdit;
    FABCoordinatorViewFrag mCallback;
    NewFrag.FABCoordinatorNewFrag mCallbackNewFrag;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (FABCoordinatorViewFrag) context;
        mCallbackNewFrag = (NewFrag.FABCoordinatorNewFrag) context;
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
                setupEditFrag(getActivity(), mObjectType, mKey, mTitleText, mBodyText);
                mCallbackNewFrag.hideFabFromNewFrag();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mCallback.putFabBackFromViewFrag();
    }

    public interface FABCoordinatorViewFrag {
        void hideFabFromViewFrag();
        void putFabBackFromViewFrag();
    }




}
