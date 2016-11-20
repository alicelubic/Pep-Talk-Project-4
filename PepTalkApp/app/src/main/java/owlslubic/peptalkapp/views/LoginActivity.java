package owlslubic.peptalkapp.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.presenters.FirebaseHelper;

import static owlslubic.peptalkapp.presenters.FirebaseHelper.writeNewChecklist;
import static owlslubic.peptalkapp.presenters.FirebaseHelper.writeNewPepTalk;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private TextInputLayout mNameLayout, mEmailLayout, mPasswordLayout;
    private EditText mEtName, mEtEmail, mEtPassword;
    private Button mSignUpButton, mSignInButton;
    private String mDisplayName, mEmail, mPassword;
    private ProgressDialog mProgDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private String mUID;
    private boolean mIsUserSignedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);

        mAuth = FirebaseAuth.getInstance();

        //TODO check if smartlock is enabled, and if you're already logged in, it should automatically
        //skip this activity
        if (mAuth.getCurrentUser() != null) {
            //already logged in, close this activity, and:
            startMainActivity();
        }

        initViews();



    }



    private void registerOrLoginUser(boolean isSignUp) {
        mDisplayName = mEtName.getText().toString().trim();
        mEmail = mEtEmail.getText().toString().trim();
        mPassword = mEtPassword.getText().toString().trim();

        //make sure there was valid input
        if (TextUtils.isEmpty(mDisplayName)) {
            //set error
            mNameLayout.setErrorEnabled(true);
            mNameLayout.setError("Please enter a name!");
            mNameLayout.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mEmail)) {
            //TODO include other stipulations for deal with this email badly formatted issue
            //set error;
            mEmailLayout.setErrorEnabled(true);
            mEmailLayout.setError("Please enter valid email address");
            mEmailLayout.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(mPassword)) {
            //set error
            mPasswordLayout.setErrorEnabled(true);
            mPasswordLayout.setError("Please enter a password");
            mPasswordLayout.requestFocus();
            return;
        }
        if(mPassword.length()<6){
            mPasswordLayout.setError("Password must be more than 6 characters");
            mPasswordLayout.requestFocus();
            return;
        }

        //assuming the data is good, now display progress dialog...
        String message;
        if (isSignUp) {
            message = "Registering...";
        } else {
            message = "Logging in...";
        }
        mProgDialog.setMessage(message);
        mProgDialog.show();


        if (!isSignUp) {
            //log in the user
            mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mProgDialog.dismiss();
                            if (task.isSuccessful()) {
                                startMainActivity();

                            } else {
                                //TODO let the user know this didn't work... and what to do next
                                Toast.makeText(LoginActivity.this, "bad news...", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "exception=" + task.getException().toString());
                            }
                        }
                    });

        } else {
            //create new user
            mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //TODO set the display name somehow
                                //insert the template peptalk content:
                                preloadPepTalks();
                                //take them to main act
                                startMainActivity();
                            } else {
                                //TODO let the user know this didn't work... and what to do next
                                Toast.makeText(LoginActivity.this, "bad news...", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, "exception=" + task.getException().toString());
                            }
                            mProgDialog.dismiss();
                        }
                    });
        }

    }

    private void preloadPepTalks(){
        if (mIsUserSignedIn) {
            writeNewChecklist(getString(R.string.checklist_water), getString(R.string.checklist_water_notes), this, true); //TODO CHECK IF USING this AS CONTEXT MAKES IT NOT WORK???
            writeNewChecklist(getString(R.string.checklist_eat), getString(R.string.checklist_eat_notes), this, true);
            writeNewChecklist(getString(R.string.checklist_move), getString(R.string.checklist_move_notes), this, true);
            writeNewChecklist(getString(R.string.checklist_moment), getString(R.string.checklist_moment_notes), this, true);
            writeNewChecklist(getString(R.string.checklist_breathe), getString(R.string.checklist_breathe_notes), this, true);
            writeNewChecklist(getString(R.string.checklist_locations), getString(R.string.checklist_locations_notes), this, true);

            writeNewPepTalk(getString(R.string.pep_past_present_title), getString(R.string.pep_past_present), this, true);
            writeNewPepTalk(getString(R.string.pep_facts_emotions_title), getString(R.string.pep_facts_emotions), this, true);
            writeNewPepTalk(getString(R.string.pep_do_your_best_title), getString(R.string.pep_do_your_best), this, true);
            writeNewPepTalk(getString(R.string.live_in_the_moment_title), getString(R.string.live_in_the_moment), this, true);
            writeNewPepTalk(getString(R.string.doing_and_not_doing_title), getString(R.string.doing_and_not_doing), this, true);
            writeNewPepTalk(getString(R.string.exercise_guilt_title), getString(R.string.exercise_guilt), this, true);
        }
    }

    private void initViews() {
        mNameLayout = (TextInputLayout) findViewById(R.id.inputlayout_login_display_name);
        mEmailLayout = (TextInputLayout) findViewById(R.id.inputlayout_login_email);
        mPasswordLayout = (TextInputLayout) findViewById(R.id.inputlayout_login_pass);
        mEtName = (EditText) findViewById(R.id.et_login_display_name);
        mEtEmail = (EditText) findViewById(R.id.et_login_email);
        mEtPassword = (EditText) findViewById(R.id.et_login_pass);
        mSignInButton = (Button) findViewById(R.id.button_login_sign_in);
        mSignUpButton = (Button) findViewById(R.id.button_login_sign_up);
        mSignInButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
        mProgDialog = new ProgressDialog(this);

    }

    @Override
    public void onClick(View v) {
        if (isInternetConnection()) {
            if (v.getId() == R.id.button_login_sign_in) {
                //sign in
                registerOrLoginUser(false);
            }
            if (v.getId() == R.id.button_login_sign_up) {
                //sign up
                registerOrLoginUser(true);
            } else {
                Log.d(TAG, "onClick: no view id");
            }
        }
    }

    private void startMainActivity() {
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    private boolean isInternetConnection() {
        //now make sure there is internet or else it won't work

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No network connection detected");
            builder.setMessage("You won't be able to sign in until connection is restored");
            builder.setPositiveButton("Check network", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (isConnected) {
                        Snackbar snackbar = Snackbar.make(getCurrentFocus(), "And we're back! Connection restored", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        dialogInterface.dismiss();
                    } else {
                        Snackbar snackbar = Snackbar.make(getCurrentFocus(), "Still no luck, try again later", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        dialogInterface.dismiss();
                    }
                }
            });
            builder.setNegativeButton("k", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }
        return true;
    }

    public void assignDBRefs() {
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mCurrentUser != null) {
            mUID = mCurrentUser.getUid();
            mIsUserSignedIn = true;
        } else {
            mIsUserSignedIn = false;
        }

//        mRootRef = FirebaseDatabase.getInstance().getReference().child(FirebaseHelper.USERS).child(mUID);
//
//        mPepTalkRef = FirebaseDatabase.getInstance().getReference().child(FirebaseHelper.USERS).child(mUID).child(FirebaseHelper.PEPTALKS);
//
//        mChecklistRef = FirebaseDatabase.getInstance().getReference().child(FirebaseHelper.USERS).child(mUID).child(FirebaseHelper.CHECKLIST);
    }
}
