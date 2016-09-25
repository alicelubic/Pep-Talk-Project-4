package owlslubic.peptalkapp.views.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.ChecklistItemObject;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.views.ChecklistActivity;
import owlslubic.peptalkapp.views.MainActivity;
import owlslubic.peptalkapp.views.PepTalkListActivity;


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
    public EditText mTitle, mBody;
    public ImageButton mDone, mCancel;
    String mObjectType, mNewOrEdit, mKey, mTitleText, mBodyText;


    //attach snackbar to activity view


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


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onActivityCreated(savedInstanceState);
    }


    public static void writeNewPeptalk(String title, String body) {//, boolean isWidgetDefault) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference peptalkRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(PEPTALKS);
            DatabaseReference pepKey = peptalkRef.push();
            String key = pepKey.getKey();
            final PepTalkObject peptalk = new PepTalkObject(key, title, body, false);
            pepKey.setValue(peptalk);
        } else {
            //TODO let the user know it didn't work
        }
    }

    public static void writeNewChecklist(String text, String notes) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference checklistRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(CHECKLIST);
            DatabaseReference itemKey = checklistRef.push();//this creates the unique key, but no data
            String key = itemKey.getKey();//then we grab the id from db so we can set it to the object when it is created
            final ChecklistItemObject item = new ChecklistItemObject(key, text, notes);
            itemKey.setValue(item);
            Log.d(TAG, "writeNewChecklist: new key is: " + key);
        } else {
            //TODO let the user know it didn't work
        }
    }

    public void updatePepTalk(String key, String title, String body) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference peptalkRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(PEPTALKS);
            peptalkRef.child(key).child(PEPTALK_TITLE).setValue(title);
            peptalkRef.child(key).child(PEPTALK_BODY).setValue(body);
            Log.d(TAG, "updatePepTalk: key is: " + key);
            Log.d(TAG, "updatePepTalk: new title is: " + title);
        }
    }

    public void updateChecklist(String key, String text, String notes) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference checklistRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(CHECKLIST);

            checklistRef.child(key).child(CHECKLIST_TEXT).setValue(text);
            checklistRef.child(key).child(CHECKLIST_NOTES).setValue(notes);
        }
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
                } else if (mObjectType.equals(PEPTALKS)&& (bodyInput.equalsIgnoreCase("") || bodyInput.equalsIgnoreCase(" "))) {
                    mBody.setError("oops! please enter valid text");
                } else {
                    //then write to database
                    if (mObjectType.equals(CHECKLIST)) {
                        if (mNewOrEdit.equals(NEW)) {
                            writeNewChecklist(titleInput, bodyInput);
                            Toast.makeText(getContext(), "checklist item added", Toast.LENGTH_SHORT).show();
                        } else if (mNewOrEdit.equals(EDIT)) {
                            updateChecklist(mKey, titleInput, bodyInput);
                            Toast.makeText(getContext(), "checklist updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "oops! something went wrong with new/edit checklist", Toast.LENGTH_SHORT).show();
                        }

                    } else if (mObjectType.equals(PEPTALKS)) {
                        if (mNewOrEdit.equals(NEW)) {
                            writeNewPeptalk(titleInput, bodyInput);
                            Toast.makeText(getContext(), "peptalk added", Toast.LENGTH_SHORT).show();
                        } else if (mNewOrEdit.equals(EDIT)) {
                            updatePepTalk(mKey, titleInput, bodyInput);
                            Toast.makeText(getContext(), "peptalk updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "oops! something went wrong with new/edit peptalk", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "oops! something went wrong", Toast.LENGTH_SHORT).show();
                    }

                    determineActivityStartIntent(view);
                }
                break;
            case R.id.imagebutton_fragment_cancel:
                determineActivityStartIntent(view);
                break;
        }


    }

    public void determineActivityStartIntent(View view) {
        if (view.getContext() instanceof PepTalkListActivity) {
            startActivity(new Intent(getActivity(), PepTalkListActivity.class));
        } else if (view.getContext() instanceof ChecklistActivity) {
            startActivity(new Intent(getActivity(), ChecklistActivity.class));
        } else {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }
    }


}
