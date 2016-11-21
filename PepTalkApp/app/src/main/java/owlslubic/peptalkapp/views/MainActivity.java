package owlslubic.peptalkapp.views;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import owlslubic.peptalkapp.R;
import owlslubic.peptalkapp.presenters.FragmentMethods;
import owlslubic.peptalkapp.views.fragments.NewEditFrag;
import owlslubic.peptalkapp.views.fragments.ViewFrag;

import static owlslubic.peptalkapp.presenters.FragmentMethods.*;
import static owlslubic.peptalkapp.presenters.FirebaseHelper.*;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
        NewEditFrag.FABCoordinatorNewFrag, ViewFrag.FABCoordinatorViewFrag {

    private static final String TAG = "MainActivity";
    private static final String PREFS = "prefs";
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView mBottomSheetHeading, mBottomSheetTopText,
            mResource1, mResource2, mResource3, mResource4;
    private FloatingActionsMenu mFabMenu;
    private DrawerLayout mDrawer;
    private FrameLayout mFrameLayout;
    private FirebaseAuth mAuth;
    private String mDisplayName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO where can this live that it won't make stuff crash?
        //to handle offline usage - disk persistence
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users");
//        ref.keepSynced(true);

        mAuth = FirebaseAuth.getInstance();
        checkNetworkStatus();
        insertContentOnNewAccountCreated();
        initViews();

        mDisplayName = getSharedPreferences("prefs", MODE_PRIVATE).getString("display name", "pal");
        Log.d(TAG, "onCreate: shared pref user name is: "+ mDisplayName);
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

        FloatingActionButton fabNewChecklist = (FloatingActionButton) findViewById(R.id.fablet_checklist);
        FloatingActionButton fabNewPeptalk = (FloatingActionButton) findViewById(R.id.fablet_peptalk);
        fabNewChecklist.setOnClickListener(this);
        fabNewPeptalk.setOnClickListener(this);


        //for launching our lil peptalks from the main screen
        TextView launchPepTalks = (TextView) findViewById(R.id.textview_main);
        launchPepTalks.setOnClickListener(this);
        launchPepTalks.setText(getResources().getString(R.string.need_a_pep_talk));


        TextView userWelcomeMessage = (TextView) findViewById(R.id.textview_main_welcome_user);
        String displayName = mAuth.getCurrentUser().getDisplayName();
        if (displayName == null) {
            /** if the display name has not been set to the FirebaseUser object yet,
             * then use the direct input for now
             * */
            displayName = getIntent().getStringExtra("Display Name");
        }
//        userWelcomeMessage.setText(String.format(getResources().getString(R.string.main_activity_welcome), displayName));
        userWelcomeMessage.setText(String.format(getResources().getString(R.string.main_activity_welcome),
                getSharedPreferences("prefs", MODE_PRIVATE).getString("display name", "pal")));

        userWelcomeMessage.setVisibility(View.VISIBLE);
        Log.d(TAG, "initViews: main welcome text is: "+ userWelcomeMessage.getText().toString());


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


        //nav header
        View headerView = navigationView.getHeaderView(0);
        TextView navWelcome = (TextView) headerView.findViewById(R.id.textview_navheader_welcome);
        String username = mAuth.getCurrentUser().getDisplayName();
        if (username == null) {
            /** if the display name has not been set to the FirebaseUser object yet,
             * then use the direct input for now
             * */
            username = getIntent().getStringExtra("Display Name");
        }
//        String welcomeBackPal = String.format(getResources().getString(R.string.welcome_back_user), username);
        //for now, we're just doing a shared prefs thing
        String welcomeBackPal = String.format(getResources().getString(R.string.welcome_back_user),
                getSharedPreferences("prefs", MODE_PRIVATE).getString("display name", "pal"));
        navWelcome.setText(welcomeBackPal);
        Log.d(TAG, "initViews: nav header welcome message with mDisplayName is: "+ welcomeBackPal);



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


    /**
     * menu stuff
     */
    @Override
    public void onBackPressed() {
        Fragment newFrag = getSupportFragmentManager().findFragmentByTag(NEW_FRAG_TAG);
        Fragment recyclerFrag = getSupportFragmentManager().findFragmentByTag(RECYCLERVIEW_FRAG_TAG);
        Fragment viewFrag = getSupportFragmentManager().findFragmentByTag(VIEW_FRAG_TAG);

        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else if (newFrag != null && newFrag.isVisible()) {
            detachFragment(this, NEW_FRAG_TAG, newFrag.getView());
        } else if (recyclerFrag != null && recyclerFrag.isVisible()) {
            detachFragment(this, RECYCLERVIEW_FRAG_TAG, recyclerFrag.getView());
        } else if (viewFrag != null && viewFrag.isVisible()) {
            detachFragment(this, VIEW_FRAG_TAG, viewFrag.getView());
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
            launchAddWidgetTextDialog();
//                setupViewFrag(this, EMERGENCY_PEPTALK, null, null, null);
            //the above method isn't good yet, come back to it later
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

        switch (item.getItemId()) {
            case R.id.nav_peptalks:
                startActivity(new Intent(MainActivity.this, PepTalkListActivity.class));
                break;
            case R.id.nav_checklist:
                startActivity(new Intent(MainActivity.this, ChecklistActivity.class));
                break;
            case R.id.nav_resources:
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
                break;
            case R.id.nav_instructions:
                launchBottomSheetFromNav();
                mBottomSheetHeading.setText(R.string.instuctions_heading);
                mBottomSheetTopText.setText(R.string.instructions_text);
                mResource1.setVisibility(View.INVISIBLE);
                mResource2.setVisibility(View.INVISIBLE);
                mResource3.setVisibility(View.INVISIBLE);
                mResource4.setVisibility(View.INVISIBLE);
                break;

            case R.id.nav_about:
                launchBottomSheetFromNav();
                mBottomSheetHeading.setText(R.string.about_heading);
                mBottomSheetTopText.setText(R.string.about_text);
                mResource1.setVisibility(View.INVISIBLE);
                mResource2.setVisibility(View.INVISIBLE);
                mResource3.setVisibility(View.INVISIBLE);
                mResource4.setVisibility(View.INVISIBLE);
                break;
            case R.id.nav_logout:
                logoutUser();
                break;
            case R.id.nav_delete_account:
                deleteAccount();
                break;

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
        emailLauncher.setType(getString(R.string.default_email_type));
        startActivity(emailLauncher);
    }

    public void launchFacebook() {
        boolean installed = appInstalledOrNot(getString(R.string.facebook_package_name));
        if (installed) {
            Intent launchFb = getPackageManager().getLaunchIntentForPackage(getString(R.string.facebook_package_name));
            startActivity(launchFb);
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout_main_activity),
                    R.string.facebook_not_installed, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    public void launchDefaultSMS() {
        Intent smsLauncher = new Intent(Intent.ACTION_MAIN);
        smsLauncher.addCategory(Intent.CATEGORY_DEFAULT);
        smsLauncher.setType(getString(R.string.default_sms_type));
        startActivity(smsLauncher);
    }


    /**
     * insert content on first run
     */
    private void insertContentOnNewAccountCreated() {
        //shared prefs to make sure this only runs one time
        boolean b;
        SharedPreferences mPrefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        b = mPrefs.getBoolean("FIRST_RUN", false);
        if (!b) {
            if (mAuth.getCurrentUser() != null) {
                writeNewChecklist(getString(R.string.checklist_water), getString(R.string.checklist_water_notes), this, true); //TODO CHECK IF USING this AS CONTEXT MAKES IT NOT WORK???
                writeNewChecklist(getString(R.string.checklist_eat), getString(R.string.checklist_eat_notes), this, true);
                writeNewChecklist(getString(R.string.checklist_move), getString(R.string.checklist_move_notes), this, true);
                writeNewChecklist(getString(R.string.checklist_moment), getString(R.string.checklist_moment_notes), this, true);
                writeNewChecklist(getString(R.string.checklist_breathe), getString(R.string.checklist_breathe_notes), this, true);
                writeNewChecklist(getString(R.string.checklist_locations), getString(R.string.checklist_locations_notes), this, true);

                writeNewPepTalk(getString(R.string.exercise_guilt_title), getString(R.string.exercise_guilt), this, true);
                writeNewPepTalk(getString(R.string.pep_facts_emotions_title), getString(R.string.pep_facts_emotions), this, true);
                writeNewPepTalk(getString(R.string.doing_and_not_doing_title), getString(R.string.doing_and_not_doing), this, true);
                writeNewPepTalk(getString(R.string.pep_past_present_title), getString(R.string.pep_past_present), this, true);
                writeNewPepTalk(getString(R.string.live_in_the_moment_title), getString(R.string.live_in_the_moment), this, true);
                writeNewPepTalk(getString(R.string.pep_do_your_best_title), getString(R.string.pep_do_your_best), this, true);
            }

            mPrefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean("FIRST_RUN", true);
            editor.apply();
        }
    }

    /**
     * can't produce plays with internettie
     */
    public void checkNetworkStatus() {
        //get ref to con man
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        //if not connected, show dialog
        if (!isConnected) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.no_network_connection_detected);
            builder.setMessage(R.string.no_network_dialog_message);
            builder.setPositiveButton(R.string.check_network, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (isConnected) {
                        Snackbar snackbar = Snackbar.make(getCurrentFocus(), R.string.connection_restored, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        dialogInterface.dismiss();
                    } else {
                        if (getCurrentFocus() != null) {
                            Snackbar snackbar = Snackbar.make(getCurrentFocus(), R.string.no_luck, Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        dialogInterface.dismiss();
                    }
                }
            });
            builder.setNegativeButton(R.string.k, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();
        }
    }


    /**
     * the beautiful switch statement
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fablet_checklist://new checklist button
                setupNewFrag(FragmentMethods.CHECKLIST_OBJ, this);
                mFrameLayout.getBackground().setAlpha(0);
                mFrameLayout.setOnTouchListener(null);
                mFabMenu.collapse();
                break;
            case R.id.fablet_peptalk://new peptalk button
                setupNewFrag(FragmentMethods.PEPTALK_OBJ, MainActivity.this);
                mFrameLayout.getBackground().setAlpha(0);
                mFrameLayout.setOnTouchListener(null);
                mFabMenu.collapse();
                break;
            case R.id.textview_main://chat bubble drawable
                setupRecyclerFrag(R.id.textview_main, this);
                break;
            case R.id.navheader_profile:
                //TODO this will be where the user accesses profile stuffs
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
/*              Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                intent.putExtra("url", "https://www.headspace.com/");
                intent.putExtra("title", "Headspace");
                startActivity(intent);
*/
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
                break;
            case R.id.tv_resource3:
                Intent intent3 = new Intent(MainActivity.this, WebViewActivity.class);
                intent3.putExtra("url", "http://self-compassion.org/");
                intent3.putExtra("title", "Self-Compassion");
                startActivity(intent3);

                break;
            case R.id.tv_resource4:
/*                Intent intent4 = new Intent(MainActivity.this, WebViewActivity.class);
                intent4.putExtra("url", "https://ottawamindfulnessclinic.com");
                intent4.putExtra("title", "Ottawa Mindfulness Clinic");
                startActivity(intent4);
*/
                //launching in browser because https doesnt load safely in WebView
                Uri uri4 = Uri.parse("https://ottawamindfulnessclinic.com");
                Intent intent4 = new Intent(Intent.ACTION_VIEW, uri4);
                startActivity(intent4);
                break;
            case R.id.imagebutton_bottomsheet_down://hide bottom sheet
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;
        }

    }

    public void launchAddWidgetTextDialog() {
        //decided to just add text straight to this because
        //grabbing the text from firebase has proved more complicated than expected
        //but i will come back to it

        //get text, save to shared prefs
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


    public void logoutUser() {
        String displayName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        Log.d(TAG, "logoutUser: " + displayName);

        if (mAuth.getCurrentUser() != null) {
            mAuth.signOut();
            Toast.makeText(MainActivity.this, "adios, " + mDisplayName + "!", Toast.LENGTH_SHORT).show();

//            Toast.makeText(MainActivity.this, "adios, " + displayName + "!", Toast.LENGTH_SHORT).show();
//                Snackbar snackbar = Snackbar.make(item.getActionView().findViewById(R.id.coordinator_layout_main_activity), "See ya later", Snackbar.LENGTH_LONG);
//                snackbar.show();
            finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

        } else {
            Toast.makeText(MainActivity.this, "you shouldnt be seeing this, because logout shouldn't be visible if you're not logged in!", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteAccount(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
/*

        //first re-authenticate the user
        AuthCredential credential = EmailAuthProvider
                .getCredential("user@example.com", "password1234");

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated.");
                    }
                });

*/
        final ProgressDialog prog = new ProgressDialog(this);
        prog.setMessage("Deleting...");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure you want to delete your account?")
                .setMessage("This action cannot be undone, plus we'll miss you")
                .setNegativeButton("nvm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nada
                    }
                })
                .setPositiveButton("yup", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prog.show();
                        if(user!=null){

                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User account deleted.");
                                                finish();
                                                prog.dismiss();
                                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                                Toast.makeText(getApplicationContext(),
                                                        "Good luck in all your future endeavors!", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Log.d(TAG, "User account not deleted, error: "+ task.getException().getMessage());
                                                Toast.makeText(getApplicationContext(), "Something went wrong... account not deleted\nsign out and back in, then try again", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();

    }


    /**
     * these methods are for callbacks from the frags
     * because the fab wasn't hiding at the right time
     * and this is not the best way to handle this
     * so i will come back to it
     */
    @Override
    public void hideFabFromNewFrag() {

    }

    @Override
    public void putFabBackFromNewFrag() {

    }

    @Override
    public void hideFabFromViewFrag() {

    }

    @Override
    public void putFabBackFromViewFrag() {

    }
}



