package owlslubic.peptalkapp.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.ChecklistItemObject;
import owlslubic.peptalkapp.models.PepTalkObject;

/**
 * Created by owlslubic on 8/30/16.
 */
public class CustomDialog extends AlertDialog {
    //consider having cool lookin buttons for these dialogs yo

    private static final String TAG = "CustomDialog";
    private static DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference checklistRef = dbRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("checklist");
    private static DatabaseReference peptalkRef = dbRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("peptalks");


    //dont think i need a constructor but whatever
    public CustomDialog(Context context) {
        super(context);
    }
    //if all this goes well, I'll write some sort of switch statement for layouts so that there's only one method to do all dis launchin

    //push appends data to a list, so it generates a unique key every time a new child is added,
    //which can be called upon with getKey




    //THESE METHODS LAUNCH THE CREATE AND EDIT DIALOGS



    //will static launch methods cause a problem?
    public static void launchNewPeptalkDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View layout = inflater.inflate(R.layout.dialog_new_peptalk, null);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//next try adjust pan
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();


        final EditText title = (EditText) dialog.findViewById(R.id.edittext_new_peptalk_title);
        final EditText body = (EditText) dialog.findViewById(R.id.edittext_new_peptalk);
        Button submit = (Button) dialog.findViewById(R.id.button_new_peptalk);


        //TODO set it so soft keyboard comes up automatically, and dialog accomodates it... coordinator maybe/
        //TODO set max char length for the title edittext and account for invalid input with error

        //this works
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String titleInput = title.getText().toString().trim();
                String bodyInput = body.getText().toString().trim();

                if (titleInput.equalsIgnoreCase("") || titleInput.equalsIgnoreCase(" ")) {
                    title.setError("oops! please enter a valid title");
                } else if (bodyInput.equalsIgnoreCase("") || bodyInput.equalsIgnoreCase(" ")) {
                    body.setError("oops! please enter valid text");
                } else {
                    writeNewPeptalk(titleInput, bodyInput);//, false);//setting all as false to start with
                    Log.i(TAG, "on submit click: title is " + titleInput);
                    Toast.makeText(context, "pep talk added", Toast.LENGTH_SHORT).show();

                    //the problem is with getting the view of where it's going!
//                    Snackbar.make(findViewById(R.id.drawer_layout), "I'm a Snackbar", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            //undo
//                            Toast.makeText(MainActivity.this, "Snackbar Action", Toast.LENGTH_LONG).show();
//                        }
//                    }).show();
//                }

                    dialog.dismiss();
                }

