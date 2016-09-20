package owlslubic.peptalkapp.views.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class EditFrag extends Fragment implements View.OnClickListener {

    private static final String USERS = "users";
    private static final String PEPTALKS = "peptalks";
    private static final String CHECKLIST = "checklist";
    private static final String PEPTALK_TITLE = "title";
    private static final String PEPTALK_BODY = "body";
    private static final String CHECKLIST_TEXT = "notes";
    private static final String CHECKLIST_NOTES = "notes";
    private static final String TAG = "EditFrag";
    public EditText mTitle, mBody;
    public ImageButton mDone, mCancel;
    public String mPepTitle, mPepBody, mPepKey;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_new_edit, container, false);
        mTitle = (EditText) view.findViewById(R.id.edittext_fragment_title);
        mBody = (EditText) view.findViewById(R.id.edittext_fragment_body);
        mDone = (ImageButton) view.findViewById(R.id.imagebutton_fragment_done);
        mCancel = (ImageButton) view.findViewById(R.id.imagebutton_fragment_cancel);


        SharedPreferences prefs = getContext().getSharedPreferences("MODEL_DATA", 1);
        mPepTitle = prefs.getString("peptalk_title", "default");
        mPepBody = prefs.getString("peptalk_body", "default");
        mPepKey = prefs.getString("peptalk_key", "default");
        Log.d(TAG, "SharedPrefs says the key is: " + mPepKey);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //TODO determine if this is updating peptalk or checklist
        //this will be called from the view fragment, or from the cardview in recyclerview fragment


        //TODO see if getting context this way is helpful or nah
        //does getACtivity get the fragment itself
        // (because we could say if the base context is the recyclerview frag do this, or if the view frag, do that),
        // or the actual activity context
//        if (getActivity().getBaseContext() instanceof PepTalkListActivity) {

        if (view.getContext() instanceof PepTalkListActivity) {
            mTitle.setText(mPepTitle);
            mBody.setText(mPepBody);

        } else if (view.getContext() instanceof ChecklistActivity) {
//            mTitle.setText(check.getTitle());
//            mBody.setText(check.getBody());

        } else {
            
            Log.d(TAG, "onViewCreated: context is not instance of any Activity");
        }


        mDone.setOnClickListener(this);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        super.onActivityCreated(savedInstanceState);
    }

    public void updatePepTalk(String key, String title, String body) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference peptalkRef = FirebaseDatabase.getInstance().getReference().child("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("peptalks");
            peptalkRef.child(key).child("title").setValue(title);
            peptalkRef.child(key).child("body").setValue(body);
            Log.d(TAG, "updatePepTalk: key is: " + key);
            Log.d(TAG, "updatePepTalk: new title is: " + title);

            //when boolean is set in edit bit, it'll be pulling this value from the object
        }
    }

    public void updateChecklist(ChecklistItemObject check, String text, String notes) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference checklistRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(CHECKLIST);

            checklistRef.child(check.getKey()).child(CHECKLIST_TEXT).setValue(text);
            checklistRef.child(check.getKey()).child(CHECKLIST_NOTES).setValue(notes);
        }
    }

    @Override
    public void onClick(View view) {
        String titleInput = mTitle.getText().toString().trim();
        String bodyInput = mBody.getText().toString().trim();

        //first check for valid input
        if (titleInput.equalsIgnoreCase("") || titleInput.equalsIgnoreCase(" ")) {
            mTitle.setError("oops! please enter a valid title");
        } else if (bodyInput.equalsIgnoreCase("") || bodyInput.equalsIgnoreCase(" ")) {
            mBody.setError("oops! please enter valid text");
        } else {

            switch (view.getId()) {
                case R.id.imagebutton_fragment_done:
                    if (view.getContext() instanceof PepTalkListActivity) {
                        //then write to database
                        updatePepTalk(mPepKey, titleInput, bodyInput);
                        startActivity(new Intent(getActivity(), PepTalkListActivity.class));
                        Log.d(TAG, "onClick updatePepTalk key is "+mPepKey);
//                        Toast.makeText(view.getContext(), "update peptalk not workin ri na", Toast.LENGTH_SHORT).show();
                    } else if (view.getContext() instanceof ChecklistActivity) {
//                        updateChecklist(titleInput, bodyInput);
                        Toast.makeText(view.getContext(), "update checklist not workin ri na", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), ChecklistActivity.class));
                    } else {
                        updatePepTalk(mPepKey, titleInput, bodyInput);
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }

                    break;

                case R.id.imagebutton_fragment_cancel:
                    if (view.getContext() instanceof PepTalkListActivity) {
                        startActivity(new Intent(getActivity(), PepTalkListActivity.class));
                    } else if (view.getContext() instanceof ChecklistActivity) {
                        startActivity(new Intent(getActivity(), ChecklistActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                    break;
            }

        }
    }

}

