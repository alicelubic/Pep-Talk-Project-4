package owlslubic.peptalkapp.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.presenters.DBHelper;
import owlslubic.peptalkapp.presenters.FragmentMethods;


/**
 * Created by owlslubic on 9/19/16.
 */

public class NewFrag extends Fragment implements View.OnClickListener {
    private static final String USERS = "users";
    private static final String TAG = "NewFrag";
    public static final String PEPTALKS = "peptalks";
    public static final String CHECKLIST = "checklist";
    public static final String PEPTALK_TITLE = "title";
    public static final String PEPTALK_BODY = "body";
    public static final String CHECKLIST_TEXT = "text";
    public static final String CHECKLIST_NOTES = "notes";
    public static final String TOP_TEXT = "top_text";
    public static final String BOTTOM_TEXT = "bottom_text";
    public static final String OBJECT_TYPE = "object_type";
    public static final String NEW_OR_EDIT = "new_or_edit";
    public static final String KEY = "key";
    public static final String NEW = "new";
    public static final String EDIT = "edit";
    private static final String NEW_FRAG_TAG = "new_frag_tag";
    public EditText mTitle, mBody;
    public ImageButton mDone, mCancel;
    String mObjectType, mNewOrEdit, mKey, mTitleText, mBodyText;
    FABCoordinatorNewFrag mCallbackNew;
    Context mContext;
//    ViewFrag.FABCoordinator mCallback;
    //attach snackbar to activity view


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mCallback = (ViewFrag.FABCoordinator) context;
        mCallbackNew = (FABCoordinatorNewFrag) context;
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_new_edit, container, false);
        mTitle = (EditText) view.findViewById(R.id.edittext_fragment_title);
        mBody = (EditText) view.findViewById(R.id.edittext_fragment_body);
        mDone = (ImageButton) view.findViewById(R.id.imagebutton_fragment_done);
        mCancel = (ImageButton) view.findViewById(R.id.imagebutton_fragment_cancel);

        mObjectType = getArguments().getString(OBJECT_TYPE);
        mNewOrEdit = getArguments().getString(NEW_OR_EDIT);
        mKey = getArguments().getString(KEY);
        mTitleText = getArguments().getString(TOP_TEXT);
        mBodyText = getArguments().getString(BOTTOM_TEXT);

        mCallbackNew.hideFabFromNewFrag();


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mTitle.setMovementMethod(new ScrollingMovementMethod());
        mTitle.setCursorVisible(true);
        mTitle.setFocusableInTouchMode(true);
        mTitle.requestFocus();
        mBody.setMovementMethod(new ScrollingMovementMethod());
        mBody.setCursorVisible(true);
        mBody.setFocusableInTouchMode(true);
        mBody.requestFocus();
        if (mNewOrEdit.equals(NEW)) {
            if (mObjectType.equals(CHECKLIST)) {
                mTitle.setHint("What do you want to add to your checklist?");
                mBody.setHint("Any notes on this?");
            } else if (mObjectType.equals(PEPTALKS)) {
                mTitle.setHint("Give your new pep talk a title");
                mBody.setHint("Everything is going to be okay because...");
            } else {
                Log.d(TAG, "onViewCreated: OBJECT TYPE FROM BUNDLE = " + mObjectType);
            }
        } else if (mNewOrEdit.equals(EDIT)) {
            mTitle.setText(mTitleText);
            mBody.setText(mBodyText);
        }
        mDone.setOnClickListener(this);
        mCancel.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imagebutton_fragment_done:

                String titleInput = mTitle.getText().toString().trim();
                String bodyInput = mBody.getText().toString().trim();

                //first check for valid input
                if (titleInput.equalsIgnoreCase("") || titleInput.equalsIgnoreCase(" ")) {
                    mTitle.setError("oops! please enter a valid title");
                } else if (mObjectType.equals(PEPTALKS) && (bodyInput.equalsIgnoreCase("") || bodyInput.equalsIgnoreCase(" "))) {
                    mBody.setError("oops! please enter valid text");
                } else {

                    //then write to database
                    if (mObjectType.equals(CHECKLIST)) {
                        if (mNewOrEdit.equals(NEW)) {
                            DBHelper.writeNewChecklist(titleInput, bodyInput, getContext(), false);
//                            Toast.makeText(getContext(), "checklist item added", Toast.LENGTH_SHORT).show();
                        } else if (mNewOrEdit.equals(EDIT)) {
                            DBHelper.updateChecklist(mKey, titleInput, bodyInput, getContext());
//                            Toast.makeText(getContext(), "checklist updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "oops! something went wrong with new/edit checklist", Toast.LENGTH_SHORT).show();
                        }


                    } else if (mObjectType.equals(PEPTALKS)) {
                        if (mNewOrEdit.equals(NEW)) {
                            DBHelper.writeNewPeptalk(titleInput, bodyInput, getContext(), false);
//                            Toast.makeText(getContext(), "peptalk added", Toast.LENGTH_SHORT).show();
                        } else if (mNewOrEdit.equals(EDIT)) {
                            DBHelper.updatePepTalk(mKey, titleInput, bodyInput, getContext());
//                            Toast.makeText(getContext(), "peptalk updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "oops! something went wrong with new/edit peptalk", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "oops! something went wrong", Toast.LENGTH_SHORT).show();
                    }

                    FragmentMethods.detachFragment(getActivity(), NEW_FRAG_TAG);
                }
                break;
            case R.id.imagebutton_fragment_cancel:
                FragmentMethods.detachFragment(getActivity(), NEW_FRAG_TAG);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        mCallbackNew.putFabBackFromNewFrag();
    }

    public interface FABCoordinatorNewFrag {
        void hideFabFromNewFrag();
        void putFabBackFromNewFrag();
    }


}