                //snackbar - DOES NOT WORK but i'd like it to
                //if the view is main act, use that layout.
                //if the view is peptalkact, use that layout
//                if (context.getClass() == MainActivity.class) {
//                    Snackbar snackbar = Snackbar.make(view.findViewById(R.id.coordinator_layout_main_activity), "Pep talk added", Snackbar.LENGTH_SHORT);
//                    snackbar.show();
//                } else if (context.getClass() == PepTalkListActivity.class) {
//                    Snackbar snackbar = Snackbar.make(view.findViewById(R.id.coordinator_layout_peptalklist_activity), "Pep talk added", Snackbar.LENGTH_SHORT);
//                    snackbar.show();
//                }


            }
        });

    }

    //works
    public static void launchEditPeptalkDialog(final Context context, final PepTalkObject peptalk) {//don't think i need position with the firebaseadapter}, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_edit_peptalk, null);
        builder.setView(layout);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.show();

        //brings up existing text to edit
        final EditText editTitle = (EditText) dialog.findViewById(R.id.edittext_edit_peptalk_title);
        editTitle.setText(peptalk.getTitle());
        editTitle.setCursorVisible(true);
        editTitle.setFocusableInTouchMode(true);
        editTitle.requestFocus();

        final EditText editBody = (EditText) dialog.findViewById(R.id.edittext_edit_peptalk_body);
        editBody.setText(peptalk.getBody());
        editBody.setCursorVisible(true);
        editBody.setFocusableInTouchMode(true);
        editBody.requestFocus();

        Button b = (Button) dialog.findViewById(R.id.button_submit_edit_peptalk);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString().trim();
                String body = editBody.getText().toString().trim();

                if (title.equalsIgnoreCase("") || title.equalsIgnoreCase(" ")) {
                    editTitle.setError("oops! please enter a valid title");
                } else if (body.equalsIgnoreCase("") || body.equalsIgnoreCase(" ")) {
                    editBody.setError("oops! please enter valid text");
                } else {
                    updatePepTalk(peptalk, title, body);
                    Log.i(TAG, "on submit click: title is " + title);
                    Toast.makeText(context, "pep talk updated", Toast.LENGTH_SHORT).show();
                    //replace with snackbar
                    dialog.dismiss();
                }


            }
        });

    }

    public static void launchNewChecklistDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_new_checklist, null);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.show();

        final EditText editText = (EditText) dialog.findViewById(R.id.edittext_new_checklist);
        final EditText editText2 = (EditText) dialog.findViewById(R.id.edittext_new_checklist_notes);
        Button submit = (Button) dialog.findViewById(R.id.button_new_checklist);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = editText.getText().toString().trim();
                String inputNotes = editText2.getText().toString().trim();


                if (inputText.equalsIgnoreCase("") || inputText.equalsIgnoreCase(" ")) {
                    editText.setError("oops! please enter a valid title");
                } else {
                    writeNewChecklist(inputText,inputNotes);
                    Toast.makeText(context, "added to checklist", Toast.LENGTH_SHORT).show();
                    //replace with snackbar
                    dialog.dismiss();
                }


            }
        });

    }

    public static void launchEditChecklistDialog(final Context context, final ChecklistItemObject check) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_edit_checklist, null);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.show();

        final EditText editText = (EditText) dialog.findViewById(R.id.edittext_edit_checklist);
        editText.setText(check.getText());
        editText.setCursorVisible(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        final EditText editText2 = (EditText) dialog.findViewById(R.id.edittext_edit_checklist_notes);
        editText2.setText(check.getNotes());
        editText2.setCursorVisible(true);
        editText2.setFocusableInTouchMode(true);
        editText2.requestFocus();

        Button submit = (Button) dialog.findViewById(R.id.button_edit_checklist);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String update = editText.getText().toString().trim();
                String updateNotes = editText2.getText().toString().trim();

                if (update.equalsIgnoreCase("") || update.equalsIgnoreCase(" ")) {
                    editText.setError("oops! please enter a valid title");
                } else {
                    updateChecklist(check, update, updateNotes);
                    Toast.makeText(context, "checklist updated", Toast.LENGTH_SHORT).show();
                    //replace with snackbar
                    dialog.dismiss();
                }

            }
        });


    }


    public static void launchDeletePepTalkDialog(final PepTalkObject peptalk, final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setNegativeButton("nevermind", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss
            }
        });
        builder.setPositiveButton("yurp", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //this is what does the actual deleting
                peptalkRef.child(peptalk.getKey()).setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Log.i(TAG, "DELETE CHECKLIST error: " + databaseError.toString());
                    }
                });

            }
        });
        builder.setTitle("Are you sure you want to delete your \"" + peptalk.getTitle() + "\" peptalk?");
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setButton(BUTTON_POSITIVE, "yurp", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                peptalkRef.child(peptalk.getKey()).setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    }
                });
                //replace with snackbar

                Toast.makeText(context, "\"" + peptalk.getTitle() + "\" deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }

    public static void launchDeleteChecklistDialog(final ChecklistItemObject check, final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setNegativeButton("nevermind", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setPositiveButton("yurp", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checklistRef.child(check.getKey()).setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        Log.i(TAG, "DELETE CHECKLIST error: " + databaseError.toString());
                    }
                });

            }
        });
        builder.setTitle("Are you sure you want to delete?");
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setButton(BUTTON_POSITIVE, "yurp", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checklistRef.child(check.getKey()).setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    }
                });
                //replace with snackbar
                Toast.makeText(context, "\"" + check.getText() + "\" deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }



    //THESE METHODS ACTUALLY INTERACT WITH DATABASE AND ARE USED IN THE DIALOGS
    //TODO put these in an async task yo

    public static void writeNewChecklist(String text, String notes) {
        DatabaseReference itemKey = checklistRef.push();//this creates the unique key, but no data
        String key = itemKey.getKey();//then we grab the id from db so we can set it to the object when it is created
        final ChecklistItemObject item = new ChecklistItemObject(key, text, notes);
        itemKey.setValue(item);
        Log.d(TAG, "writeNewChecklist: new key is: " + key);
    }

    public static void writeNewPeptalk(String title, String body) {//, boolean isWidgetDefault) {
        DatabaseReference pepKey = peptalkRef.push();
        String key = pepKey.getKey();
        final PepTalkObject peptalk = new PepTalkObject(key, title, body, false);
        pepKey.setValue(peptalk);
    }

    public static void updatePepTalk(PepTalkObject peptalk, String title, String body) {
        peptalkRef.child(peptalk.getKey()).child("title").setValue(title);
        peptalkRef.child(peptalk.getKey()).child("body").setValue(body);
        //when boolean is set in the dialog, it'll be pulling this value from the object
        //with a method like if whatever checkbox is selected, :
//        setWidgetDefaultFromDialog(peptalk,true);then update the database

    }

    public static void updateChecklist(ChecklistItemObject check, String text, String notes) {
        checklistRef.child(check.getKey()).child("text").setValue(text);
        checklistRef.child(check.getKey()).child("notes").setValue(notes);

    }





    //this will be what is launched if all the checklist items are checked
    public void launchChecklistCompleteDialog() {
    }

    //in case i cant get the fragment display to work, or if I need to launch it from the peptalk list activity i just want a backup
    public void launchPeptalkDisplayDialog() {
    }


    public static boolean setWidgetDefaultFromDialog(PepTalkObject pepTalkObject, boolean b) {
        pepTalkObject.setIsWidgetDefault(b);
        peptalkRef.child(pepTalkObject.getKey()).child("isWidgetDefault").setValue(b);

        return b;
    }

}



