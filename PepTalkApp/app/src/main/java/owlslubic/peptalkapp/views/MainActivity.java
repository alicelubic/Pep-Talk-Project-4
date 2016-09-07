package owlslubic.peptalkapp.views;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.presenters.CustomPagerAdapter;
import owlslubic.peptalkapp.presenters.OnSwipeTouchListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9;

    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView mBottomSheetHeading, mBottomSheetTopText, mBottomSheetBottomText, mPepTalkTextView, mWelcomeTextView, mSigninPromptTextView;
    private FloatingActionsMenu mFabMenu;
    private Button mSignInOrOutButton;
    private Toolbar mToolbar;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    private View mHeaderView;
    private DrawerLayout mDrawer;
    private FrameLayout mFrameLayout;
    private DatabaseReference mDbRef;


    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;
    private CustomPagerAdapter mCustomPagerAdapter;
    private List<PepTalkObject> mPepTalkList;

    @Override
    protected void onStart() {
        super.onStart();

        //to handle offline usage - disk persistence
        //TODO find where this can live
        //Calls to setPersistenceEnabled() must be made before any other usage of FirebaseDatabase instance.
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();


        //temp buttons
        Button button1 = (Button) findViewById(R.id.tempbutton_fb);
        Button button2 = (Button) findViewById(R.id.tempbutton_gmail);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchDefaultSMS();
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchDefaultEmail();
            }
        });


    }




    public void myLoginMethod() {
        //first check if the user is already signed in
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in, so send them on their way
            Toast.makeText(MainActivity.this, "Already signed in, " + auth.getCurrentUser().getDisplayName(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "check if signed in: USER IS ALREADY SIGNED IN");
//            finish();
        } else {
            // not signed in
            Log.d(TAG, "check if signed in: USER IS NOT SIGNED IN, COMMENCING GOOGLE SIGN IN");

            //for sign in
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setProviders(
//                            AuthUI.GOOGLE_PROVIDER,//GOOGLE DOESNT WORK THATS SAD
                            AuthUI.EMAIL_PROVIDER)
                    .setIsSmartLockEnabled(true)
//                    .setTheme(R.style)
                    .build(), RC_SIGN_IN);


        }


    }

    //handling the sign-in result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                Log.d(TAG, "onActivityResult: user is signed in");
                startActivity(new Intent(this, MainActivity.class));
