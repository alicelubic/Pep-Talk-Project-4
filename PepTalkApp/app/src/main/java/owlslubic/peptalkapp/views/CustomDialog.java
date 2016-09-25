package owlslubic.peptalkapp.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.ChecklistItemObject;
import owlslubic.peptalkapp.models.PepTalkObject;

/**
 * Created by owlslubic on 8/30/16.
 */
public class CustomDialog extends AlertDialog {
    //consider having cool lookin buttons for these dialogs yo

    private static final String TAG = "CustomDialog";
    private static final String PREFS = "prefs";
    private static DatabaseReference mDbRef = FirebaseDatabase.getInstance().getReference();
    private static DatabaseReference mChecklistRef;
    private static DatabaseReference mPeptalkRef;


    //dont think i need a constructor but whatever
    public CustomDialog(Context context) {
        super(context);
    }
    //if all this goes well, I'll write some sort of switch statement for layouts so that there's only one method to do all dis launchin

    //THESE METHODS LAUNCH THE CREATE AND EDIT DIALOGS

/*
    public static void launchNewPeptalkDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        final View layout = inflater.inflate(R.layout.dialog_new_peptalk, null);
        builder.setView(layout);
        builder.setNegativeButton("nvm", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("done", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//next try adjust pan
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();


        final EditText title = (EditText) dialog.findViewById(R.id.edittext_new_peptalk_title);
        final EditText body = (EditText) dialog.findViewById(R.id.edittext_new_peptalk);
        body.setMovementMethod(new ScrollingMovementMethod());


        //this works
        dialog.getButton(BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
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
//                    Snackbar snackbar = Snackbar.make(view.getRootView().findViewById(R.id.coordinator_layout_peptalklist_activity), "Please sign in to add to checklist", Snackbar.LENGTH_SHORT);
                    Toast.makeText(context, "pep talk added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

    }


    //works
    public static void launchEditPeptalkDialog(final Context context, final PepTalkObject peptalk) {//don't think i need position with the firebaseadapter}, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_edit_peptalk, null);
        builder.setView(layout);
        builder.setNegativeButton("nvm", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss
            }
        });
        builder.setPositiveButton("done", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

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
        editBody.setMovementMethod(new ScrollingMovementMethod());


        dialog.getButton(BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
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
        builder.setNegativeButton("nvm", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("done", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.show();

        final EditText editText = (EditText) dialog.findViewById(R.id.edittext_new_checklist);
        final EditText editText2 = (EditText) dialog.findViewById(R.id.edittext_new_checklist_notes);


        dialog.getButton(BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = editText.getText().toString().trim();
                String inputNotes = editText2.getText().toString().trim();


                if (inputText.equalsIgnoreCase("") || inputText.equalsIgnoreCase(" ")) {
                    editText.setError("oops! please enter a valid title");
                } else {
                    writeNewChecklist(inputText, inputNotes);
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
        builder.setNegativeButton("nvm", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("done", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        dialog.show();

        final EditText editText = (EditText) dialog.findViewById(R.id.edittext_edit_checklist);
        editText.setText(check.getTitle());
        editText.setCursorVisible(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        final EditText editText2 = (EditText) dialog.findViewById(R.id.edittext_edit_checklist_notes);
        editText2.setMovementMethod(new ScrollingMovementMethod());
        editText2.setCursorVisible(true);
        editText2.setFocusableInTouchMode(true);
        editText2.requestFocus();

        dialog.getButton(BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
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
*/

