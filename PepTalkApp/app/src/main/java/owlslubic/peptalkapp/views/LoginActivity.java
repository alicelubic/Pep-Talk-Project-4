package owlslubic.peptalkapp.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import owlslubic.peptalkapp.R;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private TextInputLayout mNameLayout, mEmailLayout, mPasswordLayout;
    private EditText mEtName, mEtEmail, mEtPassword;
    private Button mSignUpButton, mSignInButton;
    private String mDisplayName, mEmail, mPassword;
    private ProgressDialog mProgDialog;
    private FirebaseAuth mAuth;
    private TextView mExistingAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        FirebaseDatabase.getInstance().setLogLevel(Logger.Level.DEBUG);

        mAuth = FirebaseAuth.getInstance();

        //skip this activity if you're still logged in via smartlock
        if (mAuth.getCurrentUser() != null) {
            startMainActivity();
        }

        initViews();


    }

    private boolean isInputValid() {

        mDisplayName = mEtName.getText().toString().trim();
        mEmail = mEtEmail.getText().toString().trim();
        mPassword = mEtPassword.getText().toString().trim();

        if(mSignUpButton.getVisibility()==View.VISIBLE){
            if (TextUtils.isEmpty(mDisplayName)) {
                //set error
                mNameLayout.setError("Oh come now, put any name you like!");
                mNameLayout.requestFocus();
                return false;
            }
        }
        if (TextUtils.isEmpty(mEmail)) {
            //TODO include other stipulations for deal with this email badly formatted issue
            mEmailLayout.setError("Please enter valid email address");
            mEmailLayout.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(mPassword)) {
            mPasswordLayout.setError("Please enter a password");
            mPasswordLayout.requestFocus();
            return false;
        } else if (mPassword.length() < 6) {
            mPasswordLayout.setError("Password must be more than 6 characters");
            mPasswordLayout.requestFocus();
            return false;
        } else

            return true;
    }



    private void registerUser() {
        if (isInternetConnection()) {
            if (isInputValid()) {
                showProgDialog("Registering...");

                mAuth.createUserWithEmailAndPassword(mEmail, mPassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "create new user was successful");
                                    addDisplayName();
                                    //take them to main act
                                    startMainActivity();
                                }
                                else {
                                    //TODO let the user know this didn't work... and what to do next)
                                    //TODO also stipulate that if they already have an accoount, they gotta log in
                                    Toast.makeText(LoginActivity.this, "bad news...", Toast.LENGTH_SHORT).show();
                                    Log.e(TAG, "exception=" + task.getException().toString());
                                }
                                mProgDialog.dismiss();
                            }
                        });


            }
        }
    }

    private void loginUser() {
        if (isInternetConnection()) {
            if (isInputValid()) {
                showProgDialog("Logging in...");
                mAuth.signInWithEmailAndPassword(mEmail, mPassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
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
    }


    public void showProgDialog(String message) {
        mProgDialog.setMessage(message);
        mProgDialog.show();

    }


    private void initViews() {
        TextView welcomeText = (TextView) findViewById(R.id.tv_login_welcome);
        welcomeText.requestFocus();

        mNameLayout = (TextInputLayout) findViewById(R.id.inputlayout_login_display_name);
        mNameLayout.setErrorEnabled(true);
        mEmailLayout = (TextInputLayout) findViewById(R.id.inputlayout_login_email);
        mEmailLayout.setErrorEnabled(true);
        mPasswordLayout = (TextInputLayout) findViewById(R.id.inputlayout_login_pass);
        mPasswordLayout.setErrorEnabled(true);

        mEtName = (EditText) findViewById(R.id.et_login_display_name);
        mEtName.setTextColor(getResources().getColor(R.color.white));
        mEtName.setCursorVisible(true);
        mEtEmail = (EditText) findViewById(R.id.et_login_email);
        mEtEmail.setTextColor(getResources().getColor(R.color.white));
        mEtEmail.setCursorVisible(true);
        mEtPassword = (EditText) findViewById(R.id.et_login_pass);
        mEtPassword.setTextColor(getResources().getColor(R.color.white));
        mEtPassword.setCursorVisible(true);

        mSignUpButton = (Button) findViewById(R.id.button_login_sign_up);
        mSignUpButton.setOnClickListener(this);

        mSignInButton = (Button) findViewById(R.id.button_login_sign_in);
        mSignInButton.setOnClickListener(this);

        mProgDialog = new ProgressDialog(this);

        mExistingAccount = (TextView) findViewById(R.id.textview_login_alreadyhaveanaccount);
        mExistingAccount.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (isInternetConnection()) {
            switch (v.getId()) {
                case R.id.button_login_sign_up:
                    registerUser();
                    break;
                case R.id.button_login_sign_in:
                    loginUser();
                    break;
                case R.id.textview_login_alreadyhaveanaccount:
                    if (mSignUpButton.getVisibility() == View.VISIBLE) {
                        //if the sign UP button is visible when clicked,
                        //change the layout for LOGIN
                        mNameLayout.setVisibility(View.GONE);
                        mEtName.setImeOptions(EditorInfo.IME_ACTION_NONE);
                        mSignUpButton.setVisibility(View.GONE);
                        mSignInButton.setVisibility(View.VISIBLE);
                        mExistingAccount.setText(R.string.need_an_account);
                    } else {
                        //if sign up is not visible, then login must be, so
                        //change the layout for SIGN UP
                        mNameLayout.setVisibility(View.VISIBLE);
                        mEtName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                        mSignInButton.setVisibility(View.GONE);
                        mSignUpButton.setVisibility(View.VISIBLE);
                        mExistingAccount.setText(R.string.already_have_account);
                    }
                    break;

            }

        }
    }

    private void startMainActivity() {
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("Display Name", mDisplayName);
        startActivity(intent);
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

    private void addDisplayName() {
        //also save this to shared prefs for now
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("display name", mDisplayName);
        editor.commit();

        FirebaseUser user = mAuth.getCurrentUser();

        UserProfileChangeRequest updateName = new UserProfileChangeRequest.Builder()
                .setDisplayName(mDisplayName)
                .build();
        Log.d(TAG, "setDisplayName: mDisplayName = " + mDisplayName);

        if (user != null) {
            user.updateProfile(updateName)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User display name updated.");
                            } else {
                                Log.d(TAG, "User display name update failed, error: " + task.getException().getMessage());
                            }
                        }
                    });
        }




    }


}
