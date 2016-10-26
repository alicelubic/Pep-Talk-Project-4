package owlslubic.peptalkapp.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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

import static owlslubic.peptalkapp.presenters.FirebaseHelper.CHECKLIST;
import static owlslubic.peptalkapp.presenters.FragmentMethods.*;


/**
 * Created by owlslubic on 9/19/16.
 */

public class NewFrag extends Fragment implements View.OnClickListener {
    private static final String TAG = "NewFrag";
    public EditText mTitle, mBody;
    public ImageButton mDone, mCancel;
    String mObjectType, mNewOrEdit, mKey, mTitleText, mBodyText, mTitleInput, mBodyInput;
    FABCoordinatorNewFrag mCallbackNew;
    Context mContext;
    //attach snackbar to activity view

    private TextInputLayout inputLayoutTitle, inputLayoutBody;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

        mObjectType = getArguments().getString(FragmentMethods.OBJECT_TYPE);
        mNewOrEdit = getArguments().getString(FragmentMethods.NEW_OR_EDIT);
        mKey = getArguments().getString(FragmentMethods.KEY);
        mTitleText = getArguments().getString(FragmentMethods.TOP_TEXT);
        mBodyText = getArguments().getString(FragmentMethods.BOTTOM_TEXT);

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
        if (mNewOrEdit.equals(NEW)) {
            if (mObjectType.equals(FragmentMethods.CHECKLIST_OBJ)) {
                mTitle.setHint("What do you want to add to your checklist?");
                mBody.setHint("Any notes on this?");
            } else if (mObjectType.equals(FragmentMethods.PEPTALK_OBJ)) {
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

        mTitle.addTextChangedListener(new ATextWatcher(mTitle));
        mBody.addTextChangedListener(new ATextWatcher(mBody));



    }


    public boolean isPepTalk() {
        if (mObjectType.equals(FragmentMethods.PEPTALK_OBJ)) {
            return true;
        } else return false;
    }

    public boolean isChecklistItem() {
        if (mObjectType.equals(FragmentMethods.CHECKLIST_OBJ)) {
            return true;
        } else return false;
    }

    public boolean isPepBodyValid(String body) {

        if (mObjectType.equals(FragmentMethods.PEPTALK_OBJ) && (body.length() == 0)) {
            //in this case, don't go any further because we at least need some body text to move on
            return false;
        }
        return true;
    }

    public boolean isCheckValid(String title) {

        if (mObjectType.equals(FragmentMethods.CHECKLIST_OBJ) && (title.length() == 0)) {
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

    public void writeToDatabase() {
        if (mObjectType.equals(FragmentMethods.CHECKLIST_OBJ)) {
            if (mNewOrEdit.equals(NEW)) {
                FirebaseHelper.writeNewChecklist(mTitleInput, mBodyInput, getContext(), false);
            } else if (mNewOrEdit.equals(EDIT)) {
                FirebaseHelper.updateChecklist(mKey, mTitleInput, mBodyInput, getContext());
            } else {
                Toast.makeText(getContext(), "oops! something went wrong with your checklist", Toast.LENGTH_SHORT).show();
            }
        } else if (mObjectType.equals(FragmentMethods.PEPTALK_OBJ)) {
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
//                    mBody.setError("oops! please enter valid text");
                    inputLayoutBody.setError("oops! please enter something");
                    inputLayoutBody.requestFocus();

                } else if (isChecklistItem() && !isCheckValid(titleInput)) {
//                    mTitle.setError("oops! please enter valid text");
                    inputLayoutTitle.setError("oops! don't leave this blank");
                    inputLayoutTitle.requestFocus();
                } else {
                    setmTitleInput(titleInput, bodyInput);
                    writeToDatabase();
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

    @Override//TODO should setRetainInstance be before or after the super.onAct...
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onActivityCreated(savedInstanceState);
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

    private class ATextWatcher implements TextWatcher {

        View mView;

        private ATextWatcher(View view){
            mView = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
