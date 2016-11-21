package owlslubic.peptalkapp.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.presenters.FirebaseHelper;
import owlslubic.peptalkapp.presenters.FragmentMethods;

import static owlslubic.peptalkapp.presenters.FragmentMethods.*;


/**
 * Created by owlslubic on 9/19/16.
 */

public class NewEditFrag extends Fragment implements View.OnClickListener, View.OnLongClickListener, View.OnKeyListener {
    private static final String TAG = "NewEditFrag";
    private EditText mTitle, mBody;
    private ImageButton mDone, mCancel;
    private String mObjectType, mNewOrEdit, mKey, mTitleText, mBodyText, mTitleInput, mBodyInput, mWidgetTag;
    private FABCoordinatorNewFrag mCallbackNew;
    private TextInputLayout mInputLayoutTitle, mInputLayoutBody;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbackNew = (FABCoordinatorNewFrag) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getAllArguments();
        return initViews(inflater.inflate(R.layout.frag_new_edit, container, false));
    }

    private View initViews(View view) {
        mTitle = (EditText) view.findViewById(R.id.edittext_fragment_title);
        mBody = (EditText) view.findViewById(R.id.edittext_fragment_body);
        mDone = (ImageButton) view.findViewById(R.id.imagebutton_fragment_done);
        mCancel = (ImageButton) view.findViewById(R.id.imagebutton_fragment_cancel);
        mInputLayoutTitle = (TextInputLayout) view.findViewById(R.id.inputlayout_title);
        mInputLayoutBody = (TextInputLayout) view.findViewById(R.id.inputlayout_body);

        mCallbackNew.hideFabFromNewFrag();

        return view;
    }

    private void getAllArguments() {
        mObjectType = getArguments().getString(OBJECT_TYPE);
        mNewOrEdit = getArguments().getString(NEW_OR_EDIT);
        mTitleText = getArguments().getString(TOP_TEXT);
        mBodyText = getArguments().getString(BOTTOM_TEXT);
        mKey = getArguments().getString(KEY);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDone.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mTitle.setOnClickListener(this);
        mTitle.setOnLongClickListener(this);
        mBody.setOnClickListener(this);
        mBody.setOnLongClickListener(this);

        //top edittext
        mTitle.setMovementMethod(new ScrollingMovementMethod());
        mTitle.setCursorVisible(true);
        mTitle.setSingleLine();

        //bottom edittext
        mBody.setMovementMethod(new ScrollingMovementMethod());

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //determine which hint to display
        setHints();

    }


    public boolean isPepTalk() {
        return mObjectType.equals(PEPTALK_OBJ);
    }

    public boolean isChecklistItem() {
        return mObjectType.equals(CHECKLIST_OBJ);
    }

    public boolean isPepBodyValid(String body) {
        if (mObjectType.equals(PEPTALK_OBJ) && (body.length() == 0)) {
            //in this case, don't go any further because we at least need some body text to move on
            return false;
        }
        return true;
    }

    public boolean isCheckValid(String title) {

        if (mObjectType.equals(CHECKLIST_OBJ) && (title.length() == 0)) {
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
//                mBodyInput = body;
            } else {
                mTitleInput = body.substring(0, Math.min(body.length(), maxLength));
//                mBodyInput = body;
            }
        } else if (title.length() > maxLength) {
            //in this case, i'm truncating the title
            mTitleInput = title.substring(0, Math.min(title.length(), maxLength)) + "...";
//            mBodyInput = body;
        } else {
            mTitleInput = title;
        }
        mBodyInput = body;
    }

    public void writeToDatabase() {
        if (mObjectType.equals(CHECKLIST_OBJ)) {
            if (mNewOrEdit.equals(NEW)) {
                FirebaseHelper.writeNewChecklist(mTitleInput, mBodyInput, getContext(), false);
            } else if (mNewOrEdit.equals(EDIT)) {
                FirebaseHelper.updateChecklist(mKey, mTitleInput, mBodyInput, getContext());
            } else {
                Toast.makeText(getContext(), "oops! something went wrong with your checklist", Toast.LENGTH_SHORT).show();
            }
        } else if (mObjectType.equals(PEPTALK_OBJ)) {
            if (mNewOrEdit.equals(NEW)) {
                FirebaseHelper.writeNewPepTalk(mTitleInput, mBodyInput, getContext(), false);
                Log.d(TAG + "DB PROB", "NEW writeToDatabase: title: "+mTitleInput+" and body: "+mBodyInput);
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
            case R.id.edittext_fragment_title:
//                toggleKeyboard(view, true);
                break;
            case R.id.edittext_fragment_body:
//                toggleKeyboard(view, true);
                break;
            case R.id.imagebutton_fragment_done:
                String titleInput = mTitle.getText().toString().trim();
                String bodyInput = mBody.getText().toString().trim();

                //check for valid input before writing to database
                if (isPepTalk() && !isPepBodyValid(bodyInput)) {
                    mInputLayoutBody.setError("oops! please enter something");
                    mInputLayoutBody.requestFocus();
                } else if (isChecklistItem() && !isCheckValid(titleInput)) {
                    mInputLayoutTitle.setError("oops! don't leave this blank");
                    mInputLayoutTitle.requestFocus();
                } else {
                    setmTitleInput(titleInput, bodyInput);
                    writeToDatabase();
                    detachFragment(getActivity(), NEW_FRAG_TAG, getView());
                }
                break;
            case R.id.imagebutton_fragment_cancel:
                if (mTitle.getText().length() > 0 || mBody.getText().length() > 0) {
                    FragmentMethods.launchCancelAlertDialog(getContext(),getView());
                } else {
                    detachFragment(getActivity(), NEW_FRAG_TAG, getView());
                }
                break;
        }

    }

    @Override
    public boolean onLongClick(View view) {
        switch (view.getId()) {
            case R.id.edittext_fragment_title:
                mTitle.selectAll();
                break;
            case R.id.edittext_fragment_body:
                mBody.selectAll();
                break;
        }
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        switch (v.getId()) {
            case R.id.edittext_fragment_title:
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mBody.requestFocus();
                }
                break;
            case R.id.edittext_fragment_body:
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    mDone.requestFocus();
                }
                break;
        }


        return true;
    }


    public void setHints(){
        if (mNewOrEdit.equals(NEW)) {
            if (isChecklistItem()) {
                mInputLayoutTitle.setHint(getString(R.string.new_checklist_title_hint));
                mInputLayoutBody.setHint(getString(R.string.new_checklist_notes_hint));
            } else if (isPepTalk()) {
                mInputLayoutTitle.setHint(getString(R.string.new_peptalk_title_hint));
                mInputLayoutBody.setHint(getString(R.string.new_peptalk_body_hint));
            } else {
                Log.d(TAG, "onViewCreated: OBJECT TYPE FROM BUNDLE = " + mObjectType);
            }
        } else if (mNewOrEdit.equals(EDIT)) {
            Log.d(TAG + "DB PROB", "setupEditTexts: EDIT title is: "+mTitleText+ " and body is: "+mBodyText);
            mTitle.setText(mTitleText);
            mBody.setText(mBodyText);
        }
    }

    public void toggleKeyboard(View view, boolean makeVisible) {
        //TODO finish this method because this shit is so annoying
        if (makeVisible) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        } else {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);

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