//                finish();

            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
                Log.d(TAG, "onActivityResult: user is not signed in");
                Toast.makeText(MainActivity.this, "sign in failed. try again yo", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fablet_checklist:
                CustomDialog.launchNewChecklistDialog(this);
                mFrameLayout.getBackground().setAlpha(0);
                mFrameLayout.setOnTouchListener(null);
                mFabMenu.collapse();
                break;
            case R.id.fablet_peptalk:
                CustomDialog.launchNewPeptalkDialog(this);
                mFrameLayout.getBackground().setAlpha(0);
                mFrameLayout.setOnTouchListener(null);
                mFabMenu.collapse();
                break;
            case R.id.textview_main_circular:
                //launch pep talk view

                //temporarily serving as my log in stuff
                myLoginMethod();

                //not sure if this makes the viewpager appear or?

//                mViewPager.setAdapter(mCustomPagerAdapter);

                break;
            case R.id.button_navheader_signin:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    //already signed in, so sign em out
                    AuthUI.getInstance().signOut(this);
                    //so we sign out, and then change the display for signing back in
                    mSignInOrOutButton.setText(R.string.sign_in);
                    mSigninPromptTextView.setText(R.string.sign_in_prompt);
                    mWelcomeTextView.setText(R.string.welcome_blurb);

                } else {
                    //needs to sign in
                    myLoginMethod();
                }
                break;


        }

    }


    //making a method to do this so the oncreate stays clean and pretty aww
    private void initViews() {

        //fab and fablet business
        mFrameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        mFrameLayout.getBackground().setAlpha(0);
        mFabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        mFabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                mFrameLayout.getBackground().setAlpha(140);
                mFrameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mFabMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                mFrameLayout.getBackground().setAlpha(0);
                mFrameLayout.setOnTouchListener(null);
            }
        });


        com.getbase.floatingactionbutton.FloatingActionButton fabNewChecklist = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fablet_checklist);
        com.getbase.floatingactionbutton.FloatingActionButton fabNewPeptalk = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fablet_peptalk);
        fabNewChecklist.setOnClickListener(this);
        fabNewPeptalk.setOnClickListener(this);


        mPepTalkTextView = (TextView) findViewById(R.id.textview_main_circular);
        mPepTalkTextView.setOnClickListener(this);


        //TODO fuck with the toolbar here
        //toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        //nav drawer setup
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(mToggle);
        mToggle.syncState();
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);

        mHeaderView = mNavigationView.getHeaderView(0);
        mWelcomeTextView = (TextView) mHeaderView.findViewById(R.id.textview_navheader_welcome);
        mSigninPromptTextView = (TextView) mHeaderView.findViewById(R.id.textview_navheader_sign_in);
        mSignInOrOutButton = (Button) mHeaderView.findViewById(R.id.button_navheader_signin);

        mSignInOrOutButton.setOnClickListener(this);
        Log.d(TAG, "SIGN IN BUTTON LISTENER HAS BEEN SET, button says: " + mSignInOrOutButton.getText());
        //not sure if this can live here because will it be updated accordingly? should be, since oncreate will be called before and after the login screen comes up
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            //already signed in, so set text to sign out
            //TODO the below stuff might need to happen in the button onclick since the login screen doesnt open so oncreate is not called
            mSignInOrOutButton.setText(R.string.sign_out);
            mSigninPromptTextView.setText("");
            mWelcomeTextView.setText(getString(R.string.welcome_back_user) +
                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            //TODO fix that concatenation

        } else {
            //needs to sign in, set text to sign in
            mSignInOrOutButton.setText(R.string.sign_in);
            mSigninPromptTextView.setText(R.string.sign_in_prompt);
            mWelcomeTextView.setText(R.string.welcome_blurb);
        }


        //bottom sheet
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetHeading = (TextView) findViewById(R.id.textview_bottomSheetHeading);
        mBottomSheetBottomText = (TextView) findViewById(R.id.textview_bottomsheet_bottom);
        mBottomSheetTopText = (TextView) findViewById(R.id.textview_bottomsheet_top);


        /*      //viewpager stuff i was trying but i knew it was too simple to be true
        //        if(mPepTalkList.isEmpty()){
        mPepTalkList= new ArrayList<>();
//        }
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mCustomPagerAdapter = new CustomPagerAdapter(MainActivity.this,getPepTalks());//this mght null pointer, if it doesn't have the list already?

*/

    }


    //NAV DRAWER MENU options
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
        if (id == R.id.action_settings) {//this is in the overflow menu

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_peptalks) {
            Intent intent = new Intent(MainActivity.this, PepTalkListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_checklist) {
            Intent intent = new Intent(MainActivity.this, ChecklistActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_resources) {
            launchBottomSheetFromNav();

            mBottomSheetHeading.setText(R.string.more_resources_heading);
            mBottomSheetTopText.setText(R.string.more_resources);
            mBottomSheetBottomText.setText("");

        } else if (id == R.id.nav_instructions) {
            launchBottomSheetFromNav();

            mBottomSheetHeading.setText(R.string.instuctions_heading);
            mBottomSheetTopText.setText(R.string.instructions);
            mBottomSheetBottomText.setText("");


        } else if (id == R.id.nav_about) {
            launchBottomSheetFromNav();

            mBottomSheetHeading.setText(R.string.about_heading);
            mBottomSheetTopText.setText(R.string.about);
            mBottomSheetBottomText.setText("");

        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //works
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




    //launch external apps
    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean appInstalled;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            appInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            appInstalled = false;
        }
        return appInstalled;
    }

    public void launchDefaultEmail() {
        boolean installed = appInstalledOrNot("com.google.android.gm");
        if (installed) {
            Intent emailLauncher = new Intent(Intent.ACTION_VIEW);
            emailLauncher.setType("message/rfc822");
            startActivity(emailLauncher);
        } else {
            Toast.makeText(MainActivity.this, "Gmail is not currently installed on your device", Toast.LENGTH_SHORT).show();
        }

    }

    public void launchFacebook() {
        boolean installed = appInstalledOrNot("com.facebook.katana");
        if (installed) {
            Intent launchFb = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
            startActivity(launchFb);
        } else {
            Toast.makeText(MainActivity.this, "Facebook is not currently installed on your device", Toast.LENGTH_SHORT).show();
        }
    }

    public void launchDefaultSMS() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
    }



/*

    //list to feed to viewpager adapter
    public List<PepTalkObject> getPepTalks(){
        mPepTalkList.add(new PepTalkObject("","test title","test body", false));

        //the below will add updates but uh how to get existing ones?

        mDbRef.child("PepTalks").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mPepTalkList.add(dataSnapshot.getValue(PepTalkObject.class));
                Log.d(TAG, "GET PEPTALKS onChildAdded: pep talk list size: "+ mPepTalkList.size());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mPepTalkList.remove(dataSnapshot.getValue(PepTalkObject.class));
                Log.d(TAG, "GET PEPTALKS onChildRemoved: removed peptalklist size: "+ mPepTalkList.size());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

      trying the above method instead
        mDbRef.child("PepTalks").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    mPepTalkList.add(snapshot.getValue(PepTalkObject.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//
//       return mPepTalkList;
//   }







public void setupFrag(){
    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();

    mDbRef = FirebaseDatabase.getInstance().getReference();
    //get the pep talks from the db and set it to this frag,
    // then when I replace it on swipe, i get the next firebase bit

//
//    MyFragment fragment = MyFragment.newInstance();
//    transaction.add(R.id.framelayout_main_frag_container, fragment);
//    transaction.commit();



//    upon swipe:
    getSupportFragmentManager().beginTransaction()
            .replace(R.id.framelayout_main_frag_container, new MyFragment())//we are replacing, not adding to the backstack. so if we hit back, it will just go away
            .commit();


           ///












*/


}






