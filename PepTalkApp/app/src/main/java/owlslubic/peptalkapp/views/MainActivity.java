package owlslubic.peptalkapp.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.models.PepTalkObject;
import owlslubic.peptalkapp.views.fragments.EditFrag;
import owlslubic.peptalkapp.views.fragments.NewFrag;
import owlslubic.peptalkapp.views.fragments.RecyclerViewFrag;

import static owlslubic.peptalkapp.views.CustomDialog.*;
public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView mBottomSheetHeading, mBottomSheetTopText, mWelcomeTextView,
            mSigninPromptTextView, mSigninTextView, mResource1, mResource2, mResource3, mResource4, mLaunchFragMain;
    private FloatingActionsMenu mFabMenu;
    private DrawerLayout mDrawer;
    private FrameLayout mFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to handle offline usage - disk persistence
        //TODO where can this live that it won't make stuff crash?
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        initViews();
        checkNetworkStatus();
    }


    /**views stuffs*/
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


        //for launching our lil peptalks
        mLaunchFragMain = (TextView) findViewById(R.id.textview_main);
        mLaunchFragMain.setOnClickListener(this);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            mLaunchFragMain.setText(R.string.need_a_pep_talk);
        } else {
            mLaunchFragMain.setText("");
        }


        //TODO fuck with the toolbar here
        //toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }


        //nav drawer setup
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //nav login header
        View headerView = navigationView.getHeaderView(0);
        mWelcomeTextView = (TextView) headerView.findViewById(R.id.textview_navheader_welcome);
        mSigninPromptTextView = (TextView) headerView.findViewById(R.id.textview_navheader_signin);
        mSigninTextView = (TextView) headerView.findViewById(R.id.navheader_signin);
        mSigninTextView.setOnClickListener(this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            //already signed in, so set text to sign out
            mSigninTextView.setText(R.string.sign_out);
            mSigninPromptTextView.setText("");
            mWelcomeTextView.setText(getString(R.string.welcome_back_user) +
                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        } else {
            //needs to sign in, set text to sign in
            mSigninTextView.setText(R.string.sign_in);
            mSigninPromptTextView.setText(R.string.sign_in_prompt);
            mWelcomeTextView.setText(R.string.welcome_blurb);
        }
        //nav bottom imagebuttons
        ImageButton sms = (ImageButton) findViewById(R.id.imagebutton_sms);
        sms.setOnClickListener(this);
        ImageButton emailButton = (ImageButton) findViewById(R.id.imagebutton_email);
        emailButton.setOnClickListener(this);
        ImageButton fbButton = (ImageButton) findViewById(R.id.imagebutton_fb);
        fbButton.setOnClickListener(this);


        //bottom sheet
        ImageButton down = (ImageButton) findViewById(R.id.imagebutton_bottomsheet_down);
        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayout));
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        mBottomSheetHeading = (TextView) findViewById(R.id.textview_bottomSheetHeading);
        mBottomSheetTopText = (TextView) findViewById(R.id.textview_bottomsheet_top);
        mResource1 = (TextView) findViewById(R.id.tv_resource1);
        mResource1.setOnClickListener(this);
        mResource2 = (TextView) findViewById(R.id.tv_resource2);
        mResource2.setOnClickListener(this);
        mResource3 = (TextView) findViewById(R.id.tv_resource3);
        mResource3.setOnClickListener(this);
        mResource4 = (TextView) findViewById(R.id.tv_resource4);
        mResource4.setOnClickListener(this);
        down.setOnClickListener(this);


    }
    public void setupFrag(int id) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (id == R.id.textview_main) {
            RecyclerViewFrag fragment = new RecyclerViewFrag();
            transaction.add(R.id.framelayout_main_frag_container, fragment);
        } else {
            NewFrag fragment = new NewFrag();
            transaction.add(R.id.framelayout_main_frag_container, fragment);
        }
        transaction.commit();
    }


    /**for logging in*/
    public void myLoginMethod() {
        //first check if the user is already signed in
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in, so send them on their way
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout_main_activity), "You're already signed in", Snackbar.LENGTH_SHORT);
            snackbar.show();
        } else {
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
                builder.setPositiveButton("Check network", new OnClickListener() {
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
                builder.setNegativeButton("k", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                final AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // yes internet, but not already signed in, so let's sign em in or up!
                startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setProviders(
//                            AuthUI.GOOGLE_PROVIDER,//GOOGLE DOESNT WORK THATS SAD
                                AuthUI.EMAIL_PROVIDER)
                        .setIsSmartLockEnabled(true)
                        .setTheme(R.style.AppTheme)
                        .build(), RC_SIGN_IN);
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in! populate account with peptalks if we haven't already (thanks shared prefs)
                //TODO revisit this
                /** since i'm using firebaseui library, i can't figure out how to access the specific methods like onAccoutnCreated
                 * which is ideally where this would go, so this is my roundabout solution
                 * although if you uninstall and reinstall, it resets the sharedprefs and adds the content again */
                insertContentOnNewAccountCreated();
                startActivity(new Intent(this, MainActivity.class));
                mLaunchFragMain.setText(R.string.need_a_pep_talk);
            } else {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout_main_activity), "Oops! Sign in failed", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
    }


    /**toolbar menu items*/
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
        int id = item.getItemId();
        if (id == R.id.widget) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                CustomDialog.launchAddWidgetTextDialog(this);
            } else {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout_main_activity), "Please sign in to mess with your widget", Snackbar.LENGTH_SHORT);
                snackbar.setAction("sign in", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myLoginMethod();
                    }
                }).show();
            }
            return true;

        }
        return super.onOptionsItemSelected(item);
    }


    /**nav drawer stuff*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_peptalks) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Intent intent = new Intent(MainActivity.this, PepTalkListActivity.class);
                startActivity(intent);
            } else {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout_main_activity), "Please sign in to view pep talks", Snackbar.LENGTH_SHORT);
                snackbar.setAction("sign in", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myLoginMethod();
                    }
                }).show();
            }
        } else if (id == R.id.nav_checklist) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Intent intent = new Intent(MainActivity.this, ChecklistActivity.class);
                startActivity(intent);
            } else {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout_main_activity), "Please sign in to view checklist", Snackbar.LENGTH_SHORT);
                snackbar.setAction("sign in", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myLoginMethod();
                    }
                }).show();
            }
        } else if (id == R.id.nav_resources) {
            launchBottomSheetFromNav();
            mBottomSheetHeading.setText(R.string.more_resources_heading);
            mBottomSheetTopText.setText(R.string.more_resources_text);
            mResource1.setVisibility(View.VISIBLE);
            mResource2.setVisibility(View.VISIBLE);
            mResource3.setVisibility(View.VISIBLE);
            mResource4.setVisibility(View.VISIBLE);
            mResource1.setText(R.string.resource_headspace);
            mResource2.setText(R.string.resource_befriending);
            mResource3.setText(R.string.resource_selfcompassion);
            mResource4.setText(R.string.resource_mindfulness);
        } else if (id == R.id.nav_instructions) {
            launchBottomSheetFromNav();
            mBottomSheetHeading.setText(R.string.instuctions_heading);
            mBottomSheetTopText.setText(R.string.instructions_text);
            mResource1.setVisibility(View.INVISIBLE);
            mResource2.setVisibility(View.INVISIBLE);
            mResource3.setVisibility(View.INVISIBLE);
            mResource4.setVisibility(View.INVISIBLE);
        } else if (id == R.id.nav_about) {
            launchBottomSheetFromNav();
            mBottomSheetHeading.setText(R.string.about_heading);
            mBottomSheetTopText.setText(R.string.about_text);
            mResource1.setVisibility(View.INVISIBLE);
            mResource2.setVisibility(View.INVISIBLE);
            mResource3.setVisibility(View.INVISIBLE);
            mResource4.setVisibility(View.INVISIBLE);
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void launchBottomSheetFromNav() {
        //first close nav drawer
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        }
        //then open bottomsheet
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }


    /**launch external social apps for "friend reachout"*/
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
        Intent emailLauncher = new Intent(Intent.ACTION_VIEW);
        emailLauncher.addCategory(Intent.CATEGORY_DEFAULT);
        emailLauncher.setType("message/rfc822");
        startActivity(emailLauncher);
    }
    public void launchFacebook() {
        boolean installed = appInstalledOrNot("com.facebook.katana");
        if (installed) {
            Intent launchFb = getPackageManager().getLaunchIntentForPackage("com.facebook.katana");
            startActivity(launchFb);
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout_main_activity), "Facebook is not currently installed on your device", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }
    public void launchDefaultSMS() {
        Intent smsLauncher = new Intent(Intent.ACTION_MAIN);
        smsLauncher.addCategory(Intent.CATEGORY_DEFAULT);
        smsLauncher.setType("vnd.android-dir/mms-sms");
        startActivity(smsLauncher);
    }


    /**content*/
    private void insertContentOnNewAccountCreated() {
        //shared prefs to make sure this only runs one time
        boolean b;
        SharedPreferences mPrefs = getSharedPreferences("PREFS_NAME", 0);
        b = mPrefs.getBoolean("FIRST_RUN", false);
        if (!b) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                NewFrag.writeNewChecklist(getString(R.string.checklist_water), getString(R.string.checklist_water_notes));
                NewFrag.writeNewChecklist(getString(R.string.checklist_eat), getString(R.string.checklist_eat_notes));
                NewFrag.writeNewChecklist(getString(R.string.checklist_move), getString(R.string.checklist_move_notes));
                NewFrag.writeNewChecklist(getString(R.string.checklist_moment), getString(R.string.checklist_moment_notes));
                NewFrag.writeNewChecklist(getString(R.string.checklist_breathe), getString(R.string.checklist_breathe_notes));
                NewFrag.writeNewChecklist(getString(R.string.checklist_locations), getString(R.string.checklist_locations_notes));

                NewFrag.writeNewPeptalk(getString(R.string.pep_past_present_title), getString(R.string.pep_past_present));
                NewFrag.writeNewPeptalk(getString(R.string.pep_facts_emotions_title), getString(R.string.pep_facts_emotions));
                NewFrag.writeNewPeptalk(getString(R.string.pep_do_your_best_title), getString(R.string.pep_do_your_best));
                NewFrag.writeNewPeptalk(getString(R.string.live_in_the_moment_title), getString(R.string.live_in_the_moment));
                NewFrag.writeNewPeptalk(getString(R.string.doing_and_not_doing_title), getString(R.string.doing_and_not_doing));
                NewFrag.writeNewPeptalk(getString(R.string.exercise_guilt_title), getString(R.string.exercise_guilt));

            }

            mPrefs = getSharedPreferences("PREFS_NAME", 0);
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();
        }
        Log.d(TAG, "insertContentOnNewAccountCreated: sharedprefs first run is: " + mPrefs.getBoolean("FIRST_RUN", false));
    }


    /** can't produce plays with internettie*/
    public void checkNetworkStatus() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("No network connection detected");
            builder.setMessage("You may not be able to access your pep talks from our database until connection is restored");
            builder.setPositiveButton("Check network", new OnClickListener() {
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
            builder.setNegativeButton("k", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    /**the beautiful switch statement*/
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fablet_checklist:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//                    launchNewChecklistDialog(this);

                    setupFrag(R.id.fablet_checklist);

                    mFrameLayout.getBackground().setAlpha(0);
                    mFrameLayout.setOnTouchListener(null);

                    mFabMenu.collapse();
                } else {
                    Snackbar snackbar = Snackbar.make(view.getRootView().findViewById(R.id.coordinator_layout_main_activity), "Please sign in to add to checklist", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("sign in", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myLoginMethod();
                        }
                    }).show();
                }
                break;
            case R.id.fablet_peptalk:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//                    launchNewPeptalkDialog(this);
                    setupFrag(R.id.fablet_peptalk);

                    mFrameLayout.getBackground().setAlpha(0);
                    mFrameLayout.setOnTouchListener(null);
                    mFabMenu.collapse();
                } else {
                    Snackbar snackbar = Snackbar.make(view, "Please sign in to add a peptalk", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("sign in", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myLoginMethod();
                        }
                    }).show();
                }
                break;
            case R.id.textview_main:
                //launch pep talk view
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    setupFrag(R.id.textview_main);
                } else {
                    Snackbar snackbar = Snackbar.make(view, "Please sign in to view your peptalks", Snackbar.LENGTH_SHORT);
                    snackbar.setAction("sign in", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myLoginMethod();
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout_main_activity), "Please sign in to add to checklist", Snackbar.LENGTH_SHORT);
                            snackbar.setAction("sign in", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    myLoginMethod();
                                }
                            }).show();
                        }
                    }).show();
                }
                break;
            case R.id.navheader_signin:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    //already signed in, so sign em out
                    AuthUI.getInstance().signOut(this);
                    Snackbar snackbar = Snackbar.make(view.getRootView().findViewById(R.id.coordinator_layout_main_activity), "See ya later", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    //so we sign out, and then change the display for signing back in
                    mLaunchFragMain.setText("");
                    mSigninTextView.setText(R.string.sign_in);
                    mSigninPromptTextView.setText(R.string.sign_in_prompt);
                    mWelcomeTextView.setText(R.string.welcome_blurb);

                } else {
                    myLoginMethod();
                }
                break;
            case R.id.imagebutton_sms:
                launchDefaultSMS();
                break;
            case R.id.imagebutton_email:
                launchDefaultEmail();
                break;
            case R.id.imagebutton_fb:
                launchFacebook();
                break;
            case R.id.tv_resource1:
                //webview didnt wanna work
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("url", "https://www.headspace.com/");
                startActivity(intent);

                //so this launches browser instead
//                Uri uri = Uri.parse("https://www.headspace.com/");
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
                break;
            case R.id.tv_resource2:
//                Intent intent2 = new Intent(MainActivity.this, WebViewActivity.class);
//                intent2.putExtra("url", "http://www.befriendingourselves.com/Mindfulness.html");
//                startActivity(intent2);
                Uri uri2 = Uri.parse("http://www.befriendingourselves.com/Mindfulness.html");
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);
                startActivity(intent2);
                break;
            case R.id.tv_resource3:
//                Intent intent3 = new Intent(MainActivity.this, WebViewActivity.class);
//                intent3.putExtra("url", "http://self-compassion.org/");
//                startActivity(intent3);
                Uri uri3 = Uri.parse("http://self-compassion.org/");
                Intent intent3 = new Intent(Intent.ACTION_VIEW, uri3);
                startActivity(intent3);
                break;
            case R.id.tv_resource4:
                Uri uri4 = Uri.parse("https://ottawamindfulnessclinic.com");
                Intent intent4 = new Intent(Intent.ACTION_VIEW, uri4);
                startActivity(intent4);
                break;
            case R.id.imagebutton_bottomsheet_down:
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;
        }

    }


}

