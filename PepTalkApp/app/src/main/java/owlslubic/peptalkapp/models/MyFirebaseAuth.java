package owlslubic.peptalkapp.models;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by owlslubic on 9/4/16.
 */
public class MyFirebaseAuth {


    private static final String TAG = "MyFirebaseAuth";
    private static FirebaseAuth mAuth;//making this static because the dialog needs it to be in order to use the create method
    private FirebaseAuth.AuthStateListener mAuthListener;


    private void setUpFirebaseAuth() {
        //firebase auth setup - may this go in the nav drawer? should i do a different activity

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //user is signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());
                } else {
                    //user is signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
            }
        };


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
    private void signOut() {
        //SIGN OUT: TODO associate this with a button.... obviously
        FirebaseAuth.getInstance().signOut();
    }


    //creating new account has a working method in the SignUpActivity
    private void createNewAccount(String email, String password) {//maybe include String firstName
        //maybe use the nav drawer header as a sign up/sign in feature, once logged in it just welcomes you by name?
//        maybe have it so the sign in views then become invisible and the LAUNCH PEPTALKS button becomes visible? maybe some animation transition....?

        //temp sign up stuff - MOVING THIS TO A DIALOG
//
//        final EditText et_email = (EditText) findViewById(R.key.edittext_email);
//        final EditText et_pass = (EditText) findViewById(R.key.edittext_passs);
//        Button b = (Button) findViewById(R.key.button_sign_in);
//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String email = et_email.getText().toString().trim();
//                String password = et_pass.getText().toString().trim();
//                createUserWithEmailAndPassword(email, password);
//
//            }
//        });

    }

    public void createNewUser(String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, password);
/*                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //"if sign in succeeds, the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener."

                        //^not sure what logic they talkin bout but we'll see
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: " + "CREATE USER WAS SUCCESSFUL");
                        }


                        //if sign in fails:
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "onComplete: SIGN UP USER FAILED");
                        }

                    }
                });

*/
    }
}