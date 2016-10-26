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
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.presenters.FirebaseHelper;
import owlslubic.peptalkapp.presenters.FragmentMethods;
import owlslubic.peptalkapp.views.fragments.NewFrag;

import static owlslubic.peptalkapp.presenters.FragmentMethods.NEW_FRAG_TAG;
import static owlslubic.peptalkapp.presenters.FragmentMethods.RECYCLERVIEW_FRAG_TAG;
import static owlslubic.peptalkapp.presenters.FragmentMethods.VIEW_FRAG_TAG;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, NewFrag.FABCoordinatorNewFrag {
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9;
    private static final String PREFS = "prefs";
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
//        //TODO where can this live that it won't make stuff crash?
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
//        ref.keepSynced(true);


        initViews();
        checkNetworkStatus();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            setIsLogoutVisible(true);
        } else {
            setIsLogoutVisible(false);
        }


    }


    /**
     * views stuffs
     */
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
            mLaunchFragMain.setText(R.string.signup_or_login);
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
        mSigninTextView = (TextView) headerView.findViewById(R.id.navheader_signin);
        mSigninTextView.setOnClickListener(this);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            //already signed in, so set text to sign out
//            mSigninTextView.setText(R.string.sign_out);
//            mSigninPromptTextView.setText("");
            mWelcomeTextView.setText(getString(R.string.welcome_back_user) +
                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        } else {
            //needs to sign in, set text to sign in
//            mSigninTextView.setText(R.string.sign_in);
//            mSigninPromptTextView.setText(R.string.sign_in_prompt);
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

//    public void setupFrag(int id) {
//        FragmentManager manager = getSupportFragmentManager();
//        FragmentTransaction transaction = manager.beginTransaction();
//        if (id == R.id.textview_main) {
//            RecyclerViewFrag fragment = new RecyclerViewFrag();
//            transaction.add(R.id.framelayout_main_frag_container, fragment, FragmentMethods.RECYCLERVIEW_FRAG_TAG);
//        } else {
//            NewFrag fragment = new NewFrag();
//            transaction.add(R.id.framelayout_main_frag_container, fragment, FragmentMethods.NEW_FRAG_TAG);
//        }
//        transaction.commit();
//    }


    /**
     * for logging in
     */
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


    /**
     * toolbar menu items
     */
    @Override
    public void onBackPressed() {
        Fragment newFrag = getSupportFragmentManager().findFragmentByTag(FragmentMethods.NEW_FRAG_TAG);
        Fragment recyclerFrag = getSupportFragmentManager().findFragmentByTag(FragmentMethods.RECYCLERVIEW_FRAG_TAG);
        Fragment viewFrag = getSupportFragmentManager().findFragmentByTag(FragmentMethods.VIEW_FRAG_TAG);

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else if (newFrag != null && newFrag.isVisible()) {
            FragmentMethods.addFragToBackStack(this, NEW_FRAG_TAG);
        } else if (recyclerFrag != null && recyclerFrag.isVisible()) {
            FragmentMethods.detachFragment(this, RECYCLERVIEW_FRAG_TAG);
        } else if (viewFrag != null && viewFrag.isVisible()) {
            FragmentMethods.addFragToBackStack(this, VIEW_FRAG_TAG);
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
                launchAddWidgetTextDialog();
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


    /**
     * nav drawer stuff
     */

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
        } else if (id == R.id.nav_logout) {
            myLogoutMethod();
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


    /**
     * launch external social apps for "friend reachout"
     */
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


    /**
     * content
     */
    private void insertContentOnNewAccountCreated() {
        //shared prefs to make sure this only runs one time
        boolean b;
        SharedPreferences mPrefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        b = mPrefs.getBoolean("FIRST_RUN", false);
        if (!b) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseHelper.writeNewChecklist(getString(R.string.checklist_water), getString(R.string.checklist_water_notes), this, true); //TODO CHECK IF USING this AS CONTEXT MAKES IT NOT WORK???
                FirebaseHelper.writeNewChecklist(getString(R.string.checklist_eat), getString(R.string.checklist_eat_notes), this, true);
                FirebaseHelper.writeNewChecklist(getString(R.string.checklist_move), getString(R.string.checklist_move_notes), this, true);
                FirebaseHelper.writeNewChecklist(getString(R.string.checklist_moment), getString(R.string.checklist_moment_notes), this, true);
                FirebaseHelper.writeNewChecklist(getString(R.string.checklist_breathe), getString(R.string.checklist_breathe_notes), this, true);
                FirebaseHelper.writeNewChecklist(getString(R.string.checklist_locations), getString(R.string.checklist_locations_notes), this, true);

                FirebaseHelper.writeNewPeptalk(getString(R.string.pep_past_present_title), getString(R.string.pep_past_present), this, true);
                FirebaseHelper.writeNewPeptalk(getString(R.string.pep_facts_emotions_title), getString(R.string.pep_facts_emotions), this, true);
                FirebaseHelper.writeNewPeptalk(getString(R.string.pep_do_your_best_title), getString(R.string.pep_do_your_best), this, true);
                FirebaseHelper.writeNewPeptalk(getString(R.string.live_in_the_moment_title), getString(R.string.live_in_the_moment), this, true);
                FirebaseHelper.writeNewPeptalk(getString(R.string.doing_and_not_doing_title), getString(R.string.doing_and_not_doing), this, true);
                FirebaseHelper.writeNewPeptalk(getString(R.string.exercise_guilt_title), getString(R.string.exercise_guilt), this, true);
            }

            mPrefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.commit();
        }
        Log.d(TAG, "insertContentOnNewAccountCreated: sharedprefs first run is: " + mPrefs.getBoolean("FIRST_RUN", false));
    }


    /**
     * can't produce plays with internettie
     */
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
        }
    }


    /**
     * the beautiful switch statement
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fablet_checklist:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FragmentMethods.setupNewFrag(NewFrag.CHECKLIST, this);

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
                    FragmentMethods.setupNewFrag(NewFrag.PEPTALKS, MainActivity.this);
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
                    FragmentMethods.setupMainActivityFrag(R.id.textview_main, this);
                } else {
                    myLoginMethod();
                }
                break;
            case R.id.navheader_signin:
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {

                    Snackbar snackbar = Snackbar.make(view.getRootView().findViewById(R.id.coordinator_layout_main_activity), "hey friend", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    //TODO this will be where the user accesses profile stuffs
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
//                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
//                intent.putExtra("url", "https://www.headspace.com/");
//                intent.putExtra("title", "Headspace");
//                startActivity(intent);

                //so this launches browser because https doesn't safely load in webview
                Uri uri = Uri.parse("https://www.headspace.com/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.tv_resource2:
                Intent intent2 = new Intent(MainActivity.this, WebViewActivity.class);
                intent2.putExtra("url", "http://www.befriendingourselves.com/Mindfulness.html");
                intent2.putExtra("title", "Befriending Ourselves");
                startActivity(intent2);
//                Uri uri2 = Uri.parse("http://www.befriendingourselves.com/Mindfulness.html");
//                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);
//                startActivity(intent2);
                break;
            case R.id.tv_resource3:
                Intent intent3 = new Intent(MainActivity.this, WebViewActivity.class);
                intent3.putExtra("url", "http://self-compassion.org/");
                intent3.putExtra("title", "Self-Compassion");
                startActivity(intent3);
//                Uri uri3 = Uri.parse("http://self-compassion.org/");
//                Intent intent3 = new Intent(Intent.ACTION_VIEW, uri3);
//                startActivity(intent3);
                break;
            case R.id.tv_resource4:
//                Intent intent4 = new Intent(MainActivity.this, WebViewActivity.class);
//                intent4.putExtra("url", "https://ottawamindfulnessclinic.com");
//                intent4.putExtra("title", "Ottawa Mindfulness Clinic");
//                startActivity(intent4);
                //launching in browser because https doesnt load safely in WebView
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

    public void launchAddWidgetTextDialog() {
        //decided to just add text straight to this because
        // grabbing the text from firebase has proved more complicated than expected

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.dialog_add_widget_text, null);
        builder.setView(layout);
        builder.setNegativeButton("nvm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setPositiveButton("cool", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        final EditText editText = (EditText) dialog.findViewById(R.id.edittext_dialog_widget);


        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String widgetText = editText.getText().toString().trim();

                SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("WIDGET_TEXT", widgetText);
                editor.apply();
                Toast.makeText(MainActivity.this, "Text set to widget", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


    }

    public void setIsLogoutVisible(Boolean b) {
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = nav.getMenu();
        for (int index = 0; index < menu.size(); index++) {
            MenuItem item = menu.getItem(index);
            if (item.getItemId() == R.id.nav_logout) {
                item.setVisible(b);
            }
        }
    }


    public void myLogoutMethod(){
        //by stating in oncreate that it should be visible if logged in and not if not,
        //that'll take care of that. i need to do the actual signing out, and changing the textviews back

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            AuthUI.getInstance().signOut(this);
            Toast.makeText(MainActivity.this, "see ya later!", Toast.LENGTH_SHORT).show();
//                Snackbar snackbar = Snackbar.make(item.getActionView().findViewById(R.id.coordinator_layout_main_activity), "See ya later", Snackbar.LENGTH_LONG);
//                snackbar.show();
            setIsLogoutVisible(false);

            mWelcomeTextView.setText(R.string.welcome_blurb);
            mLaunchFragMain.setText(R.string.signup_or_login);
        }
        else{
            Toast.makeText(MainActivity.this, "you shouldnt be seeing this, because logout shouldn't be visible if you're not logged in!", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void hideFabFromNewFrag() {

    }

    @Override
    public void putFabBackFromNewFrag() {

    }
}



