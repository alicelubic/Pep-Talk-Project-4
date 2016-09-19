package owlslubic.peptalkapp.views.fragments;

import android.app.Activity;
import android.content.Intent;
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

public class NewFrag extends Fragment implements View.OnClickListener {
    private static final String USERS = "users";
    private static final String PEPTALKS = "peptalks";
    private static final String CHECKLIST = "checklist";
    private static final String TAG = "NewFrag";
    public EditText mTitle, mBody;
    public ImageButton mDone, mCancel;


    //attach snackbar to activity view


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mPeptalkRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(PEPTALKS);
            mChecklistRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(CHECKLIST);
        } else {
            Log.d(TAG, "onCreate: ");
        }
*/
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_new_edit, container, false);
        mTitle = (EditText) view.findViewById(R.id.edittext_fragment_title);
        mBody = (EditText) view.findViewById(R.id.edittext_fragment_body);
        mDone = (ImageButton) view.findViewById(R.id.imagebutton_fragment_done);
        mCancel = (ImageButton) view.findViewById(R.id.imagebutton_fragment_cancel);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (view.getContext() instanceof MainActivity) {
            //if it came from new peptalk button, set edittexts hints:
//            mTitle.setText("Give your new pep talk a title");
//            mBody.setText("Everything is going to be okay because...");

            //if it came from new checklist button, set edittexts hints:
            mTitle.setText("What do you want to add to your checklist?");
            mBody.setText("Any notes on this?");

        } else if (view.getContext() instanceof PepTalkListActivity) {
            mTitle.setText("Give your new pep talk a title");
            mBody.setText("Everything is going to be okay because...");


        } else if (view.getContext() instanceof ChecklistActivity) {
            mTitle.setText("What do you want to add to your checklist?");
            mBody.setText("Any notes on this?");
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


    public static void writeNewPeptalk(String title, String body) {//, boolean isWidgetDefault) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference peptalkRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(PEPTALKS);
            DatabaseReference pepKey = peptalkRef.push();
            String key = pepKey.getKey();
            final PepTalkObject peptalk = new PepTalkObject(key, title, body, false);
            pepKey.setValue(peptalk);
        }else{
            //TODO let the user know it didn't work
        }
    }
    public static void writeNewChecklist(String text, String notes){
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            DatabaseReference checklistRef = FirebaseDatabase.getInstance().getReference().child(USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(CHECKLIST);
            DatabaseReference itemKey = checklistRef.push();//this creates the unique key, but no data
            String key = itemKey.getKey();//then we grab the id from db so we can set it to the object when it is created
            final ChecklistItemObject item = new ChecklistItemObject(key, text, notes);
            itemKey.setValue(item);
            Log.d(TAG, "writeNewChecklist: new key is: " + key);
        }else{
            //TODO let the user know it didn't work
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.imagebutton_fragment_done:

                //it should decide if this is new vs edit peptalk, do this:
                //to decide, it needs to know what button was clicked to launch the frag
                String titleInput = mTitle.getText().toString().trim();
                String bodyInput = mBody.getText().toString().trim();

                //first check for valid input
                if (titleInput.equalsIgnoreCase("") || titleInput.equalsIgnoreCase(" ")) {
                    mTitle.setError("oops! please enter a valid title");
                } else if (bodyInput.equalsIgnoreCase("") || bodyInput.equalsIgnoreCase(" ")) {
                    mBody.setError("oops! please enter valid text");
                } else {
                    //then write to database
                    if (view.getContext() instanceof PepTalkListActivity) {
                        writeNewPeptalk(titleInput, bodyInput);
//                Snackbar snackbar = Snackbar.make(view.getRootView().findViewById(R.id.coordinator_layout_peptalklist_activity), "Please sign in to add to checklist", Snackbar.LENGTH_SHORT);
//                Toast.makeText(context, "pep talk added", Toast.LENGTH_SHORT).show();
                    } else if (view.getContext() instanceof ChecklistActivity) {
                        writeNewChecklist(titleInput, bodyInput);

                    } else {
                        //TODO determine if it came from the new checklist or new peptalk fablet in main activity
                        //set these accordingly:
//                        writeNewPeptalk(titleInput, bodyInput);
//                        writeNewChecklist(titleInput, bodyInput);
                    }
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
