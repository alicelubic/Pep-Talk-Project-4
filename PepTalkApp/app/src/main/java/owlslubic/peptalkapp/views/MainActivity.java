package owlslubic.peptalkapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import owlslubic.peptalkapp.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView mBottomSheetHeading, mBottomSheetTopText, mBottomSheetBottomText;
    private Button mShowBottomSheetButton;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initViews();


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


        //CREATE NEW ACCOUNT by passing the new user's email and pass:
        //TODO get together some edittexts or whatever to do the actual sign in.... in whichever activity this ends up livin in
        //TODO maybe have it so the sign in views then become invisible and the LAUNCH PEPTALKS button becomes visible? maybe some animation transition....?
        //temp sign up stuff

        final EditText et_email = (EditText) findViewById(R.id.edittext_email);
        final EditText et_pass = (EditText) findViewById(R.id.edittext_passs);
        Button b = (Button) findViewById(R.id.button_sign_in);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = et_email.getText().toString().trim();
                String password = et_pass.getText().toString().trim();
                createNewUser(email, password);

            }
        });


  /*      //SIGN IN TO EXISTING ACCOUNT:
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
                        Toast.makeText(MainActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                    }
                });

        //SIGN OUT: TODO associate this with a button.... obviously
        FirebaseAuth.getInstance().signOut();


*/


    }


    //nav drawer menu options
    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        /**these bad boys ain't workin**/

        //if it's the About/Intstruct/Resources button clicked, it should set the textviews
        // in it to whichever text is necessary

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_peptalks) {
            Toast.makeText(MainActivity.this, "Pep Talks on the wAy!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, PepTalkListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_checklist) {
            Toast.makeText(MainActivity.this, "check out da checklist!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, ChecklistActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_resources) {
            Toast.makeText(MainActivity.this, "resources", Toast.LENGTH_SHORT).show();

            launchBottomSheetFromNav();

            mBottomSheetHeading.setText("More Resources");
            mBottomSheetTopText.setText("");
            mBottomSheetBottomText.setText("");

        } else if (id == R.id.nav_instructions) {
            Toast.makeText(MainActivity.this, "instructions", Toast.LENGTH_SHORT).show();

            launchBottomSheetFromNav();

            mBottomSheetHeading.setText("Instructions");
            mBottomSheetTopText.setText("");
            mBottomSheetBottomText.setText("");


        } else if (id == R.id.nav_about) {
            Toast.makeText(MainActivity.this, "about!", Toast.LENGTH_SHORT).show();

            launchBottomSheetFromNav();

            mBottomSheetHeading.setText("About");
            mBottomSheetTopText.setText("");
            mBottomSheetBottomText.setText("");

        } else if (id == R.id.nav_logout) {
            Toast.makeText(MainActivity.this, "logout, bye", Toast.LENGTH_SHORT).show();

        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }





/*

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }



*/


    public void createNewUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
                            Toast.makeText(MainActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


//
//    mAuth.createUserWithEmailAndPassword(email, password)
//            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//        @Override
//        public void onComplete(@NonNull Task<AuthResult> task) {
//            //"if sign in succeeds, the auth state listener will be notified and logic to handle the
//            // signed in user can be handled in the listener."
//
//            //^not sure what logic they talkin bout but we'll see
//            Log.d(TAG, "onComplete: " + "CREATE USER WAS SUCCESSFUL");
//
//
//            //if sign in fails:
//            if (!task.isSuccessful()) {
//                Toast.makeText(MainActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    });


    //making a method to do this so the oncreate stays clean and pretty aww
    private void initViews() {
        //temp activity buttons stuff
        Button button = (Button) findViewById(R.id.button_temp1);
        Button button2 = (Button) findViewById(R.id.button_temp2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "check out da checklist!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, ChecklistActivity.class);
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Pep Talks on the wAy!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PepTalkListActivity.class);
                startActivity(intent);
            }
        });

        //nav drawer setup
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //TODO fuck with the toolbar here
        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //bottom sheet
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetHeading = (TextView) findViewById(R.id.textview_bottomSheetHeading);
        mBottomSheetBottomText = (TextView) findViewById(R.id.textview_bottomsheet_bottom);
        mBottomSheetTopText = (TextView) findViewById(R.id.textview_bottomsheet_top);
        mShowBottomSheetButton = (Button) findViewById(R.id.button_temp_bottom_sheet);
        mShowBottomSheetButton.setOnClickListener(this);


        //fab
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);//is this ok to put in here?

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fab:

                break;
            case R.id.button_temp_bottom_sheet://temp
                Toast.makeText(MainActivity.this, "bottom sheets", Toast.LENGTH_SHORT).show();
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;


        }

    }

    private void launchBottomSheetFromNav() {
        //close nav drawer
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }

        //then open bottomsheet
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }



    private void createNewUserAccount() {

    }

}
