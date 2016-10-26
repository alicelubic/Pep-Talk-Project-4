package owlslubic.peptalkapp.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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
import owlslubic.peptalkapp.presenters.FirebaseHelper;
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
    String mObjectType, mNewOrEdit, mKey, mTitleText, mBodyText, mTitleInput, mBodyInput;
    FABCoordinatorNewFrag mCallbackNew;
    Context mContext;
//    ViewFrag.FABCoordinatorViewFrag mCallback;
    //attach snackbar to activity view

    private TextInputLayout inputLayoutTitle, inputLayoutBody;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mCallback = (ViewFrag.FABCoordinatorViewFrag) context;
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


        inputLayoutTitle = (TextInputLayout) view.findViewById(R.id.inputlayout_title);
        inputLayoutBody = (TextInputLayout) view.findViewById(R.id.inputlayout_body);


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
//        mBody.requestFocus();
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


    public boolean isPepTalk() {
        if (mObjectType.equals(PEPTALKS)) {
            return true;
        } else return false;
    }

    public boolean isChecklistItem() {
        if (mObjectType.equals(CHECKLIST)) {
            return true;
        } else return false;
    }

    public boolean isPepBodyValid(String body) {

        if (mObjectType.equals(PEPTALKS) && (body.length()==0)) {
            //in this case, don't go any further because we at least need some body text to move on
//            mBody.setError("oops! please enter valid text");
            return false;
        }
            return true;
    }

    public boolean isCheckValid(String title) {

        if (mObjectType.equals(CHECKLIST) && (title.length()==0)) {
            //in this case, don't go any further because we at least need some the checklist item, notes dont matter
            return false;
        }
            return true;
    }

    public void setmTitleInput(String title, String body) {

        int maxLength = 40;

        if (title.equalsIgnoreCase("") || title.equalsIgnoreCase(" ")) {
            //in this case, i'm going to set the first bit of body text to be the title
            if (body.length() > maxLength) {
                mTitleInput = body.substring(0, Math.min(body.length(), maxLength)) + "...";
            } else {
                mTitleInput = body.substring(0, Math.min(body.length(), maxLength));
            }
        } else if (title.length() > maxLength) {
            //in this case, i'm truncating the title
            mTitleInput = title.substring(0, Math.min(title.length(), maxLength)) + "...";
        } else {
            mTitleInput = title;
        }

    }

    public void writeToDatabase() {//passing it this way might not matter since member variables...
        if (mObjectType.equals(CHECKLIST)) {
            if (mNewOrEdit.equals(NEW)) {
                FirebaseHelper.writeNewChecklist(mTitleInput, mBodyInput, getContext(), false);
            } else if (mNewOrEdit.equals(EDIT)) {
                FirebaseHelper.updateChecklist(mKey, mTitleInput, mBodyInput, getContext());
            } else {
                Toast.makeText(getContext(), "oops! something went wrong with your checklist", Toast.LENGTH_SHORT).show();
            }


        } else if (mObjectType.equals(PEPTALKS)) {
            if (mNewOrEdit.equals(NEW)) {
                FirebaseHelper.writeNewPeptalk(mTitleInput, mBodyInput, getContext(), false);
            } else if (mNewOrEdit.equals(EDIT)) {
                FirebaseHelper.updatePepTalk(mKey, mTitleInput, mBodyInput, getContext());
            } else {
                Toast.makeText(getContext(), "oops! something went wrong with your pep talk", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "oops! something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imagebutton_fragment_done:

                String titleInput = mTitle.getText().toString().trim();
                String bodyInput = mBody.getText().toString().trim();

                if (isPepTalk() && !isPepBodyValid(bodyInput)) {
                    mBody.setError("oops! please enter valid text");
                } else if (isChecklistItem() && !isCheckValid(titleInput)) {
                    mTitle.setError("oops! please enter valid text");
                } else {
                    setmTitleInput(titleInput, bodyInput);
                    writeToDatabase();
                    FragmentMethods.detachFragment(getActivity(), NEW_FRAG_TAG);

                }


                //TODO this logic looks horrible please find a better way to do it
                //first check for valid input
//                if (mObjectType.equals(PEPTALKS) && (mBodyInput.equalsIgnoreCase("") || mBodyInput.equalsIgnoreCase(" "))) {
//                    //in this case, don't go any further because we at least need some body text to move on
//                    mBody.setError("oops! please enter valid text");
//                } else {
//                    if (mObjectType.equals(PEPTALKS) && (mTitleInput.equalsIgnoreCase("") || mTitleInput.equalsIgnoreCase(" "))) {
//                        //in this case, i'm going to set the first bit of body text to be the title
//                        if (mBodyInput.length() > 40) {
//                            mTitleInput = mBodyInput.substring(0, Math.min(mBodyInput.length(), 40)) + "...";
//                        }
//                        mTitleInput = mBodyInput.substring(0, Math.min(mBodyInput.length(), 40));
//                    }
//                    if (mObjectType.equals(PEPTALKS) && (mTitleInput.length() > 40)) {
//                        //in this case, i'm truncating the title
//                        mTitleInput = mTitleInput.substring(0, Math.min(mTitleInput.length(), 40)) + "...";
//                    }
//
//
//                    //then write to database
//                    if (mObjectType.equals(CHECKLIST)) {
//                        if (mNewOrEdit.equals(NEW)) {
//                            FirebaseHelper.writeNewChecklist(mTitleInput, mBodyInput, getContext(), false);
//                        } else if (mNewOrEdit.equals(EDIT)) {
//                            FirebaseHelper.updateChecklist(mKey, mTitleInput, mBodyInput, getContext());
//                        } else {
//                            Toast.makeText(getContext(), "oops! something went wrong with your checklist", Toast.LENGTH_SHORT).show();
//                        }
//
//
//                    } else if (mObjectType.equals(PEPTALKS)) {
//                        if (mNewOrEdit.equals(NEW)) {
//                            FirebaseHelper.writeNewPeptalk(mTitleInput, mBodyInput, getContext(), false);
//                        } else if (mNewOrEdit.equals(EDIT)) {
//                            FirebaseHelper.updatePepTalk(mKey, mTitleInput, mBodyInput, getContext());
//                        } else {
//                            Toast.makeText(getContext(), "oops! something went wrong with your pep talk", Toast.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        Toast.makeText(getContext(), "oops! something went wrong", Toast.LENGTH_SHORT).show();
//                    }
//
//                    FragmentMethods.detachFragment(getActivity(), NEW_FRAG_TAG);
//                }

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


/**
 * MAKE THE TEXTWATCHER CLASS, MAKE SEPARATE METHODS TO VALIDATE THE TEXT INPUT, MAKE THIS NEATER OH FOR THE LOVE OF GOD
 */
}
