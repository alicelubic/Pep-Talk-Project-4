package owlslubic.peptalkapp.presenters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import owlslubic.peptalkapp.models.ChecklistItemObject;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.views.MainActivity;

import static android.content.DialogInterface.BUTTON_POSITIVE;
import static owlslubic.peptalkapp.presenters.FragmentMethods.*;

/**
 * Created by owlslubic on 10/2/16.
 */

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    public static final String USERS = "users";
    public static final String PEPTALKS = "peptalks";
    public static final String CHECKLIST = "checklist";
    private static final String PEPTALK_TITLE = "title";
    private static final String PEPTALK_BODY = "body";
    private static final String CHECKLIST_TEXT = "text";
    private static final String CHECKLIST_NOTES = "notes";
    private static final String CHECKLIST_CHECKED = "checked";
    private static DatabaseReference mRootRef;
    private static DatabaseReference mPepTalkRef;
    private static DatabaseReference mChecklistRef;
    private FirebaseUser mCurrentUser;
    private String mUID;
    private boolean mIsUserSignedIn;


    public FirebaseHelper() {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mCurrentUser != null) {
            mUID = mCurrentUser.getUid();
            mIsUserSignedIn = true;
        } else {
            mIsUserSignedIn = false;
        }

        mRootRef = FirebaseDatabase.getInstance().getReference().child(FirebaseHelper.USERS).child(mUID);

        mPepTalkRef = FirebaseDatabase.getInstance().getReference().child(FirebaseHelper.USERS).child(mUID).child(FirebaseHelper.PEPTALKS);

        mChecklistRef = FirebaseDatabase.getInstance().getReference().child(USERS).child(mUID).child(CHECKLIST);

    }

    /*
        public static boolean isUserSignedIn() {
            if (mCurrentUser != null) {
                return true;
            }
            return false;
        }

        public static String getUserId() {
            if (mCurrentUser != null) {
                return mCurrentUser.getUid();
            }
            return null;
        }


        public static DatabaseReference getDbRef(boolean isPepTalkRef, boolean isChecklistRef){
            if(isPepTalkRef){
                return mPepTalkRef;
            }else if(isChecklistRef){
                return mChecklistRef;
            }else{
                return mRootRef;
            }
        }
        */
    public static void writeNewPepTalk(final String title, String body, final Context context, final boolean isPreloadedContent) {

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            DatabaseReference pepKey = mPepTalkRef.push();
            String key = pepKey.getKey();
            final PepTalkObject peptalk = new PepTalkObject(key, title, body, false);
            pepKey.setValue(peptalk, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d(TAG, "writeNewPepTalk error: " + databaseError.getMessage());
                        Toast.makeText(context, "oops! something went wrong... sign out and try again", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!isPreloadedContent) {
                            Toast.makeText(context, title + " added to peptalks!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        } else {
            Toast.makeText(context, "oops! something went wrong...", Toast.LENGTH_SHORT).show();
        }
    }

    public static void updatePepTalk(String key, final String title, String body, final Context context) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mPepTalkRef.child(key).child(PEPTALK_TITLE).setValue(title, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Toast.makeText(context, "oops! something went wrong... sign out and try again", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, title + " updated!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            mPepTalkRef.child(key).child(PEPTALK_BODY).setValue(body);
        }
    }


    public static void deletePepTalk(final String key, final String title, final Context context) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mPepTalkRef.child(key).setValue(null, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d(TAG, "onComplete: DELETE FROM DATABASE ERROR: " + databaseError);
                        Toast.makeText(context, "oops! something went wrong, peptalk not deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "\"" + title + "\" deleted", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    public static void writeNewChecklist(final String text, String notes, final Context context, final boolean isPreloadedContent) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DatabaseReference itemKey = mChecklistRef.push();//this creates the unique key, but no data
            String key = itemKey.getKey();//then we grab the id from db so we can set it to the object when it is created
            final ChecklistItemObject item = new ChecklistItemObject(key, text, notes, false);
            itemKey.setValue(item, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d(TAG, "writeNewChecklist error: " + databaseError.getMessage());
                        Toast.makeText(context, "oops! something went wrong... sign out and try again", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!isPreloadedContent) {
                            Toast.makeText(context, text + " added to checklist!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
            Log.d(TAG, "writeNewChecklist: new key is: " + key);
        } else {
            if (!isPreloadedContent) {
                Toast.makeText(context, "oops! something went wrong... sign out and try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void updateIsChecked(String key, boolean isChecked) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mChecklistRef.child(key).child(CHECKLIST_CHECKED).setValue(isChecked, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        Log.i(TAG, "onComplete: UPDATE ISCHECKED successful");
                    }
                }
            });

        } else {
            Log.d(TAG, "updateChecklist failed, getCurrentUser() == null");
        }
    }

    public static void updateChecklist(String key, String text, String notes, final Context context) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            mChecklistRef.child(key).child(CHECKLIST_TEXT).setValue(text, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Toast.makeText(context, "oops! something went wrong... sign out and try again", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "checklist item updated!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            mChecklistRef.child(key).child(CHECKLIST_NOTES).setValue(notes);

        } else {
            Log.d(TAG, "updateChecklist failed, getCurrentUser() == null");
            Toast.makeText(context, "oops! something went wrong... sign out and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static void deleteChecklist(final ChecklistItemObject check, final Context context) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mChecklistRef.child(check.getKey()).setValue(null, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d(TAG, "onComplete: DELETE FROM DATABASE ERROR: " + databaseError);
                        Toast.makeText(context, "oops! something went wrong, item not deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "\"" + check.getText() + "\" deleted", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }


    public static void launchDeletePepTalkDialog(final String key, final String title, final Context context, final String tag, final View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Are you sure you want to delete your \"" + title + "\" peptalk?")
                .setNegativeButton("nvm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dismiss
                    }
                })
                .setPositiveButton("yep", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //this is what does the actual deleting
                        deletePepTalk(key, title, context);

                        //then close the fragment IF there is one! which wouldnt be the case if its onlongclick
                        if (isFragVisible((FragmentActivity) context, tag)) {
                            detachFragment((FragmentActivity) context, tag, view);//, false);//making this last parameter false because we just want that frag gone
                        }
                    }
                });
        builder.create().show();

    }

    public static void launchDeleteChecklistDialog(final ChecklistItemObject check, final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("Are you sure you want to delete \"" + check.getText() + "\"?")
                .setNegativeButton("nvm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //dismiss
                    }
                })
                .setPositiveButton("yep", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //this is what does the actual deleting
                        deleteChecklist(check, context);
                    }
                });
        builder.create().show();

    }


    @Nullable
    public static Object getModelByKey(String objectType, String key) {
        //TODO finish this method lol
        String title = null;
        String body = null;
//        Query queryRef = getDbRef(true, false).orderByKey().equalTo(key);
//        queryRef.addValueEventListener()


        if (objectType.equals(PEPTALK_OBJ)) {
            //but it shouldnt make a new one? or does it matter?
            return new PepTalkObject(key, title, body, false);
        } else if (objectType.equals(CHECKLIST_OBJ)) {
            return new ChecklistItemObject(key, title, body, false);
        } else {
            return null;
        }
    }
}
