package owlslubic.peptalkapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import owlslubic.peptalkapp.R;

/**
 * Created by owlslubic on 9/4/16.
 */

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    //dont necessarily need a whole activity for this, but i gotta get it working first so...
    //putting all auth methods in here for simplicity


    private static final String TAG = "SignUpActivity";
    static FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

   /*
        //this auth state stuff might need ot be in main actvity since thats where we need to tell if the user is logged in or out
        //but i'm gonna have to deal with this tomorrow


        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is signed in
//                    finish();//this should return to mainactivity
//                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
//                    startActivity(intent);
                    Log.i(TAG, "onAuthStateChanged: user is signed in: " + user.getEmail());

                } else {

                    //user is signed out
                }

                //update UI?
            }
        };
*/
        final EditText et_email = (EditText) findViewById(R.id.edittext_email);
        final EditText et_pass = (EditText) findViewById(R.id.edittext_passs);
        Button submit = (Button) findViewById(R.id.button_sign_in);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_email.getText().length() == 0 || et_pass.length() == 0) {
                    Log.d(TAG, "onClickSubmit: EMAIL OR PASS TEXTFIELD WAS EMPTY");
                }
                String email = et_email.getText().toString().trim();
                String password = et_pass.getText().toString().trim();
                createUserWithEmailAndPassword(email, password);
            }
        });


    }

    //this method seems to work
    public void createUserWithEmailAndPassword(String email, String password) {
        //maybe include String firstName
        //maybe use the nav drawer header as a sign up/sign in feature, once logged in it just welcomes you by name?
//        maybe have it so the sign in views then become invisible and the LAUNCH PEPTALKS button becomes visible? maybe some animation transition....?

        mAuth = FirebaseAuth.getInstance();

        if (email.length() == 0 || password.length() == 0) {
            Log.d(TAG, "createUserWithEmailAndPassword: EMAIL OR PASSWRD WAS NULL, account not created");
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //"if sign in succeeds, the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener."

                        //^not sure what logic they talkin bout but we'll see
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: " + "CREATE USER WAS SUCCESSFUL");
                            Toast.makeText(SignUpActivity.this, "", Toast.LENGTH_SHORT).show();
                            finish();
                        }


                        //if sign in fails:
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "onComplete: SIGN UP USER FAILED");
                            Toast.makeText(SignUpActivity.this, "Sign Up was unsuccessful", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }





/*

        private void signInExistingAccount(String email, String password){
            //When a user signs in, pass in the user's email address and password:

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //If sign-in succeeded, the AuthStateListener runs the onAuthStateChanged callback.
                            // In the callback, you can use the getCurrentUser method to get the user's account data.


                            Log.d(TAG, "signInWithEmail:onComplete: " + task.isSuccessful());

                            //if sign in fails:
                            Log.d(TAG, "signInWithEmail: failed " + task.getException());
    //                        Toast.makeText(MainActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }



*/



    public boolean isUserLoggedIn(){

        FirebaseUser user = SignUpActivity.mAuth.getCurrentUser();
        if (user != null) {
            //user is logged in
            return true;
        }
        return false;
    }

    public void logUserOut(){
        FirebaseAuth.getInstance().signOut();
        Log.i(TAG, "USER SIGNED OUT");
    }



    //in which activity do these belong?
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        Log.d(TAG, "onStart(): ADDAUTHSTATAELISTENER");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
            Log.d(TAG, "onStop(): REMOVEAUTHSTATELISTNERER");
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sign_in:

        }
    }





}