    public static void launchDeletePepTalkDialog(final PepTalkObject peptalk, final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setNegativeButton("nvm", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //dismiss
            }
        });
        builder.setPositiveButton("yep", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //this is what does the actual deleting
                mPeptalkRef.child(peptalk.getKey()).setValue(null, new DatabaseReference.CompletionListener() {
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
        dialog.setButton(BUTTON_POSITIVE, "yep", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mPeptalkRef.child(peptalk.getKey()).setValue(null, new DatabaseReference.CompletionListener() {
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
        builder.setNegativeButton("nvm", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setPositiveButton("yurp", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mChecklistRef.child(check.getKey()).setValue(null, new DatabaseReference.CompletionListener() {
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
                mChecklistRef.child(check.getKey()).setValue(null, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    }
                });
                //replace with snackbar
                Toast.makeText(context, "\"" + check.getTitle() + "\" deleted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }
/*
    public static void launchViewPepTalk(final PepTalkObject peptalk, final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.full_screen_dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_view_peptalk, null);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.show();

        TextView title = (TextView) dialog.findViewById(R.id.textview_pepview_title);
        TextView body = (TextView) dialog.findViewById(R.id.textview_pepview_body);
        body.setMovementMethod(new ScrollingMovementMethod());
        ImageButton edit = (ImageButton) dialog.findViewById(R.id.imagebutton_edit_peptalk);

        title.setText(peptalk.getTitle());
        body.setText(peptalk.getBody());
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                launchEditPeptalkDialog(context, peptalk);
            }
        });


    }

    public static void launchViewChecklist(final ChecklistItemObject check, final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.full_screen_dialog);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_view_peptalk, null);//using this layout because it was already there
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        dialog.show();

        TextView title = (TextView) dialog.findViewById(R.id.textview_pepview_title);
        TextView notes = (TextView) dialog.findViewById(R.id.textview_pepview_body);
        notes.setTextSize(14);
        notes.setMovementMethod(new ScrollingMovementMethod());
        ImageButton edit = (ImageButton) dialog.findViewById(R.id.imagebutton_edit_peptalk);

        title.setText(check.getTitle());
        notes.setText(check.getNotes());
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchEditChecklistDialog(context, check);
            }
        });

    }




    //THESE METHODS ACTUALLY INTERACT WITH DATABASE AND ARE USED IN THE DIALOGS
    //TODO put these in an async task yo


    //TODO write a new "if this exists don't add it" method that actually works
    public static void writeNewChecklist(String text, String notes) {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            mChecklistRef = mDbRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("checklist");

            DatabaseReference itemKey = mChecklistRef.push();//this creates the unique key, but no data
            String key = itemKey.getKey();//then we grab the id from db so we can set it to the object when it is created
            final ChecklistItemObject item = new ChecklistItemObject(key, text, notes);
            itemKey.setValue(item);
            Log.d(TAG, "writeNewChecklist: new key is: " + key);
        }
    }

//        moved to frag class
    public static void writeNewPeptalk(String title, String body) {//, boolean isWidgetDefault) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mPeptalkRef = mDbRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("peptalks");

            DatabaseReference pepKey = mPeptalkRef.push();
            String key = pepKey.getKey();
            final PepTalkObject peptalk = new PepTalkObject(key, title, body, false);
            pepKey.setValue(peptalk);
        }
    }

    public static void updatePepTalk(PepTalkObject peptalk, String title, String body) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mPeptalkRef = mDbRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("peptalks");

            mPeptalkRef.child(peptalk.getKey()).child("title").setValue(title);
            mPeptalkRef.child(peptalk.getKey()).child("body").setValue(body);
            //when boolean is set in the dialog, it'll be pulling this value from the object
        }
    }

    public static void updateChecklist(ChecklistItemObject check, String text, String notes) {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            mChecklistRef = mDbRef.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("checklist");

            mChecklistRef.child(check.getKey()).child("text").setValue(text);
            mChecklistRef.child(check.getKey()).child("notes").setValue(notes);
        }
    }


    //this will be what is launched if all the checklist items are checked
    public void launchChecklistCompleteDialog() {
    }

    //in case i cant get the fragment display to work, or if I need to launch it from the peptalk list activity i just want a backup
    public void launchPeptalkDisplayDialog() {
    }


    public static boolean setWidgetDefaultFromDialog(PepTalkObject pepTalkObject, boolean b) {
        pepTalkObject.setIsWidgetDefault(b);
        mPeptalkRef.child(pepTalkObject.getKey()).child("isWidgetDefault").setValue(b);
        return b;
    }
*/
    public static void launchAddWidgetTextDialog(final Context context){
        //decided to just add text straight to this because grabbing the text from firebase has proved more complicated than expected

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_add_widget_text, null);
        builder.setView(layout);
        builder.setNegativeButton("nvm", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("cool", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        final EditText editText = (EditText) dialog.findViewById(R.id.edittext_dialog_widget);


        dialog.getButton(BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String widgetText = editText.getText().toString().trim();

                SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("WIDGET_TEXT", widgetText);
                editor.apply();
                Toast.makeText(context, "Text set to widget", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });



    }




}



